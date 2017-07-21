package cn.etsoft.smarthome.Domain;

import java.io.Serializable;

public class WareChnOpItem implements Serializable{
	private String cpuid; //12输出板
	private int devType;
	private int devid;

	private String cancupid; // 12　输入板
	private int keyDownValid; // 按键板最多６个按键
	private int keyUpValid; // 按键板最多６个按键
	private int rev1;
	private int[] keyDownCmd; // 6
	private int rev2;
	private int[] keyUpCmd; // 6
	private int rev3;
	private boolean isSelect = false;

	public String getCpuid() {
		return cpuid;
	}

	public void setCpuid(String cpuid) {
		this.cpuid = cpuid;
	}

	public int getDevType() {
		return devType;
	}

	public void setDevType(int devType) {
		this.devType = devType;
	}

	public int getDevid() {
		return devid;
	}

	public void setDevid(int devid) {
		this.devid = devid;
	}

	public String getCancupid() {
		return cancupid;
	}

	public void setCancupid(String cancupid) {
		this.cancupid = cancupid;
	}

	public int getKeyDownValid() {
		return keyDownValid;
	}

	public void setKeyDownValid(int keyDownValid) {
		this.keyDownValid = keyDownValid;
	}

	public int getKeyUpValid() {
		return keyUpValid;
	}

	public void setKeyUpValid(int keyUpValid) {
		this.keyUpValid = keyUpValid;
	}

	public int getRev1() {
		return rev1;
	}

	public void setRev1(int rev1) {
		this.rev1 = rev1;
	}

	public int[] getKeyDownCmd() {
		if (keyDownCmd == null ||keyDownCmd.length == 0)
			keyDownCmd = new int[]{0,0,0,0,0,0,0,0};
		return keyDownCmd;
	}

	public void setKeyDownCmd(int[] keyDownCmd) {
		this.keyDownCmd = keyDownCmd;
	}

	public int getRev2() {
		return rev2;
	}

	public void setRev2(int rev2) {
		this.rev2 = rev2;
	}

	public int[] getKeyUpCmd() {
		if (keyUpCmd == null ||keyUpCmd.length == 0)
			keyUpCmd = new int[]{0,0,0,0,0,0,0,0};
		return keyUpCmd;
	}

	public void setKeyUpCmd(int[] keyUpCmd) {
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
