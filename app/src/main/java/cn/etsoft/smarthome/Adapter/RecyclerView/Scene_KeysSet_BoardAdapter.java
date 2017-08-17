package cn.etsoft.smarthome.Adapter.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.etsoft.smarthome.Domain.WareBoardKeyInput;
import cn.etsoft.smarthome.R;

/**
 * Created by Say GoBay on 2017/3/29.
 * 情景配按键 之  情景适配器
 */
public class Scene_KeysSet_BoardAdapter extends RecyclerView.Adapter<Scene_KeysSet_BoardAdapter.Scene_KeysSetViewHolder> {
    private List<WareBoardKeyInput> list;
    private int mPosition = 0;
    private Scene_KeysSetViewHolder.OnItemClick onItemClick;

    public Scene_KeysSet_BoardAdapter(List<WareBoardKeyInput> list) {
        this.list = list;
    }

    public void setOnItemClick(Scene_KeysSetViewHolder.OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void upData(List<WareBoardKeyInput> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public Scene_KeysSetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_horizontal_item, null);
        Scene_KeysSetViewHolder holder = new Scene_KeysSetViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final Scene_KeysSetViewHolder holder, final int position) {

        if (mPosition == position) {
            holder.itemView.setBackgroundResource(R.color.color_334eade6);  //选中项背景
        } else {
            holder.itemView.setBackgroundResource(R.color.color_08143F);  //其他项背景
        }
        holder.iv.setImageResource(R.drawable.board_icon);

        holder.tv.setText(list.get(position).getBoardName());
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

    public static class Scene_KeysSetViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv;
        private TextView tv;

        public Scene_KeysSetViewHolder(View itemView) {
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
