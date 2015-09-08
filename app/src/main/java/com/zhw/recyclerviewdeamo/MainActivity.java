package com.zhw.recyclerviewdeamo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.RecyclerListener;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * android recyclerView的简单实例代码
 * @author jan
 */
public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER, LINEAR_LAYOUT_MANAGER
    }

    private RadioButton mLinearlayoutButton;
    private RadioButton mGridLayoutButton;

    private RecyclerView mRecyclerView;
    private CustomAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LayoutManagerType mCurrentLayoutManagerType;
    //建立的模拟数据
    private ArrayList<String> mDataList;
    //网格布局中的设置列数
    private static int SpanCount=3;
    //模拟数据的个数
    private static int DataSize = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDatas();
        initViews(savedInstanceState);
    }

    private void initViews(Bundle savedInstanceState) {
        mLinearlayoutButton = (RadioButton) findViewById(R.id.linearlayout_rb);
        mLinearlayoutButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setRecyclerViewManagerType(LayoutManagerType.LINEAR_LAYOUT_MANAGER);
            }
        });
        mGridLayoutButton = (RadioButton) findViewById(R.id.gridlayout_rb);
        mGridLayoutButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setRecyclerViewManagerType(LayoutManagerType.GRID_LAYOUT_MANAGER);
            }
        });
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        if(savedInstanceState!=null){
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
            Log.i(TAG, "mCurrentLayoutManagerType="+mCurrentLayoutManagerType);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        setRecyclerViewManagerType(mCurrentLayoutManagerType);
        mAdapter = new CustomAdapter(mDataList);
        mRecyclerView.setAdapter(mAdapter);
        //设置recycler拥有固定的大小，提高展示效率
        mRecyclerView.setHasFixedSize(true);
        //设置默认的动画，在移除和添加的效果下展现，现在github上可以找到许多拓展，有兴趣的可以找找
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //实现我们给Adapter监听的方法，因为recyclerview没有Listview的OnClick和OnlongClick相似的方法
        mAdapter.setOnItemPressedListener(new CustomAdapter.OnItemPressedListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(MainActivity.this, "你点击了 item-"+mDataList.get(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean OnItemLongClick(int position) {
                //这里模拟了删除的功能
                removeItemByPosition(position);
//              insertItemByPosition(position);
                Toast.makeText(MainActivity.this, "你长按了 item-"+mDataList.get(position), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        mRecyclerView.setRecyclerListener(new RecyclerListener() {
            //资源回收后被调用
            @Override
            public void onViewRecycled(ViewHolder viewHolder) {
                CustomAdapter.ViewHolder vh = (CustomAdapter.ViewHolder) viewHolder;
                Log.d(TAG, "onViewRecycled->"+vh.getItemText().getText());
            }
        });
    }

    //创建模拟的数据
    private void initDatas() {
        mDataList = new ArrayList<String>();
        for (int i = 0; i < DataSize; i++) {
            mDataList.add(String.format(getString(R.string.iamstudent), i));
        }
    }
    /**
     * 可以改变recycler的布局显示方式
     * @param type
     */
    protected void setRecyclerViewManagerType(LayoutManagerType type) {
        int scrollPosition = 0;
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView
                    .getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }
        switch (type) {
            //网状
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(this, SpanCount);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            //线性，如list
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(this);
                mLayoutManager.canScrollHorizontally();
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(this);
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
        }
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    /**
     * 通过RecyclerView的adapter来移除指定位置的数据
     * @param position
     */
    protected void removeItemByPosition(int position) {
        if(mAdapter!=null&&position>0){
            mAdapter.notifyItemRemoved(position);
            mDataList.remove(position);
            mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());
            //你如果用了这个 ，就没有动画效果了。
//          mAdapter.notifyDataSetChanged();
        }
    }
    //对应这是可以新增指定索引的
    protected void insertItemByPosition(int position) {
        if(mAdapter!=null&&position>0){
            mAdapter.notifyItemInserted(position);
            mDataList.add(position, String.format(getString(R.string.iamstudent), position));
            mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

}