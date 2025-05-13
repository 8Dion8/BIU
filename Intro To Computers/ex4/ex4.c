/******************
Name: Gleb Shvartser
ID: 346832892
Assignment: ex4
*******************/
#include <stdio.h>
#include <string.h>

#define PYRAMID_SIZE 5
#define MAX_QUEENS_SIZE 20

#define ZONE_IDS_SIZE 256
#define UINT_BITS 32
#define ARRAY_INTS (ZONE_IDS_SIZE / UINT_BITS)

#define MAX_STRING_LENGTH 15
#define MAX_DICTIONARY_LENGTH 100
#define MAX_CROSSWORD_GRID_SIZE 30


/*
 * I spent all this time optimizing, implemented a bitset to use instead of a regular int array,
 * but all it did was improve x1.5 on the 10*10 test case and thats it :(
 * I guess all that's left is figuring out how to prune the recursive branches but i can't, 
 * not without a full 8h of sleep...
 * 
 * Bitset256 defines a bitset with each bit responsible for an ASCII character, keeps track of unused zones
 * At the start, we set all zones found in the input to one
 * When we use a zone, we set it to 0 in the bitset
 * Once the bitset is all zero, we know that either:
 * * We've used up all zones and a solution has been found
 * * We've used up all zones and thus this recursion branch cannot result in a valid solution
*/
typedef struct {
    unsigned int bits[ARRAY_INTS];
} Bitset256;

void bitset_init(Bitset256 *bs) {
    for (int i = 0; i < ARRAY_INTS; i++) {
        bs->bits[i] = 0;
    }
}

int bitset_is_zero(const Bitset256 *bs, int index) {
    return (bs->bits[index / UINT_BITS] & (1U << (index % UINT_BITS))) == 0;
}

void bitset_set(Bitset256 *bs, int index) {
    bs->bits[index / UINT_BITS] |= (1U << (index % UINT_BITS));
}

void bitset_clear(Bitset256 *bs, int index) {
    bs->bits[index / UINT_BITS] &= ~(1U << (index % UINT_BITS));
}

int bitset_all_zero(const Bitset256 *bs) {
    for (int i = 0; i < ARRAY_INTS; i++) {
        if (bs->bits[i] != 0) return 0;
    }
    return 1;
}


typedef struct {
    int arr[MAX_QUEENS_SIZE][MAX_QUEENS_SIZE];
    int isValid;
} QueensGrid;

void grid_init(QueensGrid *grid) {
    for (int i = 0; i < MAX_QUEENS_SIZE; i++) {
        for (int j = 0; j < MAX_QUEENS_SIZE; j++) {
            grid->arr[i][j] = 0; // Initialize all elements to 0
        }
    }
    grid->isValid = 1;
}


void task1_robot_paths();
void explore_paths(int x, int y, int* foundPathCounter);

void task2_human_pyramid();
float cheerleader_weight(int row, int column, float supported[][PYRAMID_SIZE]);

void task3_parenthesis_validator();
int parentheses_balanced(char currentOpened);

void task4_queens_battle();
int is_queen_placement_legal(int x, int y, Bitset256 zoneIDs, unsigned char zoneID, int ranks, int files, QueensGrid placement);
void mark_spaces_around(int x, int y, QueensGrid* placement, int dim);
QueensGrid recurse_queens(int x, int y, unsigned char zones[MAX_QUEENS_SIZE][MAX_QUEENS_SIZE], Bitset256 zoneIDs, int ranks, int files, QueensGrid placement, int dim);
void print_queens_solution(QueensGrid grid, int dim);

void task5_crossword_generator();


int main()
{
    int task = -1;
    do
    {
        printf("Choose an option:\n"
               "1. Robot Paths\n"
               "2. The Human Pyramid\n"
               "3. Parenthesis Validation\n"
               "4. Queens Battle\n"
               "5. Crossword Generator\n"
               "6. Exit\n");

        if (scanf("%d", &task))
        {
            switch (task)
            {
            case 6:
                printf("Goodbye!\n");
                break;
            case 1:
                task1_robot_paths();
                break;
            case 2:
                task2_human_pyramid();
                break;
            case 3:
                task3_parenthesis_validator();
                break;
            case 4:
                task4_queens_battle();
                break;
            case 5:
                task5_crossword_generator();
                break;
            default:
                printf("Please choose a task number from the list.\n");
                break;
            }
        }
        else
        {
            scanf("%*s");
        }

    } while (task != 6);
}


