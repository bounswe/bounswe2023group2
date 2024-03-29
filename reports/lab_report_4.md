# Project Development Weekly Progress Report

**Team Name:** Disaster Response Platform (DaRP)

**Date:** 24.10.2023

## Progress Summary


This week we started the implementation phase of our project.

On the Backend, we've successfully implemented basic CRUD operations for both profiles & users and needs & resources. Additionally, the authentication system, inclusive of session tokens, is up and running. 

On the Frontend, while the profile implementation is partially completed, we've made headway with the design of a simple map page, put together a draft for the filter menu, and brought the main page and authentication system to life. 

As for Mobile developments, the local database has been implemented, and the 'Need' table, complete with its functionalities, has been set up. Furthermore, users can now view a basic navigation bar, a simple main page, and a dummy profile page. The sign-in and login functions have templates, even addressing scenarios like password mismatches. Moreover, the UI for adding needs has been set in place.


### Objectives for the following week

With only one week left until the Customer Milestone 1 deliverables are due, our focus for the upcoming week will be:


- **Preparation for Milestone**: Making sure the application satisfies what we set out to do for Customer Milestone 1.
- **Backend Objectives**: We will improve and add new functionalities to our services, conduct unit tests regarding our endpoints, and refine the database models.
- **Frontend & Mobile Sync**: The application should be consistent between the front-end and mobile interfaces
- **User scenario implementation**: Preparing a presentation with a plausible user scenario
- **Add resource create-save-edit design implementation**: Preparing frontend form element for a used to add resource
- **Deliverables**: Preparing the deliverables required in the course.

In summary, the next week is about getting prepared to the Milestone 1 as complete as we can, including the basic functionalities of our platform and preparing Deliverables.
 
## What was planned for the week? How did it go?

