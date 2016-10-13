#ifndef UDP_H
#define UDP_H

#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <assert.h>
#include <signal.h>
#include <time.h>
#include <sys/time.h>
#include <pthread.h>
#include <netinet/in.h>

#include "udppropkt.h"
#include "str_comm.h"
#include "debug.h"
#include "json.h"
#include "link_list.h"

#define VER_SWITCH            0
#define SOCK_TYPE             SOCK_DGRAM
#define ClOUD_SERVER_PORT     8300
#define SOCKET_ERROR          -1
#define BUFF_SIZE             1024
#define BUFF_MIN_SIZE         56
#define HEAD_STRING           "head"
#define CLOUD_SERVER_IP       "192.168.1.220"

#define GW_CLIENT             0
#define APP_CLIENT            1

#define N                     100  //设置最大的定时器个数

typedef struct chns_status {
    u16 state;          //输出模块的通道状态
    u8  devUnitID[12];  //输出模块的cpuID
} CHNS_STATUS;

typedef int                  SOCKET;
typedef struct sockaddr_in   SOCKADDR_IN;

extern u8 recvbuf[BUFF_SIZE];
extern u8 sendbuf[BUFF_SIZE];

#define IP_SIZE     16
extern char local_ip[IP_SIZE];
extern struct sockaddr_in sender;
extern int sender_len;

extern SOCKET  primary_udp;
extern ware_linked_list ware_list;
extern aircond_linked_list aircond_list;
extern light_linked_list light_list;
extern curtain_linked_list curtain_list;
extern scene_linked_list scene_list;
extern rcu_linked_list rcu_list;
extern board_linked_list board_list;
extern keyinput_linked_list keyinput_list;
extern chnop_item_linked_list chnop_item_list;
extern keyop_item_linked_list keyop_item_list;

extern gw_client_linked_list gw_client_list;
extern app_client_linked_list app_client_list;
extern udp_msg_queue_linked_list msg_queue_list;

extern void udp_server(char *ip);
extern UDPPROPKT *pre_send_udp_pkt(unsigned long sender_ip, u8 *dat, int dat_len, u8 cmd, u8* rcu_id, u8 *rcu_pwd, int ack, int sub_type1, int sub_type2);

#endif // UDP_H
