package com.fgtit.login.model.api;

import com.fgtit.login.model.Asistencia;
import com.fgtit.login.model.Grupo;
import com.fgtit.login.model.Materia;
import com.fgtit.login.model.Periodo;
import com.fgtit.login.model.Prefecto;
import com.fgtit.login.model.Profesor;
import com.fgtit.login.model.User;
import com.fgtit.login.model.Huella;
import com.fgtit.login.model.Huellas;
import com.fgtit.login.model.UserAccess;

public class AbstractAPIListener implements  APIListener{
    @Override
    public void onLogin(User user) {

    }

    @Override
    public void onProfesor(Profesor profesor) {

    }

    @Override
    public void onHuella(Huella huella) {

    }

    @Override
    public  void  onPrefecto(Prefecto prefecto){

    }

    @Override
    public void onHuellas(Huellas huellas) {

    }

    @Override
    public void onUserAcceess(UserAccess userAccess) {

    }

    @Override
    public void onPeriodo(Periodo periodo) {

    }

    @Override
    public void onGrupo(Grupo grupo) {

    }

    @Override
    public void onMateria(Materia materia) {

    }

    @Override
    public void onAsistencia(Asistencia asistencia) {

    }

}
