package apps.sayan.pocketmoney;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by SAYAN on 04-04-2016.
 */

/**Format for file...
 * month
 * previousAmount
 * savedAmount
 * spent
 * spendAmount, spendAmount, spendAmount...
 * spendDetail, spendDetail, spendDetail...
 */
public class DataSet {
    static ArrayList<Information> data=new ArrayList<>();
    static Context context;

    public static void addData(SQLiteDatabase db, DBHelper helper){
        Cursor cursor=db.query(helper.MASTER_TABLE,new String[]{helper.COLUMN_MONTH, helper.COLUMN_PREVIOUS_AMOUNT, helper.COLUMN_SAVED_AMOUNT, helper.COLUMN_TOTAL_SPENT_AMOUNT},null,null,null,null,null);
        cursor.moveToFirst();
        data.clear();
        while(!cursor.isAfterLast())
        {
            Information information=new Information();
            information.month=cursor.getString(cursor.getColumnIndex(helper.COLUMN_MONTH));
            information.previousAmount= Integer.parseInt(cursor.getString(cursor.getColumnIndex(helper.COLUMN_PREVIOUS_AMOUNT)));
            information.savedAmount= Integer.parseInt(cursor.getString(cursor.getColumnIndex(helper.COLUMN_SAVED_AMOUNT)));
            information.spent= Integer.parseInt(cursor.getString(cursor.getColumnIndex(helper.COLUMN_TOTAL_SPENT_AMOUNT)));
            data.add(0, information);

            Cursor monthCursor=db.query(information.month,new String[]{helper.COLUMN_SPEND_AMOUNT,helper.COLUMN_SPEND_DETAIL},null,null,null,null,null);
            if(monthCursor.getCount()>0)
            {
                monthCursor.moveToFirst();
                information.spendAmount.clear();
                information.spendDetail.clear();
                while(!monthCursor.isAfterLast())
                {
                    information.spendAmount.add(0,Integer.parseInt(monthCursor.getString(monthCursor.getColumnIndex(helper.COLUMN_SPEND_AMOUNT))));
                    information.spendDetail.add(0,monthCursor.getString(monthCursor.getColumnIndex(helper.COLUMN_SPEND_DETAIL)));
                    monthCursor.moveToNext();
                }
            }
            monthCursor.close();
            cursor.moveToNext();
            Log.d("Database","Added info of "+information.month+ "with "+information.previousAmount+ " ");
        }
        cursor.close();
    }
}
