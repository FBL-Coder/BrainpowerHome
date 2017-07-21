package cn.etsoft.smarthome.Domain;

import java.io.Serializable;

public class WareLight implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WareDev dev;
	private int bOnOff;
	private int bTuneEn;
	private int lmVal;
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

	public int getbTuneEn() {
		return bTuneEn;
	}

	public void setbTuneEn(int bTuneEn) {
		this.bTuneEn = bTuneEn;
	}

	public int getLmVal() {
		return lmVal;
	}

	public void setLmVal(int lmVal) {
		this.lmVal = lmVal;
	}

	public int getPowChn() {
		return powChn;
	}

	public void setPowChn(int powChn) {
		this.powChn = powChn;
		dev.setPowChn(powChn);
	}
}
