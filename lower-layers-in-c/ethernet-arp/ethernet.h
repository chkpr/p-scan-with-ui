#include <linux/if_ether.h>
#ifndef ETHERNET_H
#define ETHERNET_H

#include <stdint.h>

struct eth_hdr
{
    unsigned char dmac[6];
    unsigned char smac[6];
    uint16_t ethertype;
    unsigned char payload[];
} __attribute__((packed));



struct eth_hdr *hdr = init_eth_hdr(buf);
vopid handle_frame(/* ...*/)

#endif