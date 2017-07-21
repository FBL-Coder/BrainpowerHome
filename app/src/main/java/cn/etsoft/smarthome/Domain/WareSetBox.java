package cn.etsoft.smarthome.Domain;

import java.io.Serializable;

public class WareSetBox implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WareDev dev;
	private int bOnOff;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public WareDev getDev() {
		return dev;
	}

	public void setDev(WareDev dev) {
		this.dev = dev;
	}

	public int getbOnOff() {
		return bOnOff;
	}

	public void setbOnOff(int bOnOff) {
		this.bOnOff = bOnOff;
		dev.setbOnOff(bOnOff);
	}
}
