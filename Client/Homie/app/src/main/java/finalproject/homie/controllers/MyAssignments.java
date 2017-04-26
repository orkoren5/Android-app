package finalproject.homie.controllers;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import finalproject.homie.DO.Assignment;
import finalproject.homie.R;
import finalproject.homie.adapters.AssignmentsAdapter;
import finalproject.homie.model.Model;

public class MyAssignments extends BaseNavigationActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_assignments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        long courseNumber = this.getIntent().getLongExtra("COURSE_NUMBER", 0);
        int courseIndex = this.getIntent().getIntExtra("COURSE_INDEX", 0);

        Model m = ((BaseApplication)getApplication()).getModel();
        List<Assignment> list = m.getAssignmentsForCourse(courseIndex);
        AssignmentsAdapter aa = new AssignmentsAdapter(this, list);
        if (list.isEmpty()) {
            aa.fetchDataFromBH(courseNumber);
        }

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(aa);
    }

    @Override
    protected void additem() {

    }
}
