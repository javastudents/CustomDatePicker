package datepicker.custom.android.com.datepicker;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import datepicker.custom.android.com.customdatepicker.R;
import datepicker.custom.android.com.datepicker.wheelview.OnWheelScrollListener;
import datepicker.custom.android.com.datepicker.wheelview.WheelView;
import datepicker.custom.android.com.datepicker.wheelview.adapter.NumericWheelAdapter;


public class CustomDateTimePickerDilog extends Dialog
{
    public static final int FLAG_YEAR_MONTH_DAY_HOUR_MIN = 5;
    public static final int FLAG_YEAR_MONTH_DAY = 3;
    private int flag = 0;
    public String selectedTime;

    private Context context;

    private int curYear, curMonth;

    private int[] timeInt;

    private LayoutInflater mInflater;
    private View dateView;
    private WheelView yearView;
    private WheelView monthView;
    private WheelView dayView;
    private WheelView hourView;
    private WheelView minView;
    //    private WheelView secView;
    //监听器，用作返回结果值
    private OnDatePickerClickListener listener;

    public CustomDateTimePickerDilog(Context context, String defaultTime, String title, int flag, OnDatePickerClickListener listener)
    {
        super(context, R.style.ActionSheet);
        this.flag = flag;
        this.context = context;
        this.listener = listener;
        setStartTime(defaultTime);
        initWindow(title);
        initWheel();
    }

    /*
    将原有的日期计算出来并对赋值的动作
     */
    private void setStartTime(String startTime)
    {
        // 当时间参数为空时，设置默认值
        if (TextUtils.isEmpty(startTime))
        {
            startTime = dateFormat(new Date(), "yyyy-MM-dd");
        }

        StringBuilder builder = new StringBuilder(startTime);
        //统一格式化日期
        builder.replace(4, 5, "-").replace(7, 8, "-");

        if (flag == FLAG_YEAR_MONTH_DAY)
        {
            startTime = toDateValue(builder.toString(), "yyyy-MM-dd", "yyyyMMdd");
            timeInt = new int[FLAG_YEAR_MONTH_DAY];
            timeInt[0] = Integer.valueOf(startTime.substring(0, 4));
            timeInt[1] = Integer.valueOf(startTime.substring(4, 6));
            timeInt[2] = Integer.valueOf(startTime.substring(6, 8));
            //初始化要选择的时间，即使用户没有选择时间，点击“确定”也有默认值
            selectedTime = new StringBuilder()
                    .append(timeInt[0])
                    .append("/")
                    .append((timeInt[1] + 1) <= 10 ? "0"
                            + timeInt[1] : timeInt[1])
                    .append("/")
                    .append(((timeInt[2] + 1) <= 10) ? "0"
                            + (timeInt[2]) : timeInt[2]).toString();

        }
        if (flag == FLAG_YEAR_MONTH_DAY_HOUR_MIN)
        {

            if (builder.length() == 10)
            {
                startTime = toDateValue(builder.toString(), "yyyy-MM-dd", "yyyyMMddHHmm");
            } else
            {
                startTime = toDateValue(builder.toString(), "yyyy-MM-dd HH:mm", "yyyyMMddHHmm");
            }

            timeInt = new int[FLAG_YEAR_MONTH_DAY_HOUR_MIN];
            timeInt[0] = Integer.valueOf(startTime.substring(0, 4));
            timeInt[1] = Integer.valueOf(startTime.substring(4, 6));
            timeInt[2] = Integer.valueOf(startTime.substring(6, 8));
            timeInt[3] = Integer.valueOf(startTime.substring(8, 10));
            timeInt[4] = Integer.valueOf(startTime.substring(10, 12));
            //初始化要选择的时间，即使用户没有选择时间，点击“确定”也有默认值
            selectedTime = new StringBuilder()
                    .append(timeInt[0])
                    .append("/")
                    .append((timeInt[1] + 1) <= 10 ? "0"
                            + timeInt[1] : timeInt[1])
                    .append("/")
                    .append(((timeInt[2] + 1) <= 10) ? "0"
                            + timeInt[2] : timeInt[2])
                    .append(" ")
                    .append(timeInt[3] <= 10 ? "0" + timeInt[3] : timeInt[3])
                    .append(":")
                    .append(timeInt[4] <= 10 ? "0"
                            + timeInt[4] : timeInt[4]).toString();
        }
    }

