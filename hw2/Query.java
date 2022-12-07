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
import java.util.List;

import hw1.*;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.*;
import net.sf.jsqlparser.statement.*;
import net.sf.jsqlparser.statement.select.*;

public class Query {

    private String q;

    public Query(String q) {
        this.q = q;
    }


    private Catalog catalog = Database.getCatalog();


    public Relation execute() {


        Statement statement = null;
        try {
            statement = CCJSqlParserUtil.parse(q);
        } catch (JSQLParserException e) {
            System.out.println("Unable to parse query");
            e.printStackTrace();
        }
        Select selectStatement = (Select) statement;
        PlainSelect sb = (PlainSelect) selectStatement.getSelectBody();


//        System.out.println(sb);

        FromItem fromItem = sb.getFromItem();

//        System.out.println(fromItem);


        int tableId = catalog.getTableId(fromItem.toString());
        Relation relation = new Relation(catalog.getDbFile(tableId).getAllTuples(), catalog.getTupleDesc(tableId));


        relation = processJoin(sb, relation);

        ArrayList<Integer> selectColumnIds = new ArrayList<>();
        ArrayList<Integer> renameColumnIds = new ArrayList<>();
        ArrayList<String> renameNames = new ArrayList<>();

        for (SelectItem selectItem : sb.getSelectItems()) {

            ColumnVisitor columnVisitor = new ColumnVisitor();
            selectItem.accept(columnVisitor);

            if (columnVisitor.getColumn().equals("*")) {

                break;
            }

            if (selectItem instanceof SelectExpressionItem) {

                SelectExpressionItem selectExpressionItem = (SelectExpressionItem) selectItem;

                Alias alias = selectExpressionItem.getAlias();

                if (alias != null) {

                    renameColumnIds.add(relation.getDesc().nameToId(columnVisitor.getColumn()));
                    renameNames.add(alias.getName());
                }
            }

            if (columnVisitor.isAggregate()) {


                AggregateOperator aggregateOperator = columnVisitor.getOp();

                if (sb.getGroupByColumnReferences() == null) {

                    ArrayList<Integer> aggregateColumn = new ArrayList<>();

                    aggregateColumn.add(relation.getDesc().nameToId(columnVisitor.getColumn()));

                    relation = relation.project(aggregateColumn);

                    if (!renameColumnIds.isEmpty()) {
                        renameColumnIds = new ArrayList<>();
                        renameColumnIds.add(0);
                        relation = relation.rename(renameColumnIds, renameNames);
                    }

                    return relation.aggregate(aggregateOperator, false);

                } else {

                    if (!renameColumnIds.isEmpty()) {
                        relation = relation.rename(renameColumnIds, renameNames);
                    }

                    return relation.aggregate(aggregateOperator, true);
                }
            }
            selectColumnIds.add(relation.getDesc().nameToId(columnVisitor.getColumn()));
        }


        if (!renameColumnIds.isEmpty()) {
            relation = relation.rename(renameColumnIds, renameNames);
        }

        Expression whereExpression = sb.getWhere();

        if (whereExpression != null) {

            WhereExpressionVisitor whereExpressionVisitor = new WhereExpressionVisitor();

            whereExpression.accept(whereExpressionVisitor);

            relation = relation.select(relation.getDesc().nameToId(whereExpressionVisitor.getLeft()),
                    whereExpressionVisitor.getOp(), whereExpressionVisitor.getRight());

        }

        if (!selectColumnIds.isEmpty()) {
            relation = relation.project(selectColumnIds);
        }


        return relation;
    }

    private Relation processJoin(PlainSelect sb, Relation relation) {
        List<Join> joins = sb.getJoins();

        if (joins == null) {
            return relation;
        }

//        System.out.println(joins);


        for (Join join : joins) {

            FromItem joinRightItem = join.getRightItem();

//                System.out.println("joinRightItem");
//                System.out.println(joinRightItem);

            Expression onExpression = join.getOnExpression();

//                System.out.println("onExpression");
//                System.out.println(onExpression);

            if (onExpression != null) {

                int joinRightItemTableId = catalog.getTableId(joinRightItem.toString());

                Relation rightRelation = new Relation(
                        catalog.getDbFile(catalog.getTableId(joinRightItem.toString())).getAllTuples(),
                        catalog.getTupleDesc(joinRightItemTableId));

                String[] arr = onExpression.toString().split("=");

                relation = relation.join(rightRelation,
                        relation.getDesc().nameToId(arr[0].split("\\.")[1].trim()),
                        rightRelation.getDesc().nameToId(arr[1].split("\\.")[1].trim()));
            }
        }

        return relation;

    }

}
