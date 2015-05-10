package martin.imagebrowser;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ultimate on 5/8/2015.
 */
public class getFlickrData extends RawData {

    private String LOG_TAG = getFlickrData.class.getSimpleName();
    private List<Picture> mPictures;
    private Uri mDestinationUrl;

    public getFlickrData(String search, boolean match){
        super(null);
        createAndUpdateUrl(search,match);
        mPictures = new ArrayList<Picture>();
    }

    public void execute(){
        super.setmRawURL(mDestinationUrl.toString());
        downloadJsonData downloadJsonData = new downloadJsonData();
        Log.v(LOG_TAG,"Build URI = " + mDestinationUrl.toString());
        downloadJsonData.execute(mDestinationUrl.toString());

    }

    public boolean createAndUpdateUrl(String search, boolean match){

        final String baseUrl = "https://api.flickr.com/services/feeds/photos_public.gne";
        final String TAGS_PARAM = "tags";
        final String TAGMODE_PARAM = "tagmode";
        final String FORMAT_PARAM = "format";
        final String noJsonCallback_PARAM = "nojsoncallback";

        mDestinationUrl = Uri.parse(baseUrl).buildUpon()
                .appendQueryParameter(TAGS_PARAM,search)
                .appendQueryParameter(TAGMODE_PARAM, match ?"All" : "ANY")
                .appendQueryParameter(FORMAT_PARAM, "json")
                .appendQueryParameter(noJsonCallback_PARAM, "1")
                .build();

        return mDestinationUrl != null;

    }
    public List<Picture> getMPictures(){
        return mPictures;
    }

    public void proccessResult(){
        if(getmDownloadStatus() != downloadStatus.OK){
            Log.e(LOG_TAG, "Error downloading raw file");
            return;
        }
        final String F_items = "items";
        final String F_title = "title";
        final String F_media = "media";
        final String F_photoUrl = "m";
        final String F_author = "author";
        final String F_autorId = "author_id";
        final String F_link = "link";
        final String F_tags = "tags";

        try{

            JSONObject jsonData = new JSONObject(getmData());
            //item is array
            JSONArray itemArray = jsonData.getJSONArray(F_items);
            for(int i = 0; i<itemArray.length(); i++){
                JSONObject jsonPicture = itemArray.getJSONObject(i);
                String title = jsonPicture.getString(F_title);
                String author = jsonPicture.getString(F_author);
                String authorId = jsonPicture.getString(F_autorId);
                String link = jsonPicture.getString(F_link);
                String tags = jsonPicture.getString(F_tags);

                JSONObject jsonMedia = jsonPicture.getJSONObject(F_media);
                String photoUrl = jsonMedia.getString(F_photoUrl);

                Picture photoObject = new Picture(title, author,authorId,link,tags,photoUrl);
                this.mPictures.add(photoObject);
            }
            //dump each entry
            for(Picture singlePicture :mPictures){
                Log.v(LOG_TAG, singlePicture.toString());
            }

        }catch(JSONException jsone){
            jsone.printStackTrace();
            Log.e(LOG_TAG,"Error proccessing json data");
        }
    }

    public class downloadJsonData extends downloadRawData{
        protected void onPostExecute(String webData){
            super.onPostExecute(webData);
            proccessResult();
        }
        protected String doInBackground(String... params){
            //so doesn't crash
            String[] par = {mDestinationUrl.toString()};
            return super.doInBackground(par);
        }

    }
}
