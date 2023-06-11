/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksu.mailnoti.start;

import com.ksu.mailnoti.core.mail.MailAPI;
import com.ksu.mailnoti.core.ocr.OCREngine;
import com.ksu.mailnoti.core.text.MatchedName;
import com.ksu.mailnoti.coremodules.TextRecognition;
import com.ksu.mailnoti.ui.util.html.StyledTextArea;
import javax.swing.ImageIcon;
import com.ksu.mailnoti.ui.util.BorderPanel;
import com.ksu.mailnoti.ui.util.PipePanel;
import com.ksu.mailnoti.ui.util.html.HTMLCell;
import com.ksu.mailnoti.ui.util.html.HTMLTable;
import com.ksu.mailnoti.util.SoundPlayer;
import java.awt.Color;
import javax.swing.JOptionPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import org.openimaj.video.VideoDisplay;

/**
 *
 * @author Hamdi
 */

public class StartPanel extends javax.swing.JPanel implements HyperlinkListener{
    protected ImageIcon bgImageI = new ImageIcon("icons/Default.jpg");

    public final ExtractTextPanel extractTextPanel;   
    public final StyledTextArea currentMatNameArea; //extractedData styledTextArea
    private final StyledTextArea allMatNamesArea; //extractedData styledTextArea    
    private final MailAPI mailAPI;             
    
    public final int TEST_MODE = 1;
    public final int NORMAL_MODE = 2;    
    public final int DETAILED_MODE = 3;  
    public int despayMode = DETAILED_MODE;
    public boolean speakTheDepartments = false;
    
    public TextRecognition textRecognition;
     
    public StartPanel() {
        initComponents();
        
        textRecognition = new TextRecognition(this);
        mailAPI = new MailAPI();
        
        extractTextPanel = new ExtractTextPanel(this);
        
        extractTextPanel.setPaths(BorderPanel.CENTER_RIGHT, 55, 52);             
        leftPanel.add(extractTextPanel);                                           
        
        allMatNamesArea = new StyledTextArea(1, this);
        allMatNamesArea.createHTMLTable(1, "style1", "style1");
        String[] header = {"Name", "Email", "Description", "Mails Number", "Edit"};
        allMatNamesArea.insertData(0, header, true); 
        
        allMatNamesArea.setBorder(new CompoundBorder(new EmptyBorder(10, 10, 10, 10), new LineBorder(Color.LIGHT_GRAY)));       
        //extractedData.test();
        
        currentMatNameArea = new StyledTextArea(1, this);
        currentMatNameArea.createHTMLTable(1, "style2", "style2");
        currentMatNameArea.setBorder(new CompoundBorder(new EmptyBorder(10, 10, 10, 10), new LineBorder(Color.LIGHT_GRAY)));
        
        //rightJPanel2.setOpaque(false);
        rightJPanel1.add(currentMatNameArea);
        rightJPanel2.add(allMatNamesArea);
        
        PipePanel pipePanel = new PipePanel(100, PipePanel.EXTENDED_HORIZONTAL_PATH);
        middlePanel.add(pipePanel);    
    }
    
    public void activateOpticalPen(){
        extractTextPanel.irisPenPanel.requestFocus();
    }
    
    public void activateCamera(){
        if(textRecognition.textDetection.initVideo()){
            extractTextPanel.activateCamera(textRecognition.textDetection.videoDisplay.getScreen());
            textRecognition.textDetection.activateSWTThread();
        }
    }
    public void deactivateCamera(){        
        if(textRecognition.textDetection.videoDisplay != null){
            extractTextPanel.deactivateCamera();
            textRecognition.textDetection.videoDisplay.setMode(VideoDisplay.Mode.CLOSED);   
        }
    }
    
