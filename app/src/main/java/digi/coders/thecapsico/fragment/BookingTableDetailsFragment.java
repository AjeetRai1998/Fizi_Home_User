package digi.coders.thecapsico.fragment;

import android.content.Intent;
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
import digi.coders.thecapsico.activity.BookingTablesActivity;
import digi.coders.thecapsico.adapter.MasterSubCategoryListAdapter;
import digi.coders.thecapsico.adapter.MenuAdapter;
import digi.coders.thecapsico.databinding.FragmentDiningBinding;


public class BookingTableDetailsFragment extends Fragment {

    FragmentDiningBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentDiningBinding.inflate(inflater,container,false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //loadMenu


        loadMenu();



        //booking a table

        binding.bookingTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BookingTablesActivity.class));
            }
        });
    }

    private void loadMenu() {
        binding.menuList.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.HORIZONTAL));
        binding.menuList.setAdapter(new MenuAdapter());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding=null;
    }
}