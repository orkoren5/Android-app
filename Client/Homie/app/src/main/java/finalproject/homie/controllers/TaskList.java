package finalproject.homie.controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import finalproject.homie.DO.BusinessEntity;
import finalproject.homie.DO.Task;
import finalproject.homie.DO.Task.Status;
import finalproject.homie.DO.TasksHolder;
import finalproject.homie.R;
import finalproject.homie.adapters.TasksAdapter;
import finalproject.homie.model.Model;

public class TaskList extends BaseEditAssignmentActivity
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
    protected void setSelectedItem(Menu menu) {
        menu.getItem(1).setChecked(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final Model model = ((BaseApplication) getApplication()).getModel();
        final Context me = this;
        if (model.checkAndRemoveIsDirty(Model.ASSIGNMENT_FLAG)){
            String assignmentId = this.getIntent().getStringExtra("ASSIGNMENT_ID");

            TasksHolder lists = model.getTasksForAssignment(assignmentId);
            for (Status status : Status.values()) {
                TasksAdapter ta = new TasksAdapter(this, lists.getList(status), new IEntityClickListener() {

                    @Override
                    public void onClick(BusinessEntity obj) {
                        Intent intent = new Intent(me, EditTask.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        intent.putExtra("IS_NEW", false);
                        model.setSelectedTask((Task) obj);
                        me.startActivity(intent);
                    }
                });
                ta.notifyDataSetChanged();

                RecyclerView mRecyclerView = (RecyclerView) findViewById(getRecyclerViewId(status));
                mRecyclerView.setHasFixedSize(true);
                GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(ta);
            }
        }
    }

    private int getRecyclerViewId(Status status) {
        switch (status) {
            case TODO:
                return R.id.recycler_view_todo;
            case IN_PROCESS:
                return R.id.recycler_view_process;
            case DONE:
                return R.id.recycler_view_done;
            default:
                return 0;
        }
    }

    @Override
    protected void additem() {
        Model m = ((BaseApplication)getApplication()).getModel();

        Task newTask = new Task();
        newTask.setTitle(getString(R.string.new_task));
        newTask.setRelatedAssignment(m.getSelectedAssignment());
        newTask.setAssignmentId(m.getSelectedAssignment().getID());

        m.setSelectedTask(newTask);

        Intent intent = new Intent(this, EditTask.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("IS_NEW", true);
        m.setSelectedTask(newTask);
        startActivity(intent);
    }
}