    public void updateData(String capturedName){
        extractTextPanel.extractedTextArea.resetData(false);
        CELabel.setOpaque(false);
        CSLabel.setOpaque(false);
        ISLabel.setOpaque(false);
        SELabel.setOpaque(false);
        NRLabel.setOpaque(false);        
        
        extractTextPanel.extractedTextArea.insertData(0, capturedName, false);
        if( despayMode == this.TEST_MODE){
            MatchedName matchedName = textRecognition.fuzzyNameMatching.similarNameExt.getMatchedName(0);
            if(matchedName !=null){
                currentMatNameArea.resetData(true);
                //currentMatNameArea.insertData(0, capturedName+" ---> "+improvedCapturedName, true);
                currentMatNameArea.insertData(0, textRecognition.fuzzyNameMatching.similarNameExt.getName(matchedName.getIndex())+"<span class=b> &nbsp&nbsp&nbsp&nbsp "+String.valueOf(matchedName.getBestScore())+" % "+"</span>", true);
                currentMatNameArea.insertData(0, capturedName, false);
                currentMatNameArea.insertData(0, String.valueOf(matchedName.getScoreTypeName(matchedName.getBestScoreType())), false);
                System.out.println(textRecognition.fuzzyNameMatching.similarNameExt.toString());
                System.out.println("----------------------------------------------------------");
                System.out.println("----------------------------------------------------------");
                
                String [] matchedRecord = textRecognition.fuzzyNameMatching.similarNameExt.getBestMatchedRecord();
                if(matchedRecord !=null){
                    updateMatNamesList(matchedRecord);
                }
                
            }
            
        }else if (despayMode == this.NORMAL_MODE){
            String [] matchedRecord = textRecognition.fuzzyNameMatching.similarNameExt.getBestMatchedRecord();
            currentMatNameArea.resetData(true);
            if(matchedRecord !=null){
                updateMatNamesList(matchedRecord);
                currentMatNameArea.insertData(0, matchedRecord[0], false);
                currentMatNameArea.insertData(0, matchedRecord[3]+"@KSU.EDU.SA", false);
                String depa = matchedRecord[4];
                if(depa.equals("Administration".trim()))depa="ادارة الكلية";
                else if(depa.equals("COMPUTER ENGINEERING DEPARTMENT".trim()))depa="قسم هندسة الحاسب";
                else if(depa.equals("COMPUTER SCIENCE DEPARTMENT".trim()))depa="قسم علوم الحاسب";
                else if(depa.equals("INFORMATION SYSTEMS DEPARTMENT".trim()))depa="قسم نظم معلومات";
                else if(depa.equals("IT Unit".trim()))depa="وحدة تقنية المعلومات";
                else if(depa.equals("SOFTWARE ENGINEERING DEPARTMENT".trim()))depa="قسم هندسة البرمجيات";
                else depa="غير معلوم";
                
                currentMatNameArea.insertData(0, depa, false);
                //currentMatNameArea.insertData(0, (similarNameExt.getMatchedName(0).getBestScore()+" %"), false);
                //SoundPlayer.playSound("sounds/Mouse Double Click.wav");
            }else {
                currentMatNameArea.insertData(0, "CAN NOT RECOGNIZED", false);
                //SoundPlayer.playSound("sounds/Computer Error.wav");
                //Toolkit.getDefaultToolkit().beep();
            }
        }
        else if (despayMode == this.DETAILED_MODE){
            //if(textRecognition.fuzzyNameMatching.similarNameExt.isSListUpdated||extractTextPanel.irisPenPanel.irisPenActivateState){
            if(textRecognition.fuzzyNameMatching.similarNameExt.isSListUpdated){
                currentMatNameArea.resetData(true);
                
                for (int i=0; i<textRecognition.fuzzyNameMatching.similarNameExt.similarityList.size(); i++){
                    if(i==3)
                        break;
                    MatchedName mn = textRecognition.fuzzyNameMatching.similarNameExt.similarityList.get(i);
                    String record[] = textRecognition.fuzzyNameMatching.similarNameExt.getRecord(mn.getIndex());
                    String str[] = {record[0], record[4],mn.getScoreTypeName(mn.getBestScoreType())+", "+mn.getScore(mn.FULL_MATCH), String.valueOf(mn.getBestScore()), "href{"+i+"}insert.png"};
                    currentMatNameArea.insertData(0, str, false);
                }
                
                
                String [] matchedRecord = textRecognition.fuzzyNameMatching.similarNameExt.getBestMatchedRecord();
                if(matchedRecord !=null){
                    //System.out.println(similarNameExt.toString());
                    MatchedName mn = textRecognition.fuzzyNameMatching.similarNameExt.getBestMatchedName();
                    currentMatNameArea.resetData(true);
                    String str[] = {matchedRecord[0], String.valueOf((int)mn.getBestScore())+" %"/*<br>"+mn.getScore(mn.FULL_MATCH)*/};
                    

                    
                    String depa = matchedRecord[4];
                    if(depa.equals("Administration".trim())){
                        depa="ادارة الكلية";
                        if(speakTheDepartments) SoundPlayer.playSound("sounds/nr2.wav");  
                        NRLabel.setOpaque(true);
                    }
                    else if(depa.equals("COMPUTER ENGINEERING DEPARTMENT".trim())){
                        depa="قسم هندسة الحاسب";
                        if(speakTheDepartments) SoundPlayer.playSound("sounds/ce2.wav");         
                        CELabel.setOpaque(true);
                    }
                    else if(depa.equals("COMPUTER SCIENCE DEPARTMENT".trim())){
                        depa="قسم علوم الحاسب";
                        if(speakTheDepartments) SoundPlayer.playSound("sounds/cs2.wav");     
                        CSLabel.setOpaque(true);
                    }
                    else if(depa.equals("INFORMATION SYSTEMS DEPARTMENT".trim())){
                        depa="قسم نظم معلومات";
                         if(speakTheDepartments) SoundPlayer.playSound("sounds/is2.wav");      
                        ISLabel.setOpaque(true);
                    }
                    else if(depa.equals("IT Unit".trim())){
                        depa="وحدة تقنية المعلومات";
                         if(speakTheDepartments) SoundPlayer.playSound("sounds/nr2.wav");                        
                        NRLabel.setOpaque(true);
                    }
                    else if(depa.equals("SOFTWARE ENGINEERING DEPARTMENT".trim())){
                        depa="قسم هندسة البرمجيات";
                         if(speakTheDepartments) SoundPlayer.playSound("sounds/se2.wav");                        
                        SELabel.setOpaque(true);
                    }
                    else {
                        depa="القسم غير معلوم";
                         if(speakTheDepartments) SoundPlayer.playSound("sounds/nr2.wav");                        
                        this.CELabel.setOpaque(true);
                    }
                    
                    String info = "";
                    for (int i = 0; i < OCREngine.existInfo.length; i++) {
                        if(OCREngine.existInfo[i]>0){
                            info += " "+OCREngine.INFO[i];
                        }
                    }
                    OCREngine.resetAllInfo();
                    
                        currentMatNameArea.insertData(0, depa, true); 
                        currentMatNameArea.insertData(0,str, true);   
                    //currentMatNameArea.insertData(0, "<span class=info>"+info, false);
                    updateMatNamesList(matchedRecord);
                }
                textRecognition.fuzzyNameMatching.similarNameExt.isSListUpdated = false;
            }
        }
        
        
        updateUI();
    }
    
