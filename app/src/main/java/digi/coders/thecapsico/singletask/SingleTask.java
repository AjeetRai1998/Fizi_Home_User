package digi.coders.thecapsico.singletask;

import android.app.Application;
import android.content.SharedPreferences;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SingleTask extends Application {
    private Retrofit retrofit,retrofit1;
    public static final String BASE_URL="https://hungerji.com/ApiTest/";
    public static final String BASE_URL1="https://hungerji.com/";
    public static final String MERCHANT_BASE_URL="https://hungerji.com/MerchantAPIs/";
    public static final String MAP_URL="https://maps.googleapis.com/maps/api/place/";
    private Retrofit merchantRetrofit;
    private Retrofit mapRetrofit;

    @Override
    public void onCreate() {
        super.onCreate();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        if(retrofit==null)
        {
            retrofit=new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        if(retrofit1==null)
        {
            retrofit1=new Retrofit.Builder()
                    .baseUrl(BASE_URL1)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        if(mapRetrofit==null)
        {
            mapRetrofit=new Retrofit.Builder()
                    .baseUrl(MAP_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        if(merchantRetrofit==null)
        {
            merchantRetrofit=new Retrofit.Builder()
                    .baseUrl(MERCHANT_BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

    }

    public Retrofit getMerchaneRetrofit()
    {
        return merchantRetrofit;
    }

    public Retrofit getRetrofit()
    {
        return retrofit;
    }
    public Retrofit getRetrofit1()
    {
        return retrofit1;
    }
    public Retrofit getMapRetrofit()
    {
        return mapRetrofit;
    }

    public SharedPreferences getSharedPreferene()
    {
        return getSharedPreferences("capsico_pref",MODE_PRIVATE);
    }
    public void addValue(String key, JSONObject jsonObject)
    {
        getSharedPreferene().edit().putString(key, String.valueOf(jsonObject)).commit();
    }
    public void removeUser(String key)
    {
        getSharedPreferene().edit().remove(key).commit();
    }
    public String getValue(String key)
    {
        return  getSharedPreferene().getString(key,null);
    }

}
