
public class Percolation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Percolation percolation = new Percolation(5);
		percolation.open(1, 2);
		percolation.open(1, 3);
		percolation.open(2, 3);
		percolation.open(2, 4);
		percolation.open(3, 4);
		percolation.open(4, 4);
		percolation.open(5, 4);
	}

	private WeightedQuickUnionUF wquf;
	private WeightedQuickUnionUF wqufNoBottom;
	private boolean openMatrix[][];
	private int percolationSize;

	public Percolation(int N) // create N-by-N grid, with all sites blocked
	{
		this.wquf = new WeightedQuickUnionUF(N * N + 2); // wquf[0] is for the pseudo top, wquf[N*N + 1] is for bottom.
		this.wqufNoBottom = new WeightedQuickUnionUF(N * N + 2);
		this.openMatrix = new boolean[N][N];             // initiate the openMatrix N*N to be false;
		this.percolationSize = N;
		
//		this.initiatesPseudoTop();
//		this.initiatesPseudoBottom();
	}

	private void initiatesPseudoTop(int i, int j) {
		// TODO Auto-generated method stub
		int percolateId = this.xyTo1(i, j);
		this.wquf.union(0, percolateId);
		this.wqufNoBottom.union(0, percolateId);
	}  
 
	private void initiatesPseudoBottom(int i, int j) {
		// TODO Auto-generated method stub
		int N = this.percolationSize;  
		int percolateId = this.xyTo1(i, j);
		this.wquf.union(N * N + 1, percolateId);
	}

	public void open(int i, int j) // open site (row i, column j) if it is not
									// already
	{
		this.checkForBound(i);
		this.checkForBound(j);
		
		boolean siteIJOpen = this.openMatrix[i-1][j-1];
		if(!siteIJOpen){
			this.openMatrix[i -1][j - 1] = true; // open the site
			this.unionNeighbors(i , j );         // union this site with its open neighbors
		}
		//System.out.println("Open site (" + i + ", " + j + ") " + " now the site is " + this.percolates());
	}

	private void unionNeighbors(int i, int j) {
		// TODO Auto-generated method stub
		boolean topSiteOpen = this.isSiteOpen(i - 1, j);
		boolean bottomSiteOpen = this.isSiteOpen(i + 1, j);
		boolean leftSiteOpen = this.isSiteOpen(i, j - 1);
		boolean rightSiteOpen = this.isSiteOpen(i, j + 1);
		if(topSiteOpen) this.unionTopSite(i , j);
		if(bottomSiteOpen) this.unionBottomSite(i , j);
		if(leftSiteOpen) this.unionLeftSite(i, j);
		if(rightSiteOpen) this.unionRightSite(i, j);
	}

	private void unionRightSite(int i, int j) {
		// TODO Auto-generated method stub
		int currentSite = this.xyTo1(i, j);
		int rightSite = this.xyTo1(i, j + 1);
		this.wquf.union(currentSite, rightSite);
		this.wqufNoBottom.union(currentSite, rightSite);
	}

	private void unionLeftSite(int i, int j) {
		// TODO Auto-generated method stub
		int currentSite = this.xyTo1(i, j);
		int leftSite = this.xyTo1(i, j - 1);
		this.wquf.union(currentSite, leftSite);
		this.wqufNoBottom.union(currentSite, leftSite);
	}

	private void unionBottomSite(int i, int j) {
		// TODO Auto-generated method stub
		int currentSite = this.xyTo1(i, j);
		int bottomSite = this.xyTo1(i + 1, j);
		this.wquf.union(currentSite, bottomSite);
		this.wqufNoBottom.union(currentSite, bottomSite);
	}

	private void unionTopSite(int i, int j) {
		// TODO Auto-generated method stub
		int currentSite = this.xyTo1(i, j);
		int topSite = this.xyTo1(i - 1, j);
		this.wquf.union(currentSite, topSite);
		this.wqufNoBottom.union(currentSite, topSite);
	}

	private int xyTo1(int i, int j) {
		// TODO Auto-generated method stub
		int gridI = i - 1;
		int gridJ = j - 1;
		int ufIndex = gridJ + 1 + gridI * this.percolationSize;
		return ufIndex;
	}

	private boolean isSiteOpen(int i, int j) {
		// TODO Auto-generated method stub
		if ( i < 1 || i > this.percolationSize) return false;
		else if ( j < 1 || j > this.percolationSize) return false;
		else return this.isOpen(i, j);
	}

	public boolean isOpen(int i, int j) // is site (row i, column j) open?
	{
		this.checkForBound(i);
		this.checkForBound(j);
		int gridI = i - 1;
		int gridJ = j - 1;
		return this.openMatrix[gridI][gridJ];
	}

	public boolean isFull(int i, int j) // is site (row i, column j) full?
	{
		// is the unionfind.find(pseudo top, current site)
		this.checkForBound(i);
		this.checkForBound(j);
		int currentSite = this.xyTo1(i, j);
		boolean isFull = this.wqufNoBottom.connected(0, currentSite);
		boolean isOpen = this.isOpen(i, j);
		return isFull && isOpen;
	}

	public boolean percolates() // does the system percolate?
	{

		// return unioinfind.find (pseudo top, pseudo bottom);
		boolean percolates = this.wquf.connected(0, this.percolationSize * this.percolationSize + 1 );
		return percolates;
	}
	
	private void checkForBound(int i ){
		if (i <= 0 || i > this.percolationSize) throw new IndexOutOfBoundsException("row index " + i + " out of bounds");
	}

}
