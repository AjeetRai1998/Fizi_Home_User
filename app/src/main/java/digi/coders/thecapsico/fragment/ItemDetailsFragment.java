package digi.coders.thecapsico.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.adapter.AddOnAdapter;
import digi.coders.thecapsico.databinding.AddOnLayoutBinding;
import digi.coders.thecapsico.databinding.FragmentItemDetailsBinding;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.ShowProgress;
import digi.coders.thecapsico.model.AddOnProduct;
import digi.coders.thecapsico.model.Product;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ItemDetailsFragment extends Fragment {

    private Product product;
    private SingleTask singleTask;
    private String merchantId;
    private String qty;
    private int key;
    private List<AddOnProduct> addOnProductList;
    private double total1;
    List<String> idList=new ArrayList<>();
    List<String> priceList=new ArrayList<>();
    String[] idArray=new String[idList.size()];
    static double total=0;
    public ItemDetailsFragment(Product product,String merchantId,String qty,int key) {
        this.product = product;
        this.merchantId=merchantId;
        this.qty=qty;
        this.key=key;
    }

    FragmentItemDetailsBinding binding;
    int quantity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentItemDetailsBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        singleTask=(SingleTask)getActivity().getApplication();
        quantity=Integer.parseInt(binding.quantityTxt.getText().toString());
        total1=Double.parseDouble(product.getSellPrice());


        //load add on item list

        loadAddOnItem();


        if(quantity>0) {

                double total=Double.parseDouble(qty)* Double.parseDouble(product.getSellPrice());
                binding.price.setText("₹"+total);
            binding.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (quantity > 1) {
                        quantity--;
                        binding.quantityTxt.setText(quantity + "");
                         total1=quantity * Double.parseDouble(product.getSellPrice());
                        binding.price.setText("₹"+total1);

                    }
                }
            });
            binding.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quantity++;
                    binding.quantityTxt.setText(quantity + "");
                     total1=quantity * Double.parseDouble(product.getSellPrice());
                    binding.price.setText("₹"+total1);

                }
            });
            setData();

        }

        binding.addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart("");

            }
        });
    }

    private void loadAddOnItem() {
        ShowProgress.getShowProgress(getActivity()).show();
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call=myApi.getAddOnProductList(user.getId(),merchantId,product.getId());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful())
                {
                    try {
                        JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                        String res=jsonObject.getString("res");
                        String msg=jsonObject.getString("message");
                        Log.e("sdsd",response.toString());
                        if(res.equals("success"))
                        {
                            ShowProgress.getShowProgress(getActivity()).hide();
                            JSONArray jsonArray1=jsonObject.getJSONArray("data");
                            addOnProductList=new ArrayList<>();
                            for(int i=0;i<jsonArray1.length();i++)
                            {
                                AddOnProduct addOnProduct=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),AddOnProduct.class);
                                addOnProductList.add(addOnProduct);
                            }
                            binding.addOnItemList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false ));
                            AddOnAdapter adapter=new AddOnAdapter(addOnProductList,0);
                            //handleAdapter(adapter);
                            binding.addOnItemList.setAdapter(adapter);
                        }
                        else
                        {
                            ShowProgress.getShowProgress(getActivity()).hide();
                            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                ShowProgress.getShowProgress(getActivity()).hide();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    /*private void handleAdapter(AddOnAdapter adapter) {
        adapter.getAdapterPosition(new AddOnAdapter.GetPosition() {
            @Override
            public void findPosition(int position, AddOnProduct addOnProduct, AddOnLayoutBinding binding) {
                    if(!binding.checkBox.isChecked())
                    {
                               binding.checkBox.setChecked(true);
                                */
    /*total=total-Double.parseDouble(priceList.get(i));
                                refelectPrice(Double.parseDouble(priceList.get(i)),2);
                                idList.remove(i);
                                priceList.remove(i);*/
    /*
                                idList.add(addOnProduct.getId());
                                priceList.add(addOnProduct.getPrice());
                                Log.e("dfdf",addOnProduct.getPrice());
                                total=total+Integer.parseInt(addOnProduct.getPrice());
                                refelectPrice(Double.parseDouble(addOnProduct.getPrice()),1);
                    }
                    else
                    {
                        binding.checkBox.setChecked(false);
                        for(int i=0;i<idList.size();i++)
                        {
                            if(idList.get(i).equals(addOnProductList.get(position).getId()))
                            {
                                total=total-Double.parseDouble(priceList.get(i));
                            //    total1=total1-total;
                                refelectPrice(Double.parseDouble(priceList.get(i)),2);
                                idList.remove(i);
                                priceList.remove(i);
                            }
                        }
  //
                        //binding.price.setText("₹"+total);
                    }
                Log.e("ddsd",idList+"   "+priceList+"");
            }
        });
    }*/
    private void refelectPrice(double to,int s) {
        Log.e("df",to+"");
        if(s==1) {
            double tot = total1 + to;
            total1=tot;
            Log.e("dsd",total1+""+to);
            Log.e("dsd",tot+"");
            binding.price.setText("₹" + tot);
        }
        else
        {
            double tot = total1 - to;
            total1=tot;
            Log.e("dsd",total1+""+to);
            binding.price.setText("₹" + tot);
        }
    }

    private void setData() {
        Log.e("dsd",qty+product.getSellPrice()+"");

    }
    private void addToCart(String c) {
        ShowProgress.getShowProgress(getActivity()).show();
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Log.e("meid",merchantId+"");
        Log.e("lis",idList+"");
        Gson gson = new GsonBuilder().create();
        JsonArray idArr = gson.toJsonTree(idList).getAsJsonArray();
        JsonArray priceArr = gson.toJsonTree(priceList).getAsJsonArray();
        Log.e("total",total+"");
        Log.e("curent",idList.toString());
       // Log.e("jsonarray",myCustomArray.toString());
        Log.e("dr",idList+""+priceList+"");

        Call<JsonArray> call=myApi.addToCart(user.getId(),merchantId,product.getId(),binding.quantityTxt.getText().toString(),c,idList.toString(),priceList.toString(),"");
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful())
                {
                    Log.e("sdsd",response.toString());
                    try {
                        JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));JSONObject jsonObject=jsonArray.getJSONObject(0);
                        Log.e("sdsd",jsonArray.toString());
                        String res=jsonObject.getString("res");
                        String message=jsonObject.getString("message");
                        if(res.equals("success"))
                        {
                            ShowProgress.getShowProgress(getActivity()).hide();
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            ShowProgress.getShowProgress(getActivity()).hide();
                            showAlertDialog();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                ShowProgress.getShowProgress(getActivity()).hide();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder alert=new AlertDialog.Builder(getActivity());
        AlertDialog dialog=alert.create();
        alert.setMessage("Are you sure you want to clear previous cart");
        alert.setTitle("Clear Cart");
        alert.setCancelable(false);
        alert.setIcon(getResources().getDrawable(R.drawable.logo));
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                addToCart("add");
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding=null;
    }
}