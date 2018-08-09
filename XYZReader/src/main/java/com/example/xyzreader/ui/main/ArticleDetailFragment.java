package com.example.xyzreader.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ShareCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xyzreader.R;
import com.example.xyzreader.Utils.RequestQueueInstance;

import java.util.ArrayList;

import static com.example.xyzreader.Utils.SplashScreen.mUdacityUrl;


/**
 * A fragment representing a single Article detail screen. This fragment is
 * either contained in a {@link ArticleListActivity} in two-pane mode (on
 * tablets) or a {@link ArticleDetailActivity} on handsets.
 */
public class ArticleDetailFragment extends Fragment {

    private static final String TAG = "ArticleDetailFragment";
    public static final String ARG_ITEM_ID = "item_id";

    private long mItemId;
    private View mRootView;
    private TextView articleBody;
    private TextView articleTitle;
    private Button button1;
    private Button button2;
    private Button button3;
    private ProgressBar mBookLoadingPb;
    private ArrayList<XYZReader> mDataJson;
    CollapsingToolbarLayout collapsingToolbarLayout;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ArticleDetailFragment() {
    }

    public static ArticleDetailFragment newInstance(long itemId) {
        Bundle arguments = new Bundle();
        arguments.putLong(ARG_ITEM_ID, itemId);
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItemId = getArguments().getLong(ARG_ITEM_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_article_detail, container, false);
        articleBody = mRootView.findViewById(R.id.article_body);
        articleTitle = mRootView.findViewById(R.id.article_title);
        mBookLoadingPb = mRootView.findViewById(R.id.book_loading_pb);
        button1 = mRootView.findViewById(R.id.button1);
        button2 = mRootView.findViewById(R.id.button2);
        button3 = mRootView.findViewById(R.id.button3);
        collapsingToolbarLayout = (CollapsingToolbarLayout) mRootView.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.black));
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.black));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.bright_green));

        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDataJson = RequestQueueInstance.getInstance(getActivity()).getCachedData(getActivity(), mUdacityUrl);
        if (mDataJson == null) {
            // Make an internet Request
            Log.e("Data is null", "mDataJson is null");
            return;
        }

        collapsingToolbarLayout.setTitle(mDataJson.get((int) mItemId).getTitle());
        final String mainStory = mDataJson.get((int) mItemId).getBody();
        final int mainStoryLength = mainStory.length();
        final int firstChunk = mainStoryLength / 3;
        final int secondChunk = mainStoryLength * (2 / 3);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                articleBody.setText(mainStory.substring(0, 1000));
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                articleBody.setText(mainStory.substring(900, 2000));

            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                articleBody.setText(mainStory.substring(3900, 5000));
            }
        });
        mBookLoadingPb.setVisibility(View.INVISIBLE);
        articleBody.setText(mainStory.substring((2 / 3) * mainStoryLength, mainStoryLength));
    }
}
