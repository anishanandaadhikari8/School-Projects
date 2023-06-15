package antworld.client;

import antworld.common.*;

import java.util.ArrayList;

/**
 * Created by Sushan Sapkota on 4/29/2018.
 */
public class WorkerGroup extends AntGroup{

    boolean assigend;

    public WorkerGroup(TeamNameEnum myTeam, PathFinder pathFinder)
    {
        count = 5;
        for (int i = 0; i < count; i++)
        {
            AntData tempData = new AntData(AntType.WORKER,myTeam);
            this.antlist.add(tempData);
            //Setting up relative positions

        }
        setUpPositions();
        this.pathFinder = pathFinder;
    }

    void setUpPositions(){
        relativePositions = new int[count][2];
        for(int i = 0; i < count; i++)
        {
            relativePositions[i][0] = i;
            relativePositions[i][1] = 0;
        }
    }

    public void chooseAction()
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
    }

    void spawn(int x, int y)
    {
        this.x = x;
        this.y = y;
        boolean temp = true;
        for (AntData ant : antlist)
        {
            ant.action.type = AntAction.AntActionType.EXIT_NEST;
            ant.action.x = x+relativePositions[antlist.indexOf(ant)][0];
            ant.action.y = y+relativePositions[antlist.indexOf(ant)][1];
        }
    }


}
