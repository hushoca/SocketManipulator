package com.hhoca;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.script.Bindings;
import javax.script.ScriptEngine;

import com.hhoca.utils.Utils;

public class Sniffer {
	
	private String _targetHost;
	private int _targetPort;
	private int _localPort;
	private boolean _keepRunning = true;
	
	private ServerSocket _server;
	
	public Sniffer(String targetHost, int targetPort, int localPort) {
		this._localPort = localPort;
		this._targetHost = targetHost;
		this._targetPort = targetPort;
	}
	
	public void Start() {
		try {
			_server = new ServerSocket(_localPort, 1);
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(_keepRunning) {
						try {
							System.out.println("Waiting for a client");
							Socket l_obClient = _server.accept();
							System.out.println("Client connected");
							new ConnectionHandler(l_obClient, _targetHost, _targetPort, _localPort).Execute();
						} catch (Exception e) {
							Utils.HANDLE_EXCEPTION(e);
						}
					}		
					Stop();
				}
				
			}).start();

		} catch (Exception e) {
			Utils.HANDLE_EXCEPTION(e);
		}
	}
	
	public void Stop() {
		try {
			if(_server != null) _server.close();
		} catch (Exception e) {
			Utils.HANDLE_EXCEPTION(e);
		}
	}

}


class ConnectionHandler{
	
	private Socket _client;
	private String _targetHost;
	private int _targetPort;
	private int _localPort;
	
	private boolean maintainConnection = true;
	
	ConnectionHandler(Socket s, String targetAddr, int targetPort, int localPort){
		_client = s;
		_targetHost = targetAddr;
		_targetPort = targetPort;
		_localPort = localPort;
	}
	
	public void Execute() {
			String address = _client.getInetAddress().getHostAddress();
			System.out.println("Started a new thread for connection from " + address);
			
			try {	
				Socket l_obTarget = new Socket(_targetHost, _targetPort);
				
				OutputStream l_outStrLocal = _client.getOutputStream();
				InputStream l_inStrLocal = _client.getInputStream();
				
				OutputStream l_outStrTarget = l_obTarget.getOutputStream();
				InputStream l_inStrTarget = l_obTarget.getInputStream();
			
							
				//local listener
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						try {			
							
							RequestResponseHandler l_obReqHandler = new RequestResponseHandler("OutgoingHandler.js");
							l_obReqHandler.addBinding("PARAM_TARGET_ADDR", l_obTarget.getInetAddress().getHostAddress());
							l_obReqHandler.addBinding("PARAM_TARGET_NAME", l_obTarget.getInetAddress().getHostName());
							l_obReqHandler.addBinding("PARAM_TARGET_PORT", _targetPort);
							l_obReqHandler.addBinding("PARAM_LOCAL_PORT", _localPort);
				
							while(maintainConnection) {
								String buffer = "";
								
								while(l_inStrLocal.available() > 0) {
									buffer += (char) l_inStrLocal.read();
								}			
								
								if(buffer != "") {
									String response = l_obReqHandler.handle(buffer);
									l_outStrTarget.write(response.getBytes());
								}
							}
							
							SignalTermination();
							if(l_obTarget.isConnected()) l_obTarget.close(); 
							if(_client.isConnected()) _client.close(); 
							System.out.println("Connection to target closed.");
							
						}catch(Exception e) {
							Utils.HANDLE_EXCEPTION(e);
						}
					}
					
				}).start();
			
				//target listener
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						try {
							
							RequestResponseHandler l_obReqHandler = new RequestResponseHandler("IncomingHandler.js");
							l_obReqHandler.addBinding("PARAM_TARGET_ADDR", l_obTarget.getInetAddress().getHostAddress());
							l_obReqHandler.addBinding("PARAM_TARGET_NAME", l_obTarget.getInetAddress().getHostName());
							l_obReqHandler.addBinding("PARAM_TARGET_PORT", l_obTarget.getPort());
							l_obReqHandler.addBinding("PARAM_LOCAL_PORT", _client.getPort());
							
							while(maintainConnection) {							
								String buffer = "";

								while(l_inStrTarget.available() > 0) {
									buffer += (char) l_inStrTarget.read();
								}	
								
								if(buffer != "") {
									
									String response = l_obReqHandler.handle(buffer);									
									l_outStrLocal.write(response.getBytes());

								}								
							}
							
							SignalTermination();						
							if(l_obTarget.isConnected()) l_obTarget.close(); 
							if(_client.isConnected()) _client.close(); 
							System.out.println("Connection to target closed.");
							
						}catch(Exception e) {
							Utils.HANDLE_EXCEPTION(e);
						}
					}
					
				}).start();
			
			}catch(Exception e) {
				Utils.HANDLE_EXCEPTION(e);
			}

	}
	
	private synchronized void SignalTermination() {
		maintainConnection = false;
	}
	
}