var express = require('express');
var router = express.Router();
/*Make Restful Design : CRUD*/

/*POST method : Create*/
router.post('/', function (req, res, next) {

});

/* GET method : Read*/
router.get('/', function (req, res, next) {
    res.render('adminEJS');
});

/*PUT method : Update*/
router.put('/', function (req, res, next) {

});

/*DELETE method : Delete*/
router.delete('/', function (req, res, next) {

});

module.exports = router;
