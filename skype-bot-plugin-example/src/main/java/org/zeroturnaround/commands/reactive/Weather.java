package org.zeroturnaround.commands.reactive;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.zeroturnaround.skypebot.plugins.NameCommand;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

public class Weather extends NameCommand {
  private static double absoluteZero = -273.15;
  private static String baseUrl = "http://openweathermap.org/data/2.1/weather/city/CITY_ID?type=json";

  private Map<String, String> cities = new HashMap<String, String>();

  public Weather() {
    cities.put("Brussels", "2800866");
    cities.put("Boston", "4930956");
    cities.put("London", "2643743");
    cities.put("Prague", "3067696");
    cities.put("Tallinn", "588409");
    cities.put("Tartu", "588335");
  }

  @Override
  protected String reactInternal(String conversationName, String author, String message) {
    Map<String, String> temp = getTemperatures();
    StringBuilder sb = new StringBuilder("here you are:\n");
    for (Map.Entry<String, String> me : temp.entrySet()) {
      sb.append(me.getKey());
      sb.append(": ");
      sb.append(me.getValue());
      sb.append("\n");
    }
    return sb.toString();
  }

  private Map<String, String> getTemperatures() {
    Map<String, String> result = new HashMap<String, String>();
    try {
      for (Map.Entry<String, String> me : cities.entrySet()) {
        Double kelvins = getKelvinsTemp(me.getValue());
        result.put(me.getKey(), prettyPrint(kelvins));
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  private double round(double c) {
    return Math.round(c * 100.0) / 100.0;
  }

  private String prettyPrint(Double kelvins) {
    double c = round(getC(kelvins));
    double f = round(getF(kelvins));
    return Double.toString(c) + " °C (" + Double.toString(f) + " °F)";
  }

  private Double getKelvinsTemp(String cityId) throws Exception, ParseException {
    String url = baseUrl.replaceFirst("CITY_ID", cityId);
    String json = URLConnectionReader.getText(url);
    JSONParser p = new JSONParser(JSONParser.MODE_PERMISSIVE);
    JSONObject o1 = (JSONObject) p.parse(json);
    o1 = (JSONObject) o1.get("main");
    Number kelvins = (Number) o1.get("temp");
    return kelvins.doubleValue();
  }

  private static double getF(double K) {
    return (K + absoluteZero) * 1.8 + 32;
  }
  private static double getC(double K) {
    return K + absoluteZero;
  }

  private static class URLConnectionReader {
    public static String getText(String url) throws Exception {
      URL website = new URL(url);
      URLConnection connection = website.openConnection();
      BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

      StringBuilder response = new StringBuilder();
      String inputLine;

      while ((inputLine = in.readLine()) != null)
        response.append(inputLine);

      in.close();

      return response.toString();
    }
  }

  @Override
  public String getHelp() {
    return "fetches weather data about some locations (openweathermap.org)";
  }
}