package com.telenav.mdb.exception;

public class PersistenceException extends Exception{
	    public PersistenceException() {
	    }

	    public PersistenceException(String s) {
	        super(s);
	    }

	    public PersistenceException(String s, Throwable throwable) {
	        super(s, throwable);
	    }

	    public PersistenceException(Throwable throwable) {
	        super(throwable);
	    }
}
