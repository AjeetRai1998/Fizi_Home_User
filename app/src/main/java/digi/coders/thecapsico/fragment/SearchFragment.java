package digi.coders.thecapsico.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;


import org.jetbrains.annotations.NotNull;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.adapter.RestaurantItemAdapter;

public class SearchFragment extends Fragment {
    EditText et_search_product;
    ImageView iv_back,iv_croos;
    private RecyclerView popularCusinies;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return  inflater.inflate(R.layout.fragment_search, container, false);
    }
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });

        iv_croos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_search_product.setText("");
            }
        });

        et_search_product.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et_search_product, InputMethodManager.SHOW_IMPLICIT);
        loadPopularCuisines();
    }

    private void loadPopularCuisines() {
        popularCusinies.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        //popularCusinies.setAdapter(new RestaurantItemAdapter(1));
    }

    private void initView(View view) {
        popularCusinies=view.findViewById(R.id.popular_cuisines);
        et_search_product=view.findViewById(R.id.et_search_product);
        iv_back=view.findViewById(R.id.iv_back);
        iv_croos=view.findViewById(R.id.iv_croos);

    }

    public void exchangeFragment(Fragment fragment){
        //etActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment,fragment).commit();
    }
}