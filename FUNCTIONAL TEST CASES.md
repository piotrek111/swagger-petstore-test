# Swagger Petstore Technical Assignment
Job candidate: Piotr Szczepaniak

## Suggested functional test cases for the specified endpoints
## Section 1

### Test cases for ***Image Upload*** functionality

**Endpoint:** [POST/pet/{petId}/uploadImage](POST/pet/{petId}/uploadImage)
***

#### Case 1: Upload valid image for an existing ```petId```
##### Preconditions:
- there is an existing pet in the petstore
- valid image file (i.e. supported format & size) is available
- no additional metadata provided
##### Steps:
- trigger ```POST``` request with a valid ```petId``` as the path parameter and valid image file attached as form data
- check the server response
##### Expected results:
- Server returns 200 OK for the ```POST``` request
- response is of JSON type
- response body contains name and size of  the uploaded file 

***

#### Case 2: Upload valid image for a non-existing ```petId```
This is a theoretical test case, in reality the example API doesn't care about existing `petId`
##### Preconditions:
- valid image file (i.e. supported format & size) is available
- no additional metadata provided
##### Steps:
- trigger ```POST``` request with a non-existing ```petId``` as the path parameter and valid image file attached as form data
- check the server response
##### Expected results:
- Server returns error code 400
- response is of JSON type
- response body ideally contains a short descriptive error message, i.e. *invalid petId* or something similar

***

#### Case 3: Upload invalid image for an existing ```petId```
This is another theoretical test case - the example API doesn't care about image file validation
##### Preconditions:
- there is an existing pet in the petstore
- invalid image file (i.e. unsupported format or too big file size) is available
- no additional metadata provided
##### Steps:
- trigger ```POST``` request with a valid ```petId``` as the path parameter and invalid image file attached as form data
- check the server response
##### Expected results:
- response is of JSON type
- Server returns error code, most likely `413 -  Content Too large if file is too big` or `415 - Unsupported media type` if file type is not supported
- Response body ideally contains a short descriptive error message explaining why the file was not accepted

***

#### Case 4: Upload empty form data for an existing ```petId```
##### Preconditions:
- no image file or additional metadata is used
##### Steps:
- trigger ```POST``` request with a valid ```petId``` as the path parameter and empty form data
- check the server response
##### Expected results:
- response is of JSON type
- Server returns error code 400
- response body ideally contains a short descriptive error message, i.e. *missing petId and image file* or something similar

***

#### Case 5: Upload valid image and metadata for an existing ```petId```
##### Preconditions:
- there is an existing pet in the petstore
- valid image file (i.e. supported format & size) is available
- additional metadata string is provided
##### Steps:
- trigger ```POST``` request with a valid ```petId``` as the path parameter plus metadata string and valid image file attached as form data
- check the server response
##### Expected results:
- response is of JSON type
- Server returns 200 OK for the ```POST``` request
- response body contains metadata string as well as name and size of  the uploaded file

***

#### Case 6: Upload additional valid image for an existing ```petId```
##### Preconditions:
- there is an existing pet in the petstore
- the ```petId``` already has one image associated with it
- valid image file (i.e. supported format & size) is available
##### Steps:
- trigger ```POST``` request with a valid ```petId``` as the path parameter and valid image file attached as form data
- check the server response
##### Expected results:
- response is of JSON type
- Server returns 200 OK for the ```POST``` request
- response body contains name and size of the newly uploaded file

*** 

##### Additional scenarios to consider:
1. **Action**: Upload multiple image files for the same `petId`. **Expected**: the API probably should have a limit on the number of image uploads for a single pet item. Once the limit is reached either: an error is returned **OR** the oldest image link is replaced with the most recent upload
2. **Action**: Test various formats of `petId` (valid and invalid ones). **Expected**: Invalid petId parameters (i.e. not conforming to required length / format) should ideally trigger error response from the server, like `400 - Invalid petId parameter` 

***

## Section 2
### Test cases for *Adding a new Pet in the Petstore* functionality

**Endpoint:** [/pet/{petId}](/pet/{petId})

***

#### Case 1: Add a new pet with minimum required parameters (_name_ and one _image link_ available)
##### Preconditions:
- `petId` for this request hasn't been used before (i.e. there's no existing pet with this id)
- JSON payload for the request has only `name` and a single url string in `photoUrls` array
##### Steps:
- Perform POST request with specified petId and JSON payload
##### Expected results:
- Server returns 200 - OK
- response body contains JSON payload with the supplied values for `petId`, `name` and image url

