package com.zzti.lsy.ninetingapp.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具类
 *
 * @author lsy
 */
public class DateUtil {

    /**
     * 判断时间date2是否在时间date1之前 时间格式 xxxx-xx-xx xx:xx
     */
    public static boolean isDateBefore(String date1, String date2,
                                       SimpleDateFormat formatter) {
        try {
            return formatter.parse(date1).before(formatter.parse(date2));
        } catch (ParseException e) {
            System.out.print("[SYS] " + e.getMessage());
            return false;
        }
    }

    /**
     * 查询某年某月的天数
     *
     * @param year
     * @param i
     * @return
     */
    public static int getDayOfMonth(int year, int i) {
        Calendar time = Calendar.getInstance();
        time.clear();
        time.set(Calendar.YEAR, year);
        time.set(Calendar.MONTH, i - 1);// Calendar对象默认一月为0
        int day = time.getActualMaximum(Calendar.DAY_OF_MONTH);// 本月份的天数
        return day;
    }

    /**
     * 将一个一位数变成“01”形式
     */
    public static String change0To01(int i) {
        if (i < 10) {
            return "0" + i;
        }
        return i + "";
    }

    /**
     * 截取子串
     *
     * @param srcStr      源串
     * @param pattern     匹配模式
     * @param indexOrLast
     * @param frontOrBack
     * @return
     */
    public static String spliteString(String srcStr, String pattern,
                                      String indexOrLast, String frontOrBack) {
        String result = "";
        int loc = -1;
        if (indexOrLast.equalsIgnoreCase("index")) {
            loc = srcStr.indexOf(pattern); // 取得字符串第一次出现的位置
        } else {
            loc = srcStr.lastIndexOf(pattern); // 最后一个匹配串的位置
        }
        if (frontOrBack.equalsIgnoreCase("front")) {
            if (loc != -1)
                result = srcStr.substring(0, loc); // 截取子串
        } else {
            if (loc != -1)
                result = srcStr.substring(loc + 1, srcStr.length()); // 截取子串
        }
        return result;
    }

