# Common settings of the Ronetix i.MX9 SoMs
#
# The machine file sets before including this file:
#   MACHINEOVERRIDES            e.g. "mx93:"
#   KERNEL_DEVICETREE_BASENAME  device tree without extension
#   UBOOT_CONFIG_BASENAME       U-Boot defconfig without "_defconfig"
#   ATF_PLATFORM                imx93
#   RONETIX_BOARD_NAME          name shown in the extlinux boot menu

require conf/machine/include/imx-base.inc
require conf/machine/include/ronetix-common.inc
require conf/machine/include/arm/armv8-2a/tune-cortexa55.inc

# The i.MX93 SoMs need the NXP BSP: the mainline kernel has no device
# nodes for the i.MX93 display (LCDIF, DSI, LDB), camera (MIPI CSI, ISI),
# PXP, EdgeLock enclave or the Ethos-U NPU. The i.MX8M SoMs stay on the
# mainline BSP, see ronetix-imx8m.inc.
#
# The BSP is selected by the DISTRO, not by the machine: the fslc-*
# distros include conf/fragments/bsp/fslc.conf, which sets
# IMX_DEFAULT_BSP = "mainline" after the machine configuration and so
# cannot be overridden here. Build these machines with an NXP BSP
# distro, which includes conf/fragments/bsp/nxp.conf:
#
#   MACHINE=rnx-imx93-smarc DISTRO=fsl-wayland \
#       source ./setup-environment build-rnx-imx93-smarc
#
# fsl-xwayland and fsl-framebuffer work as well. With an fslc-* distro
# the machine builds linux-fslc, which has no device tree for the board.

# U-Boot and Linux kernel from github.com/ronetix. u-boot-ronetix is
# derived from u-boot-fslc (mainline U-Boot v2025.01) and is used with
# both BSPs, linux-ronetix from linux-fslc-imx (the NXP kernel maintained
# by the FSL community).
IMX_DEFAULT_BOOTLOADER:use-nxp-bsp = "u-boot-ronetix"
IMX_DEFAULT_KERNEL:imx-nxp-bsp = "linux-ronetix"
PREFERRED_VERSION_linux-ronetix ?= "6.18%"

MACHINE_FEATURES += "pci wifi bluetooth"

# AP6275S (BCM43752A2) WiFi/BT firmware. The package comes from the bbappend
# in recipes-kernel/linux-firmware, which pulls the vendor files from
# github.com/ronetix/linux-firmware and installs them under both the bcmdhd
# and the brcmfmac names.
#
# Deliberately NOT wireless-regdb: the image already gets
# wireless-regdb-static, which is the package that carries
# /lib/firmware/regulatory.db for the kernel to load (plain wireless-regdb is
# the crda variant and RCONFLICTS with it). The boot-time
#   "Direct firmware load for regulatory.db failed with error -2"
# is only because cfg80211 asks ~80 ms before the rootfs is mounted. It is
# harmless: reg.c calls reg_query_database() again for every later regulatory
# request, in particular the one the wireless driver makes when it registers
# its wiphy, and by then the file is readable. See also the
# CONFIG_FW_LOADER_USER_HELPER_FALLBACK note in linux-ronetix/imx93-rnx.cfg -
# with the forced helper fallback this miss used to stall the boot for 60 s.
MACHINE_EXTRA_RDEPENDS += "linux-firmware-bcm43752"

# One image for every eMMC variant. The .wks sizes the root partition to
# the root filesystem (no --fixed-size), so the .wic fits the smallest
# module, and ronetix-growfs extends the partition and the ext4 to the end
# of the device on the first boot after flashing. That also moves the GPT
# backup header to the real end of the eMMC, which is what the boot-time
#   GPT:Primary header thinks Alt. header is not at the end of the disk.
#   GPT:5648511 != 7634943
# is complaining about.
MACHINE_EXTRA_RDEPENDS += "ronetix-growfs"

KERNEL_DEVICETREE ?= "freescale/${KERNEL_DEVICETREE_BASENAME}.dtb"

# U-Boot builds the boot container flash.bin (ELE container, SPL, DDR
# firmware, BL31 and U-Boot) with binman, see imx-boot-container.bbclass.
# meta-freescale enables this for i.MX8M and i.MX95 only, on i.MX93 it
# would otherwise build the container with imx-mkimage (imx-boot recipe).
UBOOT_PROVIDES_BOOT_CONTAINER = "1"

UBOOT_CONFIG ??= "sd"
UBOOT_CONFIG[sd] = "${UBOOT_CONFIG_BASENAME}_defconfig"
UBOOT_DTB_NAME = "${KERNEL_DEVICETREE_BASENAME}.dtb"
UBOOT_SUFFIX = "bin"
# "make flash.bin" fails on i.MX93 with "No rule to make target
# 'u-boot.cnt'", the binman images are built by the "all" target
UBOOT_MAKE_TARGET ?= "all"
SPL_BINARY = "spl/u-boot-spl.bin"
LOADADDR = ""

