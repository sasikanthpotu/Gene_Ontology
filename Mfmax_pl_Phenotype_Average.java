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

public class Mfmax_pl_Phenotype_Average {
	private int phenotypecount=0;
	private int pldistance=0;
	private int k=0;
	private Map<Integer,Integer>pldistancegenecount=new TreeMap<Integer,Integer>();
	private Map<Integer,Integer>pldistancephenotypecount=new TreeMap<Integer,Integer>();
	String inputfile = "Gene_Final_MF_Updaed_File.xlsx";
	String outputfile = "Phenotype_MF_average.xlsx";
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
			XSSFSheet worksheet = workbook.createSheet("Phenotype_Avg");
			//Input file declaration
			FileInputStream fis = new FileInputStream(inputFilePath);
	        @SuppressWarnings("resource")
			Sheet sheet = new XSSFWorkbook(fis).getSheet("Gene_Info");
	        Iterator<Row> rowIterator = sheet.iterator();
	        while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<?> cellIterator = row.cellIterator();
	            while (cellIterator.hasNext()) {
                       Cell cell = (Cell) cellIterator.next();
                       	if (cell.getColumnIndex() == 3) 
                       		   phenotypecount=(int)cell.getNumericCellValue();
                           else if (cell.getColumnIndex() == 6) {
                        	   pldistance=(int)cell.getNumericCellValue();
                        	   if(pldistancephenotypecount.containsKey(pldistance))
                        	   {
                        		   pldistancephenotypecount.put(pldistance, pldistancephenotypecount.get(pldistance)+phenotypecount); 
                        		   pldistancegenecount.put(pldistance, pldistancegenecount.get(pldistance)+1);
                        	   }
                        	   else
                        	   {
                        		   pldistancephenotypecount.put(pldistance, phenotypecount);
                        		   pldistancegenecount.put(pldistance, 1);
                        	   }
                        		   
                           }
	            }
	            
	        }
	        for(Map.Entry<Integer, Integer> entry:pldistancephenotypecount.entrySet())
	        {
	           //updating the last record
	           int count=pldistancegenecount.get(entry.getKey());
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
        Mfmax_pl_Phenotype_Average f=new Mfmax_pl_Phenotype_Average();
		f.writexlfile();
	}

}
