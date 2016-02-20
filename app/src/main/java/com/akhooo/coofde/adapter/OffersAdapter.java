package com.akhooo.coofde.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.akhooo.coofde.R;
import com.akhooo.coofde.bean.OfferBean;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;

/**
 * Created by vadivelansr on 1/8/2016.
 */
public class OffersAdapter extends FirebaseRecyclerAdapter<OfferBean, OffersAdapter.OffersViewHolder> {
    private static Context mContext;
    private static OfferBean mOfferBean;
    private static Query  mRef;
    private static OnItemClickListener listener;
   public interface OnItemClickListener{
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    public OffersAdapter(Context context, Class<OfferBean> modelClass, int modelLayout, Class<OffersViewHolder> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mContext = context;
        mRef = ref;
    }

    @Override
    protected void populateViewHolder(OffersViewHolder viewHolder, OfferBean offer, int position) {
        Log.e("Log", offer.getTitle() + "---");
        viewHolder.offersTitle.setText(offer.getTitle());
         viewHolder.offersViews.setText(Long.toString(offer.getViews()));
        mOfferBean = offer;


    }

    public static class OffersViewHolder extends RecyclerView.ViewHolder  {
        TextView offersTitle;
        TextView offersViews;

        public OffersViewHolder(final View itemView) {
            super(itemView);
            offersTitle = (TextView) itemView.findViewById(R.id.list_item_offers_title);
            offersViews = (TextView) itemView.findViewById(R.id.list_item_offers_views);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(itemView, getLayoutPosition());
                    }
                }
            });
        }
    }
}
