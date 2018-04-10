package com.babacit.alarm.update2

/**
 * Created by hekd on 2017/12/25.
 */
class WeekUtils {

    companion object {
        /**
         * 数字集合转换为对应星期的字符串
         */
        fun listIntToStr(weeks: ArrayList<Int>): StringBuffer {

            val sb = StringBuffer()

            weeks.forEach {
                when(it){
                    0->sb.append("周一")
                    1->sb.append("周二")
                    2->sb.append("周三")
                    3->sb.append("周四")
                    4->sb.append("周五")
                    5->sb.append("周六")
                    6->sb.append("周日")
                }
            }
            return sb
        }
    }
}