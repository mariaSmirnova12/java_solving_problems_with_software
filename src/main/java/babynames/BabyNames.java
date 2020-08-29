package babynames;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class BabyNames {

    private List<File> getFilesFromDirectory(String directoryName) throws IOException{
        ClassLoader classLoader = this.getClass().getClassLoader();
        File fileName = new File(classLoader.getResource(directoryName).getFile());

        List<File> filesInFolder = Files.walk(fileName.toPath())
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList());
        return filesInFolder;
    }
    private CSVParser createParser(File file){
        CSVParser parser = null;
        try {
            parser = CSVParser.parse(file, Charset.defaultCharset(), CSVFormat.EXCEL
                    .withTrim());
        } catch (IOException e) {
            System.out.println("error in reading csv file ");
            e.printStackTrace();
        }
        return parser;
    }
    public void readMultiCSVFile(String directoryName) throws IOException {

        List<File> filesInFolder = getFilesFromDirectory(directoryName);
        for(File file: filesInFolder){
            System.out.println("fileName = " + file.getName());
            CSVParser parser = createParser(file);
            totalBirth(parser);
        }
    }
    
    public int getRank(int year, String name, String sex){
        String fileName = "us_babynames/us_babynames_by_year/yob"+year+".csv";
        int countRank = 0;
        ClassLoader classLoader = this.getClass().getClassLoader();
        CSVParser parser = createParser(new File(classLoader.getResource(fileName).getFile()));
        for(CSVRecord record: parser){
            if(sex.equals(record.get(1))){
                countRank++;
                if(name.equals(record.get(0))){
                    return countRank;
                }
            }
        }
        return -1;
    }
    
    public int getRank(int year, String name, String sex, CSVParser parser){
        int countRank = 0;
        for(CSVRecord record: parser){
            if(sex.equals(record.get(1))){
                countRank++;
                if(name.equals(record.get(0))){
                    return countRank;
                }
            }
        }
        return -1;
    }
    
    public void testGetRank(){
        int rank = getRank(1960, "Emily", "F");
        System.out.println("rank "+rank);
        rank = getRank(1971, "Frank", "M");
        System.out.println("rank "+rank);
    }
    
    public void testGetNametoRank(){
        String name = getNametoRank(1980, 350, "F");
        System.out.println("name "+name);
        name = getNametoRank(1982, 450, "M");
        System.out.println("name "+name);
    }
    
    public String getNametoRank(int year, int rank, String sex){
        int currentRank = 0;
        ClassLoader classLoader = this.getClass().getClassLoader();
        CSVParser parser = createParser(new File(classLoader.getResource("us_babynames/us_babynames_by_year/yob"+year+".csv").getFile()));
        for(CSVRecord record: parser){
            if(sex.equals(record.get(1))){
                currentRank++;
                if(currentRank == rank){
                    return record.get(0);
                }
            }
        }
        return "NO NAME";
    }
    
    public String whatIsNameInYear(String name, int year, int newYear, String gender){
        int rank = getRank(year, name, gender);
        System.out.println("rank "+rank);
        String nameNew = getNametoRank(newYear, rank, gender);
        System.out.println("name "+nameNew);
        System.out.println(name+" born in "+year+ " would be " +nameNew+ " in "+newYear);
        return nameNew;
    }
    
    public void testWhatIsNameInYear(){
        String name = whatIsNameInYear("Susan", 1972, 2014, "F");
        name = whatIsNameInYear("Owen", 1974, 2014, "M");
    }
    
    public String yearOfHighestRank(String directoryName, String name, String gender) throws IOException {
        int highestRank = -1;
        int currentRank = 0;
        String year = "";
        List<File> filesInFolder = getFilesFromDirectory(directoryName);
        for(File file: filesInFolder){
            System.out.println("fileName = " + file.getName());
            CSVParser parser = createParser(file);
            currentRank = getRank(Integer.parseInt(file.getName().substring(3, 7)), name, gender, parser);
            if(highestRank == -1 || currentRank < highestRank){
                highestRank = currentRank;
                year = file.getName().substring(3, 7);
                System.out.println("highestRank = "+ highestRank+"year = " + file.getName().substring(3, 7));
            }
        }
        if(highestRank == -1){
            System.out.println("in files there is no such name ");
        }
        return year;
    }
    public double getAverageRank(String directoryName, String name, String gender) throws IOException {
        double averageRank = 0;
        int amount = 0;

        List<File> filesInFolder = getFilesFromDirectory(directoryName);
        for(File file: filesInFolder){
            System.out.println("fileName = " + file.getName());
            CSVParser parser = createParser(file);
            averageRank += (double) getRank(Integer.parseInt(file.getName().substring(3, 7)), name, gender, parser);
            amount++;
        }
        averageRank = averageRank/amount;
        if(averageRank == -1){
            System.out.println("in files there is no such name ");
        }
        System.out.println(averageRank);
        return averageRank;
    }
    public void testAverageRank(String directoryName) throws IOException {
        double res = getAverageRank(directoryName, "Robert", "M");
        System.out.println(res);
    }
    
    public void testgetTotalBirthsRankedHigher(){
        int res = getTotalBirthsRankedHigher(1990, "Drew", "M");
        System.out.println("name "+res);
    }
    
    public int getTotalBirthsRankedHigher(int year, String name, String gender){
        int birthAmount = 0;
        int rank = getRank(year, name, gender);
        System.out.println("rank "+rank);
        String fileName = "us_babynames/us_babynames_by_year/yob"+year+".csv";
        ClassLoader classLoader = this.getClass().getClassLoader();
        CSVParser parser = createParser(new File(classLoader.getResource(fileName).getFile()));

        int i = 0;
        for(CSVRecord record: parser){
            if(record.get(1).equals(gender)){
                i++;
                if(i<rank){
                    System.out.println("i= "+i+" rank="+rank);
                    birthAmount += Integer.parseInt(record.get(2));
                }
                else{
                    break;
                }
            }
        }
        System.out.println("birthAmount =  "+birthAmount);
        return birthAmount;
    }
    
    public void totalBirth(CSVParser parser){
        int amountBoys = 0;
        int amountBoysNames= 0;
        int amountGirls = 0;
        int amountGirlsNames = 0;
        for(CSVRecord record: parser){
            if(record.get(1).equals("F")){
                amountGirls += Integer.parseInt(record.get(2));
                amountGirlsNames++;
            }
            else{
                amountBoys += Integer.parseInt(record.get(2));
                amountBoysNames++;
            }
        }
        System.out.println("boys "+amountBoys+ " totalNames "+amountBoysNames);
        System.out.println("girls "+amountGirls+" totalNames "+amountGirlsNames);
        System.out.println("total "+(amountBoys+amountGirls)+" totalNames "+(amountBoysNames+amountGirlsNames));
    }

    public static void main(String[] args) throws IOException {
        BabyNames babyNames = new BabyNames();
        double res = babyNames.getAverageRank("us_babynames/us_babynames_by_year", "Mason", "M");
    }
    
}
