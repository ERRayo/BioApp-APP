package com.fgtit.login.model;

import org.json.JSONException;
import org.json.JSONObject;

public class UserAccess {

    public  static UserAccess getUser(JSONObject jsonObject) throws JSONException {

        int id = jsonObject.getInt("id");
        String email = jsonObject.getString("email");
        String tipo_usuario = jsonObject.getString("tipo_usuario");

        UserAccess userAccess = new UserAccess(id, email, tipo_usuario);

        return userAccess;
    }

    private  int id;
    private  String email;
    private  String tipo_usuario;

    public UserAccess(int id, String email, String tipo_usuario) {
        this.id = id;
        this.email = email;
        this.tipo_usuario = tipo_usuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
