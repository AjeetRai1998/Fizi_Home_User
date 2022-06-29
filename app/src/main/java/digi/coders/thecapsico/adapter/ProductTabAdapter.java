package digi.coders.thecapsico.adapter;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import digi.coders.thecapsico.fragment.ProductListFragment;
import digi.coders.thecapsico.fragment.RestaurantSupportFragment;
import digi.coders.thecapsico.model.Category;

public class ProductTabAdapter extends FragmentStatePagerAdapter {

    private int tabCount;
    private List<String> list;
//    private List<Category> categoryList;
    public ProductTabAdapter(@NonNull @NotNull FragmentManager fm,int tabCount,List<String> categoryName) {
        super(fm);
        this.tabCount=tabCount;
        this.list=categoryName;
  //      this.categoryList=category;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        if(position==0)
        {
            Log.e("sds",position+"");
            fragment=new RestaurantSupportFragment();
        }
        else
        {
            Log.e("sds",position+"");
            Bundle bundle=new Bundle();
//            bundle.putString("merchantId",categoryList.get(position).getMerchantId());
  //          bundle.putString("categoryId",categoryList.get(position).getId());

            fragment=new ProductListFragment();
            fragment.setArguments(bundle);

        }
        return fragment ;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position);
    }
}
