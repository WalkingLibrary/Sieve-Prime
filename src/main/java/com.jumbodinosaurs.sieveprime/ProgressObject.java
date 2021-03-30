package com.jumbodinosaurs.sieveprime;

import java.math.BigInteger;

public class ProgressObject
{
    private BigInteger currentNumberToCheck;
    
    public ProgressObject()
    {
        this.currentNumberToCheck = new BigInteger("2");
    }
    
    public BigInteger getCurrentNumberToCheck()
    {
        return currentNumberToCheck;
    }
    
    public void setCurrentNumberToCheck(BigInteger currentNumberToCheck)
    {
        this.currentNumberToCheck = currentNumberToCheck;
    }
}
