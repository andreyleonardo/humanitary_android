package br.edu.humanitary;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, AsyncResponse{

    private GoogleMap mMap;
    ConnectionUtils connectionUtils = new ConnectionUtils();
    JSONObject result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        JSONObject nearby = new JSONObject();
        try {
            JSONObject attributes = new JSONObject();
            attributes.put("fb_user_id", 1234);
            attributes.put("latitude", 0);
            attributes.put("longitude", 0);
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
        System.out.print(output);
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


        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
