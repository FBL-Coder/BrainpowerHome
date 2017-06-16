package cn.etsoft.smarthome.Domain;

import java.io.Serializable;

public class WareKeyOpItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3302607752463272412L;
	private String devUnitID; //12
	private byte devType;
	private byte devId;
	private byte keyOpCmd;
	private byte keyOp;
	private byte index;

	public String getDevUnitID() {
		return devUnitID;
	}

	public void setDevUnitID(String devUnitID) {
		this.devUnitID = devUnitID;
	}

	public byte getDevType() {
		return devType;
	}

	public void setDevType(byte devType) {
		this.devType = devType;
	}

	public byte getDevId() {
		return devId;
	}

	public void setDevId(byte devId) {
		this.devId = devId;
	}

	public byte getKeyOpCmd() {
		return keyOpCmd;
	}

	public void setKeyOpCmd(byte keyOpCmd) {
		this.keyOpCmd = keyOpCmd;
	}

	public byte getKeyOp() {
		return keyOp;
	}

	public void setKeyOp(byte keyOp) {
		this.keyOp = keyOp;
	}

	public byte getIndex() {
		return index;
	}

	public void setIndex(byte index) {
		this.index = index;
	}
}
