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
import java.util.HashMap;
import java.util.List;

import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONObject;
import java.util.Map;

public class MetadataRetriever extends CordovaPlugin {
	private static final String ERR_INVALID_OPTIONS = "ERR_INVALID_OPTIONS";
	private static final String ERR_WHEN_RETRIEVING = "ERR_WHEN_RETRIEVING";
	private final String TAG = MetadataRetriever.class.getSimpleName();
	private MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
	private Map<String, Integer> essentialQueries = new HashMap<>();
	private Map<String, Integer> allQueries = new HashMap<>();

	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		essentialQueries.put("Album", MediaMetadataRetriever.METADATA_KEY_ALBUM);
		essentialQueries.put("Artist", MediaMetadataRetriever.METADATA_KEY_ARTIST);
		essentialQueries.put("Duration", MediaMetadataRetriever.METADATA_KEY_DURATION);
		essentialQueries.put("Title", MediaMetadataRetriever.METADATA_KEY_TITLE);

		allQueries.put("Album", MediaMetadataRetriever.METADATA_KEY_ALBUM);
		allQueries.put("Artist", MediaMetadataRetriever.METADATA_KEY_ARTIST);
		allQueries.put("Duration", MediaMetadataRetriever.METADATA_KEY_DURATION);
		allQueries.put("Title", MediaMetadataRetriever.METADATA_KEY_TITLE);
		allQueries.put("AlbumArtist", MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST);
		allQueries.put("Bitrate", MediaMetadataRetriever.METADATA_KEY_BITRATE);
		allQueries.put("CDTrackNumber", MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER);
		allQueries.put("Compilation", MediaMetadataRetriever.METADATA_KEY_COMPILATION);
		allQueries.put("Composer", MediaMetadataRetriever.METADATA_KEY_COMPOSER);
		allQueries.put("Date", MediaMetadataRetriever.METADATA_KEY_DATE);
		allQueries.put("DiscNumber", MediaMetadataRetriever.METADATA_KEY_DISC_NUMBER);
		allQueries.put("Genre", MediaMetadataRetriever.METADATA_KEY_GENRE);
		allQueries.put("NumTracks", MediaMetadataRetriever.METADATA_KEY_NUM_TRACKS);
		Log.d(TAG, "metadata retriever initialized");
	}

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		JSONObject params = args.getJSONObject(0);

		if (params == null) {
			callbackContext.error(ERR_INVALID_OPTIONS);
			return false;
		}

		if (params.isNull("paths")) {
			callbackContext.error(ERR_INVALID_OPTIONS);
			return false;
		}
		final JSONArray filePaths = params.getJSONArray("paths");

		cordova.getThreadPool().execute(new Runnable() {

			private JSONObject addMetadataToJSON(String key, int metadataID, JSONObject obj) {
				String meta = mediaMetadataRetriever.extractMetadata(metadataID);
				try {
					obj.put(key, meta);
				} catch (JSONException e) {
					callbackContext.error(ERR_INVALID_OPTIONS);
				}
				return obj;
			}

			@Override
			public void run() {
				JSONArray returnObj = new JSONArray();
				for (int i = 0; i < filePaths.length(); i++) {
					try {
						mediaMetadataRetriever.setDataSource(filePaths.getString(i));
					} catch (JSONException e) {
						callbackContext.error(ERR_INVALID_OPTIONS);
					}

					JSONObject thisFileObject = new JSONObject();

					if (action.equals("getEssentialMetadata")) {
						for (Map.Entry<String, Integer> entry : essentialQueries.entrySet()) {
							addMetadataToJSON(entry.getKey(), entry.getValue(), thisFileObject);
						}
						returnObj.put(thisFileObject);
						continue;
					}

					if (action.equals("getMetadata")) {
						for (Map.Entry<String, Integer> entry : allQueries.entrySet()) {
							addMetadataToJSON(entry.getKey(), entry.getValue(), thisFileObject);
						}
						returnObj.put(thisFileObject);
						continue;
					}

					for (Map.Entry<String, Integer> entry : allQueries.entrySet()) {
						if (action.equals("get" + entry.getKey())) {
							addMetadataToJSON(entry.getKey(), entry.getValue(), thisFileObject);
						}
					}

					returnObj.put(thisFileObject);
				}

				PluginResult result = new PluginResult(Status.ERROR);
				if (returnObj.length() == 1) {
					try {
						result = new PluginResult(PluginResult.Status.OK, returnObj.getJSONObject(0));
					} catch (JSONException e) {
						callbackContext.error(ERR_WHEN_RETRIEVING);
					}
				} else {
					result = new PluginResult(PluginResult.Status.OK, returnObj);
				}

				callbackContext.sendPluginResult(result);
			}
		});
		return true;
	}
}
