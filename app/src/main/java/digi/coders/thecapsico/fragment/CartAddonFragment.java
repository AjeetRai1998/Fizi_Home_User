package digi.coders.thecapsico.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.skydoves.elasticviews.ElasticButton;
import com.skydoves.elasticviews.ElasticLayout;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.activity.CheckOutActivity;
import digi.coders.thecapsico.activity.ItemDetailsActivity;
import digi.coders.thecapsico.activity.StoreDetailsActivity;
import digi.coders.thecapsico.adapter.AddOnAdapter;
import digi.coders.thecapsico.adapter.AddOnGroupAdapter;
import digi.coders.thecapsico.adapter.SimpleProductAdapter;
import digi.coders.thecapsico.databinding.AddOnLayoutBinding;
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


public class CartAddonFragment extends BottomSheetDialogFragment implements Refresh {
    ElasticButton done;
    private Product product;
    private SingleTask singleTask;
    private String merchantId;
    private String qty;
    private int key;
    private List<AddOnProduct> addOnProductList;
//    private double total1;
    List<String> idList=new ArrayList<>();
    List<String> priceList=new ArrayList<>();
    List<String> singleIdList = new ArrayList<>();
    List<String> singlePriceList = new ArrayList<>();
    public static List<List<String>> parentList = new ArrayList<>();
    public static List<List<String>> parentIDList = new ArrayList<>();
    public static List<List<String>> parentPriceList = new ArrayList<>();
    String[] idArray = new String[idList.size()];
    String specialInstrcution = "";
    static double total = 0;
    int lastCheckedPosition = -1;

    int quantity;
    TextView product_name,total_amount,sel_name;
    RecyclerView addOnItemList;
    ElasticLayout add_to_cart;
    public static ArrayList<String> name=new ArrayList<>();
    public static ArrayList<String> amount=new ArrayList<>();
    public static ArrayList<String> id=new ArrayList<>();
    public static String mname="";
    public static Refresh refresh;
    String cart_id;
    TextView holder;
    ImageView type;
    LinearLayout cartControl;
    ElasticLayout cartAdd;


    String cust;
    public CartAddonFragment(String cust,Product product, String qty, TextView holder,ElasticLayout cartAdd,LinearLayout cartControl,int key, String cart_id) {
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
        View v = inflater.inflate(R.layout.adaon_layout, container, false);

        refresh=this;
        name.clear();
        amount.clear();
        Constraint.addOnIDList.clear();
        Constraint.addOnPriceList.clear();
        id.clear();
        singleTask=(SingleTask)getActivity().getApplication();
//        total1=Double.parseDouble(product.getSellPrice());

        addOnItemList=v.findViewById(R.id.addOnItemList);
        product_name=v.findViewById(R.id.product_name);
        add_to_cart=v.findViewById(R.id.add_to_cart);
        total_amount=v.findViewById(R.id.total_amount);
        sel_name=v.findViewById(R.id.sel_name);
        type=v.findViewById(R.id.type);

        product_name.setText(product.getName());
        total_amount.setText("Total Amount \u20b9"+product.getSellPrice());
        if(product.getMenu_type().equalsIgnoreCase("Veg")){
            Picasso.get().load(R.drawable.vegdot).into(type);

        }else{
            Picasso.get().load(R.drawable.non_veg).into(type);
        }
        loadAddOnItem();
        add_to_cart.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        if(name.size()!=0) {
                            addToCart(product, "");
//                        }else{
//                            Toast.makeText(getActivity(), "Please Select Add on Items !", Toast.LENGTH_SHORT).show();
//                        }
                    }
                }
        );
        return v;
    }

    @Override
    public void onRefresh() {
        Constraint.addOnPriceList.clear();
        Constraint.addOnIDList.clear();
        String s="";
        double a=0;
        for(int i=0;i<parentList.size();i++) {
            for(int j=0;j<parentList.get(i).size();j++) {
                if(!parentList.get(i).get(j).equalsIgnoreCase("")) {
                    if (j == 0) {
                        if (s.equalsIgnoreCase("")) {
                            Constraint.addOnPriceList.add(parentPriceList.get(i).get(j));
                            Constraint.addOnIDList.add(parentIDList.get(i).get(j));
                            a = Double.parseDouble(parentPriceList.get(i).get(j));
                            s = parentList.get(i).get(j);
                        } else {
                            Constraint.addOnPriceList.add(parentPriceList.get(i).get(j));
                            Constraint.addOnIDList.add(parentIDList.get(i).get(j));
                            a = a+Double.parseDouble(parentPriceList.get(i).get(j));
                            s = s + "," + parentList.get(i).get(j);
                        }
                    } else {
                        if (s.equalsIgnoreCase("")) {
                            Constraint.addOnPriceList.add(parentPriceList.get(i).get(j));
                            Constraint.addOnIDList.add(parentIDList.get(i).get(j));
                            a = Double.parseDouble(parentPriceList.get(i).get(j));
                            s = parentList.get(i).get(j);
                        } else {
                            Constraint.addOnPriceList.add(parentPriceList.get(i).get(j));
                            Constraint.addOnIDList.add(parentIDList.get(i).get(j));
                            a =a+ Double.parseDouble(parentPriceList.get(i).get(j));
                            s = s + "," + parentList.get(i).get(j);
                        }
                    }
                }
            }
        }

        a=a+Double.parseDouble(product.getSellPrice());
        sel_name.setText(s);
        total_amount.setText("Total Amount \u20b9"+a);
    }
    private void loadAddOnItem() {
        ShowProgress.getShowProgress(getActivity()).show();
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call=myApi.getAddOnProductList(user.getId(),product.getMerchant_id(),product.getId());
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
                        Log.e("sdsd",response.toString());
                        if(res.equals("success"))
                        {
                            ShowProgress.getShowProgress(getActivity()).hide();
                            JSONArray jsonArray1 = jsonObject.getJSONArray("data");

                            ArrayList<AddOnGroup> groupList = new ArrayList<>();
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                AddOnGroup group = new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), AddOnGroup.class);
                                groupList.add(group);

                            }
                            addOnItemList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                            //AddOnAdapter adapter=new AddOnAdapter(addOnProductList);
                            AddOnGroupAdapter adapter = new AddOnGroupAdapter(groupList);
