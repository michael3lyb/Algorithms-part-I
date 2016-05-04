/******************************************************************************
 *  Data: 2016/05/03
 *  Compilation:  javac Percolation.java
 *  Execution:    java Percolation < input10.txt
 * use for PercolationStats.java
 * Dependencies: WeightedQuickUnionUF.java
 *
 *  http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
 *  percolation modle for Monte Carlo simulation
 *
 ******************************************************************************/
// checkstyle-algs4 Percolation.java

// debug1:
// when it's percolated, every site which connected with bottom 
// virtual site is connected with the top virtual site
// that will make wrong result of isFull.

// solve1:
// isFullArray and isFullVerseArray to keep track if touch 
// the top or bottom line, and update root state


import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    //sign for each site if it is open, true is opne ,false is not
    private boolean[] isOpen; 
    private boolean[] isFullArray;  //is connected with top virtual site
    private boolean[] isFullVerseArray;   //is connected with bottom virtual site
    private boolean ifPerculate;
    private int num;
    private WeightedQuickUnionUF uf;

     // create N-by-N grid, with all sites blocked
    public Percolation(int N) {
        if (N <= 0) throw new IllegalArgumentException();
        ifPerculate = false;
        uf = new WeightedQuickUnionUF(N*N+1);
        num = N;
        isOpen = new boolean[N*N+1]; 
        isFullArray = new boolean[N*N+1]; 
        isFullVerseArray = new boolean[N*N+1]; 
        for (int i = 1; i <= N*N; i++) {
            isOpen[i] = false;
            isFullArray[i] = false;
            isFullVerseArray[i] = false;
        }

// original method
        // virtual site can make backwash issue which misktakly get wrong anwser 
        // for isFull when Percolated (bottom virtual site connect some wrong site)

        //0 is the top virtul site; N*N+1 is the bottom virtual site,
        //union first line to top virtual site, 
        // union last line to bottom virtual site
        // for (int j = 1; j <= N; j++) {
        //     uf.union(j, 0);
        //     uf.union(N*(N-1)+j, N*N+1);
        // }
    }

    // validate that i,j is a valid index
    private void validate(int i, int j) {
        if (i <= 0 || i > num) {
            throw new IndexOutOfBoundsException("index "+i+" of (" 
                + i + "," + j + ") is exceed the scope");
        }
        if (j <= 0 || j > num) {
            throw new IndexOutOfBoundsException("index "+j+"of (" 
                + i + "," + j + ") is exceed the scope");
        }
    }

    // if i,j is a valid index
    private boolean ifValidate(int i, int j) {
        return (i > 0 && i <= num) && (j > 0 && j <= num);
    }


    // open site (row i, column j) if it is not open already
    public void open(int i, int j)          
    {
        validate(i, j);
        boolean topFlag = false;
        boolean botFlag = false;
        int index = (i-1)*num+j;
        if (!isOpen[index]) {
            isOpen[index] = true;


            if (ifValidate((i-1), j)) {
                if (isOpen(i-1, j)) {
                    if (isFullArray[uf.find((i-2)*num+j)]) {
                        topFlag = true;
                    }
                    if (isFullVerseArray[uf.find((i-2)*num+j)]) {
                        botFlag = true;
                    }
                    uf.union(index, (i-2)*num+j);
                }
            } 
            //downConnect
            if (ifValidate((i+1), j)) {        
                if (isOpen(i+1, j)) {
                    if (isFullArray[uf.find((i)*num+j)]) {
                        topFlag = true;
                    }
                    if (isFullVerseArray[uf.find((i)*num+j)]) {
                        botFlag = true;
                    }

                    uf.union(index, (i)*num+j);
                }
            }  
            //leftConnect
            if (ifValidate(i, j-1)) {                
                if (isOpen(i, j-1)) {
                    if (isFullArray[uf.find((i-1)*num+(j-1))]) {
                        topFlag = true;
                    }
                    if (isFullVerseArray[uf.find((i-1)*num+(j-1))]) {
                        botFlag = true;
                    }

                    uf.union(index, (i-1)*num+(j-1));
                }
            }       
            //rightConnect
            if (ifValidate(i, j+1)) {        
                if (isOpen(i, j+1)) {
                    if (isFullArray[uf.find((i-1)*num+(j+1))]) {
                        topFlag = true;
                    }
                    if (isFullVerseArray[uf.find((i-1)*num+(j+1))]) {
                        botFlag = true;
                    }

                    uf.union(index, (i-1)*num+(j+1));
                }
            }


            if (!isFullArray[uf.find(index)]) {
                if (i == 1 || topFlag) {
                    isFullArray[uf.find(index)] = true; 
                }
            }

            if (!isFullVerseArray[uf.find(index)]) {
                if (i == num || botFlag) {
                   isFullVerseArray[uf.find(index)] = true;
                }            
            }
            if ((isFullVerseArray[uf.find(index)]) 
                && (isFullArray[uf.find(index)])) {
                ifPerculate = true;
            }
        }
    }

    // is site (row i, column j) open?
    public boolean isOpen(int i, int j) {
        validate(i, j);
        return isOpen[(i-1)*num+j];
    }

    // is site (row i, column j) full?
    public boolean isFull(int i, int j) {
        validate(i, j);
        return isFullArray[uf.find((i-1)*num+j)];
    }

    private boolean isFullVerse(int i, int j) {
        validate(i, j);
        return isFullArray[uf.find((i-1)*num+j)];
    }

    // does the system percolate?
    public boolean percolates()             
    {
        return ifPerculate;
        // if (num == 1) {
        //     return isOpen(1, 1);
        // }
    }

    // test client (optional)
    public static void main(String[] args) {
        int N = StdIn.readInt();
        Percolation uf = new Percolation(N);
        while (!StdIn.isEmpty()) {
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            if (uf.isOpen(p, q)) continue;
            uf.open(p, q);
            StdOut.println(p + " " + q);
        }
        for (int i = 1; i <= N; i++) {
            StdOut.println();
            for (int j = 1; j <= N; j++) {
                if (uf.isOpen(i, j)) {
                    StdOut.print("1 ");
                }
                else {
                    StdOut.print("0 ");
                }
            }
        }
        StdOut.println();
        StdOut.println(uf.percolates());
        // StdOut.println(uf.isFull(10,2));
        // StdOut.println(uf.isFull(9,3));
             
    }
}