package finalproject.homie.controllers;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import finalproject.homie.DAL.DataAppender;
import finalproject.homie.DAL.DataFetcher;
import finalproject.homie.DO.BusinessEntity;
import finalproject.homie.DO.Course;
import finalproject.homie.DO.User;
import finalproject.homie.R;
import finalproject.homie.adapters.CoursesAdapter;
import finalproject.homie.model.Model;

public class AllCourses extends BaseNavigationActivity {

    private boolean userUpdated = false;
    private CoursesAdapter adapter;
    private Model model = null;
    private List<Course> allCourses = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_course_list);
        super.onCreate(savedInstanceState);
        System.out.print("********Creating activity Courses\n");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        model = ((BaseApplication) getApplication()).getModel();
        allCourses = model.getAllCourses();

        this.getIntent().putExtra("RELOAD_DATA", true);
    }

    @Override
    public void setTitle() {
        this.setTitle(getString(R.string.all_courses));
    }

    @Override
    public void onStop() {
        if (userUpdated) {
            String token = ((BaseApplication) getApplicationContext()).getToken();
            new DataAppender<User>(token, new IDataResponseHandler() {
                @Override
                public void OnError(int errorCode) {

                }

                @Override
                public void OnSuccess(String message) {

                }
            }).updateUser(((BaseApplication) getApplicationContext()).getConnectedUser());
        }
        super.onStop();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();

        if (loggingOut) {
            loggingOut = false;
            return;
        }

        if (this.getIntent().getBooleanExtra("RELOAD_DATA", false)){
            final Context me = this;
            final User connectedUser = ((BaseApplication) getApplicationContext()).getConnectedUser();
            adapter = new CoursesAdapter(this, allCourses, true, new IEntityClickListener() {
                @Override
                public void onClick(BusinessEntity obj) {
                    userUpdated = true;
                    Course c = (Course)obj;
                    c.setSelected(!c.isSelected());
                    if (c.isSelected()) {
                        connectedUser.getCourseIds().add(obj.getID());
                        model.getMyCourses().add(c);
                    } else {
                        connectedUser.getCourseIds().remove(obj.getID());
                        model.getMyCourses().remove(c);
                    }
                }
            });
            adapter.notifyDataSetChanged();

            if (allCourses.isEmpty()) {
                fetchData();
            }

            RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            mRecyclerView.setHasFixedSize(true);
            final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if(mLayoutManager.findLastCompletelyVisibleItemPosition() == allCourses.size()-1){
                        fetchData();
                    }
                }
            });
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(adapter);
        }
    }

    protected void fetchData() {
        String token = ((BaseApplication) getApplicationContext()).getToken();
        final User connectedUser = ((BaseApplication) getApplicationContext()).getConnectedUser();
        new DataFetcher<Course>(allCourses, token, new IDataResponseHandler() {
            @Override
            public void OnError(int errorCode) {
                // TODO: handle Error
            }

            @Override
            public void OnSuccess(String message) {
                model.updateMyCourses(connectedUser);
                adapter.notifyDataSetChanged();
            }
        }).getAllCourses(allCourses.size(), 20);
    }
}
