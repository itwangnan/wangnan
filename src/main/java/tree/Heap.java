package tree;

public class Heap {

    int[] inits;
    int start = 1;
    int end;

    public Heap(int[] arr) {
        this.init(arr);
    }

    private void init(int[] arr) {
        int[] inits = new int[arr.length + 1];
        System.arraycopy(arr, 0, inits, 1, arr.length);
        this.inits = inits;
        this.end = inits.length - 1;
        for (int i = inits.length-1; i > 0; i--) {
            this.floor(i);
        }
    }


    private void floor(int i) {
        if (i * 2 >= inits.length){
            return;
        }
        int cur = inits[i];

        int l = i * 2;
        int lv = inits[l];

        int r = i * 2 + 1;
        int rv = Integer.MIN_VALUE;
        if (r < inits.length){
            rv = inits[r];
        }

        if (lv <= rv && lv < cur){
            inits[i] = lv;
            inits[l] = cur;
            this.floor(l);
        }else if (rv <= lv && rv < cur){
            inits[i] = rv;
            inits[r] = cur;
            this.floor(r);
        }
    }
    public boolean isNotNull(){
        return end >= start;
    }

    public int poll(){
        if (!isNotNull()){
            return -1;
        }
        int[] arr = this.inits;
        int idx = this.start;
        int res = arr[idx];

        int lastV = arr[end];

        arr[idx] = lastV;
        floor(idx);

        end--;
        return res;
    }

}
