package com.kwu.cointwebtoon;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.kwu.cointwebtoon.DataStructure.CustomBitmapPool;
import com.kwu.cointwebtoon.DataStructure.Webtoon;
import com.kwu.cointwebtoon.DataStructure.Weekday_ListItem;
import com.kwu.cointwebtoon.Views.CircularView;
import com.kwu.cointwebtoon.databinding.WeekdayItemBinding;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class WeekdayAdapter extends BaseSwipeAdapter {
    private Context mContext;
    private ArrayList<Webtoon> webtoons;
    private WeekdayItemBinding binding;


    private ListView mListView;
    private COINT_SQLiteManager manager;


    public WeekdayAdapter(ListView listView, Weekday_ListItem listItem, Context context) {
        webtoons = listItem.getList();
        mContext = context;
        mListView = listView;
        manager = COINT_SQLiteManager.getInstance(mContext);
    }

    @Override
    public int getCount() {
        return webtoons.size();
    }

    @Override
    public Object getItem(int position) {
        return webtoons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }
    @Override
    public View generateView(final int position, final ViewGroup parent) {
        CircularView v = (CircularView) LayoutInflater.from(mContext).inflate(R.layout.weekday_item, null);
        v.setParentHeight(mListView.getHeight());
        final SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(400).delay(100).playOn(layout.findViewById(R.id.btn_my));
            }
        });
        v.findViewById(R.id.week_my).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //해당 웹툰 MY웹툰으로 설정/해제
                int position  = (int)v.getTag();
                Webtoon item = (Webtoon)getItem(position);

                String result = manager.updateMyWebtoon(String.valueOf(item.getId()));

                if(result.equals("마이 웹툰 설정")){
                    item.setIs_mine(true);
                    YoYo.with(Techniques.Flash).duration(500).delay(100).playOn(v);
                    v.findViewById(R.id.btn_my).setBackgroundResource(R.drawable.my_star_active);
                    swipeLayout.findViewById(R.id.tv_my).setVisibility(View.VISIBLE);
                    swipeLayout.setBackgroundResource(R.drawable.week_background_my);
                }else if(result.equals("마이 웹툰 해제")){
                    item.setIs_mine(false);
                    YoYo.with(Techniques.Wobble).duration(500).delay(100).playOn(v);
                    v.findViewById(R.id.btn_my).setBackgroundResource(R.drawable.my_star_unactive);
                    swipeLayout.findViewById(R.id.tv_my).setVisibility(View.GONE);
                    swipeLayout.setBackgroundResource(0);
                }

            }
        });
        v.findViewById(R.id.btn_my).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //해당 웹툰 MY웹툰으로 설정/해제
                int position  = (int)v.getTag();
                Webtoon item = (Webtoon)getItem(position);

                String result = manager.updateMyWebtoon(String.valueOf(item.getId()));

                if(result.equals("마이 웹툰 설정")){
                    item.setIs_mine(true);
                    YoYo.with(Techniques.Flash).duration(500).delay(100).playOn(v);
                    v.setBackgroundResource(R.drawable.my_star_active);
                    swipeLayout.findViewById(R.id.tv_my).setVisibility(View.VISIBLE);
                    swipeLayout.setBackgroundResource(R.drawable.week_background_my);
                }else if(result.equals("마이 웹툰 해제")){
                    item.setIs_mine(false);
                    YoYo.with(Techniques.Wobble).duration(500).delay(100).playOn(v);
                    v.setBackgroundResource(R.drawable.my_star_unactive);
                    swipeLayout.findViewById(R.id.tv_my).setVisibility(View.GONE);
                    swipeLayout.setBackgroundResource(0);
                }

            }
        });
        return v;
    }
    @Override
    public void fillValues(final int position, View convertView) {

        binding = DataBindingUtil.bind(convertView);

        binding.btnMy.setTag(position);
        binding.weekMy.setTag(position);
        convertView.findViewById(R.id.week_item).setOnClickListener(new View.OnClickListener(){
            //웹툰연결
            @Override
            public void onClick(View v) {
                // get item
                Webtoon target = webtoons.get(position);
                Intent episodeIntent = new Intent(mContext, EpisodeActivity.class);
                episodeIntent.putExtra("id", target.getId());
                episodeIntent.putExtra("toontype", target.getToonType());
                episodeIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                mContext.startActivity(episodeIntent);
            }
        });

        Webtoon currentItem = webtoons.get(position);
        Glide.with(mContext)
                .load(currentItem.getThumbURL())
                .centerCrop()
                .bitmapTransform(new CropCircleTransformation(new CustomBitmapPool()))
                .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                .placeholder(R.drawable.week_placeholder)
                .into(binding.ivThumbnail);
        binding.tvTitle.setText(currentItem.getTitle());
        binding.tvTitle.setSelected(true);
        binding.tvStarScore.setText("★" + String.valueOf(currentItem.getStarScore()));
        binding.tvArtist.setText(currentItem.getArtist());

        if (currentItem.isMine()){
            //마이 웹툰 여부에 따라 별 아이콘 다르게 설정
            binding.btnMy.setBackgroundResource(R.drawable.my_star_active);
            binding.tvMy.setVisibility(View.VISIBLE);
            binding.swipe.setBackgroundResource(R.drawable.week_background_my);
        }
        else{
            binding.btnMy.setBackgroundResource(R.drawable.my_star_unactive);
            binding.tvMy.setVisibility(View.GONE);
            binding.swipe.setBackgroundResource(0);
        }

        //Update상태 아이콘 표시
        if(currentItem.isUpdated() == 0){
            binding.tvUpdateIcon.setVisibility(View.GONE);
        }
        else if(currentItem.isUpdated() == 1){
            //Updated
            setToonIcon(binding.tvUpdateIcon, R.drawable.week_icon_update, "UP", "#fc6c00");
        }
        else if(currentItem.isUpdated() == 2){
            //Dormant
            setToonIcon(binding.tvUpdateIcon, R.drawable.week_icon_dormant, "휴재", "#ffffff");
        }

        //성인 여부 표시
        if(currentItem.isAdult()){
            setToonIcon(binding.tvAdultIcon, R.drawable.main_icon_adult, "성인", "#ffffff");
        }
        else{
            binding.tvAdultIcon.setVisibility(View.GONE);
        }

        //Webtoon type 아이콘 표시
        switch (currentItem.getToonType()){
            case 'C':
            {
                //컷툰
                setToonIcon(binding.tvToontypeIcon, R.drawable.week_icon_cuttoon, "컷툰", "#28dcbe");
                break;
            }
            case 'M':
            {
                //모션툰
                setToonIcon(binding.tvToontypeIcon, R.drawable.week_icon_motiontoon, "모션", "#6d1daf");
                break;
            }
            case 'S':
            {
                //스마트툰
                setToonIcon(binding.tvToontypeIcon, R.drawable.week_icon_smarttoon, "스마트", "#0050b4");
                break;
            }
            case  'G':
            default:
            {
                //일반
                binding.tvToontypeIcon.setVisibility(View.GONE);
                break;
            }

        }

    }

    public void setToonIcon(TextView icon, int resId, String text, String textColorString){
        icon.setVisibility(View.VISIBLE);
        icon.setBackgroundResource(resId);
        icon.setText(text);
        icon.setTextColor(Color.parseColor(textColorString));
    }

    public void setItemList(Weekday_ListItem listItem){
        this.webtoons = listItem.getList();
    }

}
