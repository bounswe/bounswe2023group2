# Customer Milestone 2 Deliverables

**Team Name:** Doğal Afet Paylaşım Platformı (DAPP)

**Date:** 01.12.2023

# Deliverables

- Milestone Review (see below)
- Individual Contributions (see below)
- A pre-release of your software

## Milestone Review

**A summary of the project status**:
 - After milestone 1, we dive into the logic of the our project, which has some conflicts in the requirements. We added the new feature ``` recurrent activity```  that we forgot to provide in the first version of the requirements. This feature caused the most of the problems occurred in the action implementation. 
 - We have added dynamic forms for to be able to take needed information for different type of the activities such as food, clothing, transportation and so on. This feature had many bugs since it is hard to control form fields in both backend and frontend.
 - AWS free tier is not capable for to build both nextjs application and backend at the same time due to RAM incapabilities. We upgraded the machine for the demo, we were not able to run the github actions for automated deployment.
 - Our project defines the reliability of a user from its user type, the type of the user stricts the ability of the users to provide a safe information channel for urgent situations. So we define the user types on the backend side and give extra permissions to the verified users.
 - In overall, we have implemented the core functions of the project but the UI/UX experience and error handling should be enhanced before starting final milestone improvements.

**A summary of the customer feedback and reflections**:
- Customer requested user friendly and self-explanotory UI, espacially field labels such as `occur at`, `recurrence`, `quantity`.
- We had positive feedback on the deliverables, since we add the requested feature in previous customer demo.
- The way that we show action in the map should be improved, since we did not provide the information about the type of the transportation chosen or the road.

**Feature Improvements**:


**Describe any changes your team has made since Milestone 1 or planned for the future to improve the development process. Explain how these changes impacted or will impact your process**

### List and status of deliverables

### Provide progress according to requirements: being either not started, in progress, or completed (completed = implemented, tested, documented, and deployed)

**API endpoints (both public or private access)**

