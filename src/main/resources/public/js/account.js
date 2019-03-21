//展示用户列表
function getAllUsers(pagenum) {
    $.ajax({
        url: '/user/getAllUsers',
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
                        `<tr id="${list[i].uid}">  
                        <td>${num}</td>   
                        <td class='accountName'>${list[i].username}</td>   
                        <td class='accountPass'>${list[i].password}</td>  
                        <td class='role'>${list[i].rolename}</td> 
                        <td class='lastTime'>${list[i].lastlogintime}</td> 
                        <td class='lastIP'>${list[i].lastloginip}</td> 
                        <td><i class="fa fa-edit modify" data-toggle="modal" data-target="#modifyUserModal" title="修改"></i>
                            <i class="fa fa-trash-o deleteUser" title="删除"></i>
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
                                getUserLog(obj.curr)
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


//分页
function pageJump() {
    $.ajax({
        url: "/user/getAllUsers",
        type: "post",
        data: {},
        dataType: "json",
        success: function(data) {
            //分页
            if (data.flag == "success") {
                var list = data.Data;
                var countpage = data.countpage,
                    count = data.count;
                $('#box').paging({
                    initPageNo: 1, // 初始页码
                    totalPages: countpage, //总页数
                    totalCount: '合计' + count + '条数据', // 条目总数
                    slideSpeed: 600, // 缓动速度。单位毫秒
                    jump: true, //是否支持跳转
                    callback: function(page) { // 回调函数
                        getAllUsers(page)

                    }
                })
            } else {

            }

        }
    })
}

//添加账户
function addUser(username, password, rolename) {
    $.ajax({
        url: "/user/addUsers",
        data: {
            "username": username,
            "password": password,
            "rolename": rolename
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

//删除账户
function deleteUser(userid) {
    $.ajax({
        url: '/user/deleteUser',
        data: {
            "id": userid
        },
        type: "post",
        success: function() {
            if (data.flag == "success") {
                layer.msg('删除成功', {
                    offset: "3.8%",
                });
            }else{
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


//修改用户信息
function amend(username, newPass, newRole) {
    $.ajax({
        url: '/user/editUser',
        type: 'post',
        data: {
            "username": username,
            "password": newPass,
            "rolename": newRole
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



$(function() {

    //查询所有用户信息
    getAllUsers(1)
    //加载提示框
    layui.use('layer', function() {
        layer = layui.layer;
    });

    //添加账户
    $(".confirmAdd").on('click', function() {
        var username = $(".userName").val();
        var userpassword = $(".userPassword").val();
        var rolename = $(".userRole").val();

        //账户密码非空验证
        if (username == "" || username == null) {
            layer.msg('账户名称不能为空', {
                offset: "3.8%",
            });
            return false;
        } else if (userpassword == "" || userpassword == null) {
            layer.msg('账户密码不能为空', {
                offset: "3.8%",
            });
            return false;
        } else if (rolename == "" || rolename == null) {
            layer.msg('账户权限不能为空', {
                offset: "3.8%",
            });
            return false;
        }

        addUser(username, userpassword, rolename);
        window.location.reload();
    })


    //删除账户
    $('tbody').delegate(".deleteUser", 'click', function() {

        layer.confirm('是否删除该账户信息？', {offset: "10%"},function(index) {
            var userid = $(this).parents("tr").attr("id")
            deleteUser(userid)
            window.location.reload();
            layer.close(index);
        })
    })


    //修改用户
    $('tbody').delegate(".modify", "click", function() {
        //获取原来的账户密码
        var amendId = $(this).parents('tr').attr('id'),
            amendName = $(this).parents('tr').children('.accountName').text(),
            amendPass = $(this).parents('tr').children('.accountPass').text(),
            amendRole = $(this).parents('tr').children('.role').text();
        //填充原来的账户和权限
        $(".amendName").val(amendName);
        //$(".amendPass").val(amendPass);
        $(".amendRole").find("option:contains(" + amendRole + ")").attr("selected", true)

    })

    $(".confirmAmend").on("click", function() {
        //修改密码
        var username = $(".amendName").val(),
            newPass = $(".amendPass").val(),
            newRole = $(".amendRole").val();
        //检查非空
        if (newPass == "" || newPass == null) {
            layer.msg('密码不能为空', {
                offset: "3.8%",
            });
            return false;
        }
        amend(username, newPass, newRole)
        window.location.reload();

    })


    /*------------------------------------------动态样式部分------------------------------------------*/


    //添加账户弹框样式
    $(".userName").on("focus", function() {
        $(this).css({
            "border": "1px solid #00aeef",
        })
    })
    $(".userName").on("blur", function() {
        $(this).css({
            "border": "1px solid #ccc",
        })
    })
    $(".userPassword").on("focus", function() {
        $(this).css({
            "border": "1px solid #00aeef",
        })
    })
    $(".userPassword").on("blur", function() {
        $(this).css({
            "border": "1px solid #ccc",
        })
    })
    $(".userRole").on("focus", function() {
        $(this).css({
            "border": "1px solid #00aeef",
        })
    })
    $(".userRole").on("blur", function() {
        $(this).css({
            "border": "1px solid #ccc",
        })
    })
    //修改账户弹框样式
    $(".amendPass").on("focus", function() {
        $(this).css({
            "border": "1px solid #00aeef",
        })
    })
    $(".amendPass").on("blur", function() {
        $(this).css({
            "border": "1px solid #ccc",
        })
    })
    $(".amendRole").on("focus", function() {
        $(this).css({
            "border": "1px solid #00aeef",
        })
    })
    $(".amendRole").on("blur", function() {
        $(this).css({
            "border": "1px solid #ccc",
        })
    })

    /*------------------------------------------动态样式部分结束------------------------------------------*/


})