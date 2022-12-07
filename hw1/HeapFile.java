https://powcoder.com
代写代考加微信 powcoder
Assignment Project Exam Help
Add WeChat powcoder
https://powcoder.com
代写代考加微信 powcoder
Assignment Project Exam Help
Add WeChat powcoder
package hw1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A heap file stores a collection of tuples. It is also responsible for managing pages.
 * It needs to be able to manage page creation as well as correctly manipulating pages
 * when tuples are added or deleted.
 * @author Sam Madden modified by Doug Shook
 *
 */
public class HeapFile {
	
	public static final int PAGE_SIZE = 4096;

	private File file;
	private TupleDesc tupleDesc;

	/**
	 * Creates a new heap file in the given location that can accept tuples of the given type
	 * @param f location of the heap file
	 * @param types type of tuples contained in the file
	 */
	public HeapFile(File f, TupleDesc type) {
		//your code here

        this.file = f;
        this.tupleDesc = type;
	}
	
	public File getFile() {
		//your code here
		return file;
	}
	
	public TupleDesc getTupleDesc() {
		//your code here
		return tupleDesc;
	}
	
	/**
	 * Creates a HeapPage object representing the page at the given page number.
	 * Because it will be necessary to arbitrarily move around the file, a RandomAccessFile object
	 * should be used here.
	 * @param id the page number to be retrieved
	 * @return a HeapPage at the given page number
	 */
	public HeapPage readPage(int id) {
		//your code here

        try {

            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            randomAccessFile.seek(PAGE_SIZE * id);

            byte[] page = new byte[PAGE_SIZE];

            randomAccessFile.read(page, 0, PAGE_SIZE);

            randomAccessFile.close();

            return new HeapPage(id, page, getId());

        } catch (IOException e) {

            e.printStackTrace();

            return null;
        }



    }
	
	/**
	 * Returns a unique id number for this heap file. Consider using
	 * the hash of the File itself.
	 * @return
	 */
	public int getId() {
		//your code here
		return hashCode();
	}
	
	/**
	 * Writes the given HeapPage to disk. Because of the need to seek through the file,
	 * a RandomAccessFile object should be used in this method.
	 * @param p the page to write to disk
	 */
	public void writePage(HeapPage p) {

        try {

            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(PAGE_SIZE * p.getId());

            byte[] page = new byte[PAGE_SIZE];

            randomAccessFile.write(p.getPageData(), 0, PAGE_SIZE);

            randomAccessFile.close();

        } catch (IOException e) {

            e.printStackTrace();

        }
	}
	
	/**
	 * Adds a tuple. This method must first find a page with an open slot, creating a new page
	 * if all others are full. It then passes the tuple to this page to be stored. It then writes
	 * the page to disk (see writePage)
	 * @param t The tuple to be stored
	 * @return The HeapPage that contains the tuple
	 */
	public HeapPage addTuple(Tuple t) {
		//your code here

        for (int i=0; i<getNumPages(); i++) {

            HeapPage heapPage = readPage(i);

            try {
                heapPage.addTuple(t);

                writePage(heapPage);

                return heapPage;

            } catch (Exception e) {

            }

        }



        try {

            HeapPage heapPage = new HeapPage(getNumPages(), new byte[PAGE_SIZE], getId());

            heapPage.addTuple(t);

            writePage(heapPage);

            return heapPage;


        } catch (Exception e) {

            return null;
        }


    }


	
	/**
	 * This method will examine the tuple to find out where it is stored, then delete it
	 * from the proper HeapPage. It then writes the modified page to disk.
	 * @param t the Tuple to be deleted
	 */
	public void deleteTuple(Tuple t){
		//your code here

        HeapPage heapPage = readPage(t.getPid());

        try {
            heapPage.deleteTuple(t);

            writePage(heapPage);

        } catch (Exception e) {

            e.printStackTrace();
        }

    }
	
	/**
	 * Returns an ArrayList containing all of the tuples in this HeapFile. It must
	 * access each HeapPage to do this (see iterator() in HeapPage)
	 * @return
	 */
	public ArrayList<Tuple> getAllTuples() {
		//your code here

        ArrayList<Tuple> tuples = new ArrayList<>();

        for (int i=0; i<getNumPages(); i++) {

            for (Iterator<Tuple> it = readPage(i).iterator(); it.hasNext(); ) {
                Tuple tuple = it.next();

                tuples.add(tuple);
            }

        }

        return tuples;
    }
	
	/**
	 * Computes and returns the total number of pages contained in this HeapFile
	 * @return the number of pages
	 */
	public int getNumPages() {
		//your code here

        return (int) (file.length() / PAGE_SIZE);
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HeapFile)) return false;

        HeapFile heapFile = (HeapFile) o;

        if (file != null ? !file.equals(heapFile.file) : heapFile.file != null) return false;
        return tupleDesc != null ? tupleDesc.equals(heapFile.tupleDesc) : heapFile.tupleDesc == null;
    }

    @Override
    public int hashCode() {
        int result = file != null ? file.hashCode() : 0;
        result = 31 * result + (tupleDesc != null ? tupleDesc.hashCode() : 0);
        return result;
    }
}
