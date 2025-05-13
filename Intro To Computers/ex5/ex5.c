/******************
Name: Gleb Shvartser
ID: 346832892
Assignment: ex5
*******************/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>


#define MAIN_MENU_OPTION_WATCH 1
#define MAIN_MENU_OPTION_ADD 2
#define MAIN_MENU_OPTION_REMOVE 3
#define MAIN_MENU_OPTION_EXIT 4

#define PLAYLIST_MENU_OPTION_SHOW 1
#define PLAYLIST_MENU_OPTION_ADD 2
#define PLAYLIST_MENU_OPTION_DELETE 3
#define PLAYLIST_MENU_OPTION_SORT 4
#define PLAYLIST_MENU_OPTION_PLAY 5
#define PLAYLIST_MENU_OPTION_EXIT 6

#define SORT_MENU_OPTION_YEAR 1
#define SORT_MENU_OPTION_ASCENDING 2
#define SORT_MENU_OPTION_DESCENDING 3
#define SORT_MENU_OPTION_STREAMS 4

#define TRUE 1

// Songs and playlists use a singly-linked list structure. Each struct represents a node of the list.
typedef struct Song {
    char* title;
    char* artist;
    int year;
    char* lyrics;
    int streams;
    struct Song* next;
} Song;

typedef struct Playlist {
    Song* head;
    int length;
    char* name;
    struct Playlist* next;
} Playlist;

typedef struct Library {
    struct Playlist* head;
    int length;
} Library;

// Struct init functions
Song* init_song();
Playlist* init_playlist();
Library* init_library();

// Utils
void add_playlist(Library* library, Playlist* playlist);
void add_song(Playlist* playlist);
void delete_playlist(Library* library, Playlist* playlist);
void delete_song(Playlist* playlist, Song* song);
void delete_all_songs(Playlist* playlist, Song* song);
void free_library(Library* library);
char* get_line();
Playlist* get_playlist_at_index(Playlist* playlist, int currentIndex, int targetIndex);
Song* get_song_at_index(Song* song, int currentIndex, int targetIndex);
Song* merge_sort(Song* head, int(*comparator)(Song*, Song*));
Song* merge_linked_lists(Song* head0, Song* head1, int(*comparator)(Song*, Song*));

// Print functions
void print_main_menu();
void print_playlist(Playlist* playlist);
void print_library(Library* library);
void print_playlist_ops_menu();
void print_sorting_menu();
void play_song(Song* song);
void play_playlist(Playlist* playlist);

// Menus
void menu_playlist_ops(Playlist* playlist);
void menu_watch_playlists(Library* library);
void menu_add_playlists(Library* library);
void menu_show_playlist(Playlist* playlist);
void menu_sort_playlist(Playlist* playlist);
void menu_delete_playlist(Library* library);
void menu_delete_song(Playlist* playlist);

// Comparator functions for song sorting
int compare_by_year(Song* leftNode, Song* rightNode);
int compare_by_streams_asc(Song* leftNode, Song* rightNode);
int compare_by_streams_des(Song* leftNode, Song* rightNode);
int compare_alphabetically(Song* leftNode, Song* rightNode);



int main() {
    Library* library = init_library();
    int choice = -1;
    do {
        print_main_menu();

        if (scanf("%d", &choice)) {
            switch (choice) {
                case MAIN_MENU_OPTION_WATCH:
                    menu_watch_playlists(library);
                    break;
                case MAIN_MENU_OPTION_ADD:
                    menu_add_playlists(library);
                    break;
                case MAIN_MENU_OPTION_REMOVE:
                    menu_delete_playlist(library);
                    break;
            }
        }
        else { scanf("%*s"); }
    } while (choice != MAIN_MENU_OPTION_EXIT);

    printf("Goodbye!\n");
    free_library(library);
    return 0;
}



Song* init_song() {
    Song* song = (Song*)malloc(sizeof(Song));
    song->streams = 0;
    song->next = NULL;

    return song;
}

Playlist* init_playlist() {
    Playlist* playlist = (Playlist*)malloc(sizeof(Playlist));
    playlist->head = NULL;
    playlist->length = 0;
    playlist->name = NULL;
    playlist->next = NULL;

    return playlist;
}

