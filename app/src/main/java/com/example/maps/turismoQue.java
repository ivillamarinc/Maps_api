package com.example.maps;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.maps.WebService.Asynchtask;
import com.example.maps.WebService.WebService;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.slider.Slider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class turismoQue extends AppCompatActivity implements OnMapReadyCallback, Asynchtask {
    GoogleMap mMap;


    Double lat, lng;
    float radio ;
    Circle circulo = null;

    Slider sliderRadio;
    EditText txtLatitud, txtLongitud;
    ArrayList<Marker> markers = new ArrayList<Marker>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_turismo_que);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        lat = -1.02313;  lng = -79.459561;       radio = 1;

        txtLatitud = findViewById(R.id.txtLatitud);
        txtLongitud = findViewById(R.id.txtLongitud);
        sliderRadio = findViewById(R.id.sliderRadio);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        sliderRadio.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {
                radio = slider.getValue();
                updateInterfaz();
            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {

            }
        });


    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);




        CameraUpdate camUpd1 =
        CameraUpdateFactory
        .newLatLngZoom(new LatLng(lat, lng), 15);
        mMap.moveCamera(camUpd1);

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng center = mMap.getCameraPosition().target;
                lat = center.latitude;
                lng =center.longitude;
                updateInterfaz();

            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return true;
            }
        });


       /* LatLng quevedo = new LatLng(
                -1.022274398227109,
                -79.46115131561554
        );

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(quevedo)
                .zoom(13.5f)
                .bearing(0f)
                .tilt(0f)
                .build();

        mMap.moveCamera(
                CameraUpdateFactory.newCameraPosition(cameraPosition)
        );

        mMap.addMarker(new MarkerOptions()
                .position(quevedo)
                .title("Quevedo - Ecuador"));*/


    }
    private void updateInterfaz(){
        txtLatitud.setText(String.format("%.4f", lat));
        txtLongitud.setText(String.format("%.4f", lng));

        if(circulo!=null){  circulo.remove();  circulo = null; }

        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(lat,lng))
                .radius(radio * 100
                )
                .strokeColor(Color.RED)
                .fillColor(Color.argb(50, 150, 50, 50));

        circulo = mMap.addCircle(circleOptions);

        Map<String, String> datos = new HashMap<String, String>();
        WebService ws= new WebService(
                "https://turismo.quevedoenlinea.gob.ec/lugar_turistico/json_getlistadoMapa?lat=" + lat +   "&lng=" + lng +"&radio=" + (radio/10.0)  ,datos,

                turismoQue.this, turismoQue.this);
        ws.execute("GET");

    }


    @Override
    public void processFinish(String result) throws JSONException {



        for (Marker marker : markers) marker.remove();
        markers.clear();

        JSONObject JSONobj= new JSONObject(result);
        JSONArray jsonLista = JSONobj.getJSONArray("data");
        for(int i=0; i< jsonLista.length(); i++){
            JSONObject lugar= jsonLista.getJSONObject(i);
            markers.add(mMap.addMarker(
                    new MarkerOptions().position(
                            new LatLng(lugar.getDouble("lat"), lugar.getDouble("lng"))
                    ).title(lugar.get("nombre").toString())));

        }




    }
}