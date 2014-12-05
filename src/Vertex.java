public class Vertex {

	double x;
	double y;
	double z;
	double w = 1.0;
	
	public Vertex(){
	}
	
	public Vertex(double X, double Y, double Z){
		x = X;
		y = Y;
		z = Z;
	}
	
	public Vertex(double X, double Y, double Z, double W){
		x = X;
		y = Y;
		z = Z;
		w = W;
	}
}
