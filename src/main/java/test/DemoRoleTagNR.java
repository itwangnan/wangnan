package test;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import org.apache.commons.collections.CollectionUtils;
import str.ac.AcTest;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * 《自然语言处理入门》8.4.1 基于角色标注的中国人名识别
 * 配套书籍：http://nlp.hankcs.com/book.php
 * 讨论答疑：https://bbs.hankcs.com/
 *
 * @author hankcs
 * @see <a href="http://nlp.hankcs.com/book.php">《自然语言处理入门》</a>
 * @see <a href="https://bbs.hankcs.com/">讨论答疑</a>
 */
public class DemoRoleTagNR {

    public static void main(String[] args) throws Exception {
//        String allContent = "你好，申氏，谢谢";
//        Map<String, String> map = new HashMap<>();
//        map.put("姓邬小姐","邬");
//        map.put("陈，苹果12","陈");
//        map.put("姓氏舒","舒");
//        map.put("陈女士","陈");
//        map.put("小米12，张小姐","张");
//        map.put("姓李，李小姐","李");
//        map.put("洪小姐 苹果13两个色相头","洪");
//        map.put("石女士","石");
//        map.put("本人姓李","李");
//        map.put("有手链的，张", "张");
//        map.put("姓，荣，谢谢", "荣");
//        map.put("发邮政！ 姓周 女士","周");
//        map.put("姓氏张小姐","张");
//        map.put("备注：姓江","江");
//        map.put("你好，申氏，谢谢","申");
//        map.put("廖富婆","廖");
//        map.put("蔡","蔡");
//        map.put("毋小姐/毋女士","毋");
//        map.put("晴姐儿","晴");
//        map.put("姓张苹果13红色 ","张");
//        map.put("化齐燕女士", "燕");
//        map.put("骆大美女","骆");
//        map.put("蔡","蔡");
//        map.put("姓张，苹果13红色","张");
//        map.put("姓，荣，谢谢","荣");
//        map.put("陈，苹果12","陈");
//        map.put("备注：姓江","江");


        Segment segment = HanLP.newSegment().enableAllNamedEntityRecognize(true);
        CoreStopWordDictionary.FILTER = term -> {
            if(term.nature.startsWith("nr")) {//当为人名时放行，其他过滤掉
                return !CoreStopWordDictionary.contains(term.word);
            }else{
                return false;
            }
        };

        List<String> list = new ArrayList<>();
        AcTest.readFile("/Users/wangnan/IdeaProjects/wangnan/src/main/resources/data/1(1).txt",x -> {
            String surName = getSurName1(segment, x);
            if (surName == null){
                surName = getSurName2(x);
            }
            if (surName == null){
                surName = "没有识别到";
            }
            list.add(String.format("%s  ->  %s", x, surName));
        });

        String filePath = "/Users/wangnan/IdeaProjects/wangnan/src/main/resources/data/output.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : list) {
                writer.write(line);
                writer.newLine();  // 添加新行
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getSurName1(Segment segment, String x) {
        x = convet(x);
        List<Term> list = segment.seg(x);
        CoreStopWordDictionary.apply(list);

        if (CollectionUtils.isEmpty(list) || list.size() > 1){
            return null;
        }

        String word = list.get(0).word;
        return String.valueOf(word.charAt(0));
    }

    private static String getSurName2(String remark) {
        //无用字符擦除：
        remark = remark.replaceAll("：", "");
        remark = remark.replaceAll("，", "");
        remark = remark.replaceAll("/", "");
        remark = remark.replaceAll("（", "");
        remark = remark.replaceAll("）", "");
        remark = remark.replaceAll("\"", "");
        remark = remark.replaceAll("“", "");
        remark = remark.replaceAll(" ", "");
        remark = remark.replaceAll(",", "");

        List<String> beforeList = Arrays.asList("本人姓","姓氏", "免贵", "本人", "姓名", "姓");

        Character character = null;
        for (String s : beforeList) {
            Character surname = getBeforeChar(remark, s);
            if (surname == null){
                continue;
            }
            if (isChinese(surname)){
                character = surname;
            }
            break;
        }

        if (character != null){
            //没有匹配到
            return character.toString();
        }

        List<String> afterList = Arrays.asList("女士");
        for (String s : afterList) {
            Character surname = getAfterChar(remark, s);
            if (surname == null){
                continue;
            }
            if (isChinese(surname)){
                character = surname;
            }
            break;
        }


        if (character == null){
            //没有匹配到
            return null;
        }

        return character.toString();
    }

    private static Character getAfterChar(String remark, String pattern) {
        int index = remark.indexOf(pattern);
        if (index > 0){
            return remark.charAt(index - 1);
        }
        return null;
    }

    private static boolean isChinese(Character c){
        return '\u4e00' <= c && c <= '\u9fa5';
    }

    private static Character getBeforeChar(String remark, String pattern) {
        int index = remark.indexOf(pattern);
        int length = remark.length();
        if (index >= 0){
            int surnameIndex = index + pattern.length();
            return surnameIndex >= length ? '0' : remark.charAt(surnameIndex);
        }
        return null;
    }

    private static String convet(String remark) {
        //同义词替换：
        remark = remark.replace("大小姐", "女士");
        remark = remark.replace("大美女", "女士");
        remark = remark.replace("儿小姐", "女士");
        remark = remark.replace("女生", "女士");
        remark = remark.replace("美女", "女士");
        remark = remark.replace("姐儿", "女士");
        remark = remark.replace("富婆", "女士");
        remark = remark.replace("女士", "女士");
        remark = remark.replace("小妞", "女士");
        remark = remark.replace("公子", "女士");
        remark = remark.replace("姐姐", "女士");
        remark = remark.replace("先生", "女士");
        remark = remark.replace("老板", "女士");


        return remark;
    }

}
