package com.hig.hwangingyu.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
//import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.Map;

import javax.net.ssl.HttpsURLConnection;



public class HttpUtils {
    
    static public String HttpPost(String url, Map<String,String> attributes) throws MalformedURLException, IOException {
        URL c = new URL(url);
        HttpsURLConnection conn = (HttpsURLConnection)c.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setDefaultUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
  
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        StringBuilder request = new StringBuilder();
        for(String key : attributes.keySet()) {
            request.append(key);
            request.append("=");
            request.append(attributes.get(key));
            request.append("&");
        }
        request.deleteCharAt(request.length()-1);

        bw.write(request.toString(), 0, request.length());
        bw.flush();
        
        //conn.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder("");
        int data=0;
        while((data=br.read())!=-1){
            response.append((char)data);
        }
        return response.toString();
    }
}
