/*Available Global variables are:
	PARAM_TARGET_ADDR : IP address of the target.
	PARAM_TARGET_NAME : Name of the target.
	PARAM_TARGET_PORT : Target port.
	PARAM_LOCAL_PORT : Local listening port.

	console.log() can be used to log data to the console.
	
	This function handles the response that was sent from the target to local.
	(Example: google -> localhost)

*/
function HandleRequest(response){
	console.log("Received Response:\n" + response + "\n\n");	
	return response;
}	