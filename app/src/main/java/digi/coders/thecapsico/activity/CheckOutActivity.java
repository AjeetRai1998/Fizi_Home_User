package digi.coders.thecapsico.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cashfree.pg.CFPaymentService;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;
import com.skydoves.elasticviews.ElasticButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import dev.shreyaspatil.easyupipayment.EasyUpiPayment;
import dev.shreyaspatil.easyupipayment.exception.AppNotFoundException;
import dev.shreyaspatil.easyupipayment.listener.PaymentStatusListener;
import dev.shreyaspatil.easyupipayment.model.PaymentApp;
import dev.shreyaspatil.easyupipayment.model.TransactionDetails;
import digi.coders.thecapsico.R;
import digi.coders.thecapsico.adapter.CartOrderItemAdapter;
import digi.coders.thecapsico.databinding.ActivityCheckOutBinding;
import digi.coders.thecapsico.databinding.CheckoutBottomSheetLayoutBinding;
import digi.coders.thecapsico.databinding.OrderItemDesignBinding;
import digi.coders.thecapsico.databinding.PaymentOptionLayoutBinding;
import digi.coders.thecapsico.databinding.SelectPayementBottomsheetBinding;
import digi.coders.thecapsico.fragment.IWillChooseFragment;
import digi.coders.thecapsico.fragment.WaitingFragment;
import digi.coders.thecapsico.helper.AppConstraints;
import digi.coders.thecapsico.helper.Constraint;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.Refresh;
import digi.coders.thecapsico.helper.SharedPrefManagerLocation;
import digi.coders.thecapsico.helper.ShowProgress;
import digi.coders.thecapsico.model.CartItem;
import digi.coders.thecapsico.model.Coupon;
import digi.coders.thecapsico.model.Merchant;
import digi.coders.thecapsico.model.Order;
import digi.coders.thecapsico.model.OrderItem;
import digi.coders.thecapsico.model.Product;
import digi.coders.thecapsico.model.ShippingCharges;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.model.UserAddress;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cashfree.pg.CFPaymentService.PARAM_APP_ID;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_EMAIL;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_NAME;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_PHONE;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_AMOUNT;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_CURRENCY;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_ID;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_NOTE;

public class CheckOutActivity extends AppCompatActivity implements Refresh, PaymentStatusListener {
    ActivityCheckOutBinding binding;
    private int i = 0;
    public static UserAddress address;
    private String option;
    private SingleTask singleTask;
    private List<OrderItem> orderItemList;
    public static Merchant merchant;
    public static double subTotal, shipingAmount, otherCharges,packingCharges=0;
    public static String paymentMethodType = "COD";
    public static Order order;
    public static Coupon couponCode;
    public static String couponDiscount = "0";

    public static Refresh refresh;
    public static Activity checkOut;

    String text="";
    public static String txtInstruction="";
    public static Activity CheckOutActivity1;
    TextView restCharge,packingCharge,deliveryFee;
    Balloon balloon;
    String packingChargeAmount="0",otherChargeAmount="0",deliveryFeeAmount="0",walletStr="",walletAmount="",cashStr="",onlineStr="";

    public static int coupoShowStatus=0,walletSelect=0,codSelect=0,onlineSelect=0,walletAmountStatus=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckOutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        CheckOutActivity1=this;
        getUpiApp();
        option = getIntent().getStringExtra("option");
        singleTask = (SingleTask) getApplication();
        checkOut=this;
        refresh=this;

        paymentMethodType = "COD";
        Glide.with(this)
                .load(R.raw.loader)
                .into(binding.animation);
        Glide.with(this)
                .load(R.raw.no_data)
                .into(binding.noImage);


        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                couponCode=null;
                finish();
            }
        });


        binding.addMore.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(getApplicationContext(), StoreDetailsActivity.class);
                        in.putExtra("key", 1);
                        in.putExtra("merchant_id", merchant.getId());
                        startActivity(in);
                    }
                }
        );

        if (couponCode != null) {
            binding.couponLay.setVisibility(View.VISIBLE);
            binding.forward.setVisibility(View.GONE);
            binding.txtChange.setVisibility(View.VISIBLE);
            binding.txtOfferamount.setVisibility(View.VISIBLE);
            binding.txtDesc.setVisibility(View.VISIBLE);
            binding.textApplied.setText("Code '"+couponCode.getCouponCode()+"' applied!");
//            binding.offerTitleImg.setImageResource(R.drawable.offer_applied);
//            setData();
            loadCartCount();
//            checkPreAddress();
            //handle deliveryedit
        } else {
//            setData();
//            binding.applyCouponLayout.setVisibility(View.GONE);
            loadCartCount();
            binding.couponLay.setVisibility(View.GONE);
            binding.forward.setVisibility(View.VISIBLE);
            binding.txtChange.setVisibility(View.GONE);
            binding.txtOfferamount.setVisibility(View.GONE);
            binding.txtDesc.setVisibility(View.GONE);
            binding.textApplied.setText("Apply Coupon");
//            binding.offerTitleImg.setImageResource(R.drawable.offer_icon2);
            //handle deliveryedit
        }
        setData();


        if (AppConstraints.isFromDelLoc) {
            // checkPreAddress();
            AppConstraints.isFromDelLoc=true;
            checkCity(address);
        }
        else {
            AppConstraints.isFromDelLoc=true;
            checkCity(address);
        }

        //handle color
        binding.ten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.ten.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
                binding.twenty.setCardBackgroundColor(getResources().getColor(R.color.color_white));
                binding.thirty.setCardBackgroundColor(getResources().getColor(R.color.color_white));
                binding.tenTxt.setTextColor(getResources().getColor(R.color.color_white));
                binding.twentyTxt.setTextColor(getResources().getColor(R.color.color_black));
                binding.thirtyTxt.setTextColor(getResources().getColor(R.color.color_black));
                binding.tipAmount.getEditText().setText("10");
                binding.courierTip.setText("₹" + binding.tipAmount.getEditText().getText().toString() + "");
                binding.tipLay.setVisibility(View.VISIBLE);
            }
        });


        binding.twenty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.ten.setCardBackgroundColor(getResources().getColor(R.color.color_white));
                binding.twenty.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
                binding.thirty.setCardBackgroundColor(getResources().getColor(R.color.color_white));
                binding.tenTxt.setTextColor(getResources().getColor(R.color.color_black));
                binding.twentyTxt.setTextColor(getResources().getColor(R.color.color_white));
                binding.thirtyTxt.setTextColor(getResources().getColor(R.color.color_black));
                binding.tipAmount.getEditText().setText("20");
                binding.courierTip.setText("₹" + binding.tipAmount.getEditText().getText().toString() + "");
                binding.tipLay.setVisibility(View.VISIBLE);
            }
        });

        binding.thirty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.ten.setCardBackgroundColor(getResources().getColor(R.color.color_white));
                binding.twenty.setCardBackgroundColor(getResources().getColor(R.color.color_white));
                binding.thirty.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
                binding.tenTxt.setTextColor(getResources().getColor(R.color.color_black));
                binding.twentyTxt.setTextColor(getResources().getColor(R.color.color_black));
                binding.thirtyTxt.setTextColor(getResources().getColor(R.color.color_white));
                binding.tipAmount.getEditText().setText("30");
                binding.courierTip.setText("₹" + binding.tipAmount.getEditText().getText().toString() + "");
                binding.tipLay.setVisibility(View.VISIBLE);
            }
        });

        binding.applyCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (i == 0) {
//                    binding.forward.setVisibility(View.GONE);
//                    binding.down.setVisibility(View.VISIBLE);
//                    binding.applyCouponLayout.setVisibility(View.VISIBLE);
//                    binding.apply.setText("Apply Coupon");
//                    binding.couponLay.setVisibility(View.VISIBLE);
//                    //open
//                    i++;
//                } else {
//                    //close
//                    binding.down.setVisibility(View.GONE);
//                    binding.forward.setVisibility(View.VISIBLE);
//                    binding.applyCouponLayout.setVisibility(View.GONE);
//                    binding.couponLay.setVisibility(View.GONE);
//                    i = 0;
//                }

                CouponActivity.merchantId=merchant.getId();
                Intent intent=new Intent(getApplicationContext(),CouponActivity.class);
                intent.putExtra("key","0");
                startActivity(intent);
            }
        });

        binding.couponCode.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CouponActivity.merchantId = merchant.getId();
                Intent in = new Intent(CheckOutActivity.this, CouponActivity.class);
                in.putExtra("key", 1);
                startActivity(in);

            }
        });

        //laodOrderItem
        //  loadItemOrder();

        binding.address.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (binding.address.getText().toString().equalsIgnoreCase("address")) {
                            Intent in = new Intent(CheckOutActivity.this, DeliveryLocationActivity.class);
                            in.putExtra("key", 12);
                            startActivity(in);
                        } else {
                            Intent in = new Intent(CheckOutActivity.this, DeliveryLocationActivity.class);
                            in.putExtra("key", 12);
                            startActivity(in);
                        }
                    }
                }
        );
        binding.editDeliveryImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.editDelivery.getText().equals("Edit")) {
                    Intent in = new Intent(CheckOutActivity.this, DeliveryLocationActivity.class);
                    in.putExtra("key", 12);
                    startActivity(in);
                } else {
                    Intent in = new Intent(CheckOutActivity.this, DeliveryLocationActivity.class);
                    in.putExtra("key", 12);
                    startActivity(in);
                }
            }
        });

