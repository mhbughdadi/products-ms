package com.apogee.product.utilities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtilities {

    public static final String DATE_PATTERN_YYYY_MM_DD__HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static String getCurrentTimeStamp() {

        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN_YYYY_MM_DD__HH_MM_SS);

        return now.format(formatter);
    }
}
