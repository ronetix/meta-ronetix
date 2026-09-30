FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://syslogd"

do_install:append() {
    install -D -m 0644 ${UNPACKDIR}/syslogd ${D}${sysconfdir}/default/syslogd
}

CONFFILES:${PN} += "${sysconfdir}/default/syslogd"
