package com.zenchn.support.fragmentutil;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

/**
 * 管理fragment切换的manager
 */
public class HFragmentManager {
    // 管理类FragmentManager
    private FragmentManager mFragmentManager;
    // 容器布局id containerViewId
    private int mContainerViewId;

    /**
     * 构造函数
     *
     * @param fragmentManager 管理类FragmentManager
     * @param containerViewId 容器布局id containerViewId
     */
    public HFragmentManager(@Nullable FragmentManager fragmentManager, @IdRes int containerViewId) {
        this.mFragmentManager = fragmentManager;
        this.mContainerViewId = containerViewId;
    }

   /**
     * 添加Fragment
     * 此方法必须先调用,参数可为多个，默认只显示第一个fragment，第二个参数以后作为预加载
     */
    public void add(Fragment... fragment) {
        if (fragment == null) {
            throw new IllegalStateException("you must call add(Fragment fragment) first!");
        }

        // 开启事物
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        // 第一个参数是Fragment的容器id，需要添加的Fragment
        for (Fragment f : fragment) {
            fragmentTransaction.add(mContainerViewId, f);
        }
        //默认显示第一个，后面的隐藏
        for (int i = 0; i < fragment.length; i++) {
            Fragment f = fragment[i];
            if (i == 0) {
                fragmentTransaction.show(f);
            } else {
                fragmentTransaction.hide(f);
            }
        }
        // 一定要commit
        fragmentTransaction.commit();
    }

    /**
     * 切换显示Fragment
     */
    public void switchFragment(Fragment newFragment) {
        // 开启事物
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        // 1.先隐藏当前所有的Fragment
        List<Fragment> childFragments = mFragmentManager.getFragments();
        for (Fragment childFragment : childFragments) {
            fragmentTransaction.hide(childFragment);
        }

        // 2.如果容器里面没有我们就添加，否则显示
        if (!childFragments.contains(newFragment)) {
            fragmentTransaction.add(mContainerViewId, newFragment);
        } else {
            fragmentTransaction.show(newFragment);
        }

        // 替换Fragment
        // fragmentTransaction.replace(R.id.main_tab_fl,mHomeFragment);
        // 一定要commit
        fragmentTransaction.commit();
    }
}
