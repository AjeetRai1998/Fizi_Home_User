package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import digi.coders.thecapsico.databinding.ActivityAccountSettingBinding;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountSettingActivity extends AppCompatActivity {

    ActivityAccountSettingBinding binding;
    private ProgressDialog progressDialog;
    private SingleTask singleTask;
    public static Activity AccountSettingActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityAccountSettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AccountSettingActivity=this;
        singleTask=(SingleTask)getApplication();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("waiting...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        //handle back
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //edit account
        binding.editAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountSettingActivity.this,EditAccountActivity.class));
            }
        });


        // saved address
        binding.savedAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountSettingActivity.this,ManageAddressActivity.class));
            }
        });


        //logout
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String js=singleTask.getValue("user");
                User user=new Gson().fromJson(js,User.class);
                progressDialog.show();
                MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
                Call<JsonArray> call=myApi.logout(user.getId());
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
                                if(res.equals("success"))
                                {
                                    progressDialog.hide();
                                    Toast.makeText(AccountSettingActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    singleTask.removeUser("user");
                                    if(AboutUsActivity.AboutUsActivity!=null){
                                        AboutUsActivity.AboutUsActivity.finish();
                                    }
                                    if(AddAddressActivity.AddAddressActivity!=null){
                                        AddAddressActivity.AddAddressActivity.finish();
                                    }
                                    if(AddressSearchActivity.AddressSearchActivity!=null){
                                        AddressSearchActivity.AddressSearchActivity.finish();
                                    }
                                    if(AllCategoriesActivity.AllCategoriesActivity!=null){
                                        AllCategoriesActivity.AllCategoriesActivity.finish();
                                    }
                                    if(BrandsFullViewActivity.BrandsFullViewActivity!=null){
                                        BrandsFullViewActivity.BrandsFullViewActivity.finish();
                                    }
                                    if(BrowesStoreActivity.BrowesStoreActivity!=null){
                                        BrowesStoreActivity.BrowesStoreActivity.finish();
                                    }
                                    if(CancelOrderActivity.CancelOrderActivity!=null){
                                        CancelOrderActivity.CancelOrderActivity.finish();
                                    }
                                    if(CapsicoMoneyActivity.CapsicoMoneyActivity!=null){
                                        CapsicoMoneyActivity.CapsicoMoneyActivity.finish();
                                    }
                                    if(CartActivity.CartActivity!=null){
                                        CartActivity.CartActivity.finish();
                                    }
                                    if(ChangePasswordActivity.ChangePasswordActivity!=null){
                                        ChangePasswordActivity.ChangePasswordActivity.finish();
                                    }
                                    if(ChatActivity.ChatActivity!=null){
                                        ChatActivity.ChatActivity.finish();
                                    }
                                    if(CheckOutActivity.CheckOutActivity1!=null){
                                        CheckOutActivity.CheckOutActivity1.finish();
                                    }
                                    if(ChooseLocationActivity.ChooseLocationActivity!=null){
                                        ChooseLocationActivity.ChooseLocationActivity.finish();
                                    }
                                    if(CouponActivity.CouponActivity!=null){
                                        CouponActivity.CouponActivity.finish();
                                    }
                                    if(CouponFullViewActivity.CouponFullViewActivity!=null){
                                        CouponFullViewActivity.CouponFullViewActivity.finish();
                                    }
                                    if(CreateNewChatTokenActivity.CreateNewChatTokenActivity!=null){
                                        CreateNewChatTokenActivity.CreateNewChatTokenActivity.finish();
                                    }
                                    if(DashboardActivity.DashboardActivity!=null){
                                        DashboardActivity.DashboardActivity.finish();
                                    }
                                    if(DeliveryLocationActivity.DeliveryLocationActivity!=null){
                                        DeliveryLocationActivity.DeliveryLocationActivity.finish();
                                    }
                                    if(EditAccountActivity.EditAccountActivity!=null){
                                        EditAccountActivity.EditAccountActivity.finish();
                                    }
                                    if(ForgotPassword.ForgotPassword!=null){
                                        ForgotPassword.ForgotPassword.finish();
                                    }
                                    if(GetPermissionActivity.GetPermissionActivity!=null){
                                        GetPermissionActivity.GetPermissionActivity.finish();
                                    }
                                    if(GetStartedActivity.GetStartedActivity!=null){
                                        GetStartedActivity.GetStartedActivity.finish();
                                    }
                                    if(HelpActivity.HelpActivity!=null){
                                        HelpActivity.HelpActivity.finish();
                                    }
                                    if(IntroScreenActivity.IntroScreenActivity!=null){
                                        IntroScreenActivity.IntroScreenActivity.finish();
                                    }
                                    if(ItemDetailsActivity.ItemDetailsActivity1!=null){
                                        ItemDetailsActivity.ItemDetailsActivity1.finish();
                                    }
                                    if(LocationActivity.LocationActivity!=null){
                                        LocationActivity.LocationActivity.finish();
                                    }
                                    if(LocationOffActivity.LocationOffActivity!=null){
                                        LocationOffActivity.LocationOffActivity.finish();
                                    }
                                    if(LoginActivity.LoginActivity!=null){
                                        LoginActivity.LoginActivity.finish();
                                    }
                                    if(ManageAccountActivity.ManageAccountActivity!=null){
                                        ManageAccountActivity.ManageAccountActivity.finish();
                                    }
                                    if(ManageAddressActivity.ManageAddressActivity!=null){
                                        ManageAddressActivity.ManageAddressActivity.finish();
                                    }
                                    if(MapActivity.MapActivity!=null){
                                        MapActivity.MapActivity.finish();
                                    }
                                    if(MyLocationPickerActivity.MyLocationPickerActivity!=null){
                                        MyLocationPickerActivity.MyLocationPickerActivity.finish();
                                    }
                                    if(MyOrdersActivity.MyOrdersActivity!=null){
                                        MyOrdersActivity.MyOrdersActivity.finish();
                                    }
                                    if(NoInternetActivity.NoInternetActivity!=null){
                                        NoInternetActivity.NoInternetActivity.finish();
                                    }
                                    if(NoServiceAvaliable.NoServiceAvaliable!=null){
                                        NoServiceAvaliable.NoServiceAvaliable.finish();
                                    }
                                    if(NotificationActivity.NotificationActivity!=null){
                                        NotificationActivity.NotificationActivity.finish();
                                    }
                                    if(OffersActivity.OffersActivity!=null){
                                        OffersActivity.OffersActivity.finish();
                                    }
                                    if(OrderSummaryActivity.OrderSummaryActivity!=null){
                                        OrderSummaryActivity.OrderSummaryActivity.finish();
                                    }
                                    if(OtpActivity.OtpActivity!=null){
                                        OtpActivity.OtpActivity.finish();
                                    }
                                    if(PaymentFailureActivity.PaymentFailureActivity!=null){
                                        PaymentFailureActivity.PaymentFailureActivity.finish();
                                    }
                                    if(PickAddressActivity.PickAddressActivity!=null){
                                        PickAddressActivity.PickAddressActivity.finish();
                                    }
                                    if(ProfileActivity.ProfileActivity!=null){
                                        ProfileActivity.ProfileActivity.finish();
                                    }
                                    if(RatingActivity.RatingActivity!=null){
                                        RatingActivity.RatingActivity.finish();
                                    }
                                    if(ReviewActivity.ReviewActivity!=null){
                                        ReviewActivity.ReviewActivity.finish();
                                    }
                                    if(SearchActivity.SearchActivity!=null){
                                        SearchActivity.SearchActivity.finish();
                                    }
                                    if(SearchAddressActivity.SearchAddressActivity!=null){
                                        SearchAddressActivity.SearchAddressActivity.finish();
                                    }
                                    if(SearchRestFoodActivity.SearchRestFoodActivity!=null){
                                        SearchRestFoodActivity.SearchRestFoodActivity.finish();
                                    }
                                    if(ShopFullViewActivity.ShopFullViewActivity!=null){
                                        ShopFullViewActivity.ShopFullViewActivity.finish();
                                    }
                                    if(SignupActivity.SignupActivity!=null){
                                        SignupActivity.SignupActivity.finish();
                                    }
                                    if(StoreDetailsActivity.StoreDetailsActivity1!=null){
                                        StoreDetailsActivity.StoreDetailsActivity1.finish();
                                    }
                                    if(ThankyouActivity.ThankyouActivity!=null){
                                        ThankyouActivity.ThankyouActivity.finish();
                                    }
                                    if(ViewOrderActivity.ViewOrderActivity!=null){
                                        ViewOrderActivity.ViewOrderActivity.finish();
                                    }
                                    if(WaitingActivity.WaitingActivity!=null){
                                        WaitingActivity.WaitingActivity.finish();
                                    }
                                    if(WebActivity.WebActivity!=null){
                                        WebActivity.WebActivity.finish();
                                    }
                                    if(WishlistActivity.WishlistActivity!=null){
                                        WishlistActivity.WishlistActivity.finish();
                                    }

                                    startActivity(new Intent(AccountSettingActivity.this,LoginActivity.class));

                                    finish();
                                }
                                else
                                {
                                    progressDialog.hide();
                                    Toast.makeText(AccountSettingActivity.this, msg, Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {

                        progressDialog.hide();
                        Toast.makeText(AccountSettingActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });





            }
        });


        //saved address

        binding.savedAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountSettingActivity.this,LocationActivity.class));
            }
        });

        //facebook connect
        binding.facebookConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.facebook.com/Capsico-India-103464132247247/"));
                startActivity(intent);
            }
        });
        //instagam connect
        binding.instagramConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/capsicoindia/"));
                startActivity(intent);
            }
        });




    }
}