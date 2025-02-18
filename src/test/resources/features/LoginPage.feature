 Feature: Login Functionality for OpenCart E-commerce Website

  As a user of the OpenCart website
  I want to be able to log in with my account
  So that I can access my account-related features and manage my orders

  Scenario Outline: Successful login with valid credentials
    Given I have entered a valid "<username>" and "<password>"
    When I click on the login button 
    Then I should be logged in successfully
    
  Examples:
    | username                | password |
    | arun.shankar396@gmail.com | Arun@396 |

