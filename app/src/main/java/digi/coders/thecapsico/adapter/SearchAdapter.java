package digi.coders.thecapsico.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.activity.StoreDetailsActivity;
import digi.coders.thecapsico.databinding.SearchItemLayoutBinding;
import digi.coders.thecapsico.fragment.CartAddonFragment;
import digi.coders.thecapsico.fragment.IWillChooseFragment;
import digi.coders.thecapsico.helper.Constraint;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.ShowProgress;
import digi.coders.thecapsico.model.Product;
import digi.coders.thecapsico.model.SearchItem;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyHolder> {
    private List<SearchItem> list;
    private Activity ctx;
    int i;
    private String status;

    int cartQuantity=0;
    FragmentManager fm;
    int addonStatus=0;
    public SearchAdapter(List<SearchItem> list, int i, Activity ctx,FragmentManager fm) {
        this.i=i;
        this.list = list;
        this.fm=fm;
        this.ctx=ctx;
        singleTask = (SingleTask) ctx.getApplicationContext();
    }

    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item_layout, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {
        SearchItem searchItem=list.get(position);
        Picasso.get().load(searchItem.getIcon()).placeholder(R.drawable.placeholder1).into(holder.binding.icon);
        holder.binding.name.setText(searchItem.getName());


        holder.binding.menutype.setVisibility(View.VISIBLE);

        if(i==0) {
        if(searchItem.getData().equalsIgnoreCase("merchant"))
        {
            holder.binding.type.setText("Restaurant");
            holder.binding.cartAdd.setVisibility(View.GONE);
            holder.binding.cartAddControl.setVisibility(View.GONE);
            holder.binding.price.setVisibility(View.GONE);

            if(searchItem.getType().equalsIgnoreCase("veg")) {
                Picasso.get().load(R.drawable.vegdot).into(  holder.binding.menutype);
            }else{
                Picasso.get().load(R.drawable.non_veg).into(  holder.binding.menutype);
            }
        }
        else
        {

            holder.binding.cartAdd.setVisibility(View.VISIBLE);
            holder.binding.cartAddControl.setVisibility(View.GONE);
            holder.binding.type.setText("By "+searchItem.getShop_name());
            holder.binding.price.setText("\u20b9 "+searchItem.getEmail());
            holder.binding.price.setVisibility(View.VISIBLE);

            if(searchItem.getType().equalsIgnoreCase("veg")) {
                Picasso.get().load(R.drawable.vegdot).into(  holder.binding.menutype);
            }else{
                Picasso.get().load(R.drawable.non_veg).into(  holder.binding.menutype);
            }
        }
        }
        else{
            if(searchItem.getMenu_type().equalsIgnoreCase("veg")) {
                Picasso.get().load(R.drawable.vegdot).into(  holder.binding.menutype);
            }else{
                Picasso.get().load(R.drawable.non_veg).into(  holder.binding.menutype);
            }
            holder.binding.cartAdd.setVisibility(View.VISIBLE);
            holder.binding.cartAddControl.setVisibility(View.GONE);
            holder.binding.type.setVisibility(View.GONE);
            holder.binding.price.setText("\u20b9 "+searchItem.getEmail());
            holder.binding.price.setVisibility(View.VISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i==0) {
                    if (searchItem.getData().equals("merchant")) {
                        Intent in = new Intent(ctx, StoreDetailsActivity.class);
                        in.putExtra("merchant_id", searchItem.getId());
                        in.putExtra("key", 1);
                        ctx.startActivity(in);
                    }
                }
            }
        });

        holder.binding.cartAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Product product=new Product();
                product.setId(searchItem.getId());
                product.setMerchant_id(searchItem.getMobile());
                product.setSellPrice(searchItem.getEmail());
                product.setName(searchItem.getName());
                if(i==0) {
                    product.setMenu_type(searchItem.getType());
                }else{
                    product.setMenu_type(searchItem.getMenu_type());
                }
                loadAddOnItem(holder,product,1,"1");
