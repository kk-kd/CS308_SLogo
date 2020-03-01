package slogo.controller.operations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import slogo.controller.CommandsMapHelper;
import slogo.controller.UserDefinedFields;
import slogo.controller.listings.BasicSyntax;
import slogo.exceptions.InvalidArgumentException;
import slogo.model.Turtle;

public class UserOperations {

  private Map<String, Double> userVars;
  private Map<String, List<String>> functions;
  private CommandsMapHelper commandsMapHelper = new CommandsMapHelper();

  public static final String LOOP_EXPR = ":repcount";

  public UserOperations(Turtle turtle, UserDefinedFields userDefinedFields) {
  }

  public Double makeVariable(String variable, Double expr) throws InvalidArgumentException {
    if (!commandsMapHelper.isType(variable, BasicSyntax.VARIABLE)) {
      throw new InvalidArgumentException("The first argument is not in the form of a variable.");
    }
    userVars.put(variable, expr);
    return expr;
  }

  public String repeat(Integer expr, String commands) {
    return loop(1, expr,1, commands, LOOP_EXPR);
  }

  private String loop(Integer start, Integer end, Integer increment, String commands, String variable) {
    StringBuilder sb = new StringBuilder();
    userVars.put(variable, new Double(start));
    String as = commands.substring(2, commands.length() - 1);
    for (; userVars.get(variable) <= end; userVars.put(variable, userVars.get(variable) + increment)) {
      sb.append(as);
    }
    return sb.toString();
  }

   public String doTimes(String vl, String commands) throws InvalidArgumentException {
    String variable = vl.split(" ")[1];
    checkType(variable, BasicSyntax.VARIABLE, 1);
    String l = vl.split(" ")[2];
    checkType(l, BasicSyntax.CONSTANT, 2);
    Integer limit = Integer.parseInt(l);

    return loop(1, limit, 1, commands, variable);
   }

   public String FOR(String loopCondition, String commands) throws InvalidArgumentException {
    String variable = loopCondition.split(" ")[1];
    checkType(variable, BasicSyntax.VARIABLE, 1);
    String start = loopCondition.split(" ")[2];
    checkType(start, BasicSyntax.CONSTANT, 2);
    String end = loopCondition.split(" ")[3];
    checkType(end, BasicSyntax.CONSTANT, 3);
    String increment = loopCondition.split(" ")[4];
    checkType(increment, BasicSyntax.CONSTANT, 4);

    return loop(Integer.parseInt(start), Integer.parseInt(end), Integer.parseInt(increment), commands, variable);
   }

   private void checkType(String s, BasicSyntax type, int num) throws InvalidArgumentException {
     if (!commandsMapHelper.isType(s, type)) {
       throw new InvalidArgumentException("The " + num + " parameter in " + s + " is not a " + type.name() + " .");
     }
   }

  public String IF(Integer expr, String commands) {
    if (expr != 0) {
      return commands.substring(2, commands.length() - 1);
    } else {
      return " ";
    }
  }

  public String ifElse(Integer expr, String trueCommands, String falsecommands) {
    if (expr != 0) {
      return trueCommands.substring(2, trueCommands.length() - 1);
    } else {
      return falsecommands.substring(2, falsecommands.length() - 1);
    }
  }

  public void makeUserInstruction(String commandName, String variables, String commands) {
    List<String> current = new ArrayList<>(Arrays.asList(variables, commands.substring(2, commands.length() - 2)));
    functions.put(commandName, current);
  }
}
