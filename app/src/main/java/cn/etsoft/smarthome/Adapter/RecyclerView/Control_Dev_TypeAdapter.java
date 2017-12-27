package cn.etsoft.smarthome.Adapter.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import java.util.List;

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.View.CircleMenu.CircleDataEvent;

/**
 * Created by Say GoBay on 2017/3/29.
 * 家电控制 设备类型适配器
 */
public class Control_Dev_TypeAdapter extends RecyclerView.Adapter<Control_Dev_TypeAdapter.AdapterViewHolder> {
    private List<CircleDataEvent> list;
    private int mPosition = 0;
    private AdapterViewHolder.OnItemClick onItemClick;

    public Control_Dev_TypeAdapter(List<CircleDataEvent> list) {
        this.list = list;
    }

    public void setOnItemClick(AdapterViewHolder.OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void upData(List<CircleDataEvent> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setselectItem(int position) {
        mPosition = position;
        notifyDataSetChanged();
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_horizontal_item, null);
        AdapterViewHolder holder = new AdapterViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final AdapterViewHolder holder, final int position) {

        if (mPosition == position) {
            holder.itemView.setBackgroundResource(R.color.color_334eade6);  //选中项背景
        } else {
            holder.itemView.setBackgroundResource(R.color.color_00000000);  //其他项背景
        }
        if (position > 4) {
            holder.itemView.setBackgroundResource(R.color.color_898C92);  //其他项背景

        }
        holder.iv.setImageResource(list.get(position).getImage());
        holder.tv.setText(list.get(position).getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getLayoutPosition();
                if (position > 4) {
                    ToastUtil.showText("我们正在努力...");
                    return;
                }
                if (onItemClick != null) {
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

    public static class AdapterViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv;
        private TextView tv;

        public AdapterViewHolder(View itemView) {
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
