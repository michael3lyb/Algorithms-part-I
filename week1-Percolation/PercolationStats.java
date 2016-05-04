/******************************************************************************
 *  Data: 2016/05/03
 *  Compilation:  javac PercolationStats.java
 *  Execution:    java PercolationStats 200 100
 *  first argument is N(N_by_N grid), second is T(experiment times)
 *
 * Dependencies: Percolation.java StdOut.java StdRandom.java StdStats.java
 *
 *  http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
 *  Monte Carlo simulation to estimate the percolation threshold
 *
 *    % java PercolationStats 200 100
 *   mean                    = 0.5929934999999997
 *   stddev                  = 0.00876990421552567
 *   95% confidence interval = 0.5912745987737567, 0.5947124012262428
 *
 *   % java PercolationStats 200 100
 *   mean                    = 0.592877
 *   stddev                  = 0.009990523717073799
 *   95% confidence interval = 0.5909188573514536, 0.5948351426485464
 *

 *   % java PercolationStats 2 10000
 *   mean                    = 0.666925
 *   stddev                  = 0.11776536521033558
 *   95% confidence interval = 0.6646167988418774, 0.6692332011581226
 *
 *   % java PercolationStats 2 100000
 *   mean                    = 0.6669475
 *   stddev                  = 0.11775205263262094
 *   95% confidence interval = 0.666217665216461, 0.6676773347835391
 *
 ******************************************************************************/

// checkstyle-algs4 PercolationStats.java

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    // perform T independent experiments on an N-by-N grid
    private int num; //size of grid
    private int times; // experiment times;
    private double[] X;
    //private Percolation percolation;

    public PercolationStats(int N, int T) {
        num = N;
        times = T;
        X = new double[T];
        if (N <= 0 || T <= 0) throw new IllegalArgumentException();
        experiment();
    }

    private void experiment() {
        int randomRow = 0;
        int randomCol;
        int c = 0;
        
        double b = num*num;

        for (int i = 0; i < times; i++) {
            Percolation percolation = new Percolation(num);
            while (!percolation.percolates()) {
                randomRow = StdRandom.uniform(num)+1;
                randomCol = StdRandom.uniform(num)+1;
                percolation.open(randomRow, randomCol);  
            }
            c = 0;
            for (int j = 1; j <= num; j++) {
                for (int o = 1; o <= num; o++) {
                    if (percolation.isOpen(j, o)) {
                        c++;
                    }
                }
                
            }
            //StdOut.println(c);
            X[i] = c/b;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        
        return StdStats.mean(X);
    }   

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(X);
    }   

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean()-1.96*stddev()/Math.sqrt(times); 
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean()+1.96*stddev()/Math.sqrt(times); 
    }

    // test client (described below)
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats exp = new PercolationStats(N, T);
        //exp.experiment();
        StdOut.println("mean                    =" + exp.mean());
        StdOut.println("stddev                  =" + exp.stddev());
        StdOut.println("95% confidence interval =" + exp.confidenceLo() 
                +", "+ exp.confidenceHi());
    }
}