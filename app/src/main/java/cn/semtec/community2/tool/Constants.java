package cn.semtec.community2.tool;

public class Constants {
    // 网络协议
    public static final String SERVICE_PROTOCOL = "http://";
    //md-ss1.weedoor.com  东莞服务器
    //recom-s2.ldsd.cc:8080   南平服务器
    //semtec-test.weedoor.com:8080  伟创达测试服务器
    //app.ldsd.cc
    //10.52.1.2:8080
    //public static final String SERVICE_IP = SERVICE_PROTOCOL + "semtec-test.weedoor.com:8080";
    public static final String SERVICE_IP = SERVICE_PROTOCOL + "yitong-ss1.weedoor.com:8080";
    // 应用路径
    public static final String CONTENT_NAME = SERVICE_IP + "/smartcommunity";
    // 用户路径
    public static final String CONTENT_USER = CONTENT_NAME + "/user";
    // 城市路径
    public static final String CONTENT_CITY = CONTENT_USER + "/city/all";
    // 电话号码
    public static final String CONTENT_CELLPHONE = CONTENT_USER + "/cellphone/";
    // 验证码
    public static final String CONTENT_VERIFYCODE = "/verifycode";
    // 城市ID
    public static final String CONTENT_CITYAREACODE = CONTENT_USER + "/cityareacode/";
    // 社区
    public static final String CONTENT_COMMUNITIES = "/communities";
    // 社区ID
    public static final String CONTENT_COMMUNITYID = CONTENT_USER + "/communityid/";
    // 楼栋
    public static final String CONTENT_BLOCKS = "/blocks";
    // 楼栋ID
    public static final String CONTENT_BLOCKID = CONTENT_USER + "/blockid/";
    // 单元
    public static final String CONTENT_BUILDINGS = "/buildings";
    // 单元ID
    public static final String CONTENT_BUILDID = CONTENT_USER + "/buildid/";
    // 房间号
    public static final String CONTENT_HOUSES = "/houses";
    // 注册
    public static final String CONTENT_NEW = CONTENT_USER + "/register";
    //通过选择房屋信息绑定房产接口
    public static final String BINDHOUSEBYHOUSE = CONTENT_USER + "/bindhouseByHouse";
    //通过业主手机号绑定房产
    public static final String BINDHOUSEBYOWNER = CONTENT_USER + "/bindHouseByOwner";
    //APP用户扫描二维码绑定房产接口
    public static final String BINDHOUSEBYQRCODE = CONTENT_USER + "/bindHouseByQRcode";
    // 登录后台
    public static final String CONTENT_LOGIN = CONTENT_USER + "/login";
    // 登出后台
    public static final String CONTENT_LOGOUT = CONTENT_USER + "/logout";
    // 找回密码
    public static final String CONTENT_FORGET = CONTENT_USER + "/password/forget";
    // 待业主审批
    public static final String CONTENT_REQUEST = CONTENT_USER + "/setting/list2approved";
    // 审批同意
    public static final String CONTENT_REQUEST_APPROVED = CONTENT_USER + "/setting/approved";
    // 审批拒绝
    public static final String CONTENT_REQUEST_REJECTED = CONTENT_USER + "/setting/rejected";
    // 查看家庭成员接口
    public static final String CONTENT_GOVERN_FAMILY = CONTENT_USER + "/setting/familylist";
    // 删除家庭成员接口
    public static final String CONTENT_GOVERN_DELETE = CONTENT_USER + "/setting/familylist/delete";
    // 业主设置户主
    public static final String CONTENT_GOVERN_SETHOLDER = CONTENT_USER + "/setting/familylist/setHolder";
    // 设置是否手机接听模式
    public static final String CONTENT_PHONECALLTAG = CONTENT_USER + "/setting/phonecalltag";
    // 获取用户可用门口机
    public static final String CONTENT_GET_CLOCK = CONTENT_NAME + "/facility/publiclock/get";
    // 版本升级
    public static final String CONTENT_UPDATE = CONTENT_NAME + "/apps/sysparam/paramvalue/AndroidVer";
    // 问题反馈
    public static final String PROPERTY_CREATE = CONTENT_NAME + "/channel/propertycreate";
    // APP端获取开门记录接口
    public static final String CONTENT_LOG = CONTENT_NAME + "/facility/unlocklog/get";
    // APP端通过业主手机号绑定房产——-获取业主名下房产
    public static final String CONTENT_GETHOUSE = CONTENT_USER + "/bindHouse/getHouse";
}
