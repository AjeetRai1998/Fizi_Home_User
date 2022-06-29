package digi.coders.thecapsico.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

import digi.coders.thecapsico.fragment.DeliveryFragment;
import digi.coders.thecapsico.fragment.BookingTableDetailsFragment;
import digi.coders.thecapsico.fragment.RestaurantPhotoFragment;
import digi.coders.thecapsico.fragment.ReviewsFragment;

public class ShopDetailTabAdapter extends FragmentPagerAdapter {

    private int tabCount;
    private Fragment fragment=null;

    public ShopDetailTabAdapter(@NonNull @NotNull FragmentManager fm,int tabCount) {
        super(fm);
        this.tabCount=tabCount;
    }


    @NotNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            fragment= new BookingTableDetailsFragment();
        }
        else if (position == 1) {
            fragment=new ReviewsFragment();
        }
        else
        {
            fragment=new RestaurantPhotoFragment();

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
