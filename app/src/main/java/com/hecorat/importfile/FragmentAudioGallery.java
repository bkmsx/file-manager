package com.hecorat.importfile;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by bkmsx on 08/11/2016.
 */
public class FragmentAudioGallery extends Fragment{
    ArrayList<String> mListFolder;
    ArrayList<String> mListFirstVideo, mListVideo;
    GridView mGridView;
    String mStoragePath;
    int mCountSubFolder;
    VideoGalleryAdapter mFolderAdapter, mVideoAdapter;

    boolean mIsSubFolder;
    String[] pattern = {".mp3",".aac"};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_videos_gallery, null);
        mGridView = (GridView) view.findViewById(R.id.video_gallery);
        mStoragePath = Environment.getExternalStorageDirectory().toString();
        File fileDirectory = new File(mStoragePath);
        mListFolder = new ArrayList<>();
        mListFolder.add(mStoragePath);
        listFolderFrom(fileDirectory);
        mListFirstVideo = new ArrayList<>();
        mListVideo = new ArrayList<>();

        new AsyncTaskScanFolder().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        mIsSubFolder = false;
        mFolderAdapter = new VideoGalleryAdapter(getContext(), R.layout.image_layout, mListFirstVideo);
        mGridView.setAdapter(mFolderAdapter);
        mGridView.setOnItemClickListener(onFolderClickListener);
        return view;
    }

    private boolean matchFile(File file){
        for (int i=0; i<pattern.length; i++) {
            if (file.getName().endsWith(pattern[i])){
                return true;
            }
        }
        return false;
    }

    public void backToMain() {
        mIsSubFolder = false;
        mGridView.setAdapter(mFolderAdapter);
        mGridView.setOnItemClickListener(onFolderClickListener);
    }

    AdapterView.OnItemClickListener onFolderClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            mIsSubFolder = true;
            mListVideo.clear();
            mVideoAdapter = new VideoGalleryAdapter(getContext(), R.layout.image_layout, mListVideo);
            mGridView.setAdapter(mVideoAdapter);
            mGridView.setOnItemClickListener(onVideoClickListener);
            new AsyncTaskScanFile().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, i);
        }
    };

    AdapterView.OnItemClickListener onVideoClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            log("this audio path: "+mListVideo.get(i));
        }
    };

    private class AsyncTaskScanFile extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... value) {
            boolean subFolder = true;
            String folderPath = mListFolder.get(value[0]);
            if (folderPath.equals(mStoragePath)){
                subFolder = false;
            }
            loadAllVideo(new File(folderPath), mListVideo, subFolder);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mVideoAdapter.notifyDataSetChanged();
        }
    }

    private void loadAllVideo(File fileDirectory, ArrayList<String> listVideo, boolean subFolder){
        File[] fileList = fileDirectory.listFiles();
        for (int i=0; i<fileList.length; i++){
            if (fileList[i].isDirectory()) {
                if (subFolder) {
                    loadAllVideo(fileList[i], listVideo, true);
                }
            } else {
                if (matchFile(fileList[i])) {
                    listVideo.add(fileList[i].getAbsolutePath());
                }
            }
        }
    }

    private class AsyncTaskScanFolder extends AsyncTask<Void, Void, Void> {
        long start;
        @Override
        protected void onPreExecute() {
            start = System.currentTimeMillis();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            for (int i=0; i<mListFolder.size(); i++) {
                boolean scanSubFolder = mListFolder.get(i).equals(mStoragePath)? false:true;
                mCountSubFolder = 0;
                if (!isVideoFolder(new File(mListFolder.get(i)), scanSubFolder)){
                    mListFolder.remove(i);
                    i--;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mFolderAdapter.notifyDataSetChanged();
        }
    }

    private void listFolderFrom(File fileDirectory){
        File[] listFile = fileDirectory.listFiles();
        for (int i=0; i<listFile.length; i++) {
            if (listFile[i].isDirectory()) {
                String name = listFile[i].getName();
                if (name.charAt(0) != '.'){
                    mListFolder.add(listFile[i].getAbsolutePath());
                }
            }
        }
    }

    private boolean isVideoFolder(File fileDirectory, boolean includeSubDir) {
        if (mCountSubFolder>7) {
            return false;
        }
        boolean result = false;
        File[] fileList = fileDirectory.listFiles();
        for (int i=0; i<fileList.length; i++){
            if (fileList[i].isDirectory()) {
                if (includeSubDir) {
                    result = isVideoFolder(fileList[i], true);
                }
            } else {
                if (matchFile(fileList[i])) {
                    mListFirstVideo.add(fileList[i].getAbsolutePath());
                    result = true;
                }
            }
            if (result) {
                break;
            }
        }
        mCountSubFolder++;
        return result;
    }

    private class VideoGalleryAdapter extends ArrayAdapter<String> {

        public VideoGalleryAdapter(Context context, int resource, ArrayList<String> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String videoPath = getItem(position);
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.image_layout, null);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.image_view);
            TextView textView = (TextView) convertView.findViewById(R.id.text_view);
            ImageView iconFolder = (ImageView) convertView.findViewById(R.id.icon_folder);
            iconFolder.setVisibility(View.GONE);
            String name;
            if (mIsSubFolder) {
                name = new File(mListVideo.get(position)).getName();
            } else {
                name = new File(mListFolder.get(position)).getName();
            }
            textView.setText(name);
            int iconId;
            if (name.endsWith(".mp3")) {
                iconId = R.drawable.ic_mp3;
            } else if (name.endsWith(".aac")) {
                iconId = R.drawable.ic_aac;
            } else {
                iconId = R.drawable.ic_audio_folder;
            }
            imageView.setImageResource(iconId);
            return convertView;
        }

        @Override
        public int getCount() {
            return super.getCount();
        }
    }

    private void log(String msg) {
        Log.e("Fragment Video", msg);
    }
}
