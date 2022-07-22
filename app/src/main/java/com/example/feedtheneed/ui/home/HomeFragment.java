package com.example.feedtheneed.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.feedtheneed.CustomViewPagerAdapter;
import com.example.feedtheneed.R;
import com.example.feedtheneed.databinding.FragmentHomeBinding;
import com.example.feedtheneed.presentation.event.AddEventActivity;
import com.example.feedtheneed.presentation.user.AdditionalInformationActivity;
import com.example.feedtheneed.presentation.user.fragments.BottomSheetFragment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private BottomSheetBehavior sheetBehavior;
    private LinearLayout bottom_sheet;
    private ImageView ivBottomSheet;
    private BottomSheetDialog dialog;
    private ViewPager eventsViewPager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        bottom_sheet = root.findViewById(R.id.lvBottomSheetBehaviour);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        ivBottomSheet = root.findViewById(R.id.mazimizeBottomSheet);
        eventsViewPager = (ViewPager) root.findViewById(R.id.eventsViewPager);
        eventsViewPager.setAdapter(new CustomViewPagerAdapter(getActivity()));

        binding.floatAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddEventActivity.class));
            }
        });

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
//                        btn_bottom_sheet.setText("Close Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
//                        btn_bottom_sheet.setText("Expand Sheet");
                        binding.floatAdd.setVisibility(View.VISIBLE);
                        binding.fabList.setVisibility(View.VISIBLE);
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                binding.floatAdd.setVisibility(View.INVISIBLE);
                binding.fabList.setVisibility(View.INVISIBLE);
            }
        });

        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
        bottomSheetFragment.show(getActivity().getSupportFragmentManager(), bottomSheetFragment.getTag());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}