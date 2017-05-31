package otech.proyectofinaltep;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by omarmendozagonzalez on 03/05/17.
 */

public class HTTPDataHandler {
    static String stream = null;
    private static String DEBUG_MESSAGE = "Practica 06 JSOnAndroid";

    public HTTPDataHandler(){}

    public String getHTTPData(String urlString){
        try{
            URL url = new URL(urlString);
            HttpURLConnection urlConnection
                    = (HttpURLConnection) url.openConnection();
            int conncode = urlConnection.getResponseCode();
            Log.i(DEBUG_MESSAGE, "Codigo " + conncode);
            if(conncode == 200){
                InputStream in =
                        new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = r.readLine()) != null){
                    sb.append(line);
                }
                stream = sb.toString();
                urlConnection.disconnect();
            }

        }catch (MalformedURLException e){
            Log.i(DEBUG_MESSAGE, "MalformedURL");
            e.printStackTrace();
        }catch (IOException e){
            Log.i(DEBUG_MESSAGE,"IOException");
            e.printStackTrace();
        }finally {

        }
        return stream;
    }

}
