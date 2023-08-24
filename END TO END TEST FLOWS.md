# Swagger Petstore Technical Assignment
Job candidate: Piotr Szczepaniak

## Suggested complete test flows for the specified endpoints

#### Preface

Since the Swagger API chosen for this assignment mimics a pet store it would be a good idea to cover some `end-to-end` test flows that combine multiple operations as they would happen in real life situation. These are a couple of proposed end-to-end scenarios.


### Create - Update - Delete flow

#### Suggested structure:
1. `POST` - Add a new pet to the store
2. `GET` - Retrieve the pet and validate data
3. `POST` - Update existing pet
4. `GET` - Retrieve the pet and validate data
5. `DELETE` - Remove the pet from the store
6. `GET` - Confirm that no data exists for that petId

***

### Create with status - Update status - Search
#### Suggested structure:
1. `POST` - Add a new pet to the store with specific status
2. `GET` - Retrieve the pets, including the recently added, with the expected status
3. `POST` - Update existing pet with a new status
4. `GET` - Repeat the request  from Step 2 and confirm the pet doesn't show up in the results
5. `GET` - perform another "search by status" request and this time the pet added in Step 1 should be included
