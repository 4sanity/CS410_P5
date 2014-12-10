import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;
import org.jblas.DoubleMatrix;

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
				double x = (2.0/scene.width)*j-1.0;
				double y = (2.0/scene.height)*i-1.0;
				
				DoubleMatrix PRP = new DoubleMatrix(3,1,camera.prpx,camera.prpy,camera.prpz);
				DoubleMatrix VPN = new DoubleMatrix(3,1,camera.vpnx,camera.vpny,camera.vpnz);
				DoubleMatrix VUP = new DoubleMatrix(3,1,camera.vupx,camera.vupy,camera.vupz);
				DoubleMatrix n = normalize(VPN);
				DoubleMatrix VUPxn = VUP.mul(n);
				DoubleMatrix u = normalize(VUPxn);
				DoubleMatrix v = n.mul(u);
				double d = camera.near;
				DoubleMatrix pixel = PRP.sub(n.mul(d)).add(u.mul(x)).add(v.mul(-y));
				DoubleMatrix W = new DoubleMatrix(3,1,pixel.get(0)-PRP.get(0),pixel.get(1)-PRP.get(1),pixel.get(2)-PRP.get(2));
				
				//ready to check for intersections, normalize untested
			}
		}
		
		
		
		
		
		depthBuf.close();
		colorBuf.close();
	}
	
	public static DoubleMatrix normalize(DoubleMatrix x){
		double Mag = Math.sqrt( Math.pow(x.get(0),2)+Math.pow(x.get(1),2)+Math.pow(x.get(2),2) );
		DoubleMatrix result = x.div(Mag);
		return result;
	}
	
}










