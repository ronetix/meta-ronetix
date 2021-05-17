# Support additional firmware for BCM43456 WIFI+BT module

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRCREV_FORMAT = "linux-firmware"

SRC_URI_append = " \
	file://brcm/brcmfmac43456-sdio.bin \
	file://brcm/brcmfmac43456-sdio.txt \
"
do_install_append() {
	install -d ${D}${nonarch_base_libdir}/firmware/brcm
	install -m 0755 ${WORKDIR}/brcm/* ${D}${nonarch_base_libdir}/firmware/brcm/
}

FILES_${PN}-bcm43456 = " \
  ${nonarch_base_libdir}/firmware/brcm/brcmfmac43456-sdio.* \
"

RDEPENDS_${PN}-bcm43456 += "${PN}-broadcom-license"
LICENSE_${PN}-bcm43456 += "Firmware-broadcom_bcm43xx"
PACKAGES =+ " ${PN}-bcm43456"
