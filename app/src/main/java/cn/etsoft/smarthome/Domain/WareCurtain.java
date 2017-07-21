package cn.etsoft.smarthome.Domain;

import java.io.Serializable;

public class WareCurtain implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private WareDev dev;
	private int bOnOff;
	private int timRun;
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

	public int getTimRun() {
		return timRun;
	}

	public void setTimRun(int timRun) {
		this.timRun = timRun;
	}

	public int getPowChn() {
		return powChn;
	}

	public void setPowChn(int powChn) {
		this.powChn = powChn;
		dev.setPowChn(powChn);
	}
}
