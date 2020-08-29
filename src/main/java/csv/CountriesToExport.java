package csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class CountriesToExport {

    CSVParser parser;

    public CountriesToExport(){
        try {
            parser = readCSVFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<String> listExporters( String search){
        List<String> exporters = new ArrayList<String>();
        for(CSVRecord record: parser){
            String export = record.get("Exports");
            if(export.contains(search)){
                exporters.add(record.get("Country"));
                 System.out.println(record.get("Country"));
            }
        }
        return exporters;
    }
    
    public String countryInfo( String country){
        for(CSVRecord record: parser){
            String str = record.get("Country");
            if(str.equals(country)){
                 return record.get("Country")+": "+record.get("Exports")+": "+record.get("Value (dollars)");
            }
        }
        return "NOT FOUND";
    }

    public List<String> listExportersTwoProducts( String exportItem1, String exportItem2){
        List<String> exporters = new ArrayList<String>();
        for(CSVRecord record: parser){
            String export = record.get("Exports");
            if(export.contains(exportItem1) && export.contains(exportItem2)){
                 exporters.add(record.get("Country"));
                 System.out.println(record.get("Country"));
            }
        }
        return exporters;
    }
    
    public int numberOfExporters( String exportItem){
        int res = 0;
        for(CSVRecord record: parser){
            String export = record.get("Exports");
            if(export.contains(exportItem)){
                 res++;
                System.out.println(record.get("Country")+" "+record.get("Exports"));
            }
        }
        return res;
    }
    
    public List<String> bigExporters(String amount){
        List<String> exporters = new ArrayList<String>();
        int length = amount.length();
         for(CSVRecord record: parser){
            String money = record.get("Value (dollars)");
            if(money.length()>length){
                exporters.add(record.get("Country"));
                 System.out.println(record.get("Country")+" "+money);
                 
            }
        }
         return exporters;
    }
    public CSVParser readCSVFile() throws IOException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        File fileName = new File(classLoader.getResource("exportdata.csv").getFile());
        Reader reader = Files.newBufferedReader(fileName.toPath());
        CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL
                .withHeader("Country", "Exports", "Value (dollars)")
                .withTrim());
        return parser;
    }

    public static void main(String[] args) throws IOException {
        CountriesToExport export = new CountriesToExport();
        export.bigExporters("$999,999,999,999");
    }
}
