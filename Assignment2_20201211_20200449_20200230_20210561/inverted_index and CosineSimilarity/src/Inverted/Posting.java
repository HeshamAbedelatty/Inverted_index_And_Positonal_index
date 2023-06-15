package Inverted;

public class Posting {

    public int docId;
    public int dtf = 1;
    public Posting next = null;

    public Posting() {
        this.docId = 0;
    }
}