var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
const session = require('express-session');
var mongoose = require('mongoose');
var request = require('request');
var schedule = require('node-schedule');
var cron = require('node-cron');

//route controller
var index = require('./routes/index');
var users = require('./routes/users');
var login = require('./routes/login');
var signUp = require('./routes/signUp');
var signUpView = require('./routes/signUpView');
var userInfo = require('./routes/userInfo');
var userAdd = require('./routes/userAdd');
var adminInfo = require('./routes/adminInfo');
var adminUser = require('./routes/adminUser');

var app = express();

//session 설정
app.use(session({
    secret: 'keyboard cat',
    resave: false,
    saveUninitialized: false,
    cookie: {secure: false}
}));

//mongodb 사용
mongoose.connect('mongodb://localhost:27017/test');
var db = mongoose.connection;
db.on('error', console.error.bind(console, 'connection error:'));
db.once('open', function callback() {
    console.log("mongo db connection OK.");
});

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

// uncomment after placing your favicon in /public
//app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: false}));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

//match url
app.use('/', index);
app.use('/users', users);
app.use('/login', login);
app.use('/signUp', signUp);
app.use('/signUpView', signUpView);
app.use('/userInfo', userInfo);
app.use('/userAdd', userAdd);
app.use('/adminInfo', adminInfo);
app.use('/adminUser', adminUser);

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

var port = 8801;
var io = require('socket.io').listen(port);  // 8801 포트로 소켓을 엽니다.
var warning_num = 500;
var emergency_num = 700;

console.log('server running at ' + port + ' port');

var contact = mongoose.Schema({
    name: String,
    phoneNumber: String,
    owner: String
}, {collection: 'contact'});

var model = mongoose.model('contact', contact);

io.sockets.on('connection', function (socket) { // connection이 발생할 때 핸들러를 실행합니다.
    io.sockets.emit('conn', 'connected');
    console.log('connect');
    socket.on('message', function (message) {
        console.log(message);
        io.sockets.emit('message', message);  //client에 message 라는 키로 값을 전송합니다.
    });
    socket.on('rasp', function (message) {
        var inWater = message['inWater'];
        var outWater = message['outWater'];
        io.sockets.emit('aaa',{data:'data'});
        if (inWater >= emergency_num || outWater >= emergency_num) {
            io.sockets.emit('androidWarn', {"data": 1});
            model.find({}, function (err, data) {
                console.log(data);
                io.sockets.emit('androidPhone', data);
            });
            console.log('State===> Level is Emergency');
        }
        else if (inWater >= warning_num || outWater >= warning_num) {
            io.sockets.emit('androidWarn', {"data": 0});
            io.sockets.emit('led', {"data": 'off'});
            model.find({}, function (err, data) {
                console.log(data);
                io.sockets.emit('androidPhone', data);
            });
            console.log('State===> Level is Warning');
        }

    })
    socket.on('GPS', function (message) {
        console.log(message);
        //console.log("--------takeRainfall : " + getDateStamp() +getTimeStamp()+ "--------");
        //takeRainfall(getDateStamp(), getTimeStamp(),message.latitude,message.longtitude);
        //takeFlood(getDateStamp(), getTimeStamp());
    })
    socket.on('aaa',function (message) {
        console.log('test');
    })
});

///////////////////////////////////////////////////////////////////////////////
function dataSample(){
    var standard = null; //측정시간(요청보낸시간)
    var rainfall = {}; //예상강우량

    this.getStandard = function(){
        return standard;
    }
    this.setStandard = function(s){
        this.standard = s;
    }
    this.addData = function(key, val){
        this.rainfall[key] = val;
    }
    this.getRainfall = function(){
        return rainfall;
    }
    this.removeData = function(key){
        return rainfall.delete(key);
    }
}

function takeRainfall(d, t , x, y){
    var base_date = d;
    var base_time = t;
    var data = dfs_xy_conv("toXY", x, y);
    var nx = data.x;
    var ny = data.y;
    var url = "http://newsky2.kma.go.kr/service/SecndSrtpdFrcstInfoService2/ForecastGrib?" +
        "serviceKey=mWOUP6hFibrsdKm56wULHkl93YWqbqfALbjYOD9XH%2F1ASgmGqBlXVo5YZIpfA5P5DgSlFTaggM2zrYBUWiHQug%3D%3D" +
        "&base_date=" +base_date+
        "&base_time=" +base_time+
        "&nx=" +nx+
        "&ny=" +ny+
        "&numOfRows=10" +
        "&pageSize=10" +
        "&pageNo=1" +
        "&startPage=1" +
        "&_type=json";

    console.log("-------------");
    request(url, function(error, response_, resData) {
        if (!error && response_.statusCode == 200) {
            //console.log(resData);
            //console.log("-------------");

            var resCopy = JSON.parse(resData);
            //console.log("resCopy : " + resCopy);
            //console.log("----------------");

            var temp = resCopy.response.body.items.item;
            //console.log("temp : " + temp);
            //console.log("-------------");

            var idx =0;
            var rainData = new dataSample();
            rainData.setStandard(base_time);
            for(var i=0; i<resCopy.response.body.items.item.length; i++){
                var tmp = temp[i];
                if(tmp.category == "RN1"){
                    var t = rainData.getRainfall;
                    t[tmp.fcstTime] = tmp.fcstValue;
                }
            }
            console.log("-------------");
            console.log(rainData.getRainfall);
            //console.log(rainData.getRainfall['0400']);
        }
    })

}//function takeRainfall end

