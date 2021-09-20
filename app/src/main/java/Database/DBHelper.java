package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "cardInfo.db";

    public  DBHelper(Context context){
        super(context, DATABASE_NAME, null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ENTRIES =
                "create Table Carddetails( cardnumber TEXT, expirydate TEXT, cvv TEXT )";

        db.execSQL(SQL_CREATE_ENTRIES);

    }

    public void addInfo (String cardnumber, String expirydate, String cvv){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("cardnumber", cardnumber);
        values.put("expirydate",expirydate);
        values.put("cvv",cvv);

        Long result = db.insert("Carddetails", null, values);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
