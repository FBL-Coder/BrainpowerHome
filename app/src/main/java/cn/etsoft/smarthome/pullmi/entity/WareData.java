package cn.etsoft.smarthome.pullmi.entity;

import android.os.Bundle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.domain.ChnOpItem_scene;
import cn.etsoft.smarthome.domain.DevControl_Result;
import cn.etsoft.smarthome.domain.SetEquipmentResult;
import cn.etsoft.smarthome.domain.SetSafetyResult;
import cn.etsoft.smarthome.domain.SetSafetyResult_alarm;
import cn.etsoft.smarthome.domain.UserBean;

public class WareData implements Serializable {

    private List<RcuInfo> rcuInfos;
    private List<WareDev> devs;
    private List<WareLight> lights;
    private List<WareTv> tvs;
    private List<WareSetBox> stbs;
    private List<WareAirCondDev> airConds;
    private List<WareCurtain> curtains;
    private List<WareFreshAir> freshAirs;
    private List<WareSceneEvent> sceneEvents;
    private List<WareBoardChnout> boardChnouts;
    private List<WareBoardKeyInput> keyInputs;
    private List<WareChnOpItem> chnOpItems;
    private List<WareKeyOpItem> keyOpItems;
    private SetEquipmentResult result;
    //防区模块显示信息
    private SetSafetyResult result_safety;
    public SetSafetyResult getResult_safety() {
        return result_safety;
    }

    public void setResult_safety(SetSafetyResult result_safety) {
        this.result_safety = result_safety;
    }

    //高级设置-按键情景模块
    private ChnOpItem_scene chnOpItem_scene;
    public ChnOpItem_scene getChnOpItem_scene() {
        if (chnOpItem_scene == null)
            return new ChnOpItem_scene();
        return chnOpItem_scene;
    }

    public void setChnOpItem_scene(ChnOpItem_scene chnOpItem_scene) {
        this.chnOpItem_scene = chnOpItem_scene;
    }

    //防区模块报警
    private SetSafetyResult_alarm safetyResult_alarm;
    public SetSafetyResult_alarm getSafetyResult_alarm() {
        return safetyResult_alarm;
    }

    public void setSafetyResult_alarm(SetSafetyResult_alarm safetyResult_alarm) {
        this.safetyResult_alarm = safetyResult_alarm;
    }


    private DevControl_Result dev_result;
    private int login_result;
    private int addNewNet_reslut;
    private int addUser_reslut = -1;
    private boolean DATA_LOCAL_FLAG;
    private int DeleteNetReslut;
    private String DeleteDevid;

    public UserBean getUserBeen() {
        return userBeen;
    }

    public void setUserBeen(UserBean userBeen) {
        this.userBeen = userBeen;
    }

    private UserBean userBeen;


    public WareData() {

        if (rcuInfos == null) {
            setRcuInfos(new ArrayList<RcuInfo>());
        }

        if (lights == null) {
            setLights(new ArrayList<WareLight>());
        }
        if (curtains == null) {
            setCurtains(new ArrayList<WareCurtain>());
        }

        if (airConds == null) {
            setAirConds(new ArrayList<WareAirCondDev>());
        }
        if (freshAirs == null) {
            setFreshAirs(new ArrayList<WareFreshAir>());
        }
        if (devs == null) {
            setDevs(new ArrayList<WareDev>());
        }
        if (sceneEvents == null) {
            setSceneEvents(new ArrayList<WareSceneEvent>());
        }
        if (tvs == null) {
            setTvs(new ArrayList<WareTv>());
        }
        if (stbs == null) {
            setStbs(new ArrayList<WareSetBox>());
        }
        if (keyInputs == null) {
            setKeyInputs(new ArrayList<WareBoardKeyInput>());
        }
        if (boardChnouts == null) {
            setBoardChnouts(new ArrayList<WareBoardChnout>());
        }

        if (keyOpItems == null) {
            setKeyOpItems(new ArrayList<WareKeyOpItem>());
        }

        if (chnOpItems == null) {
            setChnOpItems(new ArrayList<WareChnOpItem>());
        }
    }

    public List<RcuInfo> getRcuInfos() {
        return rcuInfos;
    }

    public void setRcuInfos(List<RcuInfo> rcuInfos) {
        this.rcuInfos = rcuInfos;
    }

