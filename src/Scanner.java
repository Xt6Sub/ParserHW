/*
1. You do not need a separate token (number) for each operator. All of them should be translated to one token (number) which is OPERATOR. See below
2. In the switch the code for identifier and literal is complete. Do not change it

3. Symbol e means epsilon.

 BNF grammar of Mini Language

 Program" --> "("Sequence State")".
 Sequence --> "("Statements")".
 Statements --> Statements  Stmt | e
 Stmt --> "(" {NullStatement | Assignment | Conditional | Loop | Block}")".
 State -->  "("Pairs")".
 Pairs -->  Pairs Pair | e.
 Pair --> "("Identifier Literal")".
 NullStatement --> "skip".
 Assignment --> "assign" Identifier Expression.
 Conditional --> "conditional" Expression Stmt Stmt.
 Loop --> "loop" Expression Stmt.
 Block --> "block" Statements.
 Expression --> Identifier | Literal | "("Operation Expression Expression")".
 Operation --> "+" |"-" | "*" | "/" | "<" | "<=" | ">" | ">=" | "=" | "!=" | "or" | "and".

 Note: Treat Identifier and Literal as terminal symbols. Every symbol inside " and " is a terminal symbol. The rest are non terminals.

Input file: test.txt
Output:
 Line: 1, spelling = [(], kind = 9
 Line: 1, spelling = [)], kind = 10
 Line: 1, spelling = [sum], kind = 0
 Line: 1, spelling = [a], kind = 0
 Line: 1, spelling = [2], kind = 1
 Line: 1, spelling = [xyz], kind = 0
 Line: 2, spelling = [skip], kind = 6
 Line: 2, spelling = [assign], kind = 2
 Line: 2, spelling = [conditional], kind = 3
 Line: 2, spelling = [loop], kind = 4
 Line: 2, spelling = [block], kind = 5
 Line: 3, spelling = [1234], kind = 1
 Line: 4, spelling = [+], kind = 11
 Line: 4, spelling = [-], kind = 11
 Line: 4, spelling = [*], kind = 11
 Line: 4, spelling = [/], kind = 11
 Line: 4, spelling = [<], kind = 11
 Line: 4, spelling = [<=], kind = 11
 Line: 4, spelling = [>], kind = 11
 Line: 4, spelling = [>=], kind = 11
 Line: 4, spelling = [=], kind = 11
 Line: 4, spelling = [!=], kind = 11
 Line: 4, spelling = [or], kind = 8
 Line: 4, spelling = [and], kind = 7
 Line: 5, spelling = [-], kind = 11
 Line: 5, spelling = [1234], kind = 1
 Line 6: wrong token !

Note: After you get an error message for the symbol = remove this symbol and
run the program. Repeat this until the last wrong token which is: ?

You should get the following error messages:
 Line 6: wrong token !
 Line 6: wrong token ?
*/

import java.io.*;

public class Scanner{
    private char currentChar;
    private StringBuffer currentSpelling;
    private final BufferedReader inFile;
    private static int line = 1;

    public Scanner(BufferedReader inFile){
        this.inFile = inFile;
        try{
            int i = this.inFile.read();
            if(i == -1) //end of file
                currentChar = '\u0000';
            else
                currentChar = (char)i;
        }
        catch(IOException e){
            System.out.println(e);
        }
    }

    private void takeIt(){
        currentSpelling.append(currentChar);
        try{
            int i = inFile.read();
            if(i == -1) //end of file
                currentChar = '\u0000';
            else
                currentChar = (char)i;
        }
        catch(IOException e){
            System.out.println(e);
        }
    }

    private void discard(){
        try{
            int i = inFile.read();
            if(i == -1) //end of file
                currentChar = '\u0000';
            else
                currentChar = (char)i;
        }
        catch(IOException e){
            System.out.println(e);
        }
    }

    private byte scanToken() {

        //local variable to hold the identifier type initialized to NOTHING token
        byte currentToken = 12;

        //check if the current first character is a letter
        if (isLetter(currentChar)) {

            //loop to read the string
            do {
                //append the current char to the string buffer currentSpelling
                takeIt();
            }
            //condition as long as the first character is a letter
            while (isDigit(currentChar) || isLetter(currentChar));

            //return as an identifier to token class to decide if it is a SPELLING
            currentToken = Token.IDENTIFIER;
        }

        //check to see if current first character is a digit
        else if (isDigit(currentChar)) {

            //loop to read the string
            do {
                //append to current character to the string buffer currentSpelling
                takeIt();
            }
            //condition to make sure all characters are digits
            while (isDigit(currentChar));

            //return as a literal
            currentToken = Token.LITERAL;
        }

        //if the first character was not a digit or letter
        //this else statement will happen
        else {

            //switch statement to decide the currentToken
            switch (currentChar) {

                //case for left parenthesis
                case ('('):
                    takeIt();

                    //returning Left Parenthesis Token
                    return Token.LPAREN;

                //case for right parenthesis
                case (')'):
                    takeIt();

                    //returning Right parenthesis Token
                    return Token.RPAREN;

                //case for plus, multiplication, division, and equals
                //skips to end of switch
                case ('+'), ('*'), ('/'), ('='):
                    takeIt();

                    //setting currentToken as an Operator Token value
                    currentToken = Token.OPERATOR;
                    break;

                //case for - symbol
                //need to check if subtraction or a negative number
                //skips to end of switch
                case('-'):
                    takeIt();

                    //checking if next char is a digit
                    if (isDigit(currentChar)){

                        //loop to take all digits
                        while (isDigit(currentChar)){
                            takeIt();
                        }

                        //setting the token to a Literal
                        currentToken=Token.LITERAL;
                    }

                    //if next char is not a digit
                    else{

                        //setting the Token to an Operator
                        currentToken=Token.OPERATOR;
                    }

                    //skipping to end of switch
                    break;

                //case for "<" | "<=" | ">" | ">="
                //skips to end of switch
                case ('>'), ('<'):
                    takeIt();

                    //checking if the <,> is also followed by an equals sign
                    if (currentChar == '=') {
                        takeIt();
                    }

                    //setting currentToken as an Operator Token value
                    currentToken = Token.OPERATOR;
                    break;

                //case for not equals. First checking if exclamation is present
                //skips to end of switch
                case ('!'):
                    takeIt();

                    //checking is equals followed the exclamation mark
                    if (currentChar != '=') {
                        //skipping to end of switch since currentToken is initialized to Nothing
                        break;
                    }
                    //if it is an equals sign
                    else {
                        takeIt();

                        //setting currentToken as an Operator Token value
                        currentToken=Token.OPERATOR;
                    }
                    break;

                //case for end of file
                case ('\u0000'):
                    takeIt();

                    //setting currentToken to EOT
                    currentToken=Token.EOT;
                    break;
            }
        }
            //returning the currentToken value
            return currentToken;

    }

    private void scanSeparator(){
        switch(currentChar){
            case ' ': case '\n': case '\r': case '\t':
                if(currentChar == '\n')
                    line++;
                discard();
        }
    }

    public Token scan(){
        currentSpelling = new StringBuffer();
        while(currentChar == ' ' || currentChar == '\n' || currentChar == '\r')
            scanSeparator();
        byte currentKind = scanToken();
        return new Token(currentKind, currentSpelling.toString(), line);
    }

    private boolean isGraphic(char c){
        return c == '\t' ||(' ' <= c && c <= '~');
    }

    private boolean isDigit(char c){
        return '0' <= c && c <= '9';
    }

    private boolean isLetter(char c){
        return ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z');
    }
}
