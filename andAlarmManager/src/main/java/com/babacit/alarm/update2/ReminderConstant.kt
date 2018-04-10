package com.example.hekd.kotlinapp.ai

/**
 * Created by hekd on 2017/12/13.
 */
class ReminderConstant {

    companion object {
        /**
         * 闹钟类型 1：起床闹钟；2：作业闹钟； 3：生日管家；4：日常提醒； 5：眼保健操；6：纪念日； 7：喝水/吃药；8：节日提醒； 9：自定义
         */
        /**解析出错*/
        val TYPE_ERROR = -1
        /**解析默认*/
        val TYPE_DEFAULT = 0
        /**起床*/
        val TYPE_GETUP = 1
        /**作业*/
        val TYPE_WORK = 2
        /**生日*/
        val TYPE_BIRTHDAY = 3
        /**日常*/
        val TYPE_ORDINARY = 4
        /**眼保健操*/
        val TYPE_EYE_EXERCISES = 5
        /**纪念日*/
        val TYPE_MEMORIAL_DAY = 6
        /**喝水/吃药*/
        val TYPE_DRINKING = 7
        /**节日*/
        val TYPE_FESTIVAL = 8
        /**自定义*/
        val TYPE_USER_DEFINED = 9
        /**闹钟周期-每天*/
        val PERIOD_EVERYDAY = 2
        /**闹钟周期-每月*/
        val PERIOD_EVERYMONTH = 3
        /**闹钟周期-仅一次*/
        val PERIOD_ONLYONCE = 1
        /**闹钟周期-自定义*/
        val PERIOD_USER_DEFINED = 5
        /**闹钟周期-周一-周日*/
        val PERIOD_WEEK = arrayListOf("周一", "周二", "周三", "周四", "周五", "周六", "周日")
        /**解析信息*/
        val REMINDERMSG = "remindermsg"


    }
}