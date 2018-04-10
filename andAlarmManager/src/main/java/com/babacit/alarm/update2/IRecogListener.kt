package com.example.hekd.kotlinapp.ai

/**
 * Created by hekd on 2017/12/12.
 */
interface IRecogListener {
    /**
     * ASR_START 输入事件调用后，引擎准备完毕
     */
    abstract fun onAsrReady()

    /**
     * onAsrReady后检查到用户开始说话
     */
    abstract fun onAsrBegin()

    /**
     * 检查到用户开始说话停止，或者ASR_STOP 输入事件调用后，
     */
    abstract fun onAsrEnd()

    /**
     * onAsrBegin 后 随着用户的说话，返回的临时结果
     *
     * @param results 可能返回多个结果，请取第一个结果
     * @param recogResult 完整的结果
     */
    abstract fun onAsrPartialResult(results: Array<String>, recogResult: RecogResult)

    /**
     * 最终的识别结果
     *
     * @param results 可能返回多个结果，请取第一个结果
     * @param recogResult 完整的结果
     */
    abstract fun onAsrFinalResult(results: Array<String?>?, recogResult: RecogResult)

    abstract fun onAsrFinish(recogResult: RecogResult)

    abstract fun onAsrFinishError(errorCode: Int, subErrorCode: Int, errorMessage: String, descMessage: String, recogResult: RecogResult)

    /**
     * 长语音识别结束
     */
    abstract fun onAsrLongFinish()

    abstract fun onAsrVolume(volumePercent: Int, volume: Int)

    abstract fun onAsrAudio(data: ByteArray, offset: Int, length: Int)

    abstract fun onAsrExit()

    abstract fun onAsrOnlineNluResult(nluResult: String)

    abstract fun onOfflineLoaded()

    abstract fun onOfflineUnLoaded()
}