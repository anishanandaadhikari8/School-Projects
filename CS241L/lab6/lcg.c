
/********************************/
/* Anthony Galczak              */
/* 10-27-16                     */
/* CS-241 Section 001           */
/* Lab 6                        */
/* Linear Congruential Generator*/
/* that utilizes a cipher algo  */
/* to encrypt and decrypt text  */
/********************************/



#ifndef LCG_H
#define LCG_H

#include "lcg.h"


struct LinearCongruentialGenerator
{
  unsigned long m; /* modulus */
  unsigned long c; /* increment */
  unsigned long a; /* multiplier */
  unsigned long x; /* value in sequence */
};

unsigned long getNextRandomValue(struct LinearCongruentialGenerator* lcg)
{ 
  unsigned long temp = lcg->x; 
  lcg->x = (lcg->a * lcg->x + lcg->c) % lcg->m;
  return temp;
}


/* Returns a struct with m, c, a, x */
struct LinearCongruentialGenerator makeLCG(unsigned long m, unsigned long c)
{
  struct LinearCongruentialGenerator lcg;
  unsigned long productOfFactors;
  unsigned long a;
  unsigned long divisor;
  
  productOfFactors = 1;
  divisor = 2;

  lcg.m = m;
  lcg.c = c;
  
  /* Generate a using a = 1 + 2p (if 4 is a factor of m), otherwise
     a = 1 + p
     p = (product of m's unique prime factors) */
  
  /* Calculating p */
  while(m > 1)
  {
  
    if((m % divisor) == 0)
    {
      productOfFactors *= divisor;
      
      /* While m is divisible by the divisor, eliminate that factor from m */
      while ((m % divisor) == 0)
      {
        m = m / divisor;
      }
    }
    
    divisor++;
  }
  
  /* Getting a based on whether or not m is divisible by 4 */
  if((lcg.m % 4) == 0)
  {
    a = (1 + (2*productOfFactors));
  }
  else
  {
    a = (1 + productOfFactors);
  }
  
  lcg.a = a;
  lcg.x = c;

  /* If a is > m then set all values to 0 */
  /*if(a > m)
  {
    lcg.m = 0;
    lcg.c = 0;
    lcg.a = 0;
    lcg.x = 0;
  }*/ 
 
  return lcg;
}

#endif
