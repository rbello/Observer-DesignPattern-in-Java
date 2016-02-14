package designpattern.observer.source;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Cette annotation permet de d�signer les m�thodes qui, bien qu'effectuant un
 * retour imm�diat, r�alisent une op�ration asynchrone en t�che de fond dans un
 * autre thread.
 * 
 * Le but est de pr�venir le d�veloppeur que l'op�ration n'est pas termin�e lorsque
 * la m�thode effectue son retour.
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
