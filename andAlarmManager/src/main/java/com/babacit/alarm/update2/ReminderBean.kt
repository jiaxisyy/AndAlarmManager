package com.example.hekd.kotlinapp.ai

/**
 * Created by hekd on 2017/12/16.
 */

class ReminderBean {


    /**
     * merged_res : {"semantic_form":{"appid":15361,"err_no":0,"parsed_text":"每天 早上 七 点 提醒 我 起床","raw_text":"每天早上七点提醒我起床","results":[{"domain":"alarm","intent":"insert","object":{"_event":"起床","_settingtype":"schedule","_time":"每天早上七点","event":"起床","repeat":"00001111111","settingtype":"schedule","time":"07:00:00","type":"repeat"},"score":0.65}]}}
     */

    var merged_res: MergedResBean? = null

    class MergedResBean {
        /**
         * semantic_form : {"appid":15361,"err_no":0,"parsed_text":"每天 早上 七 点 提醒 我 起床","raw_text":"每天早上七点提醒我起床","results":[{"domain":"alarm","intent":"insert","object":{"_event":"起床","_settingtype":"schedule","_time":"每天早上七点","event":"起床","repeat":"00001111111","settingtype":"schedule","time":"07:00:00","type":"repeat"},"score":0.65}]}
         */

        var semantic_form: SemanticFormBean? = null

        class SemanticFormBean {
            /**
             * appid : 15361
             * err_no : 0
             * parsed_text : 每天 早上 七 点 提醒 我 起床
             * raw_text : 每天早上七点提醒我起床
             * results : [{"domain":"alarm","intent":"insert","object":{"_event":"起床","_settingtype":"schedule","_time":"每天早上七点","event":"起床","repeat":"00001111111","settingtype":"schedule","time":"07:00:00","type":"repeat"},"score":0.65}]
             */

            var appid: Int = 0
            var err_no: Int = 0
            var parsed_text: String? = null
            var raw_text: String? = null
            var results: List<ResultsBean>? = null

            class ResultsBean {
                /**
                 * domain : alarm
                 * intent : insert
                 * object : {"_event":"起床","_settingtype":"schedule","_time":"每天早上七点","event":"起床","repeat":"00001111111","settingtype":"schedule","time":"07:00:00","type":"repeat"}
                 * score : 0.65
                 */

                var domain: String? = null
                var intent: String? = null
                var `object`: ObjectBean? = null
                var score: Double = 0.toDouble()

                class ObjectBean {
                    /**
                     * _event : 起床
                     * _settingtype : schedule
                     * _time : 每天早上七点
                     * event : 起床
                     * repeat : 00001111111
                     * settingtype : schedule
                     * time : 07:00:00
                     * type : repeat
                     * date :2017-12-17
                     */

                    var _event: String? = null
                    var _settingtype: String? = null
                    var _time: String? = null
                    var event: String? = null
                    var repeat: String? = null
                    var settingtype: String? = null
                    var time: String? = null
                    var type: String? = null
                    var date: String? = null
                }
            }
        }
    }
}
