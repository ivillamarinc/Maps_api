package com.example.maps;

import static com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        /*CameraUpdate camUpd1 =
        CameraUpdateFactory
        .newLatLngZoom(new LatLng(-1.022274398227109, -79.46115131561554), 13);
        mMap.moveCamera(camUpd1);*/

        /*LatLng FushimiInaritaisha = new LatLng(34.9909393540234, 135.82319222901452);

        CameraPosition camPos = new CameraPosition.Builder()
                .target(FushimiInaritaisha)
                .zoom(19)
                .bearing(45)
                .tilt(70)
                .build();

        CameraUpdate camUpd3 =
                CameraUpdateFactory.newCameraPosition(camPos);

        mMap.animateCamera(camUpd3);*/

       /* PolygonOptions ecuador = new PolygonOptions()
                .add(new LatLng(1.4700, -81.0500))
                .add(new LatLng(1.4700, -75.1887))
                .add(new LatLng(-4.9950, -75.1887))
                .add(new LatLng(-4.9950, -81.0500))
                .add(new LatLng(1.4700, -81.0500))
                .strokeWidth(6f)
                .strokeColor(Color.RED)
                .fillColor(Color.argb(50, 255, 0, 0));
        mMap.addPolygon(ecuador);


        LatLng centroEcuador = new LatLng(-1.5, -78.5);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centroEcuador, 6));*/



        LatLng utep = new LatLng(-1.012234479913939, -79.46708543589334);

        mMap.addMarker(new MarkerOptions()
                .position(utep)
                .title("UTEQ - Universidad Técnica Estatal de Quevedo"));

        LatLng nw = new LatLng(-1.0089, -79.4726); // Noroeste
        LatLng ne = new LatLng(-1.0089, -79.4619); // Noreste
        LatLng se = new LatLng(-1.0159, -79.4619); // Sureste
        LatLng sw = new LatLng(-1.0159, -79.4726); // Suroeste

        PolygonOptions uteqPoligono = new PolygonOptions()
                .add(nw)
                .add(ne)
                .add(se)
                .add(sw)
                .add(nw)
                .strokeColor(Color.RED)
                .strokeWidth(6f)
                .fillColor(Color.argb(50, 255, 0, 0));

        mMap.addPolygon(uteqPoligono);


        LatLng centroPoligonoUTEQ = new LatLng(
                (nw.latitude + se.latitude) / 2,
                (nw.longitude + se.longitude) / 2
        );

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                centroPoligonoUTEQ, 15.8f
        ));


    }
}