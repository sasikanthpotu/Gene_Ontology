import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;


public class Shortestpath {
	
	   static int V=0;
	   public static int nodecount=0;
	   public static boolean printoutput=false;
	   private List<Integer>distance=new ArrayList<Integer>();
	   int minDistance(int dist[], Boolean sptSet[])
	    {
	        // Initialize min value
	        int min = Integer.MAX_VALUE, min_index=-1;
	 
	        for (int v = 0; v < V; v++)
	            if (sptSet[v] == false && dist[v] <= min)
	            {
	                min = dist[v];
	                min_index = v;
	            }
	 
	        return min_index;
	    }	
	   void filecalling(File file,int dist[], Map<Integer,String> revindexmap,int src,int dest,int i)
	   {
		   if(printoutput==true)
	            {
		     try{
	                   FileWriter fw = new FileWriter(file,true);
	   		            BufferedWriter bw = new BufferedWriter(fw);
	   		           if((dist[i]<Integer.MAX_VALUE))
			            {
	                          distance.add(dist[i]);
			            }
			            else
			            {
			            	distance.add(i);
			            }
	                    bw.write(nodecount+" Nodes "+" "+Collections.min(distance)+" "+Collections.max(distance)+" - "+distance+"\n");
	                    bw.close();
	                    printoutput=false;
	                    distance.clear();
	               }catch(IOException ioe){
	  		         System.out.println("Exception occurred:");
	  		    	 ioe.printStackTrace();
	  		       } 
		      }
		   else
		   {
			   if((dist[i]<Integer.MAX_VALUE))
		            {
                          distance.add(dist[i]);
		            }
		            else
		            {
		            	distance.add(i);
		            }
			   
		   }
	   }
	   void printSolution(int graph[][],int dist[], Map<Integer,String> revindexmap,int n,int src,int dest,File file)
	    {
		   try{ 
		    if(!file.exists()){
		    	   file.createNewFile();
		    	}
		  
		   }catch(IOException ioe){
		         System.out.println("Exception occurred:");
		    	 ioe.printStackTrace();
		       }
	    	Map<String,Integer>smap = new TreeMap<>();
	    	Boolean smapclear = false;
	        for (int i = 0; i < V; i++)
	        {
	        	if((dest == i) && (dist[i]<Integer.MAX_VALUE))
	        	{
	               //System.out.println("Distance from source "+revindexmap.get(src)+" to destination "+revindexmap.get(i)+" is "+dist[i]);
	               filecalling(file,dist,revindexmap,src,dest,i);
	               smapclear = true;
	               i = V;
	               smap.clear();
	        	}
	        	else
	        	{
	        		if((dist[i]<Integer.MAX_VALUE))
	        		{
	        			smap.put(revindexmap.get(i), dist[i]);
	        		}
	        	}
	        	
	        }
	        if(smapclear == false)
        	{
        		dijkstra1(graph,revindexmap,smap,src,dest,file);
        		smap.clear();
        	}
	    }
	   // A utility function to print the constructed distance array
	    void printfSolution(int dist[], Map<Integer,String> revindexmap,Map<String,Integer> smap,int n,int src,int dest,File file)
	    {
	    	try{ 
			    if(!file.exists()){
			    	   file.createNewFile();
			    	}
			  
			   }catch(IOException ioe){
			         System.out.println("Exception occurred:");
			    	 ioe.printStackTrace();
			       }
	    	int rdcount = 0;
	    	Map<String,Integer>dmap = new TreeMap<>();
	        for (int i = 0; i < V; i++)
	        {
	        	if((dist[i]<Integer.MAX_VALUE))
	        	{
	        		dmap.put(revindexmap.get(i), dist[i]);
	        	}	
	        }
	        for(String key:smap.keySet())
	        {
	        	if(dmap.containsKey(key))
	        	{
	        		int scount = smap.get(key);
	        		int dcount = dmap.get(key);
	        		int sdcount = (scount + dcount);
	        		if(rdcount == 0)
	        		{
	        			rdcount = sdcount;
	        		}
	        		else
	        		{
	        		    if(sdcount<rdcount)
	        		    {
	        			    rdcount = sdcount;
	        		    }
	        		}
	        	}
	        }
	        if(rdcount>0)
	        {
	           //System.out.println("Distance from source "+revindexmap.get(src)+" to destination "+revindexmap.get(dest)+" is "+rdcount);
	           filecalling(file,dist,revindexmap,src,dest,rdcount);
	           dmap.clear();
	        }
	    }
	    void dijkstra1(int graph[][],Map<Integer,String> revindexmap,Map<String,Integer> smap,int src,int dest,File file)
	    {
	        int dist[] = new int[V]; // The output array. dist[i] will hold
	                                 // the shortest distance from src to i
	        // sptSet[i] will true if vertex i is included in shortest
	        // path tree or shortest distance from src to i is finalized
	        Boolean sptSet[] = new Boolean[V];
	 
	        // Initialize all distances as INFINITE and stpSet[] as false
	        for (int i = 0; i < V; i++)
	        {
	            dist[i] = Integer.MAX_VALUE;
	            sptSet[i] = false;
	        }
	 
	        // Distance of source vertex from itself is always 0
	        dist[dest] = 0;
	 
	        // Find shortest path for all vertices
	        for (int count = 0; count < V-1; count++)
	        {
	            // Pick the minimum distance vertex from the set of vertices
	            // not yet processed. u is always equal to src in first
	            // iteration.
	            int u = minDistance(dist, sptSet);
	 
	            // Mark the picked vertex as processed
	            sptSet[u] = true;
	 
	            // Update dist value of the adjacent vertices of the
	            // picked vertex.
	            for (int v = 0; v < V; v++)
	 
	                // Update dist[v] only if is not in sptSet, there is an
	                // edge from u to v, and total weight of path from src to
	                // v through u is smaller than current value of dist[v]
	                if (!sptSet[v] && graph[u][v]!=0 &&
	                        dist[u] != Integer.MAX_VALUE &&
	                        dist[u]+graph[u][v] < dist[v])
	                    dist[v] = dist[u] + graph[u][v];
	        }
	 
	        // print the constructed distance array
	        printfSolution(dist,revindexmap,smap,V,src,dest,file);
	 }
	
	 
	    // Funtion that implements Dijkstra's single source shortest path
	    // algorithm for a graph represented using adjacency matrix
	    // representation
	    void dijkstra(int graph[][],Map<Integer,String> revindexmap,int source,int dest,File file)
	    {
	        int dist[] = new int[V]; // The output array. dist[i] will hold
	                                 // the shortest distance from src to i
	        // sptSet[i] will true if vertex i is included in shortest
	        // path tree or shortest distance from src to i is finalized
	        Boolean sptSet[] = new Boolean[V];
	 
	        // Initialize all distances as INFINITE and stpSet[] as false
	        for (int i = 0; i < V; i++)
	        {
	            dist[i] = Integer.MAX_VALUE;
	            sptSet[i] = false;
	        }
	 
	        // Distance of source vertex from itself is always 0
	        dist[source] = 0;
	 
	        // Find shortest path for all vertices
	        for (int count = 0; count < V-1; count++)
	        {
	            // Pick the minimum distance vertex from the set of vertices
	            // not yet processed. u is always equal to src in first
	            // iteration.
	            int u = minDistance(dist, sptSet);
	 
	            // Mark the picked vertex as processed
	            sptSet[u] = true;
	 
	            // Update dist value of the adjacent vertices of the
	            // picked vertex.
	            for (int v = 0; v < V; v++)
	               
	                // Update dist[v] only if is not in sptSet, there is an
	                // edge from u to v, and total weight of path from src to
	                // v through u is smaller than current value of dist[v]
	                if (!sptSet[v] && graph[u][v]!=0 &&
	                        dist[u] != Integer.MAX_VALUE &&
	                        dist[u]+graph[u][v] < dist[v])
	                    dist[v] = (dist[u] + graph[u][v]);
	        }
	 
	        // print the constructed distance array
	        printSolution(graph,dist,revindexmap,V,source,dest,file);
	 }
	
	
	
