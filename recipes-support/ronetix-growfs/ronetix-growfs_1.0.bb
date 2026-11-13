SUMMARY = "Grow the root filesystem to the size of the storage device on first boot"
DESCRIPTION = "\
The Ronetix i.MX9 SoMs are built with 4, 8 or 16 GB eMMC, and the .wic \
image sizes its root partition to the root filesystem only, so one image \
serves every variant. This oneshot service extends the root partition and \
its ext4 filesystem to the end of the device on the first boot after \
flashing, and does nothing on later boots."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
    file://ronetix-growfs \
    file://ronetix-growfs.service \
"

# oe-core unpacks file:// into UNPACKDIR, not WORKDIR
S = "${UNPACKDIR}"

inherit systemd

SYSTEMD_SERVICE:${PN} = "ronetix-growfs.service"
SYSTEMD_AUTO_ENABLE = "enable"

# sfdisk and partx are not in the default image, resize2fs and findmnt are,
# but depend on all four so the package is self contained.
RDEPENDS:${PN} = " \
    util-linux-sfdisk \
    util-linux-partx \
    util-linux-findmnt \
    e2fsprogs-resize2fs \
"

do_install() {
    install -D -m 0755 ${S}/ronetix-growfs ${D}${sbindir}/ronetix-growfs
    install -D -m 0644 ${S}/ronetix-growfs.service \
        ${D}${systemd_system_unitdir}/ronetix-growfs.service
}

# Nothing architecture specific, but it is installed per machine image
COMPATIBLE_MACHINE = "(mx9-nxp-bsp)"
