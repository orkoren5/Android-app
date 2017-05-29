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
import finalproject.homie.controllers.MyAssignments;
import finalproject.homie.controllers.TaskList;

/**
 * Created by I311044 on 02/03/2017.
 */

public class AssignmentsAdapter extends BaseAdapter<AssignmentsAdapter.AssignmentsViewHolder> {

    List<Assignment> assignments;

    public class AssignmentsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtDeadLine;
        public TextView txtNumber;
        protected String assignmentId;

        public AssignmentsViewHolder(View view) {
            super(view);
            txtDeadLine = (TextView) view.findViewById(R.id.txtDeadLine);
            txtNumber = (TextView) view.findViewById(R.id.txtNumber);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), TaskList.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra("ASSIGNMENT_ID", assignmentId);
            v.getContext().startActivity(intent);
        }
    }

    public AssignmentsAdapter(Context context, List<Assignment> assignments) {
        this.assignments = assignments;
        this.context = context;
    }

    public void fetchDataFromBH(IDataResponseHandler handler) {
        this.fetchDataFromBH(0, handler);
    }
    public void fetchDataFromBH(long courseNumber, final IDataResponseHandler handler) {
        final AssignmentsAdapter adapter = this;

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
        holder.assignmentId = assignment.getID();
    }

    @Override
    public int getItemCount() {
        return assignments.size();
    }
}
