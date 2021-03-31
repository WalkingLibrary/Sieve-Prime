package com.jumbodinosaurs.sieveprime;

import com.google.gson.Gson;
import com.jumbodinosaurs.devlib.discord.DiscordWebHookAPIMessage;
import com.jumbodinosaurs.devlib.util.GeneralUtil;
import com.jumbodinosaurs.devlib.util.WebUtil;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

public class Main
{
    private static String discordWebHook;
    
    public static void main(String[] args)
    {
        if(args.length <= 0)
        {
            System.out.println("No Web Hook Given");
            System.exit(1);
        }
    
        discordWebHook = args[0];
        //https://en.wikipedia.org/wiki/Sieve_of_Eratosthenes
        ArrayList<BigInteger> primeNumbers = new ArrayList<BigInteger>();
        File listOfPrimes = GeneralUtil.checkFor(GeneralUtil.userDir, "listOfPrimes.txt");
        File progressSaver = GeneralUtil.checkFor(GeneralUtil.userDir, "progress.txt");
        String progressSaverContents = GeneralUtil.scanFileContents(progressSaver);
        BigInteger currentNotifyThreshold = new BigInteger("1000");
        ProgressObject currentProgress = progressSaverContents.equals("") ?
                                         new ProgressObject() :
                                         new Gson().fromJson(progressSaverContents, ProgressObject.class);
    
        BigInteger currentNumber = currentProgress.getCurrentNumberToCheck();
        try
        {
            while(true)
            {
            
                Scanner primeScanner = new Scanner(listOfPrimes);
                boolean isPrime = true;
                while(primeScanner.hasNextBigInteger())
                {
                    BigInteger nextPrime = primeScanner.nextBigInteger();
                
                    if(currentNumber.remainder(nextPrime).equals(new BigInteger("0")))
                    {
                        isPrime = false;
                        break;
                    }
                }
                
                
                if(isPrime)
                {
                    GeneralUtil.writeContents(listOfPrimes, "\n" + currentNumber.toString(), true);
    
                    if(currentNumber.max(currentNotifyThreshold).equals(currentNumber))
                    {
                        try
                        {
                            sendWebHookMessage("Did you know " + currentNumber.toString() + " is Prime?");
                        }
                        catch(Exception e)
                        {
            
                        }
                        currentNotifyThreshold = currentNumber.multiply(currentNumber);
                    }
    
                }
            
                //Save Progress
                currentProgress.setCurrentNumberToCheck(currentNumber.add(new BigInteger("1")));
                String currentProgressString = new Gson().toJson(currentProgress);
                GeneralUtil.writeContents(progressSaver, currentProgressString, false);
                currentNumber = currentProgress.getCurrentNumberToCheck();
            
            
            }
        }
        catch(Exception e)
        {
            sendWebHookMessage("Error: " + e.getMessage() + "\n\n\n Shutting Down");
        }
    }
    
    public static void sendWebHookMessage(String message)
    {
        DiscordWebHookAPIMessage discordAPIMessage = new DiscordWebHookAPIMessage("Sieve Prime",
                                                                                  "https" +
                                                                                  "://jumbodinosaurs.com/cog.png",
                                                                                  message);
        
        try
        {
            WebUtil.sendMessageToWebHook(discordWebHook, discordAPIMessage);
        }
        catch(IOException ioException)
        {
        
        }
    }
}