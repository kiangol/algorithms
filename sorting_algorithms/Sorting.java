import java.util.Random;

public class Sorting {

    public static void swap(int a, int b, int[] list) {
        int p = list[a];
        list[a] = list[b];
        list[b] = p;
    }


    static void SelectionSort(int[] list) {

        for (int i = 0; i < list.length-1; i++) {
            int smallestIndex = i;
            for (int j = i+1; j < list.length; j++) {
                if (list[j] < list[smallestIndex]) {
                    smallestIndex = j;
                }
            }
            int temp = list[smallestIndex];
            list[smallestIndex] = list[i];
            list[i] = temp;
        }
    }

    static void InsertionSort(int[] list) {
        System.out.println("Unsorted array:");
        printArray(list);
        System.out.println("Steps:");
        for (int i = 1; i < list.length; i++) {
            int temp = list[i];
            int j = i-1;
            while (j >= 0 && list[j] > temp) {
                list[j+1] = list[j];
                j--;
            }
            list[j+1] = temp;
            printArray(list);
        }
        System.out.println("Sorted array:");
        printArray(list);
    }

    static int[] MergeSort(int[] list) {
        printArray(list);
        if(list.length <= 1) {
            return list;
        }
        // create left and right arrays
        int mid = list.length / 2;
        int[] leftArray = new int[mid];
        int[] rightArray = new int[list.length - mid];
        // fill the left array
        for (int i = 0; i < mid; i++) {
            leftArray[i] = list[i];
        }
        // fill the right array
        for (int j = 0; j < rightArray.length; j++) {
            rightArray[j] = list[mid+j];
        }

        int[] finalArray = new int[list.length];
        leftArray = MergeSort(leftArray);
        rightArray = MergeSort(rightArray);
        //merge left and right array into the resulting array
        finalArray = merge(leftArray, rightArray);
        return finalArray;
    }

    static int[] merge(int[] l, int[] r) {
        int leftIndex, rightIndex, finalIndex;
        finalIndex = 0;
        leftIndex = rightIndex = finalIndex;

        int[] finalArray = new int[l.length + r.length];

        while(leftIndex < l.length || rightIndex < r.length) {
            printArray(finalArray);
            if(leftIndex < l.length && rightIndex < r.length) {
                if(l[leftIndex] < r[rightIndex]) {
                    finalArray[finalIndex++] = l[leftIndex++];
                } else {
                    finalArray[finalIndex++] = r[rightIndex++];
                }
            }
            else if(leftIndex < l.length) {
                finalArray[finalIndex++] = l[leftIndex++];
            }
            else if(rightIndex < r.length) {
                finalArray[finalIndex++] = r[rightIndex++];
            }
        }
        return finalArray;
    }

//    public static int[] quickSort(int[] list, int low, int high) {
//        if(low >= high) return list;
//        int l = partition(list, low, high);
//        //left partition
//        quickSort(list, low, l-1);
//        //right partition
//        quickSort(list, l+1, high);
//        return list;
//    }

    static void quickSort(int[] list, int low, int high) {
        int f = 0;
        while(low < high) {
//            System.out.println("First iteration: " + f++);
            int l = partition(list, low, high);
            if((l-low) < (high-l)) {
                quickSort(list, low, l-1);
                low = l+1;
            } else {
                quickSort(list, l+1, high);
                high = l-1;
            }
        }
    }


    static int partition(int[] list, int low, int high) {
        int r = findPivot(list, low, high);
        swap(r, high, list);
        int pivot = list[high];
        int l = low;
        r = high-1;

        while(l <= r) {
            while (l <= r && list[l] <= pivot) {
                l++;
            }
            while (r >= l && list[r] >= pivot) {
                r--;
            }
            if (l < r) swap(l, r, list);
        }
        swap(l, high, list);
        return l;
    }

    static int findPivot(int[] list, int low, int high) {
        int a = list[low];
        int c = list[high];
        int b = list[(low+high)/2];

        if(a > b) {
            if(a < c) return low;
            if(b > c) return ((low+high)/2);
        }
        else if(a < b) {
            if(a > c) return low;
            if(b < c) return ((low+high)/2);
        }
        return high;
    }

    public static void printArray(int[] list) {
        for(int i = 0; i < list.length; i++) {
            System.out.print(list[i] + " ");
        }
        System.out.println();
    }

}
