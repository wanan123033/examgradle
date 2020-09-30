package com.orhanobut.logger;

public interface Printer{
	
	void addAdapter(LogAdapter adapter);
	
	Printer t(String tag);
	
	void d(String message,Object... args);
	
	void d(Object object);
	
	void e(String message,Object... args);
	
	void e(Throwable throwable,String message,Object... args);
	
	void w(String message,Object... args);
	
	void i(String message,Object... args);
	
	void v(String message,Object... args);
	
	void wtf(String message,Object... args);
	
	void json(String json);
	
	void xml(String xml);
	
	void log(int priority,String tag,String message,Throwable throwable);
	
	void clearLogAdapters();
	
}
