package xianz2.cmu.edu.project4android;

import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by zhangxian on 3/24/18.
 */

public class GetRates {

    CurrencyRates ip = null;


    public void search(String searchTerm, CurrencyRates ip) {
        this.ip = ip;
        new AsyncRatesSearch().execute(searchTerm);
    }
    /*
     * AsyncTask provides a simple way to use a thread separate from the UI thread in which to do network operations.
     * doInBackground is run in the helper thread.
     * onPostExecute is run in the UI thread, allowing for safe UI updates.
     */
    private class AsyncRatesSearch extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            return search(urls[0]);
        }

        protected void onPostExecute(String picture) {
            ip.ratesReady(picture);
        }

        /*
         * Search server for the searchTerm argument
         */
        private String search(String searchTerm) {

            try {
                System.out.println(searchTerm);
                // Task 1
//                  URL url = new URL("https://arcane-meadow-96091.herokuapp.com/WebServer?key=" + searchTerm);
                // Task 2
                URL url = new URL("https://floating-wildwood-16147.herokuapp.com/WebServer?key=" + searchTerm);
                String doc = getJson(url);
                String rates = getResult(doc, searchTerm );
                System.out.println(doc);
                System.out.println(rates);
                return rates;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
        }
    }


    /*
     * Given a url that will request json, return a Document with that json, else null
     */
    private String getJson( URL url) {
        try {

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("User-Agent", getDeviceName());

            conn.connect();

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String output = br.readLine();
            conn.disconnect();
            return output;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // jsonPaser
    private String getResult( String doc, String searchTerm) {
        try {
            JSONObject jsonObj = new JSONObject(doc);
            JSONObject jsonRate = new JSONObject();
            jsonRate = jsonObj.getJSONObject("rates");
            String rates = jsonRate.optString(searchTerm);
            return rates;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

 //  getDeviceName https://stackoverflow.com/questions/1995439/get-android-phone-model-programmatically
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }
}