	public static void main(String[] args)throws FileNotFoundException, IOException {
	int count =-1;
	int[][] graph; 
	List<String> parent = new ArrayList<>();
	Map<String,List<String>> orgmap= new TreeMap<>();
	Map<String,Integer> indexmap= new TreeMap<>();
	Map<Integer,String> revindexmap= new TreeMap<>();
	// This is the place for Input file.
	String inputfile = "go-node-parents-mf.txt";
	String workingDirectory=System.getProperty("user.home");
	String absoluteFilePath = "";
	absoluteFilePath=workingDirectory + File.separator + inputfile;
	File f = new File(absoluteFilePath);
	// This is the output file. Sample output file format is 4 nodes   5   8   –   7  8  7  7  6  5.
    // First how many nodes in the line, then minimum distance, then maximum distance, then followed by the symbol – the write all distance values.
	String outputfile = "go-mf-shortestp.txt";
	String outputFilePath = "";
	outputFilePath=workingDirectory + File.separator + outputfile;
	File file = new File(outputFilePath);
	file.delete();
	Scanner scan = new Scanner(f);
	while(scan.hasNextLine())
	{
		count = count+1;
		String line = scan.nextLine();
		String[] filedata = line.split("=");
        indexmap.put(filedata[0].trim(), count);
        revindexmap.put(count,filedata[0].trim());
		if(filedata.length>1)
		{
		   String[] parentlist = filedata[1].split(",");
		   for(int i =0; i<parentlist.length;i++)
		   {
			   boolean retval = parentlist[i].contains("GO:");
			   if(retval == true)
			   {
			     parent.add(parentlist[i].trim());   
			   }
		   }
		}
		   List<String> list = new ArrayList<String>();
	       list.addAll(parent);
	       orgmap.put(filedata[0].trim(),list);
	       parent.clear();
		
	}
	scan.close();
	V = orgmap.size();
	graph = new int[orgmap.size()][orgmap.size()];
	for (String key : orgmap.keySet()) {
		  int index1 =indexmap.get(key);
	      List<String> values = orgmap.get(key);
	      if(values.size()>0)
	      {
	    	  for(int i = 0; i<values.size();i++)
	    	  {
	    	    int index2 = indexmap.get(values.get(i));
	    	    graph[index1][index2] = 1;
	    	   // graph[index2][index1] = 1;
	    	  }
	      }
    
	}	
	Shortestpath t = new Shortestpath();
	// This is input file. Sample file format is GO:0004079,GO:0016628,GO:0035252,GO:0016301.
	String inputfile1 = "molecular-input.txt";
	String inputFilePath = " ";
	inputFilePath=workingDirectory + File.separator + inputfile1;
	File input = new File(inputFilePath);
	Scanner scanner = new Scanner(input);
	while(scanner.hasNextLine())
	{
		int source = 0;
		int dest = 0;
		String inputline = scanner.nextLine();
		String[] inputsrc = inputline.split(",");
		nodecount=inputsrc.length;
		for(int i=0;i<inputsrc.length-1;i++)
		{
			for(int j=i+1;j<inputsrc.length;j++)
			{
				if(indexmap.containsKey(inputsrc[i].trim()))
				{
					source = indexmap.get(inputsrc[i].trim());
					if(indexmap.containsKey(inputsrc[j].trim()))
						{
						  if(i==inputsrc.length-2)
						  {
							  printoutput=true;
						  }
							dest = indexmap.get(inputsrc[j].trim());
							t.dijkstra(graph,revindexmap, source,dest,file);
						}
					else{
							System.out.println("provided destination is not in input file"+inputsrc[j]);
						}
				}
			}
		}
		
	}
	scanner.close();
	System.out.println("final result updated into output file");
}
}
