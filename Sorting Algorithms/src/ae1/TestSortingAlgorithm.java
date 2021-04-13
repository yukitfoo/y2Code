package ae1;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;


public class TestSortingAlgorithm extends SortingAlgorithms {

    /**
     * run the code here, test code can be used by commenting out other lines of code
     * @param args
     * @throws FileNotFoundException if file to read dataset from doesnt exist
     */
    public static void main(String[] args) throws FileNotFoundException {

//        TEST CODE 1
//        Code to test correctnesss of the algorithm
//        int[] A can be changed to other arrays
        int[] A = {93,4,6,2,4,5,23,6,3,23,2,6,3,62,200};
        System.out.println(Arrays.toString(testSortingAlgorithm(A)));


//        TEST CODE 2
//        Reads code from dataset provided in the form of a text file
//        put datasets inside a folder called "Files" in the same directory as the src folder
        File projectDirectory = new File(System.getProperty("user.dir")+"\\Files");
        String[] files = projectDirectory.list();
//        prints out list of files available to be read
        System.out.println(Arrays.toString(files));
//        (1) fit the file name based on the sequence given from the print statement above
//        ie: "int100.txt" has name int100 defined below
//        these names are just for convenience sake and will vary from device to device
//        most likely need to change code here
        int[] int10 = readFile(files[0]);
        int[] int100 = readFile(files[1]);
        int[] int1000 = readFile(files[2]);
        int[] int50 = readFile(files[3]);
//        put the name in order into allArrays based on (1)
        int[][] allArrays = {int10, int100,int1000, int50};
//        calls TimeSortingAlgorithms and timeQuicksortInsertion
        TimeSortingAlgorithms(allArrays, files);
        timeQuicksortInsertion(allArrays);



//        TEST CODE 3
//        prints out 10 arrays to show that all arrays have random outputs
//        change arrayLength to length of array wanted
        int arrayLength = 10;
        for (int i = 0; i < 10; i++) {
            System.out.println(Arrays.toString(makeWorstCase(arrayLength)));
        }


    }

