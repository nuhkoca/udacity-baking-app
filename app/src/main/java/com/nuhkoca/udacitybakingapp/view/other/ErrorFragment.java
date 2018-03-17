package com.nuhkoca.udacitybakingapp.view.other;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nuhkoca.udacitybakingapp.R;
import com.nuhkoca.udacitybakingapp.callback.IErrorCallbackListener;
import com.nuhkoca.udacitybakingapp.databinding.FragmentErrorBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class ErrorFragment extends Fragment implements View.OnClickListener {

    private FragmentErrorBinding mFragmentErrorBinding;
    private static IErrorCallbackListener mIErrorCallbackListener;

    public static ErrorFragment getInstance(IErrorCallbackListener iErrorCallbackListener) {
        mIErrorCallbackListener = iErrorCallbackListener;

        return new ErrorFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentErrorBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_error, container, false);

        return mFragmentErrorBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mFragmentErrorBinding.bErrorRefreshFragment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mIErrorCallbackListener.onErrorScreenShown(false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mIErrorCallbackListener = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mIErrorCallbackListener = (IErrorCallbackListener) getActivity();
    }
}