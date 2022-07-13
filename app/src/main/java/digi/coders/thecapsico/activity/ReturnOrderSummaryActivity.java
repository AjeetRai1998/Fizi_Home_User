package digi.coders.thecapsico.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import digi.coders.thecapsico.BuildConfig;
import digi.coders.thecapsico.R;
import digi.coders.thecapsico.adapter.OrderItemsAdapter;
import digi.coders.thecapsico.adapter.ReturnAdapter;
import digi.coders.thecapsico.databinding.ActivityOrderSummaryBinding;
import digi.coders.thecapsico.databinding.ReturnOrderSummaryActivityBinding;
import digi.coders.thecapsico.databinding.ViewOrderBottomSheetLayoutBinding;
import digi.coders.thecapsico.helper.AppConstraints;
import digi.coders.thecapsico.helper.Constraint;
import digi.coders.thecapsico.helper.MYMvp;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.NotificationUtils;
import digi.coders.thecapsico.helper.ProgressDisplay;
import digi.coders.thecapsico.helper.ShowProgress;
import digi.coders.thecapsico.model.DeliveryBoy;
import digi.coders.thecapsico.model.Merchant;
import digi.coders.thecapsico.model.MyOrder;
import digi.coders.thecapsico.model.OrderStatus;
import digi.coders.thecapsico.model.Orderproduct;
import digi.coders.thecapsico.model.ReturnProductModel;
import digi.coders.thecapsico.model.Review;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.model.UserAddress;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReturnOrderSummaryActivity extends AppCompatActivity  {

    public static MyOrder myOrder;
    public static String orderId;
    public static String Product_IDs;
    ReturnOrderSummaryActivityBinding binding;
    private SingleTask singleTask;
    private int status;
    private List<OrderStatus> statusList;
    String delBoyNumber;
    public int transStatus=0;

    public static List<String> addProduct=new ArrayList<>();


    double deliveryTime=0;
    CountDownTimer countDownTimer;
    public static Activity OrderSummaryActivity;
    public static CountDownTimer timer;

    TextView restCharge,packingCharge,deliveryFee;
    Balloon balloon;
    String packingChargeAmount="0",otherChargeAmount="0",deliveryFeeAmount="0";

    public static long timeForDetalay=120000;
    ProgressDisplay progressDisplay;
    List<ReturnProductModel> models=new ArrayList<>();

    long time_to_return=5;

//    List<CheckboxModel> models=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ReturnOrderSummaryActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        singleTask = (SingleTask) getApplication();
        OrderSummaryActivity=this;
        Glide.with(this)
                .load(R.raw.loader)
                .into(binding.animation);


        progressDisplay=new ProgressDisplay(this);

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
                mMessageReceiver, new IntentFilter("GPSLocationUpdates"));


        status = getIntent().getIntExtra("sta", 0);

            binding.refersh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    binding.refersh.setRefreshing(false);
                           loadData();
                }
            });
        loadData();

        binding.taptoReorder.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reorder(myOrder.getOrderId(),myOrder.getUserId());
                    }
                }
        );
        balloon = new Balloon.Builder(ReturnOrderSummaryActivity.this)
                .setArrowSize(10)
                .setLayout(R.layout.other_charges_details)
                .setArrowOrientation(ArrowOrientation.TOP)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                .setArrowPosition(0.2f)
                .setWidth(BalloonSizeSpec.WRAP)
                .setCornerRadius(11f)
                .setAlpha(1.0f)
                .setBackgroundColor(ContextCompat.getColor(ReturnOrderSummaryActivity.this, R.color.pure_black))
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



//        }
    }
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String order_id = intent.getStringExtra("order_id");
            String id = intent.getStringExtra("id");
            String  amount = "90";//intent.getStringExtra("0amount");
            if(transStatus==0) {
                transStatus=1;
                loadData();
            }
