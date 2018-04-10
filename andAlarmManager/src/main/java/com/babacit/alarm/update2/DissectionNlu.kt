package com.example.hekd.kotlinapp.ai

import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by hekd on 2017/12/13.
 * 解剖语义
 * 解析优先级:提醒类型>周期>日期>时间>事件
 *
 */
class DissectionNlu {



    companion object {
        /**起床*/
        val STR_GETUP = "起床"
        /**作业*/
        val STR_WORK = "作业"
        /**生日*/
        val STR_BIRTHDAY = "生日"
        /**日常*/
        val STR_ORDINARY = "日常提醒"
        /**眼保健操*/
        val STR_EYE_EXERCISES = "眼保健操"
        /**纪念日*/
        val STR_MEMORIAL_DAY = "纪念日"
        /**喝水/吃药*/
        val STR_DRINKING = "喝水"
        val STR_MEDICINE = "吃药"
        /**节日*/
        val STR_FESTIVAL = "节"
        /**默认意图*/
        val STR_EVENT = "自定义"
        /**每天*/
        val STR_EVERYDAY = "每天"
        /**每个月*/
        val STR_EVERYMONTH = "月"

//        /**解析返回信息*/
//        var msg: ReminderMsg? = null
        /**
         *
         * 完成解析
         */
        fun dissection(s: String): ReminderMsg {

            //判断提醒类型
            when {
                s.contains(STR_GETUP) -> {
                    val msg = pteDissection(s, ReminderConstant.TYPE_GETUP)
                    println(msg.toString())
                    return msg

                }
                s.contains(STR_WORK) -> {
                    val msg = pteDissection(s, ReminderConstant.TYPE_WORK)
                    println(msg.toString())
                    return msg
                }
                s.contains(STR_BIRTHDAY) -> {
                    val msg = pteDissection(s, ReminderConstant.TYPE_BIRTHDAY)
                    println(msg.toString())
                    return msg
                }
                s.contains(STR_ORDINARY) -> {
                    val msg = pteDissection(s, ReminderConstant.TYPE_ORDINARY)
                    println(msg.toString())
                    return msg
                }
                s.contains(STR_EYE_EXERCISES) -> {
                    val msg = pteDissection(s, ReminderConstant.TYPE_EYE_EXERCISES)
                    println(msg.toString())
                    return msg
                }
                s.contains(STR_MEMORIAL_DAY) -> {
                    val msg = pteDissection(s, ReminderConstant.TYPE_MEMORIAL_DAY)
                    println(msg.toString())
                    return msg
                }
                s.contains(STR_DRINKING) -> {
                    val msg = pteDissection(s, ReminderConstant.TYPE_DRINKING)
                    println(msg.toString())
                    return msg
                }
                s.contains(STR_MEDICINE) -> {
                    val msg = pteDissection(s, ReminderConstant.TYPE_DRINKING)
                    println(msg.toString())
                    return msg
                }
                s.contains(STR_FESTIVAL) -> {
                    val msg = pteDissection(s, ReminderConstant.TYPE_FESTIVAL)
                    println(msg.toString())
                    return msg
                }
                else -> {
                    val msg = pteDissection(s, ReminderConstant.TYPE_USER_DEFINED)
                    println(msg.toString())
                    return msg
                }
            }
        }

        /**
         * 周期,时间,事件解析
         * @param s 完整语句
         * @return 解析信息
         *
         */
        private fun pteDissection(s: String, type: Int): ReminderMsg {
            /**周几*/
            var weekList = arrayListOf<Int>()
            val gson = Gson()
            val fromJson = gson.fromJson(s, ReminderBean::class.java)
            try {
                val objectBean = fromJson.merged_res!!.semantic_form!!.results!![0].`object`
                /**详细语句*/
                val raw_text = fromJson.merged_res!!.semantic_form!!.raw_text
                /**意图*/
                var _event = objectBean!!._event
                /**大体时间*/
                val _time = objectBean._time
                /**具体时间*/
                var time = objectBean.time
                /**具体日期*/
                var date = objectBean.date
                /**例如周一至周五*/
                val fromTo = "至"
                /**例如周一至周五*/
                val fromTo2 = "到"
                /**例如周一至周五中的"周一"*/
                var s1 = ""
                /**例如周一至周五中的"周五"*/
                var s2 = ""
                /**具体周期*/
                var period = 0
                /**周次个数,例如:有周一则次数+1*/
                var weekCount = 0

                when {// 周期
                    _time!!.contains(STR_EVERYDAY) -> {
                        //每天
                        period = ReminderConstant.PERIOD_EVERYDAY
                    }
                    _time.contains(STR_EVERYMONTH) -> {
                        //每月
                        period = ReminderConstant.PERIOD_EVERYMONTH
                    }

                    _time.contains(ReminderConstant.PERIOD_WEEK[0]) ||
                            _time.contains(ReminderConstant.PERIOD_WEEK[1]) ||
                            _time.contains(ReminderConstant.PERIOD_WEEK[2]) ||
                            _time.contains(ReminderConstant.PERIOD_WEEK[3]) ||
                            _time.contains(ReminderConstant.PERIOD_WEEK[4]) ||
                            _time.contains(ReminderConstant.PERIOD_WEEK[5]) ||
                            _time.contains(ReminderConstant.PERIOD_WEEK[6]) -> {
                        //周几
                        for (i in 0 until ReminderConstant.PERIOD_WEEK.size) {
                            if (raw_text!!.contains(ReminderConstant.PERIOD_WEEK[i])) {
                                weekList.add(i)
                                weekCount++
                            }
                        }
                        //连续周几,例如周一至周五
                        if (weekCount == 2 && raw_text!!.contains(fromTo)) {
                            //获取"至"在其中的角标,截取前面和后面的周期
                            val indexOf = raw_text.indexOf(fromTo)
                            //截取周一
                            s1 = raw_text.substring(indexOf - 2, indexOf)
                            s2 = raw_text.substring(indexOf + 1, indexOf + 3)
                            println("========${s1}至$s2============")
                            val i1 = weekStrToInt(s1)
                            val i2 = weekStrToInt(s2)
                            val i = i2 - i1
                            weekList.clear()
                            (0..i).mapTo(weekList) { i1+ it }

                        }
                        if (weekCount == 2 && raw_text!!.contains(fromTo2)) {
                            //获取"至"在其中的角标,截取前面和后面的周期
                            val indexOf = raw_text.indexOf(fromTo2)
                            //截取周一
                            s1 = raw_text.substring(indexOf - 2, indexOf)
                            s2 = raw_text.substring(indexOf + 1, indexOf + 3)
                            println("========${s1}到$s2============")
                            val i1 = weekStrToInt(s1)
                            val i2 = weekStrToInt(s2)
                            val i = i2 - i1
                            weekList.clear()
                            (0..i).mapTo(weekList) { i1+ it }
                        }
                        period = ReminderConstant.PERIOD_USER_DEFINED
                    }
                    else -> {
                        //仅一次
                        period = ReminderConstant.PERIOD_ONLYONCE
                    }
                }
                println("==========周期->$period=============")
                println("==========周几->$weekList=============")
                println("==========几个数->$weekCount=============")
                val (nowDate, nowTime) = getTodayTime()
                if (date == null) {
                    date = nowDate
                }
                if (time == null) {
                    time = nowTime
                }
                if (_event == null) {
                    val pair = errorNlu(raw_text)
                    _event = pair.first
                }
                return ReminderMsg(type, period, date, time, weekList, _event, raw_text!!)
            } catch (e: Exception) {
                /**
                 * 解析错误
                 * IndexOutOfBoundsException: Invalid index 0, size is 0
                 */
                /**周几*/
                val errorWeekList = arrayListOf<Int>()
                (0 until ReminderConstant.PERIOD_WEEK.size)
                        .filter { s.contains(ReminderConstant.PERIOD_WEEK[it]) }
                        .forEach { errorWeekList.add(it) }
                println("==========周期对应的数->$errorWeekList=============")
                e.printStackTrace()
                val (nowDate, nowTime) = getTodayTime()
                //获取完整语句
                val raw_text = fromJson.merged_res!!.semantic_form!!.raw_text

                return if (errorWeekList.size >= 2) {
                    val pair = errorNlu(raw_text)
                    ReminderMsg(pair.second, ReminderConstant.PERIOD_USER_DEFINED, nowDate, nowTime, errorWeekList, pair.first, raw_text!!)
                }else{
                    //返回默认信息
                    ReminderMsg(ReminderConstant.TYPE_USER_DEFINED, ReminderConstant.PERIOD_ONLYONCE, nowDate, nowTime, errorWeekList, STR_EVENT, raw_text!!)
                }
            }
        }

        /**
         *
         * 当出现周期过多时解析错误时手动解析
         * @return Pair(event,type)
         */
        private fun errorNlu(raw_text: String?): Pair<String, Int> {
            //返回解析错误的解决方案
            var error_evrnt = ""
            var error_type = 0
            when {
                raw_text!!.contains(STR_GETUP) -> {
                    error_evrnt = STR_GETUP
                    error_type = ReminderConstant.TYPE_GETUP
                }
                raw_text.contains(STR_WORK) -> {
                    error_evrnt = STR_WORK
                    error_type = ReminderConstant.TYPE_WORK
                }
                raw_text.contains(STR_BIRTHDAY) -> {
                    error_evrnt = STR_BIRTHDAY
                    error_type = ReminderConstant.TYPE_BIRTHDAY
                }
                raw_text.contains(STR_ORDINARY) -> {
                    error_evrnt = STR_ORDINARY
                    error_type = ReminderConstant.TYPE_ORDINARY
                }
                raw_text.contains(STR_EYE_EXERCISES) -> {
                    error_evrnt = STR_EYE_EXERCISES
                    error_type = ReminderConstant.TYPE_EYE_EXERCISES
                }
                raw_text.contains(STR_MEMORIAL_DAY) -> {
                    error_evrnt = STR_MEMORIAL_DAY
                    error_type = ReminderConstant.TYPE_MEMORIAL_DAY
                }
                raw_text.contains(STR_DRINKING) -> {
                    error_evrnt = STR_DRINKING
                    error_type = ReminderConstant.TYPE_DRINKING
                }
                raw_text.contains(STR_MEDICINE) -> {
                    error_evrnt = STR_MEDICINE
                    error_type = ReminderConstant.TYPE_DRINKING
                }
                raw_text.contains(STR_FESTIVAL) -> {
                    error_evrnt = STR_FESTIVAL
                    error_type = ReminderConstant.TYPE_FESTIVAL
                }
                else -> {
                    error_evrnt = STR_EVENT
                }
            }
            return Pair(error_evrnt, error_type)
        }

        /**
         * 获取今天的日期和时间
         * @return 日期和时间
         */
        private fun getTodayTime(): Pair<String, String> {
            val dateFormatDate = SimpleDateFormat("yyyy-MM-dd")
            val dateFormatTime = SimpleDateFormat("HH:mm")
            val sysDate = Date(System.currentTimeMillis())
            val nowDate = dateFormatDate.format(sysDate)
            val nowTime = dateFormatTime.format(sysDate)
            return Pair(nowDate, nowTime)
        }

        /**
         *
         * 周末文字转换为数字
         */
        private fun weekStrToInt(s: String): Int = when (s) {
            ReminderConstant.PERIOD_WEEK[0] -> 0
            ReminderConstant.PERIOD_WEEK[1] -> 1
            ReminderConstant.PERIOD_WEEK[2] -> 2
            ReminderConstant.PERIOD_WEEK[3] -> 3
            ReminderConstant.PERIOD_WEEK[4] -> 4
            ReminderConstant.PERIOD_WEEK[5] -> 5
            ReminderConstant.PERIOD_WEEK[6] -> 6
            else -> -1
        }
    }
}