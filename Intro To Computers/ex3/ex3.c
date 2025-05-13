/******************
Name: Gleb Shvartser
ID: 346832892
Assignment: ex3
*******************/

#include <stdio.h>

#define NUM_OF_BRANDS 5
#define BRANDS_NAMES 15
#define NUM_OF_TYPES 4
#define TYPES_NAMES 10
#define DAYS_IN_YEAR 365
#define addOne  1
#define addAll  2  
#define stats  3
#define print  4
#define insights  5
#define deltas  6
#define done  7

char brands[NUM_OF_BRANDS][BRANDS_NAMES] = {"Toyoga", "HyunNight", "Mazduh", "FolksVegan", "Key-Yuh"};
char types[NUM_OF_TYPES][TYPES_NAMES] = {"SUV", "Sedan", "Coupe", "GT"};
int cube[DAYS_IN_YEAR][NUM_OF_BRANDS][NUM_OF_TYPES];
int dayCounter = 0;


void printMenu() {
    printf("Welcome to the Cars Data Cube! What would you like to do?\n"
           "1.Enter Daily Data For A Brand\n"
           "2.Populate A Day Of Sales For All Brands\n"
           "3.Provide Daily Stats\n"
           "4.Print All Data\n"
           "5.Provide Overall (simple) Insights\n"
           "6.Provide Average Delta Metrics\n"
           "7.exit\n");
}

//function that populates the main array with specific values
int populateCube(int brand, int suv, int sedan, int coupe, int gt) {
    if (brand >= NUM_OF_BRANDS && brand < 0) {
        printf("This brand is not valid\n");
        // error, return non-zero
        return 1;
    }
    cube[dayCounter][brand][0] = suv;
    cube[dayCounter][brand][1] = sedan;
    cube[dayCounter][brand][2] = coupe;
    cube[dayCounter][brand][3] = gt;

    return 0;
}

//function that gets an array as input and sets passed pointers to the max value and its index
void arrMax(int arr[], int size, int* valPtr, int* indexPtr) {
    int maxIndex = 0;
    int max = arr[maxIndex];
    int val;

    //iterate over array, if we find an element bigger than the current max - set max to new value
    for (int i = 1; i < size; i++) {
        val = arr[i];
        if (val > max) {
            max = val;
            maxIndex = i;
        }
    }

    //set passed pointers to result
    *valPtr = max;
    *indexPtr = maxIndex;
}

void addOneFunc() {
    //useless function... why make bother with it if the days between brands aren't supposed to desync?
    int inputBrand, inputSUV, inputSedan, inputCoupe, inputGT;
    scanf("%d %d %d %d %d", &inputBrand, &inputSUV, &inputSedan, &inputCoupe, &inputGT);
    populateCube(inputBrand, inputSUV, inputSedan, inputCoupe, inputGT);\
    dayCounter++;
}

void addAllFunc() {
    int inputBrand, inputSUV, inputSedan, inputCoupe, inputGT;
    int populateReturnCode;

    for (int brandsLeft = NUM_OF_BRANDS; brandsLeft > 0; brandsLeft--) {
        int brandNotFinished = 1;
        while (brandNotFinished) {
            printf("No data for brands");
            // check which brands have not been populated yet
            for (int j = 0; j < NUM_OF_BRANDS; j++) {
                if (cube[dayCounter][j][0] == -1) {
                    printf(" %s", brands[j]);
                }
            }
            printf("\nPlease complete the data\n");

            scanf("%d %d %d %d %d", &inputBrand, &inputSUV, &inputSedan, &inputCoupe, &inputGT);
            populateReturnCode = populateCube(inputBrand, inputSUV, inputSedan, inputCoupe, inputGT);
            //check whether data was populated successfully 
            if (populateReturnCode == 0) { brandNotFinished = 0; }
        }
    }

    dayCounter++;
}

void statsFunc() {
    int dayToAnalyze;

    //while input is not valid
    while (1) {
        printf("What day would you like to analyze?\n");
        scanf(" %d", &dayToAnalyze);
        //printf("%d", dayCounter);
        if (dayToAnalyze > 0 && dayToAnalyze - 1 < dayCounter) {
            break;
        }
        printf("Please enter a valid day.\n");
    }

    printf("In day number %d:\n", dayToAnalyze);

    int sumOfSales = 0;
    int brandSales[NUM_OF_BRANDS] = {0};
    int typeSales[NUM_OF_TYPES] = {0};
    int sale;

    //iterate over all sales and add them to the appropriate array
    for (int brandIndex = 0; brandIndex < NUM_OF_BRANDS; brandIndex++) {
        for (int typeIndex = 0; typeIndex < NUM_OF_TYPES; typeIndex++) {
            sale = cube[dayToAnalyze-1][brandIndex][typeIndex];
            if (sale > 0) {
                sumOfSales += sale;
                brandSales[brandIndex] += sale;
                typeSales[typeIndex] += sale;
            }
        }
    }

    printf("The sales total was %d\n", sumOfSales);

    int bestBrandSaleVal, bestBrandSaleIndex;
    arrMax(brandSales, NUM_OF_BRANDS, &bestBrandSaleVal, &bestBrandSaleIndex);
    printf("The best sold brand with %d sales was %s\n", bestBrandSaleVal, brands[bestBrandSaleIndex]);

    int bestTypeSaleVal, bestTypeSaleIndex;
    arrMax(typeSales, NUM_OF_TYPES, &bestTypeSaleVal, &bestTypeSaleIndex);
    printf("The best sold type with %d sales was %s\n", bestTypeSaleVal, types[bestTypeSaleIndex]);

}

