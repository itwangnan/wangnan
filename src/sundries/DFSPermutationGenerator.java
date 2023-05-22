package sundries;

import java.util.Arrays;

public class DFSPermutationGenerator {
    public int N;
    private boolean[] used;
    private int[] result;

    public DFSPermutationGenerator(int n) {
        N = n;
        used = new boolean[n + 1];
        result = new int[n];
    }

    public static void main(String[] args) {
//        DFSPermutationGenerator generator = new DFSPermutationGenerator(4);
//        generator.make(0);

        int[] arr = new int[]{1,2,3,4};

        next_permutation(arr);
        next_permutation(arr);
        next_permutation(arr);


    }

    public void make(int level) {
        for (int i = 1; i <= N ; i++) {
            if (!used[i]) {
                used[i] = true;
                result[level] = i;
                make(level + 1);
                used[i] = false;
            }
        }

        if (level == N - 1) {
            for (int i = 0; i < N; i++) {
                System.out.print(result[i] + " ");
            }
            System.out.println();
        }
    }

    public static void next_permutation(int[] array)
    {
        int len = array.length;
        int front, back, rev_pnt;
        int tmp;

        front = len - 1;

        for (;;)
        {
            back = front;
            front--;
            if (array[front] < array[back])
            {
                rev_pnt = len;
                while (!(array[front] < array[--rev_pnt]));
                tmp = array[front];
                array[front] = array[rev_pnt];
                array[rev_pnt] = tmp;

                /* reverse substring from rev_pnt to end */
                Arrays.sort(array,rev_pnt, len);
                System.out.printf("%s\n", Arrays.toString(array));
                return;
            }

            if (front == 0)
            {
                System.out.printf("None\n");
                return;
            }
        }
    }
}
