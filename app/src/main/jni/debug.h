#ifndef DEBUG_H
#define DEBUG_H
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdarg.h>
#include "android/log.h"

#define DEBUG_SWITCH        1       /* 打开调试信息打印功能 */
#define ERR_DEBUG_SWITCH    1   /* 打印错误信息打印功能 */
#define EXAM_ASSERT_TEST_   1    /* 开启断言 */

/**
 * 简单打印调试信息
 */
#if    DEBUG_SWITCH
//#define PR_DEBUG(fmt,args...) fprintf(stderr,fmt, ##args)
#define LOG    "smartserver-jni" // 这个是自定义的LOG的标识
#define LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG,__VA_ARGS__) // 定义LOGI类型
#else
#define PR_DEBUG(fmt,args...) /*do nothing */
#define LOGI(...)
#endif

/**
 * 错误信息打印
 * 自动打印发生错误时代码所在的位置
 */
#if    ERR_DEBUG_SWITCH
#define PR_ERR(fmt,args...) fprintf(stderr,"\nError:\nFile:<%s> Fun:[%s] Line:%d\n "fmt, __FILE__, __FUNCTION__, __LINE__, ##args)
#else
#define PR_ERR(fmt,args...) /*do nothing */
#endif

/**
 * 断言
 * 对某种假设条件进行检查(若条件成立则无动作，否则报告错误信息)
 */
#ifdef EXAM_ASSERT_TEST_ // 若使用断言测试
#define EXAM_ASSERT(condition, fmt, args...)  \
({ if(condition)    \
    {   \
        fprintf(stderr,"\nError:\nFile:<%s> Fun:[%s] Line:%d\n "fmt, __FILE__, __FUNCTION__, __LINE__, ##args);   \
            abort();    \
    }   \
})

#else // 若不使用断言测试
#define EXAM_ASSERT(condition, fmt, args...) NULL
#endif

#endif // DEBUG_H
