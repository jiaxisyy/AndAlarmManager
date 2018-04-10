package com.example.hekd.kotlinapp.ai

import java.io.Serializable
import java.util.*

/**
 * Created by hekd on 2017/12/14.
 *@param type 闹钟类型 1：起床闹钟；2：作业闹钟； 3：生日管家；4：日常提醒； 5：眼保健操；6：纪念日； 7：喝水/吃药；8：节日提醒； 9：自定义
 * @param period 周期类型 1：仅一次；2：每天； 3：每月；4：每年 5：自定义
 * @param date 日期
 * @param time 时间
 * @param weeks 此字段放周几，0：周一，1：周二… 可以保存多个，用逗号隔开 如： 0,3,4,5
 * @param event 事件
 * @param raw_text 完整语句
 */
data class ReminderMsg(val type: Int, val period: Int, val date: String, val time: String, val weeks: ArrayList<Int>?, val event: String,val raw_text:String):Serializable {
    override fun toString(): String {
        return "ReminderMsg(type=$type, period=$period, date='$date', time='$time', weeks=$weeks, event='$event', raw_text='$raw_text')"
    }
}