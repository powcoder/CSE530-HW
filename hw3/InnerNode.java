https://powcoder.com
代写代考加微信 powcoder
Assignment Project Exam Help
Add WeChat powcoder
https://powcoder.com
代写代考加微信 powcoder
Assignment Project Exam Help
Add WeChat powcoder
package hw3;

import java.util.ArrayList;

import hw1.Field;
import hw1.RelationalOperator;

public class InnerNode implements Node {


    private ArrayList<Field> keys;

    private ArrayList<Node> children;

    private InnerNode parent;

    private int degree;

    public InnerNode(int degree) {

        keys = new ArrayList<>(degree);

        children = new ArrayList<>(degree + 1);

        parent = null;

        this.degree = degree;
    }

    public InnerNode(ArrayList<Field> keys, ArrayList<Node> children, int degree) {
        this.keys = keys;
        this.children = children;

        for (Node node : children) {

            node.setParent(this);
        }

        this.degree = degree;
    }

    public ArrayList<Field> getKeys() {
        //your code here
        return keys;
    }

    public ArrayList<Node> getChildren() {
        //your code here
        return children;
    }

    public int getDegree() {
        //your code here
        return degree;
    }

    public boolean isLeafNode() {

        return false;
    }

    @Override
    public Node getParent() {
        return parent;
    }

    @Override
    public Field getLargest() {
        return keys.get(keys.size() - 1);
    }

    @Override
    public void setParent(InnerNode parent) {
        this.parent = parent;

    }


    public Node findChild(Field key) {
        int i;
        for (i = 0; i < keys.size(); i++) {
            if (key.compare(RelationalOperator.LTE, keys.get(i))) {
                break;
            }
        }

        return children.get(i);
    }

//    int findChildIndex(Node node)
//    {
//        for (int i=0; i<children.size(); i++) {
//
//            if (children.get(i) == node) {
//
//                return i;
//            }
//        }
//
//
//
//
//    }

    public void update(Node old, Node nl, Node nr) {
        int ind = children.indexOf(old);

        if (ind == -1) {
//            System.out.println("no children");
            System.out.println(old);

            for (Node node : children) {

                System.out.println(node);
            }
        }

//        System.out.println(ind);

        Field nk = nl.getLargest();

        keys.add(ind, nk);

        children.set(ind, nl);

        children.add(ind + 1, nr);


        if (keys.size() > degree) {


            int m = degree / 2;

            ArrayList<Field> lk = new ArrayList<>(keys.subList(0, m + 1));

            ArrayList<Field> rk = new ArrayList<>(keys.subList(m + 1, keys.size()));

            ArrayList<Node> lc = new ArrayList<>(children.subList(0, m + 1));

            ArrayList<Node> rc = new ArrayList<>(children.subList(m + 1, children.size()));

            InnerNode ln = new InnerNode(lk, lc, degree);

            InnerNode rn = new InnerNode(rk, rc, degree);

            ln.parent = parent;

            rn.parent = parent;


            if (parent != null) {
                parent.update(this, ln, rn);


            } else {

                parent = new InnerNode(degree);

                parent.getKeys().add(ln.getLargest());

                parent.addChild(ln);

                parent.addChild(rn);

            }

            ln.keys.remove(ln.keys.size() - 1);
        }


    }

    public void addChild(Node node) {
        children.add(node);


        node.setParent(this);
    }

    public void updateKey(int ind, Field k) {
        if (ind < keys.size()) {
            keys.set(ind, k);


        } else {


            if (parent != null) {
                parent.updateKey(parent.getChildren().indexOf(this), k);
            }

        }

    }

    @Override
    public String toString() {
        return "InnerNode{" +
                "keys=" + keys +
                ", children=" + children +
                '}';
    }
}