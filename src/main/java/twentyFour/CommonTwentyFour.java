package twentyFour;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class CommonTwentyFour {

    private static final BigDecimal FLAG = new BigDecimal(24);

    private static final BigDecimal ERROR = new BigDecimal("0.001");

    private static final char SPLIT = ',';

    private static Integer i = 0;

    private static final List<Character> SYMBOL = Arrays.asList('+', '-', '_', '*', '/', '\\');

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        int n = 10; // 元素范围为1-10
        int r = 4; // 选择的次数
        //返回去重的组合数
        List<List<Integer>> combinations = generateCombinationsWithRepetitionIterative(n, r);
//        System.out.println(combinations);
        int count = 0;
        for (List<Integer> combination : combinations) {
            List<String> polishExpList = first(combination);
            boolean flag = changeCentre(polishExpList, FLAG);
            if (flag){
                count++;
            }
        }
//        System.out.println(count);

        long end = System.currentTimeMillis();
        System.out.println("time:"+(end-start));
    }





    private static boolean changeCentre(List<String> polishExpList, BigDecimal threshold) {
        Stack<BigDecimal> stack = new Stack<>();
        flag:
        for (String polishExp : polishExpList) {
            stack.clear();
            char[] chars = polishExp.toCharArray();
            StringBuilder value = new StringBuilder();
            int numCount = 0;
            for (int i = 0; i < chars.length; i++) {
                char aChar = chars[i];
                if (aChar == SPLIT) {
                    BigDecimal num = new BigDecimal(value.toString());
                    value = new StringBuilder();
                    stack.push(num);
                    numCount++;
                } else if (SYMBOL.contains(aChar) && numCount >= 2) {
                    BigDecimal x = stack.pop();
                    BigDecimal y = stack.pop();
                    BigDecimal num = null;
                    switch (aChar) {
                        case '+':
                            num = x.add(y);
                            break;
                        case '-':
                            num = x.subtract(y);
                            break;
                        case '_':
                            num = y.subtract(x);
                            break;
                        case '*':
                            num = x.multiply(y);
                            break;
                        case '/':
                            if (y.compareTo(BigDecimal.ZERO) == 0) {
                                continue flag;
                            }
                            num = x.divide(y, 8, RoundingMode.HALF_UP);
                            break;
                        case '\\':
                            if (x.compareTo(BigDecimal.ZERO) == 0) {
                                continue flag;
                            }
                            num = y.divide(x, 8, RoundingMode.HALF_UP);
                            break;
                    }
                    stack.push(num);
                    numCount--;
                } else {
                    value.append(aChar);
                }
            }

            if (stack.size() != 1) {
                throw new RuntimeException("数量异常");
            }
            BigDecimal res = stack.pop();



            if (isWithinError(threshold, res, 5)) {
//                System.out.println(polishExp);
                return true;
            }
        }
        return false;
    }

    private static List<String> first(List<Integer> combination) {
        List<String> res = new ArrayList<>();

        Set<String> list = getFourTuple(combination);
        StringBuilder sb = new StringBuilder();
        for (String tuple : list) {
            sb.append(tuple).append(",");
            List<String> itemList = addValue3(sb.toString());
            res.addAll(itemList);
            sb.delete(0, sb.length());

            String[] split = tuple.split(",");
            sb.append(split[0]).append(",");
            sb.append(split[1]).append(",");
            List<String> list1 = addValue2(sb.toString());

            sb.delete(0, sb.length());
            sb.append(split[2]).append(",");
            sb.append(split[3]).append(",");
            List<String> list2 = addValue2(sb.toString());

            List<String> strings = addValueList(list1, list2);
            res.addAll(strings);

            sb.delete(0, sb.length());
        }
        return res;
    }

    private static Set<String> getFourTuple(List<Integer> combination) {
        Set<String> distinctSet = new HashSet<>();
        for (int i = 0; i < combination.size(); i++) {
            for (int j = 0; j < combination.size(); j++) {
                if (i == j){
                    continue;
                }
                for (int k = 0; k < combination.size(); k++) {
                    if (k == j || k == i){
                        continue;
                    }
                    for (int l = 0; l < combination.size(); l++) {
                        if (l == j || l == i || l == k){
                            continue;
                        }
                        distinctSet.add(combination.get(i) + "," + combination.get(j) + "," + combination.get(k) + "," + combination.get(l));
                    }
                }
            }
        }
        return distinctSet;
    }

    private static List<String> addValueList(List<String> list1,List<String> list2) {
        List<String> res = new ArrayList<>();
        for (int x = 0; x < SYMBOL.size(); x++) {
            Character symbolX = SYMBOL.get(x);
            for (String t1 : list1) {
                for (String t2 : list2) {
                    res.add(t1 + t2 + symbolX);
                    i++;
                }
            }
        }
        return res;
    }




    private static List<String> addValue2(String value) {
        List<String> res = new ArrayList<>();
        for (int x = 0; x < SYMBOL.size(); x++) {
            Character symbolX = SYMBOL.get(x);
            res.add(value + symbolX);
            i++;
        }
        return res;
    }


    private static List<String> addValue3(String value) {
        List<String> res = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append(value);
//        if (value.equals("8,3,3,8,")){
//            System.out.println();
//        }
        for (int x = 0; x < SYMBOL.size(); x++) {
            Character symbolX = SYMBOL.get(x);
            sb.append(symbolX);
            for (int y = 0; y < SYMBOL.size(); y++) {
                Character symbolY = SYMBOL.get(y);
                sb.append(symbolY);
                for (int z = 0; z < SYMBOL.size(); z++) {
                    Character symbolZ = SYMBOL.get(z);
                    sb.append(symbolZ);
                    res.add(sb.toString());

                    sb.delete(sb.length() - 1,sb.length());
                    i++;
                }
                sb.delete(sb.length() - 1,sb.length());
            }
            sb.delete(sb.length() - 1,sb.length());
        }
        return res;
    }


    // 判断两个 BigDecimal 对象之间的误差是否小于等于指定精度
    private static boolean isWithinError(BigDecimal a, BigDecimal b, int precision) {

        // 设置精度
        a = a.setScale(precision, RoundingMode.HALF_UP);
        b = b.setScale(precision, RoundingMode.HALF_UP);

        // 计算绝对值差
        BigDecimal difference = a.subtract(b).abs();

        // 比较与误差阈值的大小关系
        return difference.compareTo(ERROR) <= 0;
    }

    public static List<List<Integer>> generateCombinationsWithRepetitionIterative(int n, int r) {
        List<List<Integer>> result = new ArrayList<>();
        int[] current = new int[r];
        while (true) {
            result.add(createCombination(current));

            int i;
            //相当于后面的值循环完，在循环前一个
            for (i = r - 1; i >= 0 && current[i] == n - 1; i--) {
                // Increment the rightmost element that is not at its maximum value
            }

            //所有都循环完了
            if (i < 0) {
                break; // All elements have reached their maximum value
            }

            //i循环的节点++
            current[i]++;

            //每次
            for (int j = i + 1; j < r; j++) {
                current[j] = current[i];
            }
        }

        return result;
    }

    private static List<Integer> createCombination(int[] indices) {
        List<Integer> combination = new ArrayList<>();
        for (int index : indices) {
            combination.add(index + 1); // Adjust indices to be in the range 1-10
        }
        return combination;
    }
}
