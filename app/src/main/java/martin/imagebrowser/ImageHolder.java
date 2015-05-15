package martin.imagebrowser;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Ultimate on 5/9/2015.
 */
public class ImageHolder extends RecyclerView.ViewHolder {

    protected ImageView thumbnail;

    public ImageHolder(View view) {
        super(view);

        this.thumbnail = (ImageView)view.findViewById(R.id.thumbnail);


    }
}
