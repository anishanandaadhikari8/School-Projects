package antworld.client;

/**
 * Created by Anish on 4/29/2018.
 */


import antworld.common.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * This is a very simple example client that implements the following protocol:
 *   <ol>
 *     <li>The server must already be running (either on a networked or local machine) and
 *     listening on port 5555 for a client socket connection.
 *     The default host for the server is foodgame.cs.unm.edu on port 5555.</li>
 *     <li>The client opens a socket to the server.</li>
 *     <li>The client then sends a PacketToServer with PacketToServer.myTeam
 *     set to the client's team enum.<br>
 *
 *       <ul>
 *         <li>If this is the client's first connection this game: The client may spawn its
 *         initial ants in this first message or may choose to wait for a future turn to
 *         spawn the ants.</li>
 *         <li>If the client is reconnecting, then the client should set myAntList = null.
 *         This will cause the next message from the server to
 *         include a full list of the client's ants (including ants that are underground,
 *         busy, and noop).</li>
 *       </ul>
 *    </li>
 *
 *     <li>
 *       The server will then send a populated PacketToClient message to the client.
 *     </li>
 *     <li>
 *       Each tick of server, the server will send a PacketToClient message to each client.
 *       After receiving the server update, the client should choose an action for each of its
 *       ants and send a PacketToServer message back to the server.
 *     </li>
 *   </ol>
 */

public class ArmyAntClient
{
  private static final boolean DEBUG = true;
  private final TeamNameEnum myTeam;
  private ObjectInputStream inputStream = null;
  private ObjectOutputStream outputStream = null;
  private boolean isConnected = false;
  //Keeps track of name and location of its assigned nest
  private NestNameEnum myNestName = null;
  private int centerX, centerY;
  private Socket clientSocket;
  private static Random random = Constants.random;
  //todo, Start with LOW_FOOD STATUS, add logic to update game status in GameStatus.java
  //and check it every 50??? ticks of game using the gameLoopCounter
  private GameStatus nestStatus = GameStatus.LOW_FOOD;
  private static int gameLoopCounter = 0;

  private HashMap <Integer,AntGroup> assigendAnt= new HashMap<>(); //here integer is the ID number of ant
  private HashMap <Integer,AntGroup> unAssigendAnt= new HashMap<>(); // here integer is the index in the array
  ArrayList<AntGroup> toSpawn = new ArrayList<>(); //stores the group that were just created to exit them next turn
  private ArrayList<AntGroup> groups = new ArrayList<>(); //is the list of all antgroups created


  private ArrayList<WorkerGroup> workerGroups = new ArrayList<>();

  //Map utilities @KirtusL
  private int worldWidth; //width of map
  private int worldHeight; //height of map
  private BufferedImage map; //The map to be used
  private PathFinder pathFinder;

  //FoodData
  ArrayList<FoodData> foodList = new ArrayList<>();

  public ArmyAntClient(String host, TeamNameEnum team, boolean reconnect)
  {
    myTeam = team;
    System.out.println("Starting " + team +" on " + host + " reconnect = " + reconnect);

    //Loading the map
    //@Kirtus L
    map = Util.loadImage("AntWorld.png", null);
    worldWidth = map.getWidth();
    worldHeight = map.getHeight();
    pathFinder = new PathFinder(map);

    isConnected = openConnection(host, reconnect);
    if (!isConnected) System.exit(0);

    mainGameLoop();
    closeAll();
  }

  private boolean openConnection(String host, boolean reconnect)
  {
    try
    {
      clientSocket = new Socket(host, Constants.PORT);
    }
    catch (UnknownHostException e)
    {
      System.err.println("Client Error: Unknown Host " + host);
      e.printStackTrace();
      return false;
    }
    catch (IOException e)
    {
      System.err.println("Client Error: Could not open connection to " + host
              + " on port " + Constants.PORT);
      e.printStackTrace();
      return false;
    }

    try
    {
      outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
      inputStream = new ObjectInputStream(clientSocket.getInputStream());

    }
    catch (IOException e)
    {
      System.err.println("Client Error: Could not open i/o streams");
      e.printStackTrace();
      return false;
    }

    PacketToServer packetOut = new PacketToServer(myTeam);

    if (reconnect) packetOut.myAntList = null;
    else
    {
      //TODO Spawn ants of whatever type you want
      int numAnts = 10;
      /*for (int i=0; i<numAnts; i++)
      {
        AntType type = AntType.WORKER;

        packetOut.myAntList.add(new AntData(type, myTeam)); //default action is BIRTH.
    }*/

      //creates an explorergroup

      for (int i = 0; i < 5; i++){
        addGroup(new ExplorerGroup(myTeam, pathFinder), packetOut);
      }


    }
    send(packetOut);
    return true;

  }

