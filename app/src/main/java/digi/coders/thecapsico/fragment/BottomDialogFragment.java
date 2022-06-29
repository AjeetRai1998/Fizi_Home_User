package digi.coders.thecapsico.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.activity.DashboardActivity;
import digi.coders.thecapsico.activity.ShopFullViewActivity;
import digi.coders.thecapsico.adapter.MasterSubCategoryListAdapter;

public class BottomDialogFragment extends BottomSheetDialogFragment {

    public static BottomDialogFragment newInstance() {
        return new BottomDialogFragment();
    }

    LinearLayout add_item;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_bottom_dialog, container, false);
        add_item=view.findViewById(R.id.add_item);
        add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MasterSubCategoryListAdapter.addPhotoBottomDialogFragment.dismiss();

                Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                        "    Item Added !  ", Snackbar.LENGTH_SHORT);
                snackBar.show();

                snackBar.getView().setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.color_green));
                snackBar.setActionTextColor(ContextCompat.getColor(getActivity(), R.color.color_white));
                //ShopFullViewActivity.cart.setVisibility(View.VISIBLE);
                Intent in =new Intent(getActivity(), DashboardActivity.class);
                in.putExtra("key",1);
                startActivity(in);
            }
        });

        return view;
    }
    public void exchangeFragment(Fragment fragment){

    }
}