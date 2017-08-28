var express = require('express');
var mongoose = require('mongoose');

var router = express.Router();
/*Make Restful Design : CRUD*/

//mongodb의 user컬렉션 형식과 똑같이 사용
var user = mongoose.Schema({
    name: String,
    password: String
}, {collection : 'user'});

//mongodb user컬렉션과 스키마 연결
var model = mongoose.model('user', user);
module.exports = model;

/*POST method : Create*/
router.post('/', function (req, res, next) {

});

/* GET method : Read*/
router.get('/', function (req, res, next) {
    var id = req.param('login_name');
    var pwd = req.param('login_password');

    model.find({name:id,password:pwd}, function(err, data) {
        if (err) throw err;
        //빈배열이면 패스워드 미스매칭이므로 로그인 실패, 빈배열이 아니면 로그인 성공
        if(data===[]){
            console.log('empty');
        }
        else{
            console.log(data);
        }

        res.send('/');
    });

});

/*PUT method : Update*/
router.put('/', function (req, res, next) {

});

/*DELETE method : Delete*/
router.delete('/', function (req, res, next) {

});

module.exports = router;
