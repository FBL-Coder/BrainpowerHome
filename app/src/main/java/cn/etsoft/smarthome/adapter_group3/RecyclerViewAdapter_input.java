package cn.etsoft.smarthome.adapter_group3;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.etsoft.smarthome.R;

/**
 * Created by Say GoBay on 2017/3/29.
 */
public class RecyclerViewAdapter_input extends RecyclerView.Adapter<RecyclerViewAdapter_input.SceneViewHolder> {
    private List<String> board_name;
    private int mPosition = 0;
    private SceneViewHolder.OnItemClick onItemClick;
    private int[] icon = new int[]{R.drawable.j1,R.drawable.j2,R.drawable.j3,R.drawable.j4,R.drawable.j5,R.drawable.j6,R.drawable.j7,R.drawable.j8};
    private int[] icon_select = new int[]{R.drawable.jx1,R.drawable.jx2,R.drawable.jx3,R.drawable.jx4,R.drawable.jx5,R.drawable.jx6,R.drawable.jx7,R.drawable.jx8};


    public RecyclerViewAdapter_input(List<String> board_name) {
        this.board_name = board_name;
    }

    public void setOnItemClick(SceneViewHolder.OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @Override
    public SceneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_list_item4, null);
        //解决 RecycleView 之下的 item 布局里 match_parent失效问题
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_list_item4,parent, false);
        SceneViewHolder holder = new SceneViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final SceneViewHolder holder, final int position) {

        if (mPosition == position) {
            holder.iv.setImageResource(icon_select[position]);  //选中项背景
        } else {
            holder.iv.setImageResource(icon[position]);  //其他项背景
        }
        holder.tv.setText(board_name.get(position));
        holder.tv.setTextColor(0xffffffff);
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
        private ImageView iv;

        public SceneViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.title);
            iv = (ImageView) itemView.findViewById(R.id.icon);
        }

        public interface OnItemClick {
            void OnItemClick(View view, int position);

            void OnItemLongClick(View view, int position);
        }
    }
}
