package finalproject.homie.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import finalproject.homie.DAL.DataFetcher;
import finalproject.homie.DO.Course;
import finalproject.homie.R;
import finalproject.homie.controllers.BaseApplication;
import finalproject.homie.controllers.IDataResponseHandler;
import finalproject.homie.controllers.IEntityClickListener;
import finalproject.homie.controllers.MyAssignments;

/**
 * Created by I311044 on 02/03/2017.
 */

public class CoursesAdapter extends BaseAdapter<CoursesAdapter.CourseViewHolder> {

    private boolean showChecked = true;
    private List<Course> courses;
    private final IEntityClickListener listener;

    public class CourseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView txtName;
        public TextView txtNumber;
        public ImageView imgSelected;

        protected long courseNumber = 0;
        protected int position = 0;
        protected String courseId;
        protected Course course;


        public CourseViewHolder(View view) {
            super(view);
            txtName = (TextView) view.findViewById(R.id.txtCourseName);
            txtNumber = (TextView) view.findViewById(R.id.txtCourseNumber);
            imgSelected = (ImageView) view.findViewById(R.id.imgCourseSelected);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(course);
            imgSelected.setVisibility(course.isSelected() ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public CoursesAdapter(Context context, List<Course> courses, boolean showChecked, IEntityClickListener listener) {
        this.courses = courses;
        this.context = context;
        this.listener = listener;
        this.showChecked = showChecked;
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
        holder.course = course;
        if (showChecked) {
            holder.imgSelected.setVisibility(course.isSelected() ? View.VISIBLE : View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

}
