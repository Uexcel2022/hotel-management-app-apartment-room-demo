package com.uexcel.executive.constants;
import com.uexcel.executive.exception.AppExceptions;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
@Component
public class Month {
    public  LocalDate getStartDate(String monthName) {
        LocalDate localDate = LocalDate.now();
        int year = localDate.getYear();
        Map<String, LocalDate> monthNames = new HashMap<>();
        monthNames.put("JANUARY",LocalDate.of(year,1,1));
        monthNames.put("FEBRUARY",LocalDate.of(year,2,1));
        monthNames.put("MARCH",LocalDate.of(year,3,1));
        monthNames.put("APRIL",LocalDate.of(year,4,1));
        monthNames.put("MAY",LocalDate.of(year,5,1));
        monthNames.put("JUNE",LocalDate.of(year,6,1));
        monthNames.put("JULY",LocalDate.of(year,7,1));
        monthNames.put("AUGUST",LocalDate.of(year,8,1));
        monthNames.put("SEPTEMBER",LocalDate.of(year,9,1));
        monthNames.put("OCTOBER",LocalDate.of(year,10,1));
        monthNames.put("NOVEMBER",LocalDate.of(year,11,1));
        monthNames.put("DECEMBER",LocalDate.of(year,12,1));
        LocalDate nameOfMonth = monthNames.get(monthName);
        if(nameOfMonth == null) {
            throw new AppExceptions(
                    HttpStatus.BAD_REQUEST.value(), Constants.BadRequest,"Invalid month name: " + monthName);
        }
        return nameOfMonth;
    }
}
