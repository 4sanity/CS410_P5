public class Camera {
	String name;
	double prpx;
	double prpy;
	double prpz;
	double vpnx;
	double vpny;
	double vpnz;
	double vupx;
	double vupy;
	double vupz;
	double near;
	double far;
	
	public Camera(String NAME, double PRPX, double PRPY, double PRPZ, double VPNX, double VPNY, double VPNZ, double VUPX, double VUPY, double VUPZ, double NEAR, double FAR){
		name = NAME;
		prpx = PRPX;
		prpy = PRPY;
		prpz = PRPZ;
		vpnx = VPNX;
		vpny = VPNY;
		vpnz = VPNZ;
		vupx = VUPX;
		vupy = VUPY;
		vupz = VUPZ;
		near = NEAR;
		far = FAR;
	}
}