//            loadStatus();


        }
    };


    private void loadData() {
        String js = singleTask.getValue("user");
        Log.e("orer",orderId+"1");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call = myApi.myReturnOrderDetails(user.getId(), orderId);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        Log.e("sdsd", jsonArray.toString());
                        String res = jsonObject.getString("res");
                        String message = jsonObject.getString("message");
                        if (res.equals("success")) {

                            JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                             myOrder = new Gson().fromJson(jsonArray1.getJSONObject(0).toString(), MyOrder.class);


                             Orderproduct[] orderproducts=myOrder.getOrderproduct();

                            setData(myOrder);

                            if(myOrder.getOrderStatus().equalsIgnoreCase("Waiting")||myOrder.getOrderStatus().equalsIgnoreCase("Placed")) {
                                binding.lineProgrss.setVisibility(View.VISIBLE);
                                timer= new CountDownTimer(timeForDetalay, 1000) {
                                    public void onTick(long millisUntilFinished) {
                                        timeForDetalay=millisUntilFinished;
                                        if(millisUntilFinished!=0) {
                                            binding.customBar.setProgressPercentage(((120000 - millisUntilFinished) / 1200), true);
                                        }else{
                                            binding.customBar.setProgressPercentage(100,true);
                                        }
                                        //                Toast.makeText(getApplicationContext(),((180000-millisUntilFinished)/1800)+"",Toast.LENGTH_LONG).show();
                                    }

                                    public void onFinish() {
                                        binding.customBar.setProgressPercentage(100, true);
                                        timeForDetalay=120000;
                                        timer.cancel();
                                        missed();

                                    }
                                };
                                timer.start();
                            }else{
                                if(timer!=null){
                                    timer.cancel();
                                    timeForDetalay=120000;
                                }
                                binding.lineProgrss.setVisibility(View.GONE);
                                binding.refundTxt.setVisibility(View.GONE);
                            }

                        } else {


                        }
                    } catch (JSONException e) {
                        Log.e("sdsd e", e.getMessage().toString());
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

                Toast.makeText(ReturnOrderSummaryActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static double packingCharges=0;
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setData(MyOrder myOrder) {

        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        Merchant merchant = myOrder.getMerchant()[0];
        packingCharges=Double.parseDouble(merchant.getPacking_charge());
        packingChargeAmount=merchant.getPacking_charge();
//        binding.packingCharge.setText("\u20b9 "+merchant.getPacking_charge());
        if(myOrder.getOrderproduct().length>0){
            Orderproduct order = myOrder.getOrderproduct()[0];
            try {
                DeliveryBoy deliveryBoy = myOrder.getDeliveryboy()[0];
                DecimalFormat decim = new DecimalFormat("#.#");

                double rating = 0;
                for (int k = 0; k < myOrder.getDeliveryboyrating().length; k++) {
                    Review review = myOrder.getDeliveryboyrating()[k];
                    rating = rating + Double.parseDouble(review.getRating());
                }
                binding.rating.setText("" + decim.format(rating / myOrder.getDeliveryboyrating().length));
                binding.deliveryBoyName.setText(deliveryBoy.getName() + " is your delivery valet today");
                if (deliveryBoy.getVacStatus().equalsIgnoreCase("vaccinated")) {
                    binding.deliveryBoyVacStatus.setText(deliveryBoy.getVacStatus());
                    binding.deliveryBoyVacStatus.setVisibility(View.VISIBLE);
                    binding.relVac.setBackgroundResource(R.drawable.green_circle);
                } else {
                    binding.deliveryBoyVacStatus.setVisibility(View.GONE);
                    binding.relVac.setBackgroundResource(R.color.transparent);
                }
                binding.deliveryBoydesc.setText(Html.fromHtml(deliveryBoy.getDescription()));
                binding.deliveryBoyName1.setText(deliveryBoy.getName() + " has delivered your order.");
                binding.deliveryBoyName2.setText("Please Rate " + deliveryBoy.getName() + "'s service. Your feedback will help him improve.");
                Picasso.get().load(AppConstraints.BASE_URL + AppConstraints.DELIVERY_BOY + deliveryBoy.getIcon()).placeholder(R.drawable.placeholder).into(binding.deliveryBoyImg);
                Picasso.get().load(AppConstraints.BASE_URL + AppConstraints.DELIVERY_BOY + deliveryBoy.getIcon()).placeholder(R.drawable.placeholder).into(binding.deliveryBoyImg1);
                delBoyNumber = deliveryBoy.getMobile();

            } catch (Exception e) {
//           Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
            binding.noOfItem.setText(order.getQty() + "|");
        }
//        orderId=order.getOrderId();

        UserAddress address = myOrder.getAddress()[0];
//        binding.orderId.setText("Order:" + myOrder.getOrderId());
        binding.rateDeliveryBoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(myOrder.getDeliveryboyratingstatus().equalsIgnoreCase("true")){
                    Toast.makeText(getApplicationContext(), "You have already rated the delivery boy.", Toast.LENGTH_SHORT).show();
                }else {
                    RatingActivity.myOrder = myOrder;
                    RatingActivity.staus = 2;
                    startActivity(new Intent(getApplicationContext(), RatingActivity.class));
                }

            }

        });


        binding.itemPrice.setText("₹ " + myOrder.getSubtotal());
        binding.billDetail.setText("ORDER ID : " + myOrder.getOrderId()+"\nBILL DETAILS");
        binding.merchantName.setText(merchant.getName());




        Glide.with(getApplicationContext()).load(AppConstraints.BASE_URL + AppConstraints.MERCHANT + merchant.getIcon()).into(binding.restImage);
        if(!myOrder.getOrderStatus().equalsIgnoreCase("Delivered")) {
            try {
                binding.orderTime.setVisibility(View.VISIBLE);

                if (!myOrder.getAcceptedTIme().equalsIgnoreCase("")) {
                    Date date= Calendar.getInstance().getTime();
                    Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(myOrder.getAcceptedTIme());
//                    Date date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(dtf.format(now));
                    double deTIme = Double.parseDouble(myOrder.getDeliveryTime());

                    Duration remainTIme = Duration.between(date.toInstant() ,date1.toInstant());
                    long minuts=remainTIme.toMinutes();
//                    long minuts=  TimeUnit.MILLISECONDS.toMinutes(remainTIme);
                    Calendar now = Calendar.getInstance();
                    if(now.get(Calendar.AM_PM) == Calendar.AM){
                        minuts=Math.abs(minuts);
                    }else{
                        minuts=Math.abs(minuts)-720;
                    }

                    double mainTIme = deTIme + 15;
                    deliveryTime = deTIme + 15;
                    int finalTime=(int) (mainTIme - Math.abs(minuts) );
                    if(finalTime>0) {

                        if(countDownTimer!=null){
                            countDownTimer.cancel();
                        }
                        countDownTimer=  new CountDownTimer((finalTime*60)*1000, 1000) {

                            public void onTick(long millisUntilFinished) {
                                binding.orderTime.setText("Arriving in " + (millisUntilFinished / 1000)/60+" minutes");

//                                mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                                //here you can have your logic to set text to edittext
                            }

                            public void onFinish() {
                                binding.orderTime.setVisibility(View.GONE);
                            }

                        }.start();

                        binding.orderTime.setVisibility(View.VISIBLE);
                    }else{
                        if(countDownTimer!=null){
                            countDownTimer.cancel();
                        }
                        binding.orderTime.setVisibility(View.GONE);
                    }
                }else{
                    if(countDownTimer!=null){
                        countDownTimer.cancel();
                    }
                    binding.orderTime.setVisibility(View.GONE);
                }
            } catch (Exception e) {
//                Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        }else{
            if(countDownTimer!=null){
                countDownTimer.cancel();
            }
            binding.orderTime.setVisibility(View.GONE);


        }
        binding.merchantName1.setText(merchant.getName());
        binding.merchantAddress.setText(merchant.getAddress());

        if(!myOrder.getAlt_number().equalsIgnoreCase("")){
            binding.addNumber.setVisibility(View.GONE);
            binding.userName.setText(user.getName()+", "+user.getMobile()+", "+myOrder.getAlt_number());
        }else {
            binding.userName.setText(user.getName()+", "+user.getMobile());
            binding.addNumber.setVisibility(View.VISIBLE);
        }
        binding.userAddress.setText(address.getAddress());

        binding.submitNumber.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(binding.mobileNumber.getText().toString().length()==10) {
                            addNumber(binding.mobileNumber.getText().toString());
                        }else{
                            Toast.makeText(getApplicationContext(),"Please Enter Mobile Number",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        binding.addNumber.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(binding.lineMobile.getVisibility()==View.VISIBLE){
                            binding.lineMobile.setVisibility(View.GONE);
                        }else{
                            binding.lineMobile.setVisibility(View.VISIBLE);
                        }
//                        addNumber();
                    }
                }
        );
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(myOrder.getCreatedAt().split(" ")[0]);
            String dtt=new SimpleDateFormat("dd MMM, yyyy").format(date);
            binding.orderStatusWithDate.setText(myOrder.getOrderStatus() + " on " + dtt+" "+myOrder.getCreatedAt().split(" ")[1]);
        }catch (Exception e){

        }


        binding.itemsList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.itemsList.setHasFixedSize(true);
        binding.itemsList.setAdapter(new OrderItemsAdapter(myOrder.getOrderproduct(),getApplicationContext()));
