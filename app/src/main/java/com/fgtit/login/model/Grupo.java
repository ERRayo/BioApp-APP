package com.fgtit.login.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Grupo {

    private int id;
    private int idProfesor;
    private int idMateria;
    private int idPeriodo;
    private boolean estadoGrupo;

    public Grupo(int id, int idProfesor, int idMateria, int idPeriodo, boolean estadoGrupo) {
        this.id = id;
        this.idProfesor = idProfesor;
        this.idMateria = idMateria;
        this.idPeriodo = idPeriodo;
        this.estadoGrupo = estadoGrupo;
    }

    public static Grupo getGrupo(JSONObject jsonObject) throws JSONException {
        int id = jsonObject.getInt("id");
        int idProfesor = jsonObject.getInt("id_profesor");
        int idMateria = jsonObject.getInt("id_materia");
        int idPeriodo = jsonObject.getInt("id_periodo");
        boolean estadoGrupo = jsonObject.getBoolean("grupo_estado");

        Grupo grupo = new Grupo(id, idProfesor, idMateria, idPeriodo, estadoGrupo);

        return grupo;
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

    public int getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(int idMateria) {
        this.idMateria = idMateria;
    }

    public int getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(int idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public boolean isEstadoGrupo() {
        return estadoGrupo;
    }

    public void setEstadoGrupo(boolean estadoGrupo) {
        this.estadoGrupo = estadoGrupo;
    }
}
