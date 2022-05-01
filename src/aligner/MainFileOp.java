package aligner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainFileOp {
    public static ArrayList<Sequence> readFile(String filePath) {

        ArrayList<Sequence> fastaList = new ArrayList<>();

        try {
            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);
            String line = "";

            int index = -1;
            while((line = br.readLine()) != null) {

                if (line.startsWith(">")) {
                    index ++;
                    Sequence f = new Sequence(line, "");
                    fastaList.add(index, f);

                }else { //add sequence as strings to list
                    Sequence f = fastaList.get(index);
                    f.appendSequence(line);
                    fastaList.set(index, f);
                }
            }
            br.close();
        } catch (IOException fnfe) {
            fnfe.printStackTrace();
        }

        return fastaList;
    }

    public static HashMap<String, Integer> getDistance(Fasta query, Fasta reference) throws Exception {
       if(query.fastaList.size() != reference.fastaList.size()){
           throw new Exception("Genome and Reference file must have same number of sequences!");
        }

        HashMap<String, Integer> results = new HashMap<>();
       // Calculate  Distance
        for(int i = 0; i < query.fastaList.size(); i++) {
            String seqGen = query.fastaList.get(i).seq;
            String seqRef = reference.fastaList.get(i).seq;
            String key = query.fastaList.get(i).header + "[VS]" + reference.fastaList.get(i).header;

            int dist;
            if (seqGen.length() == seqRef.length()){
                dist = hammingDist(seqGen, seqRef);
            }

            else {
                dist = levenshteinDist(seqGen, seqRef);
            }
            results.put(key, dist);

        }
        return results;
    }



    public static int hammingDist(String query, String reference){
        int dist = 0;
        for(int i = 0; i < query.length(); i++){
            if(query.charAt(i) != reference.charAt(i)){
                dist += 1;
            }
        }
        return dist;
    }

    public static int levenshteinDist(String query, String reference){
        int [][] dist = new int[query.length()+1][reference.length()+1];
        System.out.println("Called L");
        if(query.length() == 0){
            return reference.length();
        }
        else if (reference.length() == 0){
            return  query.length();
        }

        else{
            for(int i = 0; i <= query.length(); i++){
                for(int j = 0; j <= reference.length(); j++){

                    if(i == 0){
                        dist[0][j] = j; // initialise row
                    }
                    else if (j == 0) {
                        dist[i][0] = i; // initialise column
                    }
                    else{
                        dist[i][j] = Math.min(
                                dist[i-1][j-1] + (query.charAt(i-1) == reference.charAt(j-1) ? 0 : 1),
                                Math.min(dist[i-1][j] + 1,
                                dist[i][j-1] +1)
                        );
                    }
                }
            }
        }

        return dist[query.length()][reference.length()];
    }

    public static void main(String[] args) throws Exception {

        Environment env = new Environment();
        env.frame.setVisible(true);

    }

}
