package com.hhoca.utils;


public abstract class Utils {

	public static void HANDLE_EXCEPTION(Exception e) {
		System.err.println("An exception occured: ");
		e.printStackTrace();
		System.err.println("Application closing...");
		System.exit(-1);
	}
	
}
