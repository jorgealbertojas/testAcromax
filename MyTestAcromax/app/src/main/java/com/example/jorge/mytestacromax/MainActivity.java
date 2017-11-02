package com.example.jorge.mytestacromax;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.jorge.mytestacromax.adapter.PlayerAdapter;
import com.example.jorge.mytestacromax.adapter.PlayerAdapter.PlayerAdapterOnClickHandler;
import com.example.jorge.mytestacromax.interfaceJson.Interface;
import com.example.jorge.mytestacromax.model.Player;
import com.example.jorge.mytestacromax.utilite.ListWrapperPlayer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.jorge.mytestacromax.utilite.Information.BASE_URL;
import static com.example.jorge.mytestacromax.utilite.Information.PUT_EXTRA_FILE;
import static com.example.jorge.mytestacromax.utilite.Information.PUT_EXTRA_HEIGHT;
import static com.example.jorge.mytestacromax.utilite.Information.PUT_EXTRA_NAME;
import static com.example.jorge.mytestacromax.utilite.Information.PUT_EXTRA_TYPE;
import static com.example.jorge.mytestacromax.utilite.Information.PUT_EXTRA_WIDTH;

/** Main with list the player*/

public class MainActivity extends AppCompatActivity implements PlayerAdapterOnClickHandler {

    private PlayerAdapter mMoviesAdapter;
    private Interface mPlayerInterface;
    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;
    private FrameLayout mFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get a reference to the ProgressBar using findViewById
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_numbers);
        mFrameLayout = (FrameLayout) findViewById(R.id.fl_main);

        /*
         * LinearLayoutManager can support HORIZONTAL or VERTICAL orientations. The reverse layout
         * parameter is useful mostly for HORIZONTAL layouts that should reverse for right to left
         * languages.
         */
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mRecyclerView.setHasFixedSize(true);

        /*
         * The ForecastAdapter is responsible for linking our weather data with the Views that
         * will end up displaying our weather data.
         */
        mMoviesAdapter = new PlayerAdapter(this);

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mRecyclerView.setAdapter(mMoviesAdapter);

        /*
         * The ProgressBar that will indicate to the user that we are loading data. It will be
         * hidden when no data is loading.
         *
         * Please note: This so called "ProgressBar" isn't a bar by default. It is more of a
         * circle. We didn't make the rules (or the names of Views), we just follow them.
         */
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        /* Once all of our views are setup, we can load the weather data. */
        if (isOnline()) {
            createStackoverflowAPI();
            mPlayerInterface.getPlayer().enqueue(playerCallback);

        }else{
            Context context = getApplicationContext();
            Toast toast = Toast.makeText(context, R.string.Error_Access,Toast.LENGTH_SHORT);
            toast.show();
    }
}

    /**
     * Call Get Information Movies .
     */
    private Callback<ListWrapperPlayer<Player>> playerCallback = new Callback<ListWrapperPlayer<Player>>() {
        @Override
        public void onResponse(Call<ListWrapperPlayer<Player>> call, Response<ListWrapperPlayer<Player>> response) {
            try{
                if (response.isSuccessful()) {
                    List<Player> data = new ArrayList<>();
                    data.addAll(response.body().items);
                    mRecyclerView.setAdapter(new PlayerAdapter(data));
                    mLoadingIndicator.setVisibility(View.INVISIBLE);

                } else {
                    Log.d("QuestionsCallback", "Code: " + response.code() + " Message: " + response.message());
                }
            }catch(NullPointerException e){
                System.out.println("onActivityResult consume crashed");
                runOnUiThread(new Runnable(){
                    public void run(){

                        Context context = getApplicationContext();
                        Toast toast = Toast.makeText(context, R.string.Error_Access_empty,Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            }
        }

        @Override
        public void onFailure(Call<ListWrapperPlayer<Player>> call, Throwable t) {
            Context context = getApplicationContext();
            Toast toast = Toast.makeText(context, R.string.Error_json_data,Toast.LENGTH_SHORT);
            toast.show();
        }

    };

    /**
     * checks if internet is ok .
     */
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    /** Find Data the API Json with Retrofit */
    private void createStackoverflowAPI() {
        Gson gson = new GsonBuilder()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        mPlayerInterface = retrofit.create(Interface.class);
    }


    /** Call activity Detail with Player*/
    @Override
    public void onClick(Player player) {
        Context context = this;

        Class destinationClass = PlayerActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(PUT_EXTRA_NAME, player.getName());
        intentToStartDetailActivity.putExtra(PUT_EXTRA_FILE, player.getFile());
        intentToStartDetailActivity.putExtra(PUT_EXTRA_TYPE, player.getType());
        intentToStartDetailActivity.putExtra(PUT_EXTRA_HEIGHT, Integer.toString(((mFrameLayout.getMeasuredHeight() - mLoadingIndicator.getMeasuredHeight())/2)) );
        intentToStartDetailActivity.putExtra(PUT_EXTRA_WIDTH, Integer.toString(((mFrameLayout.getMeasuredWidth() - mLoadingIndicator.getMeasuredWidth())/2)) );
        startActivity(intentToStartDetailActivity);
    }
}


