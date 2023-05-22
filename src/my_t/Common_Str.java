package my_t;

public class Common_Str {

//    public static void main(String[] args) {
//
//        String t = "bacbabababacabababacaab";
//
//        String p = "ababaca";
//
//        char[] chart = t.toCharArray();
//        char[] charp = p.toCharArray();
//
//
//
//
//        for (int i = 0; i < chart.length; i++) {
//
//            Boolean flag = true;
//             int flagNum = i;
//            if (i+p.length() >= t.length()){
//                break;
//            }
//
//
//            for (int j = 0; j < charp.length; j++) {
//                if (chart[i] != charp[j]){
//                    flag = false;
//                    continue;
//                }
//                i++;
//            }
//
//            if (flag){
//                System.out.println(t.substring(i-p.length(),i));
//            }
//
//            i = flagNum;
//
//        }
//
//
//    }


    public static void main(String[] args) {
        String p = "ababaca";
        String t = "bacbabababacabababacaab";
        int i = t.indexOf(p);


//        for (int i = 0; i < t.length()-p.length()+1; i++) {
//
//            String str = t.substring(i, i + p.length());
//
//            if (str.equals(p)){
//                System.out.println(i+"_"+str);
//            }
//
//        }

    }
}
