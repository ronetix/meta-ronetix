FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://ronetix-blacklist.conf \
    file://ronetix-brcmfmac.conf \
"

# The sound cards of the i.MX9 SoMs, see the file itself. base-files is
# PACKAGE_ARCH = MACHINE_ARCH, so the override only affects those machines.
SRC_URI:append:mx9-generic-bsp = " file://ronetix-asound.conf"

do_install:append() {
    install -m 0755 -d ${D}${sysconfdir}/modprobe.d
    install -m 0644 ${UNPACKDIR}/ronetix-blacklist.conf ${D}${sysconfdir}/modprobe.d
    install -m 0644 ${UNPACKDIR}/ronetix-brcmfmac.conf  ${D}${sysconfdir}/modprobe.d
}

do_install:append:mx9-generic-bsp() {
    install -m 0644 ${UNPACKDIR}/ronetix-asound.conf ${D}${sysconfdir}/asound.conf
}
