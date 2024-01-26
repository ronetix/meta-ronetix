# Copyright (C) 2013-2016 Freescale Semiconductor
# Copyright 2017 NXP
# Copyright 2020-2023 Ronetix GmbH
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Linux kernel provided and supported by Ronetix"
DESCRIPTION = "Linux kernel provided and supported by Ronetix (based on the kernel provided by NXP)"

require recipes-kernel/linux/linux-imx.inc
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

FILES:${KERNEL_PACKAGE_NAME}-base += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/modules.builtin.modinfo "

DEPENDS += "lzop-native bc-native"

DEFAULT_PREFERENCE = "1"

KERNEL_SRC ?= "git://github.com/ronetix/linux.git;protocol=https"
SRCBRANCH = "lf-6.1.y"
SRCREV = "a1f94c0e402596d9862590eff689926cc80f022f"

# PV is defined in the base in linux-imx.inc file and uses the LINUX_VERSION definition
# required by kernel-yocto.bbclass.
#
# LINUX_VERSION define should match to the kernel version referenced by SRC_URI and
# should be updated once patchlevel is merged.
LINUX_VERSION = "-lts-6.1.36"

#KERNEL_CONFIG_COMMAND = "oe_runmake_call -C ${S} CC="${KERNEL_CC}" O=${B} olddefconfig"

SRC_URI = "${KERNEL_SRC};branch=${SRCBRANCH}"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

LOCALVERSION = "-imx93"

KBUILD_DEFCONFIG:mx9-nxp-bsp = "rnx_imx93_osm_defconfig"

pkg_postinst:kernel-devicetree:append () {
   rm -f $D/boot/devicetree-*
}

KERNEL_VERSION_SANITY_SKIP="1"
COMPATIBLE_MACHINE = "(mx8-nxp-bsp|mx9-nxp-bsp)"
