public class Mtl {
	
	String name;
	double[] ka = new double[3];
	double[] kd = new double[3];
	double[] ks = new double[3];
	double ns;
	double n1;
	double tr;
	double kr;
	double krf;
	
	public Mtl(){
	}
	
	public Mtl(String MTL, double[] KA, double[] KD, double[] KS, double NS){
		name = MTL;
		ka = KA;
		kd = KD;
		ks = KS;
		ns = NS;
	}
	
}
