package finalproject.homie.controllers;

/**
 * Created by I311044 on 27/05/2017.
 */

public interface IDataResponseHandler {

    void OnError(int errorCode);

    void OnSuccess();
}
