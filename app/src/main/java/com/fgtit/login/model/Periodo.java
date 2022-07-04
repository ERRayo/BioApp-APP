package com.fgtit.login.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Periodo {

    private int id;
    private String fecha_ini;
    private String fecha_fin;

    public  static Periodo getPeriodo(JSONObject jsonObject) throws JSONException {

        int id = jsonObject.getInt("id");
        String fecha_ini = jsonObject.getString("fecha_ini");
        String fecha_fin = jsonObject.getString("fecha_fin");

        Periodo periodo = new Periodo(id, fecha_ini, fecha_fin);

        return periodo;
    }

    public Periodo(int id, String fecha_ini, String fecha_fin) {
        this.id = id;
        this.fecha_ini = fecha_ini;
        this.fecha_fin = fecha_fin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha_ini() {
        return fecha_ini;
    }

    public void setFecha_ini(String fecha_ini) {
        this.fecha_ini = fecha_ini;
    }

    public String getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(String fecha_fin) {
        this.fecha_fin = fecha_fin;
    }
}
