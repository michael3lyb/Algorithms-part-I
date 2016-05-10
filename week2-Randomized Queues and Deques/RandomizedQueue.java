/******************************************************************************
 *  Compilation:  javac RandomizedQueue.java
 *  Execution:    java RandomizedQueue < input.txt
 *  Dependencies: StdIn.java StdOut.java StdRandom.java
 *  Data files:   http://algs4.cs.princeton.edu/13stacks/tobe.txt  
 *
 *  A generic randomized queue, implemented using a linked list.
 *
 *  % java RandomizedQueue < tobe.txt 
 *  to be or not to be (2 left on queue)
 *
 ******************************************************************************/
// problem 1: random dequeue means if I use link-list, 
// it's hard to index to the item, which a array is easy to do.
// solve : use a resize array to implent.

// problem 2: dequeue: when generate a index which point to a null postion
// dequeue will return a null item
// solve 2-1 fail: if the item is null, check next item, 
// until find non-null item and return
// fail reason: this is not uniform to chose item
// solve 2-2 fail: if it a null, generate a random int again, until it's not null
// so this method doesn't need a base, it generate from [0,q.length)
// problem: in worset case can generate the int in linear time;
// solve 2-3 fail: use an extra array to track the index which is not null in q
// reason: also will involve gap in the array
// solve 2-4: don't leave gaps at all!!

// problem 3: wrap-around sometimes will make last pointer in the front of first pointer
// solve 3: In this situation, I need to choose last as the base for random index.

// resize shouldn't begin from first, begin from 0

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;
//import java.lang.NullPointerException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] q;       // queue elements
    private int N;          // number of elements on queue
    private int first;      // index of first element of queue
    private int last;       // index of next available slot
    private int[] indexQ;   // track non-null index of item in q;

    // construct an empty randomized queue
   public RandomizedQueue() {
        q = (Item[]) new Object[2];
        N = 0;
        first = 0;
        last = 0;
   }
   // is the queue empty?            
   public boolean isEmpty() {
        return N == 0; 
   }        
   // return the number of items on the queue         
   public int size() {
        return N;    
   }     
   //  public void print() {
   //      for (int i = 0; i < q.length; i++) { 
   //          StdOut.print(q[i] + " ");  
   //      }  
   // }          

    // resize the underlying array
    private void resize(int max) {
        assert max >= N;
        Item[] temp = (Item[]) new Object[max];
        for (int i = 0; i < N; i++) {
            // this is fantastic !! 
            temp[i] = q[(first + i) % q.length];
        }
        q = temp;
        first = 0;
        last  = N;
    }

   // add the item          
   public void enqueue(Item item) {
        // double size of array if necessary and recopy to front of array
        if (item == null) throw new NullPointerException("can't add null item");
        if (N == q.length) resize(2*q.length);   // double size of array if necessary
        q[last++] = item;                        // add item
        if (last == q.length) last = 0;          // wrap-around
        N++;
   }  
   // remove and return a random item         
   public Item dequeue() {

        if (isEmpty()) throw new NoSuchElementException("Queue underflow");

        int index = StdRandom.uniform(N);
        index = (first + index) % q.length;
        // if (first > last) {       
        //     index = index + first;
        //     if (index >= q.length) {
        //         index = index - q.length;
        //     }
 
        // }
        // else if (first < last) {        
        //     index = index + first;
        // }

        Item item = q[index];
        q[index] = q[first];
        q[first] = null;                            // to avoid loitering
        N--;
        first++;
        if (first == q.length) first = 0;           // wrap-around
        // shrink size of array if necessary
        if (N > 0 && N == q.length/4) resize(q.length/2); 
        return item;    
   }         

   // return (but do not remove) a random item           
   public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        int index = StdRandom.uniform(N);
        index = (first + index) % q.length;
        return q[index];
   }           
   // return an independent iterator over items in random order          
   public Iterator<Item> iterator() {
        return new ArrayIterator();    
   }        

    // an iterator, doesn't implement remove() since it's optional
    private class ArrayIterator implements Iterator<Item> {
        private int i = 0;
        private Item[] tempq = (Item[]) new Object[N];
        public ArrayIterator() {
            int k = 0;
            for (int j = 0; j < q.length; j++) {
                if (q[j] != null) {
                    tempq[k++] = q[j];
                }
            }   
            StdRandom.shuffle(tempq);
        }
        
        public boolean hasNext()  {
            return i < N;                               
        }
        public void remove()      { 
            throw new UnsupportedOperationException();  
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = tempq[(i + first) % tempq.length];
            i++;
            return item;
        }
    }

   // unit testing 
   public static void main(String[] args) {
        RandomizedQueue<String> q = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            StdOut.println();
            String item = StdIn.readString();
            if (!item.equals("-")) q.enqueue(item);
            else if (!q.isEmpty()) StdOut.println(q.dequeue() + " ");
            // StdOut.println();
            // q.print();
            // StdOut.println();
        }
        StdOut.println("(" + q.size() + " left on queue)");
    }
}