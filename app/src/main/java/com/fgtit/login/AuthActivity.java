package com.fgtit.login;

import android.content.Intent;
import android.icu.text.RelativeDateTimeFormatter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fgtit.login.model.Asistencia;
import com.fgtit.login.model.Huellas;
import com.fgtit.login.model.MateriaSelect;
import com.fgtit.login.model.Model;
import com.fgtit.login.model.Profesor;
import com.fgtit.login.model.User;
import com.fgtit.login.model.api.AbstractAPIListener;
import com.fgtit.reader.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AuthActivity extends  AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set up the window layout
        setContentView(R.layout.login);

        final EditText txbEmail = findViewById(R.id.txbEmail);
        final EditText txbPass = findViewById(R.id.txbPass);
        Button bLogin = findViewById(R.id.bLogin);

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txbEmail.getText().toString();
                String password = txbPass.getText().toString();

                //Toast.makeText(AunthActivity.this, "Email: " + email + " Password: " + password, Toast.LENGTH_LONG).show();

                final Model model = Model.getInstance(AuthActivity.this.getApplication());
                model.login(email, password, new AbstractAPIListener() {
                    @Override
                    public void onLogin(User user) {
                            if (user != null) {
                                model.setUser(user);
                                Toast.makeText(AuthActivity.this, "Usuario: " + user.getEmail() + " Conectado! Tipo de usuario: " + user.getTipo_usuario(), Toast.LENGTH_LONG).show();
                                if ("Prefecto".equals(user.getTipo_usuario())) {
                                    Intent intent = new Intent(AuthActivity.this, PrefectoActivity.class);
                                    startActivity(intent);
                                }
                                else if ("Administrador".equals(user.getTipo_usuario()))
                                {
                                    Intent intent = new Intent(AuthActivity.this, Admin.class);
                                    startActivity(intent);
                                }
                            }
                            else {
                                Toast.makeText(AuthActivity.this, "Invalid login", Toast.LENGTH_LONG).show();
                            }
                    }
                });
            }
        });
    }
}