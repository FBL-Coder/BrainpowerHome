#include "str_comm.h"
#include <stdlib.h>
#include <errno.h>
#include <unistd.h>
#include <netdb.h>
#include <net/if.h>
#include <arpa/inet.h>

// 可打印字符串转换为字节数据
// 如："C8329BFD0E01" --> {0xC8, 0x32, 0x9B, 0xFD, 0x0E, 0x01}
// pSrc: 源字符串指针
// pDst: 目标数据指针
// nSrcLength: 源字符串长度
// 返回: 目标数据长度
int string_to_bytes(const char* pSrc, u8* pDst, int nSrcLength)
{
    for(int i=0; i<nSrcLength; i+=2) {
        // 输出高4位
        if(*pSrc>='0' && *pSrc<='9') {
            *pDst = (*pSrc - '0') << 4;
        } else {
            *pDst = (*pSrc - 'a' + 10) << 4;
        }

        pSrc++;

        // 输出低4位
        if(*pSrc>='0' && *pSrc<='9') {
            *pDst |= *pSrc - '0';
        } else {
            *pDst |= *pSrc - 'a' + 10;
        }
        pSrc++;
        pDst++;
    }

    // 返回目标数据长度
    return nSrcLength / 2;
}

// 字节数据转换为可打印字符串
// 如：{0xC8, 0x32, 0x9B, 0xFD, 0x0E, 0x01} --> "C8329BFD0E01"
// pSrc: 源数据指针
// pDst: 目标字符串指针
// nSrcLength: 源数据长度
// 返回: 目标字符串长度
int bytes_to_string(const u8* pSrc, u8* pDst, int nSrcLength)
{
    const char tab[]="0123456789abcdef";    // 0x0-0xf的字符查找表

    for(int i=0; i<nSrcLength; i++) {
        // 输出低4位
        *pDst++ = tab[*pSrc >> 4];

        // 输出高4位
        *pDst++ = tab[*pSrc & 0x0f];

        pSrc++;
    }

    // 输出字符串加个结束符
    *pDst = '\0';

    // 返回目标字符串长度
    return nSrcLength * 2;
}

void my_strtok(char *tmp ,char flag, char **p1, char **p2, char **p3, char **p4)
{
    //三个二级指针必须要在调用函数中定义，这样在这个函数内的赋值才能传出去。

    int i = 0,j = 0;

    int a[3]; //这个数组的定义随着截取出串的多少你需要改动，最好是自己传入一个数组，这样就好了

    char *ptr=tmp;

    while(*tmp) {
        if(*tmp==flag) {
            a[j++]=i; //记录分割符出现的位置

            *tmp='\0';//把原来的分割符替换为’/0′
        }
        tmp++;
        i++;

    }

    *p1=ptr;

    *p2=ptr+a[0]+1;

    *p3=ptr+a[1]+1;

    *p4=ptr+a[2]+1;

}

int itoa_bin(u16 data, char *str)
{
    if(str == NULL)
        return -1;

    char *start = str;

    while(data) {
        if(data & 0x1)
            *str++ = 0x31;
        else
            *str++ = 0x30;

        data >>= 1;
    }

    *str = 0;

    //reverse the order
    char *low, *high, temp;
    low = start, high = str - 1;

    while(low < high) {
        temp = *low;
        *low = *high;
        *high = temp;

        ++low;
        --high;
    }

    return 0;
}

// 获取本机ip
int get_local_ip(const char *eth_inf, char *ip)
{
    int sd;
    struct sockaddr_in sin;
    struct ifreq ifr;

    sd = socket(AF_INET, SOCK_DGRAM, 0);
    if (-1 == sd)
    {
        printf("socket error: %s\n", strerror(errno));
        return -1;
    }

    strncpy(ifr.ifr_name, eth_inf, IFNAMSIZ);
    ifr.ifr_name[IFNAMSIZ - 1] = 0;

    // if error: No such device
    if (ioctl(sd, SIOCGIFADDR, &ifr) < 0)
    {
        printf("ioctl error: %s\n", strerror(errno));
        close(sd);
        return -1;
    }

    memcpy(&sin, &ifr.ifr_addr, sizeof(sin));
    snprintf(ip, 16, "%s", inet_ntoa(sin.sin_addr));

    close(sd);
    return 0;
}