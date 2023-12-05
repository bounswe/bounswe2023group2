# Project Development Weekly Progress Report

**Team Name:** Disaster Response Platform (DaRP)

**Date:** 5.12.2023

## Progress Summary


On the backend, we implemented action, admin, feedback, recurrence endpoints. Also resource and need are enhanced.

On the frontend side:
- we added dynamic forms and a json file (for our use) containing the predefined types and subtypes of resources and needs.
- The profile page is complete except for the profile picture.
- Sort - filter design and implementation has finished.
- Activity cards containing activity info added

As for Mobile developments:
- Profile is connected to backend, users can edit their profile and see their profile info from backend.
- Need and resources have specific page for each object. When user clicks the object in list, its detailed page is shown. In this page user can edit the object (if they have authority), see its details. This page also contains buttons for showing creator's profile, deleting (if user has authority), upvoting, downvoting buttons.
- Appearance of List recyclerview items is redesigned, now they are colorful and user-friendly.
- Map shows created activities with pinpoints, if created activities closes each other it firstly shows how many of them are there, as users zoom in show the exact location.



### Objectives for the following week
As backend team we will be translating fields that are provided to front-end/mobile to Turkish. We will be working on action and recurrence design,email-verification, emergency, annotation. 

### Priority for Final Milestone:

1. **Annotation (1.2.6)**
   - **Description:** Implement and refine annotation functionality.
   - **Reason:** Strongly requested by the customer.

2. **Action (1.2.3.3)**
   - **Description:** Enhance and optimize action-related features.
   - **Reason:** Adds coordination to our application, required and useful in case of a disaster.

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
|Frontend - Frontend - Email verification page design and implementation | [#479](https://github.com/bounswe/bounswe2023group2/issues/479) | Merve | 28.11.2023 | 4h
|Frontend - Admin page design and implementation | [#486](https://github.com/bounswe/bounswe2023group2/issues/486) | Merve, Can Bora | 28.11.2023 | 8h
|Frontend - User upvote-downvote feature | [#518](https://github.com/bounswe/bounswe2023group2/issues/518) | Merve | 28.11.2023 | 4h
|Frontend - Milestone 2 Mid-review | [#527](https://github.com/bounswe/bounswe2023group2/issues/527) | Merve, Şahin, Can Bora | 22.11.2023 | 1h
|Frontend - Add ability to add/remove profile picture | [#562](https://github.com/bounswe/bounswe2023group2/issues/562) | Can Bora | 28.11.2023 | 2h
|Frontend - Add user filter to activity table | [#563](https://github.com/bounswe/bounswe2023group2/issues/563) | Merve | 28.11.2023 | 1h
|Frontend - Viewing others' profiles | [#568](https://github.com/bounswe/bounswe2023group2/issues/568) | Can Bora | 28.11.2023 | 4h
| Mobile - Map Activity Redirect | [#570](https://github.com/bounswe/bounswe2023group2/issues/570) | Hasan, Cahid | 28.11.2023 | 2h | 6h | [PR](https://github.com/bounswe/bounswe2023group2/pull/603)
| Mobile - Choose Location Through Map View | [#572](https://github.com/bounswe/bounswe2023group2/issues/572) | Hasan, Cahid | 28.11.2023 | 4h | 4h | [PR](https://github.com/bounswe/bounswe2023group2/pull/618)
| Mobile - Action Visualization | [#573](https://github.com/bounswe/bounswe2023group2/issues/573) | Hasan | 28.11.2023 | 6h
| Mobile - Main Page Design | [#582](https://github.com/bounswe/bounswe2023group2/issues/582) | Halil | 27.11.2023 | 4h | In Progress |
|Mobile - Mobile - Get Subtypes From Backend | [#542](https://github.com/bounswe/bounswe2023group2/issues/542) | Halil | 27.11.2023 | 5h | In Progress |
|Mobile - Sort and filter components and implementations | [#540](https://github.com/bounswe/bounswe2023group2/issues/540) | Halil | 27.11.2023 | 6h
|Backend - Action implementation | [#477](https://github.com/bounswe/bounswe2023group2/issues/477) | Begüm | 26.11.2023 | 6h | 24h| [PR1](https://github.com/bounswe/bounswe2023group2/pull/577),[PR2](https://github.com/bounswe/bounswe2023group2/pull/641)
|Test - Backend - User role endpoints | [#523](https://github.com/bounswe/bounswe2023group2/issues/523) | Begüm | 24.11.2023 | 3h |2h |[PR](https://github.com/bounswe/bounswe2023group2/pull/505/files)
|Test - Backend - Action implementation | [#517](https://github.com/bounswe/bounswe2023group2/issues/517) | Begüm | 26.11.2023 | 3h | 3h | [PR](https://github.com/bounswe/bounswe2023group2/pull/615)
|Backend - Feedback system upvote/downvote endpoint  | [#480](https://github.com/bounswe/bounswe2023group2/issues/480)| Aziza| 26.11.2023|5h|
|Test - Backend - Feedback endpoints | [#524](https://github.com/bounswe/bounswe2023group2/issues/524)| Aziza| 26.11.2023|2h|
|Backend - Admin CRUD and specific endpoint implementations| [#519](https://github.com/bounswe/bounswe2023group2/issues/519)| Aziza, Buse, Mehmet K.| 26.11.2023|5h| 
Backend - Implement the validation check for the coordinates/quantities of resources and needs| [#584](https://github.com/bounswe/bounswe2023group2/issues/584)|Aziza| 24.11.2023| 2h
|Mobile - CRUD Event Pages  | [#539](https://github.com/bounswe/bounswe2023group2/issues/539)| Egecan | 26.11.2023| 4h | 
|Mobile - Vote Needs and Resources | [#543](https://github.com/bounswe/bounswe2023group2/issues/543)| Egecan | 25.11.2023| 3h |
|Mobile - Admin Page Design | [#578](https://github.com/bounswe/bounswe2023group2/issues/578)| Egecan | 27.11.2023| 9h | 
|Mobile - Save User Roles | [#583](https://github.com/bounswe/bounswe2023group2/issues/583)| Egecan | 27.11.2023| 2h | 
|Mobile - Post Need & Resource when Connecting Internet Again | [#586](https://github.com/bounswe/bounswe2023group2/issues/586)| Egecan | 27.11.2023| 4h |

## Completed tasks that were not planned for the week

| Description | Issue | Assignee | Due  | Actual Duration | Artifacts |
| - | - | - | - | - | - |
| | | | | | |
|Backend - Recurring design and implementation | [#587](https://github.com/bounswe/bounswe2023group2/issues/587) | Begüm | 28.11.2023 | 6h| [Commit ID](https://github.com/bounswe/bounswe2023group2/commit/d1dfd65daca300a3d03e556cc39e194bf5568368)
|Small fixes | [#596](https://github.com/bounswe/bounswe2023group2/issues/596) | Begüm | 28.11.2023 | 2h| [PR](https://github.com/bounswe/bounswe2023group2/pull/595), [PR](https://github.com/bounswe/bounswe2023group2/pull/647), [PR](https://github.com/bounswe/bounswe2023group2/pull/645) , [PR](https://github.com/bounswe/bounswe2023group2/pull/644)
Profile Signin Redirect | [#632](https://github.com/bounswe/bounswe2023group2/issues/632) | Cahid | 28.11.2023 | 1h | [PR](https://github.com/bounswe/bounswe2023group2/pull/633)
Map Icon Colors | [#643](https://github.com/bounswe/bounswe2023group2/issues/643) | Cahid | 28.11.2023 | 2h | [PR](https://github.com/bounswe/bounswe2023group2/pull/642)

## Planned vs. Actual
As the backend team, we mostly did what we have planned. We even did additional things(like recurrency) for the sake of scenerio that we could not have foreseen during the meeting. Action implementation is not done as it's nature. 
We also must have forgotten to add Milestone preparation issues to the report.
As mobile team, we accoplished all our goals and additionally we made quite a lot of polishing for the milestone.



## Plans for the next week

| Description | Issue | Assignee | Due | Estimated Duration |
| ----------- | ----- | -------- | --- | ------------------ |
|Backend - Recurrence improvement | [#670](https://github.com/bounswe/bounswe2023group2/issues/670) | Begüm | 09.12.2023 | 5h| 
|Backend - Action Improvement | [#671](https://github.com/bounswe/bounswe2023group2/issues/671) | Begüm | 12.12.2023 | 10h| 
| Backend - Unit tests revision |[#674](https://github.com/bounswe/bounswe2023group2/issues/674) | Mehmet K. | 10.12.2023| 3h|
| Design and decide the annotation use| [#675](https://github.com/bounswe/bounswe2023group2/issues/675) | Mehmet K. | 9.12.2023 | 3h| 
| Backend - Annotation functions - Part 1  | [#676](https://github.com/bounswe/bounswe2023group2/issues/676) |Mehmet K. | 12.12.2023 | 3h| 
| Backend - Probable revisions on profile APIs | [#677](https://github.com/bounswe/bounswe2023group2/issues/677) | Mehmet K. | 12.12.2023 | 2h|
|Translation - Language Packs |[#678](https://github.com/bounswe/bounswe2023group2/issues/678)|Backend team| 12.12.2023| 2h|

## Risks
- Action design needs to be simplified. There can be lots of bugs with it.

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
