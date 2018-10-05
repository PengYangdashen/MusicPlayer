package com.pengyang.musicplayer.UI;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.pengyang.musicplayer.R;

import java.util.ArrayList;
import java.util.List;

public class IndexActivity extends BaseActivity {

    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        initview();
    }

    private void initview() {
        fragments = new ArrayList<Fragment>();
        fragments.add(new FoundFragment());
        fragments.add(new MusicFragment());
        fragments.add(new MineFragment());
        switchFragment(1);
        ((RadioGroup)findViewById(R.id.rg_buttom)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton:
                        switchFragment(0);
                        break;
                    case R.id.radioButton2:
                        switchFragment(1);
                        break;
                    case R.id.radioButton3:
                        switchFragment(2);
                        break;
                }
            }
        });
    }

    /**
     * 点击切换fragment
     *
     * @param position
     */
    public void switchFragment(int position) {
        // 开启事务
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        // 遍历集合
        for (int i = 0; i < fragments.size(); i++) {
            Fragment fragment = fragments.get(i);
            if (i == position) {
                // 显示fragment
                if (fragment.isAdded()) {
                    // 如果这个fragment已经被事务添加,显示
                    fragmentTransaction.show(fragment);
                } else {
                    // 如果这个fragment没有被事务添加过,添加
                    fragmentTransaction.add(R.id.fl_fragment, fragment);
                }
            } else {
                // 隐藏fragment
                if (fragment.isAdded()) {
                    // 如果这个fragment已经被事务添加,隐藏
                    fragmentTransaction.hide(fragment);
                }
            }
        }
        // 提交事务
        fragmentTransaction.commit();
    }
}
