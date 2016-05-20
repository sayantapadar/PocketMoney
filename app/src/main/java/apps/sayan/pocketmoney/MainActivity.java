package apps.sayan.pocketmoney;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Context context;
    RecyclerView recyclerView;
    EditText spentAmountEdit;
    EditText spentDetailEdit;
    SQLiteDatabase db;
    DBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        spentAmountEdit=(EditText)findViewById(R.id.amount_input);
        spentDetailEdit=(EditText)findViewById(R.id.detail_input);

        context=this;
        DataSet.context=this;


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(context,NewMonthActivity.class));
                }
            });
        }

        helper=new DBHelper(context);
        db=helper.getWritableDatabase();

        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        MonthRecyclerAdapter adapter=new MonthRecyclerAdapter(context,getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private ArrayList<Information> getData() {
        DataSet.addData(db,helper);
        return DataSet.data;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id==R.id.action_reset){
            helper=new DBHelper(context,2);
            helper.getWritableDatabase();
            helper=new DBHelper(context);
            db=helper.getWritableDatabase();
            startActivity(new Intent(context,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
        return super.onOptionsItemSelected(item);
    }

    public void spendClick(View view) {
        int spendAmount=0;
        if(spentAmountEdit!=null) {
            if (!String.valueOf(spentAmountEdit.getText()).equalsIgnoreCase(""))
                spendAmount = Integer.parseInt(String.valueOf(spentAmountEdit.getText()));
        }
        String spendDetail="";
        if(spentDetailEdit!=null) {
            spendDetail=String.valueOf(spentDetailEdit.getText());
            spendDetail+=" "+CalendarStuff.getDetail();
        }
        if(DataSet.data.size()>0 && spendAmount!=0) {

            /*DataSet.data.get(0).spendAmount.add(0,spendAmount);
            DataSet.data.get(0).spendDetail.add(0,spendDetail);
            DataSet.data.get(0).spent+=DataSet.data.get(0).spendAmount.get(0);
            DataSet.data.get(0).savedAmount=DataSet.data.get(0).previousAmount-DataSet.data.get(0).spent;*/
            //Log.d("App","spent="+DataSet.data.get(0).spent);
            String month=DataSet.data.get(0).month;
            ContentValues values=new ContentValues();
            values.put(helper.COLUMN_SPEND_AMOUNT, spendAmount);
            values.put(helper.COLUMN_SPEND_DETAIL, spendDetail);
            db.insert(month,null,values);

            int spent,saved;
            spent=DataSet.data.get(0).spent+spendAmount;
            saved=DataSet.data.get(0).savedAmount-spendAmount;
            values=new ContentValues();
            values.put(helper.COLUMN_SAVED_AMOUNT,saved);
            values.put(helper.COLUMN_TOTAL_SPENT_AMOUNT,spent);
            String selection=helper.COLUMN_MONTH+ " LIKE ?";
            String[] selectionArgs={month};
            db.update(helper.MASTER_TABLE,values,selection,selectionArgs);
            DataSet.data=getData();
        }
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        MonthRecyclerAdapter adapter=new MonthRecyclerAdapter(context,DataSet.data);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(spentAmountEdit.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
        imm.hideSoftInputFromWindow(spentDetailEdit.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
        spentAmountEdit.setText("");
        spentDetailEdit.setText("");
    }

    public void deleteSpendClick(View view) {

        Snackbar.make(view,"Confirm delete",Snackbar.LENGTH_LONG).setAction("Yes", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DataSet.data.size()>0) {
                    if(DataSet.data.get(0).spendAmount.size()>0) {
                        String month=DataSet.data.get(0).month;

                        int spent,saved;
                        spent=DataSet.data.get(0).spent-DataSet.data.get(0).spendAmount.get(0);
                        saved=DataSet.data.get(0).savedAmount+DataSet.data.get(0).spendAmount.get(0);
                        ContentValues values=new ContentValues();
                        values.put(helper.COLUMN_SAVED_AMOUNT,saved);
                        values.put(helper.COLUMN_TOTAL_SPENT_AMOUNT, spent);
                        String selection=helper.COLUMN_MONTH+ " LIKE ?";
                        String[] selectionArgs={month};
                        db.update(helper.MASTER_TABLE, values, selection, selectionArgs);

                        Cursor monthCursor=db.query(month, new String[]{helper.ID}, null, null, null, null, null);
                        monthCursor.moveToLast();
                        int id= Integer.parseInt(monthCursor.getString(monthCursor.getColumnIndex(helper.ID)));
                        selection=helper.ID+" LIKE ?";
                        selectionArgs= new String[]{String.valueOf(id)};
                        monthCursor.close();
                        db.delete(month,selection,selectionArgs);
                        DataSet.data=getData();
                    }
                }
                DataSet.data=getData();
                recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
                MonthRecyclerAdapter adapter=new MonthRecyclerAdapter(context,DataSet.data);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            }
        }).show();

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(spentAmountEdit.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
        imm.hideSoftInputFromWindow(spentDetailEdit.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
        spentAmountEdit.setText("");
        spentDetailEdit.setText("");
    }
}
