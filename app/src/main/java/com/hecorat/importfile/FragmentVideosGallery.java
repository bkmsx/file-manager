package com.hecorat.importfile;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by bkmsx on 08/11/2016.
 */
public class FragmentVideosGallery extends Fragment{
    ArrayList<String> mListFolder;
    ArrayList<ArrayList<String>> mListVideo;
    GridView mGridView;
    String mStoragePath;
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
        mListVideo = new ArrayList<>();
        ArrayList<String> listVideo = new ArrayList<>();
        listVideosFrom(fileDirectory, listVideo, false);
        if (listVideo.size()!=0) {
            mListVideo.add(listVideo);
        } else {
            mListFolder.remove(mStoragePath);
        }
        log("folder number: "+mListFolder.size());


        new AsyncTaskScanFile().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

//        VideoGalleryAdapter adapter = new VideoGalleryAdapter(getContext(), R.layout.image_layout, mListFolder);
//        mGridView.setAdapter(adapter);
        return view;
    }

    private class AsyncTaskScanFile extends AsyncTask<Void, Void, Void> {
        long start;
        @Override
        protected void onPreExecute() {
            start = System.currentTimeMillis();
            log("start scan");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            ArrayList<String> listVideo;
//            for (int i=0; i<mListFolder.size(); i++){
//                if (i==0&&mListFolder.get(i).equals(mStoragePath)) {
//                    continue;
//                }
//                listVideo = new ArrayList<>();
//                listVideosFrom(new File(mListFolder.get(i)), listVideo, true);
//                if (listVideo.size() != 0){
//                    mListVideo.add(listVideo);
//                } else {
//                    mListFolder.remove(i);
//                    i--;
//                }
//            }

            log("Contain video: "+folderWithMusic(mStoragePath));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            log("folder 1 in storage: "+mListFolder.get(1));
//            log("video 1 in storage: "+mListVideo.get(1).get(0));
            log("TIme: "+(System.currentTimeMillis()-start));
//            for (int i=0; i<mListFolder.size(); i++) {
//                log("folder name: "+mListFolder.get(i));
//            }
        }
    }

    private boolean folderWithMusic(String directoryPath){
        Cursor cursor;
        String selection;
        String[] projection = {};
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

//Create query for searching media files in folder
        selection = MediaStore.Video.Media.DATA + " like " + "'%" + directoryPath + "/%'";
        cursor = getContext().getContentResolver().query(uri, projection, selection, null, null);
        if (cursor != null) {
            boolean isDataPresent;
            isDataPresent = cursor.moveToFirst();
            return isDataPresent;
        }
        return false;
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
            textView.setText(new File(videoPath).getName());
            Glide.with(getContext()).load(videoPath).centerCrop().into(imageView);
            return convertView;
        }

        @Override
        public int getCount() {
            return super.getCount();
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

    private void listVideosFrom(File fileDirectory, ArrayList<String> listVideo, boolean includeSubDir) {
        File[] fileList = fileDirectory.listFiles();
        for (int i=0; i<fileList.length; i++){
            if (fileList[i].isDirectory()) {
                if (includeSubDir) {
                    listVideosFrom(fileList[i], listVideo, true);
                }
            } else {
                if (fileList[i].getName().endsWith(".mp4")) {
                    listVideo.add(fileList[i].getAbsolutePath());
                }
            }
        }
    }

    private void log(String msg) {
        Log.e("Fragment Video", msg);
    }
}
