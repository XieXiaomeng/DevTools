package com.mogul.xxm.devtools.refreshview;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.mogul.xxm.devtools.R;
import com.mogul.xxm.devtools.refreshview.ui.raindrop.CustomerFooter;
import com.mogul.xxm.libdevtools.refreshview.XRefreshView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GridViewActivity extends AppCompatActivity {

    private GridView gv;
    private List<String> str_name = new ArrayList<String>();
    private XRefreshView xRefreshView;
    private ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view);
        for (int i = 0; i < 50; i++) {
            str_name.add("数据" + i);
        }
        gv = (GridView) findViewById(R.id.gv);
        xRefreshView = (XRefreshView) findViewById(R.id.custom_view);
        xRefreshView.setPullLoadEnable(true);
        //设置在上拉加载被禁用的情况下，是否允许界面被上拉
//		xRefreshView.setMoveFootWhenDisablePullLoadMore(false);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, str_name);
        gv.setAdapter(adapter);
        xRefreshView.setPinnedTime(1000);
        xRefreshView.setAutoLoadMore(false);
//		xRefreshView.setCustomHeaderView(new CustomHeader(this));
//		xRefreshView.setCustomHeaderView(new XRefreshViewHeader(this));
        xRefreshView.setMoveForHorizontal(true);
        xRefreshView.setCustomFooterView(new CustomerFooter(this));
//		xRefreshView.setPinnedContent(true);
        //设置当非RecyclerView上拉加载完成以后的回弹时间
        xRefreshView.setScrollBackDuration(300);
        xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //模拟数据加载失败的情况
                        Random random = new Random();
                        boolean success = random.nextBoolean();
                        if (success) {
                            xRefreshView.stopRefresh();
                        } else {
                            xRefreshView.stopRefresh(false);
                        }
                        //或者
//                        xRefreshView.stopRefresh(success);
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                final List<String> addlist = new ArrayList<String>();
                for (int i = 0; i < 20; i++) {
                    addlist.add("数据" + (i + str_name.size()));
                }

                new Handler().postDelayed(new Runnable() {

                    @SuppressLint("NewApi")
                    @Override
                    public void run() {
                        if (str_name.size() <= 90) {
                            if (Build.VERSION.SDK_INT >= 11) {
                                adapter.addAll(addlist);
                            }
                            xRefreshView.stopLoadMore();
                        } else {
                            xRefreshView.setLoadComplete(true);
                        }
                    }
                }, 2000);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        xRefreshView.startRefresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 加载菜单
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        switch (menuId) {
            case R.id.menu_clear:
                str_name.clear();
                for (int i = 0; i < 50; i++) {
                    str_name.add("数据" + i);
                }
                adapter.notifyDataSetChanged();
                xRefreshView.setLoadComplete(false);
                break;
            case R.id.menu_refresh:
                xRefreshView.startRefresh();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
