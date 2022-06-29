package digi.coders.thecapsico.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.databinding.ReviewsDesignBinding;
import digi.coders.thecapsico.helper.AppConstraints;
import digi.coders.thecapsico.model.Review;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyHolder> {

    private List<Review> reviewList;
    Context ctx;

    public ReviewAdapter(List<Review> reviewList, Context ctx) {
        this.reviewList = reviewList;
        this.ctx = ctx;
    }

    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_design,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {

        Review review=reviewList.get(position);
        holder.binding.name.setText(review.getUser_details().getName());
        if(!review.getRemark().equalsIgnoreCase("")) {
            holder.binding.review.setText(review.getRemark());
        }else{
            holder.binding.review.setVisibility(View.GONE);
        }
        if(!review.getItem_rating().equalsIgnoreCase("")) {
            String[] items = review.getItem_rating().split("CAPINDIA");
            String[] name;
            name=new String[items.length];
            for (int i = 0; i < items.length; i++) {
                String[] rat = items[i].split("CAPSICO");
                if(rat.length>1) {
                    name[i] = rat[0] + "CAPSICO" + rat[1];
                }else{
                    name[i] = rat[0];
                }

            }
            holder.binding.itemRating.setLayoutManager(new LinearLayoutManager(ctx));
            holder.binding.itemRating.setHasFixedSize(true);
            holder.binding.itemRating.setAdapter(new ItemListRatingAdapter(name,ctx));
        }else{
            holder.binding.itemRating.setVisibility(View.GONE);
        }

//        holder.binding.date.setText(review.getUpdatedAt());
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(review.getUpdatedAt());
            String dtt=new SimpleDateFormat("dd MMM, yyyy").format(date);
            holder.binding.date.setText(dtt);
        }catch (Exception e){

        }

        if(Double.parseDouble(review.getRating())<2){
                holder.binding.ratingone.setVisibility(View.VISIBLE);
                holder.binding.ratingtwo.setVisibility(View.GONE);
                holder.binding.ratingThree.setVisibility(View.GONE);
                holder.binding.ratingFour.setVisibility(View.GONE);
                holder.binding.ratingFive.setVisibility(View.GONE);
        }else   if(Double.parseDouble(review.getRating())<3){
            holder.binding.ratingtwo.setVisibility(View.VISIBLE);
            holder.binding.ratingone.setVisibility(View.GONE);
            holder.binding.ratingThree.setVisibility(View.GONE);
            holder.binding.ratingFour.setVisibility(View.GONE);
            holder.binding.ratingFive.setVisibility(View.GONE);
        }else   if(Double.parseDouble(review.getRating())<4){
            holder.binding.ratingThree.setVisibility(View.VISIBLE);
            holder.binding.ratingtwo.setVisibility(View.GONE);
            holder.binding.ratingone.setVisibility(View.GONE);
            holder.binding.ratingFour.setVisibility(View.GONE);
            holder.binding.ratingFive.setVisibility(View.GONE);
        }else   if(Double.parseDouble(review.getRating())<5){
            holder.binding.ratingFour.setVisibility(View.VISIBLE);
            holder.binding.ratingtwo.setVisibility(View.GONE);
            holder.binding.ratingThree.setVisibility(View.GONE);
            holder.binding.ratingone.setVisibility(View.GONE);
            holder.binding.ratingFive.setVisibility(View.GONE);
        }else   if(Double.parseDouble(review.getRating())<6){
            holder.binding.ratingFive.setVisibility(View.VISIBLE);
            holder.binding.ratingtwo.setVisibility(View.GONE);
            holder.binding.ratingThree.setVisibility(View.GONE);
            holder.binding.ratingFour.setVisibility(View.GONE);
            holder.binding.ratingone.setVisibility(View.GONE);
        }

        if(review.getTaste().equalsIgnoreCase("1")){
            Picasso.get().load(R.drawable.bad).into(holder.binding.tImg);
            holder.binding.tText.setText("Bad");
        }else if(review.getTaste().equalsIgnoreCase("2")){
            Picasso.get().load(R.drawable.ok).into(holder.binding.tImg);
            holder.binding.tText.setText("OK");
        }else if(review.getTaste().equalsIgnoreCase("3")){
            Picasso.get().load(R.drawable.happy).into(holder.binding.tImg);
            holder.binding.tText.setText("Good");
        }else if(review.getTaste().equalsIgnoreCase("4")){
            Picasso.get().load(R.drawable.great).into(holder.binding.tImg);
            holder.binding.tText.setText("Great");
        }

        if(review.getPacking().equalsIgnoreCase("1")){
            Picasso.get().load(R.drawable.bad).into(holder.binding.pImg);
            holder.binding.pText.setText("Bad");
        }else if(review.getPacking().equalsIgnoreCase("2")){
            Picasso.get().load(R.drawable.ok).into(holder.binding.pImg);
            holder.binding.pText.setText("OK");
        }else if(review.getPacking().equalsIgnoreCase("3")){
            Picasso.get().load(R.drawable.happy).into(holder.binding.pImg);
            holder.binding.pText.setText("Good");
        }else if(review.getPacking().equalsIgnoreCase("4")){
            Picasso.get().load(R.drawable.great).into(holder.binding.pImg);
            holder.binding.pText.setText("Great");
        }

        if(review.getQuantity().equalsIgnoreCase("1")){
            Picasso.get().load(R.drawable.bad).into(holder.binding.qImg);
            holder.binding.qText.setText("Bad");
        }else if(review.getQuantity().equalsIgnoreCase("2")){
            Picasso.get().load(R.drawable.ok).into(holder.binding.qImg);
            holder.binding.qText.setText("OK");
        }else if(review.getQuantity().equalsIgnoreCase("3")){
            Picasso.get().load(R.drawable.happy).into(holder.binding.qImg);
            holder.binding.qText.setText("Good");
        }else if(review.getQuantity().equalsIgnoreCase("4")){
            Picasso.get().load(R.drawable.great).into(holder.binding.qImg);
            holder.binding.qText.setText("Great");
        }

        if(review.getHygine().equalsIgnoreCase("1")){
            Picasso.get().load(R.drawable.bad).into(holder.binding.hImg);
            holder.binding.hText.setText("Bad");
        }else if(review.getHygine().equalsIgnoreCase("2")){
            Picasso.get().load(R.drawable.ok).into(holder.binding.hImg);
            holder.binding.hText.setText("OK");
        }else if(review.getHygine().equalsIgnoreCase("3")){
            Picasso.get().load(R.drawable.happy).into(holder.binding.hImg);
            holder.binding.hText.setText("Good");
        }else if(review.getHygine().equalsIgnoreCase("4")){
            Picasso.get().load(R.drawable.great).into(holder.binding.hImg);
            holder.binding.hText.setText("Great");
        }
//        Picasso.get().load(AppConstraints.BASE_URL+AppConstraints.USER+review.getUser_details().getIcon())
//                .placeholder(R.drawable.placeholder).into(holder.binding.icon);
//



    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        ReviewsDesignBinding binding;
        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding=ReviewsDesignBinding.bind(itemView);
        }
    }
}
