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

                    //自动试机试线检测：调用自动查询通道的方法
                    // var detection = sessionStorage.getItem("detection"); //上次浏览页
                    // var conferenceid = $(".currentRoom").attr("alt");
                    // if (detection) {
                    //     getChains(detection, conferenceid, 1)
                    // } else {
                    //     getChains(1, conferenceid, 1)
                    // }

                    queryModeMsg = setInterval(function() {
                        var conferenceid = $(".currentRoom").attr("alt")
                        var zoneName = document.getElementsByClassName("nowZone")[0].innerText;
                        var detection = sessionStorage.getItem("detection"); //上次浏览页
                        if (detection) {
                            getChains(detection, conferenceid, 1, zoneName)
                        } else {
                            getChains(1, conferenceid, 1, zoneName)
                        }
                    }, 1000)

                } else if (data.result == "2") {

                    $(".selectMode").find("option:contains('实时视频质量评测')").attr("selected", true);
                    $(".nowState").text("已启动")

                    //实时视频质量评测：调用实时查询通道的方法
                    // var detection = sessionStorage.getItem("detection"); //上次浏览页
                    // var conferenceid = $(".currentRoom").attr("alt")
                    // if (detection) {
                    //     getChains(detection, conferenceid, 2)
                    // } else {
                    //     getChains(1, conferenceid, 2)
                    // }


                    queryModeMsg = setInterval(function() {
                        var conferenceid = $(".currentRoom").attr("alt")
                        var zoneName = document.getElementsByClassName("nowZone")[0].innerText;
                        var detection = sessionStorage.getItem("detection"); //上次浏览页
                        if (detection) {
                            getChains(detection, conferenceid, 2, zoneName)
                        } else {
                            getChains(1, conferenceid, 2, zoneName)
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

//查询是否已经配置通道，控制战区指示灯的亮灭
function currentRoomChain(conferenceid) {
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
                var currentZone = $(".currentRoom").text().split("-")[0].split("：")[1] //当前战区名字
                var allLightLine = $(".mapBox p span") //地图上所有的战区名字


                //存在通道则亮所在战区指示灯
                for (var i = 0; i < list.length; i++) {
                    var allZonename = list[i].zonename //每一个战区名字
                    switch (allZonename) {
                        case "北部战区":
                            $(".north i").css("color", "green");
                            break;
                        case "中部战区":
                            $(".center i").css("color", "green");
                            break;
                        case "东部战区":
                            $(".east i").css("color", "green");
                            break;
                        case "西部战区":
                            $(".west i").css("color", "green");
                            break;
                        case "南部战区":
                            $(".south i").css("color", "green");
                            break;
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


//展示当前会场通道列表，detect_type传1代表自动试机试线检测，传2代表实时视频质量评测。
function getChains(pagenum, conferenceid, detecttype, zoneName) {
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

                var list = data.Data;
                var count = data.count; //总条数

                //下面五个数组分别保存各战区的所有条链的所有评分。
                var northBox = [],
                    centerBox = [],
                    southBox = [],
                    eastBox = [],
                    westBox = []



                for (var i = 0; i < list.length; i++) {

                    var currentScore = list[i].score; //通道评分
                    var currentZonename = list[i].zonename; //当前战区


                    //将各战区的所有链的所有评分，分别追加到对应数组
                    if (currentZonename == "北部战区") {
                        northBox.push(currentScore)
                    } else if (currentZonename == "南部战区") {
                        southBox.push(currentScore)
                    } else if (currentZonename == "东部战区") {
                        eastBox.push(currentScore)
                    } else if (currentZonename == "西部战区") {
                        westBox.push(currentScore)
                    } else if (currentZonename == "中部战区") {
                        centerBox.push(currentScore)
                    }

                }


                //筛选出每个数组中评分最小的，即最严重的评分。
                northBox.sort(function(a, b) {
                    return a - b;
                })
                centerBox.sort(function(a, b) {
                    return a - b;
                })
                southBox.sort(function(a, b) {
                    return a - b;
                })
                eastBox.sort(function(a, b) {
                    return a - b;
                })
                westBox.sort(function(a, b) {
                    return a - b;
                })


                //根据最小的评分，来判断灯的颜色
                if (northBox.length > 0) {
                    var northMin = northBox[0]
                    if (1 <= northMin && northMin < 2) {
                        $(".north i").css("color", "#a90808")
                    } else if (2 <= northMin && northMin < 3) {
                        $(".north i").css("color", "#ff0505")
                    } else if (3 <= northMin && northMin < 4) {
                        $(".north i").css("color", "#ffa500")
                    } else if (4 <= northMin && northMin <= 5) {
                        $(".north i").css("color", "#008000")
                    }
                }
                if (centerBox.length > 0) {
                    var centerMin = centerBox[0]
                    if (1 <= centerMin && centerMin < 2) {
                        $(".center i").css("color", "#a90808")
                    } else if (2 <= centerMin && centerMin < 3) {
                        $(".center i").css("color", "#ff0505")
                    } else if (3 <= centerMin && centerMin < 4) {
                        $(".center i").css("color", "#ffa500")
                    } else if (4 <= centerMin && centerMin <= 5) {
                        $(".center i").css("color", "#008000")
                    }
                }
                if (southBox.length > 0) {
                    var southMin = southBox[0]
                    if (1 <= southMin && southMin < 2) {
                        $(".south i").css("color", "#a90808")
                    } else if (2 <= southMin && southMin < 3) {
                        $(".south i").css("color", "#ff0505")
                    } else if (3 <= southMin && southMin < 4) {
                        $(".south i").css("color", "#ffa500")
                    } else if (4 <= southMin && southMin <= 5) {
                        $(".south i").css("color", "#008000")
                    }
                }
                if (eastBox.length > 0) {
                    var eastMin = eastBox[0]
                    if (1 <= eastMin && eastMin < 2) {
                        $(".east i").css("color", "#a90808")
                    } else if (2 <= eastMin && eastMin < 3) {
                        $(".east i").css("color", "#ff0505")
                    } else if (3 <= eastMin && eastMin < 4) {
                        $(".east i").css("color", "#ffa500")
                    } else if (4 <= eastMin && eastMin <= 5) {
                        $(".east i").css("color", "#008000")
                    }
                }
                if (westBox.length > 0) {
                    var westMin = westBox[0]
                    if (1 <= westMin && westMin < 2) {
                        $(".west i").css("color", "#a90808")
                    } else if (2 <= westMin && westMin < 3) {
                        $(".west i").css("color", "#ff0505")
                    } else if (3 <= westMin && westMin < 4) {
                        $(".west i").css("color", "#ffa500")
                    } else if (4 <= westMin && westMin <= 5) {
                        $(".west i").css("color", "#008000")
                    }
                }


                //根据战区名字，查询当前战区列表（这里传的参数用getChains方法的即可）
                queryCurrentChainState(pagenum, conferenceid, detecttype, zoneName)



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

/*查看当前通道的评分详细信息*/
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
                                            <tr class="blur">
                                                <td>画面模糊</td>
                                                <td>${list[currentNum].localvideojson.blur}</td>
                                            </tr>
                                            <tr class="overdark">
                                                <td>亮度过暗</td>
                                                <td>${list[currentNum].localvideojson.overdark}</td>
                                            </tr>
                                            <tr class="overbright">
                                                <td>亮度过亮</td>
                                                <td>${list[currentNum].localvideojson.overbright}</td>
                                            </tr>
                                            <tr class="colorcast">
                                                <td>画面偏色</td>
                                                <td>${list[currentNum].localvideojson.colorcast}</td>
                                            </tr>
                                            <tr class="lowcontrast">
                                                <td>对比度过低</td>
                                                <td>${list[currentNum].localvideojson.lowcontrast}</td>
                                            </tr>
                                            <tr class="mosaic">
                                                <td>马赛克</td>
                                                <td>${list[currentNum].localvideojson.mosaic}</td>
                                            </tr>
                                            <tr class="blackscreen">
                                                <td>黑屏/绿屏</td>
                                                <td>${list[currentNum].localvideojson.blackscreen}</td>
                                            </tr>
                                            <tr class="frozen">
                                                <td>停滞</td>
                                                <td>${list[currentNum].localvideojson.frozen}</td>
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

//根据战区，查询该战区的通道状态
function queryCurrentChainState(pagenum, conferenceid, detecttype, zoneName) {
    $.ajax({
        url: "/chainstate/getChainstatesByZonename",
        type: "post",
        data: {
            "pagenum": pagenum,
            "conferenceid": conferenceid,
            "detect_type": detecttype,
            "zonename": zoneName
        },
        dataType: "json",
        success: function(data) {
            if (data.flag == "success") {


                var list = data.Data;

                if (list) {
                    $("#currentChainStateModal tbody").html("")
                    $("#currentChainStateModal .currentMsg p").text("")

                    for (var i = 0; i < list.length; i++) {
                        var num = i + 1; //序号
                        $("#currentChainStateModal tbody").append(
                            `<tr id="${list[i].chainid}" alt="${list[i].chainstateid}">  
                                    <td>${num}</td>   
                                    <td >${list[i].chainname}</td> 
                                    <td class="currentChainType">${list[i].chaintype}</td>     
                                    <td ><i class="fa fa-circle singalLight"></i></td> 
                                    <td >${data.time[i]}</td> 
                                   <td><i class="fa fa-edit view" style="cursor:pointer;" data-toggle="modal" data-target="#viewChainModal" alt="${i}" title="查看"></i>
                                    </td>
                                    </tr>
                                 `
                        )


                        //根据状态改变弹框内指示灯颜色
                        var currentChainstate = list[i].chainstate
                        if (currentChainstate == "严重故障") {
                            $("#" + list[i].chainid + " .singalLight").css("color", "#a90808")
                        } else if (currentChainstate == "故障") {
                            $("#" + list[i].chainid + " .singalLight").css("color", "#ff0505")
                        } else if (currentChainstate == "异常") {
                            $("#" + list[i].chainid + " .singalLight").css("color", "#ffa500")
                        } else if (currentChainstate == "正常") {
                            $("#" + list[i].chainid + " .singalLight").css("color", "#008000")
                        }
                    }

                }


            } else if (data.flag == "failure") {
                $("#currentChainStateModal .currentMsg p").text("")
                $("#currentChainStateModal tbody").html("")
                $("#currentChainStateModal .currentMsg p").text("没有通道状态数据展示")
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

    //调整地图大小
    var dpi = window.screen.height
    if (dpi > 710) {
        $(".mapBox").css("top", "10%")
    }

    window.onresize = function() {
        var dpi = window.screen.height
        if (dpi > 710) {
            $(".mapBox").css("top", "10%")
        } else {
            $(".mapBox").css("top", "0")
        }
    }

    //展示当前所在会场
    currentConference()

    //查询是否已经配置通道，控制战区指示灯的亮灭
    var conferenceid = $(".currentRoom").attr("alt")
    currentRoomChain(conferenceid)

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

    $(".addChainBtn").on("click", function() {
        //展示仓库通道
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

    //每次配置完，关闭弹窗后，重新刷新页面（意在重新查询战区指示灯的状态）
    $(".firstClose").on("click", function() {
        window.location.reload()
    })




    //点击战区，查询当前战区通道状态
    var allZone = $(".mapBox p i")
    for (var i = 0; i < allZone.length; i++) {
        allZone[i].onclick = function() {
            var zoneName = $(this).siblings("span").text()
            $(".currentZone").text(zoneName)
            $(".nowZone").text(zoneName)
            queryMode(4, zoneName)

        }
    }



    //展示当前通道详细信息
    $('#currentChainStateModal').delegate(".view", "click", function() {
        //获取当前信息在本页所在条数
        currentNum = $(this).attr("alt")
        //获取当前链的类型
        currentChainType = $(this).parents("tr").children(".currentChainType").text()
    })

    $("#viewChainModal").on('shown.bs.modal', function() {

        var detection = sessionStorage.getItem("detection"); //上次浏览页
        var conferenceid = $(".currentRoom").attr("alt");
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



})