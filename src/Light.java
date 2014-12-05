public class Light {
	
	double x;
	double y;
	double z;
	double w;
	double r;
	double g;
	double b;
	
	public Light(){
	}
	
	public Light(double X, double Y, double Z, double W, double R, double G, double B){
		x = X;
		y = Y;
		z = Z;
		w = W;
		r = R;
		g = G;
		b = B;
	}
	
	public String toString(){
		String result = "Light: " + x + " " + y + " " + z + " " + w + " " + r + " " + g + " " + b;
		return result;
	}

}