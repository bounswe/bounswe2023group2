# Cmpe451 Final Project Team Report

**Prepared by:**

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
- Buse Tolunay

**Video:** [Link to video demo ](https://www.youtube.com/watch?v=A7uFQQdA6pE)

## 1. [Group Milestone Report](#group-milestone-report)

- 1.1 [Executive Summary](#executive-summary)

  - 1.1.1 [Summary of the Project Status](#summary-of-the-project-status)
  - 1.1.2 [The Status of the Deliverables](#the-status-of-the-deliverables)
  - 1.1.3 [Final Release Notes](#final-release-notes)
  - 1.1.4 [Reflections on Final Milestone Demo and Lessons Learned](#reflections-on-final-milestone-demo-and-lessons-learned)
  - 1.1.5 [What Could Be Done Differently](what-could-be-done-differently)

- 1.2 [Progress Based on Teamwork](#progress-based-on-teamwork)

  - 1.2.1 [Summary of Work Performed by Each Team Member](#summary-of-work-performed-by-each-team-member)
  - 1.2.2 [Status of the Requirements](#status-of-the-requirements)
  - 1.2.3 [API Endpoints](#api-endpoints)
  - 1.2.4 [User Interface/User Experience](#user-interfaceuser-experience)
  - 1.2.5 [Annotations](#annotations)
  - 1.2.6 [Standards](#standards)
  - 1.2.7 [Scenarios](#scenarios)

## 2. [Project Artifacts](#project-artifacts)

- 2.1.1 [Manuals](#manuals)
  - 2.1.1.1 [User Manual](#user-manual)
  - 2.1.1.2 [System Manual](#system-manual)
- 2.1.2 [Software Requirements Specification (SRS)](#software-requirements-specification-srs)
- 2.1.3 [Software Design Documents (UML)](#software-design-documents-uml)
- 2.1.4 [User Scenarios and Mockups](#user-scenarios-and-mockups)
- 2.1.5 [Project Plan](#project-plan)
- 2.1.6 [Unit Tests](#unit-tests)

## 3. [Software Package](#software-package)

# Group Milestone Report

## Executive Summary

### Summary of the Project Status

DAPP (Disaster Provision Platform) aims to create a fluent system to ensure the aids reaches its destination both optimized and accurate way in cases of natural disasters or difficult conditions. As a team we focused on provide a platform for individuals to keep track on activities related to the disaster area besides providing logging their actions about the disaster relief. You can access the web application by clicking [here](http://3.218.226.215:3000/) and you can find the APK of the android application via [here](https://github.com/bounswe/bounswe2023group2/releases/download/final-submission-g2/app-debug.apk). Overall we accomplished the main features of the application although we could not be able to provide the customer's additional requests from us such as aggregations and large scaled actions.

Additional to the general functions we defined, We have added:

- semantic search
- text annotation

We have not implemented annotations on the web due to timing issues.
Our product is deployed on the AWS and semantic search server deployed on Digital Ocean. The application is ready for its users though some bugs. We aimed to create a responsive web application for mobile users too, and our mobile application supports android up-to version 6 . We added English and Turkish language supports beside dark-light themes. We focused on using same colors and designs in mobile and web, and choose our color palette correlated to the content.

## Final release notes

API address : http://3.218.226.215:8000/docs
Web address: http://3.218.226.215:3000/
Annotation address: http://3.218.226.215:18000/

#### What has changed?

- Feedback mechanism is changed to reliability scale.
- mobile and frontend item designs hav finalized.
- Admin page and functionalities added.
- Profile photo upload, license upload features implemented.
- User and activity report added.
- Action and recurrence mechanism changed.
- User roles enum changed and user scopes redefined.
- Emergency implemented.
- Address to X-Y and X-Y to address added endpoints added.
- Password verification and reset implemented.
- Semantic search and annotation servers deployed.
- Multi-language package (en-tr) added.
- Annotation implemented on mobile.
- Search implemented both frontend and mobile.

#### Reflections on Final Milestone Demo and Lessons Learned

After the second milestone we aimed to reach our project plan so the sprint workload drastically increased. We have tried to finish all the requirements left after milestone 2, which was hard to achieved in our conditions. We had to changed many components on web and mobile because of negative feedbacks in the milestone 2. It impacted the other ongoing tasks.

#### What Could Be Done Differently

We had several core functionalities to implemented during the project. Furthermore, we had basic application requirements such as authentication, file uploading, profile etc. We did not precede the core functionalities since we believed that we can do all of them. But we had to solve many bugs during the development which cost a lot of time.

It was hard to divide the tasks and track them during the semester due to team members general workload. And most of the team members learned the technologies during the semester, we should have more engaged in all semester for anyone to sync the project fluently.

## Status of Deliverables

### List and Status of Deliverables

| Deliverable                                                                                                    | Status    | Last Updated |
| -------------------------------------------------------------------------------------------------------------- | --------- | ------------ |
| [Software Requirements Specification](https://github.com/bounswe/bounswe2023group2/wiki/Requirements)          | Completed | 28.11.2023   |
| [Class Diagram](https://github.com/bounswe/bounswe2023group2/wiki/Class-Diagrams)                              | Completed | 30.05.2023   |
| [Use Case Diagram](https://github.com/bounswe/bounswe2023group2/wiki/Use-Cases)                                | Completed | 30.05.2023   |
| [Sequence Diagrams](https://github.com/bounswe/bounswe2023group2/wiki/Sequence-Diagrams)                       | Completed | 30.05.2023   |
| [Mockups](https://github.com/bounswe/bounswe2023group2/wiki/Mockups)                                           | Completed | 30.05.2023   |
| [Scenario and Mockup 1](https://github.com/bounswe/bounswe2023group2/wiki/Scenario-1)                          | Completed | 30.05.2023   |
| [Scenario and Mockup 2](https://github.com/bounswe/bounswe2023group2/wiki/Scenario-2---Use-Map)                | Completed | 30.05.2023   |
| [Scenario and Mockup 3](https://github.com/bounswe/bounswe2023group2/wiki/Scenario-3)                          | Completed | 30.05.2023   |
| [Scenario and Mockup 4](https://github.com/bounswe/bounswe2023group2/wiki/Scenerio-4----Creating-an-Emergency) | Completed | 30.05.2023   |
| [API Specifications](http://3.218.226.215:8000/docs)                                                           | Completed | 26.12.2023   |
| [Project Plan](https://github.com/orgs/bounswe/projects/12)                                                    | Completed | 28.11.2023   |

## Requirements Coverage

<table>
  <tr> <th> ID </th> <th> Name </th> <th> Status B/F/M </th> <th> Notes </th> <tr>
  <tr> <td> 1.1.1.1.1 </td> <td> Users shall be able to create an account using a valid and unique email address or phone number, a unique username, their name, their surname and a password. </td> <td>  C/%80/%80  </td> <td>  Mobile and web don't implement creating account with phone number </td> </tr> 
  <tr> <td> 1.1.1.2.1 </td> <td> Users shall be able to log into their account using their email, username or phone number; and password combination.  </td> <td> C/%80/%80 </td> <td> Mobile and web don't implement creating account with phone number </td> </tr>
  <tr> <td> 1.1.1.2.2 </td> <td> Users shall be able to reset their password via email verification or sms verification.  </td> <td> C/C/C </td> <td> We don't implement SMS verification since it was expensive </td> </tr>
  <tr> <td> 1.1.1.3.1 </td> <td> Users shall be able to log out of their accounts.  </td> 
      <td> C/C/C </td> 
      <td> </td> </tr>
  <tr> <td> 1.1.2.1.1 </td> <td> Guest users shall be able to create only one emergency.  </td>
      <td>  %80/%80/%80 </td>
      <td> We don't control for one emergency </td> </tr>
  <tr> <td> 1.1.2.1.2 </td> <td> Guest users shall be able to view emergencies and activities about disasters, including event types, resources available, and actions taken. </td> 
      <td> C/C/C </td>
      <td> </td> </tr>
  <tr> <td> 1.1.2.1.3 </td> <td> Guest users shall be able to filter and sort activities about emergencies, events, resources, actions, and needs based on location, date, type etc. </td>
      <td> C/C/C </td> 
      <td> </td> </tr>
  <tr> <td> 1.1.2.2.1 </td> <td> Authenticated users shall have all functionalities that guest users have. </td>  <td> C/C/C</td>   <td> </td> </tr>
  <tr> <td> 1.1.2.2.2 </td> <td> Authenticated users shall be able to create activities.  </td>   <td> C/C/C </td>  <td> </td> </tr>
  <tr> <td> 1.1.2.2.3 </td> <td> Authenticated users shall be able to update and delete their current active activities. </td> 
      <td>C/%80/%80 </td> <td> Edit is not implemented on frontend, Actions couldnt create properly in mobile </td> </tr>
  <tr> <td> 1.1.2.2.4 </td> <td> Authenticated users shall verify their accounts by verifying their phone numbers or emails unless already verified by system admins. </td>     <td>C/C/C </td> <td>SMS verification is cancelled </td> </tr>
  <tr> <td> 1.1.2.2.5 </td> <td> Authenticated users shall be able to delete their accounts. </td>   <td>C/%75/C </td>  <td> </td> </tr>
  <tr> <td> 1.1.2.2.6 </td> <td> Authenticated users shall be able to edit their profiles. </td>   <td> C/C/C </td>  <td> </td> </tr>
  <tr> <td> 1.1.2.2.7 </td> <td> Authenticated users shall be able to upvote or downvote activities. </td>  <td>C/C/C </td> <td> </td> </tr>
  <tr> <td> 1.1.2.2.8 </td> <td> Authenticated users shall be able to report malicious users or activities to the admins. </td> 
      <td>C/NC/C </td> 
      <td> </td> </tr>
  <tr> <td> 1.1.2.2.9 </td> <td> Authenticated users should be able to receive notifications about certain topics based on location, date, type etc. </td>
      <td>NC/NC/NC </td>
      <td> </td> </tr>
  <tr> <td> 1.1.2.2.10 </td> <td> Authenticated users shall be able to search activities and emergencies. </td> 
      <td>C/C/C </td>
      <td> </td> </tr>
  <tr> <td> 1.1.2.2.11 </td> <td> Authenticated users shall be able to search profiles. </td> 
      <td> C/C/C </td> 
      <td> </td> </tr>
  <tr> <td> 1.1.2.2.12 </td> <td> Authenticated users should be able to subscribe to topics using filter and search on topics. </td> 
      <td>NC/NC/NC </td> 
      <td> We cancelled requirements about the topics and notifications at the beginnig of MS3 when we realized that these issues can't implement on time </td> </tr>
  <tr> <td> 1.1.2.2.13 </td> <td> Authenticated users should be able to add and delete topics for notifications. </td> 
      <td> NC/NC/NC</td> 
      <td> </td> </tr>
  <tr> <td> 1.1.2.2.14 </td> <td> Authenticated users should be able to view details or dismiss when they receive a notification. </td> <td> NC/NC/NC </td>
      <td>  </td> </tr>
  <tr> <td> 1.1.2.2.15 </td> <td> Authenticated users shall be able to choose a role after adding a valid phone number. </td>
      <td> C/C/C </td> 
      <td> </td> </tr>
  <tr> <td> 1.1.2.2.16 </td> <td> Authenticated users shall be able to select who can see their contact information.  </td> 
      <td> C/C/C </td> 
      <td> </td> </tr>
  <tr> <td> 1.1.2.3.1 </td> <td> Role-Based users shall have all functionalities that authenticated users have. </td>
      <td>C/C/C </td> 
      <td> </td> </tr>
  <tr> <td> 1.1.2.3.2 </td> <td> Role-Based users shall be able to add information about their proficiency and how they can help. </td> <td> C/C/C </td> 
      <td> They can upload their documents on the application additionally </td> </tr>
  <tr> <td> 1.1.2.4.1 </td> <td> Credible users shall have all functionalities that role-based users have. </td> 
      <td> C/C/C</td>
      <td> </td> </tr>
  <tr> <td> 1.1.2.4.2 </td> <td> Credible user shall be able to create a special activity which is prioritized on lists and maps. </td> 
      <td>C/C/C  </td> 
      <td> The sort mechanism on backend automatically sorts the activities. </td> </tr>
  <tr> <td> 1.1.3.1 </td> <td> Admin users shall have all functionalities that credible users have.  </td> 
      <td>C/C/C </td>
      <td> Mobile don't implement the admin related specifications since it is handled in front and most of the apps has no admin panel on mobile </td> </tr>
  <tr> <td> 1.1.3.2 </td> <td> Admin users shall be able to search users and see the contact information of other users. </td> 
      <td>C/C/C </td>
      <td> </td> </tr>
  <tr> <td> 1.1.3.3 </td> <td> Admin users shall be able to view the misinformation reports. </td> 
      <td>C/C/NC </td> 
      <td> </td> </tr>
  <tr> <td> 1.1.3.4 </td> <td> Admin users shall be able to accept or reject a misinformation report.  </td> 
      <td> C/C/NC </td> 
      <td> </td> </tr>
  <tr> <td> 1.1.3.5 </td> <td> Admin users shall be able to remove activities from the platform. </td> 
      <td>C/C/NC </td> 
      <td> </td> </tr>
  <tr> <td> 1.1.3.6 </td> <td> Admin users shall be able to see authenticated users activities. </td> 
      <td> C/C/NC</td> 
      <td> </td> </tr>
  <tr> <td> 1.1.3.7 </td> <td> Admin users shall be able to cancel role-based users' role or credible users' credibility. </td>  <td> C/C/NC </td>  <td> </td> </tr>
  <tr> <td> 1.1.3.8 </td> <td> Admin users shall be able to see and recover canceled role or credibility. </td>   <td> C/C/NC </td>  <td> </td> </tr>
  <tr> <td> 1.1.3.9 </td> <td> Admin users shall be able to select or approve users as credible users. </td>   <td>C/C/NC </td>  <td> </td> </tr>
  <tr> <td> 1.1.3.10 </td> <td> Admin users shall be able to pin some activities at the top of list to increase their visibility. </td>  <td>NC/NC/NC </td>  <td> </td> </tr>
  <tr> <td> 1.1.3.11 </td> <td> Admin users shall be able to ban users from the system. </td>  <td> NC/NC/NC</td>  <td> </td> </tr>
    
  <tr> <td> 1.2.1.1 </td> <td>

A user profile shall have these attributes:... (can be seen on wiki page)

   </td> <td> C/C/C</td> <td> </td> </tr>
  <tr> <td> 1.2.1.2 </td> <td> The system should show related activities to the authenticated user that has related proficiency. (i.e A doctor should see the event that medications arrive in the area.) </td> <td>NC/NC/NC </td> <td> </td> </tr>
  <tr> <td> 1.2.2.1 </td> <td> The system shall allow users to report malicious users and activities. </td> <td> C/NC/C </td> <td> </td> </tr>
  <tr> <td> 1.2.2.2 </td> <td> The system shall allow users to check for a number of other users' upvotes or downvotes about activities. </td> <td> C/C/C </td> <td>Implemented but due to feedback the numbers shown were removed.</td> </tr>
  <tr> <td> 1.2.2.3 </td> <td> The system shall carry on reports to the administration system. </td> <td> C/C/NC</td> <td> </td> </tr>
  <tr> <td> 1.2.2.4 </td> <td> The system shall not accept the restricted accounts to register again. </td> <td>NC/NC/NC </td> <td> </td> </tr>
  <tr> <td> 1.2.3.1.1.1 </td> <td>

A resource shall have these attributes: Type(required), Subtype, can be predefined or other typed, Location, Quantity, username, creation time, last update time, upvote/downvote count, reliability scale </td> <td>C/C/C </td> <td> </td> </tr>

  <tr> <td> 1.2.3.1.1.2 </td> <td>
  A resource should have attributes: different resources should have additional attributes if needed, available at certain times, extra information, related needs  (related needs are for predefined needs and resources) (optional) </td> <td> C/C/C </td> <td> </td> </tr>
  <tr> <td> 1.2.3.1.2.1 </td> <td> Resources should be organized in a structured manner to allow for easy access and management. </td> <td>C/C/C </td> <td> </td> </tr>
  <tr> <td> 1.2.3.1.2.2 </td> <td> The following predefined resources shall be included: food, clothing, accommodation, and human resources. </td> <td> C/C/C </td> <td> </td> </tr>
  <tr> <td> 1.2.3.1.2.2.1 </td> <td>
  Subtypes of food shall include: Non-perishable items (such as pasta, rice, canned goods, and other relevant items), Baby food and similar items for infants and young children, Dietary-specific items (such as gluten-free, vegetarian, and other relevant items for individuals with specific dietary needs) </td> <td> C/C/C</td> <td> </td> </tr>
  <tr> <td> 1.2.3.1.2.2 </td> <td>

Subtypes of human resources shall include: Medical professionals (such as doctors, nurses and pharmacists), Emergency responders (such as firefighters and police officers), Support staff (such as drivers, translators and other relevant roles necessary for disaster response) </td> <td> 70%/70%/70% </td> <td> </td> </tr>

  <tr> <td> 1.2.3.1.2.3 </td> <td>

Subtypes of clothes shall include: Seasonal clothing which is appropriate for the weather conditions: coats, hats, sweaters, gloves and shoes. Underwear clothing which is necessary for personal hygiene. </td> <td>70%/70%/70% </td> <td> </td> </tr>

  <tr> <td> 1.2.3.2.1 </td> <td>
  Events shall have these attributes: Type (it can be predefined or other typed), Creation time, Creator username, Location, Confirmer username, Last confirmation time, Upvote/Downvote count, Reliability scale   </td>  <td> C/C/C </td>  <td> </td> </tr>
  <tr> <td> 1.2.3.2.2 </td> <td>

The following predefined event types shall be included: Road Blocked Event (Debris), With Live Human Collapsed Event (Debris), Power Cut (Infrastructure), On-Fire Event (Disaster), Building Tent City Event (help-arrived) </td> <td>C/C/C </td> <td> </td> </tr>

  <tr> <td> 1.2.3.3.1 </td> <td>
  Actions shall have these attributes: Type (it can be predefined or other typed), Creation time, Creator username,  Related resources and needs (Using these resources, handling these needs etc), Confirmer username, Last confirmation time, Upvote/Downvote count, Reliability scale, Current status </td> <td> C/C/C </td> <td> Confirmation information is not delivered </td> </tr>
  <tr> <td> 1.2.3.4.1 </td> <td>
  Needs shall have these attributes:, Type (can be food, clothes, shelter, medical assistance, heat; should be flexible) , Subtype according to type, Creation time, Creator/Demander username, Location, Quantity, Urgency, Upvote/Downvote count, Reliability scale, Current status </td> <td> C/C/C </td> <td> </td> </tr>
  <tr> <td> 1.2.3.4.2 </td> <td> Needs should be flexible enough to accommodate changing needs and priorities over time, as the disaster situation evolves. </td> <td> C/C/C</td> <td> </td> </tr>
  <tr> <td> 1.2.3.4.3 </td> <td> Status of needs should be in a timely update and demanders shall receive status notifications for each update. </td> <td> NC/NC/NC </td> <td> </td> </tr>
  <tr> <td> 1.2.3.5.1 </td> <td>

Emergencies shall have these attributes: Type: Can be News, Debris, Infrastructure, Disaster, Help-Arrived (Check that other than News, types are of Event Types), Location, Contact Name, Contact Number, Description, Number of Upvotes, Number of Downvotes, Creation time, Creator username, Verification notes, Updated time </td> <td> C/C/C </td> <td> </td> </tr>

  <tr> <td> 1.2.3.5.2 </td> <td> Emergencies will be used as draft event information by users.  </td> <td>NC/NC/NC </td> <td> </td> </tr> 
  <tr> <td> 1.2.4.1 </td> <td> The system shall sort activities and emergencies based on reliability scale, date, urgency level etc. </td> <td> C/C/C </td> <td> </td> </tr>
  <tr> <td> 1.2.4.2 </td> <td> The system shall filter activities and emergencies based on location, type and subtype scale, date, urgency level etc. </td> <td>C/C/C </td> <td> </td> </tr>
  <tr> <td> 1.2.4.3 </td> <td> The system shall sort users based on their roles and locations. </td> <td>C/NC/NC </td> <td> </td> </tr>
  <tr> <td> 1.2.5.1 </td> <td> Maps shall contain some annotation on them. Annotations shall have different colours biased on the urgency level.  </td> <td>NC/NC/NC </td> <td> </td> </tr>
  <tr> <td> 1.2.5.2 </td> <td> The map shall be zoomed in and out and interactable. The annotations in the map should scale up and down accordingly. </td> <td> NC/NC/NC </td> <td>Annotation is not accessible on map. </td> </tr>
  <tr> <td> 1.2.5.3 </td> <td> The user’s location shall appear on the map so that users are able to understand what’s happening around them </td> <td> C/C/C </td> <td> </td> </tr>
  <tr> <td> 1.2.5.4 </td> <td> The map shall show the locations that are filtered. </td> <td>C/C/C </td> <td> </td> </tr>
  <tr> <td> 1.2.6.1 </td> <td> The system shall use the W3C Geolocation API standard for implementing location-related information. </td> <td>C/C/C </td> <td> </td> </tr>
  <tr> <td> 1.2.6.2 </td> <td> The system shall provide all kinds of search functionality (e.g., search with filters, sort by date) for models. </td> <td> C/C/C </td> <td> </td> </tr>
  <tr> <td> 1.2.6.3 </td> <td> Users should retrieve results that are semantically similar to their queries. </td> <td>C/C/C </td> <td> </td> </tr>
  <tr> <td> 1.2.6.4 </td> <td> Users should be able to annotate different models, and annotations should comply with W3C Web Annotation Data Model. </td> <td>C/NC/50% </td> <td> </td> </tr>
  <tr> <td> 1.2.7.1 </td> <td> The system shall create in-app and push notifications. </td> <td>NC/NC/NC </td> <td> </td> </tr>
  <tr> <td> 1.2.7.2 </td> <td> The system should create notifications, if users click the “want to be notified” button on the activity. </td> <td> NC/NC/NC </td> <td> </td> </tr>
  <tr> <td> 1.2.7.3 </td> <td> The system should create notifications if users' profession might be implying that they can be a resource to a certain need.  </td> <td>NC/NC/NC </td> <td> </td> </tr>
  <tr> <td> 1.2.7.3.1 </td> <td> The system should be able to add (resource | translator | any topic) to notification settings as default when the user has a role. </td> <td> NC/NC/NC </td> <td> </td> </tr>
  <tr> <td> 1.2.7.4 </td> <td> The system should create notifications if an event takes place near users' addresses when they provide their addresses.  </td> <td>NC/NC/NC </td> <td> </td> </tr>
  <tr> <td> 1.2.7.4.1 </td> <td> The system should be able to add event | any | (user's city) topic to notification settings as default. </td> <td> NC/NC/NC</td> <td> Notification is not implemented. </td> </tr>
  <tr> <td> 1.2.8.1 </td> <td> The system shall collect users' reports about other users in the admin dashboard. </td> <td> C/C/NC </td> <td> </td> </tr>
  <tr> <td> 1.2.8.2 </td> <td> The system shall collect users' reports about activities and events in the admin dashboard. </td> <td>C/C/? </td> <td>Mobile does not include admin dashboard. </td> </tr>
  <tr> <td> 1.2.8.3 </td> <td> The system shall sort the reports according to the number of reports. </td> <td>C/C/NC </td> <td> </td> </tr>
  <tr> <td> 1.2.8.4 </td> <td> The system shall hold a list of restricted users and their phone numbers. </td> <td> NC/NC/NC</td> <td> </td> </tr>
  <tr> <td> 1.2.8.5 </td> <td> The system shall track the restricted users by their phone number in order to prevent new signs up. </td> <td>NC/NC/NC </td> <td> </td> </tr>
  <tr> <td> 1.2.8.6 </td> <td> The system shall hold the admins' logs. </td> <td> NC/NC/NC </td> <td> </td> </tr>
  <tr> <td> 1.2.9.1 </td> <td> The system shall support remember me feature.  </td> <td>C/C/C </td> <td> </td> </tr>
  <tr> <td> 1.2.9.2 </td> <td> The system shall support keep me logged in feature.  </td> <td>C/C/C </td> <td> </td> </tr>

</table>

## API

The API call templates are published on postman on:
[https://www.postman.com/bgmrsln/workspace/darp-apis/collection/27281924-62dac0e3-35d0-41ee-b87f-57a77de35036?action=share&creator=27281924](https://www.postman.com/bgmrsln/workspace/darp-apis/collection/27281924-62dac0e3-35d0-41ee-b87f-57a77de35036?action=share&creator=27281924)
Some sample calls with responses:

### Login

```
curl -X 'POST' \
  'http://3.218.226.215:8000/api/users/login' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "username_or_email_or_phone": "begu2323222m",
  "password": "a2345678"
}'
```

response:

```
{
  "ErrorMessage": "Login failed",
  "ErrorDetail": "Incorrect username or password"
}
```

With OK:

```
{
    "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJiZWd1bSIsImV4cCI6MTcwMzk1MTE3Mn0.Pe5kIGHlp-HJYX52wXWtoVnEEQ_NaeTa4olclnov7zY",
    "token_type": "Bearer",
    "user_role": "ADMIN",
    "proficiency": {
        "proficiency": null,
        "details": null
    }
}
```

### Resource declaration:

The data:

```
{
    "initialQuantity": "100",
    "open_address": "ankara",
    "occur_at": "2023-12-26",
    "details": {
        "description": "her gün yemek dağıtımı gerçekleşecektir",
        "expiration_date": "2023-12-31",
        "allergens": "wheat",
        "subtype": "warm food"
    },
    "type": "food",
    "currentQuantity": "100",
    "x": 39.9333635,
    "y": 32.8597419
}
```

response:

```
{
    "resources": [
        {
            "_id": "658f22494a8ea49101f58d81"
        }
    ]
}
```

### Emergency

A sample for GET function:

```
{
    "emergency_types": "Disaster",
}
```

response:

```
{
    "emergencies": [
        {
            "created_by_user": "ANONYMOUS",
            "contact_name": "Mehmet ",
            "contact_number": "05345769564",
            "created_at": "2023-12-29 21:33:55",
            "last_updated_at": null,
            "emergency_type": "Debris",
            "description": "I am under the debris, we are three people",
            "upvote": 0,
            "downvote": 0,
            "x": 41.09987070771009,
            "y": 28.776981867424382,
            "location": "41.10, 28.78",
            "is_active": true,
            "is_verified": false,
            "verification_note": null,
            "_id": "658f11134a8ea49101f58d7d",
            "reliability": 0.5
        },
        {
            "created_by_user": "ccahid",
            "contact_name": "Cahid Enes Keleş",
            "contact_number": "05340623847",
            "created_at": "2023-12-29 18:21:27",
            "last_updated_at": null,
            "emergency_type": "Fire",
            "description": "There is a fire",
            "upvote": 0,
            "downvote": 0,
            "x": 40.94825749098813,
            "y": 29.60209053802265,
            "location": "Kargalı Mahallesi, Gebze, Kocaeli, Marmara Region, Turkey",
            "is_active": true,
            "is_verified": false,
            "verification_note": null,
            "_id": "658ee3f64a8ea49101f58d6b",
            "reliability": 0.5
        },
        {
            "created_by_user": "ANONYMOUS",
            "contact_name": "Cahid Enes Keleş",
            "contact_number": "05340623847",
            "created_at": "2023-12-29 17:12:02",
            "last_updated_at": null,
            "emergency_type": "Medical Emergency",
            "description": "I have a serious heart attack",
            "upvote": 0,
            "downvote": 0,
            "x": 40.91597798698653,
            "y": 29.291350253321525,
            "location": "Vekil Sokak, Kurtköy Mahallesi, Pendik, Istanbul, Marmara Region, 34912, Turkey",
            "is_active": true,
            "is_verified": false,
            "verification_note": null,
            "_id": "658ed3b24a8ea49101f58d68",
            "reliability": 0.5
        },
        {
            "created_by_user": "ANONYMOUS",
            "contact_name": "begüm",
            "contact_number": "05674568798",
            "created_at": "2023-12-29 15:22:31",
            "last_updated_at": null,
            "emergency_type": "Debris",
            "description": "I am under debris",
            "upvote": 0,
            "downvote": 0,
            "x": 28.949014434507156,
            "y": 40.163132874122084,
            "location": "Ihlas yuvacık sitesi, 29 ekim mahallesi nilüfer bursa",
            "is_active": true,
            "is_verified": false,
            "verification_note": null,
            "_id": "658ee4374a8ea49101f58d6c",
            "reliability": 0.5
        },
        {
            "created_by_user": "ANONYMOUS",
            "contact_name": "Cahid Enes Keleş",
            "contact_number": "05305001007",
            "created_at": "2023-12-26 17:52:09",
            "last_updated_at": null,
            "emergency_type": "Debris",
            "description": "help me",
            "upvote": 0,
            "downvote": 0,
            "x": 41.06860277217137,
            "y": 28.920195148820994,
            "location": "41.07, 28.92",
            "is_active": true,
            "is_verified": false,
            "verification_note": null,
            "_id": "658ae89a5c04082b7ed87675",
            "reliability": 0.5
        },
        {
            "created_by_user": "ANONYMOUS",
            "contact_name": "Cahid Enes Keleş",
            "contact_number": "05340623847",
            "created_at": "2023-12-26 17:00:50",
            "last_updated_at": null,
            "emergency_type": "Debris",
            "description": "Enkaz altında kaldık 3 kişiyiz",
            "upvote": 0,
            "downvote": 0,
            "x": 0.0,
            "y": 0.0,
            "location": "Hisarüstü",
            "is_active": true,
            "is_verified": false,
            "verification_note": null,
            "_id": "658adc935c04082b7ed875f6",
            "reliability": 0.5
        },
        {
            "created_by_user": "ANONYMOUS",
            "contact_name": "Cahid Enes Keleş",
            "contact_number": "05340623847",
            "created_at": "2023-12-26 16:29:09",
            "last_updated_at": null,
            "emergency_type": "Debris",
            "description": "I am under debris, help me!",
            "upvote": 0,
            "downvote": 0,
            "x": 40.97963714612506,
            "y": 29.30552520213999,
            "location": "40.98, 29.31",
            "is_active": true,
            "is_verified": false,
            "verification_note": null,
            "_id": "658ad527580c078a11731795",
            "reliability": 0.5
        },
        {
            "created_by_user": "buse",
            "contact_name": "aziza ",
            "contact_number": "05522633465",
            "created_at": "2023-12-26 16:23:47",
            "last_updated_at": null,
            "emergency_type": "Debris",
            "description": "she is under debris",
            "upvote": 0,
            "downvote": 0,
            "x": 41.08610423828388,
            "y": 29.043956681932258,
            "location": "Bilgisayar Mühendisliği Binası, Bebek Yolu Sokağı, Etiler Mahallesi, Rumelihisarı Mahallesi, Sarıyer, Istanbul, Marmara Region, 34470, Turkey",
            "is_active": true,
            "is_verified": false,
            "verification_note": null,
            "_id": "658ad3e48646f06ceee47214",
            "reliability": 0.5
        },
        {
            "created_by_user": "ibrahim",
            "contact_name": "halil",
            "contact_number": "5554443322",
            "created_at": "2023-12-26 13:41:15",
            "last_updated_at": null,
            "emergency_type": "Fire",
            "description": "Binada yangın çıktı acil yardım",
            "upvote": 0,
            "downvote": 0,
            "x": 41.085941273162895,
            "y": 29.043998407312273,
            "location": "41,09, 29,04",
            "is_active": true,
            "is_verified": false,
            "verification_note": null,
            "_id": "658aadcce5551971f87aae6b",
            "reliability": 0.5
        },
        {
            "created_by_user": "amdin_can",
            "contact_name": null,
            "contact_number": null,
            "created_at": "2023-12-26 11:42:36",
            "last_updated_at": null,
            "emergency_type": "Fire",
            "description": "Nispetiye istasyonunun karşısındaki binada yangın var!",
            "upvote": 0,
            "downvote": 0,
            "x": 29.02417188044138,
            "y": 41.077546017046046,
            "location": "Nispetiye Metro İstasyonu / İstanbul",
            "is_active": true,
            "is_verified": false,
            "verification_note": null,
            "_id": "658abc2c9e7a26dbbc53de99",
            "reliability": 0.5
        },
        {
            "created_by_user": "ANONYMOUS",
            "contact_name": "Mehmet Kuzulugil",
            "contact_number": "05553334455",
            "created_at": "2023-12-26 10:27:44",
            "last_updated_at": null,
            "emergency_type": "Debris",
            "description": "Sokakta yıkım çalışmaları sırasında beklenmeyen olay oldu. Enkaz altında kalanlar var.",
            "upvote": 0,
            "downvote": 0,
            "x": 29.043474,
            "y": 41.08714,
            "location": "İhlas Sokak No:3",
            "is_active": true,
            "is_verified": false,
            "verification_note": null,
            "_id": "658aaaa0e5551971f87aae65",
            "reliability": 0.5
        },
        {
            "created_by_user": "mervelerle",
            "contact_name": "merve gurbuz",
            "contact_number": "5511513629",
            "created_at": "2023-12-26 10:26:34",
            "last_updated_at": null,
            "emergency_type": "News",
            "description": "Kedim enkaz altinda kaldi",
            "upvote": 0,
            "downvote": 0,
            "x": 29.043474,
            "y": 41.08714,
            "location": "hisarustu mah 1.sokak Istanbul",
            "is_active": true,
            "is_verified": false,
            "verification_note": null,
            "_id": "658aaa5ae5551971f87aae64",
            "reliability": 0.5
        }
    ]
}
```

## User Interface / User Experience

User Interface designs and the links to the source code in the project repository.

- Feature: Different colors for each type of activities are used for differentiating different type of activities easily. [Here](https://github.com/bounswe/bounswe2023group2/wiki/FE-MO-Common-Resources) is the shared color codes.
  - Web:
  - Mobile: [link](https://github.com/bounswe/bounswe2023group2/blob/a024067dbf77b86aab488c8555246b5c0b03db7b/Disaster-Response-Platform/mobile/DisasterResponsePlatform/app/src/main/res/values/colors.xml#L7)
- Feature: Light and dark themes
  - Mobile: [light theme values](https://github.com/bounswe/bounswe2023group2/tree/a024067dbf77b86aab488c8555246b5c0b03db7b/Disaster-Response-Platform/mobile/DisasterResponsePlatform/app/src/main/res/values), [dark theme values](https://github.com/bounswe/bounswe2023group2/tree/a024067dbf77b86aab488c8555246b5c0b03db7b/Disaster-Response-Platform/mobile/DisasterResponsePlatform/app/src/main/res/values-night)
- Feature: Turkish and English language supports
  - Mobile: [Turkish translations file](https://github.com/bounswe/bounswe2023group2/blob/a024067dbf77b86aab488c8555246b5c0b03db7b/Disaster-Response-Platform/mobile/DisasterResponsePlatform/app/src/main/res/values-tr/strings.xml)
  - Web:
- Feature: General UI/UX
  - Mobile: We used material theme to make the app look awesome. [Line where material library is imported](https://github.com/bounswe/bounswe2023group2/blob/a024067dbf77b86aab488c8555246b5c0b03db7b/Disaster-Response-Platform/mobile/DisasterResponsePlatform/app/build.gradle#L49)
- Feature: Activities of credible users stand out so that they are more apparent.
  - Mobile: [PR for this feature](https://github.com/bounswe/bounswe2023group2/pull/885)

**Some screenshots for UI/UX:**

[Dark theme support](https://github.com/bounswe/bounswe2023group2/assets/12631296/50d5ccd6-318d-4af4-8f98-e2670f826e3c)

[User friendly selection tools](https://github.com/bounswe/bounswe2023group2/assets/12631296/3587bac0-1574-48ee-8d07-6b07633ca394)

## Annotations

- Status:
  - An independent server application exposing annotation functionality via RESTfil API - Running and online
  - A simple helper function on Backend using the Annoserver APIs to annotate activities of DAPP system - Available on backend
- Compliance with W3C WADM:
  - Annoserver API calls have full compliance with W3C WADM standard documents.
- Implementation description
  Our annotation server exposes RESTful API functionality imposing the WADM standards for calling bodies.
  On the other hand annotation functianality is implemented by the system using a helper function on regular backend side. The Annotation server providing the API functionaltiy to annotate, text, image and data, our application is for now limited to annotate the system activities with text and image bodies. - Data store: An external annotation service (hypothes.is) is used as rather a data store. The implementation of annotating data entries (activities in DAPP) is independent of this data store.

- API calls examples to annotation server:
  - Annotating **data**: creation & retrieval

```
curl --location 'http://3.218.226.215:18000/api/annotations' \
--header 'Content-Type: application/json' \
--data '{
    "id":"RP-5zp9xEe6fpY91XSNgdA",
    "uri":"http://3.218.226.215:18000/resources/annotations/65776cc6d80e934140000b",
    "tags": [
        "api", "DAPP","rescue","resource"
    ],
    "document": {
        "title": "DAPP-Anno resourcetest new",
         "link": [{"href":"http://3.218.226.215:18000/resources/annotations/65776cc6d80e934140000b"}]
    },
    "target":[
        {"type":"Data", "selector": [{"type":"DataSelector","value":"https://dapp.kisa.net.tr/resources/annotations/65776cc6d80e934140000b"}],
        "source":"http://3.218.226.215:18000/resources/annotations/65776cc6d80e934140000b"}
        ],
    "body":{"type":"Text", "value":"Kaynağın verdiği bilgi ve önerisi: Sağladığı yemeklerin [PSAKD binasından](https://maps.app.goo.gl/hq6PxMbf4N8SWNTV7) yüklenebileceğini, yakın yerlerde olursa taşımayı kendisinin yapabileceğini söylüyor. [Fotoğrafını görebilirsiniz](https://zaytung.com/voicepics//Ahmad.jpg) "},
    "motivation": {"type":"bookmarking"}
}'
```

- image annotation creation & retrieval:

```
{
    "id":"RP-5zp9xEe6fpY91XSNgdA",
    "uri":"http://3.218.226.215:3000/resources/annotations/65776cc6d80e934140000b",
    "tags": [
        "api", "DAPP","rescue","resource"
    ],
    "document": {
        "title": "DAPP-Anno resourcetest new",
         "link": [{"href":"http://3.218.226.215:3000/resources/annotations/65776cc6d80e934140000b"}]
    },
    "target": [{
        "id": "http://3.218.226.215:3000/image1#xywh=100,100,300,300",
        "url": "http://3.218.226.215:3000/image1#xywh=100,100,300,300",
        "type": "Image",
        "id": "http://3.218.226.215:3000/image1#xywh=100,100,300,300",
        "selector": [{
      "type": "FragmentSelector",
      "conformsTo": "http://www.w3.org/TR/media-frags/",
      "value": "t=30,60"
    }],
        "format": "image/jpeg"
        }
    ],
    "body":{"type":"Text", "value":"Yollanan görselin gerçekliği tartışmalı görünüyor."},
    "motivation": {"type":"bookmarking"}
}

```

### Helper function example call

```
curl -X 'POST' \
  'http://3.218.226.215:8000/api/simple_annotation/?url=http%3A%2F%2F3.218.226.215%3A18000%2Fresources%2Fannotations%2F65776cc6d80e934140000b&annotation_text=D%C3%BCzenli%20yemek%20%C3%A7%C4%B1kartmak%20i%C3%A7in%20ba%C4%9Flant%C4%B1%20ar%C4%B1yorlar.%20Bu%20konuyla%20sahadan%20birisinin%20ilgilenmesi%20gerekir.&tags=Resource%2Cyemek%2C%20tabldot%2C%20yard%C4%B1m&imageUrl=https%3A%2F%2Fzaytung.com%2Fvoicepics%2FAhmad.jpg' \
  -H 'accept: application/json' \
  -d ''
```

## Scenario

The demonstration will follow the user story of Mehmet(victim), Ayşe(provider), Fatma(admin) simulating a disaster envinronment.

Mehmet is in an emergency situation. He needs immediate help and he has app installed but never opened it nor registered. He creates an emergency as a guest user, however due to connectivity issues his emergency couldn’t post. However soon as the connectivity is on, the post will be posted so Mehmet doesn’t need to fill the emergency informations again and again. Some people in the same area encountered the emergency in their feed and upvoted the post so the post become more visible and rescue team has been redirected to this point.

Ayşe is a manager in a cloth factory. She wants to supply new clothes by checking what is required in earthquake area. She picks the earthquake location and filter the needs around specified frame. Then she search with a text “cloth” and she observes that socks is very much needed in this case. She creates a resource activity and fills the amount, type and location and details if she want to give extra information.

Fatma is an admin and she opens up the web and logs in with her credentials. She can see Kızılay needs a 100 pair of socks. Fatma creates an action by mapping Ayşe’s 100 pair of socks as a resource to the Kızılay’s recurring need that repeats in daily intervals. Fatma also checks the reported activities and users. After creation actions she decides to check reports page. There is a created event that suggest the electricity is gone in certain area, however this event is incorrect and received so many downvotes and reports. She realizes that there are some people with bad intensions. She decides to demote the person who posted fake event.

## Research

### Annotation: Standards and implementation

The documents about WADM standard by W3C and related work have been studied as planned in Milestone 2.
The research was documented so that the group had a common knowledge and sense of the annotation use.
The research then took us to practical issues.
Annotation standards and use of an independent Annotation Server element have been well understood.
One drawback about the annotation research was, it could not be possible for the team to design a good use of annotation concept for the software as a whole.

### Semantic Search

In our project's final milestone, we enhanced our search functionality by integrating semantic search. This approach uses a specialized model on a separate server to understand the relevance between search terms, focusing on context rather than just keywords. Initially, our system performs a regex-based search, and if no results are found, it switches to semantic search for more contextually relevant findings. We also experimented with adding a translation feature to handle multiple languages, but due to its associated costs, we've not included it in the current version. Overall, this development marks a significant advancement from basic keyword searches to a more sophisticated, context-aware search experience

# Project Artifacts

---

## Manuals

---

## User Manual

### Mobile

#### Sign up

You can sign up our application with these credentials that are shown below.
![Screenshot 2023-12-29 at 22.40.57](https://hackmd.io/_uploads/rk2RxinDa.png)

#### Verify Account

After you signed up, verification code should be send to your email address (hopefully if Burak's account doesn't locked)
![Screenshot 2023-12-29 at 23.32.04](https://hackmd.io/_uploads/SJH02jhwa.png)

#### Send Reset Code

If you want to reset your password, you should enter your email to reset it
![Screenshot 2023-12-29 at 22.40.25](https://hackmd.io/_uploads/SJrhli3wT.png)

#### Reset Password

After you got the verification code, you can change your password
![Screenshot 2023-12-29 at 23.30.30](https://hackmd.io/_uploads/r1PO2onw6.png)

#### Login page

You can login with your username or email and password
![Screenshot 2023-12-29 at 22.36.43](https://hackmd.io/_uploads/S1cA1inDp.png)

#### Main Page (Emergencies)

You can see emergencies from main page
![Screenshot 2023-12-29 at 22.43.36](https://hackmd.io/_uploads/r13OZohPT.png)

#### Main Page (Needs)

You can see needs from main page by changing the tab
![Screenshot 2023-12-29 at 22.44.10](https://hackmd.io/_uploads/H1v5-shvp.png)

#### Main Page (Resources)

You can see resources from main page by changing the tab
![Screenshot 2023-12-29 at 22.44.34](https://hackmd.io/_uploads/HJW3Zj3PT.png)

#### Main Page (Events)

You can see events from main page by changing the tab
![Screenshot 2023-12-29 at 22.45.06](https://hackmd.io/_uploads/H1ZAZo2Pp.png)

#### Main Page (Actions)

You can see actions from main page by changing the tab
![Screenshot 2023-12-29 at 22.45.27](https://hackmd.io/_uploads/SkIyGo2Dp.png)

#### Create Emergency

You can create emergency (you can do it even you are guest) by clicking the + on the emergency page
![Screenshot 2023-12-29 at 22.47.11](https://hackmd.io/_uploads/SkoBGinPp.png)

#### Create Need

You can create need (you can do it if you verify your email) by clicking the + on the need page
![Screenshot 2023-12-29 at 22.52.42](https://hackmd.io/_uploads/ByLomshw6.png)
![Screenshot 2023-12-29 at 22.53.42](https://hackmd.io/_uploads/SkfAXj3DT.png)

#### Selecting Location From Map

You can select the location while creating activities.
![Screenshot 2023-12-29 at 22.57.07](https://hackmd.io/_uploads/HJLj4o3Pp.png)

#### Dynamic Spinners

![Screenshot 2023-12-29 at 22.48.46](https://hackmd.io/_uploads/S15ozonP6.png)

#### Date, Time Picker

![Screenshot 2023-12-29 at 22.49.49](https://hackmd.io/_uploads/rJtyQs2D6.png)
![Screenshot 2023-12-29 at 22.56.41](https://hackmd.io/_uploads/BJUYEsnPa.png)

#### Dynamic Recurrence Section

![Screenshot 2023-12-29 at 22.54.04](https://hackmd.io/_uploads/B1Dk4ihPp.png)
![Screenshot 2023-12-29 at 22.54.14](https://hackmd.io/_uploads/Hk-g4i3wa.png)

#### Create Resource

You can create resources (you can do it if you verify your email) by clicking the + on the resources page
![Screenshot 2023-12-29 at 22.55.50](https://hackmd.io/_uploads/S1fUEj3Pp.png)

#### Create Event

You can create events (you can do it if you verify your email) by clicking the + on the events page
![Screenshot 2023-12-29 at 22.56.08](https://hackmd.io/_uploads/HJ8v4o3D6.png)

#### Create Action (not worked properly)

You can create actions (you can do it if you verify your email) by clicking the + on the actions page (But we don't recommend mobile since it's not working properly)
![Screenshot 2023-12-29 at 22.57.45](https://hackmd.io/_uploads/BJBaVs2P6.png)

#### Emergency Details Page

You can see the emergencies details by clicking on the selected list item
![Screenshot 2023-12-29 at 23.00.41](https://hackmd.io/_uploads/rJvtSo3PT.png)
![Screenshot 2023-12-29 at 23.01.17](https://hackmd.io/_uploads/ByqcrjhD6.png)

#### Show Profile (on Details Page)

You can see the profile of creators of the activity by clicking see profile (if the creator's profile is visible to other users)
![Screenshot 2023-12-29 at 23.02.07](https://hackmd.io/_uploads/ByjTBinwT.png)

#### Upvote/Downvote and Report

Authenticated users can upvote downvote and report activities
![Screenshot 2023-12-29 at 23.02.33](https://hackmd.io/_uploads/HkHJIj3va.png)
![Screenshot 2023-12-29 at 23.03.03](https://hackmd.io/_uploads/B1V-Ij2DT.png)
![Screenshot 2023-12-29 at 23.03.51](https://hackmd.io/_uploads/SyfV8inPT.png)

#### Need Details Page

You can see the needs details by clicking on the selected list item
![Screenshot 2023-12-29 at 23.04.17](https://hackmd.io/_uploads/rJ188inwp.png)
![Screenshot 2023-12-29 at 23.04.27](https://hackmd.io/_uploads/BJvLUjhw6.png)

#### Resource Details Page

You can see the resources details by clicking on the selected list item
![Screenshot 2023-12-29 at 23.04.52](https://hackmd.io/_uploads/ByfOIs2wp.png)
![Screenshot 2023-12-29 at 23.05.32](https://hackmd.io/_uploads/By9qIihDa.png)

#### Event Details Page

You can see the needs details by clicking on the selected list item
![Screenshot 2023-12-29 at 23.06.14](https://hackmd.io/_uploads/B1MaUohwT.png)

#### Action Details Page

You can see the action details by clicking on the selected list item
![Screenshot 2023-12-29 at 23.06.33](https://hackmd.io/_uploads/SkE0Lo2Dp.png)

#### When Creator Looks details of created activity

Creators of activities can delete and edit their activities
![Screenshot 2023-12-29 at 23.07.25](https://hackmd.io/_uploads/r1tbws2wT.png)

#### Edit Page (Filling previous information version of create activity pages)

![Screenshot 2023-12-29 at 23.10.40](https://hackmd.io/_uploads/r1n6wj3w6.png)

#### Search

Users can search the activities semanticly ![Screenshot 2023-12-29 at 23.11.49](https://hackmd.io/_uploads/r1rzuinwa.png)

#### Filter and Sort

Users can filter and sort activities ![Screenshot 2023-12-29 at 23.12.41](https://hackmd.io/_uploads/BJHHdo2Pa.png)

#### Dynamically Selecting Types

![Screenshot 2023-12-29 at 23.13.21](https://hackmd.io/_uploads/rJg_donwp.png)
![Screenshot 2023-12-29 at 23.13.39](https://hackmd.io/_uploads/HkJF_ohwa.png)

#### Filter by Location

![Screenshot 2023-12-29 at 23.14.00](https://hackmd.io/_uploads/HJXcdonDa.png)
![Screenshot 2023-12-29 at 23.15.04](https://hackmd.io/_uploads/r17kKs2D6.png)

#### Map

You can view the map and see details of activities
![Screenshot 2023-12-29 at 23.15.45](https://hackmd.io/_uploads/H1ReKjhw6.png)
![Screenshot 2023-12-29 at 23.16.05](https://hackmd.io/_uploads/BkZfYj3wp.png)
![Screenshot 2023-12-29 at 23.16.44](https://hackmd.io/_uploads/ByqEtj3Pa.png)

#### Profile

![Screenshot 2023-12-29 at 23.36.39](https://hackmd.io/_uploads/ryDyCshD6.png)
![Uploading file..._ifubykaas]()

#### Edit Profile

You can edit your profile
![Screenshot 2023-12-29 at 23.34.19](https://hackmd.io/_uploads/BkAITs2Da.png)
![Screenshot 2023-12-29 at 23.34.54](https://hackmd.io/_uploads/rya_6s2v6.png)
![Screenshot 2023-12-29 at 23.35.16](https://hackmd.io/_uploads/BJbcponPa.png)

### System Manual

#### Running Docker Compose Instructions

The backend and frontend code resides under the folder `Disaster-Response-Platform`
The definitions for docker images are in `docker-compose.yaml` So under the root folder (for the code) `Disaster-Response-Platform` we run the docker compose commands. Which are:

`docker compose build` -> Build all images which are as follows:
`docker compose build backend`
`docker compose build annoserver`
`docker compose build frontend`

The docker compose file also introduces a `mongo` container and defines the inner network with the exposed ports.
The docker containers for backend, annoserver and frontend may also be taken up individually using commands like:
`docker compose up mongo -d `
`docker compose up backend -d `
`docker compose up annoserver -d `
`docker compose up frontend -d `
The docker files for each docker image/container resides on the related folder:
`backend/Dockerfile`
`frontend/Dockerfile`

### Mobile

#### Installing with a Pre-made APK

1. Install the shared APK on your Android device.
2. Once the download is complete, click open file. The device will navigate you to installation
3. If you get a "potentially harmful" warning, you can ignore it.
4. Complete installation and the app will be installed just like all other apps.

#### Running it on Android Emulator

1. Install Android Studio if you don't have it
2. Clone our project's [git url](https://github.com/bounswe/bounswe2023group2.git)
3. In Android Studio, you should create a new project from version control (File > New > Project from Version Control) with that url
4. After Android Studio fetch the file from git successfully, you should open the mobile-related section to run the code
5. You can do this by the following File > open, after that you should find the path of project
6. Then you should Select: bounswe2023group2 > Disaster-Response-Platform > mobile > DisasterResponsePlatform ![Screenshot 2023-12-29 at 21.33.17](https://hackmd.io/_uploads/HyAe-q2wa.png)
7. After you selected that, Android Studio will open the new window with Android Scope, and you can run it from emulator or connecting Android Device (if it's in usb debugging mode)

# Software Packages

The final release version, identified as Release Name: 0.9.0 can be found in Github [link](https://github.com/bounswe/bounswe2023group2/releases/tag/final-submission-g2).
Docker installation and usage guide is added in the wiki page for teammates and curious users and can be found [here](https://github.com/bounswe/bounswe2023group2/wiki/Docker-and-local-deployment-tutorial).
Apart from this tutorials. We provided backend tutorial in the [repo](https://github.com/bounswe/bounswe2023group2/tree/main/Disaster-Response-Platform/backend). Each server and software packages under the Disaster Response Platform folder is dockerized and easy to use.

# Test Reports

```
============================= test session starts ==============================
collecting ... collected 10 items

emergency_test.py::test_signup PASSED                                    [ 10%]
emergency_test.py::test_create_emergency1 PASSED                         [ 20%]
emergency_test.py::test_login PASSED                                     [ 30%]
emergency_test.py::test_create_emergency2 PASSED                         [ 40%]
emergency_test.py::test_get_emergency1 PASSED                            [ 50%]
emergency_test.py::test_get_emergency2 PASSED                            [ 60%]
emergency_test.py::test_get_all_emergencies PASSED                       [ 70%]
emergency_test.py::test_update_emergency PASSED                          [ 80%]
emergency_test.py::test_delete_emergency1 PASSED                         [ 90%]
emergency_test.py::test_delete_emergency2 PASSED                         [100%]

======================== 10 passed, 1 warning in 1.16s =========================

Process finished with exit code 0
```

```
============================= test session starts ==============================
collecting ... collected 9 items

feedback_test.py::test_signup PASSED                                     [ 11%]
feedback_test.py::test_login PASSED                                      [ 22%]
feedback_test.py::test_create_need PASSED                                [ 33%]
feedback_test.py::test_upvote_need PASSED                                [ 44%]
feedback_test.py::test_upvote_need_again PASSED                          [ 55%]
feedback_test.py::test_downvote_need PASSED                              [ 66%]
feedback_test.py::test_downvote_need_again PASSED                        [ 77%]
feedback_test.py::test_unvote_need PASSED                                [ 88%]
feedback_test.py::test_unvote_need_again PASSED                          [100%]

========================= 9 passed, 1 warning in 1.20s =========================

Process finished with exit code 0
```

```
============================= test session starts ==============================
collecting ... collected 16 items

need_test.py::test_signup PASSED                                         [  6%]
need_test.py::test_login PASSED                                          [ 12%]
need_test.py::test_create_need1 PASSED                                   [ 18%]
need_test.py::test_create_need2 PASSED                                   [ 25%]
need_test.py::test_get_need1 PASSED                                      [ 31%]
need_test.py::test_get_need2 PASSED                                      [ 37%]
need_test.py::test_get_all_needs PASSED                                  [ 43%]
need_test.py::test_update_need PASSED                                    [ 50%]
need_test.py::test_set_initial_quantity PASSED                           [ 56%]
need_test.py::test_get_initial_quantity PASSED                           [ 62%]
need_test.py::test_set_unsupplied_quantity PASSED                        [ 68%]
need_test.py::test_get_unsupplied_quantity PASSED                        [ 75%]
need_test.py::test_set_urgency PASSED                                    [ 81%]
need_test.py::test_get_urgency PASSED                                    [ 87%]
need_test.py::test_delete_need1 PASSED                                   [ 93%]
need_test.py::test_delete_need2 PASSED                                   [100%]

======================== 16 passed, 1 warning in 1.21s =========================

Process finished with exit code 0
```

```
============================= test session starts ==============================
collecting ... collected 12 items

report_test.py::test_signup PASSED                                       [  8%]
report_test.py::test_login PASSED                                        [ 16%]
report_test.py::test_create_need1 PASSED                                 [ 25%]
report_test.py::test_create_report1 PASSED                               [ 33%]
report_test.py::test_create_report2 PASSED                               [ 41%]
report_test.py::test_make_admin PASSED                                   [ 50%]
report_test.py::test_get_report1 PASSED                                  [ 58%]
report_test.py::test_get_report2 PASSED                                  [ 66%]
report_test.py::test_get_all_reports PASSED                              [ 75%]
report_test.py::test_update_report PASSED                                [ 83%]
report_test.py::test_delete_report1 PASSED                               [ 91%]
report_test.py::test_delete_report2 PASSED                               [100%]

======================== 12 passed, 1 warning in 1.25s =========================

Process finished with exit code 0

```

# Individual Reports

## Member: Name Surname

### Responsibilities:

### Main contributions:

- **Code-related significant issues:**

  - [Issue name (#xyz)](https://github.com/bounswe/bounswe2023group2/issues/xyz)

- **Management-related significant issues**:

### Pull Requests:

- **Created:**

  - [Pull request description (PR #xyz)](https://github.com/bounswe/bounswe2023group2/pull/xyz)

- **Reviewed:**

### Additional Information:

---

## Member: Cahid Enes Keleş

### Responsibilities:

I was mainly responsible for profile page, annotation, a part of map and a little bit of UI for the mobile team.

### Main contributions After MS2:

- **Code-related significant issues:**

  - [Show User From Activity #673](https://github.com/bounswe/bounswe2023group2/issues/673)
  - [Choose a Role #680](https://github.com/bounswe/bounswe2023group2/issues/680)
  - [Special Activity of Credible User #731](https://github.com/bounswe/bounswe2023group2/issues/731)
  - [Profile Picture #732](https://github.com/bounswe/bounswe2023group2/issues/772)
  - [Password Reset #773](https://github.com/bounswe/bounswe2023group2/issues/773)
  - [Delete Account #774](https://github.com/bounswe/bounswe2023group2/issues/774)
  - [Upload Document #777](https://github.com/bounswe/bounswe2023group2/issues/777)
  - [Annotations #903](https://github.com/bounswe/bounswe2023group2/issues/903)
  - [Map for Event and Emergency #916](https://github.com/bounswe/bounswe2023group2/issues/916)

- **Management-related significant issues**:

### Pull Requests After MS2:

- **Created:**

  - [MO/feature/show-user #702](https://github.com/bounswe/bounswe2023group2/pull/702)
  - [MO/feature/role-based-credible-profile #717](https://github.com/bounswe/bounswe2023group2/pull/717)
  - [MO/feature/profile-picture #856](https://github.com/bounswe/bounswe2023group2/pull/856)
  - [MO/feature/password-reset #863](https://github.com/bounswe/bounswe2023group2/pull/863)
  - [MO/feature/upload-document #876](https://github.com/bounswe/bounswe2023group2/pull/876)
  - [MO/feature/show-proficiency #882](https://github.com/bounswe/bounswe2023group2/pull/882)
  - [MO/feature/special-activity-credible #885](https://github.com/bounswe/bounswe2023group2/pull/885)
  - [MO/feature/delete-account #887](https://github.com/bounswe/bounswe2023group2/pull/887)
  - [MO/advancement/string-translation #893](https://github.com/bounswe/bounswe2023group2/pull/893)
  - [MO/bugfix/duplicate-bugfix #896](https://github.com/bounswe/bounswe2023group2/pull/896)
  - [MO/feature/annotations #900](https://github.com/bounswe/bounswe2023group2/pull/900)
  - [MO/bugfix/special-credible #912](https://github.com/bounswe/bounswe2023group2/pull/912)
  - [MO/bugfix/login-fixes #913](https://github.com/bounswe/bounswe2023group2/pull/913)
  - [MO/feature/map-event-emergency #917](https://github.com/bounswe/bounswe2023group2/pull/917)

- **Reviewed:**
  - [MO/bugfix/icons_fixed #715](https://github.com/bounswe/bounswe2023group2/pull/)
  - [MO/feature/Report-Functionality #762](https://github.com/bounswe/bounswe2023group2/pull/)
  - [MO/feature/email-verification #778](https://github.com/bounswe/bounswe2023group2/pull/)
  - [MO/feature/save added activities #841](https://github.com/bounswe/bounswe2023group2/pull/)
  - [BE/hotfix/profile-picture-set #857](https://github.com/bounswe/bounswe2023group2/pull/)
  - [MO/feature/save added actions #868](https://github.com/bounswe/bounswe2023group2/pull/)
  - [MO/feature/search-implementation #872](https://github.com/bounswe/bounswe2023group2/pull/)
  - [MO/feature/recurrence relations #884](https://github.com/bounswe/bounswe2023group2/pull/)
  - [MO/hotfix/date #930](https://github.com/bounswe/bounswe2023group2/pull/)
  - [MO/hotfix/show-profile #933](https://github.com/bounswe/bounswe2023group2/pull/)

### Additional Information:

I also added annotations to the annotation server for type and subtype names for activities (to use in dictionary purpose)

---

## Member: Egecan Serbester

### Responsibilities:

In general, I contributed to the improvement of the application by following the work on the mobile side and providing feedback to my teammates. I helped to solve the bugs that occurred by reviewing and testing most of the PRs. In addition, I was responsible for organizing the functions and code architecture of the pages about the activities. I was responsible for creating activities without an internet connection and transferring it to the backend when the connection was provided, and creating the changing needs, resources reccursively. I also implemented the pages about Events which we can basically do CRUD operations.

### Main contributions:

- **Code-related significant issues:**

  - [Mobile - CRUD Event Pages](https://github.com/bounswe/bounswe2023group2/issues/539)
  - [Mobile - Post Need & Resource when Internet Connection](https://github.com/bounswe/bounswe2023group2/issues/586)
  - [Mobile - Fix Bugs](https://github.com/bounswe/bounswe2023group2/issues/744)
  - [Mobile - Open Map as Pop-Up](https://github.com/bounswe/bounswe2023group2/issues/746)
  - [Mobile - Revise the App](https://github.com/bounswe/bounswe2023group2/issues/797)
  - [Mobile - Update Form Fields](https://github.com/bounswe/bounswe2023group2/issues/879)
  - [Mobile - New Recurrence Relations](https://github.com/bounswe/bounswe2023group2/issues/869)
  - [Mobile - Fix Spam for user role](https://github.com/bounswe/bounswe2023group2/issues/906)

- **Management-related significant issues**:
  - [Management of Dividing Tasks Inside Teams](https://github.com/bounswe/bounswe2023group2/issues/816)
  - [Updating the Requirements](https://github.com/bounswe/bounswe2023group2/issues/780)

### Pull Requests:

- **Created:**

  - [Mo/feature/crud event](https://github.com/bounswe/bounswe2023group2/pull/734)
  - [MO/bugfix/fix-bugs](https://github.com/bounswe/bounswe2023group2/pull/745)
  - [Mo/feature/save added activities](https://github.com/bounswe/bounswe2023group2/pull/841)
  - [Mo/feature/save added actions (bugfix)](https://github.com/bounswe/bounswe2023group2/pull/868)
  - [Mo/feature/recurrence relations](https://github.com/bounswe/bounswe2023group2/pull/884)
  - [Mo/bug/fix spam user role (very hotfix because it breaks server)](https://github.com/bounswe/bounswe2023group2/pull/907)
  - [#906 Solution](https://github.com/bounswe/bounswe2023group2/pull/908)
  - [MO/feature/resource-recurrent](https://github.com/bounswe/bounswe2023group2/pull/909)
  - [Solve conflicts](https://github.com/bounswe/bounswe2023group2/pull/930)
  - [Hotfix Mobile](https://github.com/bounswe/bounswe2023group2/pull/933)
  - [MO/bugfix/special-credible #912](https://github.com/bounswe/bounswe2023group2/pull/912)
  - [MO/bugfix/login-fixes #913](https://github.com/bounswe/bounswe2023group2/pull/913)
  - [MO/feature/map-event-emergency #917](https://github.com/bounswe/bounswe2023group2/pull/917)

- **Reviewed:**
  - [Mo/feature/resource detail page design](https://github.com/bounswe/bounswe2023group2/pull/710)
  - [Mo/feature/homepage layout improvements](https://github.com/bounswe/bounswe2023group2/pull/741)
  - [Mo/feature/sort filter by location date](https://github.com/bounswe/bounswe2023group2/pull/754)
  - [Mo/feature/emergency](https://github.com/bounswe/bounswe2023group2/pull/825)
  - [Mo/design/upvote downvote design update](https://github.com/bounswe/bounswe2023group2/pull/838)
  - [Mo/feature/profile-picture](https://github.com/bounswe/bounswe2023group2/pull/856)
  - [MO/feature/password-reset](https://github.com/bounswe/bounswe2023group2/pull/863)
  - [MO/feature/search-implementation](https://github.com/bounswe/bounswe2023group2/pull/872)
  - [Mo/feature/upload-document](https://github.com/bounswe/bounswe2023group2/pull/876)
  - [MO/feature/show-proficiency](https://github.com/bounswe/bounswe2023group2/pull/882)
  - [Mo/feature/special-activity-credible](https://github.com/bounswe/bounswe2023group2/pull/885)
  - [Mo/feature/delete-account](https://github.com/bounswe/bounswe2023group2/pull/887)
  - [MO/feature/Auth-Enhancement](https://github.com/bounswe/bounswe2023group2/pull/891)
  - [MO/bugfix/duplicate-bugfix](https://github.com/bounswe/bounswe2023group2/pull/896)
  - [Mo/feature/annotations](https://github.com/bounswe/bounswe2023group2/pull/900)
  - [MO/bugfix/special-credible](https://github.com/bounswe/bounswe2023group2/pull/912)
  - [Mo/bugfix/login-fixes](https://github.com/bounswe/bounswe2023group2/pull/913)
  - [Mo/feature/map-event-emergency](https://github.com/bounswe/bounswe2023group2/pull/917)
  - [Mo/feature/connect emergency to backend](https://github.com/bounswe/bounswe2023group2/pull/919)
  - [Mo/feature/reliability scale improvements](https://github.com/bounswe/bounswe2023group2/pull/927)
  - [MO/hotfix/locationfilter](https://github.com/bounswe/bounswe2023group2/pull/934)

### Additional Information:

Bug fixes and testing the PRs wasted more of my time than I thought, therefore I couldn't did the action part of mobile with respect to latest changes on backend. Sorry for that.
Additionally, I specially thanks Cahid to the extra effort in the last 2 days, this effort made us better. I thank all the team for that good application, to build up such a detailed application within 3 months despite the other responsibilities (especially courses in boun CMPE's 7th semester and the jobs that we worked) was very hard, but we figured it out somehow. Thanks for your all contributions. I may continue to develop this project further if I I find a free time.

---

## Member: Hasan Bingölbali

### Responsibilities:

My primary responsibilities were map design and implementation, search functionaliy, authentication and creating reusable classes that we used through the whole project period.

### Main contributions:

- **Code-related significant issues:**
  - [Mobile - Report malicious activities/users to admin](https://github.com/bounswe/bounswe2023group2/issues/735)
  - [Mobile - Search Implementation](https://github.com/bounswe/bounswe2023group2/issues/802)
  - [Mobile - Change Login Backend Implementation](https://github.com/bounswe/bounswe2023group2/issues/807)

### Pull Requests:

- **Created:**

  - [MO/feature/Report-Functionality](https://github.com/bounswe/bounswe2023group2/pull/762)
  - [MO/feature/search-implementation](https://github.com/bounswe/bounswe2023group2/pull/872)
  - [MO/feature/Auth-Enhancement](https://github.com/bounswe/bounswe2023group2/pull/891)

- **Reviewed:**
  - [Mo/feature/turkish language support](https://github.com/bounswe/bounswe2023group2/pull/698)
  - [Mo/feature/action page](https://github.com/bounswe/bounswe2023group2/pull/704)
  - [MO/bugfix/Fixing strings.xml](https://github.com/bounswe/bounswe2023group2/pull/709)
  - [Mo/advancement/string-translation](https://github.com/bounswe/bounswe2023group2/pull/893)
  - [added \_id to responses](https://github.com/bounswe/bounswe2023group2/pull/898)
  - [#906 Solution](https://github.com/bounswe/bounswe2023group2/pull/908)

### Additional Information:

My pull request are not many due to the fact that they required extra attention because they were critical points for our customer presentation.

---

## Member: Halil İbrahim Gürbüz

### Responsibilities:

Member of mobile team. Completing the tasks given to me and reviewing my teammates' pull requests was my responsibilities. I also took responsibility for the user interface and experience design of the mobile application.

### Main contributions:

My main contribution to the project was to design dynamic forms, main page tabbed design, sort and filter functionality, emergency functions, Turkish language support and UI/UX design.

- **Code-related significant issues:**

  - [Mobile - Add Need Form (#338)](https://github.com/bounswe/bounswe2023group2/issues/338)
  - [Mobile - Edit Need Form (#496)](https://github.com/bounswe/bounswe2023group2/issues/496)
  - [Mobile - Add Resource Page(#413)](https://github.com/bounswe/bounswe2023group2/issues/338)
  - [Mobile - CRUD Emergency Page (#733)](https://github.com/bounswe/bounswe2023group2/issues/733)
  - [Mobile - Email verification page design and implementation (#538)](https://github.com/bounswe/bounswe2023group2/issues/538)
  - [Mobile - Sort and filter components and implementations (#540)](https://github.com/bounswe/bounswe2023group2/issues/540)
  - [Mobile - Sort / Filter by Location and Date (#690)](https://github.com/bounswe/bounswe2023group2/issues/690)
  - [Mobile - Sort and filter for Action/Emergency/Event (#799)](https://github.com/bounswe/bounswe2023group2/issues/799)
  - [Mobile - Main Page Design (#582)](https://github.com/bounswe/bounswe2023group2/issues/582)
  - [Mobile - UI improvements (#601)](https://github.com/bounswe/bounswe2023group2/issues/601)
  - [Mobile - Turkish language support (#697)](https://github.com/bounswe/bounswe2023group2/issues/697)

- **Management-related significant issues**:

  - [All Teams - Requirements Update (#661)](https://github.com/bounswe/bounswe2023group2/issues/780)

### Pull Requests:

- **Created After MS2:**

  - [Mo/feature/turkish language support (PR#698)](https://github.com/bounswe/bounswe2023group2/pull/698)
  - [Mo/feature/action page (PR#704)](https://github.com/bounswe/bounswe2023group2/pull/704)
  - [MO/bugfix/Fixing strings.xml (PR#709)](https://github.com/bounswe/bounswe2023group2/pull/709)
  - [Mo/feature/resource detail page design (PR#710)](https://github.com/bounswe/bounswe2023group2/pull/710)
  - [Mo/bugfix/icons_fixed (PR#715)](https://github.com/bounswe/bounswe2023group2/pull/715)
  - [Mo/feature/homepage layout improvements (PR#741)](https://github.com/bounswe/bounswe2023group2/pull/741)
  - [Mo/feature/sort filter by location date (PR#754)](https://github.com/bounswe/bounswe2023group2/pull/754)
  - [MO/feature/email-verification (PR#778)](https://github.com/bounswe/bounswe2023group2/pull/778)
  - [Mo/feature/emergency (PR#825)](https://github.com/bounswe/bounswe2023group2/pull/825)
  - [Mo/design/upvote downvote design update (PR#838)](https://github.com/bounswe/bounswe2023group2/pull/838)
  - [Mo/feature/connect emergency to backend (PR#919)](https://github.com/bounswe/bounswe2023group2/pull/919)
  - [Mo/feature/reliability scale improvements (PR#927)](https://github.com/bounswe/bounswe2023group2/pull/927)
  - [MO/hotfix/locationfilter (PR#934)](https://github.com/bounswe/bounswe2023group2/pull/934)

- **Reviewed After MS2:**

  - [MO/feature/show-user (PR#702)](https://github.com/bounswe/bounswe2023group2/pull/702)
  - [MO/feature/role-based-credible-profile (PR#717)](https://github.com/bounswe/bounswe2023group2/pull/717)
  - [Mo/feature/crud event (PR#734)](https://github.com/bounswe/bounswe2023group2/pull/734)
  - [MO/bugfix/fix-bugs (PR#745)](https://github.com/bounswe/bounswe2023group2/pull/745)
  - [BE/refactor/resource-need-default-sort-direction (PR#755)](https://github.com/bounswe/bounswe2023group2/pull/755)
  - [Be/feature/email-verified-status-check (PR#829)](https://github.com/bounswe/bounswe2023group2/pull/829)
  - [Be/feature/feedback-status-check (PR#834)](https://github.com/bounswe/bounswe2023group2/pull/834)
  - [Be/feature/reliability-score (PR#894)](https://github.com/bounswe/bounswe2023group2/pull/894)
  - [Mo/bug/fix spam user role (PR#907)](https://github.com/bounswe/bounswe2023group2/pull/907)

### Additional Information:

After the feedback we received on MS2, I worked on adding Turkish language support and updating the voting system. Since I am new in Android development, I spent a lot of time on this project along with the time it took for me to learn. However, it was an enjoyable and instructive project to develop. Thanks to my teammates who helped me whenever I needed it.

---

## Member: Merve Gürbüz

### Responsibilities:

I am responsible for deployment, database administration, frontend development tracking and architecture.

### Main contributions:

- [Frontend - Email verification page design and implementation effort](https://github.com/bounswe/bounswe2023group2/issues/479)
- [Backend - Add s3 image upload endpoints](https://github.com/bounswe/bounswe2023group2/issues/694)
- [Frontend - Enhancing main page components](https://github.com/bounswe/bounswe2023group2/issues/696)
- [Deployment - Annotation Service in a seperate container](https://github.com/bounswe/bounswe2023group2/issues/784)
- [Integrate server to the deployment effort](https://github.com/bounswe/bounswe2023group2/issues/785)
- [Frontend - geocode converts](https://github.com/bounswe/bounswe2023group2/issues/788)
- [Frontend - add search to the navigation bar](https://github.com/bounswe/bounswe2023group2/issues/794)
- [Frontend - Add Action element on frontend](https://github.com/bounswe/bounswe2023group2/issues/526)
- [Backend - Action logic revisit](https://github.com/bounswe/bounswe2023group2/issues/695)
- [Management of Dividing Tasks Inside Teams](https://github.com/bounswe/bounswe2023group2/issues/816)

### Pull Requests:

- **Created:**
  - [699](https://github.com/bounswe/bounswe2023group2/pull/699)
  - [766](https://github.com/bounswe/bounswe2023group2/pull/766)
  - [839](https://github.com/bounswe/bounswe2023group2/pull/839)
  - [845](https://github.com/bounswe/bounswe2023group2/pull/845)
  - [846](https://github.com/bounswe/bounswe2023group2/pull/846)
  - [848](https://github.com/bounswe/bounswe2023group2/pull/848)
  - [883](https://github.com/bounswe/bounswe2023group2/pull/883)
  - [904](https://github.com/bounswe/bounswe2023group2/pull/904)
  - [915](https://github.com/bounswe/bounswe2023group2/pull/915)

### Additional Information:

I took responsibility in both backend, frontend and also deployment, which is not a great idea as I learned afterwards :). I have done many tasks in one PR which caused the huge unreadible PRs. I had to take many responsibilities unwillingly. I mainly tried to learn new concepts during the project.

---

## Member: Begüm Arslan

### Responsibilities:

I was mainly responsible for search semantic server and its endpoints. I also helped with bugfixes and file endpoints.

### Main contributions:

- **Code-related significant issues:**
  - [Backend- Search Endpoints](https://github.com/bounswe/bounswe2023group2/issues/720)
  - [Backend - Build semantic search server. ](https://github.com/bounswe/bounswe2023group2/issues/750)
  - [Backend - Download filtered resources, needs, events.](https://github.com/bounswe/bounswe2023group2/issues/721)
  - [Backend - Delete file](https://github.com/bounswe/bounswe2023group2/issues/707)
  - [Backend - Recurrence Improvement](https://github.com/bounswe/bounswe2023group2/issues/670)
  - [Backend - Action Improvement ](https://github.com/bounswe/bounswe2023group2/issues/671)
  - [Backend - Return user type with log in ](https://github.com/bounswe/bounswe2023group2/issues/703)
  - [Backend - Refactor user proficiency](https://github.com/bounswe/bounswe2023group2/issues/718)
  - [Backend- User account deletion ](https://github.com/bounswe/bounswe2023group2/issues/798)
  - [Backend - All Users Endpoint](https://github.com/bounswe/bounswe2023group2/issues/726)
  - [Backend- Bugifx- search ](https://github.com/bounswe/bounswe2023group2/issues/895)
- **Management-related significant issues**:
  - [Management of Dividing Tasks Inside Teams](https://github.com/bounswe/bounswe2023group2/issues/816)
  - [Maximum of 5 minutes video of your system being used with a good scenario. ](https://github.com/bounswe/bounswe2023group2/issues/820)

### Pull Requests:

- **Created:**

  - [BE/feature/search #751](https://github.com/bounswe/bounswe2023group2/pull/751)
  - [Be/feature/search #847](https://github.com/bounswe/bounswe2023group2/pull/847)
  - [Be/feature/download activity file](https://github.com/bounswe/bounswe2023group2/pull/853)
  - [Be/enhancement/download file](https://github.com/bounswe/bounswe2023group2/pull/866)
  - [added additional action types ](https://github.com/bounswe/bounswe2023group2/pull/899)
  - [added \_id to responses](https://github.com/bounswe/bounswe2023group2/pull/898)
  - [BE/enhancement/prof ](https://github.com/bounswe/bounswe2023group2/pull/881)
  - [BE/enhancement/user](https://github.com/bounswe/bounswe2023group2/pull/875)
  - [BE/feature/user](https://github.com/bounswe/bounswe2023group2/pull/851)
  - [BE/hotfix/action ](https://github.com/bounswe/bounswe2023group2/pull/914)
  - [bugfix](https://github.com/bounswe/bounswe2023group2/pull/849)
  - [Be/feature/search](https://github.com/bounswe/bounswe2023group2/pull/850)
  - [Update user_controller.py](https://github.com/bounswe/bounswe2023group2/pull/830)
  - [MO/feature/resource-recurrent](https://github.com/bounswe/bounswe2023group2/pull/909)
  - [Be/refactor/proficiency](https://github.com/bounswe/bounswe2023group2/pull/748)
  - [BE/feature/deelte-file #708](https://github.com/bounswe/bounswe2023group2/pull/708)
  - [BE/enhancement/login #706](https://github.com/bounswe/bounswe2023group2/pull/706)
  - [BE/fix/upload-file #705](https://github.com/bounswe/bounswe2023group2/pull/705)
  - [added returning all recurrent resources for get_resource_by_id #701](https://github.com/bounswe/bounswe2023group2/pull/701)

- **Reviewed:**
  - [Be/feature/upload file ](https://github.com/bounswe/bounswe2023group2/pull/699)
  - [BE/bugfix/need-filter-coordinates ](https://github.com/bounswe/bounswe2023group2/pull/713)
  - [BE/feature/email-verification-update-role: Update role if GUEST to AUTHENTICATED on verification ](https://github.com/bounswe/bounswe2023group2/pull/831)
  - [Be/feature/feedback update](https://github.com/bounswe/bounswe2023group2/pull/835)
  - [chore(backend): add apscheduler](https://github.com/bounswe/bounswe2023group2/pull/846)
  - [added check for predefined need type(missing person)](https://github.com/bounswe/bounswe2023group2/pull/878)
  - [Fe/feature/filter menu](https://github.com/bounswe/bounswe2023group2/pull/910)
  - [Be/hotfix/need missing person corrected](https://github.com/bounswe/bounswe2023group2/pull/911)

### Unit Tests

I have written unit tests for user endpoints. Also tried to write endpoints for action and search but had problem with it, so most of the tests were done on [postman](https://www.postman.com/bgmrsln/workspace/darp-apis/collection/27281924-62dac0e3-35d0-41ee-b87f-57a77de35036?action=share&creator=27281924)

### Additional Information:

I enjoyed creating the semantic search for our application. And I am happy with the feedback. Also I would like to thank to the team for their feedback, this made my endpoints better.

---

## Member: Ömer Şahin Albayram

### Responsibilities:

I was responsible for main map page (its frontend design and features) and its api connections.

### Main contributions:

- **Code-related significant issues:**
  - [Adjusting Filter menu](https://github.com/bounswe/bounswe2023group2/issues/681)
  - [Implementing area select for filtering ](https://github.com/bounswe/bounswe2023group2/issues/682)
  - [Map visualization for need, action, event and resources](https://github.com/bounswe/bounswe2023group2/issues/525)
  - [Adding Upvote Downvote buttons and system to information card](https://github.com/bounswe/bounswe2023group2/issues/719)
- **Others:**
- [Research about how filtered activites might be printed ](https://github.com/bounswe/bounswe2023group2/issues/493) -[Map Page user tests](https://github.com/bounswe/bounswe2023group2/issues/729) -[Creating custom markers](https://github.com/bounswe/bounswe2023group2/issues/770)

### Pull Requests:

- **Created:**

  - [Fe/feature/filter menu #921](https://github.com/bounswe/bounswe2023group2/pull/921)
  - [Fe/feature/filter menu #910](https://github.com/bounswe/bounswe2023group2/pull/910)
  - [Fe/feature/implementation of needs #769](https://github.com/bounswe/bounswe2023group2/pull/769)
  - [Connection between backend and map page / sidepopup #716](https://github.com/bounswe/bounswe2023group2/pull/716)

- **Reviewed:**
  - [Fe/annotation #922](https://github.com/bounswe/bounswe2023group2/pull/922)
  - [Fe/annotation #918 ](https://github.com/bounswe/bounswe2023group2/pull/918)
  - [Be/enhancement/download file #866](https://github.com/bounswe/bounswe2023group2/pull/866)
  - [Be/feature/download activity file #853](https://github.com/bounswe/bounswe2023group2/pull/853)
  - [Fe/feature/vote feed #619](https://github.com/bounswe/bounswe2023group2/pull/619)

### Additional Information:

Although, I little bit lost after MS2, I focused on the functionality of the main map, filtering on the main map and creating a downloadable file to facilitate organization.

## Member: Aziza Mankenova

### Responsibilities:

Overall, I was responsible for the backend implementation of need, reports, emergency and feedback system(voting). As a member of backend team, for the final milestone, I was responsible for the implementation of feedback system unvote endpoints, part of admin endpoints, emergency endpoints, filter and sort for emergency and adding an additional predefined need type. Also, I have wrote unit tests for the endpoints that I have implemented.
Also, I was involved in project management through issue tracking, documentation updates, project planning and reviewing work of my teammates.

### Main contributions:

- **Code-related significant issues**:

  - [Backend - Implement the check for the coordinates of resources and needs](https://github.com/bounswe/bounswe2023group2/issues/584)
  - [Backend - Feedback System unvote activity](https://github.com/bounswe/bounswe2023group2/issues/659)
  - [Test - Backend - Admin endpoints](https://github.com/bounswe/bounswe2023group2/issues/686)
  - [Backend - Emergency endpoints](https://github.com/bounswe/bounswe2023group2/issues/691)
  - [Backend - Adding predefined need type for lost acquaintance](https://github.com/bounswe/bounswe2023group2/issues/742)
  - [Test - Backend - Emergency endpoints ](https://github.com/bounswe/bounswe2023group2/issues/789)
  - [Backend - Report create endpoint fix](https://github.com/bounswe/bounswe2023group2/issues/795)
  - This was a collaborative issue, I have implented sort and filter for the emergency, since I have wrote the code for it.
    - [Backend - Sort and Filter (Event/Action/Emergency)](https://github.com/bounswe/bounswe2023group2/issues/827)
  - [Backend - Need CRUD endpoint implementation #326](https://github.com/bounswe/bounswe2023group2/issues/326)
  - [Backend - Test: Need CRUD Endpoints #381](https://github.com/bounswe/bounswe2023group2/issues/381)
  - [Backend - Improvements Need CRUD and additional endpoints #400](https://github.com/bounswe/bounswe2023group2/issues/400)
  - [Backend- Report malicious activities/users to admin ](https://github.com/bounswe/bounswe2023group2/issues/569)
  - [Backend - Feedback system upvote/downvote endpoint](https://github.com/bounswe/bounswe2023group2/issues/480)
  - [Backend - Admin CRUD and specific endpoint implementations](https://github.com/bounswe/bounswe2023group2/issues/519)
  - [Test - Backend - Feedback endpoints](https://github.com/bounswe/bounswe2023group2/issues/524)

- **Management-related significant issues**:
  - [Project plan](https://github.com/bounswe/bounswe2023group2/issues/812)
  - [Software design documents (using UML)](https://github.com/bounswe/bounswe2023group2/issues/941)
  - [Maximum of 5 minutes video of your system being used with a good scenario.](https://github.com/bounswe/bounswe2023group2/issues/820)
  - [Reviewing the Project Details from CMPE352 by the new members #263](https://github.com/bounswe/bounswe2023group2/issues/263)
  - [Update the Class Diagrams with respect to the changes in the requirements #265](https://github.com/bounswe/bounswe2023group2/issues/265)
  - [Create a project plan for DaRP #282](https://github.com/bounswe/bounswe2023group2/issues/282)
  - [Project Plan Contribution: Back-end Perspective #297](https://github.com/bounswe/bounswe2023group2/issues/297)
  - [Backend - Milestone 2 Plan Review](https://github.com/bounswe/bounswe2023group2/issues/489)

### Pull Requests:

- **Created:**

  - [Feedback System Unvote endpoint #658](https://github.com/bounswe/bounswe2023group2/pull/658)
  - [Be/test/feedback #660](https://github.com/bounswe/bounswe2023group2/pull/660)
  - [BE/feedback Bug fix #771](https://github.com/bounswe/bounswe2023group2/pull/771)
  - [Be/feature/emergency #836](https://github.com/bounswe/bounswe2023group2/pull/836)
  - [Report Create Bug Fix #837](https://github.com/bounswe/bounswe2023group2/pull/837)
  - [Be/feature/filter sort emergency #873](https://github.com/bounswe/bounswe2023group2/pull/873)
  - [Report bug fix #874](https://github.com/bounswe/bounswe2023group2/pull/874)
  - [added check for predefined need type(missing person) #878](https://github.com/bounswe/bounswe2023group2/pull/878)
  - [emergency reliabiliy added #924](https://github.com/bounswe/bounswe2023group2/pull/893)
  - [Emergency tests are implemented #944](https://github.com/bounswe/bounswe2023group2/pull/944)
  - [Report and need tests' errors are fixed #945](https://github.com/bounswe/bounswe2023group2/pull/945)
  - [Admin unit tests #946](https://github.com/bounswe/bounswe2023group2/pull/946)

- **Reviewed:**
  - [Be/feature/admin enpoints #621](https://github.com/bounswe/bounswe2023group2/pull/621)
  - [Fix frontend base url in forgot password controller #772](https://github.com/bounswe/bounswe2023group2/pull/772)
  - [Be/feature/sort filter for events #870](https://github.com/bounswe/bounswe2023group2/pull/870)
  - [Backend - fix report reject/accept #886](https://github.com/bounswe/bounswe2023group2/pull/886)

### Unit Tests:

I have implemented unit tests for needs, reports, emergency, feedback(upvote, downvote, unvote).

### Additional Information:

Due to being sick, I had to postpone the implementation of the tasks for that period.

---

## Member: Mehmet Kuzulugil

### Responsibilities:

I contributed to the backend coding on:

- Putting standards for backend design
- Creating template/backbone code parts (team work)
- Debugging and code reviews.
  And took the responsibility for research and implementation of an Annotation Server.

### Main contributions:

- **Code-related significant issues:**
  - [Backend - Sort and Filter (Events) #827 ](https://github.com/bounswe/bounswe2023group2/issues/827)
  - [Deployment - Annotation Service in a seperate container #784](https://github.com/bounswe/bounswe2023group2/issues/784)
  - [Annotation - Annotation Server API learning and sample uses #737](https://github.com/bounswe/bounswe2023group2/issues/737)
  - [Backend - Improvements on CRUD for User Profile #399](https://github.com/bounswe/bounswe2023group2/issues/399)
  - [Research for Annotation standards #334](https://github.com/bounswe/bounswe2023group2/issues/334)
  - [Backend - API Convention: Data Structure, Error Handling, Response Codes #290](https://github.com/bounswe/bounswe2023group2/issues/290)
    - Document: [API conventions](https://github.com/bounswe/bounswe2023group2/wiki/API-conventions)

### Pull Requests:

- **Created:**
  - [#826](https://github.com/bounswe/bounswe2023group2/pull/826)
  - [#851](https://github.com/bounswe/bounswe2023group2/pull/551)
  - [#548](https://github.com/bounswe/bounswe2023group2/pull/548)
  - [#858](https://github.com/bounswe/bounswe2023group2/pull/858)
  - [#870](https://github.com/bounswe/bounswe2023group2/pull/870)
  -
- **Reviewed:**
  - [#945](https://github.com/bounswe/bounswe2023group2/pull/945)
  - [#944](https://github.com/bounswe/bounswe2023group2/pull/944)
  - [#873](https://github.com/bounswe/bounswe2023group2/pull/873)

### Additional Information:

Finally...

---

## Member: Ramazan Burak Sarıtaş

### Responsibilities:

I was an active member of the backend team. Overall, my main responsibilities included implementing and continuously refining the Resource and Need models based on the feedbacks. I was also responsible for some email verification related tasks. I wrote unit tests for the resource feature, email verification feature and address translation feature, added filter sort functionality along with reliabilty score features to Resource and Need.

### Main contributions:

My main contributions to the project were mainly the implementation and Resource and Need. I also implemented an email verification system and a password reset system. I added the feature to translate address to lat/long and vice versa. Lastly, I designed the current vector-based logo of our application with Adobe Illustrator.

- **Code-related significant issues:**

  - [Backend - Resource CRUD endpoint implementation #325](https://github.com/bounswe/bounswe2023group2/issues/325)
  - [Backend - Need CRUD endpoint implementation #326](https://github.com/bounswe/bounswe2023group2/issues/326)
  - [Backend - Authentication related endpoint implementation #327](https://github.com/bounswe/bounswe2023group2/issues/327)
  - [Backend - Email Verification #473](https://github.com/bounswe/bounswe2023group2/issues/473)
  - [Backend - Event CRUD endpoints #475](https://github.com/bounswe/bounswe2023group2/issues/475)
  - [Backend - Action implementation #477](https://github.com/bounswe/bounswe2023group2/issues/477)
  - [Backend - User role implementation #478](https://github.com/bounswe/bounswe2023group2/issues/478)
  - [Backend - Feedback system upvote/downvote endpoint #480](https://github.com/bounswe/bounswe2023group2/issues/480)
  - [Backend - Predefined Subtypes Implementation #512](https://github.com/bounswe/bounswe2023group2/issues/512)
  - [Backend - Address Translation Service #722](https://github.com/bounswe/bounswe2023group2/issues/722)
  - [Backend - Forgot Password Implementation #740](https://github.com/bounswe/bounswe2023group2/issues/740)

- **Management-related significant issues**:

  - [Reviewing the Project Details from CMPE352 by the new members #263](https://github.com/bounswe/bounswe2023group2/issues/482)
  - [Select a sub-team #269](https://github.com/bounswe/bounswe2023group2/issues/269)
  - [Review of Project Plan #482](https://github.com/bounswe/bounswe2023group2/issues/482)
  - [Integration of the Logo to the App #606](https://github.com/bounswe/bounswe2023group2/issues/606)

### Pull Requests:

- **Created After Milestone 2:**

  - [BE/feature/email-verification #712](https://github.com/bounswe/bounswe2023group2/pull/712)
  - [BE/bugfix/need-filter-coordinates #713](https://github.com/bounswe/bounswe2023group2/pull/713)
  - [BE/feature/geocode #753](https://github.com/bounswe/bounswe2023group2/pull/753)
  - [BE/refactor/resource-need-default-sort-direction #755](https://github.com/bounswe/bounswe2023group2/pull/755)
  - [Be/feature/forgot-password #757](https://github.com/bounswe/bounswe2023group2/pull/757)
  - [BE/bugfix/reset-password-request #761](https://github.com/bounswe/bounswe2023group2/pull/761)
  - [BE/refactor/reset-password-body #767](https://github.com/bounswe/bounswe2023group2/pull/767)
  - [Be/feature/email-verified-status-check #829](https://github.com/bounswe/bounswe2023group2/pull/829)
  - [BE/feature/email-verification-update-role #831](https://github.com/bounswe/bounswe2023group2/pull/832)
  - [Be/feature/feedback-status-check #834](https://github.com/bounswe/bounswe2023group2/pull/834)
  - [BE/test/geocode #861](https://github.com/bounswe/bounswe2023group2/pull/861)
  - [BE/test/email-verification #877](https://github.com/bounswe/bounswe2023group2/pull/877)
  - [Be/feature/reliability-score #894](https://github.com/bounswe/bounswe2023group2/pull/894)

- **Reviewed After Milestone 2:**

  - [Be/refactor/user profile improvement #714](https://github.com/bounswe/bounswe2023group2/pull/714)
  - [BE/feedback Bug fix #771](https://github.com/bounswe/bounswe2023group2/pull/771)
  - [Lab report 10 #805](https://github.com/bounswe/bounswe2023group2/pull/805)
  - [Be/feature/annotation server #826](https://github.com/bounswe/bounswe2023group2/pull/826)
  - [Update user_controller.py #830](https://github.com/bounswe/bounswe2023group2/pull/830)
  - [Be/feature/emergency #836](https://github.com/bounswe/bounswe2023group2/pull/836)
  - [open address for need and resources #855](https://github.com/bounswe/bounswe2023group2/pull/855)
  - [Unit info added to resource and need #858](https://github.com/bounswe/bounswe2023group2/pull/858)
  - [BE/bugfix/error-on-schedule #862](https://github.com/bounswe/bounswe2023group2/pull/862)
  - [Be/feature/sort filter for events #870](https://github.com/bounswe/bounswe2023group2/pull/870)
  - [Fe/feature/address to xy #883](https://github.com/bounswe/bounswe2023group2/pull/883)
  - [#Solve conflicts #930](https://github.com/bounswe/bounswe2023group2/pull/930)

### Additional Information:

Lots of effort went into the project by all team members, It was a challenging but definitely a rewarding journey. I personally learned many new and important aspects of software engineering.

---

## Member: Buse Tolunay

### Responsibilities:

Mainly I was responsible for the backend implementation of need, resource and feedback system, and the email verification testing For the final milestone I was involveed in the implementation of feedback system vote and credible vote endpoints, integrating new fields for need and resource, translating the form fields to Turkish . I also wrote a unit test for email verification with Burak.
Also, I was active during planning and reviewing work of both backend and frontend teammates.

### Main contributions:

- **Code-related significant issues**:
  - [Test - Backend - Email verify Endpoints](https://github.com/bounswe/bounswe2023group2/issues/522)
  - [BTranslation - Language pack files should be prepared in various languages](https://github.com/bounswe/bounswe2023group2/issues/678)
  - [Backend - Change vote coefficients](https://github.com/bounswe/bounswe2023group2/issues/725)
  - [Frontend - Icons decision](https://github.com/bounswe/bounswe2023group2/issues/728)
  - [Frontend - Map Page user tests](https://github.com/bounswe/bounswe2023group2/issues/729)
  - [Backend-open adress for need and resource ](https://github.com/bounswe/bounswe2023group2/issues/842)
  - [Backend verification for votes](https://github.com/bounswe/bounswe2023group2/issues/852)
- **Management-related significant issues**:
  - [Project plan](https://github.com/bounswe/bounswe2023group2/issues/812)
  - [Software design documents (using UML)](https://github.com/bounswe/bounswe2023group2/issues/941)
  - [Reviewing the Project Details from CMPE352 by the new members #263](https://github.com/bounswe/bounswe2023group2/issues/263)
  - [Project Plan Contribution: Back-end Perspective #297](https://github.com/bounswe/bounswe2023group2/issues/297)
  - [Backend - Milestone 2 Plan Review](https://github.com/bounswe/bounswe2023group2/issues/489)

### Pull Requests:

- **Created:**

  - [open address for need and resources #855](https://github.com/bounswe/bounswe2023group2/pull/855)
  - [open address field](https://github.com/bounswe/bounswe2023group2/pull/844)
  - [Open address](https://github.com/bounswe/bounswe2023group2/pull/843)
  - [tr form fields#840](https://github.com/bounswe/bounswe2023group2/pull/840)
  - [Report Create Bug Fix #837](https://github.com/bounswe/bounswe2023group2/pull/837)
  - [Be/feature/feedback update #835](https://github.com/bounswe/bounswe2023group2/pull/835)
  - [Be/feature/need improvement #462](https://github.com/bounswe/bounswe2023group2/pull/462)

- **Reviewed:**
  - [emergency reliabiliy added #924](https://github.com/bounswe/bounswe2023group2/pull/924)
  - [FE/hotfix/build-error #839](https://github.com/bounswe/bounswe2023group2/pull/839)
  - [Be/test/need #467](https://github.com/bounswe/bounswe2023group2/pull/467)

### Unit Tests:

I have implemented unit tests for email verification,

### Additional Information:

I was lagging behind the class and did not participate the implementation of the 2nd milestone due to personal issues. But I decided to come back and I put a lot of effort afterwards, I hope everything works out.

---

## Member: Can Bora Uğur

### Responsibilities:

I was a part of the frontend team. My main responsibilities were translation of the website, the profile page and the admin panel, though I did work outside of this too.

### Main contributions:

- **Code-related significant issues**:
  - [Frontend - Language support](https://github.com/bounswe/bounswe2023group2/issues/724)
  - [Frontend - Finalize profile page](https://github.com/bounswe/bounswe2023group2/issues/776)
  - [Frontend - Finalize admin panel](https://github.com/bounswe/bounswe2023group2/issues/775)
  - [Frontend - Add emergency form](https://github.com/bounswe/bounswe2023group2/issues/685)
- **Management-related significant issues**:
  - [User Manual (frontend)](https://github.com/bounswe/bounswe2023group2/issues/821)

### Pull Requests:

- **Created:**
  - [Fe/enhance/admin panel](https://github.com/bounswe/bounswe2023group2/pull/889)
  - [Fe/enhance/profile pages](https://github.com/bounswe/bounswe2023group2/pull/854)
  - [Fe/feature/languages](https://github.com/bounswe/bounswe2023group2/pull/747)
  - [Fe/feature/event table](https://github.com/bounswe/bounswe2023group2/pull/765)
  - [Frontend - Fix main layout being too short](https://github.com/bounswe/bounswe2023group2/pull/867)
  - [Fe/feature/add forms](https://github.com/bounswe/bounswe2023group2/pull/711)
  - [Fe/feature/view other users](https://github.com/bounswe/bounswe2023group2/pull/629)
- **Reviewed:**
  - [Connection between backend and map page / sidepopup](https://github.com/bounswe/bounswe2023group2/pull/716)
  - [User profile picture URL added to optional info API.](https://github.com/bounswe/bounswe2023group2/pull/759)
  - [FE/feature/reset-password](https://github.com/bounswe/bounswe2023group2/pull/766)
  - [Fe/feature/implementation of needs](https://github.com/bounswe/bounswe2023group2/pull/769)
  - [BE/feature/user ](https://github.com/bounswe/bounswe2023group2/pull/851)

### Unit Tests:

None

### Additional Information:

I initially lagged behind because of all the new things I need to learn. I like to think I eventually caught on, especially in the last few weeks.

---
