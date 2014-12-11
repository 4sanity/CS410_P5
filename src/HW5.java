import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

import org.jblas.DoubleMatrix;
import org.jblas.Geometry;
import org.jblas.Solve;

public class HW5 {
	static Vector<Camera> cameras = new Vector<Camera>();
	static Vector<Light> lights = new Vector<Light>();
	static Vector<Scene> scenes = new Vector<Scene>();
	static Vector<Mtl> mtls = new Vector<Mtl>();
	static Vector<Vertex> vertices = new Vector<Vertex>();
	static Vector<Group> groups = new Vector<Group>();
	static Vector<Sphere> spheres = new Vector<Sphere>();
	static Vector<Face> faces = new Vector<Face>();
	
	public static void main (String[] args) throws IOException{
		if(args.length!=2){
			System.out.println("Error: Incorrect number of command arguments.");
			System.out.println("Usage: HW3 objectFile commandFile");
			System.exit(1);
		}
		
		String obj = args[0];
		String command = args[1];
		
		try{
			Scanner file = new Scanner(new FileReader(command));
			while(file.hasNextLine()){
				String line = file.nextLine();
				String[] words = line.split(" ");
				if(line.length()!=0){
					if(line.charAt(0)=='c'){
						Camera c = new Camera();
						c.name = words[1];
						c.prpx = Double.parseDouble(words[2]);
						c.prpy = Double.parseDouble(words[3]);
						c.prpz = Double.parseDouble(words[4]);
						c.vpnx = Double.parseDouble(words[5]);
						c.vpny = Double.parseDouble(words[6]);
						c.vpnz = Double.parseDouble(words[7]);
						c.vupx = Double.parseDouble(words[8]);
						c.vupy = Double.parseDouble(words[9]);
						c.vupz = Double.parseDouble(words[10]);
						c.near = Double.parseDouble(words[11]);
						c.far = Double.parseDouble(words[12]);
						cameras.add(c);
					}
					
					if(line.charAt(0)=='l'){
						Light l = new Light();
						l.x = Double.parseDouble(words[1]);
						l.y = Double.parseDouble(words[2]);
						l.z = Double.parseDouble(words[3]);
						l.w = Double.parseDouble(words[4]);
						l.r = Double.parseDouble(words[5]);
						l.g = Double.parseDouble(words[6]);
						l.b = Double.parseDouble(words[7]);
						lights.add(l);
					}
					
					if(line.charAt(0)=='r'){
						Scene s = new Scene();
						s.name = words[1];
						s.width = Double.parseDouble(words[2]);
						s.height = Double.parseDouble(words[3]);
						s.recursionDepth = Double.parseDouble(words[4]);
						scenes.add(s);
					}
				}//end if not null line
			}
			file.close();
		}catch(FileNotFoundException e){
            System.out.println("Error: Could not open file. File name given is..." + command);
            System.exit(1);
		}
		
		//first pass for vertices and mtls
		try{
			Scanner file = new Scanner(new FileReader(obj));
			while(file.hasNextLine()){
				String line = file.nextLine();
				String[] words = line.split(" ");
				if(line.length()!=0){
					if(words[0].equals("mtllib")){
						
						try{
							Scanner mtlfile = new Scanner(new FileReader(words[1]));
							while(mtlfile.hasNextLine()){
								String line2 = mtlfile.nextLine();
								String[] words2 = line2.split(" ");
								if(words2[0].equals("newmtl")){
									Mtl m = new Mtl();
									m.name = words2[1];
									String ka = mtlfile.nextLine();
									String[] kaW = ka.split(" ");
									m.ka[0] = Double.parseDouble(kaW[1]);
									m.ka[1] = Double.parseDouble(kaW[2]);
									m.ka[2] = Double.parseDouble(kaW[3]);
									String kd = mtlfile.nextLine();
									String[] kdW = kd.split(" ");
									m.kd[0] = Double.parseDouble(kdW[1]);
									m.kd[1] = Double.parseDouble(kdW[2]);
									m.kd[2] = Double.parseDouble(kdW[3]);
									String ks = mtlfile.nextLine();
									String[] ksW = ks.split(" ");
									m.ks[0] = Double.parseDouble(ksW[1]);
									m.ks[1] = Double.parseDouble(ksW[2]);
									m.ks[2] = Double.parseDouble(ksW[3]);
									String ns = mtlfile.nextLine();
									String[] nsW = ns.split(" ");
									m.ns = Double.parseDouble(nsW[1]);
									String n1 = mtlfile.nextLine();
									String[] n1W = n1.split(" ");
									m.n1 = Double.parseDouble(n1W[1]);
									String tr = mtlfile.nextLine();
									String[] trW = tr.split(" ");
									m.tr = Double.parseDouble(trW[1]);
									String kr = mtlfile.nextLine();
									String[] krW = kr.split(" ");
									m.kr = Double.parseDouble(krW[1]);
									String krf = mtlfile.nextLine();
									String[] krfW = krf.split(" ");
									m.krf = Double.parseDouble(krfW[1]);
									mtls.add(m);
								}
							}
							mtlfile.close();
						}catch(FileNotFoundException e){
				            System.out.println("Error: Could not open file. File name given is..." + words[1]);
				            System.exit(1);
						}	
						
					}
					
					if(line.charAt(0)=='v'){
						Vertex v = new Vertex();
						v.x = Double.parseDouble(words[1]);
						v.y = Double.parseDouble(words[2]);
						v.z = Double.parseDouble(words[3]);
						if(words.length==5){
							v.w = Double.parseDouble(words[4]);
						}
						vertices.add(v);
					}
				}//end if not null line
			}
			file.close();
		}catch(FileNotFoundException e){
            System.out.println("Error: Could not open file. File name given is..." + obj);
            System.exit(1);
		}
		
		//second pass faces, groups, spheres
		try{
			Scanner file = new Scanner(new FileReader(obj));
			Mtl currMtl = new Mtl();
			while(file.hasNextLine()){
				String line = file.nextLine();
				String[] words = line.split(" ");
				if(line.length()!=0){
					if(words[0].equals("usemtl")){
						String mtlName = words[1];
						for(int i=0; i<mtls.size(); i++){
							if(mtls.elementAt(i).name.equals(mtlName)){
								currMtl = mtls.elementAt(i);
							}
						}
					}
					
					if(line.charAt(0)=='g'){
						Group g = new Group();
						g.name = words[1];
						g.mtl = currMtl;
						groups.add(g);
					}
					
					if(line.charAt(0)=='f'){
						
						if(words.length>4){ //need triangulation
							//f v1 v2 v3 v4 v5 will result in three faces as f1 v1 v2 v3, f2 v1 v3 v4 and f3 v1 v4 v5
							Face t = new Face();
							int tri = 4;
							while(tri<=words.length){
								t.vertices[0] = vertices.elementAt(Integer.parseInt(words[1])-1);
								t.vertices[1] = vertices.elementAt(Integer.parseInt(words[tri-2])-1);
								t.vertices[2] = vertices.elementAt(Integer.parseInt(words[tri-1])-1);
								t.mtl = currMtl;
								tri++;
								if(groups.size()==0){
									Group g = new Group();
									g.faces.add(t);
									groups.add(g);
								}else{
									groups.lastElement().faces.add(t);
								}
								//System.out.println(t.vertices[0].toString());
								//System.out.println(t.vertices[1].toString());
								//System.out.println(t.vertices[2].toString());
							}
						}else{
							Face f = new Face();
							f.vertices[0] = vertices.elementAt(Integer.parseInt(words[1])-1);
							f.vertices[1] = vertices.elementAt(Integer.parseInt(words[2])-1);
							f.vertices[2] = vertices.elementAt(Integer.parseInt(words[3])-1);
							f.mtl = currMtl;
							if(groups.size()==0){
								Group g = new Group();
								g.faces.add(f);
								groups.add(g);
							}else{
								groups.lastElement().faces.add(f);
							}
						}
					}
					
					if(line.charAt(0)=='s'){
						Sphere s = new Sphere();
						s.name = words[1];
						s.x = Double.parseDouble(words[2]);
						s.y = Double.parseDouble(words[3]);
						s.z = Double.parseDouble(words[4]);
						s.r = Double.parseDouble(words[5]);
						s.mtl = currMtl;
						spheres.add(s);
					}
				}//end if line not null
				
			}
			file.close();
		}catch(FileNotFoundException e){
            System.out.println("Error: Could not open file. File name given is..." + obj);
            System.exit(1);
		}
		
	for(int i=0; i<scenes.size(); i++){
		for(int j=0; j<cameras.size(); j++){
			String depthFilename = scenes.elementAt(i).name+"_"+cameras.elementAt(j).name+"_depth.ppm";
			String colorFilename = scenes.elementAt(i).name+"_"+cameras.elementAt(j).name+"_color.ppm";
			rayTrace(depthFilename,colorFilename,scenes.elementAt(i),cameras.elementAt(j));
		}
	}
		
		
		
	//finish main
	System.out.println("-----Finished-----");
	}


