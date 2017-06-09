package finalproject.homie.DAL;

import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.TimeoutException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.ProtocolVersion;
import cz.msebera.android.httpclient.StatusLine;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.config.RequestConfig;
import cz.msebera.android.httpclient.client.methods.HttpEntityEnclosingRequestBase;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.methods.HttpPut;
import cz.msebera.android.httpclient.client.methods.HttpRequestBase;
import cz.msebera.android.httpclient.conn.ConnectTimeoutException;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.entity.mime.content.StringBody;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import finalproject.homie.DO.Assignment;
import finalproject.homie.DO.BusinessEntity;
import finalproject.homie.DO.Course;
import finalproject.homie.DO.Task;
import finalproject.homie.DO.User;
import finalproject.homie.adapters.BaseAdapter;
import finalproject.homie.controllers.BaseApplication;
import finalproject.homie.controllers.IDataResponseHandler;

/**
 * Created by I311044 on 04/03/2017.
 */

public class CallServerFunction extends BaseDAL {

    private String url;
    private String body;

    public CallServerFunction(String token, IDataResponseHandler handler) {
        super(token, handler);
    }

    public void login(String username, String password) {
        url = baseUrl + "login";
        body = "{\"name\": \"" + username + "\", \"password\":\"" + password + "\"}";
        execute();
    }

    public void addOrDeleteUserToAssignment(Assignment assignment, String username, boolean isAdd) {
        url = baseUrl + "api/addOrDeleteUserToAssignment/" + assignment.getID();
        body = "{\"name\": \"" + username + "\", \"isAdd\": " + isAdd + "}";
        execute();
    }

    @Override
    public HttpRequestBase createHttpRequest() throws JSONException {
        HttpPost request = new HttpPost(url);
        request.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
        return request;
    }

    @Override
    public MyResponse handleResponse(HttpResponse response) throws IOException {
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        JSONObject obj = null;
        try {
            obj = new JSONObject(result.toString());
        } catch (JSONException ex) {
            // Do nothing
        }

        return new MyResponse(result.toString(), obj, response.getStatusLine().getStatusCode());
    }
}