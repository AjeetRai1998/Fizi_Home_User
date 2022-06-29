package digi.coders.thecapsico.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.adapter.RestaurantAdapter;
import digi.coders.thecapsico.adapter.RestaurantItemAdapter;
import digi.coders.thecapsico.databinding.FragmentBookingBinding;
import digi.coders.thecapsico.databinding.FragmentBookingRestaurantBinding;


public class BookingRestaurantFragment extends Fragment {
    FragmentBookingRestaurantBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentBookingRestaurantBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //load Restaurant Category
        //loadRestaurantCategory();


        //load nearest restaurant
        loadNearestRestaurant();


        //click on search

    }


    private void loadNearestRestaurant() {
        binding.restaurantList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        //binding.restaurantList.setAdapter(new RestaurantAdapter(2));
    }

/*    private void loadRestaurantCategory() {

        binding.restaurentCategoryList.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
        binding.restaurentCategoryList.setAdapter(new RestaurantItemAdapter(2));

    }*/



}