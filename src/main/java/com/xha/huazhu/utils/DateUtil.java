package com.xha.huazhu.utils;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class DateUtil {
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    public static final String YYYYMMDD = "yyyyMMdd";

    public static final String YYYY_MM = "yyyy-MM";

    public static final String DD = "dd";

    public static final String MM = "MM";

    public static final String YYYY = "yyyy";

    public static final String HH_MM = "HH:mm";
    public static final String HH_MM_SS = "HH:mm:ss";


    /**
     * 为时间设置指定小时、分钟和秒
     *
     * @param date        时间
     * @param hour        小时
     * @param minute      分
     * @param millisecond 毫秒
     * @return Date
     */
    public static Date setHourAndMinuteAndSecond(Date date, Integer hour, Integer minute, Integer second, Integer millisecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (hour != null) {
            calendar.set(Calendar.HOUR_OF_DAY, hour);
        }
        if (minute != null) {
            calendar.set(Calendar.MINUTE, minute);
        }
        if (second != null) {
            calendar.set(Calendar.SECOND, second);
        }
        if (millisecond != null) {
            calendar.set(Calendar.MILLISECOND, millisecond);
        }
        return calendar.getTime();
    }

    /**
     * 获取week
     *
     * @param date the date
     * @return int
     */
    public static int getWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取所属当月中的第几天
     *
     * @param date the date
     * @return int
     */
    public static int getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取日期中的小时数
     *
     * @param date the date
     * @return int
     */
    public static int getHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 时间相加
     *
     * @param date the date
     * @param day  the day
     * @return date
     */
    public static Date addDay(Date date, int day) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    /**
     * 格式化时间
     *
     * @param date   the date
     * @param format the format
     * @return string
     */
    public static String formatDateStr(Date date, String format) {
        SimpleDateFormat f = new SimpleDateFormat(format);
        return f.format(date);
    }

    /**
     * 格式化时间
     *
     * @param dateString the dateString
     * @param format     the format
     * @return string
     */
    public static Date formatDate(String dateString, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static List getDates(int year, int month) {
        List dates = new ArrayList();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DATE, 1);
        while (cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH) < month) {
            int day = cal.get(Calendar.DAY_OF_WEEK);
            if (!(day == Calendar.SUNDAY || day == Calendar.SATURDAY)) {
                dates.add(cal.getTime().clone());
            }
            cal.add(Calendar.DATE, 1);
        }
        return dates;

    }

    //获取指定月份的天数
    public static int getDaysByYearMonth(int year, int month) {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    public void dayReport(Date month) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(month);//month 为指定月份任意日期
        int year = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH);
        int dayNumOfMonth = getDaysByYearMonth(year, m);
        cal.set(Calendar.DAY_OF_MONTH, 1);// 从一号开始

        for (int i = 0; i < dayNumOfMonth; i++, cal.add(Calendar.DATE, 1)) {
            Date d = cal.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String df = simpleDateFormat.format(d);
        }
    }


    /**
     * 获取当前时间的一周的开始时间和结束时间
     *
     * @param date
     * @return
     */
    public static List<String> getStartAndEndTime(String date) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // System.out.println("要计算日期为:" + sdf.format(cal.getTime())); // 输出要计算日期
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);

        //获取当天0点时间
        cal.set(Calendar.HOUR_OF_DAY, 0);//控制时
        cal.set(Calendar.MINUTE, 0);//控制分
        cal.set(Calendar.SECOND, 0);//控制秒
        String lastBeginDate = formatDateStr(cal.getTime(), YYYY_MM_DD);
        //获取当天23:59:59点时间
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.add(Calendar.DATE, 6);
        String lastEndDate = formatDateStr(cal.getTime(), YYYY_MM_DD);
        ArrayList<String> list = new ArrayList<>();
        list.add(lastBeginDate);
        list.add(lastEndDate);
        return list;
    }


}
