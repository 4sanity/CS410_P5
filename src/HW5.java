import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.Vector;


public class HW5 {
	static Vector<Camera> cameras = new Vector<Camera>();
	static Vector<Light> lights = new Vector<Light>();
	static Vector<Scene> scenes = new Vector<Scene>();
	static Vector<Mtl> mtls = new Vector<Mtl>();
	static Vector<Group> groups = new Vector<Group>();
	static Vector<Sphere> spheres = new Vector<Sphere>();
	static Vector<Vertex> vertices = new Vector<Vertex>();
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
				file.close();
			}
		}catch(FileNotFoundException e){
            System.out.println("Error: Could not open file. File name given is..." + command);
            System.exit(1);
		}
		
		try{
			Scanner file = new Scanner(new FileReader(command));
			while(file.hasNextLine()){
				String line = file.nextLine();
				String[] words = line.split(" ");
				
			}
		}catch(FileNotFoundException e){
            System.out.println("Error: Could not open file. File name given is..." + obj);
            System.exit(1);
		}
		
		
		
		
		
		
	}
	
}
