package digi.coders.thecapsico.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.animation.content.Content;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.List;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.activity.MapActivity;
import digi.coders.thecapsico.activity.SearchAddressActivity;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.model.AddressResponse.PredictionsItem;
import digi.coders.thecapsico.model.AddressResponse.ResponsePlace;
import digi.coders.thecapsico.model.LatLongRespons.ResponseLatLong;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlacesListAdapter extends RecyclerView.Adapter<PlacesListAdapter.MyHolder> {


    List<PredictionsItem> list;
    Context ctx;

    private SingleTask singleTask;
    public PlacesListAdapter(List<PredictionsItem> list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
        singleTask = (SingleTask) ctx.getApplicationContext();
    }

    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.place_list_item,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {

        holder.city.setText(list.get(position).getStructuredFormatting().getMainText());
        holder.desc.setText(list.get(position).getDescription());

        holder.cardList.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getLatLong(list.get(position));
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        LinearLayout cardList;
        TextView city,desc;
        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            cardList=itemView.findViewById(R.id.cardList);
            city=itemView.findViewById(R.id.city);
            desc =itemView.findViewById(R.id.desc);
        }
    }

    private void getLatLong(PredictionsItem predictionsItem) {
        MyApi myApi=singleTask.getMapRetrofit().create(MyApi.class);
        Call<ResponseLatLong> call=myApi.getLatLong(predictionsItem.getPlaceId());
        call.enqueue(new Callback<ResponseLatLong>() {
            @Override
            public void onResponse(Call<ResponseLatLong> call, Response<ResponseLatLong> response) {
                if(response.isSuccessful())
                {
                    try {

                        if(response.body().getResult().getGeometry().getLocation()!=null) {
                            MapActivity.seLocation=response.body().getResult().getGeometry().getLocation();
                            MapActivity.predictionsItem=predictionsItem;
                            SearchAddressActivity.refresh.onRefresh();
                            MapActivity.refresh.onRefresh();
                        }else{
                            Toast.makeText(ctx, "No Data Found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Toast.makeText(ctx, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseLatLong> call, Throwable t) {
//                Toast.makeText(ctx, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