Library* init_library() {
    Library* library = (Library*)malloc(sizeof(Library));
    library->head = NULL;
    library->length = 0;

    return library;
}


void add_playlist(Library* library, Playlist* playlist) {
    // No playlist exists yet, set head
    if (library->head == NULL) {
        library->head = playlist;
        library->length = 1;
        return;
    }

    // Find tail
    Playlist* tailPlaylist = library->head;
    while ((tailPlaylist->next) != NULL) { tailPlaylist = tailPlaylist->next; }

    tailPlaylist->next = playlist;
    library->length++;
}

void delete_playlist(Library* library, Playlist* playlist) {
    delete_all_songs(playlist, playlist->head);
    free(playlist->name);

    // If playlist is the head of the library, change head. Else, find tail.
    if (library->head == playlist) {
        library->head = library->head->next;
    } else {
        Playlist* currentPlaylist = library->head;
        while (currentPlaylist != playlist) {
            currentPlaylist = currentPlaylist->next;
        }
        currentPlaylist->next = currentPlaylist->next->next;
    }

    free(playlist);
    library->length--;
}

void add_song(Playlist* playlist) {
    Song* song = init_song();
    printf("Enter song's details\nTitle:\n");
    song->title = get_line();
    printf("Artist:\n");
    song->artist = get_line();
    printf("Year of release:\n");
    scanf(" %d", &(song->year));
    printf("Lyrics:\n");
    song->lyrics = get_line();
    
    // If this is the first song, set head. Otherwise, find tail
    if (playlist->head == NULL) {
        playlist->head = song;
        playlist->length = 1;
        return;
    }

    Song* tailSong = playlist->head;
    while ((tailSong->next) != NULL) { tailSong = tailSong->next; }

    tailSong->next = song;
    playlist->length++;
}

void delete_song(Playlist* playlist, Song* song) {
    // If song is the head, change head.
    if (playlist->head == song) {
        playlist->head = playlist->head->next;
    } else {
        Song* currentSong = playlist->head;
        while (currentSong->next != song) {
            currentSong = currentSong->next;
        }
        currentSong->next = currentSong->next->next;
    }
    
    free(song->artist);
    free(song->lyrics);
    free(song->title);
    free(song);
    playlist->length--;
}

void delete_all_songs(Playlist* playlist, Song* song) {
    // This function is recursive in order to delete songs one-by-one from the tail;
    // First, recurse until tail, and then start deleting from top of stack
    // Otherwise we would lose links to next nodes
    if (song == NULL) { return; }
    delete_all_songs(playlist, song->next);
    free(song->artist);
    free(song->lyrics);
    free(song->title);
    free(song);
}

void free_library(Library* library) {
    Playlist* current = library->head;
    while (current != NULL) {
        Playlist* next = current->next;
        delete_playlist(library, current);
        current = next;
    }
    free(library);
}

char* get_line() {
    // Input function that inputs a string of undefined size until a newline.
    scanf("\n");

    // Init an empty string
    int length = 0;
    char* string;
    string = malloc(sizeof((length+1)*sizeof(char)));
    if (string == NULL) { return NULL; }
    string[0] = '\0';

    int buf;
    while ((buf = getchar()) != '\n' && buf != EOF) {
        // Scan characters one by one and reallocate if it is valid
        length++;

        char* tmp = realloc(string, (length+1)*sizeof(char));

        if (tmp == NULL) {
            free(string);
            return NULL;
        }

        string = tmp;
        string[length-1] = (char)buf;
        string[length] = '\0';
    }

    return string;
}

Playlist* get_playlist_at_index(Playlist* playlist, int currentIndex, int targetIndex) {
    // Recursive function that traverses the linked list until target index is reached
    if (playlist->next == NULL) { return NULL; }
    if (currentIndex == targetIndex-1) { return playlist->next; }
    else { return get_playlist_at_index(playlist->next, currentIndex+1, targetIndex); }
}

Song* get_song_at_index(Song* song, int currentIndex, int targetIndex) {
    // Recursive function that traverses the linked list until target index is reached
    if (song->next == NULL) { return NULL; }
    if (currentIndex == targetIndex-1) { return song->next; }
    else { return get_song_at_index(song->next, currentIndex+1, targetIndex); }
}

