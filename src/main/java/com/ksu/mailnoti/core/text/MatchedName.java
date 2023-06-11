/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.ksu.mailnoti.core.text;


/**
 *
 * @author Hamdi
 */
public class MatchedName{
    int index;
    double fullNameScore;
    
    // if first Name match First Name
    double firstFirstScore;
    // Then we have 5 more possibilities
    double lastLastScore;
    double midLastScore;
    double lastMidScore;
    // check if the full name1 is equal the FirstName and MidName of name2 
    // vice versa
    //Ex. name1 = Mohammed Ali Ezzat Mokhtar
    //    name2 = Mohammed Ali       Ezzat
    double MidandLast_Mid_Score;  
    double Mid_MidandLast_Score;    
    
    // do all the previous steps after swap first name and last name.
    // if first Name match Last Name AND Last Name match first Name
    double firstLastScore;
    double lastFirstScore;
    
    //
    public static final int FULL_MATCH = 1;
    
    public static final int FF_LL_PART_MATCH = 2;
    public static final int FF_ML_PART_MATCH = 3;
    public static final int FF_LM_PART_MATCH = 4;
    
    public static final int FF_MandL_M = 5;
    public static final int FF_M_MandL = 6;
    
    public static final int FL_LF_PART_MATCH = 7;
    int bestScoreType = 0;
    double bestScore = 0;
    //
    
    
    public double getBestScore(){
        return bestScore;
        //return Double.parseDouble(new DecimalFormat("#.####").format(bestScore));
    }
    
    public int getBestScoreType(){
        return bestScoreType;
    }
    
    public int getIndex(){
        return index;
    }
    
    public void setIndex(){
        this.index=index;
    } 
    
    public double getScore(int scoreType ){
        double score = 0.0;
        if(scoreType == FULL_MATCH){
            score = fullNameScore;
        }else if(scoreType == FF_LL_PART_MATCH){
            score =  (firstFirstScore + lastLastScore)/2;
        }else if(scoreType == FF_ML_PART_MATCH){
            score =  (firstFirstScore + midLastScore)/2;
        }else if(scoreType == FF_LM_PART_MATCH){
            score =  (firstFirstScore + lastMidScore)/2;
        }else if(scoreType == FF_MandL_M){
            score =  (firstFirstScore + MidandLast_Mid_Score)/2;
        }else if(scoreType == FF_M_MandL){
            score =  (firstFirstScore + Mid_MidandLast_Score)/2;
        }else if(scoreType == FL_LF_PART_MATCH){
            score =  (firstLastScore + lastFirstScore)/2;

        }           
        return (double)(Math.round(score*100))/100;
    }
    
    public String getScoreTypeName(int scoreType){
        if(scoreType == FULL_MATCH){
            return "FULL_MATCH";                               
        }else if(scoreType == FF_LL_PART_MATCH){
            return "FF_LL_PART_MATCH";
        }else if(scoreType == FF_ML_PART_MATCH){
            return "FF_ML_PART_MATCH";
        }else if(scoreType == FF_LM_PART_MATCH){
            return "FF_LM_PART_MATCH";
        }else if(scoreType == FF_MandL_M){
            return "FirstFirst_MidandLast_Mid";                        
        }else if(scoreType == FF_M_MandL){
            return "FirstFirst_Mid_MidandLast";              
        }else if(scoreType == FL_LF_PART_MATCH){
            return "FL_LF_PART_MATCH";
        }else
            return "No Score";
    }
    
    public void updateBestScore(){
        if(getScore(FULL_MATCH) > bestScore){
            bestScore = fullNameScore;
            bestScoreType = FULL_MATCH;
        }        
        if( getScore(FF_LL_PART_MATCH) > bestScore){
            bestScore = getScore(FF_LL_PART_MATCH);
            bestScoreType = FF_LL_PART_MATCH;
        }
        if( getScore(FF_ML_PART_MATCH) > bestScore){
            bestScore = getScore(FF_ML_PART_MATCH);
            bestScoreType = FF_ML_PART_MATCH;
        }
        if( getScore(FF_LM_PART_MATCH) > bestScore){
            bestScore = getScore(FF_LM_PART_MATCH);
            bestScoreType = FF_LM_PART_MATCH;
        }

        if( getScore(FF_MandL_M) > bestScore){
            bestScore = getScore(FF_MandL_M);
            bestScoreType = FF_MandL_M;
        }        
        if( getScore(FF_M_MandL) > bestScore){
            bestScore = getScore(FF_M_MandL);
            bestScoreType = FF_M_MandL;
        }         
        if( getScore(FL_LF_PART_MATCH) > bestScore){
            bestScore = getScore(FL_LF_PART_MATCH);
            bestScoreType = FL_LF_PART_MATCH;
        }
    }
    
    public void resetData(){
        index=0;
        fullNameScore=0;
        MidandLast_Mid_Score=0;
        Mid_MidandLast_Score=0;  
        
        firstFirstScore=0;
        lastLastScore=0;
        midLastScore=0;
        lastMidScore=0;
        
        firstLastScore=0;
        lastFirstScore=0;
        
        bestScoreType = 0;
        bestScore = 0;
    }
}
