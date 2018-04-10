package com.babacit.alarm.interfaces;

import org.json.JSONObject;

public interface JsonInterface {
	public JSONObject toJson(JSONObject json);

	public void parseJson(JSONObject json);

}
