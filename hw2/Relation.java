https://powcoder.com
代写代考加微信 powcoder
Assignment Project Exam Help
Add WeChat powcoder
https://powcoder.com
代写代考加微信 powcoder
Assignment Project Exam Help
Add WeChat powcoder
package hw2;

import java.util.ArrayList;

import hw1.*;

/**
 * This class provides methods to perform relational algebra operations. It will be used
 * to implement SQL queries.
 * @author Doug Shook
 *
 */
public class Relation {

	private ArrayList<Tuple> tuples;
	private TupleDesc td;
	
	public Relation(ArrayList<Tuple> l, TupleDesc td) {
		//your code here

        tuples = l;

        this.td = td;

	}
	
	/**
	 * This method performs a select operation on a relation
	 * @param field number (refer to TupleDesc) of the field to be compared, left side of comparison
	 * @param op the comparison operator
	 * @param operand a constant to be compared against the given column
	 * @return
	 */
	public Relation select(int field, RelationalOperator op, Field operand) {

        ArrayList<Tuple> res = new ArrayList<>();

//        System.out.println(op);
//        System.out.println(operand);
//        System.out.println(tuples.size());
        for (Tuple tuple : tuples) {

//            System.out.println(tuple.getField(field));

            if(tuple.getField(field).compare(op, operand))
            {
                res.add(tuple);
            }


        }

		//your code here
		return new Relation(res, td);
	}

	private void copyField(Tuple src, Tuple des)
    {
        des.setId(src.getId());

        des.setPid(src.getPid());

        for (int i=0; i<src.getDesc().numFields(); i++) {

            des.setField(i, src.getField(i));
        }
    }

    private void copyField(Tuple src, Tuple des, ArrayList<Integer> fields)
    {
        des.setId(src.getId());

        des.setPid(src.getPid());


        for (int i=0; i<fields.size(); i++) {

            des.setField(i, src.getField(fields.get(i)));
        }
    }
	
	/**
	 * This method performs a rename operation on a relation
	 * @param fields the field numbers (refer to TupleDesc) of the fields to be renamed
	 * @param names a list of new names. The order of these names is the same as the order of field numbers in the field list
	 * @return
	 */
	public Relation rename(ArrayList<Integer> fields, ArrayList<String> names) {
		//your code here

        String[] fieldAr = new String[td.numFields()];
        Type[] types = new Type[td.numFields()];

        for (int i=0; i<td.numFields(); i++) {

            fieldAr[i] = td.getFieldName(i);
            types[i] = td.getType(i);
        }

        for (int i=0; i<fields.size(); i++) {

            fieldAr[fields.get(i)] = names.get(i);

        }

        TupleDesc tupleDesc = new TupleDesc(types, fieldAr);

        ArrayList<Tuple> res = new ArrayList<>();

        for (Tuple tuple : tuples) {

            Tuple newTuple = new Tuple(tupleDesc);

            copyField(tuple, newTuple);

            res.add(newTuple);
        }


		return new Relation(res, tupleDesc);
	}
	
	/**
	 * This method performs a project operation on a relation
	 * @param fields a list of field numbers (refer to TupleDesc) that should be in the result
	 * @return
	 */
	public Relation project(ArrayList<Integer> fields) {
		//your code here

        String[] fieldAr = new String[fields.size()];
        Type[] types = new Type[fields.size()];

        for (int i=0; i<fields.size(); i++) {

            types[i] = td.getType(fields.get(i));

            fieldAr[i] = td.getFieldName(fields.get(i));
        }

        TupleDesc tupleDesc = new TupleDesc(types, fieldAr);

        ArrayList<Tuple> res = new ArrayList<>();

        for (Tuple tuple : tuples) {

            Tuple newTuple = new Tuple(tupleDesc);

            copyField(tuple, newTuple, fields);

            res.add(newTuple);
        }


        return new Relation(res, tupleDesc);
	}
	
	/**
	 * This method performs a join between this relation and a second relation.
	 * The resulting relation will contain all of the columns from both of the given relations,
	 * joined using the equality operator (=)
	 * @param other the relation to be joined
	 * @param field1 the field number (refer to TupleDesc) from this relation to be used in the join condition
	 * @param field2 the field number (refer to TupleDesc) from other to be used in the join condition
	 * @return
	 */
	public Relation join(Relation other, int field1, int field2) {
		//your code here

        int totalFields = td.numFields() + other.getDesc().numFields();

        String[] fieldAr = new String[totalFields];
        Type[] types = new Type[totalFields];

        for (int i = 0; i < td.numFields(); i++) {

            fieldAr[i] = td.getFieldName(i);

            types[i] = td.getType(i);
        }

        for (int i = 0; i < other.getDesc().numFields() ; i++) {

            fieldAr[td.numFields() + i] = other.getDesc().getFieldName(i);

            types[td.numFields() + i] = other.getDesc().getType(i);
        }


        TupleDesc tupleDesc = new TupleDesc(types, fieldAr);

        ArrayList<Tuple> res = new ArrayList<>();

        for (Tuple tuple : tuples) {

            for (Tuple ot : other.getTuples()) {

                if (tuple.getField(field1).compare(RelationalOperator.EQ, ot.getField(field2)))
                {
                    Tuple newTuple = new Tuple(tupleDesc);

                    for (int i = 0; i < td.numFields(); i++) {

                        newTuple.setField(i, tuple.getField(i));
                    }

                    for (int i = 0; i < other.getDesc().numFields() ; i++) {


                        newTuple.setField(td.numFields() + i, ot.getField(i));
                    }

                    res.add(newTuple);
                }
            }


        }


        return new Relation(res, tupleDesc);


	}
	
	/**
	 * Performs an aggregation operation on a relation. See the lab write up for details.
	 * @param op the aggregation operation to be performed
	 * @param groupBy whether or not a grouping should be performed
	 * @return
	 */
	public Relation aggregate(AggregateOperator op, boolean groupBy) {
		//your code here

        Aggregator aggregator = new Aggregator(op, groupBy, td);

        for (Tuple tuple : tuples) {

            aggregator.merge(tuple);
        }

        ArrayList<Tuple> res = aggregator.getResults();

        return new Relation(res, res.get(0).getDesc());

    }
	
	public TupleDesc getDesc() {
		//your code here
		return td;
	}
	
	public ArrayList<Tuple> getTuples() {
		//your code here
		return tuples;
	}
	
	/**
	 * Returns a string representation of this relation. The string representation should
	 * first contain the TupleDesc, followed by each of the tuples in this relation
	 */
	public String toString() {
		//your code here

        String res = td + "\n";

        for (Tuple tuple : tuples) {

            res = res + tuple + "\n";
        }

        return res;
    }
}
