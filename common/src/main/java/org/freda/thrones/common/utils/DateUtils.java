package org.freda.thrones.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.collections4.map.LRUMap;

public class DateUtils {
    private static final int MAX_SDF_SIZE = 20;
    private static final ThreadLocal<LRUMap> dateFormatThreadLocal = new ThreadLocal<LRUMap>() {
        @Override
        protected LRUMap initialValue() {
            return new LRUMap(MAX_SDF_SIZE);
        }
    };

    public static SimpleDateFormat getDateFormat(String pattern) {
        LRUMap sdfMap = dateFormatThreadLocal.get();
        SimpleDateFormat sdf = (SimpleDateFormat) sdfMap.get(pattern);
        if (sdf == null) {
            sdf = new SimpleDateFormat(pattern);
            sdf.setLenient(false);
            sdfMap.put(pattern, sdf);
        }
        return sdf;
    }

    public static void destroy() {
        dateFormatThreadLocal.remove();
    }

    public static Date parse2d2M4y(String source) {
        try {
            return getDateFormat("dd/MM/yyyy").parse(source);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parseyyMMddHHmmss(String source) {
        try {
            return getDateFormat("yyMMddHHmmss").parse(source);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parse4y2M2d(String source) {
        try {
            return getDateFormat("yyyy-MM-dd").parse(source);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parse4y2M2d2h2m2s(String source) {
        try {
            return getDateFormat("yyyy-MM-dd HH:mm:ss").parse(source);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parse4y2M2d2h2m(String source) {
        try {
            return getDateFormat("yyyy-MM-dd HH:mm").parse(source);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parse4y2M2dT2h2m2s(String source) {
        try {
            return getDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(source);
        } catch (Exception e) {
            return null;
        }
    }

    public static String format4y2M2d2h2m2s(long date) {
        if (date <= 0)
            return null;
        try {
            return getDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parseHHmm(String source) {
        try {
            return getDateFormat("HH:mm").parse(source);
        } catch (Exception e) {
            return null;
        }
    }

    public static String formatHHmm(Date date) {
        try {
            return getDateFormat("HH:mm").format(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static String formatHHmmss(Date date) {
        try {
            return getDateFormat("HH:mm:ss").format(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parseHHmmWithoutDelimiter(String source) {
        try {
            return getDateFormat("HHmm").parse(source);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parse(String source, String pattern) {
        try {
            return getDateFormat(pattern).parse(source);
        } catch (Exception e) {
            return null;
        }
    }

    public static String format4y2M2d(Date date) {
        return getDateFormat("yyyy-MM-dd").format(date);
    }

    public static String format2d2M4y(Date date) {
        return getDateFormat("dd/MM/yyyy").format(date);
    }

    public static String format2M2d4y(Date date) {
        return getDateFormat("MM/dd/yyyy").format(date);
    }

    public static String format4y2M2d2h2m2s(Date date) {
        return getDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static String format4y2M2dT2h2m2s(Date date) {
        return getDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(date);
    }

    public static String format4y2M2d2h2m(Date date) {
        return getDateFormat("yyyy-MM-dd HH:mm").format(date);
    }

    public static Calendar dateToCalender(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static int diff(Date startDate, Date endDate) {
        return (int) Math.ceil((endDate.getTime() - startDate.getTime()) / 86400000.0D);
    }

    public static int diffHour(Date startDate, Date endDate) {
        return (int) Math.ceil((endDate.getTime() - startDate.getTime()) / 3600000.0D);
    }

    public static int diffSecond(Date startDate, Date endDate) {
        return (int) Math.ceil((endDate.getTime() - startDate.getTime()) / 1000.0D);
    }

    public static Date dateAddDays(Date date, int days) {
        Calendar cal = dateToCalender(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }

    public static String formatyyyyMMdd(Date date) {
        return getDateFormat("yyyyMMdd").format(date);
    }

}
