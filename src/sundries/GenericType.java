package sundries;

import java.util.ArrayList;

public class GenericType<T extends String>{
    public T data;
    public ArrayList<String> list;

    public <V> void test(GenericTypeCo<T,V> genericType){

    }
}