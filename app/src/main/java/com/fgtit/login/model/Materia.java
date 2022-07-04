package com.fgtit.login.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Materia {

    private int id;
    private int idCarrera;
    private String nombre;

    public Materia(int id, int idCarrera, String nombre) {
        this.id = id;
        this.idCarrera = idCarrera;
        this.nombre = nombre;
    }

    public static Materia getMateria(JSONObject jsonObject) throws JSONException {
        int id = jsonObject.getInt("id");
        int idCarrera = jsonObject.getInt("id_carrera");
        String nombre = jsonObject.getString("nombre");

        Materia materia = new Materia(id, idCarrera, nombre);

        return materia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCarrera() {
        return idCarrera;
    }

    public void setIdCarrera(int idCarrera) {
        this.idCarrera = idCarrera;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
