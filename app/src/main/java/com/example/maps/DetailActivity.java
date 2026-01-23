package com.example.maps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maps.network.Asynchtask;
import com.example.maps.network.WebService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity
        implements Asynchtask, OnMapReadyCallback {

    ImageView imgFlag;
    TextView txtName, txtCapital, txtIso, txtPhone, txtCenter;

    String iso2;
    GoogleMap mMap;
    double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imgFlag = findViewById(R.id.imgFlag);
        txtName = findViewById(R.id.txtName);
        txtCapital = findViewById(R.id.txtCapital);
        txtIso = findViewById(R.id.txtIso);
        txtPhone = findViewById(R.id.txtPhone);
        txtCenter = findViewById(R.id.txtCenter);

        iso2 = getIntent().getStringExtra("iso2");
        String name = getIntent().getStringExtra("name");
        txtName.setText(name);

        // Bandera
        String flagUrl = "http://www.geognos.com/api/en/countries/flag/" + iso2 + ".png";
        Picasso.get().load(flagUrl).into(imgFlag);

        // MAPA
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Web Service INFO
        String url = "http://www.geognos.com/api/en/countries/info/" + iso2 + ".json";
        WebService ws = new WebService(url, new HashMap<>(), this, this);
        ws.execute("GET");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void processFinish(String result) {
        try {
            JSONObject root = new JSONObject(result);
            JSONObject results = root.getJSONObject("Results");

            txtCapital.setText("📍 Capital: " +
                    results.getJSONObject("Capital").getString("Name"));

            JSONObject codes = results.getJSONObject("CountryCodes");
            txtIso.setText("🏳 ISO2: " + codes.getString("iso2")
                    + " | ISO3: " + codes.getString("iso3"));

            txtPhone.setText("📞 Prefijo Tel: " + results.getString("TelPref"));

            JSONArray geo = results.getJSONArray("GeoPt");
            lat = geo.getDouble(0);
            lng = geo.getDouble(1);
            txtCenter.setText("🌍 Centro: " + lat + ", " + lng);

            drawCountrySquare();

        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }
    }

    // 🔲 DIBUJA EL CUADRADO DEL PAÍS
    private void drawCountrySquare() {

        if (mMap == null) return;

        LatLng center = new LatLng(lat, lng);

        double offset = 5.0; // tamaño del cuadrado (grados)

        LatLng p1 = new LatLng(lat + offset, lng - offset);
        LatLng p2 = new LatLng(lat + offset, lng + offset);
        LatLng p3 = new LatLng(lat - offset, lng + offset);
        LatLng p4 = new LatLng(lat - offset, lng - offset);

        mMap.clear();

        mMap.addMarker(new MarkerOptions()
                .position(center)
                .title(txtName.getText().toString()));

        mMap.addPolygon(new PolygonOptions()
                .add(p1, p2, p3, p4)
                .strokeWidth(4)
                .strokeColor(0xFF2196F3)
                .fillColor(0x332196F3));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 3));
    }
}
