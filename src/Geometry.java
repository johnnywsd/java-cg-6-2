import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.util.LinkedList;

public class Geometry implements IGeometry {

	private double[][] vertices;
	private int[][] face;
	public Matrix matrix;
	
	public double[][] getVertices(){
		return  this.vertices;
	}
	
	public int[][] getFace(){
		return this.face;
	}
	
	private LinkedList<Geometry> childList = new LinkedList<Geometry>();
	

	public static final int NOT_INIT = -1;
	public static final int CUBE=0;
	public static final int OCTAHEDRON = 1;
	public static final int GLOBE = 2;
	public static final int SPHERE = 3;
	public static final int CYLINDER = 4;
	public static final int TORUS = 5;
	
	private int x = 0;
	private int y = 0;
	private int z = 0;
	
	public void setPosition(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public int getPositionX(){
		return this.x;
	}
	public int getPositionY(){
		return this.y;
	}
	public int getPositionZ(){
		return this.z;
	}
	
	double tmpX = 0;
	double tmpY = 0;
	double tmpZ = 0;
	public void rotateX(double radians){
		this.rotateXAux(this,radians);
	}
	private void rotateXAux(Geometry geoNode, double radians){
		geoNode.matrix.rotateX(radians);
		
		for(Geometry child : geoNode.childList){
			tmpX = child.matrix.getCenterX();
			tmpY = child.matrix.getCenterY();
			tmpZ = child.matrix.getCenterZ();
			child.matrix.setCenter(
					geoNode.matrix.getCenterX() - (child.x - geoNode.x), //- child.matrix.getCenterX(), 
					geoNode.matrix.getCenterY() - (child.y - geoNode.y), //- child.matrix.getCenterY(), 
					geoNode.matrix.getCenterZ() - (child.z - geoNode.z)  //- child.matrix.getCenterZ()
					);
//			child.matrix.rotateX(radians);	
			this.rotateXAux(child, radians);
			child.matrix.setCenter(
					tmpX, 
					tmpY, 
					tmpZ
					);
		}
	}
	public void rotateY(double radians){
		this.rotateYAux(this,radians);
	}
	private void rotateYAux(Geometry geoNode, double radians){
		geoNode.matrix.rotateY(radians);
		
//		double tmpX = 0;
//		double tmpY = 0;
//		double tmpZ = 0;
		for(Geometry child : geoNode.childList){
			tmpX = child.matrix.getCenterX();
			tmpY = child.matrix.getCenterY();
			tmpZ = child.matrix.getCenterZ();
			child.matrix.setCenter(
					geoNode.matrix.getCenterX() - (child.x - geoNode.x), 
					geoNode.matrix.getCenterY() - (child.y - geoNode.y), 
					geoNode.matrix.getCenterZ() - (child.z - geoNode.z)
					);
//			child.matrix.rotateY(radians);		
			this.rotateYAux(child, radians);
			child.matrix.setCenter(
					tmpX, 
					tmpY, 
					tmpZ
					);
		}
	}
	public void rotateZ(double radians){
		this.rotateZAux(this,radians);
	}
	private void rotateZAux(Geometry geoNode, double radians){
		geoNode.matrix.rotateZ(radians);
//		double tmpX = 0;
//		double tmpY = 0;
//		double tmpZ = 0;
		for(Geometry child : geoNode.childList){
			tmpX = child.matrix.getCenterX();
			tmpY = child.matrix.getCenterY();
			tmpZ = child.matrix.getCenterZ();
			child.matrix.setCenter(
					geoNode.matrix.getCenterX() - (child.x - geoNode.x), 
					geoNode.matrix.getCenterY() - (child.y - geoNode.y), 
					geoNode.matrix.getCenterZ() - (child.z - geoNode.z)
					);
//			child.matrix.rotateZ(radians);		
			this.rotateZAux(child, radians);
			child.matrix.setCenter(
					tmpX, 
					tmpY, 
					tmpZ
					);
		}
	}
	
	public void identity(){
		this.indentityAux(this);
	}
	private void indentityAux(Geometry geoNode){
		geoNode.matrix.identity();
		for(Geometry child : geoNode.childList){
			child.matrix.identity();
		}
	}
	
	public void scale(double s){
		this.scaleAux(this,s);
	}
	private void scaleAux(Geometry geoNode,double s){
		geoNode.matrix.scale(s);
		for(Geometry child : geoNode.childList){
			child.matrix.scale(s);
		}
	}
	
	public void scale(double x, double y, double z){
		this.scaleAux(this,x,y,z);
	}
	private void scaleAux(Geometry geoNode,double x, double y, double z){
		geoNode.matrix.scale(x,y,z);
		for(Geometry child : geoNode.childList){
			child.matrix.scale(x,y,z);
		}
	}
	
	@Override
	public void add(Geometry child) {
		
		this.childList.add(child);
		
	}


	@Override
	public Geometry getChild(int i) {
		return childList.get(i);
	}


	@Override
	public Matrix getMatrix() {
		return matrix;
	}


	@Override
	public int getNumChildren() {
		return childList.size();
	}


	@Override
	public void remove(Geometry child) {
		childList.remove(child);
	}
	

	private int type = Geometry.NOT_INIT;
	
	public int getType(){
		return this.type;
	}
	
	
	public static Geometry GeometryFactory(int TYPE){

		switch(TYPE){
		case Geometry.CUBE:
			return Geometry.CubeFactory();
		case Geometry.OCTAHEDRON:
			return Geometry.OctahedronFactory();
		case Geometry.GLOBE:
			return Geometry.GLobeFactory(25);
		case Geometry.CYLINDER:
			return Geometry.CylinderFactory(25);
		case Geometry.TORUS:
			return Geometry.TorusFactory(25, 1.0, 0.2);
		case Geometry.SPHERE:
			return Geometry.SphereFactory(25, Math.PI/4, Math.PI/4);
		default:
			return null;
		}	

	}

	public static Geometry CubeFactory(){
		Geometry rtn = new Geometry();
		rtn.type = Geometry.CUBE;
		
		rtn.vertices = 
		new double[][]
				{
		{0,1,0},{1,1,0},{1,0,0},{0,0,0},
		{1,1,0},{1,1,1},{1,0,1},{1,0,0},
		{1,1,1},{0,1,1},{0,0,1},{1,0,1},
		{0,1,1},{0,1,0},{0,0,0},{0,0,1},
		{0,0,0},{1,0,0},{1,0,1},{0,0,1},
		{0,1,1},{1,1,1},{1,1,0},{0,1,0}
				};
		
//		rtn.vertices = 
//				new double[][]
//						{
//				{-0.5,0.5,-0.5},{0.5,0.5,-0.5},{0.5,-0.5,-0.5},{-0.5,-0.5,-0.5},
//				{0.5,0.5,-0.5},{0.5,0.5,0.5},{0.5,-0.5,0.5},{0.5,-0.5,-0.5},
//				{0.5,0.5,0.5},{-0.5,0.5,0.5},{-0.5,-0.5,0.5},{0.5,-0.5,0.5},
//				{-0.5,0.5,0.5},{-0.5,0.5,-0.5},{-0.5,-0.5,-0.5},{-0.5,-0.5,0.5},
//				{-0.5,-0.5,-0.5},{0.5,-0.5,-0.5},{0.5,-0.5,0.5},{-0.5,-0.5,0.5},
//				{-0.5,0.5,0.5},{0.5,0.5,0.5},{0.5,0.5,-0.5},{-0.5,0.5,-0.5}
//						};
		rtn.face = new int[][]{
				{0,1,2,3},
				{4,5,6,7},
				{8,9,10,11},
				{12,13,14,15},
				{16,17,18,19},
				{20,21,22,23}				
		};


		rtn.matrix = new Matrix(4,4);
		rtn.matrix.identity();
		return rtn;
	}

	public static Geometry OctahedronFactory(){
		Geometry rtn = new Geometry();
		rtn.type = Geometry.OCTAHEDRON;
		rtn.vertices = 
				new double[][]
						{
				{0,0,-1},{0,-1,0},{-1,0,0},
				{1,0,0},{0,-1,0},{0,0,-1},
				{-1,0,0},{0,1,0},{0,0,-1},
				{0,0,-1},{0,1,0},{1,0,0},
				{-1,0,0},{0,-1,0},{0,0,1},
				{0,0,1},{0,-1,0},{1,0,0},
				{0,0,1},{0,1,0},{-1,0,0},
				{1,0,0},{0,1,0},{0,0,1}
						};
		rtn.face = new int[][]{
				{0,1,2},
				{3,4,5},
				{6,7,8},
				{9,10,11},
				{12,13,14},
				{15,16,17},
				{18,19,20},
				{21,22,23}
		};
		rtn.matrix = new Matrix(4,4);
		rtn.matrix.identity();
		return rtn;
	}

	public static Geometry CylinderFactory(int smoth){
		Geometry rtn = new Geometry();
		rtn.type = Geometry.CYLINDER;
		int M = smoth;
		int N = smoth;
		double u = 0;
		double v = 0;
		double rv = 0;
		rtn.vertices = new double[(M+1)*(N+1)][3];
		int index = 0;
		for(int m=0;m<=M;m++){
			for(int n=0;n<=N;n++){
				index = m+(M+1)*n;
				u = 0+2*Math.PI/smoth*m;
				v = 1.0/smoth*n;
				rv = Math.abs(v-0.0) <0.01 || Math.abs(v-1.0)<0.01 ? 0: 1;
				rtn.vertices[index][0] = Math.cos(u)*rv;
				rtn.vertices[index][1] = Math.sin(u)*rv;
				rtn.vertices[index][2] = (v-0.5)<0.001 ? -1: 1;
			}
		}
		rtn.face = new int[M*N][4];
		for(int m=0;m<M;m++){
			for(int n=0;n<N;n++){
				index = m+(M)*n;
				rtn.face[index][0] = m+(M+1)*n;
				rtn.face[index][1] = m+1+(M+1)*n;
				rtn.face[index][2] = m+1+(M+1)*(n+1);
				rtn.face[index][3] = m+(M+1)*(n+1);
			}
		}

		rtn.matrix = new Matrix(4,4);
		rtn.matrix.identity();
		return rtn;
	}

	public static Geometry SphereFactory(int smoth,double U, double V){
		Geometry rtn = new Geometry();
		rtn.type = Geometry.SPHERE;
		int M = smoth;
		int N = smoth;
		double u = 0;
		double v = 0;
		rtn.vertices = new double[(M+1)*(N+1)][3];
		int index = 0;
		for(int m=0;m<=M;m++){
			for(int n=0;n<=N;n++){
				index = m+(M+1)*n;
				u = 0+2*U/smoth*m;
				v = -V/2 + V/smoth*n;
				rtn.vertices[index][0] = Math.cos(u)*Math.cos(v);
				rtn.vertices[index][1] = Math.sin(u)*Math.cos(v);
				rtn.vertices[index][2] = Math.sin(v);
			}
		}
		rtn.face = new int[M*N][4];
		for(int m=0;m<M;m++){
			for(int n=0;n<N;n++){
				index = m+(M)*n;
				rtn.face[index][0] = m+(M+1)*n;
				rtn.face[index][1] = m+1+(M+1)*n;
				rtn.face[index][2] = m+1+(M+1)*(n+1);
				rtn.face[index][3] = m+(M+1)*(n+1);
			}
		}

		rtn.matrix = new Matrix(4,4);
		rtn.matrix.identity();
		return rtn;
	}
	public static Geometry GLobeFactory(int smoth){
		Geometry rtn = new Geometry();
		rtn.type = Geometry.GLOBE;
		int M = smoth;
		int N = smoth;
		double u = 0;
		double v = 0;
		rtn.vertices = new double[(M+1)*(N+1)][3];
		int index = 0;
		for(int m=0;m<=M;m++){
			for(int n=0;n<=N;n++){
				index = m+(M+1)*n;
				u = 0+2*Math.PI/smoth*m;
				v = -Math.PI/2 + Math.PI/smoth*n;
				rtn.vertices[index][0] = Math.cos(u)*Math.cos(v);
				rtn.vertices[index][1] = Math.sin(u)*Math.cos(v);
				rtn.vertices[index][2] = Math.sin(v);
			}
		}
		rtn.face = new int[M*N][4];
		for(int m=0;m<M;m++){
			for(int n=0;n<N;n++){
				index = m+(M)*n;
				rtn.face[index][0] = m+(M+1)*n;
				rtn.face[index][1] = m+1+(M+1)*n;
				rtn.face[index][2] = m+1+(M+1)*(n+1);
				rtn.face[index][3] = m+(M+1)*(n+1);
			}
		}

		rtn.matrix = new Matrix(4,4);
		rtn.matrix.identity();
		return rtn;
	}

	/**
	 * 
	 * @param smoth
	 * @param R 0<R<=1
	 * @param r 0<=r<R<=1
	 * @return
	 */
	public static Geometry TorusFactory(int smoth, double R, double r){
		Geometry rtn = new Geometry();
		rtn.type = Geometry.TORUS;
		int M = smoth;
		int N = smoth;
		double u = 0;
		double v = 0;
		rtn.vertices = new double[(M+1)*(N+1)][3];
		int index = 0;
		for(int m=0;m<=M;m++){
			for(int n=0;n<=N;n++){
				index = m+(M+1)*n;
				u = 0+2*Math.PI/smoth*m;
				v = 0+2*Math.PI/smoth*n;
				rtn.vertices[index][0] = Math.cos(u)*(R+r*Math.cos(v));
				rtn.vertices[index][1] = Math.sin(u)*(R+r*Math.cos(v));
				rtn.vertices[index][2] = r*Math.sin(v);
			}
		}
		rtn.face = new int[M*N][4];
		for(int m=0;m<M;m++){
			for(int n=0;n<N;n++){
				index = m+(M)*n;
				rtn.face[index][0] = m+(M+1)*n;
				rtn.face[index][1] = m+1+(M+1)*n;
				rtn.face[index][2] = m+1+(M+1)*(n+1);
				rtn.face[index][3] = m+(M+1)*(n+1);
			}
		}

		rtn.matrix = new Matrix(4,4);
		rtn.matrix.identity();
		return rtn;
	}

	public void drawCenter(Graphics g){
		Color co = g.getColor();
		g.setColor(Color.GREEN);
		int r = 10;
//		g.fillOval((int)(-r/2+this.matrix.getCenterX()), (int)(-r/2+this.matrix.getCenterX()), r, r);
		g.fillOval((int)(-r/2+this.x), (int)(-r/2+this.y), r, r);
		g.setColor(co);
	}
	
	public void drawCenter2(Graphics g){
		Color co = g.getColor();
		g.setColor(Color.GREEN);
		int r = 10;
//		g.fillOval((int)(-r/2+this.matrix.getCenterX()), (int)(-r/2+this.matrix.getCenterX()), r, r);
		g.fillOval((int)(-r/2+this.x+this.matrix.getCenterX()), (int)(-r/2+this.y+ this.matrix.getCenterY()), r, r);
		g.setColor(co);
	}

	public void drawFill(Graphics g, boolean showSkeleton,boolean showInvisibleFace){
		int offsetX =(int)(this.x - this.matrix.getCenterX());
		int offsetY =(int)(this.y - this.matrix.getCenterY());
		for(int[] oneFace: face){
			this.drawFace(g, showSkeleton,showInvisibleFace, oneFace, offsetX, offsetY, this.z);
		}
	}
	
	public void drawFillAll(Graphics g, boolean showSkeleton,boolean showInvisibleFace){
		drawFillAllAux(this,g,showSkeleton,showInvisibleFace);
	}
	private void drawFillAllAux(Geometry geoNode,Graphics g, boolean showSkeleton,boolean showInvisibleFace){
		this.drawFillOneChild(geoNode, g, showSkeleton, showInvisibleFace);
		for(Geometry child : geoNode.childList){
			this.drawFillOneChild(child, g, showSkeleton, showInvisibleFace);
		}
	}
	
	private void drawFillOneChild(Geometry geoNode,Graphics g, boolean showSkeleton,boolean showInvisibleFace){
		int offsetX =(int)(geoNode.x - geoNode.matrix.getCenterX());
		int offsetY =(int)(geoNode.y - geoNode.matrix.getCenterY());
		for(int[] oneFace: face){
			this.drawFace(g, showSkeleton,showInvisibleFace, oneFace, offsetX, offsetY, geoNode.z);
		}
	}

	private void drawFace(Graphics g, boolean showSkeleton, boolean showInvisibleFace, int[] oneFace, int x, int y, int z){

		boolean showFaceFlag = showInvisibleFace || isVisible(oneFace);
		if(!showFaceFlag){

			return;
		}


		Graphics2D   g2d   =   (Graphics2D)g;
		Color color = g2d.getColor();
		g2d.setColor(new Color(255,50,50));
		Stroke stroke = g2d.getStroke();
		Stroke bs = new BasicStroke(2f);
		g2d.setStroke(bs);

		GeneralPath path = new GeneralPath();



		double[] dst = vertices[0].clone();
		path.moveTo(dst[0]+x, dst[1]+y);
		boolean firstPoint = true;
		for(int f : oneFace){
			this.matrix.transform(vertices[f], dst);
			if(!firstPoint)
				path.lineTo(dst[0]+x, dst[1]+y);
			else{
				path.moveTo(dst[0]+x, dst[1]+y);
				firstPoint = false;
			}
		}
		path.closePath();
		g2d.fill(path);
		g2d.setColor(new Color(0,0,0));
		if(showSkeleton)
			g2d.draw(path);


		g2d.setStroke(stroke);
		g2d.setColor(color);
	}
	
	public void drawSkeletonAll(Graphics g, boolean showDash){
		this.drawSkeletonAux(this,g,showDash);
	}
	
	private void drawSkeletonAux(Geometry geoNode, Graphics g, boolean showDash){
		if(geoNode.childList.size() == 0){
			this.drawSkeletonOneChild(geoNode, g, showDash);
			return;
		}else{
			this.drawSkeletonOneChild(geoNode, g, showDash);
			for(Geometry child: geoNode.childList){
				this.drawSkeletonAux(child,g,showDash);
			}
		}
	}
	
	private void drawSkeletonOneChild(Geometry geoNode, Graphics g, boolean showDash){
		double[] dst1 = geoNode.vertices[0].clone();
		double[] dst2 = geoNode.vertices[0].clone();

		int offsetX =(int)(geoNode.x - geoNode.matrix.getCenterX());
		int offsetY =(int)(geoNode.y - geoNode.matrix.getCenterY());
		boolean dashFlag = false;
		for(int[] f: geoNode.face){
			dashFlag = showDash && !isVisible(f);
			for(int i=0;i<f.length-1;i++){
				geoNode.matrix.transform(geoNode.vertices[f[i]], dst1);
				geoNode.matrix.transform(geoNode.vertices[f[i+1]], dst2);
				if(dashFlag)
					geoNode.drawDash(g, dst1, dst2,offsetX,offsetY);
				else
					geoNode.drawLine(g, dst1, dst2,offsetX,offsetY);
			}
			geoNode.matrix.transform(geoNode.vertices[f[f.length-1]], dst1);
			geoNode.matrix.transform(geoNode.vertices[f[0]], dst2);
			if(dashFlag)
				geoNode.drawDash(g, dst1, dst2,offsetX,offsetY);
			else
				geoNode.drawLine(g, dst1, dst2,offsetX,offsetY);
		}
	}
	

	public void drawSkeleton(Graphics g, boolean showDash){
		double[] dst1 = vertices[0].clone();
		double[] dst2 = vertices[0].clone();

		int offsetX =(int)(this.x - this.matrix.getCenterX());
		int offsetY =(int)(this.y - this.matrix.getCenterY());
		boolean dashFlag = false;
		for(int[] f: face){
			dashFlag = showDash && !isVisible(f);
			for(int i=0;i<f.length-1;i++){
				this.matrix.transform(vertices[f[i]], dst1);
				this.matrix.transform(vertices[f[i+1]], dst2);
				if(dashFlag)
					this.drawDash(g, dst1, dst2,offsetX,offsetY);
				else
					this.drawLine(g, dst1, dst2,offsetX,offsetY);
			}
			this.matrix.transform(vertices[f[f.length-1]], dst1);
			this.matrix.transform(vertices[f[0]], dst2);
			if(dashFlag)
				this.drawDash(g, dst1, dst2,offsetX,offsetY);
			else
				this.drawLine(g, dst1, dst2,offsetX,offsetY);
		}
	}

	private void drawLine(Graphics g,double[] p1, double[] p2, int offsetX, int offsetY){
		Graphics2D   g2d   =   (Graphics2D)g;
		Color color = g2d.getColor();
		g2d.setColor(new Color(255,50,50));
		Stroke stroke = g2d.getStroke();
		Stroke bs = new BasicStroke(2.5f);
		g2d.setStroke(bs);

		g2d.drawLine((int)p1[0]+offsetX, (int)p1[1]+offsetY, (int)p2[0]+offsetX, (int)p2[1]+offsetY);

		g2d.setStroke(stroke);
		g2d.setColor(color);

	}
	private void drawDash(Graphics g,double[] p1, double[] p2, int offsetX, int offsetY){

		Graphics2D   g2d   =   (Graphics2D)g;
		Color color = g2d.getColor();
		g2d.setColor(Color.GRAY);
		Stroke stroke = g2d.getStroke();
		Stroke bs = new BasicStroke(1, BasicStroke.CAP_BUTT,   
				BasicStroke.JOIN_BEVEL, 0,   
				new float[]{10, 4}, 0);
		g2d.setStroke(bs);
		g2d.drawLine((int)p1[0]+offsetX, (int)p1[1]+offsetY, (int)p2[0]+offsetX, (int)p2[1]+offsetY);
		g2d.setStroke(stroke);
		g2d.setColor(color);
	}

	private boolean isVisible(int[] oneFace){
		double[] a = this.vertices[oneFace[0]].clone();
		this.matrix.transform(this.vertices[oneFace[1]], a);
		double[] b = this.vertices[oneFace[1]].clone();
		this.matrix.transform(this.vertices[oneFace[0]], b);
		double[] c = this.vertices[oneFace[2]].clone();
		this.matrix.transform(this.vertices[oneFace[2]], c);
		double[] A = vector(a,b);
		double[] B = vector(c,b);
		double z = crossProductZ(A,B);
		if(z >=0.01)
			return false;
		else
			return true;
	}

	private double[] vector(double[] A, double[] B){
		double[] rtn = A.clone();
		for(int i=0;i<rtn.length;i++){
			rtn[i] = A[i] - B[i];
		}
		return rtn;
	}

//	private double[] crossProduct(double[] A,double[] B){
//		return new double[]{
//				A[1]*B[2] - A[2]*B[1],
//				A[2]*B[0] - A[0]*B[2],
//				A[0]*B[1] - A[1]*B[0]
//		};
//	}
	private double crossProductZ(double[] A,double[] B){
		return A[0]*B[1] - A[1]*B[0];
	}



	
	
	/**
	 * @param args
	 */
	 public static void main(String[] args) {
		Geometry ge = Geometry.GeometryFactory(Geometry.CUBE);
		for(double[] p : ge.vertices)
			for(double q: p){
				System.out.print(q);
			}
	 }

}
