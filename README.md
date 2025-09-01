# 🐉 Virtual Pet 🐉

**Virtual Pet** is a web application developed as part of a learning challenge.  
The goal is not only to build a functional virtual pet system, but also to explore **AI-assisted frontend generation**, understand how to analyze and adjust AI-generated code, and successfully connect it to a backend implemented in Java with Spring Boot.  

---

## ✨ Application Description

The Virtual Pet App allows users to **create and interact with virtual pets** in a colorful interactive environment.  
Main features include:

- **Register (Signup)** → Create a new account with username & password.  
- **Login** → Authenticate and receive a JWT token.  
- **Create** → Create new pets, choosing from different dragon types with personalized colors, names, and traits.  
- **Read** → View all existing pets, track mood, energy, and needs.  
- **Update** → Interact with pets (feed, play, rest, ignore and evolve).  
- **Delete** → Remove pets you no longer want to care for.  

---

## 🔑 Roles & Authorization

- **ROLE_USER** →  
  - Manage only their own pets (view, update, delete).  

- **ROLE_ADMIN** →  
  - Full system access (view, update, delete all pets, manage users).  

The application includes **JWT authentication** to verify tokens and enforce role-based access before granting access to protected endpoints.

---

## 🤖 AI Selection & Workflow

The project requires choosing an **AI tool to generate the frontend**.  
Steps followed:  
1. **Evaluation of available AIs** 
2. **Criteria for selection**: quality of generated code, ease of iteration, ability to explain concepts, and integration support.  
3. **Chosen AI**: ChatGPT, Gemini, Copilot, Claude and dev0.   

---

## 🔍 Analysis of AI-Generated Code

The code must be understood, reviewed and refactored for use in the project

---

## 🔗 Frontend–Backend Connection

- **Backend**: Spring Boot REST API with endpoints for authentication, pets, and user management.  
- **Frontend**: React app calling backend endpoints with `fetch` or `axios`.  
- **Authentication**: JWT token stored in localStorage and attached in `Authorization: Bearer <token>` header.  

Challenges:  
- Handling CORS issues → solved with Spring Boot CORS configuration.  
- Synchronizing JSON payloads between frontend and backend DTOs.  
- Refreshing pet state after interactions (play/feed).  

---

## 📚 API Documentation

The API is documented with **Swagger**.  
Endpoints include:  

### Authentication
- `POST /auth/register` → Create new user.  
- `POST /auth/login` → Authenticate & return JWT.  

### Pets
- `GET /pets` → Get pets (all if admin, user’s pets otherwise).  
- `POST /pets` → Create a new pet.  
- `PUT /pets/{id}` → Update pet (feed, play, etc.).  
- `DELETE /pets/{id}` → Remove a pet.  

---

## 🛠️ Tech Stack

- **Frontend**: React, TailwindCSS, shadcn/ui  
- **Backend**: Java, Spring Boot, Maven  
- **Database**: MySQL  
- **Auth**: JWT  
- **Documentation**: Swagger UI  

---

## 🚀 Installation

### Prerequisites
- Node.js & npm  
- Java 17+  
- Maven  
- MySQL  

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/ToniR90/5.2-Virtual-Pet.git
   cd virtual-pet