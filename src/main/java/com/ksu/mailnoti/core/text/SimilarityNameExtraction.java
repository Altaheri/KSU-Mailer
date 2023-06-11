/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksu.mailnoti.core.text;

import com.ksu.mailnoti.core.db.ExcelReader;
import java.util.ArrayList;
import org.apache.commons.text.names.Name;

/**
 *
 * @author Hamdi
 */
public class SimilarityNameExtraction {
    public final DamerauLevenshtein dL;
    
    private ExcelReader excelReader ;    
    private final int similarityListSize = 10;
    public final ArrayList <MatchedName> similarityList = new <MatchedName>ArrayList(similarityListSize+1);
    
    public boolean isSListUpdated = true;
    
    private final double similarityListThreshold = 50;
    private Name currentName ;

    public double wellMatchedThreshold = 92; 
//    double badMatchedThreshold = 80; 
//    private final double MIN_MATCH_SCORE = 90; 
//    private final double MIN_MATCH_SCORE_PART_NAME = 60; 
    
    public SimilarityNameExtraction(){
        excelReader = new ExcelReader();
        dL = new DamerauLevenshtein();
    }
       
    public String [] getRecord(int index){       
        return excelReader.getRecord(index); 
    } 
    
    public String getName(int index){       
        return excelReader.getName(index); 
    }   
    
    public String [] getBestMatchedRecord(){       
        MatchedName mn = getBestMatchedName();
        if(mn == null) 
            return null;
        return getRecord(mn.index); 
    }

    int howmuch =0;
    public boolean computeNameSimilarity(String name)  {
        howmuch =0;
        //similarityList.clear();
        currentName = excelReader.nameParser.parse(name);
        isSListUpdated = testNameSimilarity();
        
        MatchedName mn = getMatchedName(0);
        if(mn != null) {        
            if( ! isWellMatchedName(mn) ){               
                isSListUpdated = recomputeMatchedNameWithEnhancement()||isSListUpdated ;                                      
            }
        }
        
//        if(tempList.size() != similarityList.size())
//            this.isSListUpdated = true;
//        else{
//            for (int i = 0; i < tempList.size(); i++) {
//                if(tempList.get(i).getIndex() != similarityList.get(i).getIndex() ||
//                   tempList.get(i).getBestScore() != similarityList.get(i).getBestScore())
//                    this.isSListUpdated = true;
//            }            
//        }
        //System.out.println("howmuch = " + howmuch);
        return isSListUpdated;
    }  
    
    
    public MatchedName getBestMatchedName(){        
        MatchedName mn = getMatchedName(0);      
        if( mn != null && isWellMatchedName(mn) )
            return mn;
        else 
            return null; 
    }
    
    public MatchedName getMatchedName(int index){   
        if( similarityList.isEmpty() || index > (similarityList.size()-1) ) 
            return null;
        return (MatchedName)similarityList.get(index);
    }    

    public int[] getMatchedNamesIndexes(){  
        int indexes[]= new int[similarityList.size()];
        for (int i = 0; i < similarityList.size(); i++) {
            indexes[i] = similarityList.get(i).index;            
        }
        return indexes;
    }   
    
