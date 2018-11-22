function getOne(list) {
    var rgb = new Array(3);
    var index = Math.floor(Math.random()*10);
    rgb[0] = list[index].r;
    rgb[1] = list[index].g;
    rgb[2] = list[index].b;
    return rgb;
}
function saveData(key, data) {
    var jData = JSON.stringify(data);
    localStorage.setItem(key, jData);
}

function getData(key) {
    var jData = localStorage.getItem(key);
    var data = JSON.parse(jData);
    return data;
}

function getRads (degrees) { return (Math.PI * degrees) / 180; }
var mycanvas = document.getElementById("mycanvas");
var ctx = mycanvas.getContext("2d");
//圆形类
function Circle(x,y,color){
    this.x = x;
    this.y = y;
    this.color = color;
    this.r1 = 1;
    this.r2 = 1;
    this.a1 = -0.1;
    this.a2 = -0.1;
    var ae = Math.floor(Math.random()*100)
    this.a1s = ae;
    this.a2s = ae+110;
    this.a3s = ae+260;
    this.a1e = ae;
    this.a2e = ae+110;
    this.a3e = ae+260;

    //往数组中push自己
    circleArr.push(this);
}

//渲染
Circle.prototype.render = function(){
    //新建一条路径
    ctx.beginPath();
    //创建一个圆
    ctx.arc(this.x, this.y, this.r1, 0, Math.PI*2, true);
    //设置样式颜色
    ctx.fillStyle = this.color;
    //通过填充路径的内容区域生成实心的图形
    ctx.fill();
    if (this.r1 >= 65) {
        ctx.beginPath();
        ctx.arc(this.x, this.y, this.r2, 0, Math.PI*2, true);
        ctx.fillStyle = "#f5f8f9";
        ctx.fill();
    }
    if (this.r1 >= 90) {
        ctx.lineWidth = 3;
        ctx.beginPath();
        ctx.strokeStyle = "#f5f8f9";
        ctx.arc(this.x, this.y, this.r1, getRads(this.a1s--), getRads(this.a1e++), false);
        ctx.stroke();

        ctx.lineWidth = 3;
        ctx.beginPath();
        ctx.strokeStyle = "#f5f8f9";
        ctx.arc(this.x, this.y, this.r1, getRads(this.a2s--), getRads(this.a2e++), false);
        ctx.stroke();

        ctx.lineWidth = 3;
        ctx.beginPath();
        ctx.strokeStyle = "#f5f8f9";
        ctx.arc(this.x, this.y, this.r1, getRads(this.a3s--), getRads(this.a3e++), false);
        ctx.stroke();
    }
}

//更新
Circle.prototype.update = function(){
    this.r1 += Math.abs(1/this.a1)*5;
    if (this.r1 >= 65) {
        this.r2 += Math.abs(1/this.a2)*5;
    }
    this.a1 -= 0.6;
    if(this.r1 >= 65) {
        this.a2 -= 0.6;
    }
    if(this.r1 >= 100){
        for (var i = 0; i < circleArr.length; i++) {
            if (circleArr[i] === this) {
                circleArr.splice(i,1);
            };
        }
        return false;
    }
    return true;
}
//创建一个数组
var circleArr = [];
//动态设置高度
var h = $('#articleInfo-list').height();
var w = $('#navBar').width();
console.log(w+" "+h)
setSize(h, w);
setTimeout(function () {
    h = $('#articleInfo-list').height();
    setSize(h+50, w);
}, 1000);

function setSize(height, width) {
    if (height < 500) {
        height = 500;
    }
    document.getElementById("mycanvas").height = height;
    document.getElementById("mycanvas").width = width;
}

//每300ms生成一个气泡
setInterval(function () {
    var width = mycanvas.width;
    var height = mycanvas.height;
    var x = Math.floor(Math.random()*width);
    var y = Math.floor(Math.random()*height);
    var list = getData("list");
    var rgb = getOne(list);
    var color = "rgb("+rgb[0]+","+rgb[1]+","+rgb[2]+")";
    new Circle(x,y,color);
}, 300)

//设置定时器每20毫秒更新和渲染
setInterval(function(){
    ctx.clearRect(0, 0, 1000, 600)
    for (var i = 0; i < circleArr.length; i++) {
        circleArr[i].update() ;
        circleArr[i].render();
    };
},20);