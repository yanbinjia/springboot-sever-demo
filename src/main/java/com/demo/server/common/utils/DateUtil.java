package com.demo.server.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    public final static String DATE_FORMAT = "yyyy-MM-dd";

    public final static String DATE_FORMAT_CN = "yyyy年MM月dd日";

    public final static String DATE_FORMAT_CN_MONTH = "yyyy年MM月";

    public final static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public final static String DATE_TIME_FORMAT_CN = "yyyy年MM月dd日 HH:mm:ss";

    public final static String MONTH_FORMAT = "yyyy-MM";

    public final static String DAY_FORMAT = "yyyyMMdd";

    public final static String DATE_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public final static String DATE_YYYYMMDDHH = "yyyy-MM-dd-HH";

    public final static String MONTH_DAY_FORMAT = "MMdd";

    public final static String DATE_TIME_FORMAT_CNS = "yyyy年MM月dd日 HH时mm分";

    public static Date getCurrentDate() {
        return new Date();
    }

    public static String getCurrentYMDHMS() {
        return DateUtil.getDateStr(DateUtil.getCurrentDate(), DateUtil.DATE_YYYYMMDDHHMMSS);
    }

    /**
     * 得到格式化后的当前系统日期，格式为MMdd，如0215
     *
     * @return
     */
    public static String getCurrentMonthDateStr() {
        return DateUtil.getDateStr(DateUtil.getCurrentDate(), DateUtil.MONTH_DAY_FORMAT);
    }

    /**
     * 得到格式化后的当前系统日期，格式为yyyy-MM-dd，如2006-02-15
     *
     * @return
     */
    public static String getCurrentDateStr() {
        return DateUtil.getDateStr(DateUtil.getCurrentDate(), DateUtil.DATE_FORMAT);
    }

    /**
     * 得到格式化后的当前系统日期，格式为yyyy年MM月dd日
     *
     * @return
     */
    public static String getCurrentDateStrCN() {
        return DateUtil.getDateStr(DateUtil.getCurrentDate(), DateUtil.DATE_FORMAT_CN);
    }

    /**
     * 得到格式化后的当前系统时间，格式为yyyy-MM-dd HH:mm:ss，如2006-02-15 15:23:45
     *
     * @return
     */
    public static String getCurrentDateTimeStr() {
        return DateUtil.getDateStr(DateUtil.getCurrentDate(), DateUtil.DATE_TIME_FORMAT);
    }

    /**
     * 得到格式化后的当前系统时间，格式为yyyy年MM月dd日 HH:mm:ss
     *
     * @return
     */
    public static String getCurrentDateTimeStrCN() {
        return DateUtil.getDateStr(DateUtil.getCurrentDate(), DateUtil.DATE_TIME_FORMAT_CN);
    }

    /**
     * 得到格式化后的日期，格式为yyyy-MM-dd，如2006-02-15
     *
     * @param inputDateStr
     * @return
     * @throws Exception
     */
    public static Date getDateStr(String inputDateStr) {
        return DateUtil.getDateStr(inputDateStr, DateUtil.DATE_FORMAT);
    }

    /**
     * @param inputDateStr
     * @param formatPattern
     * @return
     * @throws Exception
     */
    public static Date getDateStr(String inputDateStr, String formatPattern) {
        Date date = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatPattern);
        try {
            date = simpleDateFormat.parse(inputDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;

    }

    /**
     * 得到格式化后的日期，格式为yyyy年MM月dd日
     *
     * @param inputDateStr
     * @return
     * @throws Exception
     */
    public static Date getDateStrCN(String inputDateStr) throws Exception {
        return DateUtil.getDateStr(inputDateStr, DateUtil.DATE_FORMAT_CN);
    }

    /**
     * 得到格式化后的日，格式为yyyyMMdd，如20060210
     *
     * @param inputDate
     * @return
     */
    public static String getDateDayStr(Date inputDate) {
        return DateUtil.getDateStr(inputDate, DateUtil.DAY_FORMAT);
    }

    /**
     * 获取格式化后的日期(07/30), dateStr格式为2006-02-15 15:23:45
     *
     * @param dateStr
     * @return
     * @throws Exception
     */
    public static String getFormatMonthDay(String dateStr) throws Exception {
        Calendar cal = Calendar.getInstance();
        Date date = getDateStr(dateStr, DateUtil.DATE_TIME_FORMAT);
        cal.setTime(date);
        int day = cal.get(Calendar.DATE);
        int month = cal.get(Calendar.MONTH) + 1;
        StringBuilder sb = new StringBuilder();
        if (month < 10) {
            sb.append("0").append(month);
        } else {
            sb.append(month);
        }
        sb.append("/");
        if (day < 10) {
            sb.append("0").append(day);
        } else {
            sb.append(day);
        }
        return sb.toString();
    }

    /**
     * 得到格式化后的月份，格式为yyyy-MM，如2006-02
     *
     * @param inputDate
     * @return
     */
    public static String getDateMonthStr(Date inputDate) {
        return DateUtil.getDateStr(inputDate, DateUtil.MONTH_FORMAT);
    }

    /**
     * 得到格式化后的日期，格式为yyyy-MM-dd，如2006-02-15
     *
     * @param inputDate
     * @return 格式化后的日期，类型为String
     */
    public static String getDateStr(Date inputDate) {
        return DateUtil.getDateStr(inputDate, DateUtil.DATE_FORMAT);
    }

    /**
     * 根据格式得到格式化后的日期字符串
     *
     * @param inputDate
     * @param formatPattern
     * @return
     */
    public static String getDateStr(Date inputDate, String formatPattern) {
        if (null == inputDate) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatPattern);
        return simpleDateFormat.format(inputDate);
    }

    /**
     * 得到格式化后的日期，格式为yyyy年MM月dd日
     *
     * @param inputDate
     * @return 格式化后的日期，类型为String
     */
    public static String getDateStrCN(Date inputDate) {
        return DateUtil.getDateStr(inputDate, DateUtil.DATE_FORMAT_CN);
    }

    /**
     * 得到格式化后的时间，格式为yyyy-MM-dd HH:mm:ss，如2006-02-15 15:23:45
     *
     * @param inputDateStr
     * @return
     * @throws Exception
     */
    public static Date getDateTimeStr(String inputDateStr) throws Exception {
        return DateUtil.getDateStr(inputDateStr, DateUtil.DATE_TIME_FORMAT);
    }

    /**
     * 得到格式化后的时间，格式为yyyy年MM月dd日 HH:mm:ss，如2006日02日15日 15:23:45
     *
     * @param inputDateStr
     * @return
     * @throws Exception
     */
    public static Date getDateTimeStrCN(String inputDateStr) throws Exception {
        return DateUtil.getDateStr(inputDateStr, DateUtil.DATE_TIME_FORMAT_CN);
    }

    /**
     * 得到格式化后的时间，格式为yyyy-MM-dd HH:mm:ss，如2006-02-15 15:23:45
     *
     * @param inputDate
     * @return
     */
    public static String getDateTimeStr(Date inputDate) {
        return DateUtil.getDateStr(inputDate, DateUtil.DATE_TIME_FORMAT);
    }

    /**
     * 得到格式化后的时间，格式为yyyy年MM月dd日 HH:mm:ss，如2006年02月15日 15:23:45
     *
     * @param inputDate
     * @return
     */
    public static String getDateTimeStrCN(Date inputDate) {
        return DateUtil.getDateStr(inputDate, DateUtil.DATE_TIME_FORMAT_CN);
    }

    /**
     * 得到日期的前或后几天
     *
     * @param inputDate
     * @param nDays     如果要获得前几天日期，该参数为负数；如果要获得后几天日期，该参数为正
     * @return
     */
    public static Date getDateBeforeOrAfter(Date inputDate, int nDays) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(inputDate);
        cal.add(Calendar.DAY_OF_MONTH, nDays);
        return cal.getTime();
    }

    /**
     * 得到时间的前或后几小时
     *
     * @param inputDate
     * @param nHours    如果要获得前几小时日期，该参数为负数；如果要获得后几小时日期，该参数为正
     * @return
     */
    public static Date getDateBeforeOrAfterHours(Date inputDate, int nHours) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(inputDate);
        cal.add(Calendar.HOUR_OF_DAY, nHours);
        return cal.getTime();
    }

    /**
     * 得到时间的前或后X分钟
     *
     * @param inputDate
     * @param nMinutes  如果要获得前X分钟日期，该参数为负数；如果要获得后X分钟日期，该参数为正
     * @return
     */
    public static Date getDateBeforeOrAfterMinutes(Date inputDate, int nMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(inputDate);
        cal.add(Calendar.MINUTE, nMinutes);
        return cal.getTime();
    }

    public static Date getDateTimeStrCNS(String inputDateStr) throws Exception {
        return DateUtil.getDateStr(inputDateStr, DateUtil.DATE_TIME_FORMAT_CNS);
    }

    /**
     * 获取检查身份证锁定前两分钟时间
     *
     * @param time
     * @return
     */
    public static Date getCheckIdNoLockTime(String time) throws Exception {
        Date dateTimeStrCNS = getDateTimeStrCNS(time);
        return getDateBeforeOrAfterMinutes(dateTimeStrCNS, -2);
    }

    /**
     * 得到时间的前或后X秒
     *
     * @param inputDate
     * @param nSecs
     * @return
     */
    public static Date getDateBeforeOrAfterSeconds(Date inputDate, int nSecs) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(inputDate);
        cal.add(Calendar.SECOND, nSecs);
        return cal.getTime();
    }

    /**
     * 得到格式化后的当月第1天，格式为yyyy-MM-dd，如2006-02-01
     *
     * @return
     */
    public static String getFirstDayOfCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        int firstDay = cal.getMinimum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, firstDay);
        return DateUtil.getDateStr(cal.getTime(), DateUtil.DATE_FORMAT);
    }

    /**
     * 得到格式化后的当月第1天时间 2018-01-01 00:00:00
     *
     * @param inputDate
     * @return
     */
    public static Date getFirstTimeOfMonth(Date inputDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(inputDate);
        int firstDay = cal.getMinimum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, firstDay);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getlastTimeOfMonth(Date inputDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(inputDate);
        cal.set(Calendar.DAY_OF_MONTH, cal.getMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, cal.getMaximum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getMaximum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getMaximum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getMaximum(Calendar.MILLISECOND));
        return cal.getTime();
    }

    /**
     * 获取当年的第一天
     *
     * @param inputDate
     * @return
     */
    public static Date getFirstTimeOfYear(Date inputDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(inputDate);

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, cal.get(Calendar.YEAR));

        return calendar.getTime();
    }

    /**
     * 获取当年的最后一天
     *
     * @param inputDate
     * @return
     */
    public static Date getLastTimeOfYear(Date inputDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(inputDate);

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, cal.get(Calendar.YEAR));
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        calendar.set(Calendar.HOUR_OF_DAY, cal.getMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, cal.getMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, cal.getMaximum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, cal.getMaximum(Calendar.MILLISECOND));
        return calendar.getTime();
    }

    /**
     * 获得给定时间 例如传入2006-12-12 03:04:57，系统将返回2006-12-12 00:00:00
     *
     * @param inputDate
     * @return
     */
    public static Date getDateStartTime(Date inputDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(inputDate);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 获取给定时间，当日最后时间
     *
     * @param inputDate
     * @return
     */
    public static Date getDateEndTime(Date inputDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(inputDate);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 59);
        return c.getTime();
    }

    /**
     * 得到某个时间的明天
     *
     * @param inputDate
     * @return
     */
    public static Date getTomorrowStartTime(Date inputDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(inputDate);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.add(Calendar.DATE, 1);
        return c.getTime();
    }

    /**
     * 得到某个时间的昨天
     *
     * @param inputDate
     * @return
     */
    public static Date getYesterdayStartTime(Date inputDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(inputDate);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.add(Calendar.DATE, -1);
        return c.getTime();
    }

    /**
     * getHourOfDate
     *
     * @param inputDate
     * @return
     */
    public static int getDayOfMonth(Date inputDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(inputDate);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * getHourOfDate
     *
     * @param inputDate
     * @return
     */
    public static int getHourOfDate(Date inputDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(inputDate);
        return c.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * getMinuteOfHour
     *
     * @param inputDate
     * @return
     */
    public static int getMinuteOfHour(Date inputDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(inputDate);
        return c.get(Calendar.MINUTE);
    }

    /**
     * 比较两个日期,忽略时分秒： 如果date1和date2在同1天则返回0； 如果date1的日期小于date2的日期返回-1；
     * 如果date1的日期大于date2的日期返回1；
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int compareDate(Date date1, Date date2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(date1);
        c2.setTime(date2);
        if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
                && c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)) {
            return 0;
        } else if (c1.getTimeInMillis() < c2.getTimeInMillis()) {
            return -1;
        } else {
            return 1;
        }
    }

    /**
     * 比较date1和date2之间的毫秒差,在这个差值基础上可以换算成间隔的天、小时、分、秒
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long compareDateByMilliseconds(String date1, String date2) {
        if (StringUtils.isEmpty(date1) || StringUtils.isEmpty(date2)) {
            return 0;
        }
        long time1 = DateUtil.formatStringToMillions(date1);
        long time2 = DateUtil.formatStringToMillions(date2);
        long diff;
        if (time1 < time2) {
            diff = time2 - time1;
        } else {
            diff = time1 - time2;
        }
        return diff;
    }

    /**
     * 比较date1和date2之间的天数差
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long compareDateByDay(String date1, String date2) {
        long diff = DateUtil.compareDateByMilliseconds(date1, date2);
        if (diff > 24 * 60 * 60 * 1000) {
            return diff / (24 * 60 * 60 * 1000);
        }
        return 0;
    }

    /**
     * 比较date1和date2之间的分钟差
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long compareDateByMin(String date1, String date2) {
        long diff = DateUtil.compareDateByMilliseconds(date1, date2);
        if (diff > 60 * 1000) {
            return diff / (60 * 1000);
        }
        return 0;
    }

    /**
     * 比较date1和date2之间的小时差
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long compareDateByHour(String date1, String date2) {
        long diff = DateUtil.compareDateByMilliseconds(date1, date2);
        if (diff > 60 * 60 * 1000) {
            return diff / (60 * 60 * 1000);
        }
        return 0;
    }

    public static DateFormat getDateFormatForLdapUTCDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss'Z'");
        format.setTimeZone(TimeZone.getTimeZone("utc"));

        return format;
    }

    /**
     * 比较date1和date2之间的月差
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int compareDateByMonth(Date date1, Date date2) {
        String strDate1 = DateUtil.getDateDayStr(date1);
        String strDate2 = DateUtil.getDateDayStr(date2);
        if (strDate1.length() != 8 || strDate2.length() != 8) {
            return 0;
        }
        int year1 = Integer.parseInt(strDate1.substring(0, 4));
        int year2 = Integer.parseInt(strDate2.substring(0, 4));
        int month1 = Integer.parseInt(strDate1.substring(4, 6));
        int month2 = Integer.parseInt(strDate2.substring(4, 6));
        if (year1 > year2) {
            return (year1 - year2) * 12 + month1 - month2;
        } else if (year1 < year2) {
            return (year2 - year1) * 12 + month2 - month1;
        } else {
            return Math.abs(month1 - month2);
        }
    }

    /**
     * 返回时间差-秒
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long betweenDateBySeconds(Date date1, Date date2) {
        long time1 = date1.getTime();
        long time2 = date2.getTime();

        return (time1 - time2) / 1000;
    }

    /**
     * 将时间戳转化成年月日时分秒 如1449218829000L to yyyy-MM-dd HH:ss:mm
     *
     * @param millsTime
     * @return
     */
    public static String formatMillsToString(long millsTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millsTime);
        SimpleDateFormat simple = new SimpleDateFormat(DateUtil.DATE_TIME_FORMAT);
        return simple.format(calendar.getTime());
    }

    private static long formatStringToMillions(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DATE_TIME_FORMAT);
        Date d = null;
        try {
            d = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        return d.getTime();
    }

    /**
     * 格式化string时间 2015-10-01 00:00:00 同2015-10-01
     *
     * @param strTime
     * @return
     * @throws ParseException
     */
    public static String formatStringTime(String strTime) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(DateUtil.DATE_FORMAT);
        String formatTime = "";
        Date date = format.parse(strTime);
        formatTime = format.format(date);
        return formatTime;
    }

    /**
     * 根据身份证号上的生日计算年龄 19900909
     *
     * @param ageStr
     * @return
     * @throws Exception
     */
    public static int getAgeByBirthday(String ageStr) throws Exception {
        Date birthday = DateUtil.getDateStr(ageStr, DateUtil.DAY_FORMAT);
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthday)) {
            return 0;
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(birthday);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                age--;
            }
        }
        return age;
    }

    /**
     * 判断strTime(时间格式 2015-10-01 00:00:00)和当前时间之间的间隔是否超过24小时
     *
     * @param strTime
     * @return
     */
    public static boolean checkIntervalExceed24Hour(String strTime) {
        long interval = getIntervalTime(strTime);
        return interval >= 60 * 60 * 24 * 1000;
    }

    /**
     * 获取strTime(时间格式 2015-10-01 00:00:00)和当前时间之间的间隔
     *
     * @param strTime
     * @return
     */
    public static long getIntervalTime(String strTime) {
        long time1 = DateUtil.formatStringToMillions(strTime);
        long time2 = DateUtil.formatStringToMillions(getCurrentDateTimeStr());
        return time2 - time1;
    }

    /**
     * 去除时间中的.0 (时间格式 2015-10-01 00:00:00.0)
     *
     * @param strTime
     * @return
     */
    public static String removeDecimalPointFromTime(String strTime) {
        if (StringUtils.isEmpty(strTime)) {
            return "";
        }
        String[] split = strTime.split("\\.");
        return split[0];
    }

    /**
     * 比较date1和date2, 如果date1大于date2返回1, 相同返回0, 小于返回-1(时间格式2015-10-01 00:00:00)
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int compareTime(String date1, String date2) {
        if (StringUtils.isEmpty(date1) && StringUtils.isEmpty(date2)) {
            return 0;
        }
        if (StringUtils.isEmpty(date1) && StringUtils.isNotEmpty(date2)) {
            return -1;
        }
        if (StringUtils.isNotEmpty(date1) && StringUtils.isEmpty(date2)) {
            return 1;
        }
        long dateNum1 = formatStringToMillions(date1);
        long dateNum2 = formatStringToMillions(date2);
        if (dateNum1 > dateNum2) {
            return 1;
        } else if (dateNum1 == dateNum2) {
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * 计算verifyTime到当前时间的录入耗时字符串
     *
     * @param verifyTime
     * @return
     */
    public static String calculateVerifyTime(String text, Date verifyTime) {
        if (verifyTime == null) {
            return text + "耗时0分钟";
        }
        long curTime = System.currentTimeMillis() / 1000;
        long verifyTimeSec = verifyTime.getTime() / 1000;
        long inputTime = curTime - verifyTimeSec;
        if (inputTime < 0) {
            inputTime = 0;
        }
        long hour = 0;
        long minute = 0;
        if (inputTime > 3600) {
            hour = inputTime / 3600;
            minute = inputTime / 60 % 60;
            return text + "耗时" + hour + "小时 " + minute + "分钟";
        }
        minute = inputTime / 60 % 60;
        return text + "耗时" + minute + "分钟";

    }

    /**
     * 验证日期和格式
     *
     * @param str
     * @param format
     * @return
     */
    public static boolean isValidDate(String str, String format) {

        if (StringUtils.isBlank(str)) {
            return false;
        }

        boolean convertSuccess = true;
        SimpleDateFormat sf = new SimpleDateFormat(format);
        try {
            // 设置lenient为false.
            // 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            sf.setLenient(false);
            sf.parse(StringUtils.trim(str));
        } catch (ParseException e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        }
        return convertSuccess;
    }

    public static String changeFormat(String dateStr, String orignFormat, String destFormat) {
        return DateUtil.getDateStr(DateUtil.getDateStr(dateStr, orignFormat), destFormat);
    }

    /**
     * 返回当前月份最后一天 格式2018-12-12 59:59:59
     *
     * @return
     */
    public static String getLastDayOfMonth() {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String last = format.format(ca.getTime());
        last = last + " 59:59:59";
        return last;
    }

    public static long getMillisAfterSeconds(int seconds) {
        return System.currentTimeMillis() + seconds * 1000L; // 一定加L
    }

    public static Date addDays(Date date, int amount) {
        return DateUtils.addDays(date, amount);
    }
}
