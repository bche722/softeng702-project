package yaujen.bankai.pointandclick;

import android.util.Log;

import java.util.LinkedList;
import java.util.PriorityQueue;

public class CustomizedQueue extends LinkedList < Integer > {
    private int size;

    public CustomizedQueue (int size) {
        super ( );
        this.size = size;
    }

    @Override
    public boolean add ( Integer o ) {
        Log.d ( "testfilter", o.toString () );
        this.addLast ( o );
        if ( this.size ( ) > this.size ) {
            this.removeFirst ();
        }
        return true;
    }

    public static int getAverage ( CustomizedQueue queue) {
        int average = 0;
        for ( Integer value : queue ) {
            average += value;
        }
        return average / queue.size ( );
    }

    public String toString(){
        String string = "";
        for ( Integer value : this ) {
            string += value + " ";
        }
        return string;
    }

}
