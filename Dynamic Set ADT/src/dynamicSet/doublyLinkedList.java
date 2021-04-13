/**
 * @author Yu Kit, Foo (2441458f)
 */

package dynamicSet;

import java.util.NoSuchElementException;

public class doublyLinkedList<Item> {
    Node<Item> head;
    Node<Item> tail;


    private static class Node<Item>{
        private Item key;
        private Node<Item> next;
        private Node<Item> prev;

        private Node(Item key) {
            this.key = key;
        }
    }

    public doublyLinkedList () {
        this.head = null;
        this.tail = null;
    }

    public Node<Item> getHead() {
        return head;
    }

    public Node<Item> getTail() {
        return tail;
    }

    public int setSize() {
        Node<Item> x = this.head;
//         retrun 0 if case empty
        int size = 0;
        while (x != null) {
            size++;
            x = x.next;
        }
        return size;
    }

    public boolean isEmpty() {
        return this.head == null;
    }


    public boolean isElement(Item elt) {
//      set x as current node to traverse through linked list
        Node<Item> x = this.head;
        while (x != null) {
//            checks if key of the current node is equal to the key provided
            if (x.key.equals(elt)) {
                return true;
            }
//            if its not the key, set current node to its next node(traversal)
            x = x.next;
        }
        return false;
    }


    public void add(Item elt) {
//        case if linkedlist is empty
        if (this.setSize() == 0) {
            this.head = new Node<Item> (elt);
            this.head.prev = null;
            this.head.next = null;
            this.tail = this.head;
            return;
        }
//      if linked list is not empty and there isnt the element in the linked list
        if (!this.isElement(elt)) {
            this.tail.next = new Node<Item> (elt);
            this.tail.next.prev = this.tail;
            this.tail.next.next = null;
            this.tail = this.tail.next;
        }
    }

    public void remove(Item elt) {
//        checks if element is a part of the linked list
        if (!this.isElement(elt)) {
            System.out.println("No such element in the list");
            return;
        }

        Node<Item> eltNode = findEltNode(elt);
//        if node is not head
//        will not throw NullPointerException as it already checks for it above
        if (eltNode.prev != null) {
            eltNode.prev.next = eltNode.next;
        } else {
//            if node is head
            this.head = eltNode.next;
        }
//      if node is not the tail
        if (eltNode.next != null) {
            eltNode.next.prev = eltNode.prev;
        }
    }

    private Node<Item> findEltNode(Item elt) {
        Node<Item> x = this.head;
        while(x != null) {
            if (x.key == elt) {
                return x;
            }
            x = x.next;
        }
        return null;
    }

    /**
     * Idea: initialise new doublyLinkedList, set it up as a clone of "this", use .isElement(node) while iterating through
     * each of the nodes of T. Complexity is O(m*n); which is bad but nothing can be done to improve it since
     * we are prohibited from using java libraries like hash tables
     *
     * @param T - linked list to be merged with "this", with no duplicates
     * @return a new doubly linkedlist of the union of the 2 lists
     */
    public doublyLinkedList<Item> union(doublyLinkedList<Item> T) {
        doublyLinkedList<Item> newList = this.cloneLL();

        Node<Item> x = T.head;
        while (x != null) {
//            .add() will check if the node is already in the list or not
            newList.add(x.key);
            x = x.next;
        }
        return newList;
    }

    /**
     * Idea: using a while loop iterate through each node of the doubly linked list "this", check if the current node
     * is an element of the other linked list provided(using T.isElement(current node in iteration))
     * @param T
     * @return
     */
    public doublyLinkedList<Item> intersection(doublyLinkedList<Item> T) {
        // TODO
        doublyLinkedList<Item> newList = new doublyLinkedList<Item>();
//        set x(current node) to point to the head of the linked list ("this")
        Node<Item> x = this.head;
//        iterate through linked list "this"
        while(x != null) {
//            check if current node is an element in T
            if (T.isElement(x.key)) {
                newList.add(x.key);
            }
            x = x.next;
        }
        return newList;
    }


