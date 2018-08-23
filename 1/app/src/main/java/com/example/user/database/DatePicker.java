package com.example.user.database;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog.Builder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewAnimator;
import com.kunzisoft.switchdatetime.R.id;
import com.kunzisoft.switchdatetime.R.layout;
import com.kunzisoft.switchdatetime.R.string;
import com.kunzisoft.switchdatetime.R.style;
import com.kunzisoft.switchdatetime.Utils;
import com.kunzisoft.switchdatetime.date.OnYearSelectedListener;
import com.kunzisoft.switchdatetime.date.widget.ListPickerYearView;
import com.kunzisoft.switchdatetime.time.SwitchTimePicker;
import com.kunzisoft.switchdatetime.time.SwitchTimePicker.OnTimeSelectedListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatePicker extends DialogFragment {
    private static final String TAG = "SwitchDateTimeDialogFrg";
    private static final String STATE_DATETIME = "STATE_DATETIME";
    private static final String STATE_CURRENT_POSITION = "STATE_CURRENT_POSITION";
    private static final int UNDEFINED_POSITION = -1;
    private Calendar dateTimeCalendar = Calendar.getInstance();
    private Calendar minimumDateTime = new GregorianCalendar(1970, 1, 1);
    private Calendar maximumDateTime = new GregorianCalendar(2200, 1, 1);
    private TimeZone timeZone = TimeZone.getDefault();
    private static final String TAG_LABEL = "LABEL";
    private static final String TAG_POSITIVE_BUTTON = "POSITIVE_BUTTON";
    private static final String TAG_NEGATIVE_BUTTON = "NEGATIVE_BUTTON";
    private static final String TAG_NEUTRAL_BUTTON = "NEUTRAL_BUTTON";
    private String mLabel;
    private String mPositiveButton;
    private String mNegativeButton;
    private String mNeutralButton;
    private DatePicker.OnButtonClickListener mListener;
    private boolean is24HoursMode = false;
    private boolean highlightAMPMSelection = false;
    private int startAtPosition = -1;
    private int currentPosition = 0;
    private int alertStyleId;
    private SimpleDateFormat dayAndMonthSimpleDate;
    private SimpleDateFormat yearSimpleDate;
    private ViewAnimator viewSwitcher;
    private SwitchTimePicker timePicker;
    private MaterialCalendarView materialCalendarView;
    private ListPickerYearView listPickerYearView;
    private TextView monthAndDayHeaderValues;
    private TextView yearHeaderValues;
    private boolean blockAnimationIn;
    private boolean blockAnimationOut;

    public DatePicker() {
    }

    public static DatePicker newInstance(String label, String positiveButton, String negativeButton)
    {
        return newInstance(label, positiveButton, negativeButton, (String)null);
    }

    public static DatePicker newInstance(String label, String positiveButton, String negativeButton, String neutralButton) {
        DatePicker switchDateTimeDialogFragment = new DatePicker();
        Bundle args = new Bundle();
        args.putString("LABEL", label);
        args.putString("POSITIVE_BUTTON", positiveButton);
        args.putString("NEGATIVE_BUTTON", negativeButton);
        if (neutralButton != null) {
            args.putString("NEUTRAL_BUTTON", neutralButton);
        }

        switchDateTimeDialogFragment.setArguments(args);
        return switchDateTimeDialogFragment;
    }

    public void setOnButtonClickListener(DatePicker.OnButtonClickListener onButtonClickListener) {
        this.mListener = onButtonClickListener;
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putLong("STATE_DATETIME", this.dateTimeCalendar.getTimeInMillis());
        savedInstanceState.putInt("STATE_CURRENT_POSITION", this.currentPosition);
        this.timePicker.onSaveInstanceState(savedInstanceState);
        super.onSaveInstanceState(savedInstanceState);
    }

    @SuppressLint({"WrongConstant", "ResourceType"})
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        super.onCreateDialog(savedInstanceState);

        assert this.getActivity() != null;

        assert this.getContext() != null;

        this.dateTimeCalendar.setTimeZone(this.timeZone);
        if (this.getArguments() != null) {
            this.mLabel = this.getArguments().getString("LABEL");
            this.mPositiveButton = this.getArguments().getString("POSITIVE_BUTTON");
            this.mNegativeButton = this.getArguments().getString("NEGATIVE_BUTTON");
            this.mNeutralButton = this.getArguments().getString("NEUTRAL_BUTTON");
        }

        if (savedInstanceState != null)
        {
            this.currentPosition = savedInstanceState.getInt("STATE_CURRENT_POSITION");
            this.dateTimeCalendar.setTime(new Date(savedInstanceState.getLong("STATE_DATETIME")));
        }

        if (!this.dateTimeCalendar.before(this.minimumDateTime) &&
                !this.dateTimeCalendar.after(this.maximumDateTime))
        {
            LayoutInflater inflater = LayoutInflater.from(this.getActivity());
            this.getActivity().getTheme().applyStyle(style.Theme_SwitchDateTime, false);
            View dateTimeLayout = inflater.inflate(layout.dialog_switch_datetime_picker, (ViewGroup)this.getActivity().findViewById(id.datetime_picker));
            TextView labelView = (TextView)dateTimeLayout.findViewById(id.label);
            if (this.mLabel != null) {
                labelView.setText(this.mLabel);
            } else {
                labelView.setText(this.getString(string.label_datetime_dialog));
            }

            this.blockAnimationIn = false;
            this.blockAnimationOut = false;
            this.viewSwitcher = (ViewAnimator)dateTimeLayout.findViewById(id.dateSwitcher);
            this.viewSwitcher.getInAnimation().setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                    DatePicker.this.blockAnimationIn = true;
                }

                public void onAnimationEnd(Animation animation) {
                    DatePicker.this.blockAnimationIn = false;
                    DatePicker.this.currentPosition = DatePicker.this.viewSwitcher.getDisplayedChild();
                }

                public void onAnimationRepeat(Animation animation) {
                }
            });
            this.viewSwitcher.getOutAnimation().setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                    DatePicker.this.blockAnimationOut = true;
                }

                public void onAnimationEnd(Animation animation) {
                    DatePicker.this.blockAnimationOut = false;
                }

                public void onAnimationRepeat(Animation animation) {
                }
            });
            if (this.startAtPosition != -1) {
                this.currentPosition = this.startAtPosition;
            }

            this.viewSwitcher.setDisplayedChild(this.currentPosition);
            ImageButton buttonSwitch = (ImageButton)dateTimeLayout.findViewById(id.button_switch);
            buttonSwitch.setBackgroundColor(0);
            buttonSwitch.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    Utils.animLabelElement(view);
                    if (!DatePicker.this.blockAnimationIn || !DatePicker.this.blockAnimationOut) {
                        DatePicker.this.viewSwitcher.showNext();
                    }

                }
            });
            View timeHeaderValues = dateTimeLayout.findViewById(id.time_header_values);
            OnClickListener onTimeClickListener = new DatePicker.OnClickHeaderElementListener(DatePicker.HeaderViewsPosition.VIEW_HOURS_AND_MINUTES.getPosition());
            timeHeaderValues.setOnClickListener(onTimeClickListener);
            this.monthAndDayHeaderValues = (TextView)dateTimeLayout.findViewById(id.date_picker_month_and_day);
            OnClickListener onMonthAndDayClickListener = new DatePicker.OnClickHeaderElementListener(DatePicker.HeaderViewsPosition.VIEW_MONTH_AND_DAY.getPosition());
            this.monthAndDayHeaderValues.setOnClickListener(onMonthAndDayClickListener);
            this.yearHeaderValues = (TextView)dateTimeLayout.findViewById(id.date_picker_year);
            OnClickListener onYearClickListener = new DatePicker.OnClickHeaderElementListener(DatePicker.HeaderViewsPosition.VIEW_YEAR.getPosition());
            this.yearHeaderValues.setOnClickListener(onYearClickListener);
            if (this.dayAndMonthSimpleDate == null) {
                this.dayAndMonthSimpleDate = new SimpleDateFormat("MMMM dd", Locale.getDefault());
            }

            if (this.yearSimpleDate == null) {
                this.yearSimpleDate = new SimpleDateFormat("yyyy", Locale.getDefault());
            }

            this.dayAndMonthSimpleDate.setTimeZone(this.timeZone);
            this.yearSimpleDate.setTimeZone(this.timeZone);
            this.yearHeaderValues.setText(this.yearSimpleDate.format(this.dateTimeCalendar.getTime()));
            this.monthAndDayHeaderValues.setText(this.dayAndMonthSimpleDate.format(this.dateTimeCalendar.getTime()));
            OnTimeSelectedListener onTimeSelectedListener = new OnTimeSelectedListener() {
                @SuppressLint("WrongConstant")
                public void onTimeSelected(int hourOfDayTime, int minuteTime) {
                    DatePicker.this.dateTimeCalendar.set(11, hourOfDayTime);
                    DatePicker.this.dateTimeCalendar.set(12, minuteTime);
                }
            };
            this.timePicker = new SwitchTimePicker(this.getContext(), onTimeSelectedListener, savedInstanceState);
            this.timePicker.setIs24HourMode(this.is24HoursMode);
            this.timePicker.setHighlightAMPMSelection(this.highlightAMPMSelection);
            this.timePicker.setHourOfDay(this.dateTimeCalendar.get(11));
            this.timePicker.setMinute(this.dateTimeCalendar.get(12));
            this.timePicker.onCreateView(dateTimeLayout, savedInstanceState);
            this.timePicker.setOnClickTimeListener(onTimeClickListener);
            this.materialCalendarView = (MaterialCalendarView)dateTimeLayout.findViewById(id.datePicker);
            this.materialCalendarView.state().edit().setMinimumDate(CalendarDay.from(this.minimumDateTime)).setMaximumDate(CalendarDay.from(this.maximumDateTime)).commit();
            this.materialCalendarView.setCurrentDate(this.dateTimeCalendar);
            this.materialCalendarView.setDateSelected(this.dateTimeCalendar, true);
            this.materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
                public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay calendarDay, boolean selected) {
                    DatePicker.this.dateTimeCalendar.set(1, calendarDay.getYear());
                    DatePicker.this.dateTimeCalendar.set(2, calendarDay.getMonth());
                    DatePicker.this.dateTimeCalendar.set(5, calendarDay.getDay());
                    DatePicker.this.listPickerYearView.assignCurrentYear(calendarDay.getYear());
                    DatePicker.this.yearHeaderValues.setText(DatePicker.this.yearSimpleDate.format(DatePicker.this.dateTimeCalendar.getTime()));
                    DatePicker.this.monthAndDayHeaderValues.setText(DatePicker.this.dayAndMonthSimpleDate.format(DatePicker.this.dateTimeCalendar.getTime()));
                    DatePicker.this.timePicker.clickHour();
                }
            });
            this.materialCalendarView.invalidate();
            this.listPickerYearView = (ListPickerYearView)dateTimeLayout.findViewById(id.yearPicker);
            this.listPickerYearView.setMinYear(this.minimumDateTime.get(1));
            this.listPickerYearView.setMaxYear(this.maximumDateTime.get(1));
            this.listPickerYearView.assignCurrentYear(this.dateTimeCalendar.get(1));
            this.listPickerYearView.setDatePickerListener(new OnYearSelectedListener() {
                public void onYearSelected(View view, int yearPicker) {
                    DatePicker.this.dateTimeCalendar.set(1, yearPicker);
                    DatePicker.this.yearHeaderValues.setText(DatePicker.this.yearSimpleDate.format(DatePicker.this.dateTimeCalendar.getTime()));
                    DatePicker.this.materialCalendarView.setCurrentDate(DatePicker.this.dateTimeCalendar.getTime());
                    DatePicker.this.materialCalendarView.setDateSelected(DatePicker.this.dateTimeCalendar, true);
                    DatePicker.this.materialCalendarView.goToNext();
                    DatePicker.this.materialCalendarView.goToPrevious();
                }
            });
            Builder db;
            if (this.alertStyleId != 0) {
                db = new Builder(this.getContext(), this.alertStyleId);
            } else {
                db = new Builder(this.getContext());
            }

            db.setView(dateTimeLayout);
            if (this.mPositiveButton == null) {
                this.mPositiveButton = this.getString(17039370);
            }

            db.setPositiveButton(this.mPositiveButton, new android.content.DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (DatePicker.this.mListener != null) {
                        DatePicker.this.mListener.onPositiveButtonClick(DatePicker.this.dateTimeCalendar.getTime());
                    }

                }
            });
            if (this.mNegativeButton == null) {
                this.mNegativeButton = this.getString(17039360);
            }

            db.setNegativeButton(this.mNegativeButton, new android.content.DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (DatePicker.this.mListener != null) {
                        DatePicker.this.mListener.onNegativeButtonClick(DatePicker.this.dateTimeCalendar.getTime());
                    }

                }
            });
            if (this.mNeutralButton != null) {
                db.setNeutralButton(this.mNeutralButton, new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (DatePicker.this.mListener != null && DatePicker.this.mListener instanceof DatePicker.OnButtonWithNeutralClickListener) {
                            ((DatePicker.OnButtonWithNeutralClickListener) DatePicker.this.mListener).onNeutralButtonClick(DatePicker.this.dateTimeCalendar.getTime());
                        }

                    }
                });
            }

            return db.create();
        } else {
            throw new RuntimeException("Default date " + this.dateTimeCalendar.getTime() + " must be between " + this.minimumDateTime.getTime() + " and " + this.maximumDateTime.getTime());
        }
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        this.startAtPosition = -1;
    }

    public void startAtTimeView() {
        this.startAtPosition = DatePicker.HeaderViewsPosition.VIEW_HOURS_AND_MINUTES.getPosition();
    }

    public void startAtCalendarView() {
        this.startAtPosition = DatePicker.HeaderViewsPosition.VIEW_MONTH_AND_DAY.getPosition();
    }

    public void startAtYearView() {
        this.startAtPosition = DatePicker.HeaderViewsPosition.VIEW_YEAR.getPosition();
    }

    public void setDefaultYear(int year) {
        this.dateTimeCalendar.set(1, year);
    }

    /** @deprecated */
    @Deprecated
    public void setYear(int year) {
        this.setDefaultYear(year);
    }

    @SuppressLint("WrongConstant")
    public void setDefaultMonth(int month) {
        this.dateTimeCalendar.set(2, month);
    }

    /** @deprecated */
    @Deprecated
    public void setMonth(int month) {
        this.setDefaultMonth(month);
    }

    @SuppressLint("WrongConstant")
    public void setDefaultDay(int day) {
        this.dateTimeCalendar.set(5, day);
    }

    /** @deprecated */
    @Deprecated
    public void setDay(int day) {
        this.setDefaultDay(day);
    }

    @SuppressLint("WrongConstant")
    public void setDefaultHourOfDay(int hourOfDay) {
        this.dateTimeCalendar.set(11, hourOfDay);
    }

    /** @deprecated */
    @Deprecated
    public void setHour(int hour) {
        this.setDefaultHourOfDay(hour);
    }

    @SuppressLint("WrongConstant")
    public void setDefaultMinute(int minute) {
        this.dateTimeCalendar.set(12, minute);
    }

    /** @deprecated */
    @Deprecated
    public void setMinute(int minute) {
        this.setDefaultMinute(minute);
    }

    @SuppressLint("WrongConstant")
    public int getYear() {
        return this.dateTimeCalendar.get(1);
    }

    @SuppressLint("WrongConstant")
    public int getMonth() {
        return this.dateTimeCalendar.get(2);
    }

    @SuppressLint("WrongConstant")
    public int getDay() {
        return this.dateTimeCalendar.get(5);
    }

    @SuppressLint("WrongConstant")
    public int getHourOfDay() {
        return this.dateTimeCalendar.get(11);
    }

    @SuppressLint("WrongConstant")
    public int getMinute() {
        return this.dateTimeCalendar.get(12);
    }

    public void setDefaultDateTime(Date date) {
        this.dateTimeCalendar.setTime(date);
    }

    public void setMinimumDateTime(Date date) {
        this.minimumDateTime.setTime(date);
    }

    public void setMaximumDateTime(Date date) {
        this.maximumDateTime.setTime(date);
    }

    public Date getMinimumDateTime() {
        return this.minimumDateTime.getTime();
    }

    public Date getMaximumDateTime() {
        return this.maximumDateTime.getTime();
    }

    public SimpleDateFormat getSimpleDateMonthAndDayFormat() {
        return this.dayAndMonthSimpleDate;
    }

    public void setSimpleDateMonthAndDayFormat(SimpleDateFormat simpleDateFormat) throws DatePicker.SimpleDateMonthAndDayFormatException, SimpleDateMonthAndDayFormatException {
        Pattern patternMonthAndDay = Pattern.compile("(M|w|W|D|d|F|E|u|\\s)*");
        Matcher matcherMonthAndDay = patternMonthAndDay.matcher(simpleDateFormat.toPattern());
        if (!matcherMonthAndDay.matches()) {
            throw new DatePicker.SimpleDateMonthAndDayFormatException(simpleDateFormat.toPattern() + "isn't allowed for " + patternMonthAndDay.pattern());
        } else {
            this.dayAndMonthSimpleDate = simpleDateFormat;
        }
    }

    public void set24HoursMode(boolean is24HoursMode) {
        this.is24HoursMode = is24HoursMode;
    }

    public void setHighlightAMPMSelection(boolean highlightAMPMSelection) {
        this.highlightAMPMSelection = highlightAMPMSelection;
    }

    public void setTimeZone(TimeZone timeZone) {
        if (timeZone != null) {
            this.timeZone = timeZone;
        }

    }

    public void setAlertStyle(@StyleRes int styleId) {
        this.alertStyleId = styleId;
    }

    public class OnClickHeaderElementListener implements OnClickListener {
        private int positionView;

        OnClickHeaderElementListener(int positionView) {
            this.positionView = positionView;
        }

        public void onClick(View view) {
            Utils.animLabelElement(view);
            if (DatePicker.this.viewSwitcher.getDisplayedChild() != this.positionView) {
                DatePicker.this.viewSwitcher.setDisplayedChild(this.positionView);
            }

            DatePicker.this.startAtPosition = this.positionView;
        }
    }

    public static enum HeaderViewsPosition {
        VIEW_HOURS_AND_MINUTES(0),
        VIEW_MONTH_AND_DAY(1),
        VIEW_YEAR(2);

        private int positionSwitch;

        private HeaderViewsPosition(int position) {
            this.positionSwitch = position;
        }

        public int getPosition() {
            return this.positionSwitch;
        }
    }

    public interface OnButtonWithNeutralClickListener extends DatePicker.OnButtonClickListener {
        void onNeutralButtonClick(Date var1);
    }

    public interface OnButtonClickListener {
        void onPositiveButtonClick(Date var1);

        void onNegativeButtonClick(Date var1);
    }

    public class SimpleDateMonthAndDayFormatException extends Exception {
        SimpleDateMonthAndDayFormatException(String message) {
            super(message);
        }
    }
}
