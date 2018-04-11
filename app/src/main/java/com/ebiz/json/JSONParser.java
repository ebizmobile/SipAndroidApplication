
package com.ebiz.json;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

/**
 * @author RANJITH KUMAR K
 */
public class JSONParser {
	static InputStream is = null;
    static JSONObject jObj = null;
    static JSONArray jList =null;
    static String json = "";
 public static long elapsedTime;
    // constructor
    public JSONParser() {
 
    }
 
    public static JSONObject getJSONFromUrl(String url, List<NameValuePair> params) {
 
        // Making HTTP request
        try {
            // defaultHttpClient
        	Log.e("URL___",url);

           HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

            DefaultHttpClient client = new DefaultHttpClient();

            SchemeRegistry registry = new SchemeRegistry();
            SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
            socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
            registry.register(new Scheme("https", socketFactory, 443));
            SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
            DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());

            // Set verifier
            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
        	
            //HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));
           
            long startTime = System.currentTimeMillis();
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
            
              

           //After you get response  

            elapsedTime = System.currentTimeMillis() - startTime;
            System.out.println("GOOOOOOOOOOOOOOOOOOOOOTTTTTTTT");
            System.out.println("Total elapsed http request/response time in milliseconds: " + elapsedTime);
            Log.e("isss",is.toString());

        	} catch (UnsupportedEncodingException e)
        	{
        		e.printStackTrace();
        	} catch (ClientProtocolException e)
        	{
        		e.printStackTrace();
        	} catch (IOException e)
        	{
            e.printStackTrace();
        	}
        	try
        	{
        	//BufferedReader reader=new BufferedReader(new InputStreamReader(is));
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"),8);
            System.out.println("urllllllll"+url);
            StringBuilder sb = new StringBuilder();
            System.out.println("usersb+++++++++++"+sb);
            String line = null;
            while ((line = reader.readLine()) != null) {
            	Log.e("BUIDER VALUE----",line);
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
            System.out.println("userjson-------------"+ json);
          //  Log.e("JSON...............................................", json);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
 
        // try parse the string to a JSON object
        try
        {
            jObj = new JSONObject(json);
            System.out.println("userjobj\t\t"+jObj);
        } catch (JSONException e) 
        {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
 
        // return JSON String
        return jObj;
    }
    
    public static JSONObject getJSONFromUrl(String url)
    {
        InputStream inputStream = null;
        String result =url;
        // Making HTTP request
        try {

                HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

                DefaultHttpClient client = new DefaultHttpClient();

                SchemeRegistry registry = new SchemeRegistry();
                SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
                socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
                registry.register(new Scheme("https", socketFactory, 443));
                SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
                DefaultHttpClient httpclient = new DefaultHttpClient(mgr, client.getParams());

                // Set verifier
                HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
                // create HttpClient
               // HttpClient httpclient = new DefaultHttpClient();

                // make GET request to the given URL
                HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

                // receive response as inputStream
                inputStream = httpResponse.getEntity().getContent();

            //After you get response

           // elapsedTime = System.currentTimeMillis() - startTime;
            //System.out.println("GOOOOOOOOOOOOOOOOOOOOOTTTTTTTT");
           // System.out.println("Total elapsed http request/response time in milliseconds: " + elapsedTime);
            Log.e("isss",inputStream.toString());

        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        } catch (ClientProtocolException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        try
        {
            //BufferedReader reader=new BufferedReader(new InputStreamReader(is));
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream, "iso-8859-1"),8);
            //System.out.println("urllllllll"+url);
            StringBuilder sb = new StringBuilder();
           // System.out.println("usersb+++++++++++"+sb);
            String line = null;
            while ((line = reader.readLine()) != null) {
                Log.e("BUIDER VALUE----",line);
                sb.append(line + "\n");
            }
            inputStream.close();
            json = sb.toString();
            System.out.println("userjson-------------"+ json);
            //  Log.e("JSON...............................................", json);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try
        {
            jObj = new JSONObject(json);
            System.out.println("userjobj\t\t"+jObj);
        } catch (JSONException e)
        {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
    public static JSONObject getJSONFromUrlRest(String url)
    {

        // Making HTTP request
        try
        {
            // defaultHttpClient
            Log.e("URL___",url);
            HttpClient httpClient = new DefaultHttpClient();
            //HttpPost httpPost = new HttpPost(url);

            HttpGet httpPost = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
            Log.e("isss",is.toString());

        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        } catch (ClientProtocolException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        try {
            //BufferedReader reader=new BufferedReader(new InputStreamReader(is));
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"),8);
            System.out.println("urllllllll"+url);
            StringBuilder sb = new StringBuilder();
            System.out.println("usersb+++++++++++"+sb);
            String line = null;
            while ((line = reader.readLine()) != null) {

                Log.e("BUIDER VALUE----",line);
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
            System.out.println("userjson-------------"+ json);
            //Log.e("JSON...............................................", json);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
            System.out.println("userjobj"+jObj);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;
    }


}
