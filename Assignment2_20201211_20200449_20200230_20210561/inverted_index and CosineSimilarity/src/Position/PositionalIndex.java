package Position;

import java.util.*;
public class PositionalIndex {
    private  Map<String, Map<Integer, List<Integer>>> positionalIndex;
    public PositionalIndex() {
        positionalIndex = new HashMap<>();
    }
    public void build_position_index(List<String> documents) {
        int documentId = 1;
        for (String document : documents) {// loop in each doc in docs
            String[] words = document.toLowerCase().split("\\W+");     // split evrey doc into strings
            int position = 0;
            for (String word : words) {
                if (!positionalIndex.containsKey(word)) {
                    positionalIndex.put(word, new HashMap<>());
                }
//                String filename = "document "+ position;
                if (!positionalIndex.get(word).containsKey(documentId)) {
                    positionalIndex.get(word).put(documentId , new ArrayList<>());
                }

                positionalIndex.get(word).get(documentId).add(position);
                position++;
            }
            documentId++;
        }
    }
    //    public List<Integer> search(String term, int  documentId) {
//        term = term.toLowerCase();
//        if (index.containsKey(term)) {
//            Map<Integer, List<Integer>> documentPositions = index.get(term);
//            if (documentPositions.containsKey(documentId)) {
//                return documentPositions.get(documentId);
//            }
//        }
//        return Collections.emptyList();
//    }

    public List<List<Integer>> searchPhrase(String phrase){
        List<Integer> result = new ArrayList<>();
        List<Integer> docs = new ArrayList<>();
        List<List<Integer>> Final = new ArrayList<>();
        String[] words = phrase.toLowerCase().split("\\W+");

        if (words.length == 0) {
            Final.add(result);
            Final.add(docs);
        }

        if (!positionalIndex.containsKey(words[0])) {
            Final.add(result);
            Final.add(docs);
        }

        Map<Integer, List<Integer>> firstWordIndex = positionalIndex.get(words[0]);

        for (Integer filename : firstWordIndex.keySet()) {
            List<Integer> positions = firstWordIndex.get(filename);

            for (Integer pos : positions) {
                boolean found = true;

                for (int i = 1; i < words.length; i++) {
                    int nextPos = pos + i;

                    if (!positionalIndex.containsKey(words[i])) {
                        found = false;
                        break;
                    }

                    Map<Integer, List<Integer>> wordIndex = positionalIndex.get(words[i]);

                    if (!wordIndex.containsKey(filename) || !wordIndex.get(filename).contains(nextPos)) {
                        found = false;
                        break;
                    }
                }

                if (found) {
                    result.add(pos);
                    docs.add(filename);
                }
            }
        }

        Final.add(result);
        Final.add(docs);
        return Final;
    }
}
//    public static void main(String[] args) {
//        PositionalIndex index = new PositionalIndex();
//
// Adding documents to the index
//        index.build_position_index("doc1", "apple banana apple");
//        index.build_position_index("doc2", "banana orange  apple banana apple");
//
//        // Searching for a term in a document
//        List<Integer> positions = index.search("apple", "doc1");
//        System.out.println("Positions of 'apple' in 'doc1': " + positions);
//
//        // Searching for a term in a non-existent document
//        positions = index.search("banana", "doc3");
//        System.out.println("Positions of 'banana' in 'doc3': " + positions);
//
//        // Searching for a phrase in a document
//        String[] phrase = {"apple", "banana"};
//        List<List<Integer>> phrasePositions = index.searchPhrase(phrase, "doc1");
//        System.out.println("Positions of phrase 'apple banana' in 'doc1': " + phrasePositions);
//    }