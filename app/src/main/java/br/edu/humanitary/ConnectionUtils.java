package br.edu.humanitary;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Andrey on 04/11/15.
 */
public class ConnectionUtils extends AsyncTask<Object, Void, String> {
    public AsyncResponse delegate = null;

    @Override
    protected String doInBackground(Object... params) {
        StringBuffer chaine = new StringBuffer("");
        String urlString = (String)params[0];
        JSONObject json = (JSONObject)params[1];
        try{
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            connection.connect();

            BufferedWriter out =
                    new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            out.write(json.toString());
            out.close();

            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
                chaine.append(line);
            }

        } catch (IOException e) {
            // writing exception to log
            e.printStackTrace();
        }


        return chaine.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }
}