Song* merge_sort(Song* head, int(*comparator)(Song*, Song*)) {
    // base case
    if (head == NULL || head->next == NULL) {
        return head;
    }

    /*
    * Split the current part in half
    * Define a fast and slow pointer. Iterate over the part.
    * Slow pointer goes one node at a time, fast two.
    * When fast pointer reaches end - slow is at the middle
    */
    Song* fast = head;
    Song* slow = head;

    while (fast != NULL && fast->next != NULL) {
        fast = fast->next;
        fast = fast->next;
        slow = slow->next;
    }

    Song* left = head;
    Song* right = slow;

    // Split at.. the node before right. 
    // This is the exact moment I realized doubly-linked lists would make more sense but no way I'm rewriting anything
    // So lets just iterate again okay :D
    Song* tmp = head;
    while (tmp->next != slow) { tmp = tmp->next; }
    tmp->next = NULL;

    // Sort both parts and merge
    left = merge_sort(left, comparator);
    right = merge_sort(right, comparator);

    return merge_linked_lists(left, right, comparator);

}

Song* merge_linked_lists(Song* head0, Song* head1, int(*comparator)(Song*, Song*)) {
    // dummy head node to construct the final list from
    Song* head = init_song();
    Song* tail = head;

    // Add correct node to final list, update tail
    while (head0 != NULL || head1 != NULL) {
        if (comparator(head0, head1)) {
            tail->next = head1;
            tail = tail->next;
            head1 = head1->next;
        } else {
            tail->next = head0;
            tail = tail->next;
            head0 = head0->next;
        }
    }

    //Remove dummy head node
    Song* merged = head->next;
    free(head);

    return merged;
}


void print_main_menu() {
    printf("Please Choose:\n"); 
    printf("\t1. Watch playlists\n\t2. Add playlist\n\t3. Remove playlist\n\t4. exit\n");   
}

void print_playlist(Playlist* playlist) {
    Song* song = playlist->head;

    if (song == NULL) {
        return;
    }

    int songNum = 1;
    do {
        printf("%d. Title: %s\n", songNum++, song->title);
        printf("   Artist: %s\n", song->artist);
        printf("   Released: %d\n", song->year);
        printf("   Streams: %d\n\n", song->streams);

    // Traverse list until end
    } while ((song = song->next) != NULL);

}

void print_library(Library* library) {
    Playlist* playlist = library->head;

    if (playlist == NULL) {
        printf("\t1. Back to main menu\n");

        return;
    }

    int playlistNum = 1;
    do {
        printf("\t%d. %s\n", playlistNum++, playlist->name);

    // Traverse list until end
    } while ((playlist = playlist->next) != NULL);

    printf("\t%d. Back to main menu\n", playlistNum);
}

void print_playlist_ops_menu() {
    printf("\n\t1. Show Playlist\n\t2. Add Song\n\t3. Delete Song\n\t4. Sort\n\t5. Play\n\t6. exit\n");
}

void print_sorting_menu() {
    printf("choose:\n");
    printf("1. sort by year\n");
    printf("2. sort by streams - ascending order\n");
    printf("3. sort by streams - descending order\n");
    printf("4. sort alphabetically\n");
}

void play_song(Song* song) {
    printf("Now playing %s:\n", song->title);
    printf("$ %s $\n", song->lyrics);
    song->streams++;
}

void play_playlist(Playlist* playlist) {
    Song* currentSong = playlist->head;

    // Traverse list until end
    while (currentSong != NULL) {
        play_song(currentSong);
        printf("\n");
        currentSong = currentSong->next;
    }
}


void menu_playlist_ops(Playlist* playlist) {
    printf("playlist %s:", playlist->name);
    int choice = -1;
    do {
        print_playlist_ops_menu();

        if (scanf("%d", &choice)) {
            switch (choice) {
                case PLAYLIST_MENU_OPTION_SHOW:
                    menu_show_playlist(playlist);
                    break;
                case PLAYLIST_MENU_OPTION_ADD:
                    add_song(playlist);
                    break;
                case PLAYLIST_MENU_OPTION_DELETE:
                    menu_delete_song(playlist);
                    break;
                case PLAYLIST_MENU_OPTION_SORT:
                    menu_sort_playlist(playlist);
                    break;
                case PLAYLIST_MENU_OPTION_PLAY:
                    play_playlist(playlist);
                    break;
                
            }
        }
    } while (choice != PLAYLIST_MENU_OPTION_EXIT);
}

