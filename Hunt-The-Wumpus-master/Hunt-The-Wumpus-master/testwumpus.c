#include <stdio.h>

#include "wumpus.h"

void test_nextRoom(FILE* out, struct Map* map)
{
  int i;
  int j;
  fprintf(out, "Testing nextRoom function...\n");
  for(i = 0; i < map->rooms; ++i)
  {
    fprintf(out, "  Exits for room %d:", i);
    for(j = 0; j < map->exitsPerRoom; ++j)
    {
      fprintf(out, " %d", nextRoom(map, i, j));
    }
    fprintf(out, "\n");
  }
}

void test_neighbor(FILE* out, struct Map* map)
{
  int i;
  int j;
  int is_adjacent;
  
  fprintf(out, "Testing neighbor function...\n");
  for(i = 0; i < map->rooms; i++)
  {
    for(j = 0; j < map->rooms; j++)
    {
      is_adjacent = neighbor(i, j, map);
      fprintf(out, "  Can %d reach %d? result = %d\n",
              i, j, is_adjacent);
    }
  }
}

void testPaths(FILE* out, struct Map* map, int pathLength, int numPaths, int* paths)
{
  int i, result;
  fprintf(out, "Test length %d paths\n", pathLength);

  for(i = 0; i < numPaths; ++i)
  {
    result = validPath(pathLength, paths + i*pathLength, map);
    fprintf(out, "  Path %d, result = %d\n", i, result);
  }
}

void test_validPath(FILE* out, struct Map* map)
{
  int p2[] =
    {
      0, 0,
      0, 1,
      0, 2,
      1, 2,
      2, 3
    };

  int p3[] =
  {
    1, 2, 1,
    1, 2, 3,
    1, 1, 2,
    3, 2, 1
  };

  int p4[] =
  {
    1, 2, 1, 2,
    1, 2, 3, 4,
    1, 1, 2, 2,
    3, 2, 1, 0
  };

  fprintf(out, "esting validPath function...\n");
  testPaths(out, map, 2, 5, p2);
  testPaths(out, map, 3, 4, p3);
  testPaths(out, map, 4, 4, p4);  
}


/**
 * Run some tests using a given map and write results to log file.
 * First command line argument is name of map file.
 * Second command line argument is name of log file for output.
 */
int main(int argc, char** argv)
{
  FILE* in;
  FILE* out;
  struct Map* map;
  char* mapfile = argv[1];
  char* logfile = argv[2];

  in = fopen(mapfile, "r");
  out = fopen(logfile, "w");

  fprintf(out, "load map from %s\n", mapfile);
  map = loadMap(in);
  fclose(in);

  fprintf(out, "%d rooms, %d exits/room\n",
          map->rooms, map->exitsPerRoom);
  test_nextRoom(out, map);
  test_neighbor(out, map);
  test_validPath(out, map);

  fprintf(out, "free map resources\n");
  freeMap(map);

  fclose(out);
  return 0;
}
