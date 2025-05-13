#include "ex6.h"
#include <ctype.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define INT_BUFFER                 128
#define NUM_POKEMON                151
#define NODEARRAY_INITIAL_CAPACITY 8

// ================================================
// Basic struct definitions from ex6.h assumed:
//   PokemonData { int id; char *name; PokemonType TYPE; int hp; int attack; EvolutionStatus CAN_EVOLVE; }
//   PokemonNode { PokemonData* data; PokemonNode* left, *right; }
//   OwnerNode   { char* ownerName; PokemonNode* pokedexRoot; OwnerNode *next, *prev; }
//   OwnerNode* ownerHead;
//   const PokemonData pokedex[];
// ================================================

// --------------------------------------------------------------
// 1) Safe Input + Utility
// --------------------------------------------------------------

void trimWhitespace(char *str) {
    // Remove leading spaces/tabs/\r
    int start = 0;
    while (str[start] == ' ' || str[start] == '\t' || str[start] == '\r')
        start++;

    if (start > 0) {
        int idx = 0;
        while (str[start])
            str[idx++] = str[start++];
        str[idx] = '\0';
    }

    // Remove trailing spaces/tabs/\r
    int len = (int)strlen(str);
    while (len > 0 && (str[len - 1] == ' ' || str[len - 1] == '\t' || str[len - 1] == '\r')) {
        str[--len] = '\0';
    }
}

char *myStrdup(const char *src) {
    if (!src)
        return NULL;
    size_t len = strlen(src);
    char *dest = (char *)malloc(len + 1);
    if (!dest) {
        printf("Memory allocation failed in myStrdup.\n");
        return NULL;
    }
    strcpy(dest, src);
    return dest;
}

int readIntSafe(const char *prompt) {
    char buffer[INT_BUFFER];
    int value;
    int success = 0;

    while (!success) {
        printf("%s", prompt);

        // If we fail to read, treat it as invalid
        if (!fgets(buffer, sizeof(buffer), stdin)) {
            printf("Invalid input.\n");
            clearerr(stdin);
            continue;
        }

        // 1) Strip any trailing \r or \n
        //    so "123\r\n" becomes "123"
        size_t len = strlen(buffer);
        if (len > 0 && (buffer[len - 1] == '\n' || buffer[len - 1] == '\r'))
            buffer[--len] = '\0';
        if (len > 0 && (buffer[len - 1] == '\r' || buffer[len - 1] == '\n'))
            buffer[--len] = '\0';

        // 2) Check if empty after stripping
        if (len == 0) {
            printf("Invalid input.\n");
            continue;
        }

        // 3) Attempt to parse integer with strtol
        char *endptr;
        value = (int)strtol(buffer, &endptr, 10);

        // If endptr didn't point to the end => leftover chars => invalid
        // or if buffer was something non-numeric
        if (*endptr != '\0') {
            printf("Invalid input.\n");
        } else {
            // We got a valid integer
            success = 1;
        }
    }
    return value;
}

char *getDynamicInput() {
    char *input = NULL;
    size_t size = 0, capacity = 1;
    input = (char *)malloc(capacity);
    if (!input) {
        printf("Memory allocation failed.\n");
        return NULL;
    }

    int c;
    while ((c = getchar()) != '\n' && c != EOF) {
        if (size + 1 >= capacity) {
            capacity *= 2;
            char *temp = (char *)realloc(input, capacity);
            if (!temp) {
                printf("Memory reallocation failed.\n");
                free(input);
                return NULL;
            }
            input = temp;
        }
        input[size++] = (char)c;
    }
    input[size] = '\0';

    // Trim any leading/trailing whitespace or carriage returns
    trimWhitespace(input);

    return input;
}

