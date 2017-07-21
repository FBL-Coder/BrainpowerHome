package cn.etsoft.smarthome.Domain;

import java.io.Serializable;

public class WareKeyOpItem implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 3302607752463272412L;
	private String out_cpuCanID; //12
	private String key_cpuCanID; //12
	private int devType;
	private int devId;
	private int keyOpCmd;
	private int keyOp;
	private int index;

	public String getKey_cpuCanID() {
		return key_cpuCanID;
	}

	public void setKey_cpuCanID(String key_cpuCanID) {
		this.key_cpuCanID = key_cpuCanID;
	}

	public String getOut_cpuCanID() {
		return out_cpuCanID;
	}

	public void setOut_cpuCanID(String out_cpuCanID) {
		this.out_cpuCanID = out_cpuCanID;
	}

	public int getDevType() {
		return devType;
	}

	public void setDevType(int devType) {
		this.devType = devType;
	}

	public int getDevId() {
		return devId;
	}

	public void setDevId(int devId) {
		this.devId = devId;
	}

	public int getKeyOpCmd() {
		return keyOpCmd;
	}

	public void setKeyOpCmd(int keyOpCmd) {
		this.keyOpCmd = keyOpCmd;
	}

	public int getKeyOp() {
		return keyOp;
	}

	public void setKeyOp(int keyOp) {
		this.keyOp = keyOp;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
