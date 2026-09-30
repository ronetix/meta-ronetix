# Firmware of the Broadcom WiFi/Bluetooth modules on the Ronetix SoMs

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " \
    file://brcm/brcmfmac43456-sdio.bin \
    file://brcm/brcmfmac43456-sdio.txt \
    file://brcm/brcmfmac43456-sdio.clm_blob \
    file://brcm/brcmfmac4339-sdio.bin \
    file://brcm/brcmfmac4339-sdio.txt \
"

do_install:append() {
    install -d ${D}${nonarch_base_libdir}/firmware/brcm
    install -m 0644 ${UNPACKDIR}/brcm/brcmfmac* ${D}${nonarch_base_libdir}/firmware/brcm/
}

PACKAGES =+ "${PN}-bcm43456"

FILES:${PN}-bcm43456 = "${nonarch_base_libdir}/firmware/brcm/brcmfmac43456-sdio.*"
LICENSE:${PN}-bcm43456 = "Firmware-broadcom_bcm43xx"
RDEPENDS:${PN}-bcm43456 += "${PN}-broadcom-license"

# keep the upstream files of the package (e.g. cypress/cyfmac4339-sdio.bin),
# add the NVRAM file
FILES:${PN}-bcm4339 += "${nonarch_base_libdir}/firmware/brcm/brcmfmac4339-sdio.txt"
