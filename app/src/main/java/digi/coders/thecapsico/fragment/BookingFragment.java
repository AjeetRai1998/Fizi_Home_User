package digi.coders.thecapsico.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.activity.SearchActivity;
import digi.coders.thecapsico.databinding.FragmentBookingBinding;

public class BookingFragment extends Fragment {

    FragmentBookingBinding bookingBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bookingBinding=FragmentBookingBinding.inflate(inflater,container,false);
        return  bookingBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        //clik on searc
        bookingBinding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchActivity.class));

            }
        });
        //
        //getParentFragmentManager().beginTransaction().replace(R.id.booking_container,new BookingRestaurantFragment()).commit();
    }


/*    @Override
    public void onDestroy() {
        super.onDestroy();
        bookingBinding=null;
    }*/
}