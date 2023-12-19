# Project Development Weekly Progress Report

**Team Name:** Disaster Response Platform (DaPP)

**Date:** 19.12.2023

## Progress Summary

On the Backend, we have worked on annotations. Implemented file upload and email verification feature, fixed a couple of apis. we have progresses on action and recurrence, its development takes time since we need to do some research to implement.

On the Frontend, we have:
- translated the site to Turkish and English.
- added a reset password functionality.
- updated and refined the map page.
- re-implemented parts of the activity table to have different columns for different activity types.
- added an event table.
- following feedback that said the currently selected activity type wasn't clear, added a visual indicator for the selected type.

On Mobile: we filtered need and resources by location succesfully and we can report malicious actions to the admin.
We also work on email verification, CRUD Emergency pages, Save Action without connection and posted them when connection established.

### Objectives for the following week

We'll decide on a set of use cases we'll want to show during the presentation, and then prepare for the presentation. We also have deliverables to prepare. Other than that:

We plan to work on the following topics essentially as backend:
* Annotation
* Search
* Recurrent activities
* Reset password
* Emergency
* Bug fixes, improvements. 

Frontend will mainly be focused on emergency, annotations, recurrence and finishing incomplete pages and polishing them.

## What was planned for the week? How did it go?
| Description | Issue | Assignee | Due | Estimated Duration | Actual Duration | Artifacts |
| ----------- | ----- | -------- | --- | ------------------ | --------------- | --------- |
| Frontend - Language support | [#724](https://github.com/bounswe/bounswe2023group2/issues/724) | Can Bora | 19.12.2023 | 8h | 15h | [PR #747](https://github.com/bounswe/bounswe2023group2/pull/747)
| Frontend - View events on main page | [#684](https://github.com/bounswe/bounswe2023group2/issues/684) | Can Bora | 19.12.2023 | 6h | 4h | [PR #765](https://github.com/bounswe/bounswe2023group2/pull/765)
| Frontend - Add emergency form | [#685](https://github.com/bounswe/bounswe2023group2/issues/685) | Can Bora | 19.12.2023 | 3h | Pending
| Frontend - Add emergency form to main page | [#687](https://github.com/bounswe/bounswe2023group2/issues/687) | Can Bora | 19.12.2023 | 6h | Pending
| Frontend - Highlighting currently selected activity table | [#688](https://github.com/bounswe/bounswe2023group2/issues/688) | Can Bora | 19.12.2023 | 2h | 30m | [PR #765](https://github.com/bounswe/bounswe2023group2/pull/765)
| Frontend - Add ability to add/remove profile picture | [#562](https://github.com/bounswe/bounswe2023group2/issues/562) | Can Bora | 19.12.2023 | 2h | Pending
| Backend - Annotation Server research and installation |  [#736](https://github.com/bounswe/bounswe2023group2/issues/736) | Mehmet Kuzulugil | 17.12.2023 | 2h | 4h | [Document](https://github.com/bounswe/bounswe2023group2/wiki/Annotations-%E2%80%90-Final-design)
| Backend - Annotation Server First steps API use | [#737](https://github.com/bounswe/bounswe2023group2/issues/737) | Mehmet Kuzulugil | 17.12.2023 | 4h | 6h | Draft ready| 
| Backend - Add user profile URL to optional info API | [#738](https://github.com/bounswe/bounswe2023group2/issues/738) | Mehmet Kuzulugil | 17.14.2023 | 2h | 1h | [PR759](https://github.com/bounswe/bounswe2023group2/pull/759)
| Mobile - Profile Picture | [#732](https://github.com/bounswe/bounswe2023group2/issues/732) | Cahid | 19.12.2023 | 4h | Ongoing
| Mobile - Special Activity of Credible User | [#731](https://github.com/bounswe/bounswe2023group2/issues/731) | Cahid | 19.12.2023 | 2h | Ongoing
| Mobile - Email verification page design and implementation | [#538](https://github.com/bounswe/bounswe2023group2/issues/538) | Halil | 18.12.2023 | 2h | 4h + Ongoing | [PR778](https://github.com/bounswe/bounswe2023group2/pull/778/) 
| Mobile - Sort / Filter by Location and Date | [#690](https://github.com/bounswe/bounswe2023group2/issues/690) | Halil | 16.12.2023 | 3h | 4h | [PR754](https://github.com/bounswe/bounswe2023group2/pull/754)
| Mobile - CRUD Emergency Page | [#733](https://github.com/bounswe/bounswe2023group2/issues/733) | Halil | 19.12.2023 | 6h | 3h + Ongoing | [BR](https://github.com/bounswe/bounswe2023group2/tree/MO/feature/emergency)
| Mobile - Report malicious activities/users to admin | [#735](https://github.com/bounswe/bounswe2023group2/issues/735) | ~~Egecan~~ Hasan | 19.12.2023 | 8h | Inprogress(5h+) | [PR Report Activities](https://github.com/bounswe/bounswe2023group2/pull/762) |(https://github.com/bounswe/bounswe2023group2/issues/735#issuecomment-1863200643)
|Mobile - Post Need & Resource when Connecting Internet Again | [#586](https://github.com/bounswe/bounswe2023group2/issues/586)| Egecan | 19.12.2023| 6h | 4h+ In Progress | [Branch](https://github.com/bounswe/bounswe2023group2/tree/MO/feature/save-added-actions) |
|Backend - Build semantic search server.  | [#720](https://github.com/bounswe/bounswe2023group2/issues/720) [#750](https://github.com/bounswe/bounswe2023group2/issues/750)| begüm | 19.12.2023| 10h | 10h| [Commit ID](bab792f)
|CP - Create Test Traceability Matrix | [#723](https://github.com/bounswe/bounswe2023group2/issues/723)| begüm | 19.12.2023| 3h | 3h| [Wiki Page](https://github.com/bounswe/bounswe2023group2/wiki/Test-Traceability-Matrix)|
|Backend - Return user type with log in | [#703](https://github.com/bounswe/bounswe2023group2/issues/703)| begüm | 15.12.2023| 2h | 3h | [PR](https://github.com/bounswe/bounswe2023group2/pull/706)
|Backend - All Users Endpoint  | [#726](https://github.com/bounswe/bounswe2023group2/issues/726)| begüm | 15.12.2023| 1h | - |Cancelled
|Backend - Refactor user proficiency | [#718](https://github.com/bounswe/bounswe2023group2/issues/718)| begüm | 19.12.2023| 2h | 3h | [PR](https://github.com/bounswe/bounswe2023group2/pull/748)
|Backend - Action Improvement | [#671](https://github.com/bounswe/bounswe2023group2/issues/671)| begüm | 19.12.2023| 3h | In progress
| Frontend- Implementing area select for filtering | [#682](https://github.com/bounswe/bounswe2023group2/issues/682) | Şahin | 15.12.2023 | 5h+2h | 2h | In Progress
| Frontend - Adjusting Filter menu according to backend | [#681](https://github.com/bounswe/bounswe2023group2/issues/681) | Şahin | 15.12.2023 | 4h | 3h | In Progress
|Frontend - Adding Upvote Downvote buttons and system to information card|[#719](https://github.com/bounswe/bounswe2023group2/issues/719)|Şahin|15.12.2023|1h|1h| [#769](https://github.com/bounswe/bounswe2023group2/pull/769)
|Frontend - Map Page user tests|[#729](https://github.com/bounswe/bounswe2023group2/issues/729)|Şahin,Buse|15.12.2023|2h|1h| [Explained in comments](https://github.com/bounswe/bounswe2023group2/issues/729)
|Mobile -Action implementation | [#669](https://github.com/bounswe/bounswe2023group2/issues/669) | ~~Hasan~~ Egecan | 19.12.2023 | 10h | Changing assignee | [Explained](https://github.com/bounswe/bounswe2023group2/issues/735#issuecomment-1863200643) |
| Backend - Address Translation Service | [#772](https://github.com/bounswe/bounswe2023group2/issues/722)| Burak | 19.12.2023 | 4h | 5h | [PR #763](https://github.com/bounswe/bounswe2023group2/pull/753)
| Backend - Activities: Admin Prioritization on Sort & Filter | [#727](https://github.com/bounswe/bounswe2023group2/issues/727) | Burak | 19.12.2023 | 1h | 2h | In Progress
| Backend - Forgot Password | [#740](https://github.com/bounswe/bounswe2023group2/issues/740) | Burak | 26.12.2023 | 3h | 6h | [PR #757](https://github.com/bounswe/bounswe2023group2/pull/757)
| Backend - Action logic revisit | [#695](https://github.com/bounswe/bounswe2023group2/issues/695) | Merve | 19.12.2023 | 5h | 8h  | [branch BE/feature.action-revisit](https://github.com/bounswe/bounswe2023group2/tree/BE/feature/action-revisit)
| Frontend - Email verification page design and implementation | [#479](https://github.com/bounswe/bounswe2023group2/issues/479) | Merve | 19.12.2023 | 5h |  5h |[#766](https://github.com/bounswe/bounswe2023group2/pull/766)
| Test - Backend - Admin endpoints	| [#686](https://github.com/bounswe/bounswe2023group2/issues/686)|Aziza| 19.12.2023| 4h | 2h +Ongoing
Backend - Emergency endpoints| [#691](https://github.com/bounswe/bounswe2023group2/issues/691)| Aziza|19.12.2023|6h| 3h + Ongoing|

## Completed tasks that were not planned for the week
| Description | Issue | Assignee | Due | Actual Duration | Artifacts |
| - | - | - | - | - | - |
| Frontend - Dynamic columns for Activity Table | [#763](https://github.com/bounswe/bounswe2023group2/issues/763) | Can Bora | 19.12.2023 | 4h | [PR #765](https://github.com/bounswe/bounswe2023group2/pull/765)
| Frontend - Event form improvements | [#764](https://github.com/bounswe/bounswe2023group2/issues/764) | Can Bora | 19.12.2023 | 10m | [PR #765](https://github.com/bounswe/bounswe2023group2/pull/765)
|Backend - Feedback bugfix | - | Aziza |  19.12.2023 | 20m | [PR #771](https://github.com/bounswe/bounswe2023group2/pull/771)
|Mobile - Fix Bugs | [#744](https://github.com/bounswe/bounswe2023group2/issues/744) | Egecan |  15.12.2023 | 1h | [PR #745](https://github.com/bounswe/bounswe2023group2/pull/745)
|Mobile - Open Map as Pop-Up | [#746](https://github.com/bounswe/bounswe2023group2/issues/746) | Egecan |  16.12.2023 | 4h | [Commit on PR #754](https://github.com/bounswe/bounswe2023group2/commit/473924be87c75c6c66ae614e1de8bf8db5fb005c)
|Sign-in sign-up refactor | | Merve |  16.12.2023 | 5h | [PR #777](https://github.com/bounswe/bounswe2023group2/pull/766)
|Deployment - check  | | Merve |  16.12.2023 | 1h | refreshed secrets 
| Backend - Redirect Forgot Password User to HTML Reset Page | [#758](https://github.com/bounswe/bounswe2023group2/issues/758) | Burak | 19.12.2023 | 4h | [PR #757](https://github.com/bounswe/bounswe2023group2/pull/757), [PR #767](https://github.com/bounswe/bounswe2023group2/pull/767) |


## Planned vs. Actual
Cahid: I wasn't able to do any job this week, because of other courses.
Merve: I have worked a lot on the action system, it took several times more than expected :')
Burak: Tasks I got this week were a lot more challenging and time consuming than I expected, especially the forgot password system.

As a team, we were expecting to finish backend enhancement by the end of this week, but we have found that, we neglected emergency, so we decided to implement the emergency immediately.

## Plans for the next week
| Description | Issue | Assignee | Due | Estimated Duration |
| ----------- | ----- | -------- | --- | ------------------ |
| Mobile - Profile Picture | [#732](https://github.com/bounswe/bounswe2023group2/issues/732) | Cahid | 22.12.2023 | 4h | Ongoing
| Mobile - Special Activity of Credible User | [#731](https://github.com/bounswe/bounswe2023group2/issues/731) | Cahid | 22.12.2023 | 2h | Ongoing
| Mobile - Password Reset | [#773](https://github.com/bounswe/bounswe2023group2/issues/773) | Cahid | 26.12.2023 | 3h |
| Mobile - Delete Account | [#774](https://github.com/bounswe/bounswe2023group2/issues/774) | Cahid | 26.12.2023 | 1h |
| Mobile - Upload Document | [#777](https://github.com/bounswe/bounswe2023group2/issues/777) | Cahid | 26.12.2023 | 3h |
| Mobile - Upvote Downvote Design Update | [#779](https://github.com/bounswe/bounswe2023group2/issues/779) | Halil | 23.12.2023 | 2h |
| Updating the Requirements | [#780](https://github.com/bounswe/bounswe2023group2/issues/780) | Halil | 23.12.2023 | 3h |
| Mobile - Email verification page design and implementation | [#538](https://github.com/bounswe/bounswe2023group2/issues/538) | Halil | 22.12.2023 | 1h |
| Mobile - CRUD Emergency Page | [#733](https://github.com/bounswe/bounswe2023group2/issues/733) | Halil | 24.12.2023 | 3h |
| Mobile - Sort and filter for Action/Emergency/Event  | [#799](https://github.com/bounswe/bounswe2023group2/issues/799) | Halil | 24.12.2023 | 2h |
| Mobile - Revise the App | [#797](https://github.com/bounswe/bounswe2023group2/issues/797) | Halil,Egecan | 24.12.2023 | 5h |
|Mobile - Post Need & Resource when Connecting Internet Again | [#586](https://github.com/bounswe/bounswe2023group2/issues/586)| Egecan | 24.12.2023| 3h | 
|Mobile -Action implementation | [#669](https://github.com/bounswe/bounswe2023group2/issues/669) | Egecan | 25.12.2023 | 10h|
|Mobile - Annotation Implementation  | [#800](https://github.com/bounswe/bounswe2023group2/issues/800) | Egecan | 25.12.2023 | 8h|
| Mobile - Report malicious users to admin | [#735](https://github.com/bounswe/bounswe2023group2/issues/735) | Hasan | 19.12.2023 | 3h |
| Mobile - Change Login Backend Implementation | [#807](https://github.com/bounswe/bounswe2023group2/issues/807) | Hasan | 19.12.2023 | 1h |
| Mobile - Search Implementation | [#802](https://github.com/bounswe/bounswe2023group2/issues/802) | Hasan | 19.12.2023 | 6h |
| Frontend - Finalize admin panel | [#775](https://github.com/bounswe/bounswe2023group2/issues/775) | Can Bora | 25.12.2023 | 10h
| Frontend - Finalize profile page | [#776](https://github.com/bounswe/bounswe2023group2/issues/776) | Can Bora | 25.12.2023 | 6h
| Frontend - Add emergency form | [#685](https://github.com/bounswe/bounswe2023group2/issues/685) | Can Bora | 19.12.2023 | 2h |
| Frontend - Finalize ActivityTable | [#781](https://github.com/bounswe/bounswe2023group2/issues/781) | Can Bora | 19.12.2023 | 10h |
| Backend - AS Release for testing |[#782](https://github.com/bounswe/bounswe2023group2/issues/782)  | Mehmet K. | 21.12.2023 | 4h |
| Backend - AS Programmer's Documentation | [#737](https://github.com/bounswe/bounswe2023group2/issues/737) | Mehmet K. | 22.12.2023 | 4h |
| Deployment - AS configuration for docker |  [#784](https://github.com/bounswe/bounswe2023group2/issues/784)| Begüm, Merve, Mehmet K. | 22.12.2023 | 4h |
|Backend - Design and write endpoints for each search element. |  [#752](https://github.com/bounswe/bounswe2023group2/issues/752)| Begüm | 22.12.2023 | 6h |
|Backend - Download filtered resources, needs, events |  [#721](https://github.com/bounswe/bounswe2023group2/issues/721)| Begüm | 23.12.2023 | 3h |
|Backend - Test - Search |  [#786](https://github.com/bounswe/bounswe2023group2/issues/786)| Begüm | 23.12.2023 | 3h |
|Backend- User account deletion  |  [#798](https://github.com/bounswe/bounswe2023group2/issues/798)| Begüm | 22.12.2023 | 2h |
|FD - Make the video for the application  |  [#796](https://github.com/bounswe/bounswe2023group2/issues/796)| Begüm | 29.12.2023 | 8h |
| Test - Backend - Admin endpoints	| [#686](https://github.com/bounswe/bounswe2023group2/issues/686)|Aziza| 23.12.2023| 2h | 
|Backend - Emergency endpoints| [#691](https://github.com/bounswe/bounswe2023group2/issues/691)| Aziza|23.12.2023|4h|
|Test - Backend - Emergency endpoints| [#789](https://github.com/bounswe/bounswe2023group2/issues/789)| Aziza|23.12.2023|5h|
|Backend - Feedback modification |  [#791](https://github.com/bounswe/bounswe2023group2/issues/791)| Aziza|23.12.2023|5h|
|Backend - Report create endpoint fix | [#791](https://github.com/bounswe/bounswe2023group2/issues/795)| Aziza|23.12.2023|2h|
| Frontend- Implementing area select for filtering | [#682](https://github.com/bounswe/bounswe2023group2/issues/682) | Şahin | 21.12.2023 | 5h+2h | 2h | In Progress
| Frontend - Adjusting Filter menu according to backend | [#681](https://github.com/bounswe/bounswe2023group2/issues/681)| Şahin | 21.12.2023 | 1h |
|Frontend - Creating custom markers | [#770](https://github.com/bounswe/bounswe2023group2/issues/770)| Şahin | 21.12.2023 | 1h
|Frontend - Add Action element on frontend | [#526](https://github.com/bounswe/bounswe2023group2/issues/526)| Merve | 21.12.2023 | 6h
|Backend - Action logic revisit | [#695](https://github.com/bounswe/bounswe2023group2/issues/695)| Merve | 24.12.2023 | 6h
| Deployment - Annotation Service in a seperate container| [#784](https://github.com/bounswe/bounswe2023group2/issues/784)| Merve | 22.12.2023 | 2h 
| Deployment - Integrate server to the deployment| [#785](https://github.com/bounswe/bounswe2023group2/issues/785)| Merve | 22.12.2023 | 2h  
| Frontend - add report feature to the activities| [#787](https://github.com/bounswe/bounswe2023group2/issues/787)| Merve | 22.12.2023 | 3h  
| Frontend - geocode converts| [#788](https://github.com/bounswe/bounswe2023group2/issues/788)| Merve | 22.12.2023 | 2h  
| Frontend - Activity edit endpoints | [#790](https://github.com/bounswe/bounswe2023group2/issues/790)| Merve | 25.12.2023 | 2h  
| Frontend - Add verified icon to the verified activities| [#792](https://github.com/bounswe/bounswe2023group2/issues/792)| Merve | 22.12.2023 | 1h  
|Frontend - Feedback activity bugfix| [#793](https://github.com/bounswe/bounswe2023group2/issues/793)| Merve | 22.12.2023 | 1h  
| Backend - Activities: Admin Prioritization on Sort & Filter | [#727](https://github.com/bounswe/bounswe2023group2/issues/727) | Burak | 21.12.2023 | 2h (Continuing from last week)| 2h 
| Backend - User Role Authentication on Email Verification | [#801](https://github.com/bounswe/bounswe2023group2/issues/801) | Burak | 22.12.2023 | 2h 
| Backend - Test: Email Verification Endpoints | [#803](https://github.com/bounswe/bounswe2023group2/issues/803)| Burak |23.12.2023 | 2h 
| Backend - Test: Address Translation Endpoints | [#804](https://github.com/bounswe/bounswe2023group2/issues/804)| Burak |23.12.2023 | 3h 
| Backend - Test: Forgot/Reset Password Endpoints | [#806](https://github.com/bounswe/bounswe2023group2/issues/806)| Burak |23.12.2023 | 2h 

## Next week - Final milestone deliverables 
| Description | Issue | Assignee | Due | Estimated Duration |
| ----------- | ----- | -------- | --- | ------------------ |
| User Interface / User Experience - web #809 | [#809](https://github.com/bounswe/bounswe2023group2/issues/809) | Merve | 29.12.2023 | 4h | Ongoing
| Software #810 | [#810](https://github.com/bounswe/bounswe2023group2/issues/810) | Merve | 29.12.2023 | 2h | Ongoing
| Research #811| [#811](https://github.com/bounswe/bounswe2023group2/issues/811) | Merve | 29.12.2023 | 3h |
| Project plan #812 | [#812](https://github.com/bounswe/bounswe2023group2/issues/812) | Merve | 29.12.2023 | 1h |
| Provide progress according to requirements #813| [#813](https://github.com/bounswe/bounswe2023group2/issues/813) | Halil | 29.12.2023 | 3h |
| Deliverables - The API documentation Link to the API #814 | [#814](https://github.com/bounswe/bounswe2023group2/issues/814) | Begum | 29.12.2023 | 2h |
| Management #816 | [#816](https://github.com/bounswe/bounswe2023group2/issues/816) | Egecan | 29.12.2023 | 3h |
| Final release notes #817 | [#817](https://github.com/bounswe/bounswe2023group2/issues/817) | Burak | 29.12.2023 | 1h |
| Executive summary #818 | [#818](https://github.com/bounswe/bounswe2023group2/issues/818) | Burak | 29.12.2023 | 3h |
| Maximum of 5 minutes video of your system being used with a good scenario. #820| [#820](https://github.com/bounswe/bounswe2023group2/issues/820) | Begum | 29.12.2023 | 3h |
| User Manual: Instructions for how to use the system. #821 | [#821](https://github.com/bounswe/bounswe2023group2/issues/821) | Mehmet | 29.12.2023 | 3h |
| Deliverable - Software Requirements Specification (SRS) #822 | [#822](https://github.com/bounswe/bounswe2023group2/issues/822) | Mehmet | 29.12.2023 | 3h |
| Deliverable - System Manual #823 | [#823](https://github.com/bounswe/bounswe2023group2/issues/823) | Mehmet | 29.12.2023 | 3h |

## Risks

- We need time to prepare the presentation, to deal with any issues/bugs that may arise, and to implement some missing features. We'll need to manage time well.
- In the same vein, leaving bugfixes to the last minute may result in chaos.

## Participants

- Egecan Serbester
- ~~Begüm Arslan~~
- Merve Gürbüz
- Cahid Enes Keleş
- Aziza Mankenova
- ~~Mehmet Kuzulugil~~
- Ramazan Burak Sarıtaş
- Halil İbrahim Gürbüz
- Can Bora Uğur
- Ömer Şahin Albayram
- Hasan Bingölbali
- ~~Buse Tolunay~~
