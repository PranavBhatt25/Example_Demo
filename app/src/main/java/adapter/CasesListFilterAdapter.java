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

import banksoftware.com.banksoftware.R;
import banksoftware.com.banksoftware.StatusListActivity;
import holders.CasesViewHolder;
import interfaces.OnLoadMoreListener;
import model.CasesListClass;
import util.ConnectionDetector;

import static util.Common.openDialer;

/**
 * Created by Pranav on 4/22/2017.
 */

public class CasesListFilterAdapter extends RecyclerView.Adapter<CasesViewHolder> {
    Context context;
    LayoutInflater inflater;
    ArrayList<CasesListClass> casesArrayList = new ArrayList<>();
    ConnectionDetector mConnectionDetector;
    Activity activity;
    View view;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    RecyclerView recyclerViewNew;
    private boolean isLoading;
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private OnLoadMoreListener mOnLoadMoreListener;
    Typeface typeFaceBold, typeFaceBoldItalic, typeFaceExtraBold, typeFaceExtraBoldItalic,
            typeFaceItalic, typeFaceLight, typeFaceLightItalic, typeFaceRegular,
            typeSemibold, typeSemiboldItalic;

    public CasesListFilterAdapter(RecyclerView recyclerView, Context context, ArrayList<CasesListClass>
            casesArrayList, Activity activity) {
        this.context = context;
        this.casesArrayList = casesArrayList;
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
    public CasesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_cases_fragment, parent, false);

        CasesViewHolder sectionViewHolder = new CasesViewHolder(view);
        return sectionViewHolder;
    }

    @Override
    public void onBindViewHolder(final CasesViewHolder mViewHolder, final int position) {
        final int pos = position;
        final CasesListClass currentListData = casesArrayList.get(position);

        final String mID = currentListData.getId();
        final String mContactNo = currentListData.getContactNo();
        final String mBank = currentListData.getBank();
        final String mRefNo = currentListData.getRefNo();
        final String mAddress = currentListData.getAddress();
        final String mApplicantName = currentListData.getApplicantName();
        final String mStatus = currentListData.getStatus();



        mViewHolder.tv_ref.setText(mRefNo);
        mViewHolder.tv_name.setText(mApplicantName);
        mViewHolder.tv_contact_number.setText(mContactNo);
        mViewHolder.tv_bank.setText(mBank);
        mViewHolder.tv_address.setText(mAddress);
        mViewHolder.tv_ststus.setText(mStatus);

        mViewHolder.tv_ststus_title.setTypeface(typeSemibold);
        mViewHolder.tv_ref_no_title.setTypeface(typeSemibold);
        mViewHolder.tv_ref.setTypeface(typeFaceRegular);
        mViewHolder.tv_name_no_title.setTypeface(typeSemibold);
        mViewHolder.tv_name.setTypeface(typeFaceRegular);
        mViewHolder.tv_contact_number_title.setTypeface(typeSemibold);
        mViewHolder.tv_bank_title.setTypeface(typeSemibold);
        mViewHolder.tv_address_title.setTypeface(typeSemibold);
        mViewHolder.ll_cases_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, StatusListActivity.class);
                i.putExtra("caseId", mID);
                i.putExtra("mStatus", mStatus);
                context.startActivity(i);
            }
        });
        mViewHolder.tv_contact_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tvContactNumber = mViewHolder.tv_contact_number.getText().toString();
                openDialer(context, tvContactNumber);
            }
        });

    }

    @Override
    public int getItemCount() {
        return casesArrayList.size();
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