const char *getTypeName(PokemonType type) {
    switch (type) {
        case GRASS:
            return "GRASS";
        case FIRE:
            return "FIRE";
        case WATER:
            return "WATER";
        case BUG:
            return "BUG";
        case NORMAL:
            return "NORMAL";
        case POISON:
            return "POISON";
        case ELECTRIC:
            return "ELECTRIC";
        case GROUND:
            return "GROUND";
        case FAIRY:
            return "FAIRY";
        case FIGHTING:
            return "FIGHTING";
        case PSYCHIC:
            return "PSYCHIC";
        case ROCK:
            return "ROCK";
        case GHOST:
            return "GHOST";
        case DRAGON:
            return "DRAGON";
        case ICE:
            return "ICE";
        default:
            return "UNKNOWN";
    }
}

int getStarterName(int choice) {
    return (choice - 1) * 3;
}

// --------------------------------------------------------------
// 2) Creating & Freeing Nodes
// --------------------------------------------------------------

PokemonNode *createPokemonNode(const PokemonData *data) {
    PokemonNode *newPokemon = (PokemonNode *)malloc(sizeof(PokemonNode));
    newPokemon->data = data;
    newPokemon->left = NULL;
    newPokemon->right = NULL;

    return newPokemon;
}

OwnerNode *createOwner(char *ownerName, PokemonNode *starter) {
    OwnerNode *newOwner = (OwnerNode *)malloc(sizeof(OwnerNode));
    newOwner->ownerName = ownerName;
    newOwner->pokedexRoot = starter;

    if (ownerHead == NULL) {
        newOwner->prev = newOwner;
        newOwner->next = newOwner;
        ownerHead = newOwner;
    } else {
        OwnerNode *tail = ownerHead->prev;
        tail->next = newOwner;
        newOwner->prev = tail;
        newOwner->next = ownerHead;
        ownerHead->prev = newOwner;
    }

    return newOwner;
}

void freePokemonNode(PokemonNode *node) {
    free(node);
    node = NULL;
}

void freePokemonTree(PokemonNode *root) {
    BFSGeneric(root, freePokemonNode);
}

void freeOwnerNode(OwnerNode *owner) {
    freePokemonTree(owner->pokedexRoot);
    free(owner->ownerName);
    free(owner);
    owner = NULL;
}

void mergePokedexes(OwnerNode *ownerA, OwnerNode *ownerB) {
    // Add all pokemon from OwnerB to Queue
    BFSGeneric(ownerB->pokedexRoot, addNodeToMergeQueue);

    // Insert pokemon from queue to ownerA
    QueueNode *cur = mergeQueue;
    do {
        cur->pokemonNode->left = NULL;
        cur->pokemonNode->right = NULL;
        ownerA->pokedexRoot = insertPokemonNode(ownerA->pokedexRoot, cur->pokemonNode);
        cur = cur->next;
    } while (cur != NULL);
    
    // Free queue
    cur = mergeQueue;
    QueueNode *next;
    do {
        next = cur->next;
        free(cur);
        cur = next;
    } while (cur != NULL);

    mergeQueue = NULL;
}

/* ------------------------------------------------------------
   3) BST Insert, Search, Remove
   ------------------------------------------------------------ */

PokemonNode *insertPokemonNode(PokemonNode *root, PokemonNode *newNode) {
    if (root == NULL) {
        root = newNode;
        return root;
    }

    PokemonNode *cur = root;
    PokemonNode *parent = NULL;
    // Traverse the tree until we find the correct node place
    while (cur != NULL) {
        parent = cur;
        if (newNode->data->id > cur->data->id) {
            cur = cur->right;
        } else if (newNode->data->id < cur->data->id) {
            cur = cur->left;
        } else {
            // Node already exists in tree
            freePokemonNode(newNode);
            newNode = NULL;
            return root;
        }
    }
    
    // Update parent of added node
    if (parent->data->id > newNode->data->id) {
        parent->left = newNode;
    } else {
        parent->right = newNode;
    }

    return root;
}

