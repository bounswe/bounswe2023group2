# Add, Get, and Remove Activities API (Page by Cahid)

This API can basically add, query, and delete new resources, needs, events, and actions.

![image](https://user-images.githubusercontent.com/25232403/236641177-7085cd19-6f69-4fe7-a48c-32b010c2d06c.png)

The UI can be accessible through [here](http://13.49.41.10:8000/) (this is a separate server from my team, I initially thought we should make the dockerization and deployment phase on our own, and I did it.)

## Documentation

The documentation of the API can be viewed through [Swagger](http://13.49.41.10:8000/docs) or [Redoc](http://13.49.41.10:8000/redoc)

Though these documentations are pretty nice, here is also clean API endpoints (excluding the UI purposed ones)

- `/add/resource`: POST with json body
- `/add/need`: POST with json body
- `/add/event`: POST with json body
- `/add/action`: POST with json body
- `/get/resource`: GET (get the list of resources)
- `/get/need`: GET (get the list of needs)
- `/get/event`: GET (get the list of events)
- `/get/action`: GET (get the list of actions)
- `/get/resource?id=<id>`: GET
- `/get/need?id=<id>`: GET
- `/get/event?id=<id>`: GET
- `/get/action?id=<id>`: GET
- `/delete/resource?id=<id>`: GET
- `/delete/need?id=<id>`: GET
- `/delete/event?id=<id>`: GET
- `/delete/action?id=<id>`: GET

The Docker can be compiled and run using `run.sh`

While running the docker, adding `test` to the end of the command runs the tests.

Note: in the main project, all the endpoints that I have written start with `/addel` and the rest is the same.
