/******************
Name: Shvartser Gleb
ID:
Assignment: ex1
*******************/
#include <stdio.h>

// REMIDER : YOU CANT USE ANY CONTROL FLOW OPERATIONS OR FUNCTIONS, ONLY BITWISE.

int main() {


  int num, pos, res;
  
  // What bit
  printf("What bit:\n");
  /*Scan two integers (representing number and a position)
  Print the bit in this position. */
  printf("Please enter a number:\n");
  scanf("%d", &num);
  printf("Please enter a position:\n");  
  scanf("%d", &pos);
  res = (num >> pos) & 1;
  printf("The bit in position %d of number %d is: %d\n", pos, num, res);
  
  // Set bit
  printf("\nSet bit:\n");
  /*Scan two integers (representing number and a position)
  Make sure the bit in this position is "on" (equal to 1)
  Print the output
  Now make sure it's "off" (equal to 0)
  Print the output */
  printf("Please enter a number:\n");
  scanf("%d", &num);
  printf("Please enter a position:\n");
  scanf("%d", &pos);
  num |= (1 << pos); // OR with bitmask at pos
  printf("Number with bit %d set to 1: %d\n", pos, num);
  num &= ~(1 << pos); // AND with inverted bitmask at pos
  printf("Number with bit %d set to 0: %d\n", pos, num);
  
  // Toggle bit
  printf("\nToggle bit:\n");
  /*Scan two integers (representing number and a position)
  Toggle the bit in this position
  Print the new number */
  printf("Please enter a number:\n");
  scanf("%d", &num);
  printf("Please enter a position:\n");
  scanf("%d", &pos);
  num ^= (1 << pos); // XOR with bitmask
  printf("Number with bit %d toggled: %d\n", pos, num);
  
  // Even - Odd
  printf("\nEven - Odd:\n");
  /* Scan an integer
  If the number is even - print 1, else - print 0. */
  printf("Please enter a number:\n");
  scanf("%d", &num);
  res = !(num & 1); // AND with 1 to check last bit and invert because even is 1 for some reason
  printf("%d\n", res);
    
  // 3, 5, 7, 11
  printf("\n3, 5, 7, 11:\n");
  /* Scan two integers in octal base
  sum them up and print the result in hexadecimal base
  Print only 4 bits, in positions: 3,5,7,11 in the result. */
  int num1, num2, sum;
  printf("Please enter the first number (octal):\n");
  scanf("%o", &num1);
  printf("Please enter the second number (octal):\n");
  scanf("%o", &num2);
  sum = num1 + num2;
  printf("The sum in hexadecimal: %X\nThe 3,5,7,11 bits are: ", sum);
  printf("%d", (sum >> 3) & 1);
  printf("%d", (sum >> 5) & 1);
  printf("%d", (sum >> 7) & 1);
  printf("%d\n", (sum >> 11) & 1);

  printf("Bye!\n");
  
  return 0;
}