PokemonNode *searchPokemonBFS(PokemonNode *root, int id) {
    if (!root) {
        return NULL;
    }
    searchIO->targetID = id;
    return BFSSearch(root, markFoundNode);
}

PokemonNode *removeNodeBST(PokemonNode *root, int id) {
    if (root == NULL) {
        return NULL;
    }

    // Locate the node to be deleted
    searchIO->targetID = id;
    PokemonNode *nodeToDelete = BFSSearch(root, markFoundNode);

    if (nodeToDelete == NULL) {
        // Node not found, return the original tree
        return root;
    }

    // Find the parent of the node to be deleted
    searchIO->targetID = nodeToDelete->data->id;
    PokemonNode *parent = BFSSearch(root, markParentNode);

    // Case 1: Node to be deleted is a leaf node
    if (nodeToDelete->left == NULL && nodeToDelete->right == NULL) {
        if (parent == NULL) {
            // Node to be deleted is the root
            freePokemonNode(root);
            return NULL;
        }
        if (parent->left == nodeToDelete) {
            parent->left = NULL;
        } else {
            parent->right = NULL;
        }
        freePokemonNode(nodeToDelete);
    }
    // Case 2: Node to be deleted has only one child
    else if (nodeToDelete->left == NULL || nodeToDelete->right == NULL) {
        PokemonNode *child = (nodeToDelete->left != NULL) ? nodeToDelete->left : nodeToDelete->right;
        if (parent == NULL) {
            // Node to be deleted is the root
            freePokemonNode(root);
            return child;
        }
        if (parent->left == nodeToDelete) {
            parent->left = child;
        } else {
            parent->right = child;
        }
        freePokemonNode(nodeToDelete);
    }
    // Case 3: Node to be deleted has two children
    else {
        // Find the inorder successor (minimum node in the right subtree)
        searchIO->targetID = nodeToDelete->data->id;
        PokemonNode *successor = BFSSearch(nodeToDelete->right, markSuccessorNode);

        // Copy the data from the successor to the node to be deleted
        const PokemonData *temp = successor->data;
        // Recursively delete the successor
        root = removeNodeBST(root, successor->data->id);

        nodeToDelete->data = temp;
    }

    return root;
}

void removePokemon(OwnerNode *owner, PokemonNode *pokemon) {
    owner->pokedexRoot = removeNodeBST(owner->pokedexRoot, pokemon->data->id);
}

/* ------------------------------------------------------------
   4) Generic BST Traversals (Function Pointers)
   ------------------------------------------------------------ */

void BFSGeneric(PokemonNode *root, VisitNodeFunc visit) {
    QueueNode *queueHead = initQueueNode(root);
    int queueLen = 1;

    while (queueLen) {
        if (queueHead->pokemonNode == NULL) {
            // Safeguard against freed nodes that may occur
            queueHead = dequeue(queueHead);
            queueLen--;
            continue;
        }

        // Add children to queue
        if (queueHead->pokemonNode->left) {
            enqueue(queueHead, initQueueNode(queueHead->pokemonNode->left));
            queueLen++;
        }
        if (queueHead->pokemonNode->right) {
            enqueue(queueHead, initQueueNode(queueHead->pokemonNode->right));
            queueLen++;
        }

        visit(queueHead->pokemonNode);

        queueHead = dequeue(queueHead);
        queueLen--;
    }
}

void preOrderGeneric(PokemonNode *root, VisitNodeFunc visit) {
    if (root == NULL) {
        return;
    }

    visit(root);
    preOrderGeneric(root->left, visit);
    preOrderGeneric(root->right, visit);
}

void inOrderGeneric(PokemonNode *root, VisitNodeFunc visit) {
    if (root == NULL) {
        return;
    }

    inOrderGeneric(root->left, visit);
    visit(root);
    inOrderGeneric(root->right, visit);
}

