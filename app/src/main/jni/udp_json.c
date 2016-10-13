
#include "udp.h"
#include  "data_get.h"
#include "data_push.h"
#include "data_storage.h"

static int ware_dev_get_num = 0;
static int ware_scene_get_num = 0;
static int pkt_num = 0;

int timer_num, timer_space; //i代表定时器的个数；t表示时间，逐秒递增
struct Timer { //Timer结构体，用来保存一个定时器的信息
    int total_time;  //每隔total_time秒
    int left_time;   //还剩left_time秒
    int func;        //该定时器超时，要执行的代码的标志
} myTimer[N];   //定义Timer类型的数组，用来保存所有的定时器

u8 recvbuf[BUFF_SIZE];
u8 sendbuf[BUFF_SIZE];
char local_ip[IP_SIZE];
SOCKET  primary_udp;
struct sockaddr_in sender;
int sender_len;


int ret_thrd1;
pthread_t thread1;
int pthread_flag;

ware_linked_list ware_list;
aircond_linked_list aircond_list;
light_linked_list light_list;
curtain_linked_list curtain_list;
scene_linked_list scene_list;
rcu_linked_list rcu_list;
board_linked_list board_list;
keyinput_linked_list keyinput_list;
chnop_item_linked_list chnop_item_list;
keyop_item_linked_list keyop_item_list;

gw_client_linked_list gw_client_list;
app_client_linked_list app_client_list;
udp_msg_queue_linked_list msg_queue_list;

//5s search info
void get_info_from_gw_shorttime()
{
    node_udp_msg_queue *head = msg_queue_list.head;

    LOGI("短时查询命令数量 = %d\n", udp_msg_queue_get (&msg_queue_list, 0));

    rcu_display(&rcu_list);
    //ware_display(&ware_list);
    ware_aircond_display(&aircond_list);
    ware_light_display(&light_list);
    ware_scene_display(&scene_list);
    board_display(&board_list);
    keyinput_display(&keyinput_list);
    //keyop_item_display(&keyop_item_list);
    //chnop_item_display(&chnop_item_list);

    for(; head; head = head->next) {
        if(head->flag == 0) {
            node_gw_client *gw_head = gw_client_list.head;
            for (; gw_head; gw_head = gw_head->next) {
                if (memcmp(head->devUnitID, gw_head->gw_id, 12) == 0) {

                    char rcu_ip[16] = { 0 };
                    sprintf(rcu_ip, "%d.%d.%d.%d", gw_head->rcu_ip[0], gw_head->rcu_ip[1], gw_head->rcu_ip[2], gw_head->rcu_ip[3]);
                    LOGI("rcu_ip = %s\n", rcu_ip);
                    UDPPROPKT *send_pkt = pre_send_udp_pkt(inet_addr(rcu_ip), 0, 0, head->cmd, gw_head->gw_id, gw_head->gw_pass, IS_ACK, 0, head->id);

                    sendto(primary_udp, (u8 *)send_pkt, sizeof(UDPPROPKT), 0, (struct sockaddr *)&gw_head->gw_sender, sizeof(gw_head->gw_sender));
                    sleep(1);
                }
            }
        }
    }
}

//30m search info
void get_info_from_gw_longtime()
{
    node_udp_msg_queue *head = msg_queue_list.head;

    LOGI("长时查询命令数量 = %d\n", udp_msg_queue_get (&msg_queue_list, 1));

    if(head->flag == 1) {
        node_gw_client *gw_head = gw_client_list.head;
        for (; gw_head; gw_head = gw_head->next) {
            for(; head; head = head->next) {
                if (memcmp(head->devUnitID, gw_head->gw_id, 12) == 0) {
                    char rcu_ip[16] = { 0 };
                    sprintf(rcu_ip, "%d.%d.%d.%d", gw_head->rcu_ip[0], gw_head->rcu_ip[1], gw_head->rcu_ip[2], gw_head->rcu_ip[3]);

                    UDPPROPKT *send_pkt = pre_send_udp_pkt(inet_addr(rcu_ip), 0, 0, head->cmd, gw_head->gw_id, gw_head->gw_pass, IS_ACK, 0, head->id);
                    sendto(primary_udp, send_pkt, sizeof(UDPPROPKT), 0, (struct sockaddr *)&gw_head->gw_sender, sizeof(gw_head->gw_sender));
                    sleep(1);
                }
            }
        }
    }
}