# ELE firmware version follows the CPU revision.
#
# meta-freescale's firmware-ele-imx 2.0.6 archive only contains
# mx93a1-ahab-container.img, so an A0 die (RNX-iMX93-OSM) has to come from
# the older 0.1.0 release, which still carries mx93a0. See
# recipes-bsp/firmware-imx/firmware-ele-imx_0.1.0.bb.
#
# A1 and A2 both run the A1 container, so they need no preference and get
# meta-freescale's newest version. "%" means exactly that: no constraint.
PREFERRED_VERSION_firmware-ele-imx = \
    "${@'0.1.0' if d.getVar('IMX_SOC_REV') == 'A0' else ''}"

# The ELE container in flash.bin is CPU revision specific, and a flash.bin
# built with the container of the wrong revision does not boot at all.
# The RNX-iMX93-SMARC keeps the A1 default of imx-base.inc, the
# RNX-iMX93-OSM carries an A0 die and sets A0 itself, see
# RONETIX_IMX93_SOC_REV in rnx-imx93-osm.conf.
#
# To pick a different revision from local.conf, use the same override
# imx-base.inc assigns it with ("IMX_SOC_REV:mx93-generic-bsp ??= \"A1\""),
# a plain IMX_SOC_REV assignment loses against it:
#
#   IMX_SOC_REV:mx93-generic-bsp = "A2"

# NXP ships i.MX93 ELE containers for A0 and A1 only - an A2 die runs the
# A1 container, which is also the mapping U-Boot's
# tools/imx_build_firmware.sh uses. The container name otherwise follows
# the revision verbatim (SECO_FIRMWARE_NAME:mx93-generic-bsp in
# use-imx-security-controller-firmware.bbclass), so A2 would ask for
# mx93a2-ahab-container.img, which is in no firmware-ele-imx archive, and
# the build would stop in firmware-ele-imx:do_install.
SECO_FIRMWARE_NAME:mx93-generic-bsp = \
    "mx93${@'a1' if d.getVar('IMX_SOC_REV') == 'A2' else d.getVar('IMX_SOC_REV_LOWER')}-ahab-container.img"

DDR_FIRMWARE_NAME = " \
    lpddr4_imem_1d_v202201.bin \
    lpddr4_dmem_1d_v202201.bin \
    lpddr4_imem_2d_v202201.bin \
    lpddr4_dmem_2d_v202201.bin \
"

# Boot container at offset 32kiB, 32 and not 33 as on the i.MX8MM
IMX_BOOT_SOC_TARGET = "iMX9"
IMX_BOOT_SEEK = "32"

# Console on LPUART1
SERIAL_CONSOLES = "115200;ttyLP0"

# Boot with extlinux.conf (U-Boot distro boot). The eMMC is USDHC1
# (U-Boot and Linux mmc 0), the SD card USDHC2 (mmc 1).
UBOOT_EXTLINUX = "1"
UBOOT_EXTLINUX_LABELS = "default"
UBOOT_EXTLINUX_DEFAULT_LABEL = "${RONETIX_BOARD_NAME}"
UBOOT_EXTLINUX_MENU_DESCRIPTION:default = "${RONETIX_BOARD_NAME}"
UBOOT_EXTLINUX_FDT:default = "../${KERNEL_DEVICETREE_BASENAME}.dtb"
UBOOT_EXTLINUX_CONSOLE:default = "console=${console}"
# Root by GPT partition label, not by device node: the same image then
# boots from either the eMMC (mmcblk0) or the SD card (mmcblk1). The
# .wks labels the rootfs partition "root", and the kernel resolves
# PARTLABEL= in block/early-lookup.c. A hardcoded root=/dev/mmcblk1p2
# points at the SD slot and makes an eMMC-flashed image hang on
# rootwait - or, worse, silently boot a card that happens to be in.
UBOOT_EXTLINUX_ROOT:default ?= "root=PARTLABEL=root"

IMAGE_BOOT_FILES:append = " extlinux.conf;extlinux/extlinux.conf"

# Also build wic.zst, for "uuu -b emmc_all <flash.bin> <image.wic.zst>".
#
# meta-freescale's SOC_DEFAULT_IMAGE_FSTYPES is "wic.bmap wic.gz", but uuu
# cannot read a plain .gz: uuu/buildincmd.cpp only appends the "/*" that
# selects a decompressor for the extensions in its match[] array,
#
#     std::string match[] = { ".BZ2", ".ZST" };
#
# and libuuu/buffer.cpp only decompresses when the suffix is followed by "/".
# A bare .wic.gz therefore reaches the eMMC still gzipped - no partition
# table, and fastboot reports success. .wic.zst gets the "/*" and works.
#
# ":append", not "+=", so this survives the "IMAGE_FSTYPES ?=" in imx-base.inc.
IMAGE_FSTYPES:append = " wic.zst"
