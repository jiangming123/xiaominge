//页面展示下拉框战区列表
function allZonesList() {
    $.ajax({
        url: '/warzone/getAllWarzones',
        data: {},
        type: "post",
        dataType: "json",
        success: function(data) {
            if (data.flag == "success") {

                var list = data.Data;


                $(".zonesList").append(
                    ` <option>全部通道</option>
                     `
                )

                for (var i = 0; i < list.length; i++) {

                    $(".zonesList").append(
                        `<option alt="${list[i].zoneid}">${list[i].zonename}</option>
                     `
                    )

                }
            } else {
                layer.msg('战区列表查询失败', {
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


//页面根据战区查询会场列表
function conferenceByZone(zoneID, pagenum) {
    $.ajax({
        url: '/conference/getAllConferencesByZoneID',
        data: {
            "zoneid": zoneID,
            "pagenum": pagenum
        },
        type: "post",
        dataType: "json",
        success: function(data) {
            if (data.flag == "success") {
                $(".meetingList").html(" ")
                var list = data.Data;
                var count = data.count;

                $(".meetingList").append(`
                        <option>全部会场</option>
                    `)
                //表格
                for (var i = 0; i < list.length; i++) {

                    $(".meetingList").append(
                        `<option alt="${list[i].conferenceid}">${list[i].conferencename}</option>
                     `
                    )

                }

            } else {
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


//根据战区和会场查询通道列表
function queryChains(zoneID, conferenceid, pagenum) {
    $.ajax({
        url: '/chain/getAllChainsByZoneIDAndConferenceid',
        data: {
            "zoneid": zoneID,
            "conferenceid": conferenceid,
            "pagenum": pagenum
        },
        type: "post",
        dataType: "json",
        success: function(data) {
            if (data.flag == "success") {
                $("tbody").html(" ")
                var list = data.Data;
                var count = data.count;

                //表格
                for (var i = 0; i < list.length; i++) {

                    var num = i + (pagenum - 1) * 10 + 1; //序号

                    $("tbody").append(
                        `<tr id="${list[i].chainid}">  
                        <td>${num}</td> 
                        <td class="chainnum">${list[i].chainnumber}</td>  
                        <td class="chainname">${list[i].chainname}</td> 
                        <td class="chaintype" >${list[i].type}</td>     
                        <td class="zoneName" id="${list[i].zoneid}">${list[i].zonename}</td>  
                        <td class="conferencename" >${list[i].conferencename}</td> 
                        <td >${data.time[i]}</td> 
                        <td><i class="fa fa-edit modify" data-toggle="modal" data-target="#modifyChainModal" title="修改"></i>
                            <i class="fa fa-trash-o deleteChain" title="删除"></i>
                        </td>
                        </tr>
                     `
                    )
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
                        limit: 10 // 每页显示数
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
                                getChains(obj.curr)
                            }
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

//展示所有通道列表
function getChains(pagenum) {
    $.ajax({
        url: '/chain/getAllChains',
        data: {
            "pagenum": pagenum
        },
        type: "post",
        dataType: "json",
        success: function(data) {
            if (data.flag == "success") {
                $("tbody").html(" ")
                var list = data.Data;
                var count = data.count;

                //表格
                for (var i = 0; i < list.length; i++) {

                    var num = i + (pagenum - 1) * 10 + 1; //序号

                    $("tbody").append(
                        `<tr id="${list[i].chainid}">  
                        <td>${num}</td> 
                        <td class="chainnum">${list[i].chainnumber}</td>  
                        <td class="chainname">${list[i].chainname}</td> 
                        <td class="chaintype" >${list[i].type}</td>     
                        <td class="zoneName" id="${list[i].zoneid}">${list[i].zonename}</td>  
                        <td class="conferencename" >${list[i].conferencename}</td> 
                        <td >${data.time[i]}</td> 
                        <td>
                             <i class="fa fa-edit modify" data-toggle="modal" data-target="#modifyChainModal" title="修改"></i>
                             <i class="fa fa-trash-o deleteChain" title="删除"></i>
                        </td>
                        </tr>
                     `
                    )
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
                        limit: 10 // 每页显示数
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
                                getChains(obj.curr)
                            }
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


//添加通道
function addChain(zoneid, zonename, conferenceid, conferencename, chainname, type) {
    $.ajax({
        url: "/chain/addChain",
        data: {
            "zoneid": zoneid,
            "zonename": zonename,
            "conferenceid": conferenceid,
            "conferencename": conferencename,
            "chainname": chainname,
            "type": type
        },
        type: "post",
        success: function(data) {
            if (data.flag == "success") {
                layer.msg('添加成功', {
                    offset: "3.8%",
                });
            } else if (data.flag == "error") {
                layer.msg('添加失败', {
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

//修改通道信息
function amendChain(chainid, chainname, zonename, conferencename, type) {
    $.ajax({
        url: '/chain/editChain',
        type: 'post',
        data: {
            "chainid": chainid,
            "chainname": chainname,
            "zonename": zonename,
            "conferencename": conferencename,
            "type": type
        },
        success: function(data) {
            if (data.flag == "success") {
                layer.msg('修改成功', {
                    offset: "3.8%",
                });
            } else {
                layer.msg('修改失败', {
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

//删除通道
function deleteChain(zoneid, chainid) {
    $.ajax({
        url: '/chain/deleteChain',
        data: {
            "zoneid": zoneid,
            "chainid": chainid
        },
        type: "post",
        success: function() {
            if (data.flag == "seccess") {
                layer.msg('删除成功', {
                    offset: "3.8%",
                });
            } else {
                layer.msg('删除失败', {
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



/*添加模态框中，查询战区列表*/
function queryAllZones() {
    $.ajax({
        url: '/warzone/getAllWarzones',
        data: {},
        type: "post",
        dataType: "json",
        success: function(data) {
            if (data.flag == "success") {

                $(".zoneNameList").html(" ");
                var list = data.Data;

                for (var i = 0; i < list.length; i++) {

                    //页面搜索框的战区下拉列表
                    $(".zoneNameList").append(
                        `<option alt="${list[i].zoneid}">${list[i].zonename}</option>
                     `
                    )

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

/*添加模态框中，根据战区查询通道*/
function allConferences(zoneID) {
    $.ajax({
        url: '/conference/getAllConferencesByZoneID',
        data: {
            "zoneid": zoneID
        },
        type: "post",
        dataType: "json",
        success: function(data) {
            var list = data.Data;
            $(".conferenceName").html(" ");
            for (var i = 0; i < list.length; i++) {
                $(".conferenceName").append(
                    `
                    <option alt="${list[i].conferenceid}">${list[i].conferencename}</option>
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

$(function() {


    //展示全部通道列表
    getChains(1)

    //加载页面所有战区下拉框
    allZonesList()

    //根据战区加载会场下拉列表
    $(".zonesList").on("change", function() {
        var zoneID = $(this).find("option:selected").attr("alt"),
            zoneVal = $(this).val();

        if (zoneVal == "全部通道") {
            $(".meetingList").append(`
                <option>全部会场</option>
                `)
        } else {
            conferenceByZone(zoneID, -1)
        }
    })

    //根据战区会场查询对应通道信息
    $(".queryChain").on("click", function() {
        var zoneID = $(".zonesList").find("option:selected").attr("alt"),
            conferenceid = $(".meetingList").find("option:selected").attr("alt"),
            zoneVal = $(".zonesList").val(),
            conferenceVal = $(".meetingList").val();

        if (zoneVal == "全部通道" && conferenceVal == "全部会场") {
            getChains(1)
        } else if (conferenceVal == "全部会场") {
            queryChains(zoneID, -1, 1)
        } else {
            queryChains(zoneID, conferenceid, 1)
        }
    })



    //加载提示框
    layui.use('layer', function() {
        layer = layui.layer;
    });


    /*添加功能*/
    //添加模态框中，查询战区列表
    $("#addChainModal").on("show.bs.modal", function() {
        queryAllZones()
        //加载第一个战区的通道列表
        //var zoneID = $(".zoneName option:selected").attr("alt")
        setTimeout(function() {
            var zoneID = $(".zoneNameList option:nth-child(1)").attr("alt")
            allConferences(zoneID)
        }, 300)

    })

    //添加模态框中，战区改变，通道内容改变
    $(".zoneNameList").on("change", function() {
        var zoneID = $('.zoneNameList option:selected').attr("alt")
        allConferences(zoneID)
    })

    //添加通道
    $(".confirmAdd").on('click', function() {
        var zoneid = $(".zoneNameList option:selected").attr("alt"),
            zonename = $(".zoneNameList option:selected").val(),
            conferenceid = $(".conferenceName option:selected").attr("alt"),
            conferencename = $(".conferenceName option:selected").val(),
            chainname = $(".chainName").val(),
            type = $(".chainType").val();

        //战区名称不能为空
        if (zonename == "" || zonename == null) {
            layer.msg('所属战区名称不能为空', {
                offset: "3.8%",
            });
            return false;
        }
        //会场名称不能为空
        if (conferencename == "" || conferencename == null) {
            layer.msg('所属会场名称不能为空', {
                offset: "3.8%",
            });
            return false;
        }
        //通道名称非空验证
        if (chainname == "" || chainname == null) {
            layer.msg('通道名称不能为空', {
                offset: "3.8%",
            });
            return false;
        }

        addChain(zoneid, zonename, conferenceid, conferencename, chainname, type)
        window.location.reload();
    })


    //删除通道信息
    $('tbody').delegate(".deleteChain", 'click', function() {
        var chainnum = $(this).parents('tr').children('.chainnum').text()
        var affirm = confirm("是否删除" + chainnum + "通道信息？")
        if (affirm == true) {
            var zoneid = $(this).parents("tr").find(".zoneName").attr("id"),
                chainid = $(this).parents("tr").attr("id");

            deleteChain(zoneid, chainid)
            window.location.reload();
        }
    })

    //修改通道信息
    $('tbody').delegate(".modify", "click", function() {
        //获取原来的通道信息
        var amendChainid = $(this).parents('tr').attr("id"),
            amendChainnum = $(this).parents('tr').children('.chainnum').text(),
            amendChainname = $(this).parents('tr').children('.chainname').text(),
            amendZonename = $(this).parents('tr').children('.zoneName').text(),
            amendConferencename = $(this).parents('tr').children('.conferencename').text(),
            amendType = $(this).parents('tr').children('.chaintype').text();
        //填充原来的通道信息
        $(".newChainname").val(amendChainname);
        $(".newChainnum").val(amendChainnum);
        $(".newChaintype").find("option:contains(" + amendType + ")").attr("selected", true)
        $(".newChainid").val(amendChainid)
        $(".newZonename").val(amendZonename)
        $(".newConferencename").val(amendConferencename)
    })


    $(".confirmAmend").on("click", function() {
        //修改通道信息
        var chainid = $(".newChainid").val(),
            chainname = $(".newChainname").val(),
            zonename = $(".newZonename").val(),
            conferencename = $(".newConferencename").val(),
            type = $(".newChaintype").val();

        //通道名称不能为空
        if (chainname == "" || chainname == null) {
            layer.msg('通道名称不能为空', {
                offset: "3.8%",
            });
            return false;
        }

        //通道类型非空验证
        if (type == "" || type == null) {
            layer.msg('通道类型不能为空', {
                offset: "3.8%",
            });
            return false;
        }

        amendChain(chainid, chainname, zonename, conferencename, type)
        window.location.reload();

    })



    /*------------------------------------------动态样式部分------------------------------------------*/
    //点击添加通道样式
    $(".addNewChain").on("mousedown", function() {
        $(this).css({
            "border": "1px solid #00aeef",
            "background": "#05b35d"
        })
    })
    $(".addNewChain").on("mouseup", function() {
        $(this).css({
            "border": "1px solid transparent",
            "background": "#00ce68"
        })
    })
    //点击查询按钮样式
    $(".queryChain").on("mousedown", function() {
        $(this).css({
            "border": "1px solid #00aeef",
            "background": "#05b35d"
        })
    })
    $(".queryChain").on("mouseup", function() {
        $(this).css({
            "border": "1px solid transparent",
            "background": "#00ce68"
        })
    })
    //下拉框样式
    $(".zonesList").on("focus", function() {
        $(this).css({
            "border": "1px solid #00aeef",
        })
    })
    $(".zonesList").on("blur", function() {
        $(this).css({
            "border": "1px solid #ccc",
        })
    })
    $(".meetingList").on("focus", function() {
        $(this).css({
            "border": "1px solid #00aeef",
        })
    })
    $(".meetingList").on("blur", function() {
        $(this).css({
            "border": "1px solid #ccc",
        })
    })

    //添加通道弹框样式
    $(".zoneNameList").on("focus", function() {
        $(this).css({
            "border": "1px solid #00aeef",
        })
    })
    $(".zoneNameList").on("blur", function() {
        $(this).css({
            "border": "1px solid #ccc",
        })
    })
    $(".conferenceName").on("focus", function() {
        $(this).css({
            "border": "1px solid #00aeef",
        })
    })
    $(".conferenceName").on("blur", function() {
        $(this).css({
            "border": "1px solid #ccc",
        })
    })
    $(".chainName").on("focus", function() {
        $(this).css({
            "border": "1px solid #00aeef",
        })
    })
    $(".chainName").on("blur", function() {
        $(this).css({
            "border": "1px solid #ccc",
        })
    })
    $(".chainType").on("focus", function() {
        $(this).css({
            "border": "1px solid #00aeef",
        })
    })
    $(".chainType").on("blur", function() {
        $(this).css({
            "border": "1px solid #ccc",
        })
    })
    //修改通道弹框样式
    $(".newChainname").on("focus", function() {
        $(this).css({
            "border": "1px solid #00aeef",
        })
    })
    $(".newChainname").on("blur", function() {
        $(this).css({
            "border": "1px solid #ccc",
        })
    })
    $(".newChaintype").on("focus", function() {
        $(this).css({
            "border": "1px solid #00aeef",
        })
    })
    $(".newChaintype").on("blur", function() {
        $(this).css({
            "border": "1px solid #ccc",
        })
    })

    /*------------------------------------------动态样式部分结束------------------------------------------*/


})