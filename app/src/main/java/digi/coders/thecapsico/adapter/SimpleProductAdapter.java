package digi.coders.thecapsico.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.text.Html;
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

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.activity.CheckOutActivity;
import digi.coders.thecapsico.activity.StoreDetailsActivity;
import digi.coders.thecapsico.databinding.FoodLayoutBinding;
import digi.coders.thecapsico.fragment.CartAddonFragment;
import digi.coders.thecapsico.fragment.IWillChooseFragment;
import digi.coders.thecapsico.helper.AppConstraints;
import digi.coders.thecapsico.helper.Constraint;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.ShowProgress;
import digi.coders.thecapsico.model.Product;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SimpleProductAdapter extends RecyclerView.Adapter<SimpleProductAdapter.MyHolder> {
    private Product[] products;
    private Context ctx;
    private String status;

    int cartQuantity=0;
    FragmentManager fm;
    int addonStatus=0;
    public SimpleProductAdapter(Context ctx,Product[] products,String status,FragmentManager fm) {
        this.products = products;
        this.status=status;
        this.fm=fm;
        this.ctx=ctx;
        singleTask = (SingleTask) ctx.getApplicationContext();

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
//        ctx=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.food_layout,parent,false);
        return new MyHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {
        Product product=products[position];
        Picasso.get().load(AppConstraints.BASE_URL+AppConstraints.MASTER_PRODUCT+product.getIcon())
                .placeholder(R.drawable.placeholder1).into(holder.binding.proImage);
        holder.binding.proName.setText(product.getName());
        holder.binding.mrpPrice.setText("₹"+product.getMrp());
        holder.binding.proPrice.setText("₹"+product.getSellPrice());
        holder.binding.offPercent.setText((int)Double.parseDouble(product.getDiscount())+"% OFF");
        if(Double.parseDouble(product.getSellPrice())==Double.parseDouble(product.getMrp())){
            holder.binding.cutoff.setVisibility(View.GONE);
            holder.binding.offPercent.setVisibility(View.GONE);
        }else{
            holder.binding.cutoff.setVisibility(View.VISIBLE);
            holder.binding.offPercent.setVisibility(View.VISIBLE);
        }
        if(product.getDescription().equalsIgnoreCase("NA")){
            holder.binding.proDescription.setVisibility(View.GONE);
        }else {
            holder.binding.proDescription.setText(Html.fromHtml(product.getDescription()));
        }
        holder.binding.outOfStock.setVisibility(View.GONE);

        if(product.getMenu_type().equalsIgnoreCase("Veg")){
            Picasso.get().load(R.drawable.vegdot).into(holder.binding.type);

        }else{
            Picasso.get().load(R.drawable.non_veg).into(holder.binding.type);
        }
//        if(product.getAddOnProductName().equalsIgnoreCase("[]")){
//            holder.binding.customize.setVisibility(View.GONE);
//        }else{
//            holder.binding.customize.setVisibility(View.VISIBLE);
//        }
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);


        if (product.getIsStatus().equals("true")){
            holder.binding.outOfStock.setVisibility(View.GONE);
            holder.binding.cartAdd.setVisibility(View.VISIBLE);
            holder.binding.cartAddControl.setVisibility(View.GONE);
            holder.binding.notAvailable.setVisibility(View.GONE);

            if(product.getCartquantity() == null || product.getCartquantity().equals("0")){
                holder.binding.cartAdd.setVisibility(View.VISIBLE);
                holder.binding.cartAddControl.setVisibility(View.GONE);

            }else {
                holder.binding.cartAdd.setVisibility(View.GONE);
                holder.binding.cartAddControl.setVisibility(View.VISIBLE);
                holder.binding.count.setText(product.getCartquantity() + "");
            }
        }else {
            holder.binding.proImage.setColorFilter(filter);
            holder.binding.outOfStock.setVisibility(View.GONE);
            holder.binding.cartAdd.setVisibility(View.GONE);
            holder.binding.cartAddControl.setVisibility(View.GONE);
            holder.binding.notAvailable.setVisibility(View.VISIBLE);
        }

            holder.binding.cartAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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

                cartQuantity=Integer.parseInt(holder.binding.count.getText().toString());
                cartQuantity=cartQuantity+1;

                loadAddOnItem(holder,product,1,cartQuantity+"");
//                    Intent in=new Intent(ctx, ItemDetailsActivity.class);
//                    in.putExtra("key",9);
//                    in.putExtra("product_id",product.getId());
//                    ctx.startActivity(in);
            }
        });
        holder.binding.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cartQuantity=Integer.parseInt(holder.binding.count.getText().toString());
                cartQuantity=cartQuantity-1;

                if(cartQuantity==0){

                    holder.binding.cartAdd.setVisibility(View.VISIBLE);
                    holder.binding.cartAddControl.setVisibility(View.GONE);
                    addToCart(product,holder,"",cartQuantity+"");
                }else{

                    loadAddOnItem(holder,product,0,cartQuantity+"");
                }


//                    Intent in=new Intent(ctx, ItemDetailsActivity.class);
//                    in.putExtra("key",9);
//                    in.putExtra("product_id",product.getId());
//                    ctx.startActivity(in);
            }
        });

    }

    @Override
    public int getItemCount() {
        return products.length;
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        FoodLayoutBinding binding;
        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding=FoodLayoutBinding.bind(itemView);
        }
    }

    private SingleTask singleTask;
    private void addToCart(Product product,MyHolder holder,String c,String qty) {
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
    private void showAlertDialog(Product product,MyHolder holder,String qty) {
        AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
        AlertDialog dialog = alert.create();
        alert.setMessage("Would you like to discard your order from previous restaurant and start a new order?");
        alert.setTitle("Clear Cart");
        alert.setCancelable(false);
//        alert.setIcon(ctx.getResources().getDrawable(R.drawable.logo));
        alert.setPositiveButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CheckOutActivity.order=null;
                CheckOutActivity.couponCode=null;
                addToCart(product,holder,"Clear",qty);
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    private void loadAddOnItem(MyHolder holder,Product product,int i,String qty) {
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
