package com.nuhkoca.udacitybakingapp.view.steps.fragment;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nuhkoca.udacitybakingapp.R;
import com.nuhkoca.udacitybakingapp.databinding.FragmentStepsBinding;
import com.nuhkoca.udacitybakingapp.helper.Constants;
import com.nuhkoca.udacitybakingapp.model.RecipeResponse;
import com.nuhkoca.udacitybakingapp.presenter.steps.fragment.StepsFragmentPresenter;
import com.nuhkoca.udacitybakingapp.presenter.steps.fragment.StepsFragmentPresenterImpl;
import com.nuhkoca.udacitybakingapp.util.SnackbarPopper;

import moe.feng.common.stepperview.IStepperAdapter;
import moe.feng.common.stepperview.VerticalStepperItemView;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepsFragment extends Fragment implements StepsFragmentView, IStepperAdapter {

    private FragmentStepsBinding mFragmentStepsBinding;
    private StepsFragmentPresenter mStepsFragmentPresenter;
    private RecipeResponse mRecipeResponse;

    public static StepsFragment getInstance(RecipeResponse recipeResponse) {
        StepsFragment stepsFragment = new StepsFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.RECIPE_MODEL_INTENT_EXTRA, recipeResponse);
        stepsFragment.setArguments(bundle);

        return stepsFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFragmentStepsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_steps, container, false);

        return mFragmentStepsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mStepsFragmentPresenter = new StepsFragmentPresenterImpl(this);
        mStepsFragmentPresenter.loadSteps();
    }

    @Override
    public void onStepsLoaded() {
        if (getArguments() != null) {

            mRecipeResponse = getArguments().getParcelable(Constants.RECIPE_MODEL_INTENT_EXTRA);
            mFragmentStepsBinding.vsvSteps.setStepperAdapter(this);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mStepsFragmentPresenter = new StepsFragmentPresenterImpl(this);
    }

    @Override
    public void onDetach() {
        mStepsFragmentPresenter.destroyView();
        super.onDetach();
    }

    @NonNull
    @Override
    public CharSequence getTitle(int i) {
        return mRecipeResponse.getSteps().get(i).getShortDescription();
    }

    @Nullable
    @Override
    public CharSequence getSummary(int i) {
        return Html.fromHtml(mRecipeResponse.getSteps().get(i).getDescription());
    }

    @Override
    public int size() {
        return mRecipeResponse.getSteps().size();
    }

    @Override
    public View onCreateCustomView(final int i, final Context context, VerticalStepperItemView parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View inflateView = layoutInflater.inflate(R.layout.vertical_stepper_item, parent, false);

        final Button nextButton = inflateView.findViewById(R.id.vsiButtonNext);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFragmentStepsBinding.vsvSteps.canNext()) {
                    mFragmentStepsBinding.vsvSteps.nextStep();

                    if (getActivity()!=null) {
                        if (getActivity().findViewById(R.id.llTwoPaneMode) != null) {
                        } else {
                        }
                    }
                }
            }
        });

        final Button prevButton = inflateView.findViewById(R.id.vsiButtonPrev);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i != 0 && mFragmentStepsBinding.vsvSteps.canNext()) {
                    mFragmentStepsBinding.vsvSteps.prevStep();
                } else {
                    mFragmentStepsBinding.vsvSteps.setAnimationEnabled(!mFragmentStepsBinding.vsvSteps.isAnimationEnabled());
                }

                if (prevButton.getText().toString().equals(context.getString(R.string.steps_complete))) {
                    SnackbarPopper.pop(mFragmentStepsBinding.vsvSteps,
                            String.format(getString(R.string.congrats_for_ingredient), mRecipeResponse.getName()));
                }
            }
        });

        if (!mFragmentStepsBinding.vsvSteps.canNext()) {
            prevButton.setText(getString(R.string.steps_complete));
        }

        if (i == 0) {
            prevButton.setVisibility(View.GONE);
        } else {
            prevButton.setVisibility(View.VISIBLE);
        }

        return inflateView;
    }

    @Override
    public void onShow(int i) {

    }

    @Override
    public void onHide(int i) {

    }
}