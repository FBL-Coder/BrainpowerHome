package cn.etsoft.smarthome.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.adapter.IClick;
import cn.etsoft.smarthome.adapter.TextDeployAdapter;
import cn.etsoft.smarthome.MyApplication;

/**
 * Created by Say GoBay on 2016/8/24.
 *
 * 输入板
 */
public class ParlourFourActivity extends Activity implements AdapterView.OnItemClickListener {
    private TextView mTitle;
    private ImageView back;
    private ScrollView sv;
    private ListView lv;

    private String[] text = {"测试", "测试", "测试", "测试", "测试", "测试"};
    private String[] deploy = {"配置", "配置", "配置", "配置", "配置", "配置"};
    private String title;
    private int index = -1;
    private String uid;

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
        title = getIntent().getStringExtra("title");
        mTitle.setText(title);
        uid = getIntent().getStringExtra("uid");

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 初始化ListView]
     */
    private void initListView() {
        lv = (ListView) findViewById(R.id.parlour_four_lv);
        sv = (ScrollView) findViewById(R.id.parlour_four_sv);
        sv.smoothScrollTo(0, 0);

        if (MyApplication.getWareData().getKeyOpItems() != null) {
            int size = MyApplication.getWareData().getKeyInputs().size();
            if (size > 0) {
                for (int i = 0; i < size; i++)
                    if (title.equals(MyApplication.getWareData().getKeyInputs().get(i).getBoardName())) {
                        index = i;
                        lv.setAdapter(new TextDeployAdapter(MyApplication.getWareData().getKeyInputs().get(i).getKeyName(),
                                text, deploy, this, mListener));
                        lv.setOnItemClickListener(this);
                    }
            }
        }
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
                    Intent intent = new Intent(ParlourFourActivity.this, AddEquipmentControlActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("key_index", position);
                    bundle.putString("title", MyApplication.getWareData().getKeyInputs().get(index).getKeyName()[position]);
                    bundle.putString("uid", uid);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
            }
        }
    };
}
