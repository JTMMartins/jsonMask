## JsonMasking the easy way ##
Sometimes we may find ourselves in a situation where we have a Json String, and we want to mask some particular field or particular array, without actually using object binding..(Actualy we may not even have the object in our code), but even if we do, the binding process is a heavy one, and performance is lost. 

A common solution is to use Regex's to do the job, however regex are not very fast, and in many situtations are cumbersome to create and we may endup loosing precious time.

I've done some tests with Gson, Json-P and Jackson, and for this situation, Jackson was a clear winner, so we used their API to get most of the work done for us :).

JsonMask, let's you maks Json string directly, by specifiying the path of the key you want to mask, the key to mask, and an optional mask (if no mask is supplied a default of ***** will be used.

How to use :
Our Json String : 

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
