package dna;

import java.util.ArrayList;
import java.util.List;

public class StrandOfDna {
    List<String> genes;
    String[] stopCodons;
    String startCodon;

    public StrandOfDna(){
        genes = new ArrayList<String>();
        startCodon = "atg";
        stopCodons = new String[]{"taa", "tga", "tag"};
    }

    // return gene from a string of DNA (return substring between the start codon “ATG” and first stop codon “TAA” if it is a multiple of 3) else return empty string
    public String findSimpleGene(String dna, String startCodon, String stopCodon){
        char ch = dna.charAt(0);
        boolean hasLowercase = false;
        if(Character.isLowerCase(ch)){
            hasLowercase = true;
        }
        dna = dna.toUpperCase();
        int startInd = dna.indexOf(startCodon);
        System.out.println("startInd= "+startInd);
        if(startInd != -1){
            int endInd = dna.indexOf(stopCodon, startInd+3);
            System.out.println("endInd= "+endInd);
            if(endInd != -1){
                String res = dna.substring(startInd, endInd+3);
                if(res.length()%3==0){
                    if(hasLowercase){
                        res = res.toLowerCase();
                    }
                    return res;
                }

            }
        }
        return "";
    }

    public int findEndOfGene(String dna, int ind, String substr){

        int endInd = dna.indexOf(substr, ind);
        while(endInd != -1){
            if((endInd - ind)%3==0){
                return endInd;
            }
            endInd = dna.indexOf(substr, endInd+1);
        }
        return dna.length();
    }
    public int findStopCodon(String dna, int startInd){
        int endInd1 = findEndOfGene(dna,startInd+3, stopCodons[0]);
        int endInd2 = findEndOfGene(dna,startInd+3, stopCodons[1]);
        int endInd3 = findEndOfGene(dna,startInd+3, stopCodons[2]);
        endInd1 = Math.min(endInd1, Math.min(endInd2, endInd3));
        return endInd1;
    }
    public double findCGRatio(String dna){
        double res = 0;
        int count = 0;
        for (int i = 0; i < dna.length(); i++) {
            if (dna.charAt(i) == 'c' || dna.charAt(i) == 'g') {
                count++;
            }
        }
        return (double)count/dna.length();
    }

    public List<String> findGenes(String dna){
        char ch = dna.charAt(0);
        boolean hasUppercase = false;
        if(Character.isUpperCase(ch)){
            hasUppercase = true;
        }
        dna = dna.toLowerCase();

        System.out.println("start ");
        System.out.println("------------------------------------------");
        int startInd = dna.indexOf(startCodon);
       // System.out.println("startInd= "+startInd);
        while(startInd != -1 ){
            int endInd1 = findStopCodon(dna, startInd);
            if(endInd1 == dna.length()){
                String res = dna.substring(startInd, endInd1);
                addGene(res, hasUppercase);
                break;
            }
            String res = dna.substring(startInd, endInd1+3);
            addGene(res, hasUppercase);
            startInd = dna.indexOf(startCodon, endInd1+3);
         //   System.out.println("startInd= "+startInd);
        }
        return genes;
    }
    public void addGene(String gene, boolean hasUppercase){
        if(hasUppercase){
            gene = gene.toUpperCase();
        }
        genes.add(gene);
    }
    public int manyOccurrences(String stra, String strb){
        int count = 0;
        int startInd = strb.indexOf(stra);
        while(startInd != -1){
            count++;
            startInd = strb.indexOf(stra, startInd+stra.length());
        }
        return count;
    }
    public static void main(String[] args) {
        String str = "ATGTAAGATGCCCTAGT";
        //String str = "AATGCTAGGGTAATATGGT";
        StrandOfDna strand = new StrandOfDna();
        List <String> res = strand.findGenes(str);
        for(String item: res){
            System.out.println("Gene is "+item);
        }
        //part3.testLastPart();
        //part3.testSimpleGene();

        //String str = "SADFATGLKJRTYTTAYJYY";
        //String str = "AATGCTAGGGTAATATGGT";
        //  String str = "ATGGGTTAAGTC";
        //String str = "gatgctataat";
        //System.out.println("hello");
      //  String res = findSimpleGene(str, "ATG", "TAA");
       // System.out.println("Gen str is "+res);
    }
}
