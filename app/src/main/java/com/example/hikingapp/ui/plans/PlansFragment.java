package com.example.hikingapp.ui.plans;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hikingapp.R;
import com.example.hikingapp.data.HikingPlanDataSource;
import com.example.hikingapp.model.HikingPlan;

import java.util.List;

public class PlansFragment extends Fragment {

    public static final String TAG = "PlansFragment";
    public static final String PLANS_FRAGMENT = "com.hikingapp.PLANS_FRAGMENT";

    private PlansViewModel plansViewModel;
    private RecyclerView mPlansRecyclerView;
    private PlanAdapter mPlanAdapter;
    private List<HikingPlan> mPlanList;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView()");
        plansViewModel =
                new ViewModelProvider(requireActivity()).get(PlansViewModel.class);
        View root = inflater.inflate(R.layout.fragment_plans, container, false);

        Button create_plan_button = root.findViewById(R.id.create_plan_button);

        Activity activity = getActivity();
        mPlansRecyclerView = root.findViewById(R.id.plans_recycler_view);
        if(activity != null){
            mPlansRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
            PlanAdapter adapter = new PlanAdapter(plansViewModel.getPlans().getValue());
            mPlansRecyclerView.setAdapter(adapter);
            mPlansRecyclerView.setItemAnimator(new DefaultItemAnimator());
        }

        plansViewModel.getPlans().observe(getViewLifecycleOwner(), new Observer<List<HikingPlan>>() {
            @Override
            public void onChanged(@Nullable List<HikingPlan> plans){
                if(plans != null){
                    Log.i(TAG, "plans changed");
                    mPlansRecyclerView.setAdapter(new PlanAdapter(plans));
                }
            }
        });

        plansViewModel.getPlanToEdit().observe(getViewLifecycleOwner(), new Observer<HikingPlan>() {
            @Override
            public void onChanged(HikingPlan hikingPlan) {
                Log.i(TAG, "planToEdit changed");
                mPlansRecyclerView.setAdapter(new PlanAdapter(plansViewModel.getPlans().getValue()));
            }
        });

        create_plan_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                plansViewModel.setPlanToEdit(new HikingPlan());
                CreatePlansBottomSheetDialogFragment dialog = new CreatePlansBottomSheetDialogFragment();
                dialog.show(getActivity().getSupportFragmentManager(), CreatePlansBottomSheetDialogFragment.TAG);
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        Log.i(TAG, "onViewCreated()");
    }

    private class PlanHolder extends RecyclerView.ViewHolder {
        private HikingPlan plan;
        private Button editPlanButton;
        private Button viewPlanButton;
        private View checkbox;
        private String title;
        private TextView titleView;

        public PlanHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_plan, parent, false));
            viewPlanButton = itemView.findViewById(R.id.view_plan_button);
            editPlanButton = itemView.findViewById(R.id.edit_plan_button);
            checkbox = itemView.findViewById(R.id.checkbox_cl);
            editPlanButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Log.i(TAG, "clicked EDIT plan");
                    mPlansRecyclerView.scrollToPosition(getAdapterPosition());
                    plansViewModel.setPlanToEdit(plan);
                    EditPlansBottomSheetDialogFragment dialog = new EditPlansBottomSheetDialogFragment();
                    dialog.show(getParentFragmentManager(), EditPlansBottomSheetDialogFragment.TAG);
                }
            });
            viewPlanButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Log.i(TAG, "clicked VIEW plan");
                    mPlansRecyclerView.scrollToPosition(getAdapterPosition());
                    plansViewModel.setPlanToEdit(plan);
                    ViewPlansBottomSheetDialogFragment dialog = new ViewPlansBottomSheetDialogFragment();
                    dialog.show(getParentFragmentManager(), ViewPlansBottomSheetDialogFragment.TAG);
                }
            });

            titleView = itemView.findViewById(R.id.title);
        }

        public void bind(HikingPlan plan){
            this.plan = plan;
            title = plan.getName();
            titleView.setText(title);
            if(plan.getActive()){
                editPlanButton.setVisibility(View.GONE);
                itemView.findViewById(R.id.empty_checkbox).setVisibility(View.GONE);
                itemView.findViewById(R.id.active_checkbox).setVisibility(View.VISIBLE);
                itemView.findViewById(R.id.active_tv).setVisibility(View.VISIBLE);
            }else if (plan.getVisible()){
                itemView.findViewById(R.id.empty_checkbox).setVisibility(View.GONE);
                itemView.findViewById(R.id.show_checkbox).setVisibility(View.VISIBLE);
            }
            checkbox.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if (!plan.getActive()) {
                        // toggle visibility
                        plan.setVisible(!plan.getVisible());
                        // update plan
                        plansViewModel.updateHikingPlan(plan);
                    }
                }
            });
        }
    }

    private class PlanAdapter extends RecyclerView.Adapter<PlanHolder> {
        private List<HikingPlan> planList;

        public PlanAdapter(List<HikingPlan> planList){
            this.planList = planList;
        }

        @Override
        public PlanHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new PlanHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(PlanHolder holder, int position){
            HikingPlan plan = PlansFragment.this.plansViewModel.getPlans().getValue().get(position);
            holder.bind(plan);
        }

        @Override
        public int getItemCount(){
            return planList.size();
        }
    }

    //
    //
    // METHOD OVERRIDES FOR LOGGING
    //
    //

    @Override
    public void onDestroyView(){
        Log.i(TAG, "onDestroyView()");
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context){
        Log.i(TAG, "onAttach()");
        super.onAttach(context);
    }

    @Override
    public void onDetach(){
        Log.i(TAG, "onAttach()");
        super.onDetach();
    }

    @Override
    public void onStart(){
        Log.i(TAG, "onStart()");
        super.onStart();
    }

    @Override
    public void onPause(){
        Log.i(TAG, "onPause()");
        super.onPause();
    }

    @Override
    public void onResume(){
        Log.i(TAG, "onResume()");
        super.onResume();
    }

    @Override
    public void onStop(){
        Log.i(TAG, "onStop()");
        super.onStop();
    }

    @Override
    public void onDestroy(){
        Log.i(TAG, "onDestroy()");
        super.onDestroy();
    }
}