package finalproject.homie.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import finalproject.homie.DAL.DataFetcher;
import finalproject.homie.DO.Assignment;
import finalproject.homie.DO.Task;
import finalproject.homie.R;
import finalproject.homie.controllers.BaseApplication;

/**
 * Created by I311044 on 02/03/2017.
 */

public class TasksAdapter extends BaseAdapter<TasksAdapter.AssignmentsViewHolder> {

    List<Task> tasks;

    public class AssignmentsViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName;
        public TextView txtAssignedUser;
        public TextView txtDaysAssessment;
        public TextView txtStatus;

        public AssignmentsViewHolder(View view) {
            super(view);
            txtName = (TextView) view.findViewById(R.id.txtName);
            txtAssignedUser = (TextView) view.findViewById(R.id.txtAssignedUser);
            txtDaysAssessment = (TextView) view.findViewById(R.id.txtDaysAssessment);
            txtStatus = (TextView) view.findViewById(R.id.txtStatus);
        }
    }

    public TasksAdapter(Context context, List<Task> tasks) {
        this.tasks = tasks;
        this.context = context;
    }

    @Override
    public AssignmentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.assignment_card_view, parent, false);

        return new AssignmentsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AssignmentsViewHolder holder, int position) {
        Task task = tasks.get(position);
        Resources res = this.context.getResources();
        holder.txtName.setText(task.getTitle());
        holder.txtDaysAssessment.setText(String.valueOf(task.getDaysAssessment()));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}
