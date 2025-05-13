/******************
Name:
ID:
Assignment: ex2
*******************/

#include <stdio.h>

int main() {
	int menuChoice;
	while (1) {
		printf("Choose an option:\n\t");
		printf("1. Happy Face\n\t");
		printf("2. Balanced Number\n\t");
		printf("3. Generous Number\n\t");
		printf("4. Circle Of Joy\n\t");
		printf("5. Happy Numbers\n\t");
		printf("6. Festival Of Laughter\n\t");
		printf("7. Exit\n");

		scanf(" %d", &menuChoice);
		// clear input buffer to account for incorrect input
		while ( getchar() != '\n' );

		switch (menuChoice) {
			case 1:
				char eyesChar, noseChar, mouthChar;
				printf("Enter symbols for the eyes, nose, and mouth:\n");
				scanf(" %c %c %c", &eyesChar, &noseChar, &mouthChar);

				printf("Enter face size:\n");
				int faceSize;
				// loop while input is incorrect; if it is correct, break the loop and continue
				while (1) {
					scanf(" %d", &faceSize);
					if (faceSize > 0 && faceSize & 1) { break; }

					printf("The face's size must be an odd and positive number, please try again:\n");
				}

				// print eyes
				printf("%c%*c%c\n", eyesChar, faceSize, ' ', eyesChar);
				// print nose
				printf("%*c%c\n", (faceSize+1)/2, ' ', noseChar);
				// print mouth
				printf("\\");
				for (int i = 0; i < faceSize; i++) { printf("%c", mouthChar); }
				printf("/\n");

				break;

			case 2:
				int balancedInput = 0, digits = 0;
				printf("Enter a number:\n");
				// loop while input is incorrect; if it is correct, break the loop and continue
				while (1) {
					scanf("%d", &balancedInput);
					if (balancedInput > 0) { break; }

					printf("Only positive number is allowed, please try again:\n");
				}

				int number = balancedInput;

				// count how many digits the input has
				while (balancedInput != 0) {
					balancedInput = balancedInput / 10;
					digits++;
				}

				// calculate the sum of the right side while reducing the input number
				int leftSum = 0, rightSum = 0;
				for (int i = 0; i < digits / 2; i++) {
					rightSum = rightSum + number % 10;
					number = number / 10;
				}

				// if there is an odd number of digits skip the middle one, which happens to be the immediately next one after reducing 
				if (digits & 1) {
					number = number / 10;
				}

				// calculate the sum of the left side
				for (int i = 0; i < digits / 2; i++) {
					leftSum = leftSum + number % 10;
					number = number / 10;
				}

				if (leftSum == rightSum) {
					printf("This number is balanced and brings harmony!\n");
				} else {
					printf("This number isn't balanced and destroys harmony.\n");
				}

				break;

			case 3:
				int generousInput = 0;
				printf("Enter a number:\n");
				// loop while input is incorrect; if it is correct, break the loop and continue
				while (1) {
					scanf("%d", &generousInput);
					if (generousInput > 0) { break; }

					printf("Only positive number is allowed, please try again:\n");
				}

				/* 
				* get all the proper divisors and sum them up
				* we can iterate over up to half the input number as higher then that there will be no proper divisors
				*/
				int divisorSum = 0;
				for (int i = 1; i <= generousInput / 2; i++) {
					if (generousInput % i == 0) { divisorSum = divisorSum + i; }
				}

				if (divisorSum > generousInput) {
					printf("This number is generous!\n");
				} else {
					printf("This number does not share.\n");
				}

				break;

			case 4:
				int circleInput = 0;
				printf("Enter a number:\n");
				// loop while input is incorrect; if it is correct, break the loop and continue
				while (1) {
					scanf("%d", &circleInput);
					if (circleInput > 0) { break; }

					printf("Only positive number is allowed, please try again:\n");
				}

				int revNumber = 0;
				int orgNumber = circleInput;
				// reverse the input number into revNumber
				while (orgNumber > 0) {
					revNumber = revNumber * 10 + orgNumber % 10;
					orgNumber = orgNumber / 10; 
				}

				int isNotPrime = 0;

				// check if input is prime
				for (int i = 2; i < circleInput / 2; i++) {
					if (circleInput % i == 0) {
						isNotPrime = 1;
						break;
					}
				}

				// check if reversed input is prime
				for (int i = 2; i < revNumber / 2; i++) {
					if (revNumber % i == 0) {
						isNotPrime = 1;
						break;
					}
				}

				if (isNotPrime) {
						printf("The circle remains incomplete.\n");
				} else {
						printf("This number completes the circle of joy!\n");
				}

				break;

			case 5:
				int happyInput = 0;
				printf("Enter a number:\n");
				// loop while input is incorrect; if it is correct, break the loop and continue
				while (1) {
					scanf("%d", &happyInput);
					if (happyInput > 0) { break; }

					printf("Only positive number is allowed, please try again:\n");
				}

				printf("Between 1 and %d only these numbers bring happiness:", happyInput);

				/* 
				* ://
				* (this is an unhappy face because programming without functions feels annoying why are we doing the compilers job for it)
				* might as well copy https://oeis.org/A007770 up to uint size and just output that... O(1) time lol
				* =================== Explanation of the algorithm ===================
				* initialize a 'slow' and 'fast' variable to the input.
				* each iteration of squaring and summing up the digits, advance 'slow' by one iteration and 'fast' by two iterations
				* if a number is happy, then 'slow' and 'fast' will meet at 1, and the loop will break.
				* if a number is not happy, then it will at some point fall into some sort of loop, where 'slow' and 'fast' will meet and the loop will also break.
				* AFAIK there isn't a number that *won't* fall into one of these conditions
				*/

				int slowNum, slowCopy, fastNum, fastCopy;
				int slowSquareSum, fastSquareSum, fastSquareHalfwaySum;

				for (int numToTest = 1; numToTest <= happyInput; numToTest++) {
					slowNum = slowCopy = fastNum = fastCopy = numToTest;

					do {
						slowSquareSum = fastSquareSum = fastSquareHalfwaySum = 0;
						slowCopy = slowNum;
						fastCopy = fastNum;

						// advance 'slow' by one iteration
						while (slowCopy) {
							slowSquareSum = slowSquareSum + (slowCopy % 10) * (slowCopy % 10);
							slowCopy = slowCopy / 10;
						}
						slowNum = slowSquareSum;

						// advance 'fast' by two iterartions
						while (fastCopy) {
							fastSquareHalfwaySum = fastSquareHalfwaySum + (fastCopy % 10) * (fastCopy % 10);
							fastCopy = fastCopy / 10;
						}
						while (fastSquareHalfwaySum) {
							fastSquareSum = fastSquareSum + (fastSquareHalfwaySum % 10) * (fastSquareHalfwaySum % 10);
							fastSquareHalfwaySum = fastSquareHalfwaySum / 10;
						}
						fastNum = fastSquareSum;

					} while (slowNum != fastNum);

					if (slowNum == 1) {
						printf(" %d", numToTest);
					}
				}

				printf("\n");
				break;

			case 6:
				int smileNumber, cheerNumber;
				printf("Enter a smile and cheer number:\n");
				// loop while input is incorrect; if it is correct, break the loop and continue
				while (1) {
					if (
						scanf(" smile: %d, cheer: %d", &smileNumber, &cheerNumber) == 2 && \
						smileNumber > 0 && \
						cheerNumber > 0 && \
						smileNumber != cheerNumber ) { break; } 
					else {
						printf("Only 2 different positive numbers in the given format are allowed for the festival, please try again:\n");
						// clear input buffer
						while ( getchar() != '\n' );
					}
				}

				int festivalInput;
				printf("Enter maximum number for the festival: \n");
				// loop while input is incorrect; if it is correct, break the loop and continue
				while (1) {
					scanf("%d", &festivalInput);
					if (festivalInput > 0) { break; }

					printf("Only positive maximum number is allowed, please try again:\n");
				}

				// check divisibility and print the correct output
				for (int i = 1; i <= festivalInput; i++) {
					if (i % smileNumber == 0 && i % cheerNumber == 0) {
						printf("Festival!\n");
					} else if (i % smileNumber == 0) {
						printf("Smile!\n");
					} else if (i % cheerNumber == 0) {
						printf("Cheer!\n");
					} else {
						printf("%d\n", i);
					}
				}
				break;

			case 7:
				printf("Thank you for your journey through Numeria!\n");
				return 0;
			
			default:
				printf("This option is not available, please try again.\n");
		}
	}






        // Case 1: Draw Happy Face with given symbols for eyes, nose and mouse
        /* Example:
        * n = 3:
        * 0   0
        *   o
        * \___/
        */
    

        // Case 2: determine whether the sum of all digits to the left of the middle digit(s)
        // and the sum of all digits to the right of the middle digit(s) are equal
        /* Examples:
        Balanced: 1533, 450810, 99
        Not blanced: 1552, 34
        Please notice: the number has to be bigger than 0.
        */

        // Case 3: determine whether the sum of the proper divisors (od an integer) is greater than the number itself
        /* Examples:
        Abudant: 12, 20, 24
        Not Abudant: 3, 7, 10
        Please notice: the number has to be bigger than 0.
        */

        // Case 4: determine wether a number is a prime.
        /* Examples:
        This one brings joy: 3, 5, 11
        This one does not bring joy: 15, 8, 99
        Please notice: the number has to be bigger than 0.
        */
    

        // Happy numbers: Print all the happy numbers between 1 to the given number.
        // Happy number is a number which eventually reaches 1 when replaced by the sum of the square of each digit
        /* Examples:
        Happy :) : 7, 10
        Not Happy :( : 5, 9
        Please notice: the number has to be bigger than 0.
        */


        // Festival of Laughter: Prints all the numbers between 1 the given number:
        // and replace with "Smile!" every number that divided by the given smile number
        // and replace with "Cheer!" every number that divided by the given cheer number
        // and replace with "Festival!" every number that divided by both of them
        /* Example:
        6, smile: 2, cheer: 3 : 1, Smile!, Cheer!, Smile!, 5, Festival!
        */
        return 0;
}
