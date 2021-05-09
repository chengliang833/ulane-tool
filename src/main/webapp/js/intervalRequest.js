var inProceed = false;
setInterval(function() {
	if(!inProceed){
		inProceed = true;
	    $.ajax({
	        type : "POST",
	        url : "http://...",
			xhrFields:{
				withCredentials:true
			},
	        data : JSON.stringify({}),
	        contentType : "application/json",
	        success : function(data){
	        	let res = data.data;
	        	if(res.list != null && res.list.length > 0){
	        		let record = res.list[0];
//	        		console.log(JSON.stringify(record));
	        		if(record.interfaceStatus == 3){
	        			console.log("重新计算");
	        			reDo(record);
//	        			inProceed = false;
	        		}else{
	        			console.log("等待查询");
	        			inProceed = false;
	        		}
	        	}else{
	        		console.log("无返回值");
	        		inProceed = false;
	        	}
	        }
	    });
	}
}, 3000);

function reDo(record){
	let param = {
		};
    $.ajax({
        type : "POST",
        url : "http://...",
		xhrFields:{
			withCredentials:true
		},
        data : JSON.stringify(param),
        contentType : "application/json",
        success : function(data){
			inProceed = false;
        	console.log("请求："+JSON.stringify(data));
        }
    });
}