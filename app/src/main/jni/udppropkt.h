#ifndef UDPPROPKT_H
#define UDPPROPKT_H

#include "str_comm.h"

#define UDP_PKT_SIZE          56
#define RCU_INFO_SIZE         64
#define WARE_DEV_SIZE         40
#define AIR_SIZE              sizeof(DEV_PRO_AIRCOND)
#define LIGHT_SIZE            sizeof(DEV_PRO_LIGHT)
#define CURTAIN_SIZE          sizeof(DEV_PRO_CURTAIN)
#define WARE_AIR_SIZE         (sizeof(WARE_DEV) - 4 + sizeof(DEV_PRO_AIRCOND))
#define WARE_LGT_SIZE         (sizeof(WARE_DEV) - 4 + sizeof(DEV_PRO_LIGHT))
#define WARE_CUR_SIZE         (sizeof(WARE_DEV) - 4 + sizeof(DEV_PRO_CURTAIN))
#define WARE_TV_SIZE          (sizeof(WARE_DEV))
#define WARE_TVUP_SIZE        (sizeof(WARE_DEV))
#define SCENE_SIZE             (16 + 20 * 32)
#define BOARDCHNOUT_SIZE      (28 + 144)
#define BOARDKEYINPUT_SIZE    (28 + 6 * 12 + 6)
#define BOARDWLESSIR_SIZE     28
#define BOARDENVDETECT_SIZE   34
#define KEY_OPITEM_SIZE       16
#define CHN_OPITEM_SIZE       32

//用户传输传输数据帧格式
typedef struct tUDPPROPKT
{
   u8  head[4];    //固定值，字符串"head"
   u8  srcIp[4];   //源IP
   u8  dstIp[4];   //目的IP
   u8  uidDst[12]; //目的uid地址
   u8  pwdDst[8];  //访问目标Rcu密码
   u8  uidSrc[12]; //源uid地址
   u16 snPkt;      //帧编号，防止重复处理
   u8  sumPkt;     //本帧总包数
   u8  currPkt;    //当前第几包，从0开始计数
   u8  bAck;       //应答属性
   u8  datType;    //数据含义分类，取值E_UDP_PRO_DAT枚举
   u8  subType1;   //辅助参数
   u8  subType2;   //辅助参数
   u16 datLen;     //本包数据的dat成员长度
   u16 rev;
   u8  dat[0];     //数据区,变长
} UDPPROPKT;

typedef enum
{
   NOT_ACK,
   IS_ACK,
   NOW_ACK,
}IS_ACK_TYPE;

//设备类型定义
typedef enum
{
   e_ware_airCond,
   e_ware_tv,
   e_ware_tvUP,
   e_ware_light,
   e_ware_curtain,
   e_ware_lock,
   e_ware_value,
   e_ware_fresh_air,
   e_ware_multiChn,
   e_ware_other,
}E_WARE_TYPE;

//设备控制类型
typedef enum
{
   e_dev_IR,    //红外控制类
   e_dev_315M,  // 315M控制类
   e_dev_Chn,   //通道控制类
}E_DEV_TYPE;

//网关信息结构体定义
typedef struct rcu_info
{
   u8 devUnitID[12];  //联网模块的cpuid
   u8 devUnitPass[8]; //联网模块的访问密码
   u8 name[12];
   u8 IpAddr[4];
   u8 SubMask[4];
   u8 Gateway[4];
   u8 centerServ[4];
   u8 roomNum[4];      //
   u8 macAddr[6];
   u8 SoftVersion[2];
   u8 HwVersion[2];
   u8 bDhcp;
   u8 rev2;
} RCU_INFO;

