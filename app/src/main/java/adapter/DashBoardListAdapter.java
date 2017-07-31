package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import banksoftware.com.banksoftware.CaseListFilteredActivity;
import banksoftware.com.banksoftware.CasesDetailsActivity;
import banksoftware.com.banksoftware.R;
import holders.CasesViewHolder;
import holders.DashBoardViewHolder;
import interfaces.OnLoadMoreListener;
import model.DashBoardListClass;
import util.ConnectionDetector;

/**
 * Created by WPA2 on 4/22/2017.
 */

public class DashBoardListAdapter extends RecyclerView.Adapter<DashBoardViewHolder> {
    Context context;
    LayoutInflater inflater;
    ArrayList<DashBoardListClass> dashBoardArrayList = new ArrayList<>();
    ConnectionDetector mConnectionDetector;
    Activity activity;
    View view;
    RecyclerView recyclerViewNew;
    private boolean isLoading;
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private OnLoadMoreListener mOnLoadMoreListener;
    Typeface typeFaceBold, typeFaceBoldItalic, typeFaceExtraBold, typeFaceExtraBoldItalic,
            typeFaceItalic, typeFaceLight, typeFaceLightItalic, typeFaceRegular,
            typeSemibold, typeSemiboldItalic;

    public DashBoardListAdapter(RecyclerView recyclerView, Context context, ArrayList<DashBoardListClass>
            dashBoardArrayList, Activity activity) {
        this.context = context;
        this.dashBoardArrayList = dashBoardArrayList;
        this.activity = activity;
        inflater = LayoutInflater.from(this.context);
        mConnectionDetector = new ConnectionDetector(context);

        this.recyclerViewNew = recyclerView;
        function(recyclerViewNew);

        typeFaceBold = Typeface.createFromAsset(context.getAssets(), "opensans_bold.ttf");
        typeFaceBoldItalic = Typeface.createFromAsset(context.getAssets(), "opensans_bolditalic.ttf");
        typeFaceExtraBold = Typeface.createFromAsset(context.getAssets(), "opensans_extrabold.ttf");
        typeFaceExtraBoldItalic = Typeface.createFromAsset(context.getAssets(), "opensans_extrabolditalic.ttf");
        typeFaceItalic = Typeface.createFromAsset(context.getAssets(), "opensans_italic.ttf");
        typeFaceLight = Typeface.createFromAsset(context.getAssets(), "opensans_light.ttf");
        typeFaceLightItalic = Typeface.createFromAsset(context.getAssets(), "opensans_lightitalic.ttf");
        typeFaceRegular = Typeface.createFromAsset(context.getAssets(), "opensans_regular.ttf");
        typeSemibold = Typeface.createFromAsset(context.getAssets(), "opensans_semibold.ttf");
        typeSemiboldItalic = Typeface.createFromAsset(context.getAssets(), "opensans_semibolditalic.ttf");

    }

    @Override
    public DashBoardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_dashboard_fragment, parent, false);

        DashBoardViewHolder sectionViewHolder = new DashBoardViewHolder(view);
        return sectionViewHolder;
    }

    @Override
    public void onBindViewHolder(DashBoardViewHolder mViewHolder, final int position) {
        final int pos = position;
        final DashBoardListClass currentListData = dashBoardArrayList.get(position);

        final String mStatus = currentListData.getStatus();
        final String mCaseCount = currentListData.getCaseCount();

        mViewHolder.tv_casename.setText(mStatus);
        mViewHolder.tv_casename.setTypeface(typeSemibold);

        mViewHolder.tv_case_count.setText("( " + mCaseCount + " )");
        mViewHolder.tv_case_count.setTypeface(typeFaceRegular);

        mViewHolder.rl_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, CaseListFilteredActivity.class);
                i.putExtra("STATUS", mStatus);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dashBoardArrayList.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    public void function(RecyclerView mRecycler) {
        try {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecycler.getLayoutManager();
            mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = ((LinearLayoutManager) recyclerView.getLayoutManager()).getItemCount();
                    lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();

                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (mOnLoadMoreListener != null) {
                            mOnLoadMoreListener.onLoadMore();
                        }
                        isLoading = true;
                    }
                }
            });
        } catch (Exception r) {
            r.getStackTrace();
        }
    }
}
