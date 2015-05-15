package martin.imagebrowser;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by Ultimate on 5/10/2015.
 */
public class BaseActivity extends ActionBarActivity {


    private Toolbar mToolBar;

    public static final String PHOTO_TRANSFER = "PHOTO_TRANSFER";

    protected Toolbar activateToolbar(){
//        if(mToolBar == null ){
//            mToolBar=(Toolbar)findViewById(R.id.app_bar);
//            if(mToolBar !=null){
//                setSupportActionBar(mToolBar);
//            }
//        }
        return null;
    }
    protected Toolbar activateToolBarWithHomeEnabled(){
        activateToolbar();
        if(mToolBar != null ){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        return mToolBar;
    }
}
