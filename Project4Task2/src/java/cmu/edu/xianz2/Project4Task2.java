/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cmu.edu.xianz2;

/**
 *
 * @author zhangxian
 */
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "WebServer",
        urlPatterns = {"/WebServer/*", "/dashboard"})
public class Project4Task2 extends HttpServlet {

    //   private List<Document> seedData = new ArrayList<Document>();
    private int counter = 0;
    private Controller controller = new Controller();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        String urlPath = request.getServletPath();

        if (urlPath.equals("/dashboard")) {

            // get Mlab data
//            controller.getMlabData();
            if (controller.getMlabData().isEmpty()) {

                request.setAttribute("noDataVerify", "1");
                request.setAttribute("noData", "There is no data at Mlab !!!");
            } else {
                request.setAttribute("dataList", controller.getMlabData());
                request.setAttribute("largestRate", controller.largestRate());
                double[] currencyP = controller.getPersentage(controller.getMlabData());
                request.setAttribute("counUSDrate", currencyP[0]);
                request.setAttribute("counINRrate", currencyP[1]);
                request.setAttribute("counAUDrate", currencyP[2]);
                request.setAttribute("counCADrate", currencyP[3]);
                request.setAttribute("counCNYrate", currencyP[4]);
                request.setAttribute("AvgLatancy", getAvgLantancy(controller.getMlabData()));
            }

            //dbList = null;
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);

        } else {
            long timeStart = System.currentTimeMillis();
            // get the search parameter if it exists
            String search = request.getParameter("key");
            String clientIP = request.getRemoteAddr();
            // String phoneModel = getPhoneModel (request.getHeader("User-Agent")); 
            String phoneModel = request.getHeader("User-Agent");

            System.out.print(phoneModel.toString());
            try {

                URL url = new URL("http://data.fixer.io/api/latest?access_key=17ad1c66c2e75424e7d84144f747de61&symbols=" + search + "&format=2");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);

                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + conn.getResponseCode());
                }
                StringBuilder builder = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));

                String output;
                while ((output = br.readLine()) != null) {
                    builder.append(output);
                }

                PrintWriter out = response.getWriter();
                out.print(builder.toString() + "\n");
                out.flush();
                long timeEnd = System.currentTimeMillis();
                String latecy = (timeEnd - timeStart) + "";

                String jsonData[] = getJsonResult(builder.toString(), search);
                counter++;

//                String count = counter + "";
                System.out.println("counter:" + counter);

                // add to Mlab
                controller.addInforMlab(counter, latecy, clientIP, phoneModel, search, jsonData[2],
                        jsonData[0]);
                conn.disconnect();

            } catch (MalformedURLException e) {

                e.printStackTrace();
            }

        }

    }