//设备信息结构体定义：
typedef struct t_ware_dev
{
   u8 canCpuId[12];       //设备的执行端（输出模块）的总线id
   u8 devName[12];        //设备所属房间的名称
   u8 roomName[12];       //设备所属房间的名称
   u8 devType;            //设备类型 取值范围E_WARE_TYPE
   u8 devId;              //设备ID
   u8 devCtrlType;        //设备类型 取值范围E_DEV_TYPE
   u8 datLen;             //设备详细信息数据区长度    40字节的设备头信息
   u8 dat[4];             // 设备详细信息数据区  长度可变  对应不同的设备属性数据 但最大长度不能大于255
}WARE_DEV;

//空调设备属性
typedef struct tDevProAircond
{
   u8  bOnOff;            //开关状态 0关闭  1打开
   u8  selMode;           //当前选择的模式
   u8  selTemp;           //当前选择的温度
   u8  selSpd;            //当前选择的风速
   u8  selDirect;         //当前选择的风向
   u8  rev1;              //保留未用
   u16 powChn;            // 一个u16可以表示0-15号通道 线控中央空调 一般对应5根线  依次定义为 冷 热 大 中 小
}DEV_PRO_AIRCOND;


//空调的模式定义：
typedef enum
{
   e_air_auto = 0,
   e_air_hot,
   e_air_cool,
   e_air_dry,
   e_air_wind,
   e_air_mode_total,
}E_AIR_MODE;

//空调的命令定义
typedef enum
{
   e_air_pwrOn = 0,
   e_air_pwrOff,
   e_air_spdLow,
   e_air_spdMid,
   e_air_spdHigh,
   e_air_spdAuto,
   e_air_drctUpDn1,     //上下摇摆
   e_air_drctUpDn2,
   e_air_drctUpDn3,
   e_air_drctUpDnAuto,
   e_air_drctLfRt1,     //左右摇摆
   e_air_drctLfRt2,
   e_air_drctLfRt3,
   e_air_drctLfRtAuto,
   e_air_temp14,
   e_air_temp15,
   e_air_temp16,
   e_air_temp17,
   e_air_temp18,
   e_air_temp19,
   e_air_temp20,
   e_air_temp21,
   e_air_temp22,
   e_air_temp23,
   e_air_temp24,
   e_air_temp25,
   e_air_temp26,
   e_air_temp27,
   e_air_temp28,
   e_air_temp29,
   e_air_temp30,
   e_air_cmd_total,
}E_AIR_CMD;

//电视机命令
typedef enum
{
   e_tv_offOn = 0,
   e_tv_mute,
   e_tv_numTvAv,

   e_tv_num1,
   e_tv_num2,
   e_tv_num3,

   e_tv_num4,
   e_tv_num5,
   e_tv_num6,

   e_tv_num7,
   e_tv_num8,
   e_tv_num9,

   e_tv_numMenu,
   e_tv_numUp,
   e_tv_num0,

   e_tv_numLf,
   e_tv_enter,
   e_tv_numRt,

   e_tv_numRet,
   e_tv_numDn,
   e_tv_numLookBack,

   e_tv_userDef1,
   e_tv_userDef2,
   e_tv_userDef3,
   e_tv_cmd_total,
}E_TV_CMD;

//机顶盒命令
typedef enum
{
   e_tvUP_offOn = 0,        //电源键
   e_tvUP_mute,             //静音
   e_tvUP_numPg,            //主页键

   e_tvUP_num1,
   e_tvUP_num2,
   e_tvUP_num3,

   e_tvUP_num4,
   e_tvUP_num5,
   e_tvUP_num6,

   e_tvUP_num7,
   e_tvUP_num8,
   e_tvUP_num9,

   e_tvUP_numDemand,        //查询键
   e_tvUP_numUp,
   e_tvUP_num0,

   e_tvUP_numLf,
   e_tvUP_enter,
   e_tvUP_numRt,

   e_tvUP_numu16eract,
   e_tvUP_numDn,
   e_tvUP_numBack,

   e_tvUP_numVInc,
   e_tvUP_numInfo,
   e_tvUP_numPInc,

   e_tvUP_numVDec,
   e_tvUP_numLive,
   e_tvUP_numPDec,

   e_tvUP_userDef1,
   e_tvUP_userDef2,
   e_tvUP_userDef3,
   e_tvUP_cmd_total,
}E_TVUP_CMD;

