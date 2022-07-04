package com.fgtit.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.fgtit.login.model.Profesor;
import com.fgtit.reader.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Admin extends AppCompatActivity {

    RequestQueue mRequestQueue;
    String BASE_URL = "http://192.168.0.4:5000/";
    private ListView lstProfesores;
    ArrayList<String> datos = new ArrayList<String>();
    List<Profesor> listProfesor = new ArrayList<>();
    private EditText bProfesor;
    ArrayAdapter<Profesor> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set up the window layout
        setContentView(R.layout.admin);

        Button bAddProfesor = findViewById(R.id.bAddProfesor);
        lstProfesores = (ListView) findViewById(R.id.listVProfesores);
        bProfesor = findViewById(R.id.buscarProfesor);

        bAddProfesor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin.this, AgregarProfesor.class);
                startActivity(intent);
            }
        });

        profesoresGet();


    }

    public void profesoresGet () {
        mRequestQueue = Volley.newRequestQueue(this);
        String url= BASE_URL + "profesor";

        Response.Listener<JSONArray> successListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject obj = response.getJSONObject(i);

                        int id = obj.getInt("id");
                        String nombre = obj.getString("nombre");
                        String apellido_paterno = obj.getString("apellido_paterno");
                        String apellido_materno = obj.getString("apellido_materno");
                        String no_trabajador = obj.getString("no_trabajador");

                        listProfesor.add(new Profesor(id, nombre, apellido_paterno, apellido_materno, no_trabajador));


                        Toast.makeText(Admin.this, "Correcto", Toast.LENGTH_LONG).show();
                    }

                    adapter = new ArrayAdapter<>(Admin.this, R.layout.spinner_items, listProfesor);


                    lstProfesores.setAdapter(adapter);

                    bProfesor.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            Admin.this.adapter.getFilter().filter(s);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                    lstProfesores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //Toast.makeText(Admin.this, "Id del profe seleccionado: " + listProfesor.get(position).getId(), Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(Admin.this, UpdateProfesor.class);
                            intent.putExtra("idProfesor", listProfesor.get(position).getId());
                            startActivity(intent);

                        }
                    });



                } catch (JSONException e) {

                    Toast.makeText(Admin.this, "JSON exception huellas", Toast.LENGTH_LONG).show();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Admin.this, "Error response", Toast.LENGTH_LONG).show();
            }
        };

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, successListener, errorListener);
        mRequestQueue.add(request);

    }

}
