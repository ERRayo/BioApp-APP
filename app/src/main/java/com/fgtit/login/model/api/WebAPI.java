package com.fgtit.login.model.api;

import android.app.Application;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fgtit.login.model.Asistencia;
import com.fgtit.login.model.Grupo;
import com.fgtit.login.model.Materia;
import com.fgtit.login.model.Model;
import com.fgtit.login.model.Periodo;
import com.fgtit.login.model.Prefecto;
import com.fgtit.login.model.User;
import com.fgtit.login.model.Profesor;
import com.fgtit.login.model.Huella;
import com.fgtit.login.model.Huellas;
import com.fgtit.login.model.UserAccess;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebAPI implements API {

    public static final String BASE_URL = "http://192.168.0.7:5000/";

    private final Application mApplication;

    private RequestQueue mRequestQueue;

    private Model mModel;

    List<Integer> datos;

    public WebAPI (Application application, Model model) {
        mApplication = application;
        mRequestQueue = Volley.newRequestQueue(application);
        mModel = model;

    }

    public void login(String email, String password, final APIListener listener) {

        String url = BASE_URL + "auth/user/login";
        JSONObject jsonObject = new JSONObject();

       try {

            jsonObject.put("email", email);
            jsonObject.put("password", password);

            Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        JSONObject userObj = response.getJSONObject("user");

                        User user = User.getUser(userObj);

                        listener.onLogin(user);

                    } catch (JSONException e) {

                        Toast.makeText(mApplication, "JSON exception 1", Toast.LENGTH_LONG).show();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(mApplication, "Error response", Toast.LENGTH_LONG).show();
                }
            };

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, successListener, errorListener);
            mRequestQueue.add(request);


        } catch (JSONException e) {
            Toast.makeText(mApplication, "JSON exception ", Toast.LENGTH_LONG).show();
        }
    }

    public  void  profesor(String nombre, String apellidoP, String apellidoM, String numT, String huella1, String huella2, final  APIListener listener) {
        String url = BASE_URL + "profesor/huella";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("nombre", nombre);
            jsonObject.put("apellido_paterno", apellidoP);
            jsonObject.put("apellido_materno", apellidoM);
            jsonObject.put("no_trabajador", numT);

            jsonObject.put("huella_principal", huella1);
            jsonObject.put("huella_secundaria", huella2);

            Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        JSONObject profesorObj = response.getJSONObject("profesor");

                        Profesor profesor = Profesor.getProfesor(profesorObj);

                        listener.onProfesor(profesor);

                    } catch (JSONException e) {

                        Toast.makeText(mApplication, "JSON exception AQUi 1", Toast.LENGTH_LONG).show();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(mApplication, "Error response", Toast.LENGTH_LONG).show();
                }
            };

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, successListener, errorListener);
            mRequestQueue.add(request);

        } catch (JSONException e) {
            Toast.makeText(mApplication, "JSON exception 2", Toast.LENGTH_LONG).show();
        }
    }

    public void huella(int idPrfesor, String huella1, String huella2, final APIListener listener) {
        String url = BASE_URL + "huella";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id_profesor", idPrfesor);
            jsonObject.put("huella_principal", huella1);
            jsonObject.put("huella_secundaria", huella2);

            Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        Huella huella = Huella.getHuella(response);

                        listener.onHuella(huella);

                    } catch (JSONException e) {

                        Toast.makeText(mApplication, "JSON exception 1", Toast.LENGTH_LONG).show();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(mApplication, "Error response", Toast.LENGTH_LONG).show();
                }
            };

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, successListener, errorListener);
            mRequestQueue.add(request);

        } catch (JSONException e) {
            Toast.makeText(mApplication, "JSON exception 2", Toast.LENGTH_LONG).show();
        }
    }

    public void prefecto(int idUsuario, String nombre, String apellidoP, String apellidoM, APIListener listener) {

    }
    //---GET prefecto usando id user ---------------------------------------------------------------
    public  void profesorGetId(int idProfesor, final APIListener listener) {
        String url = BASE_URL + "prefecto/" + idProfesor;

            Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        Profesor profesor = Profesor.getProfesor(response);

                        listener.onProfesor(profesor);

                    } catch (JSONException e) {

                        Toast.makeText(mApplication, "JSON exception Prefecto ID user", Toast.LENGTH_LONG).show();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(mApplication, "Error response", Toast.LENGTH_LONG).show();
                }
            };

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, successListener, errorListener);
            mRequestQueue.add(request);

    }
    //---Arreglo de huellas ------------------------------------------------------------------------
    public void huellas(final APIListener listener) {
        String url = BASE_URL + "huella";

        datos = new ArrayList<Integer>();

        Response.Listener<JSONArray> successListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject obj = response.getJSONObject(i);

                        Huellas huella = new Huellas();

                        huella.setIdProfesor(obj.getInt("id_profesor"));

                        datos.add(huella.getIdProfesor());

                        Toast.makeText(mApplication, "Correcto", Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {

                    Toast.makeText(mApplication, "JSON exception huellas", Toast.LENGTH_LONG).show();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mApplication, "Error response", Toast.LENGTH_LONG).show();
            }
        };

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, successListener, errorListener);
        mRequestQueue.add(request);
    }
    //---Get ID user--------------------------------------------------------------------------------
    public void usuarioId(APIListener listener) {
        String url = BASE_URL + "auth/user/me";

        Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    UserAccess userAccess = UserAccess.getUser(response);

                    listener.onUserAcceess(userAccess);

                } catch (JSONException e) {

                    Toast.makeText(mApplication, "JSON exception 1", Toast.LENGTH_LONG).show();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mApplication, "Error response", Toast.LENGTH_LONG).show();
            }
        };

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, successListener, errorListener) {
          public Map<String, String> getHeaders() {
              Map<String, String> headers = new HashMap<>();
              headers.put("Content-Type", "application/json");
              headers.put("Accept", "application/json");
              headers.put("Authorization", "Bearer " + mModel.getUser().getAccess());
              return headers;
          }
        };
        mRequestQueue.add(request);


    }
    //---Get periodo -------------------------------------------------------------------------------
    public void periodoGet(APIListener listener) {
        String url = BASE_URL + "periodo/actual";

        Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    Periodo periodo = Periodo.getPeriodo(response);

                    listener.onPeriodo(periodo);

                } catch (JSONException e) {

                    Toast.makeText(mApplication, "JSON exception 1", Toast.LENGTH_LONG).show();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mApplication, "Error response", Toast.LENGTH_LONG).show();
            }
        };

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, successListener, errorListener);
        mRequestQueue.add(request);
    }
    //---GET prefecto usando id user ---------------------------------------------------------------
    public void prefectoIdUser(int id, APIListener listener) {
        String url = BASE_URL + "prefecto/user/" + id;

        Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    Prefecto prefecto = Prefecto.getPrefecto(response);

                    listener.onPrefecto(prefecto);

                } catch (JSONException e) {

                    Toast.makeText(mApplication, "JSON exception Prefecto ID user", Toast.LENGTH_LONG).show();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mApplication, "Error response", Toast.LENGTH_LONG).show();
            }
        };

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, successListener, errorListener);
        mRequestQueue.add(request);
    }

    public void grupo(int idProfesor, int idPeriodo, APIListener listener) {
        String url = BASE_URL + "grupo/profesor/fecha/" + idProfesor + "/" + idPeriodo;

        Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    Grupo grupo = Grupo.getGrupo(response);

                    listener.onGrupo(grupo);

                    JSONObject materiaObj = response.getJSONObject("materia");
                    Materia materia = Materia.getMateria(materiaObj);

                    listener.onMateria(materia);

                } catch (JSONException e) {

                    Toast.makeText(mApplication, "JSON exception Prefecto ID user", Toast.LENGTH_LONG).show();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mApplication, "Error response", Toast.LENGTH_LONG).show();
            }
        };

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, successListener, errorListener);
        mRequestQueue.add(request);
    }

    //--- Registrar Asistencia
    public void asistencia(int idGrupo, int idPrefecto, String descripcion, boolean estadoAssis, APIListener listener) {
        String url = BASE_URL + "asistencia";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id_grupo", idGrupo);
            jsonObject.put("id_prefecto", idPrefecto);
            jsonObject.put("descripcion", descripcion);
            jsonObject.put("asistencia_estado", estadoAssis);


            Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {


                        Asistencia asistencia = Asistencia.getAsistencia(response);

                        listener.onAsistencia(asistencia);

                        Toast.makeText(mApplication, "Asistencia registrada!", Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {

                        Toast.makeText(mApplication, "JSON exception Asistencia", Toast.LENGTH_LONG).show();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(mApplication, "Error response", Toast.LENGTH_LONG).show();
                }
            };

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, successListener, errorListener);
            mRequestQueue.add(request);

        } catch (JSONException e) {
            Toast.makeText(mApplication, "JSON exception 2", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void profesorId(int id_profesor, APIListener listener) {
        String url = BASE_URL + "profesor/" + id_profesor;

        Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    Profesor profesor = Profesor.getProfesor(response);

                    listener.onProfesor(profesor);

                } catch (JSONException e) {

                    Toast.makeText(mApplication, "JSON exception Profesor ID user", Toast.LENGTH_LONG).show();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mApplication, "Error response", Toast.LENGTH_LONG).show();
            }
        };

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, successListener, errorListener);
        mRequestQueue.add(request);

    }

    @Override
    public void huellaIdProfesor(int id_profesor, APIListener listener) {
        String url = BASE_URL + "huella/profesor/" + id_profesor;

        Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    Huella huella = Huella.getHuella(response);

                    listener.onHuella(huella);

                } catch (JSONException e) {

                    Toast.makeText(mApplication, "Sin huellas asigandas", Toast.LENGTH_LONG).show();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mApplication, "Error response", Toast.LENGTH_LONG).show();
            }
        };

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, successListener, errorListener);
        mRequestQueue.add(request);

    }

    @Override
    public void profesorPUT(int id, String nombre, String apellidoP, String apellidoM, String numT, APIListener listener) {
        String url = BASE_URL + "profesor/" + id;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("nombre", nombre);
            jsonObject.put("apellido_paterno", apellidoP);
            jsonObject.put("apellido_materno", apellidoM);
            jsonObject.put("no_trabajador", numT);

            Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        Profesor profesor = Profesor.getProfesor(response);

                        listener.onProfesor(profesor);

                    } catch (JSONException e) {

                        Toast.makeText(mApplication, "JSON Error en profesor", Toast.LENGTH_LONG).show();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(mApplication, "Error response", Toast.LENGTH_LONG).show();
                }
            };

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, successListener, errorListener);
            mRequestQueue.add(request);

        } catch (JSONException e) {
            Toast.makeText(mApplication, "JSON variables profesor", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void huellasPUT(int id, int idProfesor, String huella1, String huella2, APIListener listener) {
        String url = BASE_URL + "huella/" + id;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id_profesor", idProfesor);
            jsonObject.put("huella_principal", huella1);
            jsonObject.put("huella_secundaria", huella2);

            Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        Huella huella = Huella.getHuella(response);

                        listener.onHuella(huella);

                    } catch (JSONException e) {

                        Toast.makeText(mApplication, "JSON Error en huella", Toast.LENGTH_LONG).show();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(mApplication, "Error response", Toast.LENGTH_LONG).show();
                }
            };

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, successListener, errorListener);
            mRequestQueue.add(request);

        } catch (JSONException e) {
            Toast.makeText(mApplication, "JSON variables huellas PUT", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void huellasPOST(int idProfesor, String huella1, String huella2, APIListener listener) {
        String url = BASE_URL + "huella";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id_profesor", idProfesor);
            jsonObject.put("huella_principal", huella1);
            jsonObject.put("huella_secundaria", huella2);

            Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        Huella huella = Huella.getHuella(response);

                        listener.onHuella(huella);

                    } catch (JSONException e) {

                        Toast.makeText(mApplication, "JSON Error en huella", Toast.LENGTH_LONG).show();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(mApplication, "Error response", Toast.LENGTH_LONG).show();
                }
            };

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, successListener, errorListener);
            mRequestQueue.add(request);

        } catch (JSONException e) {
            Toast.makeText(mApplication, "JSON variables profesor", Toast.LENGTH_LONG).show();
        }

    }


}
