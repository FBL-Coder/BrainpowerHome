package cn.etsoft.smarthome.pullmi.entity;

import java.io.Serializable;

public class WareFreshAir implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WareDev dev;
	private byte bOnOff;
	private byte spdSel;
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

	public byte getSpdSel() {
		return spdSel;
	}

	public void setSpdSel(byte spdSel) {
		this.spdSel = spdSel;
	}

	public int getPowChn() {
		return powChn;
	}

	public void setPowChn(int powChn) {
		this.powChn = powChn;
	}
}
