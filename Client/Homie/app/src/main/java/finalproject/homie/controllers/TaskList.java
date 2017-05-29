package finalproject.homie.controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import finalproject.homie.DO.Assignment;
import finalproject.homie.DO.BusinessEntity;
import finalproject.homie.DO.Course;
import finalproject.homie.DO.Task;
import finalproject.homie.R;
import finalproject.homie.adapters.AssignmentsAdapter;
import finalproject.homie.adapters.TasksAdapter;
import finalproject.homie.model.Model;

public class TaskList extends BaseNavigationActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_task_list);
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

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
        final Model model = ((BaseApplication) getApplication()).getModel();
        final Context me = this;
        if (model.checkAndRemoveIsDirty(Model.ASSIGNMENT_FLAG)){
            String assignmentId = this.getIntent().getStringExtra("ASSIGNMENT_ID");

            List<Task> list = model.getTasksForAssignment(assignmentId);
            TasksAdapter ta = new TasksAdapter(this, list, new IEntityClickListener() {

                @Override
                public void onClick(BusinessEntity obj) {
                    Intent intent = new Intent(me, EditTask.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra("IS_NEW", false);
                    model.setSelectedTask((Task)obj);
                    me.startActivity(intent);
                }
            });

            RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_todo);
            mRecyclerView.setHasFixedSize(true);
            GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(ta);
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