    /**
     *
     * @param fileName-name of the file to be read from
     * @return array of integers extracted from the file
     * @throws FileNotFoundException if file doesnt exists
     */
    private static int[] readFile(String fileName) throws FileNotFoundException {
        File projectDirectory = new File(System.getProperty("user.dir")+"\\Files");
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

    /**
     * prints out time taken to run the algorithms, high length of arrays might throw StackOverflowError as quicksort uses recursion
     * @param arrays-array of array of integers to be ran using the algorithms
     * @param arrayNames-array of the name of array(name of dataset; ie: "int100.txt")
     */
    public static void TimeSortingAlgorithms(int[][] arrays, String[] arrayNames) {
        double[] algotime = new double[6];

        long[] timetaken = new long[10];
        long timeI;
        long timeF;
        int[] a;
        int[] b;
        for (int j = 0; j < arrays.length; j++) {
            a = arrays[j];
//            normal quicksort
            for (int i=0; i < 10; i++) {
                b = a.clone();
                timeI = System.nanoTime();
                quicksort(b, 0, b.length-1);
                timeF = System.nanoTime();
                timetaken[i] = timeF-timeI;
            }
            algotime[0] = average(timetaken);

//          quicksort with insertion
            for (int i=0; i < 10; i++) {
                b = a.clone();
                timeI = System.nanoTime();
                quicksortInsertion(b, 0, b.length-1,b.length/2);
                timeF = System.nanoTime();
                timetaken[i] = timeF-timeI;
            }
            algotime[1] = average(timetaken);
//          quicksort median of 3
            for (int i=0; i < 10; i++) {
                b = a.clone();
                timeI = System.nanoTime();
                quickSortmo3(b, 0,  (b.length-1)/2, b.length-1);
                timeF = System.nanoTime();
                timetaken[i] = timeF-timeI;
            }
            algotime[2] = average(timetaken);

//          3way quicksort
            for (int i=0; i < 10; i++) {
                b = a.clone();
                timeI = System.nanoTime();
                quicksort3way(b, 0, b.length-1);
                timeF = System.nanoTime();
                timetaken[i] = timeF-timeI;
            }
            algotime[3] = average(timetaken);

//          insertion sort
            for (int i=0; i < 10; i++) {
                b = a.clone();
                timeI = System.nanoTime();
                insertionSort(b);
                timeF = System.nanoTime();
                timetaken[i] = timeF-timeI;
            }
            algotime[4] = average(timetaken);

//          merge sort
            for (int i=0; i < 10; i++) {
                b = a.clone();
                timeI = System.nanoTime();
                mergeSort(b, 0, b.length-1);
                timeF = System.nanoTime();
                timetaken[i] = timeF-timeI;
            }
            algotime[5] = average(timetaken);

//            now that all algorithms have been ran with its average time
//            pass it to read the values
            printTimeTaken(arrayNames[j], algotime);

        }

    }

    /**
     * helper method of TimeSortingAlgorithms() that prints the running time of each algorithm
     * @param filename-name of the file (String)
     * @param time-time taken for each algorithm to run
     * All parameters here are generated from TimeSortingAlgorithm
     */
    private static void printTimeTaken(String filename, double[] time) {
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("Time taken to sort " + filename + ":\n");
        System.out.println("Normal Quicksort: " + String.format("%.2f", time[0]) + " nanoseconds\n");
        System.out.println("Quicksort with Insertion: " + String.format("%.2f", time[1]) + " nanoseconds\n");
        System.out.println("Median of 3 Quicksort: " + String.format("%.2f", time[2]) + " nanoseconds\n");
        System.out.println("3 way Quicksort: " + String.format("%.2f", time[3]) + " nanoseconds\n");
        System.out.println("Insertion Sort: " + String.format("%.2f", time[4]) + " nanoseconds\n");
        System.out.println("Merge Sort: " + String.format("%.2f", time[5]) + " nanoseconds\n");
    }

    /**
     * helper method to find average from an array of long
     * @param values-running times of each instance of the algorithm called
     * @return average from the array as a double
     */
    private static double average(long[] values) {
        long sum = 0;
        for (long i : values) {
            sum += i;
        }
        return (double) sum/values.length;

    }


    /**
     * Hard coded test for all sorting algorithms defined in the class SortingAlgorithms
     * equality tested using .equals with a clone of A sorted using Array.sort()
     * @param A- array to be sorted
     * @return Array of boolean corresponding to which sorting algorithm works
     */
    public static boolean[] testSortingAlgorithm(int[] A) {

        boolean[] allcheck = new boolean[6];
        int[] clone = A.clone();
        int[] A1 = A.clone();
        int[] A2 = A.clone();
        int[] A3 = A.clone();
        int[] A4 = A.clone();
        int[] A5 = A.clone();

        SortingAlgorithms.insertionSort(A);
        SortingAlgorithms.mergeSort(A1,0,A1.length-1);
        SortingAlgorithms.quicksort(A2,0,A2.length-1);
        SortingAlgorithms.quicksortInsertion(A3,0,A3.length-1, 4);
        SortingAlgorithms.quickSortmo3(A4,0,(int) (A4.length-1)/2,A4.length-1);
        SortingAlgorithms.quicksort3way(A5,0,A5.length-1);
        Arrays.sort(clone);



        allcheck[0] = Arrays.equals(A, clone);
        allcheck[1] = Arrays.equals(A1, clone);
        allcheck[2] = Arrays.equals(A2, clone);
        allcheck[3] = Arrays.equals(A3, clone);
        allcheck[4] = Arrays.equals(A4, clone);
        allcheck[5] = Arrays.equals(A5, clone);

        return allcheck;
    }

    /**
     * Times how the cutoff values affect Quicksort with Insertionsort
     * values printed out are in order quicksort, insertion quicksort, median of 3 quicksort, 3 way quicksort, insertion sort, merge sort
     * these values are printed out as a csv file format to be interfaced with MS Excel
     * @param arrays-array of arrays of integers to be tested with
     */
    public static void timeQuicksortInsertion(int[][] arrays) {
        long timeI;
        long timeF;
        long[] timetaken = new long[10];
        double[] algotime = new double[6];
        int[] b;


        for (int[] a : arrays) {
            for (int j = 0; j < a.length-1; j+=10) {
                for (int i=0; i < 10; i++) {
                    b = a.clone();
                    timeI = System.nanoTime();
                    quicksort(b, 0, b.length-1);
                    timeF = System.nanoTime();
                    timetaken[i] = timeF-timeI;
                }
                algotime[0] = average(timetaken);

//          quicksort with insertion
                for (int i=0; i < 10; i++) {
                    b = a.clone();
                    timeI = System.nanoTime();
                    quicksortInsertion(b, 0, b.length-1,j);
                    timeF = System.nanoTime();
                    timetaken[i] = timeF-timeI;
                }
                algotime[1] = average(timetaken);
//          quicksort median of 3
                for (int i=0; i < 10; i++) {
                    b = a.clone();
                    timeI = System.nanoTime();
                    quickSortmo3(b, 0,  (b.length-1)/2, b.length-1);
                    timeF = System.nanoTime();
                    timetaken[i] = timeF-timeI;
                }
                algotime[2] = average(timetaken);

//          3way quicksort
                for (int i=0; i < 10; i++) {
                    b = a.clone();
                    timeI = System.nanoTime();
                    quicksort3way(b, 0, b.length-1);
                    timeF = System.nanoTime();
                    timetaken[i] = timeF-timeI;
                }
                algotime[3] = average(timetaken);

//          insertion sort
                for (int i=0; i < 10; i++) {
                    b = a.clone();
                    timeI = System.nanoTime();
                    insertionSort(b);
                    timeF = System.nanoTime();
                    timetaken[i] = timeF-timeI;
                }
                algotime[4] = average(timetaken);

//          merge sort
                for (int i=0; i < 10; i++) {
                    b = a.clone();
                    timeI = System.nanoTime();
                    mergeSort(b, 0, b.length-1);
                    timeF = System.nanoTime();
                    timetaken[i] = timeF-timeI;
                }
                algotime[5] = average(timetaken);

//            now that all algorithms have been ran with its average time
//            pass it to read the values
//                printTimeTaken("value of insertion:"+j, algotime);

                System.out.print(algotime[0]+",");
                System.out.print(algotime[1]+",");
                System.out.print(algotime[2]+",");
                System.out.print(algotime[3]+",");
                System.out.print(algotime[4]+",");
                System.out.println(algotime[5]);
            }
        }
    }

    /**
     * Makes the pathological input sequence for the median of 3 quicksort algorithm
     * @param arrayLength-an integer defining the length of the array
     * @return an array that represented the worst case sequence
     */
    public static int[] makeWorstCase(int arrayLength) {
//        for cases where length of array doesnt matter
        if (arrayLength < 0) {
            throw new IllegalArgumentException("Please enter a value higher than 0");
        } else if (arrayLength == 0) return new int[] {};


        int[] a = new int[arrayLength];
        int intermediate;

        if (arrayLength % 2 == 0) {
//            the length is an even number
            intermediate = arrayLength/2;
            a[intermediate] = getRandomNumber(0);
            a[0] = getRandomNumber(a[intermediate]);
            for (int i = 1; i < intermediate; i++) {
                a[intermediate+i] = getRandomNumber(a[i-1]);
                a[i] = getRandomNumber(a[intermediate+i]);
            }

        } else {
            intermediate = ((arrayLength-1)/2);
//            the length is an odd number
//            get the first value(smallest value)
            a[0] = getRandomNumber(0);
            a[intermediate+1] = getRandomNumber(a[0]);
            a[1] = getRandomNumber(a[intermediate+1]);
            for (int i = 2; i <= intermediate; i++) {
                a[intermediate+i] = getRandomNumber(a[i-1]);
                a[i] = getRandomNumber(a[intermediate+i]);
            }
        }
        return a;



    }

    /**
     * helper method to makeWorstCase()
     * returns a random number
     * @param min-minimum number that the random number generator will generate
     * @return a random number bigger than min(param)
     */
    private static int getRandomNumber(int min) {
        min++;
        int bigValue = 10;
        return (int) (Math.random() * bigValue) + min;
    }

}




