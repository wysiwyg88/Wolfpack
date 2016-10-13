package com.lab.is.wolfpack;


import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

public class SongsManager {
    private static String TAG = "SongManager";
    // SDCard Path
    final String MEDIA_PATH = new String("/sdcard/Download");
    //final String MEDIA_PATH = new String("/storage/emulated/0/Music");
    private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

    // Constructor
    public SongsManager(){

    }

    /**
     * Function to read all mp3 files from sdcard
     * and store the details in ArrayList
     * */
    public ArrayList<HashMap<String, String>> getPlayList(Activity context){

        ContentResolver cr = context.getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        String selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("mp3");
        String[] selectionArgsMp3 = new String[]{ mimeType };

        Cursor cur = cr.query(uri, null, selectionMimeType, selectionArgsMp3, sortOrder);
        int count = 0;

        if(cur != null)
        {
            count = cur.getCount();

            if(count > 0)
            {
                while(cur.moveToNext())
                {
                    try {
                        HashMap<String, String> song = new HashMap<String, String>();
                        song.put("songTitle", cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
                        song.put("songPath", cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DATA)));

                        // Adding each song to SongList
                        songsList.add(song);
                    }catch (Exception err){
                        Log.e(TAG,err.getMessage());
                    }
                }

            }
        }

        cur.close();

        /*File home = new File(MEDIA_PATH);

        if (home.listFiles().length > 0) {
            for (File file : home.listFiles(new FileExtensionFilter())) {
                HashMap<String, String> song = new HashMap<String, String>();
                song.put("songTitle", file.getName().substring(0, (file.getName().length() - 4)));
                song.put("songPath", file.getPath());

                // Adding each song to SongList
                songsList.add(song);
            }
        }*/
        // return songs list array
        return songsList;
    }

    /**
     * Class to filter files which are having .mp3 extension
     * */
    class FileExtensionFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return (name.endsWith(".mp3") || name.endsWith(".MP3"));
        }
    }
}
