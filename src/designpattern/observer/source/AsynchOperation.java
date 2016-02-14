package designpattern.observer.source;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Cette annotation permet de désigner les méthodes qui, bien qu'effectuant un
 * retour immédiat, réalisent une opération asynchrone en tâche de fond dans un
 * autre thread.
 * 
 * Le but est de prévenir le développeur que l'opération n'est pas terminée lorsque
 * la méthode effectue son retour.
 * 
 * class Toto {
 * 		\@AsynchOperation
 * 		public void doTheJob() {
 * 			...
 * 		}
 * }
 * 
 */
@Documented
@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AsynchOperation {

}
