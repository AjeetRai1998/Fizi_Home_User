package digi.coders.thecapsico.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.adapter.ReviewAdapter;
import digi.coders.thecapsico.databinding.FragmentReviewsBinding;


public class ReviewsFragment extends Fragment {

    FragmentReviewsBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentReviewsBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //load review list
        loadReviewList();
    }

    private void loadReviewList() {
        binding.reviewList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
      //  binding.reviewList.setAdapter(new ReviewAdapter());
    }

/*    @Override
    public void onDestroy() {
        super.onDestroy();
        binding=null;
    }*/
}