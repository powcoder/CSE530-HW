https://powcoder.com
代写代考加微信 powcoder
Assignment Project Exam Help
Add WeChat powcoder
https://powcoder.com
代写代考加微信 powcoder
Assignment Project Exam Help
Add WeChat powcoder
package hw1;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a tuple that will contain a single row's worth of information
 * from a table. It also includes information about where it is stored
 *
 * @author Sam Madden modified by Doug Shook
 */
public class Tuple {

    private Map<String, Field> value = new HashMap<>();

    private TupleDesc tupleDesc;

    private int pid;

    private int id;

    /**
     * Creates a new tuple with the given description
     *
     * @param t the schema for this tuple
     */
    public Tuple(TupleDesc t) {
        //your code here

        tupleDesc = t;
    }

    public TupleDesc getDesc() {
        //your code here
        return tupleDesc;
    }

    /**
     * retrieves the page id where this tuple is stored
     *
     * @return the page id of this tuple
     */
    public int getPid() {
        //your code here
        return pid;
    }

    public void setPid(int pid) {
        //your code here
        this.pid = pid;
    }

    /**
     * retrieves the tuple (slot) id of this tuple
     *
     * @return the slot where this tuple is stored
     */
    public int getId() {
        //your code here
        return id;
    }

    public void setId(int id) {
        //your code here
        this.id = id;
    }

    public void setDesc(TupleDesc td) {
        //your code here;
        tupleDesc = td;
    }

    /**
     * Stores the given data at the i-th field
     *
     * @param i the field number to store the data
     * @param v the data
     */
    public void setField(int i, Field v) {
        //your code here
        this.value.put(tupleDesc.getFieldName(i), v);
    }

    public Field getField(int i) {
        //your code here
        return this.value.get(tupleDesc.getFieldName(i));
    }

    /**
     * Creates a string representation of this tuple that displays its contents.
     * You should convert the binary data into a readable format (i.e. display the ints in base-10 and convert
     * the String columns to readable text).
     */
    public String toString() {

        String s = "";

        for (int i = 0; i < tupleDesc.numFields(); i++) {

            s = s + getField(i) + " ";
        }

        return s;
    }

    @Override
    public boolean equals(Object o) {


//        System.out.println("test");

        if (this == o) return true;
        if (!(o instanceof Tuple)) return false;

        Tuple tuple = (Tuple) o;

//        if (tupleDesc.equals(tuple.tupleDesc)) {
//
//            return true;
//        }else{
//
//            return false;
//        }

//        System.out.println(value);
//
////        return true;
//
//        System.out.println(tuple.value);
        if (value != null ? !value.equals(tuple.value) : tuple.value != null) return false;

//        System.out.println("good");
        return tupleDesc != null ? tupleDesc.equals(tuple.tupleDesc) : tuple.tupleDesc == null;
    }

}
