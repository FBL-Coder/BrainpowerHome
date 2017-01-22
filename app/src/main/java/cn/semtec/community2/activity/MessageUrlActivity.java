package cn.semtec.community2.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import cn.etsoft.smarthome.R;
import cn.semtec.community2.tool.Constants;

public class MessageUrlActivity extends MyBaseActivity {

    private ImageView btn_back;
    private WebView webView;
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_url);
        setView();
        setListener();
        Bundle b = getIntent().getExtras();
        String url = Constants.CONTENT_NAME + b.getString("url");
        tv_title.setText(MessageActivity.comefrom);

        webView.loadUrl(url);
        webView.setWebViewClient(
                new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view,
                                                            String url) {
                        view.loadUrl(url);
                        return true;
                    }
                });
    }

    private void setView() {
        btn_back = (ImageView) findViewById(R.id.btn_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        webView = (WebView) findViewById(R.id.webView);
    }

    private void setListener() {
        btn_back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
    }
}
