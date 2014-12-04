import java.util.Vector;

public class Group {

	String name = "default";
	Vector<Face> faces = new Vector<Face>();
	Mtl mtl;
	
	public Group(){
	}
	
	public Group(String NAME){
		name = NAME;
	}
	
	public Group(String NAME, Mtl MTL){
		name = NAME;
		mtl = MTL;
	}
	
}
