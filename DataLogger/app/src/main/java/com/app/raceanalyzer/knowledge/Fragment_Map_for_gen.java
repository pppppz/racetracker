package com.app.raceanalyzer.knowledge;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.raceanalyzer.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


// method draw = draw polyline
// myOnMapLongClickListener = set mark and get location


public class Fragment_Map_for_gen extends Fragment {

    Polyline line;
    Context context;
    LatLng point;
    // Static LatLng
    LatLng startLatLng = new LatLng(13.756037, 100.532185);
    LatLng endLatLng = new LatLng(30.721419, 76.730017);
    private GoogleMap myMap;
    private TextView tvLocation;
    //LatLng startLatLng = new LatLng(30.707104, 76.690749);
    //when long clicked will mark into the map and set textfield
    GoogleMap.OnMapLongClickListener myOnMapLongClickListener =
            new GoogleMap.OnMapLongClickListener() {

                @Override
                public void onMapLongClick(LatLng p) {

                    //create mark point and set text
                    myMap.addMarker(new MarkerOptions().position(p).title(p.toString()));
                    tvLocation.setText(p.toString());
                    point = p; // set point use for draw
                }
            };
    private Button btn_draw;
    private View view;
    private FragmentActivity faActivity;
    Button.OnClickListener btn_draw_listener = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {
            draw(point);
        }
    };

    //convert location to string for set into textfield
    public static String locationStringFromLocation(final Location location) {
        return Location.convert(location.getLatitude(), Location.FORMAT_DEGREES) + " " + Location.convert(location.getLongitude(), Location.FORMAT_DEGREES);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        faActivity = super.getActivity();
        view = inflater.inflate(R.layout.fragment_choose_start_point, container, false);

        //  tvLocation = (TextView) view.findViewById(R.id.textview_SetLocation);
        //  btn_draw = (Button) view.findViewById(R.id.btn_draw);


//         myMap = ((SupportMapFragment) faActivity.getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

        myMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
        Log.e("fragment map ", "1");
        myMap.setMyLocationEnabled(true);
        Log.e("fragment map ", "2");
        myMap.setOnMapLongClickListener(myOnMapLongClickListener);
        Log.e("fragment map ", "3");
        myMap.moveCamera(CameraUpdateFactory.newLatLng(startLatLng));
        //  myMap.animateCamera(CameraUpdateFactory.zoomTo(12));

        btn_draw.setOnClickListener(btn_draw_listener);


        // Now auto clicking the button
        //    btntemp.performClick();
        return view;
    }

    public void draw(LatLng point) {

        myMap.addMarker(new MarkerOptions()
                .position(point)
                .title(point.toString()));

        Location myLocation = myMap.getMyLocation();
        if (myLocation == null) {
            Toast.makeText(faActivity.getApplicationContext(),
                    "My location not available",
                    Toast.LENGTH_LONG).show();
        } else {
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.add(point);
            polylineOptions.add(
                    new LatLng(myLocation.getLatitude(), myLocation.getLongitude()));
            myMap.addPolyline(polylineOptions);
        }
    }

    public void click_btn(View v) {

        //    Log.e("Test code", "method onclick");

        draw(point);

    }


    public void drawPath(String result) {
        if (line != null) {
            myMap.clear();
        }
        myMap.addMarker(new MarkerOptions().position(endLatLng).icon(
                BitmapDescriptorFactory.fromResource(R.drawable.abc_btn_check_material)));
        myMap.addMarker(new MarkerOptions().position(startLatLng).icon(
                BitmapDescriptorFactory.fromResource(R.drawable.abc_btn_check_to_on_mtrl_000)));
        try {
            // Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes
                    .getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);

            for (int z = 0; z < list.size() - 1; z++) {
                LatLng src = list.get(z);
                LatLng dest = list.get(z + 1);
                line = myMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(src.latitude, src.longitude),
                                new LatLng(dest.latitude, dest.longitude))
                        .width(5).color(Color.BLUE).geodesic(true));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    private class connectAsyncTask extends AsyncTask<Void, Void, String> {
        String url;
        private ProgressDialog progressDialog;

        connectAsyncTask(String urlPass) {
            url = urlPass;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Fetching route, Please wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            JSONParser jParser = new JSONParser();

            return jParser.getJSONFromUrl(url);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.hide();
            if (result != null) {
                drawPath(result);
            }
        }
    }

    public class JSONParser {

        InputStream is = null;
        JSONObject jObj = null;
        String json = "";

        // constructor
        public JSONParser() {
        }

        public String getJSONFromUrl(String url) {

            // Making HTTP request
            try {
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                json = sb.toString();
                is.close();
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
            return json;

        }
    }
}