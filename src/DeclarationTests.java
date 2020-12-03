import static org.junit.jupiter.api.Assertions.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Scanner;
import java.net.Socket;

public class DeclarationTests {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(TestCase.class);
        if (result.wasSuccessful()) {
            System.out.println("Test successful");
        } else {
            for (Failure failure : result.getFailures()) {
                System.out.println(failure.toString());
            }
        }
    }

public static class TestCase {
    private final PrintStream originalOutput = System.out;
    private final InputStream originalSysin = System.in;

    @SuppressWarnings("FieldCanBeLocal")
    private ByteArrayInputStream testIn;

    @SuppressWarnings("FieldCanBeLocal")
    private ByteArrayOutputStream testOut;

    @Before
    public void outputStart() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @After
    public void restoreInputAndOutput() {
        System.setIn(originalSysin);
        System.setOut(originalOutput);
    }

    private String getOutput() {
        return testOut.toString();
    }

    @SuppressWarnings("SameParameterValue")
    private void receiveInput(String str) {
        testIn = new ByteArrayInputStream(str.getBytes());
        System.setIn(testIn);
    }
    
    @Test(timeout = 1000)
    public void testConversationDeclarations() {
        Field[] fields = Conversation.class.getDeclaredFields();
        if (fields.length < 3) {
            fail("Ensure that you have implemented the three required fields!");
            return;
        }
        try {
            Field name = Conversation.class.getDeclaredField("nameOfConversation");
            if (name.getType() != String.class) {
                fail("Make sure the name field in conversation class is String");
                return;
            }
            if (name.getModifiers() != Modifier.PRIVATE) {
                fail("name field in conversation is not private");
                return;
            }
            
        } catch (NoSuchFieldException e) {
            fail("There's no name field in the Conversation class that's private ");
            e.printStackTrace();
            return;
        }

        try {
            Field users = Conversation.class.getDeclaredField("conversationlist");
            if (users.getType() != ArrayList.class) {
                fail("Users field in convo class isn't a user arraylist");
                return;
            }
            if (users.getModifiers() != Modifier.PRIVATE) {
                fail("users field in conversation is not private");
                return;
            }
         
        } catch (NoSuchFieldException e) {
            fail("Private Field users doesn't exist ");
            e.printStackTrace();
            return;
        }

        try {
            Field messages = Conversation.class.getDeclaredField("messages");
            if (messages.getType() != File.class) {
                fail("Field messages isn't of file type");
                return;
            }
            if (messages.getModifiers() != Modifier.PRIVATE) {
                fail("messages field in conversation is not private");
                return;
            }
       
            
        } catch (NoSuchFieldException e) {
            fail("Private Messages field doesn't exist");
            e.printStackTrace();
            return;
        }
        try {
            Method getName = Conversation.class.getDeclaredMethod("getName");
            if (getName.getModifiers() != Modifier.PUBLIC) {
                fail("Ensure that method getName in class Conversation is public");
                return;
            }
            if (!getName.getReturnType().equals(String.class)) {
                fail("Ensure that your getName method in class Conversation returns a String");
                return;
            }
           
        } catch (NoSuchMethodException e) {
            fail("A public String Method getName doesn't exist");
            e.printStackTrace();
            return;
        }
    
    try {
        Method getName = Conversation.class.getDeclaredMethod("getName");
        if (getName.getModifiers() != Modifier.PUBLIC) {
            fail("Ensure that method getName in class Conversation is public");
            return;
        }
        if (!getName.getReturnType().equals(String.class)) {
            fail("Ensure that your getName method in class Conversation returns a String");
            return;
        }
       
    } catch (NoSuchMethodException e) {
        fail("A public String method getName doesn't exist");
        e.printStackTrace();
        return;
    }

    try {
        Method addParticipant = Conversation.class.getDeclaredMethod("addParticipant");
        if (addParticipant.getModifiers() != Modifier.PUBLIC) {
            fail("Ensure that method addParticipant in class Conversation is public");
            return;
        }
        if (!addParticipant.getReturnType().equals(void.class)) {
            fail("Ensure that your addParticipant method in class Conversation returns a void");
            return;
        }
       
    } catch (NoSuchMethodException e) {
        fail("A public void Method addParticipant doesn't exist");
        e.printStackTrace();
        return;
    }
    try {
        Method removeParticipant = Conversation.class.getDeclaredMethod("removeParticipant");
        if (removeParticipant.getModifiers() != Modifier.PUBLIC) {
            fail("Ensure that method removeParticipant in class Conversation is public");
            return;
        }
        if (!removeParticipant.getReturnType().equals(void.class)) {
            fail("Ensure that your removeParticipant method in class Conversation returns a void");
            return;
        }
       
    } catch (NoSuchMethodException e) {
        fail("A public void Method removeParticipant doesn't exist");
        e.printStackTrace();
        return;
    }
    try {
        Method numberOfParticipants = Conversation.class.getDeclaredMethod("numberOfParticipants");
        if (numberOfParticipants.getModifiers() != Modifier.PUBLIC) {
            fail("Ensure that method numberOfParticipants in class Conversation is public");
            return;
        }
        if (!numberOfParticipants.getReturnType().equals(int.class)) {
            fail("Ensure that your numberOfParticipants method in class Conversation returns a integer");
            return;
        }
       
    } catch (NoSuchMethodException e) {
        fail("A public int Method numberOfParticipants doesn't exist");
        e.printStackTrace();
        return;
    }
    try {
        Method getMessages = Conversation.class.getDeclaredMethod("getMessages");
        if (getMessages.getModifiers() != Modifier.PUBLIC) {
            fail("Ensure that method getMessages in class Conversation is public");
            return;
        }
        if (!getMessages.getReturnType().equals(File.class)) {
            fail("Ensure that your getMessages method in class Conversation returns a file");
            return;
        }
       
    } catch (NoSuchMethodException e) {
        fail("A public File Method getMessages doesn't exist");
        e.printStackTrace();
        return;
    }
}
        
        
        
