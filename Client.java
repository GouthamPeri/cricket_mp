import java.io.*;
import java.net.*;
import java.util.Random;
public class Client extends Thread
{
	static int sscore=-1,score=0;
	void clientbowl(int cscore,BufferedReader keyRead,PrintWriter pwrite,BufferedReader receiveRead) throws Exception
	{
		int a,b,flag=0;
		if(cscore==-1)
		{
			flag=1;
		}
		while(true)
		{
			System.out.print("Client:");
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
			System.out.println("Server:"+b);
			Client.sscore=Integer.parseInt(receiveRead.readLine());
			if(a==b || (Client.sscore>Client.score&&flag==0))
			{
				String s;
				System.out.println("Server has scored:"+Client.sscore);
				return;
			}
		}		
	}
	void clientbat(int cscore,BufferedReader keyRead,BufferedReader receiveRead,PrintWriter pwrite) throws Exception
	{
		pwrite.flush();
		int flag=0;
		if(cscore!=-1)
			flag=1;
		int receiveMessage=0,a;
		String rm;
		while(true)
		{
			System.out.print("Client:");
			String input = keyRead.readLine();
			if(input == null || input.isEmpty() || input.length() > 1)
				a = 0;
			char c = input.charAt(0);
			if(c < '0' || c > '6')
				a = 6;
			else
				a=Integer.parseInt(input);
			System.out.println("waiting for server reply");
			if((rm = receiveRead.readLine())!=null)  
			{
				receiveMessage=Integer.parseInt(rm);
				System.out.println("server:"+receiveMessage); 
			}
			if(a==receiveMessage)
			{
				System.out.println("OUT!!!");
				System.out.println("You have scored:"+Client.score);
				pwrite.println(String.valueOf(a));
				pwrite.println(String.valueOf(Client.score));
				if(flag==1)
					System.out.println("You lost the match");
				Thread.sleep(2000);
				return;
			}
			Client.score+=a;
			pwrite.println(String.valueOf(a)); 
			pwrite.println(String.valueOf(Client.score));            
			pwrite.flush();
			if(flag==1&&Client.score>Client.sscore)
			{
				System.out.println("You have scored:"+Client.score);
				System.out.println("You won the match");
				pwrite.println("You lost the match\n");
				Thread.sleep(2000);
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
			pwrite.println("Client has choosen heads");
		}
		else
		{
			System.out.println("You choose tails");		
			pwrite.println("Client has choosen tails");
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
			pwrite.println("Client won the toss");
			res=2;
			pwrite.println(String.valueOf(res));
		}
		else
		{
			System.out.println("Server won the toss");
			pwrite.println("You won the toss");
			res=1;
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
			pwrite.println("Client has choosen to bat first");
			pwrite.println("1");
			clientbat(-1,keyRead,receiveRead,pwrite);
			clientbowl(0,keyRead,pwrite,receiveRead);
		}
		else
		{
			System.out.println("You choose to bowl");		
			pwrite.println("Client has choosen to bowl first");
			pwrite.println("2");
			clientbowl(-1,keyRead,pwrite,receiveRead);
			clientbat(sscore,keyRead,receiveRead,pwrite);
		}
	}
	void blost(BufferedReader keyRead,PrintWriter pwrite,BufferedReader receiveRead) throws Exception
	{
		System.out.println(receiveRead.readLine());
		int a=Integer.parseInt(receiveRead.readLine());
		if(a==1)
		{
			clientbowl(-1,keyRead,pwrite,receiveRead);
			clientbat(sscore,keyRead,receiveRead,pwrite);
		}
		else
		{
			clientbat(-1,keyRead,receiveRead,pwrite);
			clientbowl(0,keyRead,pwrite,receiveRead);
		}
	}
    public static void main(String[] args) throws Exception
    {
        Client c=new Client();
        int res;
		BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));
        String ip, name;
        System.out.println("Enter your name:");
        name = keyRead.readLine();
        System.out.println(name + ", please enter the Server IP Address:");
        ip = keyRead.readLine();
        Socket sock = new Socket(ip,3000);
                                // reading from keyboard (keyRead object)
        
                                // sending to server (pwrite object)
        OutputStream ostream = sock.getOutputStream(); 
        PrintWriter pwrite = new PrintWriter(ostream, true);

                                // receiving from server ( receiveRead  object)
        InputStream istream = sock.getInputStream();
        BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));
        System.out.println("Lets start the game, toss time");
        if(receiveRead.readLine().equals("1"))
        {
        res=c.tossclost(receiveRead);
        }
        else
        {
        res=c.tosscwon(keyRead,pwrite);
        }
        if(res==1)
        {
            c.blost(keyRead,pwrite,receiveRead);
        }
        else
        {
            c.bwon(keyRead,pwrite,receiveRead);
        }            
    }                    
}