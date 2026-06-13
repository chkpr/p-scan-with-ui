#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/ioctl.h>
#include <net/if.h>
#include <linux/if_tun.h>

#define CLEAR(x) memset(&(x), 0, sizeof(x))
#define print_error(...) fprintf(stderr, __VA_ARGS__)

/*
 * Taken from Kernel Documentation/networking/tuntap.txt
 */
int tun_alloc(char *dev)
{
    struct ifreq ifr;
    int fd, err;

    if( (fd = open("/dev/net/tun", O_RDWR)) < 0 ) {
        print_error("Cannot open TUN/TAP dev");
        exit(1);
    }

    CLEAR(ifr);

    /* Flags: IFF_TUN   - TUN device (no Ethernet headers)
     *        IFF_TAP   - TAP device
     *
     *        IFF_NO_PI - Do not provide packet information
     */
    ifr.ifr_flags = IFF_TAP | IFF_NO_PI;
    if( *dev ) {
        strncpy(ifr.ifr_name, dev, IFNAMSIZ);
    }

    if( (err = ioctl(fd, TUNSETIFF, (void *) &ifr)) < 0 ){
        print_error("ERR: Could not ioctl tun: %s\n", strerror(errno));
        close(fd);
        return err;
    }

    strcpy(dev, ifr.ifr_name);
    return fd;
}

int main(int argc, char **argv) {
    char dev[IFNAMSIZ] = "";
    int tap_fd = tun_alloc(dev);

    if (tap_fd < 0) {
        print_error("Erreur ouverture device TAP\n");
        return 1;
    }

    printf("Device %s ouvert, fd=%d\n", dev, tap_fd);

// Boucle temporaire pour garder le device actif
printf("Appuie sur Ctrl+C pour terminer\n");
while(1) {
    sleep(1);
}

    return 0;
}