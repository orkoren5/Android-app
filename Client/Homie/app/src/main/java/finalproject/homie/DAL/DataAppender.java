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
import finalproject.homie.adapters.BaseAdapter;
import finalproject.homie.controllers.BaseApplication;
import finalproject.homie.controllers.IDataResponseHandler;

/**
 * Created by I311044 on 04/03/2017.
 */

public class DataAppender {

    String baseUrl = "http://159.203.118.144:8081/";
    String token = "";

    public DataAppender(String token) {
        this.token = token;
    }

//    public void updateCourses(List<Course> list) throws IOException {
//        String url = baseUrl + "api/courses?university=BGU";
//        new GetDataTask<Course>(list, this.adapter, Course.class.getSimpleName()).execute(url);
//    }

    public void addAssignment(Assignment assignment, IDataResponseHandler handler) {
        String url = baseUrl + "api/assignments";
        new AppendDataTask<Assignment>(assignment, token, handler).execute(url, "POST");
    }

    public void addTask(Task task, IDataResponseHandler handler) {
        String url = baseUrl + "api/tasks";
        new AppendDataTask<Task>(task, token, handler).execute(url, "POST");
    }

    private static class AppendDataTask<T extends BusinessEntity> extends AsyncTask<String, Integer, StatusLine> {

        T obj = null;
        String token = "";
        private IDataResponseHandler handler = null;

        public AppendDataTask(T obj, String token, IDataResponseHandler handler) {
            this.obj = obj;
            this.token = token;
            this.handler = handler;
        }

        @Nullable
        private HttpEntityEnclosingRequestBase createRequest(String url, String method) {
            switch (method) {
                case "POST":
                    return new HttpPost(url);
                case "PUT":
                    return new HttpPut(url);
                default:
                    return null;
            }
        }

        private void handleSuccess(StringBuffer response) {
            String objId = response.toString();
            synchronized (obj) {
                obj.setID(objId);
                obj.notifyAll();
            }
        }

        protected StatusLine doInBackground(String[] urlParts) {
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(3 * 1000).build();
            HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
            HttpEntityEnclosingRequestBase request = createRequest(urlParts[0], urlParts[1]);
            BusinessEntity[] results = null;
            try {
                // add request header
                request.addHeader("Content-type", "application/json");
                request.addHeader("token", token);
                request.addHeader("homie_foreign_ids", obj.getForeignIdFields());
                request.setEntity(new StringEntity(obj.toJSON().toString(), ContentType.APPLICATION_JSON));
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

                if (statusCode == 200) {
                    handleSuccess(result);
                } else if (statusCode == 400) {

                }
                return response.getStatusLine();
            } catch (ConnectTimeoutException ex) {
                return new StatusLine() {
                    @Override
                    public ProtocolVersion getProtocolVersion() {
                        return null;
                    }

                    @Override
                    public int getStatusCode() {
                        return 505;
                    }

                    @Override
                    public String getReasonPhrase() {
                        return null;
                    }
                };
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(StatusLine status) {
            if (status == null) {
                // Handle crash
            } else if (status.getStatusCode() == 200) {
                this.handler.OnSuccess();
            } else if (status.getStatusCode() == 403) {
                this.handler.OnError(403);
                //Intent intent = new Intent(adapter.getContext(), LoginActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                //adapter.getContext().startActivity(intent);
            } else if (status.getStatusCode() == 505) {
                this.handler.OnError(505);
            }
        }

    }

}