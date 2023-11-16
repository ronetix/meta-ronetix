# Copyright (C) 2013-2016 Freescale Semiconductor
# Copyright 2017-2018 NXP
# Copyright 2020-2023 Ronetix GmbH

LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://Licenses/gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SUMMARY = "U-Boot for Ronetix's i.MX boards"

require recipes-bsp/u-boot/u-boot.inc
include u-boot-common.inc

LOCALVERSION = "-${SRCBRANCH}"

DEPENDS += "gnutls-native xxd-native"

PROVIDES += "u-boot"

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(mx6-generic-bsp|mx7-generic-bsp|mx8-generic-bsp|mx9-generic-bsp|rnx-imx93-osm)"
