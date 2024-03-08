package fft;

public class FFT {


    private static final Complex ZERO = new Complex(0, 0);

    // Do not instantiate.
    private FFT() {
    }

    public static Complex[] fft3(Complex[] x) {
        int n = x.length;

        // base case
        if (n == 1) return new Complex[] { x[0] };

        // radix 2 Cooley-Tukey FFT
        if (n % 2 != 0) {
            throw new IllegalArgumentException("n is not a power of 2");
        }

        // compute FFT of even terms
        Complex[] even = new Complex[n/2];
        for (int k = 0; k < n/2; k++) {
            even[k] = x[2*k];
        }
        Complex[] evenFFT = fft3(even);

        // compute FFT of odd terms
        Complex[] odd  = even;  // reuse the array (to avoid n log n space)
        for (int k = 0; k < n/2; k++) {
            odd[k] = x[2*k + 1];
        }
        Complex[] oddFFT = fft3(odd);

        // combine
        Complex[] y = new Complex[n];
        for (int k = 0; k < n/2; k++) {
            double kth = -2 * k * Math.PI / n;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            y[k]       = evenFFT[k].plus (wk.times(oddFFT[k]));
            y[k + n/2] = evenFFT[k].minus(wk.times(oddFFT[k]));
        }
        return y;
    }

    /**
     * 递归法
     */
    public static Complex[] fft(Complex[] x) {
        int n = x.length;

        // base case
        if (n == 1) {
            return new Complex[]{x[0]};
        }

        // radix 2 Cooley-Tukey FFT
        if (n % 2 != 0) {
            throw new IllegalArgumentException("n is not a power of 2");
        }

//        double kth = 2 * Math.PI / n;

//        Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
//        Complex w = new Complex(1, 0);

        // fft of even terms
        Complex[] a0 = new Complex[n / 2];

        //偶数
        for (int k = 0; k < n / 2; k++) {
            a0[k] = x[2 * k];
        }

        Complex[] y0 = fft(a0);

        // fft of odd terms
        Complex[] a1 = new Complex[n / 2];
        //奇数
        for (int k = 0; k < n / 2; k++) {
            a1[k] = x[2 * k + 1];
        }
        Complex[] y1 = fft(a1);

        // combine
        Complex[] y = new Complex[n];



        for (int k = 0; k < n / 2; k++) {



            double kth = 2 * k * Math.PI / n;
            Complex w = new Complex(Math.cos(kth), Math.sin(kth));
            y[k] = y0[k].plus(w.times(y1[k]));
            y[k + n / 2] = y0[k].minus(w.times(y1[k]));
//            w = w.times(wk);


        }
        return y;
    }


    /**
     * 优化后
     * @param x
     * @return
     */
    public static Complex[] fft2(Complex[] x,int opt) {
        int n = x.length;

        Complex[] A = bitReverseCopy(x);

        // base case
        if (n == 1) {
            return new Complex[]{x[0]};
        }

        // radix 2 Cooley-Tukey FFT
        if (n % 2 != 0) {
            throw new IllegalArgumentException("n is not a power of 2");
        }

        //层数
        for (int s = 1; s <= log(2,n); s++){

            int m = 1 << s;

            double kth =  -2 * Math.PI / m;

            Complex wk = new Complex(Math.cos(kth), opt * Math.sin(kth));

            //下个
            for (int k = 0; k < n ; k+=m) {

                Complex w = new Complex(1, 0);


                for (int j = 0; j < m / 2 ; j++) {

                    //A[k + j + m / 2]=多加个步长
                    Complex t = A[k + j + m / 2].times(w);
                    //A[k + j]
                    Complex u = A[k+j];
                    //偶
                    A[k+j] = u.plus(t) ;
                    //奇
                    A[k+j+m / 2] = u.minus(t);

                    w = w.times(wk);

                }



            }

        }

        if (opt == 1){
            for (int i = 0; i < A.length; i++) {
                A[i] = A[i].scale(A.length);
            }
        }


        return A;
    }

    private static Complex[] bitReverseCopy(Complex[] x) {
        Complex[] A = new Complex[x.length];

        for (int k = 0; k <= x.length - 1; k++) {
            int rev = rev(k, x.length-1);
            A[rev] = x[k];
        }

        return A;
    }

