# 🧩 Recruitment Management System (RMS) — Backend

This is a backend server built with **Spring Boot** for managing a recruitment process.  
It supports **Admin** and **Applicant** roles, enabling job posting, resume uploads, and resume parsing using a third-party API.

---

## 🚀 Features

### 👤 User Management
- Signup and login with JWT authentication  
- Role-based access (`Admin` / `Applicant`)

### 💼 Job Management
- Admins can create and manage job openings  
- Applicants can view and apply to available jobs

### 📄 Resume Management
- Applicants can upload resumes (PDF/DOCX)  
- Automatically extracts skills, education, experience, and contact info using:

---

## ⚙️ Tech Stack

| Category | Technology |
|-----------|-------------|
| Backend Framework | Spring Boot |
| Security | Spring Security, JWT |
| ORM | Spring Data JPA (Hibernate) |
| Database | MySQL |
| Build Tool | Maven |
| API Docs | Swagger UI |
| Resume Parsing | Apilayer Resume Parser API |

---

## 🔑 API Endpoints

### 🔐 Authentication
| Method | Endpoint | Description |
|--------|-----------|--------------|
| `POST` | `/signup` | Create user profile |
| `POST` | `/login` | Login and get JWT token |

### 👤 Applicant APIs
| Method | Endpoint | Description |
|--------|-----------|--------------|
| `POST` | `/uploadResume` | Upload resume (PDF/DOCX only) |
| `GET` | `/jobs` | View available job openings |
| `GET` | `/jobs/apply?job_id={id}` | Apply to a job opening |

### 🧑‍💼 Admin APIs
| Method | Endpoint | Description |
|--------|-----------|--------------|
| `POST` | `/admin/job` | Create new job |
| `GET` | `/admin/job/{job_id}` | View job + applicants |
| `GET` | `/admin/applicants` | List all applicants |
| `GET` | `/admin/applicant/{applicant_id}` | View applicant profile data |

---

## 🧠 Resume Parsing API Integration

**API Endpoint:**  
`https://api.apilayer.com/resume_parser/upload`

**Headers:**

**Response Example:**
```json
{
  "name": "Elon Musk",
  "email": "elonmusk@teslamotors.com",
  "phone": "65068100",
  "skills": ["Entrepreneurship", "Physics", "Maths"],
  "education": [{"name": "Wharton School of the University of Pennsylvania"}],
  "experience": [{"name": "SpaceX"}, {"name": "Tesla"}]
}
