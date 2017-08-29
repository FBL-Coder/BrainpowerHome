package cn.etsoft.smarthome.Domain;

import java.io.Serializable;

public class WareDevProFLoorHeat implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WareDev dev;
	private int  bOnOff;//???? 0?? 1??
	private int tempGet;//?????????????????
	private int tempSet;//???????????
	private int powChn;//????????????
	private int autoRun;//?????????????
	private int rev2;//

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

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public int getTempGet() {
		return tempGet;
	}

	public void setTempGet(int tempGet) {
		this.tempGet = tempGet;
	}

	public int getTempSet() {
		return tempSet;
	}

	public void setTempSet(int tempSet) {
		this.tempSet = tempSet;
	}

	public int getPowChn() {
		return powChn;
	}

	public void setPowChn(int powChn) {
		this.powChn = powChn;
	}

	public int getAutoRun() {
		return autoRun;
	}

	public void setAutoRun(int autoRun) {
		this.autoRun = autoRun;
	}

	public int getRev2() {
		return rev2;
	}

	public void setRev2(int rev2) {
		this.rev2 = rev2;
	}
}
