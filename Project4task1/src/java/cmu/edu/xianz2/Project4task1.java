package cmu.edu.xianz2;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author zhangxian
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 *
 * @author xianz2
 */
@WebServlet(name = "WebServer", urlPatterns = {"/WebServer/*"})
public class Project4task1 extends HttpServlet {
    private Controller controller = new Controller();
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
            // get the search parameter if it exists
            String search = request.getParameter("key");
            String jsonFile = controller.getFechData(search);

           // send back to client     
                PrintWriter out = response.getWriter();
		out.print(jsonFile + "\n");
                out.flush();

    }

}
