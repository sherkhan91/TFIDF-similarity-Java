
package javaapplication9;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;
import java.lang.Math;
import java.io.*;
import java.util.Scanner;
import java.util.Iterator;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import static javaapplication9.TFIDF_Similarity.Compare;


public class TFIDF_Similarity {
    /*** GLOBAL variables go here ***/
    public static Integer a =10;
    public static Integer total_document_size;
    public static Integer partition_index;
    public static HashMap<String,Double> IDFValues = new HashMap<String,Double>(); 
    public static HashMap<String, List<String>> HashMapcompareFinal = new HashMap<String,List<String>>();
    public static String[] document;
    public static boolean demo = true;
    
    
    public static void main(String[] args) {
        // TODO code application logic here
    
          
        String[] document;
        document = new String[8];
        document[0] = "you were born with potential";
        document[1] = "you were born with goodness and trust";
        document[2] = "you were born with ideas and dreams";
        document[3] = "you were born with greatness";
        document[4] = "you were born with wings";
        document[5] = "you are not meant for crawling, so don't";
        document[6] = "you have wings";
        document[7] = "learn to use them and fly";
        
        /*****************************************************/
        /***** For Testing or Playing uncomment following ****/
        /*****************************************************/
        /*** argument document[0] is first sentence 1,2,3 for ***/
        /***  respective sentences, just change document number ***/
        /*** and play around ***/
        
        
        String first_doc = document[0]; //first sentence
        String second_doc = document[3]; // second sentence
        HashMap<String,Double> IDFValues =  calculateIDF(document);  
        
        print(first_doc+"<->"+second_doc);
        Double similarity_Score = (Compare(IDFValues,first_doc,second_doc)*100);
        print("Similarity Score: "+similarity_Score.intValue()+"%");   
        
        
        
        /******************************************************/
        /*****************************************************/
     
                 
     
        /*** THIS FUNCTION READ CSV FILES DO CALCULATION AND WRITE BACK TO TEST.CSV ***/
        /*** need help write sher9khan@gmail.com ***/
        /*
        MainFunctionFileCompare();
        */
  
    }
    
    
    public static void MainFunctionFileCompare(){
        LinkedList<String> vendor_customer_name;
        LinkedList<String> external_sanction_list;
        /*** CHANGE FILE PATH AND FILE NAMES ACCORDINGLY.. ***/
        if(demo==true){
            vendor_customer_name = readCSVList("C:\\Users\\sher\\Desktop\\demo\\test_customer_list.csv");
             external_sanction_list   = readCSVList("C:\\Users\\sher\\Desktop\\demo\\test_sanction_list.csv");
        
        }else{
             vendor_customer_name = readCSVList("C:\\Users\\sher\\Desktop\\complete_demo\\vendor_customer_list.csv");
             external_sanction_list   = readCSVList("C:\\Users\\sher\\Desktop\\complete_demo\\sanction_list_us.csv");
        
        }

       // LinkedList<String> external_sanction_list = readCSVList("C:\\Users\\sher\\Downloads\\demo\\test_sanction_list.csv");
        int total_document_size = (vendor_customer_name.size()+external_sanction_list.size());
        String[] document = new String[total_document_size-1];
        int partition_index = 0;
        HashMap<String,Double> IDFValues = new HashMap<String,Double>(); 
        HashMap<String, List<String>> HashMapcompareFinal = new HashMap<String,List<String>>();
  
        for(int i =0;i<vendor_customer_name.size();i++){
            document[i] = vendor_customer_name.get(i);
            partition_index = i;
        }
          
        //print("partition index: "+String.valueOf(partition_index));
        for(int i =partition_index+1;i<external_sanction_list.size()+partition_index;i++){
            document[i] = external_sanction_list.get(i-partition_index);
            }
        print("Data Loaded into document and Size is: "+String.valueOf(document.length));
        print("Going to calculate IDF Values, sometime it can take  few minutes..");
        IDFValues = calculateIDF(document);
        print("Finished calculating IDF Values, now will check record similarity one by one..");
        Double similarity_value  = 0.0;
        String sanction_name = "";
        String vendor_name = "";
        
        for(int v=1;v<partition_index;v++){
            vendor_name = document[v];
            Integer similarityScore = 0;
            
            for(int s=partition_index;s<total_document_size-1;s++){
                sanction_name = document[s];
                similarity_value = Compare(IDFValues,vendor_name,sanction_name);
   
                similarity_value = (similarity_value*100);
                Integer tempInt = similarity_value.intValue();
                
                if(tempInt>similarityScore)
                {
                    LinkedList<String> currentVal =  new LinkedList<String>();
                     similarityScore = tempInt;
                     currentVal.add(sanction_name);
                     currentVal.add(similarityScore.toString());
                     HashMapcompareFinal.put(vendor_name, currentVal);
                }
            }
            
            
            print("Total REcords to Check: "+String.valueOf(partition_index));
            print("Records Check Till Now: "+String.valueOf(v));
            
            //HashMap<String,Double> sortedMap = HashMapCompareTemp.entrySet().stream().sorted(Entry.comparingByValue()).collect(Collectors.toMap(Entry::getKey,Entry::getValue,(e1,e2)->e1,LinkedHashMap::new));
        }
        
        writeDataLineByLineCSV(HashMapcompareFinal);
        
    }
  
