package com.fgtit.login.model.api;

import com.fgtit.login.model.Asistencia;
import com.fgtit.login.model.Grupo;
import com.fgtit.login.model.Materia;
import com.fgtit.login.model.Periodo;
import com.fgtit.login.model.Profesor;
import com.fgtit.login.model.User;
import com.fgtit.login.model.Huella;
import com.fgtit.login.model.Prefecto;
import com.fgtit.login.model.Huellas;
import com.fgtit.login.model.UserAccess;


public interface APIListener {
    void onLogin(User user);
    void onProfesor(Profesor profesor);
    void onHuella(Huella huella);
    void onPrefecto(Prefecto prefecto);
    void onHuellas(Huellas huellas);
    void onUserAcceess(UserAccess userAccess);
    void onPeriodo(Periodo periodo);
    void onGrupo(Grupo grupo);
    void onMateria(Materia materia);
    void onAsistencia(Asistencia asistencia);
}
