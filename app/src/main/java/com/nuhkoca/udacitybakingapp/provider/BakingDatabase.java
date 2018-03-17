package com.nuhkoca.udacitybakingapp.provider;

import com.nuhkoca.udacitybakingapp.helper.Constants;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by nuhkoca on 3/17/18.
 */

@Database(fileName = Constants.DATABASE_NAME, version = BakingDatabase.VERSION)
class BakingDatabase {
    static final int VERSION = 1;

    @Table(BakingContract.class)
    static final String TABLE_NAME = "baking_ingredients";
}