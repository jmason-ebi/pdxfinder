package org.pdxfinder.commands;


import org.pdxfinder.accessionidtatamodel.AccessionData;
import org.pdxfinder.dao.Marker;
import org.pdxfinder.utilities.LoaderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;


/**
 * Created by csaba on 30/06/2017.
 */
@Component
public class LoadAccessionIDs implements CommandLineRunner {

    private static final String HGNC_URL = "http://rest.genenames.org/fetch/symbol/";
    private static final String ENSEMBL_URL = "http://rest.ensembl.org/xrefs/symbol/homo_sapiens/BRAF?content-type=application/json";
    private static final String DATA_FILE_URL = "http://www.genenames.org/cgi-bin/download?col=gd_hgnc_id&col=gd_app_sym&col=gd_prev_sym&col=gd_aliases&col=gd_pub_ensembl_id&status=Approved&status_opt=2&where=&order_by=gd_app_sym_sort&format=text&limit=&hgnc_dbtag=on&submit=submit";


    private final static Logger log = LoggerFactory.getLogger(LoadAccessionIDs.class);
    private LoaderUtils loaderUtils;

    @Autowired
    public LoadAccessionIDs(LoaderUtils loaderUtils) {
        this.loaderUtils = loaderUtils;
    }



    @Override
    @Transactional
    public void run(String... args) throws Exception {

        log.info(args[0]);

        if ("loadAccessionIds".equals(args[0]) || "-loadAccessionIds".equals(args[0])) {


            long startTime = System.currentTimeMillis();
            loadAccessionIds();
            long endTime   = System.currentTimeMillis();
            long totalTime = endTime - startTime;

            int seconds = (int) (totalTime / 1000) % 60 ;
            int minutes = (int) ((totalTime / (1000*60)) % 60);

            System.out.println("Loading finished after "+minutes+" minute(s) and "+seconds+" second(s)");
        }
        else{
            log.info("Missing command");
        }

    }


    private void loadAccessionIds(){

        String[] rowData;
        String[] prevSymbols;
        HashMap<String, AccessionData> hmap = new HashMap<>();
        String symbol, hgncId, ensemblId = "";

        int rows = 0;
        int symbolConflicts = 0;
        String symbolConf = "";

        try{

            URL url = new URL(DATA_FILE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            //conn.addRequestProperty("Enctype","application/x-www-form-urlencoded");

            if(conn.getResponseCode() != 200){
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String line = reader.readLine();
                while((line = reader.readLine()) != null){
                    //HGNC_ID APPR_SYMBOL PREV_SYMBOLS SYNONYMS ENSEMBL_ID

                    rowData = line.split("\t");
                    //rowData[1] = symbol, if it is not empty and if it is not a withdrawn symbol
                    if(rowData[1] != null && !rowData[1].isEmpty() ){

                        symbol = rowData[1];

                        if(rowData[0] != null && !rowData[0].isEmpty()){
                            hgncId = rowData[0];
                        }
                        else{
                            hgncId = "";
                        }

                        if(rowData.length>4 && rowData[4] != null && !rowData[4].isEmpty()){
                            ensemblId = rowData[4];
                        }
                        else{
                            ensemblId = "";
                        }

                        //put it in the hashmap with all of its prev symbols

                        AccessionData ad = new AccessionData(symbol, hgncId, ensemblId);
                        System.out.println(symbol+" "+hgncId+" "+ ensemblId);
                        hmap.put(symbol,ad);

                        if(rowData.length>2 && !rowData[2].isEmpty()){
                            prevSymbols = rowData[2].split(",");

                                for(int i=0;i<prevSymbols.length;i++){
                                    if(hmap.containsKey(prevSymbols[i].trim())){
                                        symbolConflicts++;
                                        symbolConf = prevSymbols[i];
                                        //break;
                                    }
                                    else{
                                        hmap.put(prevSymbols[i].trim(),ad);
                                    }
                                }
                        }

                    }
                    rows++;
                }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("Symbol conflicts: "+symbolConflicts);
        System.out.println("Last conflict:"+symbolConf);
        System.out.println("Rows: "+rows);
        System.out.println("Symbols incl. prev symbols: "+hmap.size());

        updateMarkers(hmap);
    }

private void updateMarkers(HashMap hmap){

    int notUpdatedMarkers = 0;
    System.out.println("Loading all markers from Neo4j...");
    Collection<Marker> markers = loaderUtils.getAllMarkers();

    for(Marker m:markers){

        if(hmap.containsKey(m.getSymbol())){
            AccessionData ad = (AccessionData) hmap.get(m.getSymbol());
            m.setHugoId(ad.getHgncId());
            m.setEnsemblId(ad.getEnsemblId());
            System.out.println("Updating marker:"+m.getSymbol());
            loaderUtils.saveMarker(m);
        }
        else{
            notUpdatedMarkers++;
        }

    }

    System.out.println("Not updated markers: "+notUpdatedMarkers);

}



}
