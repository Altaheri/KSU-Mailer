 /*/
  *      	 Republic of Yemen
  *				  2011
  *		E / Hamdi Taher Altahery
  *
  * Project of -( Signature Recognition )-
 /*/

package com.ksu.mailnoti.ui.util.html;

import java.awt.Color;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;


public class StyledTextArea extends javax.swing.JPanel{
    public javax.swing.JScrollPane jSP = new javax.swing.JScrollPane();
    public JEditorPane dataEditorPane = new JEditorPane();
    private javax.swing.text.html.HTMLDocument htmlDoc = new javax.swing.text.html.HTMLDocument();  
    
    public ArrayList <HTMLTable> htmlTables;
    
/*******************************************************************************/
/*------------------------------>( Constructor )<------------------------------*/   
    /**
     *
     * @param numOfTables
     */
    public StyledTextArea(int numOfTables, HyperlinkListener hyperlinkListener){
        htmlTables = new ArrayList(numOfTables);
        
	setLayout(new java.awt.GridLayout(1,0));
        setBackground(new Color(255, 255, 255));
        //dataEditorPane.setBackground(new Color(255, 255, 255));
        dataEditorPane.setBackground(new Color(150, 200, 255));

        //setOpaque(false);
	//dataEditorPane.setPreferredSize(new java.awt.Dimension(1000, 200));

	URL url=null;
        try {
            url = new File("icons/").toURI().toURL();
        } catch (MalformedURLException ex) {
            Logger.getLogger(StyledTextArea.class.getName()).log(Level.SEVERE, null, ex);
        }
        htmlDoc.setBase(url);
        dataEditorPane.setEditorKit(JEditorPane.createEditorKitForContentType("text/html"));
	dataEditorPane.setEditable(false);
	//dataEditorPane.setOpaque(false);
	//dataEditorPane.setContentType("text/html");
	dataEditorPane.setBorder(
            new javax.swing.border.CompoundBorder(new javax.swing.border.MatteBorder(
            new java.awt.Insets(0, 0, 0, 5), new java.awt.Color(255,255,255,0)), null));    
        
        
        
        StyleSheet ss = new StyleSheet();
        try {
            url = new File("icons/default.css").toURI().toURL();
        } catch (MalformedURLException ex) {
            Logger.getLogger(StyledTextArea.class.getName()).log(Level.SEVERE, null, ex);
        }
        ss.importStyleSheet(url);
        HTMLEditorKit kit = (HTMLEditorKit)dataEditorPane.getEditorKit();
        kit.setStyleSheet(ss);
        
        dataEditorPane.addHyperlinkListener(hyperlinkListener);

        jSP.setViewportView(dataEditorPane);
	jSP.setBorder(null);
	jSP.getViewport().setOpaque(false);
	jSP.setOpaque(false);
	add(jSP);        

    }
/*--------------------------->( End Constructor )<-----------------------------*/
/*******************************************************************************/

    
    /**
     *
     * @param tableIndex
     * @param text
     * @param isHeader
     */
    public void insertData(int tableIndex, String text, boolean isHeader){
        String str[] = {text};
        insertData(tableIndex, str, isHeader);
    }
    
    public void insertData(int tableIndex, String text[], boolean isHeader){
        HTMLCell htmlCells[] = new HTMLCell[text.length];
        for(int i=0; i<htmlCells.length; i++){                    
            htmlCells[i] = new HTMLCell(text[i], isHeader); 
        }
        insertData(tableIndex, htmlCells);
    }
        
    public void insertData(int tableIndex, HTMLCell cell[]){              
        HTMLTable htmlTable =  htmlTables.get(tableIndex); 
        htmlTable.addHTMLTableRow(cell);
        reload();
    }
    
    public void resetData(boolean removeHeader){
        dataEditorPane.setText("");
        for(int i=0; i<htmlTables.size(); i++){
            HTMLTable htmlTable = htmlTables.get(i); 
            htmlTable.resetData(removeHeader);
        }
    }
    
    /**
     *
     */
    public void reload(){
        String htmldoc = "<!DOCTYPE html><html><head><base href='"+htmlDoc.getBase().toString()+"'>"+
        "<title></title></head><body>";                
        for(int i=0; i<htmlTables.size(); i++){
            HTMLTable htmlTable =  htmlTables.get(i); 
            htmlTable.reload();
            htmldoc += htmlTable.getHTMLTableData();                                  
        }        
        dataEditorPane.setText(htmldoc+"</body></html>");     
    }

        
    public void addHTMLTable(HTMLTable htmlTable){
        htmlTables.add(htmlTable);
    }
    public HTMLTable getHTMLTable(int tableIndex){
        return (HTMLTable) htmlTables.get(tableIndex); 
    }
    
    public void createHTMLTable(int numberOfColumn, String tableClass, String caption){
        HTMLTable htmlTable = new HTMLTable(numberOfColumn, tableClass, caption);
        htmlTables.add(htmlTable);
    }  
    
    private void initTables(){     
        htmlTables.add( new HTMLTable(3, 1, "style1", "style1","COMPUTER ENGINEERING DEPARTMENT",false));
        htmlTables.add( new HTMLTable(3, 1, "style1", "style1","COMPUTER SCIENCE DEPARTMENT",false));
        htmlTables.add( new HTMLTable(3, 1, "style1", "style1","INFORMATION SYSTEMS DEPARTMENT",false));
        htmlTables.add( new HTMLTable(3, 1, "style1", "style1","SOFTWARE ENGINEERING DEPARTMENT",false));       
    }     
    
    public void test(){
        initTables();        
        String[] header = {"Name", "Email", "No. of Messages"};
    
        String[] strs1 = {"Hamdi Taher Altahery", "hamdi.altaheri@yahoo.com", "10"};
        String[] strs2 = {"kshop on Digital manufacturin", "hamdi.altaheri@yahoo.com", "45"};
        String[] strs3 = {"dataEditorPane.setText", "hamdi.altaheri@yahoo.com", "2"};
        String[] strs4 = {"Nimbus Look and Feel", "hamdi.altaheri@yahoo.com", "3"};
        
        insertData(0, header, true); 
        insertData(1, header, true);             
        insertData(2, header, true);             
        insertData(3, header, true);             
        
	for(int i=0; i<3;i++) insertData(0, strs1, false);    
	for(int i=0; i<5;i++) insertData(1, strs2, false);   
	for(int i=0; i<7;i++) insertData(2, strs3, false);                  
	for(int i=0; i<10;i++) insertData(3, strs4, false);                   
        System.out.println(dataEditorPane.getText());
    }
    
    
    public static void main(String[] args) {
        StyledTextArea s = new StyledTextArea(1, null);
        s.test();
    }
}
