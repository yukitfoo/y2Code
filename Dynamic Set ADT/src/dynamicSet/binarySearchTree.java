/**
 * @author Yu Kit, Foo (2441458f)
 */

package dynamicSet;

/**
 * Notice that the Generic class of the tree extends comparable,
 * this is so that the key values of the node is reliably comparable regardless of
 * its class
 * @param <Item>
 */
public class binarySearchTree <Item extends Comparable<Item>> {
    private Node<Item> root; // root of BST

    private class Node<Item extends Comparable<Item>> {
        private Item key; // sorted by key
        private Node<Item> left, right, p;

        public Node(Item key) {
            this.key = key;
            this.left = null;
            this.right = null;
            this.p = null;
        }

        private Node<Item> cloneNode() {
            Node<Item> cloneNode = new Node<Item>(this.key);
            cloneNode.left = this.left;
            cloneNode.right = this.right;
            cloneNode.p = this.p;
            return cloneNode;
        }
    }

    public Node<Item> getRoot() {
        return this.root;
    }

    public binarySearchTree() {
        this.root = null;
    }

    // print= inOrder
//    used as a baseline to check that BST property is satisfied

    /**
     * Used to check the tree, if it is sorted ascendingly the BST property is maintained
     * @param x
     */
    public void print(Node<Item> x) {
        if (x != null) {
            print(x.left);
            System.out.print(x.key + ", ");
            print(x.right);
        }
    }

    /**
     * Returns the height of the tree
     * @param x
     * @return
     */
    int getHeight(Node<Item> x) {
        if (x == null)
            return -1;
        else {
            /* compute the depth of each subtree */
            int leftNodeDepth = getHeight(x.left);
            int rightNodeDepth = getHeight(x.right);

            /* use the larger one */
            if (leftNodeDepth > rightNodeDepth) return (leftNodeDepth + 1);
            else return (rightNodeDepth + 1);
        }
    }

//    insert function

    /**
     * Adds a node to the list, checks for suplicate before inserting it
     * @param z - key value to be aadded
     */
    public void add(Item z) {
//        case for when element already in tree
        if (this.isElement(z)) {
            return;
        }
//        y is trailing pointer
        Node<Item> y = null;
        Node<Item> x = this.root;
        Node<Item> newNode = new Node<Item>(z);
        while (x != null) {
            y = x;
            if (newNode.key.compareTo(x.key) < 0) x = x.left;
            else x = x.right;
        }
        newNode.p = y;
        if (y == null) this.root = newNode;
        else if (newNode.key.compareTo(y.key) < 0) y.left = newNode;
        else y.right = newNode;

    }

    /**
     * Removes a node based on the key value of the node, since the BST cannot have duplicates
     * there are only unique keys in the BST, so this implementation can be considered more flexible
     * @param z
     */
    public void remove(Item z) {
//        checks that binary tree "this" is not empty and has the element in it
        if ((!this.isElement(z))||(this.isEmpty())) return;

        Node<Item> nodeZ = findNode(z);
        if (nodeZ.left == null) {
            transplant(nodeZ, nodeZ.right);
        } else if (nodeZ.right == null) {
            transplant(nodeZ, nodeZ.left);
        } else {
            Node<Item> y = minimum(nodeZ.right);
            if (y.p != nodeZ) {
                transplant(y, y.right);
                y.right = nodeZ.right;
                y.right.p = y;
            }
            transplant(nodeZ, y);
            y.left = nodeZ.left;
            y.left.p = y;
        }
    }

    /**
     * gets the minimum value in the subtree
     * @param x - root node of the tree/subtree
     * @return
     */
    private Node<Item> minimum(Node<Item> x) {
        while (x.left != null) {
            x = x.left;
        }
        return x;
    }

    /**
     * gets the maximum value in the subtree
     * @param x - root node of the tree/subtree
     * @return
     */
    private Node<Item> maximum(Node<Item> x) {
        while (x.right != null) {
            x = x.right;
        }
        return x;
    }

    /**
     * Helper function to help find the node in the tree
     * @param k key value of the node to find
     * @return
     */
    private Node<Item> findNode(Item k) {
        Node<Item> x = this.root;
        while((x != null)&&(k != x.key)) {
            if (k.compareTo(x.key) < 0) {
                x = x.left;
            } else {
                x = x.right;
            }
        }
        return x;
    }

