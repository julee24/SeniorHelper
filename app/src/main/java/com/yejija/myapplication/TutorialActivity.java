package com.yejija.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TutorialActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnNext;
    private DatabaseReference mDatabase;
    Animation animFadeIn, animFadeIn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setContentView(R.layout.activity_tutorial);

        viewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.layoutDots);
        btnNext = findViewById(R.id.btn_next);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        animFadeIn2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in2);


        // 변화될 레이아웃들 주소
        // 원하는 경우 레이아웃을 몇 개 더 추가
        layouts = new int[]{
                R.layout.tuto_page1,
                R.layout.tuto_page2,
                R.layout.tuto_page3
        };

        // 하단 점 추가
        addBottomDots(0);

        // 알림 표시줄을 투명하게 만들기
        changeStatusBarColor();

        pagerAdapter = new PagerAdapter();
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);


        // 조건문을 통해 버튼 하나로 두개의 상황을 실행
        btnNext.setOnClickListener(v -> {
            int current = getItem(+1);
            if (current < layouts.length) {
                // 마지막 페이지가 아니라면 다음 페이지로 이동
                viewPager.setCurrentItem(current);
            }
            else {
                // 마지막 페이지라면 메인페이지로 이동
                finish();
                Intent intent7 = new Intent(getApplicationContext(), SubActivity.class);
                startActivity(intent7);

                //
            }
        });

    }

    // 하단 점(선택된 점, 선택되지 않은 점) 구현
    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length]; // 레이아웃 크기만큼 하단 점 배열에 추가

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    // 뷰페이저 변경 리스너
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // 다음 / 시작 버튼 바꾸기
            if (position == layouts.length - 1) {
                // 마지막 페이지에서는 다음 버튼을 시작버튼으로 교체
                btnNext.setText(getString(R.string.start)); // 다음 버튼을 시작버튼으로 글자 교체
                TextView tuto3 = findViewById(R.id.tuto3);
                ImageView tuto = findViewById(R.id.tuto_menu);
                TextView tuto32 = findViewById(R.id.text_tuto);
                TextView tuto33 = findViewById(R.id.start);
                tuto3.setVisibility(View.VISIBLE);
                tuto3.startAnimation(animFadeIn);
                tuto32.setVisibility(View.VISIBLE);
                tuto32.startAnimation(animFadeIn);
                tuto.setVisibility(View.VISIBLE);
                tuto.startAnimation(animFadeIn);
                tuto33.setVisibility(View.VISIBLE);
                tuto33.startAnimation(animFadeIn2);

            }
            else {
                btnNext.setText(getString(R.string.next));

                if (position == 0)
                {
                    TextView tuto1 = findViewById(R.id.textView3);
                    TextView guide = findViewById(R.id.guide);
                    ImageView point = findViewById(R.id.point);
                    tuto1.setVisibility(View.VISIBLE);
                    tuto1.startAnimation(animFadeIn);
                    guide.setVisibility(View.VISIBLE);
                    guide.startAnimation(animFadeIn2);
                    point.setVisibility(View.VISIBLE);
                    point.startAnimation(animFadeIn2);
                }
                else if (position == 1)
                {
                    TextView tuto2 = findViewById(R.id.textView4);
                    TextView bottom = findViewById(R.id.con2);
                    tuto2.setVisibility(View.VISIBLE);
                    tuto2.startAnimation(animFadeIn);
                    bottom.setVisibility(View.VISIBLE);
                    bottom.startAnimation(animFadeIn2);
                }

                // 마지막 페이지가 아니라면 다음과 건너띄기 버튼 출력

            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    // 알림 표시줄을 투명하게 만들기
    private void changeStatusBarColor() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    // 호출기 어댑터
    public class PagerAdapter extends androidx.viewpager.widget.PagerAdapter {
        private LayoutInflater layoutInflater;

        public PagerAdapter() {

        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }


}

