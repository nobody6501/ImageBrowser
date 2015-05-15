package martin.imagebrowser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {

    private static final String LOG_TAG = "MainActivity";
    private List<Picture> pictureList = new ArrayList<Picture>();
    private RecyclerView mRecyclerView;
    private FlickrRecyclerViewAdapter flickrRecyclerViewAdapter;


    private EditText userSearch;
    private String search= "today";
    static final String LAST_SEARCH = "search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,1));


        ProcessPicture proccessPicture=new ProcessPicture(search,true);
        proccessPicture.execute();

        getFlickrData jsonData=new getFlickrData(search,true);
        jsonData.execute();

        flickrRecyclerViewAdapter = new FlickrRecyclerViewAdapter(MainActivity.this,
                new ArrayList<Picture>());
        mRecyclerView.setAdapter(flickrRecyclerViewAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
                        new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        Intent i = new Intent(MainActivity.this, ImageDetail.class);
                        i.putExtra(PHOTO_TRANSFER, flickrRecyclerViewAdapter.getPhoto(position));
                        startActivity(i);
                    }
                })
        );


        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(v);
                return false;
            }
        });


        final Button button=(Button)findViewById(R.id.searchButton);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String search;
                userSearch=(EditText)findViewById(R.id.searchField);
                userSearch.setInputType(InputType.TYPE_CLASS_TEXT);
                search=userSearch.getText().toString();

                ProcessPicture proccessPicture=new ProcessPicture(search,true);
                proccessPicture.execute();

                getFlickrData jsonData=new getFlickrData(search,true);
                jsonData.execute();
                hideKeyboard(v);

            }
        });


        }



        @Override
        protected void onSaveInstanceState(Bundle savedInstanceState){

            savedInstanceState.putString(LAST_SEARCH, search);

            super.onSaveInstanceState(savedInstanceState);

        }

        @Override
        public void onRestoreInstanceState(Bundle savedInstanceState){
                super.onRestoreInstanceState(savedInstanceState);

                search = savedInstanceState.getString(LAST_SEARCH);

                ProcessPicture proccessPicture = new ProcessPicture(search, true);
                proccessPicture.execute();

                getFlickrData jsonData = new getFlickrData(search, true);
                jsonData.execute();
        }


        public class ProcessPicture extends getFlickrData {

            public ProcessPicture(String search, boolean match) {
                super(search, match);
            }

            public void execute() {
                super.execute();
                ProcessData processedData = new ProcessData();
                processedData.execute();
            }

            public class ProcessData extends downloadJsonData {
                protected void onPostExecute(String webData) {
                    super.onPostExecute(webData);
                    flickrRecyclerViewAdapter = new FlickrRecyclerViewAdapter(MainActivity.this, getMPictures());
                    mRecyclerView.setAdapter(flickrRecyclerViewAdapter);
                }
            }
        }
    protected void hideKeyboard(View view)
    {
        InputMethodManager in=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
    }


}
