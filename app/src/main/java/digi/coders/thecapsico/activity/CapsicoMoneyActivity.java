package digi.coders.thecapsico.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cashfree.pg.CFPaymentService;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

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
import digi.coders.thecapsico.adapter.TransactionAdapter;
import digi.coders.thecapsico.databinding.ActivityCapsicoMoneyBinding;
import digi.coders.thecapsico.databinding.PaymentOptionLayoutBinding;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.ShowProgress;
import digi.coders.thecapsico.model.AddToWallet;
import digi.coders.thecapsico.model.Transaction;
import digi.coders.thecapsico.model.User;
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

public class CapsicoMoneyActivity extends AppCompatActivity implements  PaymentStatusListener {

    private RecyclerView transactionHistory;
    private LinearLayout addMoney;
    private SingleTask singleTask;
    public static AddToWallet toWallet;
    ActivityCapsicoMoneyBinding binding;
    private List<Transaction> transactionList;
    public static Activity CapsicoMoneyActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCapsicoMoneyBinding.inflate(getLayoutInflater());
        singleTask=(SingleTask)getApplication();
        setContentView(binding.getRoot());
        CapsicoMoneyActivity=this;
        getUpiApp();
        initView();
        loadTransactionHistoryList();

        binding.btnAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String orderAmount=binding.inputAmount.getText().toString();

