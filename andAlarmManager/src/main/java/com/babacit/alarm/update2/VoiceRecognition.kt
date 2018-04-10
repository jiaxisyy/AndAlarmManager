package com.example.hekd.kotlinapp.ai

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import com.babacit.alarm.R
import com.babacit.alarm.ui.activity.*
import com.baidu.speech.EventListener
import com.baidu.speech.EventManager
import com.baidu.speech.EventManagerFactory
import com.baidu.speech.asr.SpeechConstant
import com.bumptech.glide.Glide
import org.json.JSONObject

/**
 * Created by hekd on 2017/12/5.
 */
class VoiceRecognition : EventListener, IRecogListener {

    /**是否第一次启动*/
    var initFlag = true
    var mActivity: Activity? = null

    constructor(context: Context) {
        mContext = context
    }

    override fun onAsrReady() {
    }

    override fun onAsrBegin() {
    }

    override fun onAsrEnd() {
    }

    override fun onAsrPartialResult(results: Array<String>, recogResult: RecogResult) {
    }

    override fun onAsrFinalResult(results: Array<String?>?, recogResult: RecogResult) {
        println(results!![0] + "==========onAsrFinalResult===========")
//        tv_voiceRawText.text="识别结果:\n"+results!![0]
    }

    override fun onAsrFinish(recogResult: RecogResult) {
    }

    override fun onAsrFinishError(errorCode: Int, subErrorCode: Int, errorMessage: String, descMessage: String, recogResult: RecogResult) {
    }

    override fun onAsrLongFinish() {
    }

    override fun onAsrVolume(volumePercent: Int, volume: Int) {
    }

    override fun onAsrAudio(data: ByteArray, offset: Int, length: Int) {
    }

    override fun onAsrExit() {
    }


    override fun onAsrOnlineNluResult(nluResult: String) {
        println("==========onAsrOnlineNluResult->$nluResult===========")
        val reminderMsg = DissectionNlu.dissection(nluResult)
        println("==========onAsrOnlineNluResult->reminderMsg->$reminderMsg===========")
        val type = reminderMsg.type

        when (type) {
        //起床
            ReminderConstant.TYPE_GETUP -> {
                val getup = Intent(mContext, GetupAlarmActivity::class.java)
                getup.putExtra(ReminderConstant.REMINDERMSG, reminderMsg)
                mContext!!.startActivity(getup)
            }
        //作业课程
            ReminderConstant.TYPE_WORK -> {
                val homework = Intent(mContext, HomeworkRemindActivity::class.java)
                homework.putExtra(ReminderConstant.REMINDERMSG, reminderMsg)
                mContext!!.startActivity(homework)
            }
        //生日不做解析,解析失败
            ReminderConstant.TYPE_BIRTHDAY -> {
                val birthday = Intent(mContext, BirthdayManagerAlarmActivity::class.java)
                birthday.putExtra(ReminderConstant.REMINDERMSG, reminderMsg)
                mContext!!.startActivity(birthday)
            }
        //日常提醒
            ReminderConstant.TYPE_ORDINARY -> {
                val dailyRemind = Intent(mContext, DailyRemindAlarmActivity::class.java)
                dailyRemind.putExtra(ReminderConstant.REMINDERMSG, reminderMsg)
                mContext!!.startActivity(dailyRemind)
            }
        //眼保间操
            ReminderConstant.TYPE_EYE_EXERCISES -> {
                val eyesProtect = Intent(mContext, EyesProtectActivity::class.java)
                eyesProtect.putExtra(ReminderConstant.REMINDERMSG, reminderMsg)
                mContext!!.startActivity(eyesProtect)

            }
        //纪念日
            ReminderConstant.TYPE_MEMORIAL_DAY -> {
                val anniversary = Intent(mContext, AnniversaryAlarmActivity::class.java)
                anniversary.putExtra(ReminderConstant.REMINDERMSG, reminderMsg)
                mContext!!.startActivity(anniversary)
            }
        //自定义
            ReminderConstant.TYPE_USER_DEFINED -> {
                val custom = Intent(mContext, CustomAlarmActivity::class.java)
                custom.putExtra(ReminderConstant.REMINDERMSG, reminderMsg)
                mContext!!.startActivity(custom)
            }
        //喝水/吃药
            ReminderConstant.TYPE_DRINKING -> {

                val water = Intent(mContext, WaterAndMedicineActivity::class.java)
                water.putExtra(ReminderConstant.REMINDERMSG, reminderMsg)
                mContext!!.startActivity(water)

            }
        //节日提醒
            ReminderConstant.TYPE_FESTIVAL -> {
                val festival = Intent(mContext, FestivalRemindAlarmActivity::class.java)
                festival.putExtra(ReminderConstant.REMINDERMSG, reminderMsg)
                mContext!!.startActivity(festival)
            }


        }


    }

