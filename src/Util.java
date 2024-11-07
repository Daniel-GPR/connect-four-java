package src;

public class Util {
    
interface ConstraintsFunctions {  
  public interface stringConstraint {
    public boolean constraint(String input);  
  }

  public interface intConstraint {
    public boolean constraint(int input);  
  }
}  

  public static String readLine( String prompt ){
    System.out.print(prompt + ": ");
    String input = System.console().readLine();

    return input;
  }


  public static String readLine( String prompt, String errorPrompt, ConstraintsFunctions.stringConstraint constraintFunc ){
    
    String currentPrompt = prompt;
    while(true){
      String input = readLine(currentPrompt);
      if(constraintFunc.constraint(input)){
        return input;
      }
      currentPrompt = !errorPrompt.isEmpty() ? errorPrompt : currentPrompt;
    }
  }

  public static String readLine( String prompt, ConstraintsFunctions.stringConstraint constraintFunc ){
    return readLine(prompt, "", constraintFunc);
  }

  public static int readInt( String prompt ){
    return stringToInt(readLine(prompt));
  }


  public static int readInt( String prompt, String errorPrompt, ConstraintsFunctions.intConstraint constraintFunc ){

    String currentPrompt = prompt;
    while(true){
      int input = stringToInt(readLine(currentPrompt));
      if(constraintFunc.constraint(input)){
        return input;
      }
      currentPrompt = !errorPrompt.isEmpty() ? errorPrompt : currentPrompt;
    }
  }

  public static int readInt( String prompt, ConstraintsFunctions.intConstraint constraintFunc ){
    return readInt(prompt, "", constraintFunc);
  }

  public static Chip readChip( String prompt, String errorPrompt ){
    
    String currentPrompt = prompt;

    while(true){
      Chip input; 
      try{
        input = Chip.valueOf(readLine(currentPrompt).toUpperCase());
        return input;
      }catch(Exception error){
        currentPrompt = errorPrompt;
        continue;
      }
    }
  }

  public static int stringToInt(String number){
    return Integer.parseInt(number);
  }

  public static void println(String message){
    System.out.println(message);
  }

  public static void print(String message){
    System.out.print(message);
  }

}