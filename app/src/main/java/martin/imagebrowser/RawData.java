package martin.imagebrowser;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Ultimate on 5/8/2015.
 */
enum downloadStatus {IDLE, PROCESSING, NOT_INITIALIZED, FAILED_OR_EMPTY, OK}

public class RawData {

    private String LOG_TAG = RawData.class.getSimpleName();
    private String mRawURL;
    private String mData;
    private downloadStatus mDownloadStatus;

    public RawData(String mRawURL) {
        this.mRawURL = mRawURL;
        this.mDownloadStatus = downloadStatus.IDLE;
    }

    public void reset(){
        this.mDownloadStatus = downloadStatus.IDLE;
        this.mRawURL = null;
        this.mData = null;
    }

    public String getmData() {
        return mData;
    }

    public downloadStatus getmDownloadStatus() {
        return mDownloadStatus;
    }

    public void setmRawURL(String mRawURL) {
        this.mRawURL = mRawURL;
    }

    public void execute(){
        this.mDownloadStatus = downloadStatus.PROCESSING;
        downloadRawData downloadRawData = new downloadRawData();
        downloadRawData.execute(mRawURL);
    }

    public class downloadRawData extends AsyncTask<String, Void, String>{

        protected void onPostExecute(String webData){
            mData = webData;
            Log.v(LOG_TAG,"Data returned was: "+mData);
            if(mData == null){
                if(mRawURL == null){
                    mDownloadStatus = downloadStatus.NOT_INITIALIZED;
                }
                else{
                    mDownloadStatus = downloadStatus.FAILED_OR_EMPTY;
                }
            }
            else{
                mDownloadStatus = downloadStatus.OK;
            }
        }
        protected String doInBackground(String... params){
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            if(params == null)
                return null;
            try{

                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                if(inputStream == null){
                    return null;
                }
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line = reader.readLine()) != null ){
                    buffer.append(line + "\n");
                }
                return buffer.toString();

            } catch(IOException e){
                Log.e(LOG_TAG, "Error", e);
                return null;

            } finally {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
                if(reader != null){
                    try{
                        reader.close();
                    } catch(IOException e){
                        Log.e(LOG_TAG, "Reader Closing Error", e);
                    }
                }

            }
        }

    }
}
