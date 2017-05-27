package finalproject.homie.DO;

import org.json.JSONException;
import org.json.JSONObject;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDaysAssessment() {
        return daysAssessment;
    }

    public void setDaysAssessment(int daysAssessment) {
        this.daysAssessment = daysAssessment;
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
        return null;
    }
}
