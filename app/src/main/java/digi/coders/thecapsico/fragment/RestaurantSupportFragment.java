package digi.coders.thecapsico.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.jetbrains.annotations.NotNull;
import digi.coders.thecapsico.R;
import digi.coders.thecapsico.activity.PlasticCutleyActivity;
import digi.coders.thecapsico.databinding.FragmentCommunitySupportBinding;


public class RestaurantSupportFragment extends Fragment {
    FragmentCommunitySupportBinding binding;
    private int i=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentCommunitySupportBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //make call
        binding.makeCallLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i==0)
                {
                    binding.callLayout.setVisibility(View.VISIBLE);
                    binding.call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:"+232323232));//change the number
                            startActivity(callIntent);
                        }
                    });
                    i++;
                }
                else
                {
                    binding.callLayout.setVisibility(View.GONE);
                    i=0;
                }
            }
        });


        //no plastic cultery

        binding.numberOfCutlery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PlasticCutleyActivity.class));
            }
        });
    }

    /*@Override
    public void onDestroy() {
        super.onDestroy();
        binding=null;
    }*/
}