    /**
     * helper function for add and remove
     * transplants a node given in v to the position of node of u
     * @param u - node to be removed from the tree
     * @param v - node to be transplanted into the tree
     */
    private void transplant(Node<Item> u, Node<Item> v) {
        if (u.p == null) {
            this.root = v;
        } else if (u == u.p.left) {
            u.p.left = v;
        } else {
            u.p.right = v;
        }

        if (v!=null) {
            v.p = u.p;
        }
    }

    /**
     * checks if the tree is empty based on its root value
     * @return
     */
    public boolean isEmpty(){
        if (this.root == null) {
            return true;
        }
        return false;
    }

    public int setSize(Node<Item> x) {
        if (x == null) return 0;
        else {
            return setSize(x.left) + setSize(x.right)+1;
        }
    }

    /**
     * checks if a key valeu is an the  key of a node in the binary search tree
     * @param k
     * @return
     */
    public boolean isElement(Item k) {
        if (this.isEmpty()) return false;
        Node<Item> x = this.root;
        while ((x!=null)) {
            if (k.compareTo(x.key) == 0) return true;
            else if (k.compareTo(x.key) < 0) x = x.left;
            else x = x.right;
        }
        return false;
    }

    /**
     * Union traverses through first tree and add all nodes into a new tree
     * then traverses through second tree and add all nodes that are not in the tree
     * @param T
     * @return
     */
    public binarySearchTree<Item> union(binarySearchTree<Item> T) {
//        TODO
        binarySearchTree<Item> returnTree = new binarySearchTree<Item>();
        returnTree.traverseUnion(this.root);
        returnTree.traverseUnion(T.root);
        return returnTree;
    }

    private void traverseUnion(Node<Item> x) {
        if (x != null) {
            traverseUnion(x.left);
            this.add(x.key);
            traverseUnion(x.right);
        }
    }


    /**
     * traverses through both trees finding using a while loop and successor,
     * traversal to the next node of a specific tree is based on a consitional that
     * checks which tree has a bigger key value in its curerent node, this is to make sure that
     * all nodes in between are detected
     * @param T - tree to find intersecting nodes from
     * @return
     */
    public binarySearchTree<Item> intersection(binarySearchTree<Item> T) {
//        TODO
        binarySearchTree<Item> returnTree = new binarySearchTree<Item>();
        Node<Item> thisBST = this.minimum(this.root);
        Node<Item> otherBST = T.minimum(T.root);
        while ((thisBST != null)&&(otherBST != null)) {
            if (thisBST.key == otherBST.key) {
                returnTree.add(thisBST.key);
                thisBST = this.successor(thisBST);
                otherBST = T.successor(otherBST);
            } else {
//                case if key of current node that is pointed to of "this" is larger than T
//                get next biggest value of T
                if (thisBST.key.compareTo(otherBST.key) > 0) {
                    otherBST = T.successor(otherBST);
                } else {
                    thisBST = T.successor(thisBST);
                }
            }
        }
        return returnTree;

    }

    /**
     * gets the node of the next biggest key value
     * Complexity of O(h)
     * @param x - current node
     * @return
     */
    public Node<Item> successor(Node<Item> x) {
        if (x.right != null) {
            return minimum(x.right);
        }
        Node<Item> y = x.p;
        while ((y != null)&&(x == y.right)) {
            x = y;
            y = y.p;
        }
        return y;
    }

