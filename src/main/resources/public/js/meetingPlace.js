//展示会场列表
function getAllMeeting(pagenum) {
    $.ajax({
        url: '/conference/getAllConferences',
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
                        `<tr>  
                        <td>${num}</td>   
                        <td class='meetingId' style="display:none">${list[i].conferenceid}</td> 
                        <td class='theaterId' style="display:none">${list[i].zoneid}</td>     
                        <td class='theaterName'>${list[i].zonename}</td>  
                        <td class='meetingName'>${list[i].conferencename}</td> 
                        <td class='serverIp'>${list[i].serverip}</td> 
                        <td class='addTime'>${data.time[i]}</td> 
                        <td><i class="fa fa-edit modify" data-toggle="modal" data-target="#modifyMeetingModal" title="修改"></i>
                            <i class="fa fa-trash-o deleteMeeting" title="删除"></i>
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
                                getAllMeeting(obj.curr)
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


//加载查询功能下拉战区列表
function queryAllTheaters() {
    $.ajax({
        url: '/warzone/getAllWarzones',
        data: {},
        type: "post",
        dataType: "json",
        success: function(data) {
            if (data.flag == "success") {

                var list = data.Data;

                for (var i = 0; i < list.length; i++) {

                    //页面搜索框的战区下拉列表
                    $(".theaterLists").append(
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
//加载添加模态框下拉战区列表
function listAllTheaters() {
    $.ajax({
        url: '/warzone/getAllWarzones',
        data: {},
        type: "post",
        dataType: "json",
        success: function(data) {
            if (data.flag == "success") {
                $(".theaterNameLists").html(" ")
                var list = data.Data;

                for (var i = 0; i < list.length; i++) {

                    //添加会场的战区下拉列表
                    $(".theaterNameLists").append(`
                         <option alt="${list[i].zoneid}">${list[i].zonename}</option>
                    `)

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


//根据战区查询会场
function conferenceBytheater(theaterID, pagenum) {
    $.ajax({
        url: '/conference/getAllConferencesByZoneID',
        data: {
            "zoneid": theaterID,
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
                        `<tr>  
                        <td>${num}</td>   
                        <td class='meetingId' style="display:none">${list[i].conferenceid}</td> 
                        <td class='theaterId' style="display:none">${list[i].zoneid}</td>     
                        <td class='theaterName'>${list[i].zonename}</td>  
                        <td class='meetingName'>${list[i].conferencename}</td> 
                        <td class='serverIp'>${list[i].serverip}</td> 
                        <td class='addTime'>${data.time[i]}</td> 
                        <td><i class="fa fa-edit modify" data-toggle="modal" data-target="#modifyMeetingModal" title="修改"></i>
                            <i class="fa fa-trash-o deleteMeeting" title="删除"></i>
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
                                var theaterID = $('.theaterLists option:selected').attr("alt");
                                conferenceBytheater(theaterID, obj.curr)
                            }
                        }
                    });
                })
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



