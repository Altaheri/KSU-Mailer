package com.ksu.mailnoti.core.text;

//import org.apache.log4j.Logger;



/**
 * @author http://itssmee.wordpress.com/2010/06/28/java-example-of-damerau-levenshtein-distance/
 * Similar to Levenshtein, Damerau-Levenshtein also calculates the edit distances between two strings.
 * It based around comparing two strings and counting the number of insertions, deletions, and
 * substitution of single characters, and transposition of two characters.
 */
public class DamerauLevenshtein {
    private String compOne;
    private String compTwo;
    private int[][] matrix;
    private Boolean calculated = false;
//    public static final Logger log = Logger.getLogger(DamerauLevenshtein.class);


    public DamerauLevenshtein(String a, String b) {
        setData(a, b);
    }
    
    public DamerauLevenshtein() {

    }    

    public int[][] getMatrix() {
        setupMatrix();
        return matrix;
    }

    public int getNumDiffs() {
        if (!calculated) setupMatrix();
        return matrix[compOne.length()][compTwo.length()];
    }

//    public float getPctDiff(){
//        float diffs = getNumDiffs();
//        return (diffs/Math.min(compOne.length(),compTwo.length()));
//    }
    
    public double getSmartPctDiff(){
        int diffs = getNumDiffs();
        if(diffs != 0){
            String str[] = getAlignSequences();    
            
            // Fix al insertion  "alhichri", "  hichri"
            if (str[0].startsWith("al") && str[1].startsWith("  ") && str[1].length()>4){
                setData(compOne.substring(2).trim(),compTwo);
            }else if(str[0].startsWith("  ") && str[1].startsWith("al") && str[0].length()>4){
                setData(compOne, compTwo.substring(2).trim());                
            }

            // Fix letter 'i' and 'l' replacement
            if( getPctDiff() > 50 ){
                for (int i = 0; i < Math.min(compOne.length(), compTwo.length()); i++) {
                    if( (compOne.charAt(i) == 'i' ) && ( compTwo.charAt(i) == 'l' ) ){
                        compTwo = compTwo.substring(0, i) + "i" + compTwo.substring(i+1);
                        setData(compOne, compTwo);                
                    }else if( (compTwo.charAt(i) == 'i' ) && ( compOne.charAt(i) == 'l' ) ){
                        compOne = compOne.substring(0, i) + "i" + compOne.substring(i+1);
                        setData(compOne, compTwo);                

                    }
                }
            }
            
            // Fix Vowel Letters i, o, u, e, a, y
            // 1. if the two string is equal in length or less by one |leng1-leng2|<=1
            // 2. delete all Vowel Letters in the two string
            // 3. after deletion if the two string is EQUAL Then the two string is equals.
            if( getPctDiff() > 50 && Math.abs(compOne.length() - compTwo.length())<=1 ){
                String str1 = compOne.replaceAll("i|o|u|e|a|y", "");
                String str2 = compTwo.replaceAll("i|o|u|e|a|y", "");
                if(str1==str2 && str1.length()>1){
                    compTwo = compOne;
                    setData(compOne, compTwo);
                }
            }

            
            // Fix Vowel Letters replacements i, o, u, e, a,
            if( getPctDiff() > 50 && Math.abs(compOne.length() - compTwo.length())<=1 ){
                boolean isChanged = false;
                char[] ch1 = compOne.toCharArray();
                char[] ch2 = compTwo.toCharArray();

                for (int i = 0; i < Math.min(compOne.length(), compTwo.length()); i++) {
                    if (ch1[i] == 'a') {                    
                        if( ch2[i] == 'e' || ch2[i] == 'i' || ch2[i] == 'o' || ch2[i] == 'u'){
                            ch2[i] = ch1[i];
                            isChanged = true;
                        }
                    }               
                    if (ch1[i] == 'e') {                    
                        if( ch2[i] == 'a' || ch2[i] == 'i' || ch2[i] == 'o' || ch2[i] == 'u'){
                            ch2[i] = ch1[i];
                            isChanged = true;
                        }
                    }
                    if (ch1[i] == 'i') {                    
                        if( ch2[i] == 'a' || ch2[i] == 'e' || ch2[i] == 'o' || ch2[i] == 'u'){
                            ch2[i] = ch1[i];
                            isChanged = true;
                        }
                    }
                    if (ch1[i] == 'o') {                    
                        if( ch2[i] == 'a' || ch2[i] == 'e' || ch2[i] == 'i' || ch2[i] == 'u'){
                            ch2[i] = ch1[i];
                            isChanged = true;
                        }
                    }
                    if (ch1[i] == 'u') {                    
                        if( ch2[i] == 'a' || ch2[i] == 'e' || ch2[i] == 'i' || ch2[i] == 'o'){
                            ch2[i] = ch1[i];
                            isChanged = true;
                        }
                    }
                } 
                if (isChanged){
                    setData(String.valueOf(ch1), String.valueOf(ch2));
                }
            }                
            
            
            // Fix mohammed name forms
            if( getPctDiff() > 50 && Math.abs(compOne.length() - compTwo.length())<=1 ){
                if ((compOne.equals("mohammed")||compOne.equals("mohmmed")||compOne.equals("mohmed")||compOne.equals("mohamed")
                    ||compOne.equals("muhammed")||compOne.equals("muhmmed")||compOne.equals("muhmed")||compOne.equals("muhamed")
                    ||compOne.equals("mohammad")||compOne.equals("mohmmad")||compOne.equals("mohmad")||compOne.equals("mohamad")
                    ||compOne.equals("muhammad")||compOne.equals("muhmmad")||compOne.equals("muhmad")||compOne.equals("muhamad"))
                    &&
                    (compTwo.equals("mohammed")||compTwo.equals("mohmmed")||compTwo.equals("mohmed")||compTwo.equals("mohamed")
                    ||compTwo.equals("muhammed")||compTwo.equals("muhmmed")||compTwo.equals("muhmed")||compTwo.equals("muhamed")
                    ||compTwo.equals("mohammad")||compTwo.equals("mohmmad")||compTwo.equals("mohmad")||compTwo.equals("mohamad")
                    ||compTwo.equals("muhammad")||compTwo.equals("muhmmad")||compTwo.equals("muhmad")||compTwo.equals("muhamad")))
                {                                                                                            
                    compTwo = compOne;
                    setData(compOne, compTwo);
                }
                
            }


        }
        return getPctDiff();
    }
    
