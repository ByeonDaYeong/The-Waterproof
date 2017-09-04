var express = require('express');
var mongoose = require('mongoose');
var router = express.Router();

var user = mongoose.Schema({
    name: String,
    password: String,
    phoneNumber: String,
    disabledType: String,
    disabledRank: String,
    addressA: String,
    addressB: String,
    personName: String,
    gender: String,
    disabledNumber: Number
}, {collection: 'user'});
var model = mongoose.model('user3', user);
module.exports = model;

/* GET home page. */
router.get('/', function (req, res, next) {
    const session = req.session;
    if(session.isLogin==true){
        model.find({name:req.session.name}, function(err, data) {
            console.log("userInfo : "+data[0]);
            res.render('userInfo',data[0]);
        });

    }
    else{
        res.redirect('/');
    }
});

module.exports = router;
