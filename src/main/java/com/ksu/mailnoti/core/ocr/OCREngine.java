/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksu.mailnoti.core.ocr;

import static com.ksu.mailnoti.core.ocr.OCREngine.existInfo;
import com.ksu.mailnoti.core.text.DamerauLevenshtein;
import com.ksu.mailnoti.core.text.InputNameImprover;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.ImageHelper;

/**
 *
 * @author Hamdi
 */
public class OCREngine {
    private Tesseract tessInstance;
    //private Ocr aspriseOCR = new Ocr();// create a new OCR engine
    private DamerauLevenshtein dl = new DamerauLevenshtein() ;
    public OCREngine(){
        System.setProperty("jna.library.path", "32".equals(System.getProperty("sun.arch.data.model")) ? "lib/Tess4j/win32-x86" : "lib/Tess4j/win32-x86-64");
            
        tessInstance = Tesseract.getInstance();  // JNA Interface Mapping
        tessInstance.setTessVariable("tessedit_write_images", "1");
        //Tesseract1 tessInstance = new Tesseract1(); // JNA Direct Mapping
        // File tessDataFolder = LoadLibs.extractTessResources("tessdata"); // Maven build bundles English data
        // tessInstance.setDatapath(tessDataFolder.getAbsolutePath());
    }
    
    public String doOCR(BufferedImage bi){
        return doOCR(bi, new Rectangle(bi.getWidth(), bi.getHeight()));
    }
    
    public String doOCR(BufferedImage bi, Rectangle rect){
        String result = null;        
        try {
            result = tessInstance.doOCR(bi, rect);
        } catch (TesseractException e) {
            System.err.println("Error: "+e.getMessage());
        }            
        return result;
    }
    
    public void changeLang(String lang){
        tessInstance.setLanguage(lang);
    }
    
    static final double MINIMUM_DESKEW_THRESHOLD = 0.05d;
    public BufferedImage deskewImage(BufferedImage bi, int deskewAlgorithm){
        double imageSkewAngle=0.0;
        if (deskewAlgorithm == 1){
            com.recognition.software.jdeskew.ImageDeskew id = new com.recognition.software.jdeskew.ImageDeskew(bi);
            imageSkewAngle = id.getSkewAngle(); // determine skew angle
            System.out.println("DeskewTest1 = "+imageSkewAngle);
   
        }else if(deskewAlgorithm == 2){
            com.ksu.mailnoti.util.ImageDeskew dt = new com.ksu.mailnoti.util.ImageDeskew();
            imageSkewAngle = dt.doIt(bi)*-57.295779513082320876798154814105;// determine skew angle in Radians
            System.out.println("DeskewTest2 = "+imageSkewAngle);                               
        }
        
        if ((imageSkewAngle > MINIMUM_DESKEW_THRESHOLD || imageSkewAngle < -(MINIMUM_DESKEW_THRESHOLD))) {
            bi = ImageHelper.rotateImage(bi, -imageSkewAngle); // deskew image
        } 
            
        return bi;
    }
    
    private String[] getLines(String str){
        if(str == null || str.isEmpty()) {
            return null;
        }
        String[] lines = str.split("\r\n|\r|\n");
        return  lines;
    }
    
