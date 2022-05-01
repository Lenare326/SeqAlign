package aligner;

import java.util.ArrayList;

public class Fasta {
    ArrayList<Sequence> fastaList;

    public Fasta(){
        this.fastaList = new ArrayList<>();
    }

    // Methods
    public int getLength() {
        return this.fastaList.size();
    }

}
