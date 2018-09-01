package com.herve.coordinator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.herve.R;
import com.herve.coordinator.view.PullToRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;


public class PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    private PullToRefreshRecyclerView lv;

    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment pageFragment = new PageFragment();
        pageFragment.setArguments(args);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, null);
        lv = (PullToRefreshRecyclerView) view.findViewById(R.id.ptr_recycler_view);
        lv.setMode(PullToRefreshBase.Mode.PULL_FROM_END);

        List<String> list = new ArrayList<String>();
        for (int i = 1; i <= 100; i++) {
            list.add(i + "        " + mPage);
        }
        lv.setAdapter(new MyAdapter(list));

        return view;
    }
}
