package finalproject.homie.DO;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Or Koren on 02/03/2017.
 */

public class Course extends BusinessEntity{
    private String name;
    private long number;
    private List<Assignment> assignmentList = new ArrayList<>();
    private boolean isSelected = false;

    public Course() {};

    private String[] getAssignmentIdsArr() {
        String[] ids = new String[assignmentList.size()];
        for (int i=0; i < assignmentList.size(); i++) {
            ids[i] = assignmentList.get(i).getID();
        }
        return ids;
    }

    public Course(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public long getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("number", number);
        json.put("assignmentIDs", getAssignmentIdsArr());
        return json;
    }

    @Override
    public String getForeignIdFields() {
        return null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Assignment> getAssignmentList() {
        return  assignmentList;
    }

    @Override
    public Course parseJSON(JSONObject json) throws JSONException{
        this.setNumber(json.getLong("number"));
        this.setName(json.getString("name"));
        this.setID(json.getString("_id"));
        return this;
    }
}
