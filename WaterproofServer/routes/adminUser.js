var express = require('express');
var mongoose = require('mongoose');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
    var personName = req.query['insertName'];
    res.render('adminUser',{personName:personName});
});

module.exports = router;
