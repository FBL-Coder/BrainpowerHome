package cn.etsoft.smarthome.View.PopupWindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import cn.etsoft.smarthome.R;

public abstract class AbstractChoicePopWindow implements OnClickListener {

    protected Context mContext;
    protected View mParentView;

    protected TextView mTVTitle;
    protected Button mButtonOK;
    protected Button mButtonCancel;
    protected ListView mListView;

    protected PopupWindow mPopupWindow;
    protected List<String> mList;

    private OnClickListener mOkListener;

    public AbstractChoicePopWindow(Context context, View parentView,
                                   List<String> list) {
        mContext = context;
        mParentView = parentView;
        mList = list;

        initView(mContext);
    }

    public void upParentView(View view){
        mParentView = view;
        mPopupWindow.setWidth(view.getWidth());
    }

    protected void initView(Context context) {
        final View customView = mParentView.inflate(context, R.layout.listview_popwindow_layout, null);
        customView.setFocusable(true);
        customView.setFocusableInTouchMode(true);
        customView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (mPopupWindow != null) {
                        mPopupWindow.dismiss();
                    }
                }
                return false;
            }
        });
        mTVTitle = (TextView) customView.findViewById(R.id.tvTitle);
        mButtonOK = (Button) customView.findViewById(R.id.btnOK);
        mButtonOK.setOnClickListener(this);
        mButtonCancel = (Button) customView.findViewById(R.id.btnCancel);
        mButtonCancel.setOnClickListener(this);
        mPopupWindow = new PopupWindow(customView, mParentView.getWidth(), 500);
        ColorDrawable dw = new ColorDrawable(0x00);
        //popupwindow页面之外可点
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(dw);
        mListView = (ListView) customView.findViewById(R.id.listView);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btnOK:
                onButtonOK(v);
                break;
            case R.id.btnCancel:
                onButtonCancel(v);
                break;
        }
    }

    public void setOnOKButtonListener(OnClickListener onClickListener) {
        mOkListener = onClickListener;
    }

    public void setTitle(String title) {
        mTVTitle.setText(title);
    }

    public void show() {
        if (!mPopupWindow.isShowing()) {
            mPopupWindow.showAsDropDown(mParentView, 0, 0);
        } else {
            if (mPopupWindow != null)
                mPopupWindow.dismiss();
        }
    }

    protected void onButtonOK(View v) {
        show();

        if (mOkListener != null) {
            mOkListener.onClick(v);
        }
    }

    protected void onButtonCancel(View v) {
        show();

    }

}
