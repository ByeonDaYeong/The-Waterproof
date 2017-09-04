var express = require('express');
var mongoose = require('mongoose');
var router = express.Router();

//mongoose 모델과 스키마 설정
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

var model = mongoose.model('user2', user);
module.exports = model;

/* GET home page. */
router.post('/', function (req, res, next) {
    console.log(req.body);
    /*var name = req.query['name'];
    var password = req.query['password'];
    var phoneNumber = req.query['phoneNumber'];
    var disabledType = req.query['disabledType'];
    var disabledRank = req.query['disabledRank'];
    var addressA = req.query['addressA'];
    var addressB = req.query['addressB'];
    var personName = req.query['personName'];
    var gender = req.query['gender'];
    var disabledNumber = req.query['disabledNumber'];*/
    model.find({name: req.body.name}, function (err, data) {
        console.log('State===> data : '+data);
        if(data.length==0){
            var obj = {
                "name": req.body.name,
                "password": req.body.password,
                "phoneNumber": req.body.phoneNumber,
                "disabledType": req.body.disabledType,
                "disabledRank": req.body.disabledRank,
                "addressA": req.body.addressA,
                "addressB": req.body.addressB,
                "personName": req.body.personName,
                "gender": req.body.gender,
                "disabledNumber": req.body.disabledNumber
            };
            var newUser = new model(obj);
            newUser.save(function (err) {
                if (err) console.log(err);
            })
        }
        else{
            console.log('State===> User is already exist.');
        }
    })
    res.redirect('/');
});

module.exports = router;
