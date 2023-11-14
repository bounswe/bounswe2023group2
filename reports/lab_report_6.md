# Project Development Weekly Progress Report

**Team Name:** Disaster Response Platform (DaRP)

**Date:** 14.11.2023

## Progress Summary

On the backend, we have had a  modest improvement on implementation of user role endpoints and tried to figure out how to move for this milestone. There were lots of design choices. We plan to finalize resource, need, action, event, filter and sort in two weeks.

On the frontend, dynamics forms have been implemented and navigation bar improved. Need and resource endpoints are implemented but the UI part design is on hold.

As for Mobile developments:

- We prepared edit need and resources form, when users clicks corresponding activity its form shows up with related data from backend.
- We also send PUT request for need and resource to backend, it works correctly.
- We created a color scheme after the feedbacks from presentation, now our colors are defined.
- We implemented an interactive map from Open Street Map, we also arranged location in need/resources forms as X,Y (latitude, longtitude) for the future usage on map.
- A part of profile page was connected to backend, users can change their email from profile page the next parts (skills, languages, etc.) will be implemented.

### Objectives for the following week

Customer Milestone 2 is getting closer, and the aspects of sorting/filtering and coordination have been emphasized. We will mostly follow our project plan, with an emphasis on resource-need design and filtering.

### UI/UX

Some UI features we made/plan on making:

- Emergency Button: Emergency creation
- Specific Marker for each specified annotation class in map.
- Clicking on map directly to choose a location while creating a new activity (resource or need)
- Having geographically categorized data, coloring in terms of urgency

Some UI features that are out of scope but would fit with the application:

- Take current coordinates of user as the location of an activity
- Siri integration

## What was planned for the week? How did it go?

