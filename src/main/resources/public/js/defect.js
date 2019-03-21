//按时间查询故障数据
function faultQuery(starttime, endtime) {
    $.ajax({
        url: "/statistics/faultquery",
        type: "post",
        data: {
            "starttime": starttime,
            "endtime": endtime
        },
        dataType: "json",
        success: function(data) {

            if (data.flag == "success") {

                var list = data.datas;


                // 初始化饼状图
                var myChart = echarts.init(document.getElementById('pieContent'));

                option = {
                    title: {
                        text: "外圆为各通道的故障数所占比例，内圆为故障详情所占比例。外圆和内圆可以点击切换",
                        left: "center", //标题位置
                        top: -5, //标题位置
                        textStyle: {
                            color: "gray",
                            fontSize: "13",
                            fontWeight: "normal"
                        }
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: "{a} <br/>{b}: {c} ({d}%)"
                    },
                    series: [{
                            name: '故障详情', //内圆名字
                            type: 'pie',
                            selectedMode: 'single', //点击弹出该区域
                            radius: ['0%', '45%'], //内圆大小

                            label: { //控制内圆上的字样式
                                normal: {
                                    position: 'inner'
                                }
                            },
                            labelLine: {
                                normal: {
                                    show: false
                                }
                            },
                            data: [] //内圆的数据
                        },
                        {
                            name: '通道故障占比', //外圆名字
                            type: 'pie',
                            selectedMode: 'single', //点击弹出该区域
                            radius: ['60%', '85%'], //外圆大小
                            label: { //控制外圆外标签样式
                                fontSize: 16
                            },
                            data: [] //外圆的数据
                        }
                    ]
                };



                // 初始化柱状图
                var mybarChart = echarts.init(document.getElementById('barContent'));

                barOption = {
                    title: [{
                            text: '',
                            left: 53, //标题位置
                            top: 3 //标题位置
                        },
                        {
                            text: "柱状图用于展示全部通道的故障次数，点击后可查看单一通道的故障详情",
                            left: "center", //标题位置
                            top: 10, //标题位置
                            textStyle: {
                                color: "gray",
                                fontSize: "13",
                                fontWeight: "normal"
                            }
                        },
                    ],
                    xAxis: {
                        name: "通道",
                        type: 'category',
                        data: [],
                        axisLabel: { //坐标轴刻度标签的相关设置。
                            interval: 0,
                            rotate: "30"
                        }
                    },
                    yAxis: {
                        name: "故障总数（次）",
                        type: 'value'
                    },
                    series: [{
                        data: [],
                        type: 'bar',
                        barWidth: 40, //柱子宽度
                        itemStyle: {
                            normal: {
                                barBorderRadius: [5, 5, 0, 0], //柱子圆弧角度
                                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ //柱子渐变色
                                    offset: 0,
                                    color: '#5EFCE8'
                                }, {
                                    offset: 1,
                                    color: '#736EFE'
                                }]),
                            }
                        }
                    }]
                };


                if (list.length == 0) {
                    $(".msgTips").css("display", "block")
                    option.series[0].data = []
                    option.series[1].data = []
                    myChart.setOption(option);
                    barOption.xAxis.data = []
                    barOption.series.data = []
                    mybarChart.setOption(barOption);
                } else {
                    $(".msgTips").css("display", "none")

                    /*------------------------------------------填充饼状图内外圆的数据（以下）------------------------------------------*/

                    //内圆的数据。本地通道没有mosaic，blackscreen，frozen，远端通道都有。
                    function fillPiedata() {
                        var blur = new Number(),
                            overdark = new Number(),
                            overbright = new Number(),
                            colorcast = new Number(),
                            lowcontrast = new Number(),
                            mosaic = new Number(),
                            blackscreen = new Number(),
                            frozen = new Number();


                        for (var i = 0; i < list.length; i++) {

                            //内圆的数据
                            blur += parseInt(list[i].overall[0].blur);
                            overdark += parseInt(list[i].overall[0].overdark);
                            overbright += parseInt(list[i].overall[0].overbright);
                            colorcast += parseInt(list[i].overall[0].colorcast);
                            lowcontrast += parseInt(list[i].overall[0].lowcontrast);
                            mosaic += parseInt(list[i].overall[0].mosaic);
                            blackscreen += parseInt(list[i].overall[0].blackscreen);
                            frozen += parseInt(list[i].overall[0].frozen);

                            //填充外圆数据
                            option.series[1].data.push({ value: list[i].counts, name: list[i].chainname })

                        }

                        //数据为0隐藏文字和指示线（外圆）
                        for (var j = 0; j < option.series[1].data.length; j++) {
                            if (option.series[1].data[j].value == 0) {
                                option.series[1].data[j].label.show = false
                                option.series[1].data[j].labelLine.show = false
                            }
                        }

                        //填充内圆数据
                        option.series[0].data = [
                            { value: blur, name: '画面模糊', label: { show: true }, labelLine: { show: true } },
                            { value: overdark, name: '亮度过暗', label: { show: true }, labelLine: { show: true } },
                            { value: overbright, name: '亮度过亮', label: { show: true }, labelLine: { show: true } },
                            { value: colorcast, name: '画面偏色', label: { show: true }, labelLine: { show: true } },
                            { value: lowcontrast, name: '对比度过低', label: { show: true }, labelLine: { show: true } },
                            { value: mosaic, name: '马赛克', label: { show: true }, labelLine: { show: true } },
                            { value: blackscreen, name: '黑屏/绿屏', label: { show: true }, labelLine: { show: true } },
                            { value: frozen, name: '停滞', label: { show: true }, labelLine: { show: true } }
                        ]
                        //数据为0隐藏文字和指示线（内圆）
                        for (var j = 0; j < option.series[0].data.length; j++) {
                            if (option.series[0].data[j].value == 0) {
                                option.series[0].data[j].label.show = false
                                option.series[0].data[j].labelLine.show = false
                            }
                        }
                    }

                    fillPiedata()



                    /*------------------------------------------填充柱状图数据（以下）------------------------------------------*/

                    for (var m = 0; m < list.length; m++) {
                        //填充x轴通道名
                        barOption.xAxis.data.push(list[m].chainname)
                        //填充y轴数值
                        barOption.series[0].data.push(list[m].counts)
                    }


                    /*------------------------------------------对饼状图点击事件进行处理（以下）------------------------------------------*/

                    //点击外圆，内圆展示对应详细数据，并且柱形图同步显示对应数据
                    function clicksector(msg) {

                        var currentIndex = msg.dataIndex,
                            currentName = msg.name;
                        if (msg.seriesIndex == 1 && msg.seriesType == "pie") { //如果是外圆  

                            //对饼状图数据进行处理
                            for (var i = 0; i < list.length; i++) {
                                //填充内圆数据
                                if (list[currentIndex].chaintype == "本地") {
                                    option.series[0].data = [
                                        { value: list[currentIndex].overall[0].blur, name: '画面模糊', label: { show: true }, labelLine: { show: true } },
                                        { value: list[currentIndex].overall[0].overdark, name: '亮度过暗', label: { show: true }, labelLine: { show: true } },
                                        { value: list[currentIndex].overall[0].overbright, name: '亮度过亮', label: { show: true }, labelLine: { show: true } },
                                        { value: list[currentIndex].overall[0].colorcast, name: '画面偏色', label: { show: true }, labelLine: { show: true } },
                                        { value: list[currentIndex].overall[0].lowcontrast, name: '对比度过低', label: { show: true }, labelLine: { show: true } }
                                    ]
                                    //数据为0隐藏文字和指示线（内圆）
                                    for (var j = 0; j < option.series[0].data.length; j++) {
                                        if (option.series[0].data[j].value == 0) {
                                            option.series[0].data[j].label.show = false
                                            option.series[0].data[j].labelLine.show = false
                                        }
                                    }
                                    myChart.setOption(option);
                                } else {
                                    option.series[0].data = [
                                        { value: list[currentIndex].overall[0].blur, name: '画面模糊', label: { show: true }, labelLine: { show: true } },
                                        { value: list[currentIndex].overall[0].overdark, name: '亮度过暗', label: { show: true }, labelLine: { show: true } },
                                        { value: list[currentIndex].overall[0].overbright, name: '亮度过亮', label: { show: true }, labelLine: { show: true } },
                                        { value: list[currentIndex].overall[0].colorcast, name: '画面偏色', label: { show: true }, labelLine: { show: true } },
                                        { value: list[currentIndex].overall[0].lowcontrast, name: '对比度过低', label: { show: true }, labelLine: { show: true } },
                                        { value: list[currentIndex].overall[0].mosaic, name: '马赛克', label: { show: true }, labelLine: { show: true } },
                                        { value: list[currentIndex].overall[0].blackscreen, name: '黑屏/绿屏', label: { show: true }, labelLine: { show: true } },
                                        { value: list[currentIndex].overall[0].frozen, name: '停滞', label: { show: true }, labelLine: { show: true } }
                                    ]
                                    //数据为0隐藏文字和指示线（内圆）
                                    for (var j = 0; j < option.series[0].data.length; j++) {
                                        if (option.series[0].data[j].value == 0) {
                                            option.series[0].data[j].label.show = false
                                            option.series[0].data[j].labelLine.show = false
                                        }
                                    }

                                    myChart.setOption(option);

                                }


                                //对柱状图数据进行处理
                                barOption.title[0].text = currentName;
                                barOption.xAxis.data = ["画面模糊", "亮度过暗", "亮度过亮", "画面偏色", "对比度过低", "马赛克", "黑屏/绿屏", "停滞"]
                                barOption.xAxis.name = "详情"
                                barOption.yAxis.name = "评分"


                                barOption.series[0].data = [list[currentIndex].overall[0].blur,
                                    list[currentIndex].overall[0].overdark,
                                    list[currentIndex].overall[0].overbright,
                                    list[currentIndex].overall[0].colorcast,
                                    list[currentIndex].overall[0].lowcontrast,
                                    list[currentIndex].overall[0].mosaic,
                                    list[currentIndex].overall[0].blackscreen,
                                    list[currentIndex].overall[0].frozen
                                ]


                                mybarChart.setOption(barOption);
                            }
                        }

                    }



                    /*------------------------------------------对柱状图点击事件进行处理（以下）------------------------------------------*/

                    //点击柱形图展示单条通道的详情评分，并且饼状图同步显示数据
                    function barclick(msg) {

                        var currentIndex = msg.dataIndex,
                            currentName = msg.name;

                        if (barOption.xAxis.name == "通道") { //如果是通道，点击则进入评分详情

                            //对饼状图数据进行处理
                            for (var i = 0; i < list.length; i++) {
                                //填充内圆数据
                                if (list[currentIndex].chaintype == "本地") {
                                    option.series[0].data = [
                                        { value: list[currentIndex].overall[0].blur, name: '画面模糊', label: { show: true }, labelLine: { show: true } },
                                        { value: list[currentIndex].overall[0].overdark, name: '亮度过暗', label: { show: true }, labelLine: { show: true } },
                                        { value: list[currentIndex].overall[0].overbright, name: '亮度过亮', label: { show: true }, labelLine: { show: true } },
                                        { value: list[currentIndex].overall[0].colorcast, name: '画面偏色', label: { show: true }, labelLine: { show: true } },
                                        { value: list[currentIndex].overall[0].lowcontrast, name: '对比度过低', label: { show: true }, labelLine: { show: true } }
                                    ]
                                    //数据为0隐藏文字和指示线（内圆）
                                    for (var j = 0; j < option.series[0].data.length; j++) {
                                        if (option.series[0].data[j].value == 0) {
                                            option.series[0].data[j].label.show = false
                                            option.series[0].data[j].labelLine.show = false
                                        }
                                    }
                                    myChart.setOption(option);
                                } else {
                                    option.series[0].data = [
                                        { value: list[currentIndex].overall[0].blur, name: '画面模糊', label: { show: true }, labelLine: { show: true } },
                                        { value: list[currentIndex].overall[0].overdark, name: '亮度过暗', label: { show: true }, labelLine: { show: true } },
                                        { value: list[currentIndex].overall[0].overbright, name: '亮度过亮', label: { show: true }, labelLine: { show: true } },
                                        { value: list[currentIndex].overall[0].colorcast, name: '画面偏色', label: { show: true }, labelLine: { show: true } },
                                        { value: list[currentIndex].overall[0].lowcontrast, name: '对比度过低', label: { show: true }, labelLine: { show: true } },
                                        { value: list[currentIndex].overall[0].mosaic, name: '马赛克', label: { show: true }, labelLine: { show: true } },
                                        { value: list[currentIndex].overall[0].blackscreen, name: '黑屏/绿屏', label: { show: true }, labelLine: { show: true } },
                                        { value: list[currentIndex].overall[0].frozen, name: '停滞', label: { show: true }, labelLine: { show: true } }
                                    ]
                                    //数据为0隐藏文字和指示线（内圆）
                                    for (var j = 0; j < option.series[0].data.length; j++) {
                                        if (option.series[0].data[j].value == 0) {
                                            option.series[0].data[j].label.show = false
                                            option.series[0].data[j].labelLine.show = false
                                        }
                                    }

                                    myChart.setOption(option);

                                }


                                //对柱状图数据进行处理
                                barOption.title[0].text = currentName;
                                barOption.xAxis.data = ["画面模糊", "亮度过暗", "亮度过亮", "画面偏色", "对比度过低", "马赛克", "黑屏/绿屏", "停滞"]
                                barOption.xAxis.name = "详情"
                                barOption.yAxis.name = "故障总数（次）"


                                barOption.series[0].data = [list[currentIndex].overall[0].blur,
                                    list[currentIndex].overall[0].overdark,
                                    list[currentIndex].overall[0].overbright,
                                    list[currentIndex].overall[0].colorcast,
                                    list[currentIndex].overall[0].lowcontrast,
                                    list[currentIndex].overall[0].mosaic,
                                    list[currentIndex].overall[0].blackscreen,
                                    list[currentIndex].overall[0].frozen
                                ]


                                mybarChart.setOption(barOption);
                            }
                        } else if (barOption.xAxis.name == "详情") { //如果是当前通道详情，点击则回到全部通道数据
                            barOption.title[0].text = " ";
                            barOption.xAxis.data = []
                            barOption.series[0].data = []
                            barOption.xAxis.name = "通道"
                            barOption.yAxis.name = "故障总数（次）"
                            for (var m = 0; m < list.length; m++) {
                                //填充x轴通道名
                                barOption.xAxis.data.push(list[m].chainname)
                                //填充y轴占比
                                barOption.series[0].data.push(list[m].counts)
                            }

                            mybarChart.setOption(barOption);
                            //还原饼状图数据
                            option.series[0].data = [];
                            option.series[1].data = [];
                            fillPiedata()
                            myChart.setOption(option);
                        }

                    }

                    myChart.on("click", clicksector);
                    //实例化饼状图图表
                    myChart.setOption(option);


                    mybarChart.on("click", barclick);
                    //实例化柱状图图表
                    mybarChart.setOption(barOption);


                }
            }


        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            console.log(XMLHttpRequest.status);
            console.log(XMLHttpRequest.readyState);
            console.log(textStatus);
        }
    })
}




