import java.util.Arrays;


public class Matrix implements IMatrix {
	private int M ;             // number of rows
	private int N ;             // number of columns
	private double[][] data;   // M-by-N array
	
	private double[] center = {0,0,0};
	
	public void setCenter(double x, double y, double z){
		center[0] = x;
		center[1] = y;
		center[2] = z;
//		center[0] = -x;
//		center[1] = -y;
//		center[2] = -z;
	}
	public double getCenterX(){
		return center[0];
	}
	public double getCenterY(){
		return center[1];
	}
	public double getCenterZ(){
		return center[2];
	}

	public int getM(){
		return M;
	}
	public int getN(){
		return N;
	}
	public Matrix(int M, int N) {
		this.M = M;
		this.N = N;
		data = new double[M][N];
	}

	public Matrix(double[][] data) {
		M = data.length;
		N = data[0].length;
		this.data = new double[M][N];
		for (int i = 0; i < M; i++)
			for (int j = 0; j < N; j++)
				this.data[i][j] = data[i][j];
	}

	@Override
	public void identity() {
		// TODO Auto-generated method stub
		if(M != N) throw new RuntimeException("Illegal matrix dimensions.");

		for (int i = 0; i < N; i++){
			Arrays.fill(data[i], 0);
			this.data[i][i] = 1;
		}
	}

	@Override
	public void set(int row, int col, double value) {
		// TODO Auto-generated method stub
		if(row >= M || col >= N)  throw new RuntimeException("Illegal matrix dimensions.");
		data[row][col] = value;
	}

	@Override
	public double get(int row, int col) {
		// TODO Auto-generated method stub
		if(row >= M || col >= N)  throw new RuntimeException("Illegal matrix dimensions.");
		return data[row][col];
	}

	public Matrix getMaxItemEachRow(){
		double[][]  newData = new double[M][1];
		for(int i=0;i<M;i++){
			newData[i][0] = this.data[i][0];
			for(int j=0; j<N;j++){
				newData[i][0] = newData[i][0]<this.data[i][j]?this.data[i][j]:newData[i][0];
			}
		}
		return new Matrix(newData);
	}

	public Matrix getMinItemEachRow(){
		double[][]  newData = new double[M][1];
		for(int i=0;i<M;i++){
			newData[i][0] = this.data[i][0];
			for(int j=0; j<N;j++){
				newData[i][0] = newData[i][0]>this.data[i][j]?this.data[i][j]:newData[i][0];
			}
		}
		return new Matrix(newData);
	}

	/**
	 * Get a single column
	 * @param col the index of column. The Index begin at 0
	 * @return A M*1 Matrix
	 */
	public Matrix getCol(int col){
		double[][] colTmp = new double[M][1];
		for(int i=0;i<M;i++){
			colTmp[i][0]=data[i][col];
		}
		return new Matrix(colTmp);
	}

	@Override
	public void translate(double x, double y, double z) {
		// TODO Auto-generated method stub
		if(M!=4 || N!=4 ) throw new RuntimeException("Illegal matrix dimensions.");
		this.data[0][3] += x;
		this.data[1][3] += y;
		this.data[2][3] += z;
		

	}

	double data1[] = null;
	double data2[] = null;
	@Override
	public void rotateX(double radians) {

		if(data1 == null){
			data1 = new double[N];
		}
		if(data2 == null){
			data2 = new double[N];
		}
		Arrays.fill(data1, 0);
		Arrays.fill(data2, 0);
		
		this.translate(-center[0], -center[1], -center[2]);
		if(M!=4 || N!=4 ) throw new RuntimeException("Illegal matrix dimensions.");
//		double data1[] = new double[N];
//		double data2[] = new double[N];
		for(int i=0; i<N; i++){
			data1[i] = Math.cos(radians)* this.data[1][i] + -Math.sin(radians)* this.data[2][i] ;
			data2[i] = Math.sin(radians)* this.data[1][i] + Math.cos(radians)* this.data[2][i];
		}
		
//		data[1] = data1;
//		data[2] = data2;
		
		for(int i=0;i<data[1].length;i++){
			data[1][i] = data1[i];
			data[2][i] = data2[i];
		}
//		data[1] = data1.clone();
//		data[2] = data2.clone();
		this.translate(center[0], center[1], center[2]);
	}

	@Override
	public void rotateY(double radians) {
		if(data1 == null){
			data1 = new double[N];
		}
		if(data2 == null){
			data2 = new double[N];
		}
		Arrays.fill(data1, 0);
		Arrays.fill(data2, 0);
			
		
		this.translate(-center[0], -center[1], -center[2]);
		if(M!=4 || N!=4 ) throw new RuntimeException("Illegal matrix dimensions.");
//		double data1[] = new double[N];
//		double data2[] = new double[N];

		for(int i=0; i<N; i++){
			data1[i] = Math.cos(radians)* this.data[0][i] + Math.sin(radians)* this.data[2][i] ;
			data2[i] = -Math.sin(radians)* this.data[0][i] + Math.cos(radians)* this.data[2][i];
		}


//		data[0] = data1;
//		data[2] = data2;
		
		for(int i=0;i<data[1].length;i++){
			data[0][i] = data1[i];
			data[2][i] = data2[i];
		}
		
//		data[0] = data1.clone();
//		data[2] = data2.clone();

		this.translate(center[0], center[1], center[2]);
	}

