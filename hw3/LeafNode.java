https://powcoder.com
代写代考加微信 powcoder
Assignment Project Exam Help
Add WeChat powcoder
https://powcoder.com
代写代考加微信 powcoder
Assignment Project Exam Help
Add WeChat powcoder
package hw3;

import hw1.Field;
import hw1.RelationalOperator;

import java.util.ArrayList;

public class LeafNode implements Node {

    private InnerNode parent;

    private int degree;
    ArrayList<Entry> entries;

    public LeafNode(int degree) {
        //your code here

        entries = new ArrayList<>(degree);

        parent = null;

        this.degree = degree;

    }

    public LeafNode(int degree, ArrayList<Entry> entries) {
        this.degree = degree;
        this.entries = entries;
    }

    public ArrayList<Entry> getEntries() {
        //your code here
        return entries;
    }

    public int getDegree() {
        //your code here
        return degree;
    }

    public boolean isLeafNode() {
        return true;
    }

    @Override
    public Node getParent() {
        return parent;
    }

    @Override
    public Field getLargest() {
        return entries.get(entries.size() - 1).getField();
    }

    @Override
    public void setParent(InnerNode parent) {
        this.parent = parent;

    }


    public Entry findEntry(Field key) {
        for (Entry entry : entries) {
            if (key.compare(RelationalOperator.EQ, entry.getField())) {

                return entry;
            }
        }

        return null;
    }

    public int findIndex(Field key) {
        int i;

        for (i = 0; i < entries.size(); i++) {
            if (key.compare(RelationalOperator.LT, entries.get(i).getField())) {

                return i;
            }

        }

        return i;
    }

    public boolean insert(Entry e) {
        if (findEntry(e.getField()) != null) {
            return false;
        }

        int ind = findIndex(e.getField());

        entries.add(ind, e);

        return true;
    }


    int getIndex() {
        return parent.getChildren().indexOf(this);
    }

    LeafNode getLeftSibling(int ind) {
        if (ind == 0) {
            return null;
        } else {
            return (LeafNode) (parent.getChildren().get(ind - 1));
        }
    }

    LeafNode getRightSibling(int ind) {
        if (ind + 1 < parent.getChildren().size()) {

            return (LeafNode) (parent.getChildren().get(ind + 1));
        } else {

            return null;
        }
    }

    public int findIndexEntry(Field key) {
        int i;

        for (i = 0; i <= entries.size(); i++) {
            if (key.compare(RelationalOperator.EQ, entries.get(i).getField())) {

                return i;
            }

        }

        return -1;
    }

    public void delete(Entry e) {

        int i = findIndexEntry(e.getField());


        if (i >= 0 && i < entries.size()) {
//            System.out.println(entries.remove(i));

            if (i == entries.size() - 1) {
                if (parent != null) {

                    if (entries.size() >= 2) {

                        parent.updateKey(getIndex(), entries.get(entries.size() - 2).getField());
                    }


                }
            }

            entries.remove(i);

            if (entries.size() < degree / 2 && parent != null) {

                int ind = getIndex();
                LeafNode left = getLeftSibling(ind);

                LeafNode right = null;

                if (left != null && left.getEntries().size() > degree / 2) {


                    entries.add(0, left.getEntries().get(left.getEntries().size() - 1));

                    left.getEntries().remove(left.getEntries().size() - 1);

                    parent.getKeys().set(ind - 1, left.getLargest());


                    return;
                } else {

                    right = getRightSibling(ind);

                    if (right != null && right.getEntries().size() > degree / 2) {

                        entries.add(right.getEntries().get(0));

                        parent.getKeys().set(ind, left.getEntries().get(0).getField());

                        right.getEntries().remove(0);

                        return;

                    }
                }

                if (left != null && left.getEntries().size() <= degree / 2) {

                    left.entries.addAll(entries);

                    parent.getKeys().set(ind, left.getLargest());

                    parent.getKeys().remove(ind - 1);

                    parent.getChildren().remove(ind);

                    return;

                } else if (right != null && right.getEntries().size() <= degree / 2) {

                    entries.addAll(right.getEntries());


                    parent.getKeys().remove(ind);

                    parent.getChildren().remove(ind + 1);

                    return;
                }

            }


        }
    }

    public boolean split() {
        if (entries.size() > degree) {

            int leftNum = (int) Math.ceil(entries.size() / 2.0);

            ArrayList<Entry> left = new ArrayList<>(entries.subList(0, leftNum));

            ArrayList<Entry> right = new ArrayList<>(entries.subList(leftNum, entries.size()));

            LeafNode leftNode = new LeafNode(degree, left);

            LeafNode rightNOde = new LeafNode(degree, right);

            leftNode.parent = parent;

            rightNOde.parent = parent;

            if (parent != null) {
                parent.update(this, leftNode, rightNOde);


            } else {

                parent = new InnerNode(degree);

                parent.getKeys().add(leftNode.getLargest());

                parent.addChild(leftNode);

                parent.addChild(rightNOde);

            }

            return true;

        } else {

            return false;
        }


    }

    @Override
    public String toString() {
        return "LeafNode{" +
                "entries=" + entries +
                '}';
    }
}