package emis.dsw.atelier.utils;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class AtelierService {

    public static final String HOST = "http://10.0.2.2/AtelierService/";

    public static JSONArray getServices() {

        return httpPostRequest("actions/getServices.php");
    }

    public static JSONArray getMyEvents() {
        return httpPostRequest("actions/getMyEvents.php");
    }

    public static JSONArray httpPostRequest(String action) {
        String response = "";

        String url = HOST + action;
        BufferedReader reader = null;
        HttpURLConnection conn = null;
        try {
            URL urlObj = new URL(url);

            conn = (HttpURLConnection) urlObj.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(url);
            wr.flush();

            Log.d("post response code", conn.getResponseCode() + " ");

            int responseCode = conn.getResponseCode();

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            response = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception ex) {
            }
        }
        Log.d("RESPONSE POST", response);

        try {
            return new JSONArray(response);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
