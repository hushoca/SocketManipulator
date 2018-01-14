package com.hhoca;

import java.io.FileReader;
import java.io.InputStream;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.hhoca.utils.Utils;

public class RequestResponseHandler {

	private ScriptEngineManager _manager;
	private ScriptEngine _engine;
	
	private final static String STARTUP_SCRIPT_LOCATION = "/resources/Startup.js";
	
	public RequestResponseHandler(String src){
		_manager = new ScriptEngineManager();
		_engine = _manager.getEngineByExtension("js");
		
		try {
			InputStream l_streamRes = getClass().getResourceAsStream(STARTUP_SCRIPT_LOCATION);
			String l_stStartupScript = new String(l_streamRes.readAllBytes());
			_engine.eval(l_stStartupScript);
			if(l_streamRes != null) l_streamRes.close();
			
			FileReader l_obReader = new FileReader(src);
			_engine.eval(l_obReader);
			if(l_obReader != null) l_obReader.close();
			
		} catch (Exception e) {
			Utils.HANDLE_EXCEPTION(e);
		}
				
	}
	
	public void addBinding(String name, Object obj) {
		_engine.put(name, obj);
	}
	
	public String handle(String request) {
		Invocable l_obInvoc = (Invocable) _engine;
		String l_stBuff = "";
		try {
			l_stBuff = (String) l_obInvoc.invokeFunction("HandleRequest", request);
			if(l_stBuff == null) return request;
		} catch (NoSuchMethodException e) {
			Utils.HANDLE_EXCEPTION(e);
		} catch (ScriptException e) {
			Utils.HANDLE_EXCEPTION(e);
		}
		return l_stBuff;
	}
	
}

