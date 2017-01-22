package cn.semtec.community2.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.etsoft.smarthome.R;

public class RecordPicActivity extends MyBaseActivity {

    private ImageView image;
    private Bitmap bitmap;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    cancelProgress();
                    if (bitmap != null)
                        image.setImageBitmap(bitmap);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_pic);
        image = (ImageView) findViewById(R.id.image);
        TextView tv_devName = (TextView) findViewById(R.id.tv_devName);
        TextView tv_date = (TextView) findViewById(R.id.tv_date);
        TextView tv_name = (TextView) findViewById(R.id.tv_name);

        Bundle bundle = getIntent().getExtras();
        String date = bundle.getString("date");
        String device = bundle.getString("device");
        String name = bundle.getString("name");
        String photoUrl = bundle.getString("photoUrl");

        tv_date.setText(date);
        tv_devName.setText(device);
        tv_name.setText(name);
        image.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        getBitmap(photoUrl);
    }

    public void getBitmap(final String url) {
        showProgress();
        new Thread() {
            @Override
            public void run() {
                super.run();
                URL myFileUrl = null;
                Drawable drawable = null;
                try {
                    myFileUrl = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(new BufferedInputStream(is));
                    handler.sendEmptyMessage(1);
                } catch (IOException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(1);
                }
            }
        }.start();
    }
}
