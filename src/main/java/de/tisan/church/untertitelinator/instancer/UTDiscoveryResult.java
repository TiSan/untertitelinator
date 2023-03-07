package de.tisan.church.untertitelinator.instancer;

public class UTDiscoveryResult
{
	String ip;
	String port;

	public UTDiscoveryResult()
	{
	}

	public UTDiscoveryResult(String ip, String port)
	{
		super();
		this.ip = ip;
		this.port = port;
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public String getPort()
	{
		return port;
	}

	public void setPort(String port)
	{
		this.port = port;
	}

}