    public static final String [] INFO = {   
        "PRIVATE AND CONFIDENTIAL",
        "Emirates NBD",
        "Samba",
        "ASSOCIATION COMPUTING MACHINERY ACM",
        "IEEE"           
    } ;
    public static final int [] existInfo = new int[INFO.length];
 
    
    public static void resetAllInfo(){
        for (int i = 0; i < existInfo.length; i++) {
            existInfo[i] = 0;
        }         
    }
    
    
    public static final float MINIMUM_MATCH_FACTOR = 80f;
    public static final float MINIMUM_NAME_CHARs = 10;
    public static final float AVERAGE_CHAR_IN_WORD = 3;    
    public static final String [] TEMP_DATA = {
        "RIYADH", "KSA", "K.S.A","SAUDI", "ARABIA", "SAUD",
        "KING", "COMPUTER", "SCIENCE","UNIVERSITY", "DEPARTMENT","CCIS",
        "COLLEGE", "COMP", "INFO", "SCHOOL","ELECTRICAL",
        "DEPT", "PO", "P.O.","BOX", "SAU","SAMBA", "KINGDOM",
        "ASSISTANT", "PROFESSOR",
        "AIRMAIL", "MAIL", "INTERNATIONAL","POST",
        "ENGINEERING","INFORMATION","UNIT","SOFTWARE",
        "UNDELIVERED", "PLEASE", "RETURN", "UNITED", 
        "CAMBRIDGE","ENGLAND","INDUSTRIAL",
        
        
        "CREDIT", "CARDS",
        "PRIVATE", "CONFIDENTIAL",
        "TAKE","ACTION","NOVEMBER",
        "QUALIFY","WIN","DETAIL",
        "ELECTRONIC","SERVICE", "REQUEST",
        "AUTORISATION", "PARIS", "INTER", "BGM", "PORT", "PAYE", "FRANCE", "PRIORITY", "PRIORITAIRE",
        "EMIRATE", "NBD",
        "BANK", "ALJAZIRA",
        "HASSAD","REWARDS","PROGRAM","MORE","USE","RIYAD","REWARD","ENJOY",
        "NCB","JEDDAH",
        "INVESTMENT",
        "STRATEGIC","TRAINING", "IRMUK", "IRM","ROYAL", "POSTAGE", "PAID",
        "RAJHI","HEAD","OFFICE","CHAIRMAN",
        "ONLINE","LEARNING","TRAINING",
        "ADVANCING","TECHNOLOGY","HUMANITY",
        "UNDERSTAND","WORLD","INLAND",
        "IEEE","ASSOCIATION", "COMPUTING", "MACHINERY","PROFESSION","ACM",
        "NAME","ADDRESS","CORRECT","PRINT",
        "LIMIT","OFFER","SAVE","YOUR","MEMBERSHIP","REINSTATE",
        "TIME", "SENSITIVE", "MATERIAL",
        "signal", "processing", "process", "magazine"
    };
    
    static final String NOT_NAME = "RIYADH"+ "KSA"+ "K.S.A"+"SAUDI"+ "ARABIA"+ "SAUD"+
                        "KING"+ "COMPUTER"+ "SCIENCE"+"UNIVERSITY"+ "DEPARTEMENT"+"CCIS"+
                        "DEPT"+ "PO"+ "P.O."+"BOX"+ "SAU"+
                        "SAMBA CREDIT CARDS";
    
    public String findName(String lines){
        return findName(getLines(lines));
    }

    public String findName(String[] lines){
        if(lines == null) return null;
        for (int i =0;i<lines.length; i++){
            lines[i] = lines[i].trim().toLowerCase();
        }
        
        int[] pwr = new int[lines.length];
        for (int i =0;i<lines.length; i++){
            for (String tempword : TEMP_DATA) {                 
                if(lines[i].contains(tempword.toLowerCase()))
                pwr[i]+=1;
            }
        }
        int min =pwr.length-1;
        for(int i=pwr.length-2; i>=0; i--){
            if(pwr[i]<pwr[min]){
                min=i;
            }else if(pwr[i]==pwr[min] && lines[i].length()>=lines[min].length()){
                min=i;
            }
        }
            
        if(pwr[min]<=1)
            return lines[min];
        else return null;
    }    

