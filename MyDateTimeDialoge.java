package com.icszx.starcity.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.icszx.starcity.R;
import com.icszx.starcity.utils.TimeUtils;
import com.liuyi.androidlibary.utils.ToastUtil;

import java.util.Calendar;
import java.util.Date;

/**
 * 时间选择控件
 * 
 * @author 等待
 * @class MyDateTimeDialoge.java
 * @time 2015年2月4日 下午3:38:17
 * @QQ 2743569843
 */
public class MyDateTimeDialoge {

	private static Button btn_year_subtract;
	private static EditText ed_year;
	private static Button btn_year_add;

	private static Button btn_month_subtract;
	private static EditText ed_month;
	private static Button btn_month_add;

	private static Button btn_day_subtract;
	private static EditText ed_day;
	private static Button btn_day_add;

	private static Button btn_hour_subtract;
	private static EditText ed_hour;
	private static Button btn_hour_add;

	private static Button btn_minutes_subtract;
	private static EditText ed_minutes;
	private static Button btn_minutes_add;

	private static Button btn_confirm;
	private static Button btn_cancel;

	private static Calendar cal;

	public static final void showDialoge(final Context context, String time, boolean isData, final TextView tv_time) {
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.getWindow().setBackgroundDrawableResource(R.drawable.common_left_bg_nomal);
		dialog.setContentView(R.layout.common_date_dialoge);
		btn_year_subtract = (Button) dialog.findViewById(R.id.btn_year_subtract);
		ed_year = (EditText) dialog.findViewById(R.id.ed_year);
		btn_year_add = (Button) dialog.findViewById(R.id.btn_year_add);
		btn_month_subtract = (Button) dialog.findViewById(R.id.btn_month_subtract);
		ed_month = (EditText) dialog.findViewById(R.id.ed_month);
		btn_month_add = (Button) dialog.findViewById(R.id.btn_month_add);
		btn_day_subtract = (Button) dialog.findViewById(R.id.btn_day_subtract);
		ed_day = (EditText) dialog.findViewById(R.id.ed_day);
		btn_day_add = (Button) dialog.findViewById(R.id.btn_day_add);
		btn_hour_subtract = (Button) dialog.findViewById(R.id.btn_hour_subtract);
		ed_hour = (EditText) dialog.findViewById(R.id.ed_hour);
		btn_hour_add = (Button) dialog.findViewById(R.id.btn_hour_add);
		btn_minutes_subtract = (Button) dialog.findViewById(R.id.btn_minutes_subtract);
		ed_minutes = (EditText) dialog.findViewById(R.id.ed_minutes);
		btn_minutes_add = (Button) dialog.findViewById(R.id.btn_minutes_add);
		
		btn_confirm = (Button) dialog.findViewById(R.id.btn_confirm);
		btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
		
		LinearLayout line_year = (LinearLayout) dialog.findViewById(R.id.line_year);
		LinearLayout line_month = (LinearLayout) dialog.findViewById(R.id.line_month);
		LinearLayout line_day = (LinearLayout) dialog.findViewById(R.id.line_day);
		
		if(isData){
			line_year.setVisibility(View.VISIBLE);
			line_month.setVisibility(View.VISIBLE);
			line_day.setVisibility(View.VISIBLE);
		}else{
			line_year.setVisibility(View.GONE);
			line_month.setVisibility(View.GONE);
			line_day.setVisibility(View.GONE);
		}

		cal = Calendar.getInstance();
		if (time.trim().length() > 0) {
			cal.set(Calendar.YEAR, TimeUtils.getYear(time));
			cal.set(Calendar.MONTH, TimeUtils.getMonth(time) - 1);
			cal.set(Calendar.DAY_OF_MONTH, TimeUtils.getDay(time));
			cal.set(Calendar.HOUR_OF_DAY, TimeUtils.getHour(time));
			cal.set(Calendar.MINUTE, TimeUtils.getMinute(time));
		}
		ed_month.setText(String.valueOf(cal.get(Calendar.MONTH) + 1));
		ed_year.setText(String.valueOf(cal.get(Calendar.YEAR)));
		ed_day.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
		ed_hour.setText(String.valueOf(cal.get(Calendar.HOUR_OF_DAY)));
		ed_minutes.setText(String.valueOf(cal.get(Calendar.MINUTE)));

		btn_year_subtract.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cal.add(Calendar.YEAR, -1);
				ed_month.setText(String.valueOf(cal.get(Calendar.MONTH) + 1));
				ed_year.setText(String.valueOf(cal.get(Calendar.YEAR)));
				ed_day.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
			}
		});
		btn_year_add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cal.add(Calendar.YEAR, +1);
				ed_month.setText(String.valueOf(cal.get(Calendar.MONTH) + 1));
				ed_year.setText(String.valueOf(cal.get(Calendar.YEAR)));
				ed_day.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
			}
		});
		btn_month_subtract.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cal.add(Calendar.MONTH, -1);
				ed_month.setText(String.valueOf(cal.get(Calendar.MONTH) + 1));
				ed_year.setText(String.valueOf(cal.get(Calendar.YEAR)));
				ed_day.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
			}
		});

		btn_month_add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cal.add(Calendar.MONTH, 1);
				ed_month.setText(String.valueOf(cal.get(Calendar.MONTH) + 1));
				ed_year.setText(String.valueOf(cal.get(Calendar.YEAR)));
				ed_day.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
			}
		});

		btn_day_subtract.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cal.add(Calendar.DAY_OF_MONTH, -1);
				ed_month.setText(String.valueOf(cal.get(Calendar.MONTH) + 1));
				ed_year.setText(String.valueOf(cal.get(Calendar.YEAR)));
				ed_day.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
			}
		});
		btn_day_add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cal.add(Calendar.DAY_OF_MONTH, 1);
				ed_month.setText(String.valueOf(cal.get(Calendar.MONTH) + 1));
				ed_year.setText(String.valueOf(cal.get(Calendar.YEAR)));
				ed_day.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
			}
		});

		btn_hour_subtract.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cal.add(Calendar.HOUR_OF_DAY, -1);
				ed_hour.setText(String.valueOf(cal.get(Calendar.HOUR_OF_DAY)));
			}
		});

		btn_hour_add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cal.add(Calendar.HOUR_OF_DAY, 1);
				ed_hour.setText(String.valueOf(cal.get(Calendar.HOUR_OF_DAY)));
			}
		});

		btn_minutes_subtract.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cal.add(Calendar.MINUTE, -1);
				ed_minutes.setText(String.valueOf(cal.get(Calendar.MINUTE)));
			}
		});

		btn_minutes_add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cal.add(Calendar.MINUTE, 1);
				ed_minutes.setText(String.valueOf(cal.get(Calendar.MINUTE)));
			}
		});

		btn_confirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String time=cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + " " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);
				long times=TimeUtils.getTimeToTimeStamp(time);
				long now= new Date().getTime()/1000;
				if(times<now){
					ToastUtil.showToast(context,"必选选择未来的时间点");
					return;
				}else{
				tv_time.setText(time);
				dialog.dismiss();}
			}
		});
		btn_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}
}