    private void updateMatNamesList(String [] data){           
        String str[] = {data[0], data[3]+"@KSU.EDU.SA", data[4], "1", "Info_icon0.png"};  
        
        //check if the name is exist in the current mail list
        // if the name is exist, increase the number of mails of this person by one
        for(int i=0 ;i<allMatNamesArea.htmlTables.size(); i++){
            HTMLTable htmlTable = (HTMLTable)allMatNamesArea.htmlTables.get(i);
            for(int j=1; j<htmlTable.tableRows.size(); j++){
                String row[] = htmlTable.getRow(j);                 
                if(row[0].equals(data[0])){
                    HTMLCell htmlCell = htmlTable.getHTMLCell(j, 3);
                    int current_Mails = Integer.parseInt(htmlCell.getText());
                    htmlCell.setText( String.valueOf(current_Mails+1));
                    allMatNamesArea.reload();
                    return;
                }
            }
        }        
        // if this is the first mail to this person, we add new entery for this person
        allMatNamesArea.insertData(0, str, false);
    }

    public void SendEMails() {                                                 
        new Thread(new Runnable(){
            public void run() {
                int totalMails=0;
                for(int i=0 ;i<allMatNamesArea.htmlTables.size(); i++){
                    totalMails = ((HTMLTable)allMatNamesArea.htmlTables.get(i)).tableRows.size()-1;
                }
                if(totalMails>0){
                    jDialog1.pack();
                    jDialog1.setLocationRelativeTo(null);
                    jDialog1.setVisible(true);
                    int step = jProgressBar1.getMaximum()/totalMails;
                    int current = 1;
                    
                    for(int i=0 ;i<allMatNamesArea.htmlTables.size(); i++){
                        HTMLTable htmlTable = (HTMLTable)allMatNamesArea.htmlTables.get(i);
                        mailAPI.sendEmail("hamdi.altahery@yahoo.com", allMatNamesArea.getHTMLTable(0).getHTMLTableData());
                        for(int j=1; j<htmlTable.tableRows.size(); j++){
                            String row[] = htmlTable.getRow(j);
                            //mailAPI.sendEmail(row[0],row[1]);
                            jLabel1.setText("Sending Emails ( "+(current++)+" of "+totalMails+" )");
                            jLabel2.setText("Current: "+row[0]+", at "+row[1]);
                            if(mailAPI.sendEmail(row[0], row[1]/*"hamdi.altahery@yahoo.com"*/, row[2], row[3])){
                                HTMLCell htmlCell = htmlTable.getHTMLCell(j, 4);
                                htmlCell.setImage("icon-ok.png");
                                htmlCell.setCellClass("ok");
                                allMatNamesArea.reload();
                            }
                            //try { Thread.sleep(1000); } catch (InterruptedException ex) {ex.printStackTrace();}
                            int value = jProgressBar1.getValue() + step;
                            if (value > jProgressBar1.getMaximum()) {
                                value = jProgressBar1.getMaximum();
                            }
                            jProgressBar1.setValue(value);
                            
                        }
                    }                    
                    jDialog1.setVisible(false);
                    allMatNamesArea.resetData(false);
                    allMatNamesArea.reload();
                    currentMatNameArea.resetData(false);
                }
            }
        }).start();        
    }                        
    public void clearList(){
        allMatNamesArea.resetData(false);
        allMatNamesArea.reload();
        currentMatNameArea.resetData(false);  
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        jProgressBar1 = new javax.swing.JProgressBar();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        leftPanel = new javax.swing.JPanel();
        middlePanel = new javax.swing.JPanel();
        rightBorderPanel = new BorderPanel();
        rightJPanel1 = new javax.swing.JPanel();
        rightJPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        SELabel = new javax.swing.JLabel();
        ISLabel = new javax.swing.JLabel();
        CSLabel = new javax.swing.JLabel();
        CELabel = new javax.swing.JLabel();
        NRLabel = new javax.swing.JLabel();

        jDialog1.setBackground(new java.awt.Color(150, 200, 255));

        jLabel1.setText("Current:");

        jLabel2.setText("Sending Emails (4 of 10) ");

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                    .addGroup(jDialog1Layout.createSequentialGroup()
                        .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        setAutoscrolls(true);
        setMinimumSize(new java.awt.Dimension(576, 324));
        setName(""); // NOI18N
        setPreferredSize(new java.awt.Dimension(768, 432));

        jPanel4.setOpaque(false);

        leftPanel.setBackground(new java.awt.Color(255, 204, 204));
        leftPanel.setMinimumSize(new java.awt.Dimension(307, 172));
        leftPanel.setPreferredSize(new java.awt.Dimension(307, 172));
        leftPanel.setLayout(new java.awt.GridLayout(1, 0));

        middlePanel.setBackground(new java.awt.Color(255, 102, 51));
        middlePanel.setOpaque(false);
        middlePanel.setLayout(new javax.swing.BoxLayout(middlePanel, javax.swing.BoxLayout.LINE_AXIS));

        rightBorderPanel.setBackground(new java.awt.Color(51, 102, 255));
        rightBorderPanel.setMinimumSize(new java.awt.Dimension(307, 172));
        rightBorderPanel.setPreferredSize(new java.awt.Dimension(307, 172));

        rightJPanel1.setLayout(new java.awt.GridLayout(1, 0));

        rightJPanel2.setBackground(new java.awt.Color(255, 204, 51));
        rightJPanel2.setLayout(new java.awt.GridLayout(1, 0));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        SELabel.setBackground(new java.awt.Color(255, 153, 153));
        SELabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        SELabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        SELabel.setText("هندسة البرمجيات");
        SELabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        ISLabel.setBackground(new java.awt.Color(255, 153, 153));
        ISLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ISLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ISLabel.setText("نظم معلومات");
        ISLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        CSLabel.setBackground(new java.awt.Color(255, 153, 153));
        CSLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        CSLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CSLabel.setText("علوم حاسوب");
        CSLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        CELabel.setBackground(new java.awt.Color(255, 153, 153));
        CELabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        CELabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CELabel.setText("هندسة حاسب");
        CELabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        NRLabel.setBackground(new java.awt.Color(255, 153, 153));
        NRLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        NRLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NRLabel.setText("غير معلوم");
        NRLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(NRLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(CELabel, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                            .addComponent(SELabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(CSLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                            .addComponent(ISLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SELabel, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CSLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ISLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CELabel, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(NRLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout rightBorderPanelLayout = new javax.swing.GroupLayout(rightBorderPanel);
        rightBorderPanel.setLayout(rightBorderPanelLayout);
        rightBorderPanelLayout.setHorizontalGroup(
            rightBorderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightBorderPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rightBorderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(rightBorderPanelLayout.createSequentialGroup()
                        .addComponent(rightJPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(rightJPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        rightBorderPanelLayout.setVerticalGroup(
            rightBorderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightBorderPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rightBorderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(rightJPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rightJPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(leftPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(middlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(rightBorderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(rightBorderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
            .addComponent(leftPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(middlePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel CELabel;
    private javax.swing.JLabel CSLabel;
    private javax.swing.JLabel ISLabel;
    private javax.swing.JLabel NRLabel;
    private javax.swing.JLabel SELabel;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JPanel middlePanel;
    private javax.swing.JPanel rightBorderPanel;
    private javax.swing.JPanel rightJPanel1;
    private javax.swing.JPanel rightJPanel2;
    // End of variables declaration//GEN-END:variables


    @Override
    public void  paintComponent(java.awt.Graphics g){
        try{
            super.paintComponent(g);
        }catch(java.lang.NullPointerException e){
            System.out.println("Error = "+e.getMessage());
            //e.printStackTrace();
            System.out.println("Graphics = "+g);
            System.out.println("super = "+super.toString());
            //System.exit(0);
        }
        
 	g.drawImage(bgImageI.getImage(), 0, 0, getWidth(), getHeight(), 0,0, bgImageI.getIconWidth(), bgImageI.getIconHeight(), null);
	g.setColor(new java.awt.Color(255, 255, 255, 100));
	g.fillRoundRect(5, 5, getWidth()-10, getHeight()-10, 12, 12);
    }

    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {            
            int raw = Integer.parseInt(e.getDescription());
            if(!( textRecognition.fuzzyNameMatching.similarNameExt.similarityList.isEmpty() || raw > (textRecognition.fuzzyNameMatching.similarNameExt.similarityList.size()-1)) ){
                MatchedName mn = textRecognition.fuzzyNameMatching.similarNameExt.similarityList.get(raw);
                String matchedRecord[] = textRecognition.fuzzyNameMatching.similarNameExt.getRecord(mn.getIndex());            


                
                        currentMatNameArea.resetData(true);    
                        String str[] = {matchedRecord[0], String.valueOf((int)mn.getBestScore())+" %"/*<br>"+mn.getScore(mn.FULL_MATCH)*/};                    



                    String depa = matchedRecord[4];
                    if(depa.equals("Administration".trim())){
                        depa="ادارة الكلية";
                         if(speakTheDepartments) SoundPlayer.playSound("sounds/nr2.wav");  
                        NRLabel.setOpaque(true);
                    }
                    else if(depa.equals("COMPUTER ENGINEERING DEPARTMENT".trim())){
                        depa="قسم هندسة الحاسب";
                         if(speakTheDepartments) SoundPlayer.playSound("sounds/ce2.wav");         
                        CELabel.setOpaque(true);
                    }
                    else if(depa.equals("COMPUTER SCIENCE DEPARTMENT".trim())){
                        depa="قسم علوم الحاسب";
                         if(speakTheDepartments) SoundPlayer.playSound("sounds/cs2.wav");     
                        CSLabel.setOpaque(true);
                    }
                    else if(depa.equals("INFORMATION SYSTEMS DEPARTMENT".trim())){
                        depa="قسم نظم معلومات";
                         if(speakTheDepartments) SoundPlayer.playSound("sounds/is2.wav");      
                        ISLabel.setOpaque(true);
                    }
                    else if(depa.equals("IT Unit".trim())){
                        depa="وحدة تقنية المعلومات";
                         if(speakTheDepartments) SoundPlayer.playSound("sounds/nr2.wav");                        
                        NRLabel.setOpaque(true);
                    }
                    else if(depa.equals("SOFTWARE ENGINEERING DEPARTMENT".trim())){
                        depa="قسم هندسة البرمجيات";
                         if(speakTheDepartments) SoundPlayer.playSound("sounds/se2.wav");                        
                        SELabel.setOpaque(true);
                    }
                    else {
                        depa="القسم غير معلوم";
                         if(speakTheDepartments) SoundPlayer.playSound("sounds/nr2.wav");                        
                        this.CELabel.setOpaque(true);
                    }                                        
                    
                        String info = "";
                        for (int i = 0; i < OCREngine.existInfo.length; i++) {
                            if(OCREngine.existInfo[i]>0){
                                info += " "+OCREngine.INFO[i]; 
                            }                            
                        }        
                        OCREngine.resetAllInfo();
                        
                        currentMatNameArea.insertData(0,str, true);   
                        currentMatNameArea.insertData(0, depa, true); 
                        //currentMatNameArea.insertData(0, "<span class=info>"+info, false);                             
                    
                   
                        //currentMatNameArea.getHTMLTable(0).getHTMLCell(0, 1).setRowSpan(3);
                        //currentMatNameArea.reload();                
                                
                
                textRecognition.textDetection.setRunningState(false);
                updateMatNamesList(matchedRecord);   
                textRecognition.fuzzyNameMatching.similarNameExt.isSListUpdated = false;  
                textRecognition.fuzzyNameMatching.similarNameExt.similarityList.clear();

//                try {
//                    FileUtils.write(outputFile, matchedRecord[0]+"\n", true);
//                } catch (IOException ex) {
//                    System.err.println("IO Error write(outputFile: "+ex.getMessage());
//                    Logger.getLogger(StartPanel.class.getName()).log(Level.SEVERE, null, ex);
//                }

            }
        }
    }
}
    
