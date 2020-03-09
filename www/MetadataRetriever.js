var exec = require('cordova/exec');
var pluginName = 'MetadataRetriever';

exports.getMetadata = function(success, error, filePath) {
	exec(success, error, pluginName, 'getMetadata', [filePath]);
};