    public static void writeDataLineByLineCSV(HashMap<String, List<String>> myHashMap) 
    {   
        String path;
        if(demo==true){
            path = "C:\\Users\\sher\\Desktop\\demo_result.csv";
        }else{
            path = "C:\\Users\\sher\\Desktop\\demo_result.csv";
        }

        //try (PrintWriter writer = new PrintWriter(new File("C:\\Users\\sher\\Downloads\\demo\\test.csv"))) 
//        try (PrintWriter writer = new PrintWriter(new File("C:\\Users\\sher\\Desktop\\complete_demo\\complete_test.csv"))) 
        try (PrintWriter writer = new PrintWriter(new File(path))) 
        {

          StringBuilder sb = new StringBuilder();
          sb.append("id");
          sb.append(',');
          sb.append("Existing Customer");
          sb.append(',');
           sb.append("Sanction List");
          sb.append(',');
          sb.append("Similarity Score");
          sb.append('\n');
          Integer count  = 0;
          writer.write(sb.toString());

          for (HashMap.Entry<String,List<String>> entry: myHashMap.entrySet())
            {
                 StringBuilder sb2 = new StringBuilder();
                sb2.append(count.toString());
                sb2.append(',');
                sb2.append(entry.getKey());
                sb2.append(',');
                sb2.append(entry.getValue().get(0));
                sb2.append(',');
                sb2.append(entry.getValue().get(1));
                sb2.append('\n');

                writer.write(sb2.toString());

                count+=1;
            }

          System.out.println("Finished writing to File!");

            } catch (FileNotFoundException e) {
          System.out.println(e.getMessage());
            } 
        } 

    
    public static LinkedList readCSVList(String path){
        LinkedList<String> temp =  new LinkedList<String>();
        
     try{
         Integer count = 0;
        Scanner read_scanner = new Scanner(new File(path));
        while(read_scanner.hasNextLine()){
            String Line = read_scanner.nextLine().replaceAll("[^a-zA-Z ]", "").toLowerCase();
            
            if(Line.length()==0){
                print("here is empty line: "+Line);
            }
            else{
//                print("appending line: "+count.toString()+" ->"+Line);
                temp.add(Line);
            }
           // print("start: "+ Line+" :END");
            count+=1;
            print("Reading File:"+path+"Row:"+String.valueOf(count));
            //print("Line is:"+Line);
        }
        read_scanner.close();
    }catch (Exception e){
         e.printStackTrace();
            }
        
        return temp;
    }
    
