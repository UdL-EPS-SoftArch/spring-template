# VibeCheck API

Backend API for VibeCheck, an event discovery and management platform for Erasmus students.
It is built with Spring Boot, Spring Data REST, Spring Security, JPA, and Cucumber.

[![Open Issues](https://img.shields.io/github/issues-raw/UdL-EPS-SoftArch/spring-template?logo=github)](https://github.com/orgs/UdL-EPS-SoftArch/projects/12)
[![CI/CD](https://github.com/UdL-EPS-SoftArch/spring-template/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/UdL-EPS-SoftArch/spring-template/actions)
[![Cucumber Reports](https://img.shields.io/badge/cucumber-reports-brightgreen)](https://UdL-EPS-SoftArch.github.io/spring-template/reports/cucumber-report.html)
[![Deployment status](https://img.shields.io/uptimerobot/status/m792691238-18db2a43adf8d8ded474f885)](https://spring-template.fly.dev/records)

## Vision

**For** Erasmus students who want to easily find and organize local activities,
**VibeCheck** is an event management and social discovery platform
**that** lets users explore, create, and join student events,
**unlike** scattered messaging groups and general social media, it provides one hub for the international student community.

## Features per Stakeholder

This table describes the intended product scope; not every feature has been implemented yet.

| Erasmus student | Admin | Anonymous visitor |
|---|---|---|
| Register, log in, log out | Log in, log out | View public events and their locations |
| View and edit profile | Review flagged content | Search and filter public events |
| Create, edit, and delete own events | Remove inappropriate events | |
| Join and leave events | Suspend users | |
| Search and filter events | | |
| Comment on events and report content | | |

## Entities Model

```mermaid
classDiagram
    class UriEntity {
        uri : String
    }

    class UserDetails
    <<interface>> UserDetails

    class User  {
        username : String
        fullName : String
        password : String
        email : String
    }

    class Category {
        id: Long
        name: String
    }

    class Subcategory {
        id: Long
        name: String
    }

    class Event {
        id: Long
        title: String
        description: String
        address: String
        dateTime: LocalDateTime
        maxParticipants: Integer
        estimatedCost: BigDecimal
    }

    class EventParticipant {
        id: Long
    }

    class Comment {
        id: Long
        content: String
        createdAt: Instant
    }

    UriEntity <|-- User
    UserDetails <|.. User
    UriEntity <|-- Category
    UriEntity <|-- Subcategory
    UriEntity <|-- Event
    UriEntity <|-- EventParticipant
    UriEntity <|-- Comment
    Category "1" <-- "*" Subcategory : contains
    Subcategory "1" <-- "*" Event : classifies
    User "1" <-- "*" Event : creator
    Event "1" <-- "*" EventParticipant
    User "1" <-- "*" EventParticipant
    Event "1" <-- "*" Comment
    User "1" <-- "*" Comment : author
```

## AI-Assisted Spec-Driven Development (SDD)

This repository serves as a starter template for **AI-Assisted Spec-Driven Development (SDD)** with **Spring Data REST** and **Cucumber BDD**.

It is pre-configured with agent skills (`skills/`), repository instructions (`AGENTS.md`, `CONVENTIONS.md`), and Aider configuration (`.aider.conf.yml`) to support both:
1. **Full-Capability AI Agents** (OpenCode, Claude Code, Cursor, GitHub Copilot, Windsurf).
2. **Token-Constrained APIs** (Aider paired with Groq free tier - 8K token limits).

For complete stage-by-stage instructions and command workflows, see **[AI_DEVELOPMENT_GUIDE.md](AI_DEVELOPMENT_GUIDE.md)**.

### SDD Agent Skills (`skills/`)
- **`skills/sdd-feature-designer/SKILL.md`**: Draft Cucumber `.feature` specs for Spring Data REST endpoints (<50 lines).
- **`skills/sdd-cucumber-steps/SKILL.md`**: Implement MockMvc step definition classes extending `StepDefs.java`.
- **`skills/spring-datarest-entity/SKILL.md`**: Create JPA `@Entity` classes extending `UriEntity` and `@RepositoryRestResource` interfaces with SpEL row-level security queries.
- **`skills/spring-datarest-hander/SKILL.md`**: Create lifecycle event handlers based on the `@RepositoryEventHandler`.
- **`skills/sdd-bdd-verifier/SKILL.md`**: Run `mvn test -Dtest=CucumberTest` and analyze test failure summaries.
