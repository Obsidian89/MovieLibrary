Assumptions:
- Assumed that there is no validation on the offer when it is created.
- That there are no downstream systems consuming information from the database
- Offers expire at the end of day.
- Requests are done by id and not be name or date.

Choices:
- Spring boot, easily configured and reduces need for boiler plate code.
- MongoDB, allows for flexible adding of new fields, supported by spring and scalable.
- Updating offer to expire when doing a GET, I assumed that there is no other systems using the
data and the volumes of offers stored is relatively low the overhead of checking each offer before
returning the offers. I would preferably use some other thread or maybe a database function to handle
the expiry logic.

Improvements
Given more time I would improve this by:
- Increased error handling, including testing around error scenarios
- Meaningful response messages
- Use more constants (especially in the unit tests)
- Validation to the offers being created
- Integration test, that call into the service and check full end to end with a test database