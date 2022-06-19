package emis.dsw.atelier.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class AtelierService {

    public static final String HOST = "http://10.0.2.2/AtelierService/";
    public static String day;
    public static int slot;
    public static String cookie;


    public static boolean rejectEvent(String event) {
        String payload = "event=" + event;
        try {
            return ((JSONObject) httpPostRequest("actions/admin/rejectEvent.php", payload)).getInt("status") == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean acceptEvent(String event) {
        String payload = "event=" + event;
        try {
            return ((JSONObject) httpPostRequest("actions/admin/acceptEvent.php", payload)).getInt("status") == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean register(String login, String pwd, String name, String surname, String phone) {
        String payload = "email=" + login + "&password=" + pwd + "&firstname=" + name + "&surname=" + surname + "&phone=" + phone;
        try {
            return ((JSONObject) httpPostRequest("utils/addUser.php", payload)).getInt("status") == 1;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean logout() {
        httpPostRequest("utils/logout.php", "");
        return false;
    }

    public static boolean login(String login, String pwd) {
        String payload = "login=" + login + "&password=" + pwd;

        try {
            return ((JSONObject) httpPostRequest("utils/login.php", payload)).getInt("status") == 1;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean checkLogin() {
        try {
            return ((JSONObject) httpPostRequest("utils/checkLogin.php", "")).getInt("status") == 2;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addEvent(int serviceId, String date, int slot) {

        String payload = "service=" + serviceId + "&date_id=" + date + "&slot=" + slot;
        try {
            return ((JSONObject) httpPostRequest("actions/addEvent.php", payload)).getInt("status") == 1;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static JSONArray getServices() {

        try {
            return (JSONArray) httpPostRequest("actions/getServices.php", "");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONArray getMyEvents() {
        try {
            return (JSONArray) httpPostRequest("actions/getMyEvents.php", "");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONArray getEventsOfDay(String date) {
        date = "date=" + date;
        return (JSONArray) httpPostRequest("actions/getEventsOfDay.php", date);
    }

    public static Object httpPostRequest(String action, String payload) {

        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

        String response = "";

        String url = HOST + action;
        BufferedReader reader = null;
        HttpURLConnection conn = null;
        try {
            URL urlObj = new URL(url);

            conn = (HttpURLConnection) urlObj.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Cookie", AtelierService.cookie);
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(payload);
            wr.flush();

            Log.d("query url", url);
            Log.d("query payload", payload);
            Log.d("post response code", conn.getResponseCode() + " ");

            int responseCode = conn.getResponseCode();

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            response = sb.toString();
            conn.getContent();

            List<HttpCookie> cookies = cookieManager.getCookieStore().getCookies();
            for (HttpCookie cookie : cookies) {
                AtelierService.cookie = cookie.toString();
            }

        } catch (ConnectException ex) {
            ex.printStackTrace();
            try {
                return new JSONObject("{\"status\":\"-2\"}");
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
            try {
                return new JSONObject(response);
            } catch (JSONException ex) {
                ex.printStackTrace();
                return null;
            }
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
