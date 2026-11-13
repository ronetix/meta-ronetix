# Copyright 2019-2026 Ronetix GmbH
# Released under the MIT license (see COPYING.MIT for the terms)

# Linux kernel for the Ronetix i.MX9 SoMs: the linux-fslc-imx recipe with
# the sources from github.com/ronetix/linux, branch
# ronetix/6.18-1.0.x-imx (the NXP kernel maintained by the FSL community,
# with the Ronetix device trees).
#
# The i.MX9 SoMs use the NXP BSP, the mainline kernel has no device nodes
# for the i.MX93 display, camera, PXP, EdgeLock enclave and NPU. The
# i.MX8M SoMs use linux-ronetix_6.12.bb (mainline, linux-fslc).
require recipes-kernel/linux/linux-fslc-imx_6.18.bb

SUMMARY = "Linux kernel for the Ronetix i.MX9 SoMs"
HOMEPAGE = "https://github.com/ronetix/linux"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI = "git://github.com/ronetix/linux.git;branch=${KBRANCH};protocol=https"
KBRANCH = "ronetix/6.18-1.0.x-imx"

SRCREV = "44793c90949abdecf42690aa84f26b21326f940d"

# Must match the kernel version of SRCREV, linux-fslc-imx_6.18.bb pins
# an older commit of the same branch (6.18.24)
LINUX_VERSION = "6.18.32"

# Drivers used by the Ronetix i.MX93 device trees which imx_v8_defconfig
# does not enable
SRC_URI:append:mx93-generic-bsp = " file://imx93-rnx.cfg"

LOCALVERSION = "-rnx"

COMPATIBLE_MACHINE = "(mx9-nxp-bsp)"
