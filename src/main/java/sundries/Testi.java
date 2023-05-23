package sundries;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Testi {


    public static void main(String[] args) throws NoSuchFieldException {

        //****************************TypeVariable************************
//        Field v = TypeTest.class.getField("v");//用反射的方式获取属性 public V v;
//        TypeVariable typeVariable = (TypeVariable) v.getGenericType();//获取属性类型
//        System.out.println("TypeVariable1:" + typeVariable);
//        System.out.println("TypeVariable2:" + Arrays.asList(typeVariable.getBounds()));//获取类型变量上界
//        System.out.println("TypeVariable3:" + typeVariable.getGenericDeclaration());//获取类型变量声明载体
//        //1.8 AnnotatedType: 如果这个这个泛型参数类型的上界用注解标记了，我们可以通过它拿到相应的注解
//        AnnotatedType[] annotatedTypes = typeVariable.getAnnotatedBounds();
//        System.out.println("TypeVariable4:" + Arrays.asList(annotatedTypes) + " : " +
//                Arrays.asList(annotatedTypes[0].getAnnotations()));
//        System.out.println("TypeVariable5:" + typeVariable.getName());

        //*********************************ParameterizedType**********************************************
//        Field list = TypeTest.class.getField("list");
//        Type genericType1 = list.getGenericType();
//        System.out.println("参数类型1:" + genericType1.getTypeName()); //参数类型1:java.util.List<T>
//
//        Field map = TypeTest.class.getField("map");
//        Type genericType2 = map.getGenericType();
//        System.out.println("参数类型2:" + genericType2.getTypeName());//参数类型2:java.util.Map<java.lang.String, T>
//
//        if (genericType2 instanceof ParameterizedType) {
//            ParameterizedType pType = (ParameterizedType) genericType2;
//            Type[] types = pType.getActualTypeArguments();
//            System.out.println("参数类型列表:" + Arrays.asList(types));//参数类型列表:[class java.lang.String, T]
//            System.out.println("参数原始类型:" + pType.getRawType());//参数原始类型:interface java.util.Map
//            System.out.println("参数父类类型:" + pType.getOwnerType());//参数父类类型:null,因为Map没有外部类，所以为null
//        }

        System.out.println((6 & (6 - 1)));
        System.out.println((8 & (8 - 1)));
    }




}
