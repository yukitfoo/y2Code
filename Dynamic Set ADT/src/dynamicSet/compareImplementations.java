/**
 * @author Yu Kit, Foo (2441458f)
 */

package dynamicSet;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class compareImplementations {


    private static int[] readFile(String fileName) throws FileNotFoundException {
        File projectDirectory = new File(System.getProperty("user.dir"));
        Scanner s = new Scanner(new File(projectDirectory+"\\"+fileName));
        int[] values = new int[2_000_000];
        int counter=0;
        while (s.hasNextInt()) {
            values[counter] = s.nextInt();
            counter++;
        }
        int[] fixedValues = new int[counter];
        for (int i =0; i < fixedValues.length; i++) {
            fixedValues[i] = values[i];
        }
        return fixedValues;
    }

    public static long timeIsElementBST(int rand, binarySearchTree<Integer> bst) {
        long timeI = System.nanoTime();
        bst.isElement(rand);
        long timeF = System.nanoTime();
        return timeF-timeI;
    }

    public static long timeIsElementDLL(int rand, doublyLinkedList<Integer> dll) {
        long timeI = System.nanoTime();
        dll.isElement(rand);
        long timeF = System.nanoTime();
        return timeF-timeI;
    }


    public static void main(String[] args) throws FileNotFoundException{
        int[] dataValues = readFile("int20k.txt");

        binarySearchTree<Integer> bst = new binarySearchTree<Integer>();
        doublyLinkedList<Integer> dll = new doublyLinkedList<Integer>();

        for (int i: dataValues) {
            bst.add(i);
            dll.add(i);
        }

        Random rand = new Random();
        long countBST = 0;
        long countDLL = 0;

        int[] rng = new int[100];
        for (int i = 0; i < 100; i ++) {
            rng[i] = rand.nextInt(50000);
        }

        for (int i: rng) {
            countBST += timeIsElementBST(i,bst);
            countDLL += timeIsElementDLL(i,dll);
        }

        System.out.println("######################################################");
        System.out.println("a) Average time taken for isElement of:");
        System.out.print("1. Binary Search Trees: ");
        System.out.println(countBST/100 + " nanoseconds");
        System.out.print("2. Doubly Linked List: ");
        System.out.println(countDLL/100 + " nanoseconds");
        System.out.println("");

        System.out.println("######################################################");
        System.out.println("b) Set Size of: ");
        System.out.print("1. Binary Search Trees: ");
        System.out.println(bst.setSize(bst.getRoot()));
        System.out.print("2. Doubly Linked List: ");
        System.out.println(dll.setSize());
        System.out.println("");

        System.out.println("######################################################");
        System.out.println("c) Height of: ");
        System.out.print("1. Binary Search Trees: ");
        System.out.print(bst.getHeight(bst.getRoot()));




    }

}
