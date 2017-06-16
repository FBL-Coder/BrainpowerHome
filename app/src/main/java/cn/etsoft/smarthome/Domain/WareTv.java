package cn.etsoft.smarthome.Domain;

import java.io.Serializable;

public class WareTv implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WareDev dev;
	private byte bOnOff;

	public byte getbOnOff() {
		return bOnOff;
	}

	public void setbOnOff(byte bOnOff) {
		this.bOnOff = bOnOff;
		dev.setbOnOff(bOnOff);
	}

	public WareDev getDev() {
		return dev;
	}

	public void setDev(WareDev dev) {
		this.dev = dev;
	}

}