void postOrderGeneric(PokemonNode *root, VisitNodeFunc visit) {
    if (root == NULL) {
        return;
    }

    postOrderGeneric(root->left, visit);
    postOrderGeneric(root->right, visit);
    visit(root);
}

PokemonNode *BFSSearch(PokemonNode *root, VisitNodeFunc visit) {
    BFSGeneric(root, visit);
    PokemonNode *result = searchIO->result;
    clearSearchIO(searchIO);
    return result;
}

// Function to print a single Pokemon node
void printPokemonNode(PokemonNode *node) {
    if (!node)
        return;
    printf("ID: %d, Name: %s, Type: %s, HP: %d, Attack: %d, Can Evolve: %s\n",
           node->data->id,
           node->data->name,
           getTypeName(node->data->TYPE),
           node->data->hp,
           node->data->attack,
           (node->data->CAN_EVOLVE == CAN_EVOLVE) ? "Yes" : "No");
}

void markFoundNode(PokemonNode *node) {
    if (!node->data) {
        return;
    }
    if (node->data->id == searchIO->targetID) {
        searchIO->result = node;
        searchIO->resultID = node->data->id;
    }
}

void markSuccessorNode(PokemonNode *node) {
    if (!node->data) {
        return;
    }
    if (node->data->id > searchIO->targetID && node->data->id < searchIO->resultID) {
        searchIO->result = node;
        searchIO->resultID = node->data->id;
    }
}

void markParentNode(PokemonNode *node) {
    if (!node->data) {
        return;
    }
    // Could be inline but this doesn't depend on the compiler optimizing condition chains
    if (node->left) {
        if (node->left->data->id == searchIO->targetID) {
            searchIO->result = node;
            searchIO->resultID = node->data->id;
        }
    }
    if (node->right) {
        if (node->right->data->id == searchIO->targetID) {
            searchIO->result = node;
            searchIO->resultID = node->data->id;
        }
    }
}

/* ------------------------------------------------------------
   5) Custom struct handlers - SearchIO and Queue
   ------------------------------------------------------------ */

SearchIO *initSearchIO() {
    SearchIO *newResults = (SearchIO *)malloc(sizeof(SearchIO));
    clearSearchIO(newResults);

    return newResults;
}

void *clearSearchIO(SearchIO *results) {
    results->result = NULL;
    results->targetID = -1;
    results->resultID = NUM_POKEMON + 1;

    return results;
}

QueueNode *initQueueNode(PokemonNode *node) {
    QueueNode *newNode = (QueueNode *)malloc(sizeof(QueueNode));
    newNode->pokemonNode = node;
    newNode->next = NULL;

    return newNode;
}

QueueNode *enqueue(QueueNode *head, QueueNode *node) {
    QueueNode *cur = head;
    while (cur->next != NULL) {
        cur = cur->next;
    }

    cur->next = node;

    return head;
}

QueueNode *dequeue(QueueNode *head) {
    QueueNode *newHead = head->next;
    free(head);
    head = NULL;
    return newHead;
}

void addNodeToMergeQueue(PokemonNode *node) {
    if (mergeQueue == NULL) {
        mergeQueue = initQueueNode(node);
    } else {
        enqueue(mergeQueue, initQueueNode(node));
    }
}

/* ------------------------------------------------------------
   6) Display Methods (BFS, Pre, In, Post, Alphabetical)
   ------------------------------------------------------------ */

void initNodeArray(NodeArray *na, int cap) {
    na->nodes = malloc(cap * sizeof(PokemonNode *));
    na->size = 0;
    na->capacity = cap;
}

void addNode(NodeArray *na, PokemonNode *node) {
    if (na->size >= na->capacity) {
        na->capacity *= 2;
        na->nodes = realloc(na->nodes, na->capacity * sizeof(PokemonNode *));
    }
    na->nodes[na->size++] = node;
}