    private static int rev(int k,int flag) {
        int length = Integer.toBinaryString(flag).length()-1;
        int a = 0;
        for (int i = 0; i <= length; i++) {
            a += (k>>(length-i) & 1)<<i;


        }

        return a;
    }

    public static long log(long basement,long n){
        return (long) (Math.log(n) / Math.log(basement));
    }


    /**
     * Returns the inverse FFT of the specified complex array.
     *
     * @param x the complex array
     * @return the inverse FFT of the complex array {@code x}
     * @throws IllegalArgumentException if the length of {@code x} is not a power of 2
     */
    public static Complex[] ifft(Complex[] x) {
        int n = x.length;
        Complex[] y = new Complex[n];

        // take conjugate
        for (int i = 0; i < n; i++) {
            y[i] = x[i].conjugate();
        }

        // compute forward FFT
        y = fft2(y,1);

        // take conjugate again
        for (int i = 0; i < n; i++) {
            y[i] = y[i].conjugate();
        }

        // divide by n
        for (int i = 0; i < n; i++) {
            y[i] = y[i].scale(1.0 / n);
        }

        return y;

    }

    /**
     * Returns the circular convolution of the two specified complex arrays.
     *
     * @param x one complex array
     * @param y the other complex array
     * @return the circular convolution of {@code x} and {@code y}
     * @throws IllegalArgumentException if the length of {@code x} does not equal
     *                                  the length of {@code y} or if the length is not a power of 2
     */
    public static Complex[] cconvolve(Complex[] x, Complex[] y) {

        // should probably pad x and y with 0s so that they have same length
        // and are powers of 2
        if (x.length != y.length) {
            throw new IllegalArgumentException("Dimensions don't agree");
        }

        int n = x.length;

        // compute FFT of each sequence
        Complex[] a = fft(x);
        Complex[] b = fft(y);

        // point-wise multiply
        Complex[] c = new Complex[n];
        for (int i = 0; i < n; i++) {
            c[i] = a[i].times(b[i]);
        }

        // compute inverse FFT
        return ifft(c);
    }

    /**
     * Returns the linear convolution of the two specified complex arrays.
     *
     * @param x one complex array
     * @param y the other complex array
     * @return the linear convolution of {@code x} and {@code y}
     * @throws IllegalArgumentException if the length of {@code x} does not equal
     *                                  the length of {@code y} or if the length is not a power of 2
     */
    public static Complex[] convolve(Complex[] x, Complex[] y) {
        Complex[] a = new Complex[2 * x.length];
        for (int i = 0; i < x.length; i++)
            a[i] = x[i];
        for (int i = x.length; i < 2 * x.length; i++)
            a[i] = ZERO;

        Complex[] b = new Complex[2 * y.length];
        for (int i = 0; i < y.length; i++)
            b[i] = y[i];
        for (int i = y.length; i < 2 * y.length; i++)
            b[i] = ZERO;

        return cconvolve(a, b);
    }

    // display an array of Complex numbers to standard output
    public static void show(Complex[] x, String title) {
        System.out.println(title);
        System.out.println("-------------------");
        for (int i = 0; i < x.length; i++) {
            System.out.println(x[i]);
        }
        System.out.println();
    }


    /***************************************************************************
     *  Test client.
     ***************************************************************************/

    /**
     * Unit tests the {@code FFT} class.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        int n = Integer.parseInt("8");
        Complex[] x = new Complex[n];

        // original data
        for (int i = 0; i < n; i++) {
            x[i] = new Complex(i, 0);
            x[i] = new Complex(StdRandom.uniform(-1.0, 1.0), 0);
        }
//        show(x, "x");

        // FFT of original data
        Complex[] y1 = fft(x);
        show(y1, "y1 = fft(x)");
        System.out.println("+++++++++++");
        Complex[] y2 = fft2(x,1);
        show(y2, "y2 = fft2(x)");

//        // take inverse FFT
//        Complex[] z = ifft(y);
//        show(z, "z = ifft(y)");


//        // circular convolution of x with itself
//        Complex[] c = cconvolve(x, x);
//        show(c, "c = cconvolve(x, x)");
//
//        // linear convolution of x with itself
//        Complex[] d = convolve(x, x);
//        show(d, "d = convolve(x, x)");
    }

}

