package digi.coders.thecapsico.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.activity.AboutUsActivity;
import digi.coders.thecapsico.activity.CapsicoMoneyActivity;
import digi.coders.thecapsico.activity.EditAccountActivity;
import digi.coders.thecapsico.activity.HelpActivity;
import digi.coders.thecapsico.activity.IntroScreenActivity;
import digi.coders.thecapsico.activity.LoginActivity;
import digi.coders.thecapsico.activity.ManageAccountActivity;
import digi.coders.thecapsico.activity.MyAccountActivity;
import digi.coders.thecapsico.activity.MyFavouritesRestaurantActivity;
import digi.coders.thecapsico.activity.MyOrdersActivity;
import digi.coders.thecapsico.activity.OffersActivity;
import digi.coders.thecapsico.activity.PaymentManageActivity;
import digi.coders.thecapsico.databinding.FragmentProfileBinding;


public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentProfileBinding.inflate(inflater,container,false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //handle manage account
        binding.manageAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ManageAccountActivity.class));
            }
        });

        //handle payment

        binding.payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PaymentManageActivity.class));
            }
        });


        //handle my account
        binding.myAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), EditAccountActivity.class));
            }
        });


        //payment and refunds
        binding.paymentAndRedunds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PaymentManageActivity.class));
            }
        });
        //capsico money


        binding.capsicoMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CapsicoMoneyActivity.class));

            }
        });

        //myorders
        binding.myOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(getActivity(), MyOrdersActivity.class));
            }
        });

        //help


        binding.help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), HelpActivity.class));
            }
        });


        //logout

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), IntroScreenActivity.class));
            }
        });

        //handle offers
        binding.offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), OffersActivity.class));
            }
        });

        //about Us
        binding.aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
            }
        });

        //handle favourite store
        binding.myFavouriteStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MyFavouritesRestaurantActivity.class));
            }
        });

    }

    /*@Override
    public void onDestroy() {
        super.onDestroy();
        binding=null;
    */

}