  /**
   *
   * @param group
   * @param packetOut
   *
   * add a  new group and also adds it to the packet to the server's antlist
   */
  void addGroup (AntGroup group, PacketToServer packetOut){
    toSpawn.add(group);
    groups.add(group);

    for (AntData ant: group.getAntList()){
      System.out.println("Adding ant to list at index " + packetOut.myAntList.size());
      unAssigendAnt.put(packetOut.myAntList.size(), group);
      packetOut.myAntList.add(ant);
    }

  }

  public void closeAll()
  {
    System.out.println("Client.closeAll()");
    {
      try
      {
        if (outputStream != null) outputStream.close();
        if (inputStream != null) inputStream.close();
        clientSocket.close();
      }
      catch (IOException e)
      {
        System.err.println("Client Error: Could not close");
        e.printStackTrace();
      }
    }
  }

  /**
   * This method is called ONCE after the socket has been opened.
   * The server assigns a nest to this client with an initial ant population.
   */
  public void setupNest(PacketToClient packetIn)
  {
    myNestName = packetIn.myNest;
    centerX = packetIn.nestData[myNestName.ordinal()].centerX;
    centerY = packetIn.nestData[myNestName.ordinal()].centerY;
    System.out.println("Client: ==== Nest Assigned ===>: " + myNestName);
  }

  /**
   * Called after socket has been created.<br>
   * This simple example client runs in a single thread. <br>
   * The mainGameLoop() has the following structure:<br>
   * <ol>
   *   <li>Start a blocking listen for message from server.</li>
   *   <li>When server message is received, if a nest has not yet been set up,
   *   then setup the nest.</li>
   *   <li> Assign actions to all ants</li>
   *   <li> Send ant actions to server.</li>
   *   <li> Loop back to step 1.</li>
   * </ol>
   * This NOT a "tight loop" because the blocking socket read
   * will not return until the server sends the next message. Thus, this loop
   * uses the server as a timer.
   */
  public void mainGameLoop()
  {
    while (true)
    {
      PacketToClient packetIn = null;
      try
      {
        if (DEBUG) System.out.println("Client: listening to socket....");
        packetIn = (PacketToClient) inputStream.readObject();
        if (DEBUG) System.out.println("Client: received <<<<<<<<<"+inputStream.available()+"<...\n" + packetIn);

        if (packetIn.myNest == null)
        {
          System.err.println("Client***ERROR***: Server returned NULL nest");
          System.exit(0);
        }
      }
      catch (IOException e)
      {
        System.err.println("Client***ERROR***: client read failed");
        e.printStackTrace();
        System.exit(0);

      }
      catch (ClassNotFoundException e)
      {
        System.err.println("ServerToClientConnection***ERROR***: client sent incorrect common format");
        e.printStackTrace();
        System.exit(0);
      }

      if (myNestName == null) setupNest(packetIn);
      if (myNestName != packetIn.myNest)
      {
        System.err.println("Client: !!!!ERROR!!!! " + myNestName);
      }

      if (DEBUG) System.out.println("Client: chooseActions: " + myNestName);
      if(packetIn.errorMsg != null)
      {
        System.out.println(packetIn.errorMsg);
      }
      //packetIn contains, myAntList, enemyAntList, foodList, nestData, tick, tickTime
      //TODO: Here is where we need to branch out
      //Ants in packetIn are only ones server sends us, we need to keep track of all
      //our live ants somewhere.


      PacketToServer packetOut = new PacketToServer(myTeam) ;


      packetOut = chooseActionsOfAllAnts(packetIn);
      // AntType type = AntType.WORKER;

      // packetOut.myAntList.add(new AntData(type, myTeam));
      send(packetOut);
    }
  }

