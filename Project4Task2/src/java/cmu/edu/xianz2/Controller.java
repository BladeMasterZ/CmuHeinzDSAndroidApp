/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cmu.edu.xianz2;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

/**
 *
 * @author zhangxian
 */
public class Controller {
    // add infornation to Mlab https://github.com/mongolab/mongodb-driver-examples/blob/master/java/JavaSimpleExample.java

    public void addInforMlab(
            int serNo,
            String userType,
            String userIP,
            String baseCurrency,
            String searchCurrency,
            String rates,
            String lastAccessed) {
        List<Document> seedData = new ArrayList<Document>();

        seedData.add(new Document("serNo", serNo)
                .append("userType", userType)
                .append("userIP", userIP)
                .append("baseCurrency", baseCurrency)
                .append("searchCurrency", searchCurrency)
                .append("rates", rates)
                .append("lastAccessed", lastAccessed)
        );

        MongoClientURI uri = new MongoClientURI("mongodb://xianz2:1234@ds213209.mlab.com:13209/xianz2");
        MongoClient client = new MongoClient(uri);
        MongoDatabase db = client.getDatabase(uri.getDatabase());

        MongoCollection<Document> currencyRates = db.getCollection("currencyRates");
        currencyRates.insertMany(seedData);

    }

    // get Data from Mlab https://github.com/mongolab/mongodb-driver-examples/blob/master/java/JavaSimpleExample.java
    public List<DataBean> getMlabData() {
        List<DataBean> dbList = new ArrayList<DataBean>();
        MongoClientURI uri = new MongoClientURI("mongodb://xianz2:1234@ds213209.mlab.com:13209/xianz2");
        MongoClient client = new MongoClient(uri);
        MongoDatabase db = client.getDatabase(uri.getDatabase());

        MongoCollection<Document> currencyRates = db.getCollection("currencyRates");
        Document orderBy = new Document("serNo", 1);
        MongoCursor<Document> cursor = currencyRates.find().sort(orderBy).iterator();

        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                DataBean dataBean = new DataBean(
                        Integer.parseInt(doc.get("serNo") + ""),
                        doc.get("userType") + "",
                        doc.get("userIP") + "",
                        doc.get("baseCurrency") + "",
                        doc.get("searchCurrency") + "",
                        doc.get("rates") + "",
                        doc.get("lastAccessed") + "");
                dbList.add(dataBean);
//                System.out.println("11111");
            }

        } finally {
            cursor.close();
        }

        return dbList;

    }

    // get largestRate https://github.com/mongolab/mongodb-driver-examples/blob/master/java/JavaSimpleExample.java
    public String largestRate() {
        MongoClientURI uri = new MongoClientURI("mongodb://xianz2:1234@ds213209.mlab.com:13209/xianz2");
        MongoClient client = new MongoClient(uri);
        MongoDatabase db = client.getDatabase(uri.getDatabase());

        MongoCollection<Document> currencyRates = db.getCollection("currencyRates");

        Document orderBy = new Document("rates", -1);
        MongoCursor<Document> cursor = currencyRates.find().sort(orderBy).iterator();
        List<DataBean> tastlist = new ArrayList<DataBean>();
        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                DataBean dataBean = new DataBean(
                        Integer.parseInt(doc.get("serNo") + ""),
                        doc.get("userType") + "",
                        doc.get("userIP") + "",
                        doc.get("baseCurrency") + "",
                        doc.get("searchCurrency") + "",
                        doc.get("rates") + "",
                        doc.get("lastAccessed") + "");
                tastlist.add(dataBean);
            }

        } finally {
            cursor.close();
        }

        String lagestRate
                = "1 EUR" + " equals "
                + tastlist.get(0).getRates() + " "
                + tastlist.get(0).getSearchCurrency();
        return lagestRate;

    }

    public double[] getPersentage(List<DataBean> dbList1) {
        double[] myList = new double[5];
        int size = dbList1.size();
        double counUSD = 0;
        double counINR = 0;
        double counAUD = 0;
        double counCAD = 0;
        double counCNY = 0;

        for (DataBean var : dbList1) {
            if (var.getSearchCurrency().equals("USD")) {
                counUSD++;
            } else if (var.getSearchCurrency().equals("INR")) {
                counINR++;
            } else if (var.getSearchCurrency().equals("AUD")) {
                counAUD++;
            } else if (var.getSearchCurrency().equals("CAD")) {
                counCAD++;
            } else if (var.getSearchCurrency().equals("CNY")) {
                counCNY++;
            }

        }
        System.out.println(size + "  " + counCNY);
        System.out.println(counCNY / size);

        double counUSDrate = counUSD / size;
        double counINRrate = counINR / size;
        double counAUDrate = counAUD / size;
        double counCADrate = counCAD / size;
        double counCNYrate = counCNY / size;

        System.out.println(counINRrate + "  " + counCNYrate);
        myList[0] = counUSDrate;
        myList[1] = counINRrate;
        myList[2] = counAUDrate;
        myList[3] = counCADrate;
        myList[4] = counCNYrate;

        return myList;

    }

}
