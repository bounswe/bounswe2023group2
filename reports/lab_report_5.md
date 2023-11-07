# Project Development Weekly Progress Report

**Team Name:** Disaster Response Platform (DaRP)

**Date:** 07.11.2023

## Progress Summary


We have completed Milestone 1.

On the backend, user models and endpoints have been established, along with authentication processes such as registration, login, and session key management. The system also supports the fundamental create, read, update, and delete (CRUD) operations for managing needs and resources. Basic user profile functionalities have been put in place. Additionally, unit tests have been conducted to ensure the reliability and correctness of the implemented features.

On the Frontend, the parts related to the activity display on the map and the creation of new activities have been completed. Registration and logging in to the account have also been completed. Page routes were established and the profile page was partially completed.

As for Mobile developments:
- Login and registration for authentication were completed by connecting to the back-end. 
- Resource Entities created in the form of adding Resource were stored in the database in the back-end and all stored resource objects were shown on the Resource page with recycler-view.
- Need Entities created in the Need addition form were stored in the local database and shown with recycler-view on the Need page.
- Need, Resource, Action, Event, Emergency and Profile tables were created for the local database in accordance with MVVM. Connection with UI is provided through ViewModels. These objects are defined and called in a single place with Dependency Injection.



### Objectives for the following week

There are three weeks until Customer Milestone 2, and it is important to be timely. This week will be focused on adding features and backend APIs that are needed for Customer Milestone 2. We will also revisit features from Milestone 1.

**Email verification:** Email verification, a necessary part of authentication, will be implemented.

**User roles:** guest - admin - verified user differences will be discussed and start to implemented

**Need Creation:**  add - update - delete functionalties of need and dynamically map will be implemented

**Milestone 1 revisit:** Partially implemented features from Milestone 1 will be worked on, either adding missing features or connecting them to backend.

**More complicated data/implementation:** Milestone 1 was more of a prototype for how we will design, code and implement. For Milestone 2, more complicated procedures are to be implemented.
So the design background should be restudied. Revisiting the issues of the Milestone 1 might be necessary.

**Continue according to the plan:** Being well prepared, the project plan provides easy and well defined goals.

## What was planned for the week? How did it go?

