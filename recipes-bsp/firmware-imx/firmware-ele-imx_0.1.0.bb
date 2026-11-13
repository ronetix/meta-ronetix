# NXP ELE (EdgeLock Enclave) firmware 0.1.0, kept for i.MX93 rev A0.
#
# meta-freescale carries firmware-ele-imx 2.0.6, whose archive contains only
# mx93a1-ahab-container.img. An i.MX93 A0 die needs mx93a0-ahab-container.img,
# and that was dropped after this 0.1.0 release - the one the 6.1/mickledore
# BSP used. Its archive holds both:
#
#   firmware-ele-imx-0.1.0/mx93a0-ahab-container.img
#   firmware-ele-imx-0.1.0/mx93a1-ahab-container.img
#
# A flash.bin built with the container of the wrong CPU revision does not
# boot - the ROM stops with no message. The RNX-iMX93-OSM carries an A0 die,
# so ronetix-imx9.inc selects this version whenever IMX_SOC_REV is "A0".
#
# This recipe is metadata only. The container itself is NXP Proprietary and
# its EULA (section 2.2) does not permit redistributing it, so the .bin is
# fetched from NXP and must not be committed to this layer. Section 2.3
# covers shipping it embedded in a product image.
#
# Derived from meta-imx/meta-bsp/recipes-bsp/firmware-imx/
# firmware-ele-imx_0.1.0.bb of the mickledore BSP.

SUMMARY = "NXP i.MX ELE firmware"
DESCRIPTION = "EdgeLock Enclave firmware for i.MX series SoCs"
HOMEPAGE = "https://www.nxp.com/"
SECTION = "base"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://COPYING;md5=2827219e81f28aba7c6a569f7c437fa7"

inherit fsl-eula-unpack use-imx-security-controller-firmware deploy

SRC_URI = "${FSL_MIRROR}/${BP}.bin;fsl-eula=true"
SRC_URI[md5sum] = "7532c2e356574c1c9255c01169d38427"
SRC_URI[sha256sum] = "4a0440168d8dfb95989a17be8fafb149589e110e7d4518e5e8a33463dfb5e5ca"

# The archive unpacks to ./${BP}/. oe-core unpacks into UNPACKDIR now, not
# WORKDIR, so S has to be spelled out - the mickledore recipe relied on the
# old default.
S = "${UNPACKDIR}/${BP}"

do_compile[noexec] = "1"

do_install() {
    install -d ${D}${nonarch_base_libdir}/firmware/imx/ele
    install -m 0644 ${S}/${SECO_FIRMWARE_NAME} \
        ${D}${nonarch_base_libdir}/firmware/imx/ele
}

do_deploy() {
    # picked up by the boot container build, see imx-boot-container.bbclass
    install -m 0644 ${S}/${SECO_FIRMWARE_NAME} ${DEPLOYDIR}
}
addtask deploy after do_install before do_build

FILES:${PN} += "${nonarch_base_libdir}/firmware/imx/ele/${SECO_FIRMWARE_NAME}"

COMPATIBLE_MACHINE = "(mx8ulp-nxp-bsp|mx9-nxp-bsp)"

# Exist only where this old version is actually wanted.
#
# meta-ronetix has BBFILE_PRIORITY 9 against meta-freescale's 5, and
# bitbake's sortPriorities() groups providers by layer priority before it
# looks at DEFAULT_PREFERENCE or the version. So simply having this recipe
# in the layer makes it win over firmware-ele-imx 2.0.6 for *every*
# machine - a PREFERRED_VERSION for the other machines does not help, and
# neither does DEFAULT_PREFERENCE = "-1". That would quietly put the 0.1.0
# ELE firmware into every flash.bin, including the A1 boards.
#
# Skipping the recipe removes it as a provider altogether, so an A1/A2
# build sees only meta-freescale's version and keeps following it when NXP
# bumps it.
python () {
    if d.getVar('IMX_SOC_REV') != 'A0':
        raise bb.parse.SkipRecipe(
            'only the i.MX93 rev A0 ELE container comes from this version, '
            'IMX_SOC_REV is "%s"' % d.getVar('IMX_SOC_REV'))
}
