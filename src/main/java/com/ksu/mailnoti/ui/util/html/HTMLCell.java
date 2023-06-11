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
public class HTMLCell extends HTMLText{
    private String cellClass = null;
    public boolean isHeaderCell = false;
    private int colSpan=1;    
    private int rowSpan=1;    
    private String htmlCell;
    
    public HTMLCell(String text){
        this(text, false);    
    } 
    public HTMLCell(String text, boolean isHeader){
        this(text, isHeader, null);    
    } 
    public HTMLCell(String text, String cellClass){
        this(text, false, cellClass);    
    }  
    public HTMLCell(String text, boolean isHeader, String cellClass){
        this(text, isHeader, cellClass, 1, 1);    
    }  
    public HTMLCell(String text, boolean isHeader, int colSpan, int rowSpan){
        this(text , isHeader, null, colSpan, colSpan);    
    }       
    public HTMLCell(String text, boolean isHeader, String cellClass, int colSpan, int rowSpan){
        super(text);
        this.isHeaderCell = isHeader;        
        this.cellClass = cellClass;
        this.colSpan = colSpan;
        this.rowSpan = rowSpan;        
        updateHTMLCell();
    }       
    
/*******************************************************************************/
    public String getHTMLCell(){
        //System.out.println(htmlCell);
        return htmlCell;
    }
    
    public void setCellClass(String cellClass){
        this.cellClass = cellClass;
        updateHTMLCell();
    }   
    
    public String getCellClass(){
        return cellClass;
    }  
    
    public void setColSpan(int colSpan){
        this.colSpan = colSpan;
    }      
    
    public void setRowSpan(int rowSpan){
        this.rowSpan = rowSpan;
    }   
        
    public final void updateHTMLCell(){      
        String htmlText = updateHTMLText();
        
        if(isHeaderCell) htmlCell = "<th ";   //"<th" TABLE_HEADER_CELL  
        else  htmlCell = "<td ";   //"<td" TABLE_DATA_CELL  

        if(cellClass!=null ) htmlCell += " class="+cellClass;
        if(colSpan>1) htmlCell += " colSpan="+colSpan;
        if(rowSpan>1) htmlCell += " rowSpan="+rowSpan;        
        htmlCell += ">" + htmlText;        

        if(isHeaderCell) htmlCell += "</th>";   // TABLE_HEADER_CELL  
        else  htmlCell += "</td>";   // TABLE_DATA_CELL  
    }
    
}
