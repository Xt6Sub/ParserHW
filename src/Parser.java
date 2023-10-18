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
        printToken();
    }

    //printing current token
    private void printToken(){
       //ie. Line: 1, spelling = [(], kind = 9
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
        accept(Token.LPAREN);
        parseSequence();
        parseState();
        accept(Token.RPAREN);
          }

    //Sequence --> "("Statements")".
    private void parseSequence(){
        accept(Token.LPAREN);
        parseStatements();
        accept(Token.RPAREN);
            }

    //Statements --> Stmt*
    private void parseStatements(){
        while(currentToken.kind != Token.RPAREN) {
            parseStmt();
        }
    }

    //Stmt --> "(" {NullStatement | Assignment | Conditional | Loop | Block}")".
    private void parseStmt(){
        accept(Token.LPAREN);

        switch (currentToken.kind) {
            case (Token.SKIP):
                parseNullStatement();
                break;

            case(Token.ASSIGN):
                parseAssignment();
                break;

            case(Token.CONDITIONAL):
                parseConditional();
                break;

            case(Token.LOOP):
                parseLoop();
                break;

            case(Token.BLOCK):
                parseBlock();
                break;
        }
        accept(Token.RPAREN);
    }

    //State -->  "("Pairs")".
    private void parseState(){
        accept(Token.LPAREN);
        parsePairs();
        accept(Token.RPAREN);
    }

    //Pairs --> Pair*.
    private void parsePairs(){
        while(currentToken.kind != Token.RPAREN) {
            parsePair();
        }
    }

    //Pair --> "("Identifier Literal")".
    private void parsePair(){
        accept(Token.LPAREN);
        if(currentToken.kind==Token.IDENTIFIER){
            acceptIt();
        }
        if (currentToken.kind==Token.LITERAL){
            acceptIt();
        }
        accept(Token.RPAREN);
    }

    //NullStatement --> skip.
    private void parseNullStatement(){

        accept(Token.SKIP);

    }

    //Assignment --> "assign" Identifier Expression.
    private void parseAssignment(){
        accept(Token.ASSIGN);
        if (currentToken.kind==Token.IDENTIFIER){
            acceptIt();
        }
        parseExpression();

    }

    //Conditional --> "conditional" Expression Stmt Stmt.
    private void parseConditional(){
        accept(Token.CONDITIONAL);
        parseExpression();
        parseStmt();
        parseStmt();
    }

    //Loop --> "loop" Expression Stmt.
    private void parseLoop(){
        accept(Token.LOOP);
        parseExpression();
        parseStmt();
    }

    //Block --> "block" Statements.
    private void parseBlock(){
        accept(Token.BLOCK);
        parseStatements();
    }

    //Expression --> Identifier | Literal | "("Operation Expression Expression")".
    private void parseExpression(){
        if(currentToken.kind==Token.IDENTIFIER||currentToken.kind==Token.LITERAL){
            acceptIt();
        }
        else{
            accept(Token.LPAREN);
            parseOperation();
            parseExpression();
            parseExpression();
            accept(Token.RPAREN);}
    }

    //Operation --> "+" |"-" | "*" | "/" | "<" | "<=" | ">" | ">=" | "=" | "!=" | "or" | "and".
    private void parseOperation(){
        if(currentToken.kind==Token.OPERATOR||currentToken.kind==Token.OR||currentToken.kind==Token.AND){
            acceptIt();
        }
    }
}

