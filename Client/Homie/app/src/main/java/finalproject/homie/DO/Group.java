package finalproject.homie.DO;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by I311044 on 27/05/2017.
 */

public class Group extends BusinessEntity {

    Assignment assignment;
    List<Task> taskList = new ArrayList<>();
    List<String> memberUserIds = new ArrayList<>();

    @Override
    public BusinessEntity parseJSON(JSONObject json) throws JSONException {
        return null;
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        return null;
    }
}
