# Project Development Weekly Progress Report

**Team Name:** Disaster Response Platform (DaRP)

**Date:** 21.11.2023

## Progress Summary


On the backend, we implemented event, user-role and report endpoints. Also resource and need are enhanced.

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

We have a list of features to finish before Milestone 2, detailed in the *"plans for next week"* table below. We'll need to prepare the deliverables and plan a scenario for the presentation. We also need to go through some decision-making about filtering and searching, which are nontrivial tasks.

### Scenario Draft
We currently have three users. The town mayor of Sultan Selim, Ahmet utilizes the DaRP application to express a recurring need for warm food to accommodate 50 people, twice a day. The community members using the app show their support for this need by upvoting it. Simultaneously, there is an existing entry for the availability of warm food in Levent which is close to this location.

Upon recognizing the upvoted need, the coordinator Ayşe decides to transport the food to the people in need. She access the application, filter for food-related needs, and sort them based on upvotes. Identifying the town in question, Ayşe notices need is entered by mayor of Sultan Selim. Because mayor is added by an admin as credible user to the application. the coordinator initiates a verification process by calling the provided contact number of Ahmet in the need description. Additionally, Ayşe filters food needs based on the geographical location of this town, as she can efficiently transport more food to this specific area. She finds out there is also need in Çeliktepe.

Ayşe then creates an action by linking the available food resource with the town's need and another nearby need. This action is set to recur twice a day for an entire week. Once the action commences, both the resource and the need will no longer be active until one week has elapsed.

### Web and Mobile Testing

We are currently using unit test on the backend, that tests the each functionality one by one, but we do not have other type of testing such as end-to-end, performance, accesibility testing. We have searched trough this testing types and decided on adding some end to end tests. Since performance testing is more about the server quality and availability that needs budget.

