# Copyright 2019-2026 Ronetix GmbH
# Released under the MIT license (see COPYING.MIT for the terms)

# U-Boot for the Ronetix i.MX SoMs: the u-boot-fslc recipe with the
# sources from github.com/ronetix/u-boot, branch ronetix/v2025.01
# (U-Boot v2025.01 with the Ronetix board support).
require recipes-bsp/u-boot/u-boot-fslc_2025.01.bb

SUMMARY = "U-Boot for the Ronetix i.MX SoMs"
HOMEPAGE = "https://github.com/ronetix/u-boot"

SRC_URI = "git://github.com/ronetix/u-boot.git;branch=${SRCBRANCH};protocol=https"
SRCBRANCH = "ronetix/v2025.01"
SRCREV = "661c37825226daefa2823f9aee3f9c7c649f59f2"

PV = "2025.01+ronetix+git${SRCPV}"

# board/ronetix/imx93-osm/imximage.cfg appends the ELE container to
# flash.bin under the CPU revision independent name
# mx93-ahab-container.img, while firmware-ele-imx deploys it as
# mx93${IMX_SOC_REV_LOWER}-ahab-container.img. imx-boot-container.bbclass
# copies it into the U-Boot build directory under the deployed name, so
# add the name the binman description expects.
do_resolve_and_populate_binaries:append:mx93-generic-bsp() {
    if [ -n "${UBOOT_CONFIG}" ]; then
        for config in ${UBOOT_MACHINE}; do
            i=$(expr $i + 1);
            for type in ${UBOOT_CONFIG}; do
                builddir="${config}-${type}"
                j=$(expr $j + 1);
                if [ $j -eq $i ]; then
                    cp ${B}/${builddir}/${SECO_FIRMWARE_NAME} \
                       ${B}/${builddir}/mx93-ahab-container.img
                fi
            done
            unset j
        done
        unset i
    fi
}
