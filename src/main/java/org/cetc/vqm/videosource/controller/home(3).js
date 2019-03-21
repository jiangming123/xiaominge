//查询当前会场
function currentConference() {
    $.ajax({
        url: "/chainstate/getChainInfo",
        type: "post",
        async:false,
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

//查询是否已经配置通道
function currentRoomChain(conferenceid) {
    $.ajax({
        url: "/chain/getActiveConferenceChains",
        type: "post",
        data: {
            "conferenceid":conferenceid
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


//展示已配置通道
function showCurrentRoomChain(conferenceid) {
    $.ajax({
        url: "/chain/getActiveConferenceChains",
        type: "post",
        data: {
            "conferenceid":conferenceid
        },
        dataType: "json",
        success: function(data) {
            if (data.flag == "success") {
                var list = data.Data; //所有使用中的通道

                if(list.length==0){
                    $("#configChainModal .modal-body").append(`
                         <p style="text-align:center;font-size:20px"><b>当前会场没有在线通道。</b></p>
                        `)
                }else{
                     $("#configChainModal tbody").append(
                        `<tr id="${list[i].chainid}">  
                        <td>${num}</td> 
                        <td class="chainnum">${list[i].chainnumber}</td>  
                        <td class="chainname">${list[i].chainname}</td> 
                        <td class="chaintype" >${list[i].type}</td>     
                        <td class="zoneName" id="${list[i].zoneid}">${list[i].zonename}</td>  
                        <td class="conferencename" >${list[i].conferencename}</td> 
                        <td >${data.time[i]}</td> 
                        <td>
                             <button class="btn btn-danger btn-sm deleteChain">移除</button>
                        </td>
                        </tr>
                     `
                    )
                }

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

//展示仓库通道
function showWarehouseChain(conferenceid) {
    $.ajax({
        url: "/chain/getInActiveConferenceChains",
        type: "post",
        data: {
            "conferenceid":conferenceid
        },
        dataType: "json",
        success: function(data) {
            if (data.flag == "success") {
                var list = data.Data; //所有使用中的通道

                if(list.length==0){
                    $("#addChainModal .modal-body").append(`
                         <p style="text-align:center;font-size:20px"><b>当前会场没有在线通道。</b></p>
                        `)
                }else{
                     $("#addChainModal tbody").append(
                        `<tr id="${list[i].chainid}">  
                        <td>${num}</td> 
                        <td><input type="checkbox" value="${list[i].chainid}"/></td>
                        <td class="chainnum">${list[i].chainnumber}</td>  
                        <td class="chainname">${list[i].chainname}</td> 
                        <td class="chaintype" >${list[i].type}</td>     
                        <td class="zoneName" id="${list[i].zoneid}">${list[i].zonename}</td>  
                        <td class="conferencename" >${list[i].conferencename}</td> 
                        <td >${data.time[i]}</td> 
                        </tr>
                     `
                    )
                }

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

    //查询是否已经配置通道
    var conferenceid=$(".currentRoom").attr("alt")
    currentRoomChain(conferenceid)

    $(".configBtn").on("clicl",function(){
        //展示已配置通道
        var conferenceid=$(".currentRoom").attr("alt")
        showCurrentRoomChain(conferenceid)
    })


})