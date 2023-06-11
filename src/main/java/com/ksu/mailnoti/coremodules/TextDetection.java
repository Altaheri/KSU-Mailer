/**
 *
 * @author Hamdi
 */

package com.ksu.mailnoti.coremodules;

import com.ksu.mailnoti.util.ImageHelper;
import org.openimaj.image.text.extraction.swt.*;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.paint.Color;
import javax.swing.JOptionPane;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.typography.hershey.HersheyFont;
import org.openimaj.math.geometry.shape.Rectangle;
import org.openimaj.video.Video;
import org.openimaj.video.VideoDisplay;
import org.openimaj.video.VideoDisplayListener;
import org.openimaj.video.capture.Device;
import org.openimaj.video.capture.VideoCapture;
import org.openimaj.video.capture.VideoCaptureException;

// Input Unit + Text Detection Unit
public class TextDetection implements VideoDisplayListener<MBFImage>{
    private final int gridWidth = 640/5;
    private final int gridHeight = 480/5; 
    
    public final static int LINE_BASED_REGION = 1;
    public final static int BLOCK_BASED_REGION = 2;
    public int textRegionsType = LINE_BASED_REGION;
    public ArrayList <Rectangle> textRegions = new ArrayList(5);
    public Rectangle stableTextRegion = null;
    public Rectangle currentTextRegion = null;
    public Rectangle lastTextRegion = null;
    public Rectangle lastLastTextRegion = null;
    
    final double MIN_TEXT_AREA = 10000;
    final double MAX_ASPECT_RATIO = 6;

    /** The video */
    private Video<MBFImage> video;   
    /** The video display which will play the video */
    public VideoDisplay<MBFImage> videoDisplay;
    /** The image component into which the video is being painted (reused) */
    private DisplayUtilities.ImageComponent imageComponent;
    /** The thread which is running the video playback */
    private Thread videoThread;
    /** for Scene Change Detection */
    private int currentFrameIndex = 0;
    public MBFImage frameOfCurrentTextRegion;
    private MBFImage currentFrame;
    private FImage currentFFrame;
    private FImage lastFFrame;

    public int frameSteadyThreshold = 500;
    public int sceneChangedThreshold = 15000;
    private boolean frameSteady = false;
    private boolean sceneChanged = true;
    private boolean detectRunningState = true;
    
    public final SWTTextDetector detector = new SWTTextDetector();
    public final static int SWT_RUN_ON_THREAD = 1;
    public final static int SWT_RUN_ON_FRAMUPDATE = 2;
    private int swtRunningMode = SWT_RUN_ON_FRAMUPDATE;
    private SWTThread swtThread;

    private final boolean testMode =false;    

    private AdvanceSetting advanceSetting;
    private final TextRecognition textRecognition;
    
    public double rotationAngle = 182;
    public final static int LINES_VIEW = 1;
    public final static int ANALYTIC_VIEW = 2;    
    private int detectionDisplaymethod = LINES_VIEW;
    private Rectangle nameRegion = null;
    
    
    public TextDetection(TextRecognition textRecognition){ 
        this.textRecognition = textRecognition;
        //initVideo();  
        initSWT();       
        advanceSetting = new AdvanceSetting(this);
    }        

