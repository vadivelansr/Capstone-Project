package com.akhooo.coofde.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akhooo.coofde.R;
import com.akhooo.coofde.config.Constants;
import com.akhooo.coofde.data.CoofdeColumns;
import com.akhooo.coofde.data.CoofdeProvider;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by vadivelansr on 2/11/2016.
 */
public class FavouritesAdapter extends CursorAdapter {
    public static class ViewHolder {
        @Bind(R.id.favourites_coofde_store)
        TextView coofdeStore;
        @Bind(R.id.favourites_coofde_title)
        TextView coofdeTitle;
        @Bind(R.id.favourites_coofde_desc)
        TextView coofdeDesc;
        @Bind(R.id.favourites_coofde_delete)
        Button coofdeDelete;
        @Bind(R.id.favourites_coofde_operation)
        Button coofdeOperation;
        @Bind(R.id.favourites_coofde_group)
        LinearLayout linearLayout;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public FavouritesAdapter(Context context, Cursor c, int flags) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.favourites_list_item_offer, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        final ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.coofdeStore.setText(cursor.getString(cursor.getColumnIndexOrThrow(CoofdeColumns.STORE)) + " - " +
                (cursor.getString(cursor.getColumnIndexOrThrow(CoofdeColumns.TYPE))));
        viewHolder.coofdeTitle.setText(cursor.getString(cursor.getColumnIndexOrThrow(CoofdeColumns.TITLE)));
        viewHolder.coofdeDesc.setText(cursor.getString(cursor.getColumnIndexOrThrow(CoofdeColumns.DESC)));
        final int isCoupon = cursor.getInt(cursor.getColumnIndexOrThrow(CoofdeColumns.COUPON));
        final String code = cursor.getString(cursor.getColumnIndexOrThrow(CoofdeColumns.CODE));
        final String coofdeId = cursor.getString(cursor.getColumnIndexOrThrow(CoofdeColumns.COOFDE_ID));
        final String url = cursor.getString(cursor.getColumnIndexOrThrow(CoofdeColumns.URL));
        if (isCoupon == 0) {
            viewHolder.coofdeOperation.setText(R.string.activate_deal);
        }

        viewHolder.coofdeDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectionClause = CoofdeColumns.COOFDE_ID + "=?";
                String[] selectionArgs = {coofdeId};
                int rows = context.getContentResolver().delete(CoofdeProvider.Coofde.CONTENT_URI,
                        selectionClause, selectionArgs);
            }
        });
        viewHolder.coofdeOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCoupon == 1) {
                    viewHolder.coofdeOperation.setText(code);
                    ClipData clipData = ClipData.newPlainText(Constants.KEY_CODE, code);
                    ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboardManager.setPrimaryClip(clipData);
                    Toast.makeText(context, context.getString(R.string.code_copied), Toast.LENGTH_SHORT).show();
                } else {
                    if (!TextUtils.isEmpty(url)) {
                        viewHolder.coofdeOperation.setText(context.getString(R.string.deal_activated));
                        Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        context.startActivity(Intent.createChooser(sendIntent, context.getString(R.string.open_browser)));
                    }
                }
            }
        });
    }
}
