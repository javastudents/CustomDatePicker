package datepicker.custom.android.com.datepicker;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Date;

import datepicker.custom.android.com.customdatepicker.R;

public class MainActivity extends Activity implements View.OnClickListener
{

    private Button btnDate,btnDateTime;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initListener();
    }

    private void initViews()
    {
        btnDate = (Button)findViewById(R.id.year_month_day);
        btnDateTime = (Button)findViewById(R.id.year_month_day_hour_min);
    }
    private void initListener()
    {
        btnDate.setOnClickListener(this);
        btnDateTime.setOnClickListener(this);
    }


    @Override
    public void onClick(View v)
    {
     switch (v.getId()){
         case R.id.year_month_day:
             DateTimeDialog.showDateTimePicker(this, null, "测试日期", CustomDateTimePickerDilog.FLAG_YEAR_MONTH_DAY, new CustomDateTimePickerDilog.OnDatePickerClickListener()
             {
                 @Override
                 public void onComplete(String response)
                 {
                     btnDate.setText(response);
                 }
             });
             break;
         case R.id.year_month_day_hour_min:
             SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
             DateTimeDialog.showDateTimePicker(this, dateFormat.format(new Date()), "测试日期", CustomDateTimePickerDilog.FLAG_YEAR_MONTH_DAY_HOUR_MIN, new CustomDateTimePickerDilog.OnDatePickerClickListener()
             {
                 @Override
                 public void onComplete(String response)
                 {
                     btnDateTime.setText(response);
                 }
             });
             break;
     }
    }
}
