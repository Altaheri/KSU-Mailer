/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksu.mailnoti.core.text;

import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Hamdi
 */
public class InputNameImprover {

    private final static List<String>garbgeList = Arrays.asList(
                "'", ",", "-", "_", "·", "/", ";","\"","‘","“","\\|","\\?","—","’","”",
                "!", "&", "«", "»", "<", ">", "~",  ":", "=", "%", "@",
                "\\+","\\*", "\\^", "\\$", "\\(", "\\)", "\\{", "\\}", "\\[", "\\]", "\\\\"             
                );
    private final static String garbge = StringUtils.join(garbgeList, "|");    
    
    private final static List<String>prefixesList = Arrays.asList(          
                "phd", "mr", "dr", "prof","mrs","miss","ms","mis" );
    // ^regex	Finds regex that must match at the beginning of the line.
    // X|Z	Finds X or Z.
    private final static String prefixes = "^("+StringUtils.join(prefixesList, "[ ]|")+"[ ])";    
    
    
    // 0 = O or D
    // 1 = L
    // 8 = B
    // 2 = Z
    // 7 = Z
    // 4 = A
    private final static List<String>missRecogLetterAsNumList1 = Arrays.asList(          
                "0", "1", "8", "2", "7", "4" );
    private final static List<String>missRecogLetterAsNumList2 = Arrays.asList(          
                "O", "L", "B", "Z", "Z", "A" );  
    
    public static String improveName(String name){
        
        try{
            if(name == null) return null;
            // Matches one or more spaces and replace them by single space.
            // +	Occurs one or more times, is short for {1,}
            // X+ - Finds one or several letter X
            String pattern = " +";
            name = name.replaceAll(pattern, " ");
            
            // Matches and remove "."
            pattern = "\\.";
            name = name.replaceAll(pattern, "");
            
            
            // Matches all miss recognized letters as numbers
            for(int i =0; i<missRecogLetterAsNumList1.size(); i++){
                name = name.replaceAll(missRecogLetterAsNumList1.get(i), missRecogLetterAsNumList2.get(i));
            }
            
            // replace the character "ﬁ" by two character "fi"
            pattern = "ﬁ";
            name = name.replaceAll(pattern, "fi").trim();
            
            // Matches all other digits and remove it
            // \d  Any digit, short for [0-9]
            pattern = "\\d";
            name = name.replaceAll(pattern, "").trim();
            
            // Matches one of the the words "phd", "ph.d", "mr", "dr", or "prof"
            // followed by [whitespace or .],and that must match at the beginning of the line. then remove it.
            name = name.toLowerCase().replaceAll(prefixes+"|"+garbge, "").trim();
            
            
            // Again matches one or more spaces and replace them by single space.
            pattern = " +";
            name = name.replaceAll(pattern, " ");
            
            // remove the first name if it is less than or equal 2"
            if(name!=null && !name.equals("")){
                //System.out.println(name);
                if(name.indexOf(" ")>0){
                    String sub = name.substring(0, name.indexOf(" "));
                    if(sub.length()<=2)
                        name = name.replaceFirst(sub, "").trim();
                }
            }
        }catch(Exception e){
            System.err.println("Error in InputNameImprover:"+e.getMessage());
            return null;
        }
        
        return name;
    }
}
