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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bocanegra.mauro.yabank.R;
import bocanegra.mauro.yabank.domainlayer.db.AppDatabase;
import bocanegra.mauro.yabank.domainlayer.db.CompanyDAO;
import bocanegra.mauro.yabank.domainlayer.db.CompanyPOJO_DB;
import bocanegra.mauro.yabank.domainlayer.pojos.AnswPOJO;
import bocanegra.mauro.yabank.domainlayer.pojos.LoginAnswPOJO;
import bocanegra.mauro.yabank.domainlayer.pojos.LoginPOJO;
import bocanegra.mauro.yabank.presentationlayer.fragments.RecargasFragment;

public class WS_DB {

    public static final String TAG = "WSDebug";
    public static final String urlLogin = "https://agentemovil.pagatodo.com/AgenteMovil.svc/agMov/login";

    static AppDatabase database;
    static CompanyDAO companyDAO;

    private static WS_DB instance;

    public synchronized  static WS_DB getInstance(){
        if(instance != null){
            instance = new WS_DB();
        }

        return instance;
    }

    // ---------------------------------------------------- //
    // -------------- WEB SERVICES METHODS  --------------- //
    // ---------------------------------------------------- //

    public static void signIn(Context c, LoginPOJO loginPOJO, OnWSRequested onWSRequested){
        performRequest(c, urlLogin, loginPOJO.toJSONObject(), onWSRequested);
    }

    // ------------------------------------------ //
    // -------------- DB METHODS  --------------- //
    // ------------------------------------------ //

    public static void instanciateDB(Activity activity, OnDBAllRequested onDBAllRequested){
        database = AppDatabase.prePopulateDB(activity);
        companyDAO = database.getCompanyDAO();

        DBTask dbTask = new DBTask(activity, onDBAllRequested);
        dbTask.execute();
    }

    // ---------------------------------------------------------- //
    // -------------- WEB SERVICES IMPLEMENTATION --------------- //
    // ---------------------------------------------------------- //

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

    // ------------------------------------------------ //
    // -------------- DB IMPLEMENTATION --------------- //
    // ------------------------------------------------ //

    private static class DBTask extends AsyncTask<Void, Void, List<CompanyPOJO_DB>> {

        private WeakReference<Activity> weakActivity;
        private OnDBAllRequested onDBAllRequested;

        public DBTask(Activity activity, OnDBAllRequested onDBListener){
            weakActivity = new WeakReference<>(activity);
            this.onDBAllRequested = onDBListener;
        }

        @Override
        protected List<CompanyPOJO_DB> doInBackground(Void... voids) {
            return companyDAO.getCompanies();
        }

        @Override
        protected void onPostExecute(List<CompanyPOJO_DB> companies) {
            super.onPostExecute(companies);
            ArrayList<CompanyPOJO_DB> companiesArr = new ArrayList<>(companies);
            onDBAllRequested.dbAnswered(companiesArr);
        }
    }

    // ------------------------------------------- //
    // -------------- LISTENING  --------------- //
    // ------------------------------------------- //

    public interface OnWSRequested{
        public void wsAnswered(AnswPOJO answPOJO);
    }

    public interface OnDBAllRequested{
        public void dbAnswered(ArrayList<CompanyPOJO_DB> arrayList);
    }

    // --------------------------------------- //
    // -------------- MESSAGES --------------- //
    // --------------------------------------- //

    public static void showError(String msg, View view) {

        Snackbar snack = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
        View snackBarView = snack.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorPrimary));
        snack.setAction("Action", null).show();
        snack.show();
    }

    // --------------------------------------- //
    // -------------- KEYBOARD --------------- //
    // --------------------------------------- //

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