//        String js=singleTask.getValue("user");
//        User user=new Gson().fromJson(js,User.class);
//        if (user.getName().isEmpty()){
//            editProfileConfirmationDialog();
//        }

        //select payment method
         balloon = new Balloon.Builder(CheckOutActivity.this)
                .setArrowSize(10)
                .setLayout(R.layout.other_charges_details)
                .setArrowOrientation(ArrowOrientation.TOP)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                .setArrowPosition(0.2f)
                .setWidth(BalloonSizeSpec.WRAP)
                .setCornerRadius(11f)
                .setAlpha(1.0f)
                .setBackgroundColor(ContextCompat.getColor(CheckOutActivity.this, R.color.pure_black))
                .setBalloonAnimation(BalloonAnimation.FADE)
                .setLifecycleOwner(new LifecycleOwner() {
                    @NonNull
                    @Override
                    public Lifecycle getLifecycle() {
                        return null;
                    }
                })
                .build();
         restCharge=balloon.getContentView().findViewById(R.id.restCharge);
         packingCharge=balloon.getContentView().findViewById(R.id.packingCharge);
        deliveryFee=balloon.getContentView().findViewById(R.id.deliveryFee);
        binding.lineotherCharges.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        packingCharge.setText("\u20b9"+packingChargeAmount);
                        restCharge.setText("\u20b9"+otherChargeAmount);
                        deliveryFee.setText("\u20b9"+deliveryFeeAmount);
                        balloon.showAlignTop(binding.lineotherCharges);
                    }
                }
        );
        binding.paymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.paymentMethod.getText().toString().equalsIgnoreCase("Select Address")){
                    if (binding.editDelivery.getText().equals("Edit")) {
                        Intent in = new Intent(CheckOutActivity.this, DeliveryLocationActivity.class);
                        in.putExtra("key", 12);
                        startActivity(in);
                    } else {
                        Intent in = new Intent(CheckOutActivity.this, DeliveryLocationActivity.class);
                        in.putExtra("key", 12);
                        startActivity(in);
                    }
                }else {
                    if (text.equalsIgnoreCase("false")) {
                        Toast.makeText(getApplicationContext(), "You have an item in cart that is Out Of Stock !", Toast.LENGTH_LONG).show();

                    } else {
                        if (!binding.address.getText().toString().equalsIgnoreCase("address")) {
                            cashStr="";
                            onlineStr="";
                            walletStr="";
                            onlineSelect=0;
                            codSelect=0;
                            walletSelect=0;
                            paymentMethod(v);
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Select Address First !", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });


        //pay here
        /*binding.pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/


        binding.message.setText(txtInstruction);
        binding.apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.apply.getText().equals("Remove Coupon")) {
                    calculateTip(subTotal, 0);
                    binding.down.setVisibility(View.GONE);
                    binding.forward.setVisibility(View.VISIBLE);
                    binding.applyCouponLayout.setVisibility(View.GONE);
                    Toast.makeText(CheckOutActivity.this, "Coupn Remove SuccessFully", Toast.LENGTH_SHORT).show();

                } else {
                    if (binding.couponCode.getEditText().getText().toString().isEmpty()) {
                        Toast.makeText(CheckOutActivity.this, "Please Enter Coupon code", Toast.LENGTH_SHORT).show();
                    } else {

                        ShowProgress.getShowProgress(CheckOutActivity.this).show();
                        String js = singleTask.getValue("user");
                        User user = new Gson().fromJson(js, User.class);
                        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
                        String lat = SharedPrefManagerLocation.getInstance(getApplicationContext()).locationModel(Constraint.LATITUDE);
                        String lon = SharedPrefManagerLocation.getInstance(getApplicationContext()).locationModel(Constraint.LONGITUDE);

                        Call<JsonArray> call = myApi.getAllCoupons(user.getId(), merchant.getId(), lat,lon,binding.couponCode.getEditText().getText().toString());
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
                                            ShowProgress.getShowProgress(CheckOutActivity.this).hide();
                                            JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                                            JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
                                            Toast.makeText(CheckOutActivity.this, msg, Toast.LENGTH_SHORT).show();
                                            binding.apply.setText("Coupon applied");
                                            Coupon coupon = new Gson().fromJson(jsonObject1.toString(), Coupon.class);
//                                            calculateCouponDiscount(coupon);

                                        } else {
                                            ShowProgress.getShowProgress(CheckOutActivity.this).hide();
                                            Toast.makeText(CheckOutActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<JsonArray> call, Throwable t) {
                                ShowProgress.getShowProgress(CheckOutActivity.this).hide();
                                Toast.makeText(CheckOutActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                }


            }
        });

        binding.message.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        txtInstruction=s.toString();
                    }
                }
        );

        //openBottomSheet();

    }

    private void editProfileConfirmationDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(CheckOutActivity.this);
        AlertDialog dialog = alert.create();
        alert.setMessage("Update your profile for better response.");
        alert.setTitle("Update Profile");
        alert.setCancelable(false);
        alert.setIcon(getResources().getDrawable(R.drawable.logo));
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(CheckOutActivity.this, EditAccountActivity.class);
                startActivity(intent);
            }
        });
//
        alert.setCancelable(false);
        alert.show();

    }

    private void fetchWalletAmount(SelectPayementBottomsheetBinding binding1) {
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call = myApi.getUserProfile(user.getId());
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
                            JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                            JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
                            User user = new Gson().fromJson(jsonObject1.toString(), User.class);
                            walletAmount=user.getWallet();
                            if(Double.parseDouble(user.getWallet())>0) {
                                if (Double.parseDouble(binding.totalAmount.getText().toString().replace("₹", "")) > Double.parseDouble(user.getWallet())) {

                                    binding1.wallet.setVisibility(View.VISIBLE);
                                    binding1.txtMsg.setVisibility(View.VISIBLE);
                                    binding1.walletImage.setImageDrawable(getResources().getDrawable(R.drawable.check));
                                    walletStr = "Wallet";
                                    binding1.walletAmount.setText("₹ " + user.getWallet()+" will be adjusted from wallet against the order of ₹ "+binding.totalAmount.getText().toString().replace("₹", ""));
                                    binding1.txtMsg.setText("Choose method to pay the remaining amount of ₹ "+(Double.parseDouble(binding.totalAmount.getText().toString().replace("₹", ""))-Double.parseDouble(user.getWallet())));
                                    walletAmountStatus=1;
                                    binding1.wallet.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if(walletSelect==0) {
                                                binding1.walletImage.setImageDrawable(getResources().getDrawable(R.drawable.check));
                                                walletStr = "Wallet";
                                                walletSelect=1;
                                                binding1.txtMsg.setVisibility(View.VISIBLE);
                                                binding1.walletAmount.setText("₹ " + user.getWallet()+" will be adjusted from wallet against the order of ₹ "+binding.totalAmount.getText().toString().replace("₹", ""));

                                            }else{
                                                binding1.walletImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
                                                walletStr = "";
                                                walletSelect=0;
                                                binding1.txtMsg.setVisibility(View.GONE);
                                                binding1.walletAmount.setText("Wallet Balance ₹ "+user.getWallet()+" (Partial Payment with wallet)");

                                            }
                                        }
                                    });
                                } else {
                                    walletAmountStatus=0;
                                    binding1.wallet.setVisibility(View.VISIBLE);
                                    binding1.txtMsg.setVisibility(View.GONE);

                                    binding1.walletImage.setImageDrawable(getResources().getDrawable(R.drawable.check));
                                    walletStr = "Wallet";
                                    binding1.walletAmount.setText("₹ " + user.getWallet()+" (Pay using wallet)");

                                    binding1.wallet.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if(walletAmountStatus==0){
                                                binding1.onlinePayImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
                                                binding1.walletImage.setImageDrawable(getResources().getDrawable(R.drawable.check));
                                                binding1.cashDeliveryImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
                                                walletStr = "Wallet";
                                                cashStr = "";
                                                onlineStr = "";
                                                walletSelect = 1;
                                                binding1.txtMsg.setVisibility(View.GONE);
                                                binding1.walletAmount.setText("₹ "+user.getWallet()+" (Pay using wallet)");

                                            }else {
                                                if (walletSelect == 0) {
                                                    binding1.walletImage.setImageDrawable(getResources().getDrawable(R.drawable.check));
                                                    walletStr = "Wallet";
                                                    walletSelect = 1;
                                                    binding1.walletAmount.setText("₹ " + user.getWallet()+" will be adjusted from wallet against the order of ₹ "+binding.totalAmount.getText().toString().replace("₹", ""));

                                                    binding1.txtMsg.setVisibility(View.VISIBLE);

                                                } else {
                                                    binding1.walletImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
                                                    walletStr = "";
                                                    walletSelect = 0;
                                                    binding1.txtMsg.setVisibility(View.GONE);
                                                    binding1.walletAmount.setText("Wallet Balance ₹ "+user.getWallet()+" (Partial Payment with wallet)");

                                                }

                                            }
                                        }
                                    });
                                }
                            }else{
                                binding1.wallet.setVisibility(View.GONE);
                                binding1.txtMsg.setVisibility(View.GONE);
                                binding1.onlinePayImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
                                binding1.walletImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
                                binding1.cashDeliveryImage.setImageDrawable(getResources().getDrawable(R.drawable.check));
                                cashStr = "COD";
                                walletSelect=0;
                                codSelect=1;
                                onlineSelect=0;
                                binding1.txtMsg.setVisibility(View.GONE);
                                binding1.walletAmount.setText("₹ " + user.getWallet()+" will be adjusted from wallet against the order of ₹ "+binding.totalAmount.getText().toString().replace("₹", ""));

                            }

                        } else {
                            Toast.makeText(CheckOutActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

                Toast.makeText(CheckOutActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCartOrderItem() {
//        ShowProgress.getShowProgress(this).show();
        text="";
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call = myApi.getCartItem(user.getId());
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
//                            ShowProgress.getShowProgress(CheckOutActivity.this).hide();
                            JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                            JSONObject jsonObject1 = jsonObject.getJSONObject("MerchantResults");
                             merchant = new Gson().fromJson(jsonObject1.toString(), Merchant.class);
                            packingCharges=Double.parseDouble(merchant.getPacking_charge());
//                            binding.packingCharge.setText("\u20b9 "+merchant.getPacking_charge());
                            packingChargeAmount=merchant.getPacking_charge();
                            binding.taxesAndCharges.setText("₹" + (Double.parseDouble(otherChargeAmount)+Double.parseDouble(packingChargeAmount)+Double.parseDouble(deliveryFeeAmount)));

                               setShopDetails(merchant);



                            if (AppConstraints.isFromDelLoc) {
                                // checkPreAddress();
                                AppConstraints.isFromDelLoc=true;
                                checkCity(address);
                            }
                            else {
                                AppConstraints.isFromDelLoc=true;
                                checkCity(address);
                            }

                            int sta=0;
                            if (jsonArray1.length() > 0) {
                                orderItemList = new ArrayList<>();
                                double aoncar=0;
                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    OrderItem orderItem = new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), OrderItem.class);
                                    aoncar=aoncar+orderItem.getProduct().getItemPrice();
                                    orderItemList.add(orderItem);

                                    if(sta==0){
                                        if(orderItem.getProduct().getIsStatus().equalsIgnoreCase("false")){
                                            sta=1;
                                            text="false";
                                        }
                                    }
                                }
                                /*binding.orderItemList.setLayoutManager(new LinearLayoutManager(CheckOutActivity.this,LinearLayoutManager.VERTICAL,false));
                                binding.orderItemList.setAdapter(new OrderItemListAdapter(orderItemList));*/
                                binding.subTotal.setText("₹" + aoncar+ "");
                                CheckOutActivity.subTotal =aoncar;

                                if(couponCode!=null){
                                 calculateCouponDiscount(couponCode);
                                }else {
                                    calculateTip(subTotal, 0);
                                }

                                CartOrderItemAdapter adapter = new CartOrderItemAdapter(orderItemList,getSupportFragmentManager());
                                binding.orderList.setLayoutManager(new LinearLayoutManager(CheckOutActivity.this, LinearLayoutManager.VERTICAL, false));

                                adapter.findMyPosition(new CartOrderItemAdapter.GetPosition() {
                                    @Override
                                    public void findPos(int position, CartOrderItemAdapter.MyHolder holder, OrderItemDesignBinding binding, String status) {
                                        if (status.equals("delete")) {
                                            updateQuantity(binding.quantityTextView,holder, position, orderItemList.get(position), 0, adapter);
                                            //  emptyLayout.setVisibility(View.VISIBLE);
                                            orderItemList.remove(position);
                                            //loadCartOrderItem();
                                        } else {

                                            binding.plusButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    int qty = Integer.parseInt(binding.quantityTextView.getText().toString());
                                                    if (qty < 1000) {
                                                        //holder.plusbutton.setVisibility(View.INVISIBLE );
                                                        //updateQuantity(holder,position,orderItemList.get(position),qty);
                                                        qty++;

                                                        if(orderItemList.get(position).getAddOnProductPrize().equalsIgnoreCase("[]")||orderItemList.get(position).getAddOnProductPrize().equalsIgnoreCase("")) {

                                                            updateQuantity(binding.quantityTextView,holder, position, orderItemList.get(position), qty, adapter);
                                                        }else{
                                                            Product product=orderItemList.get(position).getProduct();
                                                            product.setMerchant_id(orderItemList.get(position).getMerchantId());
                                                            product.setId(orderItemList.get(position).getProductId());
                                                            IWillChooseFragment bottomSheet = new IWillChooseFragment("cust",orderItemList.get(position).getProduct(), qty+"", binding.quantityTextView,null,null,0,orderItemList.get(position).getId());
//                        bottom                       Sheet.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                            bottomSheet.show(getSupportFragmentManager(), "");
                                                        }
                                                        //loadCartOrderItem();
                                                    }

                                                }
                                            });
                                            binding.minusButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    int qty = Integer.parseInt(binding.quantityTextView.getText().toString());
                                                    if (qty > 1) {
                                                        //holder.minusbutton.setVisibility( View.INVISIBLE );
                                                        qty--;
                                                        updateQuantity(binding.quantityTextView,holder, position, orderItemList.get(position), qty, adapter);


                                                    } else {
                                                    /*if(qty==0) {
                                                        updateQuantity(holder, position, orderItemList.get(position), 0, adapter);
                                                        //  emptyLayout.setVisibility(View.VISIBLE);
                                                        orderItemList.remove(position);

                                                    }*/
                                                        updateQuantity(binding.quantityTextView,holder, position, orderItemList.get(position), 0, adapter);
                                                        //  emptyLayout.setVisibility(View.VISIBLE);
                                                        orderItemList.remove(position);
                                                        //loadCartOrderItem();
                                                        //cartAdapter.notifyItemRemoved(position);
                                                        //cartAdapter.notifyItemRangeChanged(position, orderItemList.size());

                                                        //binding.layout.setVisibility(View.GONE);
                                                        //binding.cartEmptyLayout.setVisibility(View.VISIBLE);*//*
                                                    }

                                                }
                                            });
                                        }

                                    }
                                });


                                binding.orderList.setAdapter(adapter);

                                binding.nestedLayout.setVisibility(View.VISIBLE);
                                binding.animation.setVisibility(View.GONE);
                                //binding.orderItemList.setAdapter(cartAdapter);

                            } else {
                                binding.nestedLayout.setVisibility(View.GONE);
                                binding.animation.setVisibility(View.VISIBLE);
                                CheckOutActivity.couponCode=null;
//                                ShowProgress.getShowProgress(CheckOutActivity.this).hide();
                                callEmptyLayout();
                                //emptyLayout.setVisibility(View.VISIBLE);
                                //      binding.checkOut.setVisibility(View.GONE);

                            }
                        } else {
                            //emptyLayout.setVisibility(View.VISIBLE);
//                            ShowProgress.getShowProgress(CheckOutActivity.this).hide();
                            callEmptyLayout();
                            binding.nestedLayout.setVisibility(View.GONE);
                            binding.animation.setVisibility(View.VISIBLE);
                            CheckOutActivity.couponCode=null;
                            //binding.checkOut.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        binding.nestedLayout.setVisibility(View.GONE);
                        binding.animation.setVisibility(View.VISIBLE);
                        CheckOutActivity.couponCode=null;
                        e.printStackTrace();
                    }

                }
            }


            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                binding.nestedLayout.setVisibility(View.GONE);
                binding.animation.setVisibility(View.VISIBLE);
                CheckOutActivity.couponCode=null;
