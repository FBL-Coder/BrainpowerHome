package cn.etsoft.smarthome.Domain;

public class RUN_DEV_ITEM {

	private String uid;// 设备所在控制板的cpuid
	private byte devType;// 设备的类型 E_WARE_TYPE
	private byte lmVal;// 可调灯光的亮度值
	private byte rev2;
	private byte rev3;
	private byte devID;// 设备在控制板上的id
	private byte bOnoff;//
	private byte param1;// 设备运行的参数1
	private byte param2;// 设备运行的参数2

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
	public byte getDevType() {
		return devType;
	}

	public void setDevType(byte devType) {
		this.devType = devType;
	}

	public byte getLmVal() {
		return lmVal;
	}

	public void setLmVal(byte lmVal) {
		this.lmVal = lmVal;
	}

	public byte getRev2() {
		return rev2;
	}

	public void setRev2(byte rev2) {
		this.rev2 = rev2;
	}

	public byte getRev3() {
		return rev3;
	}

	public void setRev3(byte rev3) {
		this.rev3 = rev3;
	}

	public byte getDevID() {
		return devID;
	}

	public void setDevID(byte devID) {
		this.devID = devID;
	}

	public byte getbOnoff() {
		return bOnoff;
	}

	public void setbOnoff(byte bOnoff) {
		this.bOnoff = bOnoff;
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
