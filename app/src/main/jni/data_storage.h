#ifndef DATA_STORAGE_H
#define DATA_STORAGE_H

#include "udp.h"
#include "udppropkt.h"

extern void set_rcuinfo(UDPPROPKT * pkt , SOCKADDR_IN sender);
extern void set_dev_info(UDPPROPKT *pkt);
extern void ctrl_dev_reply(UDPPROPKT *pkt);
extern void fresh_dev_info(UDPPROPKT *pkt);
extern void del_dev_info(UDPPROPKT *pkt);
extern void set_events_info(UDPPROPKT *pkt);
extern void del_scene_info(UDPPROPKT *pkt);
extern void set_board_info(UDPPROPKT *pkt);
extern void del_board_info(UDPPROPKT *pkt);
extern void set_key_opitem(UDPPROPKT *pkt);
extern void del_key_opitem(UDPPROPKT *pkt);
extern void set_chn_opitem(UDPPROPKT *pkt);
extern void del_chn_opitem(UDPPROPKT *pkt);

#endif // DATA_STORAGE_H
