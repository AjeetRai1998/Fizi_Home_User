package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androchef.happytimer.countdowntimer.CircularCountDownView;
import com.androchef.happytimer.countdowntimer.HappyTimer;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.adapter.SimpleProductAdapter;
import digi.coders.thecapsico.databinding.ActivityWaitingBinding;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.ShowProgress;
import digi.coders.thecapsico.model.MyOrder;
import digi.coders.thecapsico.model.Product;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.singletask.SingleTask;
import ng.max.slideview.SlideView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WaitingActivity extends AppCompatActivity {

    ActivityWaitingBinding binding;
    private SingleTask singleTask;
    private String orderId;
    private CountDownTimer timer;

    public static Activity WaitingActivity ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityWaitingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        WaitingActivity=this;
        singleTask=(SingleTask)getApplication();
        orderId=getIntent().getStringExtra("orderId");
        playSound();
        // 15 seconds timer
         /*timer=new CountDownTimer(17000, 1000) {

            public void onTick(long millisUntilFinished) {
                binding.timeTxt.setText(millisUntilFinished / 1000+"");
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                excecuteApi(2);

            }

        };
        timer.start();
*/

        //orderCancel
        binding.trackFood.setOnSlideCompleteListener(new SlideView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideView slideView) {
                excecuteApi(1);

            }
        });

        //binding.dynamicCountDownView.setSeparatorString(":");
        binding.dynamicCountDownView.setTimerTextColor(getResources().getColor(R.color.gray3));
        binding.dynamicCountDownView.setTimerTextColor(getResources().getColor(R.color.pure_black));
        binding.dynamicCountDownView.setTimerTextFormat(CircularCountDownView.TextFormat.HOUR_MINUTE_SECOND);
        binding.dynamicCountDownView.setStrokeThicknessBackground(10f);
//        binding.dynamicCountDownView.setTimerTextSeparatorColor(getResources().getColor(R.color.color_white));
/*        binding.dynamicCountDownView.setTimerTextSeparatorSize(15f);
        binding.dynamicCountDownView.setTimerTextSize(15f);*/
        //binding.dynamicCountDownView.setShowHour(true);
        /*binding.dynamicCountDownView.setShowMinutes(true);
        binding.dynamicCountDownView.setShowSeconds(true);*/
  //      binding.dynamicCountDownView.setShowSeparators(true);
        binding.dynamicCountDownView.setTimerTextIsBold(true);
    //    binding.dynamicCountDownView.setTimerTextSeparatorIsBold(true);
        binding.dynamicCountDownView.setTimerType(HappyTimer.Type.COUNT_DOWN);

        //Set timer text background as a rectangle

        //Set timer text background as a circle
//        dynamicCountDownView.setRoundedBackground()

        //set custom background for timer text
  //      dynamicCountDownView.customBackgroundDrawable =
    //            ContextCompat.getDrawable(this, R.drawable.bg_textview_count_down_circle)

        //Initialize Your Timer with seconds
        binding.dynamicCountDownView.initTimer(15);
        binding.cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog(WaitingActivity.this);
            }
        });

        binding.dynamicCountDownView.setOnTickListener(new HappyTimer.OnTickListener() {
            @Override
            public void onTick(int i, int i1) {
                Log.e("sds",i+"");
            }
            @Override
            public void onTimeUp()
            {

                //Log.e("timeap","time up");
                excecuteApi(2);

                Toast.makeText(WaitingActivity.this, "Time up", Toast.LENGTH_SHORT).show();

            }
        });
        binding.dynamicCountDownView.startTimer();
    }

    private void excecuteApi(int i) {
        String js=singleTask.getValue("user");
        Log.e("orer",orderId);
        User user=new Gson().fromJson(js,User.class);
        ShowProgress.getShowProgress(WaitingActivity.this).show();
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
                        mMediaPlayer.stop();
                        if(i==1) {
                            ShowProgress.getShowProgress(WaitingActivity.this).hide();
                            binding.dynamicCountDownView.stopTimer();
                            OrderSummaryActivity.orderId = orderId;
                            OrderSummaryActivity.timeForDetalay = 120000;
                            Intent in = new Intent(WaitingActivity.this, OrderSummaryActivity.class);
                            in.putExtra("sta", 1);
                            startActivity(in);
                            finish();
                            if(CheckOutActivity.checkOut!=null){
                                CheckOutActivity.checkOut.finish();
                            }


                        }
                        else {

//                            Intent in = new Intent(WaitingActivity.this, ThankyouActivity.class);
//                            in.putExtra("orderId", orderId);
//                            startActivity(in);
//                            finish();
                            ShowProgress.getShowProgress(WaitingActivity.this).hide();
                            OrderSummaryActivity.orderId = orderId;
                            OrderSummaryActivity.timeForDetalay = 120000;
                            Intent in = new Intent(WaitingActivity.this, OrderSummaryActivity.class);
                            in.putExtra("sta", 1);
                            startActivity(in);
                            finish();
                            if(CheckOutActivity.checkOut!=null){
                                CheckOutActivity.checkOut.finish();
                            }
                            if(binding.dynamicCountDownView!=null) {
                                binding.dynamicCountDownView.stopTimer();
                            }
                        }

                    }else {
                        ShowProgress.getShowProgress(WaitingActivity.this).hide();
                        Toast.makeText(WaitingActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    ShowProgress.getShowProgress(WaitingActivity.this).hide();
                    //Toast.makeText(HomeActivity.this, e.getMessage().toString()+"e", Toast.LENGTH_SHORT).show();
                }

                }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                ShowProgress.getShowProgress(WaitingActivity.this).hide();
            }
        });
    }

    @Override
    public void onBackPressed() {

    }

    MediaPlayer mMediaPlayer;
    public void playSound() {
        try {

            mMediaPlayer = new MediaPlayer();
            mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.ring);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.start();
//
//           Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            Ringtone r = RingtoneManager.getRingtone(mContext, alarmSound);
//            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

                ShowProgress.getShowProgress(WaitingActivity.this).show();
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
                                    ShowProgress.getShowProgress(WaitingActivity.this).hide();
                                    Toast.makeText(WaitingActivity.this, message, Toast.LENGTH_SHORT).show();
                                    binding.dynamicCountDownView.stopTimer();
                                    startActivity(new Intent(WaitingActivity.this,CancelOrderActivity.class));
                                    finish();
                                    if(CheckOutActivity.checkOut!=null){
                                        CheckOutActivity.checkOut.finish();
                                    }
                                    mMediaPlayer.stop();
                                    //  openBottom();

                                }

                                else
                                {
                                    Toast.makeText(WaitingActivity.this, message, Toast.LENGTH_SHORT).show();
                                    ShowProgress.getShowProgress(WaitingActivity.this).hide();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        ShowProgress.getShowProgress(WaitingActivity.this).hide();
                        Toast.makeText(WaitingActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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