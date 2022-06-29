package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import digi.coders.thecapsico.databinding.ActivityAddAddressBinding;
import digi.coders.thecapsico.databinding.ActivityAddressSearchBinding;
import digi.coders.thecapsico.databinding.ActivityCartBinding;
import digi.coders.thecapsico.helper.ShowProgress;

public class AddressSearchActivity extends AppCompatActivity {

    ActivityAddressSearchBinding binding;

    public static Activity AddressSearchActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddressSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AddressSearchActivity=this;

        //handle back
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //clear text
        binding.ivCroos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.etSearchProduct.setText("");
            }
        });

        //make focus to search bar
        binding.etSearchProduct.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(binding.etSearchProduct, InputMethodManager.SHOW_IMPLICIT);
        TextWatcher tw=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() >3) {
                    new LongOperation(s.toString()).execute();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        binding.etSearchProduct.addTextChangedListener(tw);

    }

    private class LongOperation  extends AsyncTask<String, Void, Void> {

        // Required initialization
        private String s;

        public LongOperation(String s) {
            this.s = s;
        }

        private String Content;
        private String Error = null;

        //private ProgressDialog Dialog = new ProgressDialog(SetLocationActivity.this);

        protected void onPreExecute() {

//            Dialog.setMessage("Please wait..");
//            Dialog.show();
//            Dialog.setCancelable(false);
//            Dialog.setCanceledOnTouchOutside(false);

            ShowProgress.getShowProgress(AddressSearchActivity.this).show();

        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {


            BufferedReader reader=null;

            // Send data
            try
            {
                // Defined URL  where to send data
                @SuppressLint("WrongThread") URL url = new URL("https://maps.googleapis.com/maps/api/place/autocomplete/json?input="+ s+"&types=establishment&location=26.846695,80.946167&radius=500&key=AIzaSyCouenACFFeOljVUTNe0LAkGH9QS7XyRO4");

                // Send POST data request
                URLConnection conn = url.openConnection();

                conn.setConnectTimeout(5000);//define connection timeout
                conn.setReadTimeout(5000);//define read timeout
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write( "" );
                wr.flush();

                // Get the server response
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    // Append server response in string
                    sb.append(line + " ");
                }

                // Append Server Response To Content String
                Content = sb.toString();

            }
            catch(Exception ex)
            {
                Error = ex.getMessage();
            }
            finally
            {
                try
                {
                    reader.close();
                }
                catch(Exception ex) {}
            }

            return null;
        }

        protected void onPostExecute(Void unused) {
            // NOTE: You can call UI Element here.

            // Close progress dialog
            // Dialog.dismiss();

            if (Error != null) {
                Toast.makeText(getApplicationContext(),"Error encountered",Toast.LENGTH_LONG).show();

            }
            else {

                try {

                    JSONObject jsonRootObject = new JSONObject(Content);
                    JSONArray jsonArray=jsonRootObject.getJSONArray("predictions");
                    String status=jsonRootObject.getString("status");
                    Log.e("ew",jsonRootObject.toString());
                    if(status.equalsIgnoreCase("ok")) {
                        ShowProgress.getShowProgress(AddressSearchActivity.this).hide();
                        String[] str = new String[jsonArray.length()];
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            str[i] = jsonObject.getString("description");
                            RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext());
                      /*      recyclerLoc.setLayoutManager(layoutManager1);
                            recyclerLoc.setHasFixedSize(true);
                            LocationAdapter locationAdapter = new LocationAdapter(getApplicationContext(), str);
                            recyclerLoc.setAdapter(locationAdapter);*/
//
                        }
                    }else{
                        //Toast.makeText(getApplicationContext(), "Data not Found !", Toast.LENGTH_LONG).show();

                    }

                    //the result is stored in string id. you can display it now

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                }

            }
        }

    }
}