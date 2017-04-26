package finalproject.homie.DAL;

/**
 * Created by I311044 on 09/03/2017.
 */

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.config.RequestConfig;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import finalproject.homie.DO.BusinessEntity;
import finalproject.homie.controllers.LoginActivity;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class LoginTask extends AsyncTask<String, Void, String> {

    private final String mEmail;
    private final String mPassword;
    private LoginActivity activity;

    private final String url = "http://159.203.118.144:8081/login";
    public LoginTask(LoginActivity activity, String email, String password) {
        mEmail = email;
        mPassword = password;
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... params) {
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(3 * 1000).build();
        HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
        HttpPost request = new HttpPost(url);
        BusinessEntity[] results = null;
        try {
            request.setEntity(new StringEntity("{\"name\": \"" + params[0] + "\", \"password\":\"" + params[1] + "\"}"));

            // add request header
            request.addHeader("Content-type", "application/json");
            HttpResponse response = client.execute(request);

            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("Response Code : " + statusCode);

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }


            JSONObject obj = new JSONObject(result.toString());
            boolean success = obj.getBoolean("success");
            return success ? obj.getString("token") : "";
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(final String token) {
        activity.onLoginRequetReceived(token);
    }
}
