package emis.dsw.atelier.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class AtelierService {

    public static final String HOST = "http://10.0.2.2/AtelierService/";
    public static int serviceId;
    public static String day;
    public static int slot;

    public static JSONArray getServices() {

        return httpPostRequest("actions/getServices.php", "");
    }

    public static JSONArray getMyEvents() {
        return httpPostRequest("actions/getMyEvents.php", "");
    }

    public static JSONArray getEventsOfDay(String date) {
        date = "date="+date;
        return httpPostRequest("actions/getEventsOfDay.php", date);
    }

    public static JSONArray httpPostRequest(String action, String payload) {
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

            wr.write(payload);
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

    public static String getHour(int slot) {

        switch (slot) {
            case 1:
                return "9:00";
            case 2:
                return "10:00";
            case 3:
                return "11:00";
            case 4:
                return "12:00";
            case 5:
                return "13:00";
            case 6:
                return "14:00";
            case 7:
                return "15:00";
            case 8:
                return "16:00";
            case 9:
                return "17:00";

        }
        return "";
    }
}
