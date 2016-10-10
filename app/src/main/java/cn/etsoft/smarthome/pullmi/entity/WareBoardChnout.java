package cn.etsoft.smarthome.pullmi.entity;

import java.io.Serializable;

public class WareBoardChnout implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3053533526442278099L;
	private String devUnitID; //12
	private String boardName; //8
	private byte boardType;
	private byte chnCnt;
	private byte bOnline;
	private byte rev2;
	private String chnName[];

	public String[] getChnName() {
		return chnName;
	}

	public void setChnName(String[] chnName) {
		this.chnName = chnName;
	}


	public String getDevUnitID() {
		return devUnitID;
	}

	public void setDevUnitID(String devUnitID) {
		this.devUnitID = devUnitID;
	}

	public String getBoardName() {
		return boardName;
	}

	public void setBoardName(String boardName) {
		this.boardName = boardName;
	}

	public byte getBoardType() {
		return boardType;
	}

	public void setBoardType(byte boardType) {
		this.boardType = boardType;
	}

	public byte getChnCnt() {
		return chnCnt;
	}

	public void setChnCnt(byte chnCnt) {
		this.chnCnt = chnCnt;
	}

	public byte getbOnline() {
		return bOnline;
	}

	public void setbOnline(byte bOnline) {
		this.bOnline = bOnline;
	}

	public byte getRev2() {
		return rev2;
	}

	public void setRev2(byte rev2) {
		this.rev2 = rev2;
	}
}
