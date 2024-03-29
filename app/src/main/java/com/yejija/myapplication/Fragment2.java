package com.yejija.myapplication;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.github.channguyen.rsv.RangeSliderView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Fragment2 extends Fragment {

    int mMode = com.yejija.myapplication.AppConstants.MODE_INSERT;
    int weatherIndex = 0;

    RangeSliderView moodSlider;
    int moodIndex = 2;

    Note item;

    Context context;
    OnTabItemSelectedListener listener;
    OnRequestListener requestListener;

    ImageView weatherIcon;
    TextView dateTextView;

    EditText contentsInput;
    ImageView pictureImageView;

    boolean isPhotoCaptured;
    boolean isPhotoFileSaved;

    File file;
    Bitmap resultPhotoBitmap;

    SimpleDateFormat todayDateFormat;
    String currentDateString;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.context = context;

        if (context instanceof OnTabItemSelectedListener) {
            listener = (OnTabItemSelectedListener) context;
        }

        if (context instanceof OnRequestListener) {
            requestListener = (OnRequestListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (context != null) {
            context = null;
            listener = null;
            requestListener = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment2, container, false);

        initUI(rootView);

        // check current location
        if (requestListener != null) {
            requestListener.onRequest("getCurrentLocation");
        }

        applyItem();

        return rootView;
    }


    private void initUI(ViewGroup rootView) {

        weatherIcon = rootView.findViewById(R.id.weatherIcon);
        dateTextView = rootView.findViewById(R.id.dateTextView);
        contentsInput = rootView.findViewById(R.id.contentsInput);


        pictureImageView = rootView.findViewById(R.id.pictureImageView);
        pictureImageView.setOnClickListener(v -> {
            if(isPhotoCaptured || isPhotoFileSaved) {
                showDialog(AppConstants.CONTENT_PHOTO_EX);
            } else {
                showDialog(AppConstants.CONTENT_PHOTO);
            }
        });

        Button saveButton = rootView.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> {
            if(mMode == AppConstants.MODE_INSERT) {
                saveNote();
            } else if(mMode == AppConstants.MODE_MODIFY) {
                modifyNote();
            }

            if (listener != null) {
                listener.onTabSelected(0);
            }
        });

        Button deleteButton = rootView.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(v -> {
            deleteNote();

            if (listener != null) {
                listener.onTabSelected(0);
            }
        });

        Button closeButton = rootView.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTabSelected(0);
            }
        });


        moodSlider = rootView.findViewById(R.id.sliderView);
        final RangeSliderView.OnSlideListener listener = index -> {
            AppConstants.println("moodIndex changed to " + index);
            moodIndex = index;
        };

        moodSlider.setOnSlideListener(listener);
        moodSlider.setInitialIndex(2);
    }


    public void setDateString(String dateString) {
        dateTextView.setText(dateString);
    }

    public void setContents(String data) {
        contentsInput.setText(data);
    }

    public void setPicture(String picturePath, int sampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;
        resultPhotoBitmap = BitmapFactory.decodeFile(picturePath, options);

        pictureImageView.setImageBitmap(resultPhotoBitmap);
    }

    public void setMood(String mood) {
        try {
            moodIndex = Integer.parseInt(mood);
            moodSlider.setInitialIndex(moodIndex);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void setItem(Note item) {
        this.item = item;
    }

    public void applyItem() {
        com.yejija.myapplication.AppConstants.println("applyItem called.");

        if (item != null) {
            mMode = com.yejija.myapplication.AppConstants.MODE_MODIFY;

            setWeatherIndex(Integer.parseInt(item.getWeather()));
            setDateString(item.getCreateDateStr());
            setContents(item.getContents());

            String picturePath = item.getPicture();
            com.yejija.myapplication.AppConstants.println("picturePath : " + picturePath);

            if (picturePath == null || picturePath.equals("")) {
                pictureImageView.setImageResource(R.drawable.insert_picture);
            } else {
                setPicture(item.getPicture(), 1);
            }

            setMood(item.getMood());
        } else {
            mMode = com.yejija.myapplication.AppConstants.MODE_INSERT;

            setWeatherIndex(0);

            Date currentDate = new Date();
            if (todayDateFormat == null) {
                todayDateFormat = new SimpleDateFormat(getResources().getString(R.string.today_date_format), Locale.KOREA);
            }
            currentDateString = todayDateFormat.format(currentDate);
            com.yejija.myapplication.AppConstants.println("currentDateString : " + currentDateString);
            setDateString(currentDateString);

            contentsInput.setText("");
            pictureImageView.setImageResource(R.drawable.insert_picture);
            setMood("2");
        }

    }


    public void setWeather(String data) {
        com.yejija.myapplication.AppConstants.println("setWeather called : " + data);

        if (data != null) {
            if (data.equals("맑음")) {
                weatherIcon.setImageResource(R.drawable.weather_1);
                weatherIndex = 0;
            } else if (data.equals("구름 조금")) {
                weatherIcon.setImageResource(R.drawable.weather_2);
                weatherIndex = 1;
            } else if (data.equals("구름 많음")) {
                weatherIcon.setImageResource(R.drawable.weather_3);
                weatherIndex = 2;
            } else if (data.equals("흐림")) {
                weatherIcon.setImageResource(R.drawable.weather_4);
                weatherIndex = 3;
            } else if (data.equals("비")) {
                weatherIcon.setImageResource(R.drawable.weather_5);
                weatherIndex = 4;
            } else if (data.equals("눈/비")) {
                weatherIcon.setImageResource(R.drawable.weather_6);
                weatherIndex = 5;
            } else if (data.equals("눈")) {
                weatherIcon.setImageResource(R.drawable.weather_7);
                weatherIndex = 6;
            }
        }
    }

    public void setWeatherIndex(int index) {
        if (index == 0) {
            weatherIcon.setImageResource(R.drawable.weather_1);
            weatherIndex = 0;
        } else if (index == 1) {
            weatherIcon.setImageResource(R.drawable.weather_2);
            weatherIndex = 1;
        } else if (index == 2) {
            weatherIcon.setImageResource(R.drawable.weather_3);
            weatherIndex = 2;
        } else if (index == 3) {
            weatherIcon.setImageResource(R.drawable.weather_4);
            weatherIndex = 3;
        } else if (index == 4) {
            weatherIcon.setImageResource(R.drawable.weather_5);
            weatherIndex = 4;
        } else if (index == 5) {
            weatherIcon.setImageResource(R.drawable.weather_6);
            weatherIndex = 5;
        } else if (index == 6) {
            weatherIcon.setImageResource(R.drawable.weather_7);
            weatherIndex = 6;
        }

    }


    public void showDialog(int id) {

        switch(id) {

            case com.yejija.myapplication.AppConstants.CONTENT_PHOTO:
                showPhotoSelectionActivity();
                break;

            case com.yejija.myapplication.AppConstants.CONTENT_PHOTO_EX:
                showPhotoSelectionActivity();
                break;

            default:
                break;
        }
    }


    public void showPhotoSelectionActivity() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(intent, com.yejija.myapplication.AppConstants.REQ_PHOTO_SELECTION);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (intent != null) {
            switch (requestCode) {
                case com.yejija.myapplication.AppConstants.REQ_PHOTO_CAPTURE:  // 사진 찍는 경우

                    resultPhotoBitmap = decodeSampledBitmapFromResource(file, pictureImageView.getWidth(), pictureImageView.getHeight());
                    pictureImageView.setImageBitmap(resultPhotoBitmap);

                    break;

                case com.yejija.myapplication.AppConstants.REQ_PHOTO_SELECTION:  // 사진을 앨범에서 선택하는 경우

                    Uri fileUri = intent.getData();

                    ContentResolver resolver = context.getContentResolver();

                    try {
                        InputStream instream = resolver.openInputStream(fileUri);
                        resultPhotoBitmap = BitmapFactory.decodeStream(instream);
                        pictureImageView.setImageBitmap(resultPhotoBitmap);

                        instream.close();

                        isPhotoCaptured = true;
                    } catch(Exception e) {
                        e.printStackTrace();
                    }

                    break;

            }
        }
    }

    public static Bitmap decodeSampledBitmapFromResource(File res, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(res.getAbsolutePath(),options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(res.getAbsolutePath(),options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height;
            final int halfWidth = width;


            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    private String createFilename() {
        Date curDate = new Date();
        String curDateStr = String.valueOf(curDate.getTime());

        return curDateStr;
    }

    // 데이터베이스 레코드 추가
    private void saveNote() {
        String contents = contentsInput.getText().toString();

        String picturePath = savePicture();

        String sql = "insert into " + NoteDatabase.TABLE_NOTE +
                "(WEATHER, LOCATION_X, LOCATION_Y, CONTENTS, MOOD, PICTURE) values(" +
                "'"+ weatherIndex + "', " +
                "'"+ "" + "', " +
                "'"+ "" + "', " +
                "'"+ contents + "', " +
                "'"+ moodIndex + "', " +
                "'"+ picturePath + "')";

        NoteDatabase database = NoteDatabase.getInstance(context);
        database.execSQL(sql);

    }

    // 데이터베이스 레코드 수정
    private void modifyNote() {
        if (item != null) {
            String contents = contentsInput.getText().toString();

            String picturePath = savePicture();

            // update note
            String sql = "update " + NoteDatabase.TABLE_NOTE +
                    " set " +
                    "   WEATHER = '" + weatherIndex + "'" +
                    "   ,LOCATION_X = '" + "" + "'" +
                    "   ,LOCATION_Y = '" + "" + "'" +
                    "   ,CONTENTS = '" + contents + "'" +
                    "   ,MOOD = '" + moodIndex + "'" +
                    "   ,PICTURE = '" + picturePath + "'" +
                    " where " +
                    "   _id = " + item._id;

            NoteDatabase database = NoteDatabase.getInstance(context);
            database.execSQL(sql);
        }
    }


    // 레코드 삭제
    private void deleteNote() {
        com.yejija.myapplication.AppConstants.println("deleteNote called.");

        if (item != null) {
            // delete note
            String sql = "delete from " + NoteDatabase.TABLE_NOTE +
                    " where " +
                    "   _id = " + item._id;

            NoteDatabase database = NoteDatabase.getInstance(context);
            database.execSQL(sql);
        }
    }

    private String savePicture() {
        if (resultPhotoBitmap == null) {
            com.yejija.myapplication.AppConstants.println("No picture to be saved.");
            return "";
        }

        File photoFolder = new File(com.yejija.myapplication.AppConstants.FOLDER_PHOTO);

        if(!photoFolder.isDirectory()) {
            photoFolder.mkdirs();
        }

        String photoFilename = createFilename();
        String picturePath = photoFolder + File.separator + photoFilename;

        try {
            FileOutputStream outstream = new FileOutputStream(picturePath);
            resultPhotoBitmap.compress(Bitmap.CompressFormat.PNG, 100, outstream);
            outstream.close();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return picturePath;
    }




}

