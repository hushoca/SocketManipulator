# SocketManipulator
This application allows you to act as a man in the middle and manipulate the request and response to a target. Manipulation can be easily modified using JavaScript.

The OutgoingHandler.js handles the requests going from the local machine to the target machine. Whatever is returned from the HandleRequest() function is the String which will be sent to the target and the parameter of this function is the original request which was made to the local machine.

The IncomingHandler.js handles the responses coming from the target machine to the local machine. Whatever is returned from the HandleRequest() function is the String which will be sent back the application which made the request and the parameter of this function is the original response from the target machine.

This application was written in few hours and is not production level code... So memory leaks and threads that are not closed are a casual thing. SO BEWARE!!!