//添加会场
function addMeeting(theaterID, theaterName, meetingName, serverIp) {
    $.ajax({
        url: "/conference/addConference",
        data: {
            "zoneid": theaterID,
            "zonename": theaterName,
            "conferencename": meetingName,
            "serverip": serverIp
        },
        type: "post",
        success: function(data) {
            if (data.flag == "success") {
                layer.msg('添加失败', {
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


//修改会场信息
function amend(meetingId, theaterID, meetingName, serverIp) {
    $.ajax({
        url: '/conference/editConference',
        type: 'post',
        data: {
            "conferenceid": meetingId,
            "zoneid": theaterID,
            "conferencename": meetingName,
            "serverip": serverIp
        },
        success: function(data) {
            if (data.flag == "success") {
                layer.msg('修改失败', {
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

//删除会场
function deleteMeeting(meetingID) {
    $.ajax({
        url: '/conference/deleteConference',
        data: {
            "conferenceid": meetingID
        },
        type: "post",
        success: function() {
            if (data.flag == "success") {
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




$(function() {

    //查询所有会场信息
    getAllMeeting(1)

    //加载提示框
    layui.use('layer', function() {
        layer = layui.layer;
    });

    //加载查询功能下拉战区列表
    queryAllTheaters()
    //加载添加模态框下拉战区列表
    $("#addMeetingModal").on("show.bs.modal", function() {
        listAllTheaters()
    })

    //点击战区查询会场
    $(".theaterLists").on("change", function() {
        var theaterID = $('.theaterLists option:selected').attr("alt");
        var theaterName = $('.theaterLists option:selected').val();
        if (theaterName == "全部会场") {
            getAllMeeting(1)
        } else {
            conferenceBytheater(theaterID, 1)
        }
    })


    //添加会场
    $(".confirmAdd").on('click', function() {
        var theaterID = $('.theaterNameLists option:selected').attr("alt");
        var theaterName = $(".theaterNameLists").val();
        var meetingName = $(".addMeetingName").val();
        var serverIp = $(".serverIP").val();

        //会场名称非空验证
        if (meetingName == "" || meetingName == null) {
            layer.msg('会场名称不能为空', {
                offset: "3.8%",
            });
            return false;
        }

        //服务器ip验证
        //服务器非空验证
        if (serverIp == "" || serverIp == null) {
            layer.msg('服务器IP不能为空', {
                offset: "3.8%",
            });
            return false;
        }
        //服务器IP格式验证
        var reg = /^(?:(?:2[0-4][0-9]\.)|(?:25[0-5]\.)|(?:1[0-9][0-9]\.)|(?:[1-9][0-9]\.)|(?:[0-9]\.)){3}(?:(?:2[0-4][0-9])|(?:25[0-4])|(?:1[0-9][0-9])|(?:[1-9][0-9])|(?:[1-9]))$/;
        var re = new RegExp(reg);
        if (!re.test(serverIp)) {
            layer.msg('请填写有效的IP地址', {
                offset: "3.8%",
            });
            return false;
        }


        addMeeting(theaterID, theaterName, meetingName, serverIp)
        window.location.reload();
    })


    //修改会场
    $('tbody').delegate(".modify", "click", function() {
        //获取原来的会场信息
        var amendTheaterID = $(this).parents('tr').children('.theaterId').text(),
            amendTheaterName = $(this).parents('tr').children('.theaterName').text(),
            meetingId = $(this).parents('tr').children('.meetingId').text(),
            amendMeeting = $(this).parents('tr').children('.meetingName').text(),
            amendServerip = $(this).parents('tr').children('.serverIp').text();
        //填充原来的会场信息
        $(".amendTheaterID").val(amendTheaterID);
        $(".amendTheaterName").val(amendTheaterName);
        $(".amendMeeting").val(amendMeeting);
        $(".amendMeeting").attr("alt", meetingId);
        $(".amendServerip").val(amendServerip);
    })

    $(".confirmAmend").on("click", function() {
        //修改会场信息
        var theaterID = $(".amendTheaterID").val(),
            meetingName = $(".amendMeeting").val(),
            meetingId = $(".amendMeeting").attr("alt"),
            serverIp = $(".amendServerip").val();

        //会场名称非空验证
        if (meetingName == "" || meetingName == null) {
            layer.msg('会场名称不能为空', {
                offset: "3.8%",
            });
            return false;
        }
        //服务器ip验证
        //服务器非空验证
        if (serverIp == "" || serverIp == null) {
            layer.msg('服务器IP不能为空', {
                offset: "3.8%",
            });
            return false;
        }
        //服务器IP格式验证
        var reg = /^(?:(?:2[0-4][0-9]\.)|(?:25[0-5]\.)|(?:1[0-9][0-9]\.)|(?:[1-9][0-9]\.)|(?:[0-9]\.)){3}(?:(?:2[0-4][0-9])|(?:25[0-4])|(?:1[0-9][0-9])|(?:[1-9][0-9])|(?:[1-9]))$/;
        var re = new RegExp(reg);
        if (!re.test(serverIp)) {
            layer.msg('请填写有效的IP地址', {
                offset: "3.8%",
            });
            return false;
        }

        amend(theaterID, meetingId, meetingName, serverIp)
        window.location.reload();

    })


    //删除会场
    $('tbody').delegate(".deleteMeeting", 'click', function() {
        layer.confirm('是否删除该会场信息？', { offset: "10%" }, function(index) {
            var meetingID = $(this).parents("tr").children(".meetingId").text()
            deleteMeeting(meetingID)
            window.location.reload();
            layer.close(index);
        })
    })
    /*------------------------------------------动态样式部分------------------------------------------*/

    //点击查询按钮样式
    $(".addBtn").on("mousedown", function() {
        $(this).css({
            "border": "1px solid #00aeef",
            "background": "#05b35d"
        })
    })
    $(".addBtn").on("mouseup", function() {
        $(this).css({
            "border": "1px solid transparent",
            "background": "#00ce68"
        })
    })
    //顶部会场下拉框样式
    $(".theaterLists").on("focus", function() {
        $(this).css({
            "border": "1px solid #00aeef",
        })
    })
    $(".theaterLists").on("blur", function() {
        $(this).css({
            "border": "1px solid #ccc",
        })
    })
    //修改弹框样式
    $(".amendMeeting").on("focus", function() {
        $(this).css({
            "border": "1px solid #00aeef",
        })
    })
    $(".amendMeeting").on("blur", function() {
        $(this).css({
            "border": "1px solid #ccc",
        })
    })
    $(".amendServerip").on("focus", function() {
        $(this).css({
            "border": "1px solid #00aeef",
        })
    })
    $(".amendServerip").on("blur", function() {
        $(this).css({
            "border": "1px solid #ccc",
        })
    })
    //添加弹框样式
    $(".theaterNameLists").on("focus", function() {
        $(this).css({
            "border": "1px solid #00aeef",
        })
    })
    $(".theaterNameLists").on("blur", function() {
        $(this).css({
            "border": "1px solid #ccc",
        })
    })

    $(".addMeetingName").on("focus", function() {
        $(this).css({
            "border": "1px solid #00aeef",
        })
    })
    $(".addMeetingName").on("blur", function() {
        $(this).css({
            "border": "1px solid #ccc",
        })
    })
    $(".serverIP").on("focus", function() {
        $(this).css({
            "border": "1px solid #00aeef",
        })
    })
    $(".serverIP").on("blur", function() {
        $(this).css({
            "border": "1px solid #ccc",
        })
    })
    /*------------------------------------------动态样式部分结束------------------------------------------*/

})