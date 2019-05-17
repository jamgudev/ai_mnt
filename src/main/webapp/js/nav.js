$(function(){
    //安保人员名单
    (function(){
        var begin = 1;//开始消除起点
        var end = 5;//最终消除点
        var page = 1;
        var length = $(".security-list .list-info").length;
        for(var i = 0 ; i < 5; i++){
            $(".security-list .list-info").eq(i).css("display" , " table-row");
        }
        $("#pre").on("click" , function(){
            if(begin-5 >= 0){
                for(var j = begin ; j <=  end ; j++){
                    $(".security-list .list-info").eq(j-1).css("display" , "none" );
                }
                begin -= 5;
                end -= 5;
                for(var i = begin ; i <= end; i++){
                    $(".security-list .list-info").eq(i-1).css("display" , "table-row");
                }
                page  -= 1;
                $("#page-num").html(page);
            }else{
                begin  = 1;
            }
        });
        $("#next").on("click" , function(){
            if( end-length < 0  ){
                //清楚上一页
                for(var i = begin ; i <= end; i++){
                    $(".security-list .list-info").eq(i-1).css("display" , "none");
                }
                //显示下一页
                begin += 5;
                end += 5;
                for(var j = begin ; j <=  end ; j++){
                    $(".security-list .list-info").eq(j-1).css("display" , " table-row");
                }
                page  += 1;
                $("#page-num").html(page);
            }
        });
    }());
});