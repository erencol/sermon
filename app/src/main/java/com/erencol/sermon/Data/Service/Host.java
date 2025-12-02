package com.erencol.sermon.Data.Service;

import java.time.LocalDate;

public class Host {
     static final String baseUrl = "https://mobizoe.com/wp-content/uploads/";

     public static String getBaseUrl() {
          return baseUrl+getYear()+"/"+getMonth()+"/";
     }

     public static String getRetryBaseUrl() {
          return baseUrl+getYear()+"/"+getRetryMonth()+"/";
     }

     static String getMonth(){
          LocalDate today = LocalDate.now();
          int month = today.getMonthValue();
          if(month<10) {
               return "0"+month;
          } else return String.valueOf(month);
     }

     static String getRetryMonth(){
          LocalDate today = LocalDate.now();
          int month = today.getMonthValue();
          month -= 1;
          if(month<10) {
               return "0"+month;
          } else return String.valueOf(month);
     }

     static String getYear(){
          LocalDate today = LocalDate.now();
          int year = today.getYear();
          return String.valueOf(year);
     }

     public static final String getSermons = "sermon.json?t="+System.currentTimeMillis();
     // Cache'lemeyi iptal etmek için bu gerekli.
}
