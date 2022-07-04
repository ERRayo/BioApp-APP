package com.fgtit.login.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Prefecto {


    private  int id;
    private int idUsuario;
    private String nombre;
    private String apellidoP;
    private String apellidoM;



    public Prefecto(int id, int idUsuario, String nombre, String apellidoP, String apellidoM) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellidoP = apellidoP;
        this.apellidoM = apellidoM;
    }

    public static Prefecto getPrefecto(JSONObject jsonObject) throws JSONException {

        int id = jsonObject.getInt("id");
        int idPrefecto = jsonObject.getInt("id_usuario");
        String nombre = jsonObject.getString("nombre");
        String apellidoP = jsonObject.getString("apellido_paterno");
        String apellidoM = jsonObject.getString("apellido_materno");

        Prefecto prefecto = new Prefecto(id, idPrefecto, nombre, apellidoP, apellidoM);

        return prefecto;
    }

    @Override
    public String toString () {
        return this.idUsuario + "(Prefecto)";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
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
}
