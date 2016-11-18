package com.juhezi.emoticon.main.tabs;

import android.content.Context;

import com.juhezi.emoticon.abs.AbsFragment;
import com.juhezi.emoticon.abs.AbsPresenter;
import com.juhezi.emoticon.abs.AbsViewModel;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qiao1 on 2016/11/18.
 */
public abstract class TabFragment extends AbsFragment<AbsViewModel<AbsPresenter>> {
    private static String TAG = "TabFragment";

    protected String tabName;

    protected TabFragment(String tabName) {
        this.tabName = tabName;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public static class Builder {

        private static Map<String, AbsViewModel> mViewModelPool = new HashMap<>();
        private static Map<String, AbsPresenter> mPresenterPool = new HashMap<>();
        private static Map<String, TabFragment> mFragmentPool = new HashMap<>();

        public static Map<String, AbsViewModel> getViewModelPool() {
            return mViewModelPool;
        }

        public static Map<String, AbsPresenter> getPresenterPool() {
            return mPresenterPool;
        }

        public static Map<String, TabFragment> getFragmentPool() {
            return mFragmentPool;
        }

        private TabFragment mFragment;
        private AbsPresenter mPresenter;
        private AbsViewModel mViewModel;

        private String tabName;
        private Class viewModelClazz;
        private Class presenterClazz;
        private Class fragmentClazz;
        private Context context;

        public Builder setTabName(String tabName) {
            this.tabName = tabName;
            return this;
        }

        public Builder setViewModelClazz(Class viewModelClazz) {
            this.viewModelClazz = viewModelClazz;
            return this;
        }

        public Builder setPresenterClazz(Class presenterClazz) {
            this.presenterClazz = presenterClazz;
            return this;
        }

        public Builder setFragmentClazz(Class fragmentClazz) {
            this.fragmentClazz = fragmentClazz;
            return this;
        }

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public TabFragment build() {
            try {
                Constructor<TabFragment> constructorFrag = fragmentClazz.getDeclaredConstructor(String.class);
                constructorFrag.setAccessible(true);    //取消访问检查
                mFragment = constructorFrag.newInstance(tabName);
                mFragmentPool.put(tabName, mFragment);
                Constructor<AbsPresenter> constructorPre = (Constructor<AbsPresenter>) presenterClazz.getDeclaredConstructors()[0];
                mPresenter = constructorPre.newInstance(mFragment);
                mPresenterPool.put(tabName, mPresenter);
                Constructor<AbsViewModel> constructorVimo = (Constructor<AbsViewModel>) viewModelClazz.getConstructors()[0];
                mViewModel = constructorVimo.newInstance(context, mPresenter);
                mViewModelPool.put(tabName, mViewModel);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return mFragment;
        }

    }
}