//        binding.productNameWithQty.setText(order.getName() + " × " + order.getQty());
//        binding.productPrice.setText("₹ " + myOrder.getSubtotal());
        binding.subTotal.setText("₹ " + myOrder.getSubtotal());
        deliveryFeeAmount=myOrder.getShippinCharge();
        otherChargeAmount= myOrder.getOtherCharge();

        binding.packingCharge.setText("₹" + (Double.parseDouble(otherChargeAmount)+packingCharges+Double.parseDouble(deliveryFeeAmount)));

        binding.totalAmout.setText("₹ " + myOrder.getAmount());


        if (myOrder.getDeliveryBoyId().equals("")){
            binding.lineCall.setVisibility(View.GONE);
        }else {
            binding.lineCall.setVisibility(View.VISIBLE);
        }

        binding.lineCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+delBoyNumber));
                startActivity(intent);
            }
        });


        loadStatus();
    }





    private void loadStatus() {
        //Log.e("dsd","dfdff");
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call=myApi.getOrderStatus(orderId,user.getId());
        Log.e("ordrId",orderId+"ef");
        call.enqueue(new Callback<JsonArray>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful())
                {
                    try {
                        JSONArray jsonArray1=new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject= jsonArray1.getJSONObject(0);
                        String res=jsonObject.getString("res");
                        if(res.equals("success"))
                        {
                            JSONArray jsonArray2=jsonObject.getJSONArray("data");
                            statusList=new ArrayList<>();
                            int count=0;
                            int delCount=0;
                            String order_status="";
                            for(int i=0;i<jsonArray2.length();i++)
                            {
                                OrderStatus status=new Gson().fromJson(jsonArray2.getJSONObject(i).toString(),OrderStatus.class);
                                statusList.add(status);
                                if(status.getOrder_status().equalsIgnoreCase("delivered")){
                                    delCount=i;
                                    order_status="delivered";






                                }

                            }
                            count=statusList.size();

                            if(statusList.get(delCount).getOrder_status().equalsIgnoreCase("Delivered")){
                                try {
                                    Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(myOrder.getAcceptedTIme());
                                    Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(statusList.get(delCount).getCreated_at());
                                    double deTIme = Double.parseDouble(myOrder.getDeliveryTime());
                                    Duration remainTIme = Duration.between(date.toInstant(), date1.toInstant());
                                    long minuts = remainTIme.toMinutes();
                                    Calendar now = Calendar.getInstance();
//                                    if (now.get(Calendar.AM_PM) == Calendar.AM) {
//                                        minuts = Math.abs(minuts);
//                                    } else {
//                                        minuts = Math.abs(minuts) - 720;
//                                    }

                                    double mainTIme = deTIme + 15;
                                    int finalTime = (int) (mainTIme - Math.abs(minuts));
                                    if(mainTIme<minuts) {
                                        binding.link.setVisibility(View.GONE);
                                        binding.orderTime.setText("Sorry we could'nt deliver you within the given time due to unforeseen circumstances we would surely improve on your next order.Thank you for your patient and cooperation.");
                                    }else{
                                        binding.link.setVisibility(View.VISIBLE);
                                        binding.link.setOnClickListener(
                                                new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                       showRateDialoge1(ReturnOrderSummaryActivity.this);
                                                    }
                                                }
                                        );
                                        binding.orderTime.setText("You order has been delivered within " + minuts + " min. Please rate our service.");
                                    }
                                    binding.orderTime.setVisibility(View.VISIBLE);
                                }catch (Exception e){
                                    Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                                }
                            }

//                            binding.statusList.setLayoutManager(new LinearLayoutManager(OrderSummaryActivity.this,LinearLayoutManager.VERTICAL,false));
//                            binding.statusList.setAdapter(new OrderStatusAdapter(statusList));

                            binding.animation.setVisibility(View.GONE);
                            binding.bottomSheetContainer.setVisibility(View.VISIBLE);
                            String statusString=statusList.get(count-1).getOrder_status();
                            changeStatus(statusString);
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

    void changeStatus(String str){
        if(str.equalsIgnoreCase("Prepared")){
            binding.orderStatus.setText("Food Ready");
            binding.orderStatus1.setText("Your Food is Ready");
            binding.layoutNotice.setText("Your Food is Ready");
            binding.orderStatus1.setVisibility(View.VISIBLE);
            Picasso.get().load(R.drawable.pre_img).into(binding.imageForStatus);
            if(myOrder.getDelivery_boy_status_accepted().equalsIgnoreCase("true")) {
                binding.layoutDeliveryBoy.setVisibility(View.VISIBLE);
                binding.delNotice.setVisibility(View.GONE);
            }else{
                binding.layoutDeliveryBoy.setVisibility(View.GONE);
                binding.delNotice.setVisibility(View.VISIBLE);

            }

        }else if(str.equalsIgnoreCase("Delivered")){
            binding.orderStatus.setText("Order Delivered");
            binding.orderStatus1.setVisibility(View.VISIBLE);
            binding.orderStatus1.setText("Your Order has been Delivered");
            binding.layoutNotice.setText("Your Order has been Delivered");
            Picasso.get().load(R.drawable.delivered).into(binding.imageForStatus);
            if(myOrder!=null) {
                if (myOrder.getDeliveryboyratingstatus() != null) {
                    if (myOrder.getDeliveryboyratingstatus().equalsIgnoreCase("true")) {
                        binding.layoutDeliveryBoy.setVisibility(View.GONE);
                        binding.rateDeliveryBoy.setVisibility(View.VISIBLE);
                        binding.delNotice.setVisibility(View.GONE);
                    } else {
                        binding.layoutDeliveryBoy.setVisibility(View.GONE);
                        binding.rateDeliveryBoy.setVisibility(View.VISIBLE);
                        RatingActivity.myOrder = myOrder;
                        RatingActivity.staus = 2;
//                        if(transStatus==0) {
//                            transStatus=1;
                            startActivity(new Intent(getApplicationContext(), RatingActivity.class));
//                        }
                        binding.delNotice.setVisibility(View.GONE);
                    }
                }

            }
        }else if(str.equalsIgnoreCase("Waiting")){
            binding.orderStatus.setText("Waiting for confirmation");
            binding.orderStatus1.setVisibility(View.VISIBLE);
            binding.orderStatus1.setText("Restaurant will Accept your order soon");
            binding.layoutNotice.setText("Restaurant will Accept your order soon");
            Picasso.get().load(R.drawable.order_placed).into(binding.imageForStatus);
            binding.layoutDeliveryBoy.setVisibility(View.GONE);
            binding.delNotice.setVisibility(View.VISIBLE);
            binding.orderTime.setVisibility(View.GONE);
        }else if(str.equalsIgnoreCase("Missed")){
            binding.orderStatus.setText("Missed by Restaurant");
            binding.orderStatus1.setVisibility(View.GONE);
            binding.layoutNotice.setText("Order not placed.. Please Try Again.");
            Picasso.get().load(R.drawable.not_placed).into(binding.imageForStatus);
            binding.layoutDeliveryBoy.setVisibility(View.GONE);
            binding.delNotice.setVisibility(View.VISIBLE);
            binding.orderTime.setVisibility(View.GONE);
            if(myOrder.getMethod().equalsIgnoreCase("COD")) {
                binding.refundTxt.setVisibility(View.GONE);
            }else{
                binding.refundTxt.setVisibility(View.VISIBLE);
            }
            binding.taptoReorder.setVisibility(View.VISIBLE);
        }else if(str.equalsIgnoreCase("Cancelled")){
            binding.orderStatus.setText("Order Cancelled");
            binding.orderStatus1.setVisibility(View.VISIBLE);
            binding.orderStatus1.setText("Your Order has been Cancelled Successfully");
            binding.layoutNotice.setText("Your Order has been Cancelled Successfully");
            Glide.with(getApplicationContext()).load(R.drawable.cancel_order1).into(binding.imageForStatus);
            binding.layoutDeliveryBoy.setVisibility(View.GONE);
            binding.delNotice.setVisibility(View.GONE);
            binding.orderTime.setVisibility(View.GONE);

        }else if(str.equalsIgnoreCase("Preparing")){
            binding.customBar.setProgressPercentage(100,true);
            binding.lineProgrss.setVisibility(View.GONE);
               if(timer!=null){
                   timer.cancel();
               }
            binding.orderStatus.setText("Preparing Your Order");
            binding.orderStatus1.setVisibility(View.VISIBLE);
            binding.orderStatus1.setText("Your Food is being Prepared");
            binding.layoutNotice.setText("Your Food is being Prepared");
            Glide.with(getApplicationContext()).load(R.raw.preparing_food).into(binding.imageForStatus);
            if(myOrder.getDelivery_boy_status_accepted().equalsIgnoreCase("true")) {
                binding.layoutDeliveryBoy.setVisibility(View.VISIBLE);
                binding.delNotice.setVisibility(View.GONE);
            }else{
                binding.layoutDeliveryBoy.setVisibility(View.GONE);
                binding.delNotice.setVisibility(View.VISIBLE);

            }




        }else if(str.equalsIgnoreCase("Rejected")){
            binding.orderStatus.setText("Order Rejected");
            binding.orderStatus1.setVisibility(View.VISIBLE);
            binding.orderStatus1.setText("Your Order has been Rejected by the Restaurant ");
            binding.layoutNotice.setText("Your Order has been Rejected by the Restaurant ");
            Picasso.get().load(R.drawable.rejected).into(binding.imageForStatus);
            binding.layoutDeliveryBoy.setVisibility(View.GONE);
            binding.delNotice.setVisibility(View.GONE);
            binding.orderTime.setVisibility(View.GONE);
        }else if(str.equalsIgnoreCase("Picked")){
            binding.orderStatus.setText("Food is On it's Way");
            binding.layoutNotice.setText("Food is On it's Way");
            binding.orderStatus1.setVisibility(View.VISIBLE);
            if(myOrder!=null) {
                binding.orderStatus1.setText(myOrder.getDeliveryboy()[0].getName() + " has Picked Your Order.Your Happiness is on it's way");
            }
            Glide.with(getApplicationContext()).load(R.raw.rider).into(binding.imageForStatus);
            if(myOrder.getDelivery_boy_status_accepted().equalsIgnoreCase("true")) {
                binding.layoutDeliveryBoy.setVisibility(View.VISIBLE);
                binding.delNotice.setVisibility(View.GONE);
            }else{
                binding.layoutDeliveryBoy.setVisibility(View.GONE);
                binding.delNotice.setVisibility(View.VISIBLE);

            }
        }else if(str.equalsIgnoreCase("Placed")){
            binding.orderStatus.setText("Waiting for confirmation");
            binding.orderStatus1.setVisibility(View.VISIBLE);
            binding.orderStatus1.setText("Waiting for restaurant to confirm your order.");
            binding.layoutNotice.setText("Waiting for restaurant to confirm your order.");
            Picasso.get().load(R.drawable.order_placed).into(binding.imageForStatus);
            binding.layoutDeliveryBoy.setVisibility(View.GONE);
            binding.delNotice.setVisibility(View.VISIBLE);
        }

        transStatus=0;
    }
    private void reorder() {

        String js = singleTask.getValue("user");
        Log.e("orer", orderId);
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call = myApi.reorderFood(user.getId(), orderId);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        Log.e("sdsd", jsonArray.toString());
                        String res = jsonObject.getString("res");
                        String message = jsonObject.getString("message");
                        if (res.equals("success")) {

                            JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                            Toast.makeText(ReturnOrderSummaryActivity.this, message, Toast.LENGTH_SHORT).show();
                            openBottom();

                        } else {
                            Toast.makeText(ReturnOrderSummaryActivity.this, message, Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

                Toast.makeText(ReturnOrderSummaryActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addNumber(String num) {

        String js = singleTask.getValue("user");
        Log.e("orer", orderId);
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit1().create(MyApi.class);
        Call<JsonObject> call = myApi.addNumber(num, orderId,"updateNumber");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
//                        JSONObject jsonObject = jsonArray.getJSONObject(0);
//                        Log.e("sdsd", jsonArray.toString());
                        String res = jsonObject.getString("res");
//                        String message = jsonObject.getString("message");
                        if (res.equals("success")) {
                            binding.userName.setText(user.getName()+", "+user.getMobile()+", "+num);
                            binding.lineMobile.setVisibility(View.GONE);
                            binding.addNumber.setVisibility(View.GONE);
                            Toast.makeText(ReturnOrderSummaryActivity.this, "Number Added ", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(ReturnOrderSummaryActivity.this, "Number Not Added", Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                Toast.makeText(ReturnOrderSummaryActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openBottom() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.myBottomSheetDialogTheme);
        View view1 = LayoutInflater.from(this).inflate(R.layout.view_order_bottom_sheet_layout, (RelativeLayout) findViewById(R.id.bottom_sheet_container), false);
        ViewOrderBottomSheetLayoutBinding binding = ViewOrderBottomSheetLayoutBinding.bind(view1);
//        binding.Amount.setText("₹ " + myOrder.getAmount());
        binding.viewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckOutActivity.merchant = myOrder.getMerchant()[0];
                startActivity(new Intent(ReturnOrderSummaryActivity.this, CheckOutActivity.class));
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setContentView(view1);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.show();
    }



    public void goBack(View view) {
        finish();
        if(MyOrdersActivity.refresh!=null){
            MyOrdersActivity.refresh.onRefresh();
        }
    }

    public void goToHelp(View view) {
        startActivity(new Intent(this, ChatActivity.class));
    }




    /*class LoadData extends AsyncTask<Void, Void, Void> {

        String response = "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            if (response != null) {
                Log.e("sdsd", response + "");
            } else {
                Toast.makeText(OrderSummaryActivity.this, "nothing", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {

            FetchData fetchData = new FetchData();
            JSONObject jsonObject=null;
            try {
                 jsonObject=new JSONObject();
                jsonObject.put("user_id","");
                jsonObject.put("order_id","");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                response = fetchData.getJSON("https://codersadda.com/APIs/Slider", jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(CheckOutActivity.checkOut!=null){
            CheckOutActivity.checkOut.finish();
        }
        if(MyOrdersActivity.refresh!=null){
            MyOrdersActivity.refresh.onRefresh();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("GPSLocationUpdates"));
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    void missed(){
        progressDisplay.showProgress();
        String ven = singleTask.getValue("user");
        User vendor = new Gson().fromJson(ven, User.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call = myApi.missedOrder(vendor.getId(),myOrder.getOrderId());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);//new Gson().toJson(response.body()));
                        String res = jsonObject1.getString("res");
                        String message = jsonObject1.getString("message");
                        Log.e("sdsd", jsonObject1.toString());
                        if (res.equalsIgnoreCase("success")) {
                            binding.lineProgrss.setVisibility(View.GONE);
//                            changeStatus("Missed");
                            loadData();
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        } else {

                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
               progressDisplay.hideProgress();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                progressDisplay.hideProgress();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }


    public void showDialoge(Context ctx,String id,String userId) {
        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        View promptView = layoutInflater.inflate(R.layout.reorder_confirmation, null);
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ctx);
        alertDialogBuilder.setView(promptView);


        final android.app.AlertDialog alert2 = alertDialogBuilder.create();
        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView add=promptView.findViewById(R.id.add);
        TextView cancel=promptView.findViewById(R.id.cancel);


        add.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reorder(id,userId);
                        alert2.dismiss();
                    }
                }
        );

        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alert2.dismiss();
                    }
                }
        );


        alert2.show();

    }

    private void reorder(String id,String userId) {
        ShowProgress.getShowProgress(ReturnOrderSummaryActivity.this).show();
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call = myApi.reorder(userId,id);
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
                            ShowProgress.getShowProgress(ReturnOrderSummaryActivity.this).hide();
                            Intent intent=new Intent(getApplicationContext(), CheckOutActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
//                            Toast.makeText(OrderSummaryActivity.this, msg, Toast.LENGTH_SHORT).show();

                        } else {
                            ShowProgress.getShowProgress(ReturnOrderSummaryActivity.this).hide();
                            Toast.makeText(ReturnOrderSummaryActivity.this, msg, Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                ShowProgress.getShowProgress(ReturnOrderSummaryActivity.this).hide();
                Toast.makeText(ReturnOrderSummaryActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void showRateDialoge1(Context ctx) {
        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        View promptView = layoutInflater.inflate(R.layout.rate_app_dialoge1, null);
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ctx);
        alertDialogBuilder.setView(promptView);


        final android.app.AlertDialog alert2 = alertDialogBuilder.create();
        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView cancel=promptView.findViewById(R.id.cancel);
        LinearLayout lineUnsatisfied=promptView.findViewById(R.id.lineUnsatisfied);
        LinearLayout lineLovedIt=promptView.findViewById(R.id.lineLovedIt);
        LinearLayout lineExpand=promptView.findViewById(R.id.lineExpand);
        EditText edit_text=promptView.findViewById(R.id.edit_text);
        TextView submit=promptView.findViewById(R.id.submit);


        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert2.dismiss();
                    }
                }
        );

        lineUnsatisfied.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lineExpand.setVisibility(View.VISIBLE);
                    }
                }
        );

        lineLovedIt.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lineExpand.setVisibility(View.GONE);
                        showRateDialoge2(ctx);
                        alert2.dismiss();
                    }
                }
        );

        submit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(edit_text.getText().toString().length()>0){
                            submitFeedback(edit_text.getText().toString(),alert2);
                        }
                    }
                }
        );
//        alert2.setCanceledOnTouchOutside(false);

        alert2.show();

    }
    public void showRateDialoge2(Context ctx) {
        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        View promptView = layoutInflater.inflate(R.layout.rate_app_dialoge2, null);
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ctx);
        alertDialogBuilder.setView(promptView);


        final android.app.AlertDialog alert2 = alertDialogBuilder.create();
        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView cancel=promptView.findViewById(R.id.nope);
        TextView sure=promptView.findViewById(R.id.sure);


        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert2.dismiss();
                    }
                }
        );

        sure.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+ BuildConfig.APPLICATION_ID));
                        startActivity(intent);
                    }
                }
        );


//        alert2.setCanceledOnTouchOutside(false);

        alert2.show();

    }

    private void submitFeedback(String txt,android.app.AlertDialog alert2) {
        ProgressDialog progressDialog=ProgressDialog.show(ReturnOrderSummaryActivity.this,"","Loading");
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        MyApi myApi=singleTask.getRetrofit1().create(MyApi.class);
        Call<JsonObject> call=myApi.submitFeedback(user.getId(),txt);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful())
                {
                    try {
                        JSONObject jsonObject=new JSONObject(new Gson().toJson(response.body()));
                        String res=jsonObject.getString("res");
                        if(res.equals("success"))
                        {
                            alert2.dismiss();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                progressDialog.dismiss();
            }
        });

    }









}