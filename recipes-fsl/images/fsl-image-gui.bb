# Copyright (C) 2015 Freescale Semiconductor
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Freescale Image to validate i.MX machines. \
This image contains everything used to test i.MX machines including GUI, \
demos and lots of applications. This creates a very large image, not \
suitable for production."
LICENSE = "MIT"

inherit core-image

### WARNING: This image is NOT suitable for production use and is intended
###          to provide a way for users to reproduce the image used during
###          the validation process of i.MX BSP releases.

IMAGE_FEATURES += " \
    splash \
    package-management \
    ssh-server-dropbear \
    hwcodecs \
    allow-empty-password \
    allow-root-login \
    empty-root-password \
    post-install-logging \
    nfs-server \
    tools-debug \
    eclipse-debug \
    tools-testapps \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', '', \
       bb.utils.contains('DISTRO_FEATURES',     'x11', 'x11-base x11-sato', \
                                                       '', d), d)} \
"

# nodejs is deliberately not in this list: it is one of the heaviest
# compiles in the tree (~3000 objects) and on a clean build it ends up the
# sole item on the critical path, while nothing in the images needs
# Node.js on the target. Add it back here if a product image does.
CORE_IMAGE_EXTRA_INSTALL += " \
	packagegroup-core-full-cmdline \
	packagegroup-tools-bluetooth \
	packagegroup-imx-tools-audio \
	packagegroup-fsl-tools-gpu-external \
	packagegroup-fsl-tools-testapps \
	packagegroup-fsl-tools-benchmark \
	packagegroup-fsl-gstreamer1.0 \
	packagegroup-fsl-gstreamer1.0-full \
	${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'xterm', '', d)} \
	${@bb.utils.contains('DISTRO_FEATURES', 'x11 wayland', 'weston-xwayland', '', d)} \
	${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'weston-init', \
	   bb.utils.contains('DISTRO_FEATURES',     'x11', 'packagegroup-core-x11-sato-games', \
							 '', d), d)} \
	screen \
	tcf-agent \
	openssh-sftp-server \
"

# v4l2-ctl and media-ctl, for bringing up the MIPI CSI camera. NXP's own
# fsl-image-gui of the i.MX BSP gets them through imx-test, which the
# community BSP does not have, so without this the image has no way to
# enumerate a capture device.
CORE_IMAGE_EXTRA_INSTALL += " \
	v4l-utils \
	media-ctl \
"

# only for Android enabled machines
IMAGE_INSTALL:append:imxgpu3d = " \
	android-tools \
"

# only for DRM enabled machines
IMAGE_INSTALL:append:imxdrm = " \
	libdrm-tests \
"

CORE_IMAGE_EXTRA_INSTALL:append:mx8-generic-bsp = "\
    packagegroup-fsl-tools-gpu \
"

systemd_disable_vt () {
    rm ${IMAGE_ROOTFS}${root_prefix}${sysconfdir}/systemd/system/getty.target.wants/getty@tty*.service
}

IMAGE_PREPROCESS_COMMAND:append = " ${@ 'systemd_disable_vt;' if bb.utils.contains('DISTRO_FEATURES', 'systemd', True, False, d) and bb.utils.contains('USE_VT', '0', True, False, d) else ''} "
