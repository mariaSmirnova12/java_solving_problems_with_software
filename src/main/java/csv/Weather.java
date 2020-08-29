package csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class Weather {

    public File resFile;

    public CSVRecord minimumInFile(CSVParser parser){
        CSVRecord coldTemperatureRow = null;
        for(CSVRecord record: parser){
            coldTemperatureRow = findMinRow(coldTemperatureRow, record);
        }
        return coldTemperatureRow;
    }
    
    public CSVRecord lowestHumidityInFile(CSVParser parser){
        CSVRecord lowestHumidityRow = null;
        for(CSVRecord record: parser){
            lowestHumidityRow = findMinRowHumidity(lowestHumidityRow, record);
        }
        return lowestHumidityRow;
    }
    
    public void testOneCSVFile(CSVParser parser) {
        CSVRecord coldTemperatureRow = minimumInFile(parser);
        System.out.println("coldest t: at "+coldTemperatureRow.get("TimeEST")+" "+coldTemperatureRow.get("TemperatureF"));
        
    }

    public void testTempOneAverageCSVFile(CSVParser parser){
        double res = getAverageTemperatureInFile(parser);
        System.out.println(res);
    }

    public double averageTemperatureWithHighHumidityInFile(CSVParser parser, double value){

       double res = 0;
       int i = 0;
       for(CSVRecord record: parser){
           if(!record.get("Humidity").equals("N/A") && Double.parseDouble(record.get("Humidity"))>= value ){
             double currentTemp = Double.parseDouble(record.get("TemperatureF"));
             res+=currentTemp;
             i++;
           }
       }
       return res/(double)i;
   }


     public void testAverageTemperatureWithHighHumidityInFile(CSVParser parser){
         double res = averageTemperatureWithHighHumidityInFile(parser, 80);
         System.out.println(res);
     }

    public CSVRecord findMinRow( CSVRecord coldTemperatureRow, CSVRecord tmpRow){
       return findMinRow(coldTemperatureRow, tmpRow, null);
    }
    public CSVRecord findMinRow( CSVRecord coldTemperatureRow, CSVRecord tmpRow, File f){
        if(coldTemperatureRow == null){
                coldTemperatureRow = tmpRow;
                resFile = f;
        }
        else{
             double currentTemp = Double.parseDouble(tmpRow.get("TemperatureF"));
             double coldestTemp = Double.parseDouble(coldTemperatureRow.get("TemperatureF"));
             if((currentTemp < coldestTemp) && currentTemp!=-9999){
                   coldTemperatureRow = tmpRow;
                   resFile = f;
             }
        }
        return coldTemperatureRow;
    }
    public CSVRecord findMinRowHumidity(CSVRecord coldHumidityRow, CSVRecord tmpRow){
        //System.out.println("Humidity = " + tmpRow.get("Humidity")+" "+ tmpRow.get("DateUTC"));
        if(coldHumidityRow == null){
                return tmpRow;
        }
        else{
            if(tmpRow.get("Humidity").equals("N/A")){
                return coldHumidityRow;
            }
             if(coldHumidityRow.get("Humidity").equals("N/A")){
                return tmpRow;
            }
             double currentTemp = Double.parseDouble(tmpRow.get("Humidity"));
             double coldestTemp = Double.parseDouble(coldHumidityRow.get("Humidity"));
             if(currentTemp < coldestTemp){
                   coldHumidityRow = tmpRow;
             }
        }
         return coldHumidityRow;
    }
    public double getAverageTemperatureInFile(CSVParser parser){
        double res = 0;
        int i = 0;
        for(CSVRecord record: parser){
            double currentTemp = Double.parseDouble(record.get("TemperatureF"));
            res+=currentTemp;
            i++;
        }
        return res/(double)i;
    }

    public String getMinTemperatureMultiCSVFile(String directoryName) throws IOException {

        List<File> filesInFolder = getAllCSVFromDirectory(directoryName);
        CSVRecord coldTemperatureRow = null;
        for(File file: filesInFolder){
            System.out.println("fileName = " + file.getName());
            CSVParser parser = null;
            try {
                 parser = CSVParser.parse(file, Charset.defaultCharset(), CSVFormat.EXCEL
                        .withHeader("TimeEST", "TemperatureF", "Dew PointF", "Humidity", "Sea Level PressureIn", "VisibilityMPH", "Wind Direction", "Wind SpeedMPH", "Gust SpeedMPH", "PrecipitationIn", "Events", "Conditions", "WindDirDegrees", "DateUTC")
                        .withSkipHeaderRecord()
                        .withTrim());
            } catch (IOException e) {
                System.out.println("error in reading csv file ");
                e.printStackTrace();
            }
            CSVRecord coldTemperatureRowOneFile = minimumInFile(parser);
            coldTemperatureRow = findMinRow(coldTemperatureRow, coldTemperatureRowOneFile, file);
        }

        System.out.println("coldest t in all files:  "+coldTemperatureRow.get("TemperatureF"));
        return coldTemperatureRow.get("TemperatureF");
     }

    public List<File> getAllCSVFromDirectory(String directoryName) throws IOException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        File fileName = new File(classLoader.getResource(directoryName).getFile());

        List<File> filesInFolder = Files.walk(fileName.toPath())
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList());
        return filesInFolder;
    }
    public String getMinHumidityMultiCSVFile(String directoryName) throws IOException {

        List<File> filesInFolder = getAllCSVFromDirectory(directoryName);
        CSVRecord coldHumidityRow = null;
        System.out.println("multifiles:");
        for(File file: filesInFolder){
            System.out.println("fileName = " + file.getName());
            CSVParser parser = null;
            try {
                parser = CSVParser.parse(file, Charset.defaultCharset(), CSVFormat.EXCEL
                        .withHeader("TimeEST", "TemperatureF", "Dew PointF", "Humidity", "Sea Level PressureIn", "VisibilityMPH", "Wind Direction", "Wind SpeedMPH", "Gust SpeedMPH", "PrecipitationIn", "Events", "Conditions", "WindDirDegrees", "DateUTC")
                        .withSkipHeaderRecord()
                        .withTrim());
            } catch (IOException e) {
                System.out.println("error in reading csv file ");
                e.printStackTrace();
            }

            CSVRecord coldHumidityRowOneFile = lowestHumidityInFile(parser);
            System.out.println("coldest t: at "+" "+coldHumidityRowOneFile.get("Humidity"));
            coldHumidityRow = findMinRowHumidity(coldHumidityRow, coldHumidityRowOneFile);
        }
        System.out.println("coldest t in all files: at "+" "+coldHumidityRow.get("Humidity")+" "+coldHumidityRow.get("DateUTC"));
    return coldHumidityRow.get("Humidity");
    }

    public CSVParser readCSVFile(String name) throws IOException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        File fileName = new File(classLoader.getResource(name).getFile());
        Reader reader = Files.newBufferedReader(fileName.toPath());
        CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL
                .withHeader("TimeEST", "TemperatureF", "Dew PointF", "Humidity", "Sea Level PressureIn", "VisibilityMPH", "Wind Direction", "Wind SpeedMPH", "Gust SpeedMPH", "PrecipitationIn", "Events", "Conditions", "WindDirDegrees", "DateUTC")
                .withSkipHeaderRecord()
                .withTrim());
        return parser;
    }
    public static void main(String[] args) throws IOException {
        Weather weather = new Weather();
        //weather.testOneCSVFile(export.readCSVFile() );

    }
}
