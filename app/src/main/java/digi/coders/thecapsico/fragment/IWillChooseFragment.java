package digi.coders.thecapsico.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.skydoves.elasticviews.ElasticButton;
import com.skydoves.elasticviews.ElasticLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.activity.CheckOutActivity;
import digi.coders.thecapsico.activity.StoreDetailsActivity;
import digi.coders.thecapsico.adapter.AddOnGroupAdapter;
import digi.coders.thecapsico.helper.Constraint;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.Refresh;
import digi.coders.thecapsico.helper.ShowProgress;
import digi.coders.thecapsico.model.AddOnGroup;
import digi.coders.thecapsico.model.AddOnProduct;
import digi.coders.thecapsico.model.Product;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class IWillChooseFragment extends BottomSheetDialogFragment implements Refresh {

    private Product product;
    private SingleTask singleTask;
    private String merchantId;
    private String qty;
    private int key;
    private double total1;

    static double total = 0;
    int lastCheckedPosition = -1;

    int quantity;

    TextView product_name;
    ElasticButton iwill_choose,repeat;

    public static Refresh refresh;
    String cart_id;
    TextView holder;
    LinearLayout cartControl;
    ElasticLayout cartAdd;
    String cust;
    public IWillChooseFragment(String cust,Product product, String qty, TextView holder,ElasticLayout cartAdd,LinearLayout cartControl,int key,String cart_id) {
        this.cust = cust;
        this.product = product;
        this.merchantId=merchantId;
        this.qty=qty;
        this.key=key;
        this.cart_id=cart_id;
        this.holder=holder;
        this.cartAdd=cartAdd;
        this.cartControl=cartControl;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.iwillchoose_layout, container, false);

        refresh=this;
        singleTask=(SingleTask)getActivity().getApplication();
        total1=Double.parseDouble(product.getSellPrice());

        product_name=v.findViewById(R.id.product_name);
        iwill_choose=v.findViewById(R.id.iwill_choose);
        repeat=v.findViewById(R.id.repeat);

        product_name.setText(product.getName());

        repeat.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         addToCart(product,"");
                    }
                }
        );
        iwill_choose.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        CartAddonFragment bottomSheet = new CartAddonFragment("",product,qty,holder,cartAdd,cartControl,0,cart_id);
//                        bottomSheet.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        bottomSheet.show(getChildFragmentManager(), "");

                    }
                }
        );
        return v;
    }

    @Override
    public void onRefresh() {

        dismiss();
    }
    private void addToCart(Product product, String c) {
//        ShowProgress.getShowProgress(getActivity()).show();
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit1().create(MyApi.class);

        Call<JsonObject> call = null;

        call = myApi.addCart(user.getId(), "Add","",product.getMerchant_id(), product.getId(), qty, product.getMrp(),product.getPrice(), product.getSellPrice(),product.getDiscount(), c, "yes", Constraint.addOnIDList.toString(),Constraint.addOnPriceList.toString(),cart_id);


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

//                            Toast.makeText(getActivity(), Constraint.addonId+", "+Constraint.addonPrice, Toast.LENGTH_SHORT).show();
                            if(holder!=null){
                                holder.setText(qty);
                            }
                            if(cartAdd!=null){
                                cartAdd.setVisibility(View.GONE);
                                cartControl.setVisibility(View.VISIBLE);
                            }
                            if(StoreDetailsActivity.refresh!=null) {
                                StoreDetailsActivity.refresh.onRefresh();
                            }
                            if(CheckOutActivity.refresh!=null) {
                                CheckOutActivity.refresh.onRefresh();
                            }
                            dismiss();
//                            ShowProgress.getShowProgress(getActivity()).hide();
                        } else if(res.equalsIgnoreCase("remove")) {
                            showAlertDialog(product);
                            dismiss();
//                            ShowProgress.getShowProgress(getActivity()).hide();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        dismiss();
//                        ShowProgress.getShowProgress(getActivity()).hide();
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dismiss();
//                ShowProgress.getShowProgress(getActivity()).hide();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showAlertDialog(Product product) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        AlertDialog dialog = alert.create();
        alert.setMessage("Are you sure you want to clear previous cart");
        alert.setTitle("Clear Cart");
        alert.setCancelable(false);
        alert.setIcon(getActivity().getResources().getDrawable(R.drawable.logo));
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                addToCart(product,"Clear");
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
    public void onResume() {
        super.onResume();
//        dismiss();
    }


}
