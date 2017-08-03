package cn.semtec.community2.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONObject;

import java.util.Date;
import java.util.Hashtable;

import cn.semtec.community2.MyApplication;
import cn.etsoft.smarthome.R;

public class CodeActivity extends MyBaseActivity {
    private ImageView iv_code;
    private String houseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_code = (ImageView) findViewById(R.id.iv_code);
        houseId = getIntent().getExtras().getString("houseId");
        showProgress();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            Bitmap b = ZXingEncode();
            if (b != null)
                iv_code.setImageBitmap(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cancelProgress();
    }

    private Bitmap ZXingEncode() throws Exception {
        long time = new Date().getTime();
        JSONObject obj = new JSONObject();
        obj.put("timeStamp", time);
        obj.put("houseId", houseId);
        try {
            int QR_WIDTH = (int) (260 * MyApplication.density);
            int QR_HEIGHT = (int) (260 * MyApplication.density);
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix martix = writer.encode(obj.toString(), BarcodeFormat.QR_CODE,
                    QR_WIDTH, QR_HEIGHT);
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new QRCodeWriter().encode(obj.toString(),
                    BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }

    }


}