    override fun onOfflineLoaded() {
    }

    override fun onOfflineUnLoaded() {
    }


    override fun onEvent(name: String?, p1: String?, p2: ByteArray?, p3: Int, p4: Int) {

        when {
            name.equals(SpeechConstant.CALLBACK_EVENT_ASR_READY) -> // 引擎就绪，可以说话，一般在收到此事件后通过UI通知用户可以说话了
            {
                ImageUtil.setGif(imageView!!, mContext!!, R.drawable.dialog_tonality)
            }
            name.equals(SpeechConstant.CALLBACK_EVENT_ASR_FINISH) -> {
                // 识别结束
                println("识别结束++++++++++++++++++++++++")
                dialog!!.dismiss()
            }
            name.equals(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL) -> {
                val recogResult = RecogResult.parseJson(p1!!)
                // 临时识别结果, 长语音模式需要从此消息中取出结果
                val results = recogResult.getResultsRecognition()
                if (recogResult.isFinalResult()) {
                    this.onAsrFinalResult(results, recogResult)
                } else if (recogResult.isNluResult()) {
                    this.onAsrOnlineNluResult(String(p2!!, p3, p4))
                }
                println("=============最终===============" + p1)
            }

        // ... 支持的输出事件和事件支持的事件参数见“输入和输出参数”一节
        }
        // ... 支持的输出事件和事件支持的事件参数见“输入和输出参数”一节
    }


    fun initAll() {
        if (initFlag) {
            initBaiDuSDK()
            initDialog(mContext!!)
            sendMsg()
            initFlag = false
        } else {
            sendMsg()
        }

    }

    fun sendMsg() {
        // asr params(反馈请带上此行日志):{"accept-audio-data":false,"disable-punctuation":false,"accept-audio-volume":true,"pid":1736}
        // 其中{"accept-audio-data":false,"disable-punctuation":false,"accept-audio-volume":true,"pid":1736}为ASR_START 事件的参数
        if (!dialog!!.isShowing) {
            dialog!!.show()
        }
        val params = HashMap<String, Any>()
        params.put("accept-audio-data", false)
        params.put("accept-audio-volume", false)
        params.put("_nlu_online", true)
        val map = PidBuilder.create().model(PidBuilder.SEARCH).addPidInfo(params)
        val json = JSONObject(map).toString()
        eventManager!!.send(SpeechConstant.ASR_START, json, null, 0, 0)
    }

    var dialog: Dialog? = null
    var imageView: ImageView? = null
    var mContext: Context? = null
    var eventManager: EventManager? = null
    /**
     *
     * 语音初始化
     */
    private fun initBaiDuSDK() {
        eventManager = EventManagerFactory.create(mContext, "asr")
        eventManager!!.registerListener(this)
    }

    /**
     * 初始化语音设置弹窗提示界面
     */
    private fun initDialog(context: Context) {
        val inflate = LayoutInflater.from(context).inflate(R.layout.dialog_voice_reminder, null, false)
        dialog = Dialog(context, R.style.input_dialog)
        //返回键失效
        //dialog.setCancelable(false)
        dialog!!.setCanceledOnTouchOutside(false)
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        dialog!!.setContentView(inflate, layoutParams)
        imageView = inflate.findViewById(R.id.iv_dialog_voice)
        dialog!!.setOnDismissListener {
            Thread(Runnable {
                Glide.get(context).clearDiskCache()
            }).start()

        }

    }


    /**
     *
     * 监听注册
     */
    private fun initListener() {
//        btn_voiceStart.setOnClickListener(this)
    }


}