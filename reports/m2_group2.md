# Customer Milestone 2 Deliverables

**Team Name:** Doğal Afet Paylaşım Platformu (DAPP)

**Date:** 01.12.2023

# Milestone Review

## A summary of the project status:
 - After milestone 1, we dive into the logic of the our project, which has some conflicts in the requirements. We added the new feature ``` recurrent activity```  that we forgot to provide in the first version of the requirements. This feature caused the most of the problems occurred in the action implementation. 
 - We have added dynamic forms for to be able to take needed information for different type of the activities such as food, clothing, transportation and so on. This feature had many bugs since it is hard to control form fields in both backend and frontend.
 - AWS free tier is not capable for to build both nextjs application and backend at the same time due to RAM incapabilities. We upgraded the machine for the demo, we were not able to run the github actions for automated deployment.
 - Our project defines the reliability of a user from its user type, the type of the user stricts the ability of the users to provide a safe information channel for urgent situations. So we define the user types on the backend side and give extra permissions to the verified users.
 - Annotation related requirements are not visited yet, due to the urgency of other improvements. The team will consider the take action as soon as possible.
 - In overall, we have implemented the core functions of the project but the UI/UX experience and error handling should be enhanced before starting final milestone improvements.

## A summary of the customer feedback and reflections:
- Customer requested user friendly and self-explanotory UI, espacially field labels such as `occur at`, `recurrence`, `quantity`.
- We had positive feedback on the deliverables, since we add the requested feature in previous customer demo.
- The way that we show action in the map should be improved, since we did not provide the information about the type of the transportation chosen or the road.

## Future improvements:
- We decided to plan user scenerio in the first week of the final milestone. 
- We had struggles on implementing the action system we will refactor the codes related with this component.
- As frontend and mobile we will review each other work daily to keep up.
- CI/CD pipeline is needed for the backend since assignee for deployment is not available all the time.
- Requirements will be changed according to the customer feedback.
- We have initiated the exploration for **suitable annotation technologies**. Moving forward, our intention is to implement these technologies specifically in map-related features and enhance search functionalities.


## List and status of deliverables:

|Requirement | Status |
|---|---|
| 1.1 User Requirements |  |
| 1.1.1  |  |
| 1.1.1.1 |  |
| 1.1.1.1.1 | completed  |
| 1.1.1.2. |  completed |
| 1.1.1.2.1 | not started |
| 1.1.1.2.2   |  |
| 1.1.1.3. | completed |
| 1.1.1.3.1 | not started |
| 1.1.2 | not started |
| 1.1.2.1. | not started |
| 1.1.2.2. | not started |
| 1.1.2.3.  |  |
| 1.1.2.3  | completed  |
| 1.1.2.4   |  |
|  1.1.3. | completed |
| 1.1.3.1.  | in progress |
| 1.1.3.2 User Actions |  |
| 1.1.3.3  | not_started |
| 1.1.3.4 | not_started |
| 1.1.3.5 | not started |
| 1.1.3.6  | completed |
| 1.1.3.7. | completed |
| 1.1.3.8. | completed |
| 1.1.3.9. | completed |
| 1.1.3.10. | completed |
| 1.1.3.11. | completed |
| --1.2. System Requirements | not_started |
| 1.2.1. | not_started |
| 1.2.1.2 | completed  |
| 1.2.2  | in_progress |
| 1.2.2.1 | not_started |
| 1.2.2.2. | in_progress |
| 1.2.2.3| completed |
| 1.2.2.4 | completed |
| 1.2.3  |completed |
| 1.2.3.1 | not_started |
| 1.2.3.1.1 | not_started |
| 1.3.11 | completed |
| 1.3.12  | completed |
| 1.3.13 | completed |
| 1.3.14 | completed |
| 1.3.15. (with its subtopics) | completed |
| -- 1.4. Admin User (with its subtopics)  | not_started |
| ## System Requirements |  |
| 2.1.2.5. Monitoring (with its subsections) | not started |
| 2.1.2.6. Knowledge (with its subsections) | completed |
| 2.1.2.7. Rating (with its subsections) | not started |
| 2.1.2.8. Notes (with its subsections) | in progress |
| 2.1.3. Following (with its subsections) | in progress |
| 2.1.4. Privacy (with its subsections)| not started|
| -- 2.2. Communication Channel |  |
| 2.2.1 | completed |
| 2.2.2 | completed |
| 2.2.2.1 | not_started |
| 2.2.3 | not_started |
| -- 2.3. Note Taking |  |
| 2.3.1  | completed |
| 2.3.2  | completed for current |
| 2.3.3   |completed  |
| 2.3.4   |completed for current |
| 2.3.4   |completed for current |
| -- 2.4. Performance and reliability |
|2.4.1. |completed|
|2.4.1.1. |in progress|
| -- 2.5. Backup and Recovery (Consistency) |  
|2.5.1.|completed|


