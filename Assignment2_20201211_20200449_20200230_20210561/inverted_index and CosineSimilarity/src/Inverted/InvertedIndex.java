package Inverted;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;


public class InvertedIndex {


    private HashMap<String, DictEntry> Inverted_Index;
    List<String> stopWords = new ArrayList<>();
    HashMap <String,Integer> [] CosineMatrix;
   public List<Integer> len ;
    public InvertedIndex() {
        this.Inverted_Index = new HashMap<>();
        this.len =new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("stopwords.txt")));
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.split("\\s+");// hesham ahmed mohamed
                Collections.addAll(stopWords, words);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, DictEntry> getInverted_Index() {
        return Inverted_Index;
    }

    public static List<String> readDocuments(List<String> fileNames) {
        List<String> documents = new ArrayList<>();
        for (String fileName : fileNames) {
            String document = "";
            try {
                BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
                String line;
                while ((line = br.readLine()) != null) {
                    document += line + " ";
                }
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            documents.add(document);
        }
        return documents;
    }

    public  void buildInvertedIndex(List<String> documents) {
        int docId = 1;
        int i = 0 ;
        for (String document : documents) {
            String[] terms = document.split("\\W+");
            HashMap<String ,Integer> Hesham = new HashMap<>();
            for (String term : terms) {
                term = term.toLowerCase();
                if(stopWords.contains(term)) {
                    continue;
                }
                //DictEntry entry = Inverted_Index.getOrDefault(term, new DictEntry());
//                int Khalil = Hesham.get(term);
//                if (Khalil == 0)
//                {
//                    Hesham.put(term, 0);
//                }
//                else {
//                    Khalil ++;
////                    Hesham.get(term) = Khalil;
//                }
                DictEntry entry = Inverted_Index.get(term);
                if (entry == null) {
                    entry = new DictEntry();
                    Inverted_Index.put(term, entry);
                }
                entry.term_freq++;
                //handling posting list.
                // if term does not have posting list or term appear in another document
                //add a new post
                if (entry.pList == null || entry.pList.docId != docId) {
                    Posting posting = new Posting();
                    posting.docId = docId;
                    entry.doc_freq++;
                    posting.next = entry.pList;
                    entry.pList = posting;
                } else {
                    //the term appeared in same before document.
                    entry.pList.dtf++;
                }
            }
            docId++;
            i++;
        }

    }

    public static double[] computeCosineSimilarity(String query, Map<String, DictEntry> index, List<String> documents) {
        String[] queryTerms = query.split("\\W+");
        int numDocs = documents.size(); // N = 10
        int lenOfQuery = queryTerms.length;
        double[] scores = new double[numDocs];
        double[] queryVector = new double[lenOfQuery];
        double[] docVectors = new double[numDocs];
        Arrays.fill(docVectors, 0.0);
        for (int i = 0; i < lenOfQuery; i++) {
            String term = queryTerms[i].toLowerCase();
            if (index.containsKey(term)) {
                DictEntry entry = index.get(term);
                int df = entry.doc_freq;
                double idf = Math.log10(numDocs / (double) df);
                double queryWeight = (1 + Math.log10(1)) * idf;
                queryVector[i] = queryWeight;
                Posting p = entry.pList;
                while (p != null) {
                    int docId = p.docId;
                    int dtf = p.dtf;
                    double docWeight = (1 + Math.log10(dtf)) * idf;
                    docVectors[docId] += Math.pow(docWeight, 2);
                    scores[docId] += queryWeight * docWeight;
                    p = p.next;
                }
            }
        }
        for (int i = 0; i < numDocs; i++) {
            scores[i] /= Math.sqrt(docVectors[i]) * Math.sqrt(queryVector.length);
        }
        return scores;
    }

    public static List<Integer> rankFiles(double[] scores) {
        List<Integer> rankedFiles = new ArrayList<>();
        for (int i = 0; i < scores.length; i++) {
            rankedFiles.add(i);
        }
        Collections.sort(rankedFiles, new Comparator<Integer>() {
            public int compare(Integer i1, Integer i2) {
                return Double.compare(scores[i2], scores[i1]);
            }
        });
        return rankedFiles;
    }
    public void Display(){
        Posting posting ;
        TreeMap<String, DictEntry> Index = new TreeMap<String, DictEntry>(Inverted_Index);
        System.out.printf("%-15s","Term");
        System.out.printf("%-8s","DocFreq");
        System.out.printf("%-9s","TermFreq");
        System.out.printf("%-8s","PList");
        System.out.println();
        for (Map.Entry<String, DictEntry> invertedIndex : Index.entrySet()) {
            posting = invertedIndex.getValue().pList;
            System.out.printf("%-15s",invertedIndex.getKey());
            System.out.printf("%-8s",invertedIndex.getValue().doc_freq);
            System.out.printf("%-9s",invertedIndex.getValue().term_freq);
            printPostingList(posting);
            System.out.println();
            System.out.printf("%32s"," ");
            printDocTermFreq(posting);
            System.out.println();
        }
    }
    public void printDocTermFreq(Posting posting)
    {
        Vector<Integer> array = new Vector<>();

        while (posting != null) {
            array.add(posting.dtf);
            posting = posting.next;
        }
        Collections.reverse(array);
        for (int i = 0; i < array.size(); i++) {
            System.out.print(array.get(i) );
            if (array.size()- i != 1)
                System.out.print("   ");
        }
    }
    public void printPostingList(Posting posting)
    {
        Vector<Integer> array = new Vector<>();

        while (posting != null) {
            array.add(posting.docId);
            posting = posting.next;
        }
        Collections.reverse(array);
        for (int i = 0; i < array.size(); i++) {
            System.out.print(array.get(i) );
            if (array.size()- i != 1)
                System.out.print("-->");
        }
    }
    public void search(String query) {
        // Search for a word and list all files containing the word
        DictEntry dictEntry = Inverted_Index.get(query);
        if (dictEntry == null) {
            System.out.println("No documents found containing the term " + query);
        } else {
            System.out.println("Documents containing the term { " + query + " }:");
            System.out.printf("%-8s","DocFreq");
            System.out.printf("%-9s","TermFreq");
            System.out.printf("%-8s","PList");
            System.out.println();
            System.out.printf("%-8s",dictEntry.doc_freq);
            System.out.printf("%-9s",dictEntry.term_freq);
            Posting posting = dictEntry.pList;
            printPostingList(posting);
            System.out.println();
            System.out.printf("%17s"," ");
            printDocTermFreq(posting);
            System.out.println();
        }

    }
}
