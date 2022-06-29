package digi.coders.thecapsico.activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.adapter.RestaurantAdapter;
import digi.coders.thecapsico.adapter.TopPicsAdapter;
public class BrandsFullViewActivity extends AppCompatActivity {
    private RecyclerView brandsItemList;

    public static Activity BrandsFullViewActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brands_full_view);
        BrandsFullViewActivity=this;
        initView();
        loadBrandsItemList();
    }
    private void loadBrandsItemList() {
        brandsItemList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        //brandsItemList.setAdapter(new RestaurantAdapter(1));
    }
    private void initView() {
        brandsItemList=findViewById(R.id.brands_item_list);
    }

    public void goBack(View view) {
        finish();
    }
}