	@Override
	public void rotateZ(double radians) {
		if(data1 == null){
			data1 = new double[N];
		}
		if(data2 == null)
			data2 = new double[N];
		Arrays.fill(data1, 0);
		Arrays.fill(data2, 0);
		
		this.translate(-center[0], -center[1], -center[2]);
		if(M!=4 || N!=4 ) throw new RuntimeException("Illegal matrix dimensions.");
//		double data1[] = new double[N];
//		double data2[] = new double[N];
		for(int i=0; i<N; i++){
			data1[i] = Math.cos(radians)* this.data[0][i] + -Math.sin(radians)* this.data[1][i] ;
			data2[i] = Math.sin(radians)* this.data[0][i] + Math.cos(radians)* this.data[1][i];
		}
		//		for(int i=0;i<N;i++){
		//			this.data[0][i] = data1[i];
		//			this.data[1][i] = data2[i];
		//		}
		//		data[0] = Arrays.copyOf(data1, data1.length);
		//		data[1] = Arrays.copyOf(data2, data2.length);
		
//		data[0] = data1;
//		data[1] = data2;
		
		for(int i=0;i<data[1].length;i++){
			data[0][i] = data1[i];
			data[1][i] = data2[i];
		}
		
//		data[0] = data1.clone();
//		data[1] = data2.clone();
		this.translate(center[0], center[1], center[2]);

	}

	@Override
	public void scale(double x, double y, double z) {
		// TODO Auto-generated method stub
		if(M!=4 || N!=4 ) throw new RuntimeException("Illegal matrix dimensions.");
		this.translate(-this.getCenterX(), -this.getCenterY(),-this.getCenterZ());
		
		this.data[0][0] *=x;
		this.data[1][1] *=y;
		this.data[2][2] *=z;
//		this.center[0] *=x;
//		this.center[1] *=y;
//		this.center[2] *=z;
		this.translate(this.getCenterX(), this.getCenterY(),this.getCenterZ());

	}

	public void scale(double s){
		this.scale(s,s,s);
	}

	@Override
	public void leftMultiply(Matrix other) {
		// TODO Auto-generated method stub
		Matrix A = this;
		if (A.N != other.M) throw new RuntimeException("Illegal matrix dimensions.");
		double[][] newData = new double[A.M][other.N];
		for (int i = 0; i < A.M; i++)
			for (int j = 0; j < other.N; j++)
				for (int k = 0; k < A.N; k++)
					newData[i][j] += (A.data[i][k] * other.data[k][j]);
		//        for(int i=0;i<M;i++)
		//        	for(int j=0;j<N;j++){
		//        		this.data[i][j] = newData[i][j];
		//        	}
		this.data = newData;
		this.N = other.N;
	}

	@Override
	public void rightMultiply(Matrix other) {
		// TODO Auto-generated method stub
		Matrix A = other;
		if (A.N != this.M) throw new RuntimeException("Illegal matrix dimensions.");
		double[][] newData = new double[A.M][this.N];
		for (int i = 0; i < A.M; i++)
			for (int j = 0; j < this.N; j++)
				for (int k = 0; k < A.N; k++)
					newData[i][j] += (A.data[i][k] * this.data[k][j]);
		//        for(int i=0;i<M;i++)
		//        	for(int j=0;j<N;j++){
		//        		this.data[i][j] = newData[i][j];
		//        	}
		this.data = newData;
		this.M = other.M;

	}

	@Override
	public void transform(double[] src, double[] dst) {
		// TODO Auto-generated method stub
		if(src.length !=3 || dst.length !=3 || this.M != 4 || this.N != 4) 
			throw new RuntimeException("Illegal matrix dimensions.");
		Matrix A = this;
		for (int i = 0; i < A.M-1; i++){
			dst[i] = 0;
			for (int j = 0; j < 1; j++){
				for (int k = 0; k < A.N-1; k++)
					dst[i] += (A.data[i][k] * src[k]);
				dst[i] +=A.data[i][A.N-1] * 1;
			}
		}			
				
	}

	public void transform(Matrix src, Matrix dst){
		if(src.M !=3 || dst.M != 3)
			throw new RuntimeException("Illegal matrix dimensions.");
		dst.toHomogeneousMatrix();
		src.toHomogeneousMatrix();

		Matrix A = this;
		Matrix other = src;
		
		if (A.N != other.M) throw new RuntimeException("Illegal matrix dimensions.");
		for (int i = 0; i < A.M; i++)
			for (int j = 0; j < other.N; j++){
				dst.data[i][j] = 0;
				for (int k = 0; k < A.N; k++)
					dst.data[i][j] += (A.data[i][k] * other.data[k][j]);
			}
//		System.out.println(this.M+" "+this.N+" "+other.M+" "+other.N);

		dst.fromHomogeneousMatrix();
		src.fromHomogeneousMatrix();

	}

	public void toHomogeneousMatrix(){

		double[][] newData = new double[M+1][N];
		for(int i=0;i<M;i++){
			newData[i] = data[i];
		}
//		if(N>1)
//			Arrays.fill(newData[M], 0, N-2, 0);
//		newData[M][N-1] = 1;
		Arrays.fill(newData[M],1);
		this.M++;
		this.data = newData;
	}

	
	public void fromHomogeneousMatrix(){

		this.M--;
		this.data[M] = null;
	}

	public void show() {
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < N; j++) 
				System.out.printf("%4.2f ", data[i][j]);
			System.out.println();
		}
	}


}
