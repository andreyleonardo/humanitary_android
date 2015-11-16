package br.edu.humanitary;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, AsyncResponse {

    private GoogleMap mMap;
    ConnectionUtils connectionUtils = new ConnectionUtils();
    JSONObject result;
    private Marker marker;
    LocationManager locationManager;
    private boolean isGPSEnabled;
    private Boolean isNetworkProviderEnabled;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Location location = null;

        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMap = fm.getMap();

        mMap.setMyLocationEnabled(true);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
       /* Criteria criteria = new Criteria();

        String provider = locationManager.getBestProvider(criteria, true); */

        try {
            location = locationManager.getLastKnownLocation(locationManager.PASSIVE_PROVIDER);
            LatLng latlng = new LatLng(location.getLatitude(),location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
        } catch (SecurityException e) {
            Log.e("PERMISSION_EXCEPTION", "PERMISSION_NOT_GRANTED");
        }

        JSONObject nearby = new JSONObject();
        try {
            JSONObject attributes = new JSONObject();
            attributes.put("fb_user_id", 1234);
            attributes.put("latitude",location.getLatitude());
            attributes.put("longitude",location.getLongitude());
            attributes.put("fb_access_token", 1234556);


            nearby.put("near_groups", attributes);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        connectionUtils.delegate = this;
        String url = "http://humanitary.cloudapp.net/humanitary_api/near_groups";
//        String url = "http://516fcb86.ngrok.io/humanitary_api/near_groups";
        connectionUtils.execute(url, nearby);
    }

    @Override
    public void processFinish(String output) {
        //O output é o resultado retornado da API. Tratar aqui.
        String[] endPoints = null;
        String[] porPontos = null;
        try {
            JSONObject jsonAwnser = new JSONObject(output);

            JSONArray grupos = jsonAwnser.getJSONArray("nearby");
            endPoints = new String[grupos.length()];

            for (int i = 0; i < grupos.length();i++){
                JSONObject j = grupos.getJSONObject(i);

                String Nome = j.getString("group_name");
                String Descricao = j.getString("group_description");
                String Responsavel = j.getString("responsable_name");
                String Telgrup = j.getString("group_phone");
                double Longitude = Double.parseDouble(j.getString("longitude"));
                double Latitude = Double.parseDouble(j.getString("latitude"));
                String Endereco = j.getString("address");

                LatLng latLng = new LatLng(Latitude,Longitude);



                customAddMarker(latLng, Nome, Descricao);

            }


        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    public void customAddMarker(LatLng latLng, String title, String snippet){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng).title(title).snippet(snippet).draggable(true);
        marker = mMap.addMarker(markerOptions);
    }

}
