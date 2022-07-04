package com.fgtit.login.model;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

    public  static User getUser(JSONObject jsonObject) throws JSONException {

        String access = jsonObject.getString("access");
        String email = jsonObject.getString("email");
        String tipo_usuario = jsonObject.getString("tipo_usuario");

        User user = new User(access, email, tipo_usuario);

        return user;
    }

    private  String access;
    private  String email;
    private  String tipo_usuario;

    public User(String access, String email, String tipo_usuario) {
        this.access = access;
        this.email = email;
        this.tipo_usuario = tipo_usuario;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipo_usuario() {
        return tipo_usuario;
    }

    public void setTipo_usuario(String tipo_usuario) {
        this.tipo_usuario = tipo_usuario;
    }

    @Override
    public  boolean equals(Object obj) {
        boolean result = false;

        if (obj != null && obj instanceof  User) {
            User that = (User) obj;
            if (this.email.equalsIgnoreCase(that.email)) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public String toString () {
        return this.email + "(" +this.tipo_usuario + ")";
    }
}
