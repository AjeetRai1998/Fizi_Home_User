package digi.coders.thecapsico.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import digi.coders.thecapsico.activity.SearchActivity;
import digi.coders.thecapsico.adapter.CategoriesAdapter;
import digi.coders.thecapsico.databinding.FragmentHomeBinding;
import digi.coders.thecapsico.fragmentmodel.HomeViewModel;
import digi.coders.thecapsico.model.MerchantCategory;
import me.relex.circleindicator.CircleIndicator;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    public static  int i;
    public static MerchantCategory merchantCategory;
    private HomeViewModel homeViewModel;


    /*public HomeFragment(int i, MerchantCategory category) {
        this.i = i;
        this.merchantCategory=category;

    }
*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //merchantCategory= (MerchantCategory) getArguments().getSerializable("category");
        //merchantCategory = (MerchantCategory) getArguments().getParcelable("category");
/*        homeViewModel=new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        merchantCategory=homeViewModel.getCategory();
        i=homeViewModel.getStaus();*/
        Log.e("sdsd",i+"");
        if (i == 2) {
            //store
            /*homeViewModel=new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
            homeViewModel.setCategory(merchantCategory);*/
            //getParentFragmentManager().beginTransaction().replace(R.id.home_container, new OtherStoreFragment(merchantCategory)).commit();
        } else {
            //food

            /*homeViewModel=new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
            homeViewModel.setCategory(merchantCategory);*/
            /*RestaurantFragment.merchantCategory=merchantCategory;
            getParentFragmentManager().beginTransaction().replace(R.id.home_container,new RestaurantFragment()).commit()*/;
        }
       /* address=getArguments().getString("address");
        if(!address.isEmpty())
        {


        }*/
/*        String[] d=Constraint.Loc.split(",");
        localityName.setText(d[d.length-4]);
        addressText.setText(Constraint.Loc);*/

        /*offersLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), OffersActivity.class));
            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LocationActivity.class));
            }
        });
*/


        //loadCategories();

        //


        //Hanlde search

        binding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });

    }

    private void loadCategories() {
        binding.categoryList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        binding.categoryList.setAdapter(new CategoriesAdapter(getParentFragmentManager()));
    }


    /*@Override
    public void onDestroy() {
        super.onDestroy();
        binding=null;
    }*/

}