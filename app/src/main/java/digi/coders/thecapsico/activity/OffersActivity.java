package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.adapter.BestOffersAdapter;
import digi.coders.thecapsico.adapter.FeaturedOffersAdapter;

public class OffersActivity extends AppCompatActivity {

    private RecyclerView bestOffers, featuredOffers;

    public static Activity OffersActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        OffersActivity=this;
        initView();
        loadBestOffers();
        loadFeaturedProcess();
    }


    private void loadFeaturedProcess() {
        featuredOffers.setLayoutManager(new LinearLayoutManager(OffersActivity.this, LinearLayoutManager.VERTICAL, false));
        featuredOffers.setAdapter(new FeaturedOffersAdapter());
    }

    private void loadBestOffers() {
        bestOffers.setLayoutManager(new LinearLayoutManager(OffersActivity.this, LinearLayoutManager.HORIZONTAL, false));
        bestOffers.setAdapter(new BestOffersAdapter());
    }

    private void initView() {
        bestOffers = findViewById(R.id.offers_list);
        featuredOffers = findViewById(R.id.featured_offers_list);
    }

    public void goBack(View view) {
        finish();
    }

}