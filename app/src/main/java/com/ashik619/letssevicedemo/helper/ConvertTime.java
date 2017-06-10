package com.ashik619.letssevicedemo.helper;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ashik619 on 12-03-2016.
 */
public class ConvertTime {

    private String dataTime;
    private String mainDate;
    private DateFormat dateFormat;

    public ConvertTime(String dateTime) {
        this.dataTime = dateTime;
        check();
    }

    private void check() {
        DateFormat formatter = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
        mainDate = formatter.format(new Date(Long.parseLong(dataTime) * 1000L));
    }

    public String check_mili() {
        DateFormat formatter = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
        mainDate = formatter.format(new Date(Long.parseLong(dataTime)));
        return mainDate;
    }

    public String getTime() {
        return mainDate.toString();
    }

    /*public String getSureTime(int IS_WHOLE_DATE) {

        DateFormat formatter = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
        mainDate = formatter.format(new Date(Long.parseLong(dataTime) * 1000L));

        String arr[] = mainDate.split(" ");

        if (IS_WHOLE_DATE == 0) {
            return arr[1];
        } else {
            return mainDate.toString();
        }
    }*/
    public String getSureTime(int IS_WHOLE_DATE) {

        Calendar cl = Calendar.getInstance();
        cl.setTimeInMillis(Long.parseLong(dataTime));  //here your time in miliseconds
        String am_pm;
        if (cl.get(Calendar.AM_PM ) == 0){
            am_pm = "AM";
        }else am_pm = "PM";
        String date = "" + cl.get(Calendar.DAY_OF_MONTH) + "/" + (cl.get(Calendar.MONTH)+1) + "/" + cl.get(Calendar.YEAR)
                + " at "+cl.get(Calendar.HOUR)+":"+cl.get(Calendar.MINUTE)+":"+cl.get(Calendar.SECOND) +am_pm;
        return date;
    }


    public String convertWeek() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(Long.parseLong(dataTime)));

        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        switch (dayOfWeek) {
            case 1: {
                return "Mon";
            }

            case 2: {
                return "Tues";
            }

            case 3: {
                return "Wed";
            }

            case 4: {
                return "Thrus";
            }

            case 5: {
                return "Fri";
            }

            case 6: {
                return "Sat";
            }

            case 7: {
                return "Sun";
            }

        }

        return "Sun";

    }

}