    private boolean isWellMatchedName(MatchedName mname){
        boolean wellMatchedName = false;

        double bestScore = mname.getBestScore(); 
        int bestScoreType = mname.getBestScoreType();
        
//        if( 
//            //Case 1: FULL_MATCH or FF_LL_PART_MATCH with (Score > wellMatchedThreshold)
//             ((bestScore >= wellMatchedThreshold) && 
//                ( (bestScoreType == mname.FULL_MATCH) || (bestScoreType == mname.FF_LL_PART_MATCH) )) 
//            ||  
//            //Case 2: FL_LF_PART_MATCH with Score = 100                
//             ( (bestScore >= 100) && (bestScoreType == mname.FL_LF_PART_MATCH)) //90
//            ||  
//            //Case 3: FF_MandL_M or FF_M_MandL with Score > 95                               
//            //Ex. name1 = Mohammed Ali Ezzat Mokhtar
//            //    name2 = Mohammed Ali       Ezzat
//             ((bestScoreType==mname.FF_MandL_M || bestScoreType==mname.FF_M_MandL) && (bestScore >= 95))  //90              
//            || 
//            //Case 4.1: FF_LM_PART_MATCH with Score = 100 
//            // AND ------->  there is no midName in the name1  <-------           
//            // Ex. name1 = Salman Ali, name1 = Salman Ali Alqahtani  .... Ok
//            // Ex. name1 = Salman Ahmed Ali, name1 = Salman Ali Alqahtani  .... NOT OK   
//            // We Know That (name1 is always the name we get it from the reader ) SO...
//            // currentName must not has midname  ^_^
//              (bestScoreType == mname.FF_LM_PART_MATCH  && bestScore >= 100 && 
//                currentName.getMiddleName().equals("") && currentName.getLastName().length()>=3)//90
//            ){
//            wellMatchedName = true; 
//    }
            
// all the same value of threashold 
        if( 
            //Case 1: FULL_MATCH or FF_LL_PART_MATCH with (Score > wellMatchedThreshold)
             ((bestScore >= wellMatchedThreshold) && //90
                ( (bestScoreType == mname.FULL_MATCH) || (bestScoreType == mname.FF_LL_PART_MATCH) )) 
            ||  
            //Case 2: FL_LF_PART_MATCH with Score = 100                
             ( (bestScore >= wellMatchedThreshold) && (bestScoreType == mname.FL_LF_PART_MATCH)) //100
            ||  
            //Case 3: FF_MandL_M or FF_M_MandL with Score > 95                               
            //Ex. name1 = Mohammed Ali Ezzat Mokhtar
            //    name2 = Mohammed Ali       Ezzat
             ((bestScoreType==mname.FF_MandL_M || bestScoreType==mname.FF_M_MandL) && (bestScore >= wellMatchedThreshold))  //95              
            || 
            //Case 4.1: FF_LM_PART_MATCH with Score = 100 
            // AND ------->  there is no midName in the name1  <-------           
            // Ex. name1 = Salman Ali, name1 = Salman Ali Alqahtani  .... Ok
            // Ex. name1 = Salman Ahmed Ali, name1 = Salman Ali Alqahtani  .... NOT OK   
            // We Know That (name1 is always the name we get it from the reader ) SO...
            // currentName must not has midname  ^_^
              (bestScoreType == mname.FF_LM_PART_MATCH  && bestScore >= wellMatchedThreshold && 
                currentName.getMiddleName().equals("") && currentName.getLastName().length()>=3)//100
            ){
            wellMatchedName = true;             
            
            
        }        
        return wellMatchedName;                           
    }

    /**
    * make some enhancement to the currentName and the names in the similarity list.
    * and recompute the percentage of match
    * 
    * 1. Separate Conjoined Name of currentName 
    *    with refrence to all names in similarity list   
    *          Ex.: HamdiTaherAltaheri => Hamdi Taher Altaheri
    *    and then updateSimilarityList if there is better result
    * 
    * 2. Connect seperated Name in currentName 
    *    with refrence to all names in similarity list 
    *         Ex.: HamdiTaher Alta heri => Hamdi Taher Altaheri
    *    and then updateSimilarityList if there is better result
    */ 
    
    private boolean recomputeMatchedNameWithEnhancement (){
        boolean listUpdated = false;

        for (int i=0; i<similarityList.size(); i++){
            MatchedName mn = getMatchedName(i);
            Name refrence = excelReader.names[mn.getIndex()];
            String target = currentName.getFullName();
            
// 1. : HamdiTaherAltaheri => Hamdi Taher Altaheri
            Name enhancedName = separateConjoinedName(target, refrence);            
            if(enhancedName!=null){
                MatchedName matchedName = new MatchedName();
                matchedName.index = mn.getIndex();
                doCompleteCheck(matchedName, enhancedName, refrence);// name1 is always the name we get it from the reader
                
                if(matchedName.bestScore>getMatchedName(0).getBestScore()){
                    if(updateSimilarityList( matchedName, true)){
                        currentName = enhancedName;
                        listUpdated = true;
                    }                     
                }                                      
            }
             
            
// 2. : remove the first occarance of " " (concat the first 2 names)      
            target = currentName.getFullName().replaceFirst(" ", "");
            enhancedName = separateConjoinedName(target, refrence);            
            if(enhancedName!=null){
                MatchedName matchedName = new MatchedName();
                matchedName.index = mn.getIndex();
                doCompleteCheck(matchedName, enhancedName, refrence);// name1 is always the name we get it from the reader
                
                if(matchedName.bestScore>getMatchedName(0).getBestScore()){
                    if(updateSimilarityList( matchedName, true)){
                        currentName = enhancedName;
                        listUpdated = true;
                    }                     
                }                                      
            }

// 3. : remove the last occarance of " " (concat the last 2 names)           
            target = currentName.getFullName().trim();                        
            StringBuilder b = new StringBuilder(target);
            int index = target.lastIndexOf(" ");
            if(index>0){
                b.replace(index, index + 1, "" );
                target = b.toString();

                enhancedName = separateConjoinedName(target, refrence);            
                if(enhancedName!=null){
                    MatchedName matchedName = new MatchedName();
                    matchedName.index = mn.getIndex();
                    doCompleteCheck(matchedName, enhancedName, refrence);// name1 is always the name we get it from the reader

                    if(matchedName.bestScore>getMatchedName(0).getBestScore()){
                        if(updateSimilarityList( matchedName, true)){
                            currentName = enhancedName;
                            listUpdated = true;
                        }                     
                    }                                      
                }
            }
            
  
        
// 4. : Ha mdi Taher Alta heri => HamdiTaherAltaheri => Hamdi Taher Altaheri
            target = target.replaceAll(" ", "");
            enhancedName = separateConjoinedName(target, refrence);            
            if(enhancedName!=null){
                MatchedName matchedName = new MatchedName();
                matchedName.index = mn.getIndex();
                doCompleteCheck(matchedName, enhancedName, refrence);// name1 is always the name we get it from the reader
                
                if(matchedName.bestScore>getMatchedName(0).getBestScore()){
                    if(updateSimilarityList( matchedName, true)){
                        currentName = enhancedName;
                        listUpdated = true;
                    }                     
                }                                      
            }                                                                                       
        }
        return listUpdated;
    }
  
    /**
    * separate conjoined target (Name) after compare it with 
    * the FirstName and LastName of the refrence (Name)
    * 
    *    refrence, FirstName = Hamdi. LastName = Altaheri.
    *    target = HamdiTaherAltaheri =(return value)=>  Hamdi Taher Altaheri
    *    target = TaherAltaheri =(return value)=> Taher Altaheri
    *    target = HamdiAltaheri =(return value)=> Hamdi Altaheri
    *    target = HamdiTaher =(return value)=> Hamdi Taher  
    */ 
    private Name separateConjoinedName(String target, Name refrence){
        String curTarget = target;
        String fname = refrence.getFirstName();
        String lname = refrence.getLastName();
        
        String s[]={"","",""};
        
        if(curTarget.startsWith(fname)){
            s[0] = curTarget.substring(0, fname.length());
            curTarget = s[1] = curTarget.substring(fname.length());
        }
        
        if(curTarget.endsWith(lname)){
            //  refrence, FirstName = Hamdi. LastName = Altaheri.
            //  target = HamdiAltaheri =(return value)=> Hamdi Altaheri
            s[1] = curTarget.substring(0, curTarget.length()-lname.length());
            s[2] = curTarget.substring(curTarget.length()-lname.length());
        } 
        else if(lname.startsWith("al")){
            //  refrence, FirstName = Hamdi. LastName = Altaheri.
            //  target = TaherTaheri =(return value)=> Taher Taheri            
            if(curTarget.endsWith(lname.substring(2))){
                s[1] = curTarget.substring(0, curTarget.length()-lname.length()+2);
                s[2] = curTarget.substring(curTarget.length()-lname.length()+2);                
            }            
        }
        
        curTarget ="";
        for (String s1 : s) {
            if(!s1.isEmpty())curTarget += s1+" ";
        }
        
        curTarget = curTarget.trim();
        // matches one or more spaces and replace them by single space.
        String pattern = " +";
        curTarget = curTarget.replaceAll(pattern, " ");
                
        if(curTarget.isEmpty()|| curTarget.equals(target)){
            return null;
        }else{ 
            return  excelReader.nameParser.parse(curTarget);
        }                            
    }
                
    private boolean testNameSimilarity()  {
        boolean listUpdated = false;
        MatchedName matchedName = new MatchedName();
                                 
        for(int i = 0; i <excelReader.names.length; i++){  
            matchedName.resetData();
            matchedName.index = i; 
            doCompleteCheck(matchedName, currentName, excelReader.names[i]);// name1 is always the name we get it from the reader
            
            if(matchedName.getBestScore()>similarityListThreshold){
                if( updateSimilarityList(matchedName, false) ){                    
                    listUpdated = true;
                    if(isWellMatchedName(matchedName)){
                        break;
                    }   
                    matchedName = new MatchedName();
                }
            }                
        }        
        return listUpdated;
    } 
    
    private boolean updateSimilarityList (MatchedName matchedName, boolean enhanceCurrent){
        boolean listUpdated = false;

        // check if the name is currently exist in the list and 
        // remove the name in the list if the current name has better score
        // or just return if the name in the list has better score than the current name
        for (int i=0; i<similarityList.size(); i++){
            if(matchedName.getIndex() == similarityList.get(i).getIndex()){
                if(matchedName.getBestScore() >= similarityList.get(i).getBestScore()){
                    similarityList.remove(i);
                }else{
                    return false;                
                }
            }          
        }
                     
        if(similarityList.isEmpty()){
            similarityList.add(matchedName); 
            listUpdated = true;
        }else if(similarityList.size()<similarityListSize){                   
            for (int i=0; i<similarityList.size(); i++){
                if(matchedName.getBestScore() > similarityList.get(i).getBestScore()){
                    similarityList.add(i, matchedName);
                    return true;
                }
            }
            similarityList.add(matchedName);
            listUpdated = true;
        }else if(similarityList.size()==similarityListSize){
            for (int i=0; i<similarityList.size(); i++){
                if(matchedName.getBestScore() > similarityList.get(i).getBestScore()){                    
                    similarityList.remove(similarityListSize-1);
                    similarityList.add(i, matchedName);
                    listUpdated = true;
                    break;
                }
            }
        } else{
            System.err.println("Error In the List");
        }

        return listUpdated;
    }
    
    private void doCompleteCheck(MatchedName matchedName, Name name1, Name name2){
        // name1 is always the name we get it from the reader
howmuch++;
        // fullNameScore                    
        dL.setData(name1.getFullName(), name2.getFullName());              
        matchedName.fullNameScore = dL.getSmartPctDiff(); 
        // firstFirstScore;
        dL.setData(name1.getFirstName(), name2.getFirstName());              
        matchedName.firstFirstScore = dL.getSmartPctDiff(); 
        // lastLastScore
        dL.setData(name1.getLastName(), name2.getLastName());              
        matchedName.lastLastScore = dL.getSmartPctDiff(); 
        // midLastScore
        dL.setData(name1.getMiddleName(), name2.getLastName());              
        matchedName.midLastScore = dL.getSmartPctDiff(); 
        // lastMidScore
        dL.setData(name1.getLastName(), name2.getMiddleName());              
        matchedName.lastMidScore = dL.getSmartPctDiff();   
        // firstLastScore
        dL.setData(name1.getFirstName(), name2.getLastName());              
        matchedName.firstLastScore = dL.getSmartPctDiff(); 
        // lastFirstScore
        dL.setData(name1.getLastName(), name2.getFirstName());              
        matchedName.lastFirstScore = dL.getSmartPctDiff();        
        // MiddleName of name1 is equal the MiddleName + LastName of name2
        // Mid_MidandLast_Score
        dL.setData(name1.getMiddleName(), name2.getMiddleName()+" "+name2.getLastName());              
        matchedName.Mid_MidandLast_Score = dL.getSmartPctDiff();  
        // MiddleName of name2 is equal the MiddleName + LastName of name1
        // MidandLast_Mid_Score
        dL.setData(name2.getMiddleName(), name1.getMiddleName()+" "+name1.getLastName());              
        matchedName.MidandLast_Mid_Score = dL.getSmartPctDiff();  
       
        matchedName.updateBestScore();                        
    }

    public String toString(){
        String similarityStr="******************************************************\n";

        for (int i=0; i<similarityList.size(); i++){
            MatchedName mn = (MatchedName)similarityList.get(i);
            Name name = excelReader.names[mn.index];
            similarityStr+=                     
            "index: "+ (mn.index+1) +"\n"+
                currentName.getFullName()+",    "+ name.getFullName()+ ",    "+mn.getBestScore()+"\n"+
                    
                "IN:... "+currentName.getFullName()+",    "+ currentName.getFirstName()+",    "+currentName.getMiddleName()+",    "+currentName.getLastName()+"\n"+
                "DB:... "+name.getFullName()+",    "+ name.getFirstName()+",    "+name.getMiddleName()+",    "+name.getLastName()+"\n"+
                "......................................................\n"+                                
                    "firstFirstScore:    "+ mn.firstFirstScore+"\n"+  
                    "firstLastScore:    "+ mn.firstLastScore+"\n"+ 
                    "lastFirstScore:    "+ mn.lastFirstScore+"\n"+ 
                    "lastLastScore:    "+ mn.lastLastScore+"\n"+ 
                    "lastMidScore:    "+ mn.lastMidScore+"\n"+ 
                    "midLastScore:    "+ mn.midLastScore+"\n"+                 
                "......................................................\n"+ 
            "Best Score: "+mn.getScoreTypeName(mn.getBestScoreType()) + ", Score: " + mn.getBestScore()+"\n"+
            mn.getScoreTypeName(mn.FULL_MATCH) + ": "+  mn.getScore(mn.FULL_MATCH)+"\n"+
            mn.getScoreTypeName(mn.FF_LL_PART_MATCH) + ": "+  mn.getScore(mn.FF_LL_PART_MATCH)+"\n"+
            mn.getScoreTypeName(mn.FF_LM_PART_MATCH) + ": "+  mn.getScore(mn.FF_LM_PART_MATCH)+"\n"+
            mn.getScoreTypeName(mn.FF_ML_PART_MATCH) + ": "+  mn.getScore(mn.FF_ML_PART_MATCH)+"\n"+
            mn.getScoreTypeName(mn.FL_LF_PART_MATCH) + ": "+  mn.getScore(mn.FL_LF_PART_MATCH)+"\n"+                    
            "----------------------------------------------\n";            
        }
        
        return similarityStr+"******************************************************\n";
    }                           
}