    /**
     * 获取日期字符串。
     */
    public static String getDate(Date date) {
        if (date == null)
            return "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    /**
     * 获取日期字符串。
     */
    public static String getDateMonth(Date date) {
        if (date == null)
            return "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        return formatter.format(date);
    }

    /**
     * 获取日期字符串。
     */
    public static String getDateString(Date date) {
        if (date == null)
            return "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return formatter.format(date);
    }

    /**
     * 获取日期的时间戳
     */
    public static long getDateToLong(Date date) {
        if (date == null)
            return 0;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String format = formatter.format(date);
        try {
            return formatter.parse(format).getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取日期字符串。
     */
    public static String getDateYear(Date date) {
        if (date == null)
            return "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        return formatter.format(date);
    }

    /**
     * 获取日期字符串。
     */
    public static String getTime(long date) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Long time = new Long(date + "000");
        return format.format(time);
    }


    /**
     * 获取日期字符串。
     */
    public static String getDateTime(Date date) {
        if (date == null)
            return "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
        return formatter.format(date);
    }


    /**
     * 获取当前日期是星期几<br>
     *
     * @param dateString
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }


    public static int getWeek(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return w;
    }

    /**
     * 长日期转成短日期 yyyy-MM-dd HH:mm:ss 改成 yyyy-MM-dd
     */
    public static String getShortDateFormLongDate(String dateString) {
        SimpleDateFormat sdfLong = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//
        SimpleDateFormat sdfShort = new SimpleDateFormat("yyyy-MM-dd");//
        Date date = null;
        try {
            date = sdfLong.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String shortDate = getDate(date);
        shortDate = sdfShort.format(date);
        return shortDate;
    }

    /**
     * 长日期转成短日期 yyyy-MM-dd HH:mm:ss 改成 yyyy-MM-dd
     */
    public static String getShortDateFormLongDate2(String dateString) {
        String shortDate = "";
        try {

            shortDate = dateString.split(" ")[0];
        } catch (Exception e) {
            e.printStackTrace();
        }

        return shortDate;
    }

    /**
     * 给两个时间获取时间差
     */

    public static String getTimeBetweenTwo(String fromDate, String toDate) {
        DateFormat df = new SimpleDateFormat("HH:mm");
        // 前的时间
        Date fd = null;
        try {
            fd = df.parse(fromDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 后的时间
        Date td = null;
        try {
            td = df.parse(toDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 两时间差,精确到毫秒
        long diff = td.getTime() - fd.getTime();
//        long day = diff / 86400000; // 以天数为单位取整
//        long hour = diff % 86400000 / 3600000; // 以小时为单位取整

        int min = (int) (diff / 1000 / 60); // 以分钟为单位取整
//        long seconds = diff % 86400000 % 3600000 % 60000 / 1000; // 以秒为单位取整
        // 天时分秒
        return min + "分钟";
    }


    public static long getLongTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date d1 = sdf.parse(time);
            return d1.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 获取两个时间之前的时间差天数
     *
     * @param date1
     * @param date2
     * @return 天数
     */
    public static int getDayBetweenTwo(String date1, String date2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        // 前的时间
        Date fd = null;
        try {
            fd = df.parse(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 后的时间
        Date td = null;
        try {
            td = df.parse(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 两时间差,精确到毫秒
        long diff = td.getTime() - fd.getTime();
        int day = (int) (diff / 1000 / 60 / 60 / 24); // 以分钟为单位取整
        return day;

    }

    public static String getDayHour(long time) {
        int hour = 0;
        long lessDay;
        int day;
        long lessHour;
        int minute;
        // 这个地方应该将totalTime和overTime都转换成天
        if ((time % (1000 * 60 * 60 * 24)) != 0) {
            day = (int) (time / (1000 * 60 * 60 * 24));// 获取天数
            lessDay = (time % (1000 * 60 * 60 * 24));// 判断天数之后剩余的时间（毫秒）
            if ((lessDay % (1000 * 60 * 60)) != 0) {
                hour = (int) (lessDay / (1000 * 60 * 60));// 获取小时
                lessHour = (lessDay % (1000 * 60 * 60));// 获取小时之后剩余的时间（毫秒）
                if ((lessHour % (1000 * 60)) != 0) {
                    minute = (int) (lessHour % (1000 * 60));

                } else {
                    minute = (int) (lessHour / (1000 * 60));
                }
                if (minute > 30) {
                    hour++;
                }
            } else {
                hour = (int) (lessDay / (1000 * 60 * 60));// 获取小时
            }
        } else {
            day = (int) (time / (1000 * 60 * 60 * 24));// 获取天数
        }
        return day + "天" + hour + "小时";
    }

    /**
     * 在指定日期基础上加上若干秒
     *
     * @param date
     * @param l
     * @return
     */
    public static Date addMillSecond(Date date, long l) {
        long oldD = date.getTime();
        long deltaD = l * 1000L;
        long newD = oldD + deltaD;
        Date d = new Date(newD);
        return d;
    }

    /**
     * 获取当前的年份
     *
     * @return
     */
    public static int getCurYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    /**
     * 获取当前的月份
     *
     * @return
     */
    public static int getCurMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MONTH);
    }

    /**
     * 获取当前的月份
     *
     * @return
     */
    public static int getCurDay() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取一个月前的日期
     *
     * @param date 传入的日期
     * @return
     */
    public static String getMonthAgo(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        String monthAgo = simpleDateFormat.format(calendar.getTime());
        return monthAgo;
    }

    /**
     * 获取当前年月日
     */
    public static String getCurrentDate() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(d);
        return dateNowStr;
    }

    /**
     * 获取当天是当月的第几天
     *
     * @return
     */
    public static int getCurrentDay() {
        String currentDate = getCurrentDate();
        int dayIndex = Integer.parseInt(currentDate.split("-")[2]);
        return dayIndex;
    }

    /**
     * 时间戳转换成字符串
     *
     * @param seconds
     * @param format
     * @return
     */
    public static String timeStamp2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if (seconds.length() == 10) {
            return sdf.format(new Date(Long.valueOf(seconds + "000")));
        } else {
            return sdf.format(new Date(Long.valueOf(seconds)));
        }
    }


    /**
     * 获取某个日期月份的天数
     *
     * @param date
     * @return
     */
    public static int getDaysOfMonth(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        try {
            Date parse = sdf.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parse);
            return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 28;
    }


    public static boolean IsToday(String day) {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        try {
            Date date = getDateFormat().parse(day);
            cal.setTime(date);

            if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
                int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                        - pre.get(Calendar.DAY_OF_YEAR);

                if (diffDay == 0) {
                    return true;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static SimpleDateFormat getDateFormat() {
        if (null == DateLocal.get()) {
            DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
        }
        return DateLocal.get();
    }

    private static ThreadLocal<SimpleDateFormat> DateLocal = new ThreadLocal<>();

    /**
     * 指定日期N个月后的日期
     *
     * @param inputDate
     * @param number
     * @return
     */
    public static String getAfterMonth(String inputDate, int number) {
        Calendar c = Calendar.getInstance();//获得一个日历的实例
        SimpleDateFormat sdf = null;
        if (inputDate.split("-").length == 2) {
            sdf = new SimpleDateFormat("yyyy-MM");
        }else{
            sdf = new SimpleDateFormat("yyyy-MM-dd");
        }
        Date date = null;
        try {
            date = sdf.parse(inputDate);//初始日期
        } catch (Exception e) {

        }
        c.setTime(date);//设置日历时间
        c.add(Calendar.MONTH, number);//在日历的月份上增加6个月

        String strDate = sdf.format(c.getTime());//的到你想要得6个月后的日期
        return strDate;
    }

}
