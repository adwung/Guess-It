# Project Plan

**Author**: Team74

## 1 Introduction

This project plan describes the team and the process that will be followed to successfully create a Wheel of Fortune Android application for George P. Burdell

## 2 Process Description

- **Requirements Analysis**
   - **Description**
     This activity involves designing the requirements for the application. These requirements describe an external view of a system, the system's phenotype. 
   - **Entrance Criteria**
     - Requirements
     - Use Cases
   - **Exit Criteria** 
     - **Use Case Diagram**: A UML use case diagram that depicts the interactions among the elements or bjects of the system. This diagram is used to identify, clarify, and organize system requirements.
     - **Requirements Document**: An aggregation of requirements with a high-level description of each use case and the breakdown of their each individual elements including the actor, action, object, pre-conditions, and post-conditions.

- **Analysis & Design**
   - **Description**
     This activity involves designing the system for the application. Specifically, it takes the results of the business modelling and requirements analysis to determine an underlying architecture to suit the needs of the customer. From there several documents and diagrams are produced to model the necessary components of the system. These artifacts will later be a critical component of the implementation phase.
   - **Entrance Criteria**
     - Requirements
     - Use Cases
     - Use Case Diagram
   - **Exit Criteria**
     - **Component Diagram**: Satisfactory completion of this criteria involves producing a diagram that succinctly models the different components of the system and how they interact with each other
     - **Deployment Diagram**: The deployment diagram simply needs to display the physical architecture that the application will run on
     - **Class Diagram**: A UML diagram that shows the classes that will exist within the software, their attributes, operations and relationships with each other
     - **UI Design**: Graphical mockups of the Android application that displays the most important functionality in UI form
     - **Implementation artifacts**: An aggregation of the other exit criteria along with the entire Design Document

- **Implementation**
    - **Description**
      This activity involves building the software as described by the Requirements, User Cases and Design documents and that passes the test from the testing plan.  
    - **Entrance Criteria**
      - Requirements
      - Use Cases
      - Use Case Diagram
      - Design Documents
      - Testing Plan
    - **Exit Criteria**
       - **Beta Application**: A functioning and nearly feature complete version of the application.
       - **Document Revisions**: Revisions to the Design, Use Case and Testing documentation to match changes made during this phase.

- **Testing**
   - **Description**
     This activity involves designing an exhaustive set of software test-cases that will ensure that the application code meets all of the project requirements. Testing will emphasize both requirements coverage as well as code coverage to measure code efficiency and validate project goals. Software bugs found during testing will be reported and fixed accordingly. 
   - **Entrance Criteria**
     - Requirements
     - Use Cases
     - Use Case Diagram
   - **Exit Criteria**
      - **JUnit Tests**: All of the JUnit tests made at the system, integration, and unit level will pass
      - **YouTrack Bug Reports**: All software bugs reported will be resolved

- **Deployment**
   - **Description**
     This activity involves following the Deployment diagram from the Design documentation to deploy the Beta version of the applications.  The deployed application should then be run through the entire test plan and bug fixes should be applied to the application until all acceptance criteria are met.
   - **Entrance Criteria**
     - Beta Application
     - Deployment Diagram
     - Testing Plan
   - **Exit Criteria**
      - **Production Application**: Completed application ready for delivery to clients.

## 3 Team

**Team Members**
- David DeKime 
- Tim Hays
- Tanner Mapes
- Andrew Wung

**Roles**
- **Requirements Engineer**: Analyzes requirements and breaks them down into use cases and a use case diagram
- **Back-End Engineer**: Responsible for designing the back-end architecture of the system including the deployment infrastructure and components of the application
- **Front-End Engineer**: Designs the front end user interface of the application
- **Test Engineer**: Creates the testing strategy for the team including test selection, adequacy criterion, bug tracking, technologies used and test cases

**Assigned Roles**

| Role                  | Team Member  |
| --------------------- | ------------ |
| Requirements Engineer | Andrew Wung  |
| Back-End Engineer     | Tim Hays     |
| Front-End Engineer    | Tanner Mapes |
| Test Engineer         | David Dekime |
