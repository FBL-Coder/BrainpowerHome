package cn.etsoft.smarthome.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.adapter.IClick;
import cn.etsoft.smarthome.adapter.TextDeployAdapter;

/**
 * Created by Say GoBay on 2016/8/24.
 */
public class ParlourFourActivity extends Activity implements AdapterView.OnItemClickListener{
    private TextView mTitle;
    private ScrollView sv;
    private ListView lv;

    private String[] text = {"测试", "测试", "测试", "测试"};
    private String[] deploy = {"配置", "配置", "配置", "配置"};
    private String[] title = { "一楼客厅窗帘", "一楼客厅空调", "一楼客厅灯", "一楼客厅电视"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parlour_four_listview);
        //初始化标题栏
        initTitleBar();
        //初始化ListView
        initListView();
    }
    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        mTitle = (TextView) findViewById(R.id.tv_home);
        mTitle.setText(getIntent().getStringExtra("title"));
    }
    /**
     * 初始化ListView
     */
    private void initListView() {
        lv = (ListView) findViewById(R.id.parlour_four_lv);
        sv = (ScrollView) findViewById(R.id.parlour_four_sv);
        sv.smoothScrollTo(0, 0);
        lv.setAdapter(new TextDeployAdapter(title,text,deploy,this,mListener));
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "listview的item被点击了！，点击的位置是-->" + position,
                Toast.LENGTH_SHORT).show();
    }

    /**
     * 实现类，响应按钮点击事件
     */
    private IClick mListener = new IClick() {
        @Override
        public void listViewItemClick(int position, View v) {
            switch (v.getId()) {
                case R.id.parlour_four_text:
                    break;
                case R.id.parlour_four_deploy:
                    Intent intent = new Intent(ParlourFourActivity.this, EquipmentControlActivity.class);
                    intent.putExtra("title", title[position]);
                    startActivity(intent);
                    break;
            }
        }
    };
}
