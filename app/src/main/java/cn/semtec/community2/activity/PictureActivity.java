package cn.semtec.community2.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import cn.etsoft.smarthome.R;

public class PictureActivity extends MyBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        ImageView image = (ImageView) findViewById(R.id.image);
        TextView tv_devName = (TextView) findViewById(R.id.tv_devName);
        TextView tv_date = (TextView) findViewById(R.id.tv_date);

        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        String date = intent.getStringExtra("date");
        String device = intent.getStringExtra("device");
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        DateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        String d = df.format(Long.parseLong(date));

        image.setImageBitmap(bitmap);
        image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        tv_date.setText(d);
        tv_devName.setText(device);
        image.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
    }
}
