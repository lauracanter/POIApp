package com.lauracanter.poiapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParser {

    private HashMap<String, String> getPlace(JSONObject googlePlaceJson)
    {
        /*HashMap<String, String> googlePlaceMap = new HashMap<>();
        String placeName = "--NA--";
        String vicinity = "--NA--";
        String latitude = "";
        String longitude = "";
        String reference = "";*/

        Log.d("POIApp DataParser", "getPlace jsonObject= "+googlePlaceJson.toString());

           /* try {
                if(!googlePlaceJson.isNull("name")) {
                    placeName = googlePlaceJson.getString("name");
                    Log.d("POIApp DataParser", "name"+placeName);
                }

                if(!googlePlaceJson.isNull("vicinity")){
                    vicinity=googlePlaceJson.getString("vicinity");
                    Log.d("POIApp DataParser", "vicinity"+vicinity);
                }

                latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
                longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lon");

                reference = googlePlaceJson.getString("reference");

                googlePlaceMap.put("placeName", placeName);
                Log.d("POIApp","placeName: "+placeName);
                googlePlaceMap.put("vicinity", vicinity);
                Log.d("POIApp","vicinity: "+vicinity);
                googlePlaceMap.put("lat", latitude);
                Log.d("POIApp","lat: "+latitude);
                googlePlaceMap.put("lon", longitude);
                Log.d("POIApp","lon: "+longitude);
                googlePlaceMap.put("reference", reference);
                Log.d("POIApp","reference: "+reference);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return googlePlaceMap;*/

            HashMap<String, String> googlePlaceMap = new HashMap<String, String>();
            String placeName = "-NA-";
            String vicinity = "-NA-";
            String latitude = "";
            String longitude = "";
            String reference = "";

            Log.d("getPlace", "Entered");

            try {
                if (!googlePlaceJson.isNull("name")) {
                    placeName = googlePlaceJson.getString("name");
                }
                if (!googlePlaceJson.isNull("vicinity")) {
                    vicinity = googlePlaceJson.getString("vicinity");
                }
                latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
                longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
                reference = googlePlaceJson.getString("reference");
                googlePlaceMap.put("place_name", placeName);
                googlePlaceMap.put("vicinity", vicinity);
                googlePlaceMap.put("lat", latitude);
                googlePlaceMap.put("lng", longitude);
                googlePlaceMap.put("reference", reference);
                Log.d("getPlace", "Putting Places");
            } catch (JSONException e) {
                Log.d("getPlace", "Error");
                e.printStackTrace();
            }
            return googlePlaceMap;

        }


        private List<HashMap<String, String>> getPlaces(JSONArray jsonArray)
        {
            Log.d("POIApp", "getPlaces accessed");

            int count = jsonArray.length();

            List<HashMap<String, String>> placesList = new ArrayList<>();
            HashMap<String, String> placeMap;

            for(int i = 0; i<count; i++)
            {
                try {
                    placeMap = getPlace((JSONObject) jsonArray.get(i));
                    placesList.add(placeMap);
                }catch (JSONException e) {
                e.printStackTrace();
                }

            }

            return placesList;
        }

        public List<HashMap<String, String>> parse(String jsonData)
        {
            JSONArray jsonArray = null;
            JSONObject jsonObject;

            Log.d("POIApp", "parse"+jsonData);

            try {
                jsonObject = new JSONObject(jsonData);
                jsonArray=jsonObject.getJSONArray("results");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return getPlaces(jsonArray);
        }
}
