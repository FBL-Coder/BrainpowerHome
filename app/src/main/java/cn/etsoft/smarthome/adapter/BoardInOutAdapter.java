package cn.etsoft.smarthome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.pullmi.entity.UdpProPkt;
import cn.etsoft.smarthome.pullmi.entity.WareBoardChnout;
import cn.etsoft.smarthome.pullmi.entity.WareBoardKeyInput;

/**
 * Created by Say GoBay on 2016/8/29.
 */
public class BoardInOutAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<WareBoardChnout> chnListItems;
    private List<WareBoardKeyInput> inputListItems;
    private int[] image = {R.drawable.zaijiamoshi, R.drawable.waichumoshi,
            R.drawable.yingyuanmoshi, R.drawable.jiuqingmoshi,
            R.drawable.huikemoshi};
    private int board_id;

    public BoardInOutAdapter(Context context, List<WareBoardChnout> chnlst,
                             List<WareBoardKeyInput> inputlst, int id) {
        mInflater = LayoutInflater.from(context);
        if (id == UdpProPkt.E_BOARD_TYPE.e_board_chnOut.getValue()) {
            chnListItems = chnlst;
        } else if (id == UdpProPkt.E_BOARD_TYPE.e_board_keyInput.getValue()) {
            inputListItems = inputlst;
        }
        board_id = id;
    }

    @Override
    public int getCount() {
        if (board_id == UdpProPkt.E_BOARD_TYPE.e_board_chnOut.getValue()) {
            if (null != chnListItems) {
                return chnListItems.size();
            } else {
                return 0;
            }
        } else if (board_id == UdpProPkt.E_BOARD_TYPE.e_board_keyInput.getValue()) {
            if (null != inputListItems) {
                return inputListItems.size();
            } else {
                return 0;
            }
        }else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (board_id == UdpProPkt.E_BOARD_TYPE.e_board_chnOut.getValue()) {
            if (null != chnListItems) {
                return chnListItems.get(position);
            } else {
                return 0;
            }
        } else if (board_id == UdpProPkt.E_BOARD_TYPE.e_board_keyInput.getValue()) {
            if (null != inputListItems) {
                return inputListItems.get(position);
            } else {
                return 0;
            }
        }else {
            return 0;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.sceneset_listview_item, null);
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.sceneset_iv);
            viewHolder.title = (TextView) convertView.findViewById(R.id.sceneset_tv);
            viewHolder.hui = (ImageView) convertView.findViewById(R.id.sceneset_hui);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (board_id == UdpProPkt.E_BOARD_TYPE.e_board_chnOut.getValue()) {
            viewHolder.title.setText(chnListItems.get(position).getBoardName());
        } else if (board_id == UdpProPkt.E_BOARD_TYPE.e_board_keyInput.getValue()) {
            viewHolder.title.setText(inputListItems.get(position).getBoardName());
        }
        viewHolder.image.setImageResource(image[position]);
        viewHolder.hui.setImageResource(R.drawable.huijiantou);

        return convertView;
    }

    public class ViewHolder {
        private ImageView image, hui;
        public TextView title;
    }
}
