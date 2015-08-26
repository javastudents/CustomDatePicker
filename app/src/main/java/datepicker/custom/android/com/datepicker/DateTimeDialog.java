package datepicker.custom.android.com.datepicker;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;

/**
 * Created by Jinjin on 2015/8/26.
 */
public class DateTimeDialog
{
    /**
     * @param @param  context
     * @param @param  defaultTime 要显示的初始时间
     * @param @param  title dialogTitle
     * @param @param  flag 3 DateTimePickerDilog.FLAG_YEAR_MONTH_DAY_HOUR_MIN代表年月日 5 DateTimePickerDilog.FLAG_YEAR_MONTH_DAY代表年月日时分
     * @param @param  datePickerClickListener 点击确定的回调事件
     * @param @return
     * @return Dialog
     * @throws
     * @Title: showDateTimePicker
     * @Description: 显示提示框
     */

    public static Dialog showDateTimePicker(Context context, String defaultTime,String title, int flag, CustomDateTimePickerDilog.OnDatePickerClickListener datePickerClickListener)
    {
        CustomDateTimePickerDilog dateTimePickerDilog = new CustomDateTimePickerDilog(context, defaultTime,title, flag, datePickerClickListener);
        // 获取屏幕宽度
        Window window = dateTimePickerDilog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        dateTimePickerDilog.show();
        return dateTimePickerDilog;
    }
}
