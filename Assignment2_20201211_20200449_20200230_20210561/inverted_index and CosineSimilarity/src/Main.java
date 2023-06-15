import Inverted.InvertedIndex;
import CosineSimilarity.CosineSimilarity;
import CosineSimilarity.WebCrawler;
import Position.PositionalIndex;
import TFIDFMatrix.TFIdf;

import java.util.*;

import static Inverted.InvertedIndex.*;

public class Main {


    public static void main(String[] args) {

        // Read 10 text files
        List<String> fileNames = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            fileNames.add("document" + i + ".txt");
        }
        // List of documents as Strings
        List<String> documents = readDocuments(fileNames);

        // Build inverted index
        InvertedIndex invertedIndex = new InvertedIndex();
        invertedIndex.buildInvertedIndex(documents);

        // Build TF IDF Matrix
        TFIdf tfIdf = new TFIdf();
        tfIdf.buildMatrix(invertedIndex.getInverted_Index());

        // Build Positional Index
        PositionalIndex positionalIndex = new PositionalIndex();
        positionalIndex.build_position_index(documents);

        // Build CosineSimilarity Matrix
        CosineSimilarity cosineSimilarity = new CosineSimilarity();

        // create web crawler
        WebCrawler wc = new WebCrawler();

        // start program
        int n;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter 1=> display inverted index ");
            System.out.println("Enter 2=> Search for term ");
            System.out.println("Enter 3=> display Tf IDF Matrix ");
            System.out.println("Enter 4=> cosine similarity for query ");
            System.out.println("Enter 5=> search with phrase ");
            System.out.println("Enter 6=> Web Crawler ");
            System.out.println("Enter 7=> Exit ");
            System.out.print("Enter number:");
            n = scanner.nextInt();
            if (n == 1) {
                invertedIndex.Display();
                System.out.println();
            } else if (n == 2) {
                String word;
                System.out.print("Enter word: ");
                word = scanner.next();
                invertedIndex.search(word);
                System.out.println();
            } else if (n == 3) {
                tfIdf.Display();
                System.out.println();
            } else if (n == 4) {
                System.out.println("C++ (said C plus plus) is an object-oriented computer language created by notable computer: ");
                System.out.print("Enter Query like this: ");
                String query = scanner.next();
                // Rank files according to cosine similarity
                // Compute cosine similarity
                cosineSimilarity.calculateCosineSimilarity(documents, query);
                // print result
                cosineSimilarity.printRate();
                System.out.println();
            } else if (n ==5) {
                String phraseToSearch ;
                System.out.print("Enter phrase: ");
                scanner.skip("\\s*");
                phraseToSearch = scanner.nextLine();
                List<List<Integer>> searchResults = positionalIndex.searchPhrase(phraseToSearch);
                List<Integer> docs = searchResults.get(1);
                System.out.println("Search phrase: " + phraseToSearch);
                System.out.println("Search results:");
                int i = 0;
                for (Integer position : searchResults.get(0)) {
                    System.out.println("Document "+docs.get(i)+ " : ");
                    System.out.println("Position: " + position);
                    i++;
                }
                System.out.println();
            } else if (n == 6) {
                System.out.print("Enter Crawler URL like this: ");
                System.out.println("https://www.example.com");
                System.out.print("Enter URL: ");
                String url= scanner.next();

                wc.getPageLinks(url);
                System.out.println();
            } else {
                System.out.println("Thank you TA: Doaa");
            }
        }
    }
}