package CosineSimilarity;

import java.util.*;

public class  CosineSimilarity{

    static List<String> rate;
    static Map<Integer, Double> matrix = new HashMap<>();
    public CosineSimilarity() {

        rate = new ArrayList<>();
    }

    public  List<String > calculateCosineSimilarity(List<String >documents,String sentence1) {
        for (int i = 0; i <10 ; i++) {

            // Tokenize the sentences into individual words or terms
            String[] terms1 = sentence1.toLowerCase().split("\\W+");
            String[] terms2 = documents.get(i).toLowerCase().split("\\W+");

            // Compute term frequency vectors for each sentence
            Map<String, Integer> termFreq1 = computeTermFrequency(terms1);
            Map<String, Integer> termFreq2 = computeTermFrequency(terms2);

            // Calculate the dot product of the two term frequency vectors
            double dotProduct = calculateDotProduct(termFreq1, termFreq2);

            // Calculate the magnitude (Euclidean norm) of each term frequency vector
            double magnitude1 = calculateVectorMagnitude(termFreq1);
            double magnitude2 = calculateVectorMagnitude(termFreq2);

            // Compute the cosine similarity
            double res = dotProduct / (magnitude1 * magnitude2);
            // output sentence
            matrix.put(i,res);
        }
        List<Map.Entry<Integer, Double>> sortedEntries = new ArrayList<>(matrix.entrySet());
        sortedEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        for (Map.Entry<Integer, Double> sortedEntry : sortedEntries) {
            rate.add("Document " + (sortedEntry.getKey()+1) + " : (" + sortedEntry.getValue() + ")   CosineSimilarity");
        }
        return rate;
    }
    public  void printRate()
    {
        for (String s : rate) {
            System.out.println(s);
        }
    }

    private static Map<String, Integer> computeTermFrequency(String[] terms) {
        Map<String, Integer> termFreq = new HashMap<>();
        for (String term : terms) {
            termFreq.put(term, termFreq.getOrDefault(term, 0) + 1);
        }
        return termFreq;
    }

    private static double calculateDotProduct(Map<String, Integer> termFreq1, Map<String, Integer> termFreq2) {
        double dotProduct = 0.0;
        for (String term : termFreq1.keySet()) {
            if (termFreq2.containsKey(term)) {
                dotProduct += termFreq1.get(term) * termFreq2.get(term);
            }
        }
        return dotProduct;
    }

    private static double calculateVectorMagnitude(Map<String, Integer> termFreq) {
        double magnitude = 0.0;
        for (int frequency : termFreq.values()) {
            magnitude += Math.pow(frequency, 2);
        }
        return Math.sqrt(magnitude);
    }
}

