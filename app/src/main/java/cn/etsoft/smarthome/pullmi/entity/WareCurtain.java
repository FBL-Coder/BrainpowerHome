package cn.etsoft.smarthome.pullmi.entity;

import java.io.Serializable;

public class WareCurtain implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private WareDev dev;
	private byte bOnOff;
	private byte timRun;
	private int powChn;

	public WareDev getDev() {
		return dev;
	}

	public void setDev(WareDev dev) {
		this.dev = dev;
	}

	public byte getbOnOff() {
		return bOnOff;
	}

	public void setbOnOff(byte bOnOff) {
		this.bOnOff = bOnOff;
	}

	public byte getTimRun() {
		return timRun;
	}

	public void setTimRun(byte timRun) {
		this.timRun = timRun;
	}

	public int getPowChn() {
		return powChn;
	}

	public void setPowChn(int powChn) {
		this.powChn = powChn;
	}
}
