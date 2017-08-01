package cn.etsoft.smarthome.Adapter.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.etsoft.smarthome.Domain.WareDev;
import cn.etsoft.smarthome.R;

/**
 * Created by Say GoBay on 2017/3/29.
 * 按键配设备 之 按键适配器
 */
public class Key_Devs_KeysAdapter extends RecyclerView.Adapter<Key_Devs_KeysAdapter.Dev_KeysSetViewHolder> {
    private List<String> list;
    private int mPosition = 0;
    private Dev_KeysSetViewHolder.OnItemClick onItemClick;

    public Key_Devs_KeysAdapter(List<String> list) {
        this.list = list;
    }

    public void setOnItemClick(Dev_KeysSetViewHolder.OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void upData(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public Dev_KeysSetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_horizontal_item, null);
        Dev_KeysSetViewHolder holder = new Dev_KeysSetViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final Dev_KeysSetViewHolder holder, final int position) {

        if (mPosition == position) {
            holder.itemView.setBackgroundResource(R.color.color_334eade6);  //选中项背景
        } else {
            holder.itemView.setBackgroundResource(R.color.color_08143F);  //其他项背景
        }
        holder.iv.setImageResource(R.drawable.anjian_icon);

        holder.tv.setText(list.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClick != null) {
                    int pos = holder.getLayoutPosition();
                    if (holder.getLayoutPosition() != list.size())
                        mPosition = pos;
                    onItemClick.OnItemClick(holder.itemView, pos);
                    notifyDataSetChanged();
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemClick != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClick.OnItemLongClick(holder.itemView, pos);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Dev_KeysSetViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv;
        private TextView tv;

        public Dev_KeysSetViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.img_list_item);
            tv = (TextView) itemView.findViewById(R.id.text_list_item);
        }

        public interface OnItemClick {
            void OnItemClick(View view, int position);

            void OnItemLongClick(View view, int position);
        }
    }
}
