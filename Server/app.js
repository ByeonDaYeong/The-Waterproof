var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var app = express();

//Controller 등록
var index = require('./routes/index');
var loginController = require('./routes/loginController');
var adminController = require('./routes/adminController');

//View 등록
var loginView = require('./routes/loginView');
var adminView = require('./routes/adminView');

//Test 등록
var users = require('./routes/users');
var noneParamTest = require('./routes/noneParamTest');

//사용할 뷰엔진을 선택함. 경로는 views파일, 사용할 엔진은 ejs
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

// uncomment after placing your favicon in /public
//app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: false}));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

//Controller 라우팅
app.use('/', index);
app.use('/login', loginController);
app.use('/admin', adminController);

//View 라우팅
app.use('/loginView' ,loginView);
app.use('/adminView' ,adminView);

//테스트용 라우팅 파라메터 없는 라우팅시 참조
app.use('/noneParamTest', noneParamTest);
app.use('/users', users);

// catch 404 and forward to error handler
app.use(function (req, res, next) {
    var err = new Error('Not Found');
    err.status = 404;
    next(err);
});

// error handler
app.use(function (err, req, res, next) {
    // set locals, only providing error in development
    res.locals.message = err.message;
    res.locals.error = req.app.get('env') === 'development' ? err : {};

    // render the error page
    res.status(err.status || 500);
    res.render('error');
});

module.exports = app;
