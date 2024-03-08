package sundries;

import fft.Complex;
import fft.FFT;

public class Two {

    public static void main(String[] args) {
        test_fft();
    }

    public static void test_fft(){
        int length = 8;
        Complex[] c1 = new Complex[length];
        c1[0] = new Complex(1.0,0);
        c1[1] = new Complex(2.0,0);
        c1[2] = new Complex(1.0,0);
        c1[3] = new Complex(0.0,0);
        c1[4] = new Complex(0.0,0);
        c1[5] = new Complex(0.0,0);
        c1[6] = new Complex(0.0,0);
        c1[7] = new Complex(0.0,0);

        Complex[] c2 = new Complex[length];
        c2[0] = new Complex(3.0,0);
        c2[1] = new Complex(4.0,0);
        c2[2] = new Complex(0,0);
        c2[3] = new Complex(0,0);
        c2[4] = new Complex(0,0);
        c2[5] = new Complex(0,0);
        c2[6] = new Complex(0,0);
        c2[7] = new Complex(0,0);

        Complex[] r1 = FFT.fft2(c1,-1);
        Complex[] r2 = FFT.fft2(c2,-1);

        Complex[] r3 = new Complex[length];
        for (int i = 0; i < r1.length; i++) {
            if (r2.length <= i){
                r3[i] = r1[i];
            }else {
                r3[i] = r1[i].times(r2[i]);
            }

        }

        Complex[] re = FFT.fft2(r3,1);
        FFT.show(r1,"r1:");
        FFT.show(r2,"r2:");
        FFT.show(re,"re:");
    }

    public void test_ntt(){
        int length = 8;
        Complex[] c1 = new Complex[length];
        c1[0] = new Complex(1.0,0);
        c1[1] = new Complex(2.0,0);
        c1[2] = new Complex(1.0,0);
        c1[3] = new Complex(0.0,0);
        c1[4] = new Complex(0.0,0);
        c1[5] = new Complex(0.0,0);
        c1[6] = new Complex(0.0,0);
        c1[7] = new Complex(0.0,0);

        Complex[] c2 = new Complex[length];
        c2[0] = new Complex(3.0,0);
        c2[1] = new Complex(4.0,0);
        c2[2] = new Complex(0,0);
        c2[3] = new Complex(0,0);
        c2[4] = new Complex(0,0);
        c2[5] = new Complex(0,0);
        c2[6] = new Complex(0,0);
        c2[7] = new Complex(0,0);




    }

}
