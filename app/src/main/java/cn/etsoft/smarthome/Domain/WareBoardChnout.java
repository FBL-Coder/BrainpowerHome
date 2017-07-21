package cn.etsoft.smarthome.Domain;

import java.io.Serializable;

public class WareBoardChnout implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3053533526442278099L;
	private String devUnitID; //12
	private String boardName; //8
	private int boardType;
	private int chnCnt;
	private int bOnline;
	private int rev2;
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

	public int getBoardType() {
		return boardType;
	}

	public void setBoardType(int boardType) {
		this.boardType = boardType;
	}

	public int getChnCnt() {
		return chnCnt;
	}

	public void setChnCnt(int chnCnt) {
		this.chnCnt = chnCnt;
	}

	public int getbOnline() {
		return bOnline;
	}

	public void setbOnline(int bOnline) {
		this.bOnline = bOnline;
	}

	public int getRev2() {
		return rev2;
	}

	public void setRev2(int rev2) {
		this.rev2 = rev2;
	}
}
