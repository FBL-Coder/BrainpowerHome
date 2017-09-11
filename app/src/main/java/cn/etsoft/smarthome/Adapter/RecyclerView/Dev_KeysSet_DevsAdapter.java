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
 * 设备配按键 之  设备适配器
 */
public class Dev_KeysSet_DevsAdapter extends RecyclerView.Adapter<Dev_KeysSet_DevsAdapter.Dev_KeysSetViewHolder> {
    private List<WareDev> list;
    private int mPosition = 0;
    private Dev_KeysSetViewHolder.OnItemClick onItemClick;

    public Dev_KeysSet_DevsAdapter(List<WareDev> list) {
        this.list = list;
    }

    public void setOnItemClick(Dev_KeysSetViewHolder.OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void upData(List<WareDev> list){
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
            holder.itemView.setBackgroundResource(R.color.color_302f35);  //其他项背景
        }
        if (list.get(position).getType() == 0)
            holder.iv.setImageResource(R.drawable.kongtiao_heng);
        else if (list.get(position).getType() == 1)
            holder.iv.setImageResource(R.drawable.ic_launcher);
        else if (list.get(position).getType() == 2)
            holder.iv.setImageResource(R.drawable.ic_launcher);
        else if (list.get(position).getType() == 3)
            holder.iv.setImageResource(R.drawable.deng_heng);
        else if (list.get(position).getType() == 4)
            holder.iv.setImageResource(R.drawable.chuanglian_heng);
        else if (list.get(position).getType() == 5)
            holder.iv.setImageResource(R.drawable.ic_launcher);
        else if (list.get(position).getType() == 6)
            holder.iv.setImageResource(R.drawable.ic_launcher);
        else if (list.get(position).getType() == 7)
            holder.iv.setImageResource(R.drawable.ic_launcher);
        else if (list.get(position).getType() == 8)
            holder.iv.setImageResource(R.drawable.ic_launcher);
        else if (list.get(position).getType() == 9)
            holder.iv.setImageResource(R.drawable.ic_launcher);

        holder.tv.setText(list.get(position).getDevName());
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
