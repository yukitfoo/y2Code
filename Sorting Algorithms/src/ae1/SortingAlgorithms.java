package ae1;

import java.lang.reflect.Array;
import java.util.Arrays;

public class SortingAlgorithms {

//    insertion sort method
    public static int[] insertionSort(int[] A) {
        int key;
        int i;
        for (int j = 1; j <= A.length-1; j++) {
            key = A[j];
            i = j-1;
            while ((i >= 0)&&(A[i]>key)) {
                A[i+1] = A[i];
                i--;
            }
            A[i+1] = key;
        }
        return A;
    }
//merge sort method
    public static int[] mergeSort(int[]a, int p, int r) {
        if (p<r) {
            int q = (p+r)/2;
            mergeSort(a, p, q);
            mergeSort(a,q+1,r);
            a = merge(a,p,q,r);
        }
        return a;
    }

//    helper for mergesort
    private static int[] merge(int[] a, int p, int q, int r) {

        int n1 = q - p + 1;
        int n2 = r - q;
        int[] L = new int[n1+1];
        int[] R = new int[n2+1];
        System.arraycopy(a,p,L,0,n1);
        L[n1] = Integer.MAX_VALUE;
        System.arraycopy(a,q+1,R,0,n2);
        R[n2] = Integer.MAX_VALUE;
        int i = 0;
        int j = 0;
        for (int k = p; k <=r; k++) {
            if (L[i] <= R[j]) {
                a[k] = L[i];
                i++;
            } else {
                a[k] = R[j];
                j++;
            }
        }
        return a;
    }



//    helper for quicksort
    private static int partition(int[] A, int p, int r) {
        int x = A[r];
        int i = p-1;

        int temp;
        for (int j = p; j <= r-1; j++) {
//            to help swap later on
            temp = A[j];
            if (A[j] <= x) {
                i++;
//                swap(A[j],A[i])
                A[j] = A[i];
                A[i] = temp;
            }
        }
        temp = A[i+1];
        A[i+1] = A[r];
        A[r] = temp;
        return i+1;
    }

//    quicksort
    public static void quicksort(int[] A, int p, int r) {
        if (p<r) {
            int q = partition(A,p,r);
            quicksort(A,p,q-1);
            quicksort(A,q+1,r);
        }
    }



//    quicksort which utilised insertion, value of k is cutoff value to use insertion at
    public static void quicksortInsertion(int[] A, int p, int r, int k) {
        if (r<p) return;
        if (r-p < k) {
            insertionSort(A, p, r);
            return;
        }
        if (p<r) {
            int q = partition(A,p,r);
            quicksortInsertion(A,p,q-1, k);
            quicksortInsertion(A,q+1,r, k);
        }
    }

//    helper for insertion of quicksort
    private static void insertionSort(int[] A, int p, int r) {
        int key;
        int i;
        for (int j = p+1; j <= r; j++) {
            key = A[j];
            i = j-1;
            while ((i>= 0) && (A[i] > key)) {
                A[i+1] = A[i];
                i = i-1;
            }
            A[i+1] = key;
        }
    }



//    median of 3 quicksort
    public static void quickSortmo3(int[]A, int p, int q, int r) {
        if (p<r) {
            int part = partitionmo3(A,p,q,r);
            quickSortmo3(A,p,(int) (part-1+p)/2,part-1);
            quickSortmo3(A,part+1, (int) (part+1+r)/2, r);
        }
    }

//    helper partition for median of 3 quicksort
    private static int partitionmo3(int[] A, int p, int q ,int r) {
//        code here gets median and sets is as pivot by changing it to the r element
//        complexity of median is constant as it takes in 3 parameters and performs actions based on those 3 parameters
        int median = median(A,p,q,r);
        int temp = A[median];
        A[median] = A[r];
        A[r] = temp;

        int x = A[r];
        int i = p-1;


        for (int j = p; j <= r-1; j++) {
//            to help swap later on
            temp = A[j];
            if (A[j] <= x) {
                i++;
//                swap(A[j],A[i])
                A[j] = A[i];
                A[i] = temp;
            }
        }
        temp = A[i+1];
        A[i+1] = A[r];
        A[r] = temp;
        return i+1;
    }

//    helper return the middle value of 3 integers
    private static int median(int[]A, int p, int q, int r) {
        if ((A[p]<=A[r]&&p>=q)||(A[p]<=A[q]&&A[p]>=A[r])) return p;
        else if ((A[r]<=A[p]&&A[r]>=A[q])||(A[r]<=A[q]&&A[r]>=A[p])) return r;
        else return q;
    }


//    3 way partiion
    public static void quicksort3way(int[] A, int p, int r) {
//        System.out.println(Arrays.toString(A));
        if (p<r) {
            int[] details = partition3way(A,p,r);
//            avoids the pivot subarray
            quicksort3way(A,p,details[0]);
            quicksort3way(A,details[1],r);
        }
    }
//    partition the array in 3 ways, lower than pivot, equal, higher than pivot
    private static int[] partition3way(int[] A, int p, int r) {
        int pivot = A[r];
        int placement = 0;
        int lowArray = 0;
        int highArray = A.length-1;
        int temp;
        for (int i = 0; i < A.length-1; i++) {
            if (i == r) break;
            if (A[i] < pivot) {
                temp = A[i];
                A[i] = A[placement];
                A[placement] =temp;
                placement++;
                lowArray++;
            }
        }

        placement = A.length-1;
        for (int i = A.length-1; i >= 0; i--) {
            if (A[i] > pivot) {
                temp = A[i];
                A[i] = A[placement];
                A[placement] = temp;
                placement--;
                highArray--;

            } else if (A[i] < pivot) break;
        }


        return new int[]{lowArray-1, highArray+1};

    }











}