void setTimer(int time,int fun) //新建一个计时器
{
    struct Timer a;
    a.total_time = time;
    a.left_time = time;
    a.func = fun;
    myTimer[timer_num++]=a;
}

void timeout(int arg)  //判断定时器是否超时，以及超时时所要执行的动作
{
    LOGI("Time: %d\n",timer_space++);
    int j;
    for(j=0; j<timer_num; j++) {
        if(myTimer[j].left_time!=0)
            myTimer[j].left_time--;
        else {
            switch(myTimer[j].func) { //通过匹配myTimer[j].func，判断下一步选择哪种操作
                case 1:
                    get_info_from_gw_shorttime ();
                    break;
                case 2:
                    get_info_from_gw_longtime ();
                    break;
            }
            myTimer[j].left_time=myTimer[j].total_time;     //循环计时
        }
    }
}

void *singal_msg(void *arg)
{
    while(1) {
        sleep(1); //每隔一秒发送一个SIGALRM
        //kill(getpid(),SIGALRM);
        //arg = NULL;
        timeout((int)arg);
    }
}

void extract_json(u8 *buffer, SOCKADDR_IN send_client)
{
    int devType;
    int devID;
    int subType2 = -1;
    u8 devUnitID[12];
    u8 devUnitPass[8];

    LOGI("收到的字符串:%s\n", buffer);
    //解析JSON数据
    cJSON *root_json = cJSON_Parse((char *)buffer);    //将字符串解析成json结构体
    if (NULL == root_json) {
        PR_ERR("error:%s\n", cJSON_GetErrorPtr());
        cJSON_Delete(root_json);
        return;
    }

    //devUnitID
    char *devUnitID_str = cJSON_GetObjectItem(root_json, "devUnitID")->valuestring;
    if (devUnitID_str != NULL) {
        string_to_bytes(devUnitID_str, devUnitID, 24);
        free(devUnitID_str);
    } else {
        return;
    }

    cJSON *devPass_json = cJSON_GetObjectItem(root_json, "devPass");
    if (devPass_json != NULL) {
        char *pass = devPass_json->valuestring;
        memcpy(devUnitPass, pass, 8);
        free(pass);
    }

    //datType
    int datType = cJSON_GetObjectItem(root_json, "datType")->valueint;
    switch (datType) {
        case e_udpPro_getBroadCast:
            udp_broadcast(devUnitID);
            break;
        case e_udpPro_getRcuInfo:
            get_rcu_info_json(devUnitID, (u8 *)devUnitPass, send_client);
            break;
        case e_udpPro_getDevsInfo:
            get_devs_info_json(devUnitID, send_client);
            break;
        case e_udpPro_editDev: {
            //重新打包数据，转发给联网模块
            devType = cJSON_GetObjectItem(root_json, "devType")->valueint;
            devID = cJSON_GetObjectItem(root_json, "devID")->valueint;
            //控制时cmd为控制命令，编辑和删除时cmd为设备类型T_WARE_TYPE
            int cmd = cJSON_GetObjectItem(root_json, "cmd")->valueint;
            cJSON *canCpuID_str = cJSON_GetObjectItem(root_json, "canCpuID");

            u8 canCpuID[12];
            string_to_bytes(cJSON_Print(canCpuID_str), canCpuID, 24);
            ctrl_devs_json(devUnitID, canCpuID, e_udpPro_editDev, devType, devID, cmd);
        }
            break;
        case e_udpPro_delDev: {
            //重新打包数据，转发给联网模块
            devType = cJSON_GetObjectItem(root_json, "devType")->valueint;
            devID = cJSON_GetObjectItem(root_json, "devID")->valueint;
            //控制时cmd为控制命令，编辑和删除时cmd为设备类型T_WARE_TYPE
            int cmd = cJSON_GetObjectItem(root_json, "cmd")->valueint;
            cJSON *canCpuID_str = cJSON_GetObjectItem(root_json, "canCpuID");

            u8 canCpuID[12];
            string_to_bytes(cJSON_Print(canCpuID_str), canCpuID, 24);
            ctrl_devs_json(devUnitID, canCpuID, e_udpPro_delDev, devType, devID, cmd);
        }
            break;
        case e_udpPro_ctrlDev: {//重新打包数据，转发给联网模块
            devType = cJSON_GetObjectItem(root_json, "devType")->valueint;
            devID = cJSON_GetObjectItem(root_json, "devID")->valueint;
            //控制时cmd为控制命令，编辑和删除时cmd为设备类型T_WARE_TYPE
            int cmd = cJSON_GetObjectItem(root_json, "cmd")->valueint;
            cJSON *canCpuID_str = cJSON_GetObjectItem(root_json, "canCpuID");

            u8 canCpuID[12];
            string_to_bytes(cJSON_Print(canCpuID_str)+1, canCpuID, 24);
            ctrl_devs_json(devUnitID, canCpuID, e_udpPro_ctrlDev, devType, devID, cmd);
        }
            break;
        case e_udpPro_getBoards: {
            subType2 = cJSON_GetObjectItem(root_json, "subType2")->valueint;
            if(subType2 == e_board_chnOut)
                get_board_chnout_json(devUnitID, send_client);
            else if(subType2 == e_board_keyInput)
                get_board_keyinput_json(devUnitID, send_client);
        }
            break;
        case e_udpPro_getSceneEvents:
            get_events_info_json(devUnitID, send_client);
            break;
        case e_udpPro_addSceneEvents: {
            char *sceneName = cJSON_GetObjectItem(root_json, "sceneName")->valuestring;
            int devCnt = cJSON_GetObjectItem(root_json, "devCnt")->valueint;
            int eventId = cJSON_GetObjectItem(root_json, "eventId")->valueint;
            cJSON *itemAry = cJSON_GetObjectItem(root_json, "itemAry");

            add_scene_json(devUnitID, sceneName, devCnt, eventId, itemAry);
        }
            break;
        case e_udpPro_editSceneEvents:
        case e_udpPro_delSceneEvents:
        case e_udpPro_exeSceneEvents: {
            int eventId = cJSON_GetObjectItem(root_json, "eventId")->valueint;
            ctrl_scene_json(devUnitID, eventId, datType);
        }
            break;
        case e_udpPro_getKeyOpItems: {
            char *canCpuID_str = cJSON_GetObjectItem(root_json, "canCpuID")->valuestring;
            if(canCpuID_str == NULL)
                return;
            u8 canCpuID[12];
            string_to_bytes(canCpuID_str+1, canCpuID, 24);

            int key_index = cJSON_GetObjectItem(root_json, "key_index")->valueint;

            get_key_opitem_json(devUnitID, canCpuID, key_index, send_client);
        }
            break;
        case e_udpPro_setKeyOpItems: {

            char *cpuCanID_str = cJSON_GetObjectItem(root_json, "cpuCanID")->valuestring;
            u8 canCpuID[12];
            string_to_bytes(cpuCanID_str, canCpuID, 24);

            int key_index = cJSON_GetObjectItem(root_json, "key_index")->valueint;
            int cnt = cJSON_GetObjectItem(root_json, "cnt")->valueint;
            cJSON *keyop_item = cJSON_GetObjectItem(root_json, "keyop_item");

            set_key_opitem_json(devUnitID, canCpuID, key_index, cnt, keyop_item, e_udpPro_setKeyOpItems);
        }
            break;
        case e_udpPro_delKeyOpItems: {

            char *cpuCanID_str = cJSON_GetObjectItem(root_json, "cpuCanID")->valuestring;
            u8 canCpuID[12];
            string_to_bytes(cpuCanID_str, canCpuID, 24);

            int key_index = cJSON_GetObjectItem(root_json, "key_index")->valueint;
            int cnt = cJSON_GetObjectItem(root_json, "cnt")->valueint;
            cJSON *keyop_item = cJSON_GetObjectItem(root_json, "keyop_item");

            set_key_opitem_json(devUnitID, canCpuID, key_index, cnt, keyop_item, e_udpPro_delKeyOpItems);
        }
            break;
        case e_udpPro_getChnOpItems: {
            char *cpuCanID_str = cJSON_GetObjectItem(root_json, "cpuCanID")->valuestring;
            u8 canCpuID[12];
            string_to_bytes(cpuCanID_str, canCpuID, 24);

            devType = cJSON_GetObjectItem(root_json, "devType")->valueint;
            devID = cJSON_GetObjectItem(root_json, "devID")->valueint;

            get_chn_opitem_json(devUnitID, canCpuID, devType, devID, send_client);
        }
            break;
        case e_udpPro_setChnOpItems: {
            char *cpuCanID_str = cJSON_GetObjectItem(root_json, "cpuCanID")->valuestring;
            u8 canCpuID[12];
            string_to_bytes(cpuCanID_str, canCpuID, 24);

            devType = cJSON_GetObjectItem(root_json, "devType")->valueint;
            devID = cJSON_GetObjectItem(root_json, "devID")->valueint;
            int cnt = cJSON_GetObjectItem(root_json, "cnt")->valueint;
            cJSON *chnop_item = cJSON_GetObjectItem(root_json, "chnop_item");

            set_chn_opitem_json(devUnitID, canCpuID, devType, devID, cnt, chnop_item, e_udpPro_setChnOpItems);
        }
            break;
        case e_udpPro_delChnOpItems: {
            char *cpuCanID_str = cJSON_GetObjectItem(root_json, "cpuCanID")->valuestring;
            u8 canCpuID[12];
            string_to_bytes(cpuCanID_str, canCpuID, 24);

            devType = cJSON_GetObjectItem(root_json, "devType")->valueint;
            devID = cJSON_GetObjectItem(root_json, "devID")->valueint;
            int cnt = cJSON_GetObjectItem(root_json, "cnt")->valueint;
            cJSON *chnop_item = cJSON_GetObjectItem(root_json, "chnop_item");

            set_chn_opitem_json(devUnitID, canCpuID, devType, devID, cnt, chnop_item, e_udpPro_delChnOpItems);
        }
            break;

        default:
            break;
    }
}

