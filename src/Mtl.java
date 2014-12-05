public class Mtl {
	
	String name;
	double[] ka;
	double[] kd;
	double[] ks;
	double ns;
	
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
