package br.edu.humanitary;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Andrey on 04/11/15.
 */
public class ConnectionUtils extends AsyncTask<Object, Void, StringBuffer> {
    @Override
    protected StringBuffer doInBackground(Object... params) {
        StringBuffer chaine = new StringBuffer("");
        String urlString = (String)params[0];
        JSONObject json = (JSONObject)params[1];
        try{
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.connect();

            OutputStreamWriter wr= new OutputStreamWriter(connection.getOutputStream());
            wr.write(json.toString());

            InputStream inputStream = connection.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
                chaine.append(line);
            }

        } catch (IOException e) {
            // writing exception to log
            e.printStackTrace();
        }


        return chaine;
    }
}
