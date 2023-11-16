# Support additional firmware for WIFI+BT modules

SRC_URI += "\
        git://github.com/ronetix/linux-firmware.git;protocol=https;branch=master;name=linux-firmware;destsuffix=linux-firmware \
"

SRCREV = "${AUTOREV}"

do_install:append() {
        install -d ${D}${nonarch_base_libdir}/firmware/brcm
        install -m 0755 ${WORKDIR}/linux-firmware/brcm/* ${D}${nonarch_base_libdir}/firmware/brcm/
}

FPATH = "${nonarch_base_libdir}/firmware/brcm"
FILES:${PN}-bcm43752 = "${FPATH}/fw_bcm43752a2_ag.bin \
			${FPATH}/nvram_ap6275sdsr.txt \
			${FPATH}/clm_bcm43752a2_ag.blob \
			${FPATH}/BCM4362A2_001.003.006.1012.1017.hcd \
			"
PACKAGES =+ " ${PN}-bcm43752"
