package digi.coders.thecapsico.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.adapter.RestaurantPhotoAdapter;
import digi.coders.thecapsico.databinding.FragmentRestaurantPhotoBinding;

public class RestaurantPhotoFragment extends Fragment {

    FragmentRestaurantPhotoBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentRestaurantPhotoBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }


    @Override
    public void onViewStateRestored(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);


        //load restaurant photo list
        loadRestaurantPhotoList();
    }

    private void loadRestaurantPhotoList() {
        binding.restaurantPhotoList.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        binding.restaurantPhotoList.setAdapter(new RestaurantPhotoAdapter());
    }


    /*@Override
    public void onDestroy() {
        super.onDestroy();
        binding=null;
    }*/
}