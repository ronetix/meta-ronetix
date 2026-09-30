# Settings common to all Ronetix SoMs

# Development images: root logs in without password on the console and
# over SSH. For production images disable it in local.conf with
#   RONETIX_DEV_IMAGE_FEATURES = ""
RONETIX_DEV_IMAGE_FEATURES ?= "allow-empty-password allow-root-login empty-root-password"
EXTRA_IMAGE_FEATURES:append = " ${RONETIX_DEV_IMAGE_FEATURES}"
