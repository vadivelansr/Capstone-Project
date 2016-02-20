package com.akhooo.coofde.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by vadivelansr on 2/11/2016.
 */
@ContentProvider(authority = CoofdeProvider.AUTHORITY, database = CoofdeDatabase.class)
public final class CoofdeProvider {
    public static final String AUTHORITY = "com.akhooo.coofde.data.CoofdeProvider";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        String COOFDE = "coofde";
    }

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = CoofdeDatabase.COOFDE)
    public static class Coofde {
        @ContentUri(path = Path.COOFDE,
                type = "vnd.android.cursor.dir/coofde",
                defaultSort = CoofdeColumns._ID + " ASC"
        )
        public static final Uri CONTENT_URI = buildUri(Path.COOFDE);

        @InexactContentUri(name = "COOFDE_ID",
                path = Path.COOFDE + "/#",
                type = "vnd.android.cursor.item/coofde",
                whereColumn = CoofdeColumns._ID,
                pathSegment = 1

        )
        public static Uri withId(long id) {
            return buildUri(Path.COOFDE, String.valueOf(id));
        }
    }

}
