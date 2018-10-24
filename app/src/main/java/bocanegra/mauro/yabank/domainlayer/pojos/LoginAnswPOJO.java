package bocanegra.mauro.yabank.domainlayer.pojos;

import org.json.JSONObject;

public class LoginAnswPOJO extends AnswPOJO{

    public final static String keyAgente="agente";
    public final static String keyError = "error";
    public final static String keyIDUsuario = "id_user";
    public final static String keyToken = "token";

    String agente;
    String error;
    String idUsuario;
    String token;

    public LoginAnswPOJO(){}

    public String getAgente() {
        return agente;
    }

    public void setAgente(String agente) {
        this.agente = agente;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
