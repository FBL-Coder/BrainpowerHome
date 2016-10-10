#ifndef STR_COMM_H
#define STR_COMM_H

#include <stdio.h>

typedef unsigned char    u8;
typedef unsigned short   u16;

typedef int              BOOL;

#define TRUE     1
#define FALSE    0
#define ERROR   -1

int string_to_bytes(const char* pSrc, u8* pDst, int nSrcLength);
int bytes_to_string(const u8* pSrc, u8* pDst, int nSrcLength);
int itoa_bin(u16 data, char *str);
int get_local_ip(const char *eth_inf, char *ip);
#endif // STR_COMM_H