void collectAll(PokemonNode *root, NodeArray *na) {
    if (root == NULL)
        return;

    collectAll(root->left, na);
    addNode(na, root);
    collectAll(root->right, na);
}

int compareByNameNode(const void *a, const void *b) {
    PokemonNode *nodeA = *(PokemonNode **)a;
    PokemonNode *nodeB = *(PokemonNode **)b;
    return strcmp(nodeA->data->name, nodeB->data->name);
}

int countNodes(const PokemonNode *root) {
    // Base case: if the tree is empty, return 0
    if (root == NULL) {
        return 0;
    }

    // Recursive case: 1 (current node) + count of left subtree + count of right subtree
    return 1 + countNodes(root->left) + countNodes(root->right);
}

void displayAlphabetical(PokemonNode *root) {
    NodeArray na;
    initNodeArray(&na, NODEARRAY_INITIAL_CAPACITY);

    collectAll(root, &na);

    qsort(na.nodes, na.size, sizeof(PokemonNode *), compareByNameNode);

    for (int i = 0; i < na.size; i++) {
        printPokemonNode(na.nodes[i]);
    }

    free(na.nodes);
}

void displayBFS(PokemonNode *root) {
    BFSGeneric(root, printPokemonNode);
}

void preOrderTraversal(PokemonNode *root) {
    preOrderGeneric(root, printPokemonNode);
}

void inOrderTraversal(PokemonNode *root) {
    inOrderGeneric(root, printPokemonNode);
}

void postOrderTraversal(PokemonNode *root) {
    postOrderGeneric(root, printPokemonNode);
}

/* ------------------------------------------------------------
   7) Pokemon-Specific
   ------------------------------------------------------------ */

void pokemonFight(OwnerNode *owner) {
    if (!owner->pokedexRoot) {
        printf("Pokedex is empty.\n");
        return;
    }

    int firstID = readIntSafe("Enter ID of the first Pokemon: ");
    PokemonNode *firstPokemon = searchPokemonBFS(owner->pokedexRoot, firstID);
    int secondID = readIntSafe("Enter ID of the second Pokemon: ");
    PokemonNode *secondPokemon = searchPokemonBFS(owner->pokedexRoot, secondID);

    if (!firstPokemon || !secondPokemon) {
        printf("One or both Pokemon IDs not found.\n");
        return;
    }

    float firstPower = firstPokemon->data->attack * 1.5 + firstPokemon->data->hp * 1.2;
    printf("Pokemon 1: %s (Score = %.2f)\n", firstPokemon->data->name, firstPower);
    float secondPower = secondPokemon->data->attack * 1.5 + secondPokemon->data->hp * 1.2;
    printf("Pokemon 2: %s (Score = %.2f)\n", secondPokemon->data->name, secondPower);

    if (firstPower > secondPower) {
        printf("%s wins\n", firstPokemon->data->name);
    } else if (firstPower < secondPower) {
        printf("%s wins!\n", secondPokemon->data->name);
    } else {
        printf("It's a tie!\n");
    }
}

void evolvePokemon(OwnerNode *owner) {
    if (!owner->pokedexRoot) {
        printf("Cannot evolve. Pokedex empty.\n");
        return;
    }

    int id = readIntSafe("Enter ID of Pokemon to evolve: ");
    PokemonNode *pokemon = searchPokemonBFS(owner->pokedexRoot, id);
    if (!pokemon) {
        printf("No Pokemon with ID %d found.\n", id);
        return;
    }

    if (pokemon->data->CAN_EVOLVE == CANNOT_EVOLVE) {
        printf("%s (ID %d) cannot evolve.\n", pokemon->data->name, pokemon->data->id);
        return;
    }

    PokemonNode *evolved = searchPokemonBFS(owner->pokedexRoot, id + 1);
    if (evolved) {
        printf("Evolution ID %d (%s) already in the Pokedex. Releasing %s (ID %d).\n",
               evolved->data->id,
               evolved->data->name,
               pokemon->data->name,
               pokemon->data->id);
        printf("Removing Pokemon %s (ID %d).\n", pokemon->data->name, pokemon->data->id);
        removePokemon(owner, pokemon);
    } else {
        printf("Removing Pokemon %s (ID %d).\n", pokemon->data->name, pokemon->data->id);
        printf("Pokemon evolved from %s (ID %d) to ", pokemon->data->name, pokemon->data->id);
        removePokemon(owner, pokemon);
        evolved = createPokemonNode(&pokedex[id]);
        printf("%s (ID %d).\n", evolved->data->name, evolved->data->id);
        owner->pokedexRoot = insertPokemonNode(owner->pokedexRoot, evolved);
    }
}

