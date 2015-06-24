package br.ufg.inf.integracao.service;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Felipe on 23/06/2015.
 */
public class JSONFileService {
    public static void saveJSONObjectToFile(String destination, JSONObject json) throws IOException {


        SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String dataHora = f.format(new Date());

        String pasta = System.getProperty("user.dir") + File.separator + "siad" + File.separator;
        pasta = pasta.concat(destination + File.separator);

        FileWriter file = new FileWriter(pasta + dataHora + ".json");

        try {

            file.write(json.toString());

        } catch (IOException e) {

            throw e;

        } finally {
            file.flush();
            file.close();
        }

    }
}
