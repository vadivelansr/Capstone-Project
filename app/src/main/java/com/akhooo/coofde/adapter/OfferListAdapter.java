package com.akhooo.coofde.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.akhooo.coofde.R;
import com.akhooo.coofde.bean.OfferBean;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseListAdapter;

/**
 * Created by vadivelansr on 1/12/2016.
 */
public class OfferListAdapter extends FirebaseListAdapter<OfferBean> {
    public OfferListAdapter(Activity activity, Class<OfferBean> modelClass, int modelLayout, Query ref) {
        super(activity, modelClass, modelLayout, ref);
    }

    @Override
    protected void populateView(View view, OfferBean offerBean, int position) {
        TextView offersTitle;
         TextView offersViews;
        offersTitle = (TextView) view.findViewById(R.id.list_item_offers_title);
         offersViews = (TextView) view.findViewById(R.id.list_item_offers_views);

        offersTitle.setText(offerBean.getTitle());
        if(offerBean.getViews() != null)
         offersViews.setText(Long.toString(offerBean.getViews()));
    }
}
