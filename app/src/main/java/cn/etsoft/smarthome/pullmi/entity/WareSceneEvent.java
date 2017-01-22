package cn.etsoft.smarthome.pullmi.entity;

import java.io.Serializable;
import java.util.List;

public class WareSceneEvent implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9164065895025538399L;
	private String sceneName;
	private byte devCnt;
	private byte eventld;
	private byte rev2;
	private byte rev3;


	private List<WareSceneDevItem> itemAry;

	public String getSceneName() {
		return sceneName;
	}

	public void setSceneName(String sceneName) {
		this.sceneName = sceneName;
	}

	public byte getDevCnt() {
		return devCnt;
	}

	public void setDevCnt(byte devCnt) {
		this.devCnt = devCnt;
	}

	public byte getEventld() {
		return eventld;
	}

	public void setEventld(byte eventld) {
		this.eventld = eventld;
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

	public List<WareSceneDevItem> getItemAry() {
		return itemAry;
	}

	public void setItemAry(List<WareSceneDevItem> itemAry) {
		this.itemAry = itemAry;
	}
}
