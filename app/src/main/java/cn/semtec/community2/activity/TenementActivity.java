package cn.semtec.community2.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.etsoft.smarthome.R;
import cn.semtec.community2.fragment.FeedbackFragment;
import cn.semtec.community2.fragment.RepairsFragment;

public class TenementActivity extends MyBaseActivity implements View.OnClickListener {

    private View tab_1, tab_2, tab_3;
    private ImageView tab_1_img, tab_2_img, tab_3_img;
    private View btn_back;
    private TextView telement_title;
    private View checked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenement);
        setView();
        setListener();
        changeFragment(new FeedbackFragment());
    }

    private void setView() {
        btn_back = findViewById(R.id.btn_back);
        telement_title = (TextView) findViewById(R.id.telement_title);
        tab_1 = findViewById(R.id.tab_1);
        tab_2 = findViewById(R.id.tab_2);
        tab_3 = findViewById(R.id.tab_3);
        checked = tab_1;

        tab_1_img = (ImageView) findViewById(R.id.tab_1_img);
        tab_2_img = (ImageView) findViewById(R.id.tab_2_img);
        tab_3_img = (ImageView) findViewById(R.id.tab_3_img);
    }

    private void setListener() {
        btn_back.setOnClickListener(this);
        tab_1.setOnClickListener(this);
        tab_2.setOnClickListener(this);
        tab_3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (checked == v) {
            return;
        }
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.tab_1:
                changeFragment(new FeedbackFragment());
                tab_1_img.setImageResource(R.drawable.tenement_tab1_on);
                tab_2_img.setImageResource(R.drawable.tenement_tab2_off);
                tab_3_img.setImageResource(R.drawable.tenement_tab3_off);
                telement_title.setText(R.string.telement_tab1);
                checked = v;
                break;
            case R.id.tab_2:
                changeFragment(new RepairsFragment());
                tab_1_img.setImageResource(R.drawable.tenement_tab1_off);
                tab_2_img.setImageResource(R.drawable.tenement_tab2_on);
                tab_3_img.setImageResource(R.drawable.tenement_tab3_off);
                telement_title.setText(R.string.telement_tab2);
                checked = v;
                break;
            case R.id.tab_3:
                changeFragment(new RepairsFragment());
                tab_1_img.setImageResource(R.drawable.tenement_tab1_off);
                tab_2_img.setImageResource(R.drawable.tenement_tab2_off);
                tab_3_img.setImageResource(R.drawable.tenement_tab3_on);
                telement_title.setText(R.string.telement_tab3);
                checked = v;
                break;
        }
    }

    private void changeFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.main_flayout, fragment);
        transaction.commit();
    }
}
