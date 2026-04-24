IIT id :- 20240712 
UOW id :- w2152957
Name :- Yogarajah Rahulshanth

                                         Answers for Client Server Architecture Questions

Part 1

1.1  
By default, JAX-RS creates a new instance of every resource class for each incoming HTTP request (per-request lifecycle). This means resource classes are not singletons. Because of this, in-memory data structures such as HashMap and ArrayList must be declared as static fields — shared across all instances — and ideally accessed through a separate data store class. Without static storage, each request would see an empty, fresh collection, causing all stored data to be lost. In concurrent environments, ConcurrentHashMap should be used to prevent race conditions.

1.2
This class handles GET /api/v1. The @Path("/") annotation combined with the @ApplicationPath("/api/v1") from ApplicationConfig results in the full path /api/v1. The method builds a Java HashMap, populates it with API metadata, and returns it wrapped in a Response.ok(...).build() — which sends HTTP 200 OK.
The most important part is the links map inside the response. This is where HATEOAS comes in you're telling any client that calls your API: "Here are the URLs you can explore next."

Part 2

2.1  
Returning only IDs reduces response size and saves network bandwidth, but forces clients to make additional requests to retrieve full details, increasing total traffic. Returning full room objects provides all data in one request, which is more efficient for clients needing complete information. For a small-scale system like Smart Campus, returning full objects is the more practical and client-friendly approach. 

2.2  
Yes, DELETE is idempotent in this implementation. The first request successfully deletes the room and returns HTTP 200. Any subsequent identical request returns HTTP 404 because the resource no longer exists. The server's state does not change after the first deletion. Idempotency means repeated calls produce the same final state, which this implementation satisfies even though the response code differs across calls. 

Part 3

3.1  
When a client sends a request with a Content-Type header that does not match the @Consumes(MediaType.APPLICATION_JSON) annotation, JAX-RS automatically rejects the request before it even reaches the method body. The framework returns an HTTP 415 Unsupported Media Type response. This protects the server from attempting to parse incompatible data formats and ensures only correctly formatted JSON is processed. 

3.2  
Query parameters are better suited for filtering because they are optional by nature — the same endpoint /sensors works with or without ?type=CO2. In contrast, embedding the type in the path like /sensors/type/CO2 makes filtering mandatory and creates a different resource path entirely, which violates REST principles. Query parameters also support combining multiple filters easily, for example ?type=CO2&status=active. 

Part 4

4.1  
The Sub-Resource Locator pattern improves code organisation by delegating nested resource logic to dedicated classes. Instead of one large controller handling every possible URL, each class has a single, clear responsibility. This makes the code easier to read, test, and maintain. In large APIs, a single controller quickly becomes unmanageable, while the locator pattern keeps each class focused and modular. 

Part 5

5.2  
HTTP 404 means the requested URL was not found. However, the endpoint /api/v1/sensors exists and is valid. The problem is not the URL but the data inside the request body , the roomId references a resource that does not exist. HTTP 422 Unprocessable Entity is more semantically accurate because it signals that the JSON payload is syntactically valid but cannot be processed due to a broken internal reference. 


5.4  
Exposing Java stack traces is a serious security risk. They reveal internal package names, class names, library versions, and exact line numbers. An attacker can use this information to identify known vulnerabilities in the exposed libraries (via CVE databases), map the internal codebase structure, and craft targeted exploits. A well-designed API should always return generic error messages to external consumers while logging full details only on the server. 

5.5 
 JAX-RS filters implement the principle of separation of concerns. Placing logging inside every resource method creates code duplication — if you have 15 endpoints, you repeat the same logging code 15 times. A filter is written once and applied automatically to every request and response across the entire API. This makes the codebase cleaner, easier to maintain, and ensures no endpoint is accidentally missed. 




                                                     Overview of API Design
The Smart Campus API is a RESTful web service built using JAX-RS (Jersey) and Java. It was developed as part of the 5COSC022W Client-Server Architectures coursework at the University of Westminster.
The API manages two core resources — Rooms and Sensors — that represent the physical infrastructure of a university campus. Each sensor is linked to a room, and historical readings can be recorded for each sensor.

Resource Hierarchy
/api/v1/ - Discovery endpoint (API metadata)
/api/v1/rooms  -  Room collection
/api/v1/rooms/{roomId}  - Individual room
/api/v1/sensors   -  Sensor collection
/api/v1/sensors/{sensorId}  -  Individual sensor
/api/v1/sensors/{sensorId}/readings -  Reading sub-resource (Part 4)

Design Decisions

No database used. All data is stored in-memory using HashMap and ArrayList as required by the coursework specification.
Sub-Resource Locator pattern is used for sensor readings. SensorResource delegates to a dedicated SensorReadingResource class, keeping the codebase clean and modular.
Exception Mappers are used for all error handling. No raw Java stack traces are ever returned to the client. All error responses are structured JSON.
JAX-RS Filters provide automatic request and response logging across all endpoints without touching individual resource methods.

                                                  
                                                  How to Build and Launch the Server

Follow these steps exactly in order.
Prerequisites
Make sure you have the following installed on your machine before starting:

Java JDK 11 or higher
Apache Maven 3.6 or higher
NetBeans IDE (recommended) or any Java IDE
Git

Step 1 — Clone the Repository
Open your terminal and run:
bashgit clone https://github.com/YOUR-USERNAME/SmartCampusAPI.git
Then navigate into the project folder:
bashcd SmartCampusAPI

Step 2 — Open the Project in NetBeans
Open NetBeans IDE
Click File → Open Project
Navigate to the SmartCampusAPI folder you just cloned
Click Open Project
Wait for NetBeans to finish loading the Maven dependencies (you will see a progress bar at the bottom)

Step 3 — Build the Project
In NetBeans:

Right-click the project name in the Projects panel on the left
Click Clean and Build
Wait until you see "BUILD SUCCESS" in the Output panel at the bottom

Alternatively, in your terminal inside the project folder:
bashmvn clean package

Step 4 — Run the Server
In NetBeans:

Right-click the project name
Click Run

The API is now live at:
http://localhost:8080/api/v1/

Step 6 — Stop the Server
Click inside the NetBeans console panel and press ENTER. You will see

                                                           Sample curl Commands
curl 1 — Create a Room (POST /api/v1/rooms)
curl -X POST http://localhost:8080/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d "{\"id\": \"LIB-301\", \"name\": \"Library Quiet Study\", \"capacity\": 50}"

  curl 2 — Register a Sensor linked to a Room (POST /api/v1/sensors)
  curl -X POST http://localhost:8080/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d "{\"id\": \"SENS-001\", \"type\": \"CO2\", \"status\": \"ACTIVE\", \"roomId\": \"LIB-301\"}"

  curl 3 — Get All Rooms (GET /api/v1/rooms)
  curl -X GET http://localhost:8080/api/v1/rooms
