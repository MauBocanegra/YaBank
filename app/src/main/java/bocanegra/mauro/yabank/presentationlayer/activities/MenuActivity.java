package bocanegra.mauro.yabank.presentationlayer.activities;

import android.app.Activity;
import android.app.SearchManager;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.Executors;

import bocanegra.mauro.yabank.R;
import bocanegra.mauro.yabank.domainlayer.db.AppDatabase;
import bocanegra.mauro.yabank.domainlayer.db.CompanyDAO;
import bocanegra.mauro.yabank.domainlayer.db.CompanyPOJO_DB;
import bocanegra.mauro.yabank.presentationlayer.fragments.RecargasFragment;

public class MenuActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    AppDatabase database;
    CompanyDAO companyDAO;

    private static final String TAG = "MenuActivityDebug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        instanciate();
        //instanciateDatabase();

        /*
        DBTask dbTask = new DBTask(MenuActivity.this);
        dbTask.execute();
        */

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d(TAG, "query="+query);
            //doMySearch(query);
        }

    }

    // --------------------------------------------- //
    // -------------- CUSTOM METHODS --------------- //
    // --------------------------------------------- //

    private void instanciate(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    private void instanciateDatabase() {
        database = AppDatabase.prePopulateDB(MenuActivity.this);
        companyDAO = database.getCompanyDAO();

        DBTask dbTask = new DBTask(MenuActivity.this);
        dbTask.execute();

    }

    // --------------------------------------------- //
    // -------------- MENU OPTIONS --------------- //
    // --------------------------------------------- //

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_menu, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_search:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // --------------------------------------------- //
    // -------------- PAGER ADAPTER --------------- //
    // --------------------------------------------- //

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position==0){
                return RecargasFragment.newInstance();
            }
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    // --------------------------------------------- //
    // -------------- PLACEHOLDER FRAGMENT --------------- //
    // --------------------------------------------- //

    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {}
        public static PlaceholderFragment newInstance(int sectionNumber) {
            return new PlaceholderFragment();
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_menu, container, false);
        }
    }

    private class DBTask extends AsyncTask<Void, Void, List<CompanyPOJO_DB>> {

        private WeakReference<Activity> weakActivity;

        public DBTask(Activity activity){
            weakActivity = new WeakReference<>(activity);
        }

        @Override
        protected List<CompanyPOJO_DB> doInBackground(Void... voids) {
            return companyDAO.getCompanies();
        }

        @Override
        protected void onPostExecute(List<CompanyPOJO_DB> companies) {
            super.onPostExecute(companies);
            Log.d(TAG, "num_of_companies = "+companies.size());
        }
    }

}
