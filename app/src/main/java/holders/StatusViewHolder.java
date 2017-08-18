package holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import banksoftware.com.banksoftware.R;


/**
 * Created by Pranav on 9/1/17.
 */

public class StatusViewHolder extends RecyclerView.ViewHolder {
    public TextView tv_status_name;
    public LinearLayout ll_cases_main;
    public TextView tv_selected_status;

    public StatusViewHolder(View itemView) {
        super(itemView);
        tv_status_name = (TextView) itemView.findViewById(R.id.tv_status_name);
        ll_cases_main = (LinearLayout) itemView.findViewById(R.id.ll_cases_main);
        tv_selected_status = (TextView) itemView.findViewById(R.id.tv_selected_status);
    }
}
