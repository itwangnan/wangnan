package leetcode.refine.linked;

public class Linked<T> {

    T value;

    Linked<T> next;

    public Linked(T value, Linked<T> next) {
        this.value = value;
        this.next = next;
    }

    public Linked() {
    }
}
