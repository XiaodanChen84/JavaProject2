package logic;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class LogicFactory {

    private static final String PACKAGE = "logic.";
    private static final String SUFFIX = "Logic";

    private LogicFactory() {
    }

    //TODO this code is not complete, it is just here for sake of programe working. need to be changed ocmpletely
    public static <T> T getFor( String entityName ) {
        T newInstance = null;
        try{
            //getFor(type : Class<T> = (Class< T>) Class.forName(PACKAGE + entityName + SUFFIX)) : T
            newInstance = getFor((Class<T>)Class.forName(PACKAGE + entityName + SUFFIX));
                }catch(ClassNotFoundException ex){
                    ex.printStackTrace();
                }
        return newInstance;
       }
    
    public static <T> T getFor(Class<T> type ) {
        T newInstance = null;
        try{
          Constructor<T> declaredConstructor = type.getDeclaredConstructor();
          newInstance =  declaredConstructor.newInstance(); 
        }catch(InstantiationException | IllegalAccessException | IllegalArgumentException |
           InvocationTargetException | NoSuchMethodException | SecurityException ex){   
            Logger.getLogger(LogicFactory.class.getName()).log(Level.SEVERE,null, ex);
          }  
    
         return newInstance;
       }
    } 