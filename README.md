# 🩸 Blood Donor Emergency Locator

A Spring Boot + MongoDB app that lets people register as blood donors,
and lets others search for nearby available donors by blood group in an emergency.

## How it works 

1. A donor registers with their name, blood group, phone number, and location.
2. In an emergency, someone searches by blood group + their own location.
3. The app finds available donors of that blood group within a chosen radius (using MongoDB's geospatial search).

## Tech Stack

- **Backend:** Java Spring Boot
- **Database:** MongoDB (uses geospatial `2dsphere` index for "find donors near me")
- **Frontend:** Simple HTML/JS page (in `frontend/index.html`) for demo purposes

## Project Structure

```
blood-donor-locator/
├── pom.xml
├── src/main/java/com/example/blooddonor/
│   ├── BloodDonorLocatorApplication.java     # starts the app
│   ├── model/
│   │   ├── Donor.java                        # what a donor looks like in the DB
│   │   └── EmergencyRequest.java             # an urgent "need blood" request
│   ├── dto/
│   │   ├── DonorRequest.java                 # what frontend sends when registering
│   │   ├── EmergencyRequestDto.java          # what frontend sends for a Help request
│   │   ├── EmergencyResponse.java            # request + matching donors, combined
│   │   └── ApiResponse.java                  # consistent {success, message, data} format
│   ├── repository/
│   │   ├── DonorRepository.java              # talks to MongoDB (donors)
│   │   └── EmergencyRequestRepository.java   # talks to MongoDB (emergency requests)
│   ├── service/
│   │   ├── DonorService.java                 # donor business logic
│   │   └── EmergencyService.java             # Help feature logic
│   ├── exception/
│   │   ├── ResourceNotFoundException.java
│   │   └── GlobalExceptionHandler.java       # turns errors into clean JSON
│   └── controller/
│       ├── DonorController.java              # donor API endpoints
│       └── EmergencyController.java          # Help API endpoints
├── src/main/resources/application.properties
└── frontend/index.html                       # demo UI (Help / Register / Search tabs)
```

## Live API Documentation (Swagger)

Once the app is running, open this in your browser:
```
http://localhost:8080/swagger-ui.html
```
You'll get a live page listing every API endpoint, where you can test them directly — great for showing judges the API working without needing Postman.

## How to Run It

### Step 1: Install requirements
- Java 17+ (`java -version` to check)
- Maven (`mvn -version` to check)
- MongoDB running locally, OR a free MongoDB Atlas cloud cluster

### Step 2: Set your MongoDB connection
Open `src/main/resources/application.properties`.
- If MongoDB is running locally, you don't need to change anything.
- If using MongoDB Atlas (recommended if you don't want to install MongoDB), replace the line with your connection string from Atlas.

### Step 3: Run the app
```bash
cd blood-donor-locator
mvn spring-boot:run
```
The server starts at: `http://localhost:8080`

### Step 4: Open the demo frontend
Just open `frontend/index.html` in your browser (double-click it, or use VS Code's Live Server).

## API Endpoints (for testing with Postman or Swagger)

### Donors
| Action | Method | URL |
|---|---|---|
| Register a donor | POST | `/api/donors/register` |
| Search nearby donors | GET | `/api/donors/search?bloodGroup=O+&lat=28.6139&lng=77.2090&radiusKm=10` |
| List all donors | GET | `/api/donors` |
| Get one donor | GET | `/api/donors/{id}` |
| Mark availability | PATCH | `/api/donors/{id}/availability?available=false` |
| Delete a donor | DELETE | `/api/donors/{id}` |

### Emergency Help (the "I need blood urgently" feature)
| Action | Method | URL |
|---|---|---|
| Submit urgent request + get matching donors instantly | POST | `/api/emergency/request` |
| List all active emergency requests | GET | `/api/emergency/active` |
| Mark a request as resolved | PATCH | `/api/emergency/{id}/resolve` |

### Example: Register a donor (POST body)
```json
{
  "name": "Rahul Sharma",
  "bloodGroup": "O+",
  "phone": "9876543210",
  "city": "Delhi",
  "latitude": 28.6139,
  "longitude": 77.2090
}
```

### Example: Submit an emergency help request (POST body)
```json
{
  "patientName": "Anita Verma",
  "bloodGroup": "O+",
  "hospitalName": "AIIMS Delhi",
  "contactNumber": "9876543210",
  "city": "Delhi",
  "latitude": 28.6139,
  "longitude": 77.2090,
  "radiusKm": 10
}
```

### Every response follows this format:
```json
{
  "success": true,
  "message": "Donor registered successfully",
  "data": { ... }
}
```





## Live Map

Uses [Leaflet.js](https://leafletjs.com/) with free OpenStreetMap tiles — no signup or API key
required (unlike Google Maps). It shows:
- A blue marker for your current location
- A red marker for each matching donor, with their name/blood group/phone in a popup on click

