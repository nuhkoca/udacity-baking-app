package com.nuhkoca.udacitybakingapp;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nuhkoca.udacitybakingapp.databinding.FragmentTabletTutorialBinding;
import com.nuhkoca.udacitybakingapp.helper.Constants;
import com.nuhkoca.udacitybakingapp.module.GlideApp;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabletTutorialFragment extends Fragment {

    private FragmentTabletTutorialBinding mFragmentTabletTutorialBinding;

    public static TabletTutorialFragment getInstance(int order) {
        TabletTutorialFragment tabletTutorialFragment = new TabletTutorialFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(Constants.FRAGMENT_ORDER, order);

        tabletTutorialFragment.setArguments(bundle);

        return tabletTutorialFragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFragmentTabletTutorialBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_tablet_tutorial, container, false);

        return mFragmentTabletTutorialBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            int order = getArguments().getInt(Constants.FRAGMENT_ORDER);
            int resId = 0;
            String text = "";

            if (order == 0) {
                resId = R.drawable.welcome;
                text = getString(R.string.welcome_text);
            } else {
                resId = R.drawable.clap;
                text = getString(R.string.congratulations_text);
            }

            GlideApp.with(mFragmentTabletTutorialBinding.ivTutorialLogo.getContext())
                    .load(resId)
                    .into(mFragmentTabletTutorialBinding.ivTutorialLogo);

            mFragmentTabletTutorialBinding.ivTutorialText.setText(text);
        }
    }
}