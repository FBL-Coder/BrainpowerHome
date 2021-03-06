package cn.etsoft.smarthome.Adapter.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import cn.etsoft.smarthome.Domain.WareSceneEvent;
import cn.etsoft.smarthome.R;

/**
 * Created by Say GoBay on 2017/3/29.
 */
public class SceneSet_ScenesAdapter extends RecyclerView.Adapter<SceneSet_ScenesAdapter.SceneViewHolder> {
    private List<WareSceneEvent> list;
    private int mPosition = 0;
    private int[] image = {R.drawable.ic_launcher, R.drawable.ic_launcher,
            R.drawable.ic_launcher, R.drawable.ic_launcher,
            R.drawable.ic_launcher};
    private SceneViewHolder.OnItemClick onItemClick;

    public SceneSet_ScenesAdapter(List<WareSceneEvent> list) {
        this.list = list;
    }

    public void setOnItemClick(SceneViewHolder.OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @Override
    public SceneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_horizontal_item, null);
        SceneViewHolder holder = new SceneViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final SceneViewHolder holder, final int position) {

        if (mPosition == position) {
            holder.itemView.setBackgroundResource(R.color.color_334eade6);  //选中项背景
        } else {
            holder.itemView.setBackgroundResource(R.color.color_08143F);  //其他项背景
        }
        holder.iv.setImageResource(image[position % 5]);
        holder.tv.setText(list.get(position).getSceneName());
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

    public static class SceneViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv;
        private TextView tv;

        public SceneViewHolder(View itemView) {
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
