import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
public class Splitfile {

	
	  public void molecularsplit(String name)throws FileNotFoundException{	
		String[] details = new String[2];
		boolean firsttime;
		boolean molfun;
		boolean isa;
		boolean idfirst;
		boolean obsolete;
		String term = "[Term]";
		String altid = "alt_id:";
		String id = "id:";
		String namespace = name;
		String isobsolete = "is_obsolete: true";
		String is_a = "is_a:";

		List<String> parent = new ArrayList<>();
		Map<String,List<String>> map= new TreeMap<>();
		// This is the place for Input file. It will take the Input file from Home directory like c:users\Sasikanth
		String inputfile = "go.obo.txt";
		String workingDirectory=System.getProperty("user.home");
		String absoluteFilePath = "";
		absoluteFilePath=workingDirectory + File.separator + inputfile;
		File f = new File(absoluteFilePath);
		
		Scanner scan = new Scanner(f);
		firsttime = true;
		molfun = false;
		isa = false;
		idfirst = true;
		obsolete = false;
		while(scan.hasNextLine())
		
		{
			String line = scan.nextLine();
			if (line.contains(term)&&(firsttime == true))
			{
				molfun = false;
				firsttime = false;
				isa = false;
				idfirst = true;
			}
			else
			{
				if (line.contains(term)&&(firsttime == false))
				{
					if((molfun == true)&&(isa == true))
					{
				       molfun = false;
				       isa =false;
				       idfirst = true;
				       obsolete = false;
				       List<String> list = new ArrayList<String>();
				       list.addAll(parent);
				       map.put(details[1],list);
				       parent.clear();
					}
					else
					{
					    if((molfun == true)&&(isa == false)&&(obsolete == false))
					    {
				          molfun = false;
				          isa =false;
				          idfirst = true;
				          obsolete = false;
				          List<String> list = new ArrayList<String>();
				          list.addAll(parent);
				          map.put(details[1],list);
				          parent.clear();
					    }
					    else{
						   molfun = false;
					       isa =false;
					       idfirst = true;
					       obsolete = false;
					     }
					}
				}
			}
			if(!line.contains(altid))
			{
			  if(line.contains(id)&&(idfirst == true))
			   {
                details = line.split(id,2);
                idfirst = false;
			   }
			  else
			   {
				if(line.contains(namespace))
				{
					molfun = true;
				}
				if(line.contains(isobsolete)&&(molfun == true))
				{
					obsolete = true;
				}
				else
				{
					if(line.contains(is_a)&&(molfun == true))
					{
						
						String[] details1 = line.split("!");
						String[] details2 = details1[0].split(is_a);
		            	parent.add(details2[1]);
		            	isa = true;
					}
				} 
				
			}
			}
		}
		String str = mapToString(map);
		String outputfile;
		String outputFilePath = "";
		try{
			// This is the place for output file.
			    File out;
			    if(name == "namespace: molecular_function")
			    {
			       outputfile="go-node-parents-mf.txt";
			       outputFilePath=workingDirectory + File.separator + inputfile;
				   out = new File(outputFilePath);
				   BufferedWriter writer = new BufferedWriter(new FileWriter(out));
				   writer.write(str.toString());
					writer.close();
			    }
			    else
			    {
			    	if(name == "namespace: biological_process")
				    {
			    	   outputfile="go-node-parents-bp.txt";
					   outputFilePath=workingDirectory + File.separator + inputfile;
					   out = new File(outputFilePath);
					   BufferedWriter writer = new BufferedWriter(new FileWriter(out));
					   writer.write(str.toString());
						writer.close();
				    }
			    else
			    {
			    	if(name == "namespace: cellular_component")
				    {
			    	   outputfile="go-node-parents-cc.txt";
					   outputFilePath=workingDirectory + File.separator + inputfile;
					   out = new File(outputFilePath);
					   BufferedWriter writer = new BufferedWriter(new FileWriter(out));
					   writer.write(str.toString());
						writer.close();
				    }
			    }
			    }
				 }catch(IOException e){
					e.printStackTrace();
				}
		 System.out.println("File write completed");
		
	}
	
	public static String mapToString(Map<String, List<String>> map) {
		   StringBuilder stringBuilder = new StringBuilder();

		   for (String key : map.keySet()) {
		      List<String> values = map.get(key);
		      stringBuilder.append((key));
		      stringBuilder.append("=");
		      StringBuffer listOfValues = new StringBuffer();
		      if(values.size() > 0){
		    	  listOfValues.append(values.get(0));
		         for(int i=1;i<values.size();i++) {
		        	 listOfValues.append(",");
		    	     listOfValues.append(values.get(i));
		       }
		      stringBuilder.append(listOfValues+"\n");
		     
		      }
		      else
		      {
		    	  stringBuilder.append("\n"); 
		      }
		     
		   }

		   return stringBuilder.toString();
		  }
		
	}


