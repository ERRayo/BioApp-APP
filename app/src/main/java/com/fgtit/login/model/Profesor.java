package com.fgtit.login.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Profesor {

    private int id;
    private String nombre;
    private String apellidoP;
    private String apellidoM;
    private String numT;

    public Profesor(int id, String nombre, String apellidoP, String apellidoM, String numT) {
        this.id = id;
        this.nombre = nombre;
        this.apellidoP = apellidoP;
        this.apellidoM = apellidoM;
        this.numT = numT;
    }

    public  static Profesor getProfesor(JSONObject jsonObject) throws JSONException {

        int id = jsonObject.getInt("id");
        String nombre = jsonObject.getString("nombre");
        String apellidoP = jsonObject.getString("apellido_paterno");
        String apellidoM = jsonObject.getString("apellido_materno");
        String numT = jsonObject.getString("no_trabajador");

        Profesor profesor = new Profesor(id, nombre, apellidoP, apellidoM, numT);

        return profesor;
    }

    @Override
    public String toString () {
        return this.id + " - " + this.nombre +" "+ this.apellidoP +" "+ this.apellidoM;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoP() {
        return apellidoP;
    }

    public void setApellidoP(String apellidoP) {
        this.apellidoP = apellidoP;
    }

    public String getApellidoM() {
        return apellidoM;
    }

    public void setApellidoM(String apellidoM) {
        this.apellidoM = apellidoM;
    }

    public String getNumT() {
        return numT;
    }

    public void setNumT(String numT) {
        this.numT = numT;
    }

}
