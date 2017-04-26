package finalproject.homie.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

/**
 * Created by I311044 on 09/03/2017.
 */

public abstract class BaseAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {
    protected Context context;

    public Context getContext() {
        return context;
    }
}
