package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.adapter.RestaurantAdapter;

public class CouponFullViewActivity extends AppCompatActivity {

    private RecyclerView shopList;
    public static Activity CouponFullViewActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_full_view);
        CouponFullViewActivity=this;
        initView();
        loadShopList();
    }

    private void loadShopList() {
        shopList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        //shopList.setAdapter(new RestaurantAdapter(1));
    }

    private void initView() {
        shopList=findViewById(R.id.shop_list);
    }

    public void goBack(View view) {
        finish();
    }
}