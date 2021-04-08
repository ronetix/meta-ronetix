FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
	file://ronetix-blacklist.conf \
"

do_install_append() {
	install -m 0755 -d ${D}${sysconfdir}/modprobe.d
	install -m 0644 ${WORKDIR}/ronetix-blacklist.conf ${D}${sysconfdir}/modprobe.d
}
