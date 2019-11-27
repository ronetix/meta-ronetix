SUMMARY = "U-Boot for Ronetix's i.MX boards"
require u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc

inherit fsl-u-boot-localversion

LOCALVERSION_imx7-cm = "-mx7"

DEPENDS_append = " dtc-native bison-native"

PROVIDES += "u-boot"

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(imx7-cm)"
