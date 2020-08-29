import dna.StrandOfDna;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FindingGenesInDnaTester {

    StrandOfDna strand;

    @Before
    public void setUp()
    {
        strand = new StrandOfDna();
    }

    public String loadDnaFromFile(String filename)  {
        FileInputStream stream = null;
        StringBuilder response = new StringBuilder();
        try {
            ClassLoader classLoader = this.getClass().getClassLoader();
            File fileName = new File(classLoader.getResource(filename).getFile());
            stream = new FileInputStream(fileName);
            BufferedReader reader = null;
            String nextLine;

            reader = new BufferedReader(new InputStreamReader(stream));
            while ((nextLine = reader.readLine()) != null) {
                response.append(nextLine);
            }
        }
        catch (IOException e) {
            System.err.println("Problem looking for dictionary file: " + filename);
            e.printStackTrace();
        }
        finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }

    @Test
    public void testbrca1line()
    {
        String dna = loadDnaFromFile("brca1test.fa");
        List<String> actual = Arrays.asList("atgccttaa", "atgcaaagagaggccaacatttga", "atgacaaagcagatttag");
        List <String> res = strand.findGenes(dna);
        assertEquals("Size of dna", 3, res.size());
        assertEquals(actual, res);
        for(String item: res){
            System.out.println("Gene is "+item);
        }
    }
}
