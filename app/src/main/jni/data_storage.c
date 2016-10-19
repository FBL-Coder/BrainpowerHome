#include "data_storage.h"

void set_rcuinfo(UDPPROPKT * pkt , SOCKADDR_IN sender)
{
    RCU_INFO *rcuinfo = (RCU_INFO*)pkt->dat;

    int old_size = rcu_list.size;
    rcu_add(&rcu_list, *rcuinfo, rcu_list.size);
    if(old_size == rcu_list.size)
        return;

    gw_client_add (&gw_client_list, sender, pkt->uidSrc, (u8 *)"", rcuinfo->IpAddr, gw_client_list.size);
    gw_client_display(&gw_client_list);

    udp_msg_queue_add (&msg_queue_list,rcuinfo->devUnitID, e_udpPro_getDevsInfo, 0, 0, msg_queue_list.size);
    udp_msg_queue_add (&msg_queue_list,rcuinfo->devUnitID, e_udpPro_getBoards, e_board_chnOut, 0, msg_queue_list.size);
    udp_msg_queue_add (&msg_queue_list,rcuinfo->devUnitID, e_udpPro_getBoards, e_board_keyInput, 0, msg_queue_list.size);
    udp_msg_queue_add (&msg_queue_list,rcuinfo->devUnitID, e_udpPro_getKeyOpItems, 0, 0, msg_queue_list.size);
    udp_msg_queue_add (&msg_queue_list,rcuinfo->devUnitID, e_udpPro_getChnOpItems, 0, 0, msg_queue_list.size);
    udp_msg_queue_add (&msg_queue_list,rcuinfo->devUnitID, e_udpPro_getSceneEvents, 0, 0, msg_queue_list.size);

}


void set_dev_info(UDPPROPKT *pkt)
{
    int dev_cnt = 0;
    switch (pkt->subType2) {
    case e_ware_airCond:
        dev_cnt = pkt->datLen / WARE_AIR_SIZE;

        for (int i = 0; i < dev_cnt; i++) {
            WARE_DEV *ware_dev = (WARE_DEV*)(pkt->dat + i * WARE_AIR_SIZE);

            ware_add(&ware_list, *ware_dev, pkt->uidSrc, ware_list.size);

            DEV_PRO_AIRCOND *aircond = (DEV_PRO_AIRCOND *)ware_dev->dat;
            ware_aircond_add (&aircond_list, *ware_dev, *aircond, pkt->uidSrc, aircond_list.size);
            ware_aircond_display(&aircond_list);
        }
        break;

    case e_ware_tv:
        dev_cnt = pkt->datLen / WARE_TV_SIZE;

        for (int i = 0; i < dev_cnt; i++) {
            WARE_DEV *ware_dev = (WARE_DEV*)(pkt->dat + i * WARE_TV_SIZE);

            ware_add(&ware_list, *ware_dev, pkt->uidSrc, ware_list.size);
        }
        break;

    case e_ware_tvUP:
        dev_cnt = pkt->datLen / WARE_TVUP_SIZE;

        for (int i = 0; i < dev_cnt; i++) {
            WARE_DEV *ware_dev = (WARE_DEV*)(pkt->dat + i * WARE_TVUP_SIZE);

            ware_add(&ware_list, *ware_dev, pkt->uidSrc, ware_list.size);

        }
        break;

    case e_ware_light:
        dev_cnt = pkt->datLen / WARE_LGT_SIZE;

        for (int i = 0; i < dev_cnt; i++) {
            WARE_DEV *ware_dev = (WARE_DEV*)(pkt->dat + i * WARE_LGT_SIZE);
            ware_add(&ware_list, *ware_dev, pkt->uidSrc, ware_list.size);

            DEV_PRO_LIGHT *light = (DEV_PRO_LIGHT *)ware_dev->dat;
            ware_light_add (&light_list, *ware_dev, *light, pkt->uidSrc, light_list.size);
        }

        break;

    case e_ware_curtain:
        dev_cnt = pkt->datLen / WARE_CUR_SIZE;

        for (int i = 0; i < dev_cnt; i++) {
            WARE_DEV *ware_dev = (WARE_DEV*)(pkt->dat + i * WARE_CUR_SIZE);

            ware_add(&ware_list, *ware_dev, pkt->uidSrc, ware_list.size);

            DEV_PRO_CURTAIN *curtain = (DEV_PRO_CURTAIN *)ware_dev->dat;
            ware_curtain_add(&curtain_list, *ware_dev, *curtain, pkt->uidSrc, curtain_list.size);
        }
        break;

    default:
        break;
    }
}


