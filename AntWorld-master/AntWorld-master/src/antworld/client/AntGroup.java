package antworld.client;


import antworld.common.AntType;
import antworld.common.PacketToClient;
import antworld.common.PacketToServer;
import antworld.common.AntAction;
import antworld.common.AntAction.AntState;
import antworld.common.AntData;
import antworld.common.Constants;
import antworld.common.Direction;
import antworld.common.NestNameEnum;
import antworld.common.TeamNameEnum;
import antworld.common.AntAction.AntActionType;

import java.util.ArrayList;


/**
 * Created by Anish on 4/29/2018.
 */

public abstract  class AntGroup
{

  int count;
  ArrayList<AntData> antlist = new ArrayList<>();
  Direction dir;
  int currentPathSpot = 0;

  int x;
  int y;
  ArrayList<PathNode> path = new ArrayList<PathNode>();
  ArrayList<PathNode> emptyPath = new ArrayList<PathNode>();

  int[][] relativePositions;

  PathNode goal;
  PathFinder pathFinder;

  //generate the number of ants needed for this group


  public ArrayList<AntData> getAntList (){
    return antlist;
  }

  void addAnt (AntData ant) {
    antlist.add(ant);
  }

  void remove () {
    antlist.clear();
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
   * if not path is found, the just returns the current position
   */
  public void findPath()
  {
    PathNode start = new PathNode(x,y);
    path = pathFinder.generatePath(start, goal);

    if (path==null){
      path.add(start);
    }
  }


  abstract void spawn(int x, int y);
  abstract void chooseAction();


  abstract void setUpPositions();

}





