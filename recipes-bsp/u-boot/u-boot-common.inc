DEPENDS += " \
    bison-native \	
    bc-native \
    bison-native \
    dtc-native \
    flex-native \
    gnutls-native \
"

UBOOT_SRC ?= "git://github.com/ronetix/u-boot.git;protocol=https"
SRCBRANCH = "lf_v2023.04"
SRCREV = "a96bfb94eaf6dbf58068232bb33472375046a57b"

SRC_URI = "${UBOOT_SRC};branch=${SRCBRANCH}"

S = "${WORKDIR}/git"

inherit fsl-u-boot-localversion

BOOT_TOOLS = "imx-boot-tools"
