# Copyright 2019-2026 Ronetix GmbH
# Released under the MIT license (see COPYING.MIT for the terms)

# U-Boot for the Ronetix i.MX SoMs: the u-boot-fslc recipe with the
# sources from github.com/ronetix/u-boot, branch ronetix/v2025.01
# (U-Boot v2025.01 with the Ronetix board support).
require recipes-bsp/u-boot/u-boot-fslc_2025.01.bb

SUMMARY = "U-Boot for the Ronetix i.MX SoMs"
HOMEPAGE = "https://github.com/ronetix/u-boot"

SRC_URI = "git://github.com/ronetix/u-boot.git;branch=${SRCBRANCH};protocol=https"
SRCBRANCH = "ronetix/v2025.01"
SRCREV = "006a72dd689826b8dd30e6e1a1e5e991912e4c78"

PV = "2025.01+ronetix+git${SRCPV}"
