package finalproject.homie.controllers;

import android.content.Context;
import android.content.Intent;
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

import finalproject.homie.DO.BusinessEntity;
import finalproject.homie.DO.Course;
import finalproject.homie.R;
import finalproject.homie.adapters.CoursesAdapter;
import finalproject.homie.model.Model;

public class MyCourses extends BaseNavigationActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_courses);
        super.onCreate(savedInstanceState);
        System.out.print("********Creating activity Courses\n");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        this.getIntent().putExtra("RELOAD_DATA", true);
    }

    @Override
    protected void additem() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (this.getIntent().getBooleanExtra("RELOAD_DATA", false)){
            final Model model = ((BaseApplication) getApplication()).getModel();
            final Context me = this;
            CoursesAdapter ca = new CoursesAdapter(this, model.getMyCourses(), new IEntityClickListener() {
                @Override
                public void onClick(BusinessEntity obj) {
                    model.setSelectedCourse((Course)obj);
                    Intent intent = new Intent(me, MyAssignments.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra("COURSE_ID", obj.getID());
                    startActivity(intent);
                }
            });
            if (model.getMyCourses().isEmpty()) {
                ca.fetchDataFromBH();
            }
            RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            mRecyclerView.setHasFixedSize(true);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(ca);
        }
    }
}