    public static Double Compare(HashMap<String,Double> HashMapConstIDFValues,String document0,String document1){
        Double result = 0.0;
        HashMap<String,Double> HashMapCompareDoc0 = new HashMap<String,Double>();
        HashMap<String,Double> HashMapCompareDoc1 = new HashMap<String,Double>();
        HashMapCompareDoc0 = findTFIDFForThisDoc(HashMapConstIDFValues,document0);
        HashMapCompareDoc1 = findTFIDFForThisDoc(HashMapConstIDFValues,document1);
        result = CosineSim(HashMapCompareDoc0,HashMapCompareDoc1);
        return result;
    }
    
    public static HashMap findTFIDFForThisDoc(HashMap<String,Double> HashMapConstIDFValues, String document){
        HashMap<String,Double> HashMapHold7 =  new HashMap<String,Double>();
        HashMap<String,Double> HashMapTF7 =  calculateTermFrequency(document);
        HashMap<String,Double> HashMapCompareDoc7 = new HashMap<String,Double>();
        
         HashMapHold7 = TFIDFSpecific(HashMapConstIDFValues,HashMapTF7);
        Double hold7 = NormalizeL2(HashMapHold7);
        for(Map.Entry<String,Double> entry:HashMapHold7.entrySet() ){
//            print("term: "+entry.getKey()+" TF-IDF in doc1  "+(entry.getValue()/hold1));
            HashMapCompareDoc7.put(entry.getKey(), (entry.getValue()/hold7));
        }
        
        return HashMapCompareDoc7;
//        CosineSim(HashMapCompareDoc3,HashMapCompareDoc3);
        
    }
    
    
    public static Double CosineSim(HashMap<String,Double> Doc0,HashMap<String,Double> Doc1){
        Double dotProductValue  = 0.0;
        Double Doc0sumOfSquares = 0.0;
        Double Doc1sumOfSquares = 0.0;
        Double multiplyOfSqrt = 0.0;
        
        /*
        for(Map.Entry<String,Double> entry0: Doc0.entrySet()){
             for(Map.Entry<String,Double> entry1: Doc1.entrySet()){
                 if(entry0.getKey().equalsIgnoreCase(entry1.getKey())){
                     dotProductValue += (entry0.getValue()*entry1.getValue());
                 }
             }
        }
        */
        
        /*Optimised code here*/
        try{
        for(Map.Entry<String,Double> entry0: Doc0.entrySet()){
            String key = entry0.getKey();
            dotProductValue += (entry0.getValue()*Doc1.get(key)); 
       }
        }catch(Exception e){
            
        }
        
        
        for(Map.Entry<String,Double> entry0: Doc0.entrySet()){
            Doc0sumOfSquares += Math.pow(entry0.getValue(),2);
        }
        for(Map.Entry<String,Double> entry1: Doc1.entrySet()){
            Doc1sumOfSquares += Math.pow(entry1.getValue(),2);
        }
        
        multiplyOfSqrt = Math.sqrt(Doc0sumOfSquares)*Math.sqrt(Doc1sumOfSquares);
        
//        print((dotProductValue/multiplyOfSqrt));
        return (dotProductValue/multiplyOfSqrt);
    }
    
    public static Double NormalizeL2(HashMap<String,Double> specific){
        Double oneValue = 0.0;
        for(Map.Entry<String,Double> entry: specific.entrySet()){
            Double tempVal = 0.0;
            tempVal = Math.pow(entry.getValue(), 2);
            oneValue+=tempVal;
        }
        oneValue = Math.sqrt(oneValue);
        return oneValue;
    }
    
    public static HashMap TFIDFSpecific(HashMap<String,Double> IDF, HashMap<String,Double> DocTF){
     HashMap<String,Double> temp = new HashMap<String,Double>();
     /*
     for(Map.Entry<String,Double> entry: IDF.entrySet()){
         for(Map.Entry<String,Double> entry1: DocTF.entrySet()){
             if(entry.getKey().equalsIgnoreCase(entry1.getKey())){
             temp.put(entry.getKey(), entry.getValue()*entry1.getValue());
         }
         }
     }*/
     /*Optimized code below*/
     
         for(Map.Entry<String,Double> entry1: DocTF.entrySet()){
             String key_tfidf = entry1.getKey();
             //print("Specific doc key:"+key_tfidf);
             //print("idf value at above:"+IDF.get(key_tfidf));
             try{
                 temp.put(key_tfidf, (entry1.getValue()*IDF.get(key_tfidf)));
             }catch(Exception e){
                 //print("Specific doc key:"+key_tfidf);
                 //print("putting zero for it");
                 //temp.put(key_tfidf, (entry1.getValue()*0.0));
             }
             
         }
   
     
     return temp;
    }

    
   
