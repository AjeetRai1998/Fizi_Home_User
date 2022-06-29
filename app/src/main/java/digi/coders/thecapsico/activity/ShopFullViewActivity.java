package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.adapter.MasterProductAdapter;
import digi.coders.thecapsico.adapter.ShopDetailTabAdapter;
import digi.coders.thecapsico.databinding.ActivityShopFullViewBinding;
import digi.coders.thecapsico.fragment.BottomDialogFragment;
import digi.coders.thecapsico.fragment.SearchFragment;

public class ShopFullViewActivity extends AppCompatActivity {


    public static LinearLayout addcart,cart;
    public static int i=0;
    public static   BottomDialogFragment addPhotoBottomDialogFragment;
    private RecyclerView masterProductList;
    private ExtendedFloatingActionButton floatingButton;
    ActivityShopFullViewBinding binding;
    public static Activity ShopFullViewActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityShopFullViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ShopFullViewActivity=this;
        //cart.setVisibility(View.GONE);

        /*addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPhotoBottomDialogFragment =BottomDialogFragment.newInstance();
                addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                        "add_photo_dialog_fragment");

            }
        });*/

        //handle cart
        /*binding.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),DashboardActivity.class);
                intent.putExtra("key",1);
                startActivity(intent);
                finish();

            }
        });
*/



        //handle back button
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //handle tab

        binding.tablayout.addTab(binding.tablayout.newTab().setText("Booking"));
        binding.tablayout.addTab(binding.tablayout.newTab().setText("Reviews"));
        binding.tablayout.addTab(binding.tablayout.newTab().setText("Photos"));
        binding.tablayout.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.viewPager.setAdapter(new ShopDetailTabAdapter(getSupportFragmentManager(),binding.tablayout.getTabCount()));

        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tablayout));

        binding.tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //handle search
        /*binding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShopFullViewActivity.this,SearchActivity.class));
            }
        });
*/
        binding.addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void loadMasterItems() {

    }

    private void loadMasterProduct() {
        masterProductList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        //masterProductList.setAdapter(new MasterProductAdapter(getSupportFragmentManager()));

    }

    private void initView() {
        /*back=findViewById(R.id.back);
        food_switch=findViewById(R.id.food_switch);
        food_type=findViewById(R.id.food_type);
        //addcart=findViewById(R.id.addcart);
        cart=findViewById(R.id.cart);
        search=findViewById(R.id.search);*/

    }
}