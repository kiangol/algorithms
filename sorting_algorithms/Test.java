import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;

public class Test extends Sorting {
    
    static Random r = new Random();
//    static int[] sizes = {1000, 5000, 10000, 50000, 100000, 1000000, 10000000};
    static int[] sizes = {5};
    static int[] test = {5,4,1,2,8};
    static Sorting sorts = new Sorting();

    public static void main(String[] args) {
        testInsertion();
//

//        Test for forskjellige størrelser
//        for(int i = 0; i < sizes.length; i++) {
//            test = new int[sizes[i]];
//            randomizeArray(10);
////            decrArray();
//            System.out.println("Testing " + test.length + " elements");
//            testInsertion();


//        }


    }
        // unødvendig
    public static void randomizeArray(int[] list, int maxValue) {
        for (int i = 0; i < list.length; i++) {
            list[i] = r.nextInt(maxValue);
        }
    }

        //random med begrenset område
    public static void randomizeArray(int maxValue) {
        for (int i = 0; i < test.length; i++) {
            test[i] = r.nextInt(maxValue);
        }
    }

        // random med ubegrenset verdiområde
    public static void randomizeArray() {
        for (int i = 0; i < test.length; i++) {
            test[i] = r.nextInt();
        }
    }

        // sortert array stigende rekkefølge
    public static void incrArray() {
        for (int i = 0; i < test.length; i++) {
            test[i] = i;
        }
    }

    // sortert array synkende
    public static void decrArray() {
        int k = test.length;
        for (int i = 0; i < test.length; i++) {
            test[i] = k;
            k--;
        }
    }

    public static void testSelection() {
        // randomizeArray();
//        System.out.println("SelectionSort started");
        long start = System.nanoTime();
        sorts.SelectionSort(test);
        long end = System.nanoTime();
        System.out.println("SelectionSort done in " + (end - start)/1000000.0 + " ms\n");
    }

    public static void testInsertion() {
//        randomizeArray();
//        System.out.println("InsertionSort started");
        long start = System.nanoTime();
        sorts.InsertionSort(test);
        long end = System.nanoTime();
        System.out.println("InsertionSort done in " + (end - start)/1000000.0 + " ms\n");
//        printArray(test);
    }    


    public static void testMerge() {
//        randomizeArray();
//        System.out.println("MergeSort started");
        long start = System.nanoTime();
        int[] sorted = sorts.MergeSort(test);
        long end = System.nanoTime();
        System.out.println("MergeSort done in " + (end - start)/1000000.0 + " ms\n");
    }

    public static void testQuick() {
//        randomizeArray();
//        System.out.println("QuickSort started");
        long start = System.nanoTime();
//        int[] sorted =
        sorts.quickSort(test, 0, test.length-1);
        long end = System.nanoTime();
        System.out.println("QuickSort done in " + (end - start)/1000000.0 + " ms\n");
    }

    public static void testArraysSort() {
//        System.out.println("ArraysSort started");
        long start = System.nanoTime();
        Arrays.sort(test);
        long end = System.nanoTime();
        System.out.println("ArraysSort done in " + (end - start)/1000000.0 + " ms\n");

    }

        // skriv ut hele
    public static void printArray(int[] list) {
        for(int i = 0; i < list.length; i++) {
            System.out.print(list[i] + " ");
        }
        System.out.println();
    }


    // skriv ut n første elementer
    public static void printArray(int[] list, int n) {
        for(int i = 0; i < n; i++) {
            System.out.print(list[i] + " ");
        }
        System.out.println();
    }

}