FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://ronetix-blacklist.conf"

# Files of the i.MX9 SoMs only, see each file for what it does. base-files is
# PACKAGE_ARCH = MACHINE_ARCH, so the override keeps them off the other
# machines: the brcmfmac options are specific to the BCM43752A2 of the
# AP6275S, and the sound cards are the ones of these device trees.
SRC_URI:append:mx9-generic-bsp = " \
    file://ronetix-brcmfmac.conf \
    file://ronetix-asound.conf \
"

do_install:append() {
    install -m 0755 -d ${D}${sysconfdir}/modprobe.d
    install -m 0644 ${UNPACKDIR}/ronetix-blacklist.conf ${D}${sysconfdir}/modprobe.d
}

do_install:append:mx9-generic-bsp() {
    install -m 0644 ${UNPACKDIR}/ronetix-brcmfmac.conf ${D}${sysconfdir}/modprobe.d
    install -m 0644 ${UNPACKDIR}/ronetix-asound.conf   ${D}${sysconfdir}/asound.conf
}
