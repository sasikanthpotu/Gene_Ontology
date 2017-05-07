import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Shortestpath {
	  static int V=0;
	   public static int nodecount=0;
	   public static int mimid=0;
	   public static int GeneId=0;
      public static int phenotypeCount=0;
      public static String UniprotKb="";
      public static String s="";
	   public static boolean printoutput=false;
	   private List<Integer>distance=new ArrayList<Integer>();
	   private int[] sourcepairs;
	   private int [][] graph;
	   private Map<Integer,String> revindexmap; 
	   private int k=0;
	   //output file declaration
		String outputfile = "Gene_Final_MF_Updaed_File.xlsx";
		String workingDirectory=System.getProperty("user.home");
		String outputFilePath=workingDirectory + File.separator + outputfile;
		FileOutputStream fileOut;
		XSSFWorkbook workbo;
		XSSFSheet works;
	   public Shortestpath(int graph[][],Map<Integer,String> revindexmap)
	   {
		   this.graph=graph;
		   this.revindexmap=revindexmap;
		   try{
			     fileOut = new FileOutputStream(outputFilePath);
				 workbo = new XSSFWorkbook();
				 works = workbo.createSheet("Gene_Info");
		     }catch(IOException ioe){
		         System.out.println("Exception occurred:");
		    	 ioe.printStackTrace();
		       } 
	   }
	   public void dijkstrasource(ArrayList<Integer> source)
	   {
           int length=source.size();
           ArrayList<TreeMap<String,Integer>> sourcelist=new ArrayList<TreeMap<String,Integer>>();
           for(int s=0;s<length;s++)
           {
		   sourcepairs=new int[V];
		   sourcepairs=allPairdistance(source.get(s));
		   TreeMap<String,Integer>smap = new TreeMap<>();
		   for (int i = 0; i < V; i++)
		   {
			   if((sourcepairs[i]<Integer.MAX_VALUE))
			      smap.put(revindexmap.get(i), sourcepairs[i]);   
		   }
           sourcelist.add(smap);
          }
           pathcomparisionToallgoterms(sourcelist,source);
		   
	   }
	   
	   public void pathcomparisionToallgoterms(ArrayList<TreeMap<String,Integer>> sourcelist,ArrayList<Integer> source)
	   {
                   int length=sourcelist.size();
                   for(int s=0;s<length-1;s++)
                    {
                	   if(s==length-2)
                		   printoutput=true;
                       TreeMap<String,Integer> sourcemap=sourcelist.get(s);
                       for(int j=s+1;j<length;j++)
                        {
                           int dest=source.get(j);
                           if(sourcemap.containsKey(revindexmap.get(source.get(j))))
                            {
			                       filecalling(sourcemap,dest,dest);  
                            }
                           else
                             {
                                int rdcount = 0;
                                TreeMap<String,Integer> destmap=sourcelist.get(j);
                                for(String key:sourcemap.keySet())
		                        {
		        	               if(destmap.containsKey(key))
		        	                 {
		        		               int scount = sourcemap.get(key);
		        		               int dcount = destmap.get(key);
		        		               int sdcount = scount + dcount;
		        		               if(rdcount == 0)
		        			              rdcount = sdcount;
		        		                else
		        		                 {
		        		                    if(sdcount<rdcount)
		        			                   rdcount = sdcount;
		        		                  }
		        	                  }
		                         }
                                   if(rdcount>0)
		                               filecalling(destmap,dest,rdcount);
                             }
                        }
                    }
                   
	   }
	   
	   public int[] allPairdistance(int source)
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
    return dist; 
	}

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
	   public void onemfterm(int dist[], int dest,int i)
	   {
		   TreeMap<String,Integer> temp=new TreeMap<String,Integer>();
		   temp.put(revindexmap.get(dest), 0);
		   filecalling(temp,dest,i);
	   }
	   
	   void filecalling(TreeMap<String,Integer> map, int dest,int i)
	   {
		   
		   if(printoutput==true)
		   {
		     
		    	 XSSFRow row1 = works.createRow(k);
      		     XSSFCell cellA1 = row1.createCell((short) 0);
      		     cellA1.setCellValue(mimid);
      		     XSSFCell cellB1 = row1.createCell((short) 1);
   		         cellB1.setCellValue(UniprotKb);
   		         XSSFCell cellC1 = row1.createCell((short) 2);
   		         cellC1.setCellValue(GeneId);
       		     XSSFCell cellD1 = row1.createCell((short) 3);
       		     cellD1.setCellValue(phenotypeCount);
       		     XSSFCell cellE1 = row1.createCell((short) 4);
       	         cellE1.setCellValue(nodecount);
       	         XSSFCell cellF1 = row1.createCell((short) 5);
       	         XSSFCell cellG1 = row1.createCell((short) 6);
       	         XSSFCell cellH1 = row1.createCell((short) 7);
    	         cellH1.setCellValue(s);
       	         XSSFCell cellI1 = row1.createCell((short) 8);
       	         if(nodecount==1)
       	          {
       	    	     cellF1.setCellValue(0);
       	    	     cellG1.setCellValue(0);
       	    	     cellI1.setCellValue(0);
       	          }
       	         else
       	          {
       	    	      if((map.containsKey(revindexmap.get(i))))
                           distance.add(map.get(revindexmap.get(i)));
		              else
		            	   distance.add(i);
       	    	      cellF1.setCellValue(Collections.min(distance));
       	    	      cellG1.setCellValue(Collections.max(distance));
       	    	      cellI1.setCellValue(distance.toString());
       	          }
       		        k++;
	               printoutput=false;
	               distance.clear();
	               
        }
		   else
		   {
			   if((map.containsKey(revindexmap.get(i))))
                     distance.add(map.get(revindexmap.get(i)));
		        else
		             distance.add(i); 
		   }
	   }
	   
	   public void closeoutputfile()
	   {
		   try{ 
		     workbo.write(fileOut);
    	     fileOut.flush();
    	     fileOut.close();
		   }catch(IOException ioe){
		         System.out.println("Exception occurred:");
		    	 ioe.printStackTrace();
		       } 
	   }
	  
	@SuppressWarnings("resource")
	public static void main(String[] args) throws FileNotFoundException, IOException{
			int count =-1;
			int[][] graph; 
			List<String> parent = new ArrayList<>();
			Map<String,List<String>> orgmap= new TreeMap<>();
			Map<String,Integer> indexmap= new TreeMap<>();
			Map<Integer,String> revindexmap= new TreeMap<>();
			FileInputStream fis;
			Iterator<Row> rowIterator;
			XSSFWorkbook workbookin;
			Sheet sheet;
			Iterator<?> cellIterator;
			// This is the place for Input file.
			String inputfile = "go-node-parents-mf.txt";
			String workingDirectory=System.getProperty("user.home");
			String absoluteFilePath = "";
			absoluteFilePath=workingDirectory + File.separator + inputfile;
			File f = new File(absoluteFilePath);
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
			Shortestpath t = new Shortestpath(graph,revindexmap);
			String inputfile1 = "disease_Goterm_mf_count.xlsx";
			String inputFilePath = "";
			inputFilePath=workingDirectory + File.separator + inputfile1;
			fis = new FileInputStream(inputFilePath);
			workbookin = new XSSFWorkbook(fis);
	        sheet = workbookin.getSheet("WorkSheet1");
	        rowIterator = sheet.iterator();
	        int rowcount=0;
	        while (rowIterator.hasNext()) {
	            Row row = rowIterator.next();
	            cellIterator = row.cellIterator();
	            mimid=0;
	            GeneId=0;
	            phenotypeCount=0;
	            UniprotKb="";
	            s="";
	            nodecount=1;
	            System.out.println(rowcount++);
	            while (cellIterator.hasNext()) {
	                        Cell cell = (Cell) cellIterator.next(); 
	    			if (cell.getColumnIndex() == 0) 
	    				mimid=(int) cell.getNumericCellValue();
	    			if (cell.getColumnIndex() == 1) 
	    				UniprotKb=cell.getStringCellValue();
	    			if (cell.getColumnIndex() == 2) 
	    				GeneId=(int) cell.getNumericCellValue();
	    			if (cell.getColumnIndex() == 3) 
	    				phenotypeCount=(int) cell.getNumericCellValue();
	    			if (cell.getColumnIndex() == 5)
	    			{
	    				s=cell.getStringCellValue();
	    				String[] inputsrc = s.split(",");
	    				nodecount=inputsrc.length;
                        ArrayList<Integer> source=new ArrayList<Integer>();
                        for(int i=0;i<nodecount;i++)
	    				{
	    					if(indexmap.containsKey(inputsrc[i].trim())) 
 						        source.add(indexmap.get(inputsrc[i].trim()));		
	    				}
                        if(source.size()==1)
                          {
                              printoutput=true;
                              int[]des={0};
                              nodecount=1;
	    		      t.onemfterm(des,source.get(0),0);
                          }
                        else
                             t.dijkstrasource(source);
	    			}
	            }
	        }
			t.closeoutputfile();
			System.out.println("final result updated into output file");

	}

}

