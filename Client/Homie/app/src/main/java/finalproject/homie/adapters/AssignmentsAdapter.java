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
import finalproject.homie.R;
import finalproject.homie.controllers.BaseApplication;
import finalproject.homie.controllers.IDataResponseHandler;
import finalproject.homie.controllers.IEntityClickListener;
import finalproject.homie.controllers.MyAssignments;
import finalproject.homie.controllers.TaskList;

/**
 * Created by I311044 on 02/03/2017.
 */

public class AssignmentsAdapter extends BaseAdapter<AssignmentsAdapter.AssignmentsViewHolder> {

    private List<Assignment> assignments;
    private final IEntityClickListener listener;

    public class AssignmentsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView txtDeadLine;
        protected TextView txtNumber;
        protected Assignment assignment;

        public AssignmentsViewHolder(View view) {
            super(view);
            txtDeadLine = (TextView) view.findViewById(R.id.txtDeadLine);
            txtNumber = (TextView) view.findViewById(R.id.txtNumber);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(assignment);
        }
    }

    public AssignmentsAdapter(Context context, List<Assignment> assignments, IEntityClickListener listener) {
        this.assignments = assignments;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public AssignmentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.assignment_card_view, parent, false);

        return new AssignmentsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AssignmentsViewHolder holder, int position) {
        Assignment assignment = assignments.get(position);
        Resources res = this.context.getResources();
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String sDate = f.format(assignment.getDeadline());
        holder.txtDeadLine.setText(res.getString(R.string.deadline, sDate));
        holder.txtNumber.setText(res.getString(R.string.assignment_no, assignment.getNumber()));
        holder.assignment = assignment;
    }

    @Override
    public int getItemCount() {
        return assignments.size();
    }
}
