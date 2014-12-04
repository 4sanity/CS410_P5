public class Mtl {
	
	String mtlName;
	double[] ka;
	double[] kd;
	double[] ks;
	double ns;
	
	public Mtl(String MTL, double[] KA, double[] KD, double[] KS, double NS){
		mtlName = MTL;
		ka = KA;
		kd = KD;
		ks = KS;
		ns = NS;
	}
	
}
