package com.parag.fruits.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import com.parag.fruits.R;
import com.parag.fruits.adapters.FruitAdapter;
import com.parag.fruits.di.component.DaggerNetworkComponent;
import com.parag.fruits.di.component.NetworkComponent;
import com.parag.fruits.di.module.NetworkModule;
import com.parag.fruits.model.Fruit;
import com.parag.fruits.events.FruitEvent;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    NetworkComponent networkComponent;

    @Inject OkHttpClient okHttpClient;
    @Inject Request request;
    @Inject Moshi moshi;
    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @BindView(R.id.progressbar) ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        String url = "https://sampleapp-f4f6a.firebaseio.com/.json";
        networkComponent = DaggerNetworkComponent.builder().networkModule(new NetworkModule(url)).build();
        networkComponent.inject(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFruitEvent(FruitEvent fruitEvent) {
        progressBar.setVisibility(View.INVISIBLE);
        ArrayList<Fruit> fruitArrayList = new ArrayList<>(Arrays.asList(fruitEvent.fruits));
        FruitAdapter friutAdapter = new FruitAdapter(this,fruitArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(friutAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        getFruitData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void showSnackbar()
    {
        final Snackbar snackbar = Snackbar.make(findViewById(R.id.root_layout), "Unable to load", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("RETRY", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
                getFruitData();
            }
        });
        snackbar.show();
    }

    private void getFruitData()
    {
        progressBar.setVisibility(View.VISIBLE);
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call,@NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showSnackbar();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
            @Override
            public void onResponse(@NonNull Call call,@NonNull Response response) throws IOException {
                try {
                    String resp = response.body().string();
                    JSONObject jsonObject = new JSONObject(resp);
                    JSONArray fruitArray = jsonObject.getJSONArray("fruits");
                    resp = fruitArray.toString();
                    JsonAdapter<Fruit[]> jsonAdapter = moshi.adapter(Fruit[].class);
                    Fruit[] fruits = jsonAdapter.fromJson(resp);
                    EventBus.getDefault().post(new FruitEvent(fruits));
                }
                catch (JSONException je){je.printStackTrace();}
            }
        });
    }
}