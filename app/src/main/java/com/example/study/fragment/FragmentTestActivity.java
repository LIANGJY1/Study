package com.example.study.fragment;

import com.example.study.base.BaseActivity;
import com.example.study.databinding.ActivityFragmentTestBinding;

public class FragmentTestActivity extends BaseActivity<FragmentTestPresenter, ActivityFragmentTestBinding> {

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        initializeActivity();
//        mPresenter.replaceFragment(FragmentTestActivity.this);
    }

    private void initializeActivity() {
        getViewBound().btCreateFragment.setOnClickListener(v -> mPresenter.createFragment());
        getViewBound().btReplaceFragment.setOnClickListener(v -> mPresenter.replaceFragment(FragmentTestActivity.this));
    }

    @Override
    protected FragmentTestPresenter onLoadPresenter() {
        return new FragmentTestPresenter();
    }

    @Override
    protected ActivityFragmentTestBinding getViewBinding() {
        return ActivityFragmentTestBinding.inflate(getLayoutInflater());
    }
}