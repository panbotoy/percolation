public class PercolationStats {

	/**
	 * @param args
	 */
	
	private double mean;
	private double stddev;

	private double [] prob;
	private int sampleSize;
	private int grid;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int N = Integer.valueOf(args[0]);
		int T = Integer.valueOf(args[1]);
		PercolationStats percolationStats = new PercolationStats(N, T);
		double mean = percolationStats.mean();
		double stddev = percolationStats.stddev();
		double confidenceHi = percolationStats.confidenceHi();
		double confidenceLo = percolationStats.confidenceLo();
		StdOut.printf("%-24s= %.17f\n", "mean", mean);
		StdOut.printf("%-24s= %.17f\n", "stddev", stddev);
		StdOut.printf("%-24s= %.17f, %.17f\n", "95% confidence interval", confidenceLo, confidenceHi);
	}

	public PercolationStats(int N, int T) {
		// perform T independent computational experiments on an N-by-N grid
		this.sampleSize = T;
		this.grid = N;
		if(N <= 0 || T <= 0){
			throw new IllegalArgumentException("Illegal N or T");
		}
		this.prob = new double [T];
		for(int i = 0; i < T ; i ++)
		{
			prob[i] = this.getPercolationResult();
		}
	}

	private double getPercolationResult() {
		// TODO Auto-generated method stub
		Percolation percolation = new Percolation(this.grid);
		int openedSites = 0;
		int gridI = StdRandom.uniform(1, this.grid + 1);
		int gridJ = StdRandom.uniform(1, this.grid) + 1;
		while (!percolation.percolates()){
			while(percolation.isOpen(gridI, gridJ))
			{
				gridI = StdRandom.uniform(1, this.grid + 1);
				gridJ = StdRandom.uniform(1, this.grid + 1);
			}
			percolation.open(gridI, gridJ);
			openedSites++ ;
		}
		double result = 1.0 * openedSites / (this.grid * this.grid);
		return result;
	}

	public double mean() {
		// sample mean of percolation threshold
		this.mean = StdStats.mean(this.prob);
		return this.mean;
	}

	public double stddev() {
		// sample standard deviation of percolation threshold
		this.stddev = StdStats.stddev(this.prob);
		return this.stddev;
	}

	public double confidenceLo() {
		// returns lower bound of the 95% confidence interval
		double confidenceLo = this.mean - 1.96 * this.stddev / Math.sqrt(this.sampleSize);
		return confidenceLo;
	}

	public double confidenceHi() {
		// returns upper bound of the 95% confidence interval
		double confidenceHi = this.mean + 1.96 * this.stddev / Math.sqrt(this.sampleSize);
		return confidenceHi;
	}
}
