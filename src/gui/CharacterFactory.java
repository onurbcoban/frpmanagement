package gui;
//Factory pattern: karakter tipine göre uygun nesne üretir
public class CharacterFactory {
 public static Character createCharacter(String type, String name) {
     switch(type.toLowerCase()) {
         case "warrior":
             return new Warrior(name);
         case "wizard":
        	 return new Wizard(name);
         case "rogue":
        	 return new Rogue(name);
         default:
             throw new IllegalArgumentException("Unknown type: " + type);
     }
 }
}



