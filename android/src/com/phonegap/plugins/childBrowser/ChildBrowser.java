/*
 * PhoneGap is available under *either* the terms of the modified BSD license *or* the
 * MIT License (2008). See http://opensource.org/licenses/alphabetical for full text.
 *
 * Copyright (c) 2005-2011, Nitobi Software Inc.
 * Copyright (c) 2010-2011, IBM Corporation
 */
package com.phonegap.plugins.childBrowser;

import java.io.IOException;
import java.io.InputStream;

import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.bool;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ChildBrowser extends Plugin {

	protected static final String LOG_TAG = "ChildBrowser";
	private static int CLOSE_EVENT = 0;
	private static int LOCATION_CHANGED_EVENT = 1;

	private String browserCallbackId = null;

	private Dialog dialog;
	private Dialog dialog2;
	private WebView webview;
	private WebView webview2;
	private EditText edittext;
	private boolean showLocationBar = true;
	private WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
	private String html;

	/**
	 * Executes the request and returns PluginResult.
	 * 
	 * @param action
	 *            The action to execute.
	 * @param args
	 *            JSONArry of arguments for the plugin.
	 * @param callbackId
	 *            The callback id used when calling back into JavaScript.
	 * @return A PluginResult object with a status and message.
	 */
	public PluginResult execute(String action, JSONArray args, String callbackId) {
		PluginResult.Status status = PluginResult.Status.OK;
		String result = "";

		try {
			if (action.equals("showWebPage")) {
				this.browserCallbackId = callbackId;

				// If the ChildBrowser is already open then throw an error
				if (dialog != null && dialog.isShowing()) {
					return new PluginResult(PluginResult.Status.ERROR,
							"ChildBrowser is already open");
				}
				html = args.getString(0);
				result = this.showWebPage(html, true);

				if (result.length() > 0) {
					status = PluginResult.Status.ERROR;
					return new PluginResult(status, result);
				} else {
					PluginResult pluginResult = new PluginResult(status, result);
					pluginResult.setKeepCallback(true);
					return pluginResult;
				}
			} else if (action.equals("close")) {
				closeDialog();

				JSONObject obj = new JSONObject();
				obj.put("type", CLOSE_EVENT);

				PluginResult pluginResult = new PluginResult(status, obj);
				pluginResult.setKeepCallback(false);
				return pluginResult;
			} else if (action.equals("showSideMenu")) {
				showSideMenu();

				JSONObject obj = new JSONObject();
				obj.put("type", CLOSE_EVENT);

				PluginResult pluginResult = new PluginResult(status, obj);
				pluginResult.setKeepCallback(false);
				return pluginResult;
			} else if (action.equals("hideSideMenu")) {
				hideSideMenu();

				JSONObject obj = new JSONObject();
				obj.put("type", CLOSE_EVENT);

				PluginResult pluginResult = new PluginResult(status, obj);
				pluginResult.setKeepCallback(false);
				return pluginResult;
			} else if (action.equals("openExternal")) {
				result = this.openExternal(args.getString(0),
						args.optBoolean(1));
				if (result.length() > 0) {
					status = PluginResult.Status.ERROR;
				}
			} else {
				status = PluginResult.Status.INVALID_ACTION;
			}
			return new PluginResult(status, result);
		} catch (JSONException e) {
			return new PluginResult(PluginResult.Status.JSON_EXCEPTION);
		}
	}

	/**
	 * Display a new browser with the specified URL.
	 * 
	 * @param url
	 *            The url to load.
	 * @param usePhoneGap
	 *            Load url in PhoneGap webview
	 * @return "" if ok, or error message.
	 */
	public String openExternal(String url, boolean usePhoneGap) {
		try {
			Intent intent = null;
			if (usePhoneGap) {
				intent = new Intent().setClass(this.cordova.getActivity(),
						org.apache.cordova.DroidGap.class);
				intent.setData(Uri.parse(url)); // This line will be removed in
												// future.
				intent.putExtra("url", url);

				// Timeout parameter: 60 sec max - May be less if http device
				// timeout is less.
				intent.putExtra("loadUrlTimeoutValue", 60000);

				// These parameters can be configured if you want to show the
				// loading dialog
				intent.putExtra("loadingDialog", "Wait,Loading web page..."); // show
																				// loading
																				// dialog
				intent.putExtra("hideLoadingDialogOnPageLoad", true); // hide it
																		// once
																		// page
																		// has
																		// completely
																		// loaded
			} else {
				intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(url));
			}
			this.cordova.getActivity().startActivity(intent);
			return "";
		} catch (android.content.ActivityNotFoundException e) {
			Log.d(LOG_TAG,
					"ChildBrowser: Error loading url " + url + ":"
							+ e.toString());
			return e.toString();
		}
	}

	/**
	 * Closes the dialog
	 */
	private void closeDialog() {
		if (dialog2 != null) {
			dialog2.dismiss();
		}
	}

	/**
	 * Resize the child browser to show side menu
	 */
	private void showSideMenu() {
		dialog2.dismiss();
		// updateWindowSize(false);
		String result = "";
		result = this.showWebPage(html, false);
	}

	/**
	 * Resize the child browser to hide side menu
	 */
	private void hideSideMenu() {
		dialog2.dismiss();
		// updateWindowSize(true);
		String result = "";
		result = this.showWebPage(html, true);
	}

	/*
     * 
     */
	private void displayBrowser(boolean get) {

		// here I have created new dialog windiw
		dialog2 = new Dialog(cordova.getActivity(),
				android.R.style.Theme_NoTitleBar);
		dialog2.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
		dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog2.setCancelable(true);

		// from this code segment it creates models less dialog window
		Window window2 = dialog2.getWindow();
		window2.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
		window2.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		window2.setGravity(Gravity.BOTTOM | Gravity.RIGHT);

		// creates a new webview to display HTML and attached to dialog to display
		webview2 = new WebView(cordova.getContext());
		webview2.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		webview2.setWebChromeClient(new WebChromeClient());
		// WebViewClient client = new ChildBrowserClient(edittext);
		// webview.setWebViewClient(client);
		WebSettings settings2 = webview2.getSettings();
		settings2.setJavaScriptEnabled(true);
		settings2.setJavaScriptCanOpenWindowsAutomatically(true);
		settings2.setBuiltInZoomControls(true);
		settings2.setPluginsEnabled(true);
		settings2.setDomStorageEnabled(true);
		// webview.loadUrl(url);
		webview2.setId(6);
		webview2.getSettings().setLoadWithOverviewMode(true);
		webview2.getSettings().setUseWideViewPort(true);
		webview2.requestFocus();
		webview2.requestFocusFromTouch();

		final String mimeType2 = "text/html";
		final String encoding2 = "UTF-8";
		webview2.loadDataWithBaseURL("", html, mimeType2, encoding2, "");
		dialog2.setContentView(webview2);

		// get the screen size of the device
		WindowManager wm = (WindowManager) ctx
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;

		// set appropriate sizes to the dialog window whether there is side menu or not 
		lp2.copyFrom(dialog2.getWindow().getAttributes());
		lp2.width = (get) ? width : width - 300;
		lp2.height = height - 110;
		dialog2.getWindow().setAttributes(lp2);

		dialog2.show();
	}

	/**
	 * Checks to see if it is possible to go back one page in history, then does
	 * so.
	 */
	private void goBack() {
		if (this.webview.canGoBack()) {
			this.webview.goBack();
		}
	}

	/**
	 * Checks to see if it is possible to go forward one page in history, then
	 * does so.
	 */
	private void goForward() {
		if (this.webview.canGoForward()) {
			this.webview.goForward();
		}
	}

	/**
	 * Navigate to the new page
	 * 
	 * @param url
	 *            to load
	 */
	private void navigate(String url) {
		InputMethodManager imm = (InputMethodManager) this.cordova
				.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edittext.getWindowToken(), 0);

		if (!url.startsWith("http") && !url.startsWith("file:")) {
			this.webview.loadUrl("http://" + url);
		} else {
			this.webview.loadUrl(url);
		}
		this.webview.requestFocus();
	}

	/**
	 * Should we show the location bar?
	 * 
	 * @return boolean
	 */
	private boolean getShowLocationBar() {
		return this.showLocationBar;
	}

	/**
	 * Display a new browser with the specified URL.
	 * 
	 * @param url
	 *            The url to load.
	 * @param jsonObject
	 */
	public String showWebPage(final String url, boolean get) {

		final Boolean resize = get;

		// Create dialog in new thread
		Runnable runnable = new Runnable() {
			/**
			 * Convert our DIP units to Pixels
			 * 
			 * @return int
			 */
			private int dpToPixels(int dipValue) {
				int value = (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, (float) dipValue, cordova
								.getActivity().getResources()
								.getDisplayMetrics());

				return value;
			}

			public void run() {

				displayBrowser(resize);

			}

			private Bitmap loadDrawable(String filename)
					throws java.io.IOException {
				InputStream input = cordova.getActivity().getAssets()
						.open(filename);
				return BitmapFactory.decodeStream(input);
			}
		};
		this.cordova.getActivity().runOnUiThread(runnable);
		return "";
	}

	/**
	 * Create a new plugin result and send it back to JavaScript
	 * 
	 * @param obj
	 *            a JSONObject contain event payload information
	 */
	private void sendUpdate(JSONObject obj, boolean keepCallback) {
		if (this.browserCallbackId != null) {
			PluginResult result = new PluginResult(PluginResult.Status.OK, obj);
			result.setKeepCallback(keepCallback);
			this.success(result, this.browserCallbackId);
		}
	}

	/**
	 * The webview client receives notifications about appView
	 */
	public class ChildBrowserClient extends WebViewClient {
		EditText edittext;

		/**
		 * Constructor.
		 * 
		 * @param mContext
		 * @param edittext
		 */
		public ChildBrowserClient(EditText mEditText) {
			this.edittext = mEditText;
		}

		/**
		 * Notify the host application that a page has started loading.
		 * 
		 * @param view
		 *            The webview initiating the callback.
		 * @param url
		 *            The url of the page.
		 */
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			String newloc;
			if (url.startsWith("http:") || url.startsWith("https:")
					|| url.startsWith("file:")) {
				newloc = url;
			} else {
				newloc = "http://" + url;
			}

			if (!newloc.equals(edittext.getText().toString())) {
				edittext.setText(newloc);
			}

			try {
				JSONObject obj = new JSONObject();
				obj.put("type", LOCATION_CHANGED_EVENT);
				obj.put("location", url);

				sendUpdate(obj, true);
			} catch (JSONException e) {
				Log.d("ChildBrowser", "This should never happen");
			}
		}
	}
}
