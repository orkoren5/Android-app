package finalproject.homie.DAL;

import android.os.AsyncTask;
import android.provider.ContactsContract;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.StatusLine;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.config.RequestConfig;
import cz.msebera.android.httpclient.client.methods.HttpEntityEnclosingRequestBase;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.methods.HttpPut;
import cz.msebera.android.httpclient.entity.mime.content.StringBody;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import finalproject.homie.DO.Assignment;
import finalproject.homie.DO.BusinessEntity;
import finalproject.homie.DO.Course;
import finalproject.homie.adapters.BaseAdapter;
import finalproject.homie.controllers.BaseApplication;

/**
 * Created by I311044 on 04/03/2017.
 */

public class DataAppender {

    String baseUrl = "http://159.203.118.144:8081/";
    String token = "";

    private static DataAppender instance = null;

    private DataAppender(String token) {
        this.token = token;
    }
    public static DataAppender getInstance() {
        return instance == null ? instance = new DataAppender() : instance;
    }

    public void updateCourses(List<Course> list) throws IOException {
        String url = baseUrl + "api/courses?university=BGU";
        new GetDataTask<Course>(list, this.adapter, Course.class.getSimpleName()).execute(url);
    }

    public void addAssignment(Assignment assignment) throws IOException {
        String url = baseUrl + "api/assignments";
        new AppendDataTask<Assignment>(assignment, token).execute(url);
    }

    private static class AppendDataTask<T extends BusinessEntity> extends AsyncTask<String, Integer, StatusLine> {

        T obj = null;
        String token = "";

        public AppendDataTask(T obj, String token) {
            this.obj = obj;
            this.token = token;
        }

        protected StatusLine doInBackground(String[] urls) {
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(3 * 1000).build();
            HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
            HttpEntityEnclosingRequestBase request = new HttpPost(urls[0]);
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

                    results[i] = BusinessEntity.parseJSON(arr.getJSONObject(i), this.dataType);
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