public class Sphere {

	String name;
	double x;
	double y;
	double z;
	double r;
	Mtl mtl;
	
	public Sphere(){
	}
	
	public Sphere(String NAME, double X, double Y, double Z){
		name = NAME;
		x = X;
		y = Y;
		z = Z;
	}
	
	public Sphere(String NAME, double X, double Y, double Z, Mtl MTL){
		name = NAME;
		x = X;
		y = Y;
		z = Z;
		mtl = MTL;
	}
}
