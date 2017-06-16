package cn.etsoft.smarthome.adapter_group3;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.pullmi.entity.WareDev;

/**
 * Created by Say GoBay on 2017/3/29.
 */
public class RecyclerViewAdapter_Dev_output extends RecyclerView.Adapter<RecyclerViewAdapter_Dev_output.SceneViewHolder> {
    private List<WareDev> dev_list;
    private int mPosition = 0;
    private SceneViewHolder.OnItemClick onItemClick;

    public RecyclerViewAdapter_Dev_output(List<WareDev> dev_list) {
        this.dev_list = dev_list;
    }

    public void setOnItemClick(SceneViewHolder.OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @Override
    public SceneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_list_item5, null);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_list_item5, parent,false);
        SceneViewHolder holder = new SceneViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final SceneViewHolder holder, final int position) {
        holder.title.setText(dev_list.get(position).getDevName());
        if (mPosition == position) {
            holder.itemView.setBackgroundResource(R.drawable.rect_gray11);  //选中项背景
        } else {
            holder.itemView.setBackgroundResource(R.drawable.rect_gray9);  //其他项背景
        }
        if (dev_list.get(position).getType() == 3){
            if (dev_list.get(position).getbOnOff() == 0) {
                holder.appliance.setImageResource(R.drawable.light);
            } else {
                holder.appliance.setImageResource(R.drawable.lightk);
            }
        }else if (dev_list.get(position).getType() == 4){
            if (dev_list.get(position).getbOnOff() == 0) {
                holder.appliance.setImageResource(R.drawable.quanguan);
            } else {
                holder.appliance.setImageResource(R.drawable.quankai);
            }
        }else if (dev_list.get(position).getType() == 0){
            if (dev_list.get(position).getbOnOff() == 0) {
                holder.appliance.setImageResource(R.drawable.kongtiao1);
            } else {
                holder.appliance.setImageResource(R.drawable.kongtiao2);
            }

        }else if (dev_list.get(position).getType() == 1){
            if (dev_list.get(position).getbOnOff() == 0) {
                holder.appliance.setImageResource(R.drawable.ds);
            } else {
                holder.appliance.setImageResource(R.drawable.ds);
            }
        }else if (dev_list.get(position).getType() == 2){
            if (dev_list.get(position).getbOnOff() == 0) {
                holder.appliance.setImageResource(R.drawable.jidinghe);
            } else {
                holder.appliance.setImageResource(R.drawable.jidinghe);
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClick != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClick.OnItemClick(holder.itemView, pos);
                    mPosition = pos;
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
        return dev_list.size();
    }

    public static class SceneViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView appliance;

        public SceneViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            appliance = (ImageView) itemView.findViewById(R.id.appliance);

        }

        public interface OnItemClick {
            void OnItemClick(View view, int position);
            void OnItemLongClick(View view, int position);
        }
    }
}
