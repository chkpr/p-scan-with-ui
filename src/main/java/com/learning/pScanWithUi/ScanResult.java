package com.learning.pScanWithUi;

public class ScanResult {
	private int portNumber;
	private String service;
	private int responseTime;
	
	
	public ScanResult(int port, String service, int response) {
		this.portNumber = port;
		this.service = service;
		this.responseTime = response;
	}
	
	public int getPort() {
		return portNumber;
	}
	
	public String getService() {
		return service;
	}
	
	public int getResponseTime() {
		return responseTime;
	}
	
	@Override
	public String toString() {
		return "Port : " + portNumber + ", Service : " + service + ", Temps de réponse : " + responseTime;
	}
}
