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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import hw1.*;

/**
 * A class to perform various aggregations, by accepting one tuple at a time
 * @author Doug Shook
 *
 */
public class Aggregator {

    AggregateOperator operator;
    boolean groupBy;
    TupleDesc tupleDesc;

    Field result;

    Map<Field, Field> groupResult = new HashMap<>();

	public Aggregator(AggregateOperator o, boolean groupBy, TupleDesc td) {
		//your code here

        operator = o;
        this.groupBy = groupBy;
        tupleDesc = td;

	}

	/**
	 * Merges the given tuple into the current aggregation
	 * @param t the tuple to be aggregated
	 */
	public void merge(Tuple t) {
		//your code here

        if (groupBy) {

            switch (operator) {
                case SUM:
                case AVG:
                    if (groupResult.containsKey(t.getField(0))) {

                        groupResult.put(t.getField(0), new IntField(((IntField) (groupResult.get(t.getField(0)))).getValue() + ((IntField) t.getField(1)).getValue()));

                    }else{

                        groupResult.put(t.getField(0), t.getField(1
                        ));
                    }
                    break;

                case MIN:
                case MAX:
                    if (groupResult.containsKey(t.getField(0))) {

                        if(t.getField(1).compare(operator == AggregateOperator.MIN ? RelationalOperator.LT : RelationalOperator.GT, groupResult.get(t.getField(0))))
                        {
                            groupResult.put(t.getField(0), t.getField(1));
                        }

                    }else{

                        groupResult.put(t.getField(0), t.getField(1));
                    }
                    break;

                case COUNT:
                    if (groupResult.containsKey(t.getField(0))) {

                        groupResult.put(t.getField(0), new IntField(((IntField)groupResult.get(t.getField(0))).getValue()+1));

                    }else{

                        groupResult.put(t.getField(0), new IntField(1));

                    }
                    break;


            }

        }else{

            switch (operator)
            {
                case SUM:
                case AVG:
                    if (result == null)
                    {
                        result = t.getField(0);
                    }else{

                        result = new IntField( ((IntField)result).getValue() + ((IntField)t.getField(0)).getValue());
                    }

                    break;

                case MIN:
                case MAX:

                    if (result == null)
                    {
                        result = t.getField(0);
                    }else{

                        if (t.getField(0).compare(operator == AggregateOperator.MIN ? RelationalOperator.LT : RelationalOperator.GT, result)) {

                            result = t.getField(0);
                        }
                    }

                case COUNT:

                    if (result == null)
                    {
                        result = new IntField(1);
                    }else{

                        result = new IntField (((IntField)result).getValue() + 1);
                    }

            }

        }

	}
	
	/**
	 * Returns the result of the aggregation
	 * @return a list containing the tuples after aggregation
	 */
	public ArrayList<Tuple> getResults() {
		//your code here

        ArrayList<Tuple> res = new ArrayList<>();


        if (groupBy)
        {

            TupleDesc td = tupleDesc;

            if (operator == AggregateOperator.COUNT)
            {
                Type[] types = new Type[2];

                String[] fields = new String[2];

                types[0] = tupleDesc.getType(0);
                types[1] = Type.INT;

                fields[0] = tupleDesc.getFieldName(0);

                fields[1] = tupleDesc.getFieldName(1);

                td = new TupleDesc(types, fields);
            }


            for (Map.Entry<Field, Field> e: groupResult.entrySet()) {

                Tuple tuple = new Tuple(td);

                tuple.setField(0, e.getKey());

                tuple.setField(1, e.getValue());

                res.add(tuple);
            }
        }

        else{

            TupleDesc td = tupleDesc;

            if (operator == AggregateOperator.COUNT)
            {
                Type[] types = new Type[1];

                String[] fields = new String[1];

                types[0] = Type.INT;

                fields[0] = tupleDesc.getFieldName(0);


                td = new TupleDesc(types, fields);

                if (result == null)
                {
                    result = new IntField(0);
                }
            }

            Tuple tuple = new Tuple(td);

            tuple.setField(0, result);

            res.add(tuple);
        }


		return res;
	}

}
