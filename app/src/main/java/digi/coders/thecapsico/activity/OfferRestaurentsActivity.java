package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.adapter.RestaurantAdapter;
import digi.coders.thecapsico.adapter.TopRestAdapter;
import digi.coders.thecapsico.helper.Constraint;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.SharedPrefManagerLocation;
import digi.coders.thecapsico.model.Merchant;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfferRestaurentsActivity extends AppCompatActivity {

    RecyclerView rest;
    SingleTask singleTask;
    ArrayList<Merchant> merchantList;
    public  static String id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_restaurents);

        singleTask=(SingleTask)getApplication();

        findViewById(R.id.back).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                }
        );
        rest=findViewById(R.id.toprest);

//        Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();
        loadRestaurant();
    }

    private void loadRestaurant() {


        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        String lat = SharedPrefManagerLocation.getInstance(getApplicationContext()).locationModel(Constraint.LATITUDE);
        String lon = SharedPrefManagerLocation.getInstance(getApplicationContext()).locationModel(Constraint.LONGITUDE);

        Call<JsonArray> call = myApi.getOfferMerchant(user.getId(), "", "", id,"", lat, lon);

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                try {
                    JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String res = jsonObject.getString("res");
                    String msg = jsonObject.getString("message");

                    if (res.equals("success")) {

                        merchantList = new ArrayList<>();
                        JSONArray jsonArray1 = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray1.length(); i++) {
                            Merchant merchant = new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), Merchant.class);
                            merchantList.add(merchant);
                        }


                        rest.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                        rest.setAdapter(new RestaurantAdapter(1, merchantList));
                    }
                    else {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }
}