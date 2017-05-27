package finalproject.homie.DAL;

import android.content.Intent;
import android.os.AsyncTask;

import cz.msebera.android.httpclient.StatusLine;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.config.RequestConfig;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import org.json.*;

import finalproject.homie.DO.Assignment;
import finalproject.homie.DO.BusinessEntity;
import finalproject.homie.DO.Course;
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

public class DataFetcher {

    private String baseUrl = "http://159.203.118.144:8081/";
    private String token;

    public DataFetcher(String token) {
        this.token = token;
    }

    public void getCourses(List<Course> list, IDataResponseHandler handler) throws IOException {
        String url = baseUrl + "api/courses?university=BGU";
        new GetDataTask<Course>(list, Course.class, this.token, handler).execute(url);
    }

    public void getAssignments(List<Assignment> list, long courseNumber, IDataResponseHandler handler)
            throws IOException {
        String url = baseUrl + "api/assignments?courseNumber=" + courseNumber;
        new GetDataTask<Assignment>(list, Assignment.class, this.token, handler).execute(url);
    }

    private static class GetDataTask<T extends BusinessEntity> extends AsyncTask<String, Integer, StatusLine> {

        private List<T> list = null;
        private BusinessEntity.Factory<T> factory = null;
        private IDataResponseHandler handler;
        private String token;

        public GetDataTask(List<T> list, Class<T> c, String token, IDataResponseHandler handler) {
            this.list = list;
            this.handler = handler;
            this.token = token;
            factory = new BusinessEntity.Factory(c);
        }

        protected StatusLine doInBackground(String[] urls) {
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(3 * 1000).build();
            HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
            HttpGet request = new HttpGet(urls[0]);
            BusinessEntity[] results = null;
            try {
                // add request header
                request.addHeader("Content-type", "application/json");
                request.addHeader("token", token);
                HttpResponse response = client.execute(request);

                int statusCode = response.getStatusLine().getStatusCode();
                System.out.println("Response Code : " + statusCode);

                if (statusCode == 403) {
                    return response.getStatusLine();
                }

                BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));

                StringBuffer result = new StringBuffer();
                String line = "";
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }


                JSONArray arr = new JSONArray(result.toString());
                results = new BusinessEntity[arr.length()];
                for (int i = 0; i < results.length; i++) {
                    T instance = factory.create();
                    instance.parseJSON(arr.getJSONObject(i));
                    results[i] = instance;
                    list.add((T) results[i]);
                }
                return response.getStatusLine();
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            } catch (JSONException ex) {
                ex.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(StatusLine status) {
            if (status == null) {
                // Handle crash
            } else if (status.getStatusCode() == 200) {
                if (handler != null) {
                    handler.OnSuccess();
                }
            } else if (status.getStatusCode() == 403) {
                //Intent intent = new Intent(adapter.getContext(), LoginActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                //adapter.getContext().startActivity(intent);
            }
        }

    }

}