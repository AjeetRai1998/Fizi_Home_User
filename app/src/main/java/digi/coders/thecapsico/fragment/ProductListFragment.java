package digi.coders.thecapsico.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.adapter.MasterProductAdapter;
import digi.coders.thecapsico.adapter.MasterSubCategoryListAdapter;
import digi.coders.thecapsico.databinding.FragmentProductListBinding;
import digi.coders.thecapsico.fragmentmodel.ProductViewModel;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.ShowProgress;
import digi.coders.thecapsico.model.Category;
import digi.coders.thecapsico.model.Product;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductListFragment extends Fragment {
    private String categoryId;
    private String merchantId;
    private SingleTask singleTask;
    private List<Product> productList;


    FragmentProductListBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentProductListBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        singleTask=(SingleTask)getActivity().getApplication();
        categoryId=getArguments().getString("categoryId");
        merchantId=getArguments().getString("merchantId");
        Log.e("id",categoryId+merchantId+"");
        //load MastertProductItem List
        if(merchantId!=null) {
            Log.e("sdsd", merchantId);
        }
        loadMasterProductList(2);
    }
    private void loadMasterProductList(int i) {

        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call=myApi.getAllProduct(user.getId(),categoryId);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                try {

                    Log.e("respomse",response.toString());
                    JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject= jsonArray.getJSONObject(0);
                    String res=jsonObject.getString("res");
                    String msg=jsonObject.getString("message");
                    if(res.equals("success"))
                    {

                        binding.progress.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        productList=new ArrayList<>();
                        JSONArray jsonArray1=jsonObject.getJSONArray("data");
                        for(int i=0;i<jsonArray1.length();i++) {
                            Product product = new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), Product.class);
                            productList.add(product);

                        }
                        binding.masterProductItemList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        binding.masterProductItemList.setAdapter(new MasterSubCategoryListAdapter(getParentFragmentManager(),i,getActivity(),productList,merchantId));

                    }
                    else
                    {
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        binding.progress.setVisibility(View.GONE);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                binding.progress.setVisibility(View.GONE);
            }
        });






    }

    /*@Override
    public void onDestroy() {
        super.onDestroy();
        binding=null;
    }*/
}