package com.akhooo.coofde.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.akhooo.coofde.R;
import com.akhooo.coofde.activity.OfferDetailsActivity;
import com.akhooo.coofde.adapter.OfferListAdapter;
import com.akhooo.coofde.bean.OfferBean;
import com.akhooo.coofde.config.Constants;
import com.firebase.client.Firebase;
import com.firebase.client.Query;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * <p/>
 * to handle interaction events.
 * Use the {@link StoreOffersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreOffersFragment extends Fragment {
    private static final String LOG_TAG = StoreOffersFragment.class.getSimpleName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String STORE_KEY = "store_key";
    private static final String CATEGORY_KEY = "category_key";

    @Bind(R.id.progress)
    ProgressBar progressBar;
    @Bind(R.id.coofde_status)
    TextView coofdeStatus;

    private Firebase ref;
    private OfferListAdapter adapter;

    // TODO: Rename and change types of parameters
    private String mStoreKey;
    private String mCategoryKey;

    //private OnFragmentInteractionListener mListener;


    // RecyclerView recyclerView;
    public StoreOffersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment StoreOffersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StoreOffersFragment newInstance(String paramStoreKey, String paramCategoryKey) {
        StoreOffersFragment fragment = new StoreOffersFragment();
        Bundle args = new Bundle();
        args.putString(STORE_KEY, paramStoreKey);
        args.putString(CATEGORY_KEY, paramCategoryKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStoreKey = getArguments().getString(STORE_KEY);
            mCategoryKey = getArguments().getString(CATEGORY_KEY);
            //mCategoryKey = mCategoryKey.substring(0, 1).toUpperCase() + mCategoryKey.substring(1);
            ref = new Firebase(Constants.FIREBASE_URL).child(Constants.STORES).child(mStoreKey);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_store_offers, container, false);
        ButterKnife.bind(this, rootView);

        Query queryRef = ref.orderByChild("type").equalTo(mCategoryKey);
        adapter = new OfferListAdapter(getActivity(), OfferBean.class, R.layout.list_item_store_details, queryRef);

            progressBar.setVisibility(ProgressBar.GONE);
            if (rootView.findViewById(R.id.offers_list_tablet) == null) {
                ListView listView = (ListView) rootView.findViewById(R.id.offers_list);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        setItemClick(position);
                    }
                });

            } else {
                GridView gridView = (GridView) rootView.findViewById(R.id.offers_list_tablet);
                gridView.setAdapter(adapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        setItemClick(position);
                    }
                });

            }
        return rootView;
    }

    private void setItemClick(int position) {
        OfferBean bean = adapter.getItem(position);
        if (bean != null) {
            String listId = adapter.getRef(position).getKey();
            Intent intent = new Intent(getActivity(), OfferDetailsActivity.class);
            String refPath = ref.child(listId).toString();
            intent.putExtra(Constants.REF_PATH, refPath);
            intent.putExtra(Constants.KEY_STORE, mStoreKey);
            intent.putExtra(Constants.KEY_CATEGORY, mCategoryKey);
            intent.putExtra(Constants.KEY_LIST_ID, listId);
            Log.e(LOG_TAG, "RefPATH:" + refPath);
            startActivity(intent);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.cleanup();
    }
}