	private static void rayTrace(String depthFilename, String colorFilename, Scene scene, Camera camera) throws IOException {
		File depthFile = new File(depthFilename);
		File colorFile = new File(colorFilename);
		if(!depthFile.exists()){
			depthFile.createNewFile();
		}
		if(!colorFile.exists()){
			colorFile.createNewFile();
		}
		FileWriter fw = new FileWriter(depthFile.getAbsoluteFile());
		FileWriter fw2 = new FileWriter(colorFile.getAbsoluteFile());
		BufferedWriter depthBuf = new BufferedWriter(fw);
		BufferedWriter colorBuf = new BufferedWriter(fw2);
		depthBuf.write("P3\t" + scene.width + "\t" + scene.height + "\t" + "256\n");
		colorBuf.write("P3\t" + scene.width + "\t" + scene.height + "\t" + "256\n");
		
		for(double i=0; i<scene.width; i++){
			for(double j=0; j<scene.height; j++){
				double minDepth = -1;
				double GrayScale = 255.0;
				Mtl objMtl;
				DoubleMatrix S = new DoubleMatrix(3,1,0,0,0);
				DoubleMatrix N = new DoubleMatrix(3,1,0,0,0);
				
				double x = (2.0/scene.width)*j-1.0;
				double y = (2.0/scene.height)*i-1.0;
				
				DoubleMatrix PRP = new DoubleMatrix(3,1,camera.prpx,camera.prpy,camera.prpz);
				DoubleMatrix VPN = new DoubleMatrix(3,1,camera.vpnx,camera.vpny,camera.vpnz);
				DoubleMatrix VUP = new DoubleMatrix(3,1,camera.vupx,camera.vupy,camera.vupz);
				DoubleMatrix n = Geometry.normalize(VPN);
				DoubleMatrix VUPxn = VUP.mul(n);
				DoubleMatrix u = Geometry.normalize(VUPxn);
				DoubleMatrix v = n.mul(u);
				DoubleMatrix pixel = PRP.sub(n.mul(camera.near)).add(u.mul(x)).add(v.mul(-y));
				DoubleMatrix W = pixel.sub(PRP);
				
				//ready to check for intersections, normalize untested
				for(int g=0; g<groups.size(); g++){
					for(int f=0; f<groups.elementAt(g).faces.size(); f++){
						Face curr = groups.elementAt(g).faces.elementAt(f);
						Vertex a = curr.vertices[0];
						Vertex b = curr.vertices[1];
						Vertex c = curr.vertices[2];
						DoubleMatrix m = new DoubleMatrix(3,3, b.x-a.x,c.x-a.x,-W.get(0),
												b.y-a.y,c.y-a.y,-W.get(1),
												b.z-a.z,c.z-a.z,-W.get(2));
						//m = Solve.pinv(m); //psuedo inverse
						m = inverse(m);
						DoubleMatrix v2 = new DoubleMatrix(3,1,PRP.get(0)-a.x,PRP.get(1)-a.y,PRP.get(2)-a.z);
						DoubleMatrix BetaGammaT = m.mmul(v2);
						double beta = BetaGammaT.get(0);
						double gamma = BetaGammaT.get(1);
						double T = BetaGammaT.get(2);
						if(beta>=0 && gamma>=0 && (beta+gamma)>=0 && (beta+gamma)<=1){
							DoubleMatrix U1 = pixel.sub(PRP);
							DoubleMatrix U = Geometry.normalize(U1);
							DoubleMatrix Q = (PRP.add(T)).mul(U);
							DoubleMatrix LQ = Q.sub(pixel);
							double distLQ = Math.sqrt( Math.pow(LQ.get(0),2)+Math.pow(LQ.get(1),2)+Math.pow(LQ.get(2),2));
							int depth = (int) (255 - (Math.min(255.0,255*(distLQ)/(camera.far-camera.near))));
							if(minDepth==-1 || T<minDepth){
								minDepth = T;
								GrayScale = depth;
								objMtl = curr.mtl;
								//calculate N for triangles, L = Q(light)-S(surface)/normalized
								DoubleMatrix V1 = new DoubleMatrix(3,1,a.x,a.y,a.z);
								DoubleMatrix V2 = new DoubleMatrix(3,1,b.x,b.y,b.z);
								DoubleMatrix V3 = new DoubleMatrix(3,1,c.x,c.y,c.z);
								DoubleMatrix E1 = V2.sub(V1);
								DoubleMatrix E2 = V3.sub(V2);
								DoubleMatrix N1 = crossProduct(E1,E2);
								N = Geometry.normalize(N1);
								S = Q;
							}
						}
					}
				}
				for(int s=0; s<spheres.size(); s++){
					Sphere curr = spheres.elementAt(s);
					DoubleMatrix O = new DoubleMatrix(3,1, curr.x,curr.y,curr.z);
					double rSQ = Math.pow(curr.r,2);
					DoubleMatrix c1 = O.sub(PRP);
					double c = Math.sqrt( Math.pow(c1.get(0),2)+Math.pow(c1.get(1),2)+Math.pow(c1.get(2),2));
					double cSQ = Math.pow(c,2);
					W = Geometry.normalize(W);
					DoubleMatrix vt = (O.sub(PRP)).mul(W);
					double vv = vt.get(0)+vt.get(1)+vt.get(2);
					double vSQ = Math.pow(vv,2);
					double d = Math.sqrt(rSQ-(cSQ-vSQ));
					double sc = vv-d;
					DoubleMatrix Q = (PRP.add(sc)).mul(W);
					double dSQ = Math.pow(d,2);
					if(dSQ>0 && sc>camera.near && sc<camera.far){
						DoubleMatrix LQ = Q.sub(pixel);
						double distLQ = Math.sqrt( Math.pow(LQ.get(0),2)+Math.pow(LQ.get(1),2)+Math.pow(LQ.get(2),2));
						int depth = (int) (255 - (Math.min(255.0,255*(distLQ)/(camera.far-camera.near))));
						if(minDepth==-1 || sc<minDepth){
							minDepth = sc;
							GrayScale = depth;
							//System.out.println(GrayScale);
							objMtl = curr.mtl;
							//calculate N for spheres, N=surface point on sphere-circle center/normalized
							S = Q;
							N = S.sub(O);
							N = Geometry.normalize(N);
						}
					}
				}
				
				//no objects hit white, else write closest
				if(minDepth==-1){
					depthBuf.write("0 0 0\n");
					colorBuf.write("255 255 255\n");
				}else{
					depthBuf.write(GrayScale + " " + GrayScale + " " + GrayScale + "\n");
					colorBuf.write("155 155 155\n");
				}
				
				
//				//no objects hit white, else write closest
//				if(minDepth==-1){
//					outputDepth << "0 0 0" << '\n';
//					outputColor << "255 255 255" << '\n';
//				}else{
//					//lighting
//					vector<double> Ka{ MTL.Ka[0],MTL.Ka[1],MTL.Ka[2] };
//					vector<double> Kd{ MTL.Kd[0],MTL.Kd[1],MTL.Kd[2] };
//					vector<double> Ks{ MTL.Ks[0],MTL.Ks[1],MTL.Ks[2] };
//					vector<double> ambientValue{ 20,20,20 };
//				
//					vector<double> ambient{ Ka[0]*ambientValue[0],Ka[1]*ambientValue[1],Ka[2]*ambientValue[2] };
//					vector<double> diffuse{ 0,0,0 };
//					vector<double> spectular{ 0,0,0 };
//
//					vector<double> V = PRP-S;
//					V = normalize(V);
//
//					for(unsigned l=0; l<lights.size(); l++){
//						vector<double> L1 = { lights[l].X,lights[l].Y,lights[l].Z };
//						vector<double> L = L1-S;
//						normalize(L);
//					
//						double LdotN = dotProduct(L,N);
//						vector<double> R = 2*(LdotN)*N-L;
//						double VdotR = dotProduct(V,R);
//					
//						diffuse[0]+=(Kd[0]*lights[l].R)*LdotN;
//						diffuse[1]+=(Kd[1]*lights[l].G)*LdotN;
//						//if(lights[l].G==255){ cout << LdotN << endl; }
//						diffuse[2]+=(Kd[2]*lights[l].B)*LdotN;
//
//						spectular[0]+=(Ks[0]*lights[l].R)*(pow(VdotR,MTL.Ns));
//						spectular[1]+=(Ks[1]*lights[l].G)*(pow(VdotR,MTL.Ns));
//						spectular[2]+=(Ks[2]*lights[l].B)*(pow(VdotR,MTL.Ns));			
//					
//					}
//				
//					vector<double> I = ambient+diffuse+spectular;
//					int red = I[0];
//					int blue = I[1];
//					int green = I[2];
//				
//					if(red>255){ red=255; }
//					if(green>255){ green=255; }
//					if(blue>255){ blue=255; }
//					if(red<0){ red=0; }
//					if(green<0){ green=0; }
//					if(blue<0){ blue=0; }
//					
//					outputDepth << GrayScale << " " << GrayScale << " " << GrayScale << '\n';
//					outputColor << red << " " << green << " " << blue << '\n';
//				}
				
				
				
				
			}
		}
		
		
		
		
		
		depthBuf.close();
		colorBuf.close();
	}
	
//	public static DoubleMatrix normalize(DoubleMatrix x){
//		double Mag = Math.sqrt( Math.pow(x.get(0),2)+Math.pow(x.get(1),2)+Math.pow(x.get(2),2) );
//		DoubleMatrix result = x.div(Mag);
//		return result;
//	}
	
