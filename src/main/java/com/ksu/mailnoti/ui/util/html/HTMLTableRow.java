/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksu.mailnoti.ui.util.html;

/**
 *
 * @author Hamdi
 */
public class HTMLTableRow {
    private String tableRowClass = null;
    private HTMLCell rowCells[] ;
    private String htmlTableRow;
     
    public HTMLTableRow( String rowTexts[]){
        this(rowTexts, null);
    } 
    public HTMLTableRow(String rowTexts[], String tableRowClass){
        this.tableRowClass = tableRowClass;
        updateHTMLTableRow(rowTexts);
        updateHTMLTableRow();
    } 
    public HTMLTableRow(HTMLCell rowCells[]){
        this(rowCells, null);
    }      
    public HTMLTableRow(HTMLCell rowCells[], String tableRowClass){
        this.rowCells = rowCells;        
        this.tableRowClass = tableRowClass;
        updateHTMLTableRow();
    }   
    
    private void updateHTMLTableRow(String rowTexts[]){
        rowCells = new HTMLCell [rowTexts.length];
        for(int i=0; i<rowTexts.length; i++){
            rowCells[i] = new HTMLCell(rowTexts[i]);   
        }        
    } 
    
    private final void updateHTMLTableRow(){
        if(tableRowClass!=null) htmlTableRow="<tr class="+tableRowClass+">";
        else htmlTableRow="<tr>";
        
        for(int i=0; i<rowCells.length; i++){
            if(i==0 && rowCells[i].getCellClass() == null)
                rowCells[i].setCellClass("firstcell");     
            else if(i==rowCells.length-1 && rowCells[i].getCellClass() == null)
                rowCells[i].setCellClass("lastcell");     
            htmlTableRow += rowCells[i].getHTMLCell();
        }
        htmlTableRow+="</tr>";
    }  
    
    public final void reload(){
        for(int i=0; i<rowCells.length; i++){
            rowCells[i].updateHTMLCell();     
        }        
        updateHTMLTableRow();
    }    
    
    public String getHTMLRowData(){
        return htmlTableRow;
    }
    public String [] getRowsData(){
        String rows [] = new String[rowCells.length];
        for(int i=0; i<rowCells.length; i++){
            rows[i] = rowCells[i].getText();
        }
        return rows;
    }
    public HTMLCell getHTMLCell(int index){
        return rowCells[index];
    }    
}
