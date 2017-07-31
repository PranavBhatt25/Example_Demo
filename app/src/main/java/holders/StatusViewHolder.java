package holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import banksoftware.com.banksoftware.R;


/**
 * Created by wpa5 on 9/1/17.
 */

public class StatusViewHolder extends RecyclerView.ViewHolder {
    public TextView tv_status_name;
    public LinearLayout ll_cases_main;

    public StatusViewHolder(View itemView) {
        super(itemView);
        tv_status_name = (TextView) itemView.findViewById(R.id.tv_status_name);
        ll_cases_main = (LinearLayout) itemView.findViewById(R.id.ll_cases_main);
    }
}
