package bocanegra.mauro.yabank.domainlayer;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import bocanegra.mauro.yabank.R;
import bocanegra.mauro.yabank.domainlayer.pojos.AnswPOJO;
import bocanegra.mauro.yabank.domainlayer.pojos.LoginAnswPOJO;
import bocanegra.mauro.yabank.domainlayer.pojos.LoginPOJO;

public class WS {

    public static final String TAG = "WSDebug";
    public static final String urlLogin = "https://agentemovil.pagatodo.com/AgenteMovil.svc/agMov/login";

    private static WS instance;

    public synchronized  static WS getInstance(){
        if(instance != null){
            instance = new WS();
        }

        return instance;
    }

    // ------------------------------------------- //
    // -------------- WEB SERVICES --------------- //
    // ------------------------------------------- //

    public static void signIn(Context c, LoginPOJO loginPOJO, OnWSRequested onWSRequested){
        performRequest(c, urlLogin, loginPOJO.toJSONObject(), onWSRequested);
    }

    // ------------------------------------------- //
    // -------------- IMPLEMENTATION --------------- //
    // ------------------------------------------- //

    private static JSONObject performRequest(Context c, String urlString, JSONObject json, final OnWSRequested onWSRequested){

        RequestQueue queue = Volley.newRequestQueue(c);

        JsonObjectRequest req = new JsonObjectRequest(urlString, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "onWSResponse = "+response);

                try {
                    String error = response.get("error").toString();
                    Log.d(TAG, "onWSResponseERROR = "+error);
                    LoginAnswPOJO loginAnswPOJO = new LoginAnswPOJO();
                    if(!error.matches("null")){
                        loginAnswPOJO.setAgente(response.getString(LoginAnswPOJO.keyAgente));
                        loginAnswPOJO.setIdUsuario(response.getString(LoginAnswPOJO.keyIDUsuario));
                        loginAnswPOJO.setToken(response.getString(LoginAnswPOJO.keyToken));
                        loginAnswPOJO.setError(error);
                    }else{
                        loginAnswPOJO.setError(error);
                    }
                    onWSRequested.wsAnswered(loginAnswPOJO);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onWSErrorResponse"+error.getMessage());

                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        Log.e(TAG, res);
                        //JSONObject obj = new JSONObject(res);
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    }
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("SO","Android");
                params.put("Version","2.5.2");
                return params;
            }
        };
        queue.add(req);

        return null;
    }

    private static class Task extends AsyncTask<Void,Void,JSONObject>{
        @Override
        protected JSONObject doInBackground(Void... voids) {
            return null;
        }
    }

    // ------------------------------------------- //
    // -------------- LISTENING  --------------- //
    // ------------------------------------------- //

    public interface OnWSRequested{
        public void wsAnswered(AnswPOJO answPOJO);
    }

    // ------------------------------------------- //
    // -------------- MESSAGES --------------- //
    // ------------------------------------------- //

    public static void showError(String msg, View view) {

        Snackbar snack = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
        View snackBarView = snack.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorPrimary));
        snack.setAction("Action", null).show();
        snack.show();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
