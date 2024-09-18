package com.allobank.allobackendtest.error;

public class CalegException extends Exception{
	//UNTUK HANDLING EXCEPTION
	public CalegException() {
		super();
	}
	public CalegException(String message) {
		super(message);
	}
	public CalegException(String message,Throwable cause) {
		super(message,cause);
	}
	public CalegException(Throwable cause) {
		super(cause);
	}
}
