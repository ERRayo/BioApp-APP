package com.fgtit.login.model.api;

public interface API {
    void login(String email, String password, APIListener listener);

    void profesor(String nombre, String apellidoP, String apellidoM, String numT, String huella1, String huella2, APIListener listener);

    void huella(int idProfesor, String huella1, String huella2, APIListener listener);

    void prefecto (int idUsuario, String nombre, String apellidoP, String apellidoM, APIListener listener);

    void huellas(APIListener listener);

    void usuarioId(APIListener listener);

    void periodoGet(APIListener listener);

    void prefectoIdUser(int id, APIListener listener);

    void grupo(int idProfesor, int idPeriodo, APIListener listener);

    void asistencia(int idGrupo, int idPrefecto, String descripcion, boolean estadoAssis, APIListener listener);

    void profesorId(int id_profesor, APIListener listener);

    void huellaIdProfesor(int id_profesor, APIListener listener);

    void profesorPUT(int id, String nombre, String apellidoP, String apellidoM, String numT, APIListener listener);

    void huellasPUT(int id, int idProfesor, String huella1, String huella2, APIListener listener);

    void huellasPOST(int idProfesor, String huella1, String huella2, APIListener listener);

}
