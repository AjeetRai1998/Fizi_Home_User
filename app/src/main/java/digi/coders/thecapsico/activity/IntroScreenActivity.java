package digi.coders.thecapsico.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.media.JetPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.button.MaterialButton;
import com.rd.PageIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.util.Timer;
import java.util.TimerTask;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.databinding.ActivityIntroScreenBinding;
import digi.coders.thecapsico.fragment.IntroScreenFirstFragment;
import digi.coders.thecapsico.fragment.IntroScreenSecondFragment;
import digi.coders.thecapsico.fragment.IntroScreenThirdFragment;
public class IntroScreenActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final int NUM_PAGES = 3;
    private PagerAdapter pagerAdapter;
    private static int currentPage = 0;
    private static int NUM_PAGE = 0;
    ActivityIntroScreenBinding binding;
    public static Activity IntroScreenActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        binding=ActivityIntroScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        IntroScreenActivity=this;
        //view pageer and page indicator
        pagerAdapter=new IntroScreenAdapter(getSupportFragmentManager());
        binding.pager.setAdapter(pagerAdapter);
        binding.pageIndicator.setViewPager(binding.pager);
        binding.pageIndicator.setCount(NUM_PAGES);
        binding.pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                binding.pageIndicator.setSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });
        NUM_PAGE =3;
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
               binding.pager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2000, 2000);

        //getautomatic mobile no

        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Auth.CREDENTIALS_API)
                .enableAutoManage(this,this)
                .build();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

        binding.mobileno.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    HintRequest hintRequest = new HintRequest.Builder()
                            .setPhoneNumberIdentifierSupported(true)
                            .build();

                    PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(mGoogleApiClient, hintRequest);
                    try {
                        startIntentSenderForResult(intent.getIntentSender(), 1008, null, 0, 0, 0, null);
                    } catch (IntentSender.SendIntentException e) {
                        Log.e("", "Could not start hint picker Intent", e);
                    }

                }
            }
        });


        //hable login
        binding.getOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!valid())
                {
                    startActivity(new Intent(IntroScreenActivity.this,OtpActivity.class));
                }
            }
        });





    }

    String mobileno;
    private boolean valid() {

        mobileno=binding.mobileno.getText().toString();
        if(mobileno.isEmpty())
        {
            binding.mobileno.setError("Please Enter your mobile no");
            binding.mobileno.requestFocus();
            return  false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public void onConnected(@Nullable @org.jetbrains.annotations.Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull @NotNull ConnectionResult connectionResult) {

    }


    private  class IntroScreenAdapter extends FragmentStatePagerAdapter
    {

        public IntroScreenAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Fragment fragment=null;
            if(position==0)
            {
                fragment=new IntroScreenFirstFragment();
            }
            else if(position==1)
            {
                fragment=new IntroScreenSecondFragment();

            }
            else if(position==2)
            {
                fragment=new IntroScreenThirdFragment();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1008:
                if (resultCode == RESULT_OK) {
                    Credential cred = data.getParcelableExtra(Credential.EXTRA_KEY);
//                    cred.getId====: ====+919*******
                    Log.e("cred.getId", cred.getId());
                    String phoneno=cred.getId();
                    binding.mobileno.setText(phoneno.substring(3));


                } else {
                    // Sim Card not found!
                    Log.e("cred.getId", "1008 else");

                    return;
                }


                break;
        }
    }
}