package twentyfour;

import org.apache.commons.collections.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OptimizeTwentyFour {
    private static final BigDecimal ERROR = new BigDecimal("0.0001");
    private static final int PRECISION = 4;

    private static final BigDecimal FLAG = new BigDecimal(24);

    public static class Node {

        BigDecimal bigDecimal;

        String pattern;

        List<Integer> list = new ArrayList<>();

        public BigDecimal getBigDecimal() {
            return bigDecimal;
        }

        public String getPattern() {
            return pattern;
        }

        int num = 0;


        Node(BigDecimal bigDecimal, String pattern) {
            this.bigDecimal = bigDecimal;
            this.pattern = pattern;
        }

        Node(BigDecimal bigDecimal, String pattern, List<Integer> list, Integer... newNums) {
            this.bigDecimal = bigDecimal;
            this.pattern = pattern;
            if (CollectionUtils.isNotEmpty(list)) {
                this.list.addAll(list);
            }
            Collections.addAll(this.list, newNums);

        }

        Node(BigDecimal bigDecimal, String pattern, Integer... newNums) {
            this.bigDecimal = bigDecimal;
            this.pattern = pattern;
            Collections.addAll(this.list, newNums);

        }

        @SafeVarargs
        Node(BigDecimal bigDecimal, String pattern, List<Integer>... numListList) {
            this.bigDecimal = bigDecimal;
            this.pattern = pattern;
            for (List<Integer> numList : numListList) {
                this.list.addAll(numList);
            }

        }
    }

    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        int count = 4;

        Map<Integer, String> oneMap = oneAddValue(FLAG, count);

        System.out.println((System.currentTimeMillis() - start));

        Map<Integer, String> twoMap = twoAddValue(FLAG, count);

        oneMap.putAll(twoMap);

        System.out.println((System.currentTimeMillis() - start));

//        Scanner scanner = new Scanner(System.in);
//        int acc = 1;
//        for (int i = 0; i < count; i++) {
//            acc *= numPrime(scanner.nextInt());
//        }
//        String msg = oneMap.get(acc);
//        if (msg.isEmpty()){
//            System.out.println("无解");
//        }else {
//            System.out.println(msg);
//        }
    }

    /**
     * 结果得是540
     */
    private static Map<Integer,String> oneAddValue(BigDecimal max,int count) {


        LinkedList<StringBuilder> patternList = new LinkedList<>();
        patternList.push(new StringBuilder());

        LinkedList<BigDecimal> numList = new LinkedList<>();
        numList.push(max);

        LinkedList<Integer> sumList = new LinkedList<>();
        sumList.push(1);

        LinkedList<Integer> indexList = new LinkedList<>();
        indexList.push(0);


        for (int i = count; i - 1 > 0; i--) {

            StringBuilder endIndex = patternList.peekLast();
            while (true){
                boolean flag = patternList.peek() == endIndex;

                StringBuilder sb = patternList.poll();
                BigDecimal num1 = numList.poll();
                Integer sum = sumList.poll();
                Integer index = indexList.poll();
                if (sb == null || num1 == null || sum == null){
                    //异常
                    throw new RuntimeException("队列异常");
                }

                for (int j = 1; j <= 10; j++) {

                    BigDecimal num2 = new BigDecimal(j);
                    BigDecimal bigNum;
                    BigDecimal smallNum;
                    boolean isBig = false;
                    if (num1.compareTo(num2) >= 0) {
                        bigNum = num1;
                        smallNum = num2;
                        isBig = true;
                    } else {
                        bigNum = num2;
                        smallNum = num1;
                    }
                    //质数积
                    int sumPrime = sum * numPrime(j);
                    //加
                    StringBuilder sb1 = new StringBuilder(sb);
                    indexList.addLast(strJoin(sb1, String.valueOf(j), "+",isBig,index));
                    patternList.addLast(sb1);
                    numList.addLast(bigNum.add(smallNum));
                    sumList.addLast(sumPrime);

                    //减
                    StringBuilder sb2 = new StringBuilder(sb);
                    indexList.addLast(strJoin(sb2, String.valueOf(j), "-", isBig, index));
                    patternList.addLast(sb2);
                    numList.addLast(bigNum.subtract(smallNum));
                    sumList.addLast(sumPrime);

                    //反减
                    if (!bigNum.equals(smallNum)){
                        StringBuilder sb3 = new StringBuilder(sb);
                        indexList.addLast(strJoin(sb3, String.valueOf(j), "_", isBig, index));
                        patternList.addLast(sb3);
                        numList.addLast(smallNum.subtract(bigNum));
                        sumList.addLast(sumPrime);
                    }

                    //乘
                    StringBuilder sb4 = new StringBuilder(sb);
                    indexList.addLast(strJoin(sb4, String.valueOf(j), "*",isBig,index));
                    patternList.addLast(sb4);
                    numList.addLast(bigNum.multiply(smallNum));
                    sumList.addLast(sumPrime);

                    //除
                    if (smallNum.compareTo(BigDecimal.ZERO) != 0) {
                        StringBuilder sb5 = new StringBuilder(sb);
                        indexList.addLast(strJoin(sb5, String.valueOf(j), "/",isBig,index));
                        patternList.addLast(sb5);
                        numList.addLast(bigNum.divide(smallNum, 8, RoundingMode.HALF_UP));
                        sumList.addLast(sumPrime);
                    }

                    //反除
                    if (bigNum.compareTo(BigDecimal.ZERO) != 0 && !bigNum.equals(smallNum)) {
                        StringBuilder sb6 = new StringBuilder(sb);
                        indexList.addLast(strJoin(sb6, String.valueOf(j), "\\",isBig,index));
                        patternList.addLast(sb6);
                        numList.addLast(smallNum.divide(bigNum, 8, RoundingMode.HALF_UP));
                        sumList.addLast(sumPrime);
                    }
                }

                if (flag){
                    break;
                }
            }

        }


        //最后比较是否符合要求 符合的

        Map<Integer,String> res = new HashMap<>();

        while (!patternList.isEmpty() && !numList.isEmpty() && !sumList.isEmpty()){

            StringBuilder sb = patternList.poll();
            BigDecimal bigDecimal = numList.poll();
            Integer prime = sumList.poll();
            Integer index = indexList.poll();
            bigDecimal = bigDecimal.setScale(PRECISION, RoundingMode.HALF_UP);
            if (!isInteger(bigDecimal)){
                continue;
            }
            int num = bigDecimal.intValue();
            prime *= numPrime(num);

            String str = res.get(prime);
            if (str != null){
                continue;
            }
            sb.insert(index,String.valueOf(num));
            res.put(prime,sb.toString());
        }

        return res;
    }

    private static LinkedList<Node> oneAddValue(boolean last, LinkedList<Node> list) {
        LinkedList<Node> newList = new LinkedList<>();

        for (Node node : list) {
            BigDecimal num1 = node.bigDecimal;
            String str = node.pattern;
            for (int i = 1; i <= 10; i++) {
                BigDecimal num2 = new BigDecimal(i);
                BigDecimal bigNum;
                BigDecimal smallNum;
                boolean flag = true;
                if (num1.compareTo(num2) >= 0) {
                    bigNum = num1;
                    smallNum = num2;
                } else {
                    bigNum = num2;
                    smallNum = num1;
                    flag = false;
                }

                addList(newList, new Node(bigNum.add(smallNum), strJoin(str, num2.toString(), flag, "+"), node.list, i), last, addListFun());

                addList(newList, new Node(bigNum.subtract(smallNum), strJoin(str, num2.toString(), flag, "-"), node.list, i), last, addListFun());

                addList(newList, new Node(bigNum.multiply(smallNum), strJoin(str, num2.toString(), flag, "*"), node.list, i), last, addListFun());

                addList(newList, new Node(smallNum.subtract(bigNum), strJoin(num2.toString(), str, flag, "-"), node.list, i), last, addListFun());


                if (smallNum.compareTo(BigDecimal.ZERO) != 0) {
                    addList(newList, new Node(bigNum.divide(smallNum, 8, RoundingMode.HALF_UP), strJoin(str, num2.toString(), flag, "/"), node.list, i), last, addListFun());
                }

                if (bigNum.compareTo(BigDecimal.ZERO) != 0) {
                    addList(newList, new Node(smallNum.divide(bigNum, 8, RoundingMode.HALF_UP), strJoin(num2.toString(), str, flag, "/"), node.list, i), last, addListFun());
                }

            }

        }

        return newList;
    }

    private static Map<Integer,String> twoAddValue(LinkedList<Node> list) {

        List<Node> nodeList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= i; j++) {
                BigDecimal num1 = new BigDecimal(i);
                BigDecimal num2 = new BigDecimal(j);

                nodeList.add(new Node(num1.add(num2),strJoin(num1.toString(), num2.toString(), true, "+"),i,j));

                nodeList.add(new Node(num1.subtract(num2),strJoin(num1.toString(), num2.toString(), true, "-"),i,j));

                nodeList.add(new Node(num1.multiply(num2),strJoin(num1.toString(), num2.toString(), true, "*"),i,j));

                if (i != j){
                    nodeList.add(new Node(num2.subtract(num1),strJoin(num2.toString(), num1.toString(), true, "-"),i,j));
                }

                if (num2.compareTo(BigDecimal.ZERO) != 0) {
                    nodeList.add(new Node(num1.divide(num2, 8, RoundingMode.HALF_UP),strJoin(num1.toString(), num2.toString(), true, "/"),i,j));
                }

                if (num1.compareTo(BigDecimal.ZERO) != 0 && i != j) {
                    nodeList.add(new Node(num2.divide(num1, 8, RoundingMode.HALF_UP),strJoin(num2.toString(), num1.toString(), true, "/"),i,j));
                }
            }
        }

        Map<BigDecimal, Node> bigDecimalNodeMap = nodeList.stream().collect(Collectors.toMap(x -> x.bigDecimal, Function.identity(), (x1, x2) -> x2));
        Map<Integer,String> res = new HashMap<>();

        for (Node node1 : nodeList) {
            for (Node node2 : nodeList) {
                BigDecimal num1 = node1.bigDecimal;
                BigDecimal num2 = node2.bigDecimal;
                if (num1.compareTo(num2) < 0) {
                    continue;
                }

                addList(list, new Node(num1.add(num2), strJoin(num1.toString(), num2.toString(), true, "+"), node1.list, node2.list), true, addTwoListFun());

                addList(list, new Node(num1.subtract(num2), strJoin(num1.toString(), num2.toString(), true, "-"), node1.list, node2.list), true, addTwoListFun());

                addList(list, new Node(num1.multiply(num2), strJoin(num1.toString(), num2.toString(), true, "*"), node1.list, node2.list), true, addTwoListFun());

                addList(list, new Node(num2.subtract(num1), strJoin(num2.toString(), num1.toString(), true, "-"), node1.list, node2.list), true, addTwoListFun());


                if (num2.compareTo(BigDecimal.ZERO) != 0) {
                    addList(list, new Node(num1.divide(num2, 8, RoundingMode.HALF_UP), strJoin(num1.toString(), num2.toString(), true, "/"), node1.list, node2.list), true, addTwoListFun());
                }

                if (num1.compareTo(BigDecimal.ZERO) != 0) {
                    addList(list, new Node(num2.divide(num1, 8, RoundingMode.HALF_UP), strJoin(num2.toString(), num1.toString(), true, "/"), node1.list, node2.list), true, addTwoListFun());
                }
            }
        }

        Map<Integer, Node> map = list.stream().collect(Collectors.toMap(x -> x.num, Function.identity(), (x1, x2) -> x2));

        return res;
    }


    private static Map<Integer,String> twoAddValue(BigDecimal max,int count) {

        Map<String,BigDecimal> patternAccMap = new HashMap<>();
        Map<String,Integer> patternPrimeMap = new HashMap<>();

        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= i; j++) {
                BigDecimal num1 = new BigDecimal(i);
                BigDecimal num2 = new BigDecimal(j);

                int prime = numPrime(i) * numPrime(j);

                String str = strJoin(num1.toString(), num2.toString(), true, "+");
                patternAccMap.put(str,num1.add(num2));
                patternPrimeMap.put(str,prime);

                str = strJoin(num1.toString(), num2.toString(), true, "-");
                patternAccMap.put(str,num1.subtract(num2));
                patternPrimeMap.put(str,prime);

                str = strJoin(num1.toString(), num2.toString(), true, "*");
                patternAccMap.put(str,num1.multiply(num2));
                patternPrimeMap.put(str,prime);

                if (i != j){
                    str = strJoin(num2.toString(), num1.toString(), true, "-");
                    patternAccMap.put(str,num2.subtract(num1));
                    patternPrimeMap.put(str,prime);
                }


                if (num2.compareTo(BigDecimal.ZERO) != 0) {
                    str = strJoin(num1.toString(), num2.toString(), true, "/");
                    patternAccMap.put(str,num1.divide(num2, 8, RoundingMode.HALF_UP));
                    patternPrimeMap.put(str,prime);
                }

                if (num1.compareTo(BigDecimal.ZERO) != 0 && i != j) {
                    str = strJoin(num2.toString(), num1.toString(), true, "/");
                    patternAccMap.put(str,num2.divide(num1, 8, RoundingMode.HALF_UP));
                    patternPrimeMap.put(str,prime);
                }
            }
        }

        Map<Integer,String> res = new HashMap<>();

        patternAccMap.forEach((x,y) -> {

            patternAccMap.forEach((z,k) -> {

                BigDecimal num1 = y;
                BigDecimal num2 = k;
                if (num1.compareTo(num2) < 0) {
                    return;
                }

                Integer prime1 = patternPrimeMap.get(x);
                Integer prime2 = patternPrimeMap.get(z);

                int total = prime1 * prime2;

                if (isWithinError(num1.add(num2), max)) {
                    res.put(total, strJoin(x, z, true, "+"));
                    return;
                }


                if (isWithinError(num1.subtract(num2), max)) {
                    res.put(total, strJoin(x, z, true, "-"));
                    return;
                }

                if (isWithinError(num1.multiply(num2), max)) {
                    res.put(total, strJoin(x, z, true, "*"));
                    return;
                }

                if (isWithinError(num2.subtract(num1), max)) {
                    res.put(total, strJoin(z, x, true, "-"));
                    return;
                }


                if (num2.compareTo(BigDecimal.ZERO) != 0 && isWithinError(num1.divide(num2, 8, RoundingMode.HALF_UP), max)) {
                    res.put(total, strJoin(x, z, true, "/"));
                    return;
                }

                if (num1.compareTo(BigDecimal.ZERO) != 0 && isWithinError(num2.divide(num1, 8, RoundingMode.HALF_UP), max)) {
                    res.put(total, strJoin(z, x, true, "/"));
                    return;
                }


            });
        });

        return res;
    }



    private static BiConsumer<LinkedList<Node>, Node> addListFun() {
        return (newList, node) -> {
            node.bigDecimal = node.bigDecimal.setScale(PRECISION, RoundingMode.HALF_UP);
            if (isInteger(node.bigDecimal)) {
                node.list.add(node.bigDecimal.intValue());
                node.num = numMuxPrime(node.list);
                newList.add(node);
            }
        };
    }

    private static BiConsumer<LinkedList<Node>, Node> addTwoListFun() {
        return (newList, node) -> {
            if (!isWithinError(node.bigDecimal, FLAG)) {
                return;
            }
            node.num = numMuxPrime(node.list);
            newList.add(node);
        };
    }

    private static void addList(LinkedList<Node> newList, Node node, boolean last, BiConsumer<LinkedList<Node>, Node> biConsumer) {
        if (last) {
            biConsumer.accept(newList, node);
        } else {
            newList.add(node);
        }
    }

    private static String strJoin(String str1, String str2, boolean flag, String symbol) {
        if (flag) {
            return String.format("( %s %s %s )", str1, symbol, str2);
        } else {
            return String.format("( %s %s %s )", str2, symbol, str1);
        }

    }


    private static int strJoin(StringBuilder str1, String str2,String symbol,boolean isBig,int index) {
        if (isBig){
            switch (symbol){
                case "+":
                    str1.insert(index,"("+"-"+str2+")");
                    index += 1;
                    break;
                case "-":
                    str1.insert(index,"("+"+"+str2+")");
                    index += 1;
                    break;
                case "_":
                    str1.insert(index,"("+str2+"-"+")");
                    index = index + str2.length() + 2;
                    break;
                case "\\":
                    str1.insert(index,"("+str2+"/"+")");
                    index = index + str2.length() + 2;
                    break;
                case "*":
                    str1.insert(index,"("+"/"+str2+")");
                    index += 1;
                    break;
                case "/":
                    str1.insert(index,"("+"*"+str2+")");
                    index += 1;
                    break;

            }
        }else {
            switch (symbol){
                case "+":
                    str1.insert(index,"("+str2+"+"+")");
                    index = index + str2.length() + 2;
                    break;
                case "-":
                    str1.insert(index,"("+str2+"-"+")");
                    index = index + str2.length() + 2;
                    break;
                case "_":
                    str1.insert(index,"("+"-"+str2+")");
                    index = index + 1;
                    break;
                case "\\":
                    str1.insert(index,"("+"/"+str2+")");
                    index = index + 1;
                    break;
                case "*":
                    str1.insert(index,"("+"/"+str2+")");
                    index = index + 1;
                    break;
                case "/":
                    str1.insert(index,"("+"*"+str2+")");
                    index = index + 1;
                    break;

            }
        }


        return index;
    }

    private static boolean isInteger(BigDecimal bigDecimal) {
        BigDecimal remainder = bigDecimal.remainder(BigDecimal.ONE);
        return bigDecimal.compareTo(BigDecimal.ONE) >= 0
                && bigDecimal.compareTo(BigDecimal.valueOf(10)) <= 0
                && remainder.compareTo(BigDecimal.ZERO) == 0;
    }

    // 判断两个 BigDecimal 对象之间的误差是否小于等于指定精度
    private static boolean isWithinError(BigDecimal a, BigDecimal b) {

        // 设置精度
        a = a.setScale(PRECISION, RoundingMode.HALF_UP);
        b = b.setScale(PRECISION, RoundingMode.HALF_UP);

        // 计算绝对值差
        BigDecimal difference = a.subtract(b).abs();

        // 比较与误差阈值的大小关系
        return difference.compareTo(ERROR) <= 0;
    }

    private static int numMuxPrime(List<Integer> list) {
        int res = 1;
        for (Integer num : list) {
            res *= numPrime(num);
        }
        return res;
    }

    private static int numPrime(int num) {
        int res = 0;
        switch (num) {
            case 1:
                res = 2;
                break;
            case 2:
                res = 3;
                break;
            case 3:
                res = 5;
                break;
            case 4:
                res = 7;
                break;
            case 5:
                res = 11;
                break;
            case 6:
                res = 13;
                break;
            case 7:
                res = 17;
                break;
            case 8:
                res = 19;
                break;
            case 9:
                res = 23;
                break;
            case 10:
                res = 29;
                break;

        }
        return res;
    }

}
