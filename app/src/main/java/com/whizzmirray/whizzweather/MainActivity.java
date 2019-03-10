package com.whizzmirray.whizzweather;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.whizzmirray.whizzweather.http.HTTPClient;
import com.whizzmirray.whizzweather.http.JSONParser;
import com.whizzmirray.whizzweather.models.Weather;
import com.whizzmirray.whizzweather.views.MainView;
import com.whizzmirray.whizzweather.views.SlidingupView;

import org.json.JSONException;


public class MainActivity extends AppCompatActivity {
    private AsyncWeather asyncLoc;
    protected GeoDataClient mGeoDataClient;
    private Weather[] weathers = null;
    private MainView mainView;
    private ProgressBar spinner;
    private View mainContent,noInternet;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private float _slideOffset;
    SlidingupView fragment;
    private GestureDetectorCompat mDetector;
    private float height;
    private static final String DEBUG_TAG = "Gestures";
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private ImageView arrow;
    private String[] myDataset = {"paris","london"};
    private boolean finishedHTTP = false;
    public static String TAG = "MainActivity";
    private int mShortAnimationDuration;
    String lat = "51.25";
    String lon = "23.67";
    private boolean error = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
        mGeoDataClient = Places.getGeoDataClient(this, null);
        mainView = new MainView(this);
        mainContent          = findViewById(R.id.mainContent);
        noInternet           = findViewById(R.id.mainNoInternet);
        spinner              = findViewById(R.id.mainProgress);
        arrow                = findViewById(R.id.arrow);
        toolbar              = findViewById(R.id.toolbar);
        drawerLayout         = findViewById(R.id.drawer_layout);
        slidingUpPanelLayout = findViewById(R.id.sliding_layout);
        mRecyclerView        = findViewById(R.id.my_recycler_view);
        navigationView       = findViewById(R.id.navigationView);
        findViewById(R.id.locationText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });
        noInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED)
                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                else
                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new WeatherListAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.SimplePanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                _slideOffset = slideOffset;
                arrow.setRotationX(slideOffset * 180);
            }
        });

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return false;
            }
        });
        refresh();
    }
    public void refresh(){
        asyncLoc = new AsyncWeather();
        asyncLoc.execute(MainActivity.this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:
                try {
                    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                            .build();
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .setFilter(typeFilter)
                            .build(MainActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_items,menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                LatLng ll = place.getLatLng();
                lon = ll.longitude+"";
                lat = ll.latitude+"";
                refresh();
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(DEBUG_TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public abstract class UpdateListener {
        public abstract void onFinishUpdate();
    }
    private class AsyncWeather extends AsyncTask<Context,Void,Void> {

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "onPreExecute: error is " + error);
            if(!error){
                crossfade(spinner,mainContent);
            }else{
                //crossfade(spinner,noInternet);
                error = false;
            }
        }

        @Override
        protected Void doInBackground(Context... params) {
            String jsonForcast = null;
            String jsonCurr = null;
            try {
                jsonForcast = HTTPClient.getForcast(lat,lon);
                jsonCurr = HTTPClient.getWeatherJSON(lat,lon);
            } catch (Exception e) {
                error = true;
                //e.printStackTrace();
            }
            //Log.d("getWeather","json is null " + (json == null));
            if(jsonForcast != null){
                try {
                    weathers = JSONParser.parseForcastJSON(jsonForcast,jsonCurr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.d(TAG, "onPreExecute: Finished fetching HTTP");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d(TAG, "onPostExecute: error is " + error);
            if(!error){
                mainView.update(weathers);
                hidefade(noInternet);
                crossfade(mainContent,spinner);
            }else{
                crossfade(noInternet,spinner);
            }

        }
    }
    private void hidefade(final View toHide){
        toHide.animate()
                .alpha(0f)
                .setDuration(mShortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        toHide.setVisibility(View.GONE);
                    }
                });
    }
    private void crossfade(View toShow, final View toHide) {

        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        toShow.setAlpha(0f);
        toShow.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        toShow.animate()
                .alpha(1f)
                .setDuration(mShortAnimationDuration)
                .setListener(null);

        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        hidefade(toHide);
    }

}
