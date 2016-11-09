package com.hecorat.importfile;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    ViewPager mViewPager;
    TextView mVideoTitle, mImageTitle, mAudioTitle, mBtnBack;
    FragmentVideosGallery mFragmentVideosGallery;
    FragmentImagesGallery mFragmentImagesGallery;
    FragmentAudioGallery mFragmentAudioGallery;
    int mFragmentCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mVideoTitle = (TextView) findViewById(R.id.video_gallery_title);
        mImageTitle = (TextView) findViewById(R.id.image_gallery_title);
        mAudioTitle = (TextView) findViewById(R.id.audio_gallery_title);
        mBtnBack = (TextView) findViewById(R.id.btn_back_gallery);
        mVideoTitle.setTextColor(Color.MAGENTA);
        GalleryPagerAdapter galleryPagerAdapter = new GalleryPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(galleryPagerAdapter);
        mViewPager.addOnPageChangeListener(onViewPagerChanged);
        mFragmentVideosGallery = new FragmentVideosGallery();
        mFragmentImagesGallery = new FragmentImagesGallery();
        mFragmentAudioGallery = new FragmentAudioGallery();
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mFragmentCode) {
                    case 0:
                        mFragmentVideosGallery.backToMain();
                        break;
                    case 1:
                        mFragmentImagesGallery.backToMain();
                        break;
                    case 2:
                        mFragmentAudioGallery.backToMain();
                        break;
                    default:
                        mFragmentVideosGallery.backToMain();
                        break;
                }

            }
        });
    }

    ViewPager.OnPageChangeListener onViewPagerChanged = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mFragmentCode = position;
            switch (position) {
                case 0:
                    mVideoTitle.setTextColor(Color.MAGENTA);
                    mImageTitle.setTextColor(Color.WHITE);
                    mAudioTitle.setTextColor(Color.WHITE);
                    break;
                case 1:
                    mVideoTitle.setTextColor(Color.WHITE);
                    mImageTitle.setTextColor(Color.MAGENTA);
                    mAudioTitle.setTextColor(Color.WHITE);
                    break;
                case 2:
                    mVideoTitle.setTextColor(Color.WHITE);
                    mImageTitle.setTextColor(Color.WHITE);
                    mAudioTitle.setTextColor(Color.MAGENTA);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private class GalleryPagerAdapter extends FragmentPagerAdapter {

        public GalleryPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return mFragmentVideosGallery;
                case 1:
                    return mFragmentImagesGallery;
                case 2:
                    return mFragmentAudioGallery;
                default:
                    return mFragmentVideosGallery;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
