package Database;

import android.provider.BaseColumns;


public final class UserMaster {
    private UserMaster() {}

    public static class Users implements BaseColumns {
        public static final String TABLE_NAME = "carddetails";
        public static final String COLUMN_NAME_CARDNUMBER = "dcardnum";
        public static final String COLUMN_NAME_EXPIRY = "dexpiry";
        public static final String COLUMN_NAME_CVV = "dcvv";
    }
}