  // updates all ant to its groups
  private void updateAntGroups(ArrayList<AntData> antlist){
    int count = 0;
    for (AntGroup group : groups){
      group.remove();
    }
    for (AntData ant : antlist) {
      if (unAssigendAnt.containsKey(count)) {
        System.out.println("Removing ant to list at index " + count);
        AntGroup temp = unAssigendAnt.get(count);
        assigendAnt.put(ant.id, temp);
        temp.addAnt(ant);
        unAssigendAnt.remove(count);
      }

      else {
        assigendAnt.get(ant.id).addAnt(ant);
        System.out.println("updating ant to list at index " + count + "with Id" + ant.id);
      }

      count++;
    }
  }

  private void updateReceivedData(PacketToClient packetIn, PacketToServer packetOut){

    if (packetIn.foodList != null) {
      for (FoodData food : packetIn.foodList){
        System.out.println("Food List Size --------------FOUND---------------------------------" );
        if (!isContainedInFoodList(food)) foodList.add(food);
        assignWorkerGroup(1,food,packetOut);
      }
    }


  }

  boolean isContainedInFoodList(FoodData food){
    for (FoodData fooddata : foodList){
      if (fooddata.gridX == food.gridX && fooddata.gridY == food.gridY) return true;

    }
    return false;
  }

  /**
   *
   * @param num
   * @param food
   *
   * assign a certain number of Worker Groups to fetch the food
   */
  void assignWorkerGroup (int num, FoodData food, PacketToServer packetOut){
    for (WorkerGroup workerGroup : workerGroups){
      if (!workerGroup.assigend && num>0) {
        workerGroup.setGoal(new PathNode(food.gridX,food.gridY));
        workerGroup.findPath();
        num --;
      }
    }
    while (num >0){
      WorkerGroup group = new WorkerGroup(myTeam,pathFinder);
      addGroup(group, packetOut );
      group.setGoal(new PathNode(food.gridX,food.gridY));
      group.findPath();
      num --;
    }

  }


  private void send(PacketToServer packetOut)
  {
    try
    {
      System.out.println("Client: Sending>>>>>>>: " + packetOut);
      outputStream.writeObject(packetOut);
      outputStream.flush();
      outputStream.reset();
    }

    catch (IOException e)
    {
      System.err.println("Client***ERROR***: client write failed");
      e.printStackTrace();
      System.exit(0);
    }
  }



  private PacketToServer chooseActionsOfAllAnts(PacketToClient packetIn)
  {
    PacketToServer packetOut = new PacketToServer(myTeam);

    if (foodList!= null ) System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX" );

    updateAntGroups(packetIn.myAntList);


    gameLoopCounter += 1;
    if(gameLoopCounter > 50)
    {
      //DSR added getStatus, check what state the nest is in
      //TODO logic to determine what to do depending on game state, evaluated every 50 moves
      //I have this set up so we would have a class to evaluate each game state
      gameLoopCounter = 0;
      nestStatus = GameStatus.getStatus(nestStatus, packetIn);
      if(nestStatus == GameStatus.LOW_POPULATION)
      {
        //evaluate what type of ants we need and how to deploy them
      }
      else if(nestStatus == GameStatus.EMERGENCY_STATUS)
      {
        System.out.println("EMERGENCY STATUS");
        System.out.println(packetIn.toString());
        //special method to determine whats going wrong, output to
        //console
        //packetOut = method-what to do(packetIn, other data we may want to include)
      }
      else if(nestStatus == GameStatus.ATTACKING)
      {

      }
      else if(nestStatus == GameStatus.DEFENDING)
      {

      }
      else if(nestStatus == GameStatus.LOW_FOOD)
      {

      }
      else //LOW_WATER
      {
        //reassign some ants to get water
      }
    }


    int xSpawn = centerX;

    //updates the group first
    for (AntGroup group : groups)
    {

      if(toSpawn.contains(group))
      {
        group.spawn(xSpawn,centerY);
        group.setGoal(new PathNode(centerX + 70, centerY + 70));
        group.findPath();
        toSpawn.remove(group);
      }
      else
      {
        group.chooseAction();
      }
      packetOut.myAntList.addAll(group.getAntList());
      xSpawn++;
    }

    updateReceivedData (packetIn,packetOut);

    /*
    for (AntData ant : packetIn.myAntList)
    {


      AntAction action = chooseAction(packetIn, ant);

      if (action.type != AntAction.AntActionType.NOOP)
      {
        ant.action = action;
        packetOut.myAntList.add(ant);
      }
    }*/
    return packetOut;
  }






