package cn.etsoft.smarthome.pullmi.entity;

public class WareEnvDevItem {

	private String uid;
	private byte devID;
	private byte devType;
	private byte bOnOff;
	private byte param1;
	private byte param2;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public byte getDevID() {
		return devID;
	}

	public void setDevID(byte devID) {
		this.devID = devID;
	}

	public byte getDevType() {
		return devType;
	}

	public void setDevType(byte devType) {
		this.devType = devType;
	}

	public byte getbOnOff() {
		return bOnOff;
	}

	public void setbOnOff(byte bOnOff) {
		this.bOnOff = bOnOff;
	}

	public byte getParam1() {
		return param1;
	}

	public void setParam1(byte param1) {
		this.param1 = param1;
	}

	public byte getParam2() {
		return param2;
	}

	public void setParam2(byte param2) {
		this.param2 = param2;
	}
}