//灯光设备属性
typedef struct tDevProLight
{
   u8 bOnOff;         //开关状态 0关闭  1打开
   u8 bTuneEn;        //是否可调 0否   1是
   u8 lmVal;          //如果是可调灯光 亮度值 范围0-9
   u8 powChn;         //输出模块的通道号
}DEV_PRO_LIGHT;       //整个灯光设备信息字节数44

//灯光控制命令
typedef enum
{
   e_lgt_offOn = 0,
   e_lgt_onOff,
   e_lgt_dark,
   e_lgt_bright,
   e_lgt_cmd_total,
}E_LGT_CMD;

//窗帘设备属性
typedef struct tDevProCurtain
{
   u8  bOnOff;       //开关状态 0关闭  1打开
   u8  timRun;       //开关电机运转的时间  超时停止转动  防止有的电机没有行程限制
   u16 powChn;       //输出模块的通道号  ?
}DEV_PRO_CURTAIN;

//窗帘控制命令
typedef enum
{
   e_curt_offOn = 0,
   e_curt_onOff,
   e_curt_stop,
   e_curt_cmd_total,
}E_CURT_CMD;

//阀门设备属性数据
typedef struct tDevProValve
{
   u8 bOnOff;           //开关状态 0关闭  1打开
   u8 timRun;           //开关电机运转的时间  超时停止转动  防止有的电机没有行程限制
   u8 powChnOpen;       //开阀门通道
   u8 powChnClose;      //开阀门通道
}DEV_PRO_VALVE;         //整个阀门设备信息字节数44

//阀门控制命令
typedef enum
{
   e_valve_offOn = 0,
   e_valve_onOff,
   e_valve_stop,
   e_valve_cmd_total,
}E_VALVE_CMD;

//门锁设备属性数据

typedef struct tDevProLock
{
   u8 bOnOff;          //开关状态0关闭  1打开
   u8 timRun;          //开锁超时 锁打开后必须一段时间之内关上
   u8 bLockOut;        //是否反锁  0否  1是
   u8 powChnOpen;      //开锁通道
   u8 pwd[8];          // 开锁用8位密码
}DEV_PRO_LOCK;         //整个门锁设备信息字节数52

//门锁控制命令
typedef enum
{
   e_lock_open = 0,
   e_lock_close,
   e_lock_stop,
   e_lock_lockOut,
   e_lock_cmd_total,
}E_LOCK_CMD;

//新风设备属性数据
typedef struct tDevProFreshAir
{
   u8 bOnOff;         //开关状态  0关闭  1打开
   u8 spdSel;         //新风运行的风速
   u8 powChn;         //新风设备的控制通道  依次对应 开关 大 中 小
}DEV_PRO_FRESHAIR;    //整个新风设备信息字节数44

//新风控制命令
typedef enum
{
   e_freshair_open = 0,
   e_freshair_spd_low,
   e_freshair_spd_mid,
   e_freshair_spd_high,
   e_freshair_close,
   e_freshair_cmd_total,
}E_FRESHAIR_CMD;

typedef struct tDevMultiItem
{
   u8  uidBoard[12];  //通道所在的控制板
   u16 powChn;
   u16 rev;
}DEV_MULTI_ITEM;

//多通道设备属性
typedef struct tDevProMultiChn
{
   u8             bOnOff;         //开关状态 0关闭  1打开
   u8             itemCnt;        // item数组的元素个数
   u8             rev2;           //保留未用
   u8             rev3;           //保留未用
   DEV_MULTI_ITEM itemAry[12];    //数组长度可变  但是最大不超过12个
}DEV_PRO_MULTICHN;                //整个多通道设备信息字节数

