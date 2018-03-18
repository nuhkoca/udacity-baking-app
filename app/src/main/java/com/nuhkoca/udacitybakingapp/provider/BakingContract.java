package com.nuhkoca.udacitybakingapp.provider;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.UniqueConstraint;

/**
 * Created by nuhkoca on 3/17/18.
 */

@UniqueConstraint(columns = {BakingContract.COLUMN_FOOD_NAME}, onConflict = ConflictResolutionType.ABORT)
public class BakingContract {
    @DataType(DataType.Type.INTEGER)
    @PrimaryKey(onConflict = ConflictResolutionType.REPLACE)
    @AutoIncrement
    public static final String COLUMN_ID = "_id";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_FOOD_NAME = "foodName";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_QUANTITY_MEASURE_INGREDIENTS = "quantityMeasureIngredients";
}