void menu_watch_playlists(Library* library) {
    int choice;

    while (TRUE) {
        printf("Choose a playlist:\n");
        print_library(library);

        scanf(" %d", &choice);

        if (choice == library->length+1) { return; }

        Playlist* chosenPlaylist;
        if (choice == 1) { chosenPlaylist = library->head; }
        else { chosenPlaylist = get_playlist_at_index(library->head, 0, choice-1); }
        
        menu_playlist_ops(chosenPlaylist);
    }
    
}

void menu_add_playlists(Library* library) {
    printf("Enter playlist's name:\n");
    char* playlistName = get_line();
    Playlist* playlist = init_playlist();
    playlist->name = playlistName;
    add_playlist(library, playlist);
}

void menu_show_playlist(Playlist* playlist) {
    print_playlist(playlist);

    printf("choose a song to play, or 0 to quit:\n");

    int choice;
    while (TRUE) {
        scanf(" %d", &choice);

        Song* chosenSong;
        if (choice == 0) { return; }
        if (choice == 1) { chosenSong = playlist->head; }
        else { chosenSong = get_song_at_index(playlist->head, 0, choice-1); } 
        play_song(chosenSong);

        printf("choose a song to play, or 0 to quit:\n");
    }
}

void menu_sort_playlist(Playlist* playlist) {
    print_sorting_menu();

    int choice;
    if (scanf(" %d", &choice)) {
        switch (choice) {
            case SORT_MENU_OPTION_YEAR:
                playlist->head = merge_sort(playlist->head, compare_by_year);
                break;
            case SORT_MENU_OPTION_ASCENDING:
                playlist->head = merge_sort(playlist->head, compare_by_streams_asc);
                break;
            case SORT_MENU_OPTION_DESCENDING:
                playlist->head = merge_sort(playlist->head, compare_by_streams_des);
                break;
            case SORT_MENU_OPTION_STREAMS:
                playlist->head = merge_sort(playlist->head, compare_alphabetically);
                break;
        }
    }

    printf("sorted\n");
    
}

void menu_delete_playlist(Library* library) {
    printf("Choose a playlist:\n");
    print_library(library);

    int choice;
    scanf(" %d", &choice);

    if (choice == library->length+1) { return; }

    Playlist* chosenPlaylist;
    if (choice == 1) { chosenPlaylist = library->head; }
    else { chosenPlaylist = get_playlist_at_index(library->head, 0, choice-1); }

    delete_playlist(library, chosenPlaylist);
    printf("Playlist deleted.\n");
}

void menu_delete_song(Playlist* playlist) {
    print_playlist(playlist);

    printf("choose a song to delete, or 0 to quit:\n");

    int choice;
    scanf(" %d", &choice);

    Song* chosenSong;
    if (choice == 0) { return; }
    if (choice == 1) { chosenSong = playlist->head; }
    else { chosenSong = get_song_at_index(playlist->head, 0, choice-1); } 

    delete_song(playlist, chosenSong);

    printf("Song deleted successfully.\n");
}


int compare_by_year(Song* leftNode, Song* rightNode) {
    if (leftNode == NULL) { return 1; }
    if (rightNode == NULL) { return 0; }
    if (leftNode->year > rightNode->year) { return 1; }
    return 0;
}

int compare_by_streams_asc(Song* leftNode, Song* rightNode) {
    if (leftNode == NULL) { return 1; }
    if (rightNode == NULL) { return 0; }
    if (leftNode->streams > rightNode->streams) { return 1; }
    return 0;
}

int compare_by_streams_des(Song* leftNode, Song* rightNode) {
    if (leftNode == NULL) { return 1; }
    if (rightNode == NULL) { return 0; }
    if (leftNode->streams > rightNode->streams) { return 0; }
    return 1;
}

int compare_alphabetically(Song* leftNode, Song* rightNode) {
    if (leftNode == NULL) { return 1; }
    if (rightNode == NULL) { return 0; }
    if (strcmp(leftNode->title, rightNode->title) > 0) { return 1; }
    return 0;
}