/** 
 * This example uses the Apache HTTPComponents library. 
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PruebaBTC {

  private static String apiKey = "c96cf995-3fda-454b-8975-5232fe5040ba";

  public static void main(String[] args) {
    String uri = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/quotes/latest?symbol=BTC";
    List<NameValuePair> paratmers = new ArrayList<NameValuePair>();
    //paratmers.add(new BasicNameValuePair("start","1"));
    //paratmers.add(new BasicNameValuePair("limit","5000"));
    //paratmers.add(new BasicNameValuePair("convert","USD"));

    try {
      String result = makeAPICall(uri, paratmers);
      //System.out.println(result);

      
      JsonElement jelement = new JsonParser().parse(result);
      JsonObject  jobject = jelement.getAsJsonObject();
      jobject = jobject.getAsJsonObject("data");
      System.out.println(jobject);
      jobject = jobject.getAsJsonObject("BTC");
      System.out.println(jobject);
      jobject = jobject.getAsJsonObject("quote");
      System.out.println(jobject);
      jobject = jobject.getAsJsonObject("USD");
      System.out.println(jobject);
      System.out.println(jobject.get("price"));
      
    } catch (IOException e) {
      System.out.println("Error: cannont access content - " + e.toString());
    } catch (URISyntaxException e) {
      System.out.println("Error: Invalid URL " + e.toString());
    }
  }

  public static String makeAPICall(String uri, List<NameValuePair> parameters)
      throws URISyntaxException, IOException {
    String response_content = "";

    URIBuilder query = new URIBuilder(uri);
    query.addParameters(parameters);

    CloseableHttpClient client = HttpClients.createDefault();
    HttpGet request = new HttpGet(query.build());

    request.setHeader(HttpHeaders.ACCEPT, "application/json");
    request.addHeader("X-CMC_PRO_API_KEY", apiKey);

    CloseableHttpResponse response = client.execute(request);

    try {
      System.out.println(response.getStatusLine());
      HttpEntity entity = response.getEntity();
      
      InputStream is = entity.getContent();
      BufferedReader reader = new BufferedReader(new InputStreamReader(is));
      String leido;
      while( (leido = reader.readLine()) != null ) {
    	  response_content += leido;
      }
      EntityUtils.consume(entity);
    } finally {
    	
      response.close();
    }

    return response_content;
  }

}
