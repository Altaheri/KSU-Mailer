package com.ksu.mailnoti.core.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.text.names.HumanNameParser;
import org.apache.commons.text.names.Name;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {
    File dbExcel =  new File ("db/computer science1.xlsx");
    public Name [] names;    
    public String [][] dbRecords;
    public HumanNameParser nameParser;    
 
    public ExcelReader(){
        nameParser = new HumanNameParser();
        FileInputStream fis =  null;
        try {
            fis = new FileInputStream(dbExcel);
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet ws = wb.getSheet("Sheet1");
            
            int rowNum = ws.getLastRowNum() + 1;
            int colNum = ws.getRow(0).getLastCellNum();            
            names = new Name[rowNum];
            dbRecords = new String[rowNum][colNum];
                    
            for(int i = 0; i <rowNum; i++){
                XSSFRow row = ws.getRow(i);               
                
                for (int j = 0; j < colNum; j++){
                    XSSFCell cell = row.getCell(j);
                    
                    if(j==0){
                        if(cell!=null){
                            String value = cell.toString();
                            value = value.toLowerCase().replaceAll("'|\\.|phd|ph\\.d|,|-|dr", "").trim();                          
                            if (value!=null && !value.equals("")){ 
                                names[i] = nameParser.parse(value);
                            }else{
                                System.out.println("Error reading from database,  Record number ( "+(i+1)+" )+ is incorrect");                                
                            }                            
                        }else{
                            System.out.println("Error reading from database, cell number ( "+(i+1)+", "+(1)+" )+ is NULL");
                        }                              
                    }                    
                                       
                    if(cell!=null){
                        dbRecords[i][j] = cell.toString();
                    }else{
                        dbRecords[i][j] = "";
                    }                     
                }                                                                                                                                            
            }              
        } catch (IOException ex) {ex.printStackTrace();
            Logger.getLogger(ExcelReader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(fis != null) fis.close();
            } catch (IOException ex) {ex.printStackTrace();
                Logger.getLogger(ExcelReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
        
    public String [] loadRecord(int recordIndex){
        String [] record = null;
        FileInputStream fis =  null;
        try {
            fis = new FileInputStream(dbExcel);
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet ws = wb.getSheet("Sheet1");
            int colNum = ws.getRow(0).getLastCellNum();

            record = new String[colNum];
            XSSFRow row = ws.getRow(recordIndex);
            for (int j = 0; j < colNum; j++){
                XSSFCell cell = row.getCell(j);
                if(cell!=null){
                    record[j] = cell.toString();
                }else record[j] = "";
            }
        } catch (IOException ex) {ex.printStackTrace();
            Logger.getLogger(ExcelReader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {ex.printStackTrace();
                Logger.getLogger(ExcelReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }                        
        return record;
    }  

    public boolean writeCell(int row, int col, String value, File dbExcel){
        FileInputStream fis =  null;
        try {
            //File excel =  new File ("db/computer science.xlsx");
            
            fis = new FileInputStream(dbExcel);
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet ws = wb.getSheetAt(0);

            XSSFRow xssfrow = ws.getRow(row);
            XSSFCell cell = xssfrow.getCell(col);
            cell.setCellValue(value);
            fis.close();

            FileOutputStream out = new FileOutputStream(dbExcel);
            wb.write(out);
            out.close();
            System.out.println("howtodoinjava_demo.xlsx written successfully on disk.");
        } catch (IOException ex) {ex.printStackTrace();
            Logger.getLogger(ExcelReader.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
        }        
        return true;
    } 
    
    public String [] getRecord(int recordIndex){
        return dbRecords[recordIndex];
    }       
    public String getName(int recordIndex){
        return getRecord(recordIndex)[0];
    }
    public String getMail(int recordIndex){        
        return getRecord(recordIndex)[1];
    }
    
    
    public static void main(String[] args) throws IOException {        
        ExcelReader excelReader = new ExcelReader();        
        excelReader.updateDepartment();        
    }
    
    private void printData(){
        for(int i = 0; i <names.length; i++){
            System.out.println(i+"  "+names[i].getFullName()+"   ");        
        }   
    }
    
    private void updateDepartment(){
        String CE_Department = "";
        String CS_Department = "";
        String SE_Department = "";
        String IS_Department = "";
        String IT_Unit = "";
        
        FileInputStream fis =  null;
        XSSFWorkbook wb = null;
        try {
            // TODO Auto-generated method stub
            File excel =  new File ("db/departments.xlsx");
            fis = new FileInputStream(excel);
            wb = new XSSFWorkbook(fis);
            XSSFSheet ws = wb.getSheet("Sheet1");
            int rowNum = ws.getLastRowNum() ;
            System.out.println("rowNum = "+rowNum);
           
            for(int i = 0; i <=rowNum; i++){
                XSSFRow row = ws.getRow(i);      
                XSSFCell cell;
                if((cell = row.getCell(1)) != null)CE_Department += cell.toString().toLowerCase();
                if((cell = row.getCell(2)) != null)CS_Department += cell.toString().toLowerCase();
                if((cell = row.getCell(3)) != null)SE_Department += cell.toString().toLowerCase();
                if((cell = row.getCell(4)) != null)IS_Department += cell.toString().toLowerCase();
                if((cell = row.getCell(5)) != null)IT_Unit += cell.toString().toLowerCase();
            }
            fis.close();
            
            
            fis = new FileInputStream(dbExcel);
            wb = new XSSFWorkbook(fis);
            ws = wb.getSheet("Sheet1");
            rowNum = ws.getLastRowNum() ;
           
            for(int i = 2; i <=rowNum; i++){
                XSSFRow row = ws.getRow(i);
                XSSFCell cell = row.getCell(3);
                if(cell!=null){
                    String value = cell.toString().toLowerCase().trim()+"@ksu.edu.sa";
                    if(CE_Department.contains(value)) {
                        row.getCell(4).setCellValue("COMPUTER ENGINEERING DEPARTMENT");
                    }else if (CS_Department.contains(value)){
                        row.getCell(4).setCellValue("COMPUTER SCIENCE DEPARTMENT");
                    }else if (SE_Department.contains(value)){
                        row.getCell(4).setCellValue("SOFTWARE ENGINEERING DEPARTMENT");
                    }else if (IS_Department.contains(value)){
                        row.getCell(4).setCellValue("INFORMATION SYSTEMS DEPARTMENT"); 
                    }else if (IT_Unit.contains(value)){
                        row.getCell(4).setCellValue("IT Unit");
                    }
                }                                
            }              
            fis.close();
         
            FileOutputStream out = new FileOutputStream(excel);
            wb.write(out);
            out.close();
            System.out.println("howtodoinjava_demo.xlsx written successfully on disk.");
                                       
        } catch (IOException ex) {ex.printStackTrace();
            Logger.getLogger(ExcelReader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
        }
    }    
}