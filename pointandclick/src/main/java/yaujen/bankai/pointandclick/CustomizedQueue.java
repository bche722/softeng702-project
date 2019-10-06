package yaujen.bankai.pointandclick;

import java.util.LinkedList;

public class CustomizedQueue extends LinkedList < Integer > {
    private int size;

    public CustomizedQueue (int size) {
        super ( );
        this.size = size;
    }

    @Override
    public boolean add ( Integer o ) {
        this.addLast ( o );
        if ( this.size ( ) > this.size ) {
            this.removeFirst ();
        }
        return true;
    }

    public int getAverage ( ) {
        int average = 0;
        for ( Integer value : this ) {
            average += value;
        }
        return average / this.size ( );
    }

    public void setSize(int size){
        size = size==0?1:size;
        this.size = size;
    }

}
