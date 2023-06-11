/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksu.mailnoti.ui.util.html;

import java.util.ArrayList;

/**
 *
 * @author Hamdi
 */
public class HTMLTable {
    private String tableClass = null;    
    private String colgroupSpanClass = null;    
    private int numberOfColumn = 1;
    private int colgroupSpan = numberOfColumn;
    private String caption="";
    private boolean captionVisible = false;
    public ArrayList tableRows = new ArrayList(5);
    private String htmlTable = "";
    
    public HTMLTable(int numberOfColumn, String tableClass, String caption){
        this( numberOfColumn, numberOfColumn, tableClass, null, caption, false) ;
    }    
    public HTMLTable(int numberOfColumn, int colgroupSpan, String tableClass, String colgroupSpanClass, String caption){
        this( numberOfColumn, colgroupSpan, tableClass, colgroupSpanClass, caption, false) ;
    }
    public HTMLTable(int numberOfColumn, int colgroupSpan, String tableClass, String colgroupSpanClass, String caption, boolean captionVisible){
        this.numberOfColumn = numberOfColumn;
        this.colgroupSpan = colgroupSpan;
        this.tableClass = tableClass;
        this.colgroupSpanClass = colgroupSpanClass;
        this.caption = caption;
        this.numberOfColumn = numberOfColumn;
        this.captionVisible = captionVisible;
    }
    
    public void addHTMLTableRow(HTMLCell rowCells[]){
        addHTMLTableRow(new HTMLTableRow(rowCells));
    }    
    public void addHTMLTableRow(String rowTexts[]){
        addHTMLTableRow(new HTMLTableRow(rowTexts));
    }   
    public void addHTMLTableRow(HTMLTableRow htmlTableRow){
        if(tableRows.isEmpty()){
            tableRows.add(htmlTableRow);
            updateHTMLTable();            
        }else{
            tableRows.add(htmlTableRow);
            insertHTMLTableRow(htmlTableRow);
        }
    }    
    
    public final void reload(){
        for(int i=0; i<tableRows.size(); i++){
            HTMLTableRow row = (HTMLTableRow)tableRows.get(i);
            row.reload();
        }
        updateHTMLTable();
    }
    
    private void updateHTMLTable(){
        if(tableClass!=null) htmlTable="<table class="+tableClass+">";
        else htmlTable="<table>";
        
        if(!caption.equals("")&& captionVisible ){
            htmlTable+="<caption>"+caption+"</caption>";
        }        
        
        if(colgroupSpan>=1&&colgroupSpan<numberOfColumn)
            if(colgroupSpanClass !=null) htmlTable+="<colgroup span="+colgroupSpan+" class="+colgroupSpanClass+"></colgroup>";
        
        for(int i=0; i<tableRows.size(); i++){
            HTMLTableRow row = (HTMLTableRow)tableRows.get(i);
            htmlTable+=row.getHTMLRowData();  
        }
        
        htmlTable+="</table>";
    }     
    
    private void insertHTMLTableRow(HTMLTableRow htmlTableRow){                                        
        String preText=htmlTable.substring(0,htmlTable.indexOf("</table>"));
        preText+= htmlTableRow.getHTMLRowData();                
	htmlTable = preText+"</table>";                
    }    
              
    public String getHTMLTableData(){
        return htmlTable;
    }
    
    public HTMLTableRow getHTMLTableRow(int index){
        return (HTMLTableRow)tableRows.get(index);        
    } 
    
    public String [] getRow(int index){
        return getHTMLTableRow(index).getRowsData();                
    }
     
    public String getCell(int row, int column){
        return getRow(row)[column];           
    } 

    public HTMLCell getHTMLCell(int row, int column){
        HTMLTableRow htmlrow = getHTMLTableRow(row);
        return htmlrow.getHTMLCell(column);
    } 
       
    public void resetData(boolean removeHeader){
        if(tableRows != null && tableRows.size()!=0 ){
            HTMLTableRow htmlrow = getHTMLTableRow(0); //header
            tableRows.clear();
            if(!removeHeader){
                if(htmlrow.getHTMLCell(0).isHeaderCell){
                    tableRows.add(htmlrow);               
                }
            }
        }
    }
/*******************************************************************************/
}
    

