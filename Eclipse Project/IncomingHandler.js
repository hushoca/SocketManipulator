function HandleRequest(request){
	console.log("Request incoming:\n" + request);	
	var val = "PARAM_TARGET_ADDR : " + PARAM_TARGET_ADDR + "\nPARAM_TARGET_NAME: " + PARAM_TARGET_NAME + "\nPARAM_TARGET_PORT" + PARAM_TARGET_PORT + "\nPARAM_LOCAL_PORT" + PARAM_LOCAL_PORT;
	return request;//request.replace("on!", val);
}	