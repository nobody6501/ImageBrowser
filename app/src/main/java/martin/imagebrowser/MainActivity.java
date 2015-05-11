package martin.imagebrowser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {

    private static final String LOG_TAG = "MainActivity";
    private List<Picture> pictureList = new ArrayList<Picture>();
    private RecyclerView mRecyclerView;
    private FlickrRecyclerViewAdapter flickrRecyclerViewAdapter;

    private EditText userSearch;
    private String search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        GridView gridView = (GridView)findViewById(R.id.gridview);

        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(v);
                return false;
            }
        });
        flickrRecyclerViewAdapter = new FlickrRecyclerViewAdapter(MainActivity.this,
                new ArrayList<Picture>());
        mRecyclerView.setAdapter(flickrRecyclerViewAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
                mRecyclerView, new RecyclerItemClickListener.OnItemClickListener(){

            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this,"Tap", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, ImageDetail.class);
                i.putExtra(PHOTO_TRANSFER, flickrRecyclerViewAdapter.getPhoto(position));
                startActivity(i);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Intent i = new Intent(MainActivity.this, ImageDetail.class);
                i.putExtra(PHOTO_TRANSFER, flickrRecyclerViewAdapter.getPhoto(position));
                startActivity(i);
            }
        }));


//        ProcessPicture proccessPicture = new ProcessPicture(search, true);
//        proccessPicture.execute();
//
//        getFlickrData jsonData = new getFlickrData(search, true);
//        jsonData.execute();

        final Button button = (Button)findViewById(R.id.searchButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search;
                userSearch = (EditText) findViewById(R.id.searchField);
                userSearch.setInputType(InputType.TYPE_CLASS_TEXT);
                search = userSearch.getText().toString();

                ProcessPicture proccessPicture = new ProcessPicture(search, true);
                proccessPicture.execute();

                getFlickrData jsonData = new getFlickrData(search, true);
                jsonData.execute();

            }
        });

    }
    protected void hideKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

//    /**
//     * A placeholder fragment containing a simple view.
//     */
//    public static class PlaceholderFragment extends Fragment {
//
//        public PlaceholderFragment() {
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
//            return rootView;
//        }
//    }

    public class ProcessPicture extends getFlickrData{

        public ProcessPicture(String search, boolean match) {
            super(search, match);
        }
        public void execute(){
            super.execute();
            ProcessData processedData = new ProcessData();
            processedData.execute();
        }
        public class ProcessData extends downloadJsonData{
            protected void onPostExecute(String webData){
                super.onPostExecute(webData);
                flickrRecyclerViewAdapter = new FlickrRecyclerViewAdapter(MainActivity.this,getMPictures());
                mRecyclerView.setAdapter(flickrRecyclerViewAdapter);
            }
        }
    }
}
