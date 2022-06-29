package digi.coders.thecapsico.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import java.util.List;

import digi.coders.thecapsico.R;

public class OffersSliderAdapter extends PagerAdapter {

    List<String> dataList;
    Context ctx;

    public OffersSliderAdapter(Context ctx, List<String> dataList) {

        this.ctx = ctx;
        this.dataList = dataList;
    }

    private LayoutInflater layoutInflater;

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater=(LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.item_card_payment,container,false);
        ImageView img=view.findViewById(R.id.card_logo);
        //Picasso.get().load(Contants.SLIDER_URL +dataList.get(position)).placeholder(R.drawable.logo).into(img);
        //imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        container.addView (view);
        return view;
    }

    @Override
    public int getCount() {
        return 4;
        //return dataList.size();
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


