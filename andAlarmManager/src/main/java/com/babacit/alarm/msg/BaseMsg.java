package com.babacit.alarm.msg;

import org.json.JSONObject;

import com.babacit.alarm.interfaces.JsonInterface;

public class BaseMsg implements JsonInterface {

	@Override
	public JSONObject toJson(JSONObject json) {
		return json;
	}

	@Override
	public void parseJson(JSONObject json) {

	}

}