    /**
     * using 2 clone trees, traverse through a tree, after every traverse, compare the 2 node keys
     * if both keys are equal, remove both from the tree, if one is laarger than the other,
     * find the successor of the smaller node and compare the successor again. repeat the process again
     * * @param T
     * @return
     */
    public binarySearchTree<Item> difference(binarySearchTree<Item> T) {
//        TODO
        binarySearchTree<Item> returnTree = new binarySearchTree<Item>();
        Node<Item> thisBST = this.minimum(this.root);
        Node<Item> otherBST = T.minimum(T.root);

//        if this.key > other.key check = true
        boolean check = checkNode(thisBST, otherBST);
        while((thisBST != null) && (otherBST != null)) {
            check = checkNode(thisBST,otherBST);
            if (check) {
//                have a chance to be same
                if (thisBST.key == otherBST.key) {
                    thisBST = this.successor(thisBST);
                    otherBST = T.successor(otherBST);
                } else {
                    returnTree.add(otherBST.key);
                    otherBST = T.successor(otherBST);
                }
            } else {
//                no chance to be same
                if (thisBST.key != otherBST.key) {
                    returnTree.add(thisBST.key);
                }
                thisBST = this.successor(thisBST);
            }
        }

        if (check) {
            while(thisBST != null) {
                returnTree.add(thisBST.key);
                thisBST = this.successor(thisBST);
            }
        } else {
            while(otherBST != null) {
                returnTree.add(otherBST.key);
                otherBST = T.successor(otherBST);
            }
        }
        return returnTree;
    }


    /**
     * helper function, make it easier to compare the vclue, less repetition in code
     * @param thisNode
     * @param otherNode
     * @return
     */
    private boolean checkNode(Node<Item> thisNode, Node<Item> otherNode) {
        if (thisNode.key.compareTo(otherNode.key) >= 0) return true;
        return false;
    }


    /**
     * traverse through T and find if its an element in "this"
     * @param T - tree to find if its a subset of this
     * @return
     */
    public boolean subset(binarySearchTree<Item> T) {
//        TODO
        Node<Item> x = T.minimum(T.root);
        while (x != null) {
            if (!this.isElement(x.key)) return false;
            x = T.successor(x);
        }
        return true;
    }


    public binarySearchTree<Item> nonNaiveUnion(binarySearchTree<Item> T) {
        // TODO
        traverseNonNaiveUnion(this.root, T);
        return this.union(T);
    }

    /**
     * helper function to do recursion for the non naiver union
     * Complexity:
     * @param x
     * @param T
     */
    private void traverseNonNaiveUnion(Node<Item> x, binarySearchTree<Item> T) {
        Node<Item> holderNode;
        if (x != null) {
            traverseNonNaiveUnion(x.left, T);

            // conditional for leaf node
            if ((x.left == null)&&(x.right == null)) {
                if (T.isElement(x.key)) {
                    holderNode = T.findNode(x.key);
                    // min value is uncapped
                    if (this.getMin(x).compareTo(x.key)==0) {
                        // max value is uncapped
//                        uncapped min, max
                        if (this.getMax(x).compareTo(x.key) == 0) {
                            T.removeSubtree(holderNode);
                            this.transplant(x, holderNode);
//                            uncapped min, capped max
                        } else if (T.getMax(holderNode).compareTo(this.getMax(x)) < 0) {
                            T.removeSubtree(holderNode);
                            this.transplant(x, holderNode);
                        } else {
                            T.remove(holderNode.key);
                        }
                    } else if (this.getMin(x).compareTo(T.getMin(holderNode)) < 0) {
//                        capped min capped max
                        if (this.getMax(holderNode).compareTo(this.getMax(x)) < 0) {
                            T.removeSubtree(holderNode);
                            transplant(x, holderNode);
//                            capped min uncapped max
                        } else if (this.getMax(x).compareTo(x.key) == 0) {
                            T.removeSubtree(holderNode);
                            transplant(x, holderNode);
                        } else {
                            T.remove(holderNode.key);
                        }
                    }
                }
            }

            traverseNonNaiveUnion(x.right, T);
        }
    }

    /**
     * Remove a whole subtree
     * @param x - the root of the subtree to be removed
     */
    private void removeSubtree(Node<Item> x){

//        case if subtree is tree it self
        if (x.p == null) {
            this.root = null;
        } else if (x.p.left == x) {
            x.p.left = null;
        } else {
            x.p.right = null;
        }

    }

    /**
     * get maximum value for a node of a subtree/tree
     * @param x - root node of the subtree
     * @return
     */
    private Item getMax(Node<Item> x) {
        // case if it is a root
        if (x.p == null) {
            if (x.right == null) return x.key;
        } else {
            Node<Item> parent = x.p;
            while(parent != this.root) {
                if (parent.left == x) {
                    return parent.key;
                }
                parent = parent.p;
            }
        }
//            case when x has the max key
        return x.key;
    }

