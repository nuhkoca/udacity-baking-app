package com.nuhkoca.udacitybakingapp.view.steps.fragment;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.nuhkoca.udacitybakingapp.R;
import com.nuhkoca.udacitybakingapp.callback.IStepTabletCallbackListener;
import com.nuhkoca.udacitybakingapp.databinding.FragmentStepsBinding;
import com.nuhkoca.udacitybakingapp.helper.Constants;
import com.nuhkoca.udacitybakingapp.model.RecipeResponse;
import com.nuhkoca.udacitybakingapp.presenter.steps.fragment.StepsFragmentPresenter;
import com.nuhkoca.udacitybakingapp.presenter.steps.fragment.StepsFragmentPresenterImpl;

import java.util.Objects;

import moe.feng.common.stepperview.IStepperAdapter;
import moe.feng.common.stepperview.VerticalStepperItemView;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepsFragment extends Fragment implements StepsFragmentView, IStepperAdapter {

    private FragmentStepsBinding mFragmentStepsBinding;
    private StepsFragmentPresenter mStepsFragmentPresenter;
    private RecipeResponse mRecipeResponse;
    private static int mStepCount;
    private static boolean mIsClicked;

    private static IStepTabletCallbackListener mIStepTabletCallbackListener;

    public static StepsFragment getInstance(RecipeResponse recipeResponse, IStepTabletCallbackListener iStepTabletCallbackListener) {
        StepsFragment stepsFragment = new StepsFragment();

        mIStepTabletCallbackListener = iStepTabletCallbackListener;

        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.RECIPE_MODEL_INTENT_EXTRA, recipeResponse);
        stepsFragment.setArguments(bundle);

        return stepsFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFragmentStepsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_steps, container, false);

        if (savedInstanceState == null) {
            mStepCount = 0;
            mIsClicked = false;
        }

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
            mFragmentStepsBinding.vsvSteps.setCurrentStep(mStepCount);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mStepsFragmentPresenter = new StepsFragmentPresenterImpl(this);
        mIStepTabletCallbackListener = (IStepTabletCallbackListener) getActivity();
    }

    @Override
    public void onDetach() {
        mStepsFragmentPresenter.destroyView();
        mIStepTabletCallbackListener = null;
        super.onDetach();
    }

    @NonNull
    @Override
    public CharSequence getTitle(int i) {
        if (i == 0) {
            return getString(R.string.first_step_text);
        } else {
            if (i != size() - 1) {
                return mRecipeResponse.getSteps().get(i - 1).getShortDescription();
            } else {
                return getString(R.string.last_step_text);
            }
        }
    }

    @Nullable
    @Override
    public CharSequence getSummary(int i) {
        if (i == 0) {
            return Html.fromHtml(String.format(getString(R.string.first_step_summary_text), mRecipeResponse.getName()));
        } else {
            if (i != size() - 1) {
                return Html.fromHtml(mRecipeResponse.getSteps().get(i - 1).getDescription());
            } else {
                return Html.fromHtml(String.format(getString(R.string.last_step_summary_text), mRecipeResponse.getName()));
            }
        }
    }

    /**
     * +2 for Welcome and Outro steps
     */

    @Override
    public int size() {
        return (mRecipeResponse.getSteps().size() + 2);
    }

    @Override
    public View onCreateCustomView(final int i, final Context context, VerticalStepperItemView parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View inflateView = layoutInflater.inflate(R.layout.vertical_stepper_item, parent, false);

        final Button prevButton = inflateView.findViewById(R.id.vsiButtonPrev);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i != 0 && mFragmentStepsBinding.vsvSteps.canNext()) {
                    mFragmentStepsBinding.vsvSteps.prevStep();
                } else {
                    mFragmentStepsBinding.vsvSteps.setAnimationEnabled(!mFragmentStepsBinding.vsvSteps.isAnimationEnabled());
                }

                mStepCount -= 1;
            }
        });

        final Button nextButton = inflateView.findViewById(R.id.vsiButtonNext);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFragmentStepsBinding.vsvSteps.canNext()) {
                    mFragmentStepsBinding.vsvSteps.nextStep();
                    mStepCount += 1;

                    if (getActivity() != null) {
                        if (getResources().getBoolean(R.bool.isTablet)) {
                            if (i != 0 && i != size() - 1) {
                                mIStepTabletCallbackListener.onIngredientsScreenOpened(mRecipeResponse, (i - 1), true);
                            }
                        } else {
                            if (i != 0 && i != size() - 1) {
                                mIStepTabletCallbackListener.onIngredientsScreenOpened(mRecipeResponse, (i - 1), false);
                            }
                        }
                    }
                }

                if (Objects.equals(nextButton.getText().toString(), getString(R.string.steps_complete))) {
                    new MaterialDialog.Builder(getActivity())
                            .title(getString(R.string.step_dialog_title))
                            .theme(Theme.LIGHT)
                            .content(String.format(getString(R.string.step_dialog_message), mRecipeResponse.getName()))
                            .positiveText(getString(R.string.step_dialog_button))
                            .cancelable(false)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                    nextButton.setEnabled(false);
                                    nextButton.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(),
                                            android.R.color.darker_gray));

                                    mIsClicked = true;
                                }
                            })
                            .show();
                }
            }
        });

        if (mStepCount == 0) {
            if (getResources().getBoolean(R.bool.isTablet)) {
                mIStepTabletCallbackListener.onTutorialScreensActivated(true, mStepCount);
            }

            prevButton.setVisibility(View.GONE);
        } else {
            if (mStepCount == 1) {
                prevButton.setVisibility(View.GONE);
            } else {
                prevButton.setVisibility(View.VISIBLE);
            }
        }

        if (mStepCount == (size() - 1)) {
            if (getResources().getBoolean(R.bool.isTablet)) {
                mIStepTabletCallbackListener.onTutorialScreensActivated(true, mStepCount);
            }

            prevButton.setVisibility(View.GONE);
            nextButton.setText(getString(R.string.steps_complete));

            if (mIsClicked) {
                nextButton.setEnabled(false);

                if (getActivity() != null) {
                    nextButton.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(),
                            android.R.color.darker_gray));
                }
            }
        }

        return inflateView;
    }

    @Override
    public void onShow(int i) {

    }

    @Override
    public void onHide(int i) {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(Constants.STEP_COUNT_STATE, mStepCount);
        outState.putBoolean(Constants.LAST_STEP_CLICKED_STATE, mIsClicked);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState!=null){
            mStepCount = savedInstanceState.getInt(Constants.STEP_COUNT_STATE);
            mIsClicked = savedInstanceState.getBoolean(Constants.LAST_STEP_CLICKED_STATE);
        }
    }
}