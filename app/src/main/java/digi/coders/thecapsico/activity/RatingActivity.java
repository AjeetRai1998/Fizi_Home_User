package digi.coders.thecapsico.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.squareup.picasso.Picasso;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import digi.coders.thecapsico.BuildConfig;
import digi.coders.thecapsico.R;
import digi.coders.thecapsico.adapter.OrderItemRatingAdapter;
import digi.coders.thecapsico.databinding.ActivityRatingBinding;
import digi.coders.thecapsico.helper.AppConstraints;
import digi.coders.thecapsico.helper.FunctionClass;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.ShowProgress;
import digi.coders.thecapsico.model.Merchant;
import digi.coders.thecapsico.model.MyOrder;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingActivity extends AppCompatActivity {

    ActivityRatingBinding binding;
    public static int staus;
    public static MyOrder myOrder;
    private SingleTask singleTask;
    String tasteStatus="2",packingStatus="2",quantityStatus="2",hygieneStatus="2",image="";
    double rating=2.5;
    public static Activity RatingActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRatingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        RatingActivity=this;
        singleTask = (SingleTask) getApplication();
        //handle back
        setData();
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        binding.items.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.items.setHasFixedSize(true);
        binding.items.setAdapter(new OrderItemRatingAdapter(myOrder.getOrderproduct(),getApplicationContext()));

        calculate();
        binding.selectImage.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImagePicker.with(RatingActivity.this)
                                .crop()	    			//Crop image(Optional), Check Customization for more option
                                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                                .start();
                    }
                }
        );
        //loadItemList
        //loadItemList();
        binding.seekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                rating=seekParams.progress;
                binding.ratingTxt.setText("You are giving "+rating+" star rating");

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
            }
        });


        binding.tBad.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tasteStatus="1";
                        calculate();
                        binding.tcheckGood.setVisibility(View.GONE);
                        binding.tcheckBad.setVisibility(View.VISIBLE);
                        binding.tcheckGreat.setVisibility(View.GONE);
                        binding.tcheckOk.setVisibility(View.GONE);
                    }
                }
        );
        binding.tOk.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tasteStatus="2";
                        calculate();
                        binding.tcheckGood.setVisibility(View.GONE);
                        binding.tcheckBad.setVisibility(View.GONE);
                        binding.tcheckGreat.setVisibility(View.GONE);
                        binding.tcheckOk.setVisibility(View.VISIBLE);
                    }
                }
        );
        binding.thappy.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tasteStatus="3";
                        calculate();
                        binding.tcheckGood.setVisibility(View.VISIBLE);
                        binding.tcheckBad.setVisibility(View.GONE);
                        binding.tcheckGreat.setVisibility(View.GONE);
                        binding.tcheckOk.setVisibility(View.GONE);
                    }
                }
        );
        binding.tGreat.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tasteStatus="4";
                        calculate();
                        binding.tcheckGood.setVisibility(View.GONE);
                        binding.tcheckBad.setVisibility(View.GONE);
                        binding.tcheckGreat.setVisibility(View.VISIBLE);
                        binding.tcheckOk.setVisibility(View.GONE);
                    }
                }
        );

        binding.qBad.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        quantityStatus="1";
                        calculate();
                        binding.qcheckBad.setVisibility(View.VISIBLE);
                        binding.qcheckGood.setVisibility(View.GONE);
                        binding.qcheckGreat.setVisibility(View.GONE);
                        binding.qcheckOK.setVisibility(View.GONE);
                    }
                }
        );
        binding.qOk.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        quantityStatus="2";
                        calculate();
                        binding.qcheckBad.setVisibility(View.GONE);
                        binding.qcheckGood.setVisibility(View.GONE);
                        binding.qcheckGreat.setVisibility(View.GONE);
                        binding.qcheckOK.setVisibility(View.VISIBLE);
                    }
                }
        );
        binding.qHappy.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        quantityStatus="3";
                        calculate();
                        binding.qcheckBad.setVisibility(View.GONE);
                        binding.qcheckGood.setVisibility(View.VISIBLE);
                        binding.qcheckGreat.setVisibility(View.GONE);
                        binding.qcheckOK.setVisibility(View.GONE);
                    }
                }
        );
        binding.qGreat.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        quantityStatus="4";
                        calculate();
                        binding.qcheckBad.setVisibility(View.GONE);
                        binding.qcheckGood.setVisibility(View.GONE);
                        binding.qcheckGreat.setVisibility(View.VISIBLE);
                        binding.qcheckOK.setVisibility(View.GONE);
                    }
                }
        );
        binding.pBad.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        packingStatus="1";
                        calculate();
                        binding.pcheckBad.setVisibility(View.VISIBLE);
                        binding.pcheckGood.setVisibility(View.GONE);
                        binding.pcheckGreat.setVisibility(View.GONE);
                        binding.pcheckOk.setVisibility(View.GONE);
                    }
                }
        );
        binding.pOk.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        packingStatus="2";
                        calculate();
                        binding.pcheckBad.setVisibility(View.GONE);
                        binding.pcheckGood.setVisibility(View.GONE);
                        binding.pcheckGreat.setVisibility(View.GONE);
                        binding.pcheckOk.setVisibility(View.VISIBLE);
                    }
                }
        );
        binding.pHappy.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        packingStatus="3";
                        calculate();
                        binding.pcheckBad.setVisibility(View.GONE);
                        binding.pcheckGood.setVisibility(View.VISIBLE);
                        binding.pcheckGreat.setVisibility(View.GONE);
                        binding.pcheckOk.setVisibility(View.GONE);
                    }
                }
        );
        binding.pGreat.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        packingStatus="4";
                        calculate();
                        binding.pcheckBad.setVisibility(View.GONE);
                        binding.pcheckGood.setVisibility(View.GONE);
                        binding.pcheckGreat.setVisibility(View.VISIBLE);
                        binding.pcheckOk.setVisibility(View.GONE);
                    }
                }
        );
        binding.hBad.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hygieneStatus="1";
                        calculate();
                        binding.hcheckBad.setVisibility(View.VISIBLE);
                        binding.hcheckGood.setVisibility(View.GONE);
                        binding.hcheckGreat.setVisibility(View.GONE);
                        binding.hcheckOK.setVisibility(View.GONE);
                    }
                }
        );
        binding.hOk.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hygieneStatus="2";
                        calculate();
                        binding.hcheckBad.setVisibility(View.GONE);
                        binding.hcheckGood.setVisibility(View.GONE);
                        binding.hcheckGreat.setVisibility(View.GONE);
                        binding.hcheckOK.setVisibility(View.VISIBLE);
                    }
                }
        );
        binding.hHappy.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hygieneStatus="3";
                        calculate();
                        binding.hcheckBad.setVisibility(View.GONE);
                        binding.hcheckGood.setVisibility(View.VISIBLE);
                        binding.hcheckGreat.setVisibility(View.GONE);
                        binding.hcheckOK.setVisibility(View.GONE);
                    }
                }
        );
        binding.hGreat.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hygieneStatus="4";
                        calculate();
                        binding.hcheckBad.setVisibility(View.GONE);
                        binding.hcheckGood.setVisibility(View.GONE);
                        binding.hcheckGreat.setVisibility(View.VISIBLE);
                        binding.hcheckOK.setVisibility(View.GONE);
                    }
                }
        );
        binding.submitRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRating();
            }
        });

        calculate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            try {
                //Image Uri will not be null for RESULT_OK
                Uri uri = data.getData();

                    binding.selectedImage.setImageURI(uri);

                    binding.selectedImage.setVisibility(View.VISIBLE);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    image = new FunctionClass().encodeImagetoString(bitmap);

            }catch (Exception e){

            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    void calculate(){
        double total=(Double.parseDouble(tasteStatus)+Double.parseDouble(packingStatus)+Double.parseDouble(quantityStatus)+Double.parseDouble(hygieneStatus))/16;
         rating=((total*100)*5)/100;
        binding.seekBar.setProgress((float)rating);
        binding.ratingTxt.setText("You are giving "+new DecimalFormat("#.#").format(rating)+" star rating");
    }
    private void setData() {
        if(staus==1) {
//            binding.merchantPhoto.setVisibility(View.VISIBLE);
            binding.txtNotice.setVisibility(View.VISIBLE);
            binding.lineMerchant.setVisibility(View.VISIBLE);
            binding.ratingLine.setVisibility(View.VISIBLE);
            binding.deliveryBoy.setVisibility(View.GONE);
            binding.linePayMsg.setVisibility(View.GONE);
            if (myOrder != null) {
                binding.txtNotice1.setText("Rate your experience with the restaurant");
                Merchant merchants = myOrder.getMerchant()[0];
                binding.merchantName.setText("Rating for " + merchants.getName());
                binding.reviewTxt.setHint("Tell us more about food (Optional)");
                Picasso.get().load(AppConstraints.BASE_URL + AppConstraints.MERCHANT + merchants.getIcon()).placeholder(R.drawable.placeholder).into(binding.merchantPhoto);
            }
        }
        else
        {
            binding.txtNotice1.setText("Rate your experience with the delivery boy");
            binding.deliveryBoy.setVisibility(View.VISIBLE);
            binding.linePayMsg.setVisibility(View.VISIBLE);
            binding.txtNotice.setVisibility(View.GONE);
            binding.lineMerchant.setVisibility(View.GONE);
            binding.ratingLine.setVisibility(View.GONE);
            binding.merchantPhoto.setVisibility(View.GONE);
            binding.merchantName.setText("Rating for Delivery Boy");
            binding.reviewTxt.setHint("Tell us more about delivery (Optional)");
            if(myOrder.getMethod().equalsIgnoreCase("COD")) {
                binding.payMode.setText("\u20b9 CASH");
                binding.txtMsg.setText("You have been charged \u20b9"+myOrder.getAmount()+" for this order.\nPlease pay the amount to the delivery boy.\nThank you for ordering from Capsico.");
            }else{
                binding.payMode.setText("ONLINE");
                binding.txtMsg.setText("You have been charged \u20b9"+myOrder.getAmount()+" for this order.\nYour payment has been received.\nThank you for ordering from Capsico.");

            }
        }
    }

    public String itemRating="";
    private void submitRating() {
        ShowProgress.getShowProgress(this).show();
        itemRating="";
        for(int i=0;i<OrderItemRatingAdapter.arrayList.size();i++){
            if(!OrderItemRatingAdapter.arrayList.get(i).equalsIgnoreCase("")) {
                if (i == 0) {
                    if (itemRating.equalsIgnoreCase("")) {
                        itemRating = myOrder.getOrderproduct()[i].getName() + "CAPSICO" + OrderItemRatingAdapter.arrayList.get(i);
                    } else {
                        itemRating = itemRating+ "CAPINDIA"+myOrder.getOrderproduct()[i].getName() + "CAPSICO"  + OrderItemRatingAdapter.arrayList.get(i) ;
                    }
                } else {
                    if (itemRating.equalsIgnoreCase("")) {
                        itemRating = myOrder.getOrderproduct()[i].getName() + "CAPSICO" + OrderItemRatingAdapter.arrayList.get(i);
                    } else {
                        itemRating = itemRating+ "CAPINDIA"+myOrder.getOrderproduct()[i].getName() + "CAPSICO"  + OrderItemRatingAdapter.arrayList.get(i) ;
                    }
                }
            }else{
                if (i == 0) {
                    if (itemRating.equalsIgnoreCase("")) {
                        itemRating = myOrder.getOrderproduct()[i].getName() + "CAPSICO" + OrderItemRatingAdapter.arrayList.get(i);
                    } else {
                        itemRating = itemRating+ "CAPINDIA"+myOrder.getOrderproduct()[i].getName() + "CAPSICO"  + OrderItemRatingAdapter.arrayList.get(i) ;
                    }
                } else {
                    if (itemRating.equalsIgnoreCase("")) {
                        itemRating = myOrder.getOrderproduct()[i].getName() + "CAPSICO" + OrderItemRatingAdapter.arrayList.get(i);
                    } else {
                        itemRating = itemRating+ "CAPINDIA"+myOrder.getOrderproduct()[i].getName() + "CAPSICO"  + OrderItemRatingAdapter.arrayList.get(i) ;
                    }
                }
            }
        }

                String js=singleTask.getValue("user");
                User user=new Gson().fromJson(js,User.class);
                MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
                Call<JsonArray> call=null;
                if(staus==1)
                {
                    call=myApi.giveRating(user.getId(),myOrder.getOrderId(),binding.reviewTxt.getText().toString(),itemRating,tasteStatus,packingStatus,quantityStatus,hygieneStatus,"merchant",image,myOrder.getMerchantId(),(float)rating);
                }
                else
                {
                    call=myApi.giveRating(user.getId(),myOrder.getOrderId(),binding.reviewTxt.getText().toString(),itemRating,"","","","","deliveryboy","",myOrder.getDeliveryBoyId(),(float)rating);
                }

                call.enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        if(response.isSuccessful())
                        {
                            try {
                                JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                                JSONObject jsonObject=jsonArray.getJSONObject(0);
                                Log.e("sdsd",jsonArray.toString());
                                String res=jsonObject.getString("res");
                                String message=jsonObject.getString("message");
                                if(res.equals("success"))
                                {
                                    Toast.makeText(RatingActivity.this, message, Toast.LENGTH_SHORT).show();
                                    ShowProgress.getShowProgress(RatingActivity.this).hide();


                                    ratingSuccess(RatingActivity.this);

                                }

                                else
                                {
                                    Toast.makeText(RatingActivity.this, message, Toast.LENGTH_SHORT).show();
                                    ShowProgress.getShowProgress(RatingActivity.this).hide();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        ShowProgress.getShowProgress(RatingActivity.this).hide();
                        Toast.makeText(RatingActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

    String review;
    private boolean valid() {
        review=binding.reviewTxt.getText().toString();
        if(rating==0)
        {
            Toast.makeText(RatingActivity.this, "Please Rating", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(review.isEmpty())
        {
            Toast.makeText(RatingActivity.this, "Please Write Review", Toast.LENGTH_SHORT).show();
            return  false;
        }
        else
        {
            return true;
        }
    }


    public void ratingSuccess(Context ctx) {
        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        View promptView = layoutInflater.inflate(R.layout.rate_success_layout, null);
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ctx);
        alertDialogBuilder.setView(promptView);


        final android.app.AlertDialog alert2 = alertDialogBuilder.create();
        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView done=promptView.findViewById(R.id.done);


        done.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert2.dismiss();
                        startActivity(new Intent(RatingActivity.this,MyOrdersActivity.class));
                        finish();
                    }
                }
        );


        alert2.show();

    }

}