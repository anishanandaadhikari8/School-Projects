package antworld.client;

import antworld.common.LandType;
import antworld.common.Util;
import com.sun.org.apache.xpath.internal.SourceTree;
import sun.awt.image.ImageWatched;

import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Sushan Sapkota
 * The PathFinder class is used to generate paths for AntWorld
 */
public class PathFinder
{

  private BufferedImage map;
  private int width;
  private int height;
  //These lists are for the A* algorithm
  private ArrayList<PathNode> closedList;
  private ArrayList<PathNode> openList;
  private ArrayList<PathNode> emptyList;
  private ArrayList<PathNode> adjacencyList;

  public PathFinder(BufferedImage map)
  {
    this.map = map;
    this.width = map.getWidth();
    this.height = map.getHeight();
  }

  public ArrayList<PathNode> generatePath(PathNode start, PathNode goal)
  {




    ArrayList<PathNode> closedList = new ArrayList<PathNode>();
    ArrayList<PathNode> openList = new ArrayList<PathNode>();
    ArrayList<PathNode> emptyList = new ArrayList<PathNode>();
    ArrayList<PathNode> adjacencyList = new ArrayList<PathNode>();
    PathNode currentNode;
    boolean done = false;

    if(!nodeLegal(goal) || !nodeLegal(start))
    {
      return emptyList;
    }

    if ((start == null) || (goal == null))
    {
      return emptyList;
    }


    start.setH(manhattan(start, goal));
    start.setG(0.0);
    start.calcF();

    openList.add(0, start);
    while(!done)
    {
      currentNode = findLowestFValue(openList);
      closedList.add(0, currentNode);
      openList.remove(currentNode);
      adjacencyList = calcAdjacencies(currentNode, closedList);
      for(PathNode currAdj: adjacencyList)
      {
        if(!openList.contains(currAdj))
        {
          currAdj.setParent(currentNode);
          currAdj.setH(manhattan(currAdj, goal));
          currAdj.setG(currentNode.getG() + manhattan(currAdj, currentNode));
          currAdj.calcF();
          openList.add(0, currAdj);
        }
        else if(currAdj.getG() > currentNode.getG() +
                manhattan(currAdj, currentNode))
        {
          currAdj.setParent(currentNode);
          currAdj.setG(currentNode.getG() + manhattan(currAdj, currentNode));
          currAdj.setH(manhattan(currAdj, goal));
          currAdj.calcF();
        }
        if(currAdj.equals(goal))
        {
          return buildPath(start, currAdj);
        }
      }
      if(openList.isEmpty())
      {
        return emptyList;
      }
    }
    return null;
  }

  //Follows the chain of pathnodes backwards to generate the path
  private ArrayList<PathNode> buildPath(PathNode start, PathNode goal)
  {
    ArrayList<PathNode> path = new ArrayList<PathNode>();
    PathNode current = goal;
    boolean done = false;
    while(!done)
    {
      path.add(0, current);
      if(current.equals(start))
      {
        done = true;
        break;
      }
      current = current.getParent();
      if(current.equals(start))
      {
        done = true;
        path.add(0, current);
      }
    }
    return path;
  }

  //Returns legal adjacent nodes to input node
  //@Kirtus L.
  private ArrayList<PathNode> calcAdjacencies(PathNode node, ArrayList<PathNode> closedList)
  {
    ArrayList<PathNode> adjTiles = new ArrayList<PathNode>();
    int x = node.getX();
    int y = node.getY();
    PathNode temp;
    //North
    temp = new PathNode(x, y-1);
    if(nodeLegal(temp) && !closedList.contains(temp))
    {
      adjTiles.add(temp);
    }
    //NorthEast
    temp = new PathNode(x+1, y-1);
    if(nodeLegal(temp) && !closedList.contains(temp))
    {
      adjTiles.add(temp);
    }
    //East
    temp = new PathNode(x+1, y);
    if(nodeLegal(temp) && !closedList.contains(temp))
    {
      adjTiles.add(temp);
    }
    //SouthEast
    temp = new PathNode(x+1, y+1);
    if(nodeLegal(temp) && !closedList.contains(temp))
    {
      adjTiles.add(temp);
    }
    //South
    temp = new PathNode(x, y+1);
    if(nodeLegal(temp) && !closedList.contains(temp))
    {
      adjTiles.add(temp);
    }
    //SouthWest
    temp = new PathNode(x-1, y+1);
    if(nodeLegal(temp) && !closedList.contains(temp))
    {
      adjTiles.add(temp);
    }
    //West
    temp = new PathNode(x-1, y);
    if(nodeLegal(temp) && !closedList.contains(temp))
    {
      adjTiles.add(temp);
    }
    //NorthWest
    temp = new PathNode(x-1, y-1);
    if(nodeLegal(temp) && !closedList.contains(temp))
    {
      adjTiles.add(temp);
    }

    return adjTiles;

  }

  private boolean nodeLegal(PathNode temp)
  {
    int rgb = (map.getRGB(temp.getX(), temp.getY()) & 0x00FFFFFF);

    if(temp.getX() < 0 || temp.getX() >= width)
    {
      return false;
    }
    if(temp.getY() < 0 || temp.getY() >= height)
    {
      return false;
    }
    if(rgb == LandType.WATER.getMapColor())
    {
      return false;
    }
    return true;
  }

  //This is a helper method for pathfinding heuristic usage
  private PathNode findLowestFValue(ArrayList<PathNode> list)
  {
    double lowestF = list.get(0).getF();
    PathNode lowestFPathNode = list.get(0);
    for(PathNode p: list)
    {
      if(p.getF() <  lowestF)
      {
        lowestFPathNode = p;
        lowestF = p.getF();
      }
    }

    return lowestFPathNode;
  }

  //Returns the manhattan distacne between two nodes
  //@Kirtus L
  private double manhattan(PathNode start, PathNode goal)
  {
    return(Util.manhattanDistance(start.getX(), start.getY(), goal.getX(), goal.getY()));
  }

  private boolean outOfBounds(int[] vector)
  {
    if(vector[0] < 0 || vector[0] >= width)
    {
      return true;
    }
    if(vector[1] < 0 || vector[1] >= height)
    {
      return true;
    }
    return false;
  }

}
