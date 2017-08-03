package cn.semtec.community2.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.semtec.community2.MyApplication;
import cn.etsoft.smarthome.R;
import cn.semtec.community2.activity.PictureActivity;
import cn.semtec.community2.adapter.PictureFragmentAdapter;
import cn.semtec.community2.database.DBhelper;

public class PictureFragment extends Fragment {
    private View layout;
    private GridView gridView;
    private PictureFragmentAdapter adapter;
    private Cursor cursor;
    private ArrayList<Map<String, String>> mList;
    private SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_picture, null);
        mList = new ArrayList<Map<String, String>>();
        gridView = (GridView) layout.findViewById(R.id.gridView);
        adapter = new PictureFragmentAdapter(getActivity(), mList);
        gridView.setAdapter(adapter);
        setListener();
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MyApplication.cellphone == null) {
            return;
        }
        db = MyApplication.getDB();
        cursor = db.query(DBhelper.VIDEO_RECORD, null, DBhelper.RECORD_ACCOUNT + "=?", new String[]{MyApplication.cellphone},
                null, null, DBhelper.RECORD_DATE + " desc");
        mList.clear();
        for (int i = 0; i < cursor.getCount() && i < 16; i++) {
            cursor.moveToPosition(i);
            Map<String, String> map = new HashMap<String, String>();
            map.put("date", cursor.getString(cursor.getColumnIndex(DBhelper.RECORD_DATE)));
            map.put("path", cursor.getString(cursor.getColumnIndex(DBhelper.RECORD_PICTURE)));
            map.put("_id", cursor.getString(cursor.getColumnIndex(DBhelper.RECORD_ID)));
            map.put("device", cursor.getString(cursor.getColumnIndex(DBhelper.RECORD_DEVICE)));
            mList.add(map);
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    private void setListener() {
        gridView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Intent intent = new Intent(getActivity(), PictureActivity.class);
                        Map<String, String> map = mList.get(position);
                        intent.putExtra("path", map.get("path"));
                        intent.putExtra("date", map.get("date"));
                        intent.putExtra("device", map.get("device"));
                        startActivity(intent);
                    }
                });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (adapter.isShowDelete) {
                    adapter.isShowDelete = false;
                } else {
                    adapter.isShowDelete = true;
                }
                adapter.setIsShowDelete(adapter.isShowDelete);
                return true;
            }
        });
    }
}
