package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import digi.coders.thecapsico.databinding.ActivityPaymentModeBinding;

public class PaymentModeActivity extends AppCompatActivity   {

    ActivityPaymentModeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPaymentModeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //startPayment();
            }
        });

    }
//    String api_key;
//    private void startPayment() {
//       /* String jsonObject = singleTask.getValue("user");
//        user = new Gson().fromJson(jsonObject, User.class);
//        mobileNo = user.getNumber();*/
//        //Log.e("user",user.getNumber());
//        //Log.e("sds",singleTask.getValue("mobile"));
//        if (api_key == null) {
//            //Log.e("api_key", api_key);
//            final com.razorpay.Checkout co = new Checkout();
//            //co.setKeyID(api_key);
//            try {
//                JSONObject options = new JSONObject();
//                options.put("name", "Atul");
//                /*if (key.equals("1")) {
//                    options.put("description", "Payment  For " + course.getName() + "Course at  CodersAdda.com");
//                }
//                if (key.equals("2")) {
//                    options.put("description", "Payment  For " + ebook.getName() + "Course at  CodersAdda.com");
//                }*/
//                options.put("description", "Payment  For " + "Order" + "Food from Capsico");
//                options.put("image", "");
//                options.put("currency", "INR");
//                options.put("theme.color", "#FBA510");
//                //Log.e("asdsad",ORDER_ID);
//                options.put("order_id", "");
//                //String d = orderTotalPrice.getText().toString().substring(1);
//                //Log.e("sd",d);
//                //Log.e("sds",d);
//                //Log.e("sds", Double.parseDouble("23") * 100.0 + "");
//                options.put("amount", Double.parseDouble("23") * 100.0);
//                JSONObject preFill = new JSONObject();
//                preFill.put("email", "atul@gmail.com");
//                preFill.put("contact", "8707639833");
//                options.put("prefill", preFill);
//                /*       progressDialog.hide();*/
//                co.open(PaymentModeActivity.this, options);
//
//            } catch (Exception e) {
//                Toast.makeText(PaymentModeActivity.this,"Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
//                        .show();
//                Log.e("Error in payment", e.getMessage());
//                e.printStackTrace();
//            }
//        } else {
//            //getRezorPayAPiKey();
//        }
//    }
}