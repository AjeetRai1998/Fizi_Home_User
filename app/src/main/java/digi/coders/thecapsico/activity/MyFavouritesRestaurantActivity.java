package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import digi.coders.thecapsico.adapter.FavouriteRestaurantAdapter;
import digi.coders.thecapsico.databinding.ActivityMyFavouritesRestaurantBinding;

public class MyFavouritesRestaurantActivity extends AppCompatActivity {

    ActivityMyFavouritesRestaurantBinding binding;
    public static Activity MyFavouritesRestaurantActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMyFavouritesRestaurantBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MyFavouritesRestaurantActivity=this;
        //handle back

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //load favourites list

        binding.favouritesRestaurantList.setLayoutManager(new LinearLayoutManager(MyFavouritesRestaurantActivity.this,LinearLayoutManager.VERTICAL,false));
        binding.favouritesRestaurantList.setAdapter(new FavouriteRestaurantAdapter());



    }
}