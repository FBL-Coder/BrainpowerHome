#ifndef JSON_H
#define JSON_H

#include "udppropkt.h"
#include <stdio.h>
#include <stdlib.h>
#include "cJSON.h"
#include "debug.h"
#include "link_list.h"
#include "udp.h"

char *create_dev_json(u8 *devUnitID, int dataType, int subType1, int subType2);

char* create_events_json(u8 *devUnitID, int datType, int subType1, int subType2);

char* create_board_chnout_json(u8 *devUnitID, int datType, int subType1, int subType2);

char* create_board_keyinput_json(u8 *devUnitID, int datType, int subType1, int subType2);

char* create_rcu_json(u8 *devUnitID, int datType, int subType1, int subType2);

void ctrl_devs_json(u8 *devUnitID, u8 *canCpuID, int datType, int devType, int devID, int cmd);

char* create_ctl_reply_info_json(UDPPROPKT *pkt);

char *create_chn_status_json(u8 *devUnitID, int datType, int subType1, int subType2);

void ctrl_scene_json(u8 *devUnitID, int eventId, int cmd);

void add_scene_json(u8 *devUnitID, u8 *sceneName, int devCnt, int eventId, cJSON *item);

char *create_ctl_scene_reply_json(UDPPROPKT *pkt);

char *create_get_chn_opitem_reply_json(u8 *devUnitID, u8 *cpuCanID, int devType, int devID);

char *create_set_chn_opitem_reply_json(UDPPROPKT *pkt);

void set_chn_opitem_json(u8 *devUnitID, u8 *canCpuID, int devType, int devID, int cnt, cJSON *chnop_item, int cmd);

char *create_get_key_opitem_reply_json(u8 *devUnitID, u8 *cpuCanID, int key_index);

void set_key_opitem_json(u8 *devUnitID, u8 *canCpuID, int key_index, int cnt, cJSON *keyop_item, int cmd);

char *create_set_key_opitem_reply_json(UDPPROPKT *pkt);

#endif // JSON_H
