package finalproject.homie.DO;

import android.annotation.TargetApi;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.nfc.FormatException;
import android.text.Editable;
import android.text.TextWatcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import finalproject.homie.BR;
import finalproject.homie.DO.Bindables.*;

/**
 * Created by I311044 on 02/03/2017.
 */

public class Assignment extends BusinessEntity {
    private long courseNumber;
    private String courseId;
    private int number;
    private Date deadline = new Date();
    private int daysAssessment;
    private Course relatedCourse;
    private final EntityArrayList<User> users = new EntityArrayList<>();
    private TasksHolder tasks = new TasksHolder();

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public EntityArrayList<User> getUsers() {
        return users;
    }

    public long getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(long courseNumber) {
        this.courseNumber = courseNumber;
    }

    @Bindable
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
        notifyPropertyChanged(BR.number);
    }

    @Bindable
    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
        notifyPropertyChanged(BR.deadline);
    }

    @Bindable
    public int getDaysAssessment() {
        return daysAssessment;
    }

    public void setDaysAssessment(int daysAssessment) {
        this.daysAssessment = daysAssessment;
        notifyPropertyChanged(BR.daysAssessment);
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public Course getRelatedCourse() {
        return relatedCourse;
    }

    public void setRelatedCourse(Course relatedCoures) {
        this.relatedCourse = relatedCoures;
    }

    public TasksHolder getTasks() {
        return tasks;
    }

    @Override
    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        if (id != null) {
            json.put("_id", id);
        }
        json.put("courseNumber", courseNumber);
        json.put("number", number);
        json.put("deadline", Assignment.format.format(deadline));
        json.put("daysAssessment", daysAssessment);
        json.put("courseId", relatedCourse.getID());
        json.put("approvedValid", true);
        return json;
    }

    @Override
    public String getForeignIdFields() {
        return "courseId";
    }

    @Override
    public Assignment parseJSON(JSONObject json) throws JSONException{
        this.setID(json.getString("_id"));
        this.setCourseNumber(json.getLong("courseNumber"));
        this.setDaysAssessment(json.getInt("daysAssessment"));
        this.setNumber(json.getInt("number"));
        if (json.has("courseId")) {
            this.setCourseId(json.getString("courseId"));
        }

        try {
            this.setDeadline(format.parse(json.getString("deadline")));
        }
        catch (ParseException ex) {
            ex.printStackTrace();
            this.setDeadline(new Date(0));
        }

        if (json.has("tasks")) {
            JSONArray tasksArr = json.getJSONArray("tasks");
            for (int i = 0; i < tasksArr.length(); i++) {
                Task t = new Task();
                t.parseJSON(tasksArr.getJSONObject(i));
                t.setRelatedAssignment(this);
                tasks.add(t);
            }
        }

        if (json.has("userObjects")) {
            JSONArray groupArr = json.getJSONArray("userObjects");
            for (int i = 0; i < groupArr.length(); i++) {
                User u = new User();
                u.parseJSON(groupArr.getJSONObject(i));
                users.add(u);
            }
        }

        return this;
    }
}