	//cross product of 2 3x1 vectors
	public static DoubleMatrix crossProduct(DoubleMatrix a, DoubleMatrix b){
		DoubleMatrix result = new DoubleMatrix(3,1, (a.get(1)*b.get(2)-a.get(2)*b.get(1)),(a.get(2)*b.get(0)-a.get(0)*b.get(2)),(a.get(0)*b.get(1)-a.get(1)*b.get(0)) );
		return result;
	}
	
	//matrices passed in are 3x3 and invertible
	public static DoubleMatrix inverse(DoubleMatrix a){
		DoubleMatrix temp = new DoubleMatrix(3,3, (a.get(1,1)*a.get(2,2))-(a.get(1,2)*a.get(2,1)),-((a.get(0,1)*a.get(2,2))-(a.get(0,2)*a.get(2,1))),(a.get(0,1)*a.get(1,2))-(a.get(0,2)*a.get(1,1)),
													-((a.get(1,0)*a.get(2,2))-(a.get(1,2)*a.get(2,0))),(a.get(0,0)*a.get(2,2))-(a.get(0,2)*a.get(2,0)),-((a.get(0,0)*a.get(1,2))-(a.get(0,2)*a.get(1,0))),
													(a.get(1,0)*a.get(2,1))-(a.get(1,1)*a.get(2,0)),-((a.get(0,0)*a.get(2,1))-(a.get(0,1)*a.get(2,0))),(a.get(0,0)*a.get(1,1))-(a.get(0,1)*a.get(1,0)) );
		double detA = a.get(0,0)*((a.get(1,1)*a.get(2,2))-(a.get(1,2)*a.get(2,1)))-a.get(0,1)*((a.get(2,2)*a.get(1,0))-(a.get(1,2)*a.get(2,0)))+a.get(0,2)*((a.get(1,0)*a.get(2,1))-(a.get(1,1)*a.get(2,0)));
		detA = 1/detA;
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				a.put(i,j,detA*temp.get(i,j));
			}
		}
		return a;
	}	
	
