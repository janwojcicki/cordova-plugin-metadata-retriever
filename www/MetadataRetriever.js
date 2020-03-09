var exec = require('cordova/exec');
var pluginName = 'MetadataRetriever';

function prepareArgs(args) {
	if (Array.isArray(args)) {
		return { paths: args };
	} else {
		return { paths: [args] };
	}
}

exports.getMetadata = function(success, error, args) {
	exec(success, error, pluginName, 'getMetadata', [prepareArgs(args)]);
};
exports.getAlbum = function(success, error, args) {
	exec(success, error, pluginName, 'getAlbum', [prepareArgs(args)]);
};
exports.getArtist = function(success, error, args) {
	exec(success, error, pluginName, 'getArtist', [prepareArgs(args)]);
};
exports.getDuration = function(success, error, args) {
	exec(success, error, pluginName, 'getDuration', [prepareArgs(args)]);
};
exports.getTitle = function(success, error, args) {
	exec(success, error, pluginName, 'getTitle', [prepareArgs(args)]);
};
