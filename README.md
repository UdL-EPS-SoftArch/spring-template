# VibeCheck API

Backend REST service for the VibeCheck platform, built with Spring Boot.

[![Open Issues](https://img.shields.io/github/issues-raw/UdL-EPS-SoftArch/vibecheck-api?logo=github)](https://github.com/orgs/UdL-EPS-SoftArch/projects/12)
[![CI/CD](https://github.com/UdL-EPS-SoftArch/vibecheck-api/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/UdL-EPS-SoftArch/vibecheck-api/actions)

## Vision

**For** Erasmus students **who want to** easily find and organize local activities, **the VibeCheck project is an** event management and social discovery platform **that allows** users to explore, create, and join student events on an interactive map. **Unlike other** general social media platforms or scattered messaging groups, VibeCheck provides a single centralized hub specifically tailored for the international student community.

## Features per Stakeholder

| ERASMUS USER | ADMIN | ANONYMOUS |
| :--- | :--- | :--- |
| Register | Login | View public events |
| Login | Logout | View event locations |
| Logout | List flagged content | Search public events |
| Edit profile | Remove inappropriate event | Filter events by category |
| View profile | Suspend user | Redirect to Register |
| Create event | | |
| Edit event | | |
| Delete event | | |
| Join event (RSVP) | | |
| Leave event | | |
| Search and filter events | | |
| Post comments / event discussion | | |
| Report content / event | | |

## Entities Model

```mermaid
classDiagram
    class UriEntity {
        <<abstract>>
        uri : String
        version : Long
    }

    class UserDetails {
        <<interface>>
    }

    class User {
        username : String
        fullName : String
        email : String
        password : String
        admin : boolean
        suspended : boolean
    }

    class Category {
        id : Long
        name : String
    }

    class Subcategory {
        id : Long
        name : String
    }

    class Event {
        id : Long
        title : String
        description : String
        address : String
        dateTime : LocalDateTime
        maxParticipants : Integer
        estimatedCost : BigDecimal
        created : Instant
        modified : Instant
    }

    class Comment {
        id : Long
        content : String
        createdAt : Instant
    }

    class Report {
        id : Long
        createdAt : Instant
    }


    UriEntity <|-- User
    UriEntity <|-- Category
    UriEntity <|-- Subcategory
    UriEntity <|-- Event
    UriEntity <|-- Comment
    UriEntity <|-- Report
    UserDetails <|.. User

    Subcategory "0..*" --> "1" Category : category
    Event "0..*" --> "1" Subcategory : subcategory
    Event "0..*" --> "1" User : creator
    Event "0..*" --> "0..*" User : participants
    Comment "0..*" --> "1" Event : event
    Comment "0..*" --> "1" User : author
    Report "0..*" --> "1" User : reporter
    Report "0..*" --> "0..1" Event : event
    Report "0..*" --> "0..1" Comment : comment
```

A `Report` targets exactly one event or one comment; deleting the target also deletes its reports.
