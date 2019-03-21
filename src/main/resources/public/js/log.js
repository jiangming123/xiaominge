//查询已有账户列表
function getUserList() {
    $.ajax({
        url: '/log/getAllUsers',
        data: {},
        type: "post",
        dataType: "json",
        success: function(data) {

            if (data.flag == "success") {
                var list = data.Data;

                //表格
                for (var i = 0; i < list.length; i++) {

                    $(".accountList").append(
                        `<option>${list[i].username}</option>
                     `
                    )
                }
            } else {
                $(".accountList").appnd(
                    `<option>获取账户列表失败</option>
                     `
                )
            }


        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            console.log(XMLHttpRequest.status);
            console.log(XMLHttpRequest.readyState);
            console.log(textStatus);
        }
    })
}



//查询日志信息
function queryUserLog(startTime, endTime, username, pagenum) {
    $.ajax({
        url: '/log/queryLogs',
        data: {
            "starttime": startTime,
            "endtime": endTime,
            "username": username,
            "pagenum": pagenum
        },
        type: "post",
        dataType: "json",
        success: function(data) {

            if (data.flag == "success") {
                $("tbody").html(" ")
                var list = data.Data;
                var count = data.count;
                var timelist = data.time;
                //表格
                for (var i = 0; i < list.length; i++) {

                    var num = i + (pagenum - 1) * 10 + 1; //序号

                    $("tbody").append(
                        `<tr>  
                        <td>${num}</td>   
                        <td>${list[i].logid}</td>   
                        <td>${list[i].username}</td>  
                        <td>${timelist[i]}</td> 
                        <td>${list[i].operation}</td> 
                         </tr>
                     `
                    )
                }
            } else {
                alert("查询失败")
            }
           
                //分页
                layui.use(['laypage'],function(){

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
                                    getUserLog(obj.curr)
                                }
                            }
                        });
                })
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            console.log(XMLHttpRequest.status);
            console.log(XMLHttpRequest.readyState);
            console.log(textStatus);
        }
    })
}

//展示日志信息
function getUserLog(pagenum) {
    $.ajax({
        url: '/log/getAllLogs',
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
                var timelist = data.time;
                //表格
                for (var i = 0; i < list.length; i++) {

                    var num = i + (pagenum - 1) * 10 + 1; //序号

                    $("tbody").append(
                        `<tr>  
                        <td>${num}</td>   
                        <td>${list[i].logid}</td>   
                        <td>${list[i].username}</td>  
                        <td>${timelist[i]}</td> 
                        <td>${list[i].operation}</td> 
                         </tr>
                     `
                    )
                }   


                //分页
                layui.use(['laypage'],function(){

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
                                    getUserLog(obj.curr)
                                }
                            }
                        });
                })


            } else {
                alert("查询失败")
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


     //加载时间选择器
    layui.use('laydate', function() {
        var laydate = layui.laydate;

        laydate.render({
            elem: '#startTime',
            type: 'datetime'
        });
        laydate.render({
            elem: '#endTime',
            type: 'datetime'
        });
    });

    

    //获取所有用户下拉列表
    getUserList()

    //获取日志信息
    getUserLog(1)


    //查询日志信息
    $(".queryMsg").on("click", function() {
        var startTime = $("#startTime").val(),
            endTime = $("#endTime").val(),
            username = $(".accountList").val();


        //时间前后验证
        if (startTime != "" && startTime != null && endTime != "" && endTime != null && startTime > endTime) {
            alert("开始时间不能大于结束时间")
            return false;
        }

        //username=""代表查询全部
        if (username == "全部账户") {
            username = ""
        }

        queryUserLog(startTime, endTime, username, 1)

    })




/*------------------------------------------动态样式部分------------------------------------------*/

    //点击查询按钮样式
    $(".queryMsg").on("mousedown",function(){
        $(this).css({
            "border":"1px solid #00aeef",
            "background": "#05b35d"
        })
    })
     $(".queryMsg").on("mouseup",function(){
        $(this).css({
            "border":"1px solid transparent",
             "background": "#00ce68"
        })
    })
    //点击时间框样式
    $("#startTime").on("focus",function(){
        $(this).css({
            "border":"1px solid #00aeef",
        })
    })
    $("#startTime").on("blur",function(){
        $(this).css({
            "border":"1px solid #ccc",
        })
    })
     $("#endTime").on("focus",function(){
        $(this).css({
            "border":"1px solid #00aeef",
        })
    })
    $("#endTime").on("blur",function(){
        $(this).css({
            "border":"1px solid #ccc",
        })
    })
    //下拉框样式
    $(".accountList").on("focus",function(){
        $(this).css({
            "border":"1px solid #00aeef",
        })
    })
     $(".accountList").on("blur",function(){
        $(this).css({
            "border":"1px solid #ccc",
        })
    })

    /*------------------------------------------动态样式部分结束------------------------------------------*/
})