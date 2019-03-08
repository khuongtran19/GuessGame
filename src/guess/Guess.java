/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guess;
import java.io.*;
public class Guess
{ 
	public Guess(BufferedReader file) throws IOException
	{
	    subject = file.readLine();
	    root = readTree(file);
	}
	public void saveTree(PrintWriter file) throws IOException
	{
	    file.println(subject);
	    writeTree(file, root);
	}
	public void playGame() throws IOException
	{
	    Node current = root;
	    
	    System.out.println("Please answer yes/no questions");
	    if (!askYesNo("Are you thinking of a(n) " + subject))
	    	return;
	    
	    while (current.isQuestion())
	    {
	        if (askYesNo(current.getQuestion()))
	            current = current.getYesBranch();
	        else
	            current = current.getNoBranch();
	    }
	    if (!askYesNo("Are you " + current.getGuess()))
       {
         String userAnswer, userQuestion;
			System.out.print("If you not "+current.getGuess()+"then what are you doing? ");  
			userAnswer = consoleIn.readLine();
			System.out.println("");
			userQuestion = consoleIn.readLine();
			if (askYesNo("For " + userAnswer + " the answer would be"))
		    	current.convertToQuestion(userQuestion, 
		    		new Node(current.getGuess()), new Node(userAnswer));
			else
		    	current.convertToQuestion(userQuestion,
		    		new Node(userAnswer), new Node(current.getGuess()));
	    }
	}
	public static void main(String[] args) throws IOException
	{
	    System.out.print("File to read the knowledge base from? ");
	    String filename;
	    filename = consoleIn.readLine();
	    BufferedReader knowledgeIn = 
	    	new BufferedReader(new FileReader(filename));
	    Guess theGame = new Guess(knowledgeIn);
	    knowledgeIn.close();
	    do
	    {
	        theGame.playGame();
	    }
	    while (askYesNo("Would you like to play again"));
	    System.out.print("File to write the knowledge base to - blank for none? ");
	    filename = consoleIn.readLine();
	    
	    if (filename.length() > 0)
	    {
	        PrintWriter knowledgeOut = new PrintWriter(new FileWriter(filename));
	        theGame.saveTree(knowledgeOut);
	        knowledgeOut.close();
	    }
	    
	    System.exit(0);
	}
	private String subject;		
	private Node root;	 
   private static Node readTree(BufferedReader file) throws IOException
	{
	    boolean isQuestion = ((char) file.read() == '1');
	    file.skip(1);
  	    String contents = file.readLine();
	    if (isQuestion)
	    {
	        Node ifNo = readTree(file);
	        Node ifYes = readTree(file);
	        return new Node(contents, ifNo, ifYes);
	    }
	    else
	    	return new Node(contents);
	}
	private static void writeTree(PrintWriter file, Node root) throws IOException
	{
	    file.print(root.isQuestion() ? 1 : 0);
	    file.print("");
	    if (root.isQuestion())
	    {
			file.println(root.getQuestion());
	        writeTree(file, root.getNoBranch());
	        writeTree(file, root.getYesBranch());
	    }
	    else
	    	file.println(root.getGuess());
	}
	private static boolean askYesNo(String question) throws IOException
	{
	    String answer;
	    do
	    {			
			System.out.print(question + "? ");
			answer = consoleIn.readLine();
		
			if (answer.equalsIgnoreCase("YES".substring(0, answer.length())))
			    return true;
			else if (answer.equalsIgnoreCase("NO".substring(0, answer.length())))
			    return false;
			else
			    System.out.println("Please answer yes or no");
	    }
	    while (true);
	}
	private static class Node
	{
		Node(String question, Node ifNo, Node ifYes)
		{
			isQuestion = true;
			contents = question;
			this.lchild = ifNo;
			this.rchild = ifYes;
		}		
		Node(String guess)
		{ 
			isQuestion = false;
			contents = guess;
			lchild = null;
			rchild = null;
		}
		boolean isQuestion()
		{ 
			return isQuestion; 
		}
	
		String getQuestion()
		{
			return contents;
		}
		Node getNoBranch()
		{
			return lchild;
		}
		Node getYesBranch()
		{
			return rchild; 
		}

		String getGuess()
		{ 
			return contents;
		}
		
		void convertToQuestion(String question, Node ifNo, Node ifYes)
		{
	 	    isQuestion = true;
		    contents = question;
		    lchild = ifNo;
		    rchild = ifYes;
		}
				
	    private boolean isQuestion;
	    private String contents;
  	    private Node lchild, rchild;
	 }
	private static BufferedReader consoleIn = 
		new BufferedReader(new InputStreamReader(System.in));
}
