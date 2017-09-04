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
var model = mongoose.model('user4', user);
//module.exports = model;

var fe = mongoose.Schema({
    재해위험지역: String,
    재해위험유형: String,
    재해위험등급: String,
    추진사항: String,
    위험요인: String,
    침수위험지역지수: String
}, {collection: 'fe'});
var feModel = mongoose.model('fe', fe);
var llist = [];
/* GET home page. */
router.get('/', function (req, res, next) {

    model.find({}, function (err, data) {
        var a = [0.09, 0, 0.04, 0.07];
        var b = [0.25, 0.75, 0.25, 0.25];
        var c = ['없음', '없음', '없음', '없음'];
        var d = [0.21, 0.35, 0.19, 0.20];
        for (var i = 0; i < data.length; i++) {
            feModel.find({재해위험지역: {$regex: data[i].addressA}}, function (err, data2) {
                console.log(data2);
                llist.push(data2);
                console.log("asd", i);
            });
        }
        res.render('adminInfo', {data: data, a: a, b: b, c: c, d: d});
    });
});

module.exports = router;