| Description                                                            | Issue                                                           | Assignee                   | Due                  | Estimated Duration | Actual Duration                                                           | Artifacts                                                                                          |
| ---------------------------------------------------------------------- | --------------------------------------------------------------- | -------------------------- | -------------------- | ------------------ | ------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------- |
| Backend - Email Verify endpoint                                        | [473](https://github.com/bounswe/bounswe2023group2/issues/473)  | Buse Tolunay               | 15.11.2023           | 5h                 | 4h                                                                        | Ongoing                                                                                            |
| Backend - Event CRUD endpoints                                         | [475](https://github.com/bounswe/bounswe2023group2/issues/475)  | Mehmet Kuzu                | 15.11.2023           | 5h                 | In Progress                                                               |                                                                                                    |
| Backend - User role implementation                                     | [478](https://github.com/bounswe/bounswe2023group2/issues/478)  | Begüm Arslan               | 15.11.2023           | 5h                 | 4h                                                                        | Ongoing [PR](https://github.com/bounswe/bounswe2023group2/pull/505)                                |
| Backend - Feedback system create-update-delete endpoint                | [480](https://github.com/bounswe/bounswe2023group2/issues/480)  | Aziza Mankenova            | 15.11.2023           | 5h                 | 3h                                                                        | Ongoing                                                                                            |
| Resource - Need final design and coding                                | [499](https://github.com/bounswe/bounswe2023group2/issues/499)  | Burak, Begüm, Merve, Şahin | 15.11.2023           | 5h                 | 1h 30min                                                                  | Ongoing                                                                                            |
| Backend - Requirements and design documents study                      | [483](https://github.com/bounswe/bounswe2023group2/issues/483)  | Backend Team               | 15.11.2023           | 5h                 | 4h                                                                        | Ongoing                                                                                            |
| Backend - Milestone 2 Plan Review                                      | [489](https://github.com/bounswe/bounswe2023group2/issues/489)  | Backend Team               | 11.11.2023           | 1h                 | [issue comments](https://github.com/bounswe/bounswe2023group2/issues/489) |
| Backend - User profile picture implementation                          | [490](https://github.com/bounswe/bounswe2023group2/issues/490)  | Mehmet Kuzu                | 15.11.2023           | 1h                 | 1h                                                                        | [issue comments](https://github.com/bounswe/bounswe2023group2/issues/489)                          |
| Backend - Resource / Need Code adjustment                              | [500](https://github.com/bounswe/bounswe2023group2/issues/500)  | Burak                      | 22.11.2023           | 3h                 | In progress                                                               | Ongoing                                                                                            |
| Frontend - Connecting Backend to Map Page                              | [474](https://github.com/bounswe/bounswe2023group2/issues/474)  | Şahin                      | 21.11.2023 (delayed) | 2h                 |
| Frontend - Create Activity information card for map page               | [476](https://github.com/bounswe/bounswe2023group2/issues/476)  | Şahin                      | 21.11.2023 (delayed) | 4h                 |
| Frontend - Email verification page design and implementation           | [479](https://github.com/bounswe/bounswe2023group2/issues/479)  | Can Bora                   | 15.11.2023           | 6h                 | In progress                                                               |
| Frontend - Add missing profile page features                           | [491](https://github.com/bounswe/bounswe2023group2/issues/491)  | Can Bora                   | 15.11.2023           | 3h                 | In progress                                                               | [Feature branch](https://github.com/bounswe/bounswe2023group2/tree/FE/feature/profile-page-extras) |
| Deciding Activity attributes for activity information card on map page | [488](https://github.com/bounswe/bounswe2023group2/issues/488)  | Şahin, Begüm               | 15.11.2023           | 2h                 | Ongoing                                                                   |
| Frontend - Research about how filtered activites might be printed      | [#493](https://github.com/bounswe/bounswe2023group2/issues/493) | Şahin                      | 15.11.2023           | 1h                 | 1h                                                                        | [#493](https://github.com/bounswe/bounswe2023group2/issues/493)                                    |
| Frontend - Need add page implementation                                | [#487](https://github.com/bounswe/bounswe2023group2/issues/487) | Merve                      | 15.11.2023           | 2h                 | in-progress                                                               |
| Frontend - Sort and filter components and implementations              | [#492](https://github.com/bounswe/bounswe2023group2/issues/492) | Merve                      | 15.11.2023           | 4h                 | in-progress                                                               |
| Frontend - Admin page design and implementation                        | [#486](https://github.com/bounswe/bounswe2023group2/issues/486) | Merve                      | 15.11.2023           | 4h                 | delayed                                                                   |
| Mobile - Edit Profile                                                  | [#374](https://github.com/bounswe/bounswe2023group2/issues/374) | Cahid                      | 11.11.2023           | 3h                 | In progress                                                               |
| Mobile - Simple map page initialization                                | [#383](https://github.com/bounswe/bounswe2023group2/issues/383) | Hasan, Egecan              | 14.11.2023           | 6h                 | 3h                                                                        | [#509](https://github.com/bounswe/bounswe2023group2/pull/509)                                      |
| Mobile - Connect backend for Profile Information                       | [#416](https://github.com/bounswe/bounswe2023group2/issues/416) | Cahid                      | 14.11.2023           | 2h                 | In progress                                                               |
| Mobile - Learning Unit/JUnit Testing                                   | [#484](https://github.com/bounswe/bounswe2023group2/issues/484) | Mobile Team                | 10.11.2023           | 4h                 | Cancelled                                                                 |
| Mobile - Edit Resource Form                                            | [#495](https://github.com/bounswe/bounswe2023group2/issues/495) | Egecan                     | 11.11.2023           | 3h                 | 2h                                                                        | [#510](https://github.com/bounswe/bounswe2023group2/pull/510)                                      |
| Mobile - Edit Need Form                                                | [#496](https://github.com/bounswe/bounswe2023group2/issues/496) | Halil                      | 11.11.2023           | 3h                 | 3h                                                                        | [#508](https://github.com/bounswe/bounswe2023group2/pull/508)                                      |
| Mobile - Connect Edit Form's into backend                              | [#497](https://github.com/bounswe/bounswe2023group2/issues/497) | Egecan                     | 13.11.2023           | 3h                 | 4h                                                                        | [#511](https://github.com/bounswe/bounswe2023group2/pull/511)                                      |
| Mobile - Color Scheme                                                  | [#498](https://github.com/bounswe/bounswe2023group2/issues/498) | Halil                      | 14.11.2023           | 2h                 | 2h                                                                        | [#504](https://github.com/bounswe/bounswe2023group2/pull/504)                                      |
| Mobile - Delete Need and Resources                                     | [#501](https://github.com/bounswe/bounswe2023group2/issues/501) | Hasan                      | 13.11.2023           | 3h                 | In Progress                                                               |

## Completed tasks that were not planned for the week

| Description                 | Issue                                                           | Assignee     | Due        | Actual Duration | Artifacts                                                                                                |
| --------------------------- | --------------------------------------------------------------- | ------------ | ---------- | --------------- | -------------------------------------------------------------------------------------------------------- |
| Backend Team Meeting #4     | [481](https://github.com/bounswe/bounswe2023group2/issues/481)  | Backend Team | 13.11.2023 | 1h              | [meeting notes](<https://github.com/bounswe/bounswe2023group2/wiki/Backend-Team-Meeting-(Offline)-%234>) |
| Mobile - Arranging Location | [#506](https://github.com/bounswe/bounswe2023group2/issues/506) | Egecan       | 13.11.2023 | 2h              | [#507](https://github.com/bounswe/bounswe2023group2/pull/507)                                            |

## Planned vs. Actual

Cahid: Estimated durtion for overall tasks (5h) was highly underestimated. Even though I worked 10+ hours, I was not able to finish the job.
As the backend team, we were slowed down due to design unclarities. So we mostly spend our time on design/review.
As frontend, we wre planning to complete sort/filter functions but we could not be able to finish it due to other exams.

## Plans for the next week

| Description                                                     | Issue                                                           | Assignee                   | Due        | Estimated Duration |
| --------------------------------------------------------------- | --------------------------------------------------------------- | -------------------------- | ---------- | ------------------ |
| Frontend - Email verification page design and implementation    | [#479](https://github.com/bounswe/bounswe2023group2/issues/479) | Can Bora                   | 15.11.2023 | 4h                 |
| Frontend - Add missing profile page features                    | [#491](https://github.com/bounswe/bounswe2023group2/issues/491) | Can Bora                   | 15.11.2023 | 5h                 |
| Frontend - Add upvote-downvote buttons                          | [#518](https://github.com/bounswe/bounswe2023group2/issues/518) | Can Bora                   | 22.11.2023 | 6h                 |
| Frontend - Add action type activities                           | [#546](https://github.com/bounswe/bounswe2023group2/issues/546) | Merve                      | 22.11.2023 | 4h                 |
| Backend - Predefined Subtypes Implementation                    | [#512](https://github.com/bounswe/bounswe2023group2/issues/512) | Burak                      | 22.11.2023 | 4h                 |
| Backend - Creation of a Predefined Subtypes JSON File           | [#513](https://github.com/bounswe/bounswe2023group2/issues/513) | Burak                      | 22.11.2023 | 1h                 |
| Backend - Development of Predefined Subtype Endpoints           | [#514](https://github.com/bounswe/bounswe2023group2/issues/514) | Burak                      | 22.11.2023 | 3h                 |
| Backend - Event design review                                   | [#533](https://github.com/bounswe/bounswe2023group2/issues/533) | Mehmet Kuzu                | 18.11.2023 | 2h                 |
| Backend - Events CRUD (Finalize)                                | [#475](https://github.com/bounswe/bounswe2023group2/issues/475) | Mehmet Kuzu                | 22.11.2023 | 4h                 |
| Backend - Unit Tests for Events CRUD                            | [#537](https://github.com/bounswe/bounswe2023group2/issues/537) | Mehmet Kuzu                | 20.11.2023 | 3h                 |
| Backend - User role implementation                              | [478](https://github.com/bounswe/bounswe2023group2/issues/478)  | Begüm Arslan               | 15.11.2023 | 3h                 |
| Backend -Frontend Meeting                                       | [536](https://github.com/bounswe/bounswe2023group2/issues/536)  | Begüm, Şahin, Burak        | 16.11.2023 | 3h                 |
| Test - Backend - User role endpoints                            | [523](https://github.com/bounswe/bounswe2023group2/issues/523)  | Begüm                      | 21.11.2023 | 2h                 |
| Test - Backend - Feedback endpoints                             | [524](https://github.com/bounswe/bounswe2023group2/issues/524)  | Aziza Mankenova            | 21.11.2023 | 2h                 |
| Backend - Admin CRUD and specific endpoint implementations      | [519](https://github.com/bounswe/bounswe2023group2/issues/519)  | Aziza, Buse                | 21.11.2023 | 5h                 |
| Backend - Filter, Sort endpoints for action-need-resource-event | [520](https://github.com/bounswe/bounswe2023group2/issues/520)  | Begüm                      | 25.11.2023 | 7h                 |
| Resource - Need final design and coding                         | [499](https://github.com/bounswe/bounswe2023group2/issues/499)  | Burak, Begüm, Merve, Şahin | 15.11.2023 | 3h                 |
| Mobile - CRUD Event Pages                                       | [#539](https://github.com/bounswe/bounswe2023group2/issues/539) | Egecan                     | 21.11.2023 | 6h                 |
| Mobile - Vote Needs and Resources                               | [#543](https://github.com/bounswe/bounswe2023group2/issues/543) | Egecan                     | 21.11.2023 | 3h                 |
| Mobile - Design a Page for Corresponding Needs and Resources    | [#544](https://github.com/bounswe/bounswe2023group2/issues/544) | Egecan                     | 21.11.2023 | 5h                 |
| Mobile - Adding map pinpoints                                   | [#535](https://github.com/bounswe/bounswe2023group2/issues/535) | Hasan                      | 22.11.2023 | 10h                |
| Mobile - Refresh Token                                          | [#531](https://github.com/bounswe/bounswe2023group2/issues/531) | Hasan                      | 22.11.2023 | 3h                 |
| Mobile - Delete Need and Resources                              | [#501](https://github.com/bounswe/bounswe2023group2/issues/501) | Hasan                      | 13.11.2023 | 3h                 |
| Mobile - Edit Profile                                           | [#374](https://github.com/bounswe/bounswe2023group2/issues/374) | Cahid                      | 21.11.2023 | 4h                 |
| Mobile - Connect backend for Profile Information                | [#416](https://github.com/bounswe/bounswe2023group2/issues/416) | Cahid                      | 21.11.2023 | 12h                |
| Mobile - Date of Birth Picker                                   | [#528](https://github.com/bounswe/bounswe2023group2/issues/528) | Cahid                      | 21.11.2023 | 2h                 |
| Mobile - Local User Profile                                     | [#532](https://github.com/bounswe/bounswe2023group2/issues/532) | Cahid                      | 21.11.2023 | 2h                 |
| Mobile - App Icon                                               | [#534](https://github.com/bounswe/bounswe2023group2/issues/534) | Cahid                      | 21.11.2023 | 2h                 |
| Mobile - Mobile - Get Subtypes From Backend                     | [#542](https://github.com/bounswe/bounswe2023group2/issues/542) | Halil                      | 21.11.2023 | 5h                 |
| Mobile - Recycler view design                                   | [#545](https://github.com/bounswe/bounswe2023group2/issues/545) | Halil                      | 21.11.2023 | 2h                 |
| Mobile - Sort and filter components and implementations         | [#540](https://github.com/bounswe/bounswe2023group2/issues/540) | Halil, Cahid               | 21.11.2023 | 6h                 |

## Risks

- Resource - needs should be constructed well to be able to cover needs of our design. Rest of the project depends on it.
- Aggregation and coordination should be considered.
- Midterms and other responsibilities may limit our time.

## Participants

- Egecan Serbester
- Begüm Arslan
- Merve Gürbüz
- Cahid Enes Keleş
- ~~Aziza Mankenova~~
- Buse Tolunay
- Mehmet Kuzulugil
- Ramazan Burak Sarıtaş
- Halil İbrahim Gürbüz
- Can Bora Uğur
- Ömer Şahin Albayram
- Hasan Bingölbali
