package finalproject.homie.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import finalproject.homie.DAL.DataFetcher;
import finalproject.homie.DO.Course;
import finalproject.homie.R;
import finalproject.homie.controllers.BaseApplication;
import finalproject.homie.controllers.IDataResponseHandler;
import finalproject.homie.controllers.MyAssignments;

/**
 * Created by I311044 on 02/03/2017.
 */

public class CoursesAdapter extends BaseAdapter<CoursesAdapter.CourseViewHolder> {

    List<Course> courses;

    public class CourseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView txtName;
        public TextView txtNumber;
        protected long courseNumber = 0;
        protected int position = 0;
        protected String courseId;

        public CourseViewHolder(View view) {
            super(view);
            txtName = (TextView) view.findViewById(R.id.txtCourseName);
            txtNumber = (TextView) view.findViewById(R.id.txtCourseNumber);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), MyAssignments.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra("COURSE_NUMBER", courseNumber);
            intent.putExtra("COURSE_INDEX", position);
            intent.putExtra("COURSE_ID", courseId);
            v.getContext().startActivity(intent);
        }
    }

    public CoursesAdapter(Context context, List<Course> courses) {
        this.courses = courses;
        this.context = context;
    }

    public void fetchDataFromBH() {
        DataFetcher fetcher = new DataFetcher(((BaseApplication)context.getApplicationContext()).getToken());
        final CoursesAdapter adapter = this;
        try {
            fetcher.getCourses(this.courses, new IDataResponseHandler() {
                @Override
                public void OnError(int errorCode) {
                    // TODO: handle Error
                }

                @Override
                public void OnSuccess() {
                    adapter.notifyDataSetChanged();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setOnItemClickListener(View.OnClickListener listener) {

    }
    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_card_view, parent, false);

        return new CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, int position) {
        Course course = courses.get(position);
        holder.txtName.setText(course.getName());
        holder.txtNumber.setText(String.valueOf(course.getNumber()));
        holder.courseNumber = course.getNumber();
        holder.position = position;
        holder.courseId = course.getID();
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

}
