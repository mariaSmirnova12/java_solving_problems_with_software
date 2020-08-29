package dna;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class linkFinder {
    String strToSearch;

    public  linkFinder(String strToSearch){
        this.strToSearch = strToSearch;
    }

    public void findLink(String str){
        String searchStr = str.toLowerCase();
        int startInd = searchStr.indexOf(strToSearch);
        if(startInd != -1){
            int startLinkInd = searchStr.lastIndexOf("\"", startInd);
            if(startLinkInd != -1){
                int endLinkInd = searchStr.indexOf("\"", startLinkInd+1);
                if(endLinkInd != -1){
                    String res = str.substring(startLinkInd, endLinkInd+1);
                    System.out.println("res= "+res);
                }
            }
        }
    }

    public String getText(String url) throws Exception {
        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        connection.getInputStream()));

        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null){
            response.append(inputLine);
            findLink(inputLine);
        }
        in.close();

        return response.toString();
    }

    public static void main(String[] args) throws Exception {
        String url = ("https://www.dukelearntoprogram.com//course2/data/manylinks.html");
        String strToSearch = "youtube.com";
        linkFinder res = new linkFinder(strToSearch);
        res.getText(url);
    }

}