//                    Intent in=new Intent(ctx, ItemDetailsActivity.class);
//                    in.putExtra("key",9);
//                    in.putExtra("product_id",product.getId());
//                    ctx.startActivity(in);
            }
        });

        holder.binding.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product=new Product();
                product.setId(searchItem.getId());
                product.setMerchant_id(searchItem.getMobile());
                product.setSellPrice(searchItem.getEmail());
                product.setName(searchItem.getName());
                if(i==0) {
                    product.setMenu_type(searchItem.getType());
                }else{
                    product.setMenu_type(searchItem.getMenu_type());
                }
                cartQuantity=Integer.parseInt(holder.binding.count.getText().toString());
                cartQuantity=cartQuantity+1;

                loadAddOnItem(holder,product,1,cartQuantity+"");
            }
        });
        holder.binding.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cartQuantity=Integer.parseInt(holder.binding.count.getText().toString());
                cartQuantity=cartQuantity-1;
                Product product=new Product();
                product.setMerchant_id(searchItem.getMobile());
                product.setSellPrice(searchItem.getEmail());
                product.setName(searchItem.getName());
                product.setId(searchItem.getId());
                if(i==0) {
                    product.setMenu_type(searchItem.getType());
                }else{
                    product.setMenu_type(searchItem.getMenu_type());
                }
                if(cartQuantity==0){

                    holder.binding.cartAdd.setVisibility(View.VISIBLE);
                    holder.binding.cartAddControl.setVisibility(View.GONE);
                    addToCart(product,holder,"",cartQuantity+"");
                }else{

                    loadAddOnItem(holder,product,0,cartQuantity+"");
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        SearchItemLayoutBinding binding;
        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding=SearchItemLayoutBinding.bind(itemView);
        }
    }


    private SingleTask singleTask;
    private void addToCart(Product product, MyHolder holder, String c, String qty) {
        ShowProgress.getShowProgress(ctx).show();
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit1().create(MyApi.class);

        Call<JsonObject> call = null;

        call = myApi.addCart(user.getId(), "Add","",product.getMerchant_id(), product.getId(), qty, product.getMrp(),product.getPrice(), product.getSellPrice(),product.getDiscount(), c,"yes",  Constraint.addOnIDList.toString(),Constraint.addOnPriceList.toString(),"");


        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.e("sdsd", response.toString());
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
//                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        Log.e("sdsd", jsonObject.toString());
                        String res = jsonObject.getString("res");
                        String message = jsonObject.getString("msg");
                        if (res.equals("success")) {

                            Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();

                            holder.binding.count.setText(qty);
                            if(!qty.equalsIgnoreCase("0")){
                                holder.binding.cartAdd.setVisibility(View.GONE);
                                holder.binding.cartAddControl.setVisibility(View.VISIBLE);
                            }
                            if(StoreDetailsActivity.refresh!=null){
                                StoreDetailsActivity.refresh.onRefresh();
                            }
                        } else if(res.equalsIgnoreCase("remove")) {
                            showAlertDialog(product,holder,qty);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ShowProgress.getShowProgress(ctx).hide();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                ShowProgress.getShowProgress(ctx).hide();
                Toast.makeText(ctx, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showAlertDialog(Product product, MyHolder holder, String qty) {
        AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
        AlertDialog dialog = alert.create();
        alert.setMessage("Are you sure you want to clear previous cart");
        alert.setTitle("Clear Cart");
        alert.setCancelable(false);
        alert.setIcon(ctx.getResources().getDrawable(R.drawable.logo));
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                addToCart(product,holder,"Clear",qty);
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

    private void loadAddOnItem(MyHolder holder, Product product, int i, String qty) {
        ShowProgress.getShowProgress(ctx).show();
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
//        Log.e("sds", merchantId + "" + product.getId());
        Call<JsonArray> call = myApi.getAddOnProductList(user.getId(), product.getMerchant_id(), product.getId());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String res = jsonObject.getString("res");
                        String msg = jsonObject.getString("message");
                        Log.e("sdsd", response.toString());
                        if (res.equals("success")) {
                            ShowProgress.getShowProgress(ctx).hide();
                            if(i==1) {
                                if(Integer.parseInt(qty)==1) {
                                    CartAddonFragment bottomSheet = new CartAddonFragment("",product, qty,  holder.binding.count,holder.binding.cartAdd,holder.binding.cartAddControl,0,"");
                                    //                        bottomSheet.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    bottomSheet.show(fm, "");
                                    addonStatus = 1;
                                }else{
                                    IWillChooseFragment bottomSheet = new IWillChooseFragment("",product, qty, holder.binding.count,holder.binding.cartAdd,holder.binding.cartAddControl,0,"");
//                        bottomSheet.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    bottomSheet.show(fm, "");
                                }
                            }else{
                                addToCart(product,holder,"",qty);
                            }

                        } else {
                            addonStatus=0;
                            ShowProgress.getShowProgress(ctx).hide();
                            addToCart(product,holder,"",qty);


                        }
                    } catch (JSONException e) {
                        ShowProgress.getShowProgress(ctx).hide();
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                ShowProgress.getShowProgress(ctx).hide();
                Toast.makeText(ctx, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