void addPokemon(OwnerNode *owner) {
    int newPokemonID = readIntSafe("Enter ID to add: ");
    PokemonNode *newPokemon = createPokemonNode(&pokedex[newPokemonID - 1]);

    int ogSize = countNodes(owner->pokedexRoot);
    owner->pokedexRoot = insertPokemonNode(owner->pokedexRoot, newPokemon);
    int newSize = countNodes(owner->pokedexRoot);
    if (ogSize != newSize) {
        printf("Pokemon %s (ID %d) added.\n", newPokemon->data->name, newPokemon->data->id);
    } else {
        printf("Pokemon with ID %d is already in the Pokedex. No changes made.\n", newPokemonID);
    }
}

void freePokemon(OwnerNode *owner) {
    if (!owner->pokedexRoot) {
        printf("No Pokemon to release.\n");
        return;
    }

    int choice = readIntSafe("Enter Pokemon ID to release: ");
    PokemonNode *search = searchPokemonBFS(owner->pokedexRoot, choice);
    if (search) {
        printf("Removing Pokemon %s (ID %d).\n", search->data->name, search->data->id);
        removePokemon(owner, search);
    } else {
        printf("No Pokemon with ID %d found.\n", choice);
    }
}

/* ------------------------------------------------------------
   8) Display Menus
   ------------------------------------------------------------ */

void displayMenu(OwnerNode *owner) {
    if (!owner->pokedexRoot) {
        printf("Pokedex is empty.\n");
        return;
    }

    printf("Display:\n");
    printf("1. BFS (Level-Order)\n");
    printf("2. Pre-Order\n");
    printf("3. In-Order\n");
    printf("4. Post-Order\n");
    printf("5. Alphabetical (by name)\n");

    int choice = readIntSafe("Your choice: ");

    switch (choice) {
        case 1:
            displayBFS(owner->pokedexRoot);
            break;
        case 2:
            preOrderTraversal(owner->pokedexRoot);
            break;
        case 3:
            inOrderTraversal(owner->pokedexRoot);
            break;
        case 4:
            postOrderTraversal(owner->pokedexRoot);
            break;
        case 5:
            displayAlphabetical(owner->pokedexRoot);
            break;
        default:
            printf("Invalid choice.\n");
    }
}

void enterExistingPokedexMenu() {
    if (ownerHead == NULL) {
        printf("No existing Pokedexes.\n");
        return;
    }

    // list owners
    printf("\nExisting Pokedexes:\n");
    printAllOwners();
    int choice = readIntSafe("Choose a Pokedex by number: ");
    OwnerNode *cur = findOwnerByIndex(choice);

    printf("\nEntering %s's Pokedex...\n", cur->ownerName);

    int subChoice;
    do {
        printf("\n-- %s's Pokedex Menu --\n", cur->ownerName);
        printf("1. Add Pokemon\n");
        printf("2. Display Pokedex\n");
        printf("3. Release Pokemon (by ID)\n");
        printf("4. Pokemon Fight!\n");
        printf("5. Evolve Pokemon\n");
        printf("6. Back to Main\n");

        subChoice = readIntSafe("Your choice: ");

        switch (subChoice) {
            case 1:
                addPokemon(cur);
                break;
            case 2:
                displayMenu(cur);
                break;
            case 3:
                freePokemon(cur);
                break;
            case 4:
                pokemonFight(cur);
                break;
            case 5:
                evolvePokemon(cur);
                break;
            case 6:
                printf("Back to Main Menu.\n");
                break;
            default:
                printf("Invalid choice.\n");
        }
    } while (subChoice != 6);
}