    public double getPctDiff(){
        float diffs = getNumDiffs();
        int bigger = Math.max(compOne.length(),compTwo.length());
        double matchScore = 1-(diffs/bigger);
        matchScore = (double)(Math.round(matchScore*10000))/100;   
        return  matchScore;               
    }
    
        
    private void setupMatrix() {
        int cost = -1;
        int del, sub, ins;

        matrix = new int[compOne.length() + 1][compTwo.length() + 1];

        for (int i = 0; i <= compOne.length(); i++) {
            matrix[i][0] = i;
        }

        for (int i = 0; i <= compTwo.length(); i++) {
            matrix[0][i] = i;
        }

        for (int i = 1; i <= compOne.length(); i++) {
            for (int j = 1; j <= compTwo.length(); j++) {
                if (compOne.charAt(i - 1) == compTwo.charAt(j - 1)) {
                    cost = 0;
                } else {
                    cost = 1;
                }

                del = matrix[i - 1][j] + 1;
                ins = matrix[i][j - 1] + 1;
                sub = matrix[i - 1][j - 1] + cost;

                matrix[i][j] = minimum(del, ins, sub);

                if ((i > 1) && (j > 1) && (compOne.charAt(i - 1) == compTwo.charAt(j - 2)) && (compOne.charAt(i - 2) == compTwo.charAt(j - 1))) {
                    matrix[i][j] = minimum(matrix[i][j], matrix[i - 2][j - 2] + cost);
                }
            }
        }

        calculated = true;
        displayMatrix();
    }

