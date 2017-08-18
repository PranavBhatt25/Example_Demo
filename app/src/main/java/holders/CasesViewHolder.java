package holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import banksoftware.com.banksoftware.R;


/**
 * Created by Pranav on 9/1/17.
 */

public class CasesViewHolder extends RecyclerView.ViewHolder {
    public TextView tv_ref_no_title, tv_ref, tv_name_no_title, tv_name,  tv_contact_number_title,
            tv_contact_number, tv_bank_title, tv_bank,tv_address_title,tv_address,tv_ststus_title,tv_ststus;
    public LinearLayout ll_cases_main;

    public CasesViewHolder(View itemView) {
        super(itemView);
        tv_ref_no_title = (TextView) itemView.findViewById(R.id.tv_ref_no_title);
        tv_ref = (TextView) itemView.findViewById(R.id.tv_ref);
        tv_name_no_title = (TextView) itemView.findViewById(R.id.tv_name_no_title);
        tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        tv_contact_number_title = (TextView) itemView.findViewById(R.id.tv_contact_number_title);
        tv_contact_number = (TextView) itemView.findViewById(R.id.tv_contact_number);
        tv_bank_title = (TextView) itemView.findViewById(R.id.tv_bank_title);
        tv_bank = (TextView) itemView.findViewById(R.id.tv_bank);
        tv_address_title = (TextView) itemView.findViewById(R.id.tv_address_title);
        tv_address = (TextView) itemView.findViewById(R.id.tv_address);
        tv_ststus_title = (TextView) itemView.findViewById(R.id.tv_ststus_title);
        tv_ststus = (TextView) itemView.findViewById(R.id.tv_ststus);
        ll_cases_main = (LinearLayout) itemView.findViewById(R.id.ll_cases_main);
    }
}
