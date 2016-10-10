#ifndef DATA_GET_H
#define DATA_GET_H

#include "udp.h"
#include "udppropkt.h"

SOCKET init_socket(int type);
UDPPROPKT *pre_send_udp_pkt(unsigned long sender_ip, u8 *dat, int dat_len, u8 cmd, u8* rcu_id, \
                            u8 *rcu_pwd, int ack, int sub_type1, int sub_type2);
void handShake(UDPPROPKT *pkt);
UDPPROPKT *udp_pkt_bradcast(u8 *devUnitID);

#endif // DATA_GET_H