void extract_data(UDPPROPKT *udp_pro_pkt, int dat_len, SOCKADDR_IN sender)
{
    if (dat_len < BUFF_MIN_SIZE) {
        return;
    }

    if (udp_pro_pkt->datType != 2) {
        LOGI("datType = %d + ******* + subType1 = %d + ******* + subType2 = %d\n", udp_pro_pkt->datType, udp_pro_pkt->subType1, udp_pro_pkt->subType2);
    }

    if (udp_pro_pkt->bAck == 2) {
        LOGI("应答的数据包\n");
        return;
    }

    switch (udp_pro_pkt->datType) {
        case e_udpPro_getRcuInfo:          // e_udpPro_getRcuinfo
            if (udp_pro_pkt->subType2 == 1) {
                //联网模块发送信息到服务器
                set_rcuinfo(udp_pro_pkt, sender);
                //给UI发送收到消息info
                get_broadcast_reply_json(udp_pro_pkt);
                //启动线程，定时发送获取数据命令
                if(pthread_flag == 0) {
                    ret_thrd1 = pthread_create(&thread1, NULL, singal_msg, NULL);
                    // 线程创建成功，返回0,失败返回失败号
                    if (ret_thrd1 != 0) {
                        PR_ERR ("线程1创建失败\n");
                    } else {
                        LOGI("线程1创建成功\n");
                        pthread_flag = 1;
                    }
                }
            }
            break;
        case e_udpPro_handShake: //握手应答
            handShake(udp_pro_pkt);
            break;

        case e_udpPro_getDevsInfo:
            if (udp_pro_pkt->subType1 == 1) {
                set_dev_info(udp_pro_pkt);

                ware_dev_get_num++;
                if(ware_dev_get_num == 5) {
                    ware_dev_get_num = 0;
                    udp_msg_queue_add (&msg_queue_list, udp_pro_pkt->uidSrc, e_udpPro_getDevsInfo, 0, 1, msg_queue_list.size);
                }
            }
            break;

        case e_udpPro_ctrlDev:
        case e_udpPro_editDev:
            if (udp_pro_pkt->subType1 == 1) {
                //控制设备之后，返回设备的最新数据，直接更新链表
                fresh_dev_info (udp_pro_pkt);
                get_all_ctl_reply_json(udp_pro_pkt);
            }
            break;

        case e_udpPro_delDev:
            if (udp_pro_pkt->subType1 == 1) {
                del_dev_info(udp_pro_pkt);
                //转发该数据包到所有app
                get_all_ctl_reply_json(udp_pro_pkt);
            }
            break;

        case e_udpPro_getSceneEvents:
            if (udp_pro_pkt->subType1 == 0 && udp_pro_pkt->subType2 == 1) {
                set_events_info (udp_pro_pkt);
                ware_scene_get_num++;
                if(ware_scene_get_num == 15) {
                    udp_msg_queue_add (&msg_queue_list, udp_pro_pkt->uidSrc, e_udpPro_getSceneEvents, 0, 1, msg_queue_list.size);
                }
            }
            break;

        case e_udpPro_addSceneEvents:
        case e_udpPro_editSceneEvents:
        case e_udpPro_exeSceneEvents:
            if (udp_pro_pkt->subType1 == 0 && udp_pro_pkt->subType2 == 1) {
                set_events_info(udp_pro_pkt);
                get_scene_ctl_reply_json(udp_pro_pkt);
            }
            break;

        case e_udpPro_delSceneEvents:
            if (udp_pro_pkt->subType1 == 0 && udp_pro_pkt->subType2 == 1) {
                del_scene_info(udp_pro_pkt);
                get_scene_ctl_reply_json(udp_pro_pkt);//客户端需要删除该情景模式
            }

        case e_udpPro_getBoards:
            if (udp_pro_pkt->subType1 == 1) {
                set_board_info(udp_pro_pkt);

                switch (udp_pro_pkt->subType2) {
                    case e_board_chnOut:
                        udp_msg_queue_add (&msg_queue_list, udp_pro_pkt->uidSrc, e_udpPro_getBoards, e_board_chnOut, 1, msg_queue_list.size);
                        break;
                    case e_board_keyInput:
                        udp_msg_queue_add (&msg_queue_list, udp_pro_pkt->uidSrc, e_udpPro_getBoards, e_board_keyInput, 1, msg_queue_list.size);
                        break;
                    case e_board_wlessIR:
                        udp_msg_queue_add (&msg_queue_list, udp_pro_pkt->uidSrc, e_udpPro_getBoards, e_board_keyInput, 1, msg_queue_list.size);
                        break;
                    case e_board_envDetect:
                        udp_msg_queue_add (&msg_queue_list, udp_pro_pkt->uidSrc, e_udpPro_getBoards, e_board_envDetect, 1, msg_queue_list.size);
                        break;
                    default:
                        break;
                }
            }
            break;

        case e_udpPro_editBoards:
            if (udp_pro_pkt->subType1 == 1) {
                if (udp_pro_pkt->subType2 == 1) {
                    set_board_info(udp_pro_pkt);
                }
                //forward_gwdat_toapp(udp_pro_pkt, dat_len);
            }
            break;

        case e_udpPro_delBoards:
            if (udp_pro_pkt->subType1 == 1) {
                if (udp_pro_pkt->subType2 == 1) {
                    del_board_info(udp_pro_pkt);
                }
                //forward_gwdat_toapp(udp_pro_pkt, dat_len);
            }
            break;

        case e_udpPro_getKeyOpItems:
            if (udp_pro_pkt->subType1 == 1) {
                set_key_opitem(udp_pro_pkt);
                udp_msg_queue_add (&msg_queue_list, udp_pro_pkt->uidSrc, e_udpPro_getKeyOpItems, 0, 1, msg_queue_list.size);
            }
            break;
        case e_udpPro_setKeyOpItems:
            if (udp_pro_pkt->subType1 == 1) {
                int result = udp_pro_pkt->dat[12];
                if (result == 1) {
                    set_key_opitem(udp_pro_pkt);
                }
                set_key_opitem_reply_json(udp_pro_pkt);
            }
            break;
        case e_udpPro_delKeyOpItems:
            if (udp_pro_pkt->subType1 == 1) {
                int result = (int)udp_pro_pkt->dat[12];
                if (result == 1) {
                    del_key_opitem(udp_pro_pkt);
                }
                set_key_opitem_reply_json(udp_pro_pkt);
            }
            break;
        case e_udpPro_getChnOpItems:
            if (udp_pro_pkt->subType1 == 1) {
                set_chn_opitem(udp_pro_pkt);
                udp_msg_queue_add (&msg_queue_list, udp_pro_pkt->uidSrc, e_udpPro_getChnOpItems, 0, 1, msg_queue_list.size);
            }
            break;

        case e_udpPro_setChnOpItems:
            if (udp_pro_pkt->subType1 == 1) {
                int result = (int)udp_pro_pkt->dat[12];
                if (result == 1) {
                    set_chn_opitem(udp_pro_pkt);
                }
                set_chn_opitem_reply_json(udp_pro_pkt);
            }
            break;

        case e_udpPro_delChnOpItems:
            if (udp_pro_pkt->subType1 == 1) {
                int result = (int)udp_pro_pkt->dat[12];
                if (result == 1) {
                    del_chn_opitem (udp_pro_pkt);
                }
                set_chn_opitem_reply_json(udp_pro_pkt);
            }
            break;
        case e_udpPro_chns_status:
            ctrl_dev_reply(udp_pro_pkt);
            get_light_ctr_reply_json(udp_pro_pkt);
            break;
        default:
            break;
    }
}

