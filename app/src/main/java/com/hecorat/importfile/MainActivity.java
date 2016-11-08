package com.hecorat.importfile;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    ViewPager mViewPager;
    TextView mVideoTitle, mImageTitle, mAudioTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mVideoTitle = (TextView) findViewById(R.id.video_gallery_title);
        mImageTitle = (TextView) findViewById(R.id.image_gallery_title);
        mAudioTitle = (TextView) findViewById(R.id.audio_gallery_title);
        mVideoTitle.setTextColor(Color.MAGENTA);
        GalleryPagerAdapter galleryPagerAdapter = new GalleryPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(galleryPagerAdapter);
        mViewPager.addOnPageChangeListener(onViewPagerChanged);
    }

    ViewPager.OnPageChangeListener onViewPagerChanged = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
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
            return new FragmentVideosGallery();
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
