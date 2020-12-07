# README document 

## Description of each Class

Conversation.java
This class represents each of the user’s conversations. Each instance of this class will contain the fields of the conversation names, the users involved in each conversation, as well as the file in which the messages from all users in that conversation are stored. 

Server.java
This class represents a server that creates a server socket and waits for the clientGUI class to connect. Once connected, the client and server communicates with one another asynchronously. After the client class connects, it instantiates a new thread of type Userhandler for each user connection to help the program handle multiple users at the same time. 

User.java
This class represents each user of the application. It holds the user information such as full name, username, and password as well as a file that contains all the conversation names the user participated in. 

RunLocalTest.java
RunLocalTest.java is a JUnit Testing class that will check if the program has the correct implementations. (More information on this class is provided in the Program Testing section)

ClientGUI.java
Description:
This class represents the client that connects to the server and implements the view component of the application. Once connected, the client and server can both read and write data to one another asynchronously. This implements the GUI and functionality for all the screens in the application. This also implements various action listeners which responds to various user events with actions. 

ActionListeners:
actionListener
It is the main action listener for login screen, signup screen, main screen, messages screen, and adding conversation  screens
addButton ("New Conversation") calls addConversation to create a new conversation
signUpButton(“Sign Up”) hides the login frame and calls displaySignUp to open the signup frame.
signUpPageButton(“Sign Up”) calls the signup method to create a new user account
signupToLogin (“Log In”) hides the sign up page and makes the login frame visible 
submitFields ("Create Conversation") calls addConversationToFile to add conversation to users conversation fille
loginButton ( “Login”) hides the login frame. Calls logIn method to validate the user account, if user account is valid account then it calls mainScreen to display the home page with the list of user conversations. Also creates a new Timer to refresh the the user conversations in real-time.
addOtherUsers ("Add More Users") hides the addConversationFields and calls the addConversation method to allow users to add more users to the conversation 
searchButton(‘Search”) calls displaySearchMatches method to show the user all the usernames that contain the search username word
settingsButton(“Settings”) hides the main frame and calls the settings to display the settings frame
sendButton (“Send”) calls the addMessage method to send the user’s message and the displayMessages method to make this message visible to the user and the people they are sending it. Makes sure that the text field is not null before the user can send. 
backButton(“Back”) hides the message frame and makes the main frame visible so users can go back to the home messages page. 
editMessagesButton(“Edit”) calls the editMessage method so that users can change the selected message. 
editMessBackButton(“Back”) hides the editMessageFrame frame and calls the displayConversation method to display the specific conversation messages.
Conversation Name button in home page calls displayConversation to display the list of messages in the selected conversation and also creates new instance of the Timer to refresh the messages in real-time.  

settingsAL
It is the action listeners for the buttons(deleteAccountButton, homebutton, saveButton, and  logoutButton )  in the Settings frame. 
For the deleteAccountButton (Delete), it calls the delteteAccount method to delete the user’s current account. 
For the homebutton (Home), it makes settingsFrame not visible, and the mainFrame visible so users can go back to the home messages page. 
For the saveButton (Save), it calls the settings method so that users can save their new account information for full name and password. 
For the logoutButton (Logout) it exits the user from the program and sends a message informing them that they are logging out. 

deleteMessageAL
It is the actionlistener for the deleteMessageButton (“Delete”) button.  It calls the deleteMessage and editMessage methods to edit and delete specific messages.  
deleteConversation
It is the action listener for the deleteConvButton (“Delete”) button. 
Calls deleteConversation method to delete a conversation from the user's conversations list. 

editSpecificMessageAL
It is the action listener for the editSpecificMsgButton (“Edit”) button. Calls editSpecificMessage and editMessage method to allow users to edit their messages once sent. 

addUserToConversation
It's the action listener for the userButton. Calls fillConversationFields to add users to the conversations list. 

Update
It is the action listener associated with the timer. Calls the displayMessages to refresh the messages frame (messages) for the conversation selected. It ensures that messages are displayed in real time.
 
updateMain
 It’s the action listener associated with Timer. Calls the mainPanel to refresh the conversations. Makes sure that conversations created or deleted are shown in real time. 
 
Methods:
Main
Creates a socket for the user and sets the host to localhost and port 8080. Creates input and output streams.
 Calls SwingUtilities.invokeLater to run the client on EDT to avoid problems such as deadlock. 
 
Run
Creates the Login Frame with 2 text fields for users to enter their username and password. Below in the frame are the “Login” and “SignUp” button. 

Login 
Reads the username and password and sends a request to the server to validate the account. 
If the server sends back that the user’s input is invalid, informs users that information is invalid with JOptionPane error panel. If the server sends back that user input is valid, the mainFrame with the user’s conversation list is displayed. 

displaySignup
Displays the Sign Up frame with 3 text fields for users to enter their full name, username, and password. 
When users click the “Login” button below they will be taken to the login frame. 

singUp
Once the user clicks the “Sign Up” button,  if any of the text fields are empty or user types in a username that already exists they will receive an error message accordingly.  Else, the User account information is sent to the server to create an account and store the information in the users file. If this is done successfully, users will be taken into the login frame to login. Otherwise they will receive another error message.