function takeFlood(d, t){
    var userRiver = [ "삽교천", "한강", "낙동강칠백리" ]; //더미데이터

    //var sampleTime = "201707161600"; //해커톤 진행 20170901기준 홍수주의보가 일어난 가장 가까운날
    var sampleTime = d.toString() + t.toString();
    var floodUrl = "http://api.hrfco.go.kr/7541AEE7-EC2F-4F61-8B13-AF63274A7352/fldfct/list/";

    floodUrl = floodUrl + sampleTime;
    floodUrl = floodUrl + ".json";

    request(floodUrl, function(error, response_, resData) {
        if (!error && response_.statusCode == 200) {

            //console.log(resData);
            //console.log("-------------");

            var resCopy = JSON.parse(resData);
            //console.log("resCopy : " + resCopy);
            //console.log("--------------");

            var temp = resCopy.content;
            //console.log("temp : " + temp);
            //console.log("--------------");

            if(temp != undefined){
                for(var i=0; i<temp.length; i++){
                    var tmp = temp[i];
                    for(var j=0; j<userRiver.length; j++){
                        if(tmp.rvrnm == userRiver[j]){
                            console.log(tmp.rvrnm + " : "+ tmp.kind + " : " + tmp.ancdt);
                        }
                    }
                }
            }
            else console.log("its null");
        }
    })

}

function getTimeStamp() {
    var d = new Date();

    var s =
        leadingZeros(d.getHours(), 2)+
        leadingZeros(d.getMinutes(), 2);


    return s;
}
function getDateStamp(){
    var d = new Date();

    var s =
        leadingZeros(d.getFullYear(), 4)+
        leadingZeros(d.getMonth()+1, 2)+
        leadingZeros(d.getDate(), 2);
    return s;
}
function leadingZeros(n, digits) {
    var zero = '';
    n = n.toString();

    if (n.length < digits) {
        for (i = 0; i < digits - n.length; i++)
            zero += '0';
    }
    return zero + n;
}

///////////////////////////////////////////////////
var RE = 6371.00877; // 지구 반경(km)
var GRID = 5.0; // 격자 간격(km)
var SLAT1 = 30.0; // 투영 위도1(degree)
var SLAT2 = 60.0; // 투영 위도2(degree)
var OLON = 126.0; // 기준점 경도(degree)
var OLAT = 38.0; // 기준점 위도(degree)
var XO = 43; // 기준점 X좌표(GRID)
var YO = 136; // 기1준점 Y좌표(GRID)
//
// LCC DFS 좌표변환 ( code : "toXY"(위경도->좌표, v1:위도, v2:경도), "toLL"(좌표->위경도,v1:x, v2:y) )
//


function dfs_xy_conv(code, v1, v2) {
    var DEGRAD = Math.PI / 180.0;
    var RADDEG = 180.0 / Math.PI;

    var re = RE / GRID;
    var slat1 = SLAT1 * DEGRAD;
    var slat2 = SLAT2 * DEGRAD;
    var olon = OLON * DEGRAD;
    var olat = OLAT * DEGRAD;

    var sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
    sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
    var sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
    sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
    var ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
    ro = re * sf / Math.pow(ro, sn);
    var rs = {};
    if (code == "toXY") {
        rs['lat'] = v1;
        rs['lng'] = v2;
        var ra = Math.tan(Math.PI * 0.25 + (v1) * DEGRAD * 0.5);
        ra = re * sf / Math.pow(ra, sn);
        var theta = v2 * DEGRAD - olon;
        if (theta > Math.PI) theta -= 2.0 * Math.PI;
        if (theta < -Math.PI) theta += 2.0 * Math.PI;
        theta *= sn;
        rs['x'] = Math.floor(ra * Math.sin(theta) + XO + 0.5);
        rs['y'] = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);
    }
    else {
        rs['x'] = v1;
        rs['y'] = v2;
        var xn = v1 - XO;
        var yn = ro - v2 + YO;
        ra = Math.sqrt(xn * xn + yn * yn);
        if (sn < 0.0) - ra;
        var alat = Math.pow((re * sf / ra), (1.0 / sn));
        alat = 2.0 * Math.atan(alat) - Math.PI * 0.5;

        if (Math.abs(xn) <= 0.0) {
            theta = 0.0;
        }
        else {
            if (Math.abs(yn) <= 0.0) {
                theta = Math.PI * 0.5;
                if (xn < 0.0) - theta;
            }
            else theta = Math.atan2(xn, yn);
        }
        var alon = theta / sn + olon;
        rs['lat'] = alat * RADDEG;
        rs['lng'] = alon * RADDEG;
    }
    return rs;
}