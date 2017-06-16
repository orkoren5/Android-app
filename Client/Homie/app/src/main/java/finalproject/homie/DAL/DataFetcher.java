package finalproject.homie.DAL;

import android.content.Intent;
import android.os.AsyncTask;

import cz.msebera.android.httpclient.StatusLine;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.config.RequestConfig;
import cz.msebera.android.httpclient.client.methods.HttpEntityEnclosingRequestBase;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.methods.HttpPut;
import cz.msebera.android.httpclient.client.methods.HttpRequestBase;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import org.json.*;

import finalproject.homie.DO.Assignment;
import finalproject.homie.DO.BusinessEntity;
import finalproject.homie.DO.Course;
import finalproject.homie.DO.User;
import finalproject.homie.adapters.BaseAdapter;
import finalproject.homie.controllers.BaseApplication;
import finalproject.homie.controllers.IDataResponseHandler;
import finalproject.homie.controllers.LoginActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by I311044 on 04/03/2017.
 */

public class DataFetcher<T extends BusinessEntity> extends BaseDAL {

    private String url;
    private List<T> list;
    private BusinessEntity.Factory factory;

    public DataFetcher(List<T> list, String token, IDataResponseHandler handler) {
        super(token, handler);
        this.list = list;
    }

    public void getCoursesByUserId(String userId) {
        factory = new BusinessEntity.Factory(Course.class);
        url = baseUrl + "api/courses?userId=" + userId;
        execute();
    }

    public void getAllCourses(int skip, int top) {
        factory = new BusinessEntity.Factory(Course.class);
        url = baseUrl + "api/courses?university=BGU&$skip=" + skip + "&$top=" + top;
        execute();
    }

    public void getAssignments(long courseNumber) {
        factory = new BusinessEntity.Factory(Assignment.class);
        url = baseUrl + "api/myAssignments?courseNumber=" + courseNumber;
        execute();
    }

    public void getAllAssignments(User user) {
        factory = new BusinessEntity.Factory(Assignment.class);
        url = baseUrl + "api/assignments?global=true&courseId=" + user.getCourseIdsStr();
        execute();
    }

    @Override
    public HttpRequestBase createHttpRequest() throws JSONException {
        HttpRequestBase request = new HttpGet(url);
        return request;
    }

    @Override
    public MyResponse handleResponse(HttpResponse response) throws IOException, JSONException {
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        JSONArray arr = new JSONArray(result.toString());
        for (int i = 0; i < arr.length(); i++) {
            T instance = (T) factory.create();
            instance.parseJSON(arr.getJSONObject(i));
            list.add(instance);
        }
        return new MyResponse("", null, response.getStatusLine().getStatusCode());
    }
}