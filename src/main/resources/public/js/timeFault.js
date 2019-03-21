function faultQuery(starttime, endtime) {
    $.ajax({
        url: "/statistics/timequery",
        type: "post",
        data: {
            "starttime": starttime,
            "endtime": endtime
        },
        dataType: "json",
        success: function(data) {

            var list = data.datas;

            if (data.flag == "success") {
            	// 初始化echarts
                var myChart = echarts.init(document.getElementById('content'));
                
            	 var option = {
                 		title: {
                             text: "用于展示全部通道的故障次数",
                             left: "center", //标题位置
                             top: 5, //标题位置
                             textStyle: {
                                 color: "gray",
                                 fontSize: "13",
                                 fontWeight: "normal"
                             }
                         },
                     tooltip: {
                         trigger: 'axis'
                     },
                     legend: {
                         data: [],
                         orient: 'horizontal',
                         x: 1150,
                         y: 43,
                         selected:{}
                     },
                     toolbox: { //工具栏
                         show: true,
                         feature: {
                             dataZoom: {
                                 yAxisIndex: 'none'
                             },
                             dataView: { readOnly: false },
                             magicType: { type: ['line', 'bar'] },
                             restore: {},
                             saveAsImage: {}
                         }
                     },
                     xAxis: { //横坐标
                         type: 'category',
                         name: "日期",
                         boundaryGap: false,
                         data: [] //日期
                     },
                     yAxis: { //纵坐标
                         type: 'value',
                         name: "故障次数",
                         axisLabel: {
                             formatter: '{value} 次'
                         }
                     },
                     series: [] //每一条通道的数据
                 };
            	 
            	 

                if (list.length == 0) {
                    $(".msgTips").css("display", "block")
                    option.xAxis.data = []
                    option.series=[]
                    myChart.setOption(option);
                } else {
                    $(".msgTips").css("display", "none")
                                      

                    //计算开始时间和结束时间中的日期
                    Date.prototype.format = function() {

                        var currentMonth = this.getMonth() + 1
                        if (currentMonth >= 10) {
                            var s = '';
                            s += this.getFullYear() + '-'; // 获取年份。
                            s += (this.getMonth() + 1) + "-"; // 获取月份。
                            s += this.getDate(); // 获取日。
                            return (s); // 返回日期。
                        } else {
                            var s = '';
                            s += this.getFullYear() + '-0'; // 获取年份。
                            s += (this.getMonth() + 1) + "-"; // 获取月份。
                            s += this.getDate(); // 获取日。
                            return (s); // 返回日期。
                        }

                    };


                    var timeLine = new Array(); //开始时间和结束时间中的日期
                    var ab = starttime.split("-");
                    var ae = endtime.split("-");
                    var db = new Date();
                    db.setUTCFullYear(ab[0], ab[1] - 1, ab[2]);
                    var de = new Date();
                    de.setUTCFullYear(ae[0], ae[1] - 1, ae[2]);
                    var unixDb = db.getTime();
                    var unixDe = de.getTime();
                    for (var k = unixDb; k <= unixDe;) {
                        timeLine.push((new Date(parseInt(k))).format().toString());
                        k = k + 24 * 60 * 60 * 1000;
                    }

                    //将日期线填充到图表X轴
                    option.xAxis.data = timeLine



                    //向图表填充通道的故障数据  
                    for (var i = 0; i < list.length; i++) {


                        var currentDates = list[i].dates.split(","); //所有有故障的日期
                        var currentTotals = list[i].totals.split(","); //所有故障数
                        var getIndex = []; //存放故障日期在日期线中的对应的索引。
                        var faultDatas = []; //存放处理后的故障数据。


                        //顶部图示
                        option.legend.data.push(list[i].chainname)

                        //首先获取有故障的日期，在本地查询日期的数组中所对应的索引
                        for (var t = 0; t < currentDates.length; t++) {

                            for (var j = 0; j < timeLine.length; j++) {
                                if (currentDates[t] == timeLine[j]) {
                                    getIndex.push(j)
                                }
                            }
                        }


                        //然后将故障数添加到存放故障的数组的对应位置
                        faultDatas.length = timeLine.length
                        for (var k = 0; k < getIndex.length; k++) {
                            faultDatas[getIndex[k]] = currentTotals[k]
                        }


                        //最后将为空的索引数据变为0
                        for (var z = 0; z < faultDatas.length; z++) {
                            if (faultDatas[z] == undefined) {
                                faultDatas[z] = 0
                            }
                        }


                        option.series.push({
                            name: list[i].chainname,
                            type: 'line',
                            data: faultDatas
                        })


                    }

                    //默认只有第一条通道选中
                    var lineLegend=option.legend.data
                    for(var i=0;i<lineLegend.length;i++){
                    	 option.legend.selected[lineLegend[0]]=true
                 	   option.legend.selected[lineLegend[i]]=false
                    }
                    myChart.setOption(option);

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
    var starttime = getDay(-7);//7天前日期
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




    /*------------------------------------------动态样式部分------------------------------------------*/
    //点击查询按钮样式
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