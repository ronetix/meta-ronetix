FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

# The default ALSA card of the i.MX9 SoMs, see the file for what it does
# and why. alsa-state is the package that owns /etc/asound.conf, and it is
# PACKAGE_ARCH = MACHINE_ARCH on the i.MX machines (PACKAGE_ARCH:imx-generic-bsp
# in meta-freescale's alsa-state.bbappend), so the override keeps the file
# off the other machines.
#
# ":append" on do_install, so this replaces the asound.conf that
# meta-freescale installs for the NXP BSP regardless of the order the
# bbappends are parsed in.
SRC_URI:append:mx9-generic-bsp = " file://ronetix-asound.conf"

do_install:append:mx9-generic-bsp() {
    install -m 0644 ${UNPACKDIR}/ronetix-asound.conf ${D}${sysconfdir}/asound.conf
}
