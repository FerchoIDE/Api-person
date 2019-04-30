package com.consistent.service.application.models;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;


public class Contants {
	private static final Log log = LogFactoryUtil.getLog(Contants.class);
	

	public static String LENGUAJE = "";
	
	public static String[] STRUCTURE_IDS;
	
	public static String[] JOURNAL_HOTEL;
	
	public static String[] JOURNAL_RATES;
	
	public static String[] DLFILEENTRY_BASE;
	
			public static String getLanguaje(){
				
				
				if(LENGUAJE.equalsIgnoreCase("spanish")){
					return "es_ES";
				}else if(LENGUAJE.equalsIgnoreCase("english")){
					return "en_US";
				}else {
					log.info("No se establecio un lenguaje valido");
				}
				
				return new String() ;
			}
}
