package com.babacit.alarm.swvideo;

public interface AGEventHandler {
	
    void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed);

    void onJoinChannelSuccess(String channel, int uid, int elapsed);

    void onUserOffline(int uid, int reason);
    
    void onError(int err);
    
}