package com.zhw.recyclerviewdeamo;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends
        RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private List<String> dataList;

    private OnItemPressedListener onItemPressedListener;

    public CustomAdapter(List<String> data) {
        this.dataList = data;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    //替换视图内的内容
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.getDrawableId().setBackgroundResource(R.mipmap.ic_launcher);
        viewHolder.getItemText().setText(dataList.get(position));
    }

    //由RecyclerView的Layout manager来生成一个新的ViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.layout_row_item, viewGroup, false);
        return new ViewHolder(v);
    }

    /**
     * 继承RecyclerView的ViewHolder来自定义一个视图内的ViewHolder
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView drawableId;
        private TextView itemText;

        public ViewHolder(View itemView) {
            super(itemView);
            if (itemView != null) {
                //这里我为这个View添加了点击和长按事件的监听绑定，为了使本demo更像一个listView
                itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemPressedListener != null) {
                            onItemPressedListener.onItemClick(getPosition());
                        }
                    }
                });
                itemView.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (onItemPressedListener != null) {
                            return onItemPressedListener.OnItemLongClick(getPosition());
                        }
                        return false;
                    }
                });
                drawableId = (ImageView) itemView.findViewById(R.id.item_image);
                itemText = (TextView) itemView.findViewById(R.id.item_text);
            }
        }

        public ImageView getDrawableId() {
            return drawableId;
        }

        public TextView getItemText() {
            return itemText;
        }
    }

    public void setOnItemPressedListener(
            OnItemPressedListener onItemPressedListener) {
        this.onItemPressedListener = onItemPressedListener;
    }

    protected static interface OnItemPressedListener {
        void onItemClick(int position);

        boolean OnItemLongClick(int position);
    }
}