* **The API documentation.**
http://3.218.226.215:8000/docs#/
* **The link to the API**
http://3.218.226.215:8000
* **The link to the Frontend**
http://3.218.226.215:3000
* **One or more example API calls for each API function, showcasing how they would be utilized in an expected use- case scenario. (You may skip this if you already have such examples within your API documentation)**
We are already using FastAPI response models for Swagger that shows status codes and response examples. But we are also using a collaborative workspace on [Postman]( https://darp-354343.postman.co/workspace/Team-Workspace~323ad32b-b319-4a0d-8ebe-e9ebe4c0a14d/collection/25451471-54780f30-5464-4b34-ada7-4b9e804588cc?action=share&creator=25451471)

**Generated unit test reports (for backend, frontend, and mobile)**

**The general test plan for the project, which describes your product's testing strategy (e.g., unit testing, integration testing, mock data, etc.)**
Our testing strategy involves the meticulous use of unit tests for each endpoint. Additionally, we leverage Postman to rigorously assess edge cases by employing mock data.

**The status of the features in your software making use of the annotation technology. Your plans for implementing functionalities associated with annotations**
We have initiated the exploration for suitable annotation technologies. Moving forward, our intention is to implement these technologies specifically in map-related features and enhance search functionalities.
**Individual contributions**


### Individual Contributions

#### Member: Merve Gürbüz

 **Responsibilities:** I am responsible for the deployment and AWS-related tasks. I am a member of the frontend team.
 
 **Main contributions:**
I have added a sort and filter for activities. I changed the forms for any input. I added many of the backend service endpoints. I changed the frontend design to make it a responsive website. I added the action activity, which was the most challeging in this milestone. I assigned the issues according to the member's knowledge and project plan. I made the activity card for the need and resource and add the function   upvote/downvote 

- **Code-related significant issues**:
    -  [Frontend - Need add page implementation #487](https://github.com/bounswe/bounswe2023group2/issues/487)
    -  [Frontend - Sort and filter components and implementations #492](https://github.com/bounswe/bounswe2023group2/issues/492)
    -  [Frontend - user upvote-downvote feature #518](https://github.com/bounswe/bounswe2023group2/issues/518)
   - [Frontend - Action implementation #546](https://github.com/bounswe/bounswe2023group2/issues/546)

- **Management-related significant issues**:

- **Pull requests**:
  - **Reviewed:** [#641](https://github.com/bounswe/bounswe2023group2/pull/641),[#629](https://github.com/bounswe/bounswe2023group2/pull/629), [#628](https://github.com/bounswe/bounswe2023group2/pull/628),[#617](https://github.com/bounswe/bounswe2023group2/pull/617), [#550](https://github.com/bounswe/bounswe2023group2/pull/550)
    

   - **Created:**  [#549](https://github.com/bounswe/bounswe2023group2/pull/549), [#460](https://github.com/bounswe/bounswe2023group2/pull/460),[#463](https://github.com/bounswe/bounswe2023group2/pull/463), [#560](https://github.com/bounswe/bounswe2023group2/pull/560), [#589](https://github.com/bounswe/bounswe2023group2/pull/589)
 
- **Additional information**:
Since our frontend team has three members, we do not need regular meetings apart from lab meetings. But we have done milestone reviews together. 
I had to push many features in one branch since I am handling all of them together because of the member count. It is mainly because the checking out between branches is not working well and I do not want to waste time on it because of the deadline.
 
---
 
#### Member: Begüm Arslan

##### Responsibilities: 
I am a member of backend team. 


##### Main contributions:
My primary responsibility was to design and implement the action backend. I introduced a recurrence feature to both resource and need backends, thereby enhancing the complexity of the action design. Additionally, I authored endpoints related to user roles. My contributions to demo scenarios were substantial. Furthermore, I played a significant role in planning milestones with upcoming changes.

- **Code-related significant issues**:
  - [Backend - User role implementation](https://github.com/bounswe/bounswe2023group2/issues/478) 
  - [Backend - Action implementation ](https://github.com/bounswe/bounswe2023group2/issues/477)
  - [Backend - Recurring design and implementation](https://github.com/bounswe/bounswe2023group2/issues/587)
  - [Test -Backend - Action CRUD endpoints ](https://github.com/bounswe/bounswe2023group2/issues/517)
  - [Test - Backend - User role endpoints](https://github.com/bounswe/bounswe2023group2/issues/523)

- **Management-related significant issues**:
  - [Backend Team Meeting #4](https://github.com/bounswe/bounswe2023group2/issues/481)
  - [Backend - Requirements and design documents study](https://github.com/bounswe/bounswe2023group2/issues/483)
  - [Deciding Activity attributes for activity information card on map page ](https://github.com/bounswe/bounswe2023group2/issues/488)
  - [Backend - Milestone 2 Plan Review ](https://github.com/bounswe/bounswe2023group2/issues/489)
  - [Resource / Need final design and coding](https://github.com/bounswe/bounswe2023group2/issues/499)
  - [Backend Team Meeting #5](https://github.com/bounswe/bounswe2023group2/issues/521)
  - [Backend -Frontend Meeting](https://github.com/bounswe/bounswe2023group2/issues/536)
  
- **Pull requests**:
  - **Reviewed:**
   [#550](https://github.com/bounswe/bounswe2023group2/pull/550), [#612](https://github.com/bounswe/bounswe2023group2/pull/612),[#593](https://github.com/bounswe/bounswe2023group2/pull/593), [#602](https://github.com/bounswe/bounswe2023group2/pull/602), [#630](https://github.com/bounswe/bounswe2023group2/pull/630), [#594](https://github.com/bounswe/bounswe2023group2/pull/594), [#648](https://github.com/bounswe/bounswe2023group2/pull/648), [#652](https://github.com/bounswe/bounswe2023group2/pull/652) 
   I only asked for enhancements related to resource fields etc. And we mainly decided to add them.

   - **Created:**
      [#577](https://github.com/bounswe/bounswe2023group2/pull/577), [#505](https://github.com/bounswe/bounswe2023group2/pull/505), [#615](https://github.com/bounswe/bounswe2023group2/pull/615), [#576](https://github.com/bounswe/bounswe2023group2/pull/576), [#645](https://github.com/bounswe/bounswe2023group2/pull/645),[#502](https://github.com/bounswe/bounswe2023group2/pull/502),[#647](https://github.com/bounswe/bounswe2023group2/pull/647) ,[#641](https://github.com/bounswe/bounswe2023group2/pull/641),[#644](https://github.com/bounswe/bounswe2023group2/pull/644) ,[#613](https://github.com/bounswe/bounswe2023group2/pull/613) , [#595](https://github.com/bounswe/bounswe2023group2/pull/595), [#561](https://github.com/bounswe/bounswe2023group2/pull/561) (This one was supposed to be merged before ms1, forgot to push changes(test))

- **Additional information**:
  

---

#### Member: Halil İbrahim Gürbüz

##### Responsibilities: 
Member of mobile team. Completing the tasks given to me and reviewing my teammates' pull requests was my responsibilities. I also took responsibility for the UI/UX design of the mobile application.



##### Main contributions:
My main contribution to the project was to design dynamic forms, main page tabbed design, sort and filter functionality and UI/UX design.


- **Code-related significant issues**:
  - [Mobile - Edit Need Form](https://github.com/bounswe/bounswe2023group2/issues/496)
  - [Mobile - Color Scheme](https://github.com/bounswe/bounswe2023group2/issues/498)
  - [Mobile - Sort and filter components and implementations](https://github.com/bounswe/bounswe2023group2/issues/540)
  - [Mobile - Get Subtypes From Backend](https://github.com/bounswe/bounswe2023group2/issues/542)
  - [Mobile - Recycler view design](https://github.com/bounswe/bounswe2023group2/issues/545)
  - [Mobile - UI improvements](https://github.com/bounswe/bounswe2023group2/issues/601)
  - [Mobile - Main Page Design](https://github.com/bounswe/bounswe2023group2/issues/582)


- **Management-related significant issues**:

  
- **Pull requests**:
  - **Reviewed:**
   [#507](https://github.com/bounswe/bounswe2023group2/pull/507), [#510](https://github.com/bounswe/bounswe2023group2/pull/510), [#511](https://github.com/bounswe/bounswe2023group2/pull/511), [#547](https://github.com/bounswe/bounswe2023group2/pull/547), [#559](https://github.com/bounswe/bounswe2023group2/pull/559), [#567](https://github.com/bounswe/bounswe2023group2/pull/567), [#603](https://github.com/bounswe/bounswe2023group2/pull/603), [#618](https://github.com/bounswe/bounswe2023group2/pull/618), [#633](https://github.com/bounswe/bounswe2023group2/pull/633), [#654](https://github.com/bounswe/bounswe2023group2/pull/654)
   There were no conflicts in the PRs I reviewed. There was a case that I wanted to be fixed in [PR511](https://github.com/bounswe/bounswe2023group2/pull/511)

   - **Created:**
      [#504](https://github.com/bounswe/bounswe2023group2/pull/504), [#508](https://github.com/bounswe/bounswe2023group2/pull/508),  [#549](https://github.com/bounswe/bounswe2023group2/pull/549),  [#598](https://github.com/bounswe/bounswe2023group2/pull/598),  [#604](https://github.com/bounswe/bounswe2023group2/pull/604),  [#605](https://github.com/bounswe/bounswe2023group2/pull/605),  [#611](https://github.com/bounswe/bounswe2023group2/pull/611),  [#614](https://github.com/bounswe/bounswe2023group2/pull/614),  [#637](https://github.com/bounswe/bounswe2023group2/pull/637),  [#638](https://github.com/bounswe/bounswe2023group2/pull/638)

- **Additional information**:

After the feedback we received after Milestone 1, I was interested in user interface and experience design, as well as the work I would do. Even though I have knowledge on this subject, I did research from time to time.
  

---
#### Member: Mehmet Kuzulugil
##### Responsibilities
Member of Backend subteam.
Coding according to our internal division of work, reviewing fellow members' work, reconsidering design issues related to my parts in coding and also contributing to other members' reviews of design issues.

##### Main contributions:

- Code-related:
  - Events CRUD 
  - Contributed to: The requirements about data manipulation end points for users with special roles. That included review of user profile codes to implement "admin users can see and change other users profile data" requirement. 
  - Coding done/contributed/researched/tried:
       - [Backend - Improvements on CRUD for User Profile](https://github.com/bounswe/bounswe2023group2/issues/399) After Milestone 1, taking the criticisms into account, made some fine tuning.
       - [Backend - Event CRUD endpoints](https://github.com/bounswe/bounswe2023group2/issues/475)
       - [Backend - Event type enumeration and extra note fields adding](https://github.com/bounswe/bounswe2023group2/issues/625)
    - Contributed 
        - [Backend - Admin CRUD and specific endpoint implementations](https://github.com/bounswe/bounswe2023group2/issues/519)
    - Review of design: 
        - [Elaborating on Event](https://github.com/bounswe/bounswe2023group2/issues/533)
    - Research
      Another time, another place...
    - At least tried
        - [Backend - User profile picture implementation](https://github.com/bounswe/bounswe2023group2/issues/490) The coding (so implementation) prepared but we bumped into some issues of infrastructure relating to our service provider. Decided to postpone the **finalization** for the sake of practical implementation.

- Management related:
  - XXXX
  - Issues related:
    - [Review of Project Plan](https://github.com/bounswe/bounswe2023group2/issues/482)
    - [Backend - Requirements and design documents study](https://github.com/bounswe/bounswe2023group2/issues/483)
    - [Backend - Milestone 2 Plan Review](https://github.com/bounswe/bounswe2023group2/issues/489)
    - 
- Pull requests
  - Created by:
    - [357 - MİSAL](https://github.com/bounswe/bounswe2023group2/pull/357) Proposed Authorization. Closed (not merged) as decision for JWT (stateless) aproach.
  - Reviewed by:
    - [311 - MİSAL](https://github.com/bounswe/bounswe2023group2/pull/311) - [314](https://github.com/bounswe/bounswe2023group2/pull/314) - [363](https://github.com/bounswe/bounswe2023group2/pull/363) - [378](https://github.com/bounswe/bounswe2023group2/pull/378) - [392](https://github.com/bounswe/bounswe2023group2/pull/392) - [431](https://github.com/bounswe/bounswe2023group2/pull/431) - [432](https://github.com/bounswe/bounswe2023group2/pull/432) - [444](https://github.com/bounswe/bounswe2023group2/pull/444)
  - Merged by:
    - [470 - MİSAL](https://github.com/bounswe/bounswe2023group2/pull/470) - [466](https://github.com/bounswe/bounswe2023group2/pull/466) - [450](https://github.com/bounswe/bounswe2023group2/pull/450) - [440](https://github.com/bounswe/bounswe2023group2/pull/440) - [428](https://github.com/bounswe/bounswe2023group2/pull/428) - [392](https://github.com/bounswe/bounswe2023group2/pull/392) - [378](https://github.com/bounswe/bounswe2023group2/pull/378) - [363](https://github.com/bounswe/bounswe2023group2/pull/363)

##### Other information:

Check [individual contribution page](https://github.com/bounswe/bounswe2023group2/wiki/Individual-contribution-by-Mehmet-Kuzulugil-to-DaRP) for weekly effort reports.

---

#### Member: Hasan Bingölbali

##### Responsibilities: 
My main responsability was to create visually appealing map and make it functional. I also worked hard on creating satifastory user experience.


##### Main contributions:



- **Code-related significant issues**:
  - [Mobile - Adding map pinpoints](https://github.com/bounswe/bounswe2023group2/issues/535)
  - [Mobile - Map visualization for need, action, event and resources](https://github.com/bounswe/bounswe2023group2/issues/541)
  - [Mobile - Map Activity Redirect](https://github.com/bounswe/bounswe2023group2/pull/603)
  - [Mobile - Choose Location Through Map View ](https://github.com/bounswe/bounswe2023group2/issues/572)
  - [Mobile - Action Visualization ](https://github.com/bounswe/bounswe2023group2/issues/573)
  - [Mobile - Finalize Map View ](https://github.com/bounswe/bounswe2023group2/issues/574)
  - [Mobile - Delete Need and Resources](https://github.com/bounswe/bounswe2023group2/issues/501)


- **Management-related significant issues**:


  
- **Pull requests**:
  - **Reviewed:**
   [#556](https://github.com/bounswe/bounswe2023group2/pull/556),  [#603](https://github.com/bounswe/bounswe2023group2/pull/603), [#604](https://github.com/bounswe/bounswe2023group2/pull/604), [#605](https://github.com/bounswe/bounswe2023group2/pull/605), [#634](https://github.com/bounswe/bounswe2023group2/pull/634)

   - **Created:**
         [#509](https://github.com/bounswe/bounswe2023group2/pull/509),  [#555](https://github.com/bounswe/bounswe2023group2/pull/555), [#618](https://github.com/bounswe/bounswe2023group2/pull/618), [#622](https://github.com/bounswe/bounswe2023group2/pull/622), [#636](https://github.com/bounswe/bounswe2023group2/pull/636), [#603](https://github.com/bounswe/bounswe2023group2/pull/603)

- **Additional information**:
    I want to thank mobile team for working so hard. As a team we worked together, and end result was satisfactory.
--- 
#### Member: Can Bora Uğur

##### Responsibilities:
I am a member of the front-end team. My responsibilities before Customer Milestone 2, according to the project plan, were originally to work on email verification, upvote-downvotes to measure reliability, and the report functionality. I also ended up expanding the profile page from Milestone 1, and I took the responsibility of admin page implementation instead of email verification and upvote-downvotes because it was much closer to the functionality I was already implementing.

##### Main contributions:


- **Code-related significant issues**:
    - [Frontend - Admin page design and implementation](https://github.com/bounswe/bounswe2023group2/issues/486)
    - [Frontend - Add missing features for profile (split later)](https://github.com/bounswe/bounswe2023group2/issues/491)
    - [Frontend - Ability to add and remove professions, skills, languages, social media](https://github.com/bounswe/bounswe2023group2/issues/564)
    - [Frontend - Viewing others' profiles](https://github.com/bounswe/bounswe2023group2/issues/568)
    - [Frontend - Main page bugfix](https://github.com/bounswe/bounswe2023group2/issues/616)
- **Management-related significant issues**:
    - [Frontend - Milestone 2 plan review](https://github.com/bounswe/bounswe2023group2/issues/485)
    - [Frontend - Milestone 2 Mid-review](https://github.com/bounswe/bounswe2023group2/issues/527)
  
- **Pull requests**:
  - **Created:**
    - [Frontend - Add social media/skill/language/professions to user profile](https://github.com/bounswe/bounswe2023group2/pull/565)
    - [Frontend - Main page bugfix](https://github.com/bounswe/bounswe2023group2/pull/617)
    - [Frontend - Admin panel](https://github.com/bounswe/bounswe2023group2/pull/628)
    - [Frontend - Viewing other users' profiles](https://github.com/bounswe/bounswe2023group2/pull/629)
  - **Reviewed:**
    - [Backend - Admin-related endpoints](https://github.com/bounswe/bounswe2023group2/pull/621)
  

---

## The Software
