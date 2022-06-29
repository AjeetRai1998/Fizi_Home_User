package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import digi.coders.thecapsico.BuildConfig;
import digi.coders.thecapsico.R;
import digi.coders.thecapsico.databinding.ActivityAboutUsBinding;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutUsActivity extends AppCompatActivity {

    ActivityAboutUsBinding binding;

    public static Activity AboutUsActivity;
    SingleTask singleTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAboutUsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AboutUsActivity=this;
        singleTask=(SingleTask)getApplication();
        //handle back
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        binding.linePrivacy.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://yourcapsico.com/Home/PrivacyPolicies"));
                        startActivity(intent);

                    }
                }
        );
        binding.lineTerms.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://yourcapsico.com/Home/TermsConditions"));
                        startActivity(intent);

                    }
                }
        );


        binding.version.setText("V."+BuildConfig.VERSION_NAME);
        getAbout();
    }

    private void getAbout() {
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        MyApi myApi=singleTask.getRetrofit1().create(MyApi.class);
        Call<JsonObject> call=myApi.getAbout("about");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful())
                {
                    try {
                        JSONObject jsonObject=new JSONObject(new Gson().toJson(response.body()));
                        String res=jsonObject.getString("res");
                        if(res.equals("success"))
                        {
                            JSONObject jsonObject1=jsonObject.getJSONObject("data");
                            String versionName=jsonObject1.getString("text");
                            byte[] data = Base64.decode(versionName, Base64.DEFAULT);
                            String text = new String(data, "UTF-8");

                            String content =
                                    "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"+
                                            "<html><head>"+
                                            "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />"+
                                            "</head><body>";

                            content += text + "</body></html>";

                            binding.about.setText(Html.fromHtml(text));

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }
}