package apps.sayan.pocketmoney;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class NewMonthActivity extends AppCompatActivity {
    Context context;
    DBHelper helper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_month);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=this;
        DataSet.context=this;
        Log.d("Database", "in NewMonth");
        helper=new DBHelper(context);
        db=helper.getWritableDatabase();

        try {
            ((EditText)findViewById(R.id.month_input)).setText(CalendarStuff.getMonth());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addClick(View view) {
        String month= "";
        if (findViewById(R.id.month_input) != null) {
            month = String.valueOf(((EditText)findViewById(R.id.month_input)).getText());
        }
        int receivedAmount= 0;
        if ((findViewById(R.id.received_amount_input)) != null) {
            if (!String.valueOf(((EditText) findViewById(R.id.received_amount_input)).getText()).equalsIgnoreCase(""))
                receivedAmount = Integer.parseInt(String.valueOf(((EditText) findViewById(R.id.received_amount_input)).getText()));
        }
        int extras= 0;
        if ((findViewById(R.id.extras_input)) != null) {
            if(!String.valueOf(((EditText) findViewById(R.id.extras_input)).getText()).equalsIgnoreCase(""))
                extras = Integer.parseInt(String.valueOf(((EditText) findViewById(R.id.extras_input)).getText()));
        }

        add(month, receivedAmount, extras);
        startActivity(new Intent(context,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }

    private void add(String month, int receivedAmount, int extras) {
        try{
            Information newMonth=new Information();
            newMonth.month=month;
            newMonth.previousAmount=receivedAmount;
            if(DataSet.data.size()>0)
                newMonth.previousAmount+=DataSet.data.get(0).savedAmount;
            newMonth.previousAmount+=extras;
            newMonth.savedAmount=newMonth.previousAmount;
            //DataSet.data.add(0, newMonth);


            db.execSQL("CREATE TABLE " + month + " (ID INTEGER PRIMARY KEY, AMOUNT TEXT, DETAIL TEXT)");
            ContentValues values=new ContentValues();
            values.put(helper.COLUMN_MONTH,month);
            values.put(helper.COLUMN_PREVIOUS_AMOUNT,newMonth.previousAmount);
            values.put(helper.COLUMN_SAVED_AMOUNT,newMonth.savedAmount);
            values.put(helper.COLUMN_TOTAL_SPENT_AMOUNT,"0");
            db.insert(helper.MASTER_TABLE, null, values);
            DataSet.addData(db,helper);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
