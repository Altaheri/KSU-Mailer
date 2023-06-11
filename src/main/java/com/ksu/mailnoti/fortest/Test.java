/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksu.mailnoti.fortest;

import com.ksu.mailnoti.core.text.InputNameImprover;
import java.awt.Toolkit;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;

/**
 *
 * @author Hamdi
 */

public class Test {
    public static void getDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        System.out.println("the date is "+ dateFormat.format(date)); //2014/08/06 15:59:48        
    }

    
    public Test(){        
        getDate();   
        
        String EXAMPLE_TEST = "PROF Hamdi    T. Asl mr   tahery     mr    ";

        //replace 2 or more spaces with single space
        // +	Occurs one or more times, is short for {1,} 
        // X+ - Finds one or several letter X
        String pattern = " +";
        EXAMPLE_TEST = EXAMPLE_TEST.trim().replaceAll(pattern, " ");
        System.out.println(EXAMPLE_TEST); 
        
        //Matches the word "mr" followed by [whitespace or .] that must match at the beginning of the line. then replace it by "".
        // (?i) ignore letter case
        // ^regex	Finds regex that must match at the beginning of the line.
        // X|Z	Finds X or Z.
        pattern = "(?i)^mr[ |\\.]";
        System.out.println(EXAMPLE_TEST.replaceAll(pattern, "").trim());         

        // [abc]	Set definition, can match the letter a or b or c.
        // [abc][vz]	Set definition, can match a or b or c followed by either v or z.
        pattern = "(?i)H[ame]";
        System.out.println(EXAMPLE_TEST.replaceAll(pattern, "*").trim());        
    }

    
    public static void main(String[] args) { 
        getDate();
        Soundex  soundex = new  Soundex();
        try {
            System.out.println("difference = "+soundex.difference("Hamdi", "Hamdi"));
            System.out.println("difference = "+soundex.difference("semty", "Hamdi"));
        } catch (EncoderException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }        
                       
        
        String name = "href{REF}click";
        if(name.startsWith("href{")){
            String href = name.substring(5,name.indexOf('}'));
            name = name.substring(name.indexOf('}')+1);
            System.out.println(href+""+name);
        }
        
        
        
        //String name = "PROF HATIM ABOALSAMH Hamdi  /* 56  T. Al tahery   63 6ud  ---*......    ";
        name = " PROF HATIM ABOALSAMH Hamdi";
        
        System.out.println(InputNameImprover.improveName(name));
        //new Test();
               
        System.out.println( "Hamdi".indexOf(" "));
        System.out.println( "Hamdi ".indexOf(" "));
        System.out.println( " Hamdi".indexOf(" "));   
        
    //    HamdiTaherAltaheri => Hamdi Taher Altaheri
    //    TaherAltaheri => Taher Altaheri
    //    HamdiAltaheri => Hamdi Altaheri
    //    HamdiTaher => Hamdi Taher      
        String str = "HamdiTaherAltaheri";
        String s[]={"","",""};
        
        //s[0] = str;
        if(str.startsWith("Hamdi")){
            s[0] = str.substring(0, "Hamdi".length());
            str = s[1] = str.substring("Hamdi".length());
        }
        
        if(str.endsWith("Altaheri")){
            s[1] = str.substring(0, str.length()-"Altaheri".length());
            s[2] = str.substring(str.length()-"Altaheri".length());
        } 
        
        str ="";
        for (String s1 : s) {
            if(!s1.isEmpty())str += s1+" ";
        }
        System.out.println(str.trim());                
//        System.out.println( "HamdiAltaheri".substring(0, "Hamdi".length()));
//        System.out.println( "HamdiAltaheri".substring("Hamdi".length()));
        
        Toolkit.getDefaultToolkit().beep();     
        //System.out.println(" Al htaheri ".replaceAll(" ", ""));

        
// : remove the last occarance of " " (concat the last 2 names)
        String stri =" Al htah er i ";
        stri=stri.trim();
        StringBuilder b = new StringBuilder(stri);
        b.replace(stri.lastIndexOf(" "), stri.lastIndexOf(" ") + 1, "" );
        stri = b.toString();
        System.out.println(stri);

// : remove the first occarance of " " (concat the first 2 names)
        System.out.println("Al htah eri ".replaceFirst(" ", ""));

        
        
        String str0 ="aihichri";    
        String str1 ="alhtchrl";
        
        if( str0.length() == str1.length() ){
            for (int i = 0; i < str0.length(); i++) {
                if( (str0.charAt(i) == 'i' ) && ( str1.charAt(i) == 'l' ) ){
                    str1 = str1.substring(0, i) + "i" + str1.substring(i+1);
                }else if( (str1.charAt(i) == 'i' ) && ( str0.charAt(i) == 'l' ) ){
                    str0 = str0.substring(0, i) + "i" + str0.substring(i+1);
                }
            }
        }
            
            System.out.println(str0);
            System.out.println(str1);

    }
    
}