    /**
     * get minimum value for a node of a subtree/tree
     * @param x - root node of the subtree
     * @return
     */
    private Item getMin(Node<Item> x) {
        if (x.p == null) {
            if (x.left == null) return x.key;
        } else {
            Node<Item> parent = x.p;
            while(parent != this.root) {
                if (parent.right == x) {
                    return parent.key;
                }
                parent = parent.p;
            }
        }
//             case when x has the minimum key
        return x.key;
    }


    public static void main(String[] args) {
//        bst1= {-1, 1, 2, 3, 4, 18}
        binarySearchTree<Integer> bst1 = new binarySearchTree<Integer>();
        bst1.add(2);
        bst1.add(3);
        bst1.add(4);
        bst1.add(1);
        bst1.add(1);
        bst1.add(18);
        bst1.add(-1);

//        bst2 = {-1, 0, 1, 2, 4, 5, 6, 7, 73}
        binarySearchTree<Integer> bst2 = new binarySearchTree<Integer>();
        bst2.add(7);
        bst2.add(2);
        bst2.add(73);
        bst2.add(5);
        bst2.add(6);
        bst2.add(4);
        bst2.add(1);
        bst2.add(0);
        bst2.add(-1);

        System.out.println("############################################");

        System.out.println("Union of bst:");

        System.out.print("bst1: ");
        bst1.print(bst1.root);
        System.out.println(" ");

        System.out.print("bst2: ");
        bst2.print(bst2.root);
        System.out.println(" ");

        binarySearchTree<Integer> unionBST =  bst1.union(bst2);
        System.out.print("Union of bst1 and bst2: ");
        unionBST.print(unionBST.root);


        System.out.println(" ");
        System.out.println("############################################");

        System.out.println("Intersection of bst:");
        System.out.print("bst1: ");
        bst1.print(bst1.root);
        System.out.println(" ");
        System.out.print("bst2: ");
        bst2.print(bst2.root);
        System.out.println(" ");

        binarySearchTree<Integer> intersectionBST = bst1.intersection(bst2);
        System.out.print("Intersection of bst1 and bst2: ");
        intersectionBST.print(intersectionBST.root);

        System.out.println(" ");
        System.out.println("############################################");

        System.out.println("Difference of bst:");
        System.out.print("bst1: ");
        bst1.print(bst1.root);
        System.out.println(" ");
        System.out.print("bst2: ");
        bst2.print(bst2.root);
        System.out.println(" ");

        binarySearchTree<Integer> differenceBST = bst1.difference(bst2);
        System.out.print("Difference of bst1 and bst2: ");
        differenceBST.print(differenceBST.root);


        System.out.println(" ");
        System.out.println("############################################");

        System.out.println("Subset of bst:");
        binarySearchTree<Integer> bst3 = new binarySearchTree<Integer>();
        bst3.add(1);
        bst3.add(2);
        bst3.add(3);
        bst3.add(4);


        binarySearchTree<Integer> bst4 = new binarySearchTree<Integer>();
        bst4.add(1);
        bst4.add(2);
        bst4.add(3);
        bst4.add(4);
        bst4.add(123);

        System.out.print("bst1: ");
        bst1.print(bst1.root);
        System.out.println(" ");
        System.out.print("bst3: ");
        bst3.print(bst3.root);
        System.out.println(" ");
        System.out.print("bst4: ");
        bst4.print(bst4.root);
        System.out.println(" ");

        System.out.println("bst3 is a subset of bst1: "+bst1.subset(bst3));
        System.out.println("bst4 is a subset of bst1: "+bst1.subset(bst4));


        System.out.println(" ");
        System.out.println("############################################");

        System.out.println("Non Naive Union of bst:");
        System.out.print("bst1: ");
        bst1.print(bst1.root);
        System.out.println(" ");
        System.out.print("bst2: ");
        bst2.add(-2);
        bst2.print(bst2.root);
        System.out.println(" ");

        binarySearchTree<Integer> bstUnion = bst1.nonNaiveUnion(bst2);

        System.out.print("Non-Naive Union of bst1 and bst2: ");
        bstUnion.print(bstUnion.root);
        System.out.println(" ");




    }

}
