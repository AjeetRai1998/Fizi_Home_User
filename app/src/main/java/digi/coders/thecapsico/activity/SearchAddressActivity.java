package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.adapter.PlacesListAdapter;
import digi.coders.thecapsico.helper.Constraint;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.ProgressDisplay;
import digi.coders.thecapsico.helper.Refresh;
import digi.coders.thecapsico.helper.SharedPrefManagerLocation;
import digi.coders.thecapsico.model.AddressResponse.ResponsePlace;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.model.UserAddress;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchAddressActivity extends AppCompatActivity implements Refresh {

    ImageView back;
    RecyclerView address;

    EditText search;
    private SingleTask singleTask;
    ProgressDisplay progressDisplay;
    public static Refresh refresh;
    LinearLayout no_data;
    ImageView noImg;
    public static Activity SearchAddressActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_address);
        SearchAddressActivity=this;
        singleTask = (SingleTask) getApplication();
        progressDisplay=new ProgressDisplay(this);
        refresh=this;
        address=findViewById(R.id.addressList);
        back=findViewById(R.id.back);
        search=findViewById(R.id.search);
        no_data=findViewById(R.id.no_data);
        noImg=findViewById(R.id.noImg);
        Glide.with(this)
                .load(R.raw.no_location)
                .into(noImg);
        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        search.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        if(s.toString().length()>3){
                            getLocation(s.toString());
                        }
                    }
                }
        );

    }
    private void getLocation(String str) {
        progressDisplay.showProgress();
        MyApi myApi=singleTask.getMapRetrofit().create(MyApi.class);
        Call<ResponsePlace> call=myApi.getLocations(str);
        call.enqueue(new Callback<ResponsePlace>() {
            @Override
            public void onResponse(Call<ResponsePlace> call, Response<ResponsePlace> response) {

                    try {

                        if(response.body().getPredictions().size()>0) {
                            address.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            address.setHasFixedSize(true);
                            address.setAdapter(new PlacesListAdapter(response.body().getPredictions(), getApplicationContext()));
                            no_data.setVisibility(View.GONE);
                            address.setVisibility(View.VISIBLE);
                        }else{
                            no_data.setVisibility(View.VISIBLE);
                            address.setVisibility(View.GONE);
//                            Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
//                        e.printStackTrace();
                        no_data.setVisibility(View.VISIBLE);
                        address.setVisibility(View.GONE);
//                        Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }

                progressDisplay.hideProgress();
            }
            @Override
            public void onFailure(Call<ResponsePlace> call, Throwable t) {
                progressDisplay.hideProgress();
                no_data.setVisibility(View.VISIBLE);
                address.setVisibility(View.GONE);
//                Toast.makeText(getApplicationContext(), t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        finish();
    }
}