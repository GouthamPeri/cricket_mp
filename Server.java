import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.Random;
public class Server extends Thread
{
	static int cscore=-1;
	static int score=0;
	void serverbat(int sscore,BufferedReader keyRead,BufferedReader receiveRead,PrintWriter pwrite) throws Exception
	{
		int flag=0;
		if(sscore!=-1)
			flag=1;
		int receiveMessage=0,a,score=0;
		String rm;
		while(true)
		{
			System.out.print("Server:");
			String input = keyRead.readLine();
			if(input == null || input.isEmpty() || input.length() > 1)
				a = 0;
			char c = input.charAt(0);
			if(c < '0' || c > '6')
				a = 6;
			else
				a=Integer.parseInt(input);
			if((rm = receiveRead.readLine())!=null) 
			{
				receiveMessage=Integer.parseInt(rm);
				System.out.println("client:"+receiveMessage);
			}
			if(a==receiveMessage)
			{
				System.out.println("OUT!!!");
				System.out.println("You have scored:"+Server.score);
				if(flag==1)
					System.out.println("You lost the match");
				pwrite.println(String.valueOf(a));
				pwrite.println(String.valueOf(Server.score));
				Thread.sleep(2000);
				return;
			}
			Server.score+=a;
			pwrite.println(String.valueOf(a));   
			pwrite.println(String.valueOf(Server.score));
			pwrite.flush();
			if(flag==1&&Server.score>Server.cscore)
			{
				System.out.println("You have scored:"+Server.score);
				System.out.println("You won the match");
				pwrite.println("You lost the match\n");
				Thread.sleep(2000);
				return;
			}
		}
	}
	void serverbowl(int sscore,BufferedReader keyRead,PrintWriter pwrite,BufferedReader receiveRead) throws Exception
	{
		int a,b,flag=0;
		if(sscore==-1)
		{
			flag=1;
		}
		while(true)
		{
			System.out.print("Server:");
			String input = keyRead.readLine();
			if(input == null || input.isEmpty() || input.length() > 1)
				a = 0;
			char c = input.charAt(0);
			if(c < '0' || c > '6')
				a = 6;
			else
				a=Integer.parseInt(input);
			pwrite.println(String.valueOf(a));
			b=Integer.parseInt(receiveRead.readLine());
			Server.cscore=Integer.parseInt(receiveRead.readLine());
			System.out.println("Client:"+b);
			if(a==b || (Server.cscore>Server.score && flag==0))
			{
				String s;
				System.out.println("Client has scored:"+cscore);
				return;
			}
		}		
	}
	int tosscwon(BufferedReader keyRead,PrintWriter pwrite) throws Exception
	{
		int toss,res;
		System.out.print("select ur choice(1.heads\t2.tails):");
		if((toss=Integer.parseInt(keyRead.readLine()))==1)
		{
			System.out.println("You choose heads");
			pwrite.println("Server has choosen heads");
		}
		else
		{
			System.out.println("You choose tails");		
			pwrite.println("Server has choosen tails");
		}
		Random rhort=new Random();
		int hort=rhort.nextInt(2)+1;
		if(hort==1)
		{
			System.out.println("its a heads");
			pwrite.println("its a heads");
		}
		else
		{
			System.out.println("its a tails");
			pwrite.println("its a tails");
		}
		if(hort==toss)
		{
			System.out.println("You won the toss");
			pwrite.println("server won the toss");
			res=1;
			pwrite.println(String.valueOf(res));
		}
		else
		{
			System.out.println("client won the toss");
			pwrite.println("You won the toss");
			res=2;
			pwrite.println(String.valueOf(res));
		}
		return res;
	}       
	int tossclost(BufferedReader receiveRead) throws Exception
	{
		System.out.println(receiveRead.readLine());
		System.out.println(receiveRead.readLine());
		System.out.println(receiveRead.readLine());
		int res=Integer.parseInt(receiveRead.readLine());
		return res;
	}
	void bwon(BufferedReader keyRead,PrintWriter pwrite,BufferedReader receiveRead) throws Exception
	{
		System.out.println("select ur choice(1.Batting\t2.Bowling):");
		if(Integer.parseInt(keyRead.readLine())==1)
		{
			System.out.println("You choose to bat");
			pwrite.println("Server has choosen to bat first");
			pwrite.println("1");
			serverbat(-1,keyRead,receiveRead,pwrite);
			serverbowl(0,keyRead,pwrite,receiveRead);
		}
		else
		{
			System.out.println("You choose to bowl");		
			pwrite.println("Server has choosen to bowl first");
			pwrite.println("2");
			serverbowl(-1,keyRead,pwrite,receiveRead);
			serverbat(cscore,keyRead,receiveRead,pwrite);
		}
	}
	void blost(BufferedReader keyRead,PrintWriter pwrite,BufferedReader receiveRead) throws Exception
	{
		System.out.println(receiveRead.readLine());
		int a=Integer.parseInt(receiveRead.readLine());
		if(a==1)
		{
			serverbowl(-1,keyRead,pwrite,receiveRead);
			serverbat(cscore,keyRead,receiveRead,pwrite);
		}
		else
		{
			serverbat(-1,keyRead,receiveRead,pwrite);
			serverbowl(0,keyRead,pwrite,receiveRead);
		}
	}
	public static void main(String[] args) throws Exception
	{
	  Server s=new Server();
	  int res;
      ServerSocket sersock = new ServerSocket(3000);
      System.out.println("Server is ready to play, waiting for client");
      Socket sock = sersock.accept( );                          
                              // reading from keyboard (keyRead object)
      BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));
	                      // sending to client (pwrite object)
      OutputStream ostream = sock.getOutputStream(); 
      PrintWriter pwrite = new PrintWriter(ostream, true);
 
                              // receiving from server ( receiveRead  object)
      InputStream istream = sock.getInputStream();
      BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));
 
      String receiveMessage, sendMessage; 
		Random toss_select_rand=new Random();
		int toss_s_rand=toss_select_rand.nextInt(2)+1;
		pwrite.println(String.valueOf(toss_s_rand));
		if(toss_s_rand==1)
		{
			res=s.tosscwon(keyRead,pwrite);
		}
		else
		{
			res=s.tossclost(receiveRead);
		}
		if(res==1)
		{
			s.bwon(keyRead,pwrite,receiveRead);
		}
		else{
			s.blost(keyRead,pwrite,receiveRead);
		}            
    }                    
}                        