void udp_broadcast(u8 *uid)
{
    u8 devUnitID[12];
    string_to_bytes(uid, devUnitID, 24);
    UDPPROPKT *pkt = udp_pkt_bradcast(devUnitID);

    int optval = 1;//这个值一定要设置，否则可能导致sendto()失败
    setsockopt(primary_udp, SOL_SOCKET, SO_BROADCAST, &optval, sizeof(int));
    struct sockaddr_in theirAddr;
    memset(&theirAddr, 0, sizeof(struct sockaddr_in));
    theirAddr.sin_family = AF_INET;
    theirAddr.sin_addr.s_addr = inet_addr("255.255.255.255");
    theirAddr.sin_port = htons(ClOUD_SERVER_PORT);

    if((sendto(primary_udp, pkt, sizeof(UDPPROPKT), 0,
                           (struct sockaddr *)&theirAddr, sizeof(struct sockaddr))) == -1) {
        printf("sendto fail, errno=%d\n", errno);
        return ;
    }
}

void udp_server(char *ip)
{
    primary_udp = init_socket(SOCK_TYPE); //SOCK_DGRAM

    struct sockaddr_in local;
    local.sin_family      = AF_INET;
    local.sin_port        = htons(ClOUD_SERVER_PORT);
    local.sin_addr.s_addr = htonl(INADDR_ANY);

    int result = bind(primary_udp, (struct sockaddr*)&local, sizeof(local));
    if (result == SOCKET_ERROR) {
        PR_ERR("bind");
    }

    LOGI("Cloud sever bind to port %d\n", ClOUD_SERVER_PORT);
    LOGI("Sever begin to listen......\n");

    get_local_ip("wanl0", local_ip);
    udp_broadcast("37ffdb05424e323416702443");

    memset(&recvbuf, 0, sizeof(recvbuf));

    timer_num = 0;
    timer_space = 1;
    pthread_flag = 0;
    setTimer (2,1);
    setTimer (60*30,2);
    //signal (SIGALRM,timeout);

    rcu_list = rcu_create_linked_list ();
    ware_list = ware_create_linked_list();
    aircond_list = ware_aircond_create_linked_list();
    light_list = ware_light_create_linked_list ();
    curtain_list = ware_curtain_create_linked_list ();
    scene_list = ware_scene_create_linked_list ();
    board_list = board_create_linked_list ();
    keyinput_list = keyinput_create_linked_list ();
    chnop_item_list = chnop_item_create_linked_list ();
    keyop_item_list = keyop_item_create_linked_list ();
    gw_client_list = gw_client_create_linked_list ();
    app_client_list = app_client_create_linked_list ();
    msg_queue_list = udp_msg_queue_create_linked_list ();

    while (1) {
        sender_len = sizeof(sender);

        int ret = recvfrom(primary_udp, (u8*)&recvbuf, BUFF_SIZE, 0, (struct sockaddr *)&sender, (socklen_t*)&sender_len);

        if (ret <= 0) {
            usleep(1000);   //printf("recv error");
            continue;
        } else {
            /* 显示client端的网络地址*/
            LOGI ("Received a string from client %s port %d\n", inet_ntoa(sender.sin_addr), sender.sin_port);
            if (memcmp(recvbuf, HEAD_STRING, 4) != 0) {
                extract_json(recvbuf, sender);
            } else {
                extract_data((UDPPROPKT*)recvbuf, ret, sender);
            }
        }
    }
}
