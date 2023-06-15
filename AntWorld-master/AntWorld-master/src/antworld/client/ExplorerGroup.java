package antworld.client;

import antworld.common.*;
import sun.awt.image.ImageWatched;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Anish on 4/29/2018.
 */
public class ExplorerGroup extends AntGroup{

  PathNode goal;
  PathFinder pathFinder;
  int x;
  int y;
  ArrayList<PathNode> path = new ArrayList<PathNode>();
  ArrayList<PathNode> emptyPath = new ArrayList<PathNode>();
  int[][] relativePositions;
  int currentPathSpot = 0;
  BufferedImage map = Util.loadImage("AntWorld.png", null);;
  Map<Integer,Direction> directionMap = new HashMap<>();

  public ExplorerGroup(TeamNameEnum myTeam, PathFinder pathFinder)
  {
    count = 3;
    for (int i = 0; i < count; i++)
    {
      AntData tempData = new AntData(AntType.EXPLORER,myTeam);
      this.antlist.add(tempData);
      //Setting up relative positions

    }
    setUpPositions();
    this.pathFinder = pathFinder;
  }

  void setUpPositions()
  {
    relativePositions = new int[count][2]; //Index 1 is ant index, index 2 is x (0) and y (1)
    for(int i = 0; i < count; i++)
    {
      relativePositions[i][0] = i;
      relativePositions[i][1] = 0;
    }
  }

  /*public void chooseAction()
    {
      if(path != null)
      {
        if(currentPathSpot < path.size() - 1)
        {
          currentPathSpot++;
          x = path.get(currentPathSpot).getX();
          y = path.get(currentPathSpot).getY();
        }
        else
        {
          currentPathSpot = 0;
          path = emptyPath;
        }
      }
      for (int antIndex = 0; antIndex < antlist.size(); antIndex++)
      {
        AntData ant = antlist.get(antIndex);
        PathNode antNode = new PathNode(ant.gridX, ant.gridY);
        PathNode goal = new PathNode(x + relativePositions[antIndex][0], y + relativePositions[antIndex][1]);
        if(!antNode.equals(goal))
        {
          ArrayList<PathNode> antPath = pathFinder.generatePath(antNode, goal);
          dir = antNode.getDirectionTo(antPath.get(1));
          ant.action.type = AntAction.AntActionType.MOVE;
          ant.action.direction = dir;
        }
        else
        {
          ant.action.type = AntAction.AntActionType.NOOP;
        }
      }
    }*/

  public void chooseAction()
  {
    for (int antIndex = 0; antIndex < antlist.size(); antIndex++)
    {
      explore(antlist.get(antIndex));
    }
  }

  public void explore(AntData ant)
  {
    int mapColor = getMapColor(ant);
    Direction dir;

    if(mapColor == LandType.WATER.getMapColor())
    {
      dir = setDirection(ant);
      directionMap.replace(ant.id,directionMap.get(ant.id),dir);
    }
    ant.action.type = AntAction.AntActionType.MOVE;
    ant.action.direction = directionMap.get(ant.id);

  }

  public Direction setDirection(AntData ant)
  {

    return Direction.getRandomDir();
  }

  public int getMapColor(AntData ant)
  {
    int tempx = ant.gridX;
    int tempy = ant.gridY;
    int color = 0;
    if(directionMap.get(ant.id) == Direction.EAST) tempx ++;
    else if(directionMap.get(ant.id) == Direction.WEST) tempx --;
    else if(directionMap.get(ant.id) == Direction.NORTH) tempy --;
    else if(directionMap.get(ant.id) == Direction.SOUTH) tempy ++;
    else if(directionMap.get(ant.id) == Direction.NORTHEAST)
    {
      tempx ++;
      tempy --;
    }
    else if(directionMap.get(ant.id) == Direction.SOUTHEAST)
    {
      tempx ++;
      tempy ++;
    }
    else if(directionMap.get(ant.id) == Direction.NORTHWEST)
    {
      tempx --;
      tempy --;
    }
    else if(directionMap.get(ant.id) == Direction.SOUTHWEST)
    {
      tempx --;
      tempy ++;
    }
    return (map.getRGB(tempx, tempy) & 0x00FFFFFF);

  }

  //needs work
  //spawns in a line for now
  void spawn(int x, int y)
  {
    this.x = x;
    this.y = y;
    boolean temp = true;
    for (AntData ant : antlist)
    {
      ant.action.type = AntAction.AntActionType.EXIT_NEST;
      ant.action.x = x;
      ant.action.y = y;
      directionMap.put(ant.id,Direction.getRandomDir());
      if (temp) x++;
      else y++;
      temp = !temp;
    }
  }

  /**
   * Sets the goal of the group
   * @param goal the location of the goal
   */
  public void setGoal(PathNode goal)
  {
    this.goal = goal;
  }

  /**
   * Tells the group to find a path
   */
  public void findPath()
  {
    PathNode start = new PathNode(x,y);
    path = pathFinder.generatePath(start, goal);
  }

}
