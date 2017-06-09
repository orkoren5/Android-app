package finalproject.homie.DO;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by I311044 on 01/06/2017.
 */

public class User extends BusinessEntity {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public BusinessEntity parseJSON(JSONObject json) throws JSONException {
        this.setID(json.getString("_id"));
        this.setName(json.getString("name"));
        return this;
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("name", name);
        return json;
    }

    @Override
    public String getForeignIdFields() {
        return null;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
