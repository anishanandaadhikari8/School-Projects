-- Anish Adhikari

--BASIC INTERPRETER
-- IMPLEMENTED READ,DATA,RESTORE,MID$ to run Hangman
-- used Text.ParserCombinators.Parsec , to parse basic code in Haskell using custom data structures
-- then parsed the code into line, threaded it using StateTMonad, ran it first to store DATA and then ran it normally

import Text.ParserCombinators.Parsec
import Text.ParserCombinators.Parsec
import Control.Monad.State
import System.Random


type InterPrtr a = StateT Memory IO a

type Memory = ([Line'],[([Char],Constant)],[([Char],Int)],(Int,Int),Int,([Constant],Int))

data Constant = Integer Float | String [Char] deriving Show

instance Eq Constant where
  (==) (Integer x) (Integer y) = x == y
  (==) (String x) (String y) = (x == y)
  (/=) (String x) (String y) = x /= y

instance Ord Constant where
    (<) (Integer x) (Integer y) = x < y
    (<=) (Integer x) (Integer y) = x <= y
    (>) (Integer x) (Integer y) = x > y
    (>=) (Integer x) (Integer y) = x >= y

data ID = ID Char deriving Show

data Var = Var [Char]
           | Array Var Exp Exp deriving Show

data Value = ExpValue Exp
             | VarValue Var
             | FunctioValue Function
             | ConstantValue Constant deriving Show

data Op = Add | Sub| Mult | Div | Pow | Eq | Gt | Lt | Ge | Le | Neq | And | Or deriving Show

data Exp = NegateE Exp
          | OpE Op Exp Exp
          | ConstantE Constant
          | VarE Var
          | FunctionE Function [Exp]
          | Exp Exp deriving Show

data Function = Abs|Rnd|Int|Log|Len|Mid deriving Show



data Statement = Dim Var
                | End
                | Restore
                | Data [Constant]
                | Read Var
                | OnGoTo Exp [Constant]
                | OnGoSub Exp [Constant]
                | For Var Exp Exp Constant
                | Next Var
                | Input (Maybe Constant) Var
                | Let Var Exp
                | IF Exp Statement
                | GoTo Constant
                | GoSub Constant
                | Return
                | Define
                | Print [Exp] deriving Show


data Line' = NumLine Constant Statement
            | NoNumLine Statement deriving Show


-- used for array, convertys array into simple var, a (1 0) would be a10
simplifyVar :: Var-> InterPrtr Var
simplifyVar (Var v) = return (Var v)
simplifyVar (Array (Var v) e1 e2) = do
  (Integer x) <- eval e1
  (Integer y) <- eval e2
  let newVar = v ++ (show x) ++ (show y)
  return $ Var newVar

-- EVALUATORES FOR EXP and Statement

eval :: Exp -> InterPrtr Constant
eval (ConstantE (Integer x)) = return (Integer x)
eval (ConstantE (String x)) = return (String x)
eval (OpE o e1 e2) = do
  x <- eval e1
  y <- eval e2
  return (evalOP o x y)


eval (VarE v) = do
  var <- simplifyVar v
  mapping <- get
  case (evalVar var mapping) of
    Just x -> return x
    Nothing -> return (String "")

eval (Exp e) = eval e

eval (FunctionE f e) = do
  x <- eval (head e)
  case f of
    Abs -> return $ Integer (abs (getInt x))
    Int -> return $ Integer (fromIntegral$ round (getInt x))
    Log -> return $ Integer (log (getInt x))
    Len -> return $ Integer (fromIntegral.length $ getString x)
    Mid -> do
      (Integer s) <- eval (head.tail $ e)
      (Integer end) <- eval (head.tail.tail $ e)
      return $ String (mid (getString x) (round (s-1)) (round end))
    Rnd -> do
      case ((getInt x) < 0) of
        True -> do
          let (x',_) = random(mkStdGen (round (getInt x)))
          return $ Integer x'
        False -> do
          g <- liftIO $ newStdGen
          let (x',_) = randomR (0,(getInt x)-1) g
          return $ Integer x'


initializeArray mem v x y y' initial = case (y>0) of
  True -> initializeArray (updateMem mem (Var (v++(show x)++(show y))) initial) v x (y-1) y' initial
  False -> case (x>=0) of
    True -> initializeArray (updateMem mem (Var (v++(show x)++(show y))) initial) v (x-1) y' y' initial
    False -> mem

mid a x y = (take y).(drop x) $ a


evalS :: Statement -> InterPrtr ()
evalS (Let v' e) = do
  v <- simplifyVar v'
  x <- eval e
  mem <- get
  let newMem = updateMem mem v x
  put newMem

evalS (Print e) = do
  (x:xs) <- mapM eval e
  case x of
    String "\n" -> liftIO $ putStr $ (convertToString xs [])
    otherwise -> liftIO $ putStrLn $ (convertToString (x:xs) [])

evalS (Input c v') = do
  v@(Var variable) <- simplifyVar v'
  case c of
    Just x -> evalS (Print [(ConstantE x)])
    Nothing -> return ()
  a <- liftIO $ getLine
  case (last variable) of
    '$' -> evalS(Let v (ConstantE (String a)))
    otherwise -> evalS(Let v (ConstantE (Integer (read a))))


evalS (End) = do
  mem <- get
  put (goToLine mem (-10000))


evalS (Dim (Array v@(Var v') e1 e2)) = do
  (Integer x) <- eval e1
  (Integer y) <- eval e2
  mem@(line,mapping,forVal,startLine,lineNum,message) <- get
  case (last v') of
    '$' -> put $ initializeArray mem v' x y y (String "")
    otherwise -> put $ initializeArray mem v' x y y (Integer 0)


evalS (Return) = do
  mem@(_,_,_,(_,r),_,_) <- get
  put (goToReturn mem)

evalS (OnGoTo v' ints) = do
  (Integer x) <- eval v'
  let (Integer lineNum) = ints !! ((round x)-1)
  mem <- get
  put (goToLine mem (round lineNum))

evalS (IF e s) = do
  b <- eval e
  mem <- get
  case b of
    Integer 1 -> evalS s
    Integer 0 -> return ()
    otherwise -> return ()

evalS (GoTo (Integer x)) = do
  mem <- get
  put (goToLine mem (round x))

evalS (GoSub (Integer x)) = do
  liftIO $ print x
  mem <- get
  put (updateReturn mem (round x))

evalS (For v' e1 e2 (Integer s)) = do
  v@(Var x) <- simplifyVar v'
  mem@(line,mapping,forVal,startLine,lineNum,message) <- get
  case (lookup x forVal) of
   Just xVal -> do
     let (Just (Integer old)) = evalVar v mem
     (Integer final) <- eval e2
     let new = s + old
     let newMem@(line,mapping,forVal,startLine,lineNum,message) = updateMem mem v (Integer new)
     case (new > final) of
       False -> put (line,mapping,(updateTupleList forVal x (lineNum-1)),startLine,lineNum,message)
       True -> do
         let newForVal = removeFromTuple forVal x
         put $ (line,mapping,newForVal,startLine,xVal,message)
   Nothing -> do
     newVal <- eval e1
     let newMem@(line,mapping,forVal,startLine,lineNum,message) = updateMem mem v newVal
     put $ (line,mapping,[(x,lineNum-1)]++forVal,startLine,lineNum,message)

evalS (Next v) = do
  (Var i) <- simplifyVar v
  mem@(line,mapping,forVal,startLine,lineNum,message) <- get
  let (Just x) = lookup i forVal
  put $ (line,mapping,(updateTupleList forVal i lineNum),startLine,x,message)

evalS (Data x) = return ()

evalS (Read v')= do
  v <- simplifyVar v'
  mem@(_,_,_,_,_,(dat,pointer))<-get
  let dataForVar = ConstantE $ (dat !! pointer)
  put $ updateDataPointer mem
  evalS (Let v dataForVar)

evalS (Restore) = do
  (line,mapping,forVal,startLine,lineNum,(dat,pointer)) <- get
  put $ (line,mapping,forVal,startLine,lineNum,(dat,0))

evalS (_) = return ()

-- used for data, just to thread data into memory
evalS' :: Statement -> InterPrtr ()
evalS' (Data x) = do
  mem <- get
  put $ addData mem x

evalS' _ = return()




-- Helper Functions
getMaybe (Just x) = x



evalL :: Line' -> InterPrtr ()
evalL (NumLine c s) = do
  mem <- get
  let newMem = updateLine mem
  put newMem
  evalS s

evalL (NoNumLine s) =
  evalS s

removeFromTuple (x:xs) a = if (fst x /= a) then (x : removeFromTuple xs a) else xs
removeFromTuple [] _ = []
updateTupleList (x:xs) a b = if (fst x /= a) then (x : updateTupleList xs a b) else (a,b):xs
updateTupleList [] _ _ = []


addData mem@(line,mapping,forVal,startLine,lineNum,(dat,pointer)) x = (line,mapping,forVal,startLine,lineNum,(dat++x,pointer))
updateDataPointer mem@(line,mapping,forVal,startLine,lineNum,(dat,pointer)) = (line,mapping,forVal,startLine,lineNum,(dat,pointer+1))

convertToString (x:xs) acc= case x of
  (Integer x) -> convertToString xs $ acc ++ (show x)
  (String y) -> convertToString xs (acc++ y)

convertToString [] acc = acc


updateMem :: Memory -> Var -> Constant -> Memory
updateMem mem@(line,mapping,forVal,startLine,lineNum,message)  v@(Var x) val = case (evalVar v mem) of
  Nothing -> (line,[(x,val)]++mapping,forVal,startLine,lineNum,message)
  Just _ -> (line,updateTupleList mapping x val,forVal,startLine,lineNum,message)

updateLine :: Memory ->  Memory
updateLine (line,mapping,forVal,startLine,lineNum,message)  = (line,mapping,forVal,startLine,lineNum+1,message)

goToLine :: Memory -> Int ->  Memory
goToLine (line,mapping,forVal,(s,r),lineNum,message) newLineNum = (line,mapping,forVal,(s,r),div (newLineNum-s) 10,message)

goToLine' :: Memory -> Int ->  Memory
goToLine' (line,mapping,forVal,(s,r),lineNum,message) newLineNum = (line,mapping,forVal,(s,r),newLineNum,message)

currMemory :: Memory
currMemory = ([],[("d",(Integer 9)),("a$",(String "ramu"))],[("d",11)],(0,0),5,([String "a"],0))

updateReturn :: Memory -> Int ->  Memory
updateReturn (line,mapping,forVal,(s,_),lineNum,message)  i = (line,mapping,forVal,(s,lineNum),div (i-s) 10,message)

goToReturn :: Memory ->  Memory
goToReturn (line,mapping,forVal,(s,r),lineNum,message)  = (line,mapping,forVal,(s,r),r,message)

getVarMap (line,mapping,forVal,startLine,lineNum,message) = mapping

getCurrentLine (line,mapping,forVal,startLine,lineNum,message) = lineNum

getInt (Integer x) = x
getString (String x) = x





getLineNum (NumLine (Integer x) s) = round x
getLineNum (_) = 0

evalOP o (Integer x) (Integer y) = case o of
  Add -> Integer (x + y)
  Mult -> Integer (x * y)
  Sub -> Integer (x-y)
  Div -> Integer (x/y)
  Gt -> if (x>y)then (Integer 1) else (Integer 0)
  Lt -> if (x<y)then (Integer 1) else (Integer 0)
  Ge -> if (x>=y)then (Integer 1) else (Integer 0)
  Le -> if (x<=y)then (Integer 1) else (Integer 0)
  Eq -> if (x==y)then (Integer 1) else (Integer 0)
  Neq -> if (x/=y)then (Integer 1) else (Integer 0)
  And -> if (x==1 && y==1) then (Integer 1) else (Integer 0)
  Or -> if (x==1 || y==1) then (Integer 1) else (Integer 0)

evalOP o (String x) (String y) = case o of
  Eq -> if (x==y)then (Integer 1) else (Integer 0)




parseInteger :: Parser Constant
parseInteger = do
  sgn <- option ' ' (char '-')
  x <- many1 digit
  dot <- option ' ' (char '.')
  y <- option "" (many1 digit)
  return (Integer (read  ([sgn]++x++[dot]++y)))



evalInteger (Integer x) = x
evalString (String x) = x

parseString :: Parser Constant
parseString = do
  char '"'
  x <- many (noneOf "\"")
  char '"'
  return $ String x

parseConstant :: Parser Constant
parseConstant = try parseInteger <|> parseString


parseVar :: Parser Var
parseVar = try parseArrayVar' <|> try parseArrayVar <|> try parseVar'

parseVar' :: Parser Var
parseVar' = do
  x <- many1 (alphaNum <|> (char '$'))
  return $ Var x

parseArrayVar :: Parser Var
parseArrayVar = do
  v <- parseVar'
  spaces
  char '('; spaces
  x <- parseExp ; spaces
  y <- option (ConstantE (Integer 1)) parseExp
  spaces
  char ')'
  return $  Array v x y

parseArrayVar' :: Parser Var
parseArrayVar' = do
  v <- parseVar'
  spaces
  char '('; spaces
  x <- try parseOpE <|> try parseExpList <|>  try parseNegate <|>  try parseConstantE <|> try parseFunction  <|> try  parseVarE'
  space
  y <- option (ConstantE (Integer 1)) parseExp
  spaces
  char ')'
  return $  Array v x y


evalID (ID x) = x

evalVar (Var x) (line,mapping,forVal,startLine,lineNum,message)  = lookup x mapping




parseExpList :: Parser Exp
parseExpList = do
  spaces
  char '('
  spaces
  xps <- parseExp
  spaces
  char ')'
  return $ Exp xps

parseConstantE :: Parser Exp
parseConstantE = ConstantE <$> parseConstant

parseVarE :: Parser Exp
parseVarE = VarE <$> parseVar

parseVarE' :: Parser Exp
parseVarE' = VarE <$> parseVar'


parseOp :: Parser Op
parseOp = do
  x <- (try $ string "<=") <|> (try $ string ">=") <|> (try $ string "<>") <|> (try $ string "and") <|> (try $ string "or") <|> try (oneOf "+-*/<>=" >>= (\x -> return [x]))
  case  x of
    "+" -> return Add
    "-" -> return Sub
    "*" -> return Mult
    "/" -> return Div
    ">" -> return Gt
    "<" -> return Lt
    "<=" -> return Le
    ">=" -> return Ge
    "=" -> return Eq
    "<>" -> return Neq
    "and" -> return And
    "or" -> return Or


parseFunction :: Parser Exp
parseFunction = do
  x <- try (string "abs") <|> try (string "log") <|> try (string "rnd") <|> try (string "int")  <|> try (string "len")  <|> try (string "mid$")
  spaces
  char '('; spaces
  es <- many parseExp
  char ')';
  case x of
    "abs" -> return $ FunctionE Abs es
    "log" -> return $ FunctionE Log es
    "rnd" -> return $ FunctionE Rnd es
    "int" -> return $ FunctionE Int es
    "mid$" -> return $ FunctionE Mid es
    "len" -> return $ FunctionE Len es

parseExp :: Parser Exp
parseExp = do
  spaces
  x <-  try parseOpE <|> try parseExpList <|>  try parseNegate <|>  try parseConstantE <|> try parseFunction  <|> try  parseVarE
  spaces
  return x



parseOpE :: Parser Exp
parseOpE = do
  spaces
  l <-  try parseExpList  <|> try parseNegate   <|> try parseConstantE <|> try parseFunction <|> try parseVarE
  spaces
  m <- parseOp
  spaces
  r <-  try parseExpList  <|> try parseNegate <|>  try parseConstantE <|> try parseFunction <|>try parseVarE
  return $ OpE m l r

parseIfStatement :: Parser Statement
parseIfStatement = do
  string "if"
  spaces
  e <- parseExp; spaces
  string "then"; spaces
  action <- option Return  parseStatement
  case action of
    Return -> do
      lineNum <- parseInteger
      return $ IF e (GoTo lineNum)
    otherwise -> return $ IF e action

parseGoSubStatement :: Parser Statement
parseGoSubStatement = do
  string "gosub"
  spaces
  i <- parseInteger
  return $ GoSub i

parseGotoStatement :: Parser Statement
parseGotoStatement = do
  string "goto"
  spaces
  i <- parseInteger
  return $ GoTo i

parseDimStatement :: Parser Statement
parseDimStatement = do
  string "dim"
  spaces
  aV <- parseArrayVar
  return $ Dim aV

parseRestore :: Parser Statement
parseRestore = do
  string "restore"
  return Restore

parseForStatement :: Parser Statement
parseForStatement = do
  string "for"; spaces
  v <- parseVar; spaces
  char '='; spaces
  e1 <- parseExp; spaces
  string "to";spaces
  e2 <- parseExp; spaces
  x <- option "" (string "step");spaces
  case x of
    "step" -> do
      step <- parseInteger
      return $ For v e1 e2 step
    "" -> return $ For v e1 e2 (Integer 1)

parseNext :: Parser Statement
parseNext = do
  string "next" ; spaces
  v <- parseVar
  return $ Next v





parseNegate :: Parser Exp
parseNegate = do
  char '~'
  e <- parseExp
  return $ NegateE e

parseStatement :: Parser Statement
parseStatement = try parseEndStatement <|> try parseInputStatement <|> try parseLetStatement <|> try parsePrintStatement <|> try parseIfStatement
                 <|> try parseGotoStatement <|> try parseGoSubStatement <|> try parseReturnStatement <|> try parseForStatement <|> try parseNext <|> try parseDimStatement
                  <|> try parseOnGoto <|> try parseData <|> try parseRead <|> try parseRestore
parseEndStatement :: Parser Statement
parseEndStatement = do
  string "end"
  return End



parseReturnStatement :: Parser Statement
parseReturnStatement = do
  string "return"
  return Return

parseOnGoto :: Parser Statement
parseOnGoto = do
  string "on"; spaces
  e1 <- parseExp; spaces
  string "goto"; spaces
  ints <- endBy parseInteger spaces
  x <- option "" (string " ")
  return $ OnGoTo e1 ints


parseInputStatement :: Parser Statement
parseInputStatement = do
  string "input"
  spaces
  x <- option Nothing (Just <$>  parseString)
  spaces
  v <- parseVar
  return $ Input x v

parseLetStatement :: Parser Statement
parseLetStatement = do
  string "let"
  spaces
  v <- parseVar
  spaces
  char '='
  spaces
  e <- parseExp
  return $ Let v e

parsePrintStatement :: Parser Statement
parsePrintStatement = do
  string "print"
  newLine <- option ' ' (char '!')
  spaces
  x <- option (ConstantE (String "")) parseTab
  xps <- many parseExp
  case newLine of
    ' ' -> return $ Print (x:xps)
    otherwise -> return $ Print ([ConstantE (String "\n")]++[x]++xps)

parseTab :: Parser Exp
parseTab = do
  string "tab"; spaces
  char '('
  num <- many digit
  let n = read num
  char ')'
  let tab = replicate n ' '
  return $ ConstantE (String tab)

parseData :: Parser Statement
parseData = do
  string "data";spaces
  cs <- endBy parseConstant spaces
  spaces
  return $ Data cs

parseRead :: Parser Statement
parseRead = do
  string "read"; spaces
  v <- parseVar
  return $ Read v

parseDefine :: Parser Statement
parseDefine = do
  char '('; spaces
  string "define";
  x <- many $ noneOf "("
  char '('; spaces
  return Define


parseNumLine :: Parser Line'
parseNumLine = do
  char '(';spaces
  x <- parseInteger ; spaces
  sxs <-  parseStatement ; spaces
  char ')';spaces
  return $ NumLine x sxs

parseNoNumLine :: Parser Line'
parseNoNumLine = do
  s <- parseDefine
  return $ NoNumLine s

parseLine :: Parser Line'
parseLine = try parseNumLine <|> try parseNoNumLine


pmain file =  do
  ab <- parseFromFile parseNoNumLine file
  result <-  parseFromFile (many1 parseLine) file
  let l =  tail (either (const[]) id result)
  newMem@(line,mapping,forVal,startLine,lineNum,message) <- storeData l (l,[],[],(getLineNum (head l),0),0,([],0))
  x <- evalLines newMem
  return l


storeData ((NumLine _ s):xs) mem = do
  newMem <- execStateT  (evalS' s) mem
  case xs of
    [] -> return newMem
    otherwise -> storeData xs newMem


storeData [] mem = do
  return mem




evalLines x@(line,mapping,forVal,startLine,lineNum,message) = do
  case (lineNum<0) of
    True -> print lineNum
    False -> do
      newMem@(line,mapping,forVal,startLine,lineNum,message) <- execStateT (evalL (line !! lineNum)) x
      --print mapping
      --print lineNum
      --print (mapping)
      evalLines newMem