| Description                                             | Issue                                                                                                                                                                                                                                                                                                                                                                                           | Assignee      | Due        | Estimated Duration | Actual Duration | Artifacts |
| ------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------- | ---------- | ------------------ | --------------- | --------- 
| Frontend - Showing new created activity on map          | [#405](https://github.com/bounswe/bounswe2023group2/issues/405)                                                                                                                                                                                                                                                                                                                                 | Şahin         | 30.10.2023 | 5h                 |     5h            |  [PR](https://github.com/bounswe/bounswe2023group2/pull/449)         |            |     |
| Frontend - Milestone 1 review                           | [#388](https://github.com/bounswe/bounswe2023group2/issues/388)                                                                                                                                                                                                                                                                                                                                 | Frontend Team | 30.10.2023 | 30min              |       15 min          |           |            |     |
| Frontend - CRUD Activity Pages                          | [#385](https://github.com/bounswe/bounswe2023group2/issues/385)                                                                                                                                                                                                                                                                                                                                 | Merve Gürbüz | 31.10.2023 | 6h                 |     in-progress            |  [439](https://github.com/bounswe/bounswe2023group2/pull/439)   [457](https://github.com/bounswe/bounswe2023group2/pull/457)     |            |     |
| Frontend - resource pop up component implementation     | [#394](https://github.com/bounswe/bounswe2023group2/issues/394)                                                                                                                                                                                                                                                                                                                                 | Merve         | 26.10.2023 | 3h                 |    5h             |    [439](https://github.com/bounswe/bounswe2023group2/pull/439)       |            |     |
| Frontend - Implement add button to the main page        | [#396](https://github.com/bounswe/bounswe2023group2/issues/396)                                                                                                                                                                                                                                                                                                                                 | Merve         | 27.10.2023 | 1h                 |          30min       |      [417](https://github.com/bounswe/bounswe2023group2/pull/417)     |            |     |
| Frontend - Implement add button to the map page         | [#398](https://github.com/bounswe/bounswe2023group2/issues/398)                                                                                                                                                                                                                                                                                                                                 | Şahin         | 30.10.2023 | 4h                 |      3h           |    [PR](https://github.com/bounswe/bounswe2023group2/pull/449)       |            |     |
| Frontend - Make Map interactive to click                | [#402](https://github.com/bounswe/bounswe2023group2/issues/402)                                                                                                                                                                                                                                                                                                                                 | Şahin         | 30.10.2023 | 3h                 |   3h              |  [PR](https://github.com/bounswe/bounswe2023group2/pull/449)         |            |     |
| Backend - Milestone 1 review                            | [#382](https://github.com/bounswe/bounswe2023group2/issues/382)                                                                                                                                                                                                                                                                                                                                 | Backend Team  | 30.10.2023 | 3h                 |   1h              |           |            |     |
| Test - Backend - Need CRUD Endpoints                    | [#381](https://github.com/bounswe/bounswe2023group2/issues/381)                                                                                                                                                                                                                                                                                                                                 | Aziza, Buse        | 30.10.2023 | 3h                 |          5h       |     [#467](https://github.com/bounswe/bounswe2023group2/pull/467)      |            |     |
| Test - Backend - Resource CRUD Endpoints                | [#406](https://github.com/bounswe/bounswe2023group2/issues/406)                                                                                                                                                                                                                                                                                                                                 | Burak          | 30.10.2023 | 3h                 |                 |    [#459](https://github.com/bounswe/bounswe2023group2/pull/459)        |            |     |
| Test - Backend - Profile CRUD endpoints                 | [#380](https://github.com/bounswe/bounswe2023group2/issues/380)                                                                                                                                                                                                                                                                                                                                 | Mehmet        | 30.10.2023 | 3h                 |                 |           |            |     |
| Backend - Improvements Profile CRUD endpoints           | [#399](https://github.com/bounswe/bounswe2023group2/issues/399) [#393](https://github.com/bounswe/bounswe2023group2/issues/393) [#395](https://github.com/bounswe/bounswe2023group2/issues/395) [#397](https://github.com/bounswe/bounswe2023group2/issues/397) [#408](https://github.com/bounswe/bounswe2023group2/issues/408)                                                                 | Mehmet        | 30.10.2023 | 5h                 |   |[#480](https://github.com/bounswe/bounswe2023group2/pull/480) |
| Backend - Improvements Authentication CRUD endpoints    | [#403](https://github.com/bounswe/bounswe2023group2/issues/403) [#430](https://github.com/bounswe/bounswe2023group2/issues/430) [#429](https://github.com/bounswe/bounswe2023group2/issues/429)                                                                                                                                                                                                                                                                                                                                 | Begüm         | 30.10.2023 | 3h                 |   5h              |  [PR#432](https://github.com/bounswe/bounswe2023group2/pull/432)         |            |     |
| Backend - Improvements Need CRUD endpoints              | [#400](https://github.com/bounswe/bounswe2023group2/issues/400)                                                                                                                                                                                                                                                                                                                                 | Buse,Aziza    | 30.10.2023 | 2h                 |         4h        |     [#462](https://github.com/bounswe/bounswe2023group2/pull/462), [#392](https://github.com/bounswe/bounswe2023group2/pull/392)      |            |     |
| Backend - Improvements Resource CRUD endpoints          | [#404](https://github.com/bounswe/bounswe2023group2/issues/404)                                                                                                                                              | Burak         | 30.10.2023 | 2h                 |                 |       [#444](https://github.com/bounswe/bounswe2023group2/pull/444) [#452](https://github.com/bounswe/bounswe2023group2/pull/452)    |            |     |
| Test - Backend - Authentication endpoints               | [#379](https://github.com/bounswe/bounswe2023group2/issues/379) [#447](https://github.com/bounswe/bounswe2023group2/issues/447) [#433](https://github.com/bounswe/bounswe2023group2/issues/433) [#448](https://github.com/bounswe/bounswe2023group2/issues/448)                                                                                                                                                                                                                                                                                                                                | Begüm         | 30.10.2023 | 2h                 |   4h              |    [#450](https://github.com/bounswe/bounswe2023group2/pull/450)       |            |     |
| Mobile - Team Meeting #1                                | [#346](https://github.com/bounswe/bounswe2023group2/issues/346)                                                                                                                                                                                                                                                                                                                                 | Mobile Team   | 30.10.2023 | 1h                 |   1h              | [#Meeting 1](https://github.com/bounswe/bounswe2023group2/wiki/Mobile-Team-Meeting-1)          |            |     |
| Mobile - Milestone 1 Review                             | [#389](https://github.com/bounswe/bounswe2023group2/issues/389)                                                                                                                                                                                                                                                                                                                                 | Mobile Team   | 30.10.2023 | 3h                 |     3h            |    [#389]((https://github.com/bounswe/bounswe2023group2/issues/389))       |            |     |
| Mobile - CRUD Resource Pages                            | [#387](https://github.com/bounswe/bounswe2023group2/issues/387) [#412](https://github.com/bounswe/bounswe2023group2/issues/412) [#413](https://github.com/bounswe/bounswe2023group2/issues/413) [#414](https://github.com/bounswe/bounswe2023group2/issues/414)                                                                                                                                 | Egecan, Halil | 30.10.2023 | 8h                 |     10h            |   [#415](https://github.com/bounswe/bounswe2023group2/pull/415)  [#437](https://github.com/bounswe/bounswe2023group2/pull/437) [#456](https://github.com/bounswe/bounswe2023group2/pull/456)    |            |     |
| Mobile - Simple map page initialization                 | [#383](https://github.com/bounswe/bounswe2023group2/issues/383)                                                                                                                                                                                                                                                                                                                                 | Hasan, Egecan | 30.10.2023 | 6h                 |       In Progress          |           |            |     |
| Mobile - Creating local database for offline records    | [#339](https://github.com/bounswe/bounswe2023group2/issues/339) [#340](https://github.com/bounswe/bounswe2023group2/issues/340) [#342](https://github.com/bounswe/bounswe2023group2/issues/342) [#343](https://github.com/bounswe/bounswe2023group2/issues/343) [#344](https://github.com/bounswe/bounswe2023group2/issues/344) [#345](https://github.com/bounswe/bounswe2023group2/issues/345) | Hasan, Egecan | 30.10.2023 | 6h                 |  8h        |    [#436](https://github.com/bounswe/bounswe2023group2/pull/436)  [#442](https://github.com/bounswe/bounswe2023group2/pull/442) [#446](https://github.com/bounswe/bounswe2023group2/pull/446)           |     |
| Mobile - CRUD Need Pages                                | [#347](https://github.com/bounswe/bounswe2023group2/issues/347) [#410](https://github.com/bounswe/bounswe2023group2/issues/410) [#414](https://github.com/bounswe/bounswe2023group2/issues/414)                                                                                                                                                                                                 | Egecan, Halil        | 30.10.2023 | 6h                 |    8h             |     [#415](https://github.com/bounswe/bounswe2023group2/pull/415) [#437](https://github.com/bounswe/bounswe2023group2/pull/437) [#456](https://github.com/bounswe/bounswe2023group2/pull/456)     |            |     |
| Mobile - Login and Registration Connecting into Backend | [#411 ](https://github.com/bounswe/bounswe2023group2/issues/411 )                                                                                                                                                                                                                                                                                                                               | Hasan         | 30.10.2023 | 4h                 |     8h            |    [#434](https://github.com/bounswe/bounswe2023group2/pull/434) [#441](https://github.com/bounswe/bounswe2023group2/pull/441)       |
| Milestone 1 demo preparation - deploy check             | [#390](https://github.com/bounswe/bounswe2023group2/issues/390)                                                                                                                                                                                                                                                                                                                                 | All team      | 31.10.2023 | 5h                 |  3h               |           |            |     |
| Milestone 1 - Prepare Deliverables)                     | [#419](https://github.com/bounswe/bounswe2023group2/issues/419) [#465](https://github.com/bounswe/bounswe2023group2/issues/465)                                                                                                                                                                                                                                                                                                                                | All team      | 25.10.2023 | 4h                 |  3h               |           |            |     |
| Mobile - Connect backend for Profile Information        | [#416 ](https://github.com/bounswe/bounswe2023group2/issues/416 )  | Cahid Enes    | 30.10.2023 | 4h                 | In progress     |  |
| Frontend - Adding API utils                             | [#407](https://github.com/bounswe/bounswe2023group2/issues/407)                                                                                                                                                                                                                                                                                                                                 | Merve         | 31.10.2023 | 2h                 |     2h            |   [457](https://github.com/bounswe/bounswe2023group2/pull/457)        |            |     |
| Frontend - Making Activity Table more flexible          | [#409](https://github.com/bounswe/bounswe2023group2/issues/409)                                                                                                                                                                                                                                                                                                                                 | Merve, Can Bora      | 28.10.2023 | 2h                 | In progress |           |            |     |
| Frontend - Finishing profile page implementation        | [#330](https://github.com/bounswe/bounswe2023group2/issues/330)                                                                                                                                                                                                                                                                                                                                 | Can Bora      | 25.10.2023 | 4h                 | 6h              |   [PR #455](https://github.com/bounswe/bounswe2023group2/pull/455)        |            |     |
| Milestone 1 - Prepare Demos (Scenarios, Personas...)    | [#418](https://github.com/bounswe/bounswe2023group2/issues/418)                                                                                                                                                                                                                                                                                                                                 | All team      | 25.10.2023 | 4h                 |                 |           |            |     |
| Milestone 1 - Deployment                                | [#420](https://github.com/bounswe/bounswe2023group2/issues/420)                                                                                                                                                                                                                                                                                                                                 | Merve         | 25.10.2023 | 1h                 |    1h             |           |            |     |


## Completed tasks that were not planned for the week

| Description | Issue | Assignee | Due  | Actual Duration | Artifacts |
| - | - | - | - | - | - |
| Backend - Team meeting #3 | [#426](https://github.com/bounswe/bounswe2023group2/issues/426) | Backend Team | 27.10.2023  | 1h |  |
| Mobile - Logged-in Indicator | [#443](https://github.com/bounswe/bounswe2023group2/issues/443) | Cahid Enes |  | 1h | [PR #441](https://github.com/bounswe/bounswe2023group2/pull/441) |
| Mobile - User Classes | [#373](https://github.com/bounswe/bounswe2023group2/issues/373) | Cahid Enes | 23.10.2023 | 3h | [PR #375](https://github.com/bounswe/bounswe2023group2/pull/375) |
|Backend - Bugfix for resource and need get| [#451](https://github.com/bounswe/bounswe2023group2/issues/451) | Begüm, Burak | 30.11.2023 | 1h | [PR #452](https://github.com/bounswe/bounswe2023group2/pull/452) |
|signUp api response inconsistency | [#435](https://github.com/bounswe/bounswe2023group2/issues/435) | Begüm | 30.11.2023 | 3h | [PR #438](https://github.com/bounswe/bounswe2023group2/pull/438) |
|Backend - Add Coordinates to Resource and Need | [#453](https://github.com/bounswe/bounswe2023group2/issues/453) | Burak | 31.11.2023 | 1h | [PR #454](https://github.com/bounswe/bounswe2023group2/pull/454) |

## Planned vs. Actual
Milestone-1 reports and deliverables took longer then we expected. There were some bugs in the code that needs attention. 


## Plans for the next week

| Description | Issue | Assignee | Due | Estimated Duration |
| ----------- | ----- | -------- | --- | ------------------ |
|Backend - Email Verify endpoint| [473](https://github.com/bounswe/bounswe2023group2/issues/473)| Buse Tolunay| 15.11.2023|5h|
|Backend - Event CRUD endpoints | [475](https://github.com/bounswe/bounswe2023group2/issues/475)| Mehmet Kuzu| 15.11.2023|5h|
|Backend - User role implementation | [478](https://github.com/bounswe/bounswe2023group2/issues/478)| Begüm Arslan| 15.11.2023|5h|
|Backend - Feedback system create-update-delete endpoint | [480](https://github.com/bounswe/bounswe2023group2/issues/480)| Aziza Mankenova| 15.11.2023|5h|
|Resource - Need final design and coding | [499](https://github.com/bounswe/bounswe2023group2/issues/499)| Burak, Begüm, Merve, Şahin| 15.11.2023|5h|
|Backend - Requirements and design documents study | [483](https://github.com/bounswe/bounswe2023group2/issues/483)| Backend Team| 15.11.2023|5h|
|Backend - Milestone 2 Plan Review  | [489](https://github.com/bounswe/bounswe2023group2/issues/489)| Backend Team| 11.11.2023|1h|
|Backend - User profile picture implementation  | [490](https://github.com/bounswe/bounswe2023group2/issues/490)| Mehmet Kuzu| 15.11.2023|1h|
|Backend - Resource / Need Code adjustment| [500](https://github.com/bounswe/bounswe2023group2/issues/500)| Burak| 15.11.2023|3h|
|Frontend - Connecting Backend to Map Page | [474](https://github.com/bounswe/bounswe2023group2/issues/474)| Şahin| 10.11.2023|2h|
|Frontend - Create Activity information card for map page  | [476](https://github.com/bounswe/bounswe2023group2/issues/476)| Şahin| 15.11.2023|4h|
|Frontend - Email verification page design and implementation | [479](https://github.com/bounswe/bounswe2023group2/issues/479)| Can Bora | 15.11.2023 | 6h |
|Frontend - Add missing profile page features | [491](https://github.com/bounswe/bounswe2023group2/issues/491)| Can Bora | 15.11.2023 | 3h |
|Deciding Activity attributes for activity information card on map page  | [488](https://github.com/bounswe/bounswe2023group2/issues/488)| Şahin, Begüm | 15.11.2023 | 2h |
| Frontend - Research about how filtered activites might be printed | [#493](https://github.com/bounswe/bounswe2023group2/issues/493) | Şahin| 15.11.2023 | 1h |
|Frontend - Need add page implementation | [#487](https://github.com/bounswe/bounswe2023group2/issues/487) | Merve| 15.11.2023 | 2h |
|Frontend - Sort and filter components and implementations | [#492](https://github.com/bounswe/bounswe2023group2/issues/492) | Merve| 15.11.2023 | 4h |
|Frontend - Admin page design and implementation | [#486](https://github.com/bounswe/bounswe2023group2/issues/486) | Merve| 15.11.2023 | 4h |
| Mobile - Edit Profile | [#374](https://github.com/bounswe/bounswe2023group2/issues/374) | Cahid | 11.11.2023 | 3h |
| Mobile - Simple map page initialization | [#383](https://github.com/bounswe/bounswe2023group2/issues/383) | Hasan, Egecan| 14.11.2023 | 6h |
| Mobile - Connect backend for Profile Information | [#416](https://github.com/bounswe/bounswe2023group2/issues/416) | Cahid | 14.11.2023 | 2h |
| Mobile - Learning Unit/JUnit Testing | [#484](https://github.com/bounswe/bounswe2023group2/issues/484) | Mobile Team | 10.11.2023 | 4h |
| Mobile - Edit Resource Form | [#495](https://github.com/bounswe/bounswe2023group2/issues/495) | Halil | 11.11.2023 | 3h |
| Mobile - Edit Need Form | [#496](https://github.com/bounswe/bounswe2023group2/issues/496) | Halil | 11.11.2023 | 3h |
| Mobile - Connect Edit Form's into backend | [#497](https://github.com/bounswe/bounswe2023group2/issues/497) | Egecan | 13.11.2023 | 3h |
| Mobile - Color Scheme | [#498](https://github.com/bounswe/bounswe2023group2/issues/498) | Halil | 14.11.2023 | 2h |
| Mobile - Delete Need and Resources | [#501](https://github.com/bounswe/bounswe2023group2/issues/501) | Hasan | 13.11.2023 | 3h |

## Risks
- Resource - needs should be constructed well to be able to cover needs of our design. Rest of the project depends on it.
- Aggregation and coordination should be considered. 

## Participants

- Egecan Serbester 
- Begüm Arslan
- Merve Gürbüz
- Cahid Enes Keleş
- Aziza Mankenova
- Buse Tolunay
- Mehmet Kuzulugil 
- Ramazan Burak Sarıtaş
- Halil İbrahim Gürbüz
- Can Bora Uğur
- Ömer Şahin Albayram
- Hasan Bingölbali
