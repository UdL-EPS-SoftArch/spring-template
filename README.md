# Spring Boot Template

Template for a Spring Boot project including Spring REST, HATEOAS, JPA, etc. Additional details: [HELP.md](HELP.md)

[![Open Issues](https://img.shields.io/github/issues-raw/UdL-EPS-SoftArch/spring-template?logo=github)](https://github.com/orgs/UdL-EPS-SoftArch/projects/12)
[![CI/CD](https://github.com/UdL-EPS-SoftArch/spring-template/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/UdL-EPS-SoftArch/spring-template/actions)
[![Cucumber Reports](https://img.shields.io/badge/cucumber-reports-brightgreen)](https://UdL-EPS-SoftArch.github.io/spring-template/reports/cucumber-report.html)
[![Deployment status](https://img.shields.io/uptimerobot/status/m792691238-18db2a43adf8d8ded474f885)](https://spring-template.fly.dev/records)

## Vision

**For** students learning Spring Boot and REST API development
**who** want to practice backend engineering with modern tools
**the project** is a template project
**that** provides user registration, authentication, and secured record management with ownership-based access control
**Unlike** other templates, this one is designed for iterative feature building with BDD (Cucumber) tests driving the development

## Features per Stakeholder

| USER                          | ADMIN                |
|-------------------------------|----------------------|
| Register                      |                      |
| Login                         |                      |
| Create Record                 |                      |
| Retrieve Record               |                      |
| Update Record                 |                      |
| Delete Record                 |                      |

## Entities Model

```mermaid
classDiagram
    class USER {
        +int user_id PK
        +string email UK
        +string password_hash
        +string username UK
        +string full_name
        +string role
        +datetime created_at
        +datetime updated_at
    }

    class SUBJECT {
        +int subject_id PK
        +string name
        +string description
        +datetime created_at
        +datetime updated_at
    }

    class TEST {
        +int test_id PK
        +int subject_id FK
        +int author_id FK
        +string title
        +string description
        +string test_type
        +string status
        +int year
        +string difficulty
        +int max_score
        +string tags
        +boolean is_public
        +datetime published_at
        +datetime created_at
        +datetime updated_at
    }

    class QUESTION {
        +int question_id PK
        +int test_id FK
        +text question_text
        +string question_type
        +int order_index
        +int score
        +datetime created_at
        +datetime updated_at
    }

    class ANSWER_OPTION {
        +int option_id PK
        +int question_id FK
        +text option_text
        +boolean is_correct
        +int order_index
    }

    class TEST_ATTEMPT {
        +int attempt_id PK
        +int test_id FK
        +int user_id FK
        +string attempt_type
        +string status
        +int score
        +int correct_count
        +int incorrect_count
        +int unanswered_count
        +boolean counts_for_leaderboard
        +datetime started_at
        +datetime submitted_at
    }

    class ANSWER_ATTEMPT {
        +int ANSWER_ATTEMPT_id PK
        +int attempt_id FK
        +int question_id FK
        +int selected_option_id FK
        +boolean is_correct
        +decimal awarded_score
        +datetime answered_at
    }

    class LEADERBOARD {
        +int leaderboard_id PK
        +int user_id FK
        +int test_id FK
        +int subject_id FK
        +string leaderboard_type
        +int score
        +int rank
        +datetime calculated_at
    }

    class BADGE {
        +int badge_id PK
        +string name UK
        +string description
        +string icon_url
        +string requirement_type
        +int requirement_value
        +boolean is_active
        +datetime created_at
    }

    class USER_BADGE {
        +int user_badge_id PK
        +int user_id FK
        +int badge_id FK
        +datetime earned_at
        +boolean is_visible
    }

    class REPORT {
        +int report_id PK
        +int reporter_id FK
        +int test_id FK
        +int question_id FK
        +string reason
        +text description
        +string status
        +int resolved_by FK
        +datetime created_at
        +datetime resolved_at
    }

    USER "1" -- "0..*" TEST : authors
    USER "1" -- "0..*" TEST_ATTEMPT : takes
    USER "1" -- "0..*" ANSWER_ATTEMPT : answers
    USER "1" -- "0..*" LEADERBOARD : ranks_in
    USER "1" -- "0..*" USER_BADGE : earns
    USER "1" -- "0..*" REPORT : submits
    USER "1" -- "0..*" REPORT : resolves

    SUBJECT "1" -- "0..*" TEST : contains
    SUBJECT "1" -- "1" LEADERBOARD : groups

    TEST "1" --> "0..*" QUESTION : contains
    TEST "1" -- "0..*" TEST_ATTEMPT : is_taken_in
    TEST "1" -- "0..1" LEADERBOARD : ranks
    TEST "1" -- "0..*" REPORT : is_reported

    QUESTION "1" --> "0..*" ANSWER_OPTION : has
    QUESTION "1" -- "0..*" ANSWER_ATTEMPT : is_answered_in
    QUESTION "1" -- "0..*" REPORT : is_reported

    ANSWER_OPTION "1" -- "0..*" ANSWER_ATTEMPT : selected_as

    TEST_ATTEMPT "1" -- "0..*" ANSWER_ATTEMPT : contains

    BADGE "1" -- "0..*" USER_BADGE : awarded_as
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
