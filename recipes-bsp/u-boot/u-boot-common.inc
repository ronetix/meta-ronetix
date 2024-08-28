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
SRCREV = "d015fff1a3dafcf1d288fe39d5bf0d1f0edc4feb"

SRC_URI = "${UBOOT_SRC};branch=${SRCBRANCH}"

S = "${WORKDIR}/git"

inherit fsl-u-boot-localversion

BOOT_TOOLS = "imx-boot-tools"
