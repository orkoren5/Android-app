package finalproject.homie.DO;

import android.databinding.Bindable;

import org.json.JSONException;
import org.json.JSONObject;

import finalproject.homie.BR;

/**
 * Created by I311044 on 27/05/2017.
 */

public class Task extends BusinessEntity {

    public enum Status {
        TODO,
        IN_PROCESS,
        DONE
    }

    private String description;
    private String assignmentId;
    private Assignment assignment;
    private String groupId;
    private String title;
    private int daysAssessment;
    private Status status;
    private String assignedUserId;

    @Bindable
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    public String getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDaysAssessment() {
        return daysAssessment;
    }

    public void setAssignedUserId(String assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

    public String getAssignedUserId() {
        return assignedUserId;
    }

    @Bindable
    public void setDaysAssessment(int daysAssessment) {
        this.daysAssessment = daysAssessment;
        notifyPropertyChanged(BR.daysAssessment);
    }

    public Assignment getRelatedAssignment() {
        return assignment;
    }

    public void setRelatedAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    @Override
    public BusinessEntity parseJSON(JSONObject json) throws JSONException {
        this.title = json.getString("title");
        this.description = json.getString("description");
        this.daysAssessment = json.getInt("daysAssessment");
        this.status = Status.TODO; // TODO: get status
        this.assignedUserId = json.getString("assignedUserId");
        this.assignmentId = json.getString("assignmentId");

        return this;
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("title", title);
        json.put("description", description);
        json.put("status", 1);
        json.put("assignedUserId", assignedUserId);
        json.put("assignmentId", assignmentId);
        return json;
    }

    @Override
    public String getForeignIdFields() {
        return "assignmentId,assignedUserId";
    }
}
