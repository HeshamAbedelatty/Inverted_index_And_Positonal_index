package TFIDFMatrix;

import Inverted.DictEntry;
import Inverted.Posting;

import java.util.*;

public class TFIdf {

    private HashMap<String, WeightOfTerm> Matrix;
    int N = 10;
    public List<Integer> len ;
    public TFIdf() {

        this.Matrix = new HashMap<>();
        this.len =new ArrayList<>();
    }
    public  void buildMatrix(Map<String, DictEntry> invertedIndex) {

        for (Map.Entry<String, DictEntry> entry : invertedIndex.entrySet()) {
            String term;
            double limitedValue;
            WeightOfTerm weightOfTerm = new WeightOfTerm();
            Posting posting = new Posting();
            posting = entry.getValue().pList;
            term = entry.getKey();
            limitedValue = Math.log10(N / (double) entry.getValue().doc_freq);
            weightOfTerm.idf = Math.round(limitedValue * 10000.0) / 10000.0;
            while (posting != null) {
                TFList tfList = new TFList();
                tfList.docId = posting.docId;
                limitedValue = (1 + Math.log10((double) posting.dtf));
                tfList.tf = Math.round(limitedValue * 10000.0) / 10000.0;
                tfList.weight = tfList.tf * weightOfTerm.idf;
                tfList.next = weightOfTerm.pList;
                weightOfTerm.pList = tfList;
                posting = posting.next;
            }
            Matrix.put(term,weightOfTerm);
        }
    }
    public void Display(){
        TFList tfList  ;
        TreeMap<String, WeightOfTerm> matrix = new TreeMap<String, WeightOfTerm>(Matrix);
        System.out.printf("%-15s","Term");
        System.out.printf("%-6s","IDF");
//        System.out.printf("%-9s","TermFreq");
        System.out.printf("%-9s","TFList");
        System.out.println();
        for (Map.Entry<String, WeightOfTerm> entry : matrix.entrySet()) {
            tfList = entry.getValue().pList;
            System.out.printf("%-15s",entry.getKey());
            System.out.printf("%-8s",entry.getValue().idf);
//            System.out.printf("%-9s",invertedIndex.getValue().term_freq);
            printTFList(tfList);
            System.out.println();
            System.out.printf("%23s"," ");
            printTF(tfList);
            System.out.println();
            System.out.printf("%23s"," ");
            printWeight(tfList);
            System.out.println();
        }
    }
    public void printTF(TFList tfList)
    {
        Vector<Double> array = new Vector<>();

        while (tfList != null) {
            array.add(tfList.tf);
            tfList = tfList.next;
        }

        for (int i = 0; i < array.size(); i++) {
            System.out.printf("%.4f",array.get(i) );
            if (array.size()- i != 1)
                System.out.print("  ");
        }
    }
    public void printWeight(TFList tfList)
    {
        Vector<Double> array = new Vector<>();

        while (tfList != null) {
            array.add(tfList.weight);
            tfList = tfList.next;
        }
        for (int i = 0; i < array.size(); i++) {
            System.out.printf("%.4f",array.get(i) );
            if (array.size()- i != 1)
                System.out.print("  ");
        }
    }
    public void printTFList(TFList tfList)
    {
        Vector<Integer> array = new Vector<>();

        while (tfList != null) {
            array.add(tfList.docId);
            tfList = tfList.next;
        }

        for (int i = 0; i < array.size(); i++) {
            System.out.print(array.get(i) );
            if (array.size()- i != 1)
                System.out.print("------>");
        }
    }
//    for (String document : documents){
//        String[] lengthofdoc = document.split("\\s+");
//        len.add(lengthofdoc.length);
//    }
    }