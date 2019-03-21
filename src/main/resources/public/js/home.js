/*该页的工作流程：
    1.先查询当前会场
    2.查询当前的工作模式，是实时还是自动模式。
    3.根据当前模式，判断是否需要启动查询对应的链路状态列表信息。
    4.判断是否已经配置通道，是，则展示列表。否，则提示信息。


*/



//查询当前会场
function currentConference() {
    $.ajax({
        url: "/chainstate/getChainInfo",
        type: "post",
        async: false,
        data: {},
        dataType: "json",
        success: function(data) {
            if (data.flag == "success") {
                $(".currentRoom").text("")
                var list = data.infos;
                for (var i = 0; i < list.length; i++) {
                    $(".currentRoom").attr("alt", list[i].conferenceid)
                    $(".currentRoom").text("当前会场：" + list[i].zonename + "-" + list[i].conferencename)
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


//查询当前正在运行的是什么工作模式
function queryMode(controltype, zoneName) {
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

                    queryModeMsg = setInterval(function() {

                        var conferenceid = $(".currentRoom").attr("alt")
                        getChains(1, conferenceid, 1)

                    }, 1000)

                } else if (data.result == "2") {

                    $(".selectMode").find("option:contains('实时视频质量评测')").attr("selected", true);
                    $(".nowState").text("已启动")

                    queryModeMsg = setInterval(function() {

                        var conferenceid = $(".currentRoom").attr("alt")
                        getChains(1, conferenceid, 2)

                    }, 1000)


                } else if (data.result == "3") {
                    $(".nowState").text("待机")
                } else if (data.result == "-1") {
                    layer.msg('查询当前工作模式失败', {
                        offset: "3.8%",
                    });
                }
            } else if (data.flag == "failure") {


                $(".localChannel tbody").append(`
                         <tr>
                             <td colspan="3" style="color:red">未连接后台</td>
                         </tr>
                         `)

                $(".distantChannel tbody").append(`
                         <tr>
                             <td colspan="3" style="color:red">未连接后台</td>
                         </tr>
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
                $(".channelBox tbody").html(" ")
                var list = data.Data,
                    allTime = data.time;

                var today = clock(); //今天的时间


                //表格
                for (var i = 0; i < list.length; i++) {

                    var num = i + 1; //序号
                    var currentChainstate = list[i].chainstate //当前链路状态
                    var channelType = list[i].chaintype //链路的类型


                    if (channelType == "本地") {
                        $(".localChannel tbody").append(
                            `<tr id="${list[i].chainid}" alt="${list[i].chainstateid}">  
                        <td>${num}</td>   
                        <td class="channelName">${list[i].chainname}</td> 
                        <td class="currentChainType" style="display:none;">${list[i].chaintype}</td>
                        <td ><i class="fa fa-circle singalLight view" data-toggle="modal" data-target="#viewChainModal" alt="${i}" title="查看"></i></td> 
                        </td>
                        </tr>
                     `
                        )
                    }

                    var currentTime = allTime[i].split(" ")[0] //只截取日期

                    if (currentTime == today) {
                        $("#" + list[i].chainid + " .singalLight").css("display", "inline-block")
                        if (currentChainstate == "故障") {
                            $("#" + list[i].chainid + " .singalLight").css("color", "red")
                        } else if (currentChainstate == "异常") {
                            $("#" + list[i].chainid + " .singalLight").css("color", "orange")
                        } else if (currentChainstate == "正常") {
                            $("#" + list[i].chainid + " .singalLight").css("color", "green")
                        }
                    } else {
                        $("#" + list[i].chainid + " .singalLight").css("display", "none")
                    }



                }


                //表格
                for (var i = 0; i < list.length; i++) {

                    var num = i + 1; //序号
                    var currentChainstate = list[i].chainstate //当前链路状态
                    var channelType = list[i].chaintype //链路的类型
                    var currentTime = allTime[i].split(" ")[0] //只截取日期

                    if (channelType == "远端") {
                        $(".distantChannel tbody").append(
                            `<tr id="${list[i].chainid}" alt="${list[i].chainstateid}">  
                        <td>${num}</td>   
                        <td >${list[i].chainname}</td>
                        <td class="currentChainType" style="display:none;">${list[i].chaintype}</td>      
                        <td ><i class="fa fa-circle singalLight view" data-toggle="modal" data-target="#viewChainModal" alt="${i}" title="查看"></i></td> 
                        </td>
                        </tr>
                     `
                        )
                    }

                    if (currentTime == today) {
                        $("#" + list[i].chainid + " .singalLight").css("display", "inline-block")
                        if (currentChainstate == "故障") {
                            $("#" + list[i].chainid + " .singalLight").css("color", "red")
                        } else if (currentChainstate == "异常") {
                            $("#" + list[i].chainid + " .singalLight").css("color", "orange")
                        } else if (currentChainstate == "正常") {
                            $("#" + list[i].chainid + " .singalLight").css("color", "green")
                        }
                    } else {
                        $("#" + list[i].chainid + " .singalLight").css("display", "none")
                    }


                }



                if (list.length == 0) {
                    $(".localChannel tbody").append(`
                        <tr>
                            <td colspan="3">当前无已配置链路</td>
                        </tr>
                        `)

                    $(".distantChannel tbody").append(`
                        <tr>
                            <td colspan="3">当前无已配置链路</td>
                        </tr>
                        `)
                }


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


/*查看当前通道的详细信息*/
function detailChain(pagenum, conferenceid, currentNum, detecttype, currentChainType, currentChannelName) {
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
                    <div class="layui-row" style="margin-bottom: 20px;">
                        <div class="layui-col-md12">
                         <span><b>当前通道：</b></span> <span>${currentChannelName}</span>
                        </div>  
                    </div>
                    <div class="layui-row">
                        <div class="layui-col-md6">
                            <div class="layui-card">
                                <div class="layui-card-header"><b>本地视频状态：</b><span>${list[currentNum].localvideostate}</span></div>
                                <div class="layui-card-body">
                                    <table class="table table-bordered text-center table-condensed">
                                        <thead>
                                            <tr>
                                                <th>项目</td>
                                                <th>评分</td>
                                            </tr>
                                            <tr class="blur">
                                                <td>画面模糊</td>
                                                <td ><i class="fa fa-circle currentState"></i></td> 
                                            </tr>
                                            <tr class="overdark">
                                                <td>亮度过暗</td>
                                                <td ><i class="fa fa-circle currentState"></i></td> 
                                            </tr>
                                            <tr class="overbright">
                                                <td>亮度过亮</td>
                                                <td ><i class="fa fa-circle currentState"></i></td> 
                                            </tr>
                                            <tr class="colorcast">
                                                <td>画面偏色</td>
                                                <td ><i class="fa fa-circle currentState"></i></td> 
                                            </tr>
                                            <tr class="lowcontrast">
                                                <td>对比度过低</td>
                                                <td ><i class="fa fa-circle currentState"></i></td> 
                                            </tr>
                                            <tr class="mosaic">
                                                <td>马赛克</td>
                                                <td><i class="fa fa-circle currentState"></i></td>
                                            </tr>
                                            <tr class="blackscreen">
                                                <td>黑屏/绿屏</td>
                                                <td><i class="fa fa-circle currentState"></i></td>
                                            </tr>
                                            <tr class="frozen">
                                                <td>停滞</td>
                                                <td><i class="fa fa-circle currentState"></i></td>
                                            </tr>
                                            <tr class="psnr">
                                                <td>峰值信噪比</td>
                                                <td ><i class="fa fa-circle currentState"></i></td> 
                                            </tr>
                                        </thead>
                                    </table>
                                </div>
                            </div>
                        </div>
                        
                        <div class="layui-col-md6">
                            <div class="layui-card">
                                <div class="layui-card-header"><b>网络状态信息：</b><span>${list[currentNum].networkstate}</span></div>
                                <div class="layui-card-body">
                                    <table class="table table-bordered text-center table-condensed">
                                        <thead>
                                            <tr>
                                                <th>状态</th><th>错误</th><th>原因</th>
                                            </tr>
                                            <tr>
                                                <td class="networkstate"><i class="fa fa-circle currentState"></i></td> 
                                                <td class="networkfault"><i class="fa fa-circle currentState"></i></td> 
                                                <td class="networkreason"><i class="fa fa-circle currentState"></i></td> 
                                            </tr>
                                        </thead>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="layui-row">
                        <div class="layui-col-md12">
                         <span class="errorAdvice"><b>故障维修建议：</b></span>  <span>${list[currentNum].suggestion}</span>
                        </div>
                    </div>
                    <div class="layui-row" style="margin-top: 20px;">
                        <div class="layui-col-md12">
                         <span><b>查看故障关键帧图像：</b></span>  <a href="${list[currentNum].path}" target="_blank">${list[currentNum].path}</a>
                        </div>
                    </div>
                    <div class="layui-row" style="margin-top: 20px;">
                        <div class="layui-col-md12">
                         <span><b>当前数据检测时间：</b></span> <span>${data.time[currentNum]}</span>
                        </div>  
                    </div>
                        `
                        )

                        //根据评分改变颜色
                        if (1 <= list[currentNum].localvideojson.blur && list[currentNum].localvideojson.blur <= 2.5) {
                            $(".blur .currentState").css("color", "red")
                        } else if (2.5 <= list[currentNum].localvideojson.blur && list[currentNum].localvideojson.blur <= 4) {
                            $(".blur .currentState").css("color", "orange")
                        } else if (4 <= list[currentNum].localvideojson.blur && list[currentNum].localvideojson.blur <= 5) {
                            $(".blur .currentState").css("color", "green")
                        }

                        if (1 <= list[currentNum].localvideojson.overdark && list[currentNum].localvideojson.overdark <= 2.5) {
                            $(".overdark .currentState").css("color", "red")
                        } else if (2.5 <= list[currentNum].localvideojson.overdark && list[currentNum].localvideojson.overdark <= 4) {
                            $(".overdark .currentState").css("color", "orange")
                        } else if (4 <= list[currentNum].localvideojson.overdark && list[currentNum].localvideojson.overdark <= 5) {
                            $(".overdark .currentState").css("color", "green")
                        }

                        if (1 <= list[currentNum].localvideojson.overbright && list[currentNum].localvideojson.overbright <= 2.5) {
                            $(".overbright .currentState").css("color", "red")
                        } else if (2.5 <= list[currentNum].localvideojson.overbright && list[currentNum].localvideojson.overbright <= 4) {
                            $(".overbright .currentState").css("color", "orange")
                        } else if (4 <= list[currentNum].localvideojson.overbright && list[currentNum].localvideojson.overbright <= 5) {
                            $(".overbright .currentState").css("color", "green")
                        }

                        if (1 <= list[currentNum].localvideojson.colorcast && list[currentNum].localvideojson.colorcast <= 2.5) {
                            $(".colorcast .currentState").css("color", "red")
                        } else if (2.5 <= list[currentNum].localvideojson.colorcast && list[currentNum].localvideojson.colorcast <= 4) {
                            $(".colorcast .currentState").css("color", "orange")
                        } else if (4 <= list[currentNum].localvideojson.colorcast && list[currentNum].localvideojson.colorcast <= 5) {
                            $(".colorcast .currentState").css("color", "green")
                        }

                        if (1 <= list[currentNum].localvideojson.lowcontrast && list[currentNum].localvideojson.lowcontrast <= 2.5) {
                            $(".lowcontrast .currentState").css("color", "red")
                        } else if (2.5 <= list[currentNum].localvideojson.lowcontrast && list[currentNum].localvideojson.lowcontrast <= 4) {
                            $(".lowcontrast .currentState").css("color", "orange")
                        } else if (4 <= list[currentNum].localvideojson.lowcontrast && list[currentNum].localvideojson.lowcontrast <= 5) {
                            $(".lowcontrast .currentState").css("color", "green")
                        }

                        if (1 <= list[currentNum].localvideojson.psnr && list[currentNum].localvideojson.psnr <= 2.5) {
                            $(".psnr .currentState").css("color", "red")
                        } else if (2.5 <= list[currentNum].localvideojson.psnr && list[currentNum].localvideojson.psnr <= 4) {
                            $(".psnr .currentState").css("color", "orange")
                        } else if (4 <= list[currentNum].localvideojson.psnr && list[currentNum].localvideojson.psnr <= 5) {
                            $(".psnr .currentState").css("color", "green")
                        }

                        if (1 <= list[currentNum].localvideojson.mosaic && list[currentNum].localvideojson.mosaic <= 2.5) {
                            $(".mosaic .currentState").css("color", "red")
                        } else if (2.5 <= list[currentNum].localvideojson.mosaic && list[currentNum].localvideojson.mosaic <= 4) {
                            $(".mosaic .currentState").css("color", "orange")
                        } else if (4 <= list[currentNum].localvideojson.mosaic && list[currentNum].localvideojson.mosaic <= 5) {
                            $(".mosaic .currentState").css("color", "green")
                        }

                        if (1 <= list[currentNum].localvideojson.blackscreen && list[currentNum].localvideojson.blackscreen <= 2.5) {
                            $(".blackscreen .currentState").css("color", "red")
                        } else if (2.5 <= list[currentNum].localvideojson.blackscreen && list[currentNum].localvideojson.blackscreen <= 4) {
                            $(".blackscreen .currentState").css("color", "orange")
                        } else if (4 <= list[currentNum].localvideojson.blackscreen && list[currentNum].localvideojson.blackscreen <= 5) {
                            $(".blackscreen .currentState").css("color", "green")
                        }

                        if (1 <= list[currentNum].localvideojson.frozen && list[currentNum].localvideojson.frozen <= 2.5) {
                            $(".frozen .currentState").css("color", "red")
                        } else if (2.5 <= list[currentNum].localvideojson.frozen && list[currentNum].localvideojson.frozen <= 4) {
                            $(".frozen .currentState").css("color", "orange")
                        } else if (4 <= list[currentNum].localvideojson.frozen && list[currentNum].localvideojson.frozen <= 5) {
                            $(".frozen .currentState").css("color", "green")
                        }


                    }
                }

                var currentWorkMode = $(".selectMode").val()

                if (currentWorkMode == "实时视频质量评测" && currentChainType == "本地") {
                    $(".psnr").css("display", "none")
                    $(".mosaic").css("display", "none")
                    $(".blackscreen").css("display", "none")
                    $(".frozen").css("display", "none")
                    $(".distantVideoState").css("display", "none")
                } else if (currentWorkMode == "实时视频质量评测" && currentChainType == "远端") {
                    $(".psnr").css("display", "none")
                } else if (currentWorkMode == "自动试机试线检测") {
                    $(".psnr").css("display", "inline-block")

                    $(".blur").css("display", "none")
                    $(".overdark").css("display", "none")
                    $(".overbright").css("display", "none")
                    $(".colorcast").css("display", "none")
                    $(".lowcontrast").css("display", "none")
                    $(".mosaic").css("display", "none")
                    $(".blackscreen").css("display", "none")
                    $(".frozen").css("display", "none")
                    $(".distantVideoState").css("display", "none")

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


//展示已配置通道
function showCurrentRoomChain(conferenceid) {
    $.ajax({
        url: "/chain/getActiveConferenceChains",
        type: "post",
        data: {
            "conferenceid": conferenceid
        },
        dataType: "json",
        success: function(data) {
            if (data.flag == "success") {
                var list = data.Data; //所有使用中的通道

                if (list.length == 0) {
                    $("#configChainModal .modal-body .prompt").html(" ")
                    $("#configChainModal .modal-body .prompt").append(`
                         <p style="text-align:center;font-size:20px"><b>当前会场没有在线通道。</b></p>
                        `)
                } else {
                    $("#configChainModal tbody").html(" ")
                    for (var i = 0; i < list.length; i++) {
                        var num = i + 1; //序号
                        $("#configChainModal tbody").append(
                            `<tr id="${list[i].chainid}">  
                        <td>${num}</td> 
                        <td class="chainnum">${list[i].chainnumber}</td>  
                        <td class="chainname">${list[i].chainname}</td> 
                        <td class="chaintype" >${list[i].type}</td>     
                        <td class="zoneName" id="${list[i].zoneid}">${list[i].zonename}</td>  
                        <td class="conferencename" >${list[i].conferencename}</td> 
                        <td>
                             <i class="fa fa-trash-o deleteChain" title="移除"></i>
                        </td>
                        </tr>
                     `
                        )
                    }
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

//移除当前通道
function deleteCurrentChain(conferenceid, chainid) {
    $.ajax({
        url: "/chain/deleteActiveConferenceChains",
        type: "post",
        data: {
            "conferenceid": conferenceid,
            "chainid": chainid
        },
        dataType: "json",
        success: function(data) {
            if (data.flag == "success") {}
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            console.log(XMLHttpRequest.status);
            console.log(XMLHttpRequest.readyState);
            console.log(textStatus);
        }
    })
}


//展示仓库通道
function showWarehouseChain(conferenceid) {
    $.ajax({
        url: "/chain/getInActiveConferenceChains",
        type: "post",
        data: {
            "conferenceid": conferenceid
        },
        dataType: "json",
        success: function(data) {
            if (data.flag == "success") {
                var list = data.Data; //所有使用中的通道

                if (list.length == 0) {
                    $("#addChainModal .modal-body").html(" ")
                    $("#addChainModal .modal-body").append(`
                         <p style="text-align:center;font-size:20px"><b>当前会场没有可添加通道。</b></p>
                        `)
                } else {
                    $("#addChainModal tbody").html(" ")
                    for (var i = 0; i < list.length; i++) {
                        var num = i + 1; //序号
                        $("#addChainModal tbody").append(
                            `<tr id="${list[i].chainid}">  
                                   <td>${num}</td> 
                                   <td><input type="checkbox" value="${list[i].chainid}"/></td>
                                   <td class="chainnum">${list[i].chainnumber}</td>  
                                   <td class="chainname">${list[i].chainname}</td> 
                                   <td class="chaintype" >${list[i].type}</td>     
                                   <td class="zoneName" id="${list[i].zoneid}">${list[i].zonename}</td>  
                                   <td class="conferencename" >${list[i].conferencename}</td> 
                                   </tr>
                                `
                        )
                    }

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


//添加通道（从仓库中移动到使用中）
function addNewChain(conferenceid, chainids) {
    $.ajax({
        url: "/chain/addActiveConferenceChains",
        type: "post",
        data: {
            "conferenceid": conferenceid,
            "chainids": chainids
        },
        dataType: "json",
        success: function(data) {
            if (data.flag == "success") {

            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            console.log(XMLHttpRequest.status);
            console.log(XMLHttpRequest.readyState);
            console.log(textStatus);
        }
    })
}

//获取当前日期
function clock() {
    var cur = (new Date()),

        curYear = cur.getFullYear(), //年

        curM = cur.getMonth() + 1,
        curMonth = "0" + curM, //月

        curD = cur.getDate(),
        curDate = "0" + curD, //日

        curTime = (new Date()).toTimeString().split("G")[0]; //时间

    if (curM >= 10) {
        curMonth = curMonth.replace("0", "")
    }
    if (curD >= 10) {
        curDate = curDate.replace("0", "")
    }

    var today = curYear + "-" + curMonth + "-" + curDate
    return today
}

$(function() {



    //查询当前会场
    currentConference()




    //展示当前通道详细信息（弹窗）
    $('.channelBox').delegate(".view", "click", function() {
        //获取当前信息在本页所在条数
        currentNum = $(this).attr("alt")
        //获取当前链的类型
        currentChainType = $(this).parents("tr").children(".currentChainType").text()
        //当前链的名字
        currentChannelName = $(this).parents("tr").children(".channelName").text()
    })


    $("#viewChainModal").on('shown.bs.modal', function() {

        var conferenceid = $(".currentRoom").attr("alt");
        var currentMode = $(".selectMode").val();

        if (currentMode == "自动试机试线检测") {

            detailChain(1, conferenceid, currentNum, 1, currentChainType, currentChannelName)

        } else if (currentMode == "实时视频质量评测") {

            detailChain(1, conferenceid, currentNum, 2, currentChainType, currentChannelName)

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



    //暂停当前模式
    $(".pauseMode").on("click", function() {
        startAndPauseMode(3)
        // clearInterval(queryModeMsg)

    })


    //配置会场通道
    $(".configBtn").on("click", function() {
        //展示已配置通道
        var conferenceid = $(".currentRoom").attr("alt")
        showCurrentRoomChain(conferenceid)
    })
    //移除通道
    $('#configChainModal').delegate(".deleteChain", "click", function() {
        var conferenceid = $(".currentRoom").attr("alt"),
            chainid = $(this).parents("tr").attr("id");
        deleteCurrentChain(conferenceid, chainid)

        //重新展示已配置通道
        var conferenceid = $(".currentRoom").attr("alt")
        showCurrentRoomChain(conferenceid)
    })

    //展示仓库通道
    $(".addChainBtn").on("click", function() {

        var conferenceid = $(".currentRoom").attr("alt")
        showWarehouseChain(conferenceid)
    })

    //添加通道（从仓库中移动到使用中）
    $(".addNewChain").on("click", function() {

        var conferenceid = $(".currentRoom").attr("alt"),
            allInput = $("#addChainModal tbody input[type=checkbox]"),
            chainids = "";

        //获取所有选中的复选框
        for (var i = 0; i < allInput.length; i++) {
            if (allInput[i].checked) {
                chainids += allInput[i].value + ","
            }
        }

        addNewChain(conferenceid, chainids)
        //重新展示已配置通道
        var conferenceid = $(".currentRoom").attr("alt")
        showCurrentRoomChain(conferenceid)
    })



    /*------------------------------------------动态样式部分------------------------------------------*/

    //顶部查询按钮样式
    $(".configBtn").on("mousedown", function() {
        $(this).css({
            "border": "1px solid #00aeef",
            "background": "#05b35d"
        })
    })
    $(".configBtn").on("mouseup", function() {
        $(this).css({
            "border": "1px solid transparent",
            "background": "#00ce68"
        })
    })

    $(".startMode").on("mousedown", function() {
        $(this).css({
            "border": "1px solid #00aeef",
            "background": "#05b35d"
        })
    })
    $(".startMode").on("mouseup", function() {
        $(this).css({
            "border": "1px solid transparent",
            "background": "#00ce68"
        })
    })

    $(".pauseMode").on("mousedown", function() {
        $(this).css({
            "border": "1px solid #00aeef",
            "background": "#05b35d"
        })
    })
    $(".pauseMode").on("mouseup", function() {
        $(this).css({
            "border": "1px solid transparent",
            "background": "#00ce68"
        })
    })
    //弹窗按钮
    $(".addChainBtn").on("mousedown", function() {
        $(this).css({
            "border": "1px solid #00aeef",
            "background": "#05b35d"
        })
    })
    $(".addChainBtn").on("mouseup", function() {
        $(this).css({
            "border": "1px solid transparent",
            "background": "#00ce68"
        })
    })

    //下拉框样式
    $(".selectMode").on("focus", function() {
        $(this).css({
            "border": "1px solid #00aeef",
        })
    })
    $(".selectMode").on("blur", function() {
        $(this).css({
            "border": "1px solid #ccc",
        })
    })


})