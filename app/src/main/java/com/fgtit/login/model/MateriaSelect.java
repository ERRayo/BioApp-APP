package com.fgtit.login.model;

public class MateriaSelect {

    int idGrupo;
    String materia;

    public MateriaSelect(int idGrupo, String materia) {
        this.idGrupo = idGrupo;
        this.materia = materia;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    @Override
    public String toString() {
        return "ID: " + idGrupo + " - " + materia;
    }
}
