/**********************************************/
/*Anish Adhikari                              */
/*Dec 12, 2018                               */
/*CS 241L    Section #002                     */

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include "wumpus.h"

/* Generates a random integer in the range [0, n-1], inclusive. */
int getRandomInt(int n)
{
  int r = rand(); /* r = [0, RAND_MAX] */

  /* x = [0, 1) */
  double x = (double)r / ((double)RAND_MAX + 1.0);
  /* Without +1.0, there is a 1 in RAND_MAX */
  /* chance of returning n. */

  /* return: [0, n-1] */
  return (int)(x * n);
}

void printCurrentLocation(int player, struct Map* map)
{
  int i;
  printf("\nYou are in room %d\n", player);
  printf("There are exits to rooms ");
  for (i = 0; i < map->exitsPerRoom; i++)
  {
    printf("%d ", getNextRoom(map, player, i));
  }
  printf("\n");
}

void warnNearHazard(int player, int hazard, struct Map* map, char* message)
{
  if (isNeighbor(player, hazard, map))
  {
    printf("%s\n", message);
  }
}

int main(int argc, char** argv)
{
  int wumpus;
  int pit;
  int bats;
  int player;
  int arrows = 5;
  int gameOn = 1;

  char* mapFile = argc > 1 ? argv[1] : "dodecahedron.map";
  FILE* inputFile;

  struct Map* map;

  long seed = (long)time(NULL);
  srand(seed);

  printf("Loading map from %s\n", mapFile);
  inputFile = fopen(mapFile, "r");
  map = loadMap(inputFile);
  fclose(inputFile);
  printf("Loaded map with %d rooms, %d exits/room\n",
         map->rooms, map->exitsPerRoom);

  wumpus = getRandomInt(map->rooms);
  pit = getRandomInt(map->rooms);
  bats = getRandomInt(map->rooms);
  player = getRandomInt(map->rooms);

  printf("Welcome to Hunt the Wumpus\n");

  while (gameOn)
  {
    printCurrentLocation(player, map);

    warnNearHazard(player, wumpus, map, "You smell the wumpus.");
    warnNearHazard(player, pit, map, "You feel the wind from the pit.");
    warnNearHazard(player, bats, map, "You hear bats screeching.");

    if (player == wumpus)
    {
      printf("The wumpus ate you\n");
      gameOn = 0;
    }
    else if (player == pit)
    {
      printf("You fell in the pit and died\n");
      gameOn = 0;
    }
    else if (player == bats)
    {
      player = getRandomInt(map->rooms);
      printf("Bats carried you to room %d\n", player);
      bats = getRandomInt(map->rooms);
    }
    else
    {
      int read = -1;
      int choice;
      char c;

      while (read < 0)
      {
        printf("Shoot or Move? (S/M)\n");
        do
        {
          read = scanf("%c", &c);
          if (read != 1) printf("I'm confused\n");
        } while (c == ' ' || c == '\n' || c == '\t');
      }
