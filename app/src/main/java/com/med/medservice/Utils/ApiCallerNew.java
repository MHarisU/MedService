package com.med.medservice.Utils;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ApiCallerNew extends AsyncTask<String, String, String> {

    HttpURLConnection httpURLConnection = null;

    String mainFile;

    String URL_Link;

    // you may separate this or combined to caller class.
    public interface AsyncApiResponse {
        void processFinish(String response);
    }

    public AsyncApiResponse delegate = null;


    public ApiCallerNew(String URL_Link, AsyncApiResponse delegate){
        this.delegate = delegate;
        this.URL_Link = URL_Link;
    }

    public ApiCallerNew(String URL_Link){

        this.URL_Link = URL_Link;
    }




    @Override
    protected String doInBackground(String... strings) {

        try {
            URL url = new URL(URL_Link);
            //  URL url = new URL("https://www.brainywoodindia.com/wp-json/ldlms/v1/sfwd-courses/2601");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer stringBuffer = new StringBuffer();

            String line = "";
            while ((line = bufferedReader.readLine()) != null) {

                stringBuffer.append(line);

            }

            mainFile = stringBuffer.toString();

          //  jsonResponse = mainFile;

            return mainFile;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public void onPostExecute(String s) {
        super.onPostExecute(s);
        delegate.processFinish(s);

        //jsonResponse = mainFile;

     //   return mainFile;

    }
}