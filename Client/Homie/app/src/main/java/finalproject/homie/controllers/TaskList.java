package finalproject.homie.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.List;

import finalproject.homie.DO.Assignment;
import finalproject.homie.DO.Course;
import finalproject.homie.R;
import finalproject.homie.adapters.AssignmentsAdapter;
import finalproject.homie.model.Model;

public class TaskList extends BaseNavigationActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_task_list);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        Model m = ((BaseApplication) getApplication()).getModel();
        if (m.checkAndRemoveIsDirty(Model.ASSIGNMENT_FLAG)){
            long courseNumber = this.getIntent().getLongExtra("COURSE_NUMBER", 0);
            int courseIndex = this.getIntent().getIntExtra("COURSE_INDEX", 0);

            List<Assignment> list = m.getAssignmentsForCourse(courseIndex);
            AssignmentsAdapter aa = new AssignmentsAdapter(this, list);
            if (list.isEmpty()) {
                aa.fetchDataFromBH(courseNumber);
                //m.setDirty(Model.ASSIGNMENT_FLAG);
            }

            RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            mRecyclerView.setHasFixedSize(true);
            GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(aa);
        }
    }

    @Override
    protected void additem() {
        long courseNumber = this.getIntent().getLongExtra("COURSE_NUMBER", 0);
        int courseIndex = this.getIntent().getIntExtra("COURSE_INDEX", 0);
        Model m = ((BaseApplication)getApplication()).getModel();

        Course c = m.getMyCourses().get(courseIndex);

        Assignment newAssignment = new Assignment();
        newAssignment.setCourseNumber(courseNumber);
        newAssignment.setRelatedCourse(c);
        m.setSelectedAssignment(newAssignment);

        Intent intent = new Intent(this, EditAssignment.class);
        intent.putExtra("IS_NEW", true);
        startActivity(intent);
    }
}
