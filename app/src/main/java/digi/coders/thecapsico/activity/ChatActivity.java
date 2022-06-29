package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.adapter.ChatTicketAdapter;
import digi.coders.thecapsico.databinding.ActivityChatBinding;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.ShowProgress;
import digi.coders.thecapsico.model.ChatTicket;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    private SingleTask singleTask;
    private List<ChatTicket> chatTicketList;
    public static Activity ChatActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ChatActivity=this;
        singleTask=(SingleTask)getApplication();
        //handle back
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //load Ticket List

        loadTicketList();

        //chat with us
        binding.chatHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent in=new Intent(ChatActivity.this,WebActivity.class);
            in.putExtra("key",1);
            startActivity(in);
            }
        });

        binding.createNewToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatActivity.this,CreateNewChatTokenActivity.class));
            }
        });
    }

    private void loadTicketList() {
        ShowProgress.getShowProgress(ChatActivity.this).show();
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call=myApi.getMyToken(user.getId());
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
                        if(res.equals("success")) {
                            ShowProgress.getShowProgress(ChatActivity.this).hide();
                            JSONArray jsonArray1=jsonObject.getJSONArray("data");
                            chatTicketList=new ArrayList<>();
                            for(int i=0;i<jsonArray1.length();i++)
                            {
                                ChatTicket chatTicket=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),ChatTicket.class);
                                chatTicketList.add(chatTicket);
                            }
                            binding.ticketList.setLayoutManager(new LinearLayoutManager(ChatActivity.this,LinearLayoutManager.VERTICAL,false));
                            binding.ticketList.setAdapter(new ChatTicketAdapter(chatTicketList));
                        }
                        else
                        {
                            ShowProgress.getShowProgress(ChatActivity.this).hide();
                            Toast.makeText(ChatActivity.this, msg, Toast.LENGTH_SHORT).show();
                            binding.lineNoData.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                ShowProgress.getShowProgress(ChatActivity.this).hide();
                Toast.makeText(ChatActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}