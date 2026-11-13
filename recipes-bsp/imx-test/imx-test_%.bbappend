# imx-test does not inherit pkgconfig, so PKG_CONFIG_SYSROOT_DIR is never
# exported into the build environment. test/patgen/Makefile:5 reads
#
#   CFLAGS = -Os -I. -I$(PKG_CONFIG_SYSROOT_DIR)/usr/include/freetype2 \
#                   -I$(PKG_CONFIG_SYSROOT_DIR)/usr/include/libpng16
#
# so with the variable empty those collapse to the build host's
# /usr/include/freetype2 and /usr/include/libpng16, and gcc rejects them:
#
#   cc1: error: include location "/usr/include/freetype2" is unsafe for
#        cross-compilation [-Werror=poison-system-directories]
#
# patgen is part of the IMX8ULP platform test set, which is what
# PLATFORM:mx93-nxp-bsp selects, so every i.MX93 NXP-BSP build hits this.
# freetype is already in DEPENDS, so the headers are in the sysroot; only
# the path was wrong.
#
# This is a meta-freescale/imx-test bug, not a Ronetix one - drop this
# bbappend once it is fixed upstream.
inherit pkgconfig
