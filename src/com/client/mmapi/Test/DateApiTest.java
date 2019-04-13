package com.client.mmapi.Test;

import java.time.LocalDate;
import java.time.Month;

public class DateApiTest {

	public static void main(String[] args) {
		LocalDate start = LocalDate.of(2018, Month.JANUARY, 1);
		LocalDate end = LocalDate.of(2018, Month.DECEMBER, 31);
		iterateBetweenDatesJava8(start,end);

	}

	static void iterateBetweenDatesJava8(LocalDate start, LocalDate end) {
	    for (LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
	        System.out.println(date.toString());
	    }
	}
}
