var express = require('express');
var mongoose = require('mongoose');
var router = express.Router();

var contact = mongoose.Schema({
    name: String,
    phoneNumber:String,
    owner: String
}, {collection: 'contact'});

var model = mongoose.model('contact2', contact);

/* GET users listing. */
router.get('/', function(req, res, next) {
    const session = req.session;
    if(session.isLogin==true){
        model.find({},function (err, data) {
            console.log(data);
            res.render('userAdd',{data:data});
        });
    }
    else{
        res.redirect('/');
    }
});

module.exports = router;