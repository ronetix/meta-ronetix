# Firmware of the Broadcom WiFi/Bluetooth modules on the Ronetix SoMs.
#
# BCM43456 and BCM4339 of the i.MX7/8M COMs come from this layer. The
# AP6275S (BCM43752A2) of the i.MX9 SoMs comes from
# github.com/ronetix/linux-firmware, because upstream linux-firmware ships
# nothing at all for the 43752.
#
# The vendor drop carries the bcmdhd style names
#   fw_bcm43752a2_ag.bin  nvram_ap6275sdsr.txt  clm_bcm43752a2_ag.blob
#   BCM4362A2_001.003.006.1012.1017.hcd
# while brcmfmac asks for
#   brcm/brcmfmac43752-sdio[.<root compatible>].{bin,txt,clm_blob}
# and tries the compatible suffixed name first. Both naming schemes are
# installed, the board specific ones as symlinks, so that no lookup misses -
# a miss is not only cosmetic, it also costs 60 s per file when
# CONFIG_FW_LOADER_USER_HELPER_FALLBACK is set.
#
# brcmfmac drives these bcmdhd images. The two brcmfmac fixes the module
# needs live in the kernel tree, see
# recipes-kernel/linux/linux-ronetix/imx93-rnx.cfg.

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# BCM43456 and BCM4339, i.MX7/8M COMs
SRC_URI:append = " \
    file://brcm/brcmfmac43456-sdio.bin \
    file://brcm/brcmfmac43456-sdio.txt \
    file://brcm/brcmfmac43456-sdio.clm_blob \
    file://brcm/brcmfmac4339-sdio.bin \
    file://brcm/brcmfmac4339-sdio.txt \
"

# AP6275S (BCM43752A2), i.MX9 SoMs. Pinned rather than ${AUTOREV}, which
# would make the build unreproducible and re-fetch on every parse.
SRC_URI += "git://github.com/ronetix/linux-firmware.git;protocol=https;branch=master;destsuffix=ronetix-firmware"
SRCREV = "cfd8430c06a752473e67d80cd000d68c6e585487"

# destsuffix is relative to UNPACKDIR ("${WORKDIR}/sources" since oe-core
# moved it), not to WORKDIR as in the older BSP.
RONETIX_FW = "${UNPACKDIR}/ronetix-firmware/brcm"
FWDIR = "${nonarch_base_libdir}/firmware/brcm"

do_install:append() {
    install -d ${D}${FWDIR}

    # i.MX7/8M COMs
    install -m 0644 ${UNPACKDIR}/brcm/brcmfmac* ${D}${FWDIR}/

    # i.MX9 SoMs, vendor names as the 6.1 BSP shipped them
    install -m 0644 ${RONETIX_FW}/fw_bcm43752a2_ag.bin   ${D}${FWDIR}/
    install -m 0644 ${RONETIX_FW}/nvram_ap6275sdsr.txt   ${D}${FWDIR}/
    install -m 0644 ${RONETIX_FW}/clm_bcm43752a2_ag.blob ${D}${FWDIR}/
    install -m 0644 ${RONETIX_FW}/BCM4362A2_*.hcd        ${D}${FWDIR}/

    # the names brcmfmac actually requests
    install -m 0644 ${RONETIX_FW}/fw_bcm43752a2_ag.bin   ${D}${FWDIR}/brcmfmac43752-sdio.bin
    install -m 0644 ${RONETIX_FW}/nvram_ap6275sdsr.txt   ${D}${FWDIR}/brcmfmac43752-sdio.txt
    install -m 0644 ${RONETIX_FW}/clm_bcm43752a2_ag.blob ${D}${FWDIR}/brcmfmac43752-sdio.clm_blob

    # board specific variants, tried before the generic ones
    for compat in "fsl,imx93-smarc" "fsl,imx93-osm"; do
        ln -sf brcmfmac43752-sdio.bin      "${D}${FWDIR}/brcmfmac43752-sdio.${compat}.bin"
        ln -sf brcmfmac43752-sdio.txt      "${D}${FWDIR}/brcmfmac43752-sdio.${compat}.txt"
        ln -sf brcmfmac43752-sdio.clm_blob "${D}${FWDIR}/brcmfmac43752-sdio.${compat}.clm_blob"
    done

    # btbcm looks for brcm/BCM<chip>.hcd
    ln -sf BCM4362A2_001.003.006.1012.1017.hcd ${D}${FWDIR}/BCM4362A2.hcd
}

PACKAGES =+ "${PN}-bcm43456 ${PN}-bcm43752"

FILES:${PN}-bcm43456 = "${FWDIR}/brcmfmac43456-sdio.*"
LICENSE:${PN}-bcm43456 = "Firmware-broadcom_bcm43xx"
RDEPENDS:${PN}-bcm43456 += "${PN}-broadcom-license"

# keep the upstream files of the package (e.g. cypress/cyfmac4339-sdio.bin),
# add the NVRAM file
FILES:${PN}-bcm4339 += "${FWDIR}/brcmfmac4339-sdio.txt"

LICENSE:${PN}-bcm43752 = "Firmware-broadcom_bcm43xx"
RDEPENDS:${PN}-bcm43752 += "${PN}-broadcom-license"
FILES:${PN}-bcm43752 = " \
    ${FWDIR}/fw_bcm43752a2_ag.bin \
    ${FWDIR}/nvram_ap6275sdsr.txt \
    ${FWDIR}/clm_bcm43752a2_ag.blob \
    ${FWDIR}/BCM4362A2_001.003.006.1012.1017.hcd \
    ${FWDIR}/BCM4362A2.hcd \
    ${FWDIR}/brcmfmac43752-sdio.bin \
    ${FWDIR}/brcmfmac43752-sdio.txt \
    ${FWDIR}/brcmfmac43752-sdio.clm_blob \
    ${FWDIR}/brcmfmac43752-sdio.fsl,imx93-smarc.bin \
    ${FWDIR}/brcmfmac43752-sdio.fsl,imx93-smarc.txt \
    ${FWDIR}/brcmfmac43752-sdio.fsl,imx93-smarc.clm_blob \
    ${FWDIR}/brcmfmac43752-sdio.fsl,imx93-osm.bin \
    ${FWDIR}/brcmfmac43752-sdio.fsl,imx93-osm.txt \
    ${FWDIR}/brcmfmac43752-sdio.fsl,imx93-osm.clm_blob \
"
