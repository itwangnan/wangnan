package sundries;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Nest {


    public static void main(String[] args) {
//        GenericType<String> genericType = new GenericType<>();
//
//        genericType.test((x,y) -> {
//            return y;
//        });

//
//        genericType.test(new GenericTypeCo<String, List<String>>() {
//            @Override
//            public List<String> flatMap(String s, List<String> collector) {
//                return collector;
//            }
//        });

        Map<String,List<String>> map = new HashMap<>();

        map.forEach((x,y) -> {
            System.out.println(y);
        });
    }

}