// 多通道控制命令
typedef enum
{
   e_multiChn_offOn = 0,
   e_multiChn_onOff,
   e_multiChn_cmd_total,
}E_MULTICHN_CMD;

typedef enum
{
   e_udpPro_getRcuInfo = 0,
   e_udpPro_setRcuInfo,
   e_udpPro_handShake,
   e_udpPro_getDevsInfo,
   e_udpPro_ctrlDev,
   e_udpPro_addDev,
   e_udpPro_editDev,
   e_udpPro_delDev,
   e_udpPro_getBoards,
   e_udpPro_editBoards,
   e_udpPro_delBoards,
   e_udpPro_getKeyOpItems,
   e_udpPro_setKeyOpItems,
   e_udpPro_delKeyOpItems,
   e_udpPro_getChnOpItems,
   e_udpPro_setChnOpItems,
   e_udpPro_delChnOpItems,
   e_udpPro_getTimerEvents,
   e_udpPro_addTimerEvents,
   e_udpPro_editTimerEvents,
   e_udpPro_delTimerEvents,
   e_udpPro_exeTimerEvents,

   e_udpPro_getSceneEvents,
   e_udpPro_addSceneEvents,
   e_udpPro_editSceneEvents,
   e_udpPro_delSceneEvents,
   e_udpPro_exeSceneEvents,

   e_udpPro_getEnvEvents,
   e_udpPro_addEnvEvents,
   e_udpPro_editEnvEvents,
   e_udpPro_delEnvEvents,
   e_udpPro_exeEnvEvents,
   e_udpPro_security_info,
   e_udpPro_getRcuInfoNoPwd,
   e_udpPro_pwd_error,
   e_udpPro_chns_status,
   e_udpPro_keyInput_info,

   e_udpPro_getIOSet_input,
   e_udpPro_getIOSet_output,
   e_udpPro_saveIOSet_input,
   e_udpPro_saveIOSet_output,
   e_udpPro_studyIR_cmd,
   e_udpPro_studyIR_cmd_ret,

   e_udpPro_report_output,
   e_udpPro_report_cardInput,

   e_udpPro_irDev_exeRet,
   e_udpPro_quick_setDevKey,
   e_udpPro_quick_delDevKey,
   e_udpPro_get_modulePos,

   e_udpPro_soft_iap_data,
   e_udpPro_get_soft_version,
   e_udpPro_bc_key_ctrl,
   e_udpPro_ctrl_allDevs,
    e_udpPro_getBroadCast=60,
}E_UDP_PRO_DAT;

typedef enum
{
   e_86key_air_power = 0,
   e_86key_air_mode,
   e_86key_air_spd,
   e_86key_air_tempInc,
   e_86key_air_tempDec,
}E_86KEY_AIR_CMD;

typedef enum
{
   e_86keyMutex_null = 0,
   e_86keyMutex_on,
   e_86keyMutex_off,
   e_86keyMutex_stop,
   e_86keyMutex_loop,
}E_86KEY_MUTEX_TYPE;

typedef enum
{
   e_86keyCtrl_null = 0,
   e_86keyCtrl_offOn,
   e_86keyCtrl_onOff,
   e_86keyCtrl_power,
   e_86keyCtrl_dark,
   e_86keyCtrl_bright,
   e_86keyCtrl_cmd_total,
}E_86KEY_CTRL_TYPE;

typedef enum
{
   e_board_chnOut = 0,
   e_board_keyInput,
   e_board_wlessIR,
   e_board_envDetect,
}E_BOARD_TYPE;

//输出模块：
typedef struct BOARD_CHNOUT
{
   u8 devUnitID[12];      //模块的cpuid
   u8 boardName[12];      //模块名称
   u8 boardType;          //控制板类型 取值e_board_chnOut
   u8 chnCnt;             //通道数
   u8 bOnline;            //是否在线 由RCU定时发出握手包检测
   u8 rev2;
   u8 chnName[12][12];    //目前输出模块最多12个通道   长度根据chnCnt可变
}BOARD_CHNOUT;

