

	//设置iframe高度等于设备高度
	function changeFrameHeight(){
			    var ifm= document.getElementById("iframepage"); 
			    ifm.height=document.documentElement.clientHeight-68;
			}


$(function() {

	//设置iframe高度等于设备高度
	changeFrameHeight()
	//窗口大小改变，iframe高度自适应
	window.onresize=function(){  
	     changeFrameHeight();  
	}

	//填充用户名，未登录跳转到登录页
	 var username=sessionStorage.getItem("username")
     if(username){
     	$(".username").text(username)
     }else{
     	window.location.href="login.html"
     }


	//退出登录
    $(".quit").on("click", function() {
        window.location.href = "login.html"
    })


})