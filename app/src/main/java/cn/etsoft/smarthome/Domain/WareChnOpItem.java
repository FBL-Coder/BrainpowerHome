package cn.etsoft.smarthome.Domain;

import java.io.Serializable;

public class WareChnOpItem implements Serializable{
	private String cpuid; //12输出板
	private byte devType;
	private byte devid;

	private String devUnitID; // 12　输入板
	private byte keyDownValid; // 按键板最多６个按键
	private byte keyUpValid; // 按键板最多６个按键
	private int rev1;
	private byte[] keyDownCmd; // 6
	private int rev2;
	private byte[] keyUpCmd; // 6
	private int rev3;
	private boolean isSelect = false;

	public String getCpuid() {
		return cpuid;
	}

	public void setCpuid(String cpuid) {
		this.cpuid = cpuid;
	}

	public byte getDevType() {
		return devType;
	}

	public void setDevType(byte devType) {
		this.devType = devType;
	}

	public byte getDevid() {
		return devid;
	}

	public void setDevid(byte devid) {
		this.devid = devid;
	}

	public String getDevUnitID() {
		return devUnitID;
	}

	public void setDevUnitID(String devUnitID) {
		this.devUnitID = devUnitID;
	}

	public byte getKeyDownValid() {
		return keyDownValid;
	}

	public void setKeyDownValid(byte keyDownValid) {
		this.keyDownValid = keyDownValid;
	}

	public byte getKeyUpValid() {
		return keyUpValid;
	}

	public void setKeyUpValid(byte keyUpValid) {
		this.keyUpValid = keyUpValid;
	}

	public int getRev1() {
		return rev1;
	}

	public void setRev1(int rev1) {
		this.rev1 = rev1;
	}

	public byte[] getKeyDownCmd() {
		if (keyDownCmd == null ||keyDownCmd.length == 0)
			keyDownCmd = new byte[]{0,0,0,0,0,0,0,0};
		return keyDownCmd;
	}

	public void setKeyDownCmd(byte[] keyDownCmd) {
		this.keyDownCmd = keyDownCmd;
	}

	public int getRev2() {
		return rev2;
	}

	public void setRev2(int rev2) {
		this.rev2 = rev2;
	}

	public byte[] getKeyUpCmd() {
		if (keyUpCmd == null ||keyUpCmd.length == 0)
			keyUpCmd = new byte[]{0,0,0,0,0,0,0,0};
		return keyUpCmd;
	}

	public void setKeyUpCmd(byte[] keyUpCmd) {
		this.keyUpCmd = keyUpCmd;
	}

	public int getRev3() {
		return rev3;
	}

	public void setRev3(int rev3) {
		this.rev3 = rev3;
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean select) {
		isSelect = select;
	}
}
