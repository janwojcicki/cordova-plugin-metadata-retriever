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

import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;

public class MetadataRetriever extends CordovaPlugin {
	private final String TAG = MetadataRetriever.class.getSimpleName();

	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		Log.d(TAG, "metadata retriever initialized");
	}

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		if (action.equals("getMetadata")) {
			MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
			mediaMetadataRetriever.setDataSource(args.getJSONObject(0).getString("path"));

			String duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
			final PluginResult result = new PluginResult(PluginResult.Status.OK, duration);
			callbackContext.sendPluginResult(result);
		}
	}
}