    private void initWindow(String title)
    {
        mInflater = LayoutInflater.from(context);
        dateView = mInflater.inflate(R.layout.dialog_actionsheet_date_time, null);

//        设置dialog的title
        TextView dialogTitle = (TextView) dateView.findViewById(R.id.dialog_title);
        LinearLayout linearLayout = (LinearLayout) dateView.findViewById(R.id.ll_title);
        if (TextUtils.isEmpty(title))
        {
            linearLayout.setVisibility(View.GONE);
        } else
        {
            dialogTitle.setText(title);
            dialogTitle.setTextColor(Color.parseColor("#A6A6A6"));
            dialogTitle.setGravity(Gravity.CENTER);
        }

        yearView = (WheelView) dateView.findViewById(R.id.year);
        monthView = (WheelView) dateView.findViewById(R.id.month);
        dayView = (WheelView) dateView.findViewById(R.id.day);
        hourView = (WheelView) dateView.findViewById(R.id.hour);
        minView = (WheelView) dateView.findViewById(R.id.min);
//        secView = (WheelView) dateView.findViewById(R.id.sec);
        if (flag == FLAG_YEAR_MONTH_DAY)
        {
            hourView.setVisibility(View.GONE);
            minView.setVisibility(View.GONE);
        }
//        secView.setVisibility(View.GONE);

        TextView cancle = (TextView) dateView.findViewById(R.id.cancle);
        TextView confirm = (TextView) dateView.findViewById(R.id.confirm);

        cancle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CustomDateTimePickerDilog.this.dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CustomDateTimePickerDilog.this.dismiss();
                if (!TextUtils.isEmpty(selectedTime))
                {
                    listener.onComplete(selectedTime);
                    CustomDateTimePickerDilog.this.dismiss();
                }
            }
        });
    }

    private void initWheel()
    {
        Calendar calendar = Calendar.getInstance();
        curYear = calendar.get(Calendar.YEAR);
        curMonth = calendar.get(Calendar.MONTH) + 1;

        NumericWheelAdapter numericWheelAdapter1 = new NumericWheelAdapter(
                context, curYear, curYear + 10, R.layout.widget_wheelview_item, R.id.tv_item);
        numericWheelAdapter1.setLabel("年");
        yearView.setViewAdapter(numericWheelAdapter1);
        setWheelView(yearView);

        NumericWheelAdapter numericWheelAdapter2 = new NumericWheelAdapter(
                context, 1, 12, "%02d", R.layout.widget_wheelview_item, R.id.tv_item);
        numericWheelAdapter2.setLabel("月");
        monthView.setViewAdapter(numericWheelAdapter2);
        setWheelView(monthView);

        NumericWheelAdapter numericWheelAdapter3 = new NumericWheelAdapter(
                context, 1, getDay(curYear, curMonth), "%02d", R.layout.widget_wheelview_item, R.id.tv_item);
        numericWheelAdapter3.setLabel("日");
        dayView.setViewAdapter(numericWheelAdapter3);
        setWheelView(dayView);

        if (flag == FLAG_YEAR_MONTH_DAY_HOUR_MIN)
        {
            NumericWheelAdapter numericWheelAdapter4 = new NumericWheelAdapter(
                    context, 0, 23, "%02d", R.layout.widget_wheelview_item, R.id.tv_item);
            numericWheelAdapter4.setLabel("时");
            hourView.setViewAdapter(numericWheelAdapter4);
            setWheelView(hourView);
//自定义adapter
            NumericWheelAdapter numericWheelAdapter5 = new NumericWheelAdapter(
                    context, 0, 59, "%02d", R.layout.widget_wheelview_item, R.id.tv_item);
            numericWheelAdapter5.setLabel("分");
            minView.setViewAdapter(numericWheelAdapter5);
            setWheelView(minView);
            hourView.setCurrentItem(timeInt[3]);
            minView.setCurrentItem(timeInt[4]);
        }

        yearView.setCurrentItem(timeInt[0] - curYear);
        monthView.setCurrentItem(timeInt[1] - 1);
        dayView.setCurrentItem(timeInt[2] - 1);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dateView.setLayoutParams(layoutParams);
        ColorDrawable dw = new ColorDrawable(0xFFFFFFFF);
        dateView.setBackgroundDrawable(dw);
        dateView.setFocusable(true);
        setContentView(dateView);
    }

    //    初始化wheelView
    private void setWheelView(WheelView wheelView)
    {
        wheelView.setDrawShadows(false);
        wheelView.setVisibleItems(3);
        wheelView.setCyclic(true);
        wheelView.addScrollingListener(scrollListener);
//        设置横线颜色
        wheelView.setLineColor(R.color.main_color);
    }

    //    每次滚动获得日期
    OnWheelScrollListener scrollListener = new OnWheelScrollListener()
    {
        @Override
        public void onScrollingStarted(WheelView wheel)
        {

        }

        @Override
        public void onScrollingFinished(WheelView wheel)
        {

            int n_year = yearView.getCurrentItem() + curYear;
            int n_month = monthView.getCurrentItem() + 1;

            initDay(n_year, n_month);
            selectedTime = new StringBuilder()
                    .append((yearView.getCurrentItem() + curYear))
                    .append("/")
                    .append((monthView.getCurrentItem() + 1) < 10 ? "0"
                            + (monthView.getCurrentItem() + 1) : (monthView
                            .getCurrentItem() + 1))
                    .append("/")
                    .append(((dayView.getCurrentItem() + 1) < 10) ? "0"
                            + (dayView.getCurrentItem() + 1) : (dayView
                            .getCurrentItem() + 1)).toString();

            if (flag == FLAG_YEAR_MONTH_DAY_HOUR_MIN)
            {
                selectedTime = new StringBuilder()
                        .append(selectedTime)
                        .append(" ")
                        .append(hourView.getCurrentItem() < 10 ? "0" + hourView.getCurrentItem() : hourView.getCurrentItem())
                        .append(":")
                        .append(minView.getCurrentItem() < 10 ? "0"
                                + minView.getCurrentItem() : minView
                                .getCurrentItem()).toString();
            }
        }
    };

    //每次滑动初始化“日”
    private void initDay(int arg1, int arg2)
    {

        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(
                context, 1, getDay(arg1, arg2), "%02d", R.layout.widget_wheelview_item, R.id.tv_item);
        numericWheelAdapter.setLabel("日");
        dayView.setViewAdapter(numericWheelAdapter);
    }

    //    不同年份不同月份的天数
    private int getDay(int year, int month)
    {
        int day = 30;
        boolean flag = false;
        switch (year % 4)
        {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month)
        {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                day = flag ? 29 : 28;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }


    /*
    监听器，点击确认时返回日期值
     */
    public interface OnDatePickerClickListener
    {
        public void onComplete(String response);
    }

    /**
     * 格式化日期显示格式
     * 使用须知： dateString的长度要大于等于dateFormat 比如 如果dateString是2013-08-20 20:23 则dateFormat应是yyyy-MM-dd HH:mm 或 yyyy-MM-dd HH:mm:ss 或 yyyy-MM-dd HH:mm:ss:SSS其中一个
     *
     * @param dateString  原始日期数据  日期分隔符必须是“-”（yyyy-MM-dd 或 yyyy-MM-dd HH:mm 或 yyyy-MM-dd HH:mm:ss 或 yyyy-MM-dd HH:mm:ss:SSS其中一个）
     * @param dateFormat  格式化后日期格式 日期分隔符必须是“-”（yyyy-MM-dd 或 yyyy-MM-dd HH:mm 或 yyyy-MM-dd HH:mm:ss 或 yyyy-MM-dd HH:mm:ss:SSS其中一个）
     * @param valueFormat 转化成的日期字符串格式 yyyyMMmm 或 yyyyMMmmHH 或 yyyyMMmmHHmm 或 yyyyMMmmHHmmss 或 yyyyMMmmHHmmssSSS
     * @return 格式化后的日期显示
     */
    public static String toDateValue(String dateString, String dateFormat, String valueFormat)
    {
        return dateFormat(dateParse(dateString, dateFormat), valueFormat);
    }

    public static Date dateParse(String date, String format)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date d = null;
        try
        {
            d = formatter.parse(date);

        } catch (ParseException e)
        {

            e.printStackTrace();
        }
        return d;
    }

    public static String dateFormat(Date timestamp, String format)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String d = formatter.format(timestamp);
        return d;
    }
}
