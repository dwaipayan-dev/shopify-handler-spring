# shopify-handler-spring

### Assumptions:

1. I have assumed that webhook will send request to my server with it's respective payload
2. The identifying key in orders and abandonedcheckout payload is cart_token
3. I have only used inmemory datastructures to reduce dependency and easy testing.
4. Smallest unit of an interval is minute.

### Prerequisites

1. JDK 11
2. Gmail account with app password set up for mail notifications.

### Secret

1. EMAIL_USER: environment variable to store gmail sender account id
2. EMAIL_APP_PASSWORD: environment variable to store 16 character app password(gmail generated)

Please store these environment variables so they are available in the OS.

### APIS

1. /abandoned/subscribe (method: POST): The abandoned checkout webhook will make a call to this API with it's payload. Then the server will put it in the scheduling queue, using it's predefined/customizable schedule, and send email to user with the recoveryUrl.

2. /abandoned/unsubscribe (method: POST): The order placed webhook will make a call to this API with it's payload. Then the server will not send the cart_token's user any more notifications.

3. /abandoned/allmessages (method: GET): The UI polls this API every 5 seconds to update the dashboard of sent messages.

4. /abandoned/setscheduled (method: POST): Payload is in the format 

```
[{
    "days": 0,
    "hours": 0,
    "minutes": 30
},{
    "days": 1,
    "hours": 0
    "minutes": 0
}, {
    "days": 3,
    "minutes": 0
}]

```

Overwrites the schedule array with the new schedule. The calls following the old schedule will only happen if the new schedule size >= old schedule size. Else they would not occur.
