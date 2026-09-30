# Common settings of the Ronetix i.MX8M SoMs
#
# The machine file sets before including this file:
#   MACHINEOVERRIDES            e.g. "mx8mm:"
#   KERNEL_DEVICETREE_BASENAME  device tree without extension
#   UBOOT_CONFIG_BASENAME       U-Boot defconfig without "_defconfig"
#   ATF_PLATFORM                imx8mm, imx8mn or imx8mq
#   RONETIX_BOARD_NAME          name shown in the extlinux boot menu

require conf/machine/include/imx-base.inc
require conf/machine/include/ronetix-common.inc
require conf/machine/include/arm/armv8a/tune-cortexa53.inc

# The mainline BSP defaults to a generic cortexa53 tune, include the
# crypto extension for inline NEON and FPU code generation
DEFAULTTUNE:use-mainline-bsp = "cortexa53-crypto"

# U-Boot and Linux kernel from github.com/ronetix
IMX_DEFAULT_BOOTLOADER:use-mainline-bsp = "u-boot-ronetix"
IMX_DEFAULT_KERNEL:imx-mainline-bsp = "linux-ronetix"

MACHINE_FEATURES += "pci wifi bluetooth"

KERNEL_DEVICETREE ?= "freescale/${KERNEL_DEVICETREE_BASENAME}.dtb"

# U-Boot builds the boot container flash.bin (SPL, DDR firmware, BL31
# and U-Boot), see imx-boot-container.bbclass
UBOOT_CONFIG ??= "sd"
UBOOT_CONFIG[sd] = "${UBOOT_CONFIG_BASENAME}_defconfig"
UBOOT_DTB_NAME = "${KERNEL_DEVICETREE_BASENAME}.dtb"
UBOOT_SUFFIX = "bin"
UBOOT_MAKE_TARGET ?= "all"
SPL_BINARY = "spl/u-boot-spl.bin"
LOADADDR = ""

DDR_FIRMWARE_NAME = " \
    lpddr4_pmu_train_1d_imem.bin \
    lpddr4_pmu_train_1d_dmem.bin \
    lpddr4_pmu_train_2d_imem.bin \
    lpddr4_pmu_train_2d_dmem.bin \
"

# Console on UART2
SERIAL_CONSOLES = "115200;ttymxc1"

# Boot with extlinux.conf (U-Boot distro boot). U-Boot and Linux use the
# same MMC numbering, the SD card is mmc1.
UBOOT_EXTLINUX = "1"
UBOOT_EXTLINUX_LABELS = "default"
UBOOT_EXTLINUX_DEFAULT_LABEL = "${RONETIX_BOARD_NAME}"
UBOOT_EXTLINUX_MENU_DESCRIPTION:default = "${RONETIX_BOARD_NAME}"
UBOOT_EXTLINUX_FDT:default = "../${KERNEL_DEVICETREE_BASENAME}.dtb"
UBOOT_EXTLINUX_CONSOLE:default = "console=${console}"
UBOOT_EXTLINUX_ROOT:default ?= "root=/dev/mmcblk1p2"

IMAGE_BOOT_FILES:append = " extlinux.conf;extlinux/extlinux.conf"