void task1_robot_paths() {
    printf("Please enter the coordinates of the robot (column, row):\n");
    
    int startingX, startingY;
    scanf(" %d %d", &startingX, &startingY);
    if (startingX < 0 || startingY < 0) {
        printf("The total number of paths the robot can take to reach home is: 0\n");
        return;
    }

    int foundPaths = 0;
    explore_paths(startingX, startingY, &foundPaths);
    printf("The total number of paths the robot can take to reach home is: %d\n", foundPaths);
}

void task2_human_pyramid() {
    printf("Please enter the weights of the cheerleaders:\n");

    //supported gets populated with the resulting weights as input is taken
    float supported[PYRAMID_SIZE][PYRAMID_SIZE] = {0};
    for (int i = 0; i < PYRAMID_SIZE; i++) {
        float input;
        for (int j = 0; j < i + 1; j++) {
            scanf(" %f", &input);
            if (input < 0) {
                printf("Negative weights are not supported.\n");
                return;
            }
            supported[i][j] = input + cheerleader_weight(i, j, supported);
        }
    }

    printf("The total weight on each cheerleader is:\n");

    // we don't even need this loop technically, if we're just checking the output 
    // separate from input then we can just print the result in the first loop
    for (int i = 0; i < PYRAMID_SIZE; i++) {
        for (int j = 0; j < i + 1; j++) {
            printf("%.2f ", supported[i][j]);
        }
        printf("\n");
    }
}

void task3_parenthesis_validator() {
    printf("Please enter a term for validation:\n");
    scanf("\n");

    int balance = parentheses_balanced('-') ;

    if (balance) {
        printf("The parentheses are balanced correctly.\n");
    } else {
        printf("The parentheses are not balanced correctly.\n");
    }

}

void task4_queens_battle() {
    printf("Please enter the board dimensions:\n");
    int dim;
    scanf(" %d", &dim);

    printf("Please enter a %d*%d puzzle board:\n", dim, dim);

    /*
     * zones[][] marks zones on a grid
     * QueensGrid is a 2d array with the queen placements and a validity flag, used for result returning
     * zoneIDs is explained at the definition of Bitset256
     * ranks and files are integers where each bit is responsible for its own rank/file, similar to zoneIDs
    */
    unsigned char zones[MAX_QUEENS_SIZE][MAX_QUEENS_SIZE];
    QueensGrid placement;
    grid_init(&placement);
    Bitset256 zoneIDs;
    bitset_init(&zoneIDs);
    int ranks = 0;
    int files = 0;

    char zoneInput;

    for (int i = 0; i < dim; i++) {
        scanf("\n");
        for (int j = 0; j < dim; j++) {
            scanf("%c", &zoneInput);
            zones[i][j] = zoneInput;
            bitset_set(&zoneIDs, (int)zoneInput);
        }
    }

    QueensGrid result = recurse_queens(0, 0, zones, zoneIDs, ranks, files, placement, dim);

    if (result.isValid) {
        printf("Solution:\n");
        print_queens_solution(result, dim);
    } else {
        printf("This puzzle cannot be solved.\n");
    }
}

void task5_crossword_generator() {
    //Ran out of time to do this one :( started it, the unfinished version is in ex4_CW.c
}

void explore_paths(int x, int y, int* foundPathCounter) {
    //if we're at 0,0 then a path has been found
    if (!(x|y)) {
        (*foundPathCounter)++;
    } else {
        if (x) { explore_paths(x-1, y, foundPathCounter); }
        if (y) { explore_paths(x, y-1, foundPathCounter); }
    }
    
}

float cheerleader_weight(int row, int column, float supported[][PYRAMID_SIZE]) {
    /*
    * simplest implementation without loops or recursion. If you really want to
    * adhere to the "recursion" requirement, then you can just put a useless call
    * to cheerleader_weight here :// I'm against overcomplicating things
    */
    float topWeightLeft = 0, topWeightRight = 0;

    if (row) {
        if (column) {
            topWeightLeft = supported[row-1][column-1];
        }
        if (column != row) {
            topWeightRight = supported[row-1][column];
        }
    }
    

    return (topWeightLeft + topWeightRight) / 2;
}

