package com.fgtit.login.model;

public class MateriaSelect {

    int idGrupo;
    String materia;
    String aula;

    public MateriaSelect(int idGrupo, String materia, String aula) {
        this.idGrupo = idGrupo;
        this.materia = materia;
        this.aula = aula;
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

    public String getAula() { return aula; }

    public void setAula(String aula) { this.aula = aula; }

    @Override
    public String toString() {
        return "ID: " + idGrupo + " - " + materia;
    }
}
