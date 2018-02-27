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
public class IRCCVariantParser {
    
public static void main(String[] args) {
        
       IRCCVariantParser ivp = new IRCCVariantParser();
       ivp.toJSON("IRCC","C:/IRCC/",true);
       
    }

    private String toJSON(String source, String dir, boolean writeToFile) {
        String delimiter = "\t";
        StringBuilder sb = new StringBuilder();
        HashMap<String,String> sampleMap = new HashMap();
        HashMap<String,String> sangerMap = new HashMap();
        try {

            sb.append("{\"" + source + "Variation\":[");
            BufferedReader buf = new BufferedReader(new FileReader(dir + "IRCCLookUp.txt"));
            String line = buf.readLine();

            while (line != null) {
                String[] parts = line.split(delimiter);
                sampleMap.put(parts[0], parts[3]);
                line = buf.readLine();

            }
            buf.close();
            
            buf = new BufferedReader(new FileReader(dir + "IRCCSangerMapping.txt"));
            line = buf.readLine();

            while (line != null) {
                String[] parts = line.split(delimiter);
                sangerMap.put(parts[4], parts[0]);
                System.out.println(parts[4]+" "+parts[0]);
                line = buf.readLine();

            }
            buf.close();

            buf = new BufferedReader(new FileReader(dir + "IRCCVariants.txt"));
            line = buf.readLine();
            //skip headers
            line = buf.readLine();
            while (line != null) {
                //Sample, Normal, Chrom, Pos, Ref, Alt, Gene, Transcript, RNA, CDS, Protein, Type, Effect, X1000g2015aug_all, ExAC_ALL, avsnp147, Driver, VAF
                String[] parts = line.split(delimiter);
                
                sb.append("{");
                sb.append("\"").append("Model ID").append("\":\"").append(sampleMap.get(parts[0])).append("\",\n");
                sb.append("\"").append("Specimen ID").append("\":\"").append(sangerMap.get(parts[0])).append("\",\n");
                sb.append("\"").append("Sample ID").append("\":\"").append(parts[0]).append("\",\n");
                sb.append("\"").append("Normal").append("\":\"").append(parts[1]).append("\",\n");
                sb.append("\"").append("Chrom").append("\":\"").append(parts[2]).append("\",\n");
                sb.append("\"").append("Pos").append("\":\"").append(parts[3]).append("\",\n");
                sb.append("\"").append("Ref").append("\":\"").append(parts[4]).append("\",\n");
                sb.append("\"").append("Alt").append("\":\"").append(parts[5]).append("\",\n");
                sb.append("\"").append("Gene").append("\":\"").append(parts[6]).append("\",\n");
                sb.append("\"").append("Transcript").append("\":\"").append(parts[7]).append("\",\n");
                sb.append("\"").append("RNA").append("\":\"").append(parts[8]).append("\",\n");
                sb.append("\"").append("CDS").append("\":\"").append(parts[9]).append("\",\n");
                sb.append("\"").append("Protein").append("\":\"").append(parts[10]).append("\",\n");
                sb.append("\"").append("Type").append("\":\"").append(parts[11]).append("\",\n");
                sb.append("\"").append("Effect").append("\":\"").append(parts[12]).append("\",\n");
                sb.append("\"").append("X1000g2015aug_all").append("\":\"").append(parts[13]).append("\",\n");
                sb.append("\"").append("ExAC_ALL").append("\":\"").append(parts[14]).append("\",\n");
                sb.append("\"").append("avsnp147").append("\":\"").append(parts[15]).append("\",\n");
                sb.append("\"").append("Driver").append("\":\"").append(parts[16]).append("\",\n");
                sb.append("\"").append("VAF").append("\":\"").append(parts[17]).append("\"},\n");


                line = buf.readLine();
            }

            buf.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        sb.replace(sb.length() - 2, sb.length(), "]}");

     
        if(writeToFile){
            try{
                FileWriter fw = new FileWriter(dir+source+"Variants.json");
                fw.write(sb.toString());
                fw.close();
                System.out.println("Wrote JSON to "+dir+source+"Variants.json");
            }catch(IOException ioe){
                ioe.printStackTrace();
            }
        }
        return sb.toString();
    }
}
