# Spring Boot Template

Template for a Spring Boot project including Spring REST, HATEOAS, JPA, etc. Additional details: [HELP.md](HELP.md)

[![Open Issues](https://img.shields.io/github/issues-raw/UdL-EPS-SoftArch/spring-template?logo=github)](https://github.com/orgs/UdL-EPS-SoftArch/projects/12)
[![CI/CD](https://github.com/UdL-EPS-SoftArch/spring-template/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/UdL-EPS-SoftArch/spring-template/actions)
[![Cucumber Reports](https://img.shields.io/badge/cucumber-reports-brightgreen)](https://UdL-EPS-SoftArch.github.io/spring-template/reports/cucumber-report.html)
[![Deployment status](https://img.shields.io/uptimerobot/status/m792691238-18db2a43adf8d8ded474f885)](https://spring-template.fly.dev/users)

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
    class UriEntity {
        uri : String
    }

    class UserDetails
    <<interface>> UserDetails

    class User  {
        username : String
        password : String
        email : String
    }

    class Record {
        id: Long
        name: String
        description: String
        created: ZonedDateTime
        modified: ZonedDateTime
        status: Status
    }

    UriEntity <|-- User
    UserDetails <|-- User
    UriEntity <|-- Record
    User "1" <-- "*" Record: ownedBy
```
