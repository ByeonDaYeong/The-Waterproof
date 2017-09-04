var express = require('express');
var mongoose = require('mongoose');
var router = express.Router();


//mongoose 모델과 스키마 설정
var user = mongoose.Schema({
    name: String,
    password: String,
    phoneNumber: String,
    disabledType: String,
    addressA: String,
    addressB: String,
    personName: String,
    gender: String,
    disabledNumber: Number
}, {collection: 'user'});

var model = mongoose.model('user', user);
module.exports = model;

/* GET home page. */
router.get('/', function (req, res, next) {
    const session = req.session;

    //id와 password를 프론트로 부터 받아온다
    var name = req.query['login_name'];
    var pwd = req.query['login_password'];
    console.log('State===> Insert login : '+name+', password : ' + pwd);

    model.find({name:name,password:pwd}, function(err, data) {
        if (err) throw err;
        //빈배열이면 패스워드 미스매칭이므로 로그인 실패, 빈배열이 아니면 로그인 성공
        console.log('State===> data : '+data);
        if(data.length==0){
            console.log('State===> Name or password is empty ');
        }
        else{
            console.log('state===> Name and password match - ' + data);
            //session이 존재한다면 error, 존재하지 않는다면 isLogin을 등록한다
            if (session.isLogin == true) {
                console.log('State===> Already Login');
            } else {
                session.name = name;
                session.isLogin = true;
                console.log('State===> Login Success');
                //결과 출력
            }
        }
        res.redirect('userInfo');
    });
});

router.post('/', function (req, res, next) {
    const session = req.session;
    //id와 password를 프론트로 부터 받아온다
    var name = req.body['login_name'];
    var pwd = req.body['login_password'];

    console.log('State===> Insert login : '+name+', password : ' + pwd);

    model.find({name:name,password:pwd}, function(err, data) {
        if (err) throw err;
        //빈배열이면 패스워드 미스매칭이므로 로그인 실패, 빈배열이 아니면 로그인 성공
        console.log('State===> data : '+data);
        if(data.length==0){
            console.log('State===> Name or password is empty ');
        }
        else{
            console.log('state===> Name and password match - ' + data);
            //session이 존재한다면 error, 존재하지 않는다면 isLogin을 등록한다
            if (session.isLogin == true) {
                console.log('State===> Already Login');
            } else {
                session.name = name;
                session.isLogin = true;
                console.log('State===> Login Success');
                //결과 출력
            }
        }
        res.redirect('userInfo');
    });
});

module.exports = router;