package com.fgtit.login.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Huella {

    private int id;
    private int idProfesor;
    private String huella1;
    private String huella2;

    public Huella(int id, int idProfesor, String huella1, String huella2) {
        this.id = id;
        this.idProfesor = idProfesor;
        this.huella1 = huella1;
        this.huella2 = huella2;
    }

    public  static Huella getHuella(JSONObject jsonObject) throws JSONException {

        int id = jsonObject.getInt("id");
        int idProfesor = jsonObject.getInt("id_profesor");
        String huella1 = jsonObject.getString("huella_principal");
        String huella2 = jsonObject.getString("huella_secundaria");

        Huella huella = new Huella(id, idProfesor, huella1, huella2);

        return huella;
    }

    @Override
    public String toString () {
        return this.idProfesor + "(Huella Registrada)";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(int idProfesor) {
        this.idProfesor = idProfesor;
    }

    public String getHuella1() {
        return huella1;
    }

    public void setHuella1(String huella1) {
        this.huella1 = huella1;
    }

    public String getHuella2() {
        return huella2;
    }

    public void setHuella2(String huella2) {
        this.huella2 = huella2;
    }
}
