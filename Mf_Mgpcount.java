import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
public class Mf_Mgpcount {
	private HashMap<Integer,Integer> Phenotypecount=new HashMap<Integer,Integer>();
	private HashMap<Integer,Integer> mimgeneid=new HashMap<Integer,Integer>();
	private HashMap<String,String> uniprotkb=new HashMap<String,String>();
	private HashMap<Integer,String> mimuniprotkb=new HashMap<Integer,String>();
	private HashSet<String>set=new HashSet<String>();
	private int prevmimid=0;
	int k=0;
	File file;
	FileInputStream fis;
	Iterator<Row> rowIterator;
	XSSFWorkbook workbookin;
	Sheet sheet;
	Iterator<?> cellIterator;
	String mobidfile = "morbidmap.xlsx";
	String gohumanfile = "goa_human_all_mf_format.xlsx";
	String allgenefile = "all_geens_from_genenames.xlsx";
	String outputfile = "disease_Goterm_mf_count.xlsx";
	String workingDirectory=System.getProperty("user.home");
	String morbidFilePath=workingDirectory + File.separator + mobidfile;
	String gohumanFilePath=workingDirectory + File.separator + gohumanfile;
	String allgeneFilePath=workingDirectory + File.separator + allgenefile;
	String outputFilePath=workingDirectory + File.separator + outputfile;
	public void writexlfile()
	{
	try {
		//Input file declaration morbidmap and create the phenotype count map file which contains MIMID and phenotype count
		fis = new FileInputStream(morbidFilePath);
		workbookin = new XSSFWorkbook(fis);
        sheet = workbookin.getSheet("Sheet1");
        rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                String phenotype="";
                int length=0;
                ArrayList<String> alist = new ArrayList<String>();
                cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = (Cell) cellIterator.next(); 
                     if (cell.getColumnIndex() == 0) {
                    	 phenotype=cell.getStringCellValue();
                         String[] str=phenotype.split(",");
                         length=str.length;
                         for(int i=0;i<length;i++)
                        	 alist.add(str[i]);
                     }
                      if (cell.getColumnIndex() == 1) {
                    	  int mimid=(int) cell.getNumericCellValue();
                    	  if(prevmimid!=mimid)
                    	  {
                    		  prevmimid=mimid;
                    		  set.clear();
                    	  }
                    	  String pid=alist.get(length-1).trim();
                    	  
                    	  if(pid.trim().charAt(0)=='('||!set.contains(pid)||length==1)
                    	  {
                    		  set.add(pid);
                    		  if(Phenotypecount.containsKey(mimid))
                        		  Phenotypecount.put(mimid, Phenotypecount.get(mimid)+1);
                        	  else
                        		  Phenotypecount.put(mimid, 1);
                    	  } 
                       }
                }
            }
          //Input file declaration goa_human_mf_format and create the internal map which contains uniprotkb and goterms
    		fis = new FileInputStream(gohumanFilePath);
    		workbookin = new XSSFWorkbook(fis);
            sheet = workbookin.getSheet("MF_Term_Count");
            rowIterator = sheet.iterator();
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    cellIterator = row.cellIterator();
                    String uniprot="";
                    while (cellIterator.hasNext()) {
                        Cell cell = (Cell) cellIterator.next();  
                          if (cell.getColumnIndex() == 0) 
                        	  uniprot=cell.getStringCellValue();
                          if(cell.getColumnIndex() == 1)
                          {
                        	     String goterm=cell.getStringCellValue();
                        	     uniprotkb.put(uniprot.trim(),goterm);
                         }
                    }
                }
           // Input File declaration for all_geens_from_gene_names and internally create 2 maps for mimid,geneid and mimid,uniprotkb pairs
            fis = new FileInputStream(allgeneFilePath);
    		workbookin = new XSSFWorkbook(fis);
            sheet = workbookin.getSheet("Genes_from_gene");
            rowIterator = sheet.iterator();
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    cellIterator = row.cellIterator();
                    int mimid=0;
                    int geneid=0;
                    String uniprotid="";
                    while (cellIterator.hasNext()) {
                        Cell cell = (Cell) cellIterator.next();  
                          if (cell.getColumnIndex() == 1) 
                        	  geneid=(int) cell.getNumericCellValue();
                          if(cell.getColumnIndex() == 2)
                        	  mimid=(int) cell.getNumericCellValue();  
                          if(cell.getColumnIndex() == 3)
                          {
                        	  uniprotid=cell.getStringCellValue();
                        	  for(String str:uniprotid.split(","))
                        	  {
                        		  if(mimid!=0&&geneid!=0&&uniprotkb.containsKey(str.trim()))
                        		  {
                        			  mimgeneid.put(mimid,geneid);
                        			  mimuniprotkb.put(mimid,str.trim());
                        			  break;
                        		  }
                        	  }
                          }
                    }
                    
                }
            filecalling();
    	  System.out.println("output file updated");
        }
	    catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
	}
	public void filecalling()
	{	
         try{
			 FileOutputStream fileOut = new FileOutputStream(outputFilePath);
			 @SuppressWarnings("resource")
			XSSFWorkbook workbook = new XSSFWorkbook();
			 XSSFSheet worksheet = workbook.createSheet("WorkSheet1");
		     
        	 for (Entry<Integer, Integer> entry : mimgeneid.entrySet())
        	 {
        		 int mimkey=entry.getKey();
        		 XSSFRow row1 = worksheet.createRow(k);
       		     XSSFCell cellA1 = row1.createCell((short) 0);
       		     cellA1.setCellValue(mimkey);
       		     XSSFCell cellB1 = row1.createCell((short) 1);
    		     cellB1.setCellValue(mimuniprotkb.get(mimkey));
    		     XSSFCell cellC1 = row1.createCell((short) 2);
    		     cellC1.setCellValue(entry.getValue());
    		     XSSFCell cellD1 = row1.createCell((short) 3);
        		 if(Phenotypecount.containsKey(mimkey))
        			 cellD1.setCellValue(Phenotypecount.get(mimkey));
        		 else
        			 cellD1.setCellValue(0);
        		 String mfterm=mimuniprotkb.get(mimkey);
        		 XSSFCell cellE1 = row1.createCell((short) 4);
        		 cellE1.setCellValue(uniprotkb.get(mfterm).split(",").length);
        		 XSSFCell cellF1 = row1.createCell((short) 5);
        		 cellF1.setCellValue(uniprotkb.get(mfterm));
        		 k++;
        		 
        	 }
        	 workbook.write(fileOut);
       	     fileOut.flush();
       	     fileOut.close();
           }catch(IOException ioe){
		         System.out.println("Exception occurred:");
		    	 ioe.printStackTrace();
		       } 
   
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Mf_Mgpcount f = new Mf_Mgpcount();
		f.writexlfile();

	}

}
