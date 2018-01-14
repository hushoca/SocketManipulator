/*Available Global variables are:
	PARAM_TARGET_ADDR : IP address of the target.
	PARAM_TARGET_NAME : Name of the target.
	PARAM_TARGET_PORT : Target port.
	PARAM_LOCAL_PORT : Local listening port.

	console.log() can be used to log data to the console.

	This function handles the request that was sent from the local to target.
	(Example: localhost -> google)
	
*/
function HandleRequest(request){
	console.log("Received Request:\n" + request + "\n\n");	
	return request;
}	