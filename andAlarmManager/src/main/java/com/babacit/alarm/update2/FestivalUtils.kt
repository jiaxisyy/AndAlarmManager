package com.babacit.alarm.update2

import com.google.gson.annotations.Until

/**
 * Created by hekd on 2017/12/28.
 */
class FestivalUtils {
    companion object {

        val holidays = arrayListOf("圣诞节", "重阳节", "中秋节"
                , "国庆节", "教师节", "中元节"
                , "七夕节", "建军节", "建党节"
                , "父亲节", "环境日", "儿童节"
                , "端午节", "母亲节", "劳动节"
                , "地球日", "清明节", "植树节"
                , "妇女节", "情人节", "元宵节"
                , "春节", "元旦"
        )

        /**
         * 获取节假日的列表postion
         * @param str 节假日
         * @return 对应的postion
         */
        fun getHolidayPos(str: String): Int {

            for (i in 0..holidays.size) {

                if (str.contains(holidays[i])) {
                    return i
                }
            }
            return 0
        }

    }
}