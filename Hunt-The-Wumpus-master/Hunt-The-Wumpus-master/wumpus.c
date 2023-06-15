/**********************************************/
/*Anish Adhikari                              */
/*Dec 12, 2018                                */
/*CS 241L    Section #002                     */
/**********************************************/

#include <stdio.h>
#include <stdlib.h>
#include "wumpus.h"

/* @params in, type FILE
 *  contains Input Stream which can make a map
 * @returns map, type Map*
 *  Map mad out from in FIle passed in the function
 * This function makes a struct map from the input file
 *  and assigns all the value in the map strcut */


struct Map* loadMap(FILE* in)
{
  struct Map* map;
  int room;
  int exitPerRoom;
  int number=0;
  int i=0;
  int j=0;
  /* allocates memory of strcut Map to map*/
  map = malloc(sizeof(struct Map));
  /* reads the first line of Input file which contains
   *  rooms and exitsPerRoom and assigns it to map*/
  if(fscanf(in,"%d %d",&room,&exitPerRoom)!=0)
  {
    map->rooms= room;
    map->exitsPerRoom=exitPerRoom;
  }
  /* allocates required memory to map->connection*/
  map->connections= malloc(sizeof(int*)*room);
  /* allocates required memory to each element in 
   *  map->connection[i] array*/
  for (i=0;i<room;i++)
  {
    map->connections[i] =malloc(exitPerRoom*sizeof(int));
    
  }
  /*assigns the value from input file one by one to
   * map->connection[][] array*/

   for (i=0;i<room;i++)
  {
    for (j=0;j<exitPerRoom;j++)
    {
      fscanf(in,"%d",&number); 
      map->connections[i][j]=number;
    }
  }
return map;
}


/* @param map, type struct Map*
 *  contains information about the map as passed in the file
 * @param room, type int
 *  is the index of the room player is cureently on
 * @param exit, type int
 *  is the exit choice player choose in the game
 * @returns the room number based on player's choice
 * This function returns the next room based of the player
 * choice by looking through map->connection */

int nextRoom(struct Map* map, int room, int exit)
{
  return map->connections[room][exit];
}

/* @param current, type int
 *  is the index of player is currently on
 * @param test, type int
 *  is the room number which needs to be checked if its
 *  reachable or not
 * @param map, type struct Map*
 *  is the map created by the information passed in the 
 *  input file
 * @returns either 0 or 1
 * This function checkes if a given room is reachable from
 * the current room or not */
int neighbor (int current, int test, struct Map* map)
{
 int i =0;
 /* checks if the room is reachable from the
  * map->connection[current] array */
 for (i =0; i<map->exitsPerRoom; i++)
 {
  if (map->connections[current][i]==test) return 1;
 }
 return 0;

}

/* @param pathlength, type int
 *  is the length of the array pointed by path[]
 * @param path[], type int
 *  is a pointer to the array point
 * @param map, type struct Map*
 *  is the map created by the information passed in the
 *  input file
 * @return either 0 or 1
 * This function checks if the path[i+1] is reachable from
 * path[i] */
int validPath (int pathlength, int* path, struct Map* map)
{
 int i =0;
 /* uses neighbor function in a loop to check if path[i+1] 
  * is reachable from path[i]*/
 for (i =0; i< pathlength-1;i++)
 { 
  if(neighbor(path[i],path[i+1],map)==0) return 0;
 }
 return 1;
}

/* @param map type struct Map*
 *  is the map created by the information passes in the
 *  input file */
void freeMap(struct Map* map)
{
 int i =0;
 /* frees the space in the reverse order 
  * it was allocated in the load map function*/
 for (i=0;i<map->rooms;i++)
  { 
    free(map->connections[i]);
    
  }
  free(map->connections);
  free(map);
  
}

