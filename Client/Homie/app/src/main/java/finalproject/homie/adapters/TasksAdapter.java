package finalproject.homie.adapters;

import android.content.Context;
import android.content.Intent;
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
import finalproject.homie.controllers.EditTask;
import finalproject.homie.controllers.IEntityClickListener;
import finalproject.homie.controllers.TaskList;
import finalproject.homie.model.Model;

/**
 * Created by I311044 on 02/03/2017.
 */

public class TasksAdapter extends BaseAdapter<TasksAdapter.TasksViewHolder> {

    private List<Task> tasks;
    private final IEntityClickListener listener;

    public class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtTitle;
        public TextView txtAssignedUser;
        public TextView txtDaysAssessment;
        public TextView txtStatus;
        public Task relatedTask;

        public TasksViewHolder(View view) {
            super(view);
            txtTitle = (TextView) view.findViewById(R.id.txtName);
            txtAssignedUser = (TextView) view.findViewById(R.id.txtAssignedUser);
            txtDaysAssessment = (TextView) view.findViewById(R.id.txtDaysAssessment);
            txtStatus = (TextView) view.findViewById(R.id.txtStatus);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            listener.onClick(relatedTask);
        }
    }

    public TasksAdapter(Context context, List<Task> tasks, IEntityClickListener listener) {
        this.tasks = tasks;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_card_view, parent, false);

        return new TasksViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TasksViewHolder holder, int position) {
        Task task = tasks.get(position);
        Resources res = this.context.getResources();
        holder.txtTitle.setText(task.getTitle());
        holder.txtDaysAssessment.setText(res.getString(R.string.days_assessment, task.getDaysAssessment()));
        holder.relatedTask = task;
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}
