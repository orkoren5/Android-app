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
import finalproject.homie.R;

/**
 * Created by I311044 on 02/03/2017.
 */

public class AssignmentsAdapter extends BaseAdapter<AssignmentsAdapter.AssignmentsViewHolder> {

    List<Assignment> assignments;

    public class AssignmentsViewHolder extends RecyclerView.ViewHolder {
        public TextView txtDeadLine;
        public TextView txtNumber;

        public AssignmentsViewHolder(View view) {
            super(view);
            txtDeadLine = (TextView) view.findViewById(R.id.txtDeadLine);
            txtNumber = (TextView) view.findViewById(R.id.txtNumber);
        }
    }

    public AssignmentsAdapter(Context context, List<Assignment> assignments) {
        this.assignments = assignments;
        this.context = context;
    }

    public void fetchDataFromBH() {
        this.fetchDataFromBH(0);
    }
    public void fetchDataFromBH(long courseNumber) {
        DataFetcher fetcher = new DataFetcher(this);
        try {
            fetcher.getAssignments(this.assignments, courseNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    }

    @Override
    public int getItemCount() {
        return assignments.size();
    }
}
