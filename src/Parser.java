/* Complete all the methods.
EBNF of Mini Language
Program" --> "("Sequence State")".
Sequence --> "("Statements")".
Statements --> Stmt*
Stmt --> "(" {NullStatement | Assignment | Conditional | Loop | Block}")".
State -->  "("Pairs")".
Pairs -->  Pair*.
Pair --> "("Identifier Literal")".
NullStatement --> "skip".
Assignment --> "assign" Identifier Expression.
Conditional --> "conditional" Expression Stmt Stmt.
Loop --> "loop" Expression Stmt.
Block --> "block" Statements.
Expression --> Identifier | Literal | "("Operation Expression Expression")".
Operation --> "+" |"-" | "*" | "/" | "<" | "<=" | ">" | ">=" | "=" | "!=" | "or" | "and".

Note: Treat Identifier and Literal as terminal symbols. Every symbol inside " and " is a terminal symbol. The rest are non terminals.

*/
public class Parser{
    private Token currentToken;
    Scanner scanner;

    private void accept(byte expectedKind) {
        if (currentToken.kind == expectedKind){
            printToken();
            currentToken = scanner.scan();}
        else
            new Error("Syntax error: " + currentToken.spelling + " is not expected."+" Expected: "+expectedKind,
                    currentToken.line);
    }

    private void acceptIt() {
        currentToken = scanner.scan();
        //call to function to print accepted
        printToken();
    }

    //printing current token
    private void printToken(){
       //ie. Line: 1, spelling = [(], kind = 9
        //printing the accepted line like the example in test.txt
        System.out.println("Line: "+currentToken.line+", spelling = ["+currentToken.spelling+"], kind = "+currentToken.kind);
    }


    public void parse() {
        SourceFile sourceFile = new SourceFile();
        scanner = new Scanner(sourceFile.openFile());
        currentToken = scanner.scan();
        parseProgram();
        if (currentToken.kind != Token.EOT)
            new Error("Syntax error: Redundant characters at the end of program.",
                    currentToken.line);
    }

    //Program" --> "("Sequence State")".
    private void parseProgram() {

        //accepting a left parenthesis
        accept(Token.LPAREN);

        //calling parseSequence
        parseSequence();

        //calling parseState
        parseState();

        //accepting right parenthesis
        accept(Token.RPAREN);
          }

    //Sequence --> "("Statements")".
    private void parseSequence(){

        //accepting left 
        accept(Token.LPAREN);

        //calling parseStatements
        parseStatements();

        //accepting right parenthesis
        accept(Token.RPAREN);
            }

    //Statements --> Stmt*
    private void parseStatements(){

        //loop to call parseStmt until a right parenthesis is shown
        while(currentToken.kind != Token.RPAREN) {

            //calling parseStmt
            parseStmt();
        }
    }

    //Stmt --> "(" {NullStatement | Assignment | Conditional | Loop | Block}")".
    private void parseStmt(){

        //accepting left parenthesis
        accept(Token.LPAREN);

        //switch statement to accept one of the options in the parenthesis
        switch (currentToken.kind) {

            //accepting SKIP token
            case (Token.SKIP):

                //calling parseNullStatement
                parseNullStatement();

                //jumping to end of the switch statement
                break;

            //accepting Assign token
            case(Token.ASSIGN):

                //calling parseAssignment
                parseAssignment();

                //jumping to end of the switch statement
                break;

            //accepting conditional token
            case(Token.CONDITIONAL):

                //calling parseConditional
                parseConditional();

                //jumping to end of the switch statement
                break;

            //accepting Loop token
            case(Token.LOOP):

                //calling parseLoop
                parseLoop();

                //jumping to end of the switch statement
                break;

            //accepting Block token
            case(Token.BLOCK):

                //calling parseBlock
                parseBlock();

                //jumping to end of the switch statement
                break;
        }

        //accepting Right Parenthesis
        accept(Token.RPAREN);
    }

    //State -->  "("Pairs")".
    private void parseState(){

        //accepting Left parenthesis
        accept(Token.LPAREN);

        //calling parsePairs
        parsePairs();

        //accepting Right parenthesis
        accept(Token.RPAREN);
    }

    //Pairs --> Pair*.
    private void parsePairs(){
        //loop to keep calling parsePair until a right parenthesis is seen
        while(currentToken.kind != Token.RPAREN) {
            //calling parsePair
            parsePair();
        }
    }

    //Pair --> "("Identifier Literal")".
    private void parsePair(){

        //accepting Left parenthesis
        accept(Token.LPAREN);

        //if statement checking that the token is an Identifier
        if(currentToken.kind==Token.IDENTIFIER){

            //accepting it
            acceptIt();
        }

        //if statement checking that the token is an Literal
        if (currentToken.kind==Token.LITERAL){

            //accepting it
            acceptIt();
        }

        //accepting Right parenthesis
        accept(Token.RPAREN);
    }

    //NullStatement --> skip.
    private void parseNullStatement(){

        //accept Skip token
        accept(Token.SKIP);

    }

    //Assignment --> "assign" Identifier Expression.
    private void parseAssignment(){
        //accepting Assign Token
        accept(Token.ASSIGN);

        //if statement making sure its a Identifier token
        if (currentToken.kind==Token.IDENTIFIER){

            //accepting it
            acceptIt();
        }

        //calling parseExpression
        parseExpression();

    }

    //Conditional --> "conditional" Expression Stmt Stmt.
    private void parseConditional(){

        //accepting a Conditional token
        accept(Token.CONDITIONAL);

        //calling parseExpression
        parseExpression();

        //calling parseStmt
        parseStmt();

        //calling parseStmt
        parseStmt();
    }

    //Loop --> "loop" Expression Stmt.
    private void parseLoop(){

        //accepting a Loop token
        accept(Token.LOOP);

        //calling parseExpression
        parseExpression();

        //calling parseStmt
        parseStmt();
    }

    //Block --> "block" Statements.
    private void parseBlock(){

        //accepting a Block token
        accept(Token.BLOCK);

        //calling parseStatements
        parseStatements();
    }

    //Expression --> Identifier | Literal | "("Operation Expression Expression")".
    private void parseExpression(){

        //if else logic to accept an identifier or literal or ("Operation Expressing Expression")
        if(currentToken.kind==Token.IDENTIFIER||currentToken.kind==Token.LITERAL){

            //accepting if Identifier or Literal token
            acceptIt();
        }
        else{

            //accepting Left parenthesis
            accept(Token.LPAREN);

            //calling parseOperation
            parseOperation();

            //calling parseExpression
            parseExpression();

            //calling parseExpression
            parseExpression();

            //accepting Right parenthesis
            accept(Token.RPAREN);}
    }

    //Operation --> "+" |"-" | "*" | "/" | "<" | "<=" | ">" | ">=" | "=" | "!=" | "or" | "and".
    private void parseOperation(){
        if(currentToken.kind==Token.OPERATOR||currentToken.kind==Token.OR||currentToken.kind==Token.AND){
            acceptIt();
        }
    }
}

