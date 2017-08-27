var express = require('express');
var router = express.Router();
/*Make Restful Design : CRUD*/

/*POST method : Create*/
router.post('/', function (req, res, next) {
    //res.render('login');
});

/* GET method : Read*/
router.get('/', function (req, res, next) {
    res.send('/loginView');
});

/*PUT method : Update*/
router.put('/', function (req, res, next) {
    //res.render('login');
});

/*DELETE method : Delete*/
router.delete('/', function (req, res, next) {
    //res.send('/noneParam');
});

module.exports = router;
