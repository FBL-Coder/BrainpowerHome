package cn.etsoft.smarthome.pullmi.entity;

public class WareMultiItem {
	private WareDev dev;
	private byte bOnOff;
	private short spdSel;
	private short powChn;

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

	public short getSpdSel() {
		return spdSel;
	}

	public void setSpdSel(short spdSel) {
		this.spdSel = spdSel;
	}

	public short getPowChn() {
		return powChn;
	}

	public void setPowChn(short powChn) {
		this.powChn = powChn;
	}
}