## Provide progress according to requirements: being either not started, in progress, or completed (completed = implemented, tested, documented, and deployed)

## API Documentation and URL's:
* **The API documentation.**
http://3.218.226.215:8000/docs#/
* **The link to the API**
http://3.218.226.215:8000
* **The link to the Frontend**
http://3.218.226.215:3000
- **API endpoint examples**
    We are already using FastAPI response models for Swagger that shows status codes and response examples. But we are also using a collaborative workspace on [Postman]( https://darp-354343.postman.co/workspace/Team-Workspace~323ad32b-b319-4a0d-8ebe-e9ebe4c0a14d/collection/25451471-54780f30-5464-4b34-ada7-4b9e804588cc?action=share&creator=25451471)

## Software Test and Decided Approach:
Our testing strategy involves the meticulous use of unit tests for each endpoint. Additionally, we leverage Postman to rigorously assess edge cases by employing mock data.

- As frontend team there is so limited time to finish the both design and implementation. We also request changes from backend side when needed. Hence we could not find any time to test the frontend other then testing manually.
- As mobile team, we decided to use Espresso as test software, but we could not implement these tests, and we mostly relied on manual testing.
**Generated unit test reports**


# Individual Contributions
---

## Member: Merve Gürbüz

### Responsibilities: 

I am responsible for the deployment and AWS related tasks. I am a member of the frontend team.
 
### Main contributions:

I have added sort and filter for activities. I changed the forms for any type of input. I added many of the backend service endpoints. I changed the frontend design to make it a responsive website. I added the action activity, which was the most challeging in this milestone. I assigned the issues according to the member's knowledge and project plan. I made the activity card for the need and resource and add the function   upvote/downvote 

- **Code-related significant issues**:
    -  [Frontend - Need add page implementation #487](https://github.com/bounswe/bounswe2023group2/issues/487)
    -  [Frontend - Sort and filter components and implementations #492](https://github.com/bounswe/bounswe2023group2/issues/492)
    -  [Frontend - user upvote-downvote feature #518](https://github.com/bounswe/bounswe2023group2/issues/518)
   - [Frontend - Action implementation #546](https://github.com/bounswe/bounswe2023group2/issues/546)

- **Management-related significant issues**:

### Pull Requests:

  - **Reviewed:** [#641](https://github.com/bounswe/bounswe2023group2/pull/641),[#629](https://github.com/bounswe/bounswe2023group2/pull/629), [#628](https://github.com/bounswe/bounswe2023group2/pull/628),[#617](https://github.com/bounswe/bounswe2023group2/pull/617), [#550](https://github.com/bounswe/bounswe2023group2/pull/550)
    

   - **Created:**  [#549](https://github.com/bounswe/bounswe2023group2/pull/549), [#460](https://github.com/bounswe/bounswe2023group2/pull/460),[#463](https://github.com/bounswe/bounswe2023group2/pull/463), [#560](https://github.com/bounswe/bounswe2023group2/pull/560), [#589](https://github.com/bounswe/bounswe2023group2/pull/589)
 
### Additional Information:

Since our frontend team has three members, we do not need regular meetings apart from lab meets. But we have done milestone reviews together. 
I had to push many feature in one branch since I am handling all of them together because of member count. It is mainly because the checking out between branches is not working well and I do not want to waste time on it because of the deadline.
 
---
 
## Member: Begüm Arslan

### Responsibilities: 
I am a member of backend team. 


### Main contributions:
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
  
### Pull Requests:

  - **Reviewed:**
   [#550](https://github.com/bounswe/bounswe2023group2/pull/550), [#612](https://github.com/bounswe/bounswe2023group2/pull/612),[#593](https://github.com/bounswe/bounswe2023group2/pull/593), [#602](https://github.com/bounswe/bounswe2023group2/pull/602), [#630](https://github.com/bounswe/bounswe2023group2/pull/630), [#594](https://github.com/bounswe/bounswe2023group2/pull/594), [#648](https://github.com/bounswe/bounswe2023group2/pull/648), [#652](https://github.com/bounswe/bounswe2023group2/pull/652) 
   I only asked for enhancements related to resource fields etc. And we mainly decided to add them.

   - **Created:**
      [#577](https://github.com/bounswe/bounswe2023group2/pull/577), [#505](https://github.com/bounswe/bounswe2023group2/pull/505), [#615](https://github.com/bounswe/bounswe2023group2/pull/615), [#576](https://github.com/bounswe/bounswe2023group2/pull/576), [#645](https://github.com/bounswe/bounswe2023group2/pull/645),[#502](https://github.com/bounswe/bounswe2023group2/pull/502),[#647](https://github.com/bounswe/bounswe2023group2/pull/647) ,[#641](https://github.com/bounswe/bounswe2023group2/pull/641),[#644](https://github.com/bounswe/bounswe2023group2/pull/644) ,[#613](https://github.com/bounswe/bounswe2023group2/pull/613) , [#595](https://github.com/bounswe/bounswe2023group2/pull/595), [#561](https://github.com/bounswe/bounswe2023group2/pull/561) (This one was supposed to be merged before ms1, forgot to push changes(test))

### Additional Information:
  
---

## Member: Ramazan Burak Sarıtaş

### Responsibilities:
As a member of the Backend team, my responsibilities included contributing to backend implementation, participating in team meetings, and designing the logo of our platform.

### Main contributions:
Throughout Milestone 2, I remained actively involved in various aspects of the project, mainly focusing on enhancing and refining features related to the Resource and Need models and functionalities. I also implemented endpoints for the Frontend and Mobile team to fetch the necessary fields to dynamically generate the input forms of Resource and Need.

- **Code-related significant issues:**
  - Backend - GMT+3 for Storing Time ([#623](https://github.com/bounswe/bounswe2023group2/issues/623))
  - Backend - Filter (Resource and Need) ([#566](https://github.com/bounswe/bounswe2023group2/issues/566))
  - Backend - Sort (Resource and Need) ([#609](https://github.com/bounswe/bounswe2023group2/issues/609))
  - Backend - Bugfix - datetime.date cannot be inserted into mongodb ([#597](https://github.com/bounswe/bounswe2023group2/issues/597))
  - Backend - Recurrence fields to Resource and Need ([#592](https://github.com/bounswe/bounswe2023group2/issues/592))
  - Backend - Description field to Resource and Need ([#591](https://github.com/bounswe/bounswe2023group2/issues/591))
  - Backend - Form Fields Controller Enhancement ([#585](https://github.com/bounswe/bounswe2023group2/issues/585))
  - Backend - Need "Created at" & "Last Updated at" Fields ([#557](https://github.com/bounswe/bounswe2023group2/issues/557))
  - Backend - Resource "Created at" & "Last Updated at" Fields ([#552](https://github.com/bounswe/bounswe2023group2/issues/552))

- **Management-related significant issues**:
  - Integration of the Logo to the App ([#606](https://github.com/bounswe/bounswe2023group2/issues/606))
  - Backend - Predefined Subtypes Implementation ([#512](https://github.com/bounswe/bounswe2023group2/issues/512))
  - Backend - Creation of a Predefined Subtypes JSON File ([#513](https://github.com/bounswe/bounswe2023group2/issues/513))

### Pull Requests:

- **Reviewed:**
    - [#635](https://github.com/bounswe/bounswe2023group2/pull/635), [#626](https://github.com/bounswe/bounswe2023group2/pull/626), [#599](https://github.com/bounswe/bounswe2023group2/pull/599), [#595](https://github.com/bounswe/bounswe2023group2/pull/595)
    - In general, there were no conflicts in the PRs I reviewed.

 - **Created:** 
    - [#631](https://github.com/bounswe/bounswe2023group2/pull/631), [#630](https://github.com/bounswe/bounswe2023group2/pull/630), [#612](https://github.com/bounswe/bounswe2023group2/pull/612), [#602](https://github.com/bounswe/bounswe2023group2/pull/602), [#593](https://github.com/bounswe/bounswe2023group2/pull/593), [#558](https://github.com/bounswe/bounswe2023group2/pull/558), [#553](https://github.com/bounswe/bounswe2023group2/pull/553)

### Additional Information:

In addition to my primary tasks, I took the responsibility for designing the logo of our platform ([#600](https://github.com/bounswe/bounswe2023group2/issues/600)) and managed its integration into the Frontend and Mobile. ([#606](https://github.com/bounswe/bounswe2023group2/issues/606))
  
---

## Member: Halil İbrahim Gürbüz

### Responsibilities: 
Member of mobile team. Completing the tasks given to me and reviewing my teammates' pull requests was my responsibilities. I also took responsibility for the UI/UX design of the mobile application.



### Main contributions:
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

  
### Pull Requests:

- **Reviewed:**
   [#507](https://github.com/bounswe/bounswe2023group2/pull/507), [#510](https://github.com/bounswe/bounswe2023group2/pull/510), [#511](https://github.com/bounswe/bounswe2023group2/pull/511), [#547](https://github.com/bounswe/bounswe2023group2/pull/547), [#559](https://github.com/bounswe/bounswe2023group2/pull/559), [#567](https://github.com/bounswe/bounswe2023group2/pull/567), [#603](https://github.com/bounswe/bounswe2023group2/pull/603), [#618](https://github.com/bounswe/bounswe2023group2/pull/618), [#633](https://github.com/bounswe/bounswe2023group2/pull/633), [#654](https://github.com/bounswe/bounswe2023group2/pull/654)
   There were no conflicts in the PRs I reviewed. There was a case that I wanted to be fixed in [PR511](https://github.com/bounswe/bounswe2023group2/pull/511)

 - **Created:**
      [#504](https://github.com/bounswe/bounswe2023group2/pull/504), [#508](https://github.com/bounswe/bounswe2023group2/pull/508),  [#549](https://github.com/bounswe/bounswe2023group2/pull/549),  [#598](https://github.com/bounswe/bounswe2023group2/pull/598),  [#604](https://github.com/bounswe/bounswe2023group2/pull/604),  [#605](https://github.com/bounswe/bounswe2023group2/pull/605),  [#611](https://github.com/bounswe/bounswe2023group2/pull/611),  [#614](https://github.com/bounswe/bounswe2023group2/pull/614),  [#637](https://github.com/bounswe/bounswe2023group2/pull/637),  [#638](https://github.com/bounswe/bounswe2023group2/pull/638)

### Additional Information:

After the feedback we received after Milestone 1, I was interested in user interface and experience design, as well as the work I would do. Even though I have knowledge on this subject, I did research from time to time.
 
 
 
---


## Member: Aziza Mankenova

### Responsibilities:

As a member of backend team, for the milestone 2, I was responsible for the implementation of feedback system endpoints, malicious report CRUD endpoints and some of the admin endpoints. Also, took part in the backend meetings. 
### Main contributions:

My main contributions involved implementation of feedback system(upvote/downvote), malicious reports and admin endpoints, such as user verification, unverification and unauthorization, acception and rejection of malicious reports. Also, I was involved in project management through issue tracking, documentation updates, project planning and reviewing work of my teammates.

- **Code-related significant issues**:
  - [Backend- Report malicious activities/users to admin ](https://github.com/bounswe/bounswe2023group2/issues/569)
  - [Test - Backend - Report endpoints ](https://github.com/bounswe/bounswe2023group2/issues/571)
  - [Backend - Implement the check for the coordinates of resources and needs](https://github.com/bounswe/bounswe2023group2/issues/584)
  - [Backend - Feedback system upvote/downvote endpoint](https://github.com/bounswe/bounswe2023group2/issues/480)
  - This was a collaborative issue, I was responsible for implementing admin checker function, user verification, unverification and unauthorization, acception and rejection of malicious reports by admin
      - [Backend - Admin CRUD and specific endpoint implementations](https://github.com/bounswe/bounswe2023group2/issues/519)
  - [Test - Backend - Feedback endpoints](https://github.com/bounswe/bounswe2023group2/issues/524)
  
- **Management-related significant issues**:
  - [Backend Team Meeting #4](https://github.com/bounswe/bounswe2023group2/issues/481)
  - [Backend - Requirements and design documents study](https://github.com/bounswe/bounswe2023group2/issues/483)
  - [Backend - Milestone 2 Plan Review](https://github.com/bounswe/bounswe2023group2/issues/489)
  - [Backend Team Meeting #5 ](https://github.com/bounswe/bounswe2023group2/issues/521)


### Pull Requests:

- **Reviewed:** [#551](https://github.com/bounswe/bounswe2023group2/pull/551), [#615](https://github.com/bounswe/bounswe2023group2/pull/615), [#621](https://github.com/bounswe/bounswe2023group2/pull/621), [#631](https://github.com/bounswe/bounswe2023group2/pull/631) 

    Although we encountered some conflicts, they were generally minor issues, such as changes in dependencies or imports. For the most reviews, I asked for minor changes.
 - **Created** [#594](https://github.com/bounswe/bounswe2023group2/pull/594), [#590](https://github.com/bounswe/bounswe2023group2/pull/590), [#554](https://github.com/bounswe/bounswe2023group2/pull/554), [#449](https://github.com/bounswe/bounswe2023group2/pull/449), [#458](https://github.com/bounswe/bounswe2023group2/pull/458)
 
### Additional Information:

----
## Member: Egecan Serbester

### Responsibilities: 
Member of mobile team. Completing required tasks and reviewing my teammate's PR's, managing what to do in that week and allocating issues to available members of the team and presenting the mobile part at demo are my responsibilities.



### Main contributions:
My main contribution to the project is providing backend connections for need, resources, upvote/downvote and user Roles and designing their basic ui with their functionalities. Additionally, arranging the architecture for other mate's developments with respect to MVVM. 


- **Code-related significant issues**:

  - [Mobile - Edit Resource Form](https://github.com/bounswe/bounswe2023group2/issues/495)
  - [Mobile - Connect Edit Form's into backend](https://github.com/bounswe/bounswe2023group2/issues/497) 
  - [Mobile - Arranging Location as X, Y coordinates ](https://github.com/bounswe/bounswe2023group2/issues/506)
  - [Mobile - Vote Needs and Resources](https://github.com/bounswe/bounswe2023group2/issues/543)
  - [Mobile - Arranging CRUD Needs and Resources](https://github.com/bounswe/bounswe2023group2/issues/610)
  - [Mobile - Save User Role](https://github.com/bounswe/bounswe2023group2/issues/583)

- **Management-related significant issues**:

  
### Pull Requests:
  - **Reviewed:**
   [#504](https://github.com/bounswe/bounswe2023group2/pull/504),[#508](https://github.com/bounswe/bounswe2023group2/pull/508),[#509](https://github.com/bounswe/bounswe2023group2/pull/509),
[#549](https://github.com/bounswe/bounswe2023group2/pull/549),[#611](https://github.com/bounswe/bounswe2023group2/pull/611),[#614](https://github.com/bounswe/bounswe2023group2/pull/614),
[#622](https://github.com/bounswe/bounswe2023group2/pull/622),[#638](https://github.com/bounswe/bounswe2023group2/pull/638),[#642](https://github.com/bounswe/bounswe2023group2/pull/642),[#650](https://github.com/bounswe/bounswe2023group2/pull/650)

  - **Created:**
      [#510](https://github.com/bounswe/bounswe2023group2/pull/510), [#511](https://github.com/bounswe/bounswe2023group2/pull/511),[#507](https://github.com/bounswe/bounswe2023group2/pull/507),[#547](https://github.com/bounswe/bounswe2023group2/pull/547),[#567](https://github.com/bounswe/bounswe2023group2/pull/567),[#624](https://github.com/bounswe/bounswe2023group2/pull/624),[#634](https://github.com/bounswe/bounswe2023group2/pull/634),[#640](https://github.com/bounswe/bounswe2023group2/pull/640), [#646](https://github.com/bounswe/bounswe2023group2/pull/646),[#651](https://github.com/bounswe/bounswe2023group2/pull/651)
      
### Additional information:

I was focusing on the functionality part instead of UI/UX design, I add some basic UI but arranging this UI in a beautiful way is Halil Abi's responsibility in general, I specially thanks him to his efforts.
  
  

---
## Member: Mehmet Kuzulugil
### Responsibilities
Member of Backend subteam.
Coding according to our internal division of work, reviewing fellow members' work, reconsidering design issues related to my parts in coding and also contributing to other members' reviews of design issues.

### Main contributions:

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
  - [Backend Team Meeting #5](https://github.com/bounswe/bounswe2023group2/issues/529)
      - [Meeting notes](https://github.com/bounswe/bounswe2023group2/wiki/MeetingNodes-%E2%80%90BE-%235-%E2%80%90-21.11.2023)
  - [Evaluating the evaluations on our Milestone 1 delivery/demo](https://github.com/bounswe/bounswe2023group2/wiki/About-evaluations-of-Milestone-1-by-our-fellow-students)
  - Issues related:
    - [Review of Project Plan](https://github.com/bounswe/bounswe2023group2/issues/482)
    - [Backend - Requirements and design documents study](https://github.com/bounswe/bounswe2023group2/issues/483)
    - [Backend - Milestone 2 Plan Review](https://github.com/bounswe/bounswe2023group2/issues/489)


### Pull Requests:
- Created by:
    - [#551](https://github.com/bounswe/bounswe2023group2/pull/551) Event CRUD
    - [#620](https://github.com/bounswe/bounswe2023group2/pull/620) Admin endpoints
    - [#626](https://github.com/bounswe/bounswe2023group2/pull/626) Event model improvement
 - Reviewed by:
    - [#505](https://github.com/bounswe/bounswe2023group2/pull/505)
    - [#561](https://github.com/bounswe/bounswe2023group2/pull/561)
    - [#576](https://github.com/bounswe/bounswe2023group2/pull/576)
    - [#577](https://github.com/bounswe/bounswe2023group2/pull/577)
    - [#590](https://github.com/bounswe/bounswe2023group2/pull/590)
    - [#621](https://github.com/bounswe/bounswe2023group2/pull/621)
    - [#645](https://github.com/bounswe/bounswe2023group2/pull/645)
    

### Other Information:

Check [individual contribution page](https://github.com/bounswe/bounswe2023group2/wiki/Individual-contribution-by-Mehmet-Kuzulugil-to-DaRP) for weekly effort reports.

---

## Member: Hasan Bingölbali

### Responsibilities: 
My main responsability was to create visually appealing map and make it functional. I also worked hard on creating satifastory user experience.


### Main contributions:
I created map, added dynamic pinpoints for need and resource. Connected need and resource through actions. Enabled users to select need and resource location through map. Also implemented delete endpoint for need and resource and handled navigation afterwards.


- **Code-related significant issues**:
  - [Mobile - Adding map pinpoints](https://github.com/bounswe/bounswe2023group2/issues/535)
  - [Mobile - Map visualization for need, action, event and resources](https://github.com/bounswe/bounswe2023group2/issues/541)
  - [Mobile - Map Activity Redirect](https://github.com/bounswe/bounswe2023group2/pull/603)
  - [Mobile - Choose Location Through Map View ](https://github.com/bounswe/bounswe2023group2/issues/572)
  - [Mobile - Action Visualization ](https://github.com/bounswe/bounswe2023group2/issues/573)
  - [Mobile - Finalize Map View ](https://github.com/bounswe/bounswe2023group2/issues/574)
  - [Mobile - Delete Need and Resources](https://github.com/bounswe/bounswe2023group2/issues/501)


- **Management-related significant issues**:


  
### Pull Requests:
  - **Reviewed:**
   [#556](https://github.com/bounswe/bounswe2023group2/pull/556),  [#603](https://github.com/bounswe/bounswe2023group2/pull/603), [#604](https://github.com/bounswe/bounswe2023group2/pull/604), [#605](https://github.com/bounswe/bounswe2023group2/pull/605), [#634](https://github.com/bounswe/bounswe2023group2/pull/634)

   - **Created:**
         [#509](https://github.com/bounswe/bounswe2023group2/pull/509),  [#555](https://github.com/bounswe/bounswe2023group2/pull/555), [#618](https://github.com/bounswe/bounswe2023group2/pull/618), [#622](https://github.com/bounswe/bounswe2023group2/pull/622), [#636](https://github.com/bounswe/bounswe2023group2/pull/636), [#603](https://github.com/bounswe/bounswe2023group2/pull/603)

### Additional information**:

I want to thank mobile team for working so hard. As a team we worked together, and end result was satisfactory.
    
---


## Member: Can Bora Uğur

### Responsibilities:
I am a member of the front-end team. My responsibilities before Customer Milestone 2, according to the project plan, were originally to work on email verification, upvote-downvotes to measure reliability, and the report feedback functionality. I also ended up expanding the profile page from Milestone 1 to make it possible to view other's profile pages, and I took the responsibility of admin page implementation instead of email verification and upvote-downvotes because it was much closer to the functionality I was already implementing.

### Main contributions:


- **Code-related significant issues**:
    - [Frontend - Admin page design and implementation](https://github.com/bounswe/bounswe2023group2/issues/486)
    - [Frontend - Add missing features for profile (split later)](https://github.com/bounswe/bounswe2023group2/issues/491)
    - [Frontend - Ability to add and remove professions, skills, languages, social media](https://github.com/bounswe/bounswe2023group2/issues/564)
    - [Frontend - Viewing others' profiles](https://github.com/bounswe/bounswe2023group2/issues/568)
    - [Frontend - Feedback component design and implementation](https://github.com/bounswe/bounswe2023group2/issues/575)
    - [Frontend - Main page bugfix](https://github.com/bounswe/bounswe2023group2/issues/616)
- **Management-related significant issues**:
    - [Frontend - Milestone 2 plan review](https://github.com/bounswe/bounswe2023group2/issues/485)
    - [Frontend - Milestone 2 Mid-review](https://github.com/bounswe/bounswe2023group2/issues/527)
  
### Pull Requests:
  - **Created:**
    - [Frontend - Add social media/skill/language/professions to user profile](https://github.com/bounswe/bounswe2023group2/pull/565)
    - [Frontend - Main page bugfix](https://github.com/bounswe/bounswe2023group2/pull/617)
    - [Frontend - Admin panel](https://github.com/bounswe/bounswe2023group2/pull/628)
    - [Frontend - Viewing other users' profiles](https://github.com/bounswe/bounswe2023group2/pull/629)
  - **Reviewed:**
    - [Backend - Admin-related endpoints](https://github.com/bounswe/bounswe2023group2/pull/621). There were some minor changes needed, but we decided to postpone some of those changes to a later pull request.
  

---

## Member: Cahid Enes Keleş

### Responsibilities:
I was responsible for the profile page UI in the first milestone. Consequently I was responsible for the profile page backend connection, edit profile page UI and backend connection. I was also responsible for different tasks related to maps with Hasan.


### Main contributions:
I implemented the edit profile page and made the connection between backend and mobile application. I made some visual improvements to various pages like the map view and need details pages.


- **Code-related significant issues**:
    - [Mobile - Connect backend for Profile Information](https://github.com/bounswe/bounswe2023group2/issues/416)
    - [Mobile - Logged In Indicator](https://github.com/bounswe/bounswe2023group2/issues/443)
    - [Mobile - Edit Profile Controlled Fields](https://github.com/bounswe/bounswe2023group2/issues/528)
    - [Mobile - App Icon](https://github.com/bounswe/bounswe2023group2/issues/534)
    - [Mobile - Map visualization for need, action, event and resources](https://github.com/bounswe/bounswe2023group2/issues/541)
    - [Mobile - Map Activity Redirect](https://github.com/bounswe/bounswe2023group2/issues/570)
    - [Mobile - Choose Location Through Map View](https://github.com/bounswe/bounswe2023group2/issues/572)
    - [Mobile - Finalize Map View](https://github.com/bounswe/bounswe2023group2/issues/574)
    - [Mobile - Profile Redirect Login](https://github.com/bounswe/bounswe2023group2/issues/632)
    - [Mobile - Change map activity icons](https://github.com/bounswe/bounswe2023group2/issues/643)

- **Management-related significant issues**:
    - [Mobile - UI improvements](https://github.com/bounswe/bounswe2023group2/issues/601)
  
### Pull Requests:
  - **Reviewed:**
      -  [Map Pinpoints #555](https://github.com/bounswe/bounswe2023group2/pull/555)
      -  [Main Page Design #598](https://github.com/bounswe/bounswe2023group2/pull/598)
      -  [Arrange Need Resource Backend #624](https://github.com/bounswe/bounswe2023group2/pull/624)
      -  [Action Visualization #636](https://github.com/bounswe/bounswe2023group2/pull/636)
      -  [Sort and Filter #637](https://github.com/bounswe/bounswe2023group2/pull/637)
      -  [Voting Resource Need #640](https://github.com/bounswe/bounswe2023group2/pull/640)
      -  [Filtering Cloth Hotfix #646](https://github.com/bounswe/bounswe2023group2/pull/646)
      -  [User Roles #651](https://github.com/bounswe/bounswe2023group2/pull/651)

  - **Created:**
      - [Edit Profile and Backend #556](https://github.com/bounswe/bounswe2023group2/pull/556)
      - [Edit Profile Controlled Fields #559](https://github.com/bounswe/bounswe2023group2/pull/559)
      - [Map Action Redirect #603](https://github.com/bounswe/bounswe2023group2/pull/603)
      - [Proifle Redirect Signin #633](https://github.com/bounswe/bounswe2023group2/pull/633)
      - [Map Icon Colors #642](https://github.com/bounswe/bounswe2023group2/pull/642)
      - [Need Details Page UI #650](https://github.com/bounswe/bounswe2023group2/pull/650)
      - [Need Item Page Hotfix #654](https://github.com/bounswe/bounswe2023group2/pull/654)
---

## Member: Ömer Şahin Albayram

### Responsibilities:

Member of frontend team. In milestone 2, my responsibilites was making map more interactive and informative, designing and implementing filter menu, however mostly I failed to fulfill my responsibilites because of other lectures and some other private reasons.

### Main contributions:
My overall contribution to this milestone has been to plan how we do things in general, or in other words, how it is better for us to things such as how should we store resources and needs.
  
- **Management-related significant issues**:
  - [Deciding Activity attributes for activity information card on map page](https://github.com/bounswe/bounswe2023group2/issues/488)
  - [Frontend - Research about how filtered activites might be printed](https://github.com/bounswe/bounswe2023group2/issues/493)
  - [Resource / Need final design and coding](https://github.com/bounswe/bounswe2023group2/issues/499)
  - [Backend -Frontend Meeting ](https://github.com/bounswe/bounswe2023group2/issues/536)

### Pull Requests:
  - **Reviewed:** [619](https://github.com/bounswe/bounswe2023group2/pull/619) 


### Additional information:
