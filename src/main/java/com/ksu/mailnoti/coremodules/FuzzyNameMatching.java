/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksu.mailnoti.coremodules;
import com.ksu.mailnoti.core.text.InputNameImprover;
import com.ksu.mailnoti.core.text.SimilarityNameExtraction;
import com.ksu.mailnoti.start.StartPanel;

/**
 *
 * @author Hamdi T. Altaheri
 */
public class FuzzyNameMatching {
    public SimilarityNameExtraction similarNameExt; 
    private final StartPanel startPanel;
    //File outputFile ;

    public FuzzyNameMatching(StartPanel startPanel) {
        this.startPanel = startPanel;
        //outputFile = new File("dataset3\\trueResult.txt");
        similarNameExt = new SimilarityNameExtraction();
             
    }
    
    public String  startFuzzyNameMatching(String capturedName, boolean resetSimilarityList){
        String  matchedRecord = null;
        
        if (resetSimilarityList == true){
            similarNameExt.similarityList.clear();            
        }       

        // we need make improve here for the text came from IRISPen
        String improvedCapturedName = InputNameImprover.improveName(capturedName);
        
        if(improvedCapturedName!=null && !improvedCapturedName.equals("")){
            similarNameExt.computeNameSimilarity(improvedCapturedName);
            if( similarNameExt.getBestMatchedRecord() != null){
                matchedRecord = similarNameExt.getBestMatchedRecord()[0];
                matchedRecord += " "+similarNameExt.getBestMatchedName().getBestScore()+" ";
            }
        }
        
        startPanel.updateData(capturedName);            
        if (matchedRecord != null){
            similarNameExt.similarityList.clear();
        }

        return matchedRecord;
    }   
}