mainScreen
This method creates a frame with a bottom panel contain a “Settings” button and a “New Conversations Button” 
It calls the mainPanel to display the user’s current conversation list. 

mainPanel 
Calls the readConversationsFromFile method to get the list of user’s conversations. 
Runs through the array and displays each conversation as a button with a delete button next to it. 

settings
Calls the server to get the current user information. If it is not successful sends error message 
In the top panel, displays the user’s username, and provides a text field for users to enter the new username or password they would like to change to. 
The bottom panel contains the “Back”,  “Save Settings”, “Delete”, and “Logout” buttons. 

deleteAccount
sends the request to the server to delete the account. If the server is not successful ,methods send an error message to users. 

saveSettings
Methods makes sure that the username and password textfield in the settings panel is not null, and sends an error message if null. 
Sends the information in these text fields to the server so the server can save the user’s new account information to the user’s file. 
If Server is unsuccessful in saving the user's information, another error message will be sent to the user. 

readConversationsFromFile
Reads conversations from the  usersConvesationFile and stores them in the conversations array list. 

readUsers
Reads users from the  usersFile and stores them in the users array list. 

displayConversations 
Calls displayMessages to display the messages for the conversation 
Frame displays of bottom panel that consists of “Edit”, “Back”,  and “Send” button 

displayMessages 
Reads messages from the messageFile and displays it in the message Frame. 

editMessages 
Creates new frame, reads from messages Arrays and displays messages in frame 
Next to each individual message, creates an edit and delete button if the message is from the current user. If not, display a “Cannot Delete” JLabel next to message
The bottom panel contains a “Back” button to return to conversations frame 

DeleteMessages
Deletes the message next to the “Delete” button clicked 
Removes the deleted message from the message array and calls the readConversationsFromFile method

editSpecificMessage
Opens the input dialog so users can changes the specific message of their choosing 
Sends the new message to the server to save to the messages file 
If the message they selected is null, and error message will appear

deleteConversation
Send the request to the server to delete the conversation. If server was unsuccessful, sends an error message 

addMessages 
Adds a message to the conversation frame 
Appends the conversation file with new message,

addConversation
Allows users to search for other users to creates conversations with 
Contains the “search” button 

displaySearchMatches
sends the user’s input in the addConversation text field to find usernames that contain the username the user inputted. 
If server is unsuccessful in find any names, an error message will appear to the user 

fillConversationFields
Creates a new frame. Bottom panel contains “Create Conversation” and “Add More Users” buttons. The top portion of the Frame has a text field for users to enter conversation name. 
To create group conversations, clicking the “Add More Users” buttons adds the users to the conversation member list. Loops through the users array to make sure that the usernames the user inputted are valid. If not, send and error message 
“Create Conversation” button allows users to create a new conversation with the single member they searched. After adding the member, users have the choice to create a group conversation by adding more members to the conversation with the “Add More Users” button. Users can then create a conversation and will be given an error message if the conversation does not have a name. 

addConversationToFile
sends a request  to the server to add another conversation to the users conversations file. 
If server is unsuccessful, sends user an error message indicating unable to write to the file


UserHandler.java

Description
The UserHandler class is instantiated by the Server class everytime a new client connects to the server. Creating a new UserHandler thread for each client, the thread remains open whilst the User is “online”. This thread reads the input but the User through a BufferedReader, and sends back data through an Object Output Stream. A basic structure was created for the UserHandler thread were the User sends a command in the form of “COMMAND*INPUT”. The “COMMAND*” acts as a signal for what the user is asking or the action they want to perform. For example, when the user presses the login button, the server would receive a message in the following format: 

LogIn*Username*Password

The UserHandler would receive this message and identify the “LogIn*” command, and react accordingly. 

Methods
run
Endless loop that waits for user input. Once it receives a signal by the user it will check which actions the user wants to do. 
It calls the corresponding method and outputs the data back to the client through an ObjectOutputStream
readUsers
Read the file usersFile and adds the information to the Arraylist userArrayList
The File usersFile contains all of the Users that have used the program. The Users are stored as Objects. Thus they are read with ObjectInputStream.

readConversations
Reads the file “conversations” from the “currentUser” and stores the Conversations in the Arraylist “userConversations”. 
currentUser is the user that initiated the thread. 
Each user has their own “conversations” File which contains all of the conversations they form a part of. 

logIn
Receives the input by the user in the format of “LogIn*Username*Password”
Calls the method readUsers 
Checks if the username and password sent by the client matches and existing user

signUp
Receives the input by the user in the format of “SignUp*MichaelCon*Username*Password”
Checks if the username sent by the client is unique, does not already exist in file. 
Creates a new text file for the new account and then adds the new user to the Arraylist using Arraylist

searchUser
Receives the input by the user in the format of “SearchUser*Name”
Calls the method readUsers
Searches through arrayUsers for a username that resembles the input by the User
If there is one or multiple matches for the the user input it will store these users in an ArrayList “result” and return it to the client