***

#### Case 2: Add a new pet with additional (optional) fields in the body
##### Preconditions:
- `petId` for this request hasn't been used before (i.e. there's no existing pet with this id)
- JSON payload for the request has `name`, at lest one image link in `photoUrls` array as well as `category` object and at least one `tag`
##### Steps:
- Perform POST request with specified petId and JSON payload
##### Expected results:
- Server returns 200 - OK
- response body contains JSON payload with all the supplied information

***

#### Case 3: Add a pet for an existing `petId`
Note: it looks like the example API simply replaces the previous entry with the new one. So this case is a theoretical one.
##### Preconditions:
- the petId already exists
- JSON payload for this request contains at least the minimum required information (`name` and one image link)
##### Steps:
- Perform POST request with specified `petId` and JSON payload
##### Expected:
- Server return `405 - Invalid input` (?)
- Response contains rejected JSON payload with error information (?)
##### Additional notes:
- For this particular test case it's not clear from just reading Swagger examples what should be the error message. I would expect a custom message like `petId already in use` or something similar  

***

#### Case 4: add a new pet with a mix of valid in invalid data in the JSON payload
##### Preconditions:
- `petId` for this request hasn't been used before (i.e. there's no existing pet with this id)
- JSON payload for the request has `name`, at lest one image link in `photoUrls` array as well as some additional info
- at least one of the payload elements doesn't match the expected format/value (i.e. a decimal number is used in `category.id`)
##### Steps:
- Perform POST request with specified `petId` and JSON payload
##### Expected:
- Server return `405 - Invalid input`
##### Additional notes:
- Ideally, the response payload should indicate the error in a more specific way, i.e. `Invalid category id. Must be a positive integer`.

***

##### Additional scenarios to consider:
- since this request expects JSON payload with quite a few elements, the **Test Case 4** could be repeated with several different variations of the payload to ensure good coverage.

## Section 3
### Test cases for **_Finding pet by status_** 

**Endpoint:** [GET/pet/findByStatus](GET/pet/findByStatus)

***

#### Case 1: Find pets within a single valid status
##### Preconditions:
- there are pets existing for the chosen status
##### Steps:
- Perform `GET` request with a single status value as the query parameter
##### Expected results:
- Server returns `200 - OK`
- List (array of JSON objects) is returned for all the pets that exist with this status
##### Additional notes:
- Ideally, this test case could be executed three times for each supported category (_available_, _pending_, _sold_)

***

#### Case 2: Find pets that match all valid statuses
##### Preconditions:
- there are pets existing for all the supported statuses
##### Steps:
- Perform `GET` request with all three supported categories in the query string
##### Expected results:
- Server returns `200 - OK`
- List (array of JSON objects) is returned for all the pets that match supported status values 

***

#### Case 3: Search for pets with non-existing status value
Note: it looks that the API accepts anything and if there's no match it will simply return an empty array.
##### Preconditions:
- status name is neither _available_ nor _pending_ nor _sold_
##### Steps
- Perform `GET` request with a single, unsupported status (i.e. _missing_)
##### Expected results:
- Server returns `400 - Invalid status value`

***

#### Case 4: Search for pets with a mix of valid and invalid statuses
##### Preconditions:
- one valid status name is used (i.e. _sold_)
- one invalid status name is used as well (i.e. _missing_)
##### Steps:
- Perform `GET` request with both statuses in the query string array
##### Expected results:
- Server returns `200 - OK`
- List (array of JSON objects) is returned only for all the pets that match supported status value(s). Invalid entries are ignored.

***

## Section 4
### Test cases for **_Delete operation_**

**Endpoint:** [DELETE/pet/{petId}](DELETE/pet/{petId})

***

#### Case 1: Delete an existing `petId`
##### Preconditions:
- there is an existing pet in the petstore
##### Steps:
- perform DELETE request with the existing `petId` specified as path parameter
- follow up with a GET request for the previously deleted `petId`
##### Expected results:
- Server returns `200 - OK` for the `DELETE` request
- Server returns `404 - Not found` for the `GET` request

***

#### Case 2: Delete a non-existing `petId`
##### Preconditions:
- the `petId` is used is valid but longer exists in the database
##### Steps:
- perform `DELETE` request with the specified `petId` as the path parameter
##### Expected results:
- Server returns `404 - Not found`

***
#### Case 3: Delete with an invalid `petId`
##### Preconditions:
- the petId used in the request doesn't conform to the expected length / format
##### Expected results:
- Server returns `400 - Bad request`
