# Impl�mentation Basique

Cette impl�mentation tr�s sommaire du design pattern correspond � sa d�finition stricte, tel qu'on la retrouve sur [wikip�dia](https://fr.wikipedia.org/wiki/Observateur_%28patron_de_conception%29).

**Mod�lisation**

![Model](https://raw.githubusercontent.com/rbello/Observer-DesignPattern-in-Java/master/src/designpattern/observer/basic/Model.png)

**Forces**
- Tr�s simple � impl�menter, et � comprendre
- G�n�rique
- L'interface est claire, les objets observables sont clairements affich�s

**Faiblesses**
- Les interfaces ne peuvent �tre impl�ment�es qu'une fois chacune, donc une classe ne peut pas �mettre plusieurs events
- L'utilisation de AObservable impose de modifier la hi�rarchie d'h�ritage
- Pour chaque type d'event on doit cr�er une classe