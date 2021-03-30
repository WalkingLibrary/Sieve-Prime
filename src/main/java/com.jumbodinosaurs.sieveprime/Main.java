package com.jumbodinosaurs.sieveprime;

import com.google.gson.Gson;
import com.jumbodinosaurs.devlib.util.GeneralUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

public class Main
{
    
    public static void main(String[] args)
    {
        
        //https://en.wikipedia.org/wiki/Sieve_of_Eratosthenes
        ArrayList<BigInteger> primeNumbers = new ArrayList<BigInteger>();
        File listOfPrimes = GeneralUtil.checkFor(GeneralUtil.userDir, "listOfPrimes.txt");
        File progressSaver = GeneralUtil.checkFor(GeneralUtil.userDir, "progress.txt");
        String progressSaverContents = GeneralUtil.scanFileContents(progressSaver);
        
        ProgressObject currentProgress = progressSaverContents.equals("") ?
                                         new ProgressObject() :
                                         new Gson().fromJson(progressSaverContents, ProgressObject.class);
        
        BigInteger currentNumber = currentProgress.getCurrentNumberToCheck();
        
        while(true)
        {
            try
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
                }
                
                //Save Progress
                currentProgress.setCurrentNumberToCheck(currentNumber.add(new BigInteger("1")));
                String currentProgressString = new Gson().toJson(currentProgress);
                GeneralUtil.writeContents(progressSaver, currentProgressString, false);
                currentNumber = currentProgress.getCurrentNumberToCheck();
                
            }
            catch(FileNotFoundException e)
            {
                e.printStackTrace();
            }
            
        }
    }
}