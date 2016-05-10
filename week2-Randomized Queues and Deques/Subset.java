/******************************************************************************
 *  Data: 2016/05/08
 *  Compilation:  javac Subset.java
 *  Execution:    echo A B C D E F G H I | java Subset 3
 *  first argument is k
 *
 * Dependencies: Percolation.java StdOut.java StdRandom.java java.util.ArrayList
 *
 * takes a command-line integer k; reads in a sequence of N strings 
 * from standard input using StdIn.readString(); 
 * and prints out exactly k of them, uniformly at random.


 *  % echo A B C D E F G H I | java Subset 3       
 *  C                                              
 *  G                                              
 *  A                                              
 *         
 *  % echo AA BB BB BB BB BB CC CC | java Subset 8
 *  BB
 *  AA
 *  BB         
 *  CC
 *  BB
 *  BB
 *  CC
 *  BB
 *
 *  % echo A B C D E F G H I | java Subset 3       
 *  E                                              
 *  F                                              
 *  G                                              
 * 
 */

//problom: Test 1: Check that only one Deque or RandomizedQueue object is created

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.ArrayList;
public class Subset {
   // unit testing
   public static void main(String[] args) {
        RandomizedQueue<String> q1 = new RandomizedQueue<String>();
        //Deque<String> q2 = new Deque<String>();

        //ArrayList<String> in = new ArrayList<String>();

        int N = Integer.parseInt(args[0]);

        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            q1.enqueue(item);
            //in.add(item);
        }
        // String[] array = new String[in.size()];
        // in.toArray(array);

        //StdRandom.shuffle(array);
        if (N <= q1.size()) {
            for (int i = 0; i < N; i++) {
                //StdOut.println(array[i]);        
                StdOut.println(q1.dequeue());   
            }              
        }
        else {
            StdOut.println("error: k is too large");
        }
   }  
}