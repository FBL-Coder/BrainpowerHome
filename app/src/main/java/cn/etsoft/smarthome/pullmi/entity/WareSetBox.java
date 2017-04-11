package cn.etsoft.smarthome.pullmi.entity;

import java.io.Serializable;

public class WareSetBox implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WareDev dev;
	private byte bOnOff;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

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
}
