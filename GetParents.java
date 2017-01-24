import java.io.FileNotFoundException;


public class Getparents {

	
	public static void main(String[] args)throws FileNotFoundException {
		Splitfile m = new Splitfile();
		Splitfile b = new Splitfile();
		Splitfile c = new Splitfile();
		m.molecularsplit("namespace: molecular_function");
		b.molecularsplit("namespace: biological_process");
		c.molecularsplit("namespace: cellular_component");
		
	}

}