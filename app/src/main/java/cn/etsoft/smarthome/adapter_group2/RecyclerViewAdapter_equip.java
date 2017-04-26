package cn.etsoft.smarthome.adapter_group2;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.etsoft.smarthome.R;

/**
 * Created by Say GoBay on 2017/3/29.
 */
public class RecyclerViewAdapter_equip extends RecyclerView.Adapter<RecyclerViewAdapter_equip.SceneViewHolder> {
    private List<String> board_name;
    private int mPosition = 0;
    private SceneViewHolder.OnItemClick onItemClick;


    public RecyclerViewAdapter_equip(List<String> board_name) {
        this.board_name = board_name;
    }

    public void setOnItemClick(SceneViewHolder.OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @Override
    public SceneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_list_item3, null);
        SceneViewHolder holder = new SceneViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final SceneViewHolder holder, final int position) {

        if (mPosition == position) {
            holder.itemView.setBackgroundResource(R.color.color_1F4D68);  //选中项背景
        } else {
            holder.itemView.setBackgroundResource(R.color.color_2F2D2E);  //其他项背景
        }
        holder.tv.setTextColor(Color.WHITE);
        holder.tv.setText(board_name.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClick != null) {
                    int pos = holder.getLayoutPosition();
                    if (holder.getLayoutPosition() != board_name.size())
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
        return board_name.size();
    }

    public static class SceneViewHolder extends RecyclerView.ViewHolder {
        private TextView tv;

        public SceneViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.title);
        }

        public interface OnItemClick {
            void OnItemClick(View view, int position);

            void OnItemLongClick(View view, int position);
        }
    }
}