| Description                                                  | Issue                                                                                                                                                                                                                                                            | Assignee                           | Due        | Estimated Duration | Actual Duration     | Artifacts                                                                                                                 |
| ------------------------------------------------------------ | ----------------------------------------------------------------------------- | ---------------------------------- | ---------- | ------------------ | ------------------- | ------------------------------------------------------------------------------------------------------------------------- |
| Frontend - Simple map page initialization                    | [#309](https://github.com/bounswe/bounswe2023group2/issues/309)                                                                                                                                                                                                  | Şahin                              | 19.10.2023 | 4h                 | 4h                  | [PR](https://github.com/bounswe/bounswe2023group2/pull/364)                                                                                                             |
| Frontend - Simple main page and navigation component         | [#323](https://github.com/bounswe/bounswe2023group2/issues/323)                                                                                                                                                                                                  | Merve                              | 19.10.2023 | 5h                 | 5 h                 | [#315](https://github.com/bounswe/bounswe2023group2/pull/315) [#311](https://github.com/bounswe/bounswe2023group2/pull/311) |
| Frontend - Milestone 1 Mid Review                            | [#331](https://github.com/bounswe/bounswe2023group2/issues/331)                                                                                                                                                                                                  | Front-end Team                     | 20.10.2023 | 1h                 | 30 min              | -                                                                                                                         |
| Backend Team Meeting #2                                      | [#329](https://github.com/bounswe/bounswe2023group2/issues/329)                                                                                                                                                                                                  | Backend Team                       | 18.10.2023 | 40 min             | 1h                  | [Meeting Notes]([url](https://github.com/bounswe/bounswe2023group2/wiki/Backend-Team-Meeting-%232))                                                                                                              |
| Backend - Resource CRUD endpoint implementation              | [#325](https://github.com/bounswe/bounswe2023group2/issues/325)                                                                                                                                                                                                  | Burak                              | 25.10.2023 | 5h                 | 7h                  | [PR #363](https://github.com/bounswe/bounswe2023group2/pull/363)                                                          |
| Backend - Need CRUD endpoint implementation                  | [#326](https://github.com/bounswe/bounswe2023group2/issues/326)                                                                                                                                                                                                  | Buse, Aziza                        | 25.10.2023 | 5h                 | 7h                  | [PR #392](https://github.com/bounswe/bounswe2023group2/pull/392)                                                   |
| Backend - Authentication related endpoint implementation     | [#327](https://github.com/bounswe/bounswe2023group2/issues/327)  [#376](https://github.com/bounswe/bounswe2023group2/issues/376) [#367](https://github.com/bounswe/bounswe2023group2/issues/367) [#368](https://github.com/bounswe/bounswe2023group2/issues/368) | Begüm                              | 25.10.2023 | 5h                 | 15h                 | [PR](https://github.com/bounswe/bounswe2023group2/pull/378)                                                        |
| Frontend - Authentication pages' design and implementation   | [#328](https://github.com/bounswe/bounswe2023group2/issues/328)                                                                                                                                                                                                  | Merve                              | 23.10.2023 | 5h                 | in progress         |                                                                                                                           |
| Frontend - profile page design and implementation            | [#330](https://github.com/bounswe/bounswe2023group2/issues/330)                                                                                                                                                                                         | Can Bora                           | 25.10.2023 | 5h                 | In progress (5h)                 | [Branch link](https://github.com/bounswe/bounswe2023group2/tree/FE/feature/profile-page)                                  |
| Backend - Profile edit-create-delete endpoint implementation | [#324](https://github.com/bounswe/bounswe2023group2/issues/324)                                                                                                                                                                                                  | Mehmet                             | 26.10.2023 | 5h                 |  3h                 | [Related Branch](https://github.com/bounswe/bounswe2023group2/tree/BE/feature/user-profile-CRUD)                                                                                                              |
| Frontend - Adding Emergency button on navbar                 | [#321](https://github.com/bounswe/bounswe2023group2/issues/321)                                                                                                                                                                                                  | Şahin                              | 18.10.2023 | 20 min             | 30min               | [PR](https://github.com/bounswe/bounswe2023group2/pull/355)                                                  |
| RAM preparation                                              | [#332](https://github.com/bounswe/bounswe2023group2/issues/332)                                                                                                                                                                                                  | Şahin                              | 21.10.2023 | 2 h                | 1h                  |   [Link](https://docs.google.com/spreadsheets/d/1dExbvULaOg2xy98QZBBPRROhhyT8A3tUOv84SHuMJoY/edit#gid=0)                                                                                                                        |
| CI/CD backbone/draft prepared                                | [#284](https://github.com/bounswe/bounswe2023group2/issues/284)                                                                                                                                                                                                  | Merve (assisted by: Begüm, Mehmet) | 21.10.2023 | 2 h                | 1 h                 | [#322](https://github.com/bounswe/bounswe2023group2/pull/322)                                                     |
| Research for Annotation standards                            | [#334](https://github.com/bounswe/bounswe2023group2/issues/334)                                                                                                                                                                                                 | Mehmet                             | 24.10.2023 | 1h                 | 1 h                 | [Research Document](https://github.com/bounswe/bounswe2023group2/wiki/Research:-Beginner's-start-on-Annotations)          |
| Mobile - Profile page UI                                     | [#335](https://github.com/bounswe/bounswe2023group2/issues/335)                                                                                                                                                                                                  | Cahid                              | 24.10.2023 | 2h                 | 10h                  | [#375](https://github.com/bounswe/bounswe2023group2/pull/375)                                                             |
| Mobile - Simple main page and navigation bar                 | [#337](https://github.com/bounswe/bounswe2023group2/issues/337)                                                                                                                                                                                                  | Egecan                             | 24.10.2023 | 4h                 | 8 h                 | [#369](https://github.com/bounswe/bounswe2023group2/pull/369)                                                             |
| Mobile - Adding Login and Sign in Functions                  | [#336](https://github.com/bounswe/bounswe2023group2/issues/336)                                                                                                                                                                                                  | Hasan                              | 24.10.2023 | 4h                 | 5 h                 | [#371](https://github.com/bounswe/bounswe2023group2/pull/371)                                                             |
| Mobile - Mobile - Add Need Form                              | [#338](https://github.com/bounswe/bounswe2023group2/issues/338)                                                                                                                                                                                                  | Halil                              | 24.10.2023 | 3h                 | 3 h                 | [PR #415](https://github.com/bounswe/bounswe2023group2/pull/415)                                                                                                              |
| Mobile - Creating User Table in Local Database               | [#340](https://github.com/bounswe/bounswe2023group2/issues/340)                                                                                                                                                                                                  | Cahid                              | 24.10.2023 | 2h                 | In progress         |                                                                                                                           |
| Mobile - Creating Need Table in Local Database               | [#341](https://github.com/bounswe/bounswe2023group2/issues/341)                                                                                                                                                                                                  | Egecan                             | 24.10.2023 | 2h                 | 3h                  | [#370](https://github.com/bounswe/bounswe2023group2/pull/370)                                                             |
| Mobile - Creating Action Table in Local Database             | [#342](https://github.com/bounswe/bounswe2023group2/issues/342)                                                                                                                                                                                                  | Halil                              | 24.10.2023 | 2h                 | In progress         |                                                                                                                           |
| Mobile - Creating Emergency Table in Local Database          | [#343](https://github.com/bounswe/bounswe2023group2/issues/342)                                                                                                                                                                                                  | Egecan                             | 24.10.2023 | 2h                 | In progress         |                                                                                                                           |
| Mobile - Creating Event Table in Local Database              | [#344](https://github.com/bounswe/bounswe2023group2/issues/344)                                                                                                                                                                                                  | Halil                              | 24.10.2023 | 2h                 | In progress         |                                                                                                                           |
| Mobile - Creating Resource Table in Local Database           | [#345](https://github.com/bounswe/bounswe2023group2/issues/345)                                                                                                                                                                                                  | Hasan                              | 24.10.2023 | 2h                 | In progress         |                                                                                                                           |
| Mobile - Team Meeting #1                                     | [#346](https://github.com/bounswe/bounswe2023group2/issues/346)                                                                                                                                                                                                  | Mobile-Team                        | 24.10.2023 | 40min              | Postponed next-week |                                                                                                                           |


## Completed tasks that were not planned for the week

| Description | Issue | Assignee | Due  | Actual Duration | Artifacts |
| ----------------------------------------------------------------------------------------- | ---------------------------------------------------------------- | ------------- | ----------- | ---------------------------------------------------------------------------------------------- | ----------- |
|Frontend - Organizing MainLayout and Creating MapLayout|[#360](https://github.com/bounswe/bounswe2023group2/issues/360)|Şahin|21.10.2023|2h|  |
|Frontend - Adding Main CSS effects|[#366](https://github.com/bounswe/bounswe2023group2/issues/366)|Şahin|24.10.2023|1h|  |
|Frontend - More self-training on technologies | [#287](https://github.com/bounswe/bounswe2023group2/issues/287) | Can Bora | 22.10.2023 | 3h |
|Backend - Deciding on Mongo schema structure of Resource & Need - In progress| [#359](https://github.com/bounswe/bounswe2023group2/issues/359)  | Backend Team | 21.10.2023 | 3h | [Issue](https://github.com/bounswe/bounswe2023group2/issues/359) - [PR](https://github.com/bounswe/bounswe2023group2/pull/363)
|Documentation: Docker usage and local deployment|[#349](https://github.com/bounswe/bounswe2023group2/issues/349)| Merve |21.10.2023|2h|[Document](https://github.com/bounswe/bounswe2023group2/wiki/Docker-and-local-deployment-tutorial)  |
| Mobile - User Classes | [#373](https://github.com/bounswe/bounswe2023group2/issues/373) | Cahid   | 24.10.2023 | 1h | [PR #375](https://github.com/bounswe/bounswe2023group2/pull/375)  |

## Planned vs. Actual
 We have spend several hours to decide authentication mechanism. We need to decide which library we use and learn next.js iron-session library. So authentication pages are not completed yet. 



## Plans for the next week

| Description | Issue | Assignee | Due | Estimated Duration |
| ----------- | ----- | -------- | --- | ------------------ |
| Frontend - Showing new created activity on map  |[#405](https://github.com/bounswe/bounswe2023group2/issues/405)  | Şahin | 30.10.2023 | 5h |
| Frontend - Milestone 1 review |[#388](https://github.com/bounswe/bounswe2023group2/issues/388) |Frontend Team |30.10.2023 | 30min | 
|Frontend - CRUD Activity Pages|[#385](https://github.com/bounswe/bounswe2023group2/issues/385)|Frontend Team|31.10.2023|6h|
|Frontend - resource pop up component implementation|[#394](https://github.com/bounswe/bounswe2023group2/issues/394)|Merve|26.10.2023|3h|
|Frontend - Implement add button to the main page|[#396](https://github.com/bounswe/bounswe2023group2/issues/396)|Merve|27.10.2023|1h|
|Frontend - Implement add button to the map page |[#398](https://github.com/bounswe/bounswe2023group2/issues/398)|Şahin|30.10.2023|4h|
|Frontend - Make Map interactive to click|[#402](https://github.com/bounswe/bounswe2023group2/issues/402)|Şahin|30.10.2023|3h|
| Backend - Milestone 1 review                        | [#382](https://github.com/bounswe/bounswe2023group2/issues/382)     | Backend Team            | 30.10.2023 | 3h                |
| Test - Backend - Need CRUD Endpoints      | [#381](https://github.com/bounswe/bounswe2023group2/issues/381)     | Burak                                    | 30.10.2023 | 3h                |
| Test - Backend - Resource CRUD Endpoints      | [#406](https://github.com/bounswe/bounswe2023group2/issues/406)     | Buse                              | 30.10.2023 | 3h                |
| Test - Backend - Profile CRUD endpoints             | [#380](https://github.com/bounswe/bounswe2023group2/issues/380)     | Mehmet                                         | 30.10.2023 | 3h                |
| Backend - Improvements Profile CRUD endpoints             | [#399](https://github.com/bounswe/bounswe2023group2/issues/399) [#393](https://github.com/bounswe/bounswe2023group2/issues/393) [#395](https://github.com/bounswe/bounswe2023group2/issues/395) [#397](https://github.com/bounswe/bounswe2023group2/issues/397) [#408](https://github.com/bounswe/bounswe2023group2/issues/408)  | Mehmet                                         | 30.10.2023 | 5h                |      | Mehmet                                         | 30.10.2023 | 3h                | 
| Backend - Improvements Authentication CRUD endpoints             | [#403](https://github.com/bounswe/bounswe2023group2/issues/403)     | Begüm                                         | 30.10.2023 | 3h                |
| Backend - Improvements Need CRUD endpoints             | [#400](https://github.com/bounswe/bounswe2023group2/issues/400)     | Buse,Aziza                                        | 30.10.2023 | 2h                |
| Backend - Improvements Resource CRUD endpoints             | [#404](https://github.com/bounswe/bounswe2023group2/issues/404)     | Burak                                        | 30.10.2023 | 2h                |
| Test - Backend - Authentication endpoints           | [#379](https://github.com/bounswe/bounswe2023group2/issues/379)     | Begüm                                          | 30.10.2023 | 2h                |
| Mobile - Team Meeting #1 | [#346](https://github.com/bounswe/bounswe2023group2/issues/346)     | Mobile Team              | 30.10.2023 | 1h                |
| Mobile - Milestone 1 Review                         | [#389](https://github.com/bounswe/bounswe2023group2/issues/389)     | Mobile Team              | 30.10.2023 | 3h                |
| Mobile - CRUD Resource Pages                        | [#387](https://github.com/bounswe/bounswe2023group2/issues/387) [#412](https://github.com/bounswe/bounswe2023group2/issues/412) [#413](https://github.com/bounswe/bounswe2023group2/issues/413) [#414](https://github.com/bounswe/bounswe2023group2/issues/414)   | Egecan, Halil                               | 30.10.2023 | 8h                |
| Mobile - Simple map page initialization             | [#383](https://github.com/bounswe/bounswe2023group2/issues/383)     | Hasan, Egecan | 30.10.2023 | 6h  |
| Mobile - Creating local database for offline records |   [#339](https://github.com/bounswe/bounswe2023group2/issues/339) [#340](https://github.com/bounswe/bounswe2023group2/issues/340) [#342](https://github.com/bounswe/bounswe2023group2/issues/342) [#343](https://github.com/bounswe/bounswe2023group2/issues/343) [#344](https://github.com/bounswe/bounswe2023group2/issues/344) [#345](https://github.com/bounswe/bounswe2023group2/issues/345)  |   Hasan, Egecan                                 | 30.10.2023 | 6h                |
| Mobile - CRUD Need Pages             | [#347](https://github.com/bounswe/bounswe2023group2/issues/347) [#410](https://github.com/bounswe/bounswe2023group2/issues/410) [#414](https://github.com/bounswe/bounswe2023group2/issues/414)  |  Egecan                                 | 30.10.2023 | 6h                |
| Mobile - Connect backend for Profile Information  | [#416 ](https://github.com/bounswe/bounswe2023group2/issues/416 )     | Cahid Enes | 30.10.2023 | 4h  |
| Mobile - Login and Registration Connecting into Backend | [#411 ](https://github.com/bounswe/bounswe2023group2/issues/411 )  | Hasan | 30.10.2023 | 4h  |
| Milestone 1 demo preparation - deploy check             | [#390](https://github.com/bounswe/bounswe2023group2/issues/390)  | All team                             | 31.10.2023 | 5h               |
|Frontend - Adding API utils            | [#407](https://github.com/bounswe/bounswe2023group2/issues/407)  | Merve                             | 31.10.2023 | 2h               |
|Frontend - Making Activity Table more flexible            | [#409](https://github.com/bounswe/bounswe2023group2/issues/409)  | Can Bora                             | 28.10.2023 | 2h               |
|Frontend - Finishing profile page implementation            | [#330](https://github.com/bounswe/bounswe2023group2/issues/330)  | Can Bora                             | 25.10.2023 | 4h               |
|Milestone 1 - Prepare Demos (Scenarios, Personas...)            | [#418](https://github.com/bounswe/bounswe2023group2/issues/418)  |  All team                            | 25.10.2023 | 4h               |
|Milestone 1 - Prepare Deliverables)            | [#419](https://github.com/bounswe/bounswe2023group2/issues/419)  |  All team                            | 25.10.2023 | 4h               |
|Milestone 1 - Deployment             | [#420](https://github.com/bounswe/bounswe2023group2/issues/420)  |  Merve                            | 25.10.2023 | 1h               |


## Risks
- Unit tests and integration with UI may uncover bugs for the backend code we produced and tested already
- Unforeseen difficulties may arise for Activity visualization on map (as this is a rather new area for us)
- Possible earthquake in Istanbul before we complete the project. (that is why we work hard)
- World War III


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
