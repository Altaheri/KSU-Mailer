/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.ksu.mailnoti.coremodules;

import com.ksu.mailnoti.core.ocr.OCREngine;
import com.ksu.mailnoti.core.text.InputNameImprover;
import com.ksu.mailnoti.start.StartPanel;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.math.geometry.shape.Rectangle;

/**
 *
 * @author Hamdi T. Altaheri
 */
public class TextRecognition {
    public TextDetection textDetection;
    public OCREngine ocrEngine;
    public FuzzyNameMatching fuzzyNameMatching;
    public boolean recogRunningState = false;
    
    BufferedImage textRegionsBufImage;
    Object textRegions[];
    
    public TextRecognition(StartPanel startPanel){
        textDetection = new TextDetection(this);
        ocrEngine = new OCREngine();
        fuzzyNameMatching = new FuzzyNameMatching(startPanel);
        initTextRecognition();
    }
    
    private void initTextRecognition(){
        new Thread(new Runnable(){
            File outputFile = new File("trueresult.txt");
            int imageIndex=0;
            public void run(){
                while(true){
                    if (recogRunningState == true){
                        //try{Thread.sleep(5);}catch (InterruptedException IEx) {IEx.printStackTrace();}
                        final long start = System.currentTimeMillis();
                        for (int i = 0; i < textRegions.length; i++) {
                            startOCR(textRegionsBufImage, (Rectangle)textRegions[i]);
                            if(recogRunningState == false){
                                break;
                            }
                        }
                        
                        
                        long end = System.currentTimeMillis();
                        long timeTaken = end - start;
                        //System.out.println("OCR update frame Time taken: " + timeTaken/1000.0 + " milseconds");
                        recogRunningState = false;
                    }else try{Thread.sleep(5);}catch (InterruptedException IEx) {IEx.printStackTrace();}
                    //System.out.println(swtVideo.textRegion+"---------"+swtVideo.isSceneChanged());
                }
            }
        }).start();
    }
    
    public void startTextRecognition(BufferedImage bufferedImage, Object textRegions[]){
        if(textRegions!=null && textRegions.length>=1 && bufferedImage!=null){
            if(recogRunningState==false){
                textRegionsBufImage = bufferedImage;
                this.textRegions = textRegions;
                recogRunningState = true;
            }
        }
    }
    
    public void startTextRecognition(BufferedImage bufferedImage, Rectangle textRegion){
        Object textRegions[] = new Object[1];
        textRegions[0] = textRegion;
        startTextRecognition(bufferedImage, textRegions);
    }
    
    private void startOCR(BufferedImage bufferedImage, Rectangle area){
        
//        final long start = System.currentTimeMillis();
        
        if(area != null && area.getWidth()>1 && area.getHeight()>1) {
            BufferedImage croppedImage = bufferedImage.getSubimage((int)area.x, (int)area.y, (int)area.width, (int)area.height);
            
            
            BufferedImage deskewedCroppedImage = ocrEngine.deskewImage(croppedImage, 2);
            //BufferedImage deskewedCroppedImage = croppedImage;

            //DisplayUtilities.display(deskewedCroppedImage, "deskewedCroppedImage");
            //frame.drawImage(ImageUtilities.createMBFImage(deskewedCroppedImage, true), deskewedCroppedImage.getMinX(),deskewedCroppedImage.getMinY());
            //DisplayUtilities.display(frame, "deskewedCroppedImage");
            
            
            String text = ocrEngine.doOCR(deskewedCroppedImage);
            String text1=null;
            String text2=null;
            String text3=null;
            String text4=null;
            String text5=null;
            //System.out.println("\n\n-------doOCR---------------------------------------------\n"+text);
            if(text!=null && !text.equals("")){
                text1 = (text.replaceAll("\n\n|\r\n|\r|\n", "^_^")).replace("^_^","\n");
                System.out.print(text1);
                text2 = ocrEngine.matchAndCorrectData(text1);
                text3 = InputNameImprover.improveName(text2);
                text4 = ocrEngine.matchAndCorrectData(text3);
                //System.out.println("----------matchAndCorrectData------------------------------------------\n"+text);
                text5 = ocrEngine.findName(text4);
                //System.out.println("-------findName---------------------------------------------\n"+text);
                
                
            }
            if(text5!= null && !text.equals("")){
                String matchedRecord = fuzzyNameMatching.startFuzzyNameMatching(text5, false);
                if (matchedRecord != null){
                    textDetection.setRunningState(false);
                    textDetection.setNameRegion( area);                    
                    recogRunningState = false;
                    try {
                        String fileName = matchedRecord+getTime()+".png";
                        File outputdir = new File("c:\\ksumailer\\tempdata\\"+getDate());
                        if(!outputdir.exists())
                            outputdir.mkdirs();
                        File outputfile = new File(outputdir, fileName);
                        
                        ImageIO.write(textRegionsBufImage, "png", outputfile);
                    } catch (IOException e) {         System.err.println(e.getMessage());                  }
                    //ImageUtilities.write(textRegionsBufImage, new File("dataset2\\"+(++imageIndex)+".png"));                    
                }
                
            }
            
//                long end = System.currentTimeMillis();
//                long timeTaken = end - start;
//                System.out.println("OCR update frame Time taken: " + timeTaken/1000.0 + " milseconds");
            
        }
    }
    private String getDate(){
        DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        Date date = new Date();
        return dateFormat.format(date); //20140806       
    }    
    private String getTime(){
        DateFormat dateFormat = new SimpleDateFormat("hhmmss");
        Date date = new Date();
        return dateFormat.format(date); //20140806       
    } 
}
