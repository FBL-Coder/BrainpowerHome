
#include "data_get.h"

static int sn = 0;


SOCKET init_socket(int type)
{
    SOCKET sock = socket(AF_INET, type, 0);

    if (sock < 0) {
        PR_ERR("create socket error");
    }

    return sock;
}

UDPPROPKT *pre_send_udp_pkt(unsigned long sender_ip, u8 *dat, int dat_len, u8 cmd, u8* rcu_id, u8 *rcu_pwd, int ack, int sub_type1, int sub_type2)
{
    UDPPROPKT *pkt = (UDPPROPKT*)malloc(sizeof(UDPPROPKT) + dat_len);

    memcpy(pkt->head, HEAD_STRING, 4);

    //unsigned long sender_ip = inet_addr(inet_ntoa(sender_client.sin_addr));
    //unsigned long sender_ip = inet_addr("192.168.0.102");
    unsigned char * dstip   = (unsigned char*)&sender_ip;

    for (int i = 0; i < 4; i++) {
        pkt->dstIp[i] = dstip[i] & 0xff;
    }
    unsigned long server_ip;
#if VER_SWITCH
    server_ip = inet_addr(CLOUD_SERVER_IP);
#else
    server_ip = inet_addr(local_ip);
#endif

    unsigned char * srcip   = (unsigned char*)&server_ip;

    for (int i = 0; i < 4; i++) {
        pkt->srcIp[i] = srcip[i] & 0xff;
    }

    //LOGI("%d.%d.%d.%d\n", pkt->dstIp[0], pkt->dstIp[1], pkt->dstIp[2], pkt->dstIp[3]);
    memset(pkt->uidSrc, 0xff, 12);
    memset(pkt->pwdDst, 0xff, 8);
//    if (rcu_pwd != NULL)
//       memcpy(pkt->pwdDst, rcu_pwd, 8);
//   else
//    memset(pkt->pwdDst, 0xff, 8);

    //测试用
    //memcpy(pkt->pwdDst, "09702443", 8);
    memcpy(pkt->pwdDst, "16702443", 8);

    if (rcu_id != NULL)
        memcpy(pkt->uidDst, rcu_id, 12);
    else
        memset(pkt->uidDst, 0xff, 12);

    pkt->bAck     = ack;
    pkt->datType  = cmd;
    pkt->subType1 = sub_type1;
    pkt->subType2 = sub_type2;
    pkt->currPkt  = 0;
    pkt->snPkt    = sn + 1;
    pkt->sumPkt   = 1;
    pkt->datLen   = dat_len;

    sn = pkt->snPkt;
    memcpy(pkt->dat, dat, dat_len);

    return pkt;
}


UDPPROPKT *udp_pkt_bradcast(u8 *devUnitID)
{
    UDPPROPKT *pkt = (UDPPROPKT*)malloc(sizeof(UDPPROPKT));

    memcpy(pkt->head, HEAD_STRING, 4);

    unsigned long sender_ip = inet_addr("255.255.255.255");
    unsigned char * dstip   = (unsigned char*)&sender_ip;

    for (int i = 0; i < 4; i++) {
        pkt->dstIp[i] = dstip[i] & 0xff;
    }

    unsigned long server_ip = inet_addr(local_ip);
    unsigned char * srcip   = (unsigned char*)&server_ip;

    for (int i = 0; i < 4; i++) {
        pkt->srcIp[i] = srcip[i] & 0xff;
    }

    memset(pkt->uidSrc, 0xff, 12);
    memset(pkt->pwdDst, 0xff, 8);

    if (devUnitID != NULL)
        memcpy(pkt->uidDst, devUnitID, 12);
    else
        memset(pkt->uidDst, 0xff, 12);

    pkt->bAck     = IS_ACK;
    pkt->datType  = e_udpPro_getRcuInfoNoPwd;
    pkt->subType1 = 0;
    pkt->subType2 = 0;
    pkt->currPkt  = 0;
    pkt->snPkt    = sn + 1;
    pkt->sumPkt   = 1;
    pkt->datLen   = 0;

    sn = pkt->snPkt;

    return pkt;
}

void handShake(UDPPROPKT *pkt)
{
    UDPPROPKT *send_pkt = pre_send_udp_pkt(inet_addr(inet_ntoa(sender.sin_addr)), 0, 0, e_udpPro_handShake, pkt->uidSrc, pkt->pwdDst, NOW_ACK, 0, 0);

    sendto(primary_udp, send_pkt, sizeof(UDPPROPKT), 0, (struct sockaddr *)&sender, sender_len);
}
