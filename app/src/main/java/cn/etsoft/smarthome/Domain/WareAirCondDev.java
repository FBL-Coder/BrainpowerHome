package cn.etsoft.smarthome.Domain;

import java.io.Serializable;

public class WareAirCondDev implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WareDev dev;
	private byte bOnOff;
	private byte selMode;
	private byte selTemp;
	private byte selSpd;
	private byte selDirect;
	private byte rev1;
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
		dev.setbOnOff(bOnOff);
	}

	public byte getSelMode() {
		return selMode;
	}

	public void setSelMode(byte selMode) {
		this.selMode = selMode;
	}

	public byte getSelTemp() {
		return selTemp;
	}

	public void setSelTemp(byte selTemp) {
		this.selTemp = selTemp;
	}

	public byte getSelSpd() {
		return selSpd;
	}

	public void setSelSpd(byte selSpd) {
		this.selSpd = selSpd;
	}

	public byte getSelDirect() {
		return selDirect;
	}

	public void setSelDirect(byte selDirect) {
		this.selDirect = selDirect;
	}

	public byte getRev1() {
		return rev1;
	}

	public void setRev1(byte rev1) {
		this.rev1 = rev1;
	}

	public int getPowChn() {
		return powChn;
	}

	public void setPowChn(int powChn) {
		this.powChn = powChn;
	}
}
