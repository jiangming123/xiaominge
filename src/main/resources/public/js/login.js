
function login(username,password) {

    $.ajax({
        url: '/user/login',
        type: "post",
        data: {
            "username":username,
            "password":password
        },
        success: function(data) {
            if (data.flag =="success") {

                window.location.href = "index.html"

            } else {
                alert('用户名或者密码错误！')
                return false;
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
    //加载验证码
   //var verifyCode = new GVerify("CAPTCHA");

    //登录操作
    $('.login_button').on('click', function() {

        var username = $('#username').val(),
            password = $('#userpwd').val(),
            CAPTCHA = $("#code_input").val();

        //存储用户名
         sessionStorage.setItem("username", username); 

        //验证码
        //var res = verifyCode.validate(document.getElementById("code_input").value);

        //检查非空
        if (username != "" && password != "") {
            login(username, password) //登录方法
        } else if (username == "") {
            $(".msg p span").text("用户名不能为空！")
            $(".msg p").css("display", "block")
            return false;
        } else if (password == "") {
            $(".msg p span").text("密码不能为空！")
            $(".msg p").css("display", "block")
            return false;
        } 
        // else if (CAPTCHA == "") {
        //     $(".msg p span").text("验证码不能为空！")
        //     $(".msg p").css("display", "block")
        //     return false;
        // } else if (!res) {
        //     $(".msg p span").text("验证码错误!")
        //     $(".msg p").css("display", "block")
        //     $("#code_input").val("")
        //     return false;
        // }
    })

    //回车键登录
    $('body').keydown(function() {
        if (event.keyCode == 13) {
            $('.login_button').click();
        }
    })



    //忘记密码
    $('.forgetPwd').on('click', function() {
        $(".msg p span").text("请联系系统管理员获取密码")
        $(".msg p").css("display", "block")
    })




})