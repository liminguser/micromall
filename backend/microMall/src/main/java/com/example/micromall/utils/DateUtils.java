package com.example.micromall.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * 获取当前日期
     */
    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    /**
     * 获取当前时间
     */
    public static LocalTime getCurrentTime() {
        return LocalTime.now();
    }

    /**
     * 获取当前日期时间
     */
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    /**
     * Date转LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * LocalDateTime转Date
     */
    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 日期格式化
     */
    public static String format(LocalDate date) {
        return format(date, DATE_FORMAT);
    }

    /**
     * 时间格式化
     */
    public static String format(LocalTime time) {
        return format(time, TIME_FORMAT);
    }

    /**
     * 日期时间格式化
     */
    public static String format(LocalDateTime dateTime) {
        return format(dateTime, DATE_TIME_FORMAT);
    }

    /**
     * 日期格式化
     */
    public static String format(LocalDate date, String pattern) {
        if (date == null) {
            return null;
        }
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 时间格式化
     */
    public static String format(LocalTime time, String pattern) {
        if (time == null) {
            return null;
        }
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 日期时间格式化
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 日期解析
     */
    public static LocalDate parseDate(String dateStr) {
        return parseDate(dateStr, DATE_FORMAT);
    }

    /**
     * 时间解析
     */
    public static LocalTime parseTime(String timeStr) {
        return parseTime(timeStr, TIME_FORMAT);
    }

    /**
     * 日期时间解析
     */
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        return parseDateTime(dateTimeStr, DATE_TIME_FORMAT);
    }

    /**
     * 日期解析
     */
    public static LocalDate parseDate(String dateStr, String pattern) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 时间解析
     */
    public static LocalTime parseTime(String timeStr, String pattern) {
        if (StringUtils.isBlank(timeStr)) {
            return null;
        }
        return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 日期时间解析
     */
    public static LocalDateTime parseDateTime(String dateTimeStr, String pattern) {
        if (StringUtils.isBlank(dateTimeStr)) {
            return null;
        }
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
    }
} 