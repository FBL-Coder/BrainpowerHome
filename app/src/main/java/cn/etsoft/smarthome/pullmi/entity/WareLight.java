package cn.etsoft.smarthome.pullmi.entity;

import java.io.Serializable;

public class WareLight implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WareDev dev;
	private byte bOnOff;
	private byte bTuneEn;
	private byte lmVal;
	private byte powChn;

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

	public byte getbTuneEn() {
		return bTuneEn;
	}

	public void setbTuneEn(byte bTuneEn) {
		this.bTuneEn = bTuneEn;
	}

	public byte getLmVal() {
		return lmVal;
	}

	public void setLmVal(byte lmVal) {
		this.lmVal = lmVal;
	}

	public byte getPowChn() {
		return powChn;
	}

	public void setPowChn(byte powChn) {
		this.powChn = powChn;
	}
}