//	void inverseMatrix(MATRIX &a){
//		MATRIX temp;
//		temp[0][0]=(a[1][1]*a[2][2])-(a[1][2]*a[2][1]);
//		temp[0][1]=-((a[0][1]*a[2][2])-(a[0][2]*a[2][1]));
//		temp[0][2]=(a[0][1]*a[1][2])-(a[0][2]*a[1][1]);
//		temp[1][0]=-((a[1][0]*a[2][2])-(a[1][2]*a[2][0]));
//		temp[1][1]=(a[0][0]*a[2][2])-(a[0][2]*a[2][0]);
//		temp[1][2]=-((a[0][0]*a[1][2])-(a[0][2]*a[1][0]));
//		temp[2][0]=(a[1][0]*a[2][1])-(a[1][1]*a[2][0]);
//		temp[2][1]=-((a[0][0]*a[2][1])-(a[0][1]*a[2][0]));
//		temp[2][2]=(a[0][0]*a[1][1])-(a[0][1]*a[1][0]);
//		double detA = 0;
//		detA = a[0][0]*((a[1][1]*a[2][2])-(a[1][2]*a[2][1]))-a[0][1]*((a[2][2]*a[1][0])-(a[1][2]*a[2][0]))+a[0][2]*((a[1][0]*a[2][1])-(a[1][1]*a[2][0]));
//		detA = 1/detA;
//		for(unsigned i=0; i<3; i++){
//			for(unsigned j=0; j<3; j++){
//				a[i][j]=detA*temp[i][j];
//			}
//		}
//	}
	
//	vector<double> crossProduct(vector<double> v1, vector<double> v2){
//	    vector<double> result{ (v1[1]*v2[2] - v1[2]*v2[1]), (v1[2]*v2[0] - v1[0]*v2[2]), (v1[0]*v2[1] - v1[1]*v2[0]) };
//	    return result;
//	}

	
}










