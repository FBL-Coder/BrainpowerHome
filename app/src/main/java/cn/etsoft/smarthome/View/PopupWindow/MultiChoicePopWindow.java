package cn.etsoft.smarthome.View.PopupWindow;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;

import cn.etsoft.smarthome.Adapter.PopupWindow.MultiChoicAdapter;
import cn.etsoft.smarthome.R;


public class MultiChoicePopWindow extends AbstractChoicePopWindow {


    private MultiChoicAdapter<String> mMultiChoicAdapter;

    public MultiChoicePopWindow(Context context, View parentView, List<String> list, boolean flag[]) {
        super(context, parentView, list);
        initData(flag);
    }

    public void upDataFlag(boolean[] flag) {
        mMultiChoicAdapter.setSelectItem(flag);
    }


    protected void initData(boolean flag[]) {
        // TODO Auto-generated method stub
        mMultiChoicAdapter = new MultiChoicAdapter<String>(mContext, mList, flag, R.drawable.selector_checkbox1);
        mListView.setAdapter(mMultiChoicAdapter);
//		mListView.setOnItemClickListener(mMultiChoicAdapter);

//		setListViewHeightBasedOnChildren(mListView);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        Log.e("", "listAdapter.getCount() = " + listAdapter.getCount());

        int totalHeight = 0;
        int tmp = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
            tmp = listItem.getMeasuredHeight();
        }
        totalHeight += 10;
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public boolean[] getSelectItem() {
        return mMultiChoicAdapter.getSelectItem();
    }

}
