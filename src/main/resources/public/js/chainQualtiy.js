//查询当前会场
function currentConference() {
    $.ajax({
        url: "/chainstate/getChainInfo",
        type: "post",
        data: {},
        dataType: "json",
        success: function(data) {
            if (data.flag == "success") {
                $(".currentConference").text("")
                var list = data.infos;
                for (var i = 0; i < list.length; i++) {
                    $(".currentConference").attr("alt", list[i].conferenceid)
                    $(".currentConference").text(list[i].zonename + "　" + list[i].conferencename + "　通道检测")
                }


                //查询当前工作模式(该方法中根据当前工作模式，调用通道列表信息的方法。)
                queryMode(4)

            } else if (data.flag == "failure") {
                layer.msg('会场查询失败', {
                    offset: "3.8%",
                });
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            console.log(XMLHttpRequest.status);
            console.log(XMLHttpRequest.readyState);
            console.log(textStatus);
        }
    })
}



//展示当前会场通道列表，detect_type传1代表自动试机试线检测，传2代表实时视频质量评测。
function getChains(pagenum, conferenceid, detecttype) {
    $.ajax({
        url: '/chainstate/getChainstates',
        data: {
            "pagenum": pagenum,
            "conferenceid": conferenceid,
            "detect_type": detecttype
        },
        type: "post",
        dataType: "json",
        success: function(data) {
            if (data.flag == "success") {
                $("tbody").html(" ")
                var list = data.Data;
                var count = data.count; //总条数

                //表格
                for (var i = 0; i < list.length; i++) {

                    var num = i + (pagenum - 1) * 20 + 1; //序号
                    var currentChainstate=list[i].chainstate

                   
                    $("tbody").append(
                        `<tr id="${list[i].chainid}" alt="${list[i].chainstateid}">  
                        <td>${num}</td>   
                        <td >${list[i].chainname}</td> 
                        <td class="currentChainType">${list[i].chaintype}</td>     
                        <td ><i class="fa fa-circle singalLight"></i></td> 
                        <td >${data.time[i]}</td> 
                        <td><i class="fa fa-edit view" data-toggle="modal" data-target="#viewChainModal" alt="${i}" title="查看"></i>
                        </td>
                        </tr>
                     `
                    )

                     if(currentChainstate=="严重故障"){
                        $("#"+list[i].chainid+" .singalLight").css("color","#a90808")
                    }else if(currentChainstate=="故障"){
                       $("#"+list[i].chainid+" .singalLight").css("color","#ff0505")
                    }else if(currentChainstate=="异常"){
                        $("#"+list[i].chainid+" .singalLight").css("color","#ffa500")
                     }else if(currentChainstate=="正常"){
                        $("#"+list[i].chainid+" .singalLight").css("color","#008000")
                     }

                }
                //分页
                layui.use(['laypage'], function() {

                    var laypage = layui.laypage;
                    // 渲染分页
                    laypage.render({
                        elem: 'layPage' // 分页容器的id
                            ,
                        layout: ['prev', 'page', 'next', 'limits',
                                'count'
                            ] // 排版
                            ,
                        limit: 20 // 每页显示数
                            ,
                        count: count // 总条数
                            ,
                        curr: pagenum // 当前页
                            ,
                        groups: 3 // 连续出现的页数
                            ,
                        theme: '#1E9FFF' // 自定义选中色值
                            ,
                        skip: true // 开启跳页
                            ,
                        jump: function(obj, first) { // 点击页码跳页
                            if (!first) {
                                $('tbody').html("");
                                var conferenceid = $(".currentConference").attr("alt")
                                //获取当前工作模式
                                var currentMode = $(".selectMode").val();
                                //查询当页数据
                                if (currentMode == "自动试机试线检测") {
                                    getChains(obj.curr, conferenceid, 1)
                                } else if (currentMode == "实时视频质量评测") {
                                    getChains(obj.curr, conferenceid, 2)
                                }
                            }
                            //存储上次浏览页
                            sessionStorage.setItem("detection", obj.curr)
                        }
                    });
                })

            } else {
                layer.msg('查询失败', {
                    offset: "3.8%",
                });
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            console.log(XMLHttpRequest.status);
            console.log(XMLHttpRequest.readyState);
            console.log(textStatus);
        }
    })
}


//查询当前正在运行的是什么工作模式
function queryMode(controltype) {
    $.ajax({
        url: "/chainstate/control",
        type: "post",
        data: {
            "controltype": controltype
        },
        dataType: "json",
        success: function(data) {
            if (data.flag == "success") {
                if (data.result == "1") {
                    $(".selectMode").find("option:contains('自动试机试线检测')").attr("selected", true);
                    $(".nowState").text("已启动")

                    //自动试机试线检测：调用自动查询通道的方法
                    // var detection = sessionStorage.getItem("detection"); //上次浏览页
                    // var conferenceid = $(".currentConference").attr("alt");
                    // if (detection) {
                    //     getChains(detection, conferenceid, 1)
                    // } else {
                    //     getChains(1, conferenceid, 1)
                    // }

                    queryModeMsg = setInterval(function() {
                        var conferenceid = $(".currentConference").attr("alt")
                        var detection = sessionStorage.getItem("detection"); //上次浏览页
                        if (detection) {
                            getChains(detection, conferenceid, 1)
                        } else {
                            getChains(1, conferenceid, 1)
                        }
                    }, 1000)

                } else if (data.result == "2") {
                    $(".selectMode").find("option:contains('实时视频质量评测')").attr("selected", true);
                    $(".nowState").text("已启动")

                    //实时视频质量评测：调用实时查询通道的方法
                    // var detection = sessionStorage.getItem("detection"); //上次浏览页
                    // var conferenceid = $(".currentConference").attr("alt")
                    // if (detection) {
                    //     getChains(detection, conferenceid, 2)
                    // } else {
                    //     getChains(1, conferenceid, 2)
                    // }


                    queryModeMsg = setInterval(function() {
                        var conferenceid = $(".currentConference").attr("alt")
                        var detection = sessionStorage.getItem("detection"); //上次浏览页
                        if (detection) {
                            getChains(detection, conferenceid, 2)
                        } else {
                            getChains(1, conferenceid, 2)
                        }

                    }, 1000)


                } else if (data.result == "3") {
                    $(".nowState").text("待机")
                } else if (data.result == "-1") {
                    layer.msg('查询当前工作模式失败', {
                        offset: "3.8%",
                    });
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

//开启和暂停当前工作模式
function startAndPauseMode(controltype) {
    $.ajax({
        url: "/chainstate/control",
        type: "post",
        data: {
            "controltype": controltype
        },
        dataType: "json",
        success: function(data) {
            if (data.flag == "success") {
                if (controltype == "3") {
                    $(".nowState").text("待机")
                } else {
                    $(".nowState").text("已启动")
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



/*查看当前通道的详细信息(弹窗)*/
function detailChain(pagenum, conferenceid, currentNum, detecttype, currentChainType) {
    $.ajax({
        url: '/chainstate/getChainstates',
        data: {
            "pagenum": pagenum,
            "conferenceid": conferenceid,
            "detect_type": detecttype
        },
        type: "post",
        dataType: "json",
        success: function(data) {
            if (data.flag == "success") {
                $(".modalContent").html(" ")
                var list = data.Data;
                var count = data.count; //总条数

                //表格
                for (var i = 0; i < list.length; i++) {

                    if (i == currentNum) {

                        $(".modalContent").append(
                            `
                    <div class="layui-row">
                        <div class="layui-col-md4">
                            <div class="layui-card">
                                <div class="layui-card-header">本地视频状态：<span>${list[currentNum].localvideostate}</span></div>
                                <div class="layui-card-body">
                                    <table class="table table-bordered text-center table-condensed">
                                        <thead>
                                            <tr>
                                                <th>项目</td>
                                                <th>评分</td>
                                            </tr>
                                            <tr>
                                                <td>画面模糊</td>
                                                <td>${list[currentNum].localvideojson.blur}</td>
                                            </tr>
                                            <tr>
                                                <td>亮度过暗</td>
                                                <td>${list[currentNum].localvideojson.overdark}</td>
                                            </tr>
                                            <tr>
                                                <td>亮度过亮</td>
                                                <td>${list[currentNum].localvideojson.overbright}</td>
                                            </tr>
                                            <tr>
                                                <td>画面偏色</td>
                                                <td>${list[currentNum].localvideojson.colorcast}</td>
                                            </tr>
                                            <tr>
                                                <td>对比度过低</td>
                                                <td>${list[currentNum].localvideojson.lowcontrast}</td>
                                            </tr>
                                            <tr class="psnr">
                                                <td>峰值信噪比</td>
                                                <td>${list[currentNum].localvideojson.psnr}</td>
                                            </tr>
                                        </thead>
                                    </table>
                                </div>
                            </div>
                        </div>
                        
                        <div class="layui-col-md4">
                            <div class="layui-card">
                                <div class="layui-card-header">网络状态信息：<span>${list[currentNum].networkstate}</span></div>
                                <div class="layui-card-body">
                                    <table class="table table-bordered text-center table-condensed">
                                        <thead>
                                            <tr>
                                                <th>状态</th><th>错误</th><th>原因</th>
                                            </tr>
                                            <tr>
                                                <td>${list[currentNum].networkstatejson.state}</td>
                                                <td>${list[currentNum].networkstatejson.fault}</td>
                                                <td>${list[currentNum].networkstatejson.reason}</td>
                                            </tr>
                                        </thead>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="layui-row">
                        <div class="layui-col-md12">
                         <span class="errorAdvice">故障维修建议：</span>  <span>${list[currentNum].suggestion}</span>
                        </div>
                    </div>
                        `
                        )
                    }
                }

                var currentWorkMode = $(".selectMode").val()

                if (currentWorkMode=="实时视频质量评测" && currentChainType == "本地") {
                    $(".psnr").css("display", "none")
                    $(".distantVideoState").css("display","none")
                }else if(currentWorkMode=="实时视频质量评测" && currentChainType == "远端"){
                    $(".psnr").css("display", "none")
                }
            } else {
                $(".modalContent").append(`
                        <p>获取信息失败</p>
                    `)
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            console.log(XMLHttpRequest.status);
            console.log(XMLHttpRequest.readyState);
            console.log(textStatus);
        }
    })
}


/*视频源播放播放（如果是“自动试机试线检测”，启动工作模式前，需要设置一个默认的视频源,视频源设置成功，才能启动当前工作模式）*/
function videoPlay(format, signal) {
    $.ajax({
        url: "/videosource/play",
        data: {
            "time": format,
            "pattern": signal
        },
        type: "post",
        dataType: "json",
        success: function(data) {
            if (data.flag == "success") {
                //开始自动试机试线检测
                startAndPauseMode(1)
                window.location.reload();
            } else {
                layer.msg('标准视频源未设置成功', {
                    offset: "3.8%",
                });
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            console.log(XMLHttpRequest.status);
            console.log(XMLHttpRequest.readyState);
            console.log(textStatus);
        }
    })
}


$(function() {

    //加载提示框
    layui.use('layer', function() {
        layer = layui.layer;
    });


    //查询当前会场
    currentConference()




    //展示当前通道详细信息（弹窗）
    $('.content').delegate(".view", "click", function() {
        //获取当前信息在本页所在条数
        currentNum = $(this).attr("alt")
        //获取当前链的类型
        currentChainType = $(this).parents("tr").children(".currentChainType").text()
    })

    $(".modal").on('shown.bs.modal', function() {

        var detection = sessionStorage.getItem("detection"); //上次浏览页
        var conferenceid = $(".currentConference").attr("alt");
        var currentMode = $(".selectMode").val();

        if (currentMode == "自动试机试线检测") {
            if (detection) {
                detailChain(detection, conferenceid, currentNum, 1, currentChainType)

            } else {
                detailChain(1, conferenceid, currentNum, 1, currentChainType)

            }
        } else if (currentMode == "实时视频质量评测") {
            if (detection) {
                detailChain(detection, conferenceid, currentNum, 2, currentChainType)

            } else {
                detailChain(1, conferenceid, currentNum, 2, currentChainType)

            }
        }

    })


    //开始和暂停当前模式

    // 传给后端的参数所代表含义：
    // 1: 自动试机试线检测启动
    // 2: 实时视频质量评测启动
    // 3: 停止工作模式

    //开始当前模式
    $(".startMode").unbind("click").on("click", function() {

        /*视频源播放播放
        （如果是“自动试机试线检测”，启动工作模式前，需要设置一个默认的视频源，视频源设置成功，才能启动当前工作模式。启动方法在该方法内调用）*/
        var currentWorkMode = $(".selectMode").val()
        if (currentWorkMode == "自动试机试线检测") {
            videoPlay(3, 1)
        } else {
            //如果是“实时视频质量评测”，直接开启实时工作模式
            //开始实时视频质量评测
            startAndPauseMode(2)
            window.location.reload();
        }
    })


    //定时 调用自动查询通道的方法
    // var currentMode = $(".selectMode").val(),
    //     nowState = $(".nowState").text();

    // if (currentMode == "自动试机试线检测" && nowState == "已启动") {
    //     queryModeMsg = setInterval(function() {
    //         var conferenceid = $(".currentConference").attr("alt")
    //         var detection = sessionStorage.getItem("detection"); //上次浏览页
    //         if (detection) {
    //             getChains(detection, conferenceid, 1)
    //         } else {
    //             getChains(1, conferenceid, 1)
    //         }

    //     }, 1000)
    // } else if (currentMode == "实时视频质量评测" && nowState == "已启动") {
    //     queryModeMsg = setInterval(function() {
    //         var conferenceid = $(".currentConference").attr("alt")
    //         var detection = sessionStorage.getItem("detection"); //上次浏览页
    //         if (detection) {
    //             getChains(detection, conferenceid, 2)
    //         } else {
    //             getChains(1, conferenceid, 2)
    //         }

    //     }, 1000)
    // }




    //暂停当前模式
    $(".pauseMode").on("click", function() {
        startAndPauseMode(3)
        // clearInterval(queryModeMsg)

    })



//弹窗的远端视频状态代码备份
// <div class="layui-col-md4" class="distantVideoState">
//                             <div class="layui-card">
//                                 <div class="layui-card-header">远端视频状态</div>
//                                 <div class="layui-card-body">
//                                     <table class="table table-bordered text-center table-condensed">
//                                         <thead>
//                                             <tr>
//                                                 <th>项目</th>
//                                                 <th>评分</th>
//                                             </tr>
//                                             <tr >
//                                                 <td>马赛克</td>
//                                                 <td>${list[currentNum].distantvideojson.mosaic}</td>
//                                             </tr>
//                                             <tr>
//                                                 <td>黑屏/绿屏</td>
//                                                 <td>${list[currentNum].distantvideojson.blackscreen}</td>
//                                             </tr>
//                                             <tr >
//                                                 <td>停滞</td>
//                                                 <td>${list[currentNum].distantvideojson.frozen}</td>
//                                             </tr>
//                                         </thead>
//                                     </table>
//                                 </div>
//                             </div>
//                         </div>





/*------------------------------------------动态样式部分------------------------------------------*/

 //下拉框样式
    $(".selectMode").on("focus",function(){
        $(this).css({
            "border":"1px solid #00aeef",
        })
    })
     $(".selectMode").on("blur",function(){
        $(this).css({
            "border":"1px solid #ccc",
        })
    })


 //启动和暂停按钮样式
  $(".startMode").on("mousedown",function(){
        $(this).css({
            "border":"1px solid #00aeef",
            "background": "#05b35d"
        })
    })
     $(".startMode").on("mouseup",function(){
        $(this).css({
            "border":"1px solid transparent",
             "background": "#00ce68"
        })
    })
  $(".pauseMode").on("mousedown",function(){
        $(this).css({
            "border":"1px solid #00aeef",
            "background": "#05b35d"
        })
    })
     $(".pauseMode").on("mouseup",function(){
        $(this).css({
            "border":"1px solid transparent",
             "background": "#00ce68"
        })
    })

 /*------------------------------------------动态样式部分结束------------------------------------------*/

})