    /**
     * Idea: use 2 new linked list, one to store the difference and another to store a clone of list T and delete everytime a union
     * element is detected, this is to be appended to the end of the difference list
     * @param T
     * @return
     */
    public doublyLinkedList<Item> difference(doublyLinkedList<Item> T) {
        // TODO
        doublyLinkedList<Item> returnList = new doublyLinkedList<Item>();
        doublyLinkedList<Item> cloneT = T.cloneLL();

        Node<Item> x = this.head;
        while (x != null) {
            if (!T.isElement(x.key)) {
//                if value is not an element in T
                returnList.add(x.key);
            } else {
//                if value is an element in T
                cloneT.remove(x.key);
            }
            x = x.next;
        }
        if (cloneT.isEmpty()) {
            return returnList;
        }
        if (returnList.isEmpty()) {
            return cloneT;
        }

        returnList.tail.next = cloneT.head;
        cloneT.head.prev = returnList.tail;
        returnList.tail = cloneT.tail;

        return returnList;
    }

    public doublyLinkedList<Item> cloneLL() {
        doublyLinkedList<Item> newList = new doublyLinkedList<Item>();
        Node<Item> x = this.head;
        while(x != null) {
            newList.add(x.key);
            x = x.next;
        }
        return newList;
    }


    /**
     *
     * @param T
     * @return
     */
    public boolean subset(doublyLinkedList<Item> T) {
        Node<Item> x = T.head;
        while (x != null) {
            if (!this.isElement(x.key)) {
                return false;
            }
            x = x.next;
        }
        return true;

    }

    @Override
    public String toString() {
        Node<Item> x = this.head;

        if (this.setSize() == 0) return "";

        String returnValue = "";
        while (x != null) {
            if (x == this.tail) {
                returnValue += x.key;
                break;
            }
            returnValue += x.key + ", ";
            x = x.next;
        }
        return returnValue;
    }

    public static void main(String[] args) {

        doublyLinkedList<Integer> ll = new doublyLinkedList<Integer>();
        ll.add(1);
        ll.add(2);
        ll.add(3);
        ll.add(4);
        ll.add(5);

        doublyLinkedList<Integer> tt = new doublyLinkedList<Integer>();
        tt.add(3);
        tt.add(4);
        tt.add(5);
        tt.add(6);
        tt.add(7);
        tt.add(8);

        doublyLinkedList<Integer> cc = ll.cloneLL();
        cc.add(34);


        System.out.println("######################");
        System.out.println("UNION");
        System.out.print("linked list 1: ");
        System.out.println(tt);
        System.out.print("linked list 2: ");
        System.out.println(ll);
        System.out.print("Union of 1 and 2: ");
        System.out.println(ll.union(tt));

        System.out.println("######################");
        System.out.println("INTERSECTION");
        System.out.print("linked list 1: ");
        System.out.println(tt);
        System.out.print("linked list 2: ");
        System.out.println(ll);
        System.out.print("linked list 3: ");
        System.out.println(cc);
        System.out.print("Intersection of 1 and 2: ");
        System.out.println(ll.intersection(tt));
        System.out.print("Intersection of 3 and 2: ");
        System.out.println(cc.intersection(ll));

        System.out.println("######################");
        System.out.println("DIFFERENCE");
        System.out.print("linked list 1: ");
        System.out.println(tt);
        System.out.print("linked list 2: ");
        System.out.println(ll);
        System.out.print("linked list 3: ");
        System.out.println(cc);
        System.out.print("Difference of 1 and 2: ");
        System.out.println(ll.difference(tt));
        System.out.print("Difference of 3 and 2: ");
        System.out.println(cc.difference(ll));


        System.out.println("######################");
        System.out.println("SUBSET");
        System.out.print("linked list 1: ");
        System.out.println(tt);
        System.out.print("linked list 2: ");
        System.out.println(ll);
        System.out.print("linked list 3: ");
        System.out.println(cc);
        System.out.print("2 is subset of 3: ");
        System.out.println(cc.subset(ll));
        System.out.print("1 is subset of 3: ");
        System.out.println(cc.subset(tt));
    }

}