//输入模块：
typedef struct BOARD_KEYINPUT
{
   u8  devUnitID[12];     //模块的cpuid
   u8  boardName[12];     //模块名称
   u8  boardType;         //控制板类型 取值e_board_keyInput
   u8  keyCnt;            //按键数
   u8  bResetKey;         //是否是复位按键0复位     1非复位
   u8  ledBkType;         //按键的背光灯模式//背光灯   0 随灯状态变化   1 常亮      2 不亮
   u8  keyName[6][12];    //目前输入模块最多6个按键
   u8  keyAllCtrlType[6]; //总开总关 0 否 1灯总开 2灯总关 3灯总开关 4窗帘类总开 5窗帘类总关 6窗帘类总开关 7全部总开 8 全部总关  9全部总开关
   u16 rev;
}BOARD_KEYINPUT;

//无线信号转发模块：
typedef struct BOARD_WLESSIR
{
   u8 devUnitID[12]; //模块的cpuid
   u8 boardName[12]; //模块名称
   u8 boardType;     //控制板类型 取值e_board_wlessIR
   u8 bOnline;       //是否在线 由RCU定时发出握手包检测
   u8 rev2;
   u8 rev3;
}BOARD_WLESSIR;

//环境参数探测模块：
typedef struct BOARD_ENVDETECT
{
   u8  devUnitID[12]; //模块的cpuid
   u8  boardName[12]; //模块名称
   u8  boardType;     //控制板类型 取值e_board_envDetect,
   u8  temp;
   u8  humidity;
   u8  bOnline;//是否在线 由RCU定时发出握手包检测
   u16 pm25;
   u8  rev2;
   u8  rev3;
}BOARD_ENVDETECT;


//输出模块与输入模块对应关系配置
typedef struct chnop_item
{
   u8  devUnitID[12];    //按键模块的cpuid
   u8  keyDownValid;     //按键板最多6个按键，bit0~bit5 = 1 表示有效
   u8  keyUpValid;       //按键板最多6个按键，bit0~bit5 = 1 表示有效
   u16 rev1;
   u8  keyDownCmd[6];    //每个按键针对本设备的控制命令
   u16 rev2;
   u8  keyUpCmd[6];      //每个按键针对本设备的控制命令
   u16 rev3;
}CHNOP_ITEM;

//输入模块上保存着每个按键与总线上输出模块上的设备的对应关系，数据定义如下：
typedef struct keyop_item
{
   u8 devUnitID[12];    //输出模块的cpuid
   u8 devType;          //设备类型
   u8 devId;            //设备Id
   u8 keyOpCmd;         //设备操作命令
   u8 keyOp;            //按键弹起或是按下   0：按下    1：弹起
}KEYOP_ITEM;

typedef struct run_dev_item
{
   u8 uid[12];          //设备所在控制板的cpuid
   u8 devType;          //设备的类型 E_WARE_TYPE
   u8 lmVal;            //保存灯光的亮度值  0-9
   u8 rev2;
   u8 rev3;
   u8 devID;           //设备在控制板上的id
   u8 bOnoff;
   u8 param1;          //设备定时运行的参数1
   u8 param2;          //设备定时运行的参数2
}RUN_DEV_ITEM;

//情景模式
typedef struct
{
   u8           sceneName[12]; //名称
   u8           devCnt;        //参与事件的设备数目
   u8           eventId;       //事件id  用于在联网模块上唯一表示事件身份 id0、1的情景定义为在家模式与外出模式 且不可删除
   u8           rev2;
   u8           rev3;
   RUN_DEV_ITEM itemAry[32]; // 数目devCnt  最多32个
}SCENE_EVENT;

#endif // UDPPROPKT_H
