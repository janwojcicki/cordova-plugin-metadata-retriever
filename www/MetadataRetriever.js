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
	exec(success, error, pluginName, 'getEssentialMetadata', [prepareArgs(args)]);
};
exports.getAllMetadata = function(success, error, args) {
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
exports.getAlbumArtist = function(success, error, args) {
	exec(success, error, pluginName, 'getAlbumArtist', [prepareArgs(args)]);
};
exports.getBitrate = function(success, error, args) {
	exec(success, error, pluginName, 'getBitrate', [prepareArgs(args)]);
};
exports.getCDTrackNumber = function(success, error, args) {
	exec(success, error, pluginName, 'getCDTrackNumber', [prepareArgs(args)]);
};
exports.getCompilation = function(success, error, args) {
	exec(success, error, pluginName, 'getCompilation', [prepareArgs(args)]);
};
exports.getComposer = function(success, error, args) {
	exec(success, error, pluginName, 'getComposer', [prepareArgs(args)]);
};
exports.getDate = function(success, error, args) {
	exec(success, error, pluginName, 'getDate', [prepareArgs(args)]);
};
exports.getDiscNumber = function(success, error, args) {
	exec(success, error, pluginName, 'getDiscNumber', [prepareArgs(args)]);
};
exports.getGenre = function(success, error, args) {
	exec(success, error, pluginName, 'getGenre', [prepareArgs(args)]);
};
exports.getNumTracks = function(success, error, args) {
	exec(success, error, pluginName, 'getNumTracks', [prepareArgs(args)]);
};