    public static HashMap calculateIDF(String[] alldocuments){
        HashMap<String, Double> IDF = new HashMap<String,Double>();
        LinkedList<String> tempList =  new LinkedList<String>();
        
//        Integer total_documents = alldocuments.length;
        StringBuilder allterms = new StringBuilder();
        for(int j=0;j<alldocuments.length;j++){
            allterms.append(" "+alldocuments[j]);
        }
        String mAllTerms = allterms.toString();
        
        String[] allTermsArray2 =  mAllTerms.split(" ");
        Integer total_documents = alldocuments.length;
        
        for(String terms:allTermsArray2){
            if(terms.isEmpty()){
                
            }else{
                tempList.add(terms);
            }
        }
         
        String[] allTermsArray = new String[tempList.size()];
        for(int z=0;z<tempList.size();z++){
            allTermsArray[z] = tempList.get(z);
        }
            
    for(int k=0;k<allTermsArray.length;k++){
        String term = allTermsArray[k];
        Integer termAppearsInDocs = 0;
        for(int i=0; i<alldocuments.length;i++){
            String[] currentDoc = alldocuments[i].split(" ");
            for(String word: currentDoc){
                
                if(term.equalsIgnoreCase(word)){
                     
                    termAppearsInDocs+=1;
                    break;
                }
               
            }
        }
        try{
         Double total_doc = new Double(total_documents);
         Double term_appearance =  new Double(termAppearsInDocs);
//         print("total docs:"+total_doc);
//         print(term+" term appear in:"+term_appearance);
         Double idfValue =  1+Math.log(((1+total_doc)/(1+term_appearance)));
//        print(term+" : "+idfValue.toString());
        IDF.put(term, idfValue);
        }catch(Exception e){
            IDF.put(term, 0.0);
        }
        

    }
//       printHashMap(IDF);
       return IDF;
    }
    
    public static HashMap calculateTermFrequency(String docString){
        String[] doc = docString.split(" ");
         
         HashMap<String,Double> termFreqMap = new HashMap<String,Double>();
        
         Integer size = doc.length;
         for(int i=0;i<size;i++){
             if(termFreqMap.containsKey(doc[i])){
                 Double wordCount = 0.0;
                 wordCount = termFreqMap.get(doc[i]);
                 wordCount = wordCount+1.0;
                 termFreqMap.put(doc[i], wordCount);
             }
             else{
                 termFreqMap.put(doc[i], 1.0);
             }
         }
         for(Map.Entry<String,Double> entry: termFreqMap.entrySet()){
             Double size_double = new Double(size); 
             //Double normalized_tf_value = entry.getValue()/size_double;
             Double normalized_tf_value = entry.getValue();
             termFreqMap.put(entry.getKey(), normalized_tf_value);
             
         }
             
         
        
         return termFreqMap;
     }
    
    
    public static void print(HashMap<String,Double> myHashMap){
         for(Map.Entry<String,Double> entry: myHashMap.entrySet())
            print(entry.getKey()+":"+entry.getValue());
    }
    public static void printt(HashMap<String,Integer> mHashMap){
         for(Map.Entry<String,Integer> entry: mHashMap.entrySet())
            print(entry.getKey()+":"+entry.getValue());
    }  
    public static void print(LinkedList<String> mList){
         for(int i=0;i<mList.size();i++){
             System.out.println(mList.get(i));
         }
     }
    public static void print(String mPrint){
        System.out.println(mPrint);
    }
    public static void print(Double mPrint){
        System.out.println(mPrint.toString());
    }
    public static void print(Integer mPrint){
        System.out.println(mPrint.toString());
    }
    
}


