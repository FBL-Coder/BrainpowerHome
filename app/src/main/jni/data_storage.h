#ifndef DATA_STORAGE_H
#define DATA_STORAGE_H

#include "udp.h"
#include "udppropkt.h"

void set_rcuinfo(UDPPROPKT * pkt , SOCKADDR_IN sender);
void set_dev_info(UDPPROPKT *pkt);
void ctrl_dev_reply(UDPPROPKT *pkt);
void fresh_dev_info(UDPPROPKT *pkt);
void del_dev_info(UDPPROPKT *pkt);
void set_events_info(UDPPROPKT *pkt);
void del_scene_info(UDPPROPKT *pkt);
void set_board_info(UDPPROPKT *pkt);
void del_board_info(UDPPROPKT *pkt);
void set_key_opitem(UDPPROPKT *pkt);
void del_key_opitem(UDPPROPKT *pkt);
void set_chn_opitem(UDPPROPKT *pkt);
void del_chn_opitem(UDPPROPKT *pkt);

#endif // DATA_STORAGE_H