int get(int num, int index)
{
    return (num & (0x1 << index)) >> index;
}

void ctrl_dev_reply(UDPPROPKT *pkt)
{
    CHNS_STATUS *status = (CHNS_STATUS *)pkt->dat;
    char bin_string[13];
    itoa_bin(status->state, bin_string);
    LOGI("二进制字符串:%s\n", bin_string);

    for(int i = 0; i < 12; i++) {
        Node_light *light_head   = light_list.head;
        if (get(status->state, i) == 1) {

            for(int j =0; j < light_list.size; j++,light_head = light_head->next) {
                if(memcmp(light_head->ware_dev.canCpuId, status->devUnitID, 12) == 0 && light_head->light.powChn == i) {
                    light_head->light.bOnOff = 1;
                }
            }
        } else {
            for(int j =0; j < light_list.size; j++,light_head = light_head->next) {
                if(memcmp(light_head->ware_dev.canCpuId, status->devUnitID, 12) == 0 && light_head->light.powChn == i) {
                    light_head->light.bOnOff = 0;
                }
            }
        }
    }
}

void fresh_dev_info(UDPPROPKT *pkt)
{
    WARE_DEV *ware_dev = (WARE_DEV *)pkt->dat;
    switch (ware_dev->devType) {
    case 0: {
        DEV_PRO_AIRCOND *aircond = (DEV_PRO_AIRCOND *)ware_dev->dat;
        ware_aircond_add (&aircond_list, *ware_dev, *aircond, pkt->uidSrc, aircond_list.size);
    }
    break;
    default:
        break;
    }
}

void del_dev_info(UDPPROPKT *pkt)
{
    if (pkt->datLen > 255)
        return;

    WARE_DEV *ware_dev = (WARE_DEV*)pkt->dat;

    ware_remove (&ware_list, *ware_dev, pkt->uidSrc);
    switch (ware_dev->devType) {
    case e_ware_airCond:
        ware_aircond_remove (&aircond_list, *ware_dev, pkt->uidSrc);
        break;
    case e_ware_light:
        ware_light_remove (&light_list, *ware_dev, pkt->uidSrc);
        break;
    default:
        break;
    }
}

void set_events_info(UDPPROPKT *pkt)
{
    int event_cnt = pkt->datLen / SCENE_SIZE;

    for (int i = 0; i < event_cnt; i++) {
        SCENE_EVENT *event = (SCENE_EVENT*)(pkt->dat + i * SCENE_SIZE);

        ware_scene_add (&scene_list, *event, pkt->uidSrc, scene_list.size);

        ware_scene_display(&scene_list);
    }
}

void del_scene_info(UDPPROPKT *pkt)
{
    SCENE_EVENT *event = (SCENE_EVENT*)pkt->dat;

    ware_scene_remove (&scene_list, *event, pkt->uidSrc);

    ware_scene_display(&scene_list);
}

