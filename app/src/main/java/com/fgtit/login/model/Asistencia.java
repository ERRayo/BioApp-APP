package com.fgtit.login.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Asistencia {

    private int id;
    private int idGrupo;
    private int idPrefecto;
    private String fecha;
    private String hora;
    private String descripcion;
    private boolean estadoAsis;

    public Asistencia(int id, int idGrupo, int idPrefecto, String fecha, String hora, String descripcion, boolean estadoAsis) {
        this.id = id;
        this.idGrupo = idGrupo;
        this.idPrefecto = idPrefecto;
        this.fecha = fecha;
        this.hora = hora;
        this.descripcion = descripcion;
        this.estadoAsis = estadoAsis;
    }

    public static Asistencia getAsistencia(JSONObject jsonObject) throws JSONException {
        int id = jsonObject.getInt("id");
        int idGrupo = jsonObject.getInt("id_grupo");
        int idPrefecto = jsonObject.getInt("id_prefecto");
        String fecha = jsonObject.getString("fecha");
        String hora = jsonObject.getString("hora");
        String descripcion = jsonObject.getString("descripcion");
        boolean estadoAssis = jsonObject.getBoolean("asistencia_estado");

        Asistencia asistencia = new Asistencia(id, idGrupo, idPrefecto, fecha, hora, descripcion, estadoAssis);

        return  asistencia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    public int getIdPrefecto() {
        return idPrefecto;
    }

    public void setIdPrefecto(int idPrefecto) {
        this.idPrefecto = idPrefecto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isEstadoAsis() {
        return estadoAsis;
    }

    public void setEstadoAsis(boolean estadoAsis) {
        this.estadoAsis = estadoAsis;
    }
}
