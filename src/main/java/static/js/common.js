var empty = function(str) {
	return str == null || "" == str;
};

/**
 * obj.url：链接
 * obj.data：参数
 * obj.success：回功回调函数
 * obj.error：失败回调函数
 * obj.redirectCode：跳转代码，默认为200
 * obj.redirectUrl：跳转url
 */
var loadData = function(obj) {
	$.ajax({
		"type" : "POST",
		"cache" : false,
		"async" : true,
		"contentType" : "application/x-www-form-urlencoded",
		"dataType" : "json",
		"beforeSend" : function(){ //请求之前调用
			$("body").append('<div id="loadingLevel_blockOverlay" style="z-index: 2001; border: none; margin: 0px; padding: 0px; width: 100%; height: 100%; top: 0px; left: 0px; opacity: 0.4; filter: alpha(opacity=40); cursor: default; position: fixed; background-color: rgb(255, 255, 255);"></div>');
		},
		"complete" : function(){ //请求完成后调用，无论成功还是失败
			$("#loadingLevel_blockOverlay").remove();
		},
		"url" : obj.url,
		"data" : obj.data,
		"success" : function(data, textStatus) {
			if(obj.redirectCode == null) {
				obj.redirectCode = 200;
			}
			if(obj.redirectUrl != null) {
				if(obj.redirectCode == data.code) {
					window.location.href = obj.redirectUrl;
					return;
				}
			}
			obj.success(data, textStatus); //请求成功后调用
		},
		"error" : obj.error //请求失败后调用，参数：XMLHttpRequest, textStatus, errorThrown
	});
};
