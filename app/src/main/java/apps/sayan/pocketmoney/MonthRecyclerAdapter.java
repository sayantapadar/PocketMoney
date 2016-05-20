package apps.sayan.pocketmoney;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by SAYAN on 04-04-2016.
 * <p/>
 * Created by SAYAN on 04-03-2016.
 */
/**
 * Created by SAYAN on 04-03-2016.
 */


public class MonthRecyclerAdapter extends RecyclerView.Adapter<MonthRecyclerAdapter.MyViewHolder> {

    private Context context;

    public ArrayList<Information> data;

    private LayoutInflater inflater;

    public MonthRecyclerAdapter(Context context, ArrayList<Information> data) {

        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int position) {

        View view = inflater.inflate(R.layout.custom_row, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int position) {
        myViewHolder.month.setText(data.get(position).month);
        myViewHolder.previous.setText(data.get(position).previousAmount+"");
        myViewHolder.spent.setText(data.get(position).spent + "");
        myViewHolder.saved.setText(data.get(position).savedAmount + "");

        /*RecyclerView child=myViewHolder.spendRecycler;
        SpendRecyclerAdapter adapter=new SpendRecyclerAdapter(context,data.get(position).spendAmount,data.get(position).spendDetail);
        child.setAdapter(adapter);
        child.setLayoutManager(new LinearLayoutManager(context));*/
        if(data.get(position).spendAmount.size()>0) {
            String expendString="";
            expendString+=data.get(position).spendAmount.get(0).toString()+"\t\t"+data.get(position).spendDetail.get(0);
            for(int i=1; i<data.get(position).spendAmount.size(); i++){
                expendString+="\n"+data.get(position).spendAmount.get(i).toString()+"\t\t"+data.get(position).spendDetail.get(i);
            }
            myViewHolder.expend.setText(expendString);
        }
        if(position>0)
            myViewHolder.expendCard.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView month;
        TextView previous;
        TextView spent;
        TextView saved;
        TextView expend;
        CardView monthCard;
        CardView expendCard;
        public MyViewHolder(View itemView) {
            super(itemView);
            month=(TextView)itemView.findViewById(R.id.month_text);
            previous=(TextView)itemView.findViewById(R.id.previous_amount);
            spent=(TextView)itemView.findViewById(R.id.spent_amount);
            saved=(TextView)itemView.findViewById(R.id.saved_amount);
            expend=(TextView)itemView.findViewById(R.id.expend_text);
            monthCard=(CardView)itemView.findViewById(R.id.month_card);
            expendCard=(CardView)itemView.findViewById(R.id.expend_card);
            monthCard.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.month_card:
                    toggleVisibility(itemView.findViewById(R.id.expend_card));
            }
        }
        public void toggleVisibility(final View v) {
            if(v.isShown()){
                Animations.slide_up(context,v);
            }
            else{
                v.setVisibility(View.VISIBLE);
                Animations.slide_down(context, v);
            }
        }
    }
}