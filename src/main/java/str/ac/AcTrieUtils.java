package str.ac;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Ac自动机工具类

 */
public class AcTrieUtils {


    /**
     * 根据Ac自动机结果剔除字符串
     * @param str  原字符串
     * @param list Ac自动机返回结果集
     * @return 剔除过的字符串
     */
    public static <V> String eliminate(String str,List<AcDoubleArrayTrie<V>.Hit<V>> list){

        StringBuilder sb = new StringBuilder(str);

        int preLen = 0;
        list = list.stream().sorted(Comparator.comparing(x -> x.begin)).collect(Collectors.toList());

        for (AcDoubleArrayTrie.Hit hit : list) {
            int len = hit.end - hit.begin;
            sb.delete(hit.begin - preLen, hit.end - preLen);
            preLen += len;
        }


        return sb.toString();
    }


    /**
     * 判断数量多的串是否包含少的
     * @param list Ac自动机返回结果集
     * @return 剔除过的字符串
     */
    public static <V> boolean ManyConFew(List<AcDoubleArrayTrie<V>.Hit<V>> list){

        return false;
    }


}