void set_board_info(UDPPROPKT *pkt)
{
    BOARD_CHNOUT *board = (BOARD_CHNOUT*)pkt->dat; //四种类型结构一样

    switch (board->boardType) {
    case e_board_chnOut: {
        BOARD_CHNOUT *board = (BOARD_CHNOUT*)pkt->dat;
        board_add(&board_list, *board, pkt->uidSrc, board_list.size);
        board_display(&board_list);
    }
    break;

    case e_board_keyInput: {
        BOARD_KEYINPUT *keyinput = (BOARD_KEYINPUT*)pkt->dat;
        keyinput_add(&keyinput_list, *keyinput, pkt->uidSrc, keyinput_list.size);
        keyinput_display(&keyinput_list);
    }
    break;

    case e_board_wlessIR: {
        BOARD_WLESSIR *wlessir = (BOARD_WLESSIR*)pkt->dat;
    }
    break;

    case e_board_envDetect: {
        BOARD_ENVDETECT *envdetect = (BOARD_ENVDETECT*)pkt->dat;
    }
    break;
    default:
        break;
    }
}

void del_board_info(UDPPROPKT *pkt)
{
    BOARD_CHNOUT *temp = (BOARD_CHNOUT*)pkt;

    switch (temp->boardType) {
    case e_board_chnOut: {
        BOARD_CHNOUT *board = (BOARD_CHNOUT*)pkt->dat;
        board_remove (&board_list, *board, pkt->uidDst);
    }
    break;

    case e_board_keyInput: {
        BOARD_KEYINPUT *keyinput = (BOARD_KEYINPUT*)pkt->dat;
        keyinput_remove (&keyinput_list, *keyinput, pkt->uidDst);
    }
    break;

    case e_board_wlessIR: {
        BOARD_WLESSIR *wlessir = (BOARD_WLESSIR*)pkt->dat;
    }
    break;

    case e_board_envDetect: {
        BOARD_ENVDETECT *envdetect = (BOARD_ENVDETECT*)pkt->dat;
    }

    default:
        break;
    }
}

void set_key_opitem(UDPPROPKT *pkt)
{
    int cnt = (pkt->datLen - 12) / sizeof(KEYOP_ITEM);

    u8  keyinput_board_id[12];
    memcpy(keyinput_board_id, pkt->dat, 12);

    for (int i = 0; i < cnt; i++) {
        KEYOP_ITEM *keyop_item = (KEYOP_ITEM*)(pkt->dat + 12 + sizeof(KEYOP_ITEM) * i);
        keyop_item_add (&keyop_item_list, *keyop_item, pkt->uidSrc, keyinput_board_id, pkt->subType2, keyop_item_list.size);
    }
}

void del_key_opitem(UDPPROPKT *pkt)
{
    u8  keyinput_board_id[12];
    memcpy(keyinput_board_id, pkt->dat, 12);

    for (int i = 0; i < pkt->subType2; i++) {
        KEYOP_ITEM *item = (KEYOP_ITEM*)(pkt->dat + 12 + sizeof(KEYOP_ITEM) * i);
        keyop_item_remove (&keyop_item_list, pkt->uidDst, keyinput_board_id, pkt->subType2);

    }
}

void set_chn_opitem(UDPPROPKT *pkt)
{
    int cnt = pkt->subType2;

    u8  chn_board_id[12];
    u8  devType, devID;

    memcpy(chn_board_id, pkt->dat, 12);
    devType = pkt->dat[12];
    devID   = pkt->dat[13];

    for (int i = 0; i < cnt; i++) {
        CHNOP_ITEM *chnop_item = (CHNOP_ITEM*)(pkt->dat + 14 + sizeof(CHNOP_ITEM) * i);

        chnop_item_add(&chnop_item_list, *chnop_item, pkt->uidSrc, chn_board_id, devType, devID, cnt, chnop_item_list.size);
    }
}

void del_chn_opitem(UDPPROPKT *pkt)
{
    for (int i = 0; i < pkt->subType2; i++) {
        CHNOP_ITEM *item = (CHNOP_ITEM*)(pkt->dat + 14 + sizeof(CHNOP_ITEM) * i);

        chnop_item_remove (&chnop_item_list, *item, pkt->uidDst);
    }
}

