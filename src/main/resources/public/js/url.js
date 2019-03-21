$(function() {
    var iframeUrl = {
        "home": {
            "homepage":"home.html",
            "homepage_map": "home_map.html"
        },
        "state": {
            "chainQuality": "chainQualtiy.html"
        },
        "systemConfig": {
            "videoSource": "videoSource.html"
        },
         "fault": {
            "timeFault": "timeFault.html",
            "defect":"defect.html"
        },
        "systemManage": {
            "device": "device.html",
            "meeting": "meetingPlace.html",
            "account": "account.html",
            "log": "log.html",
            "chain": "chain.html",
            "localChannel":"localChannel.html"
        }
    }


    $(".layui-nav-child dd").on("click", function() {
        var id = $(this).attr("id"),
            parentId = $(this).parents("li").attr("id"),
            src = parentId + "/" + iframeUrl[parentId][id];
        $(".layui-tab-content iframe").attr("src", src);
    })

})