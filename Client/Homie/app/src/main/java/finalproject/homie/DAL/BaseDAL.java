package finalproject.homie.DAL;

import android.app.DownloadManager;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.ProtocolVersion;
import cz.msebera.android.httpclient.StatusLine;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.config.RequestConfig;
import cz.msebera.android.httpclient.client.methods.HttpEntityEnclosingRequestBase;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.methods.HttpRequestBase;
import cz.msebera.android.httpclient.conn.ConnectTimeoutException;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import finalproject.homie.DO.BusinessEntity;
import finalproject.homie.controllers.IDataResponseHandler;

/**
 * Created by I311044 on 04/06/2017.
 */

public abstract class BaseDAL extends AsyncTask<String, Integer, BaseDAL.MyResponse>{

    protected static class MyResponse {
        protected String message;
        protected JSONObject body;
        protected int statusCode;

        protected MyResponse(String message, JSONObject body, int statusCode) {
            this.statusCode = statusCode;
            this.message = message;
            this.body = body;
        }
    }

    protected String baseUrl = "http://159.203.118.144:8081/";
    //protected String baseUrl = "http://localhost:8081/";
    private String token = "";
    private IDataResponseHandler handler = null;

    public BaseDAL(String token, IDataResponseHandler handler) {
        this.handler = handler;
        this.token = token;
    }

    public abstract HttpRequestBase createHttpRequest() throws JSONException;

    public HttpRequestBase buildRequest() throws JSONException {
        HttpRequestBase request = createHttpRequest();
        request.addHeader("Content-type", "application/json");
        request.addHeader("token", token);
        return request;
    }

    public abstract MyResponse handleResponse(HttpResponse response) throws IOException, JSONException;

    protected MyResponse doInBackground(String[] urlParts) {
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(3 * 1000).build();
        HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
        BusinessEntity[] results = null;
        try {
            HttpRequestBase request = buildRequest();
            HttpResponse response = client.execute(request);

            if (response.getStatusLine().getStatusCode() == 403) {
                return new MyResponse("", null, 403);
            }

            return handleResponse(response);
        } catch (ConnectTimeoutException ex) {
            return new MyResponse("The Server is temporarily down. Please try again later", null, 505);
        } catch (IOException ex) {
            ex.printStackTrace();
            return new MyResponse("Client crash", null, 504);
        } catch (JSONException e) {
            e.printStackTrace();
            return new MyResponse("Client crash", null, 504);
        }
    }

    @Override
    protected void onPostExecute(MyResponse response) {
        int status = response.statusCode;
        if (status == 504) {
            // Handle crash
        } else if (status == 200) {
            this.handler.OnSuccess(response.message);
        } else if (status == 403) {
            this.handler.OnError(403);
            //Intent intent = new Intent(adapter.getContext(), LoginActivity.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            //adapter.getContext().startActivity(intent);
        } else if (status == 505) {
            this.handler.OnError(505);
        }
    }

}
