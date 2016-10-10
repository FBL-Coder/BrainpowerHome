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
#define CLOUD_SERVER_IP       "192.168.1.114"

#define N                      100  //设置最大的定时器个数

int timer_num, timer_space; //i代表定时器的个数；t表示时间，逐秒递增
struct Timer //Timer结构体，用来保存一个定时器的信息
{
    int total_time;  //每隔total_time秒
    int left_time;   //还剩left_time秒
    int func;        //该定时器超时，要执行的代码的标志
} myTimer[N];   //定义Timer类型的数组，用来保存所有的定时器

typedef struct chns_status{
    u16 state;          //输出模块的通道状态
    u8  devUnitID[12];  //输出模块的cpuID
} CHNS_STATUS;

typedef int                  SOCKET;
typedef struct sockaddr_in   SOCKADDR_IN;


u8                 recvbuf[BUFF_SIZE];
u8                 sendbuf[BUFF_SIZE];
char local_ip[16];

SOCKET             primary_udp;
struct sockaddr_in sender;
int                sender_len;


int ret_thrd1;
pthread_t thread1;
int pthread_flag;

void udp_server();
UDPPROPKT *pre_send_udp_pkt(unsigned long sender_ip, u8 *dat, int dat_len, u8 cmd, u8* rcu_id, u8 *rcu_pwd, int ack, int sub_type1, int sub_type2);


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

#endif // UDP_H