//                            handleGroupAdapteter(adapter);
                            addOnItemList.setAdapter(adapter);
                        }
                        else
                        {
                            ShowProgress.getShowProgress(getActivity()).hide();
                            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                ShowProgress.getShowProgress(getActivity()).hide();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void addToCart(Product product, String c) {
//
//        Constraint.addOnPriceList.addAll(amount);
//        Constraint.addOnIDList.addAll(id);
//        ShowProgress.getShowProgress(getActivity()).show();
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit1().create(MyApi.class);

        Call<JsonObject> call = null;

        call = myApi.addCart(user.getId(), "Add",cust,product.getMerchant_id(), product.getId(), qty, product.getMrp(),product.getPrice(), product.getSellPrice(),product.getDiscount(), c,"no", Constraint.addOnIDList.toString(),Constraint.addOnPriceList.toString(),cart_id);


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
                            if(IWillChooseFragment.refresh!=null) {
                                IWillChooseFragment.refresh.onRefresh();
                            }

                            if(CheckOutActivity.refresh!=null) {
                                CheckOutActivity.refresh.onRefresh();
                            }

                            name.clear();
                            amount.clear();
                            Constraint.addOnIDList.clear();
                            Constraint.addOnPriceList.clear();
                            id.clear();
                            parentList.clear();
                        } else if(res.equalsIgnoreCase("remove")) {
                            showAlertDialog(product);
                        }
//                        Toast.makeText(getActivity(), "Data Not Added", Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                dismiss();
                ShowProgress.getShowProgress(getActivity()).hide();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dismiss();
                ShowProgress.getShowProgress(getActivity()).hide();
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
                CheckOutActivity.order=null;
                CheckOutActivity.couponCode=null;
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

//    private void refelectPrice(double to, int s) {
//        Log.e("df", to + "");
//        if (s == 1) {
//            double tot = total1 + to;
//            total1 = tot;
//            Log.e("dsd", total1 + "" + to);
//            Log.e("dsd", tot + "");
//           total_amount.setText("Total Amount \u20b9" + tot);
//        } else {
//            double tot = total1 - to;
//            total1 = tot;
//            Log.e("dsd", total1 + "" + to);
//            total_amount.setText("Total Amount \u20b9" + tot);
//        }
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(IWillChooseFragment.refresh!=null) {
            IWillChooseFragment.refresh.onRefresh();
        }

        name.clear();
        amount.clear();
        Constraint.addOnIDList.clear();
        Constraint.addOnPriceList.clear();
        id.clear();
        parentList.clear();
    }

    @Override
    public void onDismiss(@NonNull @NotNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if(IWillChooseFragment.refresh!=null) {
            IWillChooseFragment.refresh.onRefresh();
        }

        name.clear();
        amount.clear();
        Constraint.addOnIDList.clear();
        Constraint.addOnPriceList.clear();
        id.clear();
        parentList.clear();
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        View bottomSheet=null;
        if (dialog != null) {
             bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
            bottomSheet.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        View view = getView();
        View finalBottomSheet = bottomSheet;
        view.post(() -> {
            View parent = (View) view.getParent();
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) (parent).getLayoutParams();
            CoordinatorLayout.Behavior behavior = params.getBehavior();
            BottomSheetBehavior bottomSheetBehavior = (BottomSheetBehavior) behavior;
            bottomSheetBehavior.setPeekHeight(view.getMeasuredHeight());
            ((View) finalBottomSheet.getParent()).setBackgroundColor(Color.TRANSPARENT);

        });
    }


}