    private void displayMatrix() {
//        log.debug("  " + compOne);
        for (int y = 0; y <= compTwo.length(); y++) {
//            if (y - 1 < 0) log.debug(" ");
//            else log.debug(compTwo.charAt(y - 1));
            for (int x = 0; x <= compOne.length(); x++) {
//               log.debug(matrix[x][y]);
            }
//            log.debug("");
        }
    }

    private int minimum(int d, int i, int s) {
        int m = Integer.MAX_VALUE;

        if (d < m) m = d;
        if (i < m) m = i;
        if (s < m) m = s;

        return m;
    }

    private int minimum(int d, int t) {
        int m = Integer.MAX_VALUE;

        if (d < m) m = d;
        if (t < m) m = t;

        return m;
    }
    
    public boolean setData(String a, String b){        
        if ((a.length() > 0 || !a.isEmpty()) || (b.length() > 0 || !b.isEmpty())) {
            compOne = a;
            compTwo = b;
        }
        calculated = false;
        return true;
    }
    
    public int getDHSimilarity(String a, String b) {
        if (!setData( a,  b)) return 100;
//        System.out.println(a);
//        System.out.println(b);
        return getDHSimilarity();
    }    

    public int getDHSimilarity() {
        int res = -1;
        int INF = compOne.length() + compTwo.length();

        matrix = new int[compOne.length() + 1][compTwo.length() + 1];

        for (int i = 0; i < compOne.length(); i++) {
            matrix[i + 1][1] = i;
            matrix[i + 1][0] = INF;
        }

        for (int i = 0; i < compTwo.length(); i++) {
            matrix[1][i + 1] = i;
            matrix[0][i + 1] = INF;
        }

        int[] DA = new int[24];

        for (int i = 0; i < 24; i++) {
            DA[i] = 0;
        }

        for (int i = 1; i < compOne.length(); i++) {
            int db = 0;

            for (int j = 1; j < compTwo.length(); j++) {

                int i1 = DA[compTwo.indexOf(compTwo.charAt(j - 1))];
                int j1 = db;
                int d = ((compOne.charAt(i - 1) == compTwo.charAt(j - 1)) ? 0 : 1);
                if (d == 0) db = j;

                matrix[i + 1][j + 1] = Math.min(Math.min(matrix[i][j] + d, matrix[i + 1][j] + 1), Math.min(matrix[i][j + 1] + 1, matrix[i1][j1] + (i - i1 - 1) + 1 + (j - j1 - 1)));
            }
            DA[compOne.indexOf(compOne.charAt(i - 1))] = i;
        }

        return matrix[compOne.length()][compTwo.length()];
    }
    
