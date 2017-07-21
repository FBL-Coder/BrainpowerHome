package cn.etsoft.smarthome.Domain;

import java.io.Serializable;

public class WareSceneDevItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8416872337552710304L;
	private String canCpuID;
	private int devID;
	private int devType;
	private int bOnOff;
	private int param1;
	private int param2;

	public String getCanCpuID() {
		return canCpuID;
	}

	public void setCanCpuID(String canCpuID) {
		this.canCpuID = canCpuID;
	}

	public int getDevID() {
		return devID;
	}

	public void setDevID(int devID) {
		this.devID = devID;
	}

	public int getDevType() {
		return devType;
	}

	public void setDevType(int devType) {
		this.devType = devType;
	}

	public int getbOnOff() {
		return bOnOff;
	}

	public void setbOnOff(int bOnOff) {
		this.bOnOff = bOnOff;
	}

	public int getParam1() {
		return param1;
	}

	public void setParam1(int param1) {
		this.param1 = param1;
	}

	public int getParam2() {
		return param2;
	}

	public void setParam2(int param2) {
		this.param2 = param2;
	}
}
