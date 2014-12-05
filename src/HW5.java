import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.Vector;


public class HW5 {
	static Vector<Camera> cameras = new Vector<Camera>();
	static Vector<Light> lights = new Vector<Light>();
	static Vector<Scene> scenes = new Vector<Scene>();
	static Vector<Mtl> mtls = new Vector<Mtl>();
	static Vector<Vertex> vertices = new Vector<Vertex>();
	static Vector<Group> groups = new Vector<Group>();
	static Vector<Sphere> spheres = new Vector<Sphere>();
	static Vector<Face> faces = new Vector<Face>();
	
	
	public static void main (String[] args){
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
								mtls.add(m);
							}
							
							if(line2.charAt(0)=='v'){
								Vertex v = new Vertex();
								v.x = Double.parseDouble(words2[1]);
								v.y = Double.parseDouble(words2[2]);
								v.z = Double.parseDouble(words2[3]);
								if(words2.length==5){
									v.w = Double.parseDouble(words2[4]);
								}
								vertices.add(v);
							}
						}
						mtlfile.close();
					}catch(FileNotFoundException e){
			            System.out.println("Error: Could not open file. File name given is..." + words[1]);
			            System.exit(1);
					}	
					
				}
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
					Face f = new Face();
					
				}
				
			}
		}catch(FileNotFoundException e){
            System.out.println("Error: Could not open file. File name given is..." + obj);
            System.exit(1);
		}
		
		
		
		
	}
	
}