    public String[] getAlignSequences(){
        String seqOne="";
        String seqTwo="";
        int i= compOne.length();
        int j= compTwo.length();
        

                
        for (; !(i==0 && j==0); ){
            if(i==0){
                while(j!=0){
                    seqOne = " " + seqOne;
                    seqTwo = compTwo.substring(j-1, j) + seqTwo;
                    j--;                  
                }
            }else if(j==0){
                while(i!=0){
                    seqOne = compOne.substring(i-1, i) + seqOne;
                    seqTwo = " " + seqTwo;
                    i--;  
                }                
            }else{                
                int cur = matrix[i][j];

                int del = matrix[i-1][j];
                int ins = matrix[i][j-1];
                int sub = matrix[i-1][j-1];

                int min = minimum(del, ins, sub);

                if( min == sub ){
                    if( sub == cur ){
                        seqOne = compOne.substring(i-1, i) + seqOne;
                        seqTwo = compTwo.substring(j-1, j) + seqTwo;
                        i--; 
                        j--;    
                    }else if( sub < cur ){
                        seqOne = compOne.substring(i-1, i) + seqOne; // X .... replacement
                        seqTwo = compTwo.substring(j-1, j) + seqTwo; // X .... replacement
                        i--; 
                        j--;                                            
                    }
                }else if(ins == min) { 
                    seqOne = " " + seqOne;
                    seqTwo = compTwo.substring(j-1, j) + seqTwo;
                    j--;                   
                }else if(del == min) { 
                    seqOne = compOne.substring(i-1, i) + seqOne;
                    seqTwo = " " + seqTwo;
                    i--;                   
                } 
            }
        }
        String str[] = {seqOne,seqTwo};
        return str;
        
    }
    public String getStyledComparison(){
        String str[] = getAlignSequences();
        String seqOne = str[0];
        String seqTwo = str[1];
        System.out.println(seqOne+"\n"+seqTwo);

        String styledseqOne="<span style=\"color:rgb(0,0,0);font-weight:bold;font-family:monospace\">";
        String styledseqTwo="<span style=\"color:rgb(0,0,0);font-weight:bold;font-family:monospace\">";        

                
        for (int i=0;  i< seqOne.length(); i++ ){           
            if( seqOne.charAt(i) != seqTwo.charAt(i) ){
                if( seqOne.charAt(i) == ' '){
                    styledseqOne += "&nbsp";
                    styledseqTwo += "<span style=color:rgb(51,153,102)>"+seqTwo.charAt(i)+"</span>";
                }else if(seqTwo.charAt(i) == ' '){
//                    styledseqOne += "<span style=color:rgb(51,153,255)>"+seqOne.charAt(i)+"</span>";
                    styledseqOne += "<span style=color:rgb(51,153,102)>"+seqOne.charAt(i)+"</span>";
                    styledseqTwo += "&nbsp";
                }else {
                    styledseqOne += "<span style=color:rgb(255,51,51)>"+seqOne.charAt(i)+"</span>";
                    styledseqTwo += "<span style=color:rgb(255,51,51)>"+seqTwo.charAt(i)+"</span>";
                }                
            } else{
                    styledseqOne += seqOne.charAt(i);
                    styledseqTwo += seqTwo.charAt(i);
            }             
        }
        styledseqOne+="</span>";
        styledseqTwo+="</span>";
        
        return styledseqOne+"<br>"+styledseqTwo;        
    }

    public static void main(String[] args) {
        
        
        
        DamerauLevenshtein dl = new DamerauLevenshtein();
        
        dl.setData("soﬁen", "sofien");  
        System.out.println("sofien = "+dl.getNumDiffs());  
        
        dl.setData("MENU1", "KING");  
        System.out.println(dl.getNumDiffs());    
        String str[] = dl.getAlignSequences();
        System.out.println(str[0]+"\n"+str[1]);
        System.out.println(dl.getSmartPctDiff());  
        System.out.println(""); 
        
        dl.setData("Ht", "gt");  
        System.out.println(dl.getNumDiffs());    
        //System.out.println( "hd".compareTo("gt") ); 
        str = dl.getAlignSequences();
        System.out.println(str[0]+"\n"+str[1]);
        System.out.println(dl.getSmartPctDiff()); 
        System.out.println(""); 
        
        dl.setData("Hamdi T. Altahery", "Hamdi Tahr Altahery");  
        System.out.println(dl.getNumDiffs());    
        str = dl.getAlignSequences();
        System.out.println(str[0]+"\n"+str[1]);
        System.out.println(dl.getSmartPctDiff());   
        //System.out.println(dl.getStyledComparison());  
        
        dl.setData("alhichri", "hichri");  
        System.out.println(dl.getNumDiffs());  
        str = dl.getAlignSequences();
        System.out.println(str[0]+"\n"+str[1]);
        System.out.println(dl.getSmartPctDiff());   
    }  
}