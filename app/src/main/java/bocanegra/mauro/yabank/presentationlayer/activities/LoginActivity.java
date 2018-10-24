package bocanegra.mauro.yabank.presentationlayer.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONObject;

import bocanegra.mauro.yabank.R;
import bocanegra.mauro.yabank.domainlayer.WS;
import bocanegra.mauro.yabank.domainlayer.pojos.AnswPOJO;
import bocanegra.mauro.yabank.domainlayer.pojos.LoginAnswPOJO;
import bocanegra.mauro.yabank.domainlayer.pojos.LoginPOJO;

public class LoginActivity extends AppCompatActivity implements WS.OnWSRequested {

    TextInputLayout textInputLayoutUsuario;
    TextInputEditText editTextUsuario;
    TextInputLayout textInputLayoutContra;
    TextInputEditText editTextContra;
    Button buttonIniciarSesion;
    View progress;
    View container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        instanciate();
        setListeners();

    }

    private void setListeners() {
        buttonIniciarSesion.setOnClickListener(buttonIniciarSesionListener());
    }

    private void instanciate(){
        textInputLayoutUsuario = findViewById(R.id.login_textinputlayout_usuario);
        textInputLayoutContra = findViewById(R.id.login_textinputlayout_contra);
        editTextUsuario = findViewById(R.id.login_edittext_usuario);
        editTextContra = findViewById(R.id.login_edittext_contra);
        buttonIniciarSesion = findViewById(R.id.login_button_iniciarsesion);
        progress = findViewById(R.id.login_progress);
        container = findViewById(R.id.login_container);
    }

    // --------------------------------------------- //
    // -------------- CUSTOM METHODS --------------- //
    // --------------------------------------------- //

    private View.OnClickListener buttonIniciarSesionListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkForErrors()==0){
                    WS.hideKeyboard(LoginActivity.this);
                    performLoginRequest();
                }
            }
        };
    }

    private int checkForErrors(){
        int errors = 0;
        if(editTextUsuario.getEditableText().toString().length()==0){
            textInputLayoutUsuario.setError(getString(R.string.login_error_usuario));
            errors++;
        }else{
            textInputLayoutUsuario.setError(null);
        }

        if(editTextContra.getEditableText().toString().length()==0){
            textInputLayoutContra.setError(getString(R.string.login_error_contra));
            errors++;
        }else{
            textInputLayoutContra.setError(null);
        }

        return errors;
    }

    private void performLoginRequest(){
        progress.setVisibility(View.VISIBLE);
        LoginPOJO loginPojo = new LoginPOJO();
        loginPojo.setUser(editTextUsuario.getEditableText().toString());
        loginPojo.setPass(editTextContra.getEditableText().toString());

        WS.signIn(LoginActivity.this, loginPojo, LoginActivity.this);
    }

    // --------------------------------------------- //
    // -------------- WS IMPLEMENTATION --------------- //
    // --------------------------------------------- //

    @Override
    public void wsAnswered(AnswPOJO answPOJO) {
        progress.setVisibility(View.GONE);
        LoginAnswPOJO loginAnswPOJO = (LoginAnswPOJO)answPOJO;
        if(loginAnswPOJO.getError().equals("null")){
            Log.d("LoginActivityError","TODO BIEN!");
            SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(getString(R.string.sharedPreferences),Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(getString(R.string.keyLogin),true);
            editor.commit();

            Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        }else{
            try{
                JSONObject jsonError = new JSONObject(loginAnswPOJO.getError());
                WS.showError(jsonError.getString("message"), container);
            }catch(Exception e){e.printStackTrace();}
        }
    }
}
