package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.adapter.AddOnGroupAdapter;
import digi.coders.thecapsico.databinding.ActivityItemDetailsBinding;
import digi.coders.thecapsico.databinding.ViewOrderBottomSheetLayoutBinding;
import digi.coders.thecapsico.helper.AppConstraints;
import digi.coders.thecapsico.helper.Constraint;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.SharedPrefManagerLocation;
import digi.coders.thecapsico.helper.ShowProgress;
import digi.coders.thecapsico.model.AddOnGroup;
import digi.coders.thecapsico.model.AddOnProduct;
import digi.coders.thecapsico.model.CartItem;
import digi.coders.thecapsico.model.CategoryProduct;
import digi.coders.thecapsico.model.OrderItem;
import digi.coders.thecapsico.model.Product;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDetailsActivity extends AppCompatActivity {

    ActivityItemDetailsBinding binding;
    public static Product product;
    public static String merchantId;
    private int key;

    private SingleTask singleTask;
    private String qty = "1";

    private List<AddOnProduct> addOnProductList;
    private List<AddOnGroup> groupList;
    private List<OrderItem> orderItemList;
    private double total1;
    List<String> idList = new ArrayList<>();
    List<String> singleIdList = new ArrayList<>();
    List<String> singlePriceList = new ArrayList<>();
    List<String> priceList = new ArrayList<>();
    String[] idArray = new String[idList.size()];
    String specialInstrcution = "";
    static double total = 0;
    int quantity;

    private int check;
    private String productId;

    public static Activity ItemDetailsActivity1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ItemDetailsActivity1=this;
        singleTask = (SingleTask) getApplication();
        key = getIntent().getIntExtra("key", 0);

        if (key == 9) {
            productId = getIntent().getStringExtra("product_id");
            Log.e("product_id", productId);
            loadProductDetails(productId);
        } else {
            setData();
            setProductData();
            loadAddOnItem();
            handleQuantity();
        }
        //handle back
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //loadCategoryWiseProductList();
        // handle quantity of item

        binding.appbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                    //  Collapsed
                    Log.e("sdsd", "collapse");
                    binding.itemName.setTextColor(getResources().getColor(R.color.pure_black));
                } else {
                    Log.e("sdsd", "expanaded");
                    //Expanded
                    binding.itemName.setTextColor(getResources().getColor(R.color.color_white));
                }
            }
        });

        // handle quantity

        binding.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart("");
            }
        });

        //getSupportFragmentManager().beginTransaction().replace(R.id.itemContainer,new ItemDetailsFragment(product,merchantId,quantity,key)).commit();

    }


    private void handleQuantity() {
        quantity = Integer.parseInt(binding.quantityTxt.getText().toString());
        if (key == 3) {

            total1 = product.getItemPrice();
        } else {
            total1 = Double.parseDouble(product.getSellPrice());
        }

        //load add On item

        if (quantity > 0) {
            int qty = Integer.parseInt(binding.quantityTxt.getText().toString());
            if (key == 3) {

                total1 = product.getItemPrice();
                double total = qty * total1;
                binding.price.setText("₹" + total);
            } else {
                total1 = Double.parseDouble(product.getSellPrice());
                double total = qty * total1;
                binding.price.setText("₹" + total);
            }


            binding.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int qty = Integer.parseInt(binding.quantityTxt.getText().toString());
                    if (qty > 1) {
                        //holder.minusbutton.setVisibility( View.INVISIBLE );

                        qty--;

                        binding.quantityTxt.setText(qty + "");
                        total1 = qty * Double.parseDouble(product.getSellPrice());
                        binding.price.setText("₹" + total1);
                        //updateQuantity(holder, position, orderItemList.get(position), qty, adapter);
                    } else {
                        if (qty != 0) {
                            addToCart("");
                        }
                        //updateQuantity(holder, position, orderItemList.get(position), 0, adapter);
                        //  emptyLayout.setVisibility(View.VISIBLE);
                        //orderItemList.remove(position);
                        //cartAdapter.notifyItemRemoved(position);
                        //cartAdapter.notifyItemRangeChanged(position, orderItemList.size());

                        //binding.layout.setVisibility(View.GONE);
                        //binding.cartEmptyLayout.setVisibility(View.VISIBLE);*//*
                    }



                    /*if (quantity > 0) {

                        quantity--;
                        binding.quantityTxt.setText(quantity + "");
                        total1 = quantity * Double.parseDouble(product.getSellPrice());
                        binding.price.setText("₹" + total1);
//                        if(!product.getCartquantity().equals("0")) {
                            if (quantity == 0) {

                                addToCart("");
                            }
                        //}

                    }*/
                }
            });
            binding.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int qty = Integer.parseInt(binding.quantityTxt.getText().toString());
                    qty++;
                    binding.quantityTxt.setText(qty + "");
                    total1 = qty * Double.parseDouble(product.getSellPrice());
                    binding.price.setText("₹" + total1);
                }
            });


        }

    }

    private void loadProductDetails(String productId) {

//        binding.shimmerViewContainer3.startShimmer();
//        binding.shimmerViewContainer3.setVisibility(View.VISIBLE);

        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        String lat = SharedPrefManagerLocation.getInstance(ItemDetailsActivity.this).locationModel(Constraint.LATITUDE);
        String lon = SharedPrefManagerLocation.getInstance(ItemDetailsActivity.this).locationModel(Constraint.LONGITUDE);
        Call<JsonArray> call = myApi.getProductDetails(user.getId(), productId);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                try {
                    JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String res = jsonObject.getString("res");
                    String msg = jsonObject.getString("message");
                    if (res.equals("success")) {
                        JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                        Product product = new Gson().fromJson(jsonArray1.getJSONObject(0).toString(), Product.class);
                        ItemDetailsActivity.product = product;
                        setData();
                        loadCartCount();
                        ItemDetailsActivity.merchantId = product.getMerchant_id();
                        loadAddOnItem();
                        setProductData();
                        handleQuantity();

//                        binding.shimmerViewContainer3.stopShimmer();
//                        binding.shimmerViewContainer3.setVisibility(View.GONE);

                    } else {
//                        binding.shimmerViewContainer3.stopShimmer();
//                        binding.shimmerViewContainer3.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
//                    binding.shimmerViewContainer3.stopShimmer();
//                    binding.shimmerViewContainer3.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
//                binding.shimmerViewContainer3.stopShimmer();
//                binding.shimmerViewContainer3.setVisibility(View.GONE);
            }
        });


    }


    private void setProductData() {
        if (product != null) {
            binding.itemName.setText(product.getName());
            Picasso.get().load(AppConstraints.BASE_URL + AppConstraints.MASTER_PRODUCT + product.getIcon()).placeholder(R.drawable.placeholder).into(binding.productImage);
            binding.price.setText("₹" + product.getItemPrice());
            binding.productDescription.setText(product.getDescription());
            binding.outOfStock.setVisibility(View.GONE);
            if (product.getIsStatus().equals("true")) {
                binding.outOfStock.setVisibility(View.GONE);
                binding.addToCart.setVisibility(View.VISIBLE);
            } else {
                binding.outOfStock.setVisibility(View.VISIBLE);
                binding.addToCart.setVisibility(View.GONE);
            }
        }
    }

    private void loadAddOnItem() {
        ShowProgress.getShowProgress(ItemDetailsActivity.this).show();
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Log.e("sds", merchantId + "" + product.getId());
        Call<JsonArray> call = myApi.getAddOnProductList(user.getId(), merchantId, product.getId());
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
                            ShowProgress.getShowProgress(ItemDetailsActivity.this).hide();
                            JSONArray jsonArray1 = jsonObject.getJSONArray("data");

                            groupList = new ArrayList<>();
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                AddOnGroup group = new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), AddOnGroup.class);
                                groupList.add(group);

                            }
                            binding.addOnItemList.setLayoutManager(new LinearLayoutManager(ItemDetailsActivity.this, LinearLayoutManager.VERTICAL, false));
                            //AddOnAdapter adapter=new AddOnAdapter(addOnProductList);
                            AddOnGroupAdapter adapter = new AddOnGroupAdapter(groupList);

                            //handleAdapter(adapter);
                            binding.addOnItemList.setAdapter(adapter);
                            binding.lineText.setVisibility(View.VISIBLE);
                        } else {
                            binding.lineText.setVisibility(View.GONE);
                            ShowProgress.getShowProgress(ItemDetailsActivity.this).hide();
                            Toast.makeText(ItemDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                ShowProgress.getShowProgress(ItemDetailsActivity.this).hide();
                Toast.makeText(ItemDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    int lastCheckedPosition = -1;


    private void refelectPrice(double to, int s) {
        Log.e("df", to + "");
        if (s == 1) {
            double tot = total1 + to;
            total1 = tot;
            Log.e("dsd", total1 + "" + to);
            Log.e("dsd", tot + "");
            binding.price.setText("₹" + tot);
        } else {
            double tot = total1 - to;
            total1 = tot;
            Log.e("dsd", total1 + "" + to);
            binding.price.setText("₹" + tot);
        }
    }

    private void setData() {
        //Log.e("dsd",qty+product.get()+"");
        if (product.getCartquantity().equals("0")) {
            binding.titleCart.setText("Add To Cart");

        } else {
            binding.quantityTxt.setText(product.getCartquantity());
            binding.titleCart.setText("Update To Cart");

        }
    }

    private void addToCart(String c) {
        ShowProgress.getShowProgress(ItemDetailsActivity.this).show();
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Log.e("meid", merchantId + "");
        Log.e("lis", idList + "");
        Gson gson = new GsonBuilder().create();
        JsonArray idArr = gson.toJsonTree(idList).getAsJsonArray();
        JsonArray priceArr = gson.toJsonTree(priceList).getAsJsonArray();
        Log.e("total", total + "");
        Log.e("curent", idList.toString());
        // Log.e("jsonarray",myCustomArray.toString());
        Log.e("dr", idList + "" + priceList + "");

        //merge price list of single and multiple ad on
        List<String> finalPriceList = new ArrayList<>();
        finalPriceList.addAll(priceList);
        finalPriceList.addAll(singlePriceList);

        //merge id list of single and multiple ad on
        List<String> finalIdList = new ArrayList<>();
        finalIdList.addAll(idList);
        finalIdList.addAll(singleIdList);
        specialInstrcution = binding.specialInstruction.getText().toString();
        Call<JsonArray> call = null;

        if (product.getCartquantity() == null || product.getCartquantity().equals("0")) {
            call = myApi.addToCart(user.getId(), merchantId, product.getId(), binding.quantityTxt.getText().toString(), c, finalIdList.toString(), finalPriceList.toString(), specialInstrcution);
        } else {
            call = myApi.updateQuantity(user.getId(), merchantId, product.getId(), binding.quantityTxt.getText().toString(), finalIdList.toString(), finalPriceList.toString(), specialInstrcution);

        }

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    Log.e("sdsd", response.toString());
                    try {
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        Log.e("sdsd", jsonArray.toString());
                        String res = jsonObject.getString("res");
                        String message = jsonObject.getString("message");
                        if (res.equals("success")) {
                            ShowProgress.getShowProgress(ItemDetailsActivity.this).hide();
                            if (!c.isEmpty()) {
                                binding.titleCart.setText("Add To cart");
                                loadAddOnItem();
                            }
                            Toast.makeText(ItemDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                            loadCartCount();

                        } else {
                            ShowProgress.getShowProgress(ItemDetailsActivity.this).hide();
                            showAlertDialog();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                ShowProgress.getShowProgress(ItemDetailsActivity.this).hide();
                Toast.makeText(ItemDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(ItemDetailsActivity.this);
        AlertDialog dialog = alert.create();
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

    private void loadCartCount() {
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call = myApi.getCartCount(user.getId());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {

                    try {
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String res = jsonObject.getString("res");
                        String msg = jsonObject.getString("message");
                        if (res.equals("success")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            CartItem cartItem = new Gson().fromJson(jsonObject1.toString(), CartItem.class);
                            openBottomSheet(cartItem);
                            //binding.price.setText("₹"+cartItem.getTotalprice()+"");
                        } else {

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }

    private void openBottomSheet(CartItem cartItem) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.myBottomSheetDialogTheme);
        View view1 = LayoutInflater.from(this).inflate(R.layout.view_order_bottom_sheet_layout, (FrameLayout) findViewById(R.id.standard_bottom_sheet), false);
        ViewOrderBottomSheetLayoutBinding binding = ViewOrderBottomSheetLayoutBinding.bind(view1);
//        binding.Amount.setText("₹ " + cartItem.getTotalprice());
        binding.viewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckOutActivity.merchant = cartItem.getMerchant()[0];
                startActivity(new Intent(ItemDetailsActivity.this, CheckOutActivity.class));
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setContentView(view1);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //loadCategoryWiseProductList();

    }


    private void loadCategoryWiseProductList() {
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        // Log.e("dssd",user.getId()+""+merchant.getId()+""+merchant.getMerchantCategoryId()+"");
        Call<JsonArray> call = myApi.getCategoryWiseProduct(user.getId(), merchantId);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                try {
                    JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String res = jsonObject.getString("res");
                    String msg = jsonObject.getString("message");
                    if (res.equals("success")) {

                        //categoryName.add(0,"Restaurant Support");
                        JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                        JSONArray jsonArray2 = jsonObject.getJSONArray("categoriesdata");
                        for (int i = 0; i < jsonArray2.length(); i++) {


                        }

                        for (int i = 0; i < jsonArray1.length(); i++) {
                            CategoryProduct categoryProduct = new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), CategoryProduct.class);
                            for (int j = 0; j < categoryProduct.getProduct().length; j++) {
                                if (categoryProduct.getProduct()[j].getCartquantity().equals("0")) {
                                    binding.titleCart.setText("Add To Cart");

                                } else {
                                    binding.quantityTxt.setText(product.getCartquantity());
                                    binding.titleCart.setText("Update To Cart");

                                }
                            }
                        }

                    } else {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });


    }

}
