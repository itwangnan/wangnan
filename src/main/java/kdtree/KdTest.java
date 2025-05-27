package kdtree;

import java.util.Arrays;
import java.util.List;

public class KdTest {


    public static void main(String[] args) throws InterruptedException {
        int len = 2;
        List<Double[]> list = Arrays.asList(
                getMap(6.27, 5.5),
                getMap(1.24, -2.86),
                getMap(17.05, -12.79),
                getMap(-6.88, -5.4),
                getMap(-2.96, -0.5),
                getMap(7.75, -22.68),
                getMap(10.8, -5.03),
                getMap(-4.6, -10.55),
                getMap(-4.96, 12.61),
                getMap(1.75, 12.26),
                getMap(15.31, -13.16),
                getMap(7.83, 15.7),
                getMap(14.63, -0.35)
        );

        KdTree tree = KdTree.convert(list,len);
        tree.print();
//        System.out.println(differ(getMap(-6.88, -5.4),getMap(-1d, -5d)));
//        System.out.println(differ(getMap(6.27, 5.5),getMap(-1d, -5d)));

        List<KdTreeNode> select = tree.select(3, getMap(-1d, -5d));
        select.forEach(x -> {
            System.out.println(Arrays.toString(x.getData()));
        });

        List<KdTreeNode> select2 = tree.select(4,  getMap(-1d, -5d));
        select2.forEach(x -> {
            System.out.println(Arrays.toString(x.getData()));
        });
//        res.forEach(x -> {
//            System.out.println(Arrays.toString(x));
//        });
//        List<Integer> list = new ArrayList<>();
//        for (int i = 0; i <= 1; i++) {
//            list.add(i);
//        }
//        int m = list.size() / 2;
//        System.out.println(list.subList(0,m));
//        System.out.println(list.subList(m+1,list.size()));
    }



    private static Double[] getMap(Double... arr) {
        return arr;
    }

//    private static double differ(Double[] data1, Double[] data2) {
//        BigDecimal res = BigDecimal.ZERO;
//        for (int i = 0; i < data1.length; i++) {
//            BigDecimal hypot = BigDecimal.valueOf(data1[i]).subtract(BigDecimal.valueOf(data2[i]));
//            hypot = hypot.multiply(hypot);
//            res = res.add(hypot);
//        }
//
//        return sqrt(res,2).doubleValue();
//    }
//
//    public static BigDecimal sqrt(BigDecimal value, int scale){
//        BigDecimal num2 = BigDecimal.valueOf(2);
//        int precision = 100;
//        MathContext mc = new MathContext(precision, RoundingMode.HALF_UP);
//        BigDecimal deviation = value;
//        int cnt = 0;
//        while (cnt < precision) {
//            deviation = (deviation.add(value.divide(deviation, mc))).divide(num2, mc);
//            cnt++;
//        }
//        deviation = deviation.setScale(scale, BigDecimal.ROUND_HALF_UP);
//        return deviation;
//    }





}
