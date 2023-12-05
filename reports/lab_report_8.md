# Project Development Weekly Progress Report

**Team Name:** Disaster Response Platform (DaRP)

**Date:** 5.12.2023

## Progress Summary


On the backend:

On the frontend side:
- Action creation form was implemented.
- We implemented most of the admin page, but it is not complete.
- We implemented the ability to see other user's profiles.
- Sort and filter implemented.
- Navigation bar changed to nextui from tailwind components.
- Need, resource and action forms enhanced.
- Modal card containing activities implemented.

As for Mobile developments:
- We redesigned main page design as it contains the actions with pretty UI/UX.
- We showed actions on map with direction arrow.
- We implemented dynamic form that gets fields from backend for need and resources.
- We implemented sort and filter for need and resources as much as backend's implementations.
- We improved our UI design
- We added location from map for creating need and resources
- We rearranged needs and resources with respect to last changes on backend
- We implemented upvote/downvote feature
- We connected backend for user Roles to show them detail pages of need and resources


### Objectives for the following week
As backend team we will be translating fields that are provided to front-end/mobile to Turkish. We will be working on action and recurrence design,email-verification, emergency, annotation.

As frontend team we will focus on map functionalities defined in the requirements, we need to enhance activity table. Moreover we did not implement the last activity type `event` and emergency forms. We will focus on implementing the feature as well as enhancing the current ones.
### Priority for Final Milestone:

1. **Annotation (1.2.6)**
   - **Description:** Implement and refine annotation functionality.
   - **Reason:** Strongly requested by the customer.

2. **Action (1.2.3.3)**
   - **Description:** Enhance and optimize action-related features.
   - **Reason:** Adds coordination to our application, required and useful in case of a disaster.