void printFunc() {
    /* 
    just a couple of months ago I was responsible for the main functionality
    of the backend on a project for Russia's largest oil company.
    today, I get 0 points on an automated test because I didn't print some stars into the console.
    god, where have I went wrong?
    (this is a joke)
    ((but not really))
    */
    printf("*****************************************\n");
    int sale;

    for (int brandIndex = 0; brandIndex < NUM_OF_BRANDS; brandIndex++) {
        printf("Sales for %s:\n", brands[brandIndex]);
        for (int dayIndex = 0; dayIndex < dayCounter; dayIndex++) {
            printf("Day %d-", dayIndex + 1);
            for (int typeIndex = 0; typeIndex < NUM_OF_TYPES; typeIndex++) {
                sale = cube[dayIndex][brandIndex][typeIndex];
                printf(" %s: %d", types[typeIndex], sale);
            }
            printf("\n");
        }
    }
    printf("*****************************************\n");
}

void insightsFunc() {
    int sale;
    int brandSales[NUM_OF_BRANDS] = {0};
    int typeSales[NUM_OF_TYPES] = {0};
    int daySales[DAYS_IN_YEAR] = {0};

    //iterate over all sales and add them to the appropriate array
    for (int dayIndex = 0; dayIndex < dayCounter; dayIndex++) {
        for (int brandIndex = 0; brandIndex < NUM_OF_BRANDS; brandIndex++) {
            for (int typeIndex = 0; typeIndex < NUM_OF_TYPES; typeIndex++) {
                sale = cube[dayIndex][brandIndex][typeIndex];

                daySales[dayIndex] += sale;
                brandSales[brandIndex] += sale;
                typeSales[typeIndex] += sale;
            }
        }
    }

    int bestBrandOverallVal, bestBrandOverallIndex;
    arrMax(brandSales, NUM_OF_BRANDS, &bestBrandOverallVal, &bestBrandOverallIndex);
    printf("The best-selling brand overall is %s: %d$\n", brands[bestBrandOverallIndex], bestBrandOverallVal);

    int bestTypeOverallVal, bestTypeOverallIndex;
    arrMax(typeSales, NUM_OF_TYPES, &bestTypeOverallVal, &bestTypeOverallIndex);
    printf("The best-selling type of car is %s: %d$\n", types[bestTypeOverallIndex], bestTypeOverallVal);

    int bestDayOverallVal, bestDayOverall;
    arrMax(daySales, DAYS_IN_YEAR, &bestDayOverallVal, &bestDayOverall);
    printf("The most profitable day was day number %d: %d$\n", bestDayOverall + 1, bestDayOverallVal);
}

void deltasFunc() {
    /*
    given formula is way too complicated for what it is... 
    intentional or not, the numerator in the delta is just last sale - first sale.
    */
    for (int brandIndex = 0; brandIndex < NUM_OF_BRANDS; brandIndex++) {
        int firstSale = 0;
        for (int typeIndex = 0; typeIndex < NUM_OF_TYPES; typeIndex++) {
            firstSale += cube[0][brandIndex][typeIndex];
        }

        int lastSale = 0;
        for (int typeIndex = 0; typeIndex < NUM_OF_TYPES; typeIndex++) {
            lastSale += cube[dayCounter-1][brandIndex][typeIndex];
        }
        float delta = (float)(lastSale - firstSale) / (dayCounter - 1);
        printf("Brand: %s, Average Delta: %f\n", brands[brandIndex], delta);
    }
}

int main() {
    // Initialize all elements to -1
    for (int i = 0; i < DAYS_IN_YEAR; i++) {
        for (int j = 0; j < NUM_OF_BRANDS; j++) {
            for (int k = 0; k < NUM_OF_TYPES; k++) {
                cube[i][j][k] = -1;
            }
        }
    }

    int choice;
    printMenu();
    scanf("%d", &choice);
    while(choice != done){
        switch(choice){
            case addOne:
                addOneFunc();
                break;

            case addAll:
                addAllFunc();
                break;

            case stats:
                statsFunc();
                break;

            case print:
                printFunc();
                break;
            
            case insights:
                insightsFunc();
                break;

            case deltas:
                deltasFunc();
                break;

            default:
                printf("Invalid input\n");
        }
        printMenu();
        scanf("%d", &choice);
    }
    printf("Goodbye!\n");
    return 0;
}
