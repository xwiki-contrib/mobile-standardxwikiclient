/*
 * cordova is available under *either* the terms of the modified BSD license *or* the
 * MIT License (2008). See http://opensource.org/licenses/alphabetical for full text.
 *
 * Copyright (c) 2005-2010, Nitobi Software Inc.
 * Copyright (c) 2011, IBM Corporation
 */

/**
 * Constructor
 */
function ChildBrowser() {
	this.loaded = false;
	this.sidemenu = false;
};

ChildBrowser.CLOSE_EVENT = 0;
ChildBrowser.LOCATION_CHANGED_EVENT = 1;

/**
 * Display a new browser with the specified URL. This method loads up a new web
 * view in a dialog.
 * 
 * @param url
 *            The url to load
 * @param options
 *            An object that specifies additional options
 */
ChildBrowser.prototype.showWebPage = function(url, options) {
	// alert("url : "+url);
	options = options || {
		showLocationBar : true,
		locationBarAlign : "top"
	};
	cordova.exec(this._onEvent, this._onError, "ChildBrowser", "showWebPage", [
			url, options ]);
};

/**
 * Display a new browser with the specified URL. This method loads up a new web
 * view in a dialog.
 * 
 * @param url
 *            The url to load
 * @param options
 *            An object that specifies additional options
 */
ChildBrowser.prototype.showHTML = function(url, options) {
	options = options || {
		showLocationBar : true,
		locationBarAlign : "top"
	};
	cordova.exec(this._onEvent, this._onError, "ChildBrowser", "showWebPage", [
			url, options ]);
};

// Make room for the side menu to show
ChildBrowser.prototype.showSideMenu = function(html) {
	cordova.exec(null, null, "ChildBrowser", "showSideMenu", []);
};

// Hide side menu area
ChildBrowser.prototype.hideSideMenu = function(html) {
	cordova.exec(null, null, "ChildBrowser", "hideSideMenu", []);
};

// Make room/Hide side menu for the side menu to show
ChildBrowser.prototype.toggleSideMenu = function(force) {
	if (force == undefined) {
		force = !this.sidemenu;
	}

	if (force) {
		this.showSideMenu();
		this.sidemenu = true;
	} else {
		this.hideSideMenu();
		this.sidemenu = false;
	}
};

/**
 * Close the browser opened by showWebPage.
 */
ChildBrowser.prototype.close = function() {
	this.loaded = false;
	cordova.exec(null, null, "ChildBrowser", "close", []);
};

/**
 * Display a new browser with the specified URL. This method starts a new web
 * browser activity.
 * 
 * @param url
 *            The url to load
 * @param usecordova
 *            Load url in cordova webview [optional]
 */
ChildBrowser.prototype.openExternal = function(url, usecordova) {
	if (usecordova === true) {
		navigator.app.loadUrl(url);
	} else {
		cordova.exec(null, null, "ChildBrowser", "openExternal", [ url,
				usecordova ]);
	}
};

/**
 * Method called when the child browser has an event.
 */
ChildBrowser.prototype._onEvent = function(data) {
	if (data.type == ChildBrowser.CLOSE_EVENT
			&& typeof window.plugins.childBrowser.onClose === "function") {
		window.plugins.childBrowser.onClose();
	}
	if (data.type == ChildBrowser.LOCATION_CHANGED_EVENT
			&& typeof window.plugins.childBrowser.onLocationChange === "function") {
		window.plugins.childBrowser.onLocationChange(data.location);
	}
};

/**
 * Method called when the child browser has an error.
 */
ChildBrowser.prototype._onError = function(data) {
	if (typeof window.plugins.childBrowser.onError === "function") {
		window.plugins.childBrowser.onError(data);
	}
};

/**
 * Maintain API consistency with iOS
 */
ChildBrowser.prototype.install = function() {
};

ChildBrowser.prototype.setPageContent = function(header, html) {
	try {
		var content = "<html><body><div id='xwikipageheader'>" + header
				+ "</div>";

		content += "<div id='xwikipagecontent'>" + html
				+ "</div></body></html>";

		if (!(header.length == 0)) {
			this.showHTML(content);
		}

	} catch (err) {
		console.log("Exception in pagebrowser setPageContent: " + err);
	}
};

/**
 * Load ChildBrowser
 */

if (!window.plugins) {
	window.plugins = {};
}
if (!window.plugins.childBrowser) {
	window.plugins.childBrowser = new ChildBrowser();
}
