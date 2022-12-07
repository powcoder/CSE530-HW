https://powcoder.com
代写代考加微信 powcoder
Assignment Project Exam Help
Add WeChat powcoder
https://powcoder.com
代写代考加微信 powcoder
Assignment Project Exam Help
Add WeChat powcoder
package hw4;

import java.io.*;
import java.util.*;

import hw1.Database;
import hw1.HeapPage;
import hw1.Tuple;

/**
 * BufferPool manages the reading and writing of pages into memory from
 * disk. Access methods call into it to retrieve pages, and it fetches
 * pages from the appropriate location.
 * <p>
 * The BufferPool is also responsible for locking;  when a transaction fetches
 * a page, BufferPool which check that the transaction has the appropriate
 * locks to read/write the page.
 */
public class BufferPool {
    /** Bytes per page, including header. */
    public static final int PAGE_SIZE = 4096;

    /** Default number of pages passed to the constructor. This is used by
     other classes. BufferPool should use the numPages argument to the
     constructor instead. */
    public static final int DEFAULT_PAGES = 50;

    int bufferSize;

    HashMap<Integer, HeapPage> bufferedPgs = new HashMap<>();

    HashMap<Integer, Set<Integer>> tid2pg = new HashMap<>();

    /**
     * Creates a BufferPool that caches up to numPages pages.
     *
     * @param numPages maximum number of pages in this buffer pool.
     */
    public BufferPool(int numPages) {
        // your code here
        this.bufferSize = numPages;
    }

    private void waitUntilNOLock(HeapPage hp)
    {
        while (true)
        {
            if (!hp.hasLock())
            {
                return;
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void waitUntilNOWriteLock(HeapPage hp)
    {
        while (true)
        {
            if (!hp.hasWriteLock())
            {
                return;
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Retrieve the specified page with the associated permissions.
     * Will acquire a lock and may block if that lock is held by another
     * transaction.
     * <p>
     * The retrieved page should be looked up in the buffer pool.  If it
     * is present, it should be returned.  If it is not present, it should
     * be added to the buffer pool and returned.  If there is insufficient
     * space in the buffer pool, an page should be evicted and the new page
     * should be added in its place.
     *
     * @param tid the ID of the transaction requesting the page
     * @param tableId the ID of the table with the requested page
     * @param pid the ID of the requested page
     * @param perm the requested permissions on the page
     */
    public HeapPage getPage(int tid, int tableId, int pid, Permissions perm)
            throws Exception {
        // your code here

        if(!tid2pg.containsKey(tid)){
            tid2pg.put(tid, new HashSet<>());
        }



        HeapPage hp = bufferedPgs.get(pid);

        if (hp == null)
        {

            if (bufferedPgs.size() >= bufferSize)
            {
                evictPage();
            }


            hp = Database.getCatalog().getDbFile(tableId).readPage(pid);

            bufferedPgs.put(pid, hp);
        }



        if (perm.equals(Permissions.READ_WRITE))
        {

            if (! hp.hasReadLock(tid))
            {
                waitUntilNOLock(hp);

                hp.giveReadWrite(tid);
            }

        }else if(perm.equals(Permissions.READ_ONLY)){

            if(! hp.hasReadLock()) {

                waitUntilNOWriteLock(hp);

                hp.giveReadLock(tid);
            }
        }

        tid2pg.get(tid).add(pid);

        return hp;

    }

    /**
     * Releases the lock on a page.
     * Calling this is very risky, and may result in wrong behavior. Think hard
     * about who needs to call this and why, and why they can run the risk of
     * calling it.
     *
     * @param tid the ID of the transaction requesting the unlock
     * @param tableID the ID of the table containing the page to unlock
     * @param pid the ID of the page to unlock
     */
    public  void releasePage(int tid, int tableId, int pid) {
        // your code here

        this.bufferedPgs.get(pid).removeReadLock(tid);
        this.bufferedPgs.get(pid).removeWriteLock(tid);
    }

    /** Return true if the specified transaction has a lock on the specified page */
    public   boolean holdsLock(int tid, int tableId, int pid) {
        // your code here
        return bufferedPgs.containsKey(pid) && bufferedPgs.get(pid).hasLock(tid);
    }

    /**
     * Commit or abort a given transaction; release all locks associated to
     * the transaction. If the transaction wishes to commit, write
     *
     * @param tid the ID of the transaction requesting the unlock
     * @param commit a flag indicating whether we should commit or abort
     */
    public   void transactionComplete(int tid, boolean commit)
            throws IOException {
        // your code here

        for (Integer pid: this.tid2pg.get(tid))
        {
            int tableId = this.bufferedPgs.get(pid).getTableId();
            this.releasePage(tid, tableId, pid);

            if(this.bufferedPgs.get(pid).isDirty()){

                if(commit)
                {
                    this.flushPage(tableId, pid);

                }else{

                    bufferedPgs.put(pid, Database.getCatalog().getDbFile(tableId).readPage(pid));
                }
            }
        }
    }

    /**
     * Add a tuple to the specified table behalf of transaction tid.  Will
     * acquire a write lock on the page the tuple is added to. May block if the lock cannot
     * be acquired.
     *
     * Marks any pages that were dirtied by the operation as dirty
     *
     * @param tid the transaction adding the tuple
     * @param tableId the table to add the tuple to
     * @param t the tuple to add
     */
    public  void insertTuple(int tid, int tableId, Tuple t)
            throws Exception {
        // your code here


        HeapPage hp = this.bufferedPgs.get( t.getPid());
        if( hp != null && hp.hasWriteLock(tid)) {
            hp.addTuple(t);
            hp.setDirty(true);
        }
    }

    /**
     * Remove the specified tuple from the buffer pool.
     * Will acquire a write lock on the page the tuple is removed from. May block if
     * the lock cannot be acquired.
     *
     * Marks any pages that were dirtied by the operation as dirty.
     *
     * @param tid the transaction adding the tuple.
     * @param tableId the ID of the table that contains the tuple to be deleted
     * @param t the tuple to add
     */
    public  void deleteTuple(int tid, int tableId, Tuple t)
            throws Exception {
        // your code here

        HeapPage hp = this.bufferedPgs.get( t.getPid());
        if( hp != null && hp.hasWriteLock(tid)) {
            hp.deleteTuple(t);
            hp.setDirty(true);
        }
    }

    private synchronized  void flushPage(int tableId, int pid) throws IOException {
        // your code here

        HeapPage pg = bufferedPgs.get(pid);
        pg.setDirty(false);
        Database.getCatalog().getDbFile(tableId).writePage(pg);
    }

    /**
     * Discards a page from the buffer pool.
     * Flushes the page to disk to ensure dirty pages are updated on disk.
     */
    private synchronized  void evictPage() throws Exception {
        // your code here

        for(int pid: bufferedPgs.keySet()){

            if(!bufferedPgs.get(pid).isDirty()){

                bufferedPgs.remove(pid);
                return;
            }
        }

        throw new Exception();
    }

}
