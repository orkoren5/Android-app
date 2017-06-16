package finalproject.homie.DAL;

import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;

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

public class DataAppender<T extends BusinessEntity> extends BaseDAL {

    private T obj = null;
    private String url;
    private String method;

    public DataAppender(String token, IDataResponseHandler handler) {
        super(token, handler);
    }

    public void updateUser(User user) {
        url = baseUrl + "api/users/" + user.getID();
        method = "PUT";
        obj = (T) user;
        execute();
    }

    public void addAssignment(Assignment assignment, boolean isNew) {
        url = baseUrl + "api/assignments";
        url += isNew ? "" : "/" + assignment.getID();
        method = isNew ? "POST" : "PUT";
        obj = (T) assignment;
        execute();
    }

    public void addTask(Task task, boolean isNew) {
        url = baseUrl + "api/tasks";
        url += isNew ? "" : "/" + task.getID();
        method = isNew ? "POST" : "PUT";
        obj = (T) task;
        execute();
    }

    @Override
    public HttpEntityEnclosingRequestBase createHttpRequest() throws JSONException {
        HttpEntityEnclosingRequestBase request = method.equals("POST") ?
                new HttpPost(url) : new HttpPut(url);
        request.addHeader("homie-foreign-ids", obj.getForeignIdFields());
        request.setEntity(new StringEntity(obj.toJSON().toString(), ContentType.APPLICATION_JSON));
        return request;
    }

    @Override
    public MyResponse handleResponse(HttpResponse response) throws IOException {
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("Response Code : " + statusCode);

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        if (statusCode == 200 && method.equals("POST")) {
            handleSuccess(result);
        }
        return new MyResponse("", null, statusCode);
    }

    private void handleSuccess(StringBuffer response) {
        String objId = response.toString();
        synchronized (obj) {
            obj.setID(objId);
            obj.notifyAll();
        }
    }
}