import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Mfterm_Phenotype_Average {
	
	private int phenotypecount=0;
	private int numberofterms=0;
	private int k=0;
	private Map<Integer,Integer>gotermgenecount=new TreeMap<Integer,Integer>();
	private Map<Integer,Integer>gotermphenotypecount=new TreeMap<Integer,Integer>();
	String inputfile = "Gene_Final_MF_Updaed_File.xlsx";
	String outputfile = "Phenotype_MFterm_average.xlsx";
	String workingDirectory=System.getProperty("user.home");
	String inputFilePath=workingDirectory + File.separator + inputfile;
	String outputFilePath=workingDirectory + File.separator + outputfile;
	public void writexlfile()
	{
		try {
			//output file declaration
			FileOutputStream fileOut = new FileOutputStream(outputFilePath);
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet worksheet = workbook.createSheet("Phenotype_MFterm_Avg");
			//Input file declaration
			FileInputStream fis = new FileInputStream(inputFilePath);
	        Sheet sheet = new XSSFWorkbook(fis).getSheet("Gene_Info");
	        Iterator<Row> rowIterator = sheet.iterator();
	        while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator cellIterator = row.cellIterator();
	            while (cellIterator.hasNext()) {
                       Cell cell = (Cell) cellIterator.next();
                       	if (cell.getColumnIndex() == 3) 
                       		   phenotypecount=(int)cell.getNumericCellValue();
                           else if (cell.getColumnIndex() == 4) {
                        	   numberofterms=(int)cell.getNumericCellValue();
                        	   if(gotermphenotypecount.containsKey(numberofterms))
                        	   {
                        		   gotermphenotypecount.put(numberofterms, gotermphenotypecount.get(numberofterms)+phenotypecount); 
                        		   gotermgenecount.put(numberofterms, gotermgenecount.get(numberofterms)+1);
                        	   }
                        	   else
                        	   {
                        		   gotermphenotypecount.put(numberofterms, phenotypecount);
                        		   gotermgenecount.put(numberofterms, 1);
                        	   }
                        		   
                           }
	            }
	            
	        }
	        for(Map.Entry<Integer, Integer> entry:gotermphenotypecount.entrySet())
	        {
	           int count=gotermgenecount.get(entry.getKey());
	           XSSFRow row1 = worksheet.createRow(k);
   		       XSSFCell cellA1 = row1.createCell((short) 0);
   		       cellA1.setCellValue(entry.getKey());
   		       XSSFCell cellB1 = row1.createCell((short) 1);
   		       cellB1.setCellValue(count);
   		       XSSFCell cellC1 = row1.createCell((short) 2);
   		       cellC1.setCellValue(entry.getValue());
   		       XSSFCell cellD1 = row1.createCell((short) 3);
   		       float avg=(float)entry.getValue()/(float)count;
   		       cellD1.setCellValue(Math.round(avg*100.0)/100.0);
	           k++;
	        }
	        workbook.write(fileOut);
	        System.out.println("output file updated");
	        fileOut.flush();
	    	fileOut.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Mfterm_Phenotype_Average f=new Mfterm_Phenotype_Average();
		f.writexlfile();

	}

}