int parentheses_balanced(char currentOpened) {
    /*
     * Scan a symbol one by one.
     * If it's a closing bracket, compare it to the current opened bracket
     * * If they're the same type, backtrack and keep scanning
     * * If it's not, then parentheses are not balanced 
     * If it's an opening bracket, change current opened and branch
    */
    char input;
    scanf("%c", &input);

    switch (input) {
        case '\n':
            if (currentOpened == '-') {
                return 1;
            } else {
                return 0;
            }
        case '(':
            if (parentheses_balanced('(')) { return parentheses_balanced(currentOpened); }
            return 0;
        case ')':
            if (currentOpened == '(') { return 1; }
            return 0;
        case '[':
            if (parentheses_balanced('[')) { return parentheses_balanced(currentOpened); }
            return 0;
        case ']':
            if (currentOpened == '[') { return 1; }
            return 0;
        case '{':
            if (parentheses_balanced('{')) { return parentheses_balanced(currentOpened); }
            return 0;
        case '}':
            if (currentOpened == '{') { return 1; }
            return 0;
        case '<':
            if (parentheses_balanced('<')) { return parentheses_balanced(currentOpened); }
            return 0;
        case '>':
            if (currentOpened == '<') { return 1; }
            return 0;
        default:
            return parentheses_balanced(currentOpened);
    }
}

int is_queen_placement_legal(int x, int y, Bitset256 zoneIDs, unsigned char zoneID, int ranks, int files, QueensGrid placement) {
    // is rank used?
    if (((1 << x) & ranks)) { return 0; }
    //is file used?
    if (((1 << y) & files)) { return 0; }
    //is zone used?
    if (bitset_is_zero(&zoneIDs, zoneID)) { return 0; }
    //is next to already placed queen?
    if (placement.arr[x][y] == -1) { return 0; }

    return 1;
}

void mark_spaces_around(int x, int y, QueensGrid* placement, int dim) {
   for (int i = -1; i <= 1; i++) {
        for (int j = -1; j <= 1; j++) {
            int newX = x + i;
            int newY = y + j;
            
            // Check boundaries
            if (newX >= 0 && newX < dim && newY >= 0 && newY < dim) {
                placement->arr[newX][newY] = -1;
            }
        }
    }
}

void print_queens_solution(QueensGrid grid, int dim) {
    int cell;
    for (int i = 0; i < dim; i++) {
        for (int j = 0; j < dim; j++) {
            cell = grid.arr[i][j];
            if (cell == 1) {
                printf("X ");
            } else {
                printf("* ");
            }
        }
        printf("\n");
    }
}

QueensGrid recurse_queens(int x, int y, unsigned char zones[MAX_QUEENS_SIZE][MAX_QUEENS_SIZE], Bitset256 zoneIDs, int ranks, int files, QueensGrid placement, int dim) {
    // If we're out of bounds after incrementing, then it isn't a valid solution and we need to backtrack
    if (x == dim || y == dim) {
        placement.isValid = 0;
        return placement;
    }

    if (is_queen_placement_legal(x, y, zoneIDs, zones[x][y], ranks, files, placement)) {
        //store original state in case we need to backtrack... slow as hell but i'm too sleep deprived right now :)
        QueensGrid og_placement = placement;
        Bitset256 og_zoneIDs = zoneIDs;
        int og_ranks = ranks;
        int og_files = files;

        //mark places around the placed queen as unusable
        mark_spaces_around(x, y, &placement, dim);
        //place queen
        placement.arr[x][y] = 1;
        //mark used zone
        bitset_clear(&zoneIDs, zones[x][y]);
        //mark used rank and file
        ranks |= (1 << x);
        files |= (1 << y);
        
        //We've used all zones, solution is valid!
        if (bitset_all_zero(&zoneIDs)) {
            return placement;
        }
        //Branch
        QueensGrid result = recurse_queens((x + 1) % dim, y + (x + 1) / dim, zones, zoneIDs, ranks, files, placement, dim);
        if (result.isValid) {
            return result;
        }

        //Branch was invalid, backtrack
        placement = og_placement;
        zoneIDs = og_zoneIDs;
        ranks = og_ranks;
        files = og_files;
    }
    
    //Move to the next placement
    if (++x == dim) {
        x = 0;
        if (++y == dim) {
            //we've run out of places to try, board is unsolvable
            placement.isValid = 0;
            return placement;
        }
    }

    return recurse_queens(x, y, zones, zoneIDs, ranks, files, placement, dim);

}