addUserToConversation
Call the method readUsers.
Reads the Users sent by the Client, adds them to the Arraylist “usersToAdd” along with the currentUser itself.
createConversation
This method creates the Conversation with the specified name and the corresponding participants.
Return true or false, if the conversation was created successfully.
Creates a file for the Conversation and adds it to the list of conversations for the other users.

editMessage
Calls the method ReadConversation
This method handles the request of editing messages 
It receives the message that the client wants to edit and and writes to file the edited message
It is only possible to edit and the messages that belong to the user.

deleteMessage
Receives the index of the message that the client wants to delete. Deletes the specified message and rewrites the “messages” without the deleted message

editSpecificMessage
Receives the newMessage from the user and the specific index of the message 
Replaces the old message with the new message and then writes it to file, confirming the correct editing of the message.

deleteAccount
Calls the method readUsers
Returns true or false if the account is successfully deleted. 
Delete the corresponding personal file attached to this account.
Removes the account from the usersFile and confirms the change. 

saveSettings
Receives the information for the account. The user is only capable of changing the name (not the username) and the password for the account. 
It changes the account’s name and password, by writing to the file usersFile the requested change.

readOtherUserConversations
This method reads the list of conversations from another specified user. Adds these conversations to the Arraylist otherUserConversations and returns it.

writeUsersToFile
First checks if the file usersFile exists, if not then create one.
This method is used to save any changes to the file usersFile.
Makes sure that data is stored and is persistent even if the program closes. 

deleteConversation
Receives the name of the conversation that the user wishes to delete.
Removes the conversation from the Clients list of Conversations.
This will remove the conversation from only the clients list. The conversation is not deleted from any other participants in the conversation. 

## Program Testing 
### GUI Manual Testing 
Once the program was completed, different situations that the user might encounter while using the program were tested to make sure the program does not crash and that the GUI displays the proper frames. All of our GUI was located in the ClientGUI class and all user interactions for this program were done with GUI.

Signup for an account was tested by creating 3 different users. It was tested that an error message would return if users clicked the signup button with any of the text fields empty or with an username that was already taken. This is to make sure that the username can be used as a unique identifier for each user and that user account information will be collected when an account is created. It was checked that users could not login without creating an account by logging with a random username and password. 

After logging in, it was checked that the first user could create a private conversation with user 2 and then a group conversation with user 2 and 3. Messages were sent among all 3 users to make sure that these messages were appearing in real time. This also made sure that applications could support simultaneous use by multiple users over the network. Various conversations were created among these 3 users. First it was checked that all conversations were appearing in the user’s messages home page as a list.  Random conversations were then deleted from each user’s lists and it was made that when the user deleted the conversation it only deleted from their list and not the other 2 user’s list. Next the functionality for editing and deleting messages was checked. User 1 entered the editMessages frame and it was made sure they could only delete their own texts and not others texts. It was checked that these changes occurred in user 2 and user 3 screen as well. 

Next the settings panel was tested. To make sure that users were able to change their full name and password,  the user exited the program and made sure they could login in again with the new password. The “Logout” button was also tested to ensure that users could successfully leave the program. Once they did, it was checked that the data in conversations, messages, and user account information was saved to the files and still persistent when the user was not connected. Lastly, to check if  accounts were deleted properly, user that deleted their account checked that they could not log back in with the same username and password. It was also checked that another person could create an account with the account username that was deleted. 

Throughout all of these texts, descriptive errors appeared as appropriate and the application did not crash under any circumstance. 
RunLocalTest.java (JUnit Testing)

RunLocalTest.java is a JUnit Testing class that will check if the program has the correct implementations. This class checks and verifies that each class exists and inherits from the correct superclass. It verifies that each field in every class exists and verifies that it has the correct type and access modifier. It verifies that each method in every class exists with the correct return type and modifier. Every method in the program has been tested with proper input and improper input. Upon proper input the program will function properly with no errors being thrown. Upon improper input, a descriptive error will be thrown. All classes, methods, and fields are tested. Listed below are methods that were tested in RunLocalTest.java...

### List of methods tested from Server.java class:
main(String[] args)

List of methods tested from User.java class:
getName()
setName(String newName)
getUsername()
getPassword()
setPassword(String newPassword)
getConversations()

List of methods tested in Conversation.java:
getName()
getParticipants()
getMessages()

List of methods tested from ClientGui.java class:
main(String[] args)
readUsers()
readConversations()
logIn(String logIn)
signUp(String signUp)
searchUser(String userSearch)
addUserToConversation(String userInput)
createConversation(String userInput)
editMessage(String userInput)
deleteMessage(String userInput)
editSpecificMessage(String userInput)
deleteAccount()
saveSettings(String userInfo)
readOtherUserConversations(User otherUser)
writeUsersToFile()
deleteConversation(String userInfo)

List of methods tested from UserHandler.java class:
readUsers()
readConversations()
logIn(String logIn)
signUp(String signUp)
searchUser(String userSearch)
addUserToConversation(String userInput)
createConversation(String userInput)
editMessage(String userInput)
deleteMessage(String userInput)
editSpecificMessage(String userInput)
deleteAccount()
saveSettings(String userInfo)
readOtherUserConversations(User otherUser)
writeUsersToFile()
deleteConversation(String userInfo)

