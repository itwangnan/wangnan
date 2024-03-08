package position;


public class GospersHackTest {

    public static void main(String[] args) {
        gospersHack(2,7);
    }

    private static int test(int x) {
        int lowbit = x & -x;

        int left = x + lowbit;
        System.out.println(Integer.toBinaryString(((x^(left))/lowbit)));
        int right = ((x^(left))/lowbit) >> 2;

        return left | right;
    }

    /**
     * Gospers Hack算法
     * @param k 多少1
     * @param n 总长度
     */
    static void gospersHack(int k, int n) {
        int cur = (1 << k) - 1;
        int limit = (1 << n);
        while (cur < limit)
        {
            // do something
            System.out.println(Integer.toBinaryString(cur));
            cur = test(cur);
        }
    }


}
