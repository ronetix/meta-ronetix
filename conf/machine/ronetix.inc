
IMX_DEFAULT_BOOTLOADER = "u-boot-ronetix"
IMX_DEFAULT_BOOTLOADER_mx8 = "u-boot-ronetix"

IMX_DEFAULT_KERNEL = "linux-ronetix"
IMX_DEFAULT_KERNEL_mx7 = "linux-ronetix"
IMX_DEFAULT_KERNEL_mx8 = "linux-ronetix"
IMX_DEFAULT_KERNEL_mx8m = "linux-ronetix"
IMX_DEFAULT_KERNEL_use-mainline-bsp = "linux-ronetix"

IMX_DEFAULT_BSP = "nxp"

# Use gstreamer 1.16 from meta-freescale
PREFERRED_VERSION_gstreamer1.0 ?= "1.16.imx"
PREFERRED_VERSION_gstreamer1.0-plugins-base ?= "1.16.imx"
PREFERRED_VERSION_gstreamer1.0-plugins-good ?= "1.16.imx"
PREFERRED_VERSION_gstreamer1.0-plugins-bad ?= "1.16.imx"
PREFERRED_VERSION_gstreamer1.0-libav ?= "1.16.0"
