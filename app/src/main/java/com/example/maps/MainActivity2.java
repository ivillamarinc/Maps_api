package com.example.maps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import com.example.maps.adapter.CountryAdapter;
import com.example.maps.model.Country;
import com.example.maps.network.Asynchtask;
import com.example.maps.network.WebService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity implements Asynchtask {

    GridView gridView;
    ArrayList<Country> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Referencia al GridView (antes era ListView)
        gridView = findViewById(R.id.gridCountries);
        lista = new ArrayList<>();

        Map<String, String> datos = new HashMap<>();

        // URL corregida con fields (el API ahora responde bien)
        String url = "https://restcountries.com/v3.1/all?fields=name,cca2,flags";

        WebService ws = new WebService(
                url,
                datos,
                this,
                this
        );

        ws.execute("GET");
    }

    @Override
    public void processFinish(String result) {
        try {
            JSONArray jsonArray = new JSONArray(result);
            lista.clear();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                // Nombre del país
                JSONObject nameObj = obj.getJSONObject("name");
                String nombre = nameObj.getString("common");

                // Código ISO
                String iso2 = obj.getString("cca2");

                // Bandera
                JSONObject flagsObj = obj.getJSONObject("flags");
                String bandera = flagsObj.getString("png");

                lista.add(new Country(nombre, iso2, bandera));
            }

            CountryAdapter adapter = new CountryAdapter(this, lista);
            gridView.setAdapter(adapter);

            gridView.setOnItemClickListener((parent, view, position, id) -> {

                Country country = lista.get(position);

                Intent intent = new Intent(MainActivity2.this, DetailActivity.class);
                intent.putExtra("iso2", country.getIso2());
                intent.putExtra("name", country.getNombre());
                intent.putExtra("flag", country.getBandera());

                startActivity(intent);
            });

        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }
    }
}
