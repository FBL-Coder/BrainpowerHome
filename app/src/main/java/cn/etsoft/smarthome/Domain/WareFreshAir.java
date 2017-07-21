package cn.etsoft.smarthome.Domain;

import java.io.Serializable;

public class WareFreshAir implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WareDev dev;
	private int bOnOff;
	private int spdSel;
	private int powChn;

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

	public int getSpdSel() {
		return spdSel;
	}

	public void setSpdSel(int spdSel) {
		this.spdSel = spdSel;
	}

	public int getPowChn() {
		return powChn;
	}

	public void setPowChn(int powChn) {
		this.powChn = powChn;
		dev.setPowChn(powChn);
	}
}