//计算最近天数
function doHandleMonth(month) {
    var m = month;
    if (month.toString().length == 1) {
        m = "0" + month;
    }
    return m;
}
//传0得到当前日期，传其他天数的负值，得到几天前的日期。
function getDay(day) {
    var today = new Date();
    var targetday_milliseconds = today.getTime() + 1000 * 60 * 60 * 24 * day;
    today.setTime(targetday_milliseconds); //注意，这行是关键代码
    var tYear = today.getFullYear();
    var tMonth = today.getMonth();
    var tDate = today.getDate();
    tMonth = doHandleMonth(tMonth + 1);
    tDate = doHandleMonth(tDate);
    return tYear + "-" + tMonth + "-" + tDate;
}





$(function() {


    //加载时间选择器
    layui.use('laydate', function() {
        var laydate = layui.laydate;

        laydate.render({
            elem: '#startTime'
        });
        laydate.render({
            elem: '#endTime'
        });
    });


    //进入页面默认展示最近7天的数据
    var endtime = getDay(0); //当前日期
    var starttime = getDay(-7); //7天前日期
    faultQuery(starttime, endtime)
    $(".timeScope").text("最近7天")



    //点击按时间查询故障
    $(".chartQuery").on("click", function() {

        var timeSelect = $(".timeQuery").val();


        if (timeSelect == "最近7天") {
            var endtime = getDay(0); //当前日期
            var starttime = getDay(-7);
            faultQuery(starttime, endtime)
            $(".timeScope").text("最近7天")

        } else if (timeSelect == "最近30天") {

            var endtime = getDay(0); //当前日期
            var starttime = getDay(-30);
            faultQuery(starttime, endtime)
            $(".timeScope").text("最近30天")
        }

    })


    $(".queryMsg").on("click", function() {

        var starttime = $("#startTime").val();
        var endtime = $("#endTime").val();

        if (starttime == "") {
            alert("开始时间不能为空")
        } else if (endtime == "") {
            alert("结束时间不能为空")
        } else if (endtime < starttime) {
            alert("结束时间不能早于开始时间")
        } else {
            faultQuery(starttime, endtime)
            $(".timeScope").text("开始时间：" + starttime + "　" + "结束时间：" + endtime)
        }

    })

    //点击切换两张图
    $(".changeChart").on("click", function() {
        if ($(this).attr("alt") == "bar") {
            $(this).attr("alt","pie") 
            $(this).attr("title","切换柱状图")
            $("#pieContent").css("display", "block")
            $("#barContent").css("display", "none")
        } else if ($(this).attr("alt") == "pie") {
            $(this).attr("alt","bar") 
            $(this).attr("title","切换饼状图")
            $("#barContent").css("display", "block")
            $("#pieContent").css("display", "none")
        }
    })


    /*------------------------------------------动态样式部分------------------------------------------*/
    //点击查询按钮样式
    $(".chartQuery").on("mousedown", function() {
        $(this).css({
            "border": "1px solid #00aeef",
            "background": "#05b35d"
        })
    })
    $(".chartQuery").on("mouseup", function() {
        $(this).css({
            "border": "1px solid transparent",
            "background": "#00ce68"
        })
    })

    $(".queryMsg").on("mousedown", function() {
        $(this).css({
            "border": "1px solid #00aeef",
            "background": "#05b35d"
        })
    })
    $(".queryMsg").on("mouseup", function() {
        $(this).css({
            "border": "1px solid transparent",
            "background": "#00ce68"
        })
    })

    //下拉框样式
    $(".timeQuery").on("focus", function() {
        $(this).css({
            "border": "1px solid #00aeef",
        })
    })
    $(".timeQuery").on("blur", function() {
        $(this).css({
            "border": "1px solid #ccc",
        })
    })

    //点击时间框样式
    $("#startTime").on("focus", function() {
        $(this).css({
            "border": "1px solid #00aeef",
        })
    })
    $("#startTime").on("blur", function() {
        $(this).css({
            "border": "1px solid #ccc",
        })
    })
    $("#endTime").on("focus", function() {
        $(this).css({
            "border": "1px solid #00aeef",
        })
    })
    $("#endTime").on("blur", function() {
        $(this).css({
            "border": "1px solid #ccc",
        })
    })


})