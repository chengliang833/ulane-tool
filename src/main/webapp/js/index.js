//var baseUrl = "http://localhost:18030";
var baseUrl = "http://wx.ulane.top";

var token;
var code;

$(function(){
	console.log("load finish...");
	code = GetQueryString("code");
	messageShow(code?code:"no code");
});


function login(){
//	sendRequest(baseUrl+"/login/sendSms", null, {
//			  "mobileNo": "15579155987"
//		}, function(data){
//			console.log("sendSms...");
		sendRequest(baseUrl+"/login/user/loginSms", null, {
			"mobileNo": "15579155987",
			"verifyCode": "111111"
		}, function(data){
			token = data.autoLoginToken;
			$("#cusToken").val(token);
		});
//	});
}

function loginWithToken(){
	let cusToken = $("#cusToken").val();
	sendRequest(baseUrl+"/tag/qryList", {"token":cusToken}, {}, function(data){
		console.log("loginWithToken...");
	})
}

function index(){
	sendRequest(baseUrl+"/tag/qryList", null, {}, function(data){
		console.log("index...");
	})
}

function logout(){
	sendRequest(baseUrl+"/logout", null, {}, function(data){
		console.log("logout...");
	})
}

function clearToken(){
	token = null;
}







function messageShow(msg){
	$("#message").text(msg);
}

function sendRequest(url, headers, param, success){
	messageShow("");
	if(!headers){
		headers = {};
	}
	headers.channel = 1;
	if(!headers.token && token){
		headers.token = token;
	}
	if(!headers.code && code){
		headers.code = code;
	}
    $.ajax({
        type : "POST",
        url : url,
		xhrFields:{
			withCredentials:true
		},
//        headers: headers, //这种无效, 不知道为啥
        beforeSend: function(request) {
        	for(let id in headers){
        		request.setRequestHeader(id, headers[id]);
        	}
//            request.setRequestHeader("Content-Type","application/json");
        },
        data : JSON.stringify(param),
        contentType : "application/json",
        success : function(data){
            console.log(JSON.stringify(data));
            if(data.code == '00000'){
            	messageShow(url+"...success");
            	success(data.data);
            }else{
            	messageShow(data.message);
            }
        }
    });
}

function GetQueryString(name){
	let reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
	let r = window.location.search.substr(1).match(reg);
//	console.log(r);
	if(r != null){
		return unescape(r[2]);
	}
	return null;
}
