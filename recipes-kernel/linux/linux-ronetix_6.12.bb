# Copyright 2019-2026 Ronetix GmbH
# Released under the MIT license (see COPYING.MIT for the terms)

# Linux kernel for the Ronetix i.MX SoMs: the linux-fslc recipe with the
# sources from github.com/ronetix/linux, branch ronetix/6.12.x+fslc
# (linux-fslc 6.12.x+fslc with the Ronetix device trees).
require recipes-kernel/linux/linux-fslc_6.12.bb

SUMMARY = "Linux kernel for the Ronetix i.MX SoMs"
HOMEPAGE = "https://github.com/ronetix/linux"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI = "git://github.com/ronetix/linux.git;branch=${KBRANCH};protocol=https"
KBRANCH = "ronetix/6.12.x+fslc"
SRCREV = "d87fa91a9aa498d297da2a0f68019d577a32e135"

# Drivers used by the Ronetix device trees, which are not enabled in
# defconfig (i.MX8M) or imx_v6_v7_defconfig (i.MX7)
SRC_URI:append:mx8mq-generic-bsp = " file://imx8mq-cm.cfg"
SRC_URI:append:mx7-generic-bsp = " file://imx7-cm.cfg"

LOCALVERSION = "-rnx"
