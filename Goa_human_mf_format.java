import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
public class Goa_human_mf_format {
	private String uniprotkbid;
	private String tempcode;
	private String tempgoterm;
	private HashSet<String> goterms=new HashSet<String>();
	private boolean firsttime=true;
	private boolean writeout=false;
	private int k=0;
	private StringBuilder sb;
	String inputfile = "goa_human_all_mf.xlsx";
	String outputfile = "goa_human_all_mf_format.xlsx";
	String workingDirectory=System.getProperty("user.home");
	String inputFilePath=workingDirectory + File.separator + inputfile;
	String outputFilePath=workingDirectory + File.separator + outputfile;
	public void writexlfile()
	{
		try {
			//output file declaration
			FileOutputStream fileOut = new FileOutputStream(outputFilePath);
			@SuppressWarnings("resource")
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet worksheet = workbook.createSheet("MF_Term_Count");
			//Input file declaration
			FileInputStream fis = new FileInputStream(inputFilePath);
	        @SuppressWarnings("resource")
			Sheet sheet = new XSSFWorkbook(fis).getSheet("goa_human");
	        Iterator<Row> rowIterator = sheet.iterator();
	        while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<?> cellIterator = row.cellIterator();
	            while (cellIterator.hasNext()) {
                       Cell cell = (Cell) cellIterator.next();
                       if(firsttime==true)
                       {                   
                           if (cell.getColumnIndex() == 0) 
                        	   uniprotkbid=cell.getStringCellValue();
                           else if (cell.getColumnIndex() == 1) 
                           {
                        	   goterms.add(cell.getStringCellValue());      
                        	   firsttime=false;
                           }
                       }
                       else
                       {
                       	if (cell.getColumnIndex() == 0) {
                       		
                       		   tempcode=cell.getStringCellValue();
                               if(uniprotkbid!=tempcode)
                             	   writeout=true;
                           }
                           else if (cell.getColumnIndex() == 1) {
                        	   tempgoterm=cell.getStringCellValue();
                           	   if(writeout==false)
                           		    goterms.add(tempgoterm);
                           }
                       }
	            }
	            if(writeout==true)
	            {
	     		  // index from 0,0... cell A1 is cell(0,0)
	     		  XSSFRow row1 = worksheet.createRow(k);
	     		  XSSFCell cellA1 = row1.createCell((short) 0);
	     		  cellA1.setCellValue(uniprotkbid);
	     		  appendgoterms();
	     		   XSSFCell cellD1 = row1.createCell((short) 1);
	     		   cellD1.setCellValue(sb.toString());
	     		   k++;
	     		   uniprotkbid=tempcode;
	     		   goterms.clear();
	     		   goterms.add(tempgoterm);
	               writeout=false;
	             }
	        }
	        //updating the last record
	  		  XSSFRow row1 = worksheet.createRow(k);
	  		  XSSFCell cellA1 = row1.createCell((short) 0);
	  		  cellA1.setCellValue(uniprotkbid);
	  		  appendgoterms();
	  		  XSSFCell cellD1 = row1.createCell((short) 1);
	  		  cellD1.setCellValue(sb.toString());
	          workbook.write(fileOut);
	    	  fileOut.flush();
	    	  fileOut.close();
	    	  System.out.println("output file updated");
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void appendgoterms()
	{
		int s=goterms.size();
		sb = new StringBuilder();
		  for(String str:goterms)
		  {
			  s--;
			  if(s==0)
				 sb.append(str.trim());
			  else
			  {
				 sb.append(str.trim());
   			    sb.append(","); 
			  }
		  }
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Goa_human_mf_format f = new Goa_human_mf_format();
		f.writexlfile();
	}

}
