## JsonMasking the easy way ##
Sometimes we may find ourselves in a situation where we have a Json String, and we want to mask some particular field or particular array, without actually using object binding..(Actualy we may not even have the object in our code), but even if we do, the binding process is a heavy one that includes many reflection operations, and performance is lost. 

A common solution is to use Regex's to do the job, however regex are not very fast, and in many situtations are cumbersome to create and we may endup loosing precious time.

I've done some tests with Gson, Json-P and Jackson, and for this situation, Jackson was a clear winner, so we used their API to get most of the work done for us :).

JsonMask, let's you mask a Json string directly by operating over the Json tree. We will only need to specifiy the path(s) of the key(s) you want to mask, the key(s) to mask, and any optional mask(s) (if no mask is supplied a default of ***** will be used.

How to use :

A sample Json message : 

	String JSON_MESSAGE = {
	  "httpStatusCode": 200,
	  "resultSet": {
	    "result": {
	      "totalSize": 1,
	      "records": [
	        {
	          "attributes": {
	            "type": "Asset",
	            "url": "http://google.com"
	          },
	          "Name": "Some test name",
	          "type": "Animal"
	        }
	      ],
	      "done": true
	    }
	  },
	  "messages": [],
	  "startedTime": 1441969739375,
	  "endTime": 1441969750317
	}

let's say we want to mask the totalSize value on the result Array.

We start by defining a list of the Masks, specifying the path to arrive to the object where the key we want to mask is. In this case the path will be ***resultSet/Result***, then we tell the key we want to mask, which is is ***totalSize**

	 final List<JsonMask> jsonMasks = new ArrayList<>(Arrays.asList(new JsonMask("resultSet/result", "totalSize"));

now we call the masker

	JsonMaskResponse result = JsonMaskService.mask(JSON_MESSAGE, jsonMasks);

and we get the masked String in the **jsonMessage** field the Object JsonMaskResponse.

	{
	  "httpStatusCode" : 200,
	  "resultSet" : {
	    "result" : {
	      "totalSize" : "********",
	      "records" : [ {
	        "attributes" : {
	          "type" : "Asset",
	          "url" : "http://google.com"
	        },
	        "Name" : "Some test name",
	        "type" : "Animal"
	      } ],
	      "done" : true
	    }
	  },
	  "messages" : [ ],
	  "startedTime" : 1441969739375,
	  "endTime" : 1441969750317
	}

You can find more examples in the tests.

This is a work in progress, some cases may not be covered currently, an I hope to find the time to further improve the code.

### Benchmarks ###

if you run the App, you will find a very simple benchmark. The first iteration is to warmup the JVM, so that it does it's magic, compiling and organizing the bytecode in the most convenient way. Currently the following values are being achieved. As yo can easily see, as soon as the JVM does its magic...things become quite fast.

	WARM UP JVM -> Masking 5000 times a root element took  595milisecods
	RUN NUMBER :1
	Test 1 -> Masking 5000 times 2 keys on second level element took -> 47milisecods
	Test 2 -> Masking 5000 times one root element took -> 42milisecods
	RUN NUMBER :2
	Test 1 -> Masking 5000 times 2 keys on second level element took -> 37milisecods
	Test 2 -> Masking 5000 times one root element took -> 35milisecods
	RUN NUMBER :3
	Test 1 -> Masking 5000 times 2 keys on second level element took -> 32milisecods
	Test 2 -> Masking 5000 times one root element took -> 29milisecods
	RUN NUMBER :4
	Test 1 -> Masking 5000 times 2 keys on second level element took -> 22milisecods
	Test 2 -> Masking 5000 times one root element took -> 21milisecods
	RUN NUMBER :5
	Test 1 -> Masking 5000 times 2 keys on second level element took -> 20milisecods
	Test 2 -> Masking 5000 times one root element took -> 21milisecods
	RUN NUMBER :6
	Test 1 -> Masking 5000 times 2 keys on second level element took -> 19milisecods
	Test 2 -> Masking 5000 times one root element took -> 17milisecods
	RUN NUMBER :7
	Test 1 -> Masking 5000 times 2 keys on second level element took -> 20milisecods
	Test 2 -> Masking 5000 times one root element took -> 15milisecods
	RUN NUMBER :8
	Test 1 -> Masking 5000 times 2 keys on second level element took -> 15milisecods
	Test 2 -> Masking 5000 times one root element took -> 15milisecods
	RUN NUMBER :9
	Test 1 -> Masking 5000 times 2 keys on second level element took -> 15milisecods
	Test 2 -> Masking 5000 times one root element took -> 15milisecods
	RUN NUMBER :10
	Test 1 -> Masking 5000 times 2 keys on second level element took -> 15milisecods
	Test 2 -> Masking 5000 times one root element took -> 19milisecods
	RUN NUMBER :11
	Test 1 -> Masking 5000 times 2 keys on second level element took -> 15milisecods
	Test 2 -> Masking 5000 times one root element took -> 15milisecods
	RUN NUMBER :12
	Test 1 -> Masking 5000 times 2 keys on second level element took -> 15milisecods
	Test 2 -> Masking 5000 times one root element took -> 15milisecods
	RUN NUMBER :13
	Test 1 -> Masking 5000 times 2 keys on second level element took -> 15milisecods
	Test 2 -> Masking 5000 times one root element took -> 17milisecods
	RUN NUMBER :14
	Test 1 -> Masking 5000 times 2 keys on second level element took -> 21milisecods
	Test 2 -> Masking 5000 times one root element took -> 21milisecods
	RUN NUMBER :15
	Test 1 -> Masking 5000 times 2 keys on second level element took -> 20milisecods
	Test 2 -> Masking 5000 times one root element took -> 16milisecods
	RUN NUMBER :16
	Test 1 -> Masking 5000 times 2 keys on second level element took -> 14milisecods
	Test 2 -> Masking 5000 times one root element took -> 15milisecods
	RUN NUMBER :17
	Test 1 -> Masking 5000 times 2 keys on second level element took -> 14milisecods
	Test 2 -> Masking 5000 times one root element took -> 15milisecods
	RUN NUMBER :18
	Test 1 -> Masking 5000 times 2 keys on second level element took -> 14milisecods
	Test 2 -> Masking 5000 times one root element took -> 19milisecods
	RUN NUMBER :19
	Test 1 -> Masking 5000 times 2 keys on second level element took -> 14milisecods
	Test 2 -> Masking 5000 times one root element took -> 14milisecods
	RUN NUMBER :20
	Test 1 -> Masking 5000 times 2 keys on second level element took -> 14milisecods
	Test 2 -> Masking 5000 times one root element took -> 15milisecods
 
