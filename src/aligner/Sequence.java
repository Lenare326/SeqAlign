package aligner;

public class Sequence {
    String header;
    String seq;

    // Constructor
    public Sequence(String header, String seq){
        this.header = header;
        this.seq =seq;
    }

    // Methods
    public void appendSequence(String sequence) {
        this.seq += sequence;
    }

}
