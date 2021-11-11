package com.enrutaglp.backend.utils;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.RandomStringUtils;
public class Utils {

	public static String generarCodigoAleatorio(int len) {
		String primeraParte = RandomStringUtils.random(4, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		String segundaParte = RandomStringUtils.random(4, "0123456789");
		return primeraParte + segundaParte;
	}
	
	public static LocalDateTime obtenerFechaHoraActual() {
		return LocalDateTime.now(ZoneId.of("America/Lima"));
	}
	
	public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
}
