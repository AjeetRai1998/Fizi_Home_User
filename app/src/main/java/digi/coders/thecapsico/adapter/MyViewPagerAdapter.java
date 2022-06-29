package digi.coders.thecapsico.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.helper.AppConstraints;


public class MyViewPagerAdapter extends PagerAdapter {

    Context ctx;
    List<String> str;
    int i;
    public MyViewPagerAdapter(List<String> str, Context ctx,int i) {
        this.str = str;
        this.i = i;
        this.ctx = ctx;
    }

    private LayoutInflater layoutInflater;
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view ;
        if(i==0) {
            view = layoutInflater.inflate(R.layout.view_pager_layout, container, false);
        }else{
            view = layoutInflater.inflate(R.layout.slider_layout2, container, false);
        }


        if(i==0) {
            ZoomageView img=view.findViewById(R.id.image);
            Picasso.get()
                    .load(AppConstraints.BASE_URL + str.get(position))
                    .into(img);

        }else{
            ImageView img=view.findViewById(R.id.image);
            Picasso.get()
                    .load(AppConstraints.BASE_URL + str.get(position)).placeholder(R.drawable.placeholder)
                    .into(img);
        }
            container.addView(view);

        return view;
    }


    @Override
    public int getCount() {

            return str.size();


    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
