package com.example.roza.places;

import android.os.AsyncTask;
import android.util.JsonReader;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Parameter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetNearbyPlaces extends AsyncTask<Object,String,String> {
    GoogleMap mMap;
    String url;
    InputStream is;
    BufferedReader bufferedReader;
    StringBuilder stringBuilder;
    String data;
    @Override
    protected String doInBackground(Object... objects) {
        mMap=(GoogleMap)objects[0];
        url=(String)objects[1];
        try
        {
            URL myurl=new URL(url);
            HttpURLConnection httpURLConnection=(HttpURLConnection)myurl.openConnection();
            httpURLConnection.connect();
            is=httpURLConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line="";
            stringBuilder= new StringBuilder();
            while ((line=bufferedReader.readLine())!=null)
            {
                stringBuilder.append(line);


            }
            data=stringBuilder.toString();

        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }



        return data;
    }

    @Override
    protected void onPostExecute(String s)
    {

        try {
            JSONObject parentaobject=new JSONObject(s);
            JSONArray resultsArray=parentaobject.getJSONArray("results");
            for (int i=0;i<resultsArray.length();i++)
            {
                JSONObject jsonObject=resultsArray.getJSONObject(i);
                JSONObject locationobject=jsonObject.getJSONObject("geometry").getJSONObject("location");

                String latitude=locationobject.getString("lat");
                String longitude=locationobject.getString("lng");

                JSONObject nameobject= resultsArray.getJSONObject(i);
                String name=nameobject.getString("name");
                String vicinty=nameobject.getString("vicinity");

                LatLng latLng=new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
                MarkerOptions markerOptions=new MarkerOptions();
                markerOptions.title(vicinty);
                markerOptions.position(latLng);
                mMap.addMarker(markerOptions);



            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        super.onPostExecute(s);
    }
}
