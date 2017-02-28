package com.example.epcej.coint_mainactivity;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by epcej on 2017-02-14.
 */

public class CustomAdapter extends PagerAdapter{
    LayoutInflater inflater;

    public CustomAdapter(LayoutInflater inflater) {
        // TODO Auto-generated constructor stub

        //전달 받은 LayoutInflater를 멤버변수로 전달
        this.inflater=inflater;
    }

    //PagerAdapter가 가지고 잇는 View의 개수를 리턴
    //보통 보여줘야하는 이미지 배열 데이터의 길이를 리턴
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 5;                       //이미지 개수 리턴(3개씩 5쪽짜리)
    }

    //ViewPager가 현재 보여질 Item(View객체)를 생성할 필요가 있는 때 자동으로 호출
    //쉽게 말해, 스크롤을 통해 현재 보여져야 하는 View를 만들어냄.
    //첫번째 파라미터 : ViewPager
    //두번째 파라미터 : ViewPager가 보여줄 View의 위치(처음부터 0,1,2,3...)
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
        View view;
        Context context = null;

        //새로운 View 객체를 Layoutinflater를 이용해서 생성
        //만들어질 View의 설계는 view_pager.xml 레이아웃 파일 사용
        view= inflater.inflate(R.layout.view_pager, null);

        context = view.getContext();                //글라이더를 위한 getContext

        //만들어진 View안에 있는 ImageView 객체 참조
        //위에서 inflated 되어 만들어진 view로부터 findViewById()를 해야한다.
        ImageView img= (ImageView)view.findViewById(R.id.webtoonImg);
        TextView lankTop, lankMid, lankBot;

        lankTop = (TextView)view.findViewById(R.id.lankTop);
        lankTop.setText(Integer.toString(position*3+1));

        lankMid = (TextView)view.findViewById(R.id.lankmiddle);
        lankMid.setText(Integer.toString(position*3+2));

        lankBot = (TextView)view.findViewById(R.id.lankBottom);
        lankBot.setText(Integer.toString(position*3+3));

        //ImageView에 현재 position 번째에 해당하는 이미지를 보여주기 위한 작업
        //현재 position에 해당하는 이미지를 setting
        /*img.setImageResource(R.drawable.genre+position);*/

        if(position <3){
            //Glide를 통해 img에 이미지를 로드한다.
            Glide.with(context).load("http://thumb.comic.naver.net/webtoon/667573/thumbnail/title_thumbnail_20151120112903_t83x90.jpg").into(img);
        }
        else{
            //Glide를 통해 img에 이미지를 로드한다.
            Glide.with(context).load("http://thumb.comic.naver.net/webtoon/679519/thumbnail/title_thumbnail_20160601180804_t83x90.jpg").into(img);
        }

        //ViewPager에 만들어 낸 View 추가
        container.addView(view);

        //Image가 세팅된 View를 리턴
        return view;
    }

    //화면에 보이지 않은 View는 destroy를 해서 메모리를 관리함.
    //첫번째 파라미터 : ViewPager
    //두번째 파라미터 : 파괴될 View의 인덱스(처음부터 0,1,2,3...)
    //세번째 파라미터 : 파괴될 객체(더 이상 보이지 않은 View 객체)
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub

        //ViewPager에서 보이지 않는 View는 제거
        //세번째 파라미터가 View 객체 이지만 데이터 타입이 Object여서 형변환 실시
        container.removeView((View)object);

    }

    //instantiateItem() 메소드에서 리턴된 Ojbect가 View가  맞는지 확인하는 메소드
    @Override
    public boolean isViewFromObject(View v, Object obj) {
        // TODO Auto-generated method stub
        return v==obj;
    }
}