var MongoClient = require('mongodb').MongoClient;
var ObjectID = require('mongodb').ObjectID;
var jwt = require('jsonwebtoken');

var sUrl = 'mongodb://localhost:27017/homie';
function getColName(sEntityName) {
	return sEntityName;
}

function checkValidity(sEntityName, oObject) {
	return true;
}

function convertHexToObjectID(sHex) {	
	try {
		return ObjectID.createFromHexString(sHex);
	} catch (err) {
		return null;
	}
}

var handleLoginRequest = function(oUser, secretKey, responseStream) {
		MongoClient.connect(sUrl, function(err, db) {
		var col = db.collection("users");
		col.findOne({name: oUser.name}, function(err, user) {
			if (err) 
				throw err;
		    if (!user || user.password != oUser.password) {
		      responseStream.json({ success: false, message: 'Authentication failed. Incorrect username or password.' });
		    } else {

		        // create a token
		        var token = jwt.sign(user, secretKey, {
		          expiresIn: "24h" // expires in 24 hours
		        });		    
		        responseStream.json({
		          success: true,
		          message: 'Enjoy your token!',
		          token: token
		        });
	      	}
	      	db.close();
	  	});
	});
};

var handleSignUpRequest = function(oUser, secretKey, responseStream) {
	MongoClient.connect(sUrl, function(err, db) {	
		var col = db.collection("users");
		if (checkValidity("users", oUser)) {
			oUser.groupIds = [];
			oUser.assignmentIds = [];
			col.insertOne(oUser, function(err, result) {

				 // create a token
		        var token = jwt.sign(oUser, secretKey, {
		          expiresIn: "24h" // expires in 24 hours
		        });		    
		        responseStream.json({
		          success: true,
		          message: 'Enjoy your token!',
		          token: token
		        });
			    db.close();
			});
		}	
	});
};

var handleGetByIdRequest = function(sEntityName, sId, responseStream) {
	var colName = getColName(sEntityName);
	MongoClient.connect(sUrl, function(err, db) {
		var col = db.collection(colName);
		col.findOne({_id: convertHexToObjectID(sId)}, function(err, item) {
			responseStream.json(item);
		    db.close();
	  	});
	});
};

var handleGetRequest = function(sEntityName, req, responseStream) {
	var colName = getColName(sEntityName);
	var query = req.query;
	MongoClient.connect(sUrl, function(err, db) {
		var col = db.collection(colName);
		if (Object.getOwnPropertyNames(query).length === 0) {
			query.owner = req.user._id.toString();
		}
		col.find(query).toArray(function(err, items) {
			responseStream.json(items);
		    db.close();
	  	});
	});
};


var handlePostRequest = function(sEntityName, req, responseStream) {
	var oObject = req.body;
	var colName = getColName(sEntityName);
	MongoClient.connect(sUrl, function(err, db) {	
		var col = db.collection(colName);
		if (checkValidity(colName, oObject)) {
			oObject.owner = req.user._id.toString();
			col.insertOne(oObject, function(err, result) {
				responseStream.send(oObject._id.toString());
				console.log(oObject._id);
			    db.close();
			});
		}	
	});
};

var handlePutRequest = function(sEntityName, req, responseStream) {
	var oFieldsToUpdate = req.body,
		colName = getColName(sEntityName);
		sObjectId = req.params.id;
	MongoClient.connect(sUrl, function(err, db) {	
		var col = db.collection(colName);
		if (checkValidity(colName, oFieldsToUpdate)) {
			col.update({_id: convertHexToObjectID(sObjectId)}, {
				$set: oFieldsToUpdate,
				$currentDate: { "lastModified": true }
			},	function(err, result) {
				var message = result.result.nModified > 0 
					? "Object was modified successfully" 
					: "Object modification failed - object ID does not exist";
				var status = result.result.nModified > 0 ? 200 : 400;
				responseStream.status(status).send(message);
				console.log(status + ": " + message);
			    db.close();
			});
		}	
	});
};

var handleDeleteRequest = function(sEntityName, req, responseStream) {
	var colName = getColName(sEntityName),
		sObjectId = req.params.id,
		sOwnerId = req.user._id.toString();
	MongoClient.connect(sUrl, function(err, db) {	
		var col = db.collection(colName);
		col.remove({_id: convertHexToObjectID(sObjectId), owner: sOwnerId},	function(err, result) {
			var message = result.result.n > 0 
				? "Object was deleted successfully" 
				: "Object deletion failed - object ID does not exist";
			var status = result.result.n > 0 ? 200 : 400;
			responseStream.status(status).send(message);
			console.log(status + ": " + message);
		    db.close();
		});		
	});
};

exports.handleGetByIdRequest = handleGetByIdRequest;
exports.handleGetRequest = handleGetRequest;
exports.handlePostRequest = handlePostRequest;
exports.handlePutRequest = handlePutRequest;
exports.handleDeleteRequest = handleDeleteRequest;
exports.handleLoginRequest = handleLoginRequest;
exports.handleSignUpRequest = handleSignUpRequest;

