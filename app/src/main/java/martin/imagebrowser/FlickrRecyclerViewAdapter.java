package martin.imagebrowser;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Ultimate on 5/9/2015.
 */
public class FlickrRecyclerViewAdapter extends RecyclerView.Adapter<ImageHolder> {

    private List<Picture> pictureList;
    private Context mContext;

    public FlickrRecyclerViewAdapter(Context context, List<Picture> pictureList) {
        mContext = context;
        this.pictureList = pictureList;
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.browse,null);
        ImageHolder imageHolder = new ImageHolder(view);
        return imageHolder;

//        return null;
    }

    //load the pics on screen only
    @Override
    public void onBindViewHolder(ImageHolder imageHolder, int i) {
        Picture pictureItem = pictureList.get(i);

        Picasso.with(mContext).load(pictureItem.getmImage()).error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(imageHolder.thumbnail);

//        ImageHolder.title.setText(pictureItem.getmTitle());

    }

    @Override
    public int getItemCount() {
        return (null != pictureList ? pictureList.size() : 0 );
    }

    public Picture getPhoto(int position){
        return(null != pictureList ? pictureList.get(position): null );
    }
}
