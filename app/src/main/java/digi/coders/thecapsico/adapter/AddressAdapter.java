package digi.coders.thecapsico.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.activity.DeliveryLocationActivity;
import digi.coders.thecapsico.activity.LocationActivity;
import digi.coders.thecapsico.activity.MapActivity;
import digi.coders.thecapsico.activity.MapUtility;
import digi.coders.thecapsico.activity.MyLocationPickerActivity;
import digi.coders.thecapsico.databinding.AddressLayoutBinding;
import digi.coders.thecapsico.helper.Constraint;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.SharedPrefManagerLocation;
import digi.coders.thecapsico.helper.ShowProgress;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.model.UserAddress;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyHolder> {

    private int status;
    private List<UserAddress> addressList;
    private SingleTask singleTask;
    private Context ctx;
    GetPosition getPosition;
    public AddressAdapter(int status, List<UserAddress> addressList,SingleTask singleTask) {
        this.status = status;
        this.addressList = addressList;
        this.singleTask=singleTask;
    }

    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ctx=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.address_layout,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, @SuppressLint("RecyclerView") int position) {

        if(status==1)
        {
            UserAddress address=addressList.get(position);
            holder.binding.addressType.setText(address.getType());
            holder.binding.address.setText(address.getAddress());
            holder.binding.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ctx, MapActivity.class);
                    /*MyLocationPickerActivity.useAddress=address;
                    intent.putExtra(MapUtility.ADDRESS,address.getAddress());
                    intent.putExtra(MapUtility.LATITUDE, address.getLatitude());
                    intent.putExtra(MapUtility.LONGITUDE, address.getLongitude());
                    intent.putExtra("status",1);*/
                    ctx.startActivity(intent);
                }
            });

            holder.binding.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowProgress.getShowProgress(ctx).show();
                    String js=singleTask.getValue("user");
                    User user=new Gson().fromJson(js,User.class);
                    MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
                    Call<JsonArray> call=myApi.deleteAddress(address.getId(),user.getId());
                    call.enqueue(new Callback<JsonArray>() {
                        @Override
                        public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                            if(response.isSuccessful())
                            {
                                try {
                                    ShowProgress.getShowProgress(ctx).hide();
                                    JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                                    String res=jsonObject.getString("res");
                                    String msg=jsonObject.getString("message");
                                    if(res.equals("success"))
                                    {
                                        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
                                        addressList.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, addressList.size());
                                        if(DeliveryLocationActivity.refresh!=null){
                                            DeliveryLocationActivity.refresh.onRefresh();
                                        }
                                    }
                                    else
                                    {

                                        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
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
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPosition.position(v,position,address);
                    //Toast.makeText(ctx, position+"p", Toast.LENGTH_SHORT).show();
                }
            });
                    
        }
        else
        {
            holder.manageLayout.setVisibility(View.VISIBLE);
            //make gone to edit and delete button
        }
    }

    public void findPosition(GetPosition getPosition)
    {
        this.getPosition=getPosition;
    }

    public  interface  GetPosition
    {
        void position(View v,int position,UserAddress address);
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder
    {
        private LinearLayout manageLayout;
        AddressLayoutBinding binding;
        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            manageLayout=itemView.findViewById(R.id.manage);
            binding=AddressLayoutBinding.bind(itemView);

        }
    }
}