// jason parser 

    private String[] getJsonResult(String doc, String searchTerm) {
        try {
            String data[] = {"time", "base", "rate"};
            JSONObject jsonObj = new JSONObject(doc);
            String timestamp = jsonObj.getString("date");
            data[0] = timestamp;
            String baseCurreny = jsonObj.getString("base");
            data[1] = baseCurreny;
            JSONObject jsonRate = new JSONObject();
            jsonRate = jsonObj.getJSONObject("rates");
            String rates = jsonRate.optString(searchTerm);
            data[2] = rates;
            System.out.println(data[0]);
            System.out.println(data[2]);
            return data;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private double getAvgLantancy(List<DataBean> dbList1) {
        long sum = 0;
        for (DataBean var : dbList1) {
            sum = sum + Long.parseLong(var.getUserType());
        }
        double avg = sum / dbList1.size();
        return avg;
    }
//// add infornation to Mlab https://github.com/mongolab/mongodb-driver-examples/blob/master/java/JavaSimpleExample.java
//
//    private void addInforMlab(
//            int serNo,
//            String userType,
//            String userIP,
//            String baseCurrency,
//            String searchCurrency,
//            String rates,
//            String lastAccessed) {
//        List<Document> seedData = new ArrayList<Document>();
//
//        seedData.add(new Document("serNo", serNo)
//                .append("userType", userType)
//                .append("userIP", userIP)
//                .append("baseCurrency", baseCurrency)
//                .append("searchCurrency", searchCurrency)
//                .append("rates", rates)
//                .append("lastAccessed", lastAccessed)
//        );
//
//        MongoClientURI uri = new MongoClientURI("mongodb://xianz2:1234@ds213209.mlab.com:13209/xianz2");
//        MongoClient client = new MongoClient(uri);
//        MongoDatabase db = client.getDatabase(uri.getDatabase());
//
//        MongoCollection<Document> currencyRates = db.getCollection("currencyRates");
//        currencyRates.insertMany(seedData);
//
//    }

//// get Data from Mlab https://github.com/mongolab/mongodb-driver-examples/blob/master/java/JavaSimpleExample.java
//    private List<DataBean> getMlabData() {
//        List<DataBean> dbList = new ArrayList<DataBean>();
//        MongoClientURI uri = new MongoClientURI("mongodb://xianz2:1234@ds213209.mlab.com:13209/xianz2");
//        MongoClient client = new MongoClient(uri);
//        MongoDatabase db = client.getDatabase(uri.getDatabase());
//        
//
//        MongoCollection<Document> currencyRates = db.getCollection("currencyRates");
//        Document orderBy = new Document("serNo", 1);
//        MongoCursor<Document> cursor = currencyRates.find().sort(orderBy).iterator();
//
//        try {
//            while (cursor.hasNext()) {
//                Document doc = cursor.next();
//                DataBean dataBean = new DataBean(
//                        Integer.parseInt(doc.get("serNo") + ""),
//                        doc.get("userType") + "",
//                        doc.get("userIP") + "",
//                        doc.get("baseCurrency") + "",
//                        doc.get("searchCurrency") + "",
//                        doc.get("rates") + "",
//                        doc.get("lastAccessed") + "");
//                dbList.add(dataBean);
////                System.out.println("11111");
//            }
//
//        } finally {
//            cursor.close();
//        }
//
//        return dbList;
//       
//
//    }
//// get largestRate https://github.com/mongolab/mongodb-driver-examples/blob/master/java/JavaSimpleExample.java
//    private String largestRate() {
//        MongoClientURI uri = new MongoClientURI("mongodb://xianz2:1234@ds213209.mlab.com:13209/xianz2");
//        MongoClient client = new MongoClient(uri);
//        MongoDatabase db = client.getDatabase(uri.getDatabase());
//
//        MongoCollection<Document> currencyRates = db.getCollection("currencyRates");
//
//        Document orderBy = new Document("rates", -1);
//        MongoCursor<Document> cursor = currencyRates.find().sort(orderBy).iterator();
//        List<DataBean> tastlist = new ArrayList<DataBean>();
//        try {
//            while (cursor.hasNext()) {
//                Document doc = cursor.next();
//                DataBean dataBean = new DataBean(
//                        Integer.parseInt(doc.get("serNo") + ""),
//                        doc.get("userType") + "",
//                        doc.get("userIP") + "",
//                        doc.get("baseCurrency") + "",
//                        doc.get("searchCurrency") + "",
//                        doc.get("rates") + "",
//                        doc.get("lastAccessed") + "");
//                tastlist.add(dataBean);
//            }
//
//        } finally {
//            cursor.close();
//        }
//
//        String lagestRate = 
//                "1 EUR" + " equals "
//                + tastlist.get(0).getRates() + " "
//                + tastlist.get(0).getSearchCurrency();
//        return lagestRate;
//
//    }
//    private double[] getPersentage(List<DataBean> dbList1) {
//        double[] myList = new double[5];
//        int size = dbList1.size();
//        double counUSD = 0;
//        double counINR = 0;
//        double counAUD = 0;
//        double counCAD = 0;
//        double counCNY = 0;
//
//        for (DataBean var : dbList1) {
//            if (var.getSearchCurrency().equals("USD")) {
//                counUSD++;
//            } else if (var.getSearchCurrency().equals("INR")) {
//                counINR++;
//            } else if (var.getSearchCurrency().equals("AUD")) {
//                counAUD++;
//            } else if (var.getSearchCurrency().equals("CAD")) {
//                counCAD++;
//            } else if (var.getSearchCurrency().equals("CNY")) {
//                counCNY++;
//            }
//
//        }
//        System.out.println(size + "  " + counCNY);
//        System.out.println(counCNY / size);
//
//        double counUSDrate = counUSD / size;
//        double counINRrate = counINR / size;
//        double counAUDrate = counAUD / size;
//        double counCADrate = counCAD / size;
//        double counCNYrate = counCNY / size;
//
//        System.out.println(counINRrate + "  " + counCNYrate);
//        myList[0] = counUSDrate;
//        myList[1] = counINRrate;
//        myList[2] = counAUDrate;
//        myList[3] = counCADrate;
//        myList[4] = counCNYrate;
//
//        return myList;
//
//    }
}
