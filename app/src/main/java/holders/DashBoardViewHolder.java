package holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import banksoftware.com.banksoftware.R;


/**
 * Created by wpa5 on 9/1/17.
 */

public class DashBoardViewHolder extends RecyclerView.ViewHolder {
    public TextView tv_casename, tv_case_count;
    public RelativeLayout rl_favorite;

    public DashBoardViewHolder(View itemView) {
        super(itemView);
        tv_casename = (TextView) itemView.findViewById(R.id.tv_casename);
        tv_case_count = (TextView) itemView.findViewById(R.id.tv_case_count);
        rl_favorite = (RelativeLayout) itemView.findViewById(R.id.rl_favorite);

    }


}
