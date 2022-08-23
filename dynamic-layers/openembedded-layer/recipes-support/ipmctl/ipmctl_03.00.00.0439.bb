SUMMARY = "Utility for managing Intel Optane DC persistent memory modules"
DESCRIPTION = "Utility for configuring and managing Intel Optane Persistent \
Memory modules (PMem). It supports functionality to: \
Discover DCPMMs on the platform. \
Provision the platform memory configuration. \
View and update the firmware on DCPMMs. \
Configure data-at-rest security on DCPMMs. \
Track health and performance of DCPMMs. \
Debug and troubleshoot DCPMMs."

HOMEPAGE = "https://github.com/intel/ipmctl"
BUGTRACKER = "https://github.com/intel/ipmctl/issues"

LICENSE = "BSD-3-Clause | BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=72b9da60da6219d612ce30b746a0fe71  \
                    file://edk2/License.txt;md5=6123e5bf044a66db96c4ce88a36b2d08"

SRC_URI = "git://github.com/intel/ipmctl.git;protocol=https;branch=development;name=ipmctl; \
        git://github.com/tianocore/edk2.git;protocol=https;name=edk2;destsuffix=git/edk2;branch=master \
        file://0001-Ignore-STATIC_ASSERTs-and-NULL-define-for-os-and-ut-builds.patch;patchdir=edk2 \
        file://0001-CMakeLists-disable-Werror.patch \
        file://227d9cb35658fe104ff6fde62e4a00e6d595df0d.patch \
"

SRCREV_ipmctl = "8a73a975e2501c9baa6e4b1fb9101b40a227d084"
#tag edk2-stable202205
SRCREV_edk2 = "16779ede2d366bfc6b702e817356ccf43425bcc8"

S = "${WORKDIR}/git"

inherit cmake dos2unix

DEPENDS = "ndctl pkgconfig-native"

EXTRA_OECMAKE = "-DRELEASE=ON"

do_configure:prepend() {
    for dir in BaseTools MdeModulePkg MdePkg ShellPkg ; do
        ln -sf edk2/${dir} ${S}
    done
}

do_install:append() {
    # Remove /var/log/ipmctl as anything created in /var/log will not be
    # available when tmpfs is mounted at /var/volatile/log.
    rm -rf ${D}${localstatedir}/log
} 