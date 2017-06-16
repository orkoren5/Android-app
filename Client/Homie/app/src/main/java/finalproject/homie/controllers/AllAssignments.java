package finalproject.homie.controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import finalproject.homie.DAL.DataFetcher;
import finalproject.homie.DO.Assignment;
import finalproject.homie.DO.BusinessEntity;
import finalproject.homie.DO.Course;
import finalproject.homie.DO.User;
import finalproject.homie.R;
import finalproject.homie.adapters.AssignmentsAdapter;
import finalproject.homie.model.Model;

public class AllAssignments extends BaseNavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_assignments);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setTitle() {
        setTitle(R.string.all_assignments);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (loggingOut) {
            loggingOut = false;
            return;
        }

        final Model model = ((BaseApplication) getApplication()).getModel();
        final Context me = this;
        final User connectedUser = ((BaseApplication) getApplicationContext()).getConnectedUser();

        if (model.checkAndRemoveIsDirty(Model.ASSIGNMENT_FLAG)){
            final List<Assignment> list = model.getAllAssignments();
            final AssignmentsAdapter aa = new AssignmentsAdapter(this, list, new IEntityClickListener() {
                @Override
                public void onClick(BusinessEntity obj) {

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
                }).getAllAssignments(connectedUser);
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