void openPokedexMenu() {
    printf("Your name: ");
    char *name = getDynamicInput();
    if (findOwnerByName(name)) {
        printf("Owner '%s' already exists. Not creating a new Pokedex.\n", name);
        free(name);
        return;
    }

    printf("Choose Starter:\n1. Bulbasaur\n2. Charmander\n3. Squirtle\n");
    int choice = readIntSafe("Your choice: ");

    PokemonNode *starterPokemon = createPokemonNode(&pokedex[getStarterName(choice)]);
    createOwner(name, starterPokemon);

    printf("New Pokedex created for %s with starter %s.\n", name, starterPokemon->data->name);

    //free(name);
}

void deletePokedex() {
    if (ownerHead == NULL) {
        printf("No existing Pokedexes to delete.\n");
        return;
    }

    printf("\n=== Delete a Pokedex ===\n");
    printAllOwners();
    int choice = readIntSafe("Choose a Pokedex to delete by number: ");
    OwnerNode *target = findOwnerByIndex(choice);
    printf("Deleting %s's entire Pokedex...\n", target->ownerName);
    removeOwnerFromCircularList(target);
    freeOwnerNode(target);
    printf("Pokedex deleted.\n");
}

void mergePokedexMenu() {
    if (ownerHead == NULL || ownerHead->next == ownerHead) {
        printf("Not enough owners to merge.\n");
        return;
    }

    printf("\n=== Merge Pokedexes ===\n");
    printf("Enter name of first owner: ");
    char *ownerAName = getDynamicInput();
    OwnerNode *ownerA = findOwnerByName(ownerAName);
    printf("Enter name of second owner: ");
    char *ownerBName = getDynamicInput();
    OwnerNode *ownerB = findOwnerByName(ownerBName);
    printf("Merging %s and %s...\n", ownerA->ownerName, ownerB->ownerName);

    if (ownerB->pokedexRoot) {
        mergePokedexes(ownerA, ownerB);
    }

    printf("Merge completed.\n");
    printf("Owner '%s' has been removed after merging.\n", ownerBName);

    free(ownerAName);
    free(ownerBName);

    removeOwnerFromCircularList(ownerB);
    free(ownerB->ownerName);
    free(ownerB);
    ownerB = NULL;
}

void mainMenu() {
    int choice;
    do {
        printf("\n=== Main Menu ===\n");
        printf("1. New Pokedex\n");
        printf("2. Existing Pokedex\n");
        printf("3. Delete a Pokedex\n");
        printf("4. Merge Pokedexes\n");
        printf("5. Sort Owners by Name\n");
        printf("6. Print Owners in a direction X times\n");
        printf("7. Exit\n");
        choice = readIntSafe("Your choice: ");

        switch (choice) {
            case 1:
                openPokedexMenu();
                break;
            case 2:
                enterExistingPokedexMenu();
                break;
            case 3:
                deletePokedex();
                break;
            case 4:
                mergePokedexMenu();
                break;
            case 5:
                sortOwners();
                break;
            case 6:
                printOwnersCircular();
                break;
            case 7:
                printf("Goodbye!\n");
                break;
            default:
                printf("Invalid.\n");
        }
    } while (choice != 7);
}

/* ------------------------------------------------------------
   9) Sorting Owners (Bubble Sort on Circular List)
   ------------------------------------------------------------ */

