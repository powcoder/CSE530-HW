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


public class BPlusTree {

    private Node root;

    public BPlusTree(int degree) {
        //your code here

        root = new LeafNode(degree);


    }

    public LeafNode search(Field f) {


        LeafNode leafNode = searchLeaf(f);

        Entry e = leafNode.findEntry(f);

        if (e == null) {

            return null;
        } else {

            return leafNode;
        }

    }

    private LeafNode searchLeaf(Field f) {

        Node curNode = root;

        while (true) {
            if (curNode.isLeafNode()) {
                break;
            }

            InnerNode inner = (InnerNode) curNode;

//            ArrayList<Node> children = inner.getChildren();
//
            curNode = inner.findChild(f);

        }

        LeafNode leafNode = (LeafNode) curNode;

        return leafNode;

    }


    public void insert(Entry e) {
        //your code here

        LeafNode leafNode = searchLeaf(e.getField());

        if (!leafNode.insert(e)) {
            return;
        } else {


            leafNode.split();

            if (root.getParent() != null) {


                root = root.getParent();
            }
        }
    }

    public void delete(Entry e) {
        //your code here

        LeafNode leafNode = searchLeaf(e.getField());

//        Entry entry = leafNode.findEntry(e.getField());

        if (leafNode != null) {

            leafNode.delete(e);
        }


    }

    public Node getRoot() {
        //your code here
        return root;
    }


}