    @Test
	public void classExist() {
		Class<?> clazz = null;
		try {
			clazz = Class.forName("Conversation");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (clazz == null) {
			System.out.println("Class doesn't exist");
		}
	}
	
	@Test
	public void ClassImplementsSuper() {
		boolean isSuper = Object.class.isAssignableFrom(Conversation.class);
		if (isSuper == true) {
			System.out.println("Conversation class implements object superclass");
		} 
		else {
			System.out.println("This shouldn't be possible how would a class not at least implement object");
		}
	}
	
				
	
    }
	@Test(timeout = 1000)
    public void testUserDeclarations() {
        Field[] fields = User.class.getDeclaredFields();
        if (fields.length < 5) {
            fail("Ensure that you have implemented the five required fields!");
            return;
        }
        try {
            Field client = User.class.getDeclaredField("client");
            if (client.getType() != Socket.class) {
                fail("Make sure the name client in conversation class is Socket type");
                return;
            }
            if (client.getModifiers() != Modifier.PRIVATE) {
                fail("client field in User class is not private");
                return;
            }
            
        } catch (NoSuchFieldException e) {
            fail("There's no private client field in the User class ");
            e.printStackTrace();
            return;
        }

        try {
            Field name = User.class.getDeclaredField("name");
            if (name.getType() != String.class) {
                fail("name field in user class isn't a string type");
                return;
            }
            if (name.getModifiers() != Modifier.PRIVATE) {
                fail("name field in User class is not private");
                return;
            }
         
        } catch (NoSuchFieldException e) {
            fail("There's no private name field in the User class");
            e.printStackTrace();
            return;
        }

        try {
            Field username = User.class.getDeclaredField("username");
            if (username.getType() != String.class) {
                fail("Field username isn't of String type");
                return;
            }
            if (username.getModifiers() != Modifier.PRIVATE) {
                fail("username field in User class is not private");
                return;
            }
       
            
        } catch (NoSuchFieldException e) {
            fail("private username field in User class doesn't exist");
            e.printStackTrace();
            return;
        }
       
        try {
            Field password = User.class.getDeclaredField("password");
            if (password.getType() != String.class) {
                fail("Field password isn't of String type");
                return;
            }
            if (password.getModifiers() != Modifier.PRIVATE) {
                fail("password field in User class is not private");
                return;
            }
       
            
        } catch (NoSuchFieldException e) {
            fail("private password field in user class doesn't exist");
            e.printStackTrace();
            return;
        }
        try {
            Field conversations = User.class.getDeclaredField("conversations");
            if (conversations.getType() != File.class) {
                fail("Field conversations isn't of file type");
                return;
            }
            if (conversations.getModifiers() != Modifier.PRIVATE) {
                fail("conversations field in User class is not private");
                return;
            }
       
            
        } catch (NoSuchFieldException e) {
            fail("private conversations field doesn't exist");
            e.printStackTrace();
            return;
        }
        try {
            Field streamOut = User.class.getDeclaredField("streamOut");
            if (streamOut.getType() != PrintStream.class) {
                fail("Field streamOut isn't of PrintStream type");
                return;
            }
            if (streamOut.getModifiers() != Modifier.PRIVATE) {
                fail("streamOut field in User class is not private");
                return;
            }
       
            
        } catch (NoSuchFieldException e) {
            fail("private streamOut field in user class doesn't exist");
            e.printStackTrace();
            return;
        }
        try {
            Field streamIn = User.class.getDeclaredField("StreamIn");
            if (streamIn.getType() != InputStream.class) {
                fail("Field username isn't of String type");
                return;
            }
            if (streamIn.getModifiers() != Modifier.PRIVATE) {
                fail("streamIn field in User class is not private");
                return;
            }
       
            
        } catch (NoSuchFieldException e) {
            fail("private username field in user class doesn't exist");
            e.printStackTrace();
            return;
        }
        try {
            Method getOutStream = User.class.getDeclaredMethod("getOutStream");
            if (getOutStream.getModifiers() != Modifier.PUBLIC) {
                fail("Ensure that method getOutStream in class User is public");
                return;
            }
            if (!getOutStream.getReturnType().equals(PrintStream.class)) {
                fail("Ensure that your getOutStream method in class User returns a PrintStream");
                return;
            }
           
        } catch (NoSuchMethodException e) {
            fail("Public PrintStream eMethod getOutStream doesn't exist");
            e.printStackTrace();
            return;
        }
        try {
            Method getInputStream = User.class.getDeclaredMethod("getInputStream");
            if (getInputStream.getModifiers() != Modifier.PUBLIC) {
                fail("Ensure that method getInputStream in class User is public");
                return;
            }
            if (!getInputStream.getReturnType().equals(InputStream.class)) {
                fail("Ensure that your getInputStream method in class User returns a InputStream");
                return;
            }
           
        } catch (NoSuchMethodException e) {
            fail("public InputStream Method getInputStream doesn't exist");
            e.printStackTrace();
            return;
        }
        try {
            Method getName = User.class.getDeclaredMethod("getName");
            if (getName.getModifiers() != Modifier.PUBLIC) {
                fail("Ensure that method getName in class User is public");
                return;
            }
            if (!getName.getReturnType().equals(String.class)) {
                fail("Ensure that your getName method in class User returns a String");
                return;
            }
           
        } catch (NoSuchMethodException e) {
            fail("public String Method getName doesn't exist");
            e.printStackTrace();
            return;
        }
        try {
            Method setName = User.class.getDeclaredMethod("setName");
            if (setName.getModifiers() != Modifier.PUBLIC) {
                fail("Ensure that method setName in class User is public");
                return;
            }
            if (!setName.getReturnType().equals(void.class)) {
                fail("Ensure that your setName method in class User returns nothing(void)");
                return;
            }
           
        } catch (NoSuchMethodException e) {
            fail("public void Method setName doesn't exist");
            e.printStackTrace();
            return;
        }
        try {
            Method getUsername = User.class.getDeclaredMethod("getUsername");
            if (getUsername.getModifiers() != Modifier.PUBLIC) {
                fail("Ensure that method getUsername in class User is public");
                return;
            }
            if (!getUsername.getReturnType().equals(String.class)) {
                fail("Ensure that your getUsername method in class User returns a PrintStream");
                return;
            }
           
        } catch (NoSuchMethodException e) {
            fail("Public string Method getUsername doesn't exist");
            e.printStackTrace();
            return;
        }
        try {
            Method setUsername = User.class.getDeclaredMethod("setUsername");
            if (setUsername.getModifiers() != Modifier.PUBLIC) {
                fail("Ensure that method setUsername in class User is public");
                return;
            }
            if (!setUsername.getReturnType().equals(void.class)) {
                fail("Ensure that your setUsername method in class User returns void");
                return;
            }
           
        } catch (NoSuchMethodException e) {
            fail("Public void Method setUsername doesn't exist");
            e.printStackTrace();
            return;
        }
        try {
            Method getPassword = User.class.getDeclaredMethod("getPassword");
            if (getPassword.getModifiers() != Modifier.PUBLIC) {
                fail("Ensure that method getPassword in class User is public");
                return;
            }
            if (!getPassword.getReturnType().equals(String.class)) {
                fail("Ensure that your getPassword method in class User returns a String");
                return;
            }
           
        } catch (NoSuchMethodException e) {
            fail("public String Method getPassword doesn't exist");
            e.printStackTrace();
            return;
        }
        try {
            Method setPassword = User.class.getDeclaredMethod("setPassword");
            if (setPassword.getModifiers() != Modifier.PUBLIC) {
                fail("Ensure that method setPassword in class User is public");
                return;
            }
            if (!setPassword.getReturnType().equals(void.class)) {
                fail("Ensure that your setPassword method in class User returns a void");
                return;
            }
           
        } catch (NoSuchMethodException e) {
            fail("Public void Method setPassword doesn't exist");
            e.printStackTrace();
            return;
        }
        try {
            Method getConversations = User.class.getDeclaredMethod("getConversations");
            if (getConversations.getModifiers() != Modifier.PUBLIC) {
                fail("Ensure that method getConversations in class User is public");
                return;
            }
            if (!getConversations.getReturnType().equals(File.class)) {
                fail("Ensure that your getConversations method in class User returns a File");
                return;
            }
           
        } catch (NoSuchMethodException e) {
            fail("public file Method getConversations doesn't exist");
            e.printStackTrace();
            return;
        }
	}
	@Test(timeout = 1000)
    public void testUserHandlerDeclarations() {
        Field[] fields = UserHandler.class.getDeclaredFields();
        if (fields.length < 8) {
            fail("Ensure that you have implemented the eight required fields!");
            return;
        }
        try {
            Field socket = UserHandler.class.getDeclaredField("socket");
            if (socket.getType() != Socket.class) {
                fail("Make sure the field socket in UserHandler class is Socket type");
                return;
            }
            if (socket.getModifiers() != Modifier.PRIVATE) {
                fail("socket field in Userhandler class is not private");
                return;
            }
            
        } catch (NoSuchFieldException e) {
            fail("There's no private socket field in the Userhandler class ");
            e.printStackTrace();
            return;
        }
        try {
            Field userArrayList = UserHandler.class.getDeclaredField("userArrayList");
            if (userArrayList.getType() != ArrayList.class) {
                fail("Make sure the field userArrayList in UserHandler class is ArrayList type");
                return;
            }
            if (userArrayList.getModifiers() != Modifier.PRIVATE) {
                fail("userArrayList field in Userhandler class is not private");
                return;
            }
            
        } catch (NoSuchFieldException e) {
            fail("There's no private userArrayList field in the Userhandler class ");
            e.printStackTrace();
            return;
        }
        try {
            Field usersFile = UserHandler.class.getDeclaredField("usersFile");
            if (usersFile.getType() != File.class) {
                fail("Make sure the field usersFile in UserHandler class is File type");
                return;
            }
            if (usersFile.getModifiers() != Modifier.PRIVATE) {
                fail("usersFile field in Userhandler class is not private");
                return;
            }
            
        } catch (NoSuchFieldException e) {
            fail("There's no private userArrayList field in the Userhandler class ");
            e.printStackTrace();
            return;
        }
        try {
            Field usersToAdd = UserHandler.class.getDeclaredField("usersToAdd");
            if (usersToAdd.getType() != ArrayList.class) {
                fail("Make sure the field usersToAdd in UserHandler class is ArrayList type");
                return;
            }
            if (usersToAdd.getModifiers() != Modifier.PRIVATE) {
                fail("usersToAdd field in Userhandler class is not private");
                return;
            }
            
        } catch (NoSuchFieldException e) {
            fail("There's no private usersToAdd field in the Userhandler class ");
            e.printStackTrace();
            return;
        }
        try {
            Field userConversations = UserHandler.class.getDeclaredField("userConversations");
            if (userConversations.getType() != ArrayList.class) {
                fail("Make sure the field userArrayList in UserHandler class is ArrayList type");
                return;
            }
            if (userConversations.getModifiers() != Modifier.PRIVATE) {
                fail("userConversations field in Userhandler class is not private");
                return;
            }
            
        } catch (NoSuchFieldException e) {
            fail("There's no private userConversations field in the Userhandler class ");
            e.printStackTrace();
            return;
        }
        try {
            Field currentUser = UserHandler.class.getDeclaredField("currentUser");
            if (currentUser.getType() != User.class) {
                fail("Make sure the field currentUser in UserHandler class is User type");
                return;
            }
            if (currentUser.getModifiers() != Modifier.PRIVATE) {
                fail("currentUser field in Userhandler class is not private");
                return;
            }
            
        } catch (NoSuchFieldException e) {
            fail("There's no private currentUser field in the Userhandler class ");
            e.printStackTrace();
            return;
        }
        try {
            Field messagesArr = UserHandler.class.getDeclaredField("messagesArr");
            if (messagesArr.getType() != ArrayList.class) {
                fail("Make sure the field messagesArr in UserHandler class is ArrayList type");
                return;
            }
            if (messagesArr.getModifiers() != Modifier.PRIVATE) {
                fail("messagesArr field in Userhandler class is not private");
                return;
            }
            
        } catch (NoSuchFieldException e) {
            fail("There's no private messageArr field in the Userhandler class ");
            e.printStackTrace();
            return;
        }
        try {
            Field messages = UserHandler.class.getDeclaredField("messages");
            if (messages.getType() != File.class) {
                fail("Make sure the field messages in UserHandler class is File type");
                return;
            }
            if (messages.getModifiers() != Modifier.PRIVATE) {
                fail("messages field in Userhandler class is not private");
                return;
            }
            
        } catch (NoSuchFieldException e) {
            fail("There's no private messages field in the Userhandler class ");
            e.printStackTrace();
            return;
        }
        
        try {
            Method run = UserHandler.class.getDeclaredMethod("run");
            if (run.getModifiers() != Modifier.PUBLIC) {
                fail("Ensure that method run in class Userhandler is public");
                return;
            }
            if (!run.getReturnType().equals(void.class)) {
                fail("Ensure that your run method in class Userhandler returns a void");
                return;
            }
           
        } catch (NoSuchMethodException e) {
            fail("Public void Method run doesn't exist");
            e.printStackTrace();
            return;
        }
        try {
            Method ReadUsers = UserHandler.class.getDeclaredMethod("ReadUsers");
            if (ReadUsers.getModifiers() != Modifier.PUBLIC) {
                fail("Ensure that method ReadUsers in class Userhandler is public");
                return;
            }
            if (!ReadUsers.getReturnType().equals(void.class)) {
                fail("Ensure that your ReadUsers method in class Userhandler returns a void");
                return;
            }
           
        } catch (NoSuchMethodException e) {
            fail("Public void Method ReadUsers doesn't exist");
            e.printStackTrace();
            return;
        }
        try {
            Method readConversations = UserHandler.class.getDeclaredMethod("readConversations");
            if (readConversations.getModifiers() != Modifier.PRIVATE) {
                fail("Ensure that method readConversations in class Userhandler is private");
                return;
            }
            if (!readConversations.getReturnType().equals(void.class)) {
                fail("Ensure that your readConversations method in class Userhandler returns a void");
                return;
            }
           
        } catch (NoSuchMethodException e) {
            fail("Private void Method readConversations doesn't exist");
            e.printStackTrace();
            return;
        }
        try {
            Method LogIn = UserHandler.class.getDeclaredMethod("LogIn");
            if (LogIn.getModifiers() != Modifier.PUBLIC) {
                fail("Ensure that method LogIn in class Userhandler is public");
                return;
            }
            if (!LogIn.getReturnType().equals(boolean.class)) {
                fail("Ensure that your LogIn method in class Userhandler returns a boolean");
                return;
            }
           
        } catch (NoSuchMethodException e) {
            fail("Public boolean Method LogIn doesn't exist");
            e.printStackTrace();
            return;
        }
        try {
            Method SignUp = UserHandler.class.getDeclaredMethod("SignUp");
            if (SignUp.getModifiers() != Modifier.PUBLIC) {
                fail("Ensure that method SignUp in class Userhandler is public");
                return;
            }
            if (!SignUp.getReturnType().equals(boolean.class)) {
                fail("Ensure that your SignUp method in class Userhandler returns a boolean");
                return;
            }
           
        } catch (NoSuchMethodException e) {
            fail("Public boolean Method SignUp doesn't exist");
            e.printStackTrace();
            return;
        }
        try {
            Method SearchUser = UserHandler.class.getDeclaredMethod("SearchUser");
            if (SearchUser.getModifiers() != Modifier.PUBLIC) {
                fail("Ensure that method SearchUser in class Userhandler is public");
                return;
            }
            if (!SearchUser.getReturnType().equals(ArrayList.class)) {
                fail("Ensure that your SearchUser method in class Userhandler returns a ArrayList");
                return;
            }
           
        } catch (NoSuchMethodException e) {
            fail("Public ArrayList Method SearchUser doesn't exist");
            e.printStackTrace();
            return;
        }
        try {
            Method addUserToConversation = UserHandler.class.getDeclaredMethod("addUserToConversation");
            if (addUserToConversation.getModifiers() != Modifier.PUBLIC) {
                fail("Ensure that method addUserToConversation in class Userhandler is public");
                return;
            }
            if (!addUserToConversation.getReturnType().equals(void.class)) {
                fail("Ensure that your ReadUsers method in class Userhandler returns a void");
                return;
            }
           
        } catch (NoSuchMethodException e) {
            fail("Public void Method ReadUsers doesn't exist");
            e.printStackTrace();
            return;
        }
        try {
            Method createConversation = UserHandler.class.getDeclaredMethod("createConversation");
            if (createConversation.getModifiers() != Modifier.PUBLIC) {
                fail("Ensure that method createConversation in class Userhandler is public");
                return;
            }
            if (!createConversation.getReturnType().equals(boolean.class)) {
                fail("Ensure that your createConversation method in class Userhandler returns a boolean");
                return;
            }
           
        } catch (NoSuchMethodException e) {
            fail("Public boolean Method createConversation doesn't exist");
            e.printStackTrace();
            return;
        }
        try {
            Method editMessage = UserHandler.class.getDeclaredMethod("editMessage");
            if (editMessage.getModifiers() != Modifier.PRIVATE) {
                fail("Ensure that method editMessage in class Userhandler is private");
                return;
            }
            if (!editMessage.getReturnType().equals(void.class)) {
                fail("Ensure that your editMessage method in class Userhandler returns a void");
                return;
            }
           
        } catch (NoSuchMethodException e) {
            fail("Private void Method editMessage doesn't exist");
            e.printStackTrace();
            return;
        }
        try {
            Method deleteMessage = UserHandler.class.getDeclaredMethod("deleteMessage");
            if (deleteMessage.getModifiers() != Modifier.PRIVATE) {
                fail("Ensure that method deleteMessage in class Userhandler is private");
                return;
            }
            if (!deleteMessage.getReturnType().equals(void.class)) {
                fail("Ensure that your deleteMessage method in class Userhandler returns a void");
                return;
            }
           
        } catch (NoSuchMethodException e) {
            fail("Private void Method deleteMessage doesn't exist");
            e.printStackTrace();
            return;
        }
        try {
            Method editSpecificMessage = UserHandler.class.getDeclaredMethod("editSpecificMessage");
            if (editSpecificMessage.getModifiers() != Modifier.PRIVATE) {
                fail("Ensure that method editSpecificMessage in class Userhandler is private");
                return;
            }
            if (!editSpecificMessage.getReturnType().equals(void.class)) {
                fail("Ensure that your editSpecificMessage method in class Userhandler returns a void");
                return;
            }
           
        } catch (NoSuchMethodException e) {
            fail("Private void Method editSpecificMessage doesn't exist");
            e.printStackTrace();
            return;
        }
        try {
            Method deleteAccount = UserHandler.class.getDeclaredMethod("deleteAccount");
            if (deleteAccount.getModifiers() != Modifier.PRIVATE) {
                fail("Ensure that method deleteAccount in class Userhandler is private");
                return;
            }
            if (!deleteAccount.getReturnType().equals(boolean.class)) {
                fail("Ensure that your deleteAccount method in class Userhandler returns a boolean");
                return;
            }
           
        } catch (NoSuchMethodException e) {
            fail("Public boolean Method deleteAccount doesn't exist");
            e.printStackTrace();
            return;
        }
        try {
            Method saveSettings = UserHandler.class.getDeclaredMethod("saveSettings");
            if (saveSettings.getModifiers() != Modifier.PRIVATE) {
                fail("Ensure that method saveSettings in class Userhandler is private");
                return;
            }
            if (!saveSettings.getReturnType().equals(void.class)) {
                fail("Ensure that your saveSettings method in class Userhandler returns a void");
                return;
            }
           
        } catch (NoSuchMethodException e) {
            fail("Private void Method saveSettings doesn't exist");
            e.printStackTrace();
            return;
        }
        try {
            Method readOtherUserConversations = UserHandler.class.getDeclaredMethod("readOtherUserConversations");
            if (readOtherUserConversations.getModifiers() != Modifier.PRIVATE) {
                fail("Ensure that method readOtherUserConversations in class Userhandler is private");
                return;
            }
            if (!readOtherUserConversations.getReturnType().equals(ArrayList.class)) {
                fail("Ensure that your readOtherUserConversations method in class Userhandler returns a arrayList");
                return;
            }
           
        } catch (NoSuchMethodException e) {
            fail("Private arraylist Method readOtherUserConversations doesn't exist");
            e.printStackTrace();
            return;
        }
        try {
            Method writeUsersToFile = UserHandler.class.getDeclaredMethod("writeUsersToFile");
            if (writeUsersToFile.getModifiers() != Modifier.PRIVATE) {
                fail("Ensure that method writeUsersToFile in class Userhandler is private");
                return;
            }
            if (!writeUsersToFile.getReturnType().equals(void.class)) {
                fail("Ensure that your writeUsersToFile method in class Userhandler returns a void");
                return;
            }
           
        } catch (NoSuchMethodException e) {
            fail("Private void Method writeUsersToFile doesn't exist");
            e.printStackTrace();
            return;
        }
  
    }
	@Test(timeout = 1000)
    public void testMessageHandlerDeclarations() {
        Field[] fields = MessageHandler.class.getDeclaredFields();
        if (fields.length < 1) {
            fail("Ensure that you have implemented the one required field!");
            return;
        }
        try {
            Field server = MessageHandler.class.getDeclaredField("server");
            if (server.getType() != InputStream.class) {
                fail("Make sure the field server in MessageHandler class is InputStream type");
                return;
            }
            if (server.getModifiers() != Modifier.PRIVATE) {
                fail("Server field in MessageHandler class is not private");
                return;
            }
            
        } catch (NoSuchFieldException e) {
            fail("There's no private InputStream Server field in the MessageHandler class ");
            e.printStackTrace();
            return;
        }
        try {
            Method run = MessageHandler.class.getDeclaredMethod("run");
            if (run.getModifiers() != Modifier.PUBLIC) {
                fail("Ensure that method run in class MessageHandler is public");
                return;
            }
            if (!run.getReturnType().equals(void.class)) {
                fail("Ensure that your run method in class MessageHandler returns a void");
                return;
            }
           
        } catch (NoSuchMethodException e) {
            fail("Public void Method run doesn't exist in MessageHandler");
            e.printStackTrace();
            return;
        }
    }
	@Test(timeout = 1000)
    public void testClientDeclarations() {
        Field[] fields = Client.class.getDeclaredFields();
        if (fields.length < 2) {
            fail("Ensure that you have implemented the two required fields!");
            return;
        }
        try {
            Field host = Client.class.getDeclaredField("host");
            if (host.getType() != String.class) {
                fail("Make sure the field host in Client class is String type");
                return;
            }
            if (host.getModifiers() != Modifier.PRIVATE) {
                fail("host field in client class is not private");
                return;
            }
            
        } catch (NoSuchFieldException e) {
            fail("There's no private String host field in the Client class ");
            e.printStackTrace();
            return;
        }
        try {
            Field port = Client.class.getDeclaredField("port");
            if (port.getType() != int.class) {
                fail("Make sure the field port in Client class is integer type");
                return;
            }
            if (port.getModifiers() != Modifier.PRIVATE) {
                fail("port field in client class is not private");
                return;
            }
            
        } catch (NoSuchFieldException e) {
            fail("There's no private Integer port field in the Client class ");
            e.printStackTrace();
            return;
        }
        try {
            Method run = Client.class.getDeclaredMethod("run");
            if (run.getModifiers() != Modifier.PUBLIC) {
                fail("Ensure that method run in class Client is public");
                return;
            }
            if (!run.getReturnType().equals(void.class)) {
                fail("Ensure that your run method in class Client returns a void");
                return;
            }
           
        } catch (NoSuchMethodException e) {
            fail("Public void Method run doesn't exist in Client class");
            e.printStackTrace();
            return;
        }
    }
        
}