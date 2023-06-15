package TFIDFMatrix;

class TFList {

    public int docId;
    public double tf = 0.0;
    public double weight = 0.0;
    TFList next = null;

    public TFList() {
        this.docId = 0;
    }
}