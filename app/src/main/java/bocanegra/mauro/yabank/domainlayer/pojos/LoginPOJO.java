package bocanegra.mauro.yabank.domainlayer.pojos;

import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginPOJO {

    final String keyUser = "user";
    final String keyPassword = "pass";
    final String keyData = "data";

    String user;
    String pass;

    public LoginPOJO(){}

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Map<String, String> toMap(){
        Map<String, String> map = new HashMap<>();
        Map<String, String> data = new HashMap<>();
        data.put(keyUser,user);
        data.put(keyPassword, pass);
        map.put(keyData, data.toString());
        Log.d("LoginPOJO,",map.toString());
        return map;
    }

    public JSONObject toJSONObject(){
        JSONObject json=null;
        try{
            json = new JSONObject();
            JSONObject params = new JSONObject();
            params.put(keyUser,user);
            params.put(keyPassword,pass);
            json.put(keyData,params);
        }catch(Exception e){
            e.printStackTrace();
        }
        return json;
    }
}
