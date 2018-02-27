/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IRCC;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author sbn
 */
public class UPDOGParser {

    public static void main(String[] args) {
        
        String dataSource = "IRCC";
        String fileLocation = "./data/";
        
        // will create a file <dataSource>.json in <fileLocation> dir
        boolean writeToFile = true;
        
        UPDOGParser parser = new UPDOGParser();

       System.out.println(parser.toJSON(dataSource, fileLocation, writeToFile));
       
    }

    private String toJSON(String source, String dir, boolean writeToFile) {
        String delimiter = "\t";
        StringBuilder sb = new StringBuilder();
        HashMap<String, String> sexMap = new HashMap();
        HashMap<String, ArrayList<String>> modelSpecimens = new HashMap();
        HashMap<String, Specimen> specimenMap = new HashMap();
        HashMap<String, ArrayList<String>> samplePlatforms = new HashMap();

        try {

            sb.append("{\"" + source + "\":[");
            BufferedReader buf = new BufferedReader(new FileReader(dir + "PT.txt"));
            String line = buf.readLine();

            while (line != null) {
                String[] parts = line.split(delimiter);
                sexMap.put(parts[1], parts[2]);
                line = buf.readLine();

            }
            buf.close();

            buf = new BufferedReader(new FileReader(dir + "MolChar.txt"));
            line = buf.readLine();

            while (line != null) {
                String[] parts = line.split(delimiter);
                //(String id, String modelId, String implantSite, String implantType, String strain, String passage) {
                Specimen specimen = new Specimen(parts[5], parts[1], parts[3], parts[4], parts[2], parts[6]);

                specimenMap.put(parts[5], specimen);

                if (modelSpecimens.containsKey(parts[1])) {
                    modelSpecimens.get(parts[1]).add(parts[5]);
                } else {
                    ArrayList<String> specimenIds = new ArrayList();
                    specimenIds.add(parts[5]);
                    modelSpecimens.put(parts[1], specimenIds);
                }

                line = buf.readLine();

            }
            buf.close();

            buf = new BufferedReader(new FileReader(dir + "IRCCPlatforms.txt"));
            line = buf.readLine();

            while (line != null) {
                String[] parts = line.split(delimiter);

                if (samplePlatforms.containsKey(parts[0])) {
                    samplePlatforms.get(parts[0]).add(parts[3]);
                } else {
                    ArrayList<String> platforms = new ArrayList();
                    platforms.add(parts[3]);
                    samplePlatforms.put(parts[0], platforms);
                }
                line = buf.readLine();
            }
            buf.close();

            buf = new BufferedReader(new FileReader(dir + "PTModel.txt"));
            line = buf.readLine();

            while (line != null) {
                String[] parts = line.split(delimiter);

                sb.append("{");
                sb.append("\"").append("Patient ID").append("\":\"").append(parts[1]).append("\",\n");

                sb.append("\"").append("Gender").append("\":\"").append(sexMap.get(parts[1])).append("\",\n");
                sb.append("\"").append("Age").append("\":\"").append(parts[3]).append("\",\n");
                sb.append("\"").append("Clinical Diagnosis").append("\":\"").append(parts[5]).append("\",\n");
                sb.append("\"").append("Stage").append("\":\"").append(parts[6]).append("\",\n");
                sb.append("\"").append("Primary Site").append("\":\"").append(parts[9]).append("\",\n");
                sb.append("\"").append("Sample Site").append("\":\"").append(parts[10]).append("\",\n");
                sb.append("\"").append("Tumor Type").append("\":\"").append(parts[11]).append("\",\n");
                sb.append("\"").append("Model ID").append("\":\"").append(parts[12]).append("\",\n");
                sb.append("\"").append("Treatment Naive").append("\":\"").append(parts[13]).append("\",\n");
                sb.append("\"").append("Treatment").append("\":\"").append(parts[14]).append("\",\n");
                
                // maybe this field should be validation with the value fingerprinting?
                sb.append("\"").append("Fingerprinting").append("\":\"").append(parts[20]).append("\",\n");
                
                sb.append("\"").append("QA Passage").append("\":\"").append(parts[21].replaceAll("\"", "").trim()).append("\",\n");

                // two trailing spaces to allow for the sb.replace to work if no specimens
                sb.append("\"Specimens\":[  ");

                for(String specimenId : modelSpecimens.get(parts[12])) {
                    Specimen specimen = specimenMap.get(specimenId);
                    sb.append("{");
                    sb.append("\"Specimen ID\":\"").append(specimen.getId()).append("\",");
                    sb.append("\"Engraftment Type\":\"").append(specimen.getImplantType()).append("\",");
                    sb.append("\"Engraftment Site\":\"").append(specimen.getImplantSite()).append("\",");
                    sb.append("\"Strain\":\"").append(specimen.getStrain()).append("\",");
                    sb.append("\"Passage\":\"").append(specimen.getPassage()).append("\",");

                    // one trailing space for the sb.replace to work if no platforms
                    sb.append("\"Platforms\":[ ");
                    for (String platform : samplePlatforms.get(specimen.getId())) {
                        sb.append("{\"Platform\":\"").append(platform).append("\"},");
                    }
                    sb.replace(sb.length() - 1, sb.length(), "]},\n");
                }

                sb.replace(sb.length() - 2, sb.length(), "],\n");

                sb.append("\"").append("Institution").append("\":\"").append(source).append("\"},\n");

                line = buf.readLine();
            }

            buf.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        sb.replace(sb.length() - 2, sb.length(), "]}");

     
        if(writeToFile){
            try{
                FileWriter fw = new FileWriter(dir+source+".json");
                fw.write(sb.toString());
                fw.close();
                System.out.println("Wrote JSON to "+dir+source+".json");
            }catch(IOException ioe){
                ioe.printStackTrace();
            }
        }
        return sb.toString();
    }
}