void sortOwners() {
    // These could be one if but this doesn't depend on condition chain optimizations
    if (ownerHead == NULL) {
        printf("0 or 1 owners only => no need to sort.\n");
        return;
    }
    if (ownerHead->next == ownerHead) {
        printf("0 or 1 owners only => no need to sort.\n");
        return;
    }

    int swapped;
    OwnerNode *current;
    OwnerNode *tail = ownerHead->prev;
    // Cute little bubble sort :3
    do {
        swapped = 0;
        current = ownerHead;

        while (current->next != tail) {
            if (strcmp(current->ownerName, current->next->ownerName) > 0) {
                swapOwnerData(current, current->next);
                swapped = 1;
            }
            current = current->next;
        }
        tail = current;
    } while (swapped);

    printf("Owners sorted by name.\n");
}

void swapOwnerData(OwnerNode *a, OwnerNode *b) {
    if (a == NULL || b == NULL) {
        return;
    }

    // Swap ownerName
    char *tempName = a->ownerName;
    a->ownerName = b->ownerName;
    b->ownerName = tempName;

    // Swap pokedexRoot
    PokemonNode *tempPokedex = a->pokedexRoot;
    a->pokedexRoot = b->pokedexRoot;
    b->pokedexRoot = tempPokedex;
}

/* ------------------------------------------------------------
   10) Circular List Linking & Searching
   ------------------------------------------------------------ */

void removeOwnerFromCircularList(OwnerNode *target) {
    if (target == ownerHead) {
        if (ownerHead->next == ownerHead) {
            ownerHead = NULL;
            return;
        }
        ownerHead = target->next;
        ownerHead->prev = target->prev;
        target->prev->next = ownerHead;

    } else {
        OwnerNode *cur = ownerHead;
        while (cur->next != target) {
            cur = cur->next;
        }
        cur->next = target->next;
        target->next->prev = cur;
    }
}

OwnerNode *findOwnerByName(const char *name) {
    if (ownerHead == NULL) {
        return NULL;
    }
    OwnerNode *cur = ownerHead;
    do {
        if (strcmp(cur->ownerName, name) == 0) {
            return cur;
        }
        cur = cur->next;
    } while (cur != ownerHead);

    return NULL;
}

OwnerNode *findOwnerByIndex(int index) {
    OwnerNode *cur = ownerHead;
    for (int i = 1; i < index; i++) {
        cur = cur->next;
    }

    return cur;
}

/* ------------------------------------------------------------
   11) Printing Owners in a Circle
   ------------------------------------------------------------ */

void printOwnersCircular() {
    if (ownerHead == NULL) {
        printf("No owners.\n");
        return;
    }

    printf("Enter direction (F or B): ");
    char choice;
    scanf(" %c ", &choice);

    int n = readIntSafe("How many prints? ");

    OwnerNode *cur = ownerHead;
    if (choice == 'f' || choice == 'F') {
        for (int i = 1; i <= n; i++) {
            printf("[%d] %s\n", i, cur->ownerName);
            cur = cur->next;
        }
    } else {
        for (int i = 1; i <= n; i++) {
            printf("[%d] %s\n", i, cur->ownerName);
            cur = cur->prev;
        }
    }
}

void printAllOwners() {
    OwnerNode *cur = ownerHead;
    int index = 1;
    do {
        printf("%d. %s\n", index++, cur->ownerName);
        cur = cur->next;
    } while (cur != ownerHead);
}

/* ------------------------------------------------------------
   12) Cleanup All Owners at Program End
   ------------------------------------------------------------ */

void freeAllOwners() {
    if (!ownerHead) {
        return;
    }
    OwnerNode *cur = ownerHead;
    OwnerNode *head = ownerHead;
    do {
        OwnerNode *next = cur->next;
        freeOwnerNode(cur);
        cur = next;
    } while (cur != NULL && cur != head);
}


int main() {
    searchIO = initSearchIO();
    mainMenu();
    freeAllOwners();
    free(searchIO);
    return 0;
}