    /**
     * 	Sets up all the Video.
     */
    public boolean initVideo(){      
        try {
            List<Device> devices = VideoCapture.getVideoDevices();
            if(devices.size()==0){
                        //custom title, error icon
                JOptionPane.showMessageDialog(null,
                    "There is no Camera in this machine.",
                    "Inane error",
                    JOptionPane.ERROR_MESSAGE);
                return false;                   
            }
            
            Device dev=null;
            for (int i = 0; i < devices.size(); i++) {
                dev = devices.get(i);
                if(dev.getNameStr().toLowerCase().contains("microsoft"))
                    break;
                //System.out.println(((VideoDevice)dev));
            }

            // Setup a new video from the VideoCapture class
            if (dev != null){
                video = new VideoCapture( 640,480, dev);
                System.out.println(dev.getNameStr());
                System.out.println(video.getHeight()+" "+video.getWidth());
                //System.exit(0);  
            }else{
                video = new VideoCapture( 640,480);
            }
                
            
            if(testMode){
                videoDisplay = VideoDisplay.createVideoDisplay(video);
                imageComponent = videoDisplay.getScreen();               
            }else{
                imageComponent = new DisplayUtilities.ImageComponent( true );
                // Reset the video displayer to use the capture class
                videoDisplay = new VideoDisplay<MBFImage>( video, imageComponent );                
            }
            
            imageComponent.setPreferredSize( new Dimension(320, 240) );
            imageComponent.setAllowZoom(false);
            imageComponent.setAutoFit(true);
            //imageComponent.setShowXYPosition(false);
            //imageComponent.setShowPixelColours(false);          

            videoDisplay.addVideoListener(this);
            currentFrame = video.getNextFrame();
            lastFFrame = currentFrame.flatten();
            
            // Start the new video playback thread
            this.videoThread = new Thread(this.videoDisplay);
            this.videoThread.start();
        } catch (VideoCaptureException ex) {
            Logger.getLogger(TextDetection.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Error: "+ex.getMessage());
            return false;
        }
        return true;
    }  
    
    /**
     * 	Sets up all the SWT.
     */  
    private void initSWT(){      
        detector.getOptions().direction = SWTTextDetector.Direction.DarkOnLight;
        detector.getOptions().maxDiameterStrokeRatio = 50;//10
        detector.getOptions().minArea = 20;//38
        detector.getOptions().minHeight = 7;//10
        detector.getOptions().maxHeight = 35;//300
        detector.getOptions().minLettersPerLine = 5;//3
//        detector.getOptions().doubleSize = true;//false
        
        swtThread = new SWTThread();
        swtThread.start();
        activateSWTThread();                    

    } 
    
    public void activateSWTThread(){
        if(swtRunningMode == SWT_RUN_ON_THREAD){
            swtThread.resumeRun();
        }
    }
    public void deactivateSWTThread(){
        swtThread.pauseRun = true;
    }
    
    public void changeRunningMode(int runningMode){
        if(runningMode == SWT_RUN_ON_THREAD){
            if(swtRunningMode != SWT_RUN_ON_THREAD){
                swtRunningMode = SWT_RUN_ON_THREAD;
                activateSWTThread();
            }
        }else if(runningMode == SWT_RUN_ON_FRAMUPDATE){
            if(swtRunningMode != SWT_RUN_ON_FRAMUPDATE){
                deactivateSWTThread();
                swtRunningMode = SWT_RUN_ON_FRAMUPDATE;
            }
        }
    }   
    
    public boolean getRunningState(){
        return detectRunningState;
    }
    public void setRunningState(boolean state){
//        if(state == false){
//            swtThread.pauseRun = true;
//        }else if(swtRunningMode == SWT_RUN_ON_THREAD){
//            swtThread.resumeRun();
//        }
        
        sceneChanged = state; 
        detectRunningState = state; 
    }
    public void setNameRegion(Rectangle area){
        nameRegion = area;
    }    
    
    private void startSWT(MBFImage frame){
        //currentFrame = frame;
        currentFFrame = frame.flatten().normalise();
        checkSceneChange(currentFFrame);                 
        
        if(!detectRunningState){
            if(!textRegions.isEmpty()){
                textRegions.clear();
                //nameRegion = null;
            }              
        }else  {           
            final long start = System.currentTimeMillis();
            
            detector.analyseImage(currentFFrame);   
            extractTextRegion();     
            
            final long end = System.currentTimeMillis();
            final long timeTaken = end - start;
            System.out.println("SWT_Analyser Time taken: " + timeTaken + " milseconds");
            if ( /*frameSteady ||*/detectRunningState){
                //System.out.println("ocr running");
                textRecognition.startTextRecognition(ImageUtilities.createBufferedImage(currentFFrame), textRegions.toArray());
                
            }
            //else
                //System.out.println("frameSteady = "+frameSteady +"   detectRunningState = "+ detectRunningState);
        }        
    }
        
    private void extractTextRegion(){
        if(detector.getLines() == null && detectRunningState) 
            return;
        
        ArrayList <Rectangle> list = new ArrayList(5);   

        // extract line TextRegions if textRegionsType = LINE_BASED_REGION
        if(textRegionsType == LINE_BASED_REGION){
            for (final LineCandidate line : detector.getLines()) {
                list.add(line.getRegularBoundingBox());
            }            
        }
        
        // extract block(of lines) TextRegions if textRegionsType = BLOCK_BASED_REGION        
        else if(textRegionsType == BLOCK_BASED_REGION){
            //final MBFImage allLetters = currentFrame.clone();
            for (final LineCandidate line : detector.getLines()) {
                //System.out.println(detector.getLines().size());
                for (final LetterCandidate lc : line.getLetters()){                            
                    Rectangle temp = lc.getRegularBoundingBox();
                    float expansionAmount = (float) (0.3 * Math.sqrt(temp.calculateArea()));
                    float x = (float) (temp.minX() - expansionAmount );
                    float y = (float) (temp.minY() - expansionAmount );
                    float width = (float)( temp.getWidth()+2*expansionAmount );
                    float height = (float)( temp.getHeight()+2*expansionAmount );

                    //allLetters.drawShape(temp, 2, RGBColour.GREEN);
                    temp.setBounds(x, y, width, height);
                    //allLetters.drawShape(lc.getRegularBoundingBox(), 1, RGBColour.RED);

                    if(list.isEmpty()){
                        list.add(temp);
                    }else {
                        boolean isOverlap=false;
                        for (int i = 0; i < list.size(); i++) {
                            if(list.get(i).isOverlapping(temp)){
                                list.set(i, list.get(i).union(temp));
                                isOverlap=true;
                                break;
                            }
                        }
                        if( !isOverlap){
                            list.add(temp);
                        }
                    }
                }
            }

//            DisplayUtilities.display(allLetters, "All candidate letters");
//            dispalyCurrentRegions(currentFrame.clone(), list, "Text Regions");
            mergeOverlappedRegions(list);   
//            dispalyCurrentRegions(currentFrame.clone(), list, "mergeOverlappedRegions 1");             
            mergeNeighboringLines(list, "Vertical");        
//            dispalyCurrentRegions(currentFrame.clone(), list, "mergeNeighboringLines - Vertical");             
            mergeOverlappedRegions(list);    
//            dispalyCurrentRegions(currentFrame.clone(), list, "mergeOverlappedRegions 2");             
            mergeNeighboringLines(list, "Horizontal");        
//            dispalyCurrentRegions(currentFrame.clone(), list, "mergeNeighboringLines - Horizontal");             
            mergeOverlappedRegions(list);             
//            dispalyCurrentRegions(currentFrame.clone(), list, "mergeOverlappedRegions - 3");             
            computeStableRegions(list);            
        }       


        // make sure that all regions are inside image dimensions
        Rectangle area;
        for (int i = 0; i < list.size(); i++) {
            area = list.get(i);           
            if(area.y+area.height>video.getHeight())
                area.height = video.getHeight()-area.y;
            if(area.x+area.width>video.getWidth())
                area.width = video.getWidth()-area.x;
            if(area.x<0) 
                area.x=0;
            if(area.y<0) 
                area.y=0;
        }
        
        this.textRegions = list;
        //this.frameOfCurrentTextRegion = this.currentFrame.clone();
        //this.frameOfCurrentTextRegion = this.currentFrame;
    }
    
    private void dispalyCurrentRegions(MBFImage image, List <Rectangle> list, String caption){
        for (int i = 0; i < list.size(); i++) {
            image.drawShape(list.get(i), 2, RGBColour.GREEN);
        }
        DisplayUtilities.display(image, caption);        
    }
    
    private void computeStableRegions(ArrayList <Rectangle> textRegions){        
        currentTextRegion = null;
        for (Rectangle region : textRegions) {
            if(region.calculateArea()> MIN_TEXT_AREA){
                if((region.width/region.height)<MAX_ASPECT_RATIO){
                    if(currentTextRegion==null){
                        currentTextRegion = region;
                    }else if( region.calculateArea()>currentTextRegion.calculateArea())//||
                            //(((region.width/region.height)<(textRegion.width/textRegion.height))&&
                            //(region.y>textRegion.getBottomRight().getY())))
                    {
                        currentTextRegion = region;
                    }
                }else if(currentTextRegion==null){
                    currentTextRegion = region;
                }else if( region.calculateArea()>currentTextRegion.calculateArea())//||
                            //(((region.width/region.height)<(textRegion.width/textRegion.height))&&
                            //(region.y>textRegion.getBottomRight().getY())))
                {
                        currentTextRegion = region;
                }
            }                    
        }   
        
        if (currentTextRegion!= null){
            if(currentTextRegion.y+currentTextRegion.height>video.getHeight())
                currentTextRegion.height = video.getHeight()-currentTextRegion.y;
            if(currentTextRegion.x+currentTextRegion.width>video.getWidth())
                currentTextRegion.width = video.getWidth()-currentTextRegion.x;
            if(currentTextRegion.x<0)
                currentTextRegion.x=0;
            if(currentTextRegion.y<0)
                currentTextRegion.y=0;
        }
            
        if(stableTextRegion == null){
            stableTextRegion = currentTextRegion;
        }else {
            double currentState = getPercentageOverlap(currentTextRegion, lastTextRegion);
            //currentState += getPercentageOverlap(currentTextRegion, lastLastTextRegion);
            
            double stableState = getPercentageOverlap(stableTextRegion, lastTextRegion);
            //stableState += getPercentageOverlap(stableTextRegion, lastLastTextRegion);
            
            if( currentState >= stableState){
                stableTextRegion = currentTextRegion;
            }
        }
                
        lastLastTextRegion = lastTextRegion;
        lastTextRegion = currentTextRegion;                
    }    
    
    private double getPercentageOverlap(Rectangle rec1, Rectangle rec2){
        if ( (rec1 == null) || (rec2 == null) ){
            return 0.0;
        }else{
            return Math.min(rec1.percentageOverlap(rec2), rec2.percentageOverlap(rec1));
        }
    }
    
    private void mergeOverlappedRegions(ArrayList <Rectangle> textRegions){
        for (int i = 0; i < textRegions.size(); i++) {            
            for (int j = 0; j < textRegions.size(); j++) {
                if(j!=i){
                    if(textRegions.get(i).isOverlapping(textRegions.get(j))){
                        textRegions.set(i, textRegions.get(i).union(textRegions.get(j)));
                        textRegions.remove(j);
                        j--;
                        
                        if(i==textRegions.size())
                            break;
                    }
                }                
            }
        } 
    }
    
    private void mergeNeighboringLines(ArrayList <Rectangle> textRegions, String direction){
        for (int i = 0; i < textRegions.size(); i++) {            
            for (int j = 0; j < textRegions.size(); j++) {
                if(j!=i){ 
                    if (direction.equals("Vertical")){
                        if( (Math.abs(textRegions.get(i).minX()-textRegions.get(j).minX()) < Math.min(textRegions.get(i).getHeight(), textRegions.get(j).getHeight())/4) && 
                                ( Math.abs(textRegions.get(i).maxY()-textRegions.get(j).minY()) <= Math.min(textRegions.get(i).getHeight(), textRegions.get(j).getHeight())) )
                        {                    
                            textRegions.set(i, textRegions.get(i).union(textRegions.get(j)));
                            textRegions.remove(j);
                            j--;

                            if(i==textRegions.size())
                                break;
                        }
                    }
                    else if (direction.equals("Horizontal")){
                        if( (Math.abs(textRegions.get(i).maxX()-textRegions.get(j).minX()) < 20) &&
                                (Math.abs(textRegions.get(i).minY()-textRegions.get(j).minY()) < 20)
                                ) {                    
                            textRegions.set(i, textRegions.get(i).union(textRegions.get(j)));
                            textRegions.remove(j);
                            j--;

                            if(i==textRegions.size())
                                break;
                        }
                    }                    
                    
                }                
            }
        }      
    }   
    
    private void checkSceneChange (FImage currentFFrame){               
        // compute the squared difference from the last frame
        float val = 0;
        for (int y = 0; y < currentFFrame.height; y++) {
            for (int x = 0; x < currentFFrame.width; x++) {
                final float diff = (currentFFrame.pixels[y][x] - lastFFrame.pixels[y][x]);
                val += diff * diff;
            }
        }
        // System.out.println("motion: "+val);        
        // might need adjust threshold:


        if(val < frameSteadyThreshold){
            frameSteady = true;
        }else{
            frameSteady = false;
            if (val > sceneChangedThreshold) {
                if(sceneChanged == true){
                    detectRunningState = true;
                }else{
                    sceneChanged = true;
                }
                System.out.println("motion: "+val);
                System.out.println(sceneChanged+ "  :   "+detectRunningState);
            }
        }
        
        // set the current frame to the last frame
        lastFFrame = currentFFrame;                
    }
    
    @Override
    public void afterUpdate(VideoDisplay<MBFImage> display) {  }
    
    @Override
    public void beforeUpdate(MBFImage frame) {       
        if (frame == null){
            return; 
        }
        
        rotate(frame);
        
        currentFrameIndex++;
        if(swtRunningMode == SWT_RUN_ON_FRAMUPDATE)
            if(currentFrameIndex % 3 == 0){
                startSWT(frame);
        }

        // draw TextRegions which depend on the value of the attribute textRegionsType as:
        // line TextRegions if textRegionsType = LINE_BASED_REGION
        // block of lines if textRegionsType = BLOCK_BASED_REGION
        try{
            if(detectionDisplaymethod == this.LINES_VIEW){
                if(textRegionsType == this.BLOCK_BASED_REGION)
                    drawRegions(frame, false, false, false, true, true);
                else
                    drawRegions(frame, false, false, false, true, false);
            }else if (detectionDisplaymethod == this.ANALYTIC_VIEW){
                BufferedImage img = ImageUtilities.createBufferedImageForDisplay(frame);
                setImageTransparency(frame, 0.9);
                if(nameRegion!=null){
                    BufferedImage croppedImage = img.getSubimage((int)nameRegion.x, (int)nameRegion.y, (int)nameRegion.getWidth(), (int)nameRegion.getHeight());
                    frame.drawShape(nameRegion, 3, RGBColour.BLUE);
                    //frame.drawShape(new Rectangle((int)nameRegion.x, (int)nameRegion.y, (int)nameRegion.getWidth(), (int)nameRegion.getHeight()), RGBColour.RED);
                    MBFImage frame1 = ImageUtilities.createMBFImage(croppedImage, true);                    
                    frame.drawImage(frame1, (int)nameRegion.x, (int)nameRegion.y);
                }
            }
            
        }catch(java.util.ConcurrentModificationException ex){
            System.err.println("Error: ConcurrentModificationException, " + ex.getMessage());
        }
        drawGrid(frame);      
    }
    
    private void rotate(MBFImage frame) {
        //try{Thread.sleep(3000);}catch (InterruptedException IEx) {IEx.printStackTrace();}        
        //final long start = System.currentTimeMillis();
        BufferedImage img = ImageUtilities.createBufferedImageForDisplay(frame);
        //DisplayUtilities.display(img, "img");
        BufferedImage rimg = ImageHelper.rotateImage(img, rotationAngle);
        //DisplayUtilities.display(rimg, "rimg");       
        MBFImage frame1 = ImageUtilities.createMBFImage(rimg, true);
        //System.out.println(frame1);
        //System.out.println(frame);
        frame.drawImage(frame1, 0, 0);

        frame.drawShapeFilled(new Rectangle(0,0,640,7*(int)Math.abs(180-rotationAngle)), RGBColour.BLACK);
        frame.drawShapeFilled(new Rectangle(0,0,6*(int)Math.abs(180-rotationAngle),480), RGBColour.BLACK);
        frame.drawLine(0, 7*(int)Math.abs(180-rotationAngle), 640, 7*(int)Math.abs(180-rotationAngle), RGBColour.DARK_GRAY);
        frame.drawLine(6*(int)Math.abs(180-rotationAngle), 0, 6*(int)Math.abs(180-rotationAngle), 480, RGBColour.DARK_GRAY);
        //long end = System.currentTimeMillis();
        //long timeTaken = end - start;
        //System.out.println("Rotate Time taken: " + timeTaken/1000.0 + " milseconds");        
    }
    
/*******************************************************************************/
/*-------------------->( Method To : ------------------- )<--------------------*/
    Color color = Color.BLUE;
    private void setImageTransparency(MBFImage frame, double transparency) {
        BufferedImage img = ImageUtilities.createBufferedImageForDisplay(frame);
        
    	int imageW = img.getWidth();
    	int imageH = img.getHeight();
        
		int pixel[]= new int[3];
		int lBCrgb[] = new int[3];
    	//System.out.println("imageW = "+imageW+"  imageH = "+imageH);
    	
		BufferedImage bufImage = new BufferedImage(imageW, imageH, BufferedImage.TYPE_INT_RGB);
		bufImage.getGraphics().drawImage(img, 0, 0, null);
		WritableRaster wrRaster = bufImage.getRaster();
		
		lBCrgb[0] = (int)Math.round(transparency*color.getRed());
		lBCrgb[1] = (int)Math.round(transparency*color.getGreen());
		lBCrgb[2] = (int)Math.round(transparency*color.getBlue());
    	//System.out.println("lBCrgb[0] = "+lBCrgb[0]+"  lBCrgb[1] = "+lBCrgb[1]+"  lBCrgb[2] = "+lBCrgb[2]);

		for(int y=0; y<imageH; y++)
			for(int x=0; x<imageW; x++){
				wrRaster.getPixel(x, y, pixel);
				if( (pixel[0] = ((int)Math.round((1.0-transparency)*pixel[0])+lBCrgb[0]) )> 255) pixel[0]=255;
				if( (pixel[1] = ((int)Math.round((1.0-transparency)*pixel[1])+lBCrgb[1]) )> 255) pixel[1]=255;
				if( (pixel[2] = ((int)Math.round((1.0-transparency)*pixel[2])+lBCrgb[2]) )> 255) pixel[2]=255;
				wrRaster.setPixel(x, y, pixel);
			}
        MBFImage frame1 = ImageUtilities.createMBFImage(bufImage, true);
        frame.drawImage(frame1, 0, 0);
    }
/*-------------------->( End ------------------- Method )<---------------------*/
/*******************************************************************************/
    
    
    private void drawRegions(MBFImage frame, boolean letters, boolean words, boolean lines, boolean combinedRegions, boolean stableRegion){     
        // draw letters || words || lines        
        if(letters || words || lines){
            List<LineCandidate> lineCandidateList = detector.getLines();
            if(lineCandidateList != null) {
                for (final LineCandidate lineCand : lineCandidateList) {
                    if(letters){
                        for (final LetterCandidate lc : lineCand.getLetters())
                            frame.drawShape(lc.getRegularBoundingBox(), 3, RGBColour.GREEN);
                    }
                    if(words){
                        for (final WordCandidate wc : lineCand.getWords())
                            frame.drawShape(wc.getRegularBoundingBox(), 3, RGBColour.BLUE);
                    }
                    if(lines){
                        frame.drawShape(lineCand.getRegularBoundingBox(), 3, RGBColour.RED);
                    }
                } 
            }
        }
        // draw combined Regions
        if(combinedRegions){        
            for (Rectangle region : textRegions) {
                frame.drawShape(region, 3, RGBColour.RED);
                double asRatio = region.width/region.height;
                asRatio = (double)(Math.round(asRatio*100))/100;
                frame.drawText(String.valueOf((int)region.calculateArea()),region.getBottomRight(), HersheyFont.TIMES_MEDIUM, 15 );
                frame.drawText(String.valueOf(asRatio),(int)region.x,(int)region.getBottomRight().getY(), HersheyFont.TIMES_MEDIUM, 15 );
            }         
        }        
        // draw stable TextRegion
        if(stableRegion){        
            if (stableTextRegion != null ) {
                frame.drawShape(stableTextRegion, 3, RGBColour.BLUE);
                double asRatio = stableTextRegion.width/stableTextRegion.height;
                asRatio = (double)(Math.round(asRatio*100))/100;
                frame.drawText(String.valueOf((int)stableTextRegion.calculateArea()),stableTextRegion.getBottomRight(), HersheyFont.TIMES_MEDIUM, 15 );
                frame.drawText(String.valueOf(asRatio),(int)stableTextRegion.x,(int)stableTextRegion.maxY(), HersheyFont.TIMES_MEDIUM, 15 );
            }

            if (currentTextRegion != null ) {
                frame.drawShape(currentTextRegion, 1, RGBColour.BLUE);
            }            
        }     
    }
    
    private void drawGrid (MBFImage frame){
        frame.drawLine(0, gridHeight*1, 640, gridHeight*1, RGBColour.DARK_GRAY);
        frame.drawLine(0, gridHeight*2, 640, gridHeight*2, RGBColour.DARK_GRAY);
        frame.drawLine(0, gridHeight*3, 640, gridHeight*3, RGBColour.DARK_GRAY);
        frame.drawLine(0, gridHeight*4, 640, gridHeight*4, RGBColour.DARK_GRAY);
        
        frame.drawLine(gridWidth*1, 0, gridWidth*1, 480, RGBColour.DARK_GRAY);
        frame.drawLine(gridWidth*2, 0, gridWidth*2, 480, RGBColour.DARK_GRAY);
        frame.drawLine(gridWidth*3, 0, gridWidth*3, 480, RGBColour.DARK_GRAY);
        frame.drawLine(gridWidth*4, 0, gridWidth*4, 480, RGBColour.DARK_GRAY);        
    }
    public void showAdvancedSetting(){
        advanceSetting.setLocationRelativeTo(null);
        advanceSetting.setVisible(true);
    }
    
    public static void main(String[] args) throws VideoCaptureException {
        new TextDetection(null);
        Rectangle r1=new Rectangle(0,0, 20,20);
        Rectangle r2=new Rectangle(0,0, 10,10);
        
        System.out.println(r1.percentageOverlap(r2));
        System.out.println(r2.percentageOverlap(r1));        
    }
    
    class SWTThread extends Thread {
        protected boolean pauseRun = true;
        public void run(){
            while(true){
                if(pauseRun==true||videoDisplay.isPaused()||videoDisplay.isStopped()){
                    pauseRun();
                    continue;
                }
                // * IMPORTANT to get clone Image of the next frame:
                // because method getNextFrame() return reference of
                // MBFImage to the video frame and this value will be
                // changed every moment.
                // * ALSO we should use method getNextFrame() (NOT getCurrentFrame()
                // to get pure frame without any extra drawing.
                currentFrame = video.getNextFrame().clone();
                rotate(currentFrame);
                startSWT (currentFrame);
            }
        }
        synchronized private void pauseRun(){
            try{this.wait();}catch(InterruptedException IEx){IEx.printStackTrace();}
        }
        synchronized public void resumeRun(){
            pauseRun=false;
            notify();
        }              
    }
}