//                ShowProgress.getShowProgress(CheckOutActivity.this).hide();
            }
        });


    }

    private void callEmptyLayout() {
        if (orderItemList.size() == 0) {
            binding.bottomSheetContainer.setVisibility(View.GONE);
            binding.cartEmptyLayout.setVisibility(View.VISIBLE);
            binding.browseRestaurants.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(CheckOutActivity.this, DashboardActivity.class));
                    finish();
                }
            });
        }

    }

    private void setShopDetails(Merchant merchant) {

        if(!merchant.getEstimatedDelivery().equalsIgnoreCase("")) {
            int estMinut = Integer.parseInt(merchant.getEstimatedDelivery()) % 60;
            int estTIme = Integer.parseInt(merchant.getEstimatedDelivery()) / 60;
            if (estTIme > 0) {
                if (estMinut > 0) {
                    String estTImeForValue = estTIme + " Hr " + estMinut + " Mins";
                    binding.type.setText("ETA : " + estTImeForValue);
                } else {
                    String estTImeForValue = estTIme + " Hour";
                    binding.type.setText("ETA : " + estTImeForValue);
                }
            } else {
                String estTImeForValue = estMinut + " Mins";
                binding.type.setText("ETA : " + estTImeForValue);
            }

        }

        binding.mechantName.setText(merchant.getName());
        binding.mechantAddress.setText(merchant.getAddress());
//        Picasso.get().load(AppConstraints.BASE_URL + AppConstraints.MERCHANT + merchant.getIcon()).placeholder(R.drawable.placeholder).placeholder(R.drawable.placeholder).into(binding.shopimage);
        Glide.with(getApplicationContext())
                .load(AppConstraints.BASE_URL + AppConstraints.MERCHANT + merchant.getIcon())
                .placeholder(R.drawable.placeholder).into(binding.shopimage);
    }

    private void updateQuantity(TextView text,CartOrderItemAdapter.MyHolder holder, int position, OrderItem item, int qty, CartOrderItemAdapter cartAdapter) {
        ShowProgress.getShowProgress(CheckOutActivity.this).show();
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Log.e("sde", qty + "" + item.getAddOnProductId() + "" + item.getAddOnProductPrize());
        Call<JsonArray> call = myApi.updateQuantity(user.getId(), item.getMerchantId(), item.getProductId(), String.valueOf(qty), item.getAddOnProductId().toString(), item.getAddOnProductPrize().toString(), "");
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
                            ShowProgress.getShowProgress(CheckOutActivity.this).hide();
                            loadCartCount();
                            loadCartOrderItem();
                            order=null;
                            JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                            Toast.makeText(CheckOutActivity.this, msg, Toast.LENGTH_SHORT).show();
                            text.setText(qty + "");
                            cartAdapter.notifyItemChanged(position);

                            if (jsonArray1.length() > 0) {

                            } else {

                            }
                            if (qty == 0) {


                            }

                        } else {
                            ShowProgress.getShowProgress(CheckOutActivity.this).hide();
                            Toast.makeText(CheckOutActivity.this, msg, Toast.LENGTH_SHORT).show();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                ShowProgress.getShowProgress(CheckOutActivity.this).hide();
                Toast.makeText(CheckOutActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    double newAmount;

    private void calculateCouponDiscount(Coupon coupon) {
        double minPurchase = Double.parseDouble(coupon.getMinimumPurchase());
        if (coupon.getType().equals("amount")) {
            if (subTotal > minPurchase) {
                //coupon applied
                double uptoUse = Double.parseDouble(coupon.getMaxAmountuse());
                double discount = Double.parseDouble(coupon.getDiscount());
                binding.couponDiscount.setText("- ₹" + String.valueOf(discount));
                binding.txtOfferamount.setText(Html.fromHtml("<span>₹" + String.valueOf(discount)+"</span>"));
                couponDiscount = String.valueOf(discount);
                newAmount = subTotal - discount;
                Log.e("discount", newAmount + "");
                calculateTip(newAmount, 1);

                if(!isFinishing()) {
                    if(coupoShowStatus==0){
                        coupoShowStatus=1;
                        ShowMesgDialoge(couponCode, couponDiscount, CheckOutActivity.this);
                    }

                }
            } else {
                Toast.makeText(CheckOutActivity.this, "Coupon not Application for your purchase", Toast.LENGTH_SHORT).show();

                //not applied
            }

        } else {

            if (subTotal > minPurchase) {

                //coupon applied
                double uptoUse = Double.parseDouble(coupon.getMaxAmountuse());
                double discount = Double.parseDouble(coupon.getDiscount());
                double discountInAmount = (subTotal * discount) / 100;
                if (discountInAmount > uptoUse) {
                    binding.couponDiscount.setText("- ₹" + String.valueOf(uptoUse));
                    binding.txtOfferamount.setText(Html.fromHtml("<span>₹" + String.valueOf(uptoUse)+"</span>"));
                    couponDiscount = String.valueOf(uptoUse);
                    newAmount = subTotal - uptoUse;
                    calculateTip(newAmount, 1);

                } else {
                    binding.couponDiscount.setText("- ₹" + String.valueOf(discountInAmount));
                    binding.txtOfferamount.setText(Html.fromHtml("<span>₹" + String.valueOf(discountInAmount)+"</span>"));
                    couponDiscount = String.valueOf(discountInAmount);
                    newAmount = subTotal - discountInAmount;
                    calculateTip(newAmount, 1);
                }

                if(!isFinishing()) {
                    if(coupoShowStatus==0){
                        coupoShowStatus=1;
                        ShowMesgDialoge(couponCode, couponDiscount, CheckOutActivity.this);
                    }

                }
            } else {
                //not applied
                Toast.makeText(CheckOutActivity.this, "Coupon not Application for your purchase", Toast.LENGTH_SHORT).show();

            }
        }

    }


    private void calculateTip(double newAmount, int i) {

        final double[] amount = {0};
        if (i == 1) {
            amount[0] = newAmount +Double.parseDouble(binding.taxesAndCharges.getText().toString().replace("₹",""));
            Log.e("oddd", "e");
        } else if (i == 0) {
            amount[0] = newAmount + Double.parseDouble(binding.taxesAndCharges.getText().toString().replace("₹",""));


        }

        binding.totalAmount.setText("₹" + amount[0]);

        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

//                if (s.length() > 0) {
//                    binding.courierTip.setText("₹" + s.toString());
//                    double tAmount = Double.parseDouble(s.toString());
//                    if (i == 1) {
//                        amount[0] = newAmount + Double.parseDouble(binding.taxesAndCharges.getText().toString().replace("₹",""));
//                    } else {
//                        amount[0] = CheckOutActivity.subTotal + Double.parseDouble(binding.taxesAndCharges.getText().toString().replace("₹",""));
//                    }
//
////                    if(couponCode!=null){
////                        amount=(amount+packingCharges-Double.parseDouble(couponDiscount));
////                    }else{
////                        amount=(amount+packingCharges);
////                    }
//                    binding.totalAmount.setText("₹" + amount[0]);
//                    binding.tipLay.setVisibility(View.VISIBLE);
//                } else {
//                    if (i == 1) {
//                        amount[0] = newAmount + Double.parseDouble(binding.taxesAndCharges.getText().toString().replace("₹",""));
//                    } else {
//                        amount[0] = CheckOutActivity.subTotal + Double.parseDouble(binding.taxesAndCharges.getText().toString().replace("₹",""));
//                    }
////                    if(couponCode!=null){
////                        amount=(amount+packingCharges-Double.parseDouble(couponDiscount));
////                    }else{
////                        amount=(amount+packingCharges);
////                    }
//                    binding.totalAmount.setText("₹" + amount[0]);
//                    binding.courierTip.setText("₹0");
//                    binding.tipLay.setVisibility(View.GONE);
//
//                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    binding.courierTip.setText("₹" + s.toString());
                    double tAmount = Double.parseDouble(s.toString());
                    if (i == 1) {
                        amount[0] = newAmount + Double.parseDouble(binding.taxesAndCharges.getText().toString().replace("₹",""));
                    } else {
                        amount[0] = CheckOutActivity.subTotal + Double.parseDouble(binding.taxesAndCharges.getText().toString().replace("₹",""));
                    }
//                    if(couponCode!=null){
//                        amount=(amount+packingCharges-Double.parseDouble(couponDiscount));
//                    }else{
//                        amount=(amount+packingCharges);
//                    }

                    binding.totalAmount.setText("₹" + amount[0]);
                    binding.tipLay.setVisibility(View.VISIBLE);
                } else {

                    if (i == 1) {
                        amount[0] = newAmount + Double.parseDouble(binding.taxesAndCharges.getText().toString().replace("₹",""));
                    } else {
                        amount[0] = CheckOutActivity.subTotal + Double.parseDouble(binding.taxesAndCharges.getText().toString().replace("₹",""));
                    }
//                    if(couponCode!=null){
//                        amount=(amount+packingCharges-Double.parseDouble(couponDiscount));
//                    }else{
//                        amount=(amount+packingCharges);
//                    }
                    binding.totalAmount.setText("₹" + amount[0]);
                    binding.courierTip.setText("₹0");
                    binding.tipLay.setVisibility(View.GONE);

                }
            }


            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    binding.courierTip.setText("₹" + s.toString());
                    double tAmount = Double.parseDouble(s.toString());
                    if (i == 1) {
                        amount[0] = newAmount + tAmount+Double.parseDouble(binding.taxesAndCharges.getText().toString().replace("₹",""));
                    } else {
                        amount[0] = CheckOutActivity.subTotal +tAmount+ Double.parseDouble(binding.taxesAndCharges.getText().toString().replace("₹",""));
                    }
//                    if(couponCode!=null){
//                        amount=(amount+packingCharges-Double.parseDouble(couponDiscount));
//                    }else{
//                        amount=(amount+packingCharges);
//                    }

                    binding.totalAmount.setText("₹" + amount[0]);
                    binding.tipLay.setVisibility(View.VISIBLE);
                } else {

                    if (i == 1) {
                        amount[0] = newAmount + Double.parseDouble(binding.taxesAndCharges.getText().toString().replace("₹",""));
                    } else {
                        amount[0] = CheckOutActivity.subTotal + Double.parseDouble(binding.taxesAndCharges.getText().toString().replace("₹",""));
                    }

                    binding.totalAmount.setText("₹" + amount[0]);
                    binding.courierTip.setText("₹0");
                    binding.tipLay.setVisibility(View.GONE);

                }

            }
        };
        binding.tipAmount.getEditText().addTextChangedListener(tw);
    }

    private void loadShippingCharge(UserAddress address) {
        Log.e("addressdvjsdfgjksd", "shippin");
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);

        Log.e("address id", address.getId() + "" + "sdsd");
        Call<JsonArray> call = myApi.calculateShippingCharges(user.getId(), address.getId(), merchant.getId());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    Log.e("sdsd", response.body() + "");
                    try {
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String res = jsonObject.getString("res");
                        String msg = jsonObject.getString("message");
                        if (res.equals("success")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            ShippingCharges charges = new Gson().fromJson(jsonObject1.toString(), ShippingCharges.class);
                            deliveryFeeAmount=charges.getShippingcharges();
                            binding.taxesAndCharges.setText("₹" + (Double.parseDouble(otherChargeAmount)+Double.parseDouble(packingChargeAmount)+Double.parseDouble(deliveryFeeAmount)));

                            Log.e("shipin charge", charges.getShippingcharges());
                            CheckOutActivity.shipingAmount = Double.parseDouble(charges.getShippingcharges());
                            //binding.distance.setText(charges.getKilometer() + "km");
                            //Log.e("distance",charges.getKilometer()+"sdsd");

//                            amount = subTotal ;
                            //binding.taxesAndCharges.setText("₹" + shipingAmount);
                            binding.totalAmount.setText("₹" + (subTotal));
                            if(couponCode!=null) {
                                calculateCouponDiscount(couponCode);
                            }else{
                                calculateTip(subTotal,1);
                            }
//                            calculateTip(amount,0);


                        } else {
                             Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }


            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(CheckOutActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadCartCount() {
        //ShowProgress.getShowProgress(CheckOutActivity.this).show();
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
                            //                ShowProgress.getShowProgress(CheckOutActivity.this).hide();

                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            CartItem cartItem = new Gson().fromJson(jsonObject1.toString(), CartItem.class);
//                            binding.subTotal.setText("₹" + cartItem.getTotalprice() + "");
                            double tax = Double.parseDouble(cartItem.getTaxesandPackingCharge());
                            //double t = (cartItem.getTotalprice() * tax) / 100;
                            double roundOff = Math.round(tax * 100) / 100;
                            CheckOutActivity.otherCharges = tax;
//                            CheckOutActivity.subTotal = Double.parseDouble(cartItem.getTotalprice());
                            Log.e("sdd", roundOff + "");
                            otherChargeAmount=tax+"";
//                            amount=amount+otherCharges;
//                            calculateTip(amount,0);
//                            calculateTip(Double.parseDouble(cartItem.getTotalprice()), 0);

                            loadCartOrderItem();
                            if(merchant!=null) {
                                if (merchant.getTaxesCharge() != null) {
                                    return;
                                }
                            }
                        } else {
                            //                  ShowProgress.getShowProgress(CheckOutActivity.this).hide();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                //        ShowProgress.getShowProgress(CheckOutActivity.this).hide();
                Toast.makeText(CheckOutActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void checkPreAddress() {
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call = myApi.getAllAddress(user.getId(), "");
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
                            JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                            JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
                            UserAddress address = new Gson().fromJson(jsonObject1.toString(), UserAddress.class);
                            binding.address.setText(address.getAddress());
                            CheckOutActivity.address = address;
                            if (address != null) {
                                checkCity(address);
                                //loadShippingCharge(ddress);
                            }
                        } else {

                            binding.editDelivery.setText("Add Address");
                            binding.address.setText("No Address");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

                Toast.makeText(CheckOutActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkCity(UserAddress address) {

        try {
            String js = singleTask.getValue("user");
            User user = new Gson().fromJson(js, User.class);
            MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
            Call<JsonArray> call = myApi.checkCity(user.getId(), address.getLatitude(), address.getLongitude());
            call.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if (response.isSuccessful()) {
                        Log.e("check city response", response.body().toString());
                        try {
                            JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String res = jsonObject.getString("res");
                            if (res.equals("success")) {
                                loadShippingCharge(address);
                                Log.e("ds", "suces");
                            } else {
                                binding.taxesAndCharges.setText("₹" + (Double.parseDouble(otherChargeAmount)+Double.parseDouble(packingChargeAmount)+Double.parseDouble(deliveryFeeAmount)));
//                                calculateCouponDiscount(couponCode);
                                startActivity(new Intent(CheckOutActivity.this, NoServiceAvaliable.class));
//                                Toast.makeText(CheckOutActivity.this, "Please Choose Another Address", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {

                    Toast.makeText(CheckOutActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        catch(Exception e)
        {
//            Toast.makeText(CheckOutActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//
//            Intent in=new Intent(CheckOutActivity.this,DeliveryLocationActivity.class);
//            startActivity(in);
        }


    }

    private void setData() {

        if (address != null) {
            binding.address.setText(address.getAddress());
            binding.paymentMethod.setText("Tap to Pay");
            Log.e("sadd", "here");
//            loadShippingCharge(address);
        }else{
            binding.paymentMethod.setText("Select Address");
        }

//        if (option != null) {
//            binding.type.setText(option);
//        }
//        binding.type.setText("ETA :" + merchant.getEstimatedDelivery() + " mins");
    }

    private void paymentMethod(View v) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.myBottomSheetDialogTheme);
        View view1 = LayoutInflater.from(this).inflate(R.layout.select_payement_bottomsheet, (CoordinatorLayout) findViewById(R.id.bottom_sheet_container), false);
        bottomSheetDialog.setContentView(view1);
        SelectPayementBottomsheetBinding binding1 = SelectPayementBottomsheetBinding.bind(view1);
        fetchWalletAmount(binding1);

        binding1.cashOnDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(walletAmountStatus==0){
                    binding1.walletImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
                    binding1.onlinePayImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
                    binding1.cashDeliveryImage.setImageDrawable(getResources().getDrawable(R.drawable.check));
                    cashStr = "COD";
                    walletStr = "";
                    onlineStr = "";
                    codSelect = 1;
                    onlineSelect = 0;
                    binding1.txtMsg.setVisibility(View.GONE);
                    binding1.walletAmount.setText("₹ "+walletAmount+" (Pay using wallet)");

                }else {
                    if (codSelect == 0) {
                        binding1.onlinePayImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
                        binding1.cashDeliveryImage.setImageDrawable(getResources().getDrawable(R.drawable.check));
                        cashStr = "COD";
                        onlineStr = "";
                        codSelect = 1;
                        onlineSelect = 0;
                    } else {
                        binding1.onlinePayImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
                        binding1.cashDeliveryImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
                        cashStr = "";
                        onlineStr = "";
                        codSelect = 0;
                        onlineSelect = 0;
                    }
                }
            }
        });
        binding1.onlinePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(walletAmountStatus==0){
                    binding1.onlinePayImage.setImageDrawable(getResources().getDrawable(R.drawable.check));
                    binding1.cashDeliveryImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
                    binding1.walletImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
                    onlineStr = "ONLINE";
                    walletStr = "";
                    cashStr = "";
                    onlineSelect = 1;
                    codSelect = 0;
                    binding1.txtMsg.setVisibility(View.GONE);
                    binding1.walletAmount.setText("₹ "+walletAmount+" (Pay using wallet)");

                }else {
                    if (onlineSelect == 0) {
                        binding1.onlinePayImage.setImageDrawable(getResources().getDrawable(R.drawable.check));
                        binding1.cashDeliveryImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
                        onlineStr = "ONLINE";
                        cashStr = "";
                        onlineSelect = 1;
                        codSelect = 0;

                    } else {
                        binding1.onlinePayImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
                        binding1.cashDeliveryImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
                        onlineStr = "";
                        cashStr = "";
                        onlineSelect = 0;
                        codSelect = 0;
                    }
                }
            }
        });
        binding1.selectMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valid()) {

                    if(CheckOutActivity.order!=null){
                        if(walletAmountStatus==0) {
                            paymentMethodType=walletStr+onlineStr+cashStr;
                            if (paymentMethodType == "ONLINE") {
                                // binding.pay.setVisibility(View.VISIBLE);
                                //binding.bt.setText(msg);
                                //initiatePayment();
//                            UPIPayment("1");
                                //openBottomSheet();
                                paymentOption(v, String.valueOf(binding.totalAmount.getText().toString().replace("₹", "")));
                            } else {
                                makeOrder(paymentMethodType);
                            }
                        }else{
                            if(walletStr.length()>0){
                                if(cashStr.length()>0){
                                    paymentMethodType="Wallet+COD";
                                    makeOrder(paymentMethodType);
                                }else  if(onlineStr.length()>0){
                                    paymentMethodType="Wallet+ONLINE";
                                    paymentOption(v, (Double.parseDouble(binding.totalAmount.getText().toString().replace("₹", ""))-Double.parseDouble(walletAmount))+"");

                                }else{
                                    if(Double.parseDouble(binding.totalAmount.getText().toString().replace("₹", ""))<=Double.parseDouble(walletAmount)) {
                                        paymentMethodType = "Wallet";
                                        makeOrder(paymentMethodType);
                                    }else{
                                        Toast.makeText(getApplicationContext(),"Choose any method to pay remaining amount.",Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                }
                            }else{
                                if(cashStr.length()>0){
                                    paymentMethodType="COD";
                                    makeOrder(paymentMethodType);
                                }else{
                                    paymentMethodType="ONLINE";
                                    paymentOption(v, String.valueOf(binding.totalAmount.getText().toString().replace("₹", "")));

                                }
                            }
                        }

                    }else {

                        if(walletAmountStatus==0) {
                            if (cashStr.length() > 0) {
                                paymentMethodType = "COD";
                            }else if (walletStr.length() > 0) {
                                paymentMethodType = "Wallet";
                            } else if (onlineStr.length() > 0)  {
                                paymentMethodType = "ONLINE";

                            }else{
                                Toast.makeText(getApplicationContext(), "Please Select Payment Method !", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }else {
                            if (walletStr.length() > 0) {
                                if (cashStr.length() > 0) {
                                    paymentMethodType = "Wallet+COD";
                                } else if (onlineStr.length() > 0) {
                                    paymentMethodType = "Wallet+ONLINE";

                                } else {
                                    if (Double.parseDouble(binding.totalAmount.getText().toString().replace("₹", "")) <= Double.parseDouble(walletAmount)) {

                                        paymentMethodType = "Wallet";
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Choose any method to pay remaining amount.", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                }
                            } else {
                                if (cashStr.length() > 0) {
                                    paymentMethodType = "COD";
                                } else {
                                    paymentMethodType = "ONLINE";

                                }
                            }
                        }
                        makeOrder(paymentMethodType);

                    }

                    bottomSheetDialog.dismiss();
                }


            }
        });

        binding1.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.show();

    }
    private void paymentOption(View v,String amount) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.myBottomSheetDialogTheme);
        View view1 = LayoutInflater.from(this).inflate(R.layout.payment_option_layout, (CoordinatorLayout) findViewById(R.id.bottom_sheet_container), false);
        bottomSheetDialog.setContentView(view1);
        PaymentOptionLayoutBinding binding = PaymentOptionLayoutBinding.bind(view1);
//        fetchWalletAmount(binding);

        if(paymentMethodType.equalsIgnoreCase("Wallet+ONLINE")){
            binding.layoutCashfree.setVisibility(View.GONE);
        }

        binding.layoutPhone.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            boolean isInstalled = false;
                            for(int i=0;i<pkgAppsList.size();i++){
                                ResolveInfo resolveInfo = (ResolveInfo) pkgAppsList.get(i);
                                if(resolveInfo.activityInfo.packageName.equalsIgnoreCase("com.phonepe.app")){
                                    isInstalled=true;
                                    break;
                                }
                            }
                            if(isInstalled) {
                                UPIPayment(amount, PaymentApp.PHONE_PE);
                            }else{
                                Toast.makeText(getApplicationContext(), "Phonepe not found in your device... Please Install and continue", Toast.LENGTH_SHORT).show();

                            }
                        }catch (AppNotFoundException exception) {
                            Toast.makeText(getApplicationContext(), "Phonepe not found in your device... Please Install and continue", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
        );
        binding.layoutGpay.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            boolean isInstalled = false;
                            for(int i=0;i<pkgAppsList.size();i++){
                                ResolveInfo resolveInfo = (ResolveInfo) pkgAppsList.get(i);
                                if(resolveInfo.activityInfo.packageName.equalsIgnoreCase("com.google.android.apps.nbu.paisa.user")){
                                    isInstalled=true;
                                    break;
                                }
                            }
                            if(isInstalled) {
                                UPIPayment(amount, PaymentApp.GOOGLE_PAY);
                            }else{
                                Toast.makeText(getApplicationContext(), "Googlepay not found in your device... Please Install and continue", Toast.LENGTH_SHORT).show();

                            }
                        }catch (AppNotFoundException exception) {
                            Toast.makeText(getApplicationContext(), "Googlepay not found in your device... Please Install and continue", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
        );
        binding.layoutPaytm.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            boolean isInstalled = false;
                            for(int i=0;i<pkgAppsList.size();i++){
                                ResolveInfo resolveInfo = (ResolveInfo) pkgAppsList.get(i);
                                if(resolveInfo.activityInfo.packageName.equalsIgnoreCase("net.one97.paytm")){
                                    isInstalled=true;
                                    break;
                                }
                            }
                            if(isInstalled) {
                                UPIPayment(amount, PaymentApp.PAYTM);
                            }else{
                                Toast.makeText(getApplicationContext(), "Paytm not found in your device... Please Install and continue", Toast.LENGTH_SHORT).show();

                            }
                        }catch (AppNotFoundException exception) {
                            Toast.makeText(getApplicationContext(), "Paytm not found in your device... Please Install and continue", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
        );
        binding.layoutUPI.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            boolean isInstalled = false;
                            for(int i=0;i<pkgAppsList.size();i++){
                                ResolveInfo resolveInfo = (ResolveInfo) pkgAppsList.get(i);
                                if(resolveInfo.activityInfo.packageName.equalsIgnoreCase("in.org.npci.upiapp")){
                                    isInstalled=true;
                                    break;
                                }
                            }
                            if(isInstalled) {
                                UPIPayment(amount, PaymentApp.BHIM_UPI);
                            }else{
                                Toast.makeText(getApplicationContext(), "Paytm not found in your device... Please Install and continue", Toast.LENGTH_SHORT).show();

                            }
                        }catch (AppNotFoundException exception) {
                            Toast.makeText(getApplicationContext(), "Paytm not found in your device... Please Install and continue", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
        );
        binding.layoutCashfree.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        initiatePayment(amount);
                    }
                }
        );


        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.show();

    }
    private void makeOrder(String paymentMethodType) {
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        ShowProgress.getShowProgress(CheckOutActivity.this).show();
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Log.e("sds", paymentMethodType + "");
        String lat = SharedPrefManagerLocation.getInstance(getApplicationContext()).locationModel(Constraint.LATITUDE);
        String lon = SharedPrefManagerLocation.getInstance(getApplicationContext()).locationModel(Constraint.LONGITUDE);

        String c_code="";
        if(couponCode!=null){
            c_code=couponCode.getCouponCode();
        }
        Call<JsonArray> call = myApi.order(merchant.getId(), user.getId(), String.valueOf(subTotal), binding.totalAmount.getText().toString().replace("₹",""), paymentMethodType, address.getId(), c_code + "", couponDiscount, String.valueOf(shipingAmount), binding.message.getText().toString(), binding.tipAmount.getEditText().getText().toString(),lat,lon, String.valueOf(otherCharges));
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String res = jsonObject.getString("res");
                        String msg = jsonObject.getString("message");
                        ShowProgress.getShowProgress(CheckOutActivity.this).hide();

                        WaitingFragment.walletAmount=walletAmount;
                        if (res.equals("success")) {
                            //Toast.makeText(CheckOutActivity.this, msg, Toast.LENGTH_SHORT).show();
                            Log.e("sdsd", jsonArray.toString());
                            JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                            JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
                            Order order = new Gson().fromJson(jsonObject1.toString(), Order.class);
                            if(CheckOutActivity.order==null) {
                                CheckOutActivity.order = order;
                            }
                            Log.e("dfdf", msg);
                            if (paymentMethodType.equalsIgnoreCase("ONLINE")) {
                                // binding.pay.setVisibility(View.VISIBLE);
                                //binding.bt.setText(msg);
                                //initiatePayment();
                                paymentOption(null,String.valueOf(binding.totalAmount.getText().toString().replace("₹","")));
                                //openBottomSheet();
                            }else if (paymentMethodType.equalsIgnoreCase("Wallet+ONLINE")) {
                                // binding.pay.setVisibility(View.VISIBLE);
                                //binding.bt.setText(msg);
                                //initiatePayment();
                                paymentOption(null,String.valueOf(Double.parseDouble(binding.totalAmount.getText().toString().replace("₹",""))-Double.parseDouble(walletAmount)));
                                //openBottomSheet();
                            } else {
                                CheckOutActivity.order=null;
                                CheckOutActivity.couponCode=null;
                                WaitingFragment.orderId= order.getOrderId();
                                WaitingFragment.paymentMethod= order.getMethod();
                                WaitingFragment.amount= order.getAmount();
                               WaitingFragment waitingFragment=new WaitingFragment();
                               waitingFragment.show(getSupportFragmentManager(),"");

                                //Toast.makeText(CheckOutActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }

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

  /*  private void getRazorPayApiKey() {
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        ShowProgress.getShowProgress(CheckOutActivity.this).show();
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call = myApi.getRazorPayKey(user.getId());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {

                    try {
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        Log.e("sdsde", jsonArray.toString());
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String res = jsonObject.getString("res");
                        String msg = jsonObject.getString("message");
                        ShowProgress.getShowProgress(CheckOutActivity.this).hide();
                        if (res.equals("success")) {
                            Toast.makeText(CheckOutActivity.this, msg, Toast.LENGTH_SHORT).show();
                            String key = jsonObject.getString("PaymentKey");
                            startPayment(key);
                            Log.e("sdsd", key);
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
    }*/

    private boolean valid() {
        if (binding.editDelivery.getText().equals("Add Address")) {
            Toast.makeText(CheckOutActivity.this, "Please Choose Delivery Address", Toast.LENGTH_SHORT).show();
            return false;

        } else if (paymentMethodType.isEmpty()) {
            Toast.makeText(CheckOutActivity.this, "Please Choose a Payment method", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void loadItemOrder() {
        ShowProgress.getShowProgress(this).show();
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call = myApi.getCartItem(user.getId());
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
                            ShowProgress.getShowProgress(CheckOutActivity.this).hide();
                            JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                            if (jsonArray1.length() > 0) {
                                orderItemList = new ArrayList<>();
                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    OrderItem orderItem = new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), OrderItem.class);
                                    orderItemList.add(orderItem);
                                }
                                /*binding.orderItemList.setLayoutManager(new LinearLayoutManager(CheckOutActivity.this, LinearLayoutManager.VERTICAL, false));
                                binding.orderItemList.setAdapter(new OrderItemListAdapter(orderItemList));*/
                            } else {
                            }
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

    /*    private void startPayment(String key) {
            final Activity activity = this;
            String jsonObject = singleTask.getValue("user");
            User user = new Gson().fromJson(jsonObject, User.class);
                final Checkout co = new Checkout();
                co.setKeyID(key);
                Log.e("sdds",key);

                try {
                    JSONObject options = new JSONObject();
                    options.put("name", user.getName());
                    options.put("description", "Payment  For " + "Order Food at  Capsico");
                    options.put("image","https://capsico.digitalcoders.in/assets/uploads/logo.png");
                    options.put("currency", "INR");
                    options.put("theme.color", "#ff7f00");
                    options.put("order_id", order.getRazorpayOrderID());
                    options.put("amount", amount * 100.0);
                    Log.e("sdsds",amount+"");
                    JSONObject preFill = new JSONObject();
                    preFill.put("email", user.getEmail());
                    preFill.put("contact", user.getMobile());
                    options.put("prefill", preFill);
                    *//*       progressDialog.hide();*//*
                co.open(activity, options);

            } catch (Exception e) {
                Toast.makeText(CheckOutActivity.this,"Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                Log.e("Error in payment", e.getMessage());
                e.printStackTrace();
            }

    }*/

    private void initiatePayment(String amount) {
        Map<String, String> params = new HashMap<>();
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        params.put(PARAM_APP_ID, order.getApp_id());
        params.put(PARAM_ORDER_ID, order.getOrderId());
        params.put(PARAM_ORDER_AMOUNT, String.valueOf(amount));
        params.put(PARAM_ORDER_NOTE, "Food Order On Capsico");
        params.put(PARAM_CUSTOMER_NAME, user.getName());
        params.put(PARAM_CUSTOMER_PHONE, user.getMobile());
        if (user.getEmail().isEmpty()) {
            params.put(PARAM_CUSTOMER_EMAIL, "capcisodeveloper@gmail.com");
        } else {
            params.put(PARAM_CUSTOMER_EMAIL, user.getEmail());
        }

        params.put(PARAM_ORDER_CURRENCY, "INR");
        CFPaymentService cfPaymentService = CFPaymentService.getCFPaymentServiceInstance();
        cfPaymentService.setOrientation(0);
        //for production
       /* cfPaymentService.doPayment(OrderSummaryActivity.this, params,

                "", "PROD", "#F8A31A", "#FFFFFF", false);
*/
        cfPaymentService.doPayment(CheckOutActivity.this, params, order.getToken(), order.getEnv(), "#ff7f00", "#FFFFFF", false);
    }


    private void openBottomSheet() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.myBottomSheetDialogTheme);
        View view1 = LayoutInflater.from(this).inflate(R.layout.checkout_bottom_sheet_layout, (CoordinatorLayout) findViewById(R.id.bottom_sheet_container), false);
        CheckoutBottomSheetLayoutBinding binding1 = CheckoutBottomSheetLayoutBinding.bind(view1);
//        binding.Amount.setText("₹ " + amount);
        binding1.payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // paymentMethod(v);
                paymentOption(v,String.valueOf(binding.totalAmount.getText().toString().replace("₹","")));
                // Toast.makeText(CheckOutActivity.this, "sds", Toast.LENGTH_SHORT).show();
            }
        });
        bottomSheetDialog.setContentView(view1);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.show();
    }


    String paymentMode, orderId, txTime, referenceid, type, txMsg, signature, orderAmout, txStatus;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("m", "API Response : ");
        if (data != null) {

            Bundle bundle = data.getExtras();
            if (bundle != null)
                for (String key : bundle.keySet()) {
                    if (bundle.getString(key) != null) {
                        Log.e("sdsd", bundle.toString() + "");
                        Log.d("resp", key + " : " + bundle.getString(key));
                        paymentMode = bundle.getString("paymentMode");
                        orderId = bundle.getString("orderId");
                        txTime = bundle.getString("txTime");
                        referenceid = bundle.getString("referenceId");
                        type = bundle.getString("type");
                        txMsg = bundle.getString("txMsg");
                        signature = bundle.getString("signature");
                        orderAmout = bundle.getString("orderAmount");
                        txStatus = bundle.getString("txStatus");
                    }
                }
            if (txStatus.equals("SUCCESS")) {
                updatePaymentStatus(referenceid, txStatus, "bundle", orderId, txMsg);

            } else if (txStatus.equals("CANCELLED")) {
                Toast.makeText(CheckOutActivity.this, "Retry Again", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CheckOutActivity.this, "FAILED", Toast.LENGTH_SHORT).show();
                Log.e("ds", referenceid + "sdsd");
                updatePaymentStatus(referenceid, txStatus, "bundle", orderId, txMsg);
            }
        }
    }


    private void updatePaymentStatus(String referenceid, String success, String bundle, String orderId, String s) {
        ShowProgress.getShowProgress(this).show();
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);

        Log.e("sdsd", success);
        Call<JsonArray> call = myApi.updateStatus(order.getOrderId(), user.getId(), success, referenceid, bundle);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    Log.e("response", response.toString());
                    try {
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String res = jsonObject.getString("res");
                        String msg = jsonObject.getString("message");
                        if (res.equals("success")) {

                            JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                            Log.e("sdsdaw", jsonArray1.toString() + "");
//                            Toast.makeText(CheckOutActivity.this, msg, Toast.LENGTH_SHORT).show();
                            couponCode=null;
                            WaitingFragment.orderId= order.getOrderId();
                            WaitingFragment.paymentMethod= order.getMethod();
                            WaitingFragment.amount= order.getAmount();
                            WaitingFragment waitingFragment=new WaitingFragment();
                            waitingFragment.show(getSupportFragmentManager(),"");

                            CheckOutActivity.order=null;

                        } else {

                            Toast.makeText(CheckOutActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }

                }
//                Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                ShowProgress.getShowProgress(CheckOutActivity.this).hide();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                ShowProgress.getShowProgress(CheckOutActivity.this).hide();
                Toast.makeText(getApplicationContext(),t.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });


    }

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    @Override
    public void onBackPressed() {

        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            couponCode=null;
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Tap back button in order to exit", Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
    }


    @Override
    protected void onResume() {
        super.onResume();
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        if (user.getName().isEmpty()) {
            editProfileConfirmationDialog();
        }

    }

    @Override
    public void onRefresh() {
        loadCartOrderItem();
    }

    EasyUpiPayment easyUpiPayment;

    public void UPIPayment(String amount,PaymentApp paymentApp) throws AppNotFoundException{


        Random random=new Random();
        try {
        EasyUpiPayment.Builder  builder= new EasyUpiPayment.Builder(this)
                .with(paymentApp)
                .setPayeeVpa("Q915573461@ybl")
                .setPayeeName("Capsico")
                .setTransactionId(System.currentTimeMillis()+"")
                .setTransactionRefId(System.currentTimeMillis()+"")
                .setPayeeMerchantCode("")
                .setDescription("Capsico Online Ordering")
                .setAmount(amount);


            // Build instance
            easyUpiPayment = builder.build();

            // Register Listener for Events
            easyUpiPayment.setPaymentStatusListener(this);

            // Start payment / transaction
            easyUpiPayment.startPayment();
        } catch (AppNotFoundException exception) {
            if(paymentApp==PaymentApp.PAYTM) {
                Toast.makeText(getApplicationContext(), "Paytm not found in your device... Please Install and continue", Toast.LENGTH_SHORT).show();
            }else if(paymentApp==PaymentApp.PHONE_PE) {
                Toast.makeText(getApplicationContext(), "Phonepe not found in your device... Please Install and continue", Toast.LENGTH_SHORT).show();
            }else if(paymentApp==PaymentApp.GOOGLE_PAY) {
                Toast.makeText(getApplicationContext(), "Googlepay not found in your device... Please Install and continue", Toast.LENGTH_SHORT).show();
            }
        }

    }

    List pkgAppsList;
    void getUpiApp(){
       PackageManager packageManager = getPackageManager();
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_DEFAULT);
        mainIntent.addCategory(Intent.CATEGORY_BROWSABLE);
        mainIntent.setAction(Intent.ACTION_VIEW);
        Uri uri1 = new Uri.Builder().scheme("upi").authority("pay").build();
        mainIntent.setData(uri1);
        pkgAppsList =
                getPackageManager().queryIntentActivities(mainIntent, 0);


    }
    @Override
    public void onTransactionCancelled() {
        Toast.makeText(CheckOutActivity.this,"Cancelled by user",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTransactionCompleted(@NonNull TransactionDetails transactionDetails) {
        switch (transactionDetails.getTransactionStatus()) {
            case SUCCESS:
                onTransactionSuccess();
                break;
            case FAILURE:
                onTransactionFailed();
                break;
            case SUBMITTED:
                onTransactionSubmitted();
                break;
        }
    }


    private void onTransactionSuccess() {
        referenceid=System.currentTimeMillis()+"";
        orderId=System.currentTimeMillis()+"";
        updatePaymentStatus(referenceid, "SUCCESS", orderId, orderId, "Order");

    }

    private void onTransactionSubmitted() {
        // Payment Pending
        Toast.makeText(CheckOutActivity.this,"Pending | Submitted",Toast.LENGTH_LONG).show();

    }

    private void onTransactionFailed() {
        // Payment Failed
        Toast.makeText(CheckOutActivity.this,"Failed",Toast.LENGTH_LONG).show();

    }

    public void ShowMesgDialoge(Coupon coupinModel, String amounta, Context ctx) {
        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        View promptView = layoutInflater.inflate(R.layout.coupon_success_layout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
        alertDialogBuilder.setView(promptView);


        final AlertDialog alert2 = alertDialogBuilder.create();
        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ElasticButton ok=promptView.findViewById(R.id.ok);
        TextView code=promptView.findViewById(R.id.code);
        TextView amount=promptView.findViewById(R.id.amount);
        ImageView  animGif=promptView.findViewById(R.id.animGif);

        Glide.with(getApplicationContext())
                .load(R.raw.gift)
                .into(animGif);
        code.setText("'"+coupinModel.getCouponCode()+"' applied !");
        amount.setText("\u20b9 "+amounta+" Saved with this coupon");
        ok.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert2.dismiss();
                    }
                }
        );

        new CountDownTimer(4000, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                alert2.dismiss();

            }
        }.start();

        alert2.show();
    }



}
