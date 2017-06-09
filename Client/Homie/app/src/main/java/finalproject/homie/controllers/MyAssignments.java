package finalproject.homie.controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.io.IOException;
import java.util.List;

import finalproject.homie.DAL.DataAppender;
import finalproject.homie.DAL.DataFetcher;
import finalproject.homie.DO.Assignment;
import finalproject.homie.DO.BusinessEntity;
import finalproject.homie.DO.Course;
import finalproject.homie.R;
import finalproject.homie.adapters.AssignmentsAdapter;
import finalproject.homie.adapters.CoursesAdapter;
import finalproject.homie.model.Model;

public class MyAssignments extends BaseNavigationActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_assignments);
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Course c = ((BaseApplication) getApplication()).getModel().getSelectedCourse();
        if (c != null) {
            this.setTitle(c.getName());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        final Model model = ((BaseApplication) getApplication()).getModel();
        final Context me = this;
        if (model.checkAndRemoveIsDirty(Model.ASSIGNMENT_FLAG)){
            Course c = model.getSelectedCourse();

            final List<Assignment> list = model.getAssignmentsForCourse(c.getID());
            final AssignmentsAdapter aa = new AssignmentsAdapter(this, list, new IEntityClickListener() {
                @Override
                public void onClick(BusinessEntity obj) {
                    model.setSelectedAssignment((Assignment)obj);
                    Intent intent = new Intent(me, TaskList.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra("ASSIGNMENT_ID", obj.getID());
                    startActivity(intent);
                }
            });
            aa.notifyDataSetChanged();
            if (list.isEmpty()) {
                String token = ((BaseApplication)getApplicationContext()).getToken();
                new DataFetcher<Assignment>(list, token, new IDataResponseHandler() {
                    @Override
                    public void OnError(int errorCode) {
                        // TODO: handle error
                    }

                    @Override
                    public void OnSuccess(String message) {
                        for (Assignment a : list) {
                            model.updateTasksForAssignment(a);
                            a.setRelatedCourse(model.getSelectedCourse());
                        }
                        aa.notifyDataSetChanged();
                    }
                }).getAssignments(c.getNumber());
            }

            RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            mRecyclerView.setHasFixedSize(true);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(aa);
        }
    }

    @Override
    protected void additem() {
        Model m = ((BaseApplication)getApplication()).getModel();

        Course c = m.getSelectedCourse();

        Assignment newAssignment = new Assignment();
        newAssignment.setCourseNumber(c.getNumber());
        newAssignment.setRelatedCourse(c);
        newAssignment.getUsers().add(((BaseApplication)this.getApplication()).getConnectedUser());
        m.setSelectedAssignment(newAssignment);

        Intent intent = new Intent(this, EditAssignment.class);
        intent.putExtra("IS_NEW", true);
        startActivity(intent);
    }
}