3. **Event (1.2.3.3)**
   - **Description:** Implement the `event` described in the glossary.
   - **Reason:** Event shows the disaster-related static happenings created by users (the road is blocked, buildings are destroyed, power cut.

3. **Emergency (1.1.3.6)**
   - **Description:** Ensure robustness and effectiveness of emergency-related functionalities.
   - **Reason:** Essential for emergent cases where users may be unable to log in.

4. **Email Verification (1.1.1.2.2)**
   - **Description:** Implement and fine-tune email verification processes.
   - **Reason:** Vital for user verification purposes.
5. **Search (1.1.2.2.10)**
   - **Description:** Develop and optimize search functionality.
   - **Reason:** Invaluable in our disaster-focused application for collecting and organizing information.

**Additional Customer Requirement: Recurrent Need and Resources:**
   - **Description:** Implemented in Milestone 2; fine-tuning required for optimal performance.

---

**Notification and SMS Limitation:**
   - Unfortunately, due to budget constraints, we regret to inform you that we are unable to incorporate notifications and SMS features at this time. Please be assured that our team is committed to delivering a high-quality product within the specified limitations.

**Comparison with Practice-App:**
   - We have communicated with the customer about the absence of SMS features in our solution, citing budgetary constraints. It's worth noting that these features have been successfully implemented in the practice app.




## What was planned for the week? How did it go?

| Description                                             | Issue                                                                                                                                                                                                                                                                                                                                                                                           | Assignee      | Due        | Estimated Duration | Actual Duration | Artifacts |
| ---------------------------- | ------------------------------------------ | ------------- | ---------- | ------------------ | --------------- | --------- 
|Mobile - App Icon | [#534](https://github.com/bounswe/bounswe2023group2/issues/534) | Cahid | 21.11.2023 | 2h | 1h
|Frontend - Frontend - Email verification page design and implementation | [#479](https://github.com/bounswe/bounswe2023group2/issues/479) | Merve | 28.11.2023 | 4h | 1h    (in-progress) | 
|Frontend - Admin page design and implementation | [#486](https://github.com/bounswe/bounswe2023group2/issues/486) | Merve, Can Bora | 28.11.2023 | 8h | 6h | [#628](https://github.com/bounswe/bounswe2023group2/pull/628)
|Frontend - User upvote-downvote feature | [#518](https://github.com/bounswe/bounswe2023group2/issues/518) | Merve | 28.11.2023 | 4h | 2h | [PR](https://github.com/bounswe/bounswe2023group2/pull/665) 
|Frontend - Milestone 2 Mid-review | [#527](https://github.com/bounswe/bounswe2023group2/issues/527) | Merve, Şahin, Can Bora | 22.11.2023 | 1h | 1h
|Frontend - Add ability to add/remove profile picture | [#562](https://github.com/bounswe/bounswe2023group2/issues/562) | Can Bora | 28.11.2023 | 2h | Pending
|Frontend - Add user filter to activity table | [#563](https://github.com/bounswe/bounswe2023group2/issues/563) | Merve | 28.11.2023 | 1h | 3h | [PR](https://github.com/bounswe/bounswe2023group2/pull/665)
|Frontend - Viewing others' profiles | [#568](https://github.com/bounswe/bounswe2023group2/issues/568) | Can Bora | 28.11.2023 | 4h | 6h
| Mobile - Map Activity Redirect | [#570](https://github.com/bounswe/bounswe2023group2/issues/570) | Hasan, Cahid | 28.11.2023 | 2h | 6h | [PR](https://github.com/bounswe/bounswe2023group2/pull/603)
| Mobile - Choose Location Through Map View | [#572](https://github.com/bounswe/bounswe2023group2/issues/572) | Hasan, Cahid | 28.11.2023 | 4h | 4h | [PR](https://github.com/bounswe/bounswe2023group2/pull/618)
| Mobile - Action Visualization | [#573](https://github.com/bounswe/bounswe2023group2/issues/573) | Hasan | 28.11.2023 | 6h | 8h | [PR](https://github.com/bounswe/bounswe2023group2/pull/636)
| Mobile - Main Page Design | [#582](https://github.com/bounswe/bounswe2023group2/issues/582) | Halil | 27.11.2023 | 4h | 5h | [PR598](https://github.com/bounswe/bounswe2023group2/pull/598), [PR614](https://github.com/bounswe/bounswe2023group2/pull/614) 
|Mobile - Mobile - Get Subtypes From Backend | [#542](https://github.com/bounswe/bounswe2023group2/issues/542) | Halil | 27.11.2023 | 5h | 7h | [PR604](https://github.com/bounswe/bounswe2023group2/pull/604) |
|Mobile - Sort and filter components and implementations | [#540](https://github.com/bounswe/bounswe2023group2/issues/540) | Halil | 27.11.2023 | 6h | 12h | [PR605](https://github.com/bounswe/bounswe2023group2/pull/605), [PR637](https://github.com/bounswe/bounswe2023group2/pull/637), [PR638](https://github.com/bounswe/bounswe2023group2/pull/638) |
|Backend - Action implementation | [#477](https://github.com/bounswe/bounswe2023group2/issues/477) | Begüm | 26.11.2023 | 6h | 24h| [PR1](https://github.com/bounswe/bounswe2023group2/pull/577),[PR2](https://github.com/bounswe/bounswe2023group2/pull/641)
|Test - Backend - User role endpoints | [#523](https://github.com/bounswe/bounswe2023group2/issues/523) | Begüm | 24.11.2023 | 3h |2h |[PR](https://github.com/bounswe/bounswe2023group2/pull/505/files)
|Test - Backend - Action implementation | [#517](https://github.com/bounswe/bounswe2023group2/issues/517) | Begüm | 26.11.2023 | 3h | 3h | [PR](https://github.com/bounswe/bounswe2023group2/pull/615)
|Backend - Feedback system upvote/downvote endpoint  | [#480](https://github.com/bounswe/bounswe2023group2/issues/480)| Aziza| 26.11.2023|5h|5h| [PR #590](https://github.com/bounswe/bounswe2023group2/pull/590)
|Test - Backend - Feedback endpoints | [#524](https://github.com/bounswe/bounswe2023group2/issues/524)| Aziza| 26.11.2023|2h| 2h| [PR #660](https://github.com/bounswe/bounswe2023group2/pull/660)
|Backend - Admin CRUD and specific endpoint implementations| [#519](https://github.com/bounswe/bounswe2023group2/issues/519)| Aziza, Buse, Mehmet K.| 26.11.2023|5h| 6h| [PR #621](https://github.com/bounswe/bounswe2023group2/pull/621)
Backend - Implement the validation check for the coordinates/quantities of resources and needs| [#584](https://github.com/bounswe/bounswe2023group2/issues/584)|Aziza| 24.11.2023| 2h| 3h| [PR #594](https://github.com/bounswe/bounswe2023group2/pull/594)
|Mobile - CRUD Event Pages  | [#539](https://github.com/bounswe/bounswe2023group2/issues/539)| Egecan | 26.11.2023| 4h | In Progress (2h) | [branch](https://github.com/bounswe/bounswe2023group2/tree/MO/feature/CRUD-Event) |
|Mobile - Vote Needs and Resources | [#543](https://github.com/bounswe/bounswe2023group2/issues/543)| Egecan | 25.11.2023| 3h | 3h | [#640](https://github.com/bounswe/bounswe2023group2/pull/640) |
|Mobile - Admin Page Design | [#578](https://github.com/bounswe/bounswe2023group2/issues/578)| Egecan | 27.11.2023| 9h | Postponed | |
|Mobile - Save User Roles | [#583](https://github.com/bounswe/bounswe2023group2/issues/583)| Egecan | 27.11.2023| 2h | 2h | [#651](https://github.com/bounswe/bounswe2023group2/pull/651) | 
|Mobile - Post Need & Resource when Connecting Internet Again | [#586](https://github.com/bounswe/bounswe2023group2/issues/586)| Egecan | 27.11.2023| 4h | Postponed | |
| Backend - Filter (Resource and Need) | [#566](https://github.com/bounswe/bounswe2023group2/issues/566) |Burak | 27.11.2023 | 4h | 4h | [PR #612](https://github.com/bounswe/bounswe2023group2/pull/612)
| Backend - Sort (Resource and Need) | [#609](https://github.com/bounswe/bounswe2023group2/issues/609) |Burak | 27.11.2023 | 4h | 3h | [PR #612](https://github.com/bounswe/bounswe2023group2/pull/612)


## Completed tasks that were not planned for the week

| Description | Issue | Assignee | Due  | Actual Duration | Artifacts |
| - | - | - | - | - | - |
|Backend - Recurring design and implementation | [#587](https://github.com/bounswe/bounswe2023group2/issues/587) | Begüm | 28.11.2023 | 6h| [Commit ID](https://github.com/bounswe/bounswe2023group2/commit/d1dfd65daca300a3d03e556cc39e194bf5568368)
|Small fixes | [#596](https://github.com/bounswe/bounswe2023group2/issues/596) | Begüm | 28.11.2023 | 2h| [PR](https://github.com/bounswe/bounswe2023group2/pull/595), [PR](https://github.com/bounswe/bounswe2023group2/pull/647), [PR](https://github.com/bounswe/bounswe2023group2/pull/645) , [PR](https://github.com/bounswe/bounswe2023group2/pull/644)
Profile Signin Redirect | [#632](https://github.com/bounswe/bounswe2023group2/issues/632) | Cahid | 28.11.2023 | 1h | [PR](https://github.com/bounswe/bounswe2023group2/pull/633)
Map Icon Colors | [#643](https://github.com/bounswe/bounswe2023group2/issues/643) | Cahid | 28.11.2023 | 2h | [PR](https://github.com/bounswe/bounswe2023group2/pull/642)
Swagger fix|  | Merve | 28.11.2023 | 3h | [PR](https://github.com/bounswe/bounswe2023group2/pull/627)
Deployment error in frontend |  | Merve | 28.11.2023 | 2h | [PR](https://github.com/bounswe/bounswe2023group2/pull/599)
Add action on frontend |  | Merve | 28.11.2023 | 8h | [PR](https://github.com/bounswe/bounswe2023group2/pull/648)
| Frontend - Main page bugfix | [#616](https://github.com/bounswe/bounswe2023group2/issues/616) | Can Bora | 27.11.2023 | 15m | [PR #617](https://github.com/bounswe/bounswe2023group2/pull/617) |
| Mobile - Arranging CRUD Needs and Resources | [#610](https://github.com/bounswe/bounswe2023group2/issues/610) | Egecan | 26.11.2023 | 8h | [#624](https://github.com/bounswe/bounswe2023group2/pull/624),[#634](https://github.com/bounswe/bounswe2023group2/pull/634) |
| Mobile - UI improvements | [#601](https://github.com/bounswe/bounswe2023group2/issues/601) | Halil | 26.11.2023 | 4h | [#611](https://github.com/bounswe/bounswe2023group2/pull/611) |
| Requirements Update  | [#661](https://github.com/bounswe/bounswe2023group2/issues/661) | Halil | 05.12.2023 | 3h | - |
| Backend - Form Fields Controller Ehancement | [#585](https://github.com/bounswe/bounswe2023group2/issues/585) |Burak | 27.11.2023 | 4h | [PR #593](https://github.com/bounswe/bounswe2023group2/pull/593), [PR #631](https://github.com/bounswe/bounswe2023group2/pull/631)
| Backend - Resource and Need Refinements | [#591](https://github.com/bounswe/bounswe2023group2/issues/591), [#592](https://github.com/bounswe/bounswe2023group2/issues/592) |Burak | 27.11.2023 | 4h | [PR #593](https://github.com/bounswe/bounswe2023group2/pull/593)
| Logo Design | [#600](https://github.com/bounswe/bounswe2023group2/issues/600) |Burak | 27.11.2023 | 3h | [Logo Files](https://github.com/bounswe/bounswe2023group2/wiki/FE-MO-Common-Resources#logo)
| Milestone report | [#662](https://github.com/bounswe/bounswe2023group2/issues/662)  |All team | 27.11.2023 | 6h | [Report]( https://github.com/bounswe/bounswe2023group2/pull/665)



## Planned vs. Actual
As the backend team, we mostly did what we have planned. We even did additional things(like recurrency) for the sake of scenerio that we could not have foreseen during the meeting. Since action has a complex system, it contains all the features implemented before, It took several hours to fix buges.
As frontend team, we couldn't test the admin page enough, but it will get fixed. We needed to implement many features just before the milestone demo, since there were many logical desicions to make before the implementation. We had expected to have more time on both testing and design. 
As a mobile team, even though we have done a lot of work for this milestone, we had to postpone some of the work we had targeted. One of the reasons for this was the reappearance of work that we had assumed to be closed in the past, and the change of the backend and therefore the mobile application of some completed work due to design issues. 

We also have forgotten to add Milestone preparation issues to the report.

## Plans for the next week

| Description | Issue | Assignee | Due | Estimated Duration |
| ----------- | ----- | -------- | --- | ------------------ |
|Backend - Recurrence improvement | [#670](https://github.com/bounswe/bounswe2023group2/issues/670) | Begüm | 09.12.2023 | 5h| 
|Backend - Action Improvement | [#671](https://github.com/bounswe/bounswe2023group2/issues/671) | Begüm | 12.12.2023 | 10h| 
|Mobile -Action implementation | [#669](https://github.com/bounswe/bounswe2023group2/issues/669) | Hasan | 12.12.2023 | 10h| 
| Backend - Unit tests revision |[#674](https://github.com/bounswe/bounswe2023group2/issues/674) | Mehmet K. | 10.12.2023| 3h|
| Design and decide the annotation use| [#675](https://github.com/bounswe/bounswe2023group2/issues/675) | Mehmet K. | 9.12.2023 | 3h| 
| Backend - Annotation functions - Part 1  | [#676](https://github.com/bounswe/bounswe2023group2/issues/676) |Mehmet K. | 12.12.2023 | 3h| 
| Backend - Probable revisions on profile APIs | [#677](https://github.com/bounswe/bounswe2023group2/issues/677) | Mehmet K. | 12.12.2023 | 2h|
|Translation - Language Packs |[#678](https://github.com/bounswe/bounswe2023group2/issues/678)|Backend team| 12.12.2023| 2h |
| Mobile - Show user from activities | [#637](https://github.com/bounswe/bounswe2023group2/issues/673) | Cahid | 12.12.2023 | 2h
| Mobile - Role Based / Credible Profile | [#679](https://github.com/bounswe/bounswe2023group2/issues/679) | Cahid | 12.12.2023 | 3h
| Mobile - Choose a Role | [#680](https://github.com/bounswe/bounswe2023group2/issues/680) | Cahid | 12.12.2023 | 4h
| Frontend- Implementing area select for filtering | [#682](https://github.com/bounswe/bounswe2023group2/issues/682) | Şahin | 12.12.2023 | 5h
| Frontend - Adjusting Filter menu according to backend | [#681](https://github.com/bounswe/bounswe2023group2/issues/681) | Şahin | 12.12.2023 | 4h
| Frontend - Map visualization for need, action, event and resources | [#525](https://github.com/bounswe/bounswe2023group2/issues/525) | Şahin | 12.12.2023 | 2h
| Frontend - Create Activity information card for map page | [#476](https://github.com/bounswe/bounswe2023group2/issues/476) | Şahin | 12.12.2023 | 4h
| Frontend - Connecting Backend to Map Page | [#474](https://github.com/bounswe/bounswe2023group2/issues/474) | Şahin | 12.12.2023 | 2h
| Frontend - Add event form | [#683](https://github.com/bounswe/bounswe2023group2/issues/683) | Can Bora | 12.12.2023 | 4h |
| Frontend - View events on main page | [#684](https://github.com/bounswe/bounswe2023group2/issues/684) | Can Bora | 12.12.2023 | 4h |
| Frontend - Add emergency form | [#685](https://github.com/bounswe/bounswe2023group2/issues/685) | Can Bora | 12.12.2023 | 4h |
| Frontend - Add emergency form to main page | [#687](https://github.com/bounswe/bounswe2023group2/issues/687) | Can Bora | 12.12.2023 | 4h |
| Backend - Add s3 image upload endpoints  | [#694](https://github.com/bounswe/bounswe2023group2/issues/694) | Merve | 12.12.2023 | 4h |
| Backend - Action logic revisit  | [#695](https://github.com/bounswe/bounswe2023group2/issues/695) | Merve | 12.12.2023 | 8h |
| Frontend - Enhancing main page components   | [#696](https://github.com/bounswe/bounswe2023group2/issues/696) | Merve | 12.12.2023 | 6h |
| Frontend - Highlighting currently selected activity table | [#688](https://github.com/bounswe/bounswe2023group2/issues/688) | Can Bora | 12.12.2023 | 2h |
|Mobile - CRUD Event Pages  |[#539](https://github.com/bounswe/bounswe2023group2/issues/539)| Egecan | 12.12.2023| 3h |
|Mobile - Admin Page Design |[#578](https://github.com/bounswe/bounswe2023group2/issues/578)| Egecan | 12.12.2023| 9h |
|Mobile - Post Need & Resource when Connecting Internet Again | [#586](https://github.com/bounswe/bounswe2023group2/issues/586)| Egecan | 12.12.2023| 6h |
| Mobile - Resource detail page design | [#689](https://github.com/bounswe/bounswe2023group2/issues/689)| Halil | 11.12.2023| 3h |
| Mobile - Sort / Filter by Location and Date | [#690](https://github.com/bounswe/bounswe2023group2/issues/690)| Halil | 11.12.2023| 4h |
| Mobile - Action list and detail page | [#693](https://github.com/bounswe/bounswe2023group2/issues/586)| Halil | 11.12.2023| 4h |
|Test - Backend - Admin endpoints| [#686](https://github.com/bounswe/bounswe2023group2/issues/686)|Aziza| 12.12.2023 | 4h|
|Backend - Emergency endpoints| [#691](https://github.com/bounswe/bounswe2023group2/issues/691)|Aziza| 12.12.2023 | 6h|
| Backend - Email Verification | [#473](https://github.com/bounswe/bounswe2023group2/issues/473) | Burak | 12.12.2023 | 5h





## Risks
- The action design, as it currently stands, is complicated, so it may have bugs.

## Participants

- Egecan Serbester
- Begüm Arslan
- Merve Gürbüz
- Cahid Enes Keleş
- Aziza Mankenova
- Mehmet Kuzulugil
- Ramazan Burak Sarıtaş
- Halil İbrahim Gürbüz
- Can Bora Uğur
- Ömer Şahin Albayram
- Hasan Bingölbali
- ~~Buse Tolunay~~
