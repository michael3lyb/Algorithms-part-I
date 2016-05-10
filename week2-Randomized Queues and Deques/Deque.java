/******************************************************************************
 *  Compilation:  javac Deque.java
 *  Execution:    java Deque < input.txt
 *  Dependencies: StdIn.java StdOut.java
 *
 *  A generic Deque, implemented using a singly-linked list.
 *  Each stack element is of type Item.
 *
 *  This version uses a static nested class Node (to save 8 bytes per
 *  Node), whereas the version in the textbook uses a non-static nested
 *  class (for simplicity).
 *  
 *  % more tobe2.txt 
 *  to be or not to - be - - that - - - is
 *
 *  % java Deque < tobe2.txt
 *  to be not that or be (2 left on stack)
 *
 ******************************************************************************/
// Performance requirements.
//1. each deque operation in constant worst-case time.
//2.A deque containing N items must use at most 48N + 192 bytes of memory.

//problom 1: if use single link list, removeLast will cost O(n)
// because last node don't know who is sencond last node
// it need to scan from frot to end to find it
// solve : use double link list.
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;
//import java.lang.NullPointerException;

public class Deque<Item> implements Iterable<Item> {
    private Node<Item> first;    // beginning of queue
    private Node<Item> last;     // end of queue
    private int N;               // number of elements on queue
    
    // helper linked list class
    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
        private Node<Item> prev;        
    }

    /**
     * construct an empty deque
     */
   public Deque() {
        first = null;
        last  = null;
        N = 0;
   }                           
    /**
     * Returns true if this queue is empty.
     *
     * @return true if this queue is empty; false otherwise
     */
   public boolean isEmpty() {
        return (first == null || last == null);
   }              
 
    /**
     * Returns the number of items in this stack.
     *
     * @return the number of items in this stack
     */   
   public int size() {
        return N;
   } 
      
   // add the item to the front
   public void addFirst(Item item) {
        if (item == null) throw new NullPointerException("can't add null item");
        Node<Item> oldfirst = first;
        first = new Node<Item>();
        first.item = item;
        first.prev = null;
        first.next = oldfirst;
        if (isEmpty()) last = first;
        else           oldfirst.prev = first;
        N++;
   }     
   // add the item to the end
   public void addLast(Item item) {
        if (item == null) throw new NullPointerException("can't add null item");
        Node<Item> oldlast = last;
        last = new Node<Item>();
        last.item = item;
        last.next = null;
        last.prev = oldlast;
        if (isEmpty()) first = last;
        else           oldlast.next = last;
        N++;
   } 
   // remove and return the item from the front     
   public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        Item item = first.item;
        first = first.next;
        N--;
        if (isEmpty()) {
            last = null;   // to avoid loitering
        }
        else {
            first.prev = null;
        }
        return item;
   }       
   // remove and return the item from the end        
   public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        Item item = last.item;
        last = last.prev;
        
        N--;
        if (isEmpty()) {
            first = null;   // to avoid loitering 
        }
        else {
            last.next = null;              
        }
        return item;
   }            
    /**
     * Returns an iterator that iterates over the items in this queue in FIFO order.
     *
     * @return an iterator that iterates over the items in this queue in FIFO order
     */
   public Iterator<Item> iterator() {
        return new ListIterator<Item>(first);  
   }     

    // an iterator, doesn't implement remove() since it's optional
    private class ListIterator<Item> implements Iterator<Item> {
        private Node<Item> current;

        public ListIterator(Node<Item> first) {
            current = first;
        }

        public boolean hasNext()  { 
            return current != null;                     
        }
        public void remove()      { 
            throw new UnsupportedOperationException();  
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next; 
            return item;
        }
    }

   // unit testing
   public static void main(String[] args) {
        Deque<String> q = new Deque<String>();
        boolean change = true;
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-") && !item.equals("+")) {
                if (change) q.addFirst(item); 
                else        q.addLast(item); 
                change = !change;
            }

            else if (!q.isEmpty() && item.equals("-")) 
                StdOut.print(q.removeFirst() + " ");
            else if (!q.isEmpty() && item.equals("+")) 
                StdOut.print(q.removeLast() + " ");
        }
        StdOut.println("(" + q.size() + " left on queue)");
   }  
}