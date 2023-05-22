package my_t;

public class My_T {


    public static void main(String[] args) {
        String p = "006005002";

        boolean flag = test(p);
        System.out.println(flag);
    }

    private static boolean test(String p) {

        if (p == null){
            return false;
        }
        char[] chars = p.toCharArray();

        StringBuffer first = new StringBuffer();
        int flag = 0;
        StringBuffer flagStr = new StringBuffer();
        int j = 0;
        Long num = 0l;


        while (flag < chars.length-1){

            for (int i = j; i < chars.length; i++) {

                if (flagStr.length() > 0 && chars[i] != '0'){

                    if (flagStr.indexOf(String.valueOf(chars[i])) == 0){
                        flagStr.delete(0,1);
                        if (flagStr.length() == 0 && i < chars.length-1){
                            num-=1;
                            flagStr.append(num);
                            j = i+1;
                            break;
                        }
                        if (i == chars.length-1){
                            return true;
                        }
                    }else {
                        flag+=1;
                        flagStr.delete(0,flagStr.length());
                        j = flag;
                        break;
                    }

                }else if (flagStr.length() == 0 && (chars[i] != '0' || first.length() != 0)){
                    first.append(chars[i]);
                    if (flagStr.length() != 0) {
                        flagStr.delete(0, flagStr.length());
                    }
                    num = Long.valueOf(first.toString())-1;
                    flagStr.append(num);
                    flag = i;

                }


            }

        }


        return false;
    }
}