    public String matchAndCorrectData(String data){ 
        if(data == null) return null;
        data = data.trim();
        String[] lines = getLines(data);
        String[] words;
        if(lines == null) return data;
        try{
            for (String line : lines) {
                //1.
                // reject the line if it has digits ... the name does not have digit
                // we accept 2 as max. digits in the name as it is mistake convert by OCR
                int numberOfDigit = 0;
                for (char ch : line.toCharArray()) {
                    if (Character.isDigit(ch)) numberOfDigit++;
                }
                if (numberOfDigit>2) {
                    // delete this line form all paragraph
                    data = data.replaceAll(line+"\n|"+line, ""); 
                    continue; 
                }
                
                //2.
                // check minimum name characters
                // line = InputNameImprover.improveName(line);
                if (line.length()<MINIMUM_NAME_CHARs) {
                    // delete this line form all paragraph
                    data = data.replaceAll(line+"\n|"+line, ""); 
                    continue;  
                }
                
                //3.
                // check number words(parts) in name (line)
                // the name must have one space at least (have two parts)
                // line = wwwlrmukcouk ..... NOT CORRECT
                if (!line.contains(" ")) {
                    // delete this line form all paragraph
                    data = data.replaceAll(line+"\n|"+line, ""); 
                    continue;  
                }
                                                            
                //4.             
                // try to correct each word and compute average characters in line's word
                // 4.a computr -> computer
                // 4.b line = www mnnk co u ..... NOT CORRECT
                words = line.trim().split(" ");
                float averageCharInWord = 0f; 
                for (String word : words) {                
                    // 4.a correct each word in name (line)
                    String matched = matchAndCorrectWord(word);
                    if(matched!=null)
                        data = data.replace(word, matched);                   
                    averageCharInWord += word.length();
                }
                
                // 4.b check the average characters in line's word                
                if(averageCharInWord/words.length < AVERAGE_CHAR_IN_WORD){
                    // delete this line form all paragraph
                    data = data.replaceAll(line+"\n|"+line, ""); 
                    continue;                    
                }
                    
            }
        }catch(java.util.regex.PatternSyntaxException ex)   {
            System.err.println("Error: "+ex.getMessage());
            return data;
        }
        
        
        if(data.equals("|") || data.equals("||") || data.equals("|||"))
            return "";    
        
        return data;    
    }

    



    
    private String matchAndCorrectWord(String word){
        String matched = null;
        double pctDiff=100f;      
        
//        for (String tempword : TEMP_DATA) {                 
//            for(int i=0; i<3; i++){
//                if (i==1) tempword = tempword.substring(0,1).concat(tempword.substring(1).toLowerCase());//first litter capital
//                if (i==2) tempword = tempword.toLowerCase();//all litter small
//                dl.setData(word, tempword);
//                if(dl.getPctDiff()>MINIMUM_MATCH_FACTOR){
//                    if(dl.getPctDiff()==0.0) {
//                        matched = null;
//                        pctDiff=0f;                            
//                        break;
//                    }
//                    if(pctDiff>dl.getPctDiff()){
//                        pctDiff = dl.getPctDiff();
//                        matched = tempword;
//                    }
//                }                
//            }            
//        }       
    
        
        for (String tempword : TEMP_DATA) {                 
            tempword = tempword.toLowerCase();
            word = word.trim().toLowerCase();
            dl.setData(word, tempword);
            if(dl.getPctDiff()>MINIMUM_MATCH_FACTOR){
                if(dl.getPctDiff()<100.0){
                    pctDiff = dl.getPctDiff();
                    matched = tempword;
                }
                for (int i = 0; i < INFO.length; i++) {
                    if(INFO[i].toLowerCase().contains(tempword)){
                        existInfo[i]++;
                    }                    
                }                                    
            }            
        }  
               
        if (matched!=null) {            
            System.out.println("replaced: " + word +" ,  with: "+ matched+ " , because: "+ pctDiff);
        }             
        return matched;
    }    
    
    public static void main(String[] args) {
        
        OCREngine mc = new OCREngine();
        String s ="Kimg mputer science SCIECE IVERSITY PARTEMENT private and confidential \n jjfj fj ffj RgyADH hfghg";
        
        String s1 ="23\n" +
"M\n" +
"K:\n" +
"C\n" +
"p\n" +
"11\n" +
"Sa";
        s=mc.matchAndCorrectData(s);
        System.out.println(s);

//        try {
//            File imageFile = new File("images/PO1.1.jpg");
//            BufferedImage image = ImageIO.read(imageFile); 
//            String result = mc.doOCR(image);
//            String[] lines = mc.getLines(result);
//
//            System.out.println(result);
//            System.out.println(mc.findName(lines));
//        } catch (IOException e) {  e.printStackTrace(); }        
    }      
    
}
