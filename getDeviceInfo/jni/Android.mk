LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

src_dir                    := src

LOCAL_SRC_FILES            := $(src_dir)/getDeviceInfo.c
LOCAL_MODULE               := getDeviceInfo

include $(BUILD_SHARED_LIBRARY)
