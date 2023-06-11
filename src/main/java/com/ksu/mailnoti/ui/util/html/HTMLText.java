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
public class HTMLText {
    private String text = "";    
    private String textClass = null;
    private String htmlText="";
    private String image = null;//"hamdi.png";    
    private String hyperlink = null;  
    
    public HTMLText(String text){
        this(text, null);
    }
    public HTMLText(String text, String textClass){       
        //text = "href{REF}click"; 
        if(text.startsWith("href{")){            
            hyperlink = text.substring(5,text.indexOf('}'));//REF
            text = text.substring(text.indexOf('}')+1);//click            
        }
        
        if(text.endsWith(".png")||text.endsWith(".jpg")){
            image=text;
        }else this.text = text;
        
        this.textClass = textClass;
    }      
    
/*******************************************************************************/
    public String getHTMLText(){
        return htmlText;
    }
    public String getText(){
        return text;
    }   
    public void setImage(String imag){
        this.image = imag;
    }  
    public void setText(String text){
        this.text = text;
    }     
    public void setHyperlink(String hyperlink){
        this.hyperlink = hyperlink;
    }      
    public String updateHTMLText(){ 
        htmlText="";
        
        if(hyperlink != null) htmlText += "<a href='"+hyperlink+"'>";
        
        if(text != null){
            if(textClass!=null ){
                htmlText = "<span class="+textClass+">"+text+"</span>";
            }else{
                htmlText += text;
            }
        }
        

        if(image != null) htmlText += "<img src='"+image+"' BORDER='0'>";
        
        if(hyperlink != null) htmlText += "</a>";    
        
        return htmlText;
    }
/*******************************************************************************/
    
}
