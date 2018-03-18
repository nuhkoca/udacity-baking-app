package com.nuhkoca.udacitybakingapp.presenter.steps.fragment;

import com.nuhkoca.udacitybakingapp.view.steps.fragment.StepsFragmentView;

/**
 * Created by nuhkoca on 3/14/18.
 */

public class StepsFragmentPresenterImpl implements StepsFragmentPresenter {

    private StepsFragmentView mStepsFragmentView;

    public StepsFragmentPresenterImpl(StepsFragmentView mStepsFragmentView) {
        this.mStepsFragmentView = mStepsFragmentView;
    }

    @Override
    public void loadSteps() {
        mStepsFragmentView.onStepsLoaded();
    }

    @Override
    public void addItemsToDatabase() {
        mStepsFragmentView.onItemsAddedToDatabase();
    }

    @Override
    public void removeItemsFromDatabase() {
        mStepsFragmentView.onItemsRemovedFromDatabase();
    }

    @Override
    public void destroyView() {
        if (mStepsFragmentView != null) {
            mStepsFragmentView = null;
        }
    }
}