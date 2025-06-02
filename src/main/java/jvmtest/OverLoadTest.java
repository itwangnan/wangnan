package jvmtest;

import java.io.Serializable;

/**
 * 编译期间选择静态分派目标
 */
public class OverLoadTest {
    public static void sayHello(Object arg) {
        System.out.println("hello Object");
    }
    public static void sayHello(Object... arg) {
        System.out.println("hello Object...");
    }
    public static void sayHello(int arg) {
        System.out.println("hello int");
    }
    public static void sayHello(int... arg) {
        System.out.println("hello int...");
    }
    public static void sayHello(long arg) {
        System.out.println("hello long");
    }
    public static void sayHello(long... arg) {
        System.out.println("hello long...");
    }
    public static void sayHello(float arg) {
        System.out.println("hello float");
    }
    public static void sayHello(float... arg) {
        System.out.println("hello float...");
    }
    public static void sayHello(double arg) {
        System.out.println("hello double");
    }
    public static void sayHello(double... arg) {
        System.out.println("hello double...");
    }
    public static void sayHello(Character arg) {
        System.out.println("hello Character");
    }
    public static void sayHello(Character... arg) {
        System.out.println("hello Character...");
    }
    public static void sayHello(char arg) {
        System.out.println("hello char");
    }
    public static void sayHello(char... arg) {
        System.out.println("hello char ...");
    }
    public static void sayHello(Serializable arg) {
        System.out.println("hello Serializable");
    }

    public static void main(String[] args) {
        //char > int > long > float > double > Character > Serializable > Object
        // > char... > int... > long... > float... > double...
        // Character... > Object...  这些和上面的基础类型变长参数是编译冲突的
        sayHello('a');
    }
}
