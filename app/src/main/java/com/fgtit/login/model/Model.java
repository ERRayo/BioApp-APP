package com.fgtit.login.model;

import android.app.Application;


import com.fgtit.login.model.api.APIListener;
import com.fgtit.login.model.api.AbstractAPIListener;
import com.fgtit.login.model.api.WebAPI;
import com.fgtit.login.model.api.API;

public class Model {

    private static Model sInstance = null;
    private final API mApi;
    private User mUser;
    private Profesor mProfesor;
    private Huella mHuella;
    private Prefecto mPrefecto;
    private Huellas mHuellas;
    private Periodo mPerido;
    private UserAccess mUserAccess;
    private Grupo mGrupo;
    private Materia mMateria;
    private Asistencia mAsistencia;
    private Profesor mProfesorID;


    public static Model getInstance(Application application) {
        if (sInstance == null) {
            sInstance = new Model(application);
        }
        return  sInstance;
    }

    private final Application mApplication;

    private Model(Application application) {
        mApplication = application;
        mApi = new WebAPI(mApplication, this);
    }

    public  Application getmApplication() {
        return  mApplication;
    }

    //---Login
    public void login(String email, String password, final APIListener listener) {
        mApi.login(email, password, listener);
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        this.mUser = user;
    }

    //---Profesor
    public void profesor(String nombre, String apellidoP, String apellidoM, String numT, String huella1, String huella2, final APIListener listener)  {
        mApi.profesor(nombre, apellidoP, apellidoM, numT, huella1, huella2, listener);
    }

    public Profesor getProfesor() {
        return mProfesor;
    }

    public void setProfesor(Profesor profesor) {
        this.mProfesor = profesor;
    }

    //---Huella
    public void huella(int idProfespr, String huella1, String huella2, final  APIListener listener) {
        mApi.huella(idProfespr, huella1, huella2, listener);
    }

    public Huella getHuella() {
        return mHuella;
    }

    public void setHuella(Huella huella) {
        this.mHuella = huella;
    }

    //---- Prefecto
    public  void prefecto(int idUsuario, String nombre, String apellidoP, String apellidoM, final  APIListener listener) {
        mApi.prefecto(idUsuario, nombre, apellidoP, apellidoM, listener);
    }

    public Prefecto getPrefecto() {
        return mPrefecto;
    }

    public void setPrefecto(Prefecto prefecto) {
        this.mPrefecto = prefecto;
    }

    //--- Huellas-----------------------------------------------------------------------------------
    public void huellas(final  APIListener listener) {
        mApi.huellas(listener);
    }

    public Huellas getHuellas() {
        return mHuellas;
    }

    public void setHuellas(Huellas huellas) {
        this.mHuellas = huellas;
    }

    //--- GET id user de la sesion -----------------------------------------------------------------
    public void  prefectoID(final APIListener listener) {
        mApi.usuarioId(listener);
    }

    public UserAccess getUserAccess() {
        return mUserAccess;
    }

    public void setUserAccess(UserAccess userAccess) {
        this.mUserAccess = userAccess;
    }

    //---Periodo -----------------------------------------------------------------------------------
    public void periodo(final  APIListener listener) {
        mApi.periodoGet(listener);
    }

    public Periodo getPerido() {
        return mPerido;
    }

    public void setPerido(Periodo mPerido) {
        this.mPerido = mPerido;
    }
    //--- Get id prefeccto -------------------------------------------------------------------------
    public void prefectoIdUser(int id, final  APIListener listener) {
        mApi.prefectoIdUser(id, listener);
    }
    //--- Get Grupo --------------------------------------------------------------------------------
    public void grupo(int id_profesor, int idPeriodo, final APIListener listener) {
        mApi.grupo(id_profesor, idPeriodo, listener);
    }

    public Grupo getGrupo() {
        return mGrupo;
    }

    public void setGrupo(Grupo grupo) {
        this.mGrupo = grupo;
    }
    //--- Materia

    public Materia getMateria() {
        return mMateria;
    }

    public void setMateria(Materia materia) {
        this.mMateria = materia;
    }
    //--- Registro de asistencia -------------------------------------------------------------------
    public void asistencia(int idGrupo, int idPrefecto, String descripcion, boolean estadoAssis, final APIListener listener) {
        mApi.asistencia(idGrupo, idPrefecto, descripcion, estadoAssis, listener);
    }

    public Asistencia getAsistencia() {
        return mAsistencia;
    }

    public void setAsistencia(Asistencia asistencia) {
        this.mAsistencia = asistencia;
    }

    //--- Profesor por ID
    public void profesorID(int id_profesor, final APIListener listener) {
        mApi.profesorId(id_profesor, listener);
    }
    //--- Huella por id de profesor
    public void huellaIdProfesor(int id_profesor, final APIListener listener) {
        mApi.huellaIdProfesor(id_profesor, listener);
    }
    //--- Profesor PUT
    public void profesorPUT(int id, String nombre, String apellidoP, String apellidoM, String numT, final APIListener listener) {
        mApi.profesorPUT(id, nombre, apellidoP, apellidoM, numT, listener);
    }
    //--- huellas PUT
    public void huellasPUT(int id,int idProfesor, String huella1, String huella2, final APIListener listener) {
        mApi.huellasPUT(id, idProfesor, huella1, huella2, listener);
    }
    //--- huellas POST
    public void huellasPOST(int idProfesor, String huella1, String huella2, final APIListener listener) {
        mApi.huellasPOST(idProfesor, huella1, huella2, listener);

    }
}
