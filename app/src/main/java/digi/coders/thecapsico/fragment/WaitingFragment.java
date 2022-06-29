package digi.coders.thecapsico.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.skydoves.elasticviews.ElasticButton;
import com.skydoves.elasticviews.ElasticLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.activity.CancelOrderActivity;
import digi.coders.thecapsico.activity.CheckOutActivity;
import digi.coders.thecapsico.activity.OrderSummaryActivity;
import digi.coders.thecapsico.activity.StoreDetailsActivity;
import digi.coders.thecapsico.activity.WaitingActivity;
import digi.coders.thecapsico.databinding.WaitingLayoutBinding;
import digi.coders.thecapsico.helper.Constraint;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.Refresh;
import digi.coders.thecapsico.helper.ShowProgress;
import digi.coders.thecapsico.model.Product;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.singletask.SingleTask;
import ng.max.slideview.SlideView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WaitingFragment extends BottomSheetDialogFragment implements Refresh {

    WaitingLayoutBinding binding;
    private SingleTask singleTask;
    public static String paymentMethod="",amount="",walletAmount="";
    public static String orderId;
    private CountDownTimer timer;
    long timeForDetalay=15000;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=WaitingLayoutBinding.inflate(inflater,container,false);
        singleTask=(SingleTask)getActivity().getApplication();
//        playSound();
        setCancelable(false);

        binding.progressCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog(getActivity());
            }
        });


        binding.progressNotCancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        excecuteApi(1);
                    }
                }
        );

        if(paymentMethod.equalsIgnoreCase("cod")){
            binding.imgMethod.setImageResource(R.drawable.timer_img2);
            binding.txtMethod.setText("Cash on delivery");
            binding.txtDesc.setText("Pay \u20b9"+amount+" at the time of delivery");
            binding.txtDesc.setVisibility(View.VISIBLE);
        }else  if(paymentMethod.equalsIgnoreCase("wallet")){
            binding.imgMethod.setImageResource(R.drawable.timer_img2);
            binding.txtMethod.setText("Wallet");
            binding.txtDesc.setText("Pay \u20b9"+amount+" at the time of delivery");
            binding.txtDesc.setVisibility(View.GONE);
        }else  if(paymentMethod.equalsIgnoreCase("Wallet+ONLINE")){
            binding.imgMethod.setImageResource(R.drawable.timer_img2);
            binding.txtMethod.setText("Wallet+ONLINE");
            binding.txtDesc.setText("Pay \u20b9"+amount+" at the time of delivery");
            binding.txtDesc.setVisibility(View.GONE);
        }else  if(paymentMethod.equalsIgnoreCase("wallet+cod")){
            binding.imgMethod.setImageResource(R.drawable.timer_img2);
            binding.txtMethod.setText("Wallet+COD");
            binding.txtDesc.setText("Pay \u20b9"+(Double.parseDouble(amount)-Double.parseDouble(walletAmount))+" at the time of delivery");
            binding.txtDesc.setVisibility(View.VISIBLE);
        }else{
            binding.imgMethod.setImageResource(R.drawable.timer_img1);
            binding.txtMethod.setText("Online Payment");
            binding.txtDesc.setVisibility(View.GONE);
        }

        timer= new CountDownTimer(timeForDetalay, 1000) {
            public void onTick(long millisUntilFinished) {
                timeForDetalay=millisUntilFinished;
                binding.customBar.setProgressPercentage(((15000 - millisUntilFinished) / 150), true);
//                Toast.makeText(getApplicationContext(),((180000-millisUntilFinished)/1800)+"",Toast.LENGTH_LONG).show();
            }

            public void onFinish() {
                binding.customBar.setProgressPercentage(100, true);
                timeForDetalay=15000;
                timer.cancel();
                excecuteApi(2);

            }
        };
        timer.start();
        return binding.getRoot();
    }

    @Override
    public void onRefresh() {

        dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();
//        dismiss();
    }


    private void excecuteApi(int i) {
        String js=singleTask.getValue("user");
        Log.e("orer",orderId);
        User user=new Gson().fromJson(js,User.class);
        ShowProgress.getShowProgress(getActivity()).show();
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call=myApi.assignOrder(user.getId(),orderId);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                try {

                    Log.i("assign", response.body().toString());

                    JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    String status= jsonObject.getString("res");
                    if (status.equalsIgnoreCase("success")){
//                        setCancelable(true);
//                        mMediaPlayer.stop();
                        if(i==1) {
                            ShowProgress.getShowProgress(getActivity()).hide();

                            OrderSummaryActivity.orderId = orderId;
                            OrderSummaryActivity.timeForDetalay = 120000;
                            Intent in = new Intent(getActivity(), OrderSummaryActivity.class);
                            in.putExtra("sta", 1);
                            startActivity(in);
                            timer.cancel();
                            if(CheckOutActivity.CheckOutActivity1!=null){
                                CheckOutActivity.CheckOutActivity1.finish();
                            }
                            dismiss();


                        }
                        else {

//                            Intent in = new Intent(getActivity(), ThankyouActivity.class);
//                            in.putExtra("orderId", orderId);
//                            startActivity(in);
//                            finish();
                            ShowProgress.getShowProgress(getActivity()).hide();
                            OrderSummaryActivity.orderId = orderId;
                            OrderSummaryActivity.timeForDetalay = 120000;
                            Intent in = new Intent(getActivity(), OrderSummaryActivity.class);
                            in.putExtra("sta", 1);
                            startActivity(in);

                            timer.cancel();
                            if(CheckOutActivity.CheckOutActivity1!=null){
                                CheckOutActivity.CheckOutActivity1.finish();
                            }
                            dismiss();

                        }

                    }else {
                        ShowProgress.getShowProgress(getActivity()).hide();
                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    ShowProgress.getShowProgress(getActivity()).hide();
                    //Toast.makeText(HomeActivity.this, e.getMessage().toString()+"e", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                ShowProgress.getShowProgress(getActivity()).hide();
            }
        });
    }
//    MediaPlayer mMediaPlayer;
//    public void playSound() {
//        try {
//
//            mMediaPlayer = new MediaPlayer();
//            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.ring);
//            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            mMediaPlayer.start();
////
////           Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
////            Ringtone r = RingtoneManager.getRingtone(mContext, alarmSound);
////            r.play();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    private void showAlertDialog(Context ctx) {
        AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
        AlertDialog dialog = alert.create();
        alert.setMessage("Are you sure that you want to cancel this order?");
        alert.setTitle("Cancel Order");
        alert.setCancelable(false);
//        alert.setIcon(ctx.getResources().getDrawable(R.drawable.logo));
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ShowProgress.getShowProgress(getActivity()).show();
                String js=singleTask.getValue("user");
                Log.e("orer",orderId);
                User user=new Gson().fromJson(js,User.class);
                MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
                Call<JsonArray> call=myApi.cancelOrder(user.getId(),orderId);
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
                                    ShowProgress.getShowProgress(getActivity()).hide();
                                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                                    startActivity(new Intent(getActivity(), CancelOrderActivity.class));

                                    timer.cancel();
                                    dismiss();
                                    if(CheckOutActivity.CheckOutActivity1!=null){
                                        CheckOutActivity.CheckOutActivity1.finish();
                                    }
//                                    mMediaPlayer.stop();
                                    //  openBottom();

                                }

                                else
                                {
                                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                    ShowProgress.getShowProgress(getActivity()).hide();
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
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

}
