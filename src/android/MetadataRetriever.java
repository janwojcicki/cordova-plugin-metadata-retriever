package org.apache.cordova.plugin;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;

import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;
import android.media.MediaMetadataRetriever;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONObject;
import java.util.Map;

public class MetadataRetriever extends CordovaPlugin {
	private static final String ERR_INVALID_OPTIONS = "ERR_INVALID_OPTIONS";
	private static final String ERR_NOT_INITIALIZED = "ERR_NOT_INITIALIZED";
	private final String TAG = MetadataRetriever.class.getSimpleName();
	private MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
	private Map<String, Integer> possibleQueries;

	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		possibleQueries.put("Album", MediaMetadataRetriever.METADATA_KEY_ALBUM);
		possibleQueries.put("Artist", MediaMetadataRetriever.METADATA_KEY_ARTIST);
		possibleQueries.put("Duration", MediaMetadataRetriever.METADATA_KEY_DURATION);
		possibleQueries.put("Title", MediaMetadataRetriever.METADATA_KEY_TITLE);
		Log.d(TAG, "metadata retriever initialized");
	}

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		JSONObject params = args.getJSONObject(0);

		if (params == null) {
			callbackContext.error(ERR_INVALID_OPTIONS);
			return false;
		}

		JSONArray filePaths = new JSONArray();

		if (params.isNull("path")) {
			if (params.isNull("paths")) {
				callbackContext.error(ERR_INVALID_OPTIONS);
				return false;
			}

			filePaths = params.getJSONArray("paths");

		} else {
			filePaths.put(params.getString("path"));
		}

		JSONArray returnObj = new JSONArray();

		for (int i = 0; i < filePaths.length(); i++) {
			mediaMetadataRetriever.setDataSource(filePaths.getString(i));

			JSONObject thisFileObject = new JSONObject();

			for (Map.Entry<String, Integer> entry : possibleQueries.entrySet()) {
				if (action.equals("get" + entry.getKey())) {
					addMetadataToJSON(entry.getKey(), entry.getValue(), thisFileObject);
				}
			}

			if (action.equals("getMetadata")) {
				for (Map.Entry<String, Integer> entry : possibleQueries.entrySet()) {
					addMetadataToJSON(entry.getKey(), entry.getValue(), thisFileObject);
				}
			}

			returnObj.put(thisFileObject);
		}

		final PluginResult result;
		if (returnObj.length() == 1) {
			result = new PluginResult(PluginResult.Status.OK, returnObj.getJSONObject(0));
		} else {
			result = new PluginResult(PluginResult.Status.OK, returnObj);
		}

		callbackContext.sendPluginResult(result);
		return true;
	}

	private JSONObject addMetadataToJSON(String key, int metadataID, JSONObject obj) throws JSONException {
		String meta = mediaMetadataRetriever.extractMetadata(metadataID);
		obj.put(key, meta);
		return obj;
	}
}
