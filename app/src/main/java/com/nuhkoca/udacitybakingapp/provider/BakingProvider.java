package com.nuhkoca.udacitybakingapp.provider;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by nuhkoca on 3/17/18.
 */

@ContentProvider(
        authority = BakingProvider.AUTHORITY,
        database = BakingDatabase.class
)
public class BakingProvider {
    static final String AUTHORITY = "com.nuhkoca.udacitybakingapp.provider.provider";


    @TableEndpoint(table = BakingDatabase.TABLE_NAME)
    public static class BakingIngredients {

        @ContentUri(
                path = "ingredients",
                type = "vnd.android.cursor.dir/ingredients")
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/ingredients");
    }
}