  //=============================================================================
  // This method sets the given action to EXIT_NEST if and only if the given
  //   ant is underground.
  // Returns true if an action was set. Otherwise returns false
  //=============================================================================
  //TODO might be nice to pop out in formation, both to start and when we are attacked
  //maybe change input to AntData[] ants
  private boolean exitNest(AntData ant, AntAction action)
  {
    if (ant.state == AntAction.AntState.UNDERGROUND)
    {
      action.type = AntAction.AntActionType.EXIT_NEST;
      action.x = centerX - (Constants.NEST_RADIUS-1) + random.nextInt(2 * (Constants.NEST_RADIUS-1));
      action.y = centerY - (Constants.NEST_RADIUS-1) + random.nextInt(2 * (Constants.NEST_RADIUS-1));
      return true;
    }
    return false;
  }


  private boolean attackAdjacent(AntData ant, AntAction action)
  {
    return false;
  }

  private boolean pickUpFoodAdjacent(AntData ant, AntAction action)
  {
    return false;
  }

  private boolean goHomeIfCarryingOrHurt(AntData ant, AntAction action)
  {
    return false;
  }

  private boolean pickUpWater(AntData ant, AntAction action)
  {
    return false;
  }

  private boolean goToEnemyAnt(AntData ant, AntAction action)
  {
    return false;
  }

  private boolean goToFood(AntData ant, AntAction action)
  {
    return false;
  }

  private boolean goToGoodAnt(AntData ant, AntAction action)
  {
    return false;
  }

  private boolean goExplore(AntData ant, AntAction action)
  {
    Direction dir = Direction.getRandomDir();
    action.type = AntAction.AntActionType.MOVE;
    action.direction = dir;
    return true;
  }


  private AntAction chooseAction(PacketToClient data, AntData ant)
  {
    AntAction action = new AntAction(AntAction.AntActionType.NOOP);

    if (ant.action.type == AntAction.AntActionType.BUSY)
    {
      //TODO: Now that the server has told you this ant is BUSY,
      //   The server will stop including it in updates until its state changes
      //   from BUSY to NOOP. At that point, the ant will have wasted a turn in NOOP
      //   that it could have used to do something. Therefore,
      //   the client should save this ant in some structure (such as a HashSet).
      return action;
    }

    //This is simple example of possible actions in order of what you might consider
    //   precedence.
    if (exitNest(ant, action)) return action;

    /*if (attackAdjacent(ant, action)) return action;

    if (pickUpFoodAdjacent(ant, action)) return action;

    if (goHomeIfCarryingOrHurt(ant, action)) return action;

    if (pickUpWater(ant, action)) return action;

    if (goToEnemyAnt(ant, action)) return action;

    if (goToFood(ant, action)) return action;

    if (goToGoodAnt(ant, action)) return action;*/

    if (goExplore(ant, action)) return action;

    return action;
  }

  private static String usage()
  {
    return "Usage:\n    [-h hostname] [-t teamname] [-r]\n\n"+
            "Each argument group is optional and can be in any order.\n" +
            "-r specifies that the client is reconnecting.";
  }


  /**
   * @param args Array of command-line arguments (See usage()).
   */
  public static void main(String[] args)
  {
    String serverHost = "localhost";
    boolean reconnection = false;
    if (args.length > 0) serverHost = args[args.length -1];

    TeamNameEnum team = TeamNameEnum.Army;
    if (args.length > 1)
    {
      team = TeamNameEnum.getTeamByString(args[0]);
    }
    new ArmyAntClient(serverHost, team, reconnection);
    //TODO: some initial game setup here? don't know nest at this point.
    //birth our ants in exploration groups?
    //?TODO: or we can wait until setupNest(PacketToClient packetIn) is called
    //when we will know the nest. Do the initialization in that method.
  }

}
