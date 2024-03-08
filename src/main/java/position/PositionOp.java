package position;

public class PositionOp {

    public static void main(String[] args) {
        System.out.println(Integer.toBinaryString(flp(0B11101000)));
        System.out.println(clp(11));
    }

    /**
     * 向下取2幂
     * @param n
     * @return
     */
    public static int flp(int n) {
        //将最高位1右边所有低位都置为1
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        //n包括最高位1和右边都是1，减去右移1，得到最高位1
        return n - (n >> 1);
    }

    /**
     * 向上取2幂
     * @param n
     * @return
     */
    public static int clp(int n) {
        //-1保证如果本身是2的幂会取到本身
        n = n - 1;
        //将最高位1右边所有低位都置为1
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        //加1得到向上2的幂
        return n + 1;
    }
}
