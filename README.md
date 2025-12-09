# 📚 MyMemory – Personal Digital Journal

**MyMemory** is a private digital journal platform built to help users securely store, organize, and search their personal memories and notes. Whether you’re a student, professional, or someone who wants to keep daily reflections, MyMemory provides a clean and intuitive interface to manage your thoughts efficiently.

Store memories, organize them into categories, manage images, and search for content—all from a secure and user-friendly backend API.  

---

## 📖 Table of Contents

- [📌 Introduction](#-introduction)  
- [🔖 Project Overview](#-project-overview)  
- [🔐 Technologies Used](#-technologies-used)  
- [💻 Setup & Installation](#-setup--installation)  
- [🌱 Branching Strategy](#-branching-strategy)  
- [🔁 Git Workflow](#-git-workflow)  
- [🔀 Pull Request & Merge](#-pull-request--merge)  
- [⚔️ Conflict Resolution](#-conflict-resolution)  
- [📝 License](#-license)  
- [🚀 Next Steps](#-next-steps)  

---

## 📌 Introduction

**MyMemory** is a backend service designed to securely manage users’ personal memories and notes. Users can:

- Register and authenticate securely  
- Create, read, update, and delete personal memories  
- Organize memories into categories (Tour, Party, Love, Family, Friends, Study, Personal…)  
- Upload images associated with memories  
- Search for memories by title, category, or keyword  
- Ensure all data is private and accessible only to the owner  

Ideal for students and anyone who wants a structured, secure, and private digital space for their thoughts.  

---

## 🔖 Project Overview

MyMemory aims to provide a clean, efficient, and secure backend for storing personal reflections:

- **User Authentication & Authorization:** JWT-based security, role-based access control  
- **Memory Management:** CRUD operations, search, and image upload  
- **Category Management:** Pre-seeded categories for easy organization  
- **Database Design:** Relational database using PostgreSQL with proper entity relationships  
- **Error Handling:** Global exception management for consistent API responses  
- **API Documentation:** Swagger UI for clear API reference  

---

## 🔐 Technologies Used

| Technology                | Version        |
|---------------------------|----------------|
| Java & Spring Boot        | 3.x             |
| PostgreSQL                | 15.x            |
| Spring Data JPA           | 3.x             |
| JWT Authentication        | 1.x             |
| Swagger/OpenAPI           | 3.x             |
| Lombok                    | 1.18.x          |
| pgAdmin                   | 6.x             |
| Maven                     | 4.x             |

---

## 💻 Setup & Installation

### Step 1: Clone the Repository

```bash
git clone [https://github.com/yourusername/mymemory.git](https://github.com/makara17092020/MY_MEMORY_BACK_END.git)
cd mymemory
