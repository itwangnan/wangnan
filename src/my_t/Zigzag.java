package my_t;

import java.util.Random;

public class Zigzag {


    public static void main(String[] args) {

        int x = 5;
        int y = 3;

        int[][] arr = new int[x][y];

        Random rd = new Random();

        for (int i = 0; i < arr.length; i++) {

            for (int j = 0; j < arr[i].length; j++) {

                arr[i][j] = rd.nextInt(1000);
                System.out.print(arr[i][j]+"  ");
            }
            System.out.println();
        }
        System.out.println("=======");
        //Zigzag 遍历

//        test(arr,x,y);
        test2(arr,x,y);


    }

    private static void test2(int[][] arr, int x, int y) {

        int ar = 0;
        int ac = 0;
        int br = 0;
        int bc = 0;
        int endR = arr.length-1;
        int endC = arr[0].length-1;
        boolean flag = false;

        while (ar != endR+1){

            printLevel(arr,ar,ac,br,bc,flag);
            //
            ar = ac == endC ? ar+1 : ar;
            ac = ac == endC ? ac : ac+1;
            bc = br == endR ? bc+1 : bc;
            br = br == endR ? br : br+1;



            flag = !flag;
        }







    }

    private static void printLevel(int[][] arr, int ar, int ac, int br, int bc, boolean flag) {

        if (flag){
            //加一是把第一次也打印出来
            while (ar != br+1){
                System.out.print(arr[ar++][ac--] + " ");
            }

        }else {
            //减一是把第一次也打印出来
            while (br != ar-1){
                System.out.print(arr[br--][bc++] + " ");
            }
        }

    }

    private static void test(int[][] arr, int x, int y) {
        int n = 0;
        int m = 0;
        int flag = arr[n][m];
        System.out.print(flag+"  ");
        while (n != (x-1) || m != (y-1)){

            //第一列和最后一列都是向下，其他向右
            if (n == 0 || n == (x-1)){
                if (m == (y-1)){
                    ++n;
                }else {
                    ++m;
                }
                flag = arr[n][m];
                System.out.print(flag+ " ");
                if (m == 0 || n == x-1){
                    while (m != y-1){
                        flag = arr[--n][++m];
                        System.out.print(flag + " ");
                    }

                }else {

                    while (m != 0 && n != (x-1)){
                        flag = arr[++n][--m];
                        System.out.print(flag + " ");
                    }

                }

            }else if (m == 0 || m == (y-1)){
                if (n == (x-1)){
                    ++m;
                }else {
                    ++n;
                }
                flag = arr[n][m];
                System.out.print(flag+ " ");
                if (n == 0 ||  m == (y-1)){
                    while (n != x-1){
                        flag = arr[++n][--m];
                        System.out.print(flag + " ");
                    }

                }else {
                    while (n != 0 && m != (y-1)){
                        flag = arr[--n][++m];
                        System.out.print(flag + " ");
                    }
                }

            }



        }
    }
}