                        if(orderAmount.isEmpty())
                        {
                            binding.inputAmount.setError("Please Enter amount of money");
                            binding.inputAmount.requestFocus();
                        }
                        else {
                            startPayment(orderAmount);
                        }
                    }
                }
        );
    }

    private void startPayment(String orderAmount) {
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        ShowProgress.getShowProgress(CapsicoMoneyActivity.this).show();
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call = myApi.addToWallet(user.getId(),orderAmount);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String res = jsonObject.getString("res");
                        String msg = jsonObject.getString("message");
                        ShowProgress.getShowProgress(CapsicoMoneyActivity.this).hide();
                        if (res.equals("success")) {
                            Toast.makeText(CapsicoMoneyActivity.this, msg, Toast.LENGTH_SHORT).show();
                            Log.e("sdsd", jsonArray.toString());
                            JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                            JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
                            AddToWallet addToWallet = new Gson().fromJson(jsonObject1.toString(), AddToWallet.class);
                            toWallet=addToWallet;
                            paymentOption(null,toWallet.getAmount());
                        } else {
                            ShowProgress.getShowProgress(CapsicoMoneyActivity.this).hide();
                            Toast.makeText(CapsicoMoneyActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    private void loadTransactionHistoryList() {
//        ShowProgress.getShowProgress(CapsicoMoneyActivity.this).show();
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call=myApi.getTransactionHistory(user.getId());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful())
                {

                    try {
                        JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject= jsonArray.getJSONObject(0);
                        String res=jsonObject.getString("res");
                        String msg=jsonObject.getString("message");
                        String wallet=jsonObject.getString("wallet");
                        binding.walletMoney.setText("₹ "+wallet);
                        if(res.equals("success"))
                        {
                            //ShowProgress.getShowProgress(CapsicoMoneyActivity.this).hide();
                            JSONArray jsonArray1=jsonObject.getJSONArray("data");
                            transactionList=new ArrayList<>();
                            for(int i=0;i<jsonArray1.length();i++)
                            {
                                Transaction transaction=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),Transaction.class);
                                transactionList.add(transaction);
                            }
                            transactionHistory.setLayoutManager(new LinearLayoutManager(CapsicoMoneyActivity.this,LinearLayoutManager.VERTICAL,false));
                            transactionHistory.setAdapter(new TransactionAdapter(transactionList));

                            if(transactionList.size()==0){
                                binding.noData.setVisibility(View.VISIBLE);
                                transactionHistory.setVisibility(View.GONE);
                                binding.txtTransa.setVisibility(View.GONE);
                            }else{
                                binding.noData.setVisibility(View.GONE);
                                transactionHistory.setVisibility(View.VISIBLE);
                                binding.txtTransa.setVisibility(View.VISIBLE);
                            }
                        }
                        else
                        {
                            binding.noData.setVisibility(View.VISIBLE);
                            transactionHistory.setVisibility(View.GONE);
                            binding.txtTransa.setVisibility(View.GONE);
                            //ShowProgress.getShowProgress(CapsicoMoneyActivity.this).hide();
                            Toast.makeText(CapsicoMoneyActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        binding.noData.setVisibility(View.VISIBLE);
                        transactionHistory.setVisibility(View.GONE);
                        binding.txtTransa.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                binding.noData.setVisibility(View.VISIBLE);
                binding.txtTransa.setVisibility(View.GONE);
                transactionHistory.setVisibility(View.GONE);
                //ShowProgress.getShowProgress(CapsicoMoneyActivity.this).hide();
                Toast.makeText(CapsicoMoneyActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initView() {
        transactionHistory=findViewById(R.id.transaction_list);
        addMoney=findViewById(R.id.add_money);
    }






    public void goBack(View view) {
        finish();
    }

    public void goToBack(View view) {
        finish();
    }





//    private void initiatePayment() {
//        Map<String, String> params = new HashMap<>();
//        String js = singleTask.getValue("user");
//        User user = new Gson().fromJson(js, User.class);
//        params.put(PARAM_APP_ID, toWallet.getAppId());
//        params.put(PARAM_ORDER_ID, toWallet.getOrderId());
//        params.put(PARAM_ORDER_AMOUNT, toWallet.getAmount());
//        params.put(PARAM_ORDER_NOTE, "For Add Money in Wallet");
//        params.put(PARAM_CUSTOMER_NAME, user.getName());
//        params.put(PARAM_CUSTOMER_PHONE, user.getMobile());
//        params.put(PARAM_CUSTOMER_EMAIL, user.getEmail());
//        params.put(PARAM_ORDER_CURRENCY, "INR");
//        CFPaymentService cfPaymentService = CFPaymentService.getCFPaymentServiceInstance();
//        cfPaymentService.setOrientation(0);
//        //for production
//       /* cfPaymentService.doPayment(OrderSummaryActivity.this, params,
//
//                "", "PROD", "#F8A31A", "#FFFFFF", false);
//*/
//        cfPaymentService.doPayment(CapsicoMoneyActivity.this, params, toWallet.getToken(), toWallet.getType(), "#42b22a", "#FFFFFF", false);
//    }

    private void initiatePayment() {
        Map<String, String> params = new HashMap<>();
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        params.put(PARAM_APP_ID,  toWallet.getAppId());
        params.put(PARAM_ORDER_ID, toWallet.getOrderId());
        params.put(PARAM_ORDER_AMOUNT, toWallet.getAmount());
        params.put(PARAM_ORDER_NOTE, "For Add Money in Wallet");
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
        cfPaymentService.doPayment(CapsicoMoneyActivity.this, params, toWallet.getToken(), toWallet.getEnv(), "#ff7f00", "#FFFFFF", false);
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
                updatePaymentStatus(referenceid, txStatus, bundle.toString(), txMsg);
            } else {
                updatePaymentStatus("", txMsg, bundle.toString(), txMsg);
            }
        }
    }


    private void updatePaymentStatus(String referenceid, String status, String bundle, String s) {
        ShowProgress.getShowProgress(this).show();
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call = myApi.paymentConfirmation(user.getId(),referenceid,toWallet.getOrderId(),bundle.toString(),status);
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
                            ShowProgress.getShowProgress(CapsicoMoneyActivity.this).dismiss();
                            Toast.makeText(CapsicoMoneyActivity.this, msg, Toast.LENGTH_SHORT).show();
                        } else {
                            ShowProgress.getShowProgress(CapsicoMoneyActivity.this).dismiss();
                            Toast.makeText(CapsicoMoneyActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                ShowProgress.getShowProgress(CapsicoMoneyActivity.this).dismiss();
                Toast.makeText(CapsicoMoneyActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
    @Override
    protected void onResume() {
        super.onResume();
        loadTransactionHistoryList();
    }

    private void paymentOption(View v,String amount) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.myBottomSheetDialogTheme);
        View view1 = LayoutInflater.from(this).inflate(R.layout.payment_option_layout, (CoordinatorLayout) findViewById(R.id.bottom_sheet_container), false);
        bottomSheetDialog.setContentView(view1);
        PaymentOptionLayoutBinding binding = PaymentOptionLayoutBinding.bind(view1);
//        fetchWalletAmount(binding);


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

                        initiatePayment();
                    }
                }
        );


        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.show();

    }

    @Override
    public void onTransactionCancelled() {
        Toast.makeText(CapsicoMoneyActivity.this,"Cancelled by user",Toast.LENGTH_LONG).show();
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
        updatePaymentStatus(referenceid, "SUCCESS", orderId, "Order");

    }

    private void onTransactionSubmitted() {
        // Payment Pending
        Toast.makeText(CapsicoMoneyActivity.this,"Pending | Submitted",Toast.LENGTH_LONG).show();

    }

    private void onTransactionFailed() {
        // Payment Failed
        Toast.makeText(CapsicoMoneyActivity.this,"Failed",Toast.LENGTH_LONG).show();

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

    EasyUpiPayment easyUpiPayment;

    public void UPIPayment(String amount,PaymentApp paymentApp) throws AppNotFoundException{

        try {
            EasyUpiPayment.Builder  builder= new EasyUpiPayment.Builder(this)
                    .with(paymentApp)
                    .setPayeeVpa("Q915573461@ybl")
                    .setPayeeName("Capsico")
                    .setTransactionId(System.currentTimeMillis()+"")
                    .setTransactionRefId(System.currentTimeMillis()+"")
                    .setPayeeMerchantCode("")
                    .setDescription("Capsico Online Ordering")
                    .setAmount(Double.parseDouble(amount)+"");


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

}