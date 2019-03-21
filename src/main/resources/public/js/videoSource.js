/*展示全部视频格式和信号列表*/
function formatsAndSignals() {
    $.ajax({
        url: "/videosource/info",
        data: {},
        type: "post",
        dataType: "json",
        success: function(data) {
            if (data.flag == "success") {
                var formats = data.videoformats,
                    signalSD = data.SDs,
                    signalHD = data.HDs;
                $(".videoFormat ul").html(" ")
                $(".SDs").html(" ")
                $(".HDs").html(" ")
                /*视频格式列表*/
                for (var i = 0; i < formats.length; i++) {
                    
                    $(".videoFormat ul").append(`
                        <li alt="${i}">${formats[i]}</li>
                    `)
                }

                /*标清信号列表*/
                for (var i = 0; i < signalSD.length; i++) {
                     
                    $(".SDs").append(`
                        <li alt="${i}">${signalSD[i]}</li>
                    `)
                }

                /*高清信号列表*/
                for (var i = 0; i < signalHD.length; i++) {
                    
                    $(".HDs").append(`
                        <li alt="${i}">${signalHD[i]}</li>
                    `)
                }
            }else if(data.flag=="failure"){
                 alert("视频源查询失败")
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            console.log(XMLHttpRequest.status);
            console.log(XMLHttpRequest.readyState);
            console.log(textStatus);
        }
    })
}


/*点击视频格式播放*/
function videoPlay(format, signal) {
    $.ajax({
        url: "/videosource/play",
        data: {
            "time": format,
            "pattern": signal
        },
        type: "post",
        dataType: "json",
        success: function(data) {},
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            console.log(XMLHttpRequest.status);
            console.log(XMLHttpRequest.readyState);
            console.log(textStatus);
        }
    })
}



$(function() {
    

    //展示全部视频格式和信号列表
    formatsAndSignals()
    
    


    //点击PAL显示标清信号列表，其他视频格式显示高清信号列表
    $(".videoFormat ul li").on("click", function() {
        if ($(this).text() == "PAL") {
            $(".SDs").css("display", "block")
            $(".HDs").css("display", "none")
        } else {
            $(".SDs").css("display", "none")
            $(".HDs").css("display", "block")
        }
    })






    //点击信号列表改变图片
    $(".signalList li").on("click", function() {

        //标清图片
        if ($(this).parents(".SDs")) {
            var photoSrc;
            var photoName = $(this).text();
            switch (photoName) {
                case "白场.bmp":
                    photoSrc = "../images/SD/白场.bmp";

                    break;
                case "彩条.bmp":
                    photoSrc = "../images/SD/彩条.bmp";

                    break;
                case "黑场.bmp":
                    photoSrc = "../images/SD/黑场.bmp";

                    break;
                case "红场.bmp":
                    photoSrc = "../images/SD/红场.bmp";

                    break;
                case "蓝场.bmp":
                    photoSrc = "../images/SD/蓝场.bmp";

                    break;
                case "绿场.bmp":
                    photoSrc = "../images/SD/绿场.bmp";

                    break;
                case "谈话.bmp":
                    photoSrc = "../images/SD/谈话.bmp";

                    break;
                default:
                    photoSrc = "../images/SD/白场.bmp";

            }
            $(".videoPhoto img").attr("src", photoSrc)
        }

        //高清图片
        if ($(this).parents(".HDs")) {
            var photoSrc;
            var photoName = $(this).text();
            switch (photoName) {
                case "白场.bmp":
                    photoSrc = "../images/HD/白场.bmp";

                    break;
                case "彩条.bmp":
                    photoSrc = "../images/HD/彩条.bmp";

                    break;
                case "黑场.bmp":
                    photoSrc = "../images/HD/黑场.bmp";

                    break;
                case "红场.bmp":
                    photoSrc = "../images/HD/红场.bmp";

                    break;
                case "蓝场.bmp":
                    photoSrc = "../images/HD/蓝场.bmp";

                    break;
                case "绿场.bmp":
                    photoSrc = "../images/HD/绿场.bmp";

                    break;
                case "谈话.bmp":
                    photoSrc = "../images/HD/谈话.bmp";

                    break;
                default:
                    photoSrc = "../images/HD/白场.bmp";

            }
            $(".videoPhoto img").attr("src", photoSrc)
        }
    })








    /*点击模块变色*/
    //视频格式模块点击变色
    $(".videoFormat").on("click", function(e) {
        e = e || window.event;
        var target = e.srcElement || e.target,
            allFormats = $(".videoFormat li");

        for (var i = 0; i < allFormats.length; i++) {
            if (target == allFormats[i]) {
                allFormats[i].className = "selectedInfo"
                allFormats[i].style.border = "1px solid #337ab7"
            } else {
                allFormats[i].className = "bg-primary"
                allFormats[i].style.border = "1px solid #f5f6f7"
            }
        }
    })
    //信号列表模块点击变色
    $(".signalList").on("click", function(e) {
        e = e || window.event;
        var target = e.srcElement || e.target,
            allSignals = $(".signalList li");

        for (var i = 0; i < allSignals.length; i++) {
            if (target == allSignals[i]) {
                allSignals[i].className = "selectedInfo"
                allSignals[i].style.border = "1px solid #337ab7"
            } else {
                allSignals[i].className = "bg-primary"
                allSignals[i].style.border = "1px solid #f5f6f7"
            }
        }
    })








    /*播放视频*/
    $(".signalList li").on("click", function() {
        var format = $(".videoFormat .selectedInfo").attr("alt"),
            signal = $(this).attr("alt");

        videoPlay(format, signal)
    })


})