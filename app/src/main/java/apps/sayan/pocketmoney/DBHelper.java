package apps.sayan.pocketmoney;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by SAYAN on 13-05-2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    protected final String ID="ID";
    protected final String MASTER_TABLE="MASTER";
    protected final String COLUMN_MONTH="MONTH";
    protected final String COLUMN_PREVIOUS_AMOUNT="PREVIOUS_AMOUNT";
    protected final String COLUMN_SAVED_AMOUNT="SAVED_AMOUNT";
    protected final String COLUMN_TOTAL_SPENT_AMOUNT="SPENT_AMOUNT";

    protected final String COLUMN_SPEND_AMOUNT="AMOUNT";
    protected final String COLUMN_SPEND_DETAIL="DETAIL";

    public DBHelper(Context context) {
        super(context, "PocketMoneyDB", null, 1);
    }
    public DBHelper(Context context, int version) {
        super(context, "PocketMoneyDB", null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE MASTER (ID INTEGER PRIMARY KEY, MONTH TEXT, PREVIOUS_AMOUNT TEXT, SAVED_AMOUNT TEXT, SPENT_AMOUNT TEXT)");
            Log.d("Database","MASTER added" );
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            Log.d("Database","Trying to upgrade");
            String[] projection = {COLUMN_MONTH};
            Cursor c = db.query(MASTER_TABLE, projection, null, null, null, null, null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                String month = c.getString(c.getColumnIndex(COLUMN_MONTH));
                Log.d("Database", "Deleted table " + month);
                db.execSQL("DROP TABLE IF EXISTS " + month);
                c.moveToNext();
            }
            c.close();
            db.execSQL("DROP TABLE IF EXISTS " + MASTER_TABLE);
            onCreate(db);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db,oldVersion,newVersion);
    }
}
