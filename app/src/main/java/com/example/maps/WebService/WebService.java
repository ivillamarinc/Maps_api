package com.example.maps.WebService;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class WebService extends AsyncTask<String, Long, String> {

    private Map<String, String> datos;
    private String url;
    private Context actividad;
    private String xml = null;
    private Asynchtask callback = null;
    ProgressDialog progDailog;

    public WebService(String urlWebService, Map<String, String> data, Context activity, Asynchtask callback) {
        this.url = urlWebService;
        this.datos = data;
        this.actividad = activity;
        this.callback = callback;
    }

    public WebService() {}

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progDailog = new ProgressDialog(actividad);
        progDailog.setMessage("Cargando Web Service...");
        progDailog.setCancelable(false);
        progDailog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        String result;

        try {
            URL url = new URL(this.url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod(params[0]);
            conn.setDoInput(true);

            int responseCode = conn.getResponseCode();
            InputStream inputStream =
                    (responseCode == HttpURLConnection.HTTP_OK)
                            ? conn.getInputStream()
                            : conn.getErrorStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            reader.close();
            conn.disconnect();
            result = sb.toString();

        } catch (Exception e) {
            Log.e("WebService", "Error", e);
            result = "ERROR";
        }

        return result;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        progDailog.dismiss();
        this.xml = response;

        try {
            callback.processFinish(this.xml);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
