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
import finalproject.homie.controllers.LoginActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by I311044 on 04/03/2017.
 */

public class DataFetcher {

    String baseUrl = "http://159.203.118.144:8081/";

    BaseAdapter adapter;

    public DataFetcher(BaseAdapter adapter) {
        this.adapter = adapter;
    }

    public void getCourses(List<Course> list) throws IOException {
        String url = baseUrl + "api/courses?university=BGU";
        new GetDataTask<Course>(list, this.adapter, Course.class).execute(url);
    }

    public void getAssignments(List<Assignment> list, long courseNumber) throws IOException {
        String url = baseUrl + "api/assignments?courseNumber=" + courseNumber;
        new GetDataTask<Assignment>(list, this.adapter, Assignment.class).execute(url);
    }

    private static class GetDataTask<T extends BusinessEntity> extends AsyncTask<String, Integer, StatusLine> {

        List<T> list = null;
        BusinessEntity.BusinessEntityFactory<T> factory = null;
        BaseAdapter adapter;

        public GetDataTask(List<T> list, BaseAdapter adapter, Class<T> c) {
            this.list = list;
            this.adapter = adapter;
            factory = new BusinessEntity.BusinessEntityFactory(c);
        }

        protected StatusLine doInBackground(String[] urls) {
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(3 * 1000).build();
            HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
            HttpGet request = new HttpGet(urls[0]);
            BusinessEntity[] results = null;
            String token = ((BaseApplication)adapter.getContext().getApplicationContext()).getToken();
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
                    results[i] = instance.parseJSON(arr.getJSONObject(i));
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
                adapter.notifyDataSetChanged();
            } else if (status.getStatusCode() == 403) {
                //Intent intent = new Intent(adapter.getContext(), LoginActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                //adapter.getContext().startActivity(intent);
            }
        }

    }

}