On Frontend, we have looked [Selenium](https://www.selenium.dev/documentation/webdriver/) [Appium](https://appium.io/docs/en/2.1/), [Cypress](https://nextjs.org/docs/pages/building-your-application/optimizing/testing). Firstl, we thought using selenium could be a good idea as a result of its beginner-friendly approach. But we have found that Cypress is adviced by the Next.js that we are usgin currently, hence using it makes more sense.

On Mobile we will test our application with user manual testing as always we did in PRs. We also searched different automated test frameworks [Appium](https://appium.io/docs/en/2.1/), [Espresso](https://developer.android.com/training/testing/espresso), [UI Automator](https://developer.android.com/training/testing/other-components/ui-automator), [Calabash](https://github.com/calabash/calabash-android). We decided to use Espresso for automating testing. With using the Espresso we will automate interacting with UI components and making it effective for both functional and UI testing.


## What was planned for the week? How did it go?

| Description                                             | Issue                                                                                                                                                                                                                                                                                                                                                                                           | Assignee      | Due        | Estimated Duration | Actual Duration | Artifacts |
| ---------------------------- | ------------------------------------------ | ------------- | ---------- | ------------------ | --------------- | --------- 
|Frontend - Email verification page design and implementation | [#479](https://github.com/bounswe/bounswe2023group2/issues/479)| Can Bora | 15.11.2023 | 4h | Postponed |
|Frontend - Add missing profile page features | [#491](https://github.com/bounswe/bounswe2023group2/issues/491)| Can Bora | 15.11.2023 | 5h | 6h | [PR #565](https://github.com/bounswe/bounswe2023group2/pull/565)
|Frontend - Add upvote-downvote buttons | [#518](https://github.com/bounswe/bounswe2023group2/issues/518)| Can Bora, Merve | 22.11.2023 | 6h | in-progress | [FE/feature/](https://github.com/bounswe/bounswe2023group2/tree/FE/feature/activity-card)
|Frontend - Add action type activities | [#546](https://github.com/bounswe/bounswe2023group2/issues/546) | Merve | 22.11.2023 | 4h | in-progress
|Backend - Predefined Subtypes Implementation|[#512](https://github.com/bounswe/bounswe2023group2/issues/512)|Burak|22.11.2023| 4h | 3h | [PR #550](https://github.com/bounswe/bounswe2023group2/pull/550)
|Backend - Creation of a Predefined Subtypes JSON File |[#513](https://github.com/bounswe/bounswe2023group2/issues/513)|Burak|22.11.2023| 1h | 1h | [PR #550](https://github.com/bounswe/bounswe2023group2/pull/550)
|Backend - Development of Predefined Subtype Endpoints|[#514](https://github.com/bounswe/bounswe2023group2/issues/514)|Burak|22.11.2023| 3h | 2h | [PR #550](https://github.com/bounswe/bounswe2023group2/pull/550)
|Backend - Event design review|[#533](https://github.com/bounswe/bounswe2023group2/issues/533)|Mehmet Kuzu|18.11.2023| 2h | 2h | 
|Backend - Events CRUD (Finalize)|[#475](https://github.com/bounswe/bounswe2023group2/issues/475)|Mehmet Kuzu|22.11.2023| 4h | 4h | [PR #551](https://github.com/bounswe/bounswe2023group2/pull/551)
|Backend - Unit Tests for Events CRUD|[#537](https://github.com/bounswe/bounswe2023group2/issues/537)|Mehmet Kuzu|20.11.2023| 3h | 3h | [PR #551](https://github.com/bounswe/bounswe2023group2/pull/551)
|Backend - User role implementation | [478](https://github.com/bounswe/bounswe2023group2/issues/478)| Begüm Arslan| 15.11.2023|3h| 6h | [505](https://github.com/bounswe/bounswe2023group2/pull/505)|
|Backend -Frontend Meeting | [536](https://github.com/bounswe/bounswe2023group2/issues/536)| Begüm, Şahin| 16.11.2023|3h| 1h | [Code review](https://github.com/bounswe/bounswe2023group2/pull/550/files)
|Frontend - Create Activity information card for map page  | [476](https://github.com/bounswe/bounswe2023group2/issues/476)| Şahin| 21.11.2023 (delayed)|4h|
|Frontend - Connecting Backend to Map Page | [474](https://github.com/bounswe/bounswe2023group2/issues/474)| Şahin| 21.11.2023 (delayed)|2h|
|Test - Backend - User role endpoints   | [523](https://github.com/bounswe/bounswe2023group2/issues/523)| Begüm| 21.11.2023|2h| 1h | [505](https://github.com/bounswe/bounswe2023group2/pull/505)
|Backend - Feedback system upvote/downvote endpoint  | [480](https://github.com/bounswe/bounswe2023group2/issues/480)| Aziza| 23.11.2023|2h|Ongoing|
|Test - Backend - Feedback endpoints | [524](https://github.com/bounswe/bounswe2023group2/issues/524)| Aziza| 23.11.2023|2h|Ongoing|
|Backend - Admin CRUD and specific endpoint implementations| [519](https://github.com/bounswe/bounswe2023group2/issues/519)| Aziza, Buse| 21.11.2023|5h| Ongoing
|Backend - Filter, Sort endpoints for action-need-resource-event   | [520](https://github.com/bounswe/bounswe2023group2/issues/520)| Begüm| 25.11.2023|7h|2h| only decided on design, passed it to Burak
|Backend Team Meeting #5| [521](https://github.com/bounswe/bounswe2023group2/issues/521)| Begüm, Aziza| 21.11.2023|1h|1h
|Resource - Need final design and coding | [499](https://github.com/bounswe/bounswe2023group2/issues/499)| Burak, Begüm, Merve, Şahin| 15.11.2023|3h|4h| [PR #550](https://github.com/bounswe/bounswe2023group2/pull/550)| 
|Mobile - CRUD Event Pages  | [#539](https://github.com/bounswe/bounswe2023group2/issues/539)| Egecan | 21.11.2023| 6h | 	Postponed | |
|Mobile - Vote Needs and Resources | [#543](https://github.com/bounswe/bounswe2023group2/issues/543)| Egecan | 21.11.2023| 3h | 	Postponed | |
|Mobile - Design a Page for Corresponding Needs and Resources| [#544](https://github.com/bounswe/bounswe2023group2/issues/544)| Egecan | 21.11.2023| 5h | 6h |[#567](https://github.com/bounswe/bounswe2023group2/pull/567)
|Mobile - Adding map pinpoints|[#535](https://github.com/bounswe/bounswe2023group2/issues/535)|Hasan|22.11.2023| 10h | 12h | [#555](https://github.com/bounswe/bounswe2023group2/pull/555)
|Mobile - Refresh Token|[#531](https://github.com/bounswe/bounswe2023group2/issues/531)|Hasan|22.11.2023| 3h |In progress
|Mobile - Delete Need and Resources | [#501](https://github.com/bounswe/bounswe2023group2/issues/501) | Hasan | 13.11.2023 | 3h | In progress
|Mobile - Edit Profile | [#374](https://github.com/bounswe/bounswe2023group2/issues/374) | Cahid | 21.11.2023 | 4h | 3h | [PR](https://github.com/bounswe/bounswe2023group2/pull/556)
|Mobile - Connect backend for Profile Information | [#416](https://github.com/bounswe/bounswe2023group2/issues/416) | Cahid | 21.11.2023 | 12h | 14h | [PR](https://github.com/bounswe/bounswe2023group2/pull/556)
|Mobile - Date of Birth Picker | [#528](https://github.com/bounswe/bounswe2023group2/issues/528) | Cahid | 21.11.2023 | 2h | 5h | [PR](https://github.com/bounswe/bounswe2023group2/pull/559)
|Mobile - Local User Profile | [#532](https://github.com/bounswe/bounswe2023group2/issues/532) | Cahid | 21.11.2023 | 2h | Postponed
|Mobile - App Icon | [#534](https://github.com/bounswe/bounswe2023group2/issues/534) | Cahid | 21.11.2023 | 2h | In Progress
|Mobile - Mobile - Get Subtypes From Backend | [#542](https://github.com/bounswe/bounswe2023group2/issues/542) | Halil | 21.11.2023 | 5h | In Progress |
|Mobile - Recycler view design | [#545](https://github.com/bounswe/bounswe2023group2/issues/545) | Halil | 21.11.2023 | 2h | 5h | [PR #549](https://github.com/bounswe/bounswe2023group2/pull/549)
|Mobile - Sort and filter components and implementations | [#540](https://github.com/bounswe/bounswe2023group2/issues/540) | Halil, Cahid | 21.11.2023 | 6h | In Progress
## Completed tasks that were not planned for the week

| Description | Issue | Assignee | Due  | Actual Duration | Artifacts |
| - | - | - | - | - | - |
|Backend - Resource Date Fields (created_at, last_updated_at)|[#552](https://github.com/bounswe/bounswe2023group2/issues/552)|Burak|22.11.2023| 1h | [PR #553](https://github.com/bounswe/bounswe2023group2/pull/553) |
|Backend - Need Date Fields (created_at, last_updated_at)|[#557](https://github.com/bounswe/bounswe2023group2/issues/557)|Burak|22.11.2023| 0.5h | [PR #558](https://github.com/bounswe/bounswe2023group2/pull/558) |
| Mobile - Unifying All Form Designs | - | Halil | 19.11.2023 | 2h | [PR #545](https://github.com/bounswe/bounswe2023group2/issues/545) |
Backend- Report malicious activities/users to admin | [#569](https://github.com/bounswe/bounswe2023group2/issues/569) |Aziza | 22.11.2023| 4h |[PR #554](https://github.com/bounswe/bounswe2023group2/pull/554)|
Test - Backend - Report endpoints| [#571](https://github.com/bounswe/bounswe2023group2/issues/571) |Aziza | 22.11.2023| 2h |[PR #554](https://github.com/bounswe/bounswe2023group2/pull/554)|
|Backend - User Profile Enhancement - Unplanned| |Mehmet Kuzu|22.11.2023| <1h | [PR #548](https://github.com/bounswe/bounswe2023group2/pull/548)

## Planned vs. Actual
As the backend team, we were slowed down due to design unclarities. So we mostly spend our time on design/review. As the development progresses, new feature requirements for *resource* and *need* also arises, hence we continuously work on them. 
As frontend, we were planning to complete sort/filter functions but we could not be able to finish it due to other exams. We mainly implemented the design parts of the features but did not connect to the server, since we needed to clarify many of the functions together with the backend. One of our team-mates needed to delay his tasks because of the trip decided before. 

Begüm: This week I spend time on design of filter/sort, resource, action... I encounter lots of bugs during implementation so user-role task took more than I anticipated. I could have designed the filter only and figured out how to do it. But I prioritized Action over filter and sort for the next week.
Aziza: Due to some incosistencies in the requirements feedback system was understood as malicious reports by me, which was implemented. However, feedback system implied upvotes/downvotes which would be implemented before M2. Admin endpoints would be implemented upon the finished implementation of user roles.
Mehmet Kuzulugil: To be honest, the week was not so easy for me. Tasks by other courses blocked my work. That was not a problem of workload only, some issues about the project required concentrated thinking and problem solving. This load I mentioned made problem solving difficult for me.
Anyway, I think this type of irregularities are part of our profession and I think the recent work by the team provided the conditions to overcome the inconvenience. The plan as a whole is on its way.
Cahid: I again underestimated the time, so I couldn't finish a task. Also importance of a task was reconsidered and concluded that it would be better if we focus our attention to more important tasks before milestone 2.
Halil: I did research on how to implement the search & filter [#540](https://github.com/bounswe/bounswe2023group2/issues/540) functionality and started UI development, but we will connect the backend this week. While waiting for the necessary endpoints from the backend for [#542](https://github.com/bounswe/bounswe2023group2/issues/542), I unified the style of the all forms in the app in [PR #545](https://github.com/bounswe/bounswe2023group2/issues/545), but after the endpoints were ready, I could not find enough time. I'll do it this week.
Hasan: We prioritized some key points over small bug fixes. So my main focus was creating an well designed map so I put my effort on it.
Egecan: I prepared client-side of [CRUD Event](https://github.com/bounswe/bounswe2023group2/issues/539) & [Upvote/Downvote](https://github.com/bounswe/bounswe2023group2/issues/543), I will implemented connecting backend when backend is ready.

## Plans for the next week

| Description | Issue | Assignee | Due | Estimated Duration |
| ----------- | ----- | -------- | --- | ------------------ |
|Mobile - App Icon | [#534](https://github.com/bounswe/bounswe2023group2/issues/534) | Cahid | 21.11.2023 | 2h
|Frontend - Frontend - Email verification page design and implementation | [#479](https://github.com/bounswe/bounswe2023group2/issues/479) | Merve | 28.11.2023 | 4h
|Frontend - Admin page design and implementation | [#486](https://github.com/bounswe/bounswe2023group2/issues/486) | Merve, Can Bora | 28.11.2023 | 8h
|Frontend - User upvote-downvote feature | [#518](https://github.com/bounswe/bounswe2023group2/issues/518) | Merve | 28.11.2023 | 4h
|Frontend - Milestone 2 Mid-review | [#527](https://github.com/bounswe/bounswe2023group2/issues/527) | Merve, Şahin, Can Bora | 22.11.2023 | 1h
|Frontend - Add ability to add/remove profile picture | [#562](https://github.com/bounswe/bounswe2023group2/issues/562) | Can Bora | 28.11.2023 | 2h
|Frontend - Add user filter to activity table | [#563](https://github.com/bounswe/bounswe2023group2/issues/563) | Merve | 28.11.2023 | 1h
|Frontend - Viewing others' profiles | [#568](https://github.com/bounswe/bounswe2023group2/issues/568) | Can Bora | 28.11.2023 | 4h
| Mobile - Map Activity Redirect | [#570](https://github.com/bounswe/bounswe2023group2/issues/570) | Hasan, Cahid | 28.11.2023 | 2h
| Mobile - Choose Location Through Map View | [#572](https://github.com/bounswe/bounswe2023group2/issues/572) | Hasan, Cahid | 28.11.2023 | 4h
| Mobile - Action Visualization | [#573](https://github.com/bounswe/bounswe2023group2/issues/573) | Hasan, Cahid | 28.11.2023 | 6h
| Mobile - Main Page Design | [#582](https://github.com/bounswe/bounswe2023group2/issues/582) | Halil | 27.11.2023 | 4h | In Progress |
|Mobile - Mobile - Get Subtypes From Backend | [#542](https://github.com/bounswe/bounswe2023group2/issues/542) | Halil | 27.11.2023 | 5h | In Progress |
|Mobile - Sort and filter components and implementations | [#540](https://github.com/bounswe/bounswe2023group2/issues/540) | Halil, Cahid | 27.11.2023 | 6h
|Backend - Action implementation | [#477](https://github.com/bounswe/bounswe2023group2/issues/477) | Begüm | 26.11.2023 | 6h
|Test - Backend - User role endpoints | [#523](https://github.com/bounswe/bounswe2023group2/issues/523) | Begüm | 24.11.2023 | 3h
|Test - Backend - Action implementation | [#517](https://github.com/bounswe/bounswe2023group2/issues/517) | Begüm | 26.11.2023 | 3h
|Backend - Feedback system upvote/downvote endpoint  | [#480](https://github.com/bounswe/bounswe2023group2/issues/480)| Aziza| 26.11.2023|5h|
|Test - Backend - Feedback endpoints | [#524](https://github.com/bounswe/bounswe2023group2/issues/524)| Aziza| 26.11.2023|2h|
|Backend - Admin CRUD and specific endpoint implementations| [#519](https://github.com/bounswe/bounswe2023group2/issues/519)| Aziza, Buse, Mehmet K.| 26.11.2023|5h| 
Backend - Implement the validation check for the coordinates/quantities of resources and needs| [#584](https://github.com/bounswe/bounswe2023group2/issues/584)|Aziza| 24.11.2023| 2h
|Mobile - CRUD Event Pages  | [#539](https://github.com/bounswe/bounswe2023group2/issues/539)| Egecan | 26.11.2023| 4h | 
|Mobile - Vote Needs and Resources | [#543](https://github.com/bounswe/bounswe2023group2/issues/543)| Egecan | 25.11.2023| 3h |
|Mobile - Admin Page Design | [#578](https://github.com/bounswe/bounswe2023group2/issues/578)| Egecan | 27.11.2023| 9h | 
|Mobile - Save User Roles | [#583](https://github.com/bounswe/bounswe2023group2/issues/583)| Egecan | 27.11.2023| 2h | 
|Mobile - Post Need & Resource when Connecting Internet Again | [#586](https://github.com/bounswe/bounswe2023group2/issues/586)| Egecan | 27.11.2023| 4h | 



## Risks
- Our time will be split between the features planned for Milestone 2, the preparation of deliverables and the presentation, and other commitments. There is a risk of not finishing everything on time.
- Miscommunication may cause discrepancies between teams or implementations of different team members.
- Types and subtypes of resources/needs need to be defined well.

## Participants

- Egecan Serbester 
- Begüm Arslan
- Merve Gürbüz
- Cahid Enes Keleş
- Aziza Mankenova
- ~~Buse Tolunay~~
- ~~Mehmet Kuzulugil (Health)~~
- Ramazan Burak Sarıtaş
- Halil İbrahim Gürbüz
- Can Bora Uğur
- ~~Ömer Şahin Albayram~~
- Hasan Bingölbali