    public List<WareDev> getDevs() {
        if(devs==null)
            devs = new ArrayList<>();
        return devs;
    }

    public void setDevs(List<WareDev> devs) {
        if (this.devs != null)
            this.devs.clear();
        this.devs = devs;
    }

    public List<WareLight> getLights() {
        return lights;
    }

    public void setLights(List<WareLight> lights) {
        if (this.lights != null)
            this.lights.clear();
        this.lights = lights;
    }

    public List<WareAirCondDev> getAirConds() {
        return airConds;
    }

    public void setAirConds(List<WareAirCondDev> airConds) {
        if (this.airConds != null)
            this.airConds.clear();
        this.airConds = airConds;
    }

    public List<WareCurtain> getCurtains() {
        return curtains;
    }

    public void setCurtains(List<WareCurtain> curtains) {
        if (this.curtains != null)
            this.curtains.clear();
        this.curtains = curtains;
    }

    public List<WareFreshAir> getFreshAirs() {
        return freshAirs;
    }

    public void setFreshAirs(List<WareFreshAir> freshAirs) {
        this.freshAirs = freshAirs;
    }

    /**
     * 获取情景模块数据
     * @return
     */
    public List<WareSceneEvent> getSceneEvents() {
        return sceneEvents;
    }

    /**
     * 设置情景模块数据
     * @param sceneEvents
     */
    public void setSceneEvents(List<WareSceneEvent> sceneEvents) {
        this.sceneEvents = sceneEvents;
    }

    public List<WareTv> getTvs() {
        return tvs;
    }

    public void setTvs(List<WareTv> tvs) {
        if (this.tvs != null)
            this.tvs.clear();
        this.tvs = tvs;
    }

    public List<WareSetBox> getStbs() {
        return stbs;
    }

    public void setStbs(List<WareSetBox> stbs) {
        this.stbs = stbs;
    }

    public List<WareBoardChnout> getBoardChnouts() {
        return boardChnouts;
    }

    public void setBoardChnouts(List<WareBoardChnout> boardChnouts) {
        this.boardChnouts = boardChnouts;
    }

    public List<WareBoardKeyInput> getKeyInputs() {
        return keyInputs;
    }

    public void setKeyInputs(List<WareBoardKeyInput> keyInputs) {
        this.keyInputs = keyInputs;
    }

    public List<WareChnOpItem> getChnOpItems() {
        return chnOpItems;
    }

    public void setChnOpItems(List<WareChnOpItem> chnOpItems) {
        this.chnOpItems = chnOpItems;
    }

    public List<WareKeyOpItem> getKeyOpItems() {
        return keyOpItems;
    }

    public void setKeyOpItems(List<WareKeyOpItem> keyOpItems) {
        this.keyOpItems = keyOpItems;
    }

    public SetEquipmentResult getResult() {
        return result;
    }

    public void setResult(SetEquipmentResult result) {
        this.result = result;
    }

    public DevControl_Result getDev_result() {
        return dev_result;
    }

    public void setDev_result(DevControl_Result dev_result) {
        this.dev_result = dev_result;
    }

    public boolean isDATA_LOCAL_FLAG() {
        return DATA_LOCAL_FLAG;
    }

    public void setDATA_LOCAL_FLAG(boolean DATA_IP_FLAG) {
        this.DATA_LOCAL_FLAG = DATA_IP_FLAG;
    }

    public int getLogin_result() {
        return login_result;
    }

    public void setLogin_result(int login_result) {
        this.login_result = login_result;
    }

    public int getAddNewNet_reslut() {
        return addNewNet_reslut;
    }

    public void setAddNewNet_reslut(int addNewNet_reslut) {
        this.addNewNet_reslut = addNewNet_reslut;
    }

    public int getAddUser_reslut() {
        return addUser_reslut;
    }

    public void setAddUser_reslut(int addUser_reslut) {
        this.addUser_reslut = addUser_reslut;
    }
    public Bundle getDeleteNetReslut() {
        Bundle bundle = new Bundle();
        bundle.putString("id",DeleteDevid);
        bundle.putInt("Reslut",DeleteNetReslut);
        return bundle;

    }

    public void setDeleteNetReslut(String DeleteDevid,int DeleteNetReslut) {
        this.DeleteNetReslut = DeleteNetReslut;
        this.DeleteDevid = DeleteDevid;
    }
}

