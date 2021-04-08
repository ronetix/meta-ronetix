FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

RDEPENDS_${PN}_append = " adwaita-icon-theme adwaita-icon-theme-cursors"

# [Shell] is already uncommented by default in weston.ini
INI_UNCOMMENT_ASSIGNMENTS_remove_mx8mq = " \
    \\[shell\\] \
"

INI_UNCOMMENT_ASSIGNMENTS_append_mx6 = " \
